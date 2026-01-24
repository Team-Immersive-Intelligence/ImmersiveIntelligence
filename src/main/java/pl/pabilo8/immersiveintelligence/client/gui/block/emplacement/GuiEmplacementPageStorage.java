package pl.pabilo8.immersiveintelligence.client.gui.block.emplacement;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBar;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBarGroup;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Emplacement;
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
		DecoPanel panelPlatform, panelBase;
		addComponents(
				panelPlatform = new DecoPanel(4, 8+8)
						.withSize(152+96-8, 76-8)
						.withBackground(DecoTextures.GUI_BG_STEEL)
						.withBackgroundMask(DecoTextures.RES_TEXTURES_DECO_TEMPLATE_SQUARE)
						.withTitleLabel(IIReference.GUI_LABEL_KEY+"emplacement.platform_inventory", DecoAlignment.TOP_LEFT),

				panelBase = new DecoPanel(4, 76+4+8)
						.withSize(152+96-8, 76-8)
						.withBackground(DecoTextures.GUI_BG_STEEL)
						.withBackgroundMask(DecoTextures.RES_TEXTURES_DECO_TEMPLATE_SQUARE)
						.withTitleLabel(IIReference.GUI_LABEL_KEY+"emplacement.base_inventory", DecoAlignment.TOP_LEFT)
		);

		panelBase.addComponents(
				new DecoBarGroup(panelBase.width-8-24+4, 4+2)
						.withBar(decoBar -> decoBar
								.withTemplate(DecoGuiUtils.BAR_STRUCTURAL_INTEGRITY_BASE)
								.withLimits(0, Emplacement.baseHealth, () -> (int)tile.baseHealth.getHealth())
								.withHeight(panelBase.height-8)
						)
						.withBar(decoBar -> decoBar
								.withTemplate(DecoGuiUtils.BAR_ELECTRIC_ENERGY.apply(tile.energyStorage))
								.withHeight(panelBase.height-8)
						)
		);

		if(tile.currentWeapon!=null)
		{
			panelPlatform.addComponent(
					new DecoBar(panelPlatform.width-8-12+4, 4+2)
							.withTemplate(DecoGuiUtils.BAR_ARMOR_INTEGRITY)
							.withLimits(0, tile.currentWeapon.getMaxHealth(), () -> (int)tile.currentWeapon.getHealth())
							.withHeight(panelPlatform.height-8)
			);
			tile.currentWeapon.initializeGUI(panelPlatform);
		}

	}
}
