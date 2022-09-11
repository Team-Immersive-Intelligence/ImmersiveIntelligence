package pl.pabilo8.immersiveintelligence.common.block.fortification;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.block.fortification.tileentity.TileEntityMineSign;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIITileProvider;
import pl.pabilo8.immersiveintelligence.common.block.fortification.BlockIIMineSign.IIBlockTypes_MineSign;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.EnumTileProvider;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockEnum;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IITileProviderEnum;
import pl.pabilo8.immersiveintelligence.common.util.block.ItemBlockIIBase;

/**
 * @author Pabilo8
 * @since 2019-05-17
 */
public class BlockIIMineSign extends BlockIITileProvider<IIBlockTypes_MineSign>
{
	public BlockIIMineSign()
	{
		super("mine_sign", Material.WOOD, PropertyEnum.create("type", IIBlockTypes_MineSign.class), ItemBlockIIBase::new, IEProperties.FACING_HORIZONTAL);
		setHardness(3.0F);
		setResistance(15.0F);
		setLightOpacity(0);
		setBlockLayer(BlockRenderLayer.CUTOUT_MIPPED);
	}

	public enum IIBlockTypes_MineSign implements IITileProviderEnum
	{
		@EnumTileProvider(tile = TileEntityMineSign.class)
		MINE_SIGN
	}
}