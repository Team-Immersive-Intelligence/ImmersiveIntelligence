package pl.pabilo8.immersiveintelligence.common.block.rotary_device;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.client.models.IOBJModelCallback;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.Properties;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.BlockIIMechanicalDevice1.IIBlockTypes_MechanicalDevice1;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity.TileEntityMechanicalPump;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIITileProvider;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.EnumTileProvider;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockProperties;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IITileProviderEnum;
import pl.pabilo8.immersiveintelligence.common.util.block.ItemBlockIIBase;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 03.10.2020
 */
public class BlockIIMechanicalDevice1 extends BlockIITileProvider<IIBlockTypes_MechanicalDevice1>
{
	public enum IIBlockTypes_MechanicalDevice1 implements IITileProviderEnum
	{
		@EnumTileProvider(tile = TileEntityMechanicalPump.class)
		@IIBlockProperties(renderLayer = BlockRenderLayer.CUTOUT)
		MECHANICAL_PUMP
	}

	public BlockIIMechanicalDevice1()
	{
		super("mechanical_device1", Material.IRON, PropertyEnum.create("type", IIBlockTypes_MechanicalDevice1.class), ItemBlockIIBase::new,
				IEProperties.MULTIBLOCKSLAVE, IEProperties.DYNAMICRENDER, IOBJModelCallback.PROPERTY, Properties.AnimationProperty,
				IEProperties.SIDECONFIG[0], IEProperties.SIDECONFIG[1], IEProperties.SIDECONFIG[2],
				IEProperties.SIDECONFIG[3], IEProperties.SIDECONFIG[4], IEProperties.SIDECONFIG[5]);
		setHardness(3.0F);
		setResistance(15.0F);
		setBlockLayer(BlockRenderLayer.CUTOUT);
		this.setMetaMobilityFlag(IIBlockTypes_MechanicalDevice1.MECHANICAL_PUMP, EnumPushReaction.BLOCK);
		setToolTypes(IIReference.TOOL_HAMMER);
	}

	@Override
	public boolean canIEBlockBePlaced(World world, BlockPos pos, IBlockState newState, EnumFacing side, float hitX, float hitY, float hitZ, EntityPlayer player, ItemStack stack)
	{
		if(stack.getItemDamage()==IIBlockTypes_MechanicalDevice1.MECHANICAL_PUMP.getMeta())
		{
			BlockPos above = pos.up();
			return !world.isOutsideBuildHeight(above)&&world.getBlockState(above).getBlock().isReplaceable(world, above);
		}
		return true;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isSideSolid(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull EnumFacing side)
	{
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileEntityMechanicalPump)
			return !((TileEntityMechanicalPump)te).dummy||side==EnumFacing.UP;
		return true;
	}

	@Override
	public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		if(state.getValue(IEProperties.MULTIBLOCKSLAVE))
			return 0;
		return super.getLightOpacity(state, world, pos);
	}
}