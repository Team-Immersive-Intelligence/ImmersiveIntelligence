package pl.pabilo8.immersiveintelligence.common.block;

import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.types.IIBlockTypes_Ore;

/**
 * @author Pabilo8
 * @since 01.09.2020
 */
public class BlockIIOre extends BlockIIBase<IIBlockTypes_Ore>
{
	public BlockIIOre()
	{
		super("ore", Material.ROCK, PropertyEnum.create("type", IIBlockTypes_Ore.class), ItemBlockIEBase.class, false);
		setOpaque(true);
		setHardness(3.0F);
		setResistance(10.0F);
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		if(getMetaFromState(state)==IIBlockTypes_Ore.FLUORITE.getMeta())
		{
			ItemStack out = IIUtils.getStackWithMetaName(IIContent.itemMaterialGem, "fluorite");
			out.setCount((1+Math.round((float)Math.random()))*(1+fortune));
			drops.add(out);
		}
		else if(getMetaFromState(state)==IIBlockTypes_Ore.PHOSPHORUS.getMeta())
		{
			ItemStack out = IIUtils.getStackWithMetaName(IIContent.itemMaterialGem, "phosphorus");
			out.setCount((2+Math.round((float)Math.random()))*(1+fortune));
			drops.add(out);
		}
		else
			super.getDrops(drops, world, pos, state, fortune);
	}

	public void setMiningLevels()
	{
		this.setHarvestLevel("pickaxe", 3, this.getStateFromMeta(IIBlockTypes_Ore.PLATINUM.getMeta()));
		this.setHarvestLevel("pickaxe", 1, this.getStateFromMeta(IIBlockTypes_Ore.ZINC.getMeta()));
		this.setHarvestLevel("pickaxe", 2, this.getStateFromMeta(IIBlockTypes_Ore.TUNGSTEN.getMeta()));
		this.setHarvestLevel("pickaxe", 1, this.getStateFromMeta(IIBlockTypes_Ore.SALT.getMeta()));
		this.setHarvestLevel("pickaxe", 3, this.getStateFromMeta(IIBlockTypes_Ore.FLUORITE.getMeta()));
		this.setHarvestLevel("pickaxe", 3, this.getStateFromMeta(IIBlockTypes_Ore.PHOSPHORUS.getMeta()));
	}
}
