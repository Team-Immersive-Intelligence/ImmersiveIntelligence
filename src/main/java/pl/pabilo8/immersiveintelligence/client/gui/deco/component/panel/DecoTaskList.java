package pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoList;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplates;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyCollection;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static pl.pabilo8.immersiveintelligence.common.util.IIReference.GUI_LABEL_KEY;

/**
 * Generic "request/job editor list" component:
 * <ul>
 *     <li>mode tabs (Requests/Jobs)</li>
 *     <li>scrolled list</li>
 *     <li>add/remove/duplicate/clear buttons</li>
 * </ul>
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @implSpec Callers must provide: <ul>
 * <li>entries (full list)</li>
 * <li>predicate telling whether entry is a "job"; non-job entries are shown as requests</li>
 * <li>entry renderer + click callback</li>
 * <li>button actions (usually implemented by the GUI owning the data)</li>
 * </ul>
 * @since 08.02.2026
 */
public class DecoTaskList<T extends INBTSerializable<NBTTagCompound>> extends DecoPanel
{
	private static final int TAB_H = 18;
	private static final int BTN_W = 25, BTN_H = 14, BTN_GAP = 1;
	private static final int LIST_Y_OFF = 8+TAB_H-4;
	private int listWidth, listHeight;

	private ListMode mode = ListMode.JOBS;
	private EasyCollection<T, NBTTagCompound> allEntries;
	private Predicate<T> isJobPredicate = t -> false;
	private DecoList<T> list;

	@Nullable
	private T selected;
	@Nullable
	private Consumer<ListMode> onModeChanged;
	@Nullable
	private Supplier<T> blankTaskSupplier;
	@Nullable
	private Function<T, T> duplicateFunction;
	@Nullable
	private DecoEntryPanelBuilder<T> displayFunction;

	private boolean showJobsTab = true;
	@Nullable
	private Consumer<T> onSelectedChanged;

	public DecoTaskList(int x, int y)
	{
		super(x, y);
		withBackground(null);
		withBackgroundMask(null);
	}

	//--- Setters ---//

	@Override
	public DecoTaskList<T> withSize(int width, int height)
	{
		this.listWidth = width;
		this.listHeight = height;
		//noinspection unchecked
		return (DecoTaskList<T>)super.withSize(listWidth, LIST_Y_OFF+listHeight+4+BTN_H+4);
	}

	public DecoTaskList<T> withEntries(EasyCollection<T, NBTTagCompound> entries)
	{
		this.allEntries = entries;
		refreshListEntries();
		return this;
	}

	public DecoTaskList<T> withIsJobPredicate(Predicate<T> predicate)
	{
		this.isJobPredicate = predicate!=null?predicate: (t -> false);
		refreshListEntries();
		return this;
	}

	public DecoTaskList<T> withDisplayFunction(DecoEntryPanelBuilder<T> builder)
	{
		this.displayFunction = builder;
		if(list!=null)
			list.withDisplayFunction(builder);
		return this;
	}

	public DecoTaskList<T> withModeHandling(@Nullable ListMode initialMode, Consumer<ListMode> onModeChanged)
	{
		this.mode = normalizeMode(initialMode);
		this.onModeChanged = onModeChanged;
		return this;
	}

	/**
	 * Fired whenever selection changes (including becoming null due to filtering/mode change).
	 */
	public DecoTaskList<T> withOnSelectedChanged(@Nullable Consumer<T> onSelectedChanged)
	{
		this.onSelectedChanged = onSelectedChanged;
		return this;
	}

	/**
	 * Supplies a new blank task for the Add button.
	 */
	public DecoTaskList<T> withBlankTaskSupplier(@Nullable Supplier<T> blankTaskSupplier)
	{
		this.blankTaskSupplier = blankTaskSupplier;
		return this;
	}

	/**
	 * For GUIs that only have a single list type but still want standardized list + buttons.
	 */
	public DecoTaskList<T> withShowJobsTab(boolean showJobsTab)
	{
		this.showJobsTab = showJobsTab;
		return this;
	}

	public ListMode getMode()
	{
		return mode;
	}

	public void setMode(ListMode mode)
	{
		this.mode = normalizeMode(mode);
		if(onModeChanged!=null)
			onModeChanged.accept(this.mode);
		refreshListEntries();
	}

	private static ListMode normalizeMode(@Nullable ListMode mode)
	{
		return mode==null||mode==ListMode.TASKS?ListMode.REQUESTS: mode;
	}

	@Nullable
	public T getSelected()
	{
		return selected;
	}

	/**
	 * @return entries visible in the current tab (after applying REQUESTS/JOBS filter).
	 */
	public List<T> getFilteredEntries()
	{
		if(allEntries==null)
			return Collections.emptyList();
		return allEntries.stream()
				.filter(t -> (mode==ListMode.JOBS)==isJobPredicate.test(t))
				.collect(Collectors.toList());
	}

	private void refreshListEntries()
	{
		if(list==null)
			return;

		List<T> filtered = getFilteredEntries();
		list.withEntries(filtered);

		//Drop selection if it moved out of current view
		if(selected!=null&&((mode==ListMode.JOBS)!=isJobPredicate.test(selected)))
			setSelected(null);
	}

	@Override
	protected boolean initialize()
	{
		if(!super.initialize())
			return false;

		//Mode tabs
		DecoButton tabJobs = new DecoButton(0, 4)
				.withSize(listWidth/2, TAB_H)
				.withBackground(DecoTextures.COMPONENT_TAB_VERTICAL)
				.withText(GUI_LABEL_KEY+"task_editor.jobs")
				.withTranslatedTooltip(GUI_LABEL_KEY+"task_editor.jobs.tooltip")
				.withOnLMBPressed(() -> setMode(ListMode.JOBS));

		DecoButton tabRequests = new DecoButton(listWidth/2, 4)
				.withSize(listWidth/2, TAB_H)
				.withBackground(DecoTextures.COMPONENT_TAB_VERTICAL)
				.withText(GUI_LABEL_KEY+"task_editor.requests")
				.withTranslatedTooltip(GUI_LABEL_KEY+"task_editor.requests.tooltip")
				.withOnLMBPressed(() -> setMode(ListMode.REQUESTS));

		addComponent(tabJobs);
		addComponent(tabRequests);

		if(!showJobsTab)
		{
			//stretch requests tab; hide jobs tab
			tabRequests.withSize(listWidth, TAB_H);
			tabJobs.visible = tabJobs.enabled = false;
			this.mode = ListMode.REQUESTS;
		}

		//List
		list = addComponent(new DecoList<T>(0, LIST_Y_OFF)
				.withSize(listWidth, listHeight)
				.withOnEntryClicked(this::setSelected)
		);
		if(displayFunction!=null)
			list.withDisplayFunction(displayFunction);

		//Action buttons
		int btnY = 4+TAB_H+listHeight+1;
		int btnWidth = (width-8)/4;
		int firstX = 3;
		IIColor buttonGray = IIColor.fromHex("efefef");
		addComponents(
				new DecoButton(firstX, btnY)
						.withTemplate(DecoTemplates.ACTION_BUTTON_ADD)
						.withBackground(DecoTextures.COMPONENT_BUTTON)
						.withSize(btnWidth, BTN_H)
						.withBackgroundColor(buttonGray)
						.withOnLMBPressed(this::onAddPressed),
				new DecoButton(firstX+(btnWidth+BTN_GAP), btnY)
						.withTemplate(DecoTemplates.ACTION_BUTTON_REMOVE)
						.withBackground(DecoTextures.COMPONENT_BUTTON)
						.withSize(btnWidth, BTN_H)
						.withBackgroundColor(buttonGray)
						.withOnLMBPressed(this::onRemovePressed),
				new DecoButton(firstX+2*(btnWidth+BTN_GAP), btnY)
						.withTemplate(DecoTemplates.ACTION_BUTTON_DUPLICATE)
						.withBackground(DecoTextures.COMPONENT_BUTTON)
						.withSize(btnWidth, BTN_H)
						.withBackgroundColor(buttonGray)
						.withOnLMBPressed(this::onDuplicatePressed),
				new DecoButton(firstX+3*(btnWidth+BTN_GAP), btnY)
						.withTemplate(DecoTemplates.ACTION_BUTTON_CLEAR)
						.withBackground(DecoTextures.COMPONENT_BUTTON)
						.withSize(btnWidth, BTN_H)
						.withBackgroundColor(buttonGray)
						.withOnLMBPressed(this::onClearPressed)
		);

		refreshListEntries();
		return true;
	}

	//--- Entry Modification ---//

	private void onAddPressed()
	{
		if(blankTaskSupplier==null||allEntries==null)
			return;
		//Construct a new task and select it.
		T created = blankTaskSupplier.get();
		if(created==null)
			return;
		this.allEntries.add(created);
		selected = created;
		refreshListEntries();
	}

	private void onRemovePressed()
	{
		if(selected==null||allEntries==null)
			return;
		//Remove task and set selected to null
		this.allEntries.remove(selected);
		setSelected(null);
		refreshListEntries();
	}

	private void onDuplicatePressed()
	{
		if(selected==null||blankTaskSupplier==null||allEntries==null)
			return;
		//Create a new blank task and copy data from selected into it.
		T created = allEntries.copyEntry(selected);
		//Ensure duplicate is visible in current mode, otherwise switch mode to match it
		setMode(isJobPredicate.test(created)?ListMode.JOBS: ListMode.REQUESTS);

		refreshListEntries();
	}

	private void onClearPressed()
	{
		if(allEntries==null)
			return;
		//Clear only current mode
		allEntries.removeIf(t -> isJobPredicate.test(t)!=(this.mode==ListMode.JOBS));
		selected = null;
		refreshListEntries();
	}

	private void setSelected(@Nullable T selected)
	{
		if(allEntries==null)
			return;
		this.selected = selected;
		if(onSelectedChanged!=null)
			onSelectedChanged.accept(this.selected);
	}

	public enum ListMode implements ISerializableEnum
	{
		REQUESTS,
		JOBS,
		/**
		 * @deprecated Use REQUESTS. Kept only so older GUI-sync data and external references do not hard-fail.
		 */
		@Deprecated
		TASKS
	}
}
