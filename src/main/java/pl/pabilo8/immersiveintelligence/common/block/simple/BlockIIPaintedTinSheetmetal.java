package pl.pabilo8.immersiveintelligence.common.block.simple;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIIPaintedTinSheetmetal.PaintedColors;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockEnum;
import pl.pabilo8.immersiveintelligence.common.util.block.ItemBlockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;

/**
 * @author Avalon (avalon@iiteam.net)
 * @since 02.11.2026
 */
public class BlockIIPaintedTinSheetmetal extends BlockIIBase<PaintedColors>
{
	public BlockIIPaintedTinSheetmetal()
	{
		super("painted_sheetmetal_tin", PropertyEnum.create("type", PaintedColors.class), Material.IRON, ItemBlockIIBase::new);
		setFullCube(true);

		setHardness(4.0F);
		setResistance(10.0F);
		setCategory(IICategory.RESOURCES);
	}

	public enum PaintedColors implements IIBlockEnum
	{
		WHITE,
		ORANGE,
		MAGENTA,
		LIGHT_BLUE,
		YELLOW,
		LIME,
		PINK,
		GRAY,
		SILVER,
		CYAN,
		PURPLE,
		BLUE,
		BROWN,
		GREEN,
		RED,
		BLACK
	}
}
