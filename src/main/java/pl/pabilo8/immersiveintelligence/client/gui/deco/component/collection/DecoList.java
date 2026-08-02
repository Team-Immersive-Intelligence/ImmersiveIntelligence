package pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.tuple.Pair;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoComponent;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoEntryPanel;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @ii-approved 0.3.1
 * @since 31.01.2025
 **/
public class DecoList<T> extends DecoScrolledCollection<DecoList<T>, T>
{
	protected Consumer<T> onEntryClicked = null;
	private T lastHoveredEntry = null;

	public DecoList(int x, int y)
	{
		super(x, y);

		withOnPressed((gui, mouseButton, mouseX, mouseY) -> {
			Tuple<Integer, Integer> clicked = getClickedEntryIndex(gui.x+2, gui.y-scroll+2, mouseX, mouseY);
			if(clicked!=null)
			{
				if(clicked.getFirst()==ON_CREATE_OPTION)
					return runCreateAction();

				lastHoveredEntry = entries.get(clicked.getFirst());
				if(onEntryClicked!=null)
				{
					onEntryClicked.accept(lastHoveredEntry);
					return true;
				}
				return false;
			}

			if(maxScroll > 0&&IIMath.isPointInRectangle(gui.x+gui.width-8, gui.y, gui.x+gui.width, gui.y+gui.height, mouseX, mouseY))
				return true;

			if(onEntryClicked!=null)
				onEntryClicked.accept(lastHoveredEntry = null);
			return false;
		});
		withOnDragged((gui, button, mouseX, mouseY) -> {
			if(maxScroll > 0&&IIMath.isPointInRectangle(gui.x+gui.width-8, gui.y, gui.x+gui.width, gui.y+gui.height, mouseX, mouseY))
			{
				this.scroll = (int)MathHelper.clamp((float)(mouseY-gui.y-7)/(float)(gui.height-14)*(float)maxScroll, 0, maxScroll);
				return true;
			}
			return false;
		});
	}


	public DecoList<T> withOnEntryClicked(Consumer<T> onClicked)
	{
		this.onEntryClicked = onClicked;
		return this;
	}

	@Override
	protected int getAddButtonHeight()
	{
		return 16;
	}

	@Override
	protected boolean shouldAlwaysHaveScrollbar()
	{
		return true;
	}

	@Override
	protected void draw(int mouseX, int mouseY, float partialTicks)
	{
		drawList(x, y, width, mouseX, mouseY, partialTicks);
	}

	@Override
	public void cleanup()
	{
		display.cleanupDisplay();
	}

	@Override
	protected boolean ownsVirtualChild(DecoComponent<?> component)
	{
		return display.ownsComponent(component);
	}

	@Override
	protected DecoMouseCapture decoMousePressedVirtualChild(Minecraft mc, int mouseX, int mouseY, MouseButton button)
	{
		Optional<Pair<DecoEntryPanel<T>, Integer>> hovered = getHoveredPanel(mouseX, mouseY);
		if(!hovered.isPresent())
			return null;

		Pair<DecoEntryPanel<T>, Integer> pair = hovered.get();
		DecoMouseCapture capture = pair.getKey().decoMousePressed(mc, mouseX-x, mouseY-pair.getValue(), button);
		return capture==null?null: capture.translated(-x, -pair.getValue());
	}

	//--- Public Methods ---//

	/**
	 * @return the entries of the collection
	 * @apiNote This returns a copy of the entries list, so modifications to the returned list will not affect the original list.
	 */
	public List<T> getEntries()
	{
		ArrayList<T> result = new ArrayList<>(entries);
		result.removeAll(toBeRemoved);
		return result;
	}

	/**
	 * @return a stream of the entries in the collection
	 */
	public Stream<T> streamEntries()
	{
		return getEntries().stream();
	}

	/**
	 * Checks if the list contains any entry that matches the given predicate
	 *
	 * @param predicate The predicate to match the entries against
	 * @return true if any entry matches the predicate, false otherwise
	 */
	public boolean hasAny(Predicate<T> predicate)
	{
		return getEntries().stream().anyMatch(predicate);
	}

	/**
	 * Matches the first entry against the given predicate
	 *
	 * @param predicate The predicate to match the entries against
	 * @return the first entry that matches the predicate, or null if no entry matches
	 */
	public Optional<T> findFirst(Predicate<T> predicate)
	{
		return getEntries().stream().filter(predicate).findFirst();
	}

	/**
	 * Removes all entries that match the given predicate from the list
	 *
	 * @param predicate The predicate to match the entries against
	 */
	public void removeIf(Predicate<T> predicate)
	{
		getEntries().stream().filter(predicate).forEach(this::removeEntry);
	}

	//--- Internal Methods ---//

	private Optional<Pair<DecoEntryPanel<T>, Integer>> getHoveredPanel(int mouseX, int mouseY)
	{
		Tuple<Integer, Integer> clicked = getClickedEntryIndex(x+2, y-scroll+2, mouseX, mouseY);
		if(clicked!=null&&clicked.getFirst()!=ON_CREATE_OPTION)
		{
			//Only panels allow more complex interactions
			if(!(display instanceof DecoEntryPanel))
				return Optional.empty();
			Integer index = clicked.getFirst();
			Integer heightOffset = clicked.getSecond();
			T entry = entries.get(index);
			DecoEntryPanel<T> panel = ((DecoEntryPanel<T>)display).getElementPanel(entry);
			if(panel==null)
				return Optional.empty();

			this.lastHoveredEntry = entry;
			return Optional.of(Pair.of(panel, heightOffset));
		}
		return Optional.empty();
	}
}
