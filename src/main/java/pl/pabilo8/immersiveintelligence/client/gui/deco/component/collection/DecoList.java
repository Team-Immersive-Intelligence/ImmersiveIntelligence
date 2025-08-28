package pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.util.Tuple;
import org.apache.commons.lang3.tuple.Pair;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoEntryPanel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @ii-approved 0.3.1
 * @since 31.01.2025
 **/
public class DecoList<T> extends DecoScrolledCollection<DecoList<T>, T>
{
	public DecoList(int x, int y)
	{
		super(x, y);

		//Mouse
		withOnPressed((gui, mouseButton, mouseX, mouseY) ->
				getHoveredPanel(mouseX, mouseY).map(pair ->
						pair.getKey().decoMousePressed(ClientUtils.mc(), mouseX-gui.x, mouseY-pair.getValue(), mouseButton)
				).orElse(false));
		withOnReleased((gui, mouseButton, mouseX, mouseY) ->
				getHoveredPanel(mouseX, mouseY).map(pair ->
						{
							pair.getKey().mouseReleased(mouseX-gui.x, mouseY-pair.getValue());
							return true;
						}
				).orElse(false));
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
		if(clicked!=null)
		{
			//Run the "create" action if the special option was clicked
			if(clicked.getFirst()==ON_CREATE_OPTION)
			{
				runCreateAction();
				return Optional.empty();
			}

			//Only panels allow more complex interactions
			if(!(display instanceof DecoEntryPanel))
				return Optional.empty();
			DecoEntryPanel<T> panel = (DecoEntryPanel<T>)display;
			Integer index = clicked.getFirst();
			Integer heightOffset = clicked.getSecond();
			//Apply the list element representation to the panel, so actions can affect it
			panel.applyElement(entries.get(index));
			return Optional.of(Pair.of(panel, heightOffset));
		}
		return Optional.empty();
	}
}
