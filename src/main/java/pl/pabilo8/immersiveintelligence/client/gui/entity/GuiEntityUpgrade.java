package pl.pabilo8.immersiveintelligence.client.gui.entity;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import pl.pabilo8.immersiveintelligence.api.upgrade.IUpgradableDevice;
import pl.pabilo8.immersiveintelligence.api.upgrade.Upgrade;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.UpgradeOperation;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoEntityGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoTreeDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBar;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoItemStackDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoScenarioDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage;
import pl.pabilo8.immersiveintelligence.client.gui.deco.tree.IDecoTreeNode;
import pl.pabilo8.immersiveintelligence.client.gui.deco.tree.upgrade.UpgradeTechTreeWrapper;
import pl.pabilo8.immersiveintelligence.client.gui.deco.tree.upgrade.UpgradeTreeNodeRenderer;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerEntityUpgrade;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBeginMachineUpgrade;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 10.07.2019
 */
@DecoTemplate(name = "upgrade_tile", category = DecoGuiCategory.GENERIC_ENTITY)
public class GuiEntityUpgrade<T extends Entity & IIEInventory & IUpgradableDevice> extends DecoEntityGui<T, ContainerEntityUpgrade<T>>
{
	private final UpgradeTechTree techTree;
	private DecoTreeDisplay<Upgrade> techTreeDisplay;
	private DecoPanel panelInfo;

	@SyncNBT(nullable = true)
	public String lastUpgrade;
	private DecoScenarioDisplay scenario;

	public GuiEntityUpgrade(EntityPlayer player, T tile)
	{
		super(player, tile, IIGUI.UPGRADE_ENTITY);
		this.techTree = tile!=null?UpgradeTechTree.getTreeFor(tile): null;
	}

	@Override
	public void onInit()
	{
		ResLoc style = DecoTextures.BG_STEEL;
		switch(entity.getUpgradableMachineStyle())
		{
			case WOODEN:
				style = DecoTextures.BG_WOODEN;
				break;
			case BRICKS:
				style = DecoTextures.BG_BRICKS;
				break;
			case CONCRETE:
				style = DecoTextures.BG_CONCRETE;
				break;
			case SANDBAGS:
				style = DecoTextures.BG_SANDBAGS;
				break;
			case STEEL:
			default:
				break;
		}

		startBackground()
				.withBox(style, 0, 0, 256, 152+8+8)
				.withTitleBar("desc.immersiveintelligence.upgrade_gui.title")
				.withNextLayer()
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 40, 136+24+8, 176, 92)
				.withFrame(DecoTextures.FRAME_WOODEN_THIN, 4, false, new boolean[]{true, false, false, false})
				.withInventorySlots(SlotStyle.VANILLA, container.inventorySlots)
				.withInventoryTitleBar()
				.build();

		//Upgrade
		addComponents(
				new DecoPanel(4, 4+8)
						.withSize(108, 152)
						.withBackground(DecoTextures.BG_PAPER)
						.withBackgroundMask(DecoTextures.TEMPLATE_SQUARE),
				scenario = new DecoScenarioDisplay(4+2, 4+2+8)
						.withSize(108-4, 96)
						.withBackgroundColor(IIColor.BLACK.withAlpha(32))
						.withScale(0.125f)
						.withRotation(-12.5f, 5)
						.withRotationAnimation(240, 0)
						.withInteractionAllowed(true),
				new DecoButton(118-4, 16-8-4+14-14+8)
						.withSize(69, 14)
						.withBackground(DecoTextures.COMPONENT_TAB_VERTICAL)
						.withText(IIReference.DESCRIPTION_KEY+"upgrade_gui.tech_tree")
						.withOnLMBPressed(() -> {
							panelInfo.visible = panelInfo.enabled = false;
							techTreeDisplay.visible = techTreeDisplay.enabled = true;
							refreshModelPreview(null);
						}),
				new DecoButton(118-4+69, 16-8-4+14-14+8)
						.withSize(69, 14)
						.withBackground(DecoTextures.COMPONENT_TAB_VERTICAL)
						.withText(IIReference.DESCRIPTION_KEY+"upgrade_gui.info")
						.withOnLMBPressed(() -> {
							panelInfo.visible = panelInfo.enabled = true;
							techTreeDisplay.visible = techTreeDisplay.enabled = false;
							if(lastUpgrade!=null&&!lastUpgrade.isEmpty())
								refreshModelPreview(Upgrade.getUpgradeByID(ResLoc.of(lastUpgrade)));
						}),
				panelInfo = new DecoPanel(118-4, 16-8-4+14+8)
						.withSize(146-8, 146-8)
						.withBackground(DecoTextures.BG_STEEL)
						.withBackgroundMask(DecoTextures.TEMPLATE_SQUARE),
				techTreeDisplay = new DecoTreeDisplay<Upgrade>(118-4, 16-8-4+14+8)
						.withTree(new UpgradeTechTreeWrapper(techTree, entity)
						{
							@Override
							public void onNodeClicked(@Nonnull IDecoTreeNode<Upgrade> node)
							{
								panelInfo.visible = panelInfo.enabled = true;
								techTreeDisplay.visible = techTreeDisplay.enabled = false;
								showUpgrade(node.getUserData());
								refreshModelPreview(node.getUserData());
							}
						})
						.withNodeRenderer(new UpgradeTreeNodeRenderer())
						.withSize(146-8, 146-8)
						.withBackground(DecoSprite.atlasSprite(DecoTextures.BG_DARK, 64))
		);

		if(lastUpgrade==null)
		{
			showUpgrade(null);
			refreshModelPreview(null);
		}
		else
		{
			Upgrade current = Upgrade.getUpgradeByID(ResLoc.of(lastUpgrade));
			showUpgrade(current);
			refreshModelPreview(current);
		}
	}

	private void showUpgrade(Upgrade upgrade)
	{
		panelInfo.cleanup();
		if(upgrade!=null)
		{
			//Name and descriptionm
			panelInfo.addLabel(upgrade.getLocalizedName(), 2+20, 2)
					.withSize(panelInfo.width-4-20, 20)
					.withWrapping(true)
					.withAlign(DecoAlignment.CENTER);
			DecoLabel descLabel = panelInfo.addLabel(TextFormatting.ITALIC+I18n.format(String.format("machineupgrade.%s.%s.desc",
									upgrade.getId().getResourceDomain(),
									upgrade.getId().getResourcePath().replace("/", "."))),
							4, 22)
					.withSize(panelInfo.width-8, 20)
					.withWrapping(true)
					.withAlign(DecoAlignment.TOP_LEFT);

			//Icon
			panelInfo.addComponent(new DecoImage(4, 4))
					.withSize(16, 16)
					.withImageLocation(upgrade.getIcon(), true);
			//Required stacks
			List<IngredientStack> requiredStacks = upgrade.getRequiredStacks();
			for(int i = 0; i < requiredStacks.size(); i++)
			{
				panelInfo.addComponent(new DecoItemStackDisplay(2+(i%7*20), 22+2+descLabel.getTotalHeight()+(i/7)))
						.withSize(18, 18)
						.withStack(requiredStacks.get(i));
			}

			//Required energy
			int energyRequired = (int)(upgrade.getProgressRequired()*0.75f);
			panelInfo.addComponent(new DecoBar(4, 22+64+28-16))
					.withTemplate(DecoTemplates.BAR_ELECTRIC_ENERGY_INPUT)
					.withLimits(0, energyRequired, upgrade::getProgressRequired)
					.withSize(panelInfo.width-4-4, 12)
					.withHorizontalMode(true);

			//Install button
			final boolean shouldInstall = !entity.isUpgradeInstalled(upgrade);
			final boolean canInstall = entity.addUpgrade(upgrade, UpgradeOperation.PROBE);
			DecoButton button = panelInfo.addComponent(new DecoButton(4, 22+64+28))
					.withWidth(panelInfo.width-8)
					.withText(IIReference.DESCRIPTION_KEY+(shouldInstall?"upgrade_gui.install": "upgrade_gui.remove"))
					.withOnLMBPressed(() -> {
						IIPacketHandler.sendToServer(new MessageBeginMachineUpgrade(entity, upgrade, this.mc.player, shouldInstall));
						closeGUI();
					});

			if(shouldInstall&&!canInstall)
				button.enabled = false;
		}
	}

	private void refreshModelPreview(Upgrade upgrade)
	{
		ArrayList<AMTModel> builder = new ArrayList<>();

		//Add base model
		ResLoc baseRes = techTree.getModelLocation();
		if(baseRes!=null)
			builder.add(new AMTModel(DefaultVertexFormats.ITEM, baseRes));

		//Collect all installed upgrades
		ArrayList<Upgrade> upgrades = new ArrayList<>(entity.getAllInstalledUpgrades());
		//Remove incompatible from preview and add requirements
		if(upgrade!=null)
		{
			upgrades.removeAll(techTree.getAllIncompatibleUpgrades(upgrade));
			upgrades.addAll(techTree.getAllRequiredUpgrades(upgrade.getPurpose()));
			upgrades.add(upgrade);
		}

		//Add all installed upgrades
		upgrades.stream().distinct()
				.map(techTree::getUpgradeModelLocation)
				.filter(Objects::nonNull)
				.map(upgradeRes -> new AMTModel(DefaultVertexFormats.ITEM, upgradeRes))
				.forEach(builder::add);

		//Build
		AMTModel built = new AMTModel(builder.toArray(new AMTModel[0]));
		Vec3d center = built.findActualModelCenter();
		Vec3d size = built.findModelSize();
		float maxEdge = (float)Math.max(size.x, Math.max(size.y, size.z));

		scenario.withModel(false, built);
		scenario.withOrigin(center.x, center.y, center.z);
		scenario.withTranslation(-center.x, -center.y, -center.z);
		scenario.withScale(Math.min(maxEdge==0?0.125f: (0.125f/(maxEdge/6f)), 0.325f));
	}
}
