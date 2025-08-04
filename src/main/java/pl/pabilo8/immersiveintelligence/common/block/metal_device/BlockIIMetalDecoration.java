package pl.pabilo8.immersiveintelligence.common.block.metal_device;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.BlockRenderLayer;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.BlockIIMetalDecoration.IIBlockTypes_MetalDecoration;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockEnum;
import pl.pabilo8.immersiveintelligence.common.util.block.ItemBlockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 17.05.2019
 */
public class BlockIIMetalDecoration extends BlockIIBase<IIBlockTypes_MetalDecoration>
{
	public BlockIIMetalDecoration()
	{
		super("metal_decoration", PropertyEnum.create("type", IIBlockTypes_MetalDecoration.class), Material.IRON, ItemBlockIIBase::new);
		setHardness(3.0F);
		setResistance(15.0F);
		setCategory(IICategory.RESOURCES);

		setToolTypes(IIReference.TOOL_HAMMER);

		setBlockLayer(BlockRenderLayer.CUTOUT_MIPPED);
	}

	public enum IIBlockTypes_MetalDecoration implements IIBlockEnum
	{
		COIL_DATA,
		ELECTRONIC_ENGINEERING,
		ADVANCED_ELECTRONIC_ENGINEERING,
		MECHANICAL_ENGINEERING,
		HEAVY_MECHANICAL_ENGINEERING,
		COIL_STEEL_MOTOR_BELT,
		COMPUTER_ENGINEERING
	}
}