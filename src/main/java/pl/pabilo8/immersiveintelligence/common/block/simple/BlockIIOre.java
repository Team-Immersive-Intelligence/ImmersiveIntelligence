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

/**
 * @author Pabilo8
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

	public enum Ores implements IIBlockEnum
	{
		@IIBlockProperties(harvestLevel = 3)
		PLATINUM,
		@IIBlockProperties(harvestLevel = 1)
		ZINC,
		@IIBlockProperties(harvestLevel = 2)
		TUNGSTEN,
		@IIBlockProperties(harvestLevel = 1)
		SALT,
		@IIBlockProperties(harvestLevel = 3)
		FLUORITE,
		@IIBlockProperties(harvestLevel = 3)
		PHOSPHORUS
	}

	@Override
	public void getDrops(@Nonnull NonNullList<ItemStack> drops, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull IBlockState state, int fortune)
	{
		switch(state.getValue(property))
		{
			case FLUORITE:
				drops.add(IIContent.itemMaterialGem.getStack(MaterialsGem.FLUORITE,
						(1+Math.round((float)Math.random()))*(1+fortune)));
				break;
			case PHOSPHORUS:
				drops.add(IIContent.itemMaterialGem.getStack(MaterialsGem.PHOSPHORUS,
						(2+Math.round((float)Math.random()))*(1+fortune)));
				break;
			default:
				super.getDrops(drops, world, pos, state, fortune);
				break;
		}

	}
}
