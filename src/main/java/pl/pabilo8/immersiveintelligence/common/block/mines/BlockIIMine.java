package pl.pabilo8.immersiveintelligence.common.block.mines;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumFuseTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmo;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmoComponent;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmoCore;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.mines.BlockIIMine.IIBlockTypes_Mine;
import pl.pabilo8.immersiveintelligence.common.block.mines.tileentity.TileEntityTripMine;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIITileProvider;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockProperties;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IITileProviderEnum;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.TernaryValue;
import pl.pabilo8.immersiveintelligence.common.util.block.ItemBlockIIBase;

import java.util.ArrayList;
import java.util.Arrays;
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
	public boolean useCustomStateMapper()
	{
		return false;
	}

	@Override
	public String getCustomStateMapping(int meta, boolean itemBlock)
	{
		return name;
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

	public static abstract class ItemBlockMineBase extends ItemBlockIIBase implements IAmmo
	{
		public ItemBlockMineBase(BlockIIMine b)
		{
			super(b);
		}

		@Override
		public float getDefaultVelocity()
		{
			return 1f;
		}

		@Override
		public EnumCoreTypes[] getAllowedCoreTypes()
		{
			return new EnumCoreTypes[]{EnumCoreTypes.CANISTER};
		}

		public void makeDefault(ItemStack stack)
		{
			if(!ItemNBTHelper.hasKey(stack, "core"))
				ItemNBTHelper.setString(stack, "core", "core_brass");
			if(!ItemNBTHelper.hasKey(stack, "core_type"))
				ItemNBTHelper.setString(stack, "core_type", getAllowedCoreTypes()[0].getName());
			if(stack.getMetadata()==IIBlockTypes_Mine.MAIN.getMeta()&&!ItemNBTHelper.hasKey(stack, "fuse"))
				ItemNBTHelper.setString(stack, "fuse", getAllowedFuseTypes()[0].getName());
		}

		@Override
		public IAmmoCore getCore(ItemStack stack)
		{
			if(!ItemNBTHelper.hasKey(stack, "core"))
				makeDefault(stack);
			return AmmoRegistry.INSTANCE.getCore(ItemNBTHelper.getString(stack, "core"));
		}

		@Override
		public EnumCoreTypes getCoreType(ItemStack stack)
		{
			if(!ItemNBTHelper.hasKey(stack, "core_type"))
				makeDefault(stack);
			return EnumCoreTypes.v(ItemNBTHelper.getString(stack, "core_type"));
		}

		@Override
		public int getPaintColor(ItemStack stack)
		{
			if(ItemNBTHelper.hasKey(stack, "paint_color"))
				return ItemNBTHelper.getInt(stack, "paint_color");
			return -1;
		}

		@Override
		public void registerSprites(TextureMap map)
		{

		}

		@Override
		public ItemStack getBulletWithParams(String core, String coreType, String... components)
		{
			ItemStack stack = new ItemStack(this, 1, IIBlockTypes_Mine.MAIN.getMeta());
			ItemNBTHelper.setString(stack, "core", core);
			ItemNBTHelper.setString(stack, "core_type", coreType);
			NBTTagList tagList = new NBTTagList();
			Arrays.stream(components).map(NBTTagString::new).forEachOrdered(tagList::appendTag);

			if(tagList.tagCount() > 0)
			{
				ItemNBTHelper.getTag(stack).setTag("components", tagList);
				NBTTagList nbt = new NBTTagList();
				for(int i = 0; i < tagList.tagCount(); i += 1)
					nbt.appendTag(new NBTTagCompound());

				ItemNBTHelper.getTag(stack).setTag("component_nbt", nbt);
			}

			return stack;
		}

		@Override
		public ItemStack getBulletCore(String core, String coreType)
		{
			ItemStack stack = new ItemStack(this, 1, IIBlockTypes_Mine.CORE.getMeta());
			ItemNBTHelper.setString(stack, "core", core);
			ItemNBTHelper.setString(stack, "core_type", coreType);
			return stack;
		}

		@Override
		public boolean isBulletCore(ItemStack stack)
		{
			return stack.getMetadata()==IIBlockTypes_Mine.CORE.getMeta();
		}

		@Override
		public EnumFuseTypes[] getAllowedFuseTypes()
		{
			return new EnumFuseTypes[]{EnumFuseTypes.CONTACT};
		}

		@Override
		public void setFuseType(ItemStack stack, EnumFuseTypes type)
		{
			ItemNBTHelper.setString(stack, "fuse", type.getName());
		}

		@Override
		public EnumFuseTypes getFuseType(ItemStack stack)
		{
			if(!ItemNBTHelper.hasKey(stack, "fuse"))
				makeDefault(stack);
			return EnumFuseTypes.v(ItemNBTHelper.getString(stack, "fuse"));
		}

		@Override
		public boolean isProjectile()
		{
			return false;
		}

		@Override
		public IAmmoComponent[] getComponents(ItemStack stack)
		{
			if(ItemNBTHelper.hasKey(stack, "components"))
			{
				ArrayList<IAmmoComponent> arrayList = new ArrayList<>();
				NBTTagList components = (NBTTagList)ItemNBTHelper.getTag(stack).getTag("components");
				for(int i = 0; i < components.tagCount(); i++)
					arrayList.add(AmmoRegistry.INSTANCE.getComponent(components.getStringTagAt(i)));
				return arrayList.toArray(new IAmmoComponent[0]);
			}
			return new IAmmoComponent[0];
		}

		@Override
		public NBTTagCompound[] getComponentsNBT(ItemStack stack)
		{
			if(ItemNBTHelper.hasKey(stack, "component_nbt"))
			{
				ArrayList<NBTTagCompound> arrayList = new ArrayList<>();
				NBTTagList components = (NBTTagList)ItemNBTHelper.getTag(stack).getTag("component_nbt");
				for(int i = 0; i < components.tagCount(); i++)
					arrayList.add(components.getCompoundTagAt(i));
				return arrayList.toArray(new NBTTagCompound[0]);
			}
			return new NBTTagCompound[0];
		}

		@Override
		public void addComponents(ItemStack stack, IAmmoComponent component, NBTTagCompound componentNBT)
		{
			NBTTagList comps = ItemNBTHelper.getTag(stack).getTagList("components", 8);
			NBTTagList nbts = ItemNBTHelper.getTag(stack).getTagList("component_nbt", 10);

			comps.appendTag(new NBTTagString(component.getName()));
			nbts.appendTag(componentNBT.copy());

			ItemNBTHelper.getTag(stack).setTag("components", comps);
			ItemNBTHelper.getTag(stack).setTag("component_nbt", nbts);
		}

		@Override
		public ItemStack setPaintColour(ItemStack stack, int color)
		{
			ItemNBTHelper.setInt(stack, "paint_color", color);
			return stack;
		}

		@Override
		public ItemStack setComponentNBT(ItemStack stack, NBTTagCompound... tagCompounds)
		{
			NBTTagList component_nbt = new NBTTagList();
			for(NBTTagCompound tagCompound : tagCompounds)
				component_nbt.appendTag(tagCompound);
			assert stack.getTagCompound()!=null;
			stack.getTagCompound().setTag("component_nbt", component_nbt);
			return stack;
		}
	}
}