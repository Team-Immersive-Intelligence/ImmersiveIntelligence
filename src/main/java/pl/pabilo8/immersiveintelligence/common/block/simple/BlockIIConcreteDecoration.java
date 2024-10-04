package pl.pabilo8.immersiveintelligence.common.block.simple;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIIConcreteDecoration.ConcreteDecorations;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIIStairs;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockEnum;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockProperties;
import pl.pabilo8.immersiveintelligence.common.util.block.ItemBlockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;

import java.util.Arrays;

/**
 * @author Pabilo8
 * @since 29.08.2020
 */
public class BlockIIConcreteDecoration extends BlockIIBase<ConcreteDecorations>
{
	public BlockIIConcreteDecoration()
	{
		super("concrete_decoration", PropertyEnum.create("type", ConcreteDecorations.class), Material.ROCK, ItemBlockIIBase::new);
		setHardness(3.0F);
		setResistance(15.0F);
		setLightOpacity(255);
		setFullCube(true);
		setCategory(IICategory.RESOURCES);
	}

	public enum ConcreteDecorations implements IIBlockEnum
	{
		@IIBlockProperties(hardness = 10, blastResistance = 360, harvestLevel = 1, oreDict = "bricksConcrete")
		CONCRETE_BRICKS,
		@IIBlockProperties(hardness = 80, blastResistance = 1600, harvestLevel = 2, oreDict = "sturdyBricksConcrete")
		STURDY_CONCRETE_BRICKS,
		@IIBlockProperties(hardness = 120, blastResistance = 2400, harvestLevel = 3, oreDict = "uberConcrete")
		UBERCONCRETE
	}

	public static BlockIIStairs[] getStairs()
	{
		return Arrays.stream(ConcreteDecorations.values())
				.map(s -> new BlockIIStairs("concrete_decoration_stairs", s.getName(),
						IIContent.blockConcreteDecoration.getStateFromMeta(s.ordinal())))
				.toArray(BlockIIStairs[]::new);
	}
}