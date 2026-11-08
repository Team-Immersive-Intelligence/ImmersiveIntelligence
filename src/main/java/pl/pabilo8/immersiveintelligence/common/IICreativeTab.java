package pl.pabilo8.immersiveintelligence.common;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
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
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.arithmetic.DataOperationAdd;
import pl.pabilo8.immersiveintelligence.api.data.operations.arithmetic.DataOperationMultiply;
import pl.pabilo8.immersiveintelligence.api.data.operations.fluidstack.DataOperationFluidGetAmount;
import pl.pabilo8.immersiveintelligence.api.data.operations.fluidstack.DataOperationFluidGetID;
import pl.pabilo8.immersiveintelligence.api.data.operations.itemstack.DataOperationItemStackGetCount;
import pl.pabilo8.immersiveintelligence.api.data.operations.itemstack.DataOperationItemStackGetItemID;
import pl.pabilo8.immersiveintelligence.api.data.operations.itemstack.DataOperationItemStackGetMeta;
import pl.pabilo8.immersiveintelligence.api.data.operations.text.DataOperationStringLength;
import pl.pabilo8.immersiveintelligence.api.data.operations.vector.DataOperationVectorLength;
import pl.pabilo8.immersiveintelligence.api.data.types.*;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
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
 * @author Pabilo8 (pabilo@iiteam.net)
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

	private static String creativeAmmoName(String german, String english)
	{
		return IIConfig.Ammunition.useGermanCreativeAmmoNames?german: english;
	}

	private static ItemStack getMagazine(Magazines magazine, String name, ItemStack... bullets)
	{
		//Exclude unsupported ammunition before the pattern fills the magazine.
		bullets = Arrays.stream(bullets).filter(bullet -> !bullet.isEmpty()).toArray(ItemStack[]::new);
		return IIContent.itemBulletMagazine.getMagazine(magazine, bullets).setStackDisplayName(name);
	}

	private static ItemStack getColorMagazine(Magazines magazine, String name, IIColor... colors)
	{
		ItemStack[] bullets = new ItemStack[colors.length];
		for(int i = 0; i < colors.length; i++)
		{
			//get bullet stack
			ItemStack stack = magazine.ammo.getAmmoStack(IIContent.ammoCoreIron, CoreType.PIERCING, FuseType.CONTACT, IIContent.ammoComponentTracerPowder)
					.setStackDisplayName(getTracerBulletName(colors[i]));
			//set tracer color
			magazine.ammo.setComponentNBT(stack, EasyNBT.parseNBT("{colour: %s}", colors[i]));
			//set paint color
			magazine.ammo.setPaintColor(stack, colors[i]);
			bullets[i] = stack;
		}
		return getMagazine(magazine, name, bullets);
	}

	private static String getTracerBulletName(IIColor color)
	{
		if(IIConfig.Ammunition.useGermanCreativeAmmoNames)
			return getGermanColorName(color)+"markierungspatrone";

		return switch(color.getDyeColor())
		{
			case WHITE -> "White Tracer";
			case ORANGE -> "Orange Tracer";
			case MAGENTA -> "Magenta Tracer";
			case LIGHT_BLUE -> "Light Blue Tracer";
			case YELLOW -> "Yellow Tracer";
			case LIME -> "Lime Tracer";
			case PINK -> "Pink Tracer";
			case GRAY -> "Grey Tracer";
			case SILVER -> "Light Grey Tracer";
			case CYAN -> "Cyan Tracer";
			case PURPLE -> "Purple Tracer";
			case BLUE -> "Blue Tracer";
			case BROWN -> "Brown Tracer";
			case GREEN -> "Green Tracer";
			case RED -> "Red Tracer";
			default -> "Black Tracer";
		};
	}

	/**
	 * Deutsche Qualität
	 */
	private static String getGermanColorName(IIColor color)
	{
		return switch(color.getDyeColor())
		{
			case WHITE -> "Weiß";
			case ORANGE -> "Orange";
			case MAGENTA -> "Magenta";
			case LIGHT_BLUE -> "Hellblau";
			case YELLOW -> "Gelb";
			case LIME -> "Lime";
			case PINK -> "Rosa";
			case GRAY -> "Grau";
			case SILVER -> "Silber";
			case CYAN -> "Cyan";
			case PURPLE -> "Purpur";
			case BLUE -> "Blau";
			case BROWN -> "Braun";
			case GREEN -> "Grün";
			case RED -> "Rot";
			default -> "Schwarz";
		};
	}

	@Override
	@Nonnull
	public ItemStack getTabIconItem()
	{
		return IIContent.blockMetalDecoration.getStack(IIBlockTypes_MetalDecoration.COIL_DATA, 1);
	}

	@Nonnull
	@Override
	public ResourceLocation getBackgroundImage()
	{
		if(!IIConfig.australianCreativeTabs)
			return super.getBackgroundImage();
		return selectedCategory.getCreativeTabTexture();
	}

	@Nonnull
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
			if(item instanceof ItemBlockIIBase itemBlock)
			{
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
			case ELECTRONICS:
				tabNewLine(list);
				addExamplePunchtapes(list);
				tabNewLine(list);
				addExampleCircuits(list);
				break;
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

	private void addExamplePunchtapes(NonNullList<ItemStack> list)
	{
		ArrayList<String> propagandaMessages = new ArrayList<>();
		propagandaMessages.add("Glory to Engineers Cause!");
		propagandaMessages.add("Engineers Prevail!");
		propagandaMessages.add("Engineers of All Addons - Unite!");
		propagandaMessages.add("Steel is Certain.");
		propagandaMessages.add("They build sailboats, we're building Battleships!");
		propagandaMessages.add("Strong Industry - Our Key to Victory!");
		propagandaMessages.add("Stronger Heart, Stronger Steel.");
		propagandaMessages.add("Might through Science and Industry.");

		//Example Data Packet
		addPunchtape(list, "Example Punchtape", "Example Punchtape that has a few random value variables.",
				new DataPacket()
						.with('a', new DataTypeInteger(Math.round(Utils.RAND.nextFloat()*1000)))
						.with('b', new DataTypeInteger(Math.round(Utils.RAND.nextFloat()*10)))
						.with('d', new DataTypeBoolean(Utils.RAND.nextBoolean()))
						.with('t', new DataTypeString(propagandaMessages.get(Math.round(Utils.RAND.nextFloat()*(propagandaMessages.size()-1)))))
						.with('u', new DataTypeString("This is a non-random test string!"))
						.with('r', new DataTypeArray(
								new DataTypeInteger(5),
								new DataTypeFloat(10),
								new DataTypeInteger(25),
								new DataTypeFloat(100)
						))
						.with('v', new DataTypeVector(new Vec3d(
								Math.round(Utils.RAND.nextFloat()*100)-50,
								Math.round(Utils.RAND.nextFloat()*100)-50,
								Math.round(Utils.RAND.nextFloat()*100)-50
						)))
						.with('s', new DataTypeItemStack(
								list.stream().filter(itemStack -> !itemStack.isEmpty()).findAny().orElse(
										new ItemStack(IIContent.itemRifle)
								)
						))
						.with('f', new DataTypeFluidStack(new FluidStack(
								FluidRegistry.getBucketFluids().stream()
										.skip((int)(Utils.RAND.nextFloat()*FluidRegistry.getBucketFluids().size()))
										.findFirst().orElse(FluidRegistry.WATER),
								Utils.RAND.nextInt(5000)
						)))
		);

		//Artillery Howitzer
		addPunchtape(list, "Artillery Howitzer - Fire", "Example Punchtape that makes the Artillery Howitzer fire at a random yaw and pitch.",
				new DataPacket()
						.with('c', new DataTypeString("fire"))
						.with('y', new DataTypeFloat(Math.round(Utils.RAND.nextFloat()*360)-180))
						.with('p', new DataTypeFloat(Math.round(Utils.RAND.nextFloat()*90)))
		);
		addPunchtape(list, "Artillery Howitzer - Aim", "Example Punchtape that makes the Artillery Howitzer aim at a specific yaw and pitch.",
				new DataPacket()
						.with('c', new DataTypeString("aim"))
						.with('y', new DataTypeFloat(Math.round(Utils.RAND.nextFloat()*360)-180))
						.with('p', new DataTypeFloat(Math.round(Utils.RAND.nextFloat()*90)))
		);
		addPunchtape(list, "Artillery Howitzer - Load", "Example Punchtape that makes the Artillery Howitzer load next shell from the ammunition conveyor.",
				new DataPacket()
						.with('c', new DataTypeString("load"))
		);
		addPunchtape(list, "Artillery Howitzer - Unload", "Example Punchtape that makes the Artillery Howitzer unload its current shell.",
				new DataPacket()
						.with('c', new DataTypeString("unload"))
		);
		addPunchtape(list, "Artillery Howitzer - Stop", "Example Punchtape that makes the Artillery Howitzer stop its current action.",
				new DataPacket()
						.with('c', new DataTypeString("stop"))
		);

		//Emplacement
		addPunchtape(list, "Emplacement - Fire", "Example Punchtape that makes the Emplacement fire at a random yaw and pitch.",
				new DataPacket()
						.with('c', new DataTypeString("fire"))
						.with('y', new DataTypeFloat(Math.round(Utils.RAND.nextFloat()*360)-180))
						.with('p', new DataTypeFloat(Math.round(Utils.RAND.nextFloat()*90)))
		);
		addPunchtape(list, "Emplacement - Fire at XYZ Position", "Example Punchtape that makes the Emplacement fire at a specific XYZ position offset from its center.",
				new DataPacket()
						.with('c', new DataTypeString("fire"))
						.with('x', new DataTypeFloat(Math.round(Utils.RAND.nextFloat()*100)-50))
						.with('y', new DataTypeFloat(Math.round(Utils.RAND.nextFloat()*100)-50))
						.with('z', new DataTypeFloat(Math.round(Utils.RAND.nextFloat()*100)-50))
		);

		addPunchtape(list, "Emplacement - Hide for Repairs", "Example Punchtape that makes the Emplacement hide for repairs.",
				new DataPacket()
						.with('c', new DataTypeString("hide"))
		);
		addPunchtape(list, "Emplacement - Show", "Example Punchtape that makes the Emplacement show itself.",
				new DataPacket()
						.with('c', new DataTypeString("show"))
		);

		//Inserter
		int maxItems = Utils.RAND.nextInt(96), itemsPerTurn = Utils.RAND.nextInt(5)+1;
		addPunchtape(list, "Inserter", String.format("Example Punchtape that makes the Inserter insert %s items from one inventory to another, grabbing %s per turn.",
						maxItems, itemsPerTurn),
				new DataPacket()
						.with('c', new DataTypeString("add"))
						.with('a', new DataTypeString("item"))
						.with('e', new DataTypeInteger(maxItems))
						.with('t', new DataTypeInteger(itemsPerTurn))
		);

		maxItems = Utils.RAND.nextInt(96);
		itemsPerTurn = Utils.RAND.nextInt(5)+1;
		addPunchtape(list, "Inserter - Clay Balls Only", "Example Punchtape that makes the Inserter insert only Clay Balls from one inventory to another, grabbing 1 per turn.",
				new DataPacket()
						.with('c', new DataTypeString("add"))
						.with('a', new DataTypeString("item"))
						.with('e', new DataTypeInteger(maxItems))
						.with('t', new DataTypeInteger(itemsPerTurn))
						.with('s', new DataTypeItemStack(new ItemStack(Items.CLAY_BALL)))
		);

		//Printing Press
		addPunchtape(list, "Printing Press - Print Text", "Example Punchtape that makes the Printing Press print a random amount of pages filled with engineer propaganda.",
				new DataPacket()
						.with('m', new DataTypeString("text"))
						.with('a', new DataTypeInteger(Math.round(Utils.RAND.nextFloat()*9)+1))
						.with('t', new DataTypeString(propagandaMessages.get(Math.round(Utils.RAND.nextFloat()*(propagandaMessages.size()-1)))))
		);

		//Packer
		addPunchtape(list, "Packer - Pack Items", "Example Punchtape that makes the Packer pack a random amount of items into a container.",
				new DataPacket()
						.with('c', new DataTypeString("add"))
						.with('a', new DataTypeString("item"))
						.with('m', new DataTypeString("amount"))
						.with('e', new DataTypeInteger(Math.round(Utils.RAND.nextFloat()*10)+1))
		);
	}

	private void addExampleCircuits(NonNullList<ItemStack> list)
	{
		//Add A and B
		addFunctionalCircuit(list, "C=A+B Circuit", "Example Circuit that adds two numbers together.",
				new DataPacket()
						.with('c', new DataTypeExpression(new DataType[]{
								new DataTypeAccessor('a'),
								new DataTypeAccessor('b')
						}, new DataOperationAdd(), ' '))
		);

		//Multiply A by B if D
		addFunctionalCircuit(list, "IF D => K=A*B Circuit", "Example Circuit that multiplies two numbers together if variable D of type boolean equals true.",
				new DataPacket()
						.with('k', new DataTypeExpression(new DataType[]{
								new DataTypeAccessor('a'),
								new DataTypeAccessor('b')
						}, new DataOperationMultiply(), 'd'))
		);

		//Character Count of String U
		addFunctionalCircuit(list, "Character Count Circuit", "Example Circuit that counts the amount of characters in a string.",
				new DataPacket()
						.with('c', new DataTypeExpression(new DataType[]{
								new DataTypeAccessor('u')
						}, new DataOperationStringLength(), ' '))
		);

		//Length of Vector V
		addFunctionalCircuit(list, "Vector Length Circuit", "Example Circuit that calculates the length of a vector.",
				new DataPacket()
						.with('c', new DataTypeExpression(new DataType[]{
								new DataTypeAccessor('v')
						}, new DataOperationVectorLength(), ' '))
		);

		//Count, ID and Damage of ItemStack S
		addFunctionalCircuit(list, "ItemStack Properties Circuit", "Example Circuit that returns the count (A), ID (B) and damage/metadata (C) of an ItemStack.",
				new DataPacket()
						.with('a', new DataTypeExpression(new DataType[]{
								new DataTypeAccessor('s')
						}, new DataOperationItemStackGetCount(), ' '))
						.with('b', new DataTypeExpression(new DataType[]{
								new DataTypeAccessor('s')
						}, new DataOperationItemStackGetItemID(), ' '))
						.with('c', new DataTypeExpression(new DataType[]{
								new DataTypeAccessor('s')
						}, new DataOperationItemStackGetMeta(), ' '))
		);

		//Count and ID of FluidStack F
		addFunctionalCircuit(list, "FluidStack Properties Circuit", "Example Circuit that returns the amount (A) and ID (B) of a FluidStack.",
				new DataPacket()
						.with('a', new DataTypeExpression(new DataType[]{
								new DataTypeAccessor('f')
						}, new DataOperationFluidGetAmount(), ' '))
						.with('b', new DataTypeExpression(new DataType[]{
								new DataTypeAccessor('f')
						}, new DataOperationFluidGetID(), ' '))
		);
	}

	private void addPunchtape(NonNullList<ItemStack> list, String name, String description, DataPacket packet)
	{
		ItemStack stack = IIContent.itemPunchtape.getStack(1);
		stack.setStackDisplayName(name);
		ItemNBTHelper.setLore(stack, description);
		IIContent.itemPunchtape.writeDataToItem(stack, packet);
		list.add(stack);
	}

	private void addFunctionalCircuit(NonNullList<ItemStack> list, String name, String description, DataPacket packet)
	{
		ItemStack stack = IIContent.itemCircuit.getStack(1);
		stack.setStackDisplayName(name);
		ItemNBTHelper.setLore(stack, description);
		IIContent.itemCircuit.writeDataToItem(stack, packet);
		list.add(stack);
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
		items.put(creativeAmmoName("AH8", "HA8"), IIContent.itemAmmoHeavyArtillery);
		items.put(creativeAmmoName("AH6L", "MA6"), IIContent.itemAmmoMediumArtillery);
		items.put(creativeAmmoName("AH6K", "LA6"), IIContent.itemAmmoLightArtillery);
		items.put(creativeAmmoName("AG4", "LG4"), IIContent.itemAmmoLightGun);
		items.put(creativeAmmoName("G4", "RG4"), IIContent.itemRailgunGrenade);
		items.put(creativeAmmoName("AH6M", "M6"), IIContent.itemAmmoMortar);

		//Guns and Howitzers
		items.forEach((name, item) -> {
			tabNewLine(list);
			addAmmo(list, item, name+creativeAmmoName("/P1 Panzergranate", "/APHE-L Shell"),
					IIContent.ammoCoreLead, CoreType.PIERCING, FuseType.CONTACT, IIContent.ammoComponentTNT);
			addAmmo(list, item, name+creativeAmmoName("/P2 Panzergranate", "/APHE-S Shell"),
					IIContent.ammoCoreSteel, CoreType.PIERCING, FuseType.CONTACT, IIContent.ammoComponentTNT);
			addAmmo(list, item, name+creativeAmmoName("/P3 Panzergranate", "/APHE-W Shell"),
					IIContent.ammoCoreTungsten, CoreType.PIERCING, FuseType.CONTACT, IIContent.ammoComponentTNT);

			addAmmo(list, item, name+creativeAmmoName("/P5 Hochladungsgranate", "/HEAT-T Shell"),
					IIContent.ammoCoreCopper, CoreType.SHAPED, FuseType.CONTACT, IIContent.ammoComponentRDX, IIContent.ammoComponentTracerPowder);
			addAmmo(list, item, name+creativeAmmoName("/P6 Hochladungsgranate", "/HEAT-DS Shell"),
					IIContent.ammoCoreBrass, CoreType.SHAPED_SABOT, FuseType.CONTACT, IIContent.ammoComponentHMX);

			addAmmo(list, item, name+creativeAmmoName("/P7 KE-Geschoss", "/APDS-W Sabot"),
					IIContent.ammoCoreTungsten, CoreType.PIERCING_SABOT, FuseType.CONTACT);
			addAmmo(list, item, name+creativeAmmoName("/P8 KE-Geschoss", "/APDS-DU Sabot"),
					IIContent.ammoCoreUranium, CoreType.PIERCING_SABOT, FuseType.CONTACT);

			addAmmo(list, item, name+creativeAmmoName("/W1 Flakgranate", "/AA-T Shell"),
					IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.PROXIMITY,
					IIContent.ammoComponentWhitePhosphorus, AmmoRegistry.getComponent("shrapnel_steel"), IIContent.ammoComponentTracerPowder);
			addAmmo(list, item, name+creativeAmmoName("/W4 Napalmgranate", "/NAP-T Shell"),
					IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.PROXIMITY, getFluidComponent("napalm"), IIContent.ammoComponentTracerPowder);
			addAmmo(list, item, name+creativeAmmoName("/W6 Elektrobrandgranate", "/EI Shell"),
					IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.PROXIMITY, IIContent.ammoComponentTesla, IIContent.ammoComponentWhitePhosphorus);
			addAmmo(list, item, name+creativeAmmoName("/W7 Industriestörunggranate", "/CHEM Shell"),
					IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.PROXIMITY,
					getFluidComponent("hydrofluoric_acid"), getFluidComponent("mustard_gas"), IIContent.ammoComponentTesla);

			addAmmo(list, item, name+creativeAmmoName("/A1 Sprenggranate", "/HE-T Shell"),
					IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.CONTACT, IIContent.ammoComponentHMX, IIContent.ammoComponentTracerPowder);
			addAmmo(list, item, name+creativeAmmoName("/A7 Sprenggranate", "/CL-HE-T Cluster Shell"),
					IIContent.ammoCoreIron, CoreType.CLUSTER, FuseType.CONTACT, IIContent.ammoComponentHMX, IIContent.ammoComponentTracerPowder);
			addAmmo(list, item, name+creativeAmmoName("/A2 Phosphorgranate", "/WP-T Shell"),
					IIContent.ammoCoreIron, CoreType.CANISTER, FuseType.CONTACT, IIContent.ammoComponentWhitePhosphorus, IIContent.ammoComponentTracerPowder);
			addAmmo(list, item, name+creativeAmmoName("/A16 \"Geburtstagsgranate\"", "/NUKE-T Shell"),
					IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.CONTACT, IIContent.ammoComponentNuke, IIContent.ammoComponentTracerPowder);
		});

		//Missiles
		items.clear();
		items.put(creativeAmmoName("AR6", "R6"), IIContent.itemAmmoRocketLight);
//		items.put("AR10", IIContent.itemAmmoRocketHeavy);
		items.put(creativeAmmoName("AR6F", "GM6"), IIContent.itemAmmoGuidedMissile);

		items.forEach((name, item) -> {
			tabNewLine(list);
			addAmmo(list, item, name+creativeAmmoName("/P2 Panzerabwehrrakete", "/APHE-S Rocket"),
					IIContent.ammoCoreSteel, CoreType.PIERCING, FuseType.CONTACT, IIContent.ammoComponentTNT);
			addAmmo(list, item, name+creativeAmmoName("/P3 Panzerabwehrrakete", "/APHE-W Rocket"),
					IIContent.ammoCoreTungsten, CoreType.PIERCING, FuseType.CONTACT, IIContent.ammoComponentTNT);
			addAmmo(list, item, name+creativeAmmoName("/P4 Panzerabwehrrakete", "/APHE-DU Rocket"),
					IIContent.ammoCoreUranium, CoreType.PIERCING, FuseType.CONTACT, IIContent.ammoComponentTNT);

			addAmmo(list, item, name+creativeAmmoName("/P3 Hochladungsrakete", "/HEAT Rocket"),
					IIContent.ammoCoreCopper, CoreType.SHAPED, FuseType.CONTACT, IIContent.ammoComponentRDX);
			addAmmo(list, item, name+creativeAmmoName("/P4 Hochladungsrakete", "/HEAT-DS Rocket"),
					IIContent.ammoCoreBrass, CoreType.SHAPED_SABOT, FuseType.CONTACT, IIContent.ammoComponentHMX);

			//Some trivia: during the 2nd World War, Germans actually called them the nonsensical name "Flakraketen"
			addAmmo(list, item, name+creativeAmmoName("/W1 Flugabwehrrakete", "/AA-T Rocket"),
					IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.PROXIMITY,
					IIContent.ammoComponentWhitePhosphorus, AmmoRegistry.getComponent("shrapnel_steel"), IIContent.ammoComponentTracerPowder);

			addAmmo(list, item, name+creativeAmmoName("/W4 Napalmrakete", "/NAP-T Rocket"),
					IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.PROXIMITY, getFluidComponent("napalm"), IIContent.ammoComponentTracerPowder);
			addAmmo(list, item, name+creativeAmmoName("/W6 Elektrobrandrakete", "/EI Rocket"),
					IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.PROXIMITY, IIContent.ammoComponentTesla, IIContent.ammoComponentWhitePhosphorus);

			addAmmo(list, item, name+creativeAmmoName("/A1 Sprengrakete", "/HE Rocket"),
					IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.CONTACT, IIContent.ammoComponentHMX);
			addAmmo(list, item, name+creativeAmmoName("/A7 Sprengrakete", "/CL-HE Rocket"),
					IIContent.ammoCoreIron, CoreType.CLUSTER, FuseType.CONTACT, IIContent.ammoComponentHMX);
			addAmmo(list, item, name+creativeAmmoName("/A3 Brandrakete", "/WP Rocket"),
					IIContent.ammoCoreIron, CoreType.CANISTER, FuseType.CONTACT, IIContent.ammoComponentWhitePhosphorus);

		});

		//Grenades
		tabNewLine(list);
		addAmmo(list, IIContent.itemGrenade, creativeAmmoName("G5/A1 Stielhandgranate", "HG5/HE Grenade"),
				IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.TIMED, IIContent.ammoComponentTNT);
		addAmmo(list, IIContent.itemGrenade, creativeAmmoName("G5/A7 Phosphorhandgranate", "HG5/WP Grenade"),
				IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.TIMED, IIContent.ammoComponentWhitePhosphorus);
		addAmmo(list, IIContent.itemGrenade, creativeAmmoName("G3/A3 Sprenghandgranate", "HG3/HE-RDX Grenade"),
				IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.TIMED, IIContent.ammoComponentRDX);
		addAmmo(list, IIContent.itemGrenade, creativeAmmoName("G4/A4 Sprenghandgranate", "HG4/HE-HMX Grenade"),
				IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.TIMED, IIContent.ammoComponentHMX);
		addAmmo(list, IIContent.itemGrenade, creativeAmmoName("G5/A11 Chemhandgranate", "HG5/CHEM Grenade"),
				IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.TIMED, getFluidComponent("chlorine"));
		addAmmo(list, IIContent.itemGrenade, creativeAmmoName("G5/A5 Schockhandgranate", "HG5/STUN Grenade"),
				IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.TIMED, IIContent.ammoComponentFirework);

		//Revolver

		//Magazine fed weapons
		items.clear();
		items.put(creativeAmmoName("AM1P", "SMG1"), IIContent.itemAmmoSubmachinegun);
		items.put(creativeAmmoName("AM1M", "AR1"), IIContent.itemAmmoAssaultRifle);
		items.put(creativeAmmoName("AM2", "MG2"), IIContent.itemAmmoMachinegun);
		items.put(creativeAmmoName("AM3", "AC3"), IIContent.itemAmmoAutocannon);

		items.forEach((name, item) -> {
			tabNewLine(list);

			//Regular Ammo
			ItemStack stackP1 = addAmmo(list, item, name+creativeAmmoName("/P1 Rundkopfgeschoss", "/SP Soft Point"),
					IIContent.ammoCoreLead, CoreType.SOFTPOINT, FuseType.CONTACT);
			ItemStack stackP2 = addAmmo(list, item, name+creativeAmmoName("/P2 Spitzkopfgeschoss", "/AP Armor Piercing"),
					IIContent.ammoCoreSteel, CoreType.PIERCING, FuseType.CONTACT);
			ItemStack stackP5 = addAmmo(list, item, name+creativeAmmoName("/P5 Phosphorpatrone", "/API Incendiary"),
					IIContent.ammoCoreBrass, CoreType.PIERCING, FuseType.CONTACT, IIContent.ammoComponentWhitePhosphorus);
			ItemStack stackW1 = addAmmo(list, item, name+creativeAmmoName("/W1 Flakpatrone", "/AA-T Round"),
					IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.PROXIMITY,
					IIContent.ammoComponentWhitePhosphorus, AmmoRegistry.getComponent("shrapnel_steel"), IIContent.ammoComponentTracerPowder);
			ItemStack stackW6 = addAmmo(list, item, name+creativeAmmoName("/W6 Elektrobrandpatrone", "/EI Round"),
					IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.PROXIMITY, IIContent.ammoComponentTesla, IIContent.ammoComponentWhitePhosphorus);
			ItemStack stackA1 = addAmmo(list, item, name+creativeAmmoName("/A1 Sprengpatrone", "/HE Round"),
					IIContent.ammoCoreBrass, CoreType.SOFTPOINT, FuseType.CONTACT, IIContent.ammoComponentHMX);
			ItemStack stackT = addAmmo(list, item, name+creativeAmmoName("/T Gummigeschoss", "/RB Round"),
					IIContent.ammoCoreRubber, CoreType.SOFTPOINT, FuseType.CONTACT);

			//Color tracer bullets
			ItemStack stackM1 = addAmmo(list, item, name+creativeAmmoName("/P14 Blaumarkierungpatrone", "/TR-B Blue Tracer"),
					IIContent.ammoCoreIron, CoreType.PIERCING, FuseType.CONTACT, IIContent.ammoComponentTracerPowder);
			item.setComponentNBT(stackM1, EasyNBT.parseNBT("{colour: %s}", IIColor.MC_DARK_BLUE));
			item.setPaintColor(stackM1, IIColor.MC_DARK_BLUE);

			ItemStack stackM2 = addAmmo(list, item, name+creativeAmmoName("/P15 Grünmarkierungpatrone", "/TR-G Green Tracer"),
					IIContent.ammoCoreIron, CoreType.PIERCING, FuseType.CONTACT, IIContent.ammoComponentTracerPowder);
			item.setComponentNBT(stackM2, EasyNBT.parseNBT("{colour: %s}", IIColor.MC_DARK_GREEN));
			item.setPaintColor(stackM2, IIColor.MC_DARK_GREEN);

			ItemStack stackM3 = addAmmo(list, item, name+creativeAmmoName("/P16 Rotmarkierungpatrone", "/TR-R Red Tracer"),
					IIContent.ammoCoreIron, CoreType.PIERCING, FuseType.CONTACT, IIContent.ammoComponentTracerPowder);
			item.setComponentNBT(stackM3, EasyNBT.parseNBT("{colour: %s}", IIColor.MC_DARK_RED));
			item.setPaintColor(stackM3, IIColor.MC_DARK_RED);

			ItemStack stackM4 = addAmmo(list, item, name+creativeAmmoName("/P17 Gelbmarkierungpatrone", "/TR-Y Yellow Tracer"),
					IIContent.ammoCoreIron, CoreType.PIERCING, FuseType.CONTACT, IIContent.ammoComponentTracerPowder);
			item.setComponentNBT(stackM4, EasyNBT.parseNBT("{colour: %s}", IIColor.MC_GOLD));
			item.setPaintColor(stackM4, IIColor.MC_GOLD);

			//Magazines
			tabNewLine(list);
			for(Magazines magazine : Magazines.values())
				if(magazine.ammo==item)
				{
					list.add(getMagazine(magazine, name+creativeAmmoName("/SP1P2 Magazine", "/SP+AP Magazine"), stackP1, stackP2));
					list.add(getMagazine(magazine, name+creativeAmmoName("/SP1P5W6 Magazine", "/SP+API+EI Magazine"), stackP1, stackP5, stackW6));
					list.add(getMagazine(magazine, name+creativeAmmoName("/SW1W6 Magazine", "/AA+HE Magazine"), stackW1, stackA1));
					list.add(getMagazine(magazine, name+creativeAmmoName("/ST Magazine", "/RB Magazine"), stackT));
				}

			//Magazines
			Arrays.stream(Magazines.values())
					.filter(m -> m.ammo==item)
					.map(m -> getColorMagazine(m, name+creativeAmmoName("/SP14-17 Markierungmagazine", "/TR-BGRY Tracer Magazine"), IIColor.MC_DARK_BLUE, IIColor.MC_DARK_GREEN, IIColor.MC_DARK_RED, IIColor.MC_GOLD))
					.forEach(list::add);
		});

		items.clear();
		items.put(creativeAmmoName("D8E%sfunkladung", "RC8%sRadio Explosives"), ((ItemBlockMineBase)IIContent.blockRadioExplosives.itemBlock));
		items.put(creativeAmmoName("D8T%stellermine", "AT8%sLandmine"), ((ItemBlockMineBase)IIContent.blockTellermine.itemBlock));
		items.put(creativeAmmoName("D8S%sspringmine", "AP8%sTripmine"), ((ItemBlockMineBase)IIContent.blockTripmine.itemBlock));
		items.put(creativeAmmoName("D8M%sseemine", "NM8%sNaval Mine"), IIContent.itemNavalMine);
		items.forEach((name, item) -> {
			tabNewLine(list);
			addAmmo(list, item, String.format(name, creativeAmmoName("/P1 Spreng", "/HE ")),
					IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.CONTACT, IIContent.ammoComponentTNT);
			addAmmo(list, item, String.format(name, creativeAmmoName("/P2 Phosphor", "/WP ")),
					IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.CONTACT, IIContent.ammoComponentWhitePhosphorus);
			addAmmo(list, item, String.format(name, creativeAmmoName("/P3 Elektro", "/EL ")),
					IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.CONTACT, IIContent.ammoComponentTesla);
			addAmmo(list, item, String.format(name, creativeAmmoName("/P4 Nuklear", "/NUKE ")),
					IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.CONTACT, IIContent.ammoComponentNuke);
		});
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

	@SideOnly(Side.CLIENT)
	public void setHoveringText(GuiWidgetAustralianTabs tabs)
	{
		this.hovered = tabs;
	}
}
