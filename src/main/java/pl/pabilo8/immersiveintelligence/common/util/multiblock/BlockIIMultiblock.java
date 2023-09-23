package pl.pabilo8.immersiveintelligence.common.util.multiblock;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.api.MultiblockHandler.IMultiblock;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.ITileDrop;
import blusunrize.immersiveengineering.common.blocks.TileEntityMultiblockPart;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIITileProvider;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IITileMultiblockEnum;
import pl.pabilo8.immersiveintelligence.common.util.block.ItemBlockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IExplosionResistantMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.ILadderMultiblock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * @author Pabilo8
 * @since 25-06-2019
 */
public abstract class BlockIIMultiblock<E extends Enum<E> & IITileMultiblockEnum> extends BlockIITileProvider<E>
{
	public BlockIIMultiblock(String name, Material material, PropertyEnum<E> mainProperty, Object... additionalProperties)
	{
		super(name, material, mainProperty, ItemBlockIIBase::new, combineProperties(additionalProperties, IEProperties.FACING_HORIZONTAL, IEProperties.MULTIBLOCKSLAVE));

		//try getting multiblocks, if null, leave it alone
		for(E v : enumValues)
		{
			Class<? extends IMultiblock> mb = v.getMultiblock();
			if(mb!=null)
				try
				{
					IIContent.MULTIBLOCKS.add(mb.newInstance());
				} catch(InstantiationException|IllegalAccessException ignored) {}
		}
		//does not need the property to be set
		Arrays.fill(this.hidden, true);

		setLightOpacity(0);
		setFullCube(false);
		setBlockLayer(BlockRenderLayer.CUTOUT);
		setToolTypes(IIReference.TOOL_HAMMER);
	}

	//--- Other Methods ---//

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		state = super.getActualState(state, world, pos);
		return state;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		TileEntity tileEntity = world.getTileEntity(pos);
		if(tileEntity instanceof TileEntityMultiblockPart&&world.getGameRules().getBoolean("doTileDrops"))
		{
			TileEntityMultiblockPart<?> tile = (TileEntityMultiblockPart<?>)tileEntity;
			if(!tile.formed&&tile.pos==-1&&!tile.getOriginalBlock().isEmpty())
				world.spawnEntity(new EntityItem(world, pos.getX()+.5, pos.getY()+.5, pos.getZ()+.5, tile.getOriginalBlock().copy()));

			if(tile.formed&&tile instanceof IIEInventory)
			{
				IIEInventory master = (IIEInventory)tile.master();
				if(master!=null&&(!(master instanceof ITileDrop)||!((ITileDrop)master).preventInventoryDrop())&&master.getDroppedItems()!=null)
					for(ItemStack s : master.getDroppedItems())
						if(!s.isEmpty())
							world.spawnEntity(new EntityItem(world, pos.getX()+.5, pos.getY()+.5, pos.getZ()+.5, s.copy()));
			}
		}
		if(tileEntity instanceof TileEntityMultiblockPart)
			((TileEntityMultiblockPart<?>)tileEntity).disassemble();
		super.breakBlock(world, pos, state);
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		int meta = this.getMetaFromState(state);

		if(meta >= 0&&meta < this.enumValues.length)
			super.getDrops(drops, world, pos, state, fortune);
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{
		ItemStack stack = getOriginalBlock(world, pos);
		if(!stack.isEmpty())
			return stack;

		return super.getPickBlock(state, target, world, pos, player);
	}

	public ItemStack getOriginalBlock(World world, BlockPos pos)
	{
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileEntityMultiblockPart)
			return ((TileEntityMultiblockPart<?>)te).getOriginalBlock();
		return ItemStack.EMPTY;
	}

	public boolean isLadder(@Nonnull IBlockState state, IBlockAccess world, @Nonnull BlockPos pos, @Nullable EntityLivingBase entity)
	{
		TileEntity te = world.getTileEntity(pos);
		return te instanceof ILadderMultiblock&&((ILadderMultiblock)te).isLadder();
	}

	@Override
	public float getExplosionResistance(World world, @Nonnull BlockPos pos, Entity exploder, @Nonnull Explosion explosion)
	{
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof IExplosionResistantMultiblock)
		{
			float v = ((IExplosionResistantMultiblock)te).getExplosionResistance();
			if(v!=-1)
				return v/5f;
		}
		return super.getExplosionResistance(world, pos, exploder, explosion);
	}
}