package pl.pabilo8.immersiveintelligence.common.block.mines;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreType;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.FuseType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.mines.BlockIIMine.IIBlockTypes_Mine;
import pl.pabilo8.immersiveintelligence.common.block.mines.tileentity.TileEntityTripMine;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoMine;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIITileProvider;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockProperties;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IITileProviderEnum;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.TernaryValue;
import pl.pabilo8.immersiveintelligence.common.util.block.ItemBlockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import java.util.function.Function;

/**
 * @author Pabilo8
 * @since 31.01.2021
 */
public abstract class BlockIIMine extends BlockIITileProvider<IIBlockTypes_Mine>
{
	public enum IIBlockTypes_Mine implements IITileProviderEnum
	{
		@IIBlockProperties(hidden = TernaryValue.TRUE)
		MAIN,
		@IIBlockProperties(hidden = TernaryValue.TRUE)
		CORE
	}

	public BlockIIMine(String name, Class<? extends TileEntity> tileClass, Function<BlockIIMine, ItemBlockMineBase> itemBlock, Object... additionalProperties)
	{
		super(name, Material.CIRCUITS, PropertyEnum.create("dummy", IIBlockTypes_Mine.class), i -> itemBlock.apply(((BlockIIMine)i)), additionalProperties);
		setHardness(0.25F);
		setResistance(1.0F);
		setLightOpacity(0);

		IIContent.TILE_ENTITIES.add(tileClass);

		addToTESRMap(IIBlockTypes_Mine.MAIN, IIBlockTypes_Mine.CORE);
	}

	@Override
	public String getMappingsExtension(int meta, boolean itemBlock)
	{
		return null;
	}

	@Override
	public final boolean hasTileEntity(IBlockState state)
	{
		return state.getValue(property)==IIBlockTypes_Mine.MAIN;
	}

	@Override
	public final TileEntity createBasicTE(IIBlockTypes_Mine type)
	{
		return type==IIBlockTypes_Mine.MAIN?getMineTileEntity(): null;
	}

	protected abstract TileEntity getMineTileEntity();

	@Override
	public boolean canIEBlockBePlaced(World world, BlockPos pos, IBlockState newState, EnumFacing side, float hitX, float hitY, float hitZ, EntityPlayer player, ItemStack stack)
	{
		return true;
	}

	@Deprecated
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return false;
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		super.neighborChanged(state, world, pos, blockIn, fromPos);
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileEntityTripMine)
		{
			TileEntityTripMine mine = (TileEntityTripMine)te;
			if(world.isAirBlock(pos.down()))
			{
				if(mine.digLevel==15)
					mine.explode();
				this.dropBlockAsItem(mine.getWorld(), pos, world.getBlockState(pos), 0);
				mine.getWorld().setBlockToAir(pos);
			}
		}
	}

	public static abstract class ItemBlockMineBase extends ItemBlockIIBase implements IAmmoTypeItem<ItemBlockMineBase, EntityAmmoMine>
	{
		public ItemBlockMineBase(BlockIIMine b)
		{
			super(b);
		}

		@Override
		public float getVelocity()
		{
			return 1f;
		}

		@Override
		public CoreType[] getAllowedCoreTypes()
		{
			return new CoreType[]{CoreType.CANISTER};
		}

		@Override
		public ItemStack getAmmoStack(AmmoCore core, CoreType coreType, FuseType fuse, AmmoComponent... components)
		{
			ItemStack stack = new ItemStack(this, 1, IIBlockTypes_Mine.MAIN.getMeta());
			EasyNBT.wrapNBT(stack)
					.withString(NBT_CORE, core.getName())
					.withString(NBT_CORE_TYPE, coreType.getName())
					.withString(NBT_FUSE, fuse.getName())
					.withList(NBT_COMPONENTS, c -> new NBTTagString(c.getName()), components)
					.withList(NBT_COMPONENTS_DATA, c -> new NBTTagCompound(), components);
			return stack;
		}

		@Override
		public ItemStack getAmmoCoreStack(AmmoCore core, CoreType coreType)
		{
			ItemStack stack = new ItemStack(this, 1, IIBlockTypes_Mine.CORE.getMeta());
			EasyNBT.wrapNBT(stack)
					.withString(NBT_CORE, core.getName())
					.withString(NBT_CORE_TYPE, coreType.getName());
			return stack;
		}

		@Override
		public boolean isBulletCore(ItemStack stack)
		{
			return stack.getMetadata()==IIBlockTypes_Mine.CORE.getMeta();
		}

		@Override
		public FuseType[] getAllowedFuseTypes()
		{
			return new FuseType[]{FuseType.CONTACT};
		}

		@Nonnull
		@Override
		public EntityAmmoMine getAmmoEntity(World world)
		{
			return new EntityAmmoMine(world);
		}
	}
}