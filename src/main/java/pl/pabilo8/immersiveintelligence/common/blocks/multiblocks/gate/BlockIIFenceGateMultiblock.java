package pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.gate;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import pl.pabilo8.immersiveintelligence.common.blocks.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.TileEntityMultiblockConnectable;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.gate.MultiblockAluminiumChainFenceGate.TileEntityAluminiumChainFenceGate;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.gate.MultiblockAluminiumFenceGate.TileEntityAluminiumFenceGate;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.gate.MultiblockSteelChainFenceGate.TileEntitySteelChainFenceGate;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.gate.MultiblockSteelFenceGate.TileEntitySteelFenceGate;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.gate.MultiblockWoodenChainFenceGate.TileEntityWoodenChainFenceGate;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.gate.MultiblockWoodenFenceGate.TileEntityWoodenFenceGate;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_FenceGate;

import javax.annotation.Nonnull;
import java.util.Arrays;

/**
 * @author Pabilo8
 * @since 20-06-2019
 */
@SuppressWarnings("deprecation")
public class BlockIIFenceGateMultiblock extends BlockIIMultiblock<IIBlockTypes_FenceGate>
{
	public BlockIIFenceGateMultiblock()
	{
		super("gate_multiblock", Material.WOOD, PropertyEnum.create("type", IIBlockTypes_FenceGate.class), ItemBlockIEBase.class,
				IEProperties.FACING_HORIZONTAL, IEProperties.DYNAMICRENDER, IEProperties.BOOLEANS[0]);
		setHardness(3.0F);
		setResistance(15.0F);
		lightOpacity = 0;

		for(int i = 0; i < IIBlockTypes_FenceGate.values().length; i++)
			this.setMetaBlockLayer(i, BlockRenderLayer.CUTOUT);

		this.setAllNotNormalBlock();
	}

	/**
	 * Sometimes this actually works ^^
	 * (unless you do weird block trickery, that is)
	 */
	@Override
	public Material getMaterial(IBlockState state)
	{
		if(state.getValue(property).getMeta() > 1)
			return Material.IRON;
		return Material.WOOD;
	}

	@Override
	public boolean useCustomStateMapper()
	{
		return true;
	}

	@Override
	public String getCustomStateMapping(int meta, boolean itemBlock)
	{
		if(IIBlockTypes_FenceGate.values()[meta].needsCustomState())
			return IIBlockTypes_FenceGate.values()[meta].getCustomState();
		return null;
	}

	@Override
	public TileEntity createBasicTE(World world, IIBlockTypes_FenceGate type)
	{
		switch(type)
		{
			case WOODEN:
				return new TileEntityWoodenFenceGate();
			case WOODEN_CHAIN:
				return new TileEntityWoodenChainFenceGate();
			case STEEL:
				return new TileEntitySteelFenceGate();
			case STEEL_CHAIN:
				return new TileEntitySteelChainFenceGate();
			case ALUMINIUM:
				return new TileEntityAluminiumFenceGate();
			case ALUMINIUM_CHAIN:
				return new TileEntityAluminiumChainFenceGate();
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
			state = ext.withProperty(IEProperties.CONNECTIONS, ((TileEntityMultiblockConnectable<?, ?>)te).genConnBlockstate());
		}
		return state;
	}
}
