package pl.pabilo8.immersiveintelligence.common.blocks;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.DimensionBlockPos;
import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.TileEntityImmersiveConnectable;
import blusunrize.immersiveengineering.api.shader.CapabilityShader;
import blusunrize.immersiveengineering.client.models.IOBJModelCallback;
import blusunrize.immersiveengineering.common.blocks.BlockIEBase.IBlockEnum;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.*;
import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import blusunrize.immersiveengineering.common.blocks.TileEntityMultiblockPart;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.obj.OBJModel.OBJState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.api.utils.IIMultiblockInterfaces.ILadderMultiblock;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IUpgradableMachine;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.TileEntityMultiblockConnectable;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Pabilo8
 * @since 2019-05-17
 */
public abstract class BlockIITileProvider<E extends Enum<E> & IBlockEnum> extends BlockIIBase<E> implements IColouredBlock
{
	private boolean hasColours = false;

	public BlockIITileProvider(String name, Material material, PropertyEnum<E> mainProperty, Class<? extends ItemBlockIEBase> itemBlock, Object... additionalProperties)
	{
		super(name, material, mainProperty, itemBlock, additionalProperties);
	}

	private static final Map<DimensionBlockPos, TileEntity> tempTile = new HashMap<>();

	@SubscribeEvent
	public static void onTick(TickEvent.ServerTickEvent ev)
	{
		if(ev.phase==TickEvent.Phase.END)
			tempTile.clear();
	}

	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		TileEntity basic = createBasicTE(world, state.getValue(property));
		Collection<IProperty<?>> keys = state.getPropertyKeys();
		if(basic instanceof IDirectionalTile)
		{
			EnumFacing newFacing = null;
			if(keys.contains(IEProperties.FACING_HORIZONTAL))
				newFacing = state.getValue(IEProperties.FACING_HORIZONTAL);
			else if(keys.contains(IEProperties.FACING_ALL))
				newFacing = state.getValue(IEProperties.FACING_ALL);
			int type = ((IDirectionalTile)basic).getFacingLimitation();
			if(newFacing!=null)
			{
				switch(type)
				{
					case 2:
					case 4:
					case 5:
					case 6:
						if(newFacing.getAxis()==Axis.Y)
							newFacing = null;
						break;
					case 3:
						if(newFacing.getAxis()!=Axis.Y)
							newFacing = null;
						break;
				}
				if(newFacing!=null)
					((IDirectionalTile)basic).setFacing(newFacing);
			}
		}
		if(basic instanceof IAttachedIntegerProperies)
		{
			IAttachedIntegerProperies tileIntProps = (IAttachedIntegerProperies)basic;
			String[] names = ((IAttachedIntegerProperies)basic).getIntPropertyNames();
			for(String propertyName : names)
			{
				PropertyInteger property = tileIntProps.getIntProperty(propertyName);
				if(keys.contains(property))
					tileIntProps.setValue(propertyName, state.getValue(property));
			}
		}

		return basic;
	}

	@Override
	protected IBlockState getInitDefaultState()
	{
		IBlockState ret = super.getInitDefaultState();
		if(ret.getPropertyKeys().contains(IEProperties.FACING_ALL))
			ret = ret.withProperty(IEProperties.FACING_ALL, getDefaultFacing());
		else if(ret.getPropertyKeys().contains(IEProperties.FACING_HORIZONTAL))
			ret = ret.withProperty(IEProperties.FACING_HORIZONTAL, getDefaultFacing());
		return ret;
	}

	@Nullable
	public abstract TileEntity createBasicTE(World worldIn, E type);

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
//	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		TileEntity tile = world.getTileEntity(pos);
		DimensionBlockPos dpos = new DimensionBlockPos(pos, world instanceof World?((World)world).provider.getDimension(): 0);
		if(tile==null&&tempTile.containsKey(dpos))
			tile = tempTile.get(dpos);
		if(tile!=null&&(!(tile instanceof ITileDrop)||!((ITileDrop)tile).preventInventoryDrop()))
		{
			if(tile instanceof IIEInventory&&((IIEInventory)tile).getDroppedItems()!=null)
			{
				for(ItemStack s : ((IIEInventory)tile).getDroppedItems())
					if(!s.isEmpty())
						drops.add(s);
			}
			else if(tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
			{
				IItemHandler h = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
				if(h instanceof IEInventoryHandler)
					for(int i = 0; i < h.getSlots(); i++)
						if(!h.getStackInSlot(i).isEmpty())
						{
							drops.add(h.getStackInSlot(i));
							((IEInventoryHandler)h).setStackInSlot(i, ItemStack.EMPTY);
						}
			}
		}
		if(tile instanceof ITileDrop)
		{
			NonNullList<ItemStack> s = ((ITileDrop)tile).getTileDrops(harvesters.get(), state);
			drops.addAll(s);
		}
		else
			super.getDrops(drops, world, pos, state, fortune);

		tempTile.remove(dpos);
	}

	/**
	 * Called serverside after this block is replaced with another in Chunk, but before the Tile Entity is updated
	 */
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof IHasDummyBlocks)
			((IHasDummyBlocks)tile).breakDummies(pos, state);
		if(tile instanceof IImmersiveConnectable&&!world.isRemote)
			ImmersiveNetHandler.INSTANCE.clearAllConnectionsFor(Utils.toCC(tile), world, world.getGameRules().getBoolean("doTileDrops"));
		tempTile.put(new DimensionBlockPos(pos, world.provider.getDimension()), tile);
		super.breakBlock(world, pos, state);
		world.removeTileEntity(pos);
	}

	/**
	 * Spawns the block's drops in the world. By the time this is called the Block has possibly been set to air via
	 * Block.removedByPlayer
	 */
	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity tile, ItemStack stack)
	{
		if(tile instanceof IAdditionalDrops)
		{
			Collection<ItemStack> stacks = ((IAdditionalDrops)tile).getExtraDrops(player, state);
			if(stacks!=null&&!stacks.isEmpty())
				for(ItemStack s : stacks)
					if(!s.isEmpty())
						spawnAsEntity(world, pos, s);
		}
		super.harvestBlock(world, player, pos, state, tile, stack);
	}

	@Override
	public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof IEntityProof)
			return ((IEntityProof)tile).canEntityDestroy(entity);
		return super.canEntityDestroy(state, world, pos, entity);
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof ITileDrop)
		{
			ItemStack s = ((ITileDrop)tile).getPickBlock(player, world.getBlockState(pos), target);
			if(!s.isEmpty())
				return s;
		}
		Item item = Item.getItemFromBlock(this);
		return item==Items.AIR?ItemStack.EMPTY: new ItemStack(item, 1, this.damageDropped(world.getBlockState(pos)));
	}


	/**
	 * Called on server when World#addBlockEvent is called. If server returns true, then also called on the client. On the
	 * Server, this may perform additional changes to the world, like pistons replacing the block with an extended base. On
	 * the client, the update may involve replacing tile entities or effects such as sounds or particles
	 */
	@Override
	public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int eventID, int eventParam)
	{
		super.eventReceived(state, worldIn, pos, eventID, eventParam);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		return tileentity!=null&&tileentity.receiveClientEvent(eventID, eventParam);
	}

	protected EnumFacing getDefaultFacing()
	{
		return EnumFacing.NORTH;
	}

	/**
	 * Get the actual Block state of this Block at the given position. This applies properties not visible in the metadata,
	 * such as fence connections.
	 */
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		state = super.getActualState(state, world, pos);
		TileEntity tile = world.getTileEntity(pos);

		if(tile instanceof IAttachedIntegerProperies)
		{
			for(String s : ((IAttachedIntegerProperies)tile).getIntPropertyNames())
				state = applyProperty(state, ((IAttachedIntegerProperies)tile).getIntProperty(s), ((IAttachedIntegerProperies)tile).getIntPropertyValue(s));
		}

		if(tile instanceof IDirectionalTile&&(state.getPropertyKeys().contains(IEProperties.FACING_ALL)||state.getPropertyKeys().contains(IEProperties.FACING_HORIZONTAL)))
		{
			PropertyDirection prop = state.getPropertyKeys().contains(IEProperties.FACING_HORIZONTAL)?IEProperties.FACING_HORIZONTAL: IEProperties.FACING_ALL;
			state = applyProperty(state, prop, ((IDirectionalTile)tile).getFacing());
		}
		if(tile instanceof IActiveState)
		{
			IProperty boolProp = ((IActiveState)tile).getBoolProperty(IActiveState.class);
			if(state.getPropertyKeys().contains(boolProp))
				state = applyProperty(state, boolProp, ((IActiveState)tile).getIsActive());
		}

		if(tile instanceof IDualState)
		{
			IProperty boolProp = ((IDualState)tile).getBoolProperty(IDualState.class);
			if(state.getPropertyKeys().contains(boolProp))
				state = applyProperty(state, boolProp, ((IDualState)tile).getIsSecondState());
		}

		if(tile instanceof TileEntityMultiblockPart)
			state = applyProperty(state, IEProperties.MULTIBLOCKSLAVE, ((TileEntityMultiblockPart)tile).isDummy());
		else if(tile instanceof IHasDummyBlocks)
			state = applyProperty(state, IEProperties.MULTIBLOCKSLAVE, ((IHasDummyBlocks)tile).isDummy());

		if(tile instanceof IMirrorAble)
			state = applyProperty(state, ((IMirrorAble)tile).getBoolProperty(IMirrorAble.class), ((IMirrorAble)tile).getIsMirrored());

		return state;
	}

	@Override
	public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof IDirectionalTile)
		{
			if(!((IDirectionalTile)tile).canRotate(axis))
				return false;
			IBlockState state = world.getBlockState(pos);
			if(state.getPropertyKeys().contains(IEProperties.FACING_ALL)||state.getPropertyKeys().contains(IEProperties.FACING_HORIZONTAL))
			{
				PropertyDirection prop = state.getPropertyKeys().contains(IEProperties.FACING_HORIZONTAL)?IEProperties.FACING_HORIZONTAL: IEProperties.FACING_ALL;
				EnumFacing f = ((IDirectionalTile)tile).getFacing();
				int limit = ((IDirectionalTile)tile).getFacingLimitation();

				if(limit==0)
					f = EnumFacing.VALUES[(f.ordinal()+1)%EnumFacing.VALUES.length];
				else if(limit==1)
					f = axis.getAxisDirection()==AxisDirection.POSITIVE?f.rotateAround(axis.getAxis()).getOpposite(): f.rotateAround(axis.getAxis());
				else if(limit==2||limit==5)
					f = axis.getAxisDirection()==AxisDirection.POSITIVE?f.rotateY(): f.rotateYCCW();
				if(f!=((IDirectionalTile)tile).getFacing())
				{
					EnumFacing old = ((IDirectionalTile)tile).getFacing();
					((IDirectionalTile)tile).setFacing(f);
					((IDirectionalTile)tile).afterRotation(old, f);
					state = applyProperty(state, prop, ((IDirectionalTile)tile).getFacing());
					world.setBlockState(pos, state.cycleProperty(prop));
				}
			}
		}
		return false;
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		state = super.getExtendedState(state, world, pos);
		if(state instanceof IExtendedBlockState)
		{
			IExtendedBlockState extended = (IExtendedBlockState)state;
			TileEntity te = world.getTileEntity(pos);
			if(te!=null)
			{
				if(te instanceof IConfigurableSides)
					for(int i = 0; i < 6; i++)
						if(extended.getUnlistedNames().contains(IEProperties.SIDECONFIG[i]))
							extended = extended.withProperty(IEProperties.SIDECONFIG[i], ((IConfigurableSides)te).getSideConfig(i));
				if(te instanceof IAdvancedHasObjProperty)
					extended = extended.withProperty(Properties.AnimationProperty, ((IAdvancedHasObjProperty)te).getOBJState());
				else if(te instanceof IHasObjProperty)
					extended = extended.withProperty(Properties.AnimationProperty, new OBJState(((IHasObjProperty)te).compileDisplayList(), true));
				if(te instanceof IDynamicTexture)
					extended = extended.withProperty(IEProperties.OBJ_TEXTURE_REMAP, ((IDynamicTexture)te).getTextureReplacements());
				if(te instanceof IOBJModelCallback)
					extended = extended.withProperty(IOBJModelCallback.PROPERTY, (IOBJModelCallback)te);
				if(te.hasCapability(CapabilityShader.SHADER_CAPABILITY, null))
					extended = extended.withProperty(CapabilityShader.BLOCKSTATE_PROPERTY, te.getCapability(CapabilityShader.SHADER_CAPABILITY, null));
				if(te instanceof IPropertyPassthrough&&((IExtendedBlockState)state).getUnlistedNames().contains(IEProperties.TILEENTITY_PASSTHROUGH))
					extended = extended.withProperty(IEProperties.TILEENTITY_PASSTHROUGH, te);
				if(te instanceof TileEntityImmersiveConnectable&&((IExtendedBlockState)state).getUnlistedNames().contains(IEProperties.CONNECTIONS))
					extended = extended.withProperty(IEProperties.CONNECTIONS, ((TileEntityImmersiveConnectable)te).genConnBlockstate());
				if(te instanceof TileEntityMultiblockConnectable&&((IExtendedBlockState)state).getUnlistedNames().contains(IEProperties.CONNECTIONS))
					extended = extended.withProperty(IEProperties.CONNECTIONS, ((TileEntityMultiblockConnectable)te).genConnBlockstate());
			}
			state = extended;
		}

		return state;
	}

	@Override
	public void onIEBlockPlacedBy(World world, BlockPos pos, IBlockState state, EnumFacing side, float hitX, float hitY, float hitZ, EntityLivingBase placer, ItemStack stack)
	{
		TileEntity tile = world.getTileEntity(pos);

		if(tile instanceof IDirectionalTile)
		{
			EnumFacing f = ((IDirectionalTile)tile).getFacingForPlacement(placer, pos, side, hitX, hitY, hitZ);
			((IDirectionalTile)tile).setFacing(f);
			if(tile instanceof IAdvancedDirectionalTile)
				((IAdvancedDirectionalTile)tile).onDirectionalPlacement(side, hitX, hitY, hitZ, placer);
		}
		if(tile instanceof ITileDrop)
		{
			((ITileDrop)tile).readOnPlacement(placer, stack);
		}
		if(tile instanceof IHasDummyBlocks)
		{
			((IHasDummyBlocks)tile).placeDummies(pos, state, side, hitX, hitY, hitZ);
		}
		if(tile instanceof IPlacementInteraction)
		{
			((IPlacementInteraction)tile).onTilePlaced(world, pos, state, side, hitX, hitY, hitZ, placer, stack);
		}
	}

	/**
	 * Called when the block is right clicked by a player.
	 */
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		ItemStack heldItem = player.getHeldItem(hand);
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof IConfigurableSides&&Utils.isHammer(heldItem)&&!world.isRemote)
		{
			int iSide = player.isSneaking()?side.getOpposite().ordinal(): side.ordinal();
			if(((IConfigurableSides)tile).toggleSide(iSide, player))
				return true;
		}
		if(tile instanceof IDirectionalTile&&Utils.isHammer(heldItem)&&((IDirectionalTile)tile).canHammerRotate(side, hitX, hitY, hitZ, player)&&!world.isRemote)
		{
			EnumFacing f = ((IDirectionalTile)tile).getFacing();
			EnumFacing oldF = f;
			int limit = ((IDirectionalTile)tile).getFacingLimitation();

			if(limit==0)
				f = EnumFacing.VALUES[(f.ordinal()+1)%EnumFacing.VALUES.length];
			else if(limit==1)
				f = player.isSneaking()?f.rotateAround(side.getAxis()).getOpposite(): f.rotateAround(side.getAxis());
			else if(limit==2||limit==5)
				f = player.isSneaking()?f.rotateYCCW(): f.rotateY();
			((IDirectionalTile)tile).setFacing(f);
			((IDirectionalTile)tile).afterRotation(oldF, f);
			tile.markDirty();
			world.notifyBlockUpdate(pos, state, state, 3);
			world.addBlockEvent(tile.getPos(), tile.getBlockType(), 255, 0);
			return true;
		}
		if(tile instanceof IHammerInteraction&&Utils.isHammer(heldItem)&&!world.isRemote)
		{
			boolean b = ((IHammerInteraction)tile).hammerUseSide(side, player, hitX, hitY, hitZ);
			if(b)
				return b;
		}
		if(tile instanceof IPlayerInteraction)
		{
			boolean b = ((IPlayerInteraction)tile).interact(side, player, hand, heldItem, hitX, hitY, hitZ);
			if(b)
				return b;
		}
		if(tile instanceof IUpgradableMachine&&pl.pabilo8.immersiveintelligence.api.Utils.isWrench(heldItem))
		{
			IUpgradableMachine u = ((IUpgradableMachine)tile);
			if(u.getInstallProgress()==0)
			{
				pl.pabilo8.immersiveintelligence.common.CommonProxy.openUpgradeGuiForTile(player, (TileEntity & IUpgradableMachine)tile);
				return true;
			}
		}
		if(tile instanceof IGuiTile&&hand==EnumHand.MAIN_HAND&&!player.isSneaking())
		{
			TileEntity master = ((IGuiTile)tile).getGuiMaster();
			if(!world.isRemote&&master!=null&&((IGuiTile)master).canOpenGui(player))
			{
				pl.pabilo8.immersiveintelligence.common.CommonProxy.openGuiForTile(player, (TileEntity & IGuiTile)master);
			}
			return true;
		}
		return false;
	}

	/**
	 * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
	 * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
	 * block, etc.
	 */
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos)
	{
		if(!world.isRemote)
			ApiUtils.addFutureServerTask(world, () ->
			{
				if(world.isBlockLoaded(pos))
				{
					TileEntity tile = world.getTileEntity(pos);
					if(tile instanceof INeighbourChangeTile&&!tile.getWorld().isRemote)
						((INeighbourChangeTile)tile).onNeighborBlockChange(fromPos);
				}
			});
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof ILightValue)
			return ((ILightValue)te).getLightValue();
		return 0;
	}

	public BlockIITileProvider setHasColours()
	{
		this.hasColours = true;
		return this;
	}

	@Override
	public boolean hasCustomBlockColours()
	{
		return hasColours;
	}

	@Override
	public int getRenderColour(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex)
	{
		if(worldIn!=null&&pos!=null)
		{
			TileEntity tile = worldIn.getTileEntity(pos);
			if(tile instanceof IColouredTile)
				return ((IColouredTile)tile).getRenderColour(tintIndex);
		}
		return 0xffffff;
	}

	/**
	 * Get the geometry of the queried face at the given position and state. This is used to decide whether things like
	 * buttons are allowed to be placed on the face, or how glass panes connect to the face, among other things.
	 * <p>
	 * Common values are {@code SOLID}, which is the default, and {@code UNDEFINED}, which represents something that does
	 * not fit the other descriptions and will generally cause other things not to connect to the face.
	 *
	 * @return an approximation of the form of the given face
	 */
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing side)
	{
		if(normalBlockCheck(state))
			return BlockFaceShape.SOLID;
		else if(side!=null)
		{
			TileEntity te = world.getTileEntity(pos);
			if(te instanceof IFaceShape)
				return ((IFaceShape)te).getFaceShape(side);
			else
			{
				AxisAlignedBB bb = getBoundingBox(state, world, pos);
				double wMin = side.getAxis()==Axis.X?bb.minZ: bb.minX;
				double wMax = side.getAxis()==Axis.X?bb.maxZ: bb.maxX;
				double hMin = side.getAxis()==Axis.Y?bb.minZ: bb.minY;
				double hMax = side.getAxis()==Axis.Y?bb.maxZ: bb.maxY;
				if(wMin==0&&hMin==0&&wMax==1&&hMax==1)
					return BlockFaceShape.SOLID;
				else if(hMin==0&&hMax==1&&wMin==(1-wMax))
				{
					if(wMin > .375)
						return BlockFaceShape.MIDDLE_POLE_THIN;
					else if(wMin > .3125)
						return BlockFaceShape.MIDDLE_POLE;
					else
						return BlockFaceShape.MIDDLE_POLE_THICK;
				}
				else if(hMin==wMin&&hMax==wMax)
				{
					if(wMin > .375)
						return BlockFaceShape.CENTER_SMALL;
					else if(wMin > .3125)
						return BlockFaceShape.CENTER;
					else
						return BlockFaceShape.CENTER_BIG;
				}
				return BlockFaceShape.UNDEFINED;
			}
		}
		return super.getBlockFaceShape(world, state, pos, side);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		if(world.getBlockState(pos).getBlock()!=this)
			return FULL_BLOCK_AABB;
		else
		{
			TileEntity te = world.getTileEntity(pos);
			if(te instanceof IBlockBounds)
			{
				float[] bounds = ((IBlockBounds)te).getBlockBounds();
				if(bounds!=null&&bounds.length > 5)
					return new AxisAlignedBB(bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5]);
			}
		}
		return super.getBoundingBox(state, world, pos);
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB mask, List<AxisAlignedBB> list, @Nullable Entity ent, boolean isActualState)
	{
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof IAdvancedCollisionBounds)
		{
			List<AxisAlignedBB> bounds = ((IAdvancedCollisionBounds)te).getAdvancedColisionBounds();
			if(bounds!=null&&!bounds.isEmpty())
			{
				for(AxisAlignedBB aabb : bounds)
					if(aabb!=null&&mask.intersects(aabb))
						list.add(aabb);
				return;
			}
		}
		super.addCollisionBoxToList(state, world, pos, mask, list, ent, isActualState);
	}

	/**
	 * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit.
	 */
	@Override
	public RayTraceResult collisionRayTrace(IBlockState state, World world, BlockPos pos, Vec3d start, Vec3d end)
	{
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof IAdvancedSelectionBounds)
		{
			List<AxisAlignedBB> list = ((IAdvancedSelectionBounds)te).getAdvancedSelectionBounds();
			if(list!=null&&!list.isEmpty())
			{
				RayTraceResult min = null;
				double minDist = Double.POSITIVE_INFINITY;
				for(AxisAlignedBB aabb : list)
				{
					RayTraceResult mop = this.rayTrace(pos, start, end, aabb.offset(-pos.getX(), -pos.getY(), -pos.getZ()));
					if(mop!=null)
					{
						double dist = mop.hitVec.squareDistanceTo(start);
						if(dist < minDist)
						{
							min = mop;
							minDist = dist;
						}
					}
				}
				return min;
			}
		}
		return super.collisionRayTrace(state, world, pos, start, end);
	}

	@Override
	public boolean hasComparatorInputOverride(IBlockState state)
	{
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos)
	{
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof IEBlockInterfaces.IComparatorOverride)
			return ((IEBlockInterfaces.IComparatorOverride)te).getComparatorInputOverride();
		return 0;
	}


	@Override
	public int getWeakPower(IBlockState blockState, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof IEBlockInterfaces.IRedstoneOutput)
			return ((IEBlockInterfaces.IRedstoneOutput)te).getWeakRSOutput(blockState, side);
		return 0;
	}

	@Override
	public int getStrongPower(IBlockState blockState, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof IEBlockInterfaces.IRedstoneOutput)
			return ((IEBlockInterfaces.IRedstoneOutput)te).getStrongRSOutput(blockState, side);
		return 0;
	}

	/**
	 * Can this block provide power. Only wire currently seems to have this change based on its state.
	 */
	@Override
	public boolean canProvidePower(IBlockState state)
	{
		return true;
	}

	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof IEBlockInterfaces.IRedstoneOutput)
			return ((IEBlockInterfaces.IRedstoneOutput)te).canConnectRedstone(state, side);
		return false;
	}

	/**
	 * Called When an Entity Collided with the Block
	 */
	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity)
	{
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileEntityIEBase)
		{
			//ladders
			((TileEntityIEBase)te).onEntityCollision(world, entity);
			if(te instanceof ILadderMultiblock&&((ILadderMultiblock)te).isLadder()&&entity instanceof EntityLivingBase&&!((EntityLivingBase)entity).isOnLadder())
			{
				float f5 = 0.15F;
				if(entity.motionX < -f5)
					entity.motionX = -f5;
				if(entity.motionX > f5)
					entity.motionX = f5;
				if(entity.motionZ < -f5)
					entity.motionZ = -f5;
				if(entity.motionZ > f5)
					entity.motionZ = f5;

				entity.fallDistance = 0.0F;
				if(entity.motionY < -0.15D)
					entity.motionY = -0.15D;

				if(entity.motionY < 0&&entity instanceof EntityPlayer&&entity.isSneaking())
				{
					entity.motionY = 0;
					return;
				}
				if(entity.collidedHorizontally)
					entity.motionY = .2;
			}
		}
	}


}