package pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.util.Tuple;
import org.apache.commons.lang3.tuple.Pair;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoEntryPanel;

import java.util.Optional;

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
						pair.getKey().decoMousePressed(ClientUtils.mc(), mouseY-pair.getValue(), mouseX-gui.x, mouseButton)
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
		drawList(x, y, mouseX, mouseY, partialTicks);
	}

	@Override
	public void cleanup()
	{

	}

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