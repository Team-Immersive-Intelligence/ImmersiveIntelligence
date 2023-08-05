package pl.pabilo8.immersiveintelligence.common.block.simple;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIIMetalBase.Metals;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockEnum;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockProperties;
import pl.pabilo8.immersiveintelligence.common.util.block.ItemBlockIIBase;

/**
 * @author Pabilo8
 * @since 05.09.2022
 */
public class BlockIIMetalBase extends BlockIIBase<Metals>
{
	public BlockIIMetalBase(String name)
	{
		super(name, PropertyEnum.create("type", Metals.class), Material.IRON, ItemBlockIIBase::new);
		setFullCube(true);

		setHardness(5.0F);
		setResistance(10.0F);
	}

	public enum Metals implements IIBlockEnum
	{
		// TODO: 05.09.2022 values
		@IIBlockProperties(hardness = 5, blastResistance = 10)
		PLATINUM,
		@IIBlockProperties(hardness = 4, blastResistance = 10)
		ZINC,
		@IIBlockProperties(hardness = 10, blastResistance = 10)
		TUNGSTEN,
		@IIBlockProperties(hardness = 5, blastResistance = 10)
		BRASS,
		@IIBlockProperties(hardness = 6, blastResistance = 10)
		DURALUMINIUM
	}
}
