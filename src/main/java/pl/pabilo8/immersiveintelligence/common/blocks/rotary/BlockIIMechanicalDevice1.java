package pl.pabilo8.immersiveintelligence.common.blocks.rotary;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.client.models.IOBJModelCallback;
import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import net.minecraft.block.material.EnumPushReaction;
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
import net.minecraftforge.common.property.Properties;
import pl.pabilo8.immersiveintelligence.common.blocks.BlockIITileProvider;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MechanicalDevice1;

/**
 * @author Pabilo8
 * @since 03.10.2020
 */
public class BlockIIMechanicalDevice1 extends BlockIITileProvider<IIBlockTypes_MechanicalDevice1>
{
	public BlockIIMechanicalDevice1()
	{
		super("mechanical_device1", Material.IRON, PropertyEnum.create("type", IIBlockTypes_MechanicalDevice1.class), ItemBlockIEBase.class, IEProperties.MULTIBLOCKSLAVE,
				IEProperties.DYNAMICRENDER, IOBJModelCallback.PROPERTY, Properties.AnimationProperty,
				IEProperties.SIDECONFIG[0], IEProperties.SIDECONFIG[1], IEProperties.SIDECONFIG[2], IEProperties.SIDECONFIG[3], IEProperties.SIDECONFIG[4], IEProperties.SIDECONFIG[5]);
		lightOpacity = 0;
		setHardness(3.0F);
		setResistance(15.0F);
		this.setMetaBlockLayer(IIBlockTypes_MechanicalDevice1.MECHANICAL_PUMP.getMeta(), BlockRenderLayer.CUTOUT);
		this.setAllNotNormalBlock();
		this.setMetaMobilityFlag(IIBlockTypes_MechanicalDevice1.MECHANICAL_PUMP.getMeta(), EnumPushReaction.BLOCK);
	}

	@Override
	public boolean useCustomStateMapper()
	{
		return true;
	}

	@Override
	public String getCustomStateMapping(int meta, boolean itemBlock)
	{
		return null;
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


	/**
	 * Get the actual Block state of this Block at the given position. This applies properties not visible in the metadata,
	 * such as fence connections.
	 */
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		state = super.getActualState(state, world, pos);
		return state;
	}


	@Override
	public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileEntityMechanicalPump)
			return !((TileEntityMechanicalPump)te).dummy||side==EnumFacing.UP;
		return true;
	}

	@Override
	public TileEntity createBasicTE(World world, IIBlockTypes_MechanicalDevice1 type)
	{
		if(type==IIBlockTypes_MechanicalDevice1.MECHANICAL_PUMP)
		{
			return new TileEntityMechanicalPump();
		}
		return null;
	}

	@Override
	public int getLightOpacity(IBlockState state, IBlockAccess w, BlockPos pos)
	{
		if(state.getValue(IEProperties.MULTIBLOCKSLAVE))
			return 0;
		return super.getLightOpacity(state, w, pos);
	}

	@Override
	public boolean allowHammerHarvest(IBlockState state)
	{
		return true;
	}
}