package pl.pabilo8.immersiveintelligence.common.block.simple;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.BlockRenderLayer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIIConcreteDecoration.ConcreteDecorations;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIIStairs;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockEnum;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockProperties;
import pl.pabilo8.immersiveintelligence.common.util.block.ItemBlockIIBase;

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
	}

	public enum ConcreteDecorations implements IIBlockEnum
	{
		@IIBlockProperties(hardness = 2, blastResistance = 360, harvestLevel = 1, oreDict = "bricksConcrete")
		CONCRETE_BRICKS,
		@IIBlockProperties(hardness = 6, blastResistance = 1600, harvestLevel = 2, oreDict = "sturdyBricksConcrete")
		STURDY_CONCRETE_BRICKS,
		@IIBlockProperties(hardness = 10, blastResistance = 2400, harvestLevel = 3, oreDict = "uberConcrete")
		UBERCONCRETE
	}

	public static BlockIIStairs[] getStairs()
	{
		BlockIIStairs[] stairs = new BlockIIStairs[ConcreteDecorations.values().length];
		for(int i = 0; i < ConcreteDecorations.values().length; i++)
		{
			final ConcreteDecorations value = ConcreteDecorations.values()[i];
			stairs[i] = new BlockIIStairs("concrete_decoration_stairs_"+value.getName(), IIContent.blockConcreteDecoration.getStateFromMeta(i));
		}
		return stairs;
	}
}