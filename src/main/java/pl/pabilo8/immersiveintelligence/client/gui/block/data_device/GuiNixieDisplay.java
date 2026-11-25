package pl.pabilo8.immersiveintelligence.client.gui.block.data_device;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoDropdownDataLetters;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoDropdown;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoElementDisplays;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiCategory;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplate;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.SlotStyle;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.nixie.TileEntityNixieDisplayBase;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.nixie.TileEntityNixieDisplayBase.DisplayMode;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerSmallDataDevice;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;

/**
 * Configures the display mode and input variables for a Nixie display.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 26.10.2026
 */
@DecoTemplate(name = "nixie_display", category = DecoGuiCategory.DATA_TILE)
public class GuiNixieDisplay extends DecoTileGui<TileEntityNixieDisplayBase, ContainerSmallDataDevice<TileEntityNixieDisplayBase>>
{
	private static final String KEY = "ii.gui.nixie_display.";
	@SyncNBT(events = SyncEvents.TILE_CLIENT_MESSAGE)
	public DisplayMode mode = DisplayMode.NUMBER_TEXT;
	@SyncNBT(events = SyncEvents.TILE_CLIENT_MESSAGE)
	public String variables = "abcd";

	public GuiNixieDisplay(EntityPlayer player, TileEntityNixieDisplayBase tile)
	{
		super(player, tile, IIGUI.NIXIE_DISPLAY);
		if(tile!=null)
		{
			mode = tile.mode;
			variables = tile.variables;
		}
	}

	@Override
	public void onInit()
	{
		startBackground()
				.withBox(DecoTextures.BG_STEEL, 0, 0, 240, 88)
				.withTitleBar(tile)
				.withNextLayer()
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 32, 88, 176, 92)
				.withFrame(DecoTextures.FRAME_WOODEN_THIN, 4, false, new boolean[]{true, false, false, false})
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventoryTitleBar()
				.build();
		addLabel(KEY+"mode", 8, 13);
		addComponent(new DecoDropdown<DisplayMode>(64, 10)
				.withSize(168, 18)
				.withEntries(DisplayMode.values())
				.withSelectedEntry(mode)
				.withDisplayFunction(DecoElementDisplays.getSimpleTextDisplay(value -> I18n.format(KEY+value.getName())))
				.withOnSelectedEntry((oldMode, newMode) -> {
					mode = newMode;
					refreshGUI();
				}));
		int count = mode==DisplayMode.DIGITS?tile.getTubeCount(): 1;
		for(int i = 0; i < count; i++)
		{
			final int index = i;
			addLabel(new DecoLabel(fontRenderer, 8+i*56, 40)
					.withRawText(mode==DisplayMode.DIGITS?I18n.format(KEY+"tube", i+1): I18n.format(KEY+"value")));
			addComponent(new DecoDropdownDataLetters(8+i*56, 54)
					.withSelectedEntry((Character)variables.charAt(i))
					.withOnSelectedEntry((oldLetter, newLetter) -> {
						StringBuilder letters = new StringBuilder(variables);
						letters.setCharAt(index, newLetter);
						variables = letters.toString();
					}));
		}
	}
}
