package pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.wooden;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import blusunrize.immersiveengineering.common.blocks.TileEntityMultiblockPart;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.Properties;
import pl.pabilo8.immersiveintelligence.common.blocks.BlockIITileProvider;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.wooden.TileEntitySkyCrateStation.TileEntitySkyCrateStationParent;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalDevice;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_WoodenMultiblock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pabilo8 on 2019-06-05.
 */
public class BlockIIWoodenMultiblock extends BlockIITileProvider<IIBlockTypes_WoodenMultiblock>
{
	public BlockIIWoodenMultiblock()
	{
		super("wooden_multiblock", Material.WOOD, PropertyEnum.create("type", IIBlockTypes_WoodenMultiblock.class), ItemBlockIEBase.class, false, IEProperties.FACING_HORIZONTAL, IEProperties.MULTIBLOCKSLAVE, IEProperties.DYNAMICRENDER, IEProperties.BOOLEANS[0], Properties.AnimationProperty);
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
	public TileEntity createBasicTE(World world, IIBlockTypes_WoodenMultiblock type)
	{
		switch(type)
		{
			case SKYCRATE_STATION:
			{
				return new TileEntitySkyCrateStation();
			}
			case SKYCRATE_STATION_PARENT:
			{
				return new TileEntitySkyCrateStationParent();
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
	public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return getMetaFromState(state)!=IIBlockTypes_MetalDevice.AMMUNITION_CRATE.getMeta();
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		TileEntity tileEntity = world.getTileEntity(pos);
		if(tileEntity instanceof TileEntityMultiblockPart&&world.getGameRules().getBoolean("doTileDrops"))
		{
			TileEntityMultiblockPart tile = (TileEntityMultiblockPart)tileEntity;
			if(!tile.formed&&tile.pos==-1&&tile.getOriginalBlock()!=null)
				world.spawnEntity(new EntityItem(world, pos.getX()+.5, pos.getY()+.5, pos.getZ()+.5, tile.getOriginalBlock().copy()));

			if(tileEntity instanceof IInventory)
				InventoryHelper.dropInventoryItems(world, pos, (IInventory)tile);
		}
		if(tileEntity instanceof TileEntityMultiblockPart)
			((TileEntityMultiblockPart)tileEntity).disassemble();
		super.breakBlock(world, pos, state);
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		return new ArrayList<ItemStack>();
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
			return ((TileEntityMultiblockPart)te).getOriginalBlock();
		return ItemStack.EMPTY;
	}
}
