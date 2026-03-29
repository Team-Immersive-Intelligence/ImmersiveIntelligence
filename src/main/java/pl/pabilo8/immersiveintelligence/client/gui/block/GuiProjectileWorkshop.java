package pl.pabilo8.immersiveintelligence.client.gui.block;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
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
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoDropdown;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoEntryPanelBuilder;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBar;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoItemStackDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoScenarioDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTBullet;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTBullet.BulletState;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTLocator;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityProjectileWorkshop;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerProjectileWorkshop;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 18.08.2025
 * @ii-approved 0.3.1
 * @since 10.07.2019
 */
@DecoTemplate(name = "projectile_workshop", category = DecoGuiCategory.PRODUCTION_TILE)
public class GuiProjectileWorkshop extends DecoGui<TileEntityProjectileWorkshop, ContainerProjectileWorkshop>
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
	public static ResourceLocation VELOCITY = IIReference.RES_II.with("gui/ammo_icons/velocity");

	private MultiblockInteractablePart openedPart;
	boolean hasFillerUpgrade;

	//Non-upgraded
	private DecoPanel ammoInfoPanel;
	private DecoDropdown<CoreType> coreTypeDropdown;
	private IAmmoTypeItem<?, ?> ammoType;
	private AmmoCore ammoCore;
	private CoreType coreType;

	private DecoItemStackDisplay exampleStackDisplay;
	private DecoScenarioDisplay scenario;
	private DecoLabel costLabel;

	//Upgraded

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
		syncAnimatedParts(openedPart = (Utils.RAND.nextGaussian() > 0.5f?tile.lid1: tile.lid2), true);

		//Add background
		startBackground()
				.withBox(DecoTextures.BG_STEEL_ROUGH, DecoTextures.TEMPLATE_SQUARE, 0, 0, 256, 130)
				.withTitleBar(tile)
				.withNextLayer()
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 44, 136, 176, 92)
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

	}

	private void addCoreWorkshopComponents()
	{
		addComponents(
				coreTypeDropdown = new DecoDropdown<CoreType>(0, 112)
						.withSize(144, 20)
						.withDropdownWidth(144)
						.withScrollBarBackground(DecoTextures.COMPONENT_SLIDER_PAPER)
						.withBackground(DecoTextures.COMPONENT_BUTTON_PAPER)
						.withListBackground(DecoTextures.COMPONENT_TEXT_FIELD)
						.withEntries(CoreType.values())
						.withSelectedEntry(coreType)
						.withOnSelectedEntry((oldType, newType) -> this.coreType = newType)
						.withDisplayFunction(new DecoEntryPanelBuilder<CoreType>()
								.withBackground(DecoTextures.BG_PAPER)
								.withBackgroundMask(DecoTextures.TEMPLATE_PAPER)
								//Type Icon, Label, and Letter
								.withComponent("icon", new DecoItemStackDisplay(2, 2)
										.withSize(16, 18)
								)
								.withLabel("label",
										new DecoLabel(fontRenderer, 20, 2)
												.withSize(48, 18)
												.withAlign(DecoAlignment.LEFT)
												.withText("Core")
								)
								.withElementApplyMethod(this::drawCoreTypeEntry)
								.withElementTooltip(typeMeta -> "a")
						)
						.withTranslatedTooltip("Core Type"),

				new DecoDropdown<IAmmoTypeItem<?, ?>>(0, 93)
						.withSize(144, 20)
						.withDropdownWidth(144)
						.withScrollBarBackground(DecoTextures.COMPONENT_SLIDER_PAPER)
						.withBackground(DecoTextures.COMPONENT_BUTTON_PAPER)
						.withListBackground(DecoTextures.COMPONENT_TEXT_FIELD)
						.withEntries(AmmoRegistry.getAllAmmoItems())
						.withSelectedEntry(ammoType)
						.withOnSelectedEntry((oldType, newType) -> this.ammoType = newType)
						.withDisplayFunction(new DecoEntryPanelBuilder<IAmmoTypeItem<?, ?>>()
								.withBackground(DecoTextures.BG_PAPER)
								.withBackgroundMask(DecoTextures.TEMPLATE_PAPER)
								//Type Icon, Label, and Letter
								.withComponent("icon", new DecoItemStackDisplay(2, 2)
										.withSize(16, 18)
								)
								.withLabel("label",
										new DecoLabel(fontRenderer, 20, 2)
												.withSize(48, 18)
												.withAlign(DecoAlignment.LEFT)
												.withText("Type")

								)
								.withElementApplyMethod(this::drawAmmoTypeEntry)
								.withElementTooltip(typeMeta -> "a")
						)
						.withTranslatedTooltip("Ammunition Type"),
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

				//Progress bar
				new DecoImage(8, 28)
						.withSize(37, 39)
						.withImageLocation(PROGRESS_BAR, true)
						.withUV(64, 0, 0, 37, 39)
		);
		addLabel("Cost:", 48, 79)
				.withSize(78, 9);
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
			costLabel.withText("Cost: 250 Pabulions and one Yevreiski Cent");
			return;
		}

		//Only allow core types defined by the ammo type
		if(Arrays.stream(ammoType.getAllowedCoreTypes()).noneMatch(t -> t==coreType))
			this.coreType = ammoType.getAllowedCoreTypes()[0];

		//Update the types available in the dropdown
		coreTypeDropdown.withEntries(ammoType.getAllowedCoreTypes())
				.withSelectedEntry(coreType);

		//List cost, available materials and update 3D model
		costLabel.withText(ammoType.getCoreMaterialNeeded()+"x");
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
				.addText("Material Preview")
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
						.withListBackground(DecoTextures.COMPONENT_TEXT_FIELD)
						.withDisplayFunction(new DecoEntryPanelBuilder<AmmoCore>()
								.withBackground(DecoTextures.BG_PAPER)
								.withBackgroundMask(DecoTextures.TEMPLATE_PAPER)
								//Type Icon, Label, and Letter
								.withComponent("icon", new DecoItemStackDisplay(2, 1)
										.withSize(16, 16)
								)
								.withLabel("label",
										new DecoLabel(fontRenderer, 20, 2)
												.withSize(48, 16)
												.withAlign(DecoAlignment.LEFT)
												.withText("Type")

								)
								.withElementApplyMethod(this::drawAmmoCoreEntry)
						)
						.withOnSelectedEntry((ammoCoreOld, ammoCoreNew) -> {
							this.ammoCore = ammoCoreNew;
							updateCoreInfo();
						})
		);

		//Core
		ammoInfoPanel.addLabel(new DecoLabel(fontRenderer, 0, 37)
				.addText("Core")
				.withSize(ammoInfoPanel.width, 8)
				.withAlign(DecoAlignment.CENTER)
		);
		addImageWithLabel(1, 45, COMPONENT_SLOTS, coreType.getComponentSlots(),
				"Component Slots");
		addImageWithLabel(ammoInfoPanel.width/2, 45, COMPONENT_SHAPE, "desc.immersiveintelligence.effect_shape."+coreType.getEffectShape().getName().toLowerCase(),
				"Component Effect Shape");

		addImageWithLabel(1, 45+13, COMPONENT_EFFICIENCY, coreType.getComponentEffectivenessMod()*ammoCore.getExplosionModifier()+"x",
				"Component Efficiency Modifier", "Determines potency.", "Responsible for explosive power, chemical gas concentration.");
		addImageWithLabel(ammoInfoPanel.width/2, 45+13, COMPONENT_SIZE, Utils.formatDouble(ammoType.getComponentSize(), "0.##")+"x",
				"Component Size Multiplier", "Determines component volume, responsible for explosion radius, gas spread range multiplier.");

		//Ballistics
		if(ammoType.getClass().isAnnotationPresent(IIAmmoProjectile.class))
		{
			IIAmmoProjectile projectileInfo = IIUtils.getAnnotation(IIAmmoProjectile.class, ammoType);
			assert projectileInfo!=null;
			CachedBallisticStats stats = AmmoBallisticsCache.get(ammoType, ammoType.getAmmoStack(ammoCore, coreType, FuseType.CONTACT));
			ammoInfoPanel.addLabel(new DecoLabel(fontRenderer, 0, 73)
					.addText("Ballistics")
					.withSize(ammoInfoPanel.width, 8)
					.withAlign(DecoAlignment.CENTER)
			);

			addImageWithLabel(1, 81-2, MASS, Utils.formatDouble(ammoType.getCoreMass(ammoCore, new AmmoComponent[0]), "0.##"),
					"Core Mass");
			addImageWithLabel(ammoInfoPanel.width/2, 81-2, DAMAGE, Utils.formatDouble(ammoType.getDamage()*ammoCore.getDamageModifier()*coreType.getDamageMod(), "0.##"),
					"Damage Dealt");
			addImageWithLabel(1, 81+13, VELOCITY, ammoType.getVelocity()+" b/t",
					"Velocity");
			addImageWithLabel(ammoInfoPanel.width/2, 81+13, AVAILABLE_FUZES, ammoType.getAllowedFuseTypes().length+"/"+FuseType.values().length,
					"Available Fuzes");
			addImageWithLabel(1, 81+13+13, PENETRATION_HARDNESS,
					Utils.formatDouble(IIAmmoUtils.getCombinedDepth(ammoType, coreType), "0.##")+"b "+
							I18n.format("desc.immersiveintelligence.penetration_hardness."+IIAmmoUtils.getCombinedHardness(ammoCore, coreType).getName().toLowerCase()),
					"Penetration Hardness");
			/*addImageWithLabel(ammoInfoPanel.width/2, 81+13+13, MAX_PENETRATION_DEPTH, ,
					"Max. Penetration Depth");*/
			addImageWithLabel(1, 81+13+13+13, FLAT_TRAJECTORY_RANGE, Utils.formatDouble(stats.getMaxDirectRange(), "0.##")+"b",
					"Flat-Trajectory Range");
			addImageWithLabel(ammoInfoPanel.width/2, 81+13+13+13, ARTILLERY_RANGE, projectileInfo.artillery()?
							Utils.formatDouble(stats.getGetMaxArtilleryRange(), "0.##")+"b": "-",
					"Max. Artillery Range");
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
		return super.onSaveTileData()
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
