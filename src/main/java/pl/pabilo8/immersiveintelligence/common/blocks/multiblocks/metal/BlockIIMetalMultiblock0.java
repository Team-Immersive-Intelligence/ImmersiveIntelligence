package pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.client.models.IOBJModelCallback;
import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import pl.pabilo8.immersiveintelligence.common.blocks.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.TileEntityMultiblockConnectable;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first.*;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalMultiblock0;

import java.util.Arrays;

/**
 * Created by Pabilo8 on 20-06-2019.
 */
public class BlockIIMetalMultiblock0 extends BlockIIMultiblock<IIBlockTypes_MetalMultiblock0>
{
	public BlockIIMetalMultiblock0()
	{
		super("metal_multiblock", Material.IRON, PropertyEnum.create("type", IIBlockTypes_MetalMultiblock0.class), ItemBlockIEBase.class, IEProperties.FACING_HORIZONTAL,
				IEProperties.BOOLEANS[0], IEProperties.BOOLEANS[1], IEProperties.MULTIBLOCKSLAVE, IOBJModelCallback.PROPERTY);
		setHardness(3.0F);
		setResistance(15.0F);
		lightOpacity = 0;
		this.setAllNotNormalBlock();

		tesrMap.put(IIBlockTypes_MetalMultiblock0.PRINTING_PRESS.getMeta(), IIBlockTypes_MetalMultiblock0.PRINTING_PRESS.getName());
		tesrMap.put(IIBlockTypes_MetalMultiblock0.RADIO_STATION.getMeta(), IIBlockTypes_MetalMultiblock0.RADIO_STATION.getName());
		tesrMap.put(IIBlockTypes_MetalMultiblock0.AMMUNITION_FACTORY.getMeta(), IIBlockTypes_MetalMultiblock0.AMMUNITION_FACTORY.getName());
		tesrMap.put(IIBlockTypes_MetalMultiblock0.ELECTROLYZER.getMeta(), IIBlockTypes_MetalMultiblock0.ELECTROLYZER.getName());
		tesrMap.put(IIBlockTypes_MetalMultiblock0.CHEMICAL_BATH.getMeta(), IIBlockTypes_MetalMultiblock0.CHEMICAL_BATH.getName());
		tesrMap.put(IIBlockTypes_MetalMultiblock0.PRECISSION_ASSEMBLER.getMeta(), IIBlockTypes_MetalMultiblock0.PRECISSION_ASSEMBLER.getName());
		tesrMap.put(IIBlockTypes_MetalMultiblock0.ARTILLERY_HOWITZER.getMeta(), IIBlockTypes_MetalMultiblock0.ARTILLERY_HOWITZER.getName());


	}

	@Override
	public boolean useCustomStateMapper()
	{
		return true;
	}

	@Override
	public String getCustomStateMapping(int meta, boolean itemBlock)
	{
		if(IIBlockTypes_MetalMultiblock0.values()[meta].needsCustomState())
			return IIBlockTypes_MetalMultiblock0.values()[meta].getCustomState();
		return null;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public TileEntity createBasicTE(World world, IIBlockTypes_MetalMultiblock0 type)
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
			case CHEMICAL_BATH:
			{
				return new TileEntityChemicalBath();
			}
			case ELECTROLYZER:
			{
				return new TileEntityElectrolyzer();
			}
			case CONVEYOR_SCANNER:
			{
				return new TileEntityConveyorScanner();
			}
			case PRECISSION_ASSEMBLER:
			{
				return new TileEntityPrecissionAssembler();
			}
			case ARTILLERY_HOWITZER:
			{
				return new TileEntityArtilleryHowitzer();
			}
			case AMMUNITION_FACTORY:
			{
				return new TileEntityAmmunitionFactory();
			}
			case BALLISTIC_COMPUTER:
			{
				return new TileEntityBallisticComputer();
			}
			case PACKER:
			{
				return new TileEntityPacker();
			}
		}
		return null;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		BlockStateContainer base = super.createBlockState();
		IUnlistedProperty[] unlisted = (base instanceof ExtendedBlockState)?((ExtendedBlockState)base).getUnlistedProperties().toArray(new IUnlistedProperty[0]): new IUnlistedProperty[0];
		unlisted = Arrays.copyOf(unlisted, unlisted.length+1);
		unlisted[unlisted.length-1] = IEProperties.CONNECTIONS;
		return new ExtendedBlockState(this, base.getProperties().toArray(new IProperty[0]), unlisted);
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		state = super.getExtendedState(state, world, pos);
		if(state instanceof IExtendedBlockState)
		{
			IExtendedBlockState ext = (IExtendedBlockState)state;
			TileEntity te = world.getTileEntity(pos);
			if(!(te instanceof TileEntityMultiblockConnectable))
				return state;
			state = ext.withProperty(IEProperties.CONNECTIONS, ((TileEntityMultiblockConnectable)te).genConnBlockstate());
		}
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

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		super.breakBlock(world, pos, state);
	}

}
