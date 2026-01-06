package pl.pabilo8.immersiveintelligence.client.gui.block.emplacement;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoList;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoAlignment;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiCategory;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplate;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 16.07.2021
 */
@DecoTemplate(name = "emplacement_fire_missions", category = DecoGuiCategory.TERRITORY_CONTROL_TILE)
public class GuiEmplacementPageFireMissions extends GuiEmplacement
{
	public GuiEmplacementPageFireMissions(EntityPlayer player, TileEntityEmplacement tile)
	{
		super(player, tile, IIGUI.EMPLACEMENT_FIRE_MISSIONS);
	}

	@Override
	public void onInit()
	{
		super.onInit();
		addComponents(
				new DecoList<String>(2, 2+10+8)
						.withSize(96, 152-6-18-14+4),
				new DecoPanel(2, 152-6-18+2+8)
						.withBackground(DecoTextures.GUI_BG_STEEL)
						.withBackgroundMask(DecoTextures.RES_TEXTURES_DECO_TEMPLATE_PAPER)
						.withSize(96, 18),
				new DecoPanel(2+96+1, 2+8)
						.withBackground(DecoTextures.GUI_BG_PAPER)
						.withBackgroundMask(DecoTextures.RES_TEXTURES_DECO_TEMPLATE_PAPER)
						.withSize(244-96-4, 152-6)
		);
		addLabel("Tasks", 2, 2+6)
				.withSize(96, 12)
				.withAlign(DecoAlignment.CENTER);
	}
}
