package pl.pabilo8.immersiveintelligence.client.gui.block.emplacement;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoAlignment;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiCategory;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplate;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 16.07.2021
 */
@DecoTemplate(name = "emplacement_storage", category = DecoGuiCategory.TERRITORY_CONTROL_TILE)
public class GuiEmplacementPageStorage extends GuiEmplacement
{
	public GuiEmplacementPageStorage(EntityPlayer player, TileEntityEmplacement tile)
	{
		super(player, tile, IIGUI.EMPLACEMENT_STORAGE);
	}

	@Override
	public void onInit()
	{
		super.onInit();
		addComponents(
				new DecoPanel(4, 8+8)
						.withSize(152+96-8, 76-8)
						.withBackground(DecoTextures.GUI_BG_STEEL)
						.withBackgroundMask(DecoTextures.RES_TEXTURES_DECO_TEMPLATE_SQUARE)
						.withTitleLabel(IIReference.GUI_LABEL_KEY+"emplacement.platform_inventory", DecoAlignment.TOP_LEFT),

				new DecoPanel(4, 76+4+8)
						.withSize(152+96-8, 76-8)
						.withBackground(DecoTextures.GUI_BG_STEEL)
						.withBackgroundMask(DecoTextures.RES_TEXTURES_DECO_TEMPLATE_SQUARE)
						.withTitleLabel(IIReference.GUI_LABEL_KEY+"emplacement.base_inventory", DecoAlignment.TOP_LEFT)
		);
	}
}
