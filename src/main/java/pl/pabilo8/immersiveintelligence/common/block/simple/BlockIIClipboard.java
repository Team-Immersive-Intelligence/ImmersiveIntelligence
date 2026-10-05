package pl.pabilo8.immersiveintelligence.common.block.simple;

import blusunrize.immersiveengineering.api.IEProperties;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.tileentity.TileEntity;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIIClipboard.ClipboardType;
import pl.pabilo8.immersiveintelligence.common.block.simple.tileentity.TileEntityClipboard;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIITileProvider;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IITileProviderEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;

/**
 * Placeable form of the Engineer's Clipboard; it intentionally has no ItemBlock.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 21.09.2026
 */
public class BlockIIClipboard extends BlockIITileProvider<ClipboardType>
{
	public BlockIIClipboard()
	{
		super("clipboard", Material.WOOD, PropertyEnum.create("type", ClipboardType.class), null,
				IEProperties.FACING_ALL);
		setHardness(1.5f);
		setResistance(2f);
		setSoundType(SoundType.WOOD);
		setCategory(IICategory.TOOLS);
	}

	public enum ClipboardType implements IITileProviderEnum
	{
		CLIPBOARD;

		@Override
		public Class<? extends TileEntity> getTile()
		{
			return TileEntityClipboard.class;
		}
	}
}
