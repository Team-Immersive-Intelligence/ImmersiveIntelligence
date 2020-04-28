package pl.pabilo8.immersiveintelligence.common;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.crafting.PrecissionAssemblerRecipe;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalDecoration;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIBullet;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIBulletMagazine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pabilo8 on 2019-05-07.
 */
public class IICreativeTab extends CreativeTabs
{
	public static List<Fluid> fluidBucketMap = new ArrayList<>();

	public IICreativeTab(String name)
	{
		super(name);
	}

	@Override
	public ItemStack createIcon()
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
		list.add(ItemIIBullet.getAmmoStack(1, "artillery_8bCal", "CoreSteel", "TNT", "", 1f));
		list.add(ItemIIBullet.getAmmoStack(1, "artillery_8bCal", "CoreSteel", "RDX", "", 1f));
		list.add(ItemIIBullet.getAmmoStack(1, "artillery_8bCal", "CoreSteel", "HMX", "", 1f));

		list.add(ItemIIBullet.getAmmoStack(1, "artillery_8bCal", "CoreTungsten", "TNT", "", 1f));
		list.add(ItemIIBullet.getAmmoStack(1, "artillery_8bCal", "CoreTungsten", "RDX", "", 1f));
		list.add(ItemIIBullet.getAmmoStack(1, "artillery_8bCal", "CoreTungsten", "HMX", "", 1f));

		list.add(ItemIIBullet.getAmmoStack(1, "artillery_8bCal", "CoreBrass", "TNT", "", 1f));
		list.add(ItemIIBullet.getAmmoStack(1, "artillery_8bCal", "CoreBrass", "RDX", "", 1f));
		list.add(ItemIIBullet.getAmmoStack(1, "artillery_8bCal", "CoreBrass", "HMX", "", 1f));

		list.add(ItemIIBullet.getAmmoStack(1, "artillery_8bCal", "CoreBrass", "HMX", "white_phosphorus", 0.5f).setStackDisplayName("M65 White-Phosphorus Shell"));

		list.add(ItemIIBullet.getAmmoStack(1, "grenade_4bCal", "CoreBrass", "TNT", "", 1f).setStackDisplayName("Stielhandgranate mk.1"));
		list.add(ItemIIBullet.getAmmoStack(1, "grenade_4bCal", "CoreBrass", "white_phosphorus", "", 1f).setStackDisplayName("Phosphorgranate mk.1"));
		list.add(ItemIIBullet.getAmmoStack(1, "grenade_4bCal", "CoreBrass", "RDX", "", 1f).setStackDisplayName("Sprenghandgranate mk.1"));
		list.add(ItemIIBullet.getAmmoStack(1, "grenade_4bCal", "CoreBrass", "HMX", "", 1f).setStackDisplayName("Sprenghandgranate mk.2"));
		ItemStack grenade_firework = ItemIIBullet.getAmmoStack(1, "grenade_4bCal", "CoreBrass", "firework", "", 1f).setStackDisplayName("Feuerwerkhandgranate mk.1");
		try
		{
			NBTTagCompound firework_nbt=JsonToNBT.getTagFromJson("{Explosion:{Type:0b,Colors:[I;3887386]}}");
			ItemNBTHelper.setTagCompound(grenade_firework,"firstComponentNBT",firework_nbt);

		} catch(NBTException e)
		{
			e.printStackTrace();
		}

		list.add(grenade_firework);

		ItemStack bullet1 = ItemIIBullet.getAmmoStack(1, "machinegun_2bCal", "CoreBrass", "TNT", "", 1f).setStackDisplayName("Sprengpatrone mk.1");
		ItemStack bullet2 = ItemIIBullet.getAmmoStack(1, "machinegun_2bCal", "CoreTungsten", "shrapnel_tungsten", "", 1f).setStackDisplayName("Wolframpatrone mk.1");
		ItemStack bullet3 = ItemIIBullet.getAmmoStack(1, "machinegun_2bCal", "CoreSteel", "", "", 1f).setStackDisplayName("Stahlpatrone mk.1");
		ItemStack bullet4 = ItemIIBullet.getAmmoStack(1, "machinegun_2bCal", "CoreBrass", "white_phosphorus", "", 1f).setStackDisplayName("Phosphorpatrone mk.1");
		list.add(bullet1);
		list.add(bullet2);
		list.add(bullet3);
		list.add(bullet4);
		list.add(ItemIIBulletMagazine.getMagazine("machinegun", bullet1, bullet2, bullet3, bullet4));

		ItemStack bullet5 = ItemIIBullet.getAmmoStack(1, "machinegun_2bCal", "CoreUranium", "shrapnel_uranium", "", 1f).setStackDisplayName("M1A1 Uranium Bullet");
		list.add(bullet5);
		list.add(ItemIIBulletMagazine.getMagazine("machinegun", bullet5, bullet5, bullet5, bullet5));
	}
}
