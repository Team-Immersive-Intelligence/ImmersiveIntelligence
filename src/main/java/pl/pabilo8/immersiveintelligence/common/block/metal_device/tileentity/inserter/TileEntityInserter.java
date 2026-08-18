package pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.inserter;

import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.common.util.FakePlayerUtil;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import pl.pabilo8.immersiveintelligence.api.crafting.IngredientReference;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataHandlingUtils;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.utils.MinecartBlockHelper;
import pl.pabilo8.immersiveintelligence.api.utils.minecart.IMinecartBlockPickable;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Inserter;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A machine that can transfer items between containers based on a list of tasks.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 12.08.2026
 * @since 15.07.2019
 */
public class TileEntityInserter extends TileEntityInserterBase
{
	public static final LinkedHashMap<String, Supplier<InserterTask>> TASKS = new LinkedHashMap<>();
	private static final Set<String> WIRES = ImmutableSet.of(WireType.LV_CATEGORY, WireType.MV_CATEGORY);

	static
	{
		TASKS.put("item", InserterTaskItem::new);
		TASKS.put("place_block", InserterTaskPlaceBlock::new);
		TASKS.put("from_minecart", InserterTaskFromMinecart::new);
		TASKS.put("into_minecart", InserterTaskIntoMinecart::new);
	}

	@Nonnull
	@Override
	protected Set<String> getAcceptedPowerWires()
	{
		return WIRES;
	}

	@Override
	public int getPickupSpeed()
	{
		return Inserter.taskTime;
	}

	@Override
	public int getEnergyUsage()
	{
		return Inserter.energyUsage;
	}

	@Override
	public int getEnergyCapacity()
	{
		return Inserter.energyCapacity;
	}

	@Override
	public int getMaxTakeAmount()
	{
		return Inserter.maxTake;
	}

	@Nonnull
	@Override
	public LinkedHashMap<String, Supplier<InserterTask>> getAvailableTasks()
	{
		return TASKS;
	}

	@Override
	public void onPacketReceive(DataPacket packet)
	{
		super.onPacketReceive(packet);
		taskRetryDelay = 0;
		final boolean[] changed = {false};

		IIDataHandlingUtils.expectingStringParam('c', packet, command -> {
			DataType s = packet.get('s');
			DataType a = packet.get('a');
			DataType i = packet.get('i');
			DataType o = packet.get('o');

			switch(command)
			{
				case "add":
				{
					if(packet.has('a')&&getAvailableTasks().containsKey(a.toString()))
					{
						Supplier<InserterTask> supplier = getAvailableTasks().get(a.toString());
						InserterTask task = supplier.get();

						if(packet.has('i'))
						{
							EnumFacing f = null;
							if(i instanceof DataTypeInteger)
								f = EnumFacing.getHorizontal(EnumFacing.getFront(((DataTypeInteger)i).value).getHorizontalIndex());
							else if(i instanceof DataTypeString)
							{
								String ss = i.toString().toUpperCase();
								f = Arrays.stream(EnumFacing.values()).filter(e -> e.name().equals(ss)).findFirst().orElse(null);
							}
							if(f!=null)
								task.facingIn = f;
						}

						if(packet.has('o'))
						{
							EnumFacing f = null;
							if(o instanceof DataTypeInteger)
								f = EnumFacing.getHorizontal(EnumFacing.getFront(((DataTypeInteger)o).value).getHorizontalIndex());
							else if(o instanceof DataTypeString)
							{
								String ss = o.toString().toUpperCase();
								f = Arrays.stream(EnumFacing.values()).filter(e -> e.name().equals(ss)).findFirst().orElse(null);
							}
							if(f!=null)
								task.facingOut = f;
						}

						IIDataHandlingUtils.expectingIntegerParam('1', packet,
								integer -> task.distanceIn = MathHelper.clamp(integer, -1, 2));
						IIDataHandlingUtils.expectingIntegerParam('0', packet,
								integer -> task.distanceOut = MathHelper.clamp(integer, -1, 2));

						if(packet.has('s'))
							task.stack = IIDataHandlingUtils.ingredientFromData(packet.get('s'));

						if(packet.has('e'))
						{
							int requested = packet.get('e') instanceof DataTypeInteger?
									packet.getVarInType(DataTypeInteger.class, packet.get('e')).value:
									task.stack.inputSize;
							task.overrideTakeAmount = task.stack.inputSize;
							task.stack.inputSize = requested;
							task.isJob = false;
						}

						if(packet.has('t'))
						{
							task.overrideTakeAmount = MathHelper.clamp(
									packet.getVarInType(DataTypeInteger.class, packet.get('t')).value, 1, 64);
							task.strictAmount = true;
						}
						tasks.add(task);
						changed[0] = true;
					}
				}
				break;
				case "remove":
				{
					int before = tasks.size();
					if(a instanceof DataTypeInteger)
					{
						int index = ((DataTypeInteger)a).value;
						if(index >= 0&&index < tasks.size())
							tasks.remove(index);
					}
					else
					{
						IngredientReference filter = IIDataHandlingUtils.ingredientFromData(s);
						Predicate<InserterTask> p = filter.isWildcard()&&!filter.hasLogisticTag()?
								task -> true: task -> task.stack.equals(filter);

						if(packet.has('a'))
							p = p.and(task -> task.getName().equals(a.toString()));
						tasks.removeIf(p);
					}
					changed[0] = tasks.size()!=before;
				}
				break;
				case "clear":
				{
					changed[0] = !tasks.isEmpty();
					tasks.clear();
					current = null;
					currentTaskIndex = -1;
				}
				break;
			}
		});

		refreshCurrentTask();
		if(changed[0])
		{
			markDirty();
			if(!world.isRemote)
				updateTileForEvent(SyncEvents.TILE_RECIPE_CHANGED);
		}
	}

	/**
	 * Take items from one container and place in another one.
	 * Works as a base task for others.
	 */
	public static class InserterTaskItem extends InserterTask
	{
		private transient ItemStack plannedStack = ItemStack.EMPTY;

		public InserterTaskItem()
		{
			super();
		}

		@Override
		@ParametersAreNonnullByDefault
		public boolean canExecute(TileEntityInserterBase tile, World world, BlockPos posIn, BlockPos posOut, EnumFacing facingIn, EnumFacing facingOut, boolean in)
		{
			EnumFacing facing = in?facingIn: facingOut;
			IItemHandler handler = getTargetCapability(world, in?posIn: posOut,
					CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite(), in);
			if(handler==null)
				return false;

			if(!in)
			{
				ItemStack held = tile.inventory.get(0);
				ItemStack candidate = held.isEmpty()?plannedStack.copy(): held.copy();
				if(!held.isEmpty()&&!plannedStack.isEmpty())
				{
					if(!ItemHandlerHelper.canItemStacksStack(held, plannedStack))
						return false;
					candidate.grow(plannedStack.getCount());
				}
				if(candidate.isEmpty())
					return false;

				ItemStack remainder = ItemHandlerHelper.insertItem(handler, candidate, true);
				int accepted = candidate.getCount()-remainder.getCount();
				if(accepted <= 0||accepted < held.getCount())
					return false;

				int acceptedPlanned = accepted-held.getCount();
				if(strictAmount&&acceptedPlanned < plannedStack.getCount())
					return false;
				if(acceptedPlanned < plannedStack.getCount())
					plannedStack.setCount(acceptedPlanned);
				return plannedStack.isEmpty()?remainder.isEmpty(): acceptedPlanned > 0;
			}

			int requested = getAmountToBeTaken(tile);
			if(requested <= 0)
				return false;

			ItemStack held = tile.inventory.get(0);
			int room = Math.max(0, tile.getSlotLimit(0)-held.getCount());
			if(room <= 0)
				return false;

			int available = 0;
			ItemStack selected = ItemStack.EMPTY;
			for(int slot = 0; slot < handler.getSlots()&&available < requested&&available < room; slot++)
			{
				ItemStack source = handler.getStackInSlot(slot);
				if(!matches(source))
					continue;
				if(!held.isEmpty()&&!ItemHandlerHelper.canItemStacksStack(held, source))
					continue;
				if(!selected.isEmpty()&&!ItemHandlerHelper.canItemStacksStack(selected, source))
					continue;
				if(selected.isEmpty())
					selected = source;

				int amount = Math.min(requested-available, room-available);
				ItemStack simulated = handler.extractItem(slot, amount, true);
				available += simulated.getCount();
			}

			if(available <= 0||strictAmount&&available < requested)
				return false;
			plannedStack = selected.copy();
			plannedStack.setCount(available);
			return true;
		}

		@Override
		public boolean execute(TileEntityInserterBase tile, World world, BlockPos posIn, BlockPos posOut, EnumFacing facingIn, EnumFacing facingOut, boolean in)
		{
			EnumFacing facing = in?facingIn: facingOut;
			IItemHandler handler = getTargetCapability(world, in?posIn: posOut,
					CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite(), in);
			if(handler==null)
				return false;

			if(!in)
			{
				ItemStack held = tile.inventory.get(0);
				if(held.isEmpty())
					return false;
				ItemStack remainder = ItemHandlerHelper.insertItem(handler, held, false);
				tile.inventory.set(0, remainder);
				return remainder.isEmpty();
			}

			if(plannedStack.isEmpty())
				return false;

			int targetAmount = plannedStack.getCount();
			int moved = 0;
			ItemStack held = tile.inventory.get(0);
			for(int slot = 0; slot < handler.getSlots()&&moved < targetAmount; slot++)
			{
				ItemStack source = handler.getStackInSlot(slot);
				if(!matches(source)||!ItemHandlerHelper.canItemStacksStack(plannedStack, source))
					continue;
				if(!held.isEmpty()&&!ItemHandlerHelper.canItemStacksStack(held, source))
					continue;

				int amount = targetAmount-moved;
				ItemStack simulated = handler.extractItem(slot, amount, true);
				if(simulated.isEmpty())
					continue;
				ItemStack rejected = tile.insertionHandler.insertItem(0, simulated, true);
				int accepted = simulated.getCount()-rejected.getCount();
				if(accepted <= 0)
					continue;

				ItemStack extracted = handler.extractItem(slot, accepted, false);
				ItemStack left = tile.insertionHandler.insertItem(0, extracted, false);
				int inserted = extracted.getCount()-left.getCount();
				if(!left.isEmpty())
					handler.insertItem(slot, left, false);
				moved += inserted;
			}

			if(!isJob&&moved > 0)
				stack.inputSize = Math.max(0, stack.inputSize-moved);
			return moved==targetAmount;
		}

		protected final void setPlannedStack(ItemStack stack)
		{
			plannedStack = stack.copy();
		}

		protected final ItemStack getPlannedStack()
		{
			return plannedStack;
		}

		protected boolean matches(ItemStack source)
		{
			return !source.isEmpty()&&stack.matchesItemStackIgnoringSize(source);
		}

		@Override
		protected void clearCachedTargets()
		{
			super.clearCachedTargets();
			plannedStack = ItemStack.EMPTY;
		}

		@Override
		public String getName()
		{
			return "item";
		}

		@Override
		public float getTimeModifier()
		{
			return 0;
		}
	}

	/**
	 * Take a block from a container and place it on the ground
	 */
	public static class InserterTaskPlaceBlock extends InserterTaskItem
	{
		public InserterTaskPlaceBlock()
		{
			super();
		}

		@Override
		public boolean canExecute(TileEntityInserterBase tile, World world, BlockPos posIn, BlockPos posOut, EnumFacing facingIn, EnumFacing facingOut, boolean in)
		{
			if(in)
				return super.canExecute(tile, world, posIn, posOut, facingIn, facingOut, true);

			if(!isJob&&stack.inputSize <= 0)
				return false;
			ItemStack target = tile.inventory.get(0).isEmpty()?getPlannedStack(): tile.inventory.get(0);
			return !target.isEmpty()&&target.getItem() instanceof ItemBlock
					&&world.mayPlace(((ItemBlock)target.getItem()).getBlock(), posOut, false, facingOut, null);
		}

		@Override
		public boolean execute(TileEntityInserterBase tile, World world, BlockPos posIn, BlockPos posOut, EnumFacing facingIn, EnumFacing facingOut, boolean in)
		{
			if(in)
				return super.execute(tile, world, posIn, posOut, facingIn, facingOut, true);

			if(!isJob&&stack.inputSize <= 0)
				return false;

			ItemStack held = tile.inventory.get(0);
			if(held.isEmpty()||!(held.getItem() instanceof ItemBlock)
					||!world.mayPlace(((ItemBlock)held.getItem()).getBlock(), posOut, false, facingOut, null))
				return false;

			FakePlayer fakePlayer = FakePlayerUtil.getFakePlayer(world);
			IBlockState state = ((ItemBlock)held.getItem()).getBlock().getStateForPlacement(
					world, posOut, facingOut, 0.5f, 0.5f, 0.5f, held.getMetadata(), fakePlayer);
			boolean placed = ((ItemBlock)held.getItem()).placeBlockAt(
					held, fakePlayer, world, posOut, facingOut,
					0.5f, 0.5f, 0.5f, state
			);
			if(placed)
				held.shrink(1);
			return placed;
		}

		@Override
		protected int getAmountToBeTaken(TileEntityInserterBase tile)
		{
			return Math.min(1, super.getAmountToBeTaken(tile));
		}

		@Override
		public String getName()
		{
			return "place_block";
		}

		@Override
		public float getTimeModifier()
		{
			return 0.5f;
		}
	}

	/**
	 * Take a crate or barrel from a minecart and load it into a container
	 */
	public static class InserterTaskFromMinecart extends InserterTaskItem
	{
		@Nullable
		private transient EntityMinecart cachedMinecart;

		public InserterTaskFromMinecart()
		{
			super();
		}

		@Override
		public boolean canExecute(TileEntityInserterBase tile, World world, BlockPos posIn, BlockPos posOut, EnumFacing facingIn, EnumFacing facingOut, boolean in)
		{
			if(!in)
				return super.canExecute(tile, world, posIn, posOut, facingIn, facingOut, false);

			if(!isJob&&stack.inputSize <= 0)
				return false;

			EntityMinecart cart = getMinecart(world, posIn);
			if(!(cart instanceof IMinecartBlockPickable))
				return false;

			ItemStack carried = ((IMinecartBlockPickable)cart).getBlockForPickup().getFirst();
			if(!matches(carried))
				return false;
			setPlannedStack(carried);
			return true;
		}

		@Override
		public boolean execute(TileEntityInserterBase tile, World world, BlockPos posIn, BlockPos posOut, EnumFacing facingIn, EnumFacing facingOut, boolean in)
		{
			if(!in)
				return super.execute(tile, world, posIn, posOut, facingIn, facingOut, false);

			if(!isJob&&stack.inputSize <= 0)
				return false;

			EntityMinecart cart = getMinecart(world, posIn);
			if(!(cart instanceof IMinecartBlockPickable))
				return false;

			Tuple<ItemStack, EntityMinecart> block = ((IMinecartBlockPickable)cart).getBlockForPickup();
			if(!matches(block.getFirst()))
				return false;
			tile.inventory.set(0, block.getFirst().copy());

			EntityMinecart replacement = MinecartBlockHelper.getMinecartFromBlockStack(ItemStack.EMPTY, world);
			replacement.setPosition(cart.posX, cart.posY, cart.posZ);
			cart.setDead();
			world.spawnEntity(replacement);

			if(!isJob)
				stack.inputSize = Math.max(0, stack.inputSize-1);
			return true;
		}

		@Nullable
		private EntityMinecart getMinecart(World world, BlockPos pos)
		{
			if(cachedMinecart!=null&&!cachedMinecart.isDead)
				return cachedMinecart;
			cachedMinecart = world.getEntitiesWithinAABB(EntityMinecart.class, new AxisAlignedBB(pos),
					entity -> entity instanceof IMinecartBlockPickable).stream().findFirst().orElse(null);
			return cachedMinecart;
		}

		@Override
		protected void clearCachedTargets()
		{
			super.clearCachedTargets();
			cachedMinecart = null;
		}

		@Override
		public String getName()
		{
			return "from_minecart";
		}

		@Override
		public float getTimeModifier()
		{
			return 2f;
		}
	}

	/**
	 * Take a crate or barrel from a container and load it onto a minecart
	 */
	public static class InserterTaskIntoMinecart extends InserterTaskItem
	{
		@Nullable
		private transient EntityMinecartEmpty cachedMinecart;

		public InserterTaskIntoMinecart()
		{
			super();
		}

		@Override
		public boolean canExecute(TileEntityInserterBase tile, World world, BlockPos posIn, BlockPos posOut, EnumFacing facingIn, EnumFacing facingOut, boolean in)
		{
			if(in)
				return super.canExecute(tile, world, posIn, posOut, facingIn, facingOut, true);

			if(!isJob&&stack.inputSize <= 0)
				return false;
			ItemStack target = tile.inventory.get(0).isEmpty()?getPlannedStack(): tile.inventory.get(0);
			return !target.isEmpty()&&getMinecart(world, posOut)!=null;
		}

		@Override
		public boolean execute(TileEntityInserterBase tile, World world, BlockPos posIn, BlockPos posOut, EnumFacing facingIn, EnumFacing facingOut, boolean in)
		{
			if(in)
				return super.execute(tile, world, posIn, posOut, facingIn, facingOut, true);

			if(!isJob&&stack.inputSize <= 0)
				return false;

			EntityMinecartEmpty cart = getMinecart(world, posOut);
			if(cart==null||tile.inventory.get(0).isEmpty())
				return false;

			ItemStack placed = tile.inventory.get(0).copy();
			placed.setCount(1);
			tile.inventory.get(0).shrink(1);

			EntityMinecart replacement = MinecartBlockHelper.getMinecartFromBlockStack(placed, world);
			replacement.setPosition(cart.posX, cart.posY, cart.posZ);
			((IMinecartBlockPickable)replacement).setMinecartBlock(placed);
			cart.setDead();
			world.spawnEntity(replacement);
			return true;
		}

		@Nullable
		private EntityMinecartEmpty getMinecart(World world, BlockPos pos)
		{
			if(cachedMinecart!=null&&!cachedMinecart.isDead)
				return cachedMinecart;
			cachedMinecart = world.getEntitiesWithinAABB(EntityMinecartEmpty.class, new AxisAlignedBB(pos))
					.stream().findFirst().orElse(null);
			return cachedMinecart;
		}

		@Override
		protected void clearCachedTargets()
		{
			super.clearCachedTargets();
			cachedMinecart = null;
		}

		@Override
		protected int getAmountToBeTaken(TileEntityInserterBase tile)
		{
			return Math.min(1, super.getAmountToBeTaken(tile));
		}

		@Override
		public String getName()
		{
			return "into_minecart";
		}

		@Override
		public float getTimeModifier()
		{
			return 2f;
		}
	}

}
