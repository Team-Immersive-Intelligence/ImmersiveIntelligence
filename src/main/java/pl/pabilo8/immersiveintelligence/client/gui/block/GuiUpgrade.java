package pl.pabilo8.immersiveintelligence.client.gui.block;

import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.api.upgrade.IUpgradableDevice;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoTreeDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoScenarioDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.tree.DecoTreeNodeRenderer;
import pl.pabilo8.immersiveintelligence.client.gui.deco.tree.upgrade.UpgradeTechTreeWrapper;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiCategory;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoSprite;
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
				.withBox(style, 0, 0, 256, 152+8)
				.withTitleBar("desc.immersiveintelligence.upgrade_gui.title")
				.withNextLayer()
				.withBox(DecoTextures.GUI_BG_WOODEN, DecoTextures.RES_TEXTURES_DECO_TEMPLATE_ROUND_WOODEN, 40, 136+24, 176, 92)
				.withFrame(DecoTextures.GUI_FRAME_WOODEN_THIN, 4, false, new boolean[]{true, false, false, false})
				.withInventorySlots(SlotStyle.VANILLA, container.inventorySlots)
				.withInventoryTitleBar()
				.build();

		UpgradeTechTree techTree = UpgradeTechTree.getTreeFor(tile);
		//Upgrade
		addComponents(
				new DecoPanel(4, 4)
						.withSize(108, 152)
						.withBackground(DecoTextures.GUI_BG_PAPER)
						.withBackgroundMask(DecoTextures.RES_TEXTURES_DECO_TEMPLATE_SQUARE),
				new DecoScenarioDisplay(4+2, 4+2)
						.withSize(108-4, 96)
						.withBackgroundColor(IIColor.BLACK.withAlpha(32))
						.withScale(0.125f)
						.withRotation(-12.5f, 5)
						.withModel(false, new AMTModel(DefaultVertexFormats.ITEM, IIReference.RES_BLOCK_MODEL.with("multiblock/emplacement/upgrade_preview_base.obj")))
						.withRotationAnimation(240, 0),

				new DecoButton(118-4, 16-8-4+14-14)
						.withSize(69, 14)
						.withBackground(DecoTextures.RES_TEXTURES_DECO_COMPONENT_TAB_VERTICAL)
						.withRawText("Tech Tree"),
				new DecoButton(118-4+69, 16-8-4+14-14)
						.withSize(69, 14)
						.withBackground(DecoTextures.RES_TEXTURES_DECO_COMPONENT_TAB_VERTICAL)
						.withRawText("Information"),
				new DecoTreeDisplay(118-4, 16-8-4+14)
						.withTree(new UpgradeTechTreeWrapper(techTree, tile))
						.withNodeRenderer(new DecoTreeNodeRenderer())
						.withSize(146-8, 146-8)
						.withBackground(DecoSprite.atlasSprite(DecoTextures.GUI_BG_DARK, 64))
		);
		/*addLabel("Available Upgrades", 118, 8)
				.withSize(116, 8)
				.withAlign(DecoAlignment.CENTER);*/
	}
}
