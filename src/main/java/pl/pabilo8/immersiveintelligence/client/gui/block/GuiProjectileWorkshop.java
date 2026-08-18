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
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 18.08.2025
 * @ii-approved 0.3.1
 * @since 10.07.2019
 */
@DecoTemplate(name = "projectile_workshop", category = DecoGuiCategory.PRODUCTION_TILE)
public class GuiProjectileWorkshop extends DecoTileGui<TileEntityProjectileWorkshop, ContainerProjectileWorkshop>
{
	private static final String GUI_KEY = IIReference.GUI_LABEL_KEY+"projectile_workshop.";

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
	public static ResourceLocation VELOCITY = IIReference.RES_II.with("gui/ammo_icons/velocity");

	private MultiblockInteractablePart openedPart;
	boolean hasFillerUpgrade;

	//Non-upgraded
	private DecoPanel ammoInfoPanel, fillerInfoPanel;
	private DecoDropdown<CoreType> coreTypeDropdown;
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
				.withBox(DecoTextures.BG_STEEL_ROUGH, DecoTextures.TEMPLATE_SQUARE, 0, 0, hasFillerUpgrade?176: 256, 130)
				.withTitleBar(tile)
				.withNextLayer()
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, hasFillerUpgrade?0: 44, 136, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventoryTitleBar()

				.conditionally(!hasFillerUpgrade, builder -> builder
						.withNextLayer()
						.withBox(DecoTextures.BG_STEEL_ROUGH, DecoTextures.TEMPLATE_SQUARE, 0, 0, 144, 130)
						.withInventorySlots(SlotStyle.IE_INPUT, container.inputSlot)

						.withNextLayer()
						.withBox(DecoTextures.BG_PAPER, DecoTextures.TEMPLATE_PAPER, 144, 0, 120, 130)

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
						.withBox(DecoTextures.BG_PAPER, DecoTextures.TEMPLATE_PAPER, 4, 128+8-64-4, 176-4-8, 64)
						.withTitleBar(GUI_KEY+"component_info", DecoAlignment.TOP_LEFT)
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
				new DecoBar(176-8, 4)
						.withSize(12, 64)
						.withTemplate(DecoTemplates.BAR_ELECTRIC_ENERGY.apply(tile.energyStorage)),
				//Right-side paper panel
				this.fillerInfoPanel = new DecoPanel(4, 128+8-64+4)
						.withSize(176, 64)
						.withBackground(null),
				//Stored component display
				new DecoItemStackDisplay(176/2-9, 12)
						.withSize(16, 16)
						.withOnTooltip(gui -> {
							AmmoComponent current = tile.componentInside.getComponent();
							if(current==null||tile.componentInside.amount <= 0)
								return Collections.singleton(I18n.format("gui.immersiveengineering.empty"));
							return Arrays.asList(
									current.getColor().getHexCol(current.getTranslatedName()),
									TextFormatting.GRAY.toString()+tile.componentInside.amount+" Units"+TextFormatting.RESET,
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
				new DecoItemStackDisplay(176-8-24+2, 45-8)
						.withSize(16, 16)
						.withBackgroundTexture(DecoSprite.atlasSprite(DecoTextures.SLOT_IE, 32, true)),

				//Progress bar background
				new DecoImage(8+64+8+2+1-48-2, 28+4+4+4+1)
						.withSize(48, 8)
						.withImageLocation(PROGRESS_BAR, true)
						.withUV(64, 16, 11, 16+48, 11+8),
				new DecoImage(8+64+8+2+1, 28+4+4)
						.withSize(8, 19)
						.withImageLocation(PROGRESS_BAR, true)
						.withUV(64, 37, 19, 37+8, 19+19),
				new DecoImage(8+64+8+2+1+2+8, 28+4+4+4)
						.withSize(48, 10)
						.withImageLocation(PROGRESS_BAR, true)
						.withUV(64, 16, 0, 16+48, 10),

				//Progress bar
				new DecoImage(8+64+8+2+1-48, 28+4+4+4+1+1)
						.withSize(44, 6)
						.withImageLocation(PROGRESS_BAR, true)
						.withUV(64, 0, 47, 44, 47+6)
						.withAnimation(ImageAnimationDirection.LEFT_TO_RIGHT, DecoGuiUtils.getMultiblockProductionSingleProgress(tile, 0f, 0.45f)),
				new DecoImage(8+64+8+2+1, 28+4+4)
						.withSize(8, 19)
						.withImageLocation(PROGRESS_BAR, true)
						.withUV(64, 45, 19, 45+8, 19+19)
						.withAnimation(ImageAnimationDirection.LEFT_TO_RIGHT, DecoGuiUtils.getMultiblockProductionSingleProgress(tile, 0.45f, 0.55f)),
				new DecoImage(8+64+8+2+1+2+8+2, 28+4+4+4+2)
						.withSize(44, 6)
						.withImageLocation(PROGRESS_BAR, true)
						.withUV(64, 0, 39, 44, 39+6)
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

		fillerInfoPanel.addLabel(TextFormatting.ITALIC+stack.component.getTranslatedName(), 4, 0)
				.withSize(76, 9)
				.withAlign(DecoAlignment.LEFT);
		fillerInfoPanel.addLabel(I18n.format(GUI_KEY+"component.role", stack.component.getRole().getLocalizedName()), 4, 12)
				.withSize(100, 9)
				.withAlign(DecoAlignment.LEFT);
		fillerInfoPanel.addLabel(I18n.format(GUI_KEY+"component.density", stack.component.getDensity()), 4, 24)
				.withSize(100, 9)
				.withAlign(DecoAlignment.LEFT);
		fillerInfoPanel.addLabel(I18n.format(GUI_KEY+"component.slots_taken", stack.component.getSlotsTaken()), 4, 36)
				.withSize(100, 9)
				.withAlign(DecoAlignment.LEFT);
		if(stack.component.showInManual())
			fillerInfoPanel.addComponent(new DecoButton(176-6-16-8-2, -4)
					.withSize(20, 20)

					.withBackground(DecoTextures.COMPONENT_BUTTON)
					.withBackgroundColor(IIColor.fromPackedRGB(0x7A7ABC))
					.withPadding(2, 2, 2, 2)
					.withIconAlignment(DecoAlignment.CENTER)
					//Engineer's Manual
					.withIcon(new ItemStack(IEContent.itemTool, 1, 3))
					.withTranslatedTooltip(IIReference.GUI_TOOLTIP_KEY+"widget.manual.page")

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
						.withOnSelectedEntry((oldType, newType) -> this.coreType = newType)
						.withDisplayFunction(new DecoEntryPanelBuilder<CoreType>()
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
												.withText(GUI_KEY+"entry.core")
								)
								.withElementApplyMethod(this::drawCoreTypeEntry)
								.withElementTooltip(typeMeta -> GUI_KEY+"tooltip.core_type")
						)
						.withTranslatedTooltip(GUI_KEY+"tooltip.core_type"),

				new DecoDropdown<IAmmoTypeItem<?, ?>>(0, 93)
						.withSize(144, 20)
						.withDropdownWidth(144)
						.withScrollBarBackground(DecoTextures.COMPONENT_SLIDER_PAPER)
						.withBackground(DecoTextures.COMPONENT_BUTTON_PAPER)
						.withEntries(AmmoRegistry.getAllAmmoItems())
						.withSortFunction(new DecoElementSorter<IAmmoTypeItem<?, ?>>()
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
						.withOnSelectedEntry((oldType, newType) -> this.ammoType = newType)
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
												.withText(GUI_KEY+"entry.type")

								)
								.withElementApplyMethod(this::drawAmmoTypeEntry)
								.withElementTooltip(typeMeta -> I18n.format(GUI_KEY+"tooltip.ammunition_type"))
						)
						.withTranslatedTooltip(GUI_KEY+"tooltip.ammunition_type"),
				//3D display and cost label item
				scenario = new DecoScenarioDisplay(46, 20)
						.withSize(80, 60)
						.withTranslation(0, -0.5, 0)
						.withRotation(-22.5f, 12.5f)
						.withRotationAnimation(120, 0),
				exampleStackDisplay = new DecoItemStackDisplay(110, 75)
						.withStack(IIContent.ammoCoreBrass.getMaterial().getExampleStack()),

				//Paper page with ammo stats
				ammoInfoPanel = new DecoPanel(144, 0)
						.withSize(120, 130)
						.withBackground(null),

				//Energy bar
				new DecoBar(126, 18)
						.withSize(12, 69)
						.withTemplate(DecoTemplates.BAR_ELECTRIC_ENERGY.apply(tile.energyStorage)),

				//Progress bar background
				new DecoImage(8, 28)
						.withSize(16, 29)
						.withImageLocation(PROGRESS_BAR, true)
						.withUV(64, 0, 0, 16, 29),
				new DecoImage(8, 28+29)
						.withSize(37, 10)
						.withImageLocation(PROGRESS_BAR, true)
						.withUV(64, 0, 29, 37, 29+10),
				//Progress bar
				new DecoImage(8+2, 28+2)
						.withSize(12, 27)
						.withImageLocation(PROGRESS_BAR, true)
						.withUV(64, 52, 26, 52+12, 26+27)
						.withAnimation(ImageAnimationDirection.TOP_TO_BOTTOM, DecoGuiUtils.getMultiblockProductionSingleProgress(tile, 0f, 0.6f)),
				new DecoImage(8+2+4, 28+2+27+1)
						.withSize(29, 7)
						.withImageLocation(PROGRESS_BAR, true)
						.withUV(64, 35, 57, 35+29, 57+7)
						.withAnimation(ImageAnimationDirection.LEFT_TO_RIGHT, DecoGuiUtils.getMultiblockProductionSingleProgress(tile, 0.6f, 1f))
		);
		addLabel(GUI_KEY+"cost", 48, 79);
		costLabel = addLabel("", 48, 79)
				.withSize(62, 9)
				.withAlign(DecoAlignment.RIGHT);
		addValueListener(() -> coreType.hashCode()+ammoType.hashCode()+ammoCore.hashCode())
				.addObserver(hash -> updateCoreInfo());
		updateCoreInfo();
	}

	private void updateCoreInfo()
	{
		if(ammoType==null||coreType==null)
		{
			exampleStackDisplay.withStack(ItemStack.EMPTY);
			scenario.withModel(false, new AMTLocator("missingno", Vec3d.ZERO));
			costLabel.withText(GUI_KEY+"invalid_cost");
			return;
		}

		//Only allow core types defined by the ammo type
		if(Arrays.stream(ammoType.getAllowedCoreTypes()).noneMatch(t -> t==coreType))
			this.coreType = ammoType.getAllowedCoreTypes()[0];

		//Update the types available in the dropdown
		coreTypeDropdown.withEntries(ammoType.getAllowedCoreTypes())
				.withSelectedEntry(coreType);

		//List cost, available materials and update 3D model
		costLabel.withRawText(I18n.format(GUI_KEY+"unit.multiplier", ammoType.getCoreMaterialNeeded()));
		List<ItemStack> stacks = AmmoRegistry.getAllCores().stream()
				.map(AmmoCore::getMaterial)
				.map(IngredientStack::getExampleStack)
				.filter(stack -> !stack.isEmpty())
				.collect(Collectors.toList());
		exampleStackDisplay.withStack(new IngredientStack(stacks));
		scenario.withModel(false, new AMTBullet("boolit", Vec3d.ZERO, AmmoRegistry.getGenericModel(ammoType))
				.withProperties(ammoCore, coreType, null)
				.withState(BulletState.BULLET_UNUSED)
		);
		scenario.withScale(0.85f);

		ammoInfoPanel.cleanup();
		//Core material dropdown
		ammoInfoPanel.addLabel(new DecoLabel(fontRenderer, 0, 8)
				.withText(GUI_KEY+"material_preview")
				.withSize(ammoInfoPanel.width, 8)
				.withAlign(DecoAlignment.CENTER)
		);
		ammoInfoPanel.addComponent(
				new DecoDropdown<AmmoCore>(0, 17)
						.withSize(ammoInfoPanel.width, 18)
						.withDropdownWidth(ammoInfoPanel.width)
						.withEntries(AmmoRegistry.getAllCores().stream().filter(AmmoPart::showInManual).collect(Collectors.toList()))
						.withSelectedEntry(ammoCore)
						.withScrollBarBackground(DecoTextures.COMPONENT_SLIDER_PAPER)
						.withBackground(DecoTextures.COMPONENT_BUTTON_PAPER)
						.withDisplayFunction(new DecoEntryPanelBuilder<AmmoCore>()
								.withBackground(DecoTextures.BG_PAPER)
								.withBackgroundMask(DecoTextures.TEMPLATE_PAPER)
								//Type Icon, Label, and Letter
								.withComponent("icon", p -> new DecoItemStackDisplay(2, 1)
										.withSize(16, 16)
								)
								.withLabel("label", p ->
										new DecoLabel(fontRenderer, 20, 2)
												.withSize(48, 16)
												.withAlign(DecoAlignment.LEFT)
												.withText(GUI_KEY+"entry.type")

								)
								.withElementApplyMethod(this::drawAmmoCoreEntry)
						)
						.withSortFunction(new DecoElementSorter<AmmoCore>()
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
							this.ammoCore = ammoCoreNew;
							updateCoreInfo();
						})
		);

		//Core
		ammoInfoPanel.addLabel(new DecoLabel(fontRenderer, 0, 37)
				.withText(GUI_KEY+"section.core")
				.withSize(ammoInfoPanel.width, 8)
				.withAlign(DecoAlignment.CENTER)
		);
		addImageWithLabel(1, 45, COMPONENT_SLOTS, coreType.getComponentSlots(),
				GUI_KEY+"tooltip.component_slots");
		addImageWithLabel(ammoInfoPanel.width/2, 45, COMPONENT_SHAPE, "desc.immersiveintelligence.effect_shape."+coreType.getEffectShape().getName().toLowerCase(),
				GUI_KEY+"tooltip.component_effect_shape");

		addImageWithLabel(1, 45+13, COMPONENT_EFFICIENCY, I18n.format(GUI_KEY+"unit.multiplier", coreType.getComponentEffectivenessMod()*ammoCore.getExplosionModifier()),
				GUI_KEY+"tooltip.component_efficiency", GUI_KEY+"tooltip.component_efficiency.line1", GUI_KEY+"tooltip.component_efficiency.line2");
		addImageWithLabel(ammoInfoPanel.width/2, 45+13, COMPONENT_SIZE, I18n.format(GUI_KEY+"unit.multiplier", Utils.formatDouble(ammoType.getComponentSize(), "0.##")),
				GUI_KEY+"tooltip.component_size", GUI_KEY+"tooltip.component_size.line1");

		//Ballistics
		if(ammoType.getClass().isAnnotationPresent(IIAmmoProjectile.class))
		{
			IIAmmoProjectile projectileInfo = IIUtils.getAnnotation(IIAmmoProjectile.class, ammoType);
			assert projectileInfo!=null;
			CachedBallisticStats stats = AmmoBallisticsCache.get(ammoType, ammoType.getAmmoStack(ammoCore, coreType, FuseType.CONTACT));
			ammoInfoPanel.addLabel(new DecoLabel(fontRenderer, 0, 73)
					.withText(GUI_KEY+"section.ballistics")
					.withSize(ammoInfoPanel.width, 8)
					.withAlign(DecoAlignment.CENTER)
			);

			addImageWithLabel(1, 81-2, MASS, Utils.formatDouble(ammoType.getCoreMass(ammoCore, new AmmoComponent[0]), "0.##"),
					GUI_KEY+"tooltip.core_mass");
			addImageWithLabel(ammoInfoPanel.width/2, 81-2, DAMAGE, Utils.formatDouble(ammoType.getDamage()*ammoCore.getDamageModifier()*coreType.getDamageMod(), "0.##"),
					GUI_KEY+"tooltip.damage_dealt");
			addImageWithLabel(1, 81+13, VELOCITY, I18n.format(GUI_KEY+"unit.blocks_per_tick", ammoType.getVelocity()),
					GUI_KEY+"tooltip.velocity");
			addImageWithLabel(ammoInfoPanel.width/2, 81+13, AVAILABLE_FUZES, ammoType.getAllowedFuseTypes().length+"/"+FuseType.values().length,
					GUI_KEY+"tooltip.available_fuzes");
			addImageWithLabel(1, 81+13+13, PENETRATION_HARDNESS,
					I18n.format(GUI_KEY+"value.penetration_hardness",
							Utils.formatDouble(IIAmmoUtils.getCombinedDepth(ammoType, coreType), "0.##"),
							I18n.format("desc.immersiveintelligence.penetration_hardness."+IIAmmoUtils.getCombinedHardness(ammoCore, coreType).getName().toLowerCase())
					),
					GUI_KEY+"tooltip.penetration_hardness");
			/*addImageWithLabel(ammoInfoPanel.width/2, 81+13+13, MAX_PENETRATION_DEPTH, ,
					"Max. Penetration Depth");*/
			addImageWithLabel(1, 81+13+13+13, FLAT_TRAJECTORY_RANGE, I18n.format(GUI_KEY+"unit.blocks", Utils.formatDouble(stats.getMaxDirectRange(), "0.##")),
					GUI_KEY+"tooltip.flat_trajectory_range");
			addImageWithLabel(ammoInfoPanel.width/2, 81+13+13+13, ARTILLERY_RANGE, projectileInfo.artillery()?
							I18n.format(GUI_KEY+"unit.blocks", Utils.formatDouble(stats.getGetMaxArtilleryRange(), "0.##")): "-",
					GUI_KEY+"tooltip.max_artillery_range");
		}
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

	private void addImageWithLabel(int x, int y, ResourceLocation icon, Object value, String... tooltipLines)
	{
		ammoInfoPanel.addComponent(
				new DecoImage(x, y)
						.withImageLocation(icon, true)
						.withSize(16, 16)
						.withTranslatedTooltip(tooltipLines)
		);
		ammoInfoPanel.addLabel(String.valueOf(value), x+16+2, y+4+1);
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
