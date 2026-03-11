package pl.pabilo8.immersiveintelligence.client.gui.block.emplacement;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoSwitch;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoDropdown;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiCategory;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplate;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 16.07.2021
 */
@DecoTemplate(name = "emplacement_target_filters", category = DecoGuiCategory.TERRITORY_CONTROL_TILE)
public class GuiEmplacementPageTargetFilters extends GuiEmplacement
{
	public GuiEmplacementPageTargetFilters(EntityPlayer player, TileEntityEmplacement tile)
	{
		super(player, tile, IIGUI.EMPLACEMENT_TARGET_FILTERS);
	}

	@Override
	public void onInit()
	{
		super.onInit();
		DecoPanel topBar;
		addComponents(
				topBar = new DecoPanel(2, 2+8)
						.withBackground(DecoTextures.BG_PAPER)
						.withBackgroundMask(DecoTextures.TEMPLATE_PAPER)
						.withSize(152+96-4, 18),

				new DecoPanel(2, 2+19+8)
						.withBackground(DecoTextures.BG_STEEL)
						.withBackgroundMask(DecoTextures.TEMPLATE_PAPER)
						.withSize(122-1, 152-24),
				new DecoPanel(2+122+1, 2+19+8)
						.withBackground(DecoTextures.BG_PAPER)
						.withBackgroundMask(DecoTextures.TEMPLATE_PAPER)
						.withSize(122-1, 152-24)
		);

		//Add target preset selection
		topBar.addComponents(
				new DecoDropdown<String>(2, 2)
						.withSize(88, 14)
						.withDropdownWidth(88)
						.withEntries("Mobs", "Everyone", "Enemy Factions", "Shells", "Custom")
						.withSelectedEntry("Mobs"),
				new DecoButton(2+88, 2)
						.withSize(32, 14)
						.withRawText("Reset")
						.withTranslatedTooltip("Reverts any changes done to this task."),
				new DecoButton(2+88+32, 2)
						.withSize(32, 14)
						.withRawText("Save")
						.withTranslatedTooltip("Saves the current task."),
				new DecoSwitch(2+88+32+32+2, 2+2)
						.withText("Current Task")
						.withTranslatedTooltip("When active, this task becomes the Emplacement weapon's main task.")
		);
	}
}
