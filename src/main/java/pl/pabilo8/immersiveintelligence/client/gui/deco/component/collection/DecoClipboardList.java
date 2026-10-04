package pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection;

import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeVector;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.clipboard.ClipboardEntry;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.clipboard.DecoClipboardUtils;
import pl.pabilo8.immersiveintelligence.common.item.tools.ItemIIClipboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Edits typed clipboard entries and their task state.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 04.10.2026
 * @since 21.09.2026
 */
public class DecoClipboardList extends DecoList<ClipboardEntry>
{
	@Nullable
	private ClipboardEntry selected;
	@Nullable
	private Supplier<Vec3d> positionSupplier;
	private final Consumer<ClipboardEntry> onChanged;

	public DecoClipboardList(int x, int y, Consumer<ClipboardEntry> onChanged)
	{
		super(x, y);
		this.onChanged = onChanged;
		withBackground(DecoTextures.BG_PAPER);
		withListBackground(DecoTextures.BG_PAPER);
		withScrollBarBackground(DecoTextures.COMPONENT_SLIDER_PAPER);
		withDisplayFunction(DecoClipboardUtils.createEntryPanel(this::removeEntry, onChanged));
		addCreateAction(DecoTextures.ICON_ACTION_ADD, "ii.gui.clipboard.add",
				() -> DecoClipboardUtils.createEntry(""));
		addCreateAction(DecoTextures.ICON_ACTION_ACCEPT, "ii.gui.clipboard.add_todo", () -> {
			ClipboardEntry entry = DecoClipboardUtils.createEntry("");
			if(entry!=null)
				entry.setTodo(true);
			return entry;
		});
		addCreateAction(DecoTextures.ICON_ACTION_PIN, "ii.gui.clipboard.add_position", () -> {
			Vec3d position = positionSupplier==null?null: positionSupplier.get();
			return position==null?null: DecoClipboardUtils.createEntry(new DataTypeVector(position));
		});
		withOnEntryClicked(entry -> selected = entry);
	}

	/**
	 * Sets the position source. The button reads it when clicked.
	 */
	public DecoClipboardList withPositionSupplier(Supplier<Vec3d> positionSupplier)
	{
		this.positionSupplier = positionSupplier;
		return this;
	}

	@Override
	public boolean addEntry(@Nonnull ClipboardEntry entry)
	{
		if(getEntries().size() >= ItemIIClipboard.MAX_ENTRIES||!super.addEntry(entry))
			return false;
		selected = entry;
		onChanged.accept(entry);
		return true;
	}

	@Override
	public boolean removeEntry(@Nonnull ClipboardEntry entry)
	{
		if(toBeRemoved.contains(entry)||!super.removeEntry(entry))
			return false;
		if(entry==selected)
			selected = null;
		onChanged.accept(entry);
		return true;
	}

	@Override
	public void onGuiEvent(DecoGuiEvent event)
	{
		ClipboardEntry hovered = getHoveredEntry();
		ClipboardEntry current = hovered==null?selected: hovered;
		switch(event)
		{
			case COPY ->
			{
				if(current!=null)
					DecoClipboardUtils.copyEntry(current);
			}
			case CUT ->
			{
				if(current!=null&&DecoClipboardUtils.copyEntry(current))
					removeEntry(current);
			}
			case PASTE ->
			{
				ClipboardEntry pasted = DecoClipboardUtils.pasteEntry();
				if(pasted!=null)
					addEntry(pasted);
			}
			default -> super.onGuiEvent(event);
		}
	}
}
