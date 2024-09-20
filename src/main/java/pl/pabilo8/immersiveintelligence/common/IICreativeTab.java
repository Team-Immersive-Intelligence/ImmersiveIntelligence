package pl.pabilo8.immersiveintelligence.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreType;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.FuseType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.client.gui.GuiWidgetAustralianTabs;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.BlockIIMetalDecoration.IIBlockTypes_MetalDecoration;
import pl.pabilo8.immersiveintelligence.common.block.mines.BlockIIMine.ItemBlockMineBase;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIBulletMagazine.Magazines;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockEnum;
import pl.pabilo8.immersiveintelligence.common.util.block.ItemBlockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Pabilo8
 * @author GabrielV (gabriel@iiteam.net) - Creative Sub Tabs
 * @updated 21.05.2024
 * @ii-approved 0.3.1
 * @since 7.07.2019
 */
public class IICreativeTab extends CreativeTabs
{
	public static IICategory selectedCategory = IICategory.RESOURCES;
	public static List<Fluid> fluidBucketMap = new ArrayList<>();
	@SideOnly(Side.CLIENT)
	private GuiWidgetAustralianTabs hovered;

	public IICreativeTab(String name)
	{
		super(name);
	}

	@Override
	@Nonnull
	public ItemStack getTabIconItem()
	{
		return IIContent.blockMetalDecoration.getStack(IIBlockTypes_MetalDecoration.COIL_DATA, 1);
	}

	@Override
	public ResourceLocation getBackgroundImage()
	{
		return selectedCategory.getCreativeTabTexture();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public String getTranslatedTabLabel()
	{
		return "itemGroup.immersiveintelligence";
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean isTabInFirstRow()
	{
		//The trick to outtrick them all
		if(hovered!=null)
		{
			hovered.drawHovered();
			hovered = null;
		}
		return super.isTabInFirstRow();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void displayAllRelevantItems(@Nonnull NonNullList<ItemStack> list)
	{
		if(!IIConfig.australianCreativeTabs)
		{
			super.displayAllRelevantItems(list);
			for(Fluid fluid : fluidBucketMap)
				addFluidBucket(fluid, list);
			addExampleAmmo(list);
			return;
		}

		for(Item item : Item.REGISTRY)
		{
			if(item instanceof ItemBlockIIBase)
			{
				ItemBlockIIBase itemBlock = (ItemBlockIIBase)item;
				BlockIIBase<?> block = itemBlock.getBlock();

				for(IIBlockEnum subItem : block.enumValues)
				{
					if(block.isHidden(subItem.getMeta())||block.getCategory(subItem.getMeta())!=selectedCategory)
						continue;
					list.add(new ItemStack(block, 1, subItem.getMeta()));
				}
			}
			else if(item instanceof ItemIISubItemsBase)
			{
				//use parent category as fallback
				IICategory parentCategory = IICategory.RESOURCES;
				if(item.getClass().isAnnotationPresent(IIItemProperties.class))
				{
					IIItemProperties properties = item.getClass().getAnnotation(IIItemProperties.class);
					if(properties.hidden())
						continue;
					parentCategory = properties.category();
				}
				//assign individual sub-items to their categories
				for(IIItemEnum subItem : ((ItemIISubItemsBase<?>)item).getSortedSubItems())
				{
					if(subItem.isHidden())
						continue;
					IICategory category = subItem.getCategory();
					if(category==IICategory.NULL)
						category = parentCategory;

					if(category==selectedCategory)
						list.add(new ItemStack(item, 1, subItem.getMeta()));
				}
			}
			else if(item.getClass().isAnnotationPresent(IIItemProperties.class))
			{
				IIItemProperties properties = item.getClass().getAnnotation(IIItemProperties.class);
				if(properties.hidden())
					continue;
				if(properties.category()==selectedCategory)
					item.getSubItems(this, list);
			}
			else if(selectedCategory==IICategory.RESOURCES)
				item.getSubItems(this, list);
		}

		switch(selectedCategory)
		{
			case WARFARE:
				addExampleAmmo(list);
				break;
			case RESOURCES:
			{
				tabNewLine(list);
				for(Fluid fluid : fluidBucketMap)
					addFluidBucket(fluid, list);
			}
			break;
			default:
				break;
		}

	}

	public void addFluidBucket(Fluid fluid, NonNullList<ItemStack> list)
	{
		UniversalBucket bucket = ForgeModContainer.getInstance().universalBucket;
		ItemStack stack = new ItemStack(bucket);
		FluidStack fs = new FluidStack(fluid, bucket.getCapacity());
		IFluidHandlerItem fluidHandler = new FluidBucketWrapper(stack);
		if(fluidHandler.fill(fs, true)==fs.amount)
			list.add(fluidHandler.getContainer());
	}

	public void addExampleAmmo(NonNullList<ItemStack> list)
	{
		LinkedHashMap<String, IAmmoTypeItem<?, ?>> items = new LinkedHashMap<>();
		items.put("AH8", IIContent.itemAmmoHeavyArtillery);
		items.put("AH6L", IIContent.itemAmmoMediumArtillery);
		items.put("AH6K", IIContent.itemAmmoLightArtillery);
		items.put("AG4", IIContent.itemAmmoLightGun);
		items.put("G4", IIContent.itemRailgunGrenade);

		//Guns and Howitzers
		items.forEach((name, item) -> {
			tabNewLine(list);
			addAmmo(list, item, name+"/P1 Panzergranate",
					IIContent.ammoCoreLead, CoreType.PIERCING, FuseType.CONTACT, IIContent.ammoComponentTNT);
			addAmmo(list, item, name+"/P2 Panzergranate",
					IIContent.ammoCoreSteel, CoreType.PIERCING, FuseType.CONTACT, IIContent.ammoComponentTNT);
			addAmmo(list, item, name+"/P3 Panzergranate",
					IIContent.ammoCoreTungsten, CoreType.PIERCING, FuseType.CONTACT, IIContent.ammoComponentTNT);

			addAmmo(list, item, name+"/P5 Hochladungsgranate",
					IIContent.ammoCoreCopper, CoreType.SHAPED, FuseType.CONTACT, IIContent.ammoComponentRDX, IIContent.ammoComponentTracerPowder);
			addAmmo(list, item, name+"/P6 Hochladungsgranate",
					IIContent.ammoCoreBrass, CoreType.SHAPED_SABOT, FuseType.CONTACT, IIContent.ammoComponentHMX);

			addAmmo(list, item, name+"/P7 KE-Geschoss",
					IIContent.ammoCoreTungsten, CoreType.PIERCING_SABOT, FuseType.CONTACT);
			addAmmo(list, item, name+"/P8 KE-Geschoss",
					IIContent.ammoCoreUranium, CoreType.PIERCING_SABOT, FuseType.CONTACT);

			addAmmo(list, item, name+"/W1 Flakgranate",
					IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.PROXIMITY,
					IIContent.ammoComponentWhitePhosphorus, AmmoRegistry.getComponent("shrapnel_steel"), IIContent.ammoComponentTracerPowder);
			addAmmo(list, item, name+"/W4 Napalmgranate",
					IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.PROXIMITY, getFluidComponent("napalm"), IIContent.ammoComponentTracerPowder);
			addAmmo(list, item, name+"/W6 Elektrobrandgranate",
					IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.PROXIMITY, IIContent.ammoComponentTesla, IIContent.ammoComponentWhitePhosphorus);
			addAmmo(list, item, name+"/W7 Industriestörunggranate",
					IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.PROXIMITY,
					getFluidComponent("hydrofluoric_acid"), getFluidComponent("mustard_gas"), IIContent.ammoComponentTesla);

			addAmmo(list, item, name+"/A1 Sprenggranate",
					IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.CONTACT, IIContent.ammoComponentHMX, IIContent.ammoComponentTracerPowder);
			addAmmo(list, item, name+"/A7 Sprenggranate",
					IIContent.ammoCoreIron, CoreType.CLUSTER, FuseType.CONTACT, IIContent.ammoComponentHMX, IIContent.ammoComponentTracerPowder);
			addAmmo(list, item, name+"/A2 Phosphorgranate",
					IIContent.ammoCoreIron, CoreType.CANISTER, FuseType.CONTACT, IIContent.ammoComponentWhitePhosphorus, IIContent.ammoComponentTracerPowder);
			addAmmo(list, item, name+"/A16 \"Geburtstagsgranate\"",
					IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.CONTACT, IIContent.ammoComponentNuke, IIContent.ammoComponentTracerPowder);
		});

		//Missiles
		items.clear();
		items.put("AR6", IIContent.itemAmmoRocketLight);
		items.put("AR10", IIContent.itemAmmoRocketHeavy);
		items.put("AR6F", IIContent.itemAmmoGuidedMissile);


		items.forEach((name, item) -> {
			tabNewLine(list);
			addAmmo(list, item, name+"/P2 Panzerabwehrrakete",
					IIContent.ammoCoreSteel, CoreType.PIERCING, FuseType.CONTACT, IIContent.ammoComponentTNT);
			addAmmo(list, item, name+"/P3 Panzerabwehrrakete",
					IIContent.ammoCoreTungsten, CoreType.PIERCING, FuseType.CONTACT, IIContent.ammoComponentTNT);
			addAmmo(list, item, name+"/P4 Panzerabwehrrakete",
					IIContent.ammoCoreUranium, CoreType.PIERCING, FuseType.CONTACT, IIContent.ammoComponentTNT);

			addAmmo(list, item, name+"/P3 Hochladungsrakete",
					IIContent.ammoCoreCopper, CoreType.SHAPED, FuseType.CONTACT, IIContent.ammoComponentRDX);
			addAmmo(list, item, name+"/P4 Hochladungsrakete",
					IIContent.ammoCoreBrass, CoreType.SHAPED_SABOT, FuseType.CONTACT, IIContent.ammoComponentHMX);

			//Some trivia, during the germans actually called them the nonsensical name "Flakraketen"
			addAmmo(list, item, name+"/W1 Flugabwehrrakete",
					IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.PROXIMITY,
					IIContent.ammoComponentWhitePhosphorus, AmmoRegistry.getComponent("shrapnel_steel"), IIContent.ammoComponentTracerPowder);

			addAmmo(list, item, name+"/W4 Napalmrakete",
					IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.PROXIMITY, getFluidComponent("napalm"), IIContent.ammoComponentTracerPowder);
			addAmmo(list, item, name+"/W6 Elektrobrandrakete",
					IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.PROXIMITY, IIContent.ammoComponentTesla, IIContent.ammoComponentWhitePhosphorus);

			addAmmo(list, item, name+"/A1 Sprengrakete",
					IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.CONTACT, IIContent.ammoComponentHMX);
			addAmmo(list, item, name+"/A7 Sprengrakete",
					IIContent.ammoCoreIron, CoreType.CLUSTER, FuseType.CONTACT, IIContent.ammoComponentHMX);
			addAmmo(list, item, name+"/A3 Brandrakete",
					IIContent.ammoCoreIron, CoreType.CANISTER, FuseType.CONTACT, IIContent.ammoComponentWhitePhosphorus);

		});

		//Grenades
		tabNewLine(list);
		addAmmo(list, IIContent.itemGrenade, "G5/A1 Stielhandgranate",
				IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.TIMED, IIContent.ammoComponentTNT);
		addAmmo(list, IIContent.itemGrenade, "G5/A7 Phosphorhandgranate",
				IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.TIMED, IIContent.ammoComponentWhitePhosphorus);
		addAmmo(list, IIContent.itemGrenade, "G3/A3 Sprenghandgranate",
				IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.TIMED, IIContent.ammoComponentRDX);
		addAmmo(list, IIContent.itemGrenade, "G4/A4 Sprenghandgranate",
				IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.TIMED, IIContent.ammoComponentHMX);
		addAmmo(list, IIContent.itemGrenade, "G5/A11 Chemhandgranate",
				IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.TIMED, getFluidComponent("chlorine"));
		addAmmo(list, IIContent.itemGrenade, "G5/A5 Schockhandgranate",
				IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.TIMED, IIContent.ammoComponentFirework);

		//Revolver

		//Magazine fed weapons
		items.clear();
		items.put("AM1P", IIContent.itemAmmoSubmachinegun);
		items.put("AM1M", IIContent.itemAmmoAssaultRifle);
		items.put("AM2", IIContent.itemAmmoMachinegun);
		items.put("AM3", IIContent.itemAmmoAutocannon);
		items.put("E2", IIContent.itemAmmoMortar);

		items.forEach((name, item) -> {
			tabNewLine(list);

			//Regular Ammo
			ItemStack stackP1 = addAmmo(list, item, name+"/P1 Rundkopfgeschoss",
					IIContent.ammoCoreLead, CoreType.SOFTPOINT, FuseType.CONTACT);
			ItemStack stackP2 = addAmmo(list, item, name+"/P2 Spitzkopfgeschoss",
					IIContent.ammoCoreSteel, CoreType.PIERCING, FuseType.CONTACT);
			ItemStack stackP5 = addAmmo(list, item, name+"/P5 Phosphorpatrone",
					IIContent.ammoCoreBrass, CoreType.PIERCING, FuseType.CONTACT, IIContent.ammoComponentHMX);
			ItemStack stackW1 = addAmmo(list, item, name+"/W1 Flakpatrone",
					IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.PROXIMITY,
					IIContent.ammoComponentWhitePhosphorus, AmmoRegistry.getComponent("shrapnel_steel"), IIContent.ammoComponentTracerPowder);
			ItemStack stackW6 = addAmmo(list, item, name+"/W6 Elektrobrandpatrone",
					IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.PROXIMITY, IIContent.ammoComponentTesla, IIContent.ammoComponentWhitePhosphorus);
			ItemStack stackA1 = addAmmo(list, item, name+"/A1 Sprengpatrone",
					IIContent.ammoCoreBrass, CoreType.SOFTPOINT, FuseType.CONTACT, IIContent.ammoComponentHMX);

			//Color tracer bullets
			ItemStack stackM1 = addAmmo(list, item, name+"/P14 Blaumarkierungpatrone",
					IIContent.ammoCoreIron, CoreType.PIERCING, FuseType.CONTACT, IIContent.ammoComponentTracerPowder);
			item.setComponentNBT(stackM1, EasyNBT.parseNBT("{colour: %s}", IIColor.MC_DARK_BLUE));
			item.setPaintColor(stackM1, IIColor.MC_DARK_BLUE);

			ItemStack stackM2 = addAmmo(list, item, name+"/P15 Grünmarkierungpatrone",
					IIContent.ammoCoreIron, CoreType.PIERCING, FuseType.CONTACT, IIContent.ammoComponentTracerPowder);
			item.setComponentNBT(stackM2, EasyNBT.parseNBT("{colour: %s}", IIColor.MC_DARK_GREEN));
			item.setPaintColor(stackM2, IIColor.MC_DARK_GREEN);

			ItemStack stackM3 = addAmmo(list, item, name+"/P16 Rotmarkierungpatrone",
					IIContent.ammoCoreIron, CoreType.PIERCING, FuseType.CONTACT, IIContent.ammoComponentTracerPowder);
			item.setComponentNBT(stackM3, EasyNBT.parseNBT("{colour: %s}", IIColor.MC_DARK_RED));
			item.setPaintColor(stackM3, IIColor.MC_DARK_RED);

			ItemStack stackM4 = addAmmo(list, item, name+"/P17 Gelbmarkierungpatrone",
					IIContent.ammoCoreIron, CoreType.PIERCING, FuseType.CONTACT, IIContent.ammoComponentTracerPowder);
			item.setComponentNBT(stackM4, EasyNBT.parseNBT("{colour: %s}", IIColor.MC_GOLD));
			item.setPaintColor(stackM4, IIColor.MC_GOLD);

			//Magazines
			tabNewLine(list);
			for(Magazines magazine : Magazines.values())
				if(magazine.ammo==item)
				{
					list.add(getMagazine(magazine, name+"/SP1P2 Magazine", stackP1, stackP2));
					list.add(getMagazine(magazine, name+"/SP1P5W6 Magazine", stackP1, stackP5, stackW6));
					list.add(getMagazine(magazine, name+"/SW1W6 Magazine", stackW1, stackA1));
				}

			//Magazines
			Arrays.stream(Magazines.values())
					.filter(m -> m.ammo==item)
					.map(m -> getColorMagazine(m, name+"/SP14-17 Markierungmagazine", IIColor.MC_DARK_BLUE, IIColor.MC_DARK_GREEN, IIColor.MC_DARK_RED, IIColor.MC_GOLD))
					.forEach(list::add);
		});

		items.clear();
		items.put("D8E%sfunkladung", ((ItemBlockMineBase)IIContent.blockRadioExplosives.itemBlock));
		items.put("D8T%stellermine", ((ItemBlockMineBase)IIContent.blockTellermine.itemBlock));
		items.put("D8S%sspringmine", ((ItemBlockMineBase)IIContent.blockTripmine.itemBlock));
		items.put("D8M%sseemine", IIContent.itemNavalMine);
		items.forEach((name, item) -> {
			tabNewLine(list);
			addAmmo(list, item, String.format(name, "/P1 Spreng"),
					IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.CONTACT, IIContent.ammoComponentTNT);
			addAmmo(list, item, String.format(name, "/P2 Phosphor"),
					IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.CONTACT, IIContent.ammoComponentWhitePhosphorus);
			addAmmo(list, item, String.format(name, "/P3 Elektro"),
					IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.CONTACT, IIContent.ammoComponentTesla);
			addAmmo(list, item, String.format(name, "/P4 Nuklear"),
					IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.CONTACT, IIContent.ammoComponentNuke);
		});
	}

	/**
	 * Adds empty slots to the list to fill the entire row
	 *
	 * @param list The list to add to
	 */
	private static void tabNewLine(NonNullList<ItemStack> list)
	{
		int missing = (9-list.size()%9)%9;
		for(int i = 0; i < missing; i++)
			list.add(ItemStack.EMPTY);
	}

	@Nullable
	private AmmoComponent getFluidComponent(String fluidName)
	{
		AmmoComponent component = AmmoRegistry.getComponent("fluid_"+fluidName);
		if(component!=null)
			return component;
		return AmmoRegistry.getComponent("gas_"+fluidName);
	}

	private ItemStack addAmmo(NonNullList<ItemStack> list, IAmmoTypeItem<?, ?> ammo, String displayName,
							  AmmoCore core, CoreType type, FuseType fuse,
							  AmmoComponent... components)
	{
		//check if core and type are allowed
		if(Arrays.stream(ammo.getAllowedCoreTypes()).noneMatch(t -> t==type))
			return ItemStack.EMPTY;
		if(Arrays.stream(ammo.getAllowedFuseTypes()).noneMatch(t -> t==fuse))
			return ItemStack.EMPTY;
		if(!Arrays.stream(components).allMatch(c -> c!=null&&c.matchesBullet(ammo)))
			return ItemStack.EMPTY;

		//Get ammo stack and add it to the menu
		ItemStack stack = ammo.getAmmoStack(core, type, fuse, components);
		stack.setStackDisplayName(displayName);

		//set proximity fuse
		if(fuse==FuseType.PROXIMITY)
			ammo.setFuseParameter(stack, Math.max(1, ammo.getCaliber()/2));
		else if(fuse==FuseType.TIMED)
			ammo.setFuseParameter(stack, Math.max(1, ammo.getCaliber()/2*80));

		list.add(stack);
		return stack;
	}

	private static ItemStack getMagazine(Magazines magazine, String name, ItemStack... bullets)
	{
		return IIContent.itemBulletMagazine.getMagazine(magazine, bullets).setStackDisplayName(name);
	}

	private static ItemStack getColorMagazine(Magazines magazine, String name, IIColor... colors)
	{
		ItemStack[] bullets = new ItemStack[colors.length];
		for(int i = 0; i < colors.length; i++)
		{
			//get bullet stack
			ItemStack stack = magazine.ammo.getAmmoStack(IIContent.ammoCoreIron, CoreType.PIERCING, FuseType.CONTACT, IIContent.ammoComponentTracerPowder)
					.setStackDisplayName(getGermanColorName(colors[i])+"markierungspatrone");
			//set tracer color
			magazine.ammo.setComponentNBT(stack, EasyNBT.parseNBT("{colour: %s}", colors[i]));
			//set paint color
			magazine.ammo.setPaintColor(stack, colors[i]);
			bullets[i] = stack;
		}
		return getMagazine(magazine, name, bullets);
	}

	/**
	 * Deutsche Qualität
	 */
	private static String getGermanColorName(IIColor color)
	{
		switch(color.getDyeColor())
		{
			case WHITE:
				return "Weiß";
			case ORANGE:
				return "Orange";
			case MAGENTA:
				return "Magenta";
			case LIGHT_BLUE:
				return "Hellblau";
			case YELLOW:
				return "Gelb";
			case LIME:
				return "Lime";
			case PINK:
				return "Rosa";
			case GRAY:
				return "Grau";
			case SILVER:
				return "Silber";
			case CYAN:
				return "Cyan";
			case PURPLE:
				return "Purpur";
			case BLUE:
				return "Blau";
			case BROWN:
				return "Braun";
			case GREEN:
				return "Grün";
			case RED:
				return "Rot";
			default:
			case BLACK:
				return "Schwarz";
		}
	}

	@SideOnly(Side.CLIENT)
	public void setHoveringText(GuiWidgetAustralianTabs tabs)
	{
		this.hovered = tabs;
	}
}
