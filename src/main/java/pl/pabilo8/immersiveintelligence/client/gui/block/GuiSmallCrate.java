package pl.pabilo8.immersiveintelligence.client.gui.block;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiCategory;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplate;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.simple.tileentity.TileEntitySmallCrate;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerIICrate;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 17.05.2019
 */
@DecoTemplate(name = "small_crate", category = DecoGuiCategory.GENERIC_TILE)
public class GuiSmallCrate extends DecoGui<TileEntitySmallCrate, ContainerIICrate<TileEntitySmallCrate>>
{
	public GuiSmallCrate(EntityPlayer player, TileEntitySmallCrate tile)
	{
		super(player, tile, IIGUI.SMALL_CRATE);
	}

	@Override
	public void onInit()
	{
		startBackground()
				.withBox(tile.isWooden()?DecoTextures.GUI_BG_WOODEN: DecoTextures.GUI_BG_STEEL, 0, 0, 176, 76)
				.withTitleBar(tile)
				.withBox(DecoTextures.GUI_BG_WOODEN, DecoTextures.RES_TEXTURES_DECO_TEMPLATE_ROUND_WOODEN, 0, 76, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.inventorySlots)
				.withInventoryTitleBar()
				.build();
	}
}
