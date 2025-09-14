package pl.pabilo8.immersiveintelligence.client.gui.block;

import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.api.upgrade.IUpgradableDevice;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoTreeDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoScenarioDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoAlignment;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiCategory;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplate;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerUpgrade;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 10.07.2019
 */
@DecoTemplate(name = "upgrade", category = DecoGuiCategory.PRODUCTION_TILE)
public class GuiUpgrade<T extends TileEntityIEBase & IIEInventory & IUpgradableDevice> extends DecoGui<T, ContainerUpgrade<T>>
{
	public GuiUpgrade(EntityPlayer player, T tile)
	{
		super(player, tile, IIGUI.UPGRADE);
	}

	@Override
	public void onInit()
	{
		ResLoc style;
		switch(tile.getUpgradableMachineTier())
		{
			case WOODEN:
			default:
				style = DecoTextures.GUI_BG_WOODEN;
				break;
			case STEEL:
				style = DecoTextures.GUI_BG_STEEL;
				break;
		}

		startBackground()
				.withBox(style, 0, 0, 240, 136)
				.withTitleBar("desc.immersiveintelligence.upgrade_gui.title")
				.withNextLayer()
				.withBox(DecoTextures.GUI_BG_WOODEN, DecoTextures.RES_TEXTURES_DECO_TEMPLATE_ROUND_WOODEN, 32, 136, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.inventorySlots)
				.withInventoryTitleBar()
				.withNextLayer()
				.withBox(style, DecoTextures.RES_TEXTURES_DECO_TEMPLATE_SQUARE, 4, 112-4, 128-16, 24)
				.build();

		UpgradeTechTree techTree = UpgradeTechTree.getTreeFor(tile);
		//Upgrade
		addComponents(
				new DecoScenarioDisplay(7, 16-4)
						.withSize(106, 96)
						.withBackgroundColor(IIColor.BLACK)
						.withScale(0.125f)
						.withRotation(-12.5f, 5)
						.withModel(false, new AMTModel(DefaultVertexFormats.ITEM, IIReference.RES_BLOCK_MODEL.with("multiblock/emplacement/upgrade_preview_base.obj")))
						.withRotationAnimation(100, 0),
				new DecoTreeDisplay(118, 16)
						.withSize(116, 112)
						.withBackground(DecoTextures.GUI_BG_PAPER)
						.withBackgroundMask(DecoTextures.RES_TEXTURES_DECO_TEMPLATE_PAPER)
		);
		addLabel("Available Upgrades", 118, 8)
				.withSize(116, 8)
				.withAlign(DecoAlignment.CENTER);
	}
}
