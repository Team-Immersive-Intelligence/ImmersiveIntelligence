package pl.pabilo8.immersiveintelligence.client.gui.block;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiCategory;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplate;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.SlotStyle;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.simple.tileentity.backpack.TileEntityStorageBackpack;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerBackpackBlock;

/**
 * Displays the inventory of a placed storage backpack.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 18.11.2026
 */
@DecoTemplate(name = "backpack_block", category = DecoGuiCategory.GENERIC_ITEM)
public class GuiBackpackBlock extends DecoTileGui<TileEntityStorageBackpack, ContainerBackpackBlock>
{
	public GuiBackpackBlock(EntityPlayer player, TileEntityStorageBackpack tile)
	{
		super(player, tile, IIGUI.BACKPACK_BLOCK);
	}

	@Override
	public void onInit()
	{
		startBackground()
				.withBox(DecoTextures.BG_STEEL_ROUGH, DecoTextures.TEMPLATE_ROUND, 0, 0, 176, 64)
				.withTitleBar(tile)
				.withInventorySlots(SlotStyle.VANILLA_STEEL, container.backpackSlots)
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 0, 64, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventoryTitleBar()
				.build();
	}
}
