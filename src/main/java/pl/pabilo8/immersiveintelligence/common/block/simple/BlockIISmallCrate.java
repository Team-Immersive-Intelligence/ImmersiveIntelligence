package pl.pabilo8.immersiveintelligence.common.block.simple;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.block.simple.tileentity.TileEntitySmallCrate;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIITileProvider;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIISmallCrate.IIBlockTypes_SmallCrate;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.EnumTileProvider;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockEnum;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockProperties;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IITileProviderEnum;
import pl.pabilo8.immersiveintelligence.common.util.block.ItemBlockIIBase;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 2019-05-17
 */
public class BlockIISmallCrate extends BlockIITileProvider<IIBlockTypes_SmallCrate>
{
	public BlockIISmallCrate()
	{
		super("small_crate", Material.IRON, PropertyEnum.create("type", IIBlockTypes_SmallCrate.class), ItemBlockIIBase::new, IEProperties.FACING_HORIZONTAL);
		setHardness(3.0F);
		setResistance(15.0F);
		setLightOpacity(0);
		setBlockLayer(BlockRenderLayer.CUTOUT);

		setSubMaterial(IIBlockTypes_SmallCrate.WOODEN_CRATE_BOX,Material.WOOD);
		setSubMaterial(IIBlockTypes_SmallCrate.WOODEN_CRATE_CUBE,Material.WOOD);
		setSubMaterial(IIBlockTypes_SmallCrate.WOODEN_CRATE_WIDE,Material.WOOD);
	}

	public enum IIBlockTypes_SmallCrate implements IITileProviderEnum
	{
		WOODEN_CRATE_BOX,
		WOODEN_CRATE_CUBE,
		WOODEN_CRATE_WIDE,
		METAL_CRATE_BOX,
		METAL_CRATE_CUBE,
		METAL_CRATE_WIDE,
		REINFORCED_CRATE_BOX,
		REINFORCED_CRATE_CUBE,
		REINFORCED_CRATE_WIDE;

		@Override
		public Class<? extends TileEntity> getTile()
		{
			return TileEntitySmallCrate.class;
		}
	}
}