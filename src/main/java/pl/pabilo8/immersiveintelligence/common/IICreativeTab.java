package pl.pabilo8.immersiveintelligence.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.crafting.PrecissionAssemblerRecipe;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalDecoration;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIBullet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pabilo8 on 2019-05-07.
 */
public class IICreativeTab extends CreativeTabs
{
	public IICreativeTab(String name)
	{
		super(name);
	}

	@Override
	public ItemStack createIcon()
	{
		return new ItemStack(CommonProxy.block_metal_decoration, 1, IIBlockTypes_MetalDecoration.COIL_DATA.ordinal());
	}

	public static List<Fluid> fluidBucketMap = new ArrayList<>();

	@Override
	@SideOnly(Side.CLIENT)
	public void displayAllRelevantItems(NonNullList<ItemStack> list)
	{
		super.displayAllRelevantItems(list);

		addAssemblySchemes(list);
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
			list.add(ImmersiveIntelligence.proxy.item_assembly_scheme.getStackForRecipe(recipe));
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

		list.add(ItemIIBullet.getAmmoStack(1, "artillery_8bCal", "CoreBrass", "HMX", "white_phosphorus", 0.5f).setStackDisplayName("Wurfgranate 39"));

		list.add(ItemIIBullet.getAmmoStack(1, "grenade_4bCal", "CoreBrass", "TNT", "", 1f).setStackDisplayName("Stielhandgranate 38"));
		list.add(ItemIIBullet.getAmmoStack(1, "grenade_4bCal", "CoreBrass", "white_phosphorus", "", 1f).setStackDisplayName("Stielhandgranate 39"));
		list.add(ItemIIBullet.getAmmoStack(1, "grenade_4bCal", "CoreBrass", "RDX", "", 1f).setStackDisplayName("Stielhandgranate 42"));
		list.add(ItemIIBullet.getAmmoStack(1, "grenade_4bCal", "CoreBrass", "HMX", "", 1f).setStackDisplayName("Stielhandgranate 46"));
	}
}
