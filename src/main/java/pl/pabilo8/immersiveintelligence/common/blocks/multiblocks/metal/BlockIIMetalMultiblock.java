package pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal;

import blusunrize.immersiveengineering.api.DimensionBlockPos;
import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.Properties;
import pl.pabilo8.immersiveintelligence.api.data.radio.RadioNetwork;
import pl.pabilo8.immersiveintelligence.common.blocks.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalMultiblock;

/**
 * Created by Pabilo8 on 20-06-2019.
 */
public class BlockIIMetalMultiblock extends BlockIIMultiblock<IIBlockTypes_MetalMultiblock>
{
	public BlockIIMetalMultiblock()
	{
		super("metal_multiblock", Material.IRON, PropertyEnum.create("type", IIBlockTypes_MetalMultiblock.class), ItemBlockIEBase.class, false, IEProperties.FACING_HORIZONTAL, IEProperties.MULTIBLOCKSLAVE, IEProperties.DYNAMICRENDER, IEProperties.BOOLEANS[0], Properties.AnimationProperty);
		setHardness(3.0F);
		setResistance(15.0F);
		lightOpacity = 0;
		this.setAllNotNormalBlock();

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
	public CreativeTabs getCreativeTabToDisplayOn()
	{
		return null;
	}

	@Override
	public TileEntity createBasicTE(World world, IIBlockTypes_MetalMultiblock type)
	{
		switch(type)
		{
			case RADIO_STATION:
			{
				return new TileEntityRadioStation();
			}
			case DATA_INPUT_MACHINE:
			{
				return new TileEntityDataInputMachine();
			}
			case ARITHMETIC_LOGIC_MACHINE:
			{
				return new TileEntityArithmeticLogicMachine();
			}
			case PRINTING_PRESS:
			{
				return new TileEntityPrintingPress();
			}
		}
		return null;
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		state = super.getActualState(state, world, pos);
		return state;
	}

	@Override
	public boolean allowHammerHarvest(IBlockState state)
	{
		return true;
	}

	@Override
	public boolean canIEBlockBePlaced(World world, BlockPos pos, IBlockState newState, EnumFacing side, float hitX, float hitY, float hitZ, EntityPlayer player, ItemStack stack)
	{
		return true;
	}

	@Deprecated
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		TileEntity tileEntity = world.getTileEntity(pos);

		if(tileEntity instanceof TileEntityRadioStation)
		{
			TileEntityRadioStation tile = (TileEntityRadioStation)tileEntity;
			if(!tile.isDummy())
			{
				RadioNetwork.INSTANCE.removeDevice(new DimensionBlockPos(tile));
			}
		}
		super.breakBlock(world, pos, state);

	}

}
