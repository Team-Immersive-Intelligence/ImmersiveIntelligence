package pl.pabilo8.immersiveintelligence.client.gui.block.emplacement;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoTaskList;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiCategory;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplate;
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
				new DecoTaskList<>(0, 0)
						.withSize(96, 152-32)
						.withShowJobsTab(false)
		);
	}
}
