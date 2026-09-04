package pl.pabilo8.immersiveintelligence.client.gui.block;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreType;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.FuseType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoPart;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem.IIAmmoProjectile;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.AmmoBallisticsCache;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.AmmoBallisticsCache.CachedBallisticStats;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.IIAmmoUtils;
import pl.pabilo8.immersiveintelligence.api.crafting.BulletComponentStack;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoDropdown;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoElementDisplays.DecoElementSorter;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoInformationList;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoInformationList.InformationEntry;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoEntryPanelBuilder;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBar;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoItemStackDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoScenarioDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage.ImageAnimationDirection;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTBullet;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTBullet.BulletState;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTLocator;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.ammo.components.factory.AmmoComponentFluid;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityProjectileWorkshop;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerProjectileWorkshop;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 04.09.2026
 * @ii-approved 0.3.1
 * @since 10.07.2019
 */
@DecoTemplate(name = "projectile_workshop", category = DecoGuiCategory.PRODUCTION_TILE)
public class GuiProjectileWorkshop extends DecoTileGui<TileEntityProjectileWorkshop, ContainerProjectileWorkshop>
{
	@DecoResource
	public static ResourceLocation PROGRESS_BAR = IIReference.RES_II.with("gui/projectile_workshop");
	@DecoResource
	public static ResourceLocation ARTILLERY_RANGE = IIReference.RES_II.with("gui/ammo_icons/artillery_range");
	@DecoResource
	public static ResourceLocation AVAILABLE_FUZES = IIReference.RES_II.with("gui/ammo_icons/available_fuzes");
	@DecoResource
	public static ResourceLocation COMPONENT_EFFICIENCY = IIReference.RES_II.with("gui/ammo_icons/component_efficiency");
	@DecoResource
	public static ResourceLocation COMPONENT_SHAPE = IIReference.RES_II.with("gui/ammo_icons/component_shape");
	@DecoResource
	public static ResourceLocation COMPONENT_SIZE = IIReference.RES_II.with("gui/ammo_icons/component_size");
	@DecoResource
	public static ResourceLocation COMPONENT_SLOTS = IIReference.RES_II.with("gui/ammo_icons/component_slots");
	@DecoResource
	public static ResourceLocation DAMAGE = IIReference.RES_II.with("gui/ammo_icons/damage");
	@DecoResource
	public static ResourceLocation FLAT_TRAJECTORY_RANGE = IIReference.RES_II.with("gui/ammo_icons/flat_trajectory_range");
	@DecoResource
	public static ResourceLocation MASS = IIReference.RES_II.with("gui/ammo_icons/mass");
	@DecoResource
	public static ResourceLocation PENETRATION_HARDNESS = IIReference.RES_II.with("gui/ammo_icons/penetration_hardness");
	@DecoResource
	public static ResourceLocation PENETRATION_DEPTH = IIReference.RES_II.with("gui/ammo_icons/max_penetration_depth");
	@DecoResource
	public static ResourceLocation VELOCITY = IIReference.RES_II.with("gui/ammo_icons/velocity");

	private MultiblockInteractablePart openedPart;
	boolean hasFillerUpgrade;

	//Non-upgraded
	private DecoPanel fillerInfoPanel;
	private DecoInformationList ammoInfoList;
	private DecoDropdown<CoreType> coreTypeDropdown;
	private DecoEntryPanelBuilder<CoreType> coreTypeDisplay;
	private IAmmoTypeItem<?, ?> ammoType;
	private AmmoCore ammoCore;
	private CoreType coreType;

	private DecoItemStackDisplay exampleStackDisplay;
	private DecoScenarioDisplay scenario;
	private DecoLabel costLabel;

	public GuiProjectileWorkshop(EntityPlayer player, TileEntityProjectileWorkshop tile)
	{
		super(player, tile, IIGUI.PROJECTILE_WORKSHOP);
		if(tile==null)
			return;
		hasFillerUpgrade = tile.isUpgradeInstalled(IIContent.UPGRADE_CORE_FILLER);
		ammoCore = IIContent.ammoCoreIron;
		coreType = tile.coreType;
		ammoType = tile.producedAmmo;
	}

	@Override
	public void onInit()
	{
		syncAnimatedParts(openedPart = Utils.RAND.nextGaussian() > 0.5f?tile.lid1: tile.lid2, true);

		//Add background
		startBackground()
				.withBox(DecoTextures.BG_STEEL_ROUGH, DecoTextures.TEMPLATE_SQUARE, 0, 0, hasFillerUpgrade?176: 304, 130)
				.withTitleBar(tile)
				.withNextLayer()
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, hasFillerUpgrade?0:
						68, 136, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventoryTitleBar()

				.conditionally(!hasFillerUpgrade, builder -> builder
						.withNextLayer()
						.withBox(DecoTextures.BG_STEEL_ROUGH, DecoTextures.TEMPLATE_SQUARE, 0, 0, 144, 130)
						.withInventorySlots(SlotStyle.IE_INPUT, container.inputSlot)

						.withNextLayer()
						.withBox(DecoTextures.BG_PAPER, DecoTextures.TEMPLATE_PAPER, 144, 0, (168), 130)

						.withNextLayer()
						.withBox(DecoTextures.BG_BLUEPRINT, DecoTextures.TEMPLATE_PAPER, 0, 86, 144, 44)
						.withBox(DecoTextures.BG_BLUEPRINT, DecoTextures.TEMPLATE_PAPER, 0, 14, 144, 72)

						.withNextLayer()
						.withBox(DecoTextures.BG_PAPER, DecoTextures.TEMPLATE_TICKET, 44, 18, 96, 72)
				)
				.conditionally(hasFillerUpgrade, builder -> builder
						.withInventorySlots(SlotStyle.IE_INPUT, container.inputSlot)
						.withInventorySlots(SlotStyle.IE_INPUT, container.componentInputSlot)
						.withNextLayer()
						.withBox(DecoTextures.BG_PAPER, DecoTextures.TEMPLATE_PAPER, 4, 60, 164, 72)
						.withTitleBar("ii.gui.projectile_workshop.component_info", DecoAlignment.TOP_LEFT)
				)
				.build();

		//Add foreground
		if(!hasFillerUpgrade)
			addCoreWorkshopComponents();
		else
			addCoreFillerComponents();

		//Restart this GUI when upgrade is installed
		addValueListener(() -> tile.isUpgradeInstalled(IIContent.UPGRADE_CORE_FILLER))
				.addObserver(hasFillerUpgrade -> refreshGUI());
	}

	private void addCoreFillerComponents()
	{
		//Upgraded
		addComponents(
				//Energy bar
				new DecoBar(168, 4)
						.withSize(12, 64)
						.withTemplate(DecoTemplates.BAR_ELECTRIC_ENERGY.apply(tile.energyStorage)),
				//Right-side paper panel
				this.fillerInfoPanel = new DecoPanel(4, 68)
						.withSize(176, 64)
						.withBackground(null),
				//Stored component display
				new DecoItemStackDisplay(container.componentInputSlot.xPos, container.componentInputSlot.yPos)
						.withSize(16, 16)
						.withOnTooltip(gui -> {
							AmmoComponent current = tile.componentInside.getComponent();
							if(current==null||tile.componentInside.amount <= 0)
								return Collections.singleton(I18n.format("gui.immersiveengineering.empty"));
							return Arrays.asList(
									current.getColor().getHexCol(current.getTranslatedName()),
									TextFormatting.GRAY+I18n.format("ii.gui.projectile_workshop.tooltip.units", tile.componentInside.amount)+TextFormatting.RESET,
									TextFormatting.GRAY+
											(current instanceof AmmoComponentFluid?(current.getTranslatedName()+" "+Utils.formatDouble(tile.componentInside.amount/16f*1000, "0")+" mB"):
													(Utils.formatDouble(tile.componentInside.amount/16f, "0.#")+" x "+current.getMaterial().getExampleStack().getDisplayName()))
											+TextFormatting.RESET
							);
						})
						.withBackgroundTexture(DecoSprite.atlasSprite(DecoTextures.SLOT_IE, 32, true))
						.withProgressBar(partialTicks -> tile.componentInside.getAmountPercentage(),
								() -> new IIColor[]{tile.componentInside.getColor()}
						),
				new DecoItemStackDisplay(146, 37)
						.withSize(16, 16)
						.withBackgroundTexture(DecoSprite.atlasSprite(DecoTextures.SLOT_IE, 32, true)),

				//Progress bar background
				new DecoImage(33, 41)
						.withSize(48, 8)
						.withImageLocation(PROGRESS_BAR, true)
						.withUV(64, 16, 11, 64, 19),
				new DecoImage(83, 36)
						.withSize(8, 19)
						.withImageLocation(PROGRESS_BAR, true)
						.withUV(64, 37, 19, 45, 38),
				new DecoImage(93, 40)
						.withSize(48, 10)
						.withImageLocation(PROGRESS_BAR, true)
						.withUV(64, 16, 0, 64, 10),

				//Progress bar
				new DecoImage(35, 42)
						.withSize(44, 6)
						.withImageLocation(PROGRESS_BAR, true)
						.withUV(64, 0, 47, 44, 53)
						.withAnimation(ImageAnimationDirection.LEFT_TO_RIGHT, DecoGuiUtils.getMultiblockProductionSingleProgress(tile, 0f, 0.45f)),
				new DecoImage(83, 36)
						.withSize(8, 19)
						.withImageLocation(PROGRESS_BAR, true)
						.withUV(64, 45, 19, 53, 38)
						.withAnimation(ImageAnimationDirection.LEFT_TO_RIGHT, DecoGuiUtils.getMultiblockProductionSingleProgress(tile, 0.45f, 0.55f)),
				new DecoImage(95, 42)
						.withSize(44, 6)
						.withImageLocation(PROGRESS_BAR, true)
						.withUV(64, 0, 39, 44, 45)
						.withAnimation(ImageAnimationDirection.LEFT_TO_RIGHT, DecoGuiUtils.getMultiblockProductionSingleProgress(tile, 0.55f, 1f))
		);

		addValueListener(() -> tile.componentInside.serializeNBT().toString()+"|"+tile.componentFillAmount)
				.addObserver(hash -> updateFillerInfo());
		updateFillerInfo();
	}

	private void updateFillerInfo()
	{
		BulletComponentStack stack = tile.componentInside;
		this.fillerInfoPanel.cleanup();
		if(stack==null||stack.isEmpty()||stack.component==null)
			return;

		DecoLabel title = fillerInfoPanel.addLabel(TextFormatting.BOLD+stack.component.getTranslatedName(), 4, 0)
				.withSize(160, 18)
				.withAlign(DecoAlignment.TOP)
				.withWrapping(true);
		fillerInfoPanel.addLabel(new DecoLabel(this.fontRenderer, 42, title.getTotalHeight()+1))
				.withText(
						I18n.format("ii.gui.projectile_workshop.component.role", stack.component.getRole().getLocalizedName()),
						I18n.format("ii.gui.projectile_workshop.component.density", stack.component.getDensity()),
						I18n.format("ii.gui.projectile_workshop.component.slots_taken", stack.component.getSlotsTaken())
				)
				.withSize(152, 48)
				.withAlign(DecoAlignment.TOP_LEFT);

		fillerInfoPanel.addComponents(
				new DecoPanel(4, 12)
						.withSize(34, 34)
						.withBackground(null)
						.withFrame(new DecoFrame(DecoTextures.FRAME_PAPER, true, 16)),
				new DecoItemStackDisplay(4, 12)
						.withStack(stack.component.getMaterial())
						.withSize(34, 34)
						.withIconSize(20)
						.withOnTooltip(null)
		);


		if(stack.component.showInManual())
			fillerInfoPanel.addComponent(new DecoButton(2, 46)
					.withSize(164, 16)
					.withText("ii.gui_tooltip.widget.manual.page")

					.withBackground(DecoTextures.COMPONENT_BUTTON)
					.withBackgroundColor(IIColor.fromPackedRGB(0x7A7ABC))
					.withPadding(2, 2, 2, 2)
					.withIconAlignment(DecoAlignment.CENTER)
					//Engineer's Manual
					.withIcon(new ItemStack(IEContent.itemTool, 1, 3))
					.withOnLMBPressed(() -> {
						int index = AmmoRegistry.getAllComponents().stream()
								.filter(AmmoPart::showInManual)
								.filter(c -> !c.getMaterial().getExampleStack().isEmpty())
								.collect(Collectors.toList())
								.indexOf(stack.component);
						openManualWidget("bullet_components", index);
					})
			);
	}

	private void addCoreWorkshopComponents()
	{
		addComponents(
				coreTypeDropdown = new DecoDropdown<CoreType>(0, 112)
						.withSize(144, 20)
						.withDropdownWidth(144)
						.withScrollBarBackground(DecoTextures.COMPONENT_SLIDER_PAPER)
						.withBackground(DecoTextures.COMPONENT_BUTTON_PAPER)
						.withEntries(CoreType.values())
						.withSelectedEntry(coreType)
						.withOnSelectedEntry((oldType, newType) -> onCoreTypeChanged(newType))
						.withDisplayFunction(coreTypeDisplay = new DecoEntryPanelBuilder<CoreType>()
								.withBackground(DecoTextures.BG_PAPER)
								.withBackgroundMask(DecoTextures.TEMPLATE_PAPER)
								//Type Icon, Label, and Letter
								.withComponent("icon", p -> new DecoItemStackDisplay(2, 2)
										.withSize(16, 18)
								)
								.withLabel("label", p ->
										new DecoLabel(fontRenderer, 20, 2)
												.withSize(48, 18)
												.withAlign(DecoAlignment.LEFT)
												.withText("ii.gui.projectile_workshop.entry.core")
								)
								.withElementApplyMethod(this::drawCoreTypeEntry)
								.withElementTooltip(typeMeta -> "ii.gui.projectile_workshop.tooltip.core_type")
						)
						.withTranslatedTooltip("ii.gui.projectile_workshop.tooltip.core_type"),

				new DecoDropdown<IAmmoTypeItem<?, ?>>(0, 93)
						.withSize(144, 20)
						.withDropdownWidth(144)
						.withScrollBarBackground(DecoTextures.COMPONENT_SLIDER_PAPER)
						.withBackground(DecoTextures.COMPONENT_BUTTON_PAPER)
						.withEntries(AmmoRegistry.getAllAmmoItems())
						.withSortFunction(new DecoElementSorter<>()
						{
							@Override
							public List<IAmmoTypeItem<?, ?>> sort(List<IAmmoTypeItem<?, ?>> elements)
							{
								return elements;
							}

							@Nullable
							@Override
							public List<IAmmoTypeItem<?, ?>> autocomplete(List<IAmmoTypeItem<?, ?>> elements, String input)
							{
								return elements.stream()
										.filter(e -> e.getName().toLowerCase().startsWith(input.toLowerCase()))
										.collect(Collectors.toList());
							}
						})
						.withSelectedEntry(ammoType)
						.withOnSelectedEntry((oldType, newType) -> onAmmoTypeChanged(newType))
						.withDisplayFunction(new DecoEntryPanelBuilder<IAmmoTypeItem<?, ?>>()
								.withBackground(DecoTextures.BG_PAPER)
								.withBackgroundMask(DecoTextures.TEMPLATE_PAPER)
								//Type Icon, Label, and Letter
								.withComponent("icon", p -> new DecoItemStackDisplay(2, 2)
										.withSize(16, 18)
								)
								.withLabel("label", p ->
										new DecoLabel(fontRenderer, 20, 2)
												.withSize(48, 18)
												.withAlign(DecoAlignment.LEFT)
												.withText("ii.gui.projectile_workshop.entry.type")

								)
								.withElementApplyMethod(this::drawAmmoTypeEntry)
								.withElementTooltip(typeMeta -> I18n.format("ii.gui.projectile_workshop.tooltip.ammunition_type"))
						)
						.withTranslatedTooltip("ii.gui.projectile_workshop.tooltip.ammunition_type"),
				//3D display and cost label item
				scenario = new DecoScenarioDisplay(46, 20)
						.withSize(80, 60)
						.withTranslation(0, -0.5, 0)
						.withRotation(-22.5f, 12.5f)
						.withRotationAnimation(120, 0),
				exampleStackDisplay = new DecoItemStackDisplay(110, 75)
						.withStack(IIContent.ammoCoreBrass.getMaterial().getExampleStack()),

				//Core material selector and scrollable projectile information
				new DecoDropdown<AmmoCore>(144, 17)
						.withSize((168), 18)
						.withDropdownWidth((168))
						.withEntries(AmmoRegistry.getAllCores().stream().filter(AmmoPart::showInManual).collect(Collectors.toList()))
						.withSelectedEntry(ammoCore)
						.withScrollBarBackground(DecoTextures.COMPONENT_SLIDER_PAPER)
						.withBackground(DecoTextures.COMPONENT_BUTTON_PAPER)
						.withDisplayFunction(new DecoEntryPanelBuilder<AmmoCore>()
								.withBackground(DecoTextures.BG_PAPER)
								.withBackgroundMask(DecoTextures.TEMPLATE_PAPER)
								.withComponent("icon", p -> new DecoItemStackDisplay(2, 1)
										.withSize(16, 16)
								)
								.withLabel("label", p ->
										new DecoLabel(fontRenderer, 20, 2)
												.withSize(48, 16)
												.withAlign(DecoAlignment.LEFT)
												.withText("ii.gui.projectile_workshop.entry.type")
								)
								.withElementApplyMethod(this::drawAmmoCoreEntry)
						)
						.withSortFunction(new DecoElementSorter<>()
						{
							@Override
							public List<AmmoCore> sort(List<AmmoCore> elements)
							{
								return elements;
							}

							@Nullable
							@Override
							public List<AmmoCore> autocomplete(List<AmmoCore> elements, String input)
							{
								return elements.stream()
										.filter(e -> e.getName().toLowerCase().startsWith(input.toLowerCase()))
										.collect(Collectors.toList());
							}
						})
						.withOnSelectedEntry((ammoCoreOld, ammoCoreNew) -> {
							if(ammoCoreNew==null||ammoCoreNew==ammoCore)
								return;
							this.ammoCore = ammoCoreNew;
							updateCoreInfo();
						}),
				ammoInfoList = new DecoInformationList(144, 38)
						.withSize(166, 95),

				//Energy bar
				new DecoBar(126, 18)
						.withSize(12, 69)
						.withTemplate(DecoTemplates.BAR_ELECTRIC_ENERGY.apply(tile.energyStorage)),

				//Progress bar background
				new DecoImage(8, 28)
						.withSize(16, 29)
						.withImageLocation(PROGRESS_BAR, true)
						.withUV(64, 0, 0, 16, 29),
				new DecoImage(8, 57)
						.withSize(37, 10)
						.withImageLocation(PROGRESS_BAR, true)
						.withUV(64, 0, 29, 37, 39),
				//Progress bar
				new DecoImage(10, 30)
						.withSize(12, 27)
						.withImageLocation(PROGRESS_BAR, true)
						.withUV(64, 52, 26, 64, 53)
						.withAnimation(ImageAnimationDirection.TOP_TO_BOTTOM, DecoGuiUtils.getMultiblockProductionSingleProgress(tile, 0f, 0.6f)),
				new DecoImage(14, 58)
						.withSize(29, 7)
						.withImageLocation(PROGRESS_BAR, true)
						.withUV(64, 35, 57, 64, 64)
						.withAnimation(ImageAnimationDirection.LEFT_TO_RIGHT, DecoGuiUtils.getMultiblockProductionSingleProgress(tile, 0.6f, 1f))
		);
		addLabel("ii.gui.projectile_workshop.material_preview", 144, 8)
				.withSize((168), 8)
				.withAlign(DecoAlignment.CENTER);
		addLabel("ii.gui.projectile_workshop.cost", 48, 79);
		costLabel = addLabel("", 48, 79)
				.withSize(62, 9)
				.withAlign(DecoAlignment.RIGHT);
		addValueListener(() -> coreType.hashCode()+ammoType.hashCode()+ammoCore.hashCode())
				.addObserver(hash -> updateCoreInfo());
		updateCoreInfo();
	}

	private void onAmmoTypeChanged(IAmmoTypeItem<?, ?> newType)
	{
		if(newType==null||newType==ammoType)
			return;
		this.ammoType = newType;
		updateCoreInfo();
		syncCoreConfiguration();
	}

	private void onCoreTypeChanged(CoreType newType)
	{
		if(newType==null||newType==coreType)
			return;
		this.coreType = newType;
		updateCoreInfo();
		syncCoreConfiguration();
	}

	private void refreshCoreTypeDropdown()
	{
		if(ammoType==null||coreTypeDropdown==null)
			return;

		CoreType[] allowedTypes = ammoType.getAllowedCoreTypes();
		if(Arrays.stream(allowedTypes).noneMatch(type -> type==coreType))
			coreType = allowedTypes[0];

		coreTypeDropdown.withEntries(allowedTypes)
				.withSelectedEntry(coreType);
		if(coreTypeDisplay!=null)
			coreTypeDisplay.refreshCache();
	}

	private void syncCoreConfiguration()
	{
		if(hasFillerUpgrade||ammoType==null||coreType==null)
			return;
		IIPacketHandler.sendToServer(new MessageIITileSync(tile, EasyNBT.newNBT()
				.withEnum("core_type", coreType)
				.withString("produced_bullet", ammoType.getName())
		));
	}

	private void updateCoreInfo()
	{
		if(ammoType==null||coreType==null)
		{
			exampleStackDisplay.withStack(ItemStack.EMPTY);
			scenario.withModel(false, new AMTLocator("missingno", Vec3d.ZERO));
			costLabel.withText("ii.gui.projectile_workshop.invalid_cost");
			ammoInfoList.withEntries(Collections.emptyList());
			return;
		}

		refreshCoreTypeDropdown();

		//List cost, available materials and update 3D model
		costLabel.withRawText(I18n.format("ii.gui.projectile_workshop.unit.multiplier", ammoType.getCoreMaterialNeeded()));
		List<ItemStack> stacks = AmmoRegistry.getAllCores().stream()
				.map(AmmoCore::getMaterial)
				.map(IngredientStack::getExampleStack)
				.filter(stack -> !stack.isEmpty())
				.collect(Collectors.toList());
		exampleStackDisplay.withStack(new IngredientStack(stacks));

		AMTBullet amtBullet = new AMTBullet("boolit", Vec3d.ZERO, AmmoRegistry.getGenericModel(ammoType));
		scenario.withModel(false, amtBullet
				.withProperties(ammoCore, coreType, null)
				.withState(BulletState.BULLET_UNUSED)
		).withCentering(amtBullet.getBoundingBox(), 1.75f, 0.5f, 1f);

		List<InformationEntry> information = new ArrayList<>();
		information.add(InformationEntry.title(I18n.format("ii.gui.projectile_workshop.section.core")));
		information.add(createInformationEntry(COMPONENT_SLOTS, coreType.getComponentSlots(),
				"ii.gui.projectile_workshop.tooltip.component_slots",
				"ii.gui.projectile_workshop.tooltip.component_slots.tooltip"
		));
		information.add(createInformationEntry(COMPONENT_SHAPE,
				I18n.format("desc.immersiveintelligence.effect_shape."+coreType.getEffectShape().getName().toLowerCase()),
				"ii.gui.projectile_workshop.tooltip.component_effect_shape",
				"ii.gui.projectile_workshop.tooltip.component_effect_shape.tooltip"
		));
		information.add(createInformationEntry(COMPONENT_EFFICIENCY,
				I18n.format("ii.gui.projectile_workshop.unit.multiplier", coreType.getComponentEffectivenessMod()*ammoCore.getExplosionModifier()),
				"ii.gui.projectile_workshop.tooltip.component_efficiency",
				"ii.gui.projectile_workshop.tooltip.component_efficiency.tooltip"
		));
		information.add(createInformationEntry(COMPONENT_SIZE,
				I18n.format("ii.gui.projectile_workshop.unit.multiplier", Utils.formatDouble(ammoType.getComponentSize(), "0.##")),
				"ii.gui.projectile_workshop.tooltip.component_size",
				"ii.gui.projectile_workshop.tooltip.component_size.tooltip"
		));

		//Ballistics
		if(ammoType.getClass().isAnnotationPresent(IIAmmoProjectile.class))
		{
			IIAmmoProjectile projectileInfo = IIUtils.getAnnotation(IIAmmoProjectile.class, ammoType);
			assert projectileInfo!=null;
			CachedBallisticStats stats = AmmoBallisticsCache.get(ammoType, ammoType.getAmmoStack(ammoCore, coreType, FuseType.CONTACT));
			information.add(InformationEntry.title(I18n.format("ii.gui.projectile_workshop.section.ballistics")));
			information.add(createInformationEntry(MASS,
					Utils.formatDouble(ammoType.getCoreMass(ammoCore, new AmmoComponent[0]), "0.##"),
					"ii.gui.projectile_workshop.tooltip.core_mass",
					"ii.gui.projectile_workshop.tooltip.core_mass.tooltip"
			));
			information.add(createInformationEntry(DAMAGE,
					Utils.formatDouble(ammoType.getDamage()*ammoCore.getDamageModifier()*coreType.getDamageMod(), "0.##"),
					"ii.gui.projectile_workshop.tooltip.damage_dealt",
					"ii.gui.projectile_workshop.tooltip.damage_dealt.tooltip"
			));
			information.add(createInformationEntry(VELOCITY,
					I18n.format("ii.gui.projectile_workshop.unit.blocks_per_tick", ammoType.getVelocity()),
					"ii.gui.projectile_workshop.tooltip.velocity",
					"ii.gui.projectile_workshop.tooltip.velocity.tooltip"
			));
			information.add(createInformationEntry(AVAILABLE_FUZES,
					Arrays.stream(ammoType.getAllowedFuseTypes())
							.map(FuseType::getLocalizedName)
							.collect(Collectors.joining(", ")),
					"ii.gui.projectile_workshop.tooltip.available_fuzes",
					"ii.gui.projectile_workshop.tooltip.available_fuzes.tooltip"
			));
			information.add(createInformationEntry(PENETRATION_HARDNESS,
					IIAmmoUtils.getCombinedHardness(ammoCore, coreType).getLocalizedName(),
					"ii.gui.projectile_workshop.tooltip.penetration_hardness",
					"ii.gui.projectile_workshop.tooltip.penetration_hardness.tooltip"
			));
			information.add(createInformationEntry(PENETRATION_DEPTH,
					I18n.format("ii.gui.projectile_workshop.unit.blocks",
							Utils.formatDouble(IIAmmoUtils.getCombinedDepth(ammoType, coreType), "0.##")),
					"ii.gui.projectile_workshop.tooltip.penetration_depth",
					"ii.gui.projectile_workshop.tooltip.penetration_depth.tooltip"
			));
			information.add(createInformationEntry(FLAT_TRAJECTORY_RANGE,
					I18n.format("ii.gui.projectile_workshop.unit.blocks", Utils.formatDouble(stats.getMaxDirectRange(), "0.##")),
					"ii.gui.projectile_workshop.tooltip.flat_trajectory_range",
					"ii.gui.projectile_workshop.tooltip.flat_trajectory_range.tooltip"
			));
			information.add(createInformationEntry(ARTILLERY_RANGE,
					projectileInfo.artillery()?
							I18n.format("ii.gui.projectile_workshop.unit.blocks", Utils.formatDouble(stats.getGetMaxArtilleryRange(), "0.##")): "-",
					"ii.gui.projectile_workshop.tooltip.max_artillery_range",
					"ii.gui.projectile_workshop.tooltip.max_artillery_range.tooltip"
			));
		}

		ammoInfoList.withEntries(information);
	}

	private InformationEntry createInformationEntry(ResourceLocation icon, Object value, String tooltipKey, String... tooltipLines)
	{
		List<String> tooltip = new ArrayList<>();
		tooltip.add(I18n.format(tooltipKey));
		Arrays.stream(tooltipLines)
				.map(I18n::format)
				.forEach(tooltip::add);
		return InformationEntry.information(tooltip.get(0), icon, String.valueOf(value), tooltip.toArray(new String[0]));
	}

	private void drawAmmoCoreEntry(AmmoCore core, DecoEntryPanelBuilder<AmmoCore> builder)
	{
		builder.label("label").withText("item."+"immersiveintelligence"+".bullet.component."+core.getName()+".name");
		builder.component("icon", DecoItemStackDisplay.class)
				.withStack(core.getMaterial());
	}

	private void drawCoreTypeEntry(CoreType coreType, DecoEntryPanelBuilder<CoreType> builder)
	{
		builder.label("label").withText("desc.immersiveintelligence."+"bullet_core_type."+coreType.getName());
		builder.component("icon", DecoItemStackDisplay.class)
				.withStack(ammoType.getAmmoCoreStack(ammoCore, coreType));
	}

	private void drawAmmoTypeEntry(IAmmoTypeItem<?, ?> ammoType, DecoEntryPanelBuilder<IAmmoTypeItem<?, ?>> builder)
	{
		//typeDropdown.getSelectedEntry()
		ItemStack stack = ammoType.getAmmoStack(ammoCore, ammoType.getAllowedCoreTypes()[0], FuseType.CONTACT);
		builder.label("label")
				.withRawText(stack.getDisplayName());
		if(!stack.isEmpty())
			builder.component("icon", DecoItemStackDisplay.class)
					.withStack(stack);

		//I18n.format("item.immersiveintelligence."+s.toLowerCase()+".bullet.name");
	}

	@Override
	protected EasyNBT onSaveTileData()
	{
		EasyNBT nbt = super.onSaveTileData();
		if(hasFillerUpgrade)
			return nbt.withInt("component_fill_amount", tile.componentFillAmount);

		return nbt
				.withEnum("core_type", coreType)
				.withString("produced_bullet", ammoType.getName());
	}

	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
		syncAnimatedParts(openedPart, false);
	}
}
