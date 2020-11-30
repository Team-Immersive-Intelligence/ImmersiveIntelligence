package pl.pabilo8.immersiveintelligence.common;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.bullets.IBullet;
import pl.pabilo8.immersiveintelligence.api.crafting.PrecissionAssemblerRecipe;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalDecoration;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIBulletMagazine;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8
 * @since 2019-05-07
 */
public class IICreativeTab extends CreativeTabs
{
	public static List<Fluid> fluidBucketMap = new ArrayList<>();

	public IICreativeTab(String name)
	{
		super(name);
	}

	@Override
	public ItemStack getTabIconItem()
	{
		return new ItemStack(CommonProxy.block_metal_decoration, 1, IIBlockTypes_MetalDecoration.COIL_DATA.ordinal());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void displayAllRelevantItems(NonNullList<ItemStack> list)
	{
		super.displayAllRelevantItems(list);

		//addAssemblySchemes(list);
		addExampleBullets(list);

		for(Fluid fluid : fluidBucketMap)
			addFluidBucket(fluid, list);
	}

	public void addFluidBucket(Fluid fluid, NonNullList list)
	{
		UniversalBucket bucket = ForgeModContainer.getInstance().universalBucket;
		ItemStack stack = new ItemStack(bucket);
		FluidStack fs = new FluidStack(fluid, bucket.getCapacity());
		IFluidHandlerItem fluidHandler = new FluidBucketWrapper(stack);
		if(fluidHandler.fill(fs, true)==fs.amount)
		{
			list.add(fluidHandler.getContainer());
		}
	}

	public void addAssemblySchemes(NonNullList list)
	{
		for(PrecissionAssemblerRecipe recipe : PrecissionAssemblerRecipe.recipeList)
		{
			list.add(CommonProxy.item_assembly_scheme.getStackForRecipe(recipe));
		}
	}

	public void addExampleBullets(NonNullList list)
	{
		list.add(CommonProxy.item_ammo_artillery.getBulletWithParams("core_steel", "softpoint", "tnt"));
		list.add(CommonProxy.item_ammo_artillery.getBulletWithParams("core_steel", "softpoint", "rdx"));
		list.add(CommonProxy.item_ammo_artillery.getBulletWithParams("core_steel", "softpoint", "hmx"));

		list.add(CommonProxy.item_ammo_artillery.getBulletWithParams("core_tungsten", "piercing", "tnt"));
		list.add(CommonProxy.item_ammo_artillery.getBulletWithParams("core_tungsten", "piercing", "rdx"));
		list.add(CommonProxy.item_ammo_artillery.getBulletWithParams("core_tungsten", "piercing", "hmx"));

		list.add(CommonProxy.item_ammo_artillery.getBulletWithParams("core_brass", "canister", "tnt"));
		list.add(CommonProxy.item_ammo_artillery.getBulletWithParams("core_brass", "canister", "rdx"));
		list.add(CommonProxy.item_ammo_artillery.getBulletWithParams("core_brass", "canister", "hmx"));

		list.add(CommonProxy.item_ammo_artillery.getBulletWithParams("core_brass", "canister", "hmx", "white_phosphorus").setStackDisplayName("M65 White-Phosphorus Shell"));

		list.add(CommonProxy.item_grenade.getBulletWithParams("core_brass", "canister", "tnt").setStackDisplayName("Stielhandgranate mk.1"));
		list.add(CommonProxy.item_grenade.getBulletWithParams("core_brass", "canister", "white_phosphorus").setStackDisplayName("Phosphorgranate mk.1"));
		list.add(CommonProxy.item_grenade.getBulletWithParams("core_brass", "canister", "rdx").setStackDisplayName("Sprenghandgranate mk.1"));
		list.add(CommonProxy.item_grenade.getBulletWithParams("core_brass", "canister", "hmx").setStackDisplayName("Sprenghandgranate mk.2"));
		ItemStack grenade_firework = CommonProxy.item_grenade.getBulletWithParams("core_brass", "canister", "firework").setStackDisplayName("Feuerwerkhandgranate mk.1");
		try
		{
			((NBTTagList)ItemNBTHelper.getTag(grenade_firework).getTag("component_nbt")).set(0, JsonToNBT.getTagFromJson("{Explosion:{Type:0b,Colors:[I;3887386]}}"));
		} catch(NBTException e)
		{
			e.printStackTrace();
		}

		list.add(grenade_firework);

		list.add(CommonProxy.item_ammo_revolver.getBulletWithParams("core_brass", "canister", "white_phosphorus").setStackDisplayName("Flammpatrone mk.1"));
		list.add(CommonProxy.item_ammo_revolver.getBulletWithParams("core_tungsten", "piercing").setStackDisplayName("Wolframpatrone mk.1"));
		ItemStack bullet_tracer = CommonProxy.item_ammo_revolver.getBulletWithParams("core_brass", "canister", "tracer_powder").setStackDisplayName("Markierungspatrone mk.1");
		try
		{
			((NBTTagList)ItemNBTHelper.getTag(bullet_tracer).getTag("component_nbt")).set(0, JsonToNBT.getTagFromJson("{colour:3887386}"));
		} catch(NBTException e)
		{
			e.printStackTrace();
		}
		list.add(bullet_tracer);

		ItemStack bullet1 = CommonProxy.item_ammo_machinegun.getBulletWithParams("core_brass", "softpoint", "tnt").setStackDisplayName("Sprengpatrone mk.1");
		ItemStack bullet2 = CommonProxy.item_ammo_machinegun.getBulletWithParams("core_tungsten", "piercing", "shrapnel_tungsten").setStackDisplayName("Wolframpatrone mk.1");
		ItemStack bullet3 = CommonProxy.item_ammo_machinegun.getBulletWithParams("core_steel", "piercing").setStackDisplayName("Stahlpatrone mk.1");
		ItemStack bullet4 = CommonProxy.item_ammo_machinegun.getBulletWithParams("core_brass", "softpoint", "white_phosphorus").setStackDisplayName("Phosphorpatrone mk.1");
		list.add(bullet1);
		list.add(bullet2);
		list.add(bullet3);
		list.add(bullet4);
		list.add(ItemIIBulletMagazine.getMagazine("machinegun", bullet1, bullet2, bullet3, bullet4));

		ItemStack bullet5 = CommonProxy.item_ammo_machinegun.getBulletWithParams("core_uranium", "piercing", "shrapnel_uranium").setStackDisplayName("M1A1 Uranium Bullet");
		list.add(bullet5);
		list.add(ItemIIBulletMagazine.getMagazine("machinegun", bullet5, bullet5, bullet5, bullet5));


		list.add(addColorBulletMagazine(CommonProxy.item_ammo_machinegun, "machinegun", 65327, 16711680, 25343, 16772608));
		list.add(addColorBulletMagazine(CommonProxy.item_ammo_submachinegun, "submachinegun", 65327, 16711680, 25343, 16772608));

	}

	ItemStack addColorBulletMagazine(IBullet type, String magName, int... colors)
	{
		ArrayList<ItemStack> bullets = new ArrayList<>();
		for(int color : colors)
		{

			ItemStack stack = type.getBulletWithParams("core_brass", "softpoint", "tracer_powder").setStackDisplayName(getGermanColorName(color)+"markierungspatrone");
			NBTTagCompound tag = new NBTTagCompound();
			tag.setInteger("colour", color);
			type.setComponentNBT(stack, tag);
			type.setPaintColour(stack, color);
			bullets.add(stack);
		}

		ItemStack stack = new ItemStack(CommonProxy.item_bullet_magazine, 1, CommonProxy.item_bullet_magazine.getMetaBySubname(magName));
		NonNullList<ItemStack> l = NonNullList.withSize(ItemIIBulletMagazine.getBulletCapactity(stack), ItemStack.EMPTY);
		for(int i = 0; i < l.size(); i++)
		{
			l.set(i, bullets.get(i%(bullets.size())));
		}
		NBTTagList list = blusunrize.immersiveengineering.common.util.Utils.writeInventory(l);
		ItemNBTHelper.getTag(stack).setTag("bullets", list);
		ItemIIBulletMagazine.makeDefault(stack);
		return stack;
	}

	//Deutsche Qualität
	private String getGermanColorName(int color)
	{
		switch(Utils.getRGBTextFormatting(color))
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
}
