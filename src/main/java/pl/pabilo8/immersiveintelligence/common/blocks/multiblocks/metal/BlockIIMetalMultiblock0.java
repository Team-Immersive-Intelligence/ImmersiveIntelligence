package pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.client.models.IOBJModelCallback;
import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;
import pl.pabilo8.immersiveintelligence.api.utils.IIMultiblockInterfaces.IExplosionResistantMultiblock;
import pl.pabilo8.immersiveintelligence.api.utils.IIMultiblockInterfaces.ILadderMultiblock;
import pl.pabilo8.immersiveintelligence.common.blocks.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.TileEntityMultiblockConnectable;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first.*;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalMultiblock0;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * @author Pabilo8
 * @since 20-06-2019
 */
@SuppressWarnings("deprecation")
public class BlockIIMetalMultiblock0 extends BlockIIMultiblock<IIBlockTypes_MetalMultiblock0>
{
	public BlockIIMetalMultiblock0()
	{
		super("metal_multiblock", Material.IRON, PropertyEnum.create("type", IIBlockTypes_MetalMultiblock0.class), ItemBlockIEBase.class, IEProperties.FACING_HORIZONTAL,
				IEProperties.BOOLEANS[0], IEProperties.BOOLEANS[1], IEProperties.MULTIBLOCKSLAVE, IEProperties.DYNAMICRENDER, IOBJModelCallback.PROPERTY, Properties.AnimationProperty);
		setHardness(3.0F);
		setResistance(15.0F);
		lightOpacity = 0;
		this.setAllNotNormalBlock();

		//DO NOT USE MIPMAPPING! UV ISSUES
		setBlockLayer(BlockRenderLayer.CUTOUT);

		addToTESRMap(IIBlockTypes_MetalMultiblock0.PRINTING_PRESS);
		addToTESRMap(IIBlockTypes_MetalMultiblock0.RADIO_STATION);
		addToTESRMap(IIBlockTypes_MetalMultiblock0.AMMUNITION_FACTORY);
		addToTESRMap(IIBlockTypes_MetalMultiblock0.ELECTROLYZER);
		addToTESRMap(IIBlockTypes_MetalMultiblock0.CHEMICAL_BATH);
		addToTESRMap(IIBlockTypes_MetalMultiblock0.PRECISSION_ASSEMBLER);
		addToTESRMap(IIBlockTypes_MetalMultiblock0.PACKER);

	}

	@Override
	public boolean useCustomStateMapper()
	{
		return true;
	}

	@Override
	@Nullable
	public String getCustomStateMapping(int meta, boolean itemBlock)
	{
		if(IIBlockTypes_MetalMultiblock0.values()[meta].needsCustomState())
			return IIBlockTypes_MetalMultiblock0.values()[meta].getCustomState();
		return null;
	}

	@Nonnull
	@Override
	public EnumBlockRenderType getRenderType(@Nonnull IBlockState state)
	{
		switch(state.getValue(property))
		{
			case BALLISTIC_COMPUTER:
			case ARTILLERY_HOWITZER:
				return EnumBlockRenderType.MODEL;
			default:
				return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
		}
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
			case PACKER_OLD:
			{
				return new TileEntityPackerOld();
			}
			case PACKER:
			{
				return new TileEntityPacker();
			}
		}
		return null;
	}

	@Nonnull
	@Override
	protected BlockStateContainer createBlockState()
	{
		BlockStateContainer base = super.createBlockState();
		IUnlistedProperty<?>[] unlisted = (base instanceof ExtendedBlockState)?((ExtendedBlockState)base).getUnlistedProperties().toArray(new IUnlistedProperty[0]): new IUnlistedProperty[0];
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
	public boolean allowHammerHarvest(@Nonnull IBlockState state)
	{
		return true;
	}

	@Override
	public boolean canIEBlockBePlaced(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState newState, @Nonnull EnumFacing side, float hitX, float hitY, float hitZ, @Nonnull EntityPlayer player, @Nonnull ItemStack stack)
	{
		return true;
	}

	public boolean isLadder(IBlockState state, IBlockAccess world, @Nonnull BlockPos pos, @Nullable EntityLivingBase entity)
	{
		TileEntity te = world.getTileEntity(pos);
		return te instanceof ILadderMultiblock&&((ILadderMultiblock)te).isLadder();
	}

	@Override
	public float getExplosionResistance(World world, @Nonnull BlockPos pos, Entity exploder, @Nonnull Explosion explosion)
	{
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof IExplosionResistantMultiblock)
			return ((IExplosionResistantMultiblock)te).getExplosionResistance()/5f;
		return super.getExplosionResistance(world, pos, exploder, explosion);
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		super.breakBlock(world, pos, state);
	}

	public float getBlockResistance()
	{
		return blockResistance;
	}
}
