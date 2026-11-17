package pl.pabilo8.immersiveintelligence.client.gui.block;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiCategory;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplate;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.SlotStyle;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntitySmallChemicalReactor;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerSmallChemicalReactor;

/**
 * @author Avalon (avalon@iiteam.net)
 * @since 17.11.2026
 */

@DecoTemplate(name = "small_chemical_reactor", category = DecoGuiCategory.PRODUCTION_TILE)
public class GuiSmallChemicalReactor extends DecoTileGui<TileEntitySmallChemicalReactor, ContainerSmallChemicalReactor>
{
	public GuiSmallChemicalReactor(EntityPlayer player, TileEntitySmallChemicalReactor tile)
	{
		super(player, tile, IIGUI.SMALL_CHEMICAL_REACTOR);
	}

	@Override
	public void onInit()
	{
		startBackground()
				.withBox(DecoTextures.BG_STEEL, 0, 0, 176, 76)
				.withTitleBar(tile)
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 0, 76, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventoryTitleBar()
				.build();
	}
}
