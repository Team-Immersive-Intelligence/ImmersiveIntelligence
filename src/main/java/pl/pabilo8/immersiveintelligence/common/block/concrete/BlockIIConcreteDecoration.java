package pl.pabilo8.immersiveintelligence.common.block.concrete;

import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.BlockIIBase;
import pl.pabilo8.immersiveintelligence.common.block.BlockIISlab;
import pl.pabilo8.immersiveintelligence.common.block.BlockIIStairs;
import pl.pabilo8.immersiveintelligence.common.block.types.IIBlockTypes_ConcreteDecoration;

/**
 * @author Pabilo8
 * @since 2019-05-17
 */
public class BlockIIConcreteDecoration extends BlockIIBase<IIBlockTypes_ConcreteDecoration>
{
	public BlockIIConcreteDecoration()
	{
		super("concrete_decoration", Material.ROCK, PropertyEnum.create("type", IIBlockTypes_ConcreteDecoration.class), ItemBlockIEBase.class);
		setMiningLevels(this);
	}

	public static class BlockIIConcreteSlab extends BlockIISlab<IIBlockTypes_ConcreteDecoration>
	{
		public BlockIIConcreteSlab()
		{
			super("concrete_decoration_slab", Material.ROCK, PropertyEnum.create("type", IIBlockTypes_ConcreteDecoration.class));
			setMiningLevels(this);
		}
	}

	public static BlockIIStairs[] getStairs()
	{
		BlockIIStairs[] stairs = new BlockIIStairs[IIBlockTypes_ConcreteDecoration.values().length];
		for(int i = 0; i < IIBlockTypes_ConcreteDecoration.values().length; i++)
		{
			final IIBlockTypes_ConcreteDecoration value = IIBlockTypes_ConcreteDecoration.values()[i];
			stairs[i] = new BlockIIStairs("concrete_decoration_stairs_"+value.getName(), IIContent.blockConcreteDecoration.getStateFromMeta(i));
		}
		return stairs;
	}

	public static void setMiningLevels(BlockIIBase<IIBlockTypes_ConcreteDecoration> block)
	{
		block.setHardness(3.0F);
		block.setResistance(15.0F);
		block.setLightOpacity(255);

		block.setMetaExplosionResistance(IIBlockTypes_ConcreteDecoration.CONCRETE_BRICKS.getMeta(), 360);
		block.setMetaHardness(IIBlockTypes_ConcreteDecoration.CONCRETE_BRICKS.getMeta(), 2.0F);
		block.setMetaExplosionResistance(IIBlockTypes_ConcreteDecoration.STURDY_CONCRETE_BRICKS.getMeta(), 1600);
		block.setMetaHardness(IIBlockTypes_ConcreteDecoration.STURDY_CONCRETE_BRICKS.getMeta(), 6.0F);
		block.setMetaExplosionResistance(IIBlockTypes_ConcreteDecoration.UBERCONCRETE.getMeta(), 2400);
		block.setMetaHardness(IIBlockTypes_ConcreteDecoration.UBERCONCRETE.getMeta(), 10.0F);

		block.setHarvestLevel("pickaxe", 1, block.getStateFromMeta((IIBlockTypes_ConcreteDecoration.CONCRETE_BRICKS.getMeta())));
		block.setHarvestLevel("pickaxe", 2, block.getStateFromMeta((IIBlockTypes_ConcreteDecoration.STURDY_CONCRETE_BRICKS.getMeta())));
		block.setHarvestLevel("pickaxe", 3, block.getStateFromMeta((IIBlockTypes_ConcreteDecoration.UBERCONCRETE.getMeta())));
	}
}