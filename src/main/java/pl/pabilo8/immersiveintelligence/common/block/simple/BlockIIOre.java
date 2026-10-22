package pl.pabilo8.immersiveintelligence.common.block.simple;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIIOre.Ores;
import pl.pabilo8.immersiveintelligence.common.item.crafting.material.ItemIIMaterialGem.MaterialsGem;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockEnum;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockProperties;
import pl.pabilo8.immersiveintelligence.common.util.block.ItemBlockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;

import javax.annotation.Nonnull;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 01.09.2020
 */
public class BlockIIOre extends BlockIIBase<Ores>
{
	public BlockIIOre()
	{
		super("ore", PropertyEnum.create("type", Ores.class), Material.ROCK, ItemBlockIIBase::new, false);
		setFullCube(true);
		setHardness(3.0F);
		setResistance(10.0F);
		setCategory(IICategory.RESOURCES);
	}

	@Override
	public void getDrops(@Nonnull NonNullList<ItemStack> drops, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull IBlockState state, int fortune)
	{
		Ores ore = state.getValue(property);
		if(ore.dropSupplier!=null)
			drops.add(ore.dropSupplier.apply(fortune));
	}

	public enum Ores implements IIBlockEnum
	{
		@IIBlockProperties(harvestLevel = 3)
		PLATINUM,
		@IIBlockProperties(harvestLevel = 1)
		ZINC,
		@IIBlockProperties(harvestLevel = 2)
		TUNGSTEN,
		@IIBlockProperties(harvestLevel = 1)
		SALT(() -> IIContent.itemMaterialGem.getStack(MaterialsGem.SALT)),
		@IIBlockProperties(harvestLevel = 3)
		FLUORITE(() -> IIContent.itemMaterialGem.getStack(MaterialsGem.FLUORITE)),
		@IIBlockProperties(harvestLevel = 3)
		PHOSPHORUS(() -> IIContent.itemMaterialGem.getStack(MaterialsGem.PHOSPHORUS)),
		@IIBlockProperties(harvestLevel = 1)
		SULFUR(() -> IIContent.itemMaterialGem.getStack(MaterialsGem.SULFUR)),
		@IIBlockProperties(harvestLevel = 1, oreDict = "oreSulfur")
		SULFUR_OVERWORLD(() -> IIContent.itemMaterialGem.getStack(MaterialsGem.SULFUR)),
		@IIBlockProperties(harvestLevel = 1)
		TIN,
		@IIBlockProperties(harvestLevel = 5)
		CHROMIUM,
		@IIBlockProperties(harvestLevel = 3, oreDict = "orePhosphorus")
		PHOSPHORUS_OVERWORLD(() -> IIContent.itemMaterialGem.getStack(MaterialsGem.PHOSPHORUS)),
		@IIBlockProperties(harvestLevel = 3, oreDict = "oreFluorite")
		FLUORITE_OVERWORLD(() -> IIContent.itemMaterialGem.getStack(MaterialsGem.FLUORITE));

		private final Function<Integer, ItemStack> dropSupplier;

		Ores(Supplier<ItemStack> stackSupplier)
		{
			this.dropSupplier = (fortune) -> {
				ItemStack stack = stackSupplier.get();
				stack.setCount((1+Math.round((float)Math.random()))*(1+fortune));
				return stack;
			};
		}

		Ores()
		{
			this.dropSupplier = null;
		}
	}
}
