package pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.inserter;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.common.util.FakePlayerUtil;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeItemStack;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.utils.MinecartBlockHelper;
import pl.pabilo8.immersiveintelligence.api.utils.minecart.IMinecartBlockPickable;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Inserter;
import pl.pabilo8.immersiveintelligence.common.IIUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Pabilo8
 * @since 15-07-2019
 */
public class TileEntityInserter extends TileEntityInserterBase
{
	public static final HashMap<String, Function<NBTTagCompound, InserterTask>> TASKS = new HashMap<>();
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
	protected HashMap<String, Function<NBTTagCompound, InserterTask>> getAvailableTasks()
	{
		return TASKS;
	}

	// TODO: 21.12.2021 sounds
	@SideOnly(Side.CLIENT)
	@Override
	protected void handleSounds()
	{
		/*if(world.isRemote&&world.getTotalWorldTime()%10==0)
		{
			if(armDirection!=nextDirection)
			{
				world.playSound(ClientUtils.mc().player, getPos(), IISounds.inserter_backward, SoundCategory.BLOCKS, .25f, 1);
			}
			else if(pickProgress!=nextPickProgress)
			{
				world.playSound(ClientUtils.mc().player, getPos(), IISounds.inserter_forward, SoundCategory.BLOCKS, .25f, 1);
			}
		}*/
	}

	// TODO: 21.12.2021 compat
	@Override
	public void onPacketReceive(DataPacket packet)
	{
		super.onPacketReceive(packet);

		DataType c = packet.getPacketVariable('c');
		DataType m = packet.getPacketVariable('m');
		DataType s = packet.getPacketVariable('s');
		DataType a = packet.getPacketVariable('a');

		DataType i = packet.getPacketVariable('i');
		DataType o = packet.getPacketVariable('o');

		//old inserter compat
		if(m.toString().equals("set")||m.toString().equals("add"))
		{
			DataTypeInteger count = packet.getVarInType(DataTypeInteger.class, c);
			IngredientStack ss;
			if(packet.hasVariable('s'))
			{
				ss = new IngredientStack(packet.getVarInType(DataTypeItemStack.class, s).value);
				ss.inputSize = count.value;
			}
			else
				ss = new IngredientStack("*", count.value);


			InserterTaskItem task = new InserterTaskItem(ss, null, null);
			task.overrideTakeAmount = 1;
			task.isJob = false;
			tasks.add(task);
		}
		//new inserter
		else
		{
			/*
			c: command: add/remove/clear
			a: action: item, place_block, minecart_in, minecart_out, etc.
			s: (optional) {stack} or string
			e: (optional) expires after @e items/MBs
			t: (optional) override take amount

			i: (optional) input direction - string or int
			o: (optional) output direction - string or int

			1: (optional) input distance - int
			0: (optional) output distance - int

			*/
			switch(c.toString())
			{
				case "add":
				{
					if(packet.hasVariable('a')&&TASKS.containsKey(a.toString()))
					{
						Function<NBTTagCompound, InserterTask> fun = TASKS.get(a.toString());
						InserterTask task = fun.apply(new NBTTagCompound());

						//input facing, default null
						if(packet.hasVariable('i'))
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

						//output facing, default null
						if(packet.hasVariable('o'))
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

						//1 resembles I, and 0 resembles O
						if(packet.getPacketVariable('1') instanceof DataTypeInteger)
							task.distanceIn = MathHelper.clamp(((DataTypeInteger)packet.getPacketVariable('1')).value, -1, 2);
						if(packet.getPacketVariable('0') instanceof DataTypeInteger)
							task.distanceOut = MathHelper.clamp(((DataTypeInteger)packet.getPacketVariable('0')).value, -1, 2);

						if(packet.hasVariable('s'))
							task.stack = IIUtils.ingredientFromData(packet.getPacketVariable('s'));

						//expires (requests - tasks ending after r amount of items)
						if(packet.hasVariable('e'))
						{

							int requested =
									packet.getPacketVariable('e') instanceof DataTypeInteger?
											packet.getVarInType(DataTypeInteger.class, packet.getPacketVariable('e')).value:
											task.stack.inputSize;
							task.overrideTakeAmount = task.stack.inputSize;
							task.stack.inputSize = requested;
							task.isJob = false;
						}

						//overrides amount of items per operation
						if(packet.hasVariable('t'))
						{
							task.overrideTakeAmount = MathHelper.clamp(
									packet.getVarInType(DataTypeInteger.class, packet.getPacketVariable('t')).value, 1, 64);
							task.strictAmount = true;
						}
						tasks.add(task);
					}
				}
				break;
				case "remove":
				{
					/*
					uses action, but parameter is optional
					by id (int)
					{by stack, by ore}
					if 'a', check if task name matches
					*/
					if(a instanceof DataTypeInteger)
					{
						tasks.remove(((DataTypeInteger)a).value);
					}
					else
					{
						Predicate<InserterTask> p;
						if(s instanceof DataTypeString)
							p = packerTask -> packerTask.stack.oreName.equals(s.toString());
						else if(s instanceof DataTypeItemStack)
							p = packerTask -> packerTask.stack.equals(IIUtils.ingredientFromData(s));
						else
							p = packerTask -> true;

						if(packet.hasVariable('a'))
							p = p.and(task -> task.getName().equals(a.toString()));
						tasks.removeIf(p);
					}
				}
				break;
				case "clear":
				{
					tasks.clear();
					current = null;
				}
				break;

			}

			sendUpdate();
		}

	}

	/**
	 * Take items from one container and place in another one.
	 * Works as a base task for others.
	 */
	public static class InserterTaskItem extends InserterTask
	{
		public InserterTaskItem(IngredientStack stack, @Nullable EnumFacing facingIn, @Nullable EnumFacing facingOut)
		{
			super(facingIn, facingOut);
			this.stack = stack;
		}

		public InserterTaskItem(NBTTagCompound nbt)
		{
			super(nbt);
			this.stack = IngredientStack.readFromNBT(((NBTTagCompound)nbt.getTag("stack")));
		}

		@Override
		@ParametersAreNonnullByDefault
		public boolean canExecute(TileEntityInserterBase tile, World world, BlockPos posIn, BlockPos posOut, EnumFacing facingIn, EnumFacing facingOut, boolean in)
		{
			IItemHandler cap = null;

			//prioritize TEs first
			TileEntity te = world.getTileEntity(in?posIn: posOut);
			EnumFacing facing = (in?facingIn: facingOut);
			if(te!=null&&te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite()))
				cap = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite());
			else //if no TE, try to find entity with cap
			{
				Optional<Entity> first = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(in?posIn: posOut),
								input -> input.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite()))
						.stream()
						.findFirst();

				if(first.isPresent())
					cap = first.get().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite());
			}

			if(cap!=null)
			{
				if(in)
				{
					//take: overridden amount if specified
					//else : if isn't job - inserter take amount,
					//else: the most you can, either ins. take amount or items left
					int toBeTaken = overrideTakeAmount!=-1?overrideTakeAmount: Math.min(tile.takeAmount, isJob?stack.inputSize: tile.takeAmount);

					//iterate all the slots,
					for(int slot = 0; slot < cap.getSlots(); slot++)
					{
						ItemStack inSlot = cap.getStackInSlot(slot);
						if(!inSlot.isEmpty()&&("*".equals(stack.oreName)||stack.matchesItemStackIgnoringSize(inSlot)))
						{
							//amount that is finally taken
							int actualSize = Math.min(inSlot.getCount(), toBeTaken);
							cap.extractItem(slot, actualSize, true);

							//enough items
							if((toBeTaken -= actualSize)==0)
								return true;
						}
					}

					//hasn't taken enough items, but has *some* items, return whether it can take less
					return strictAmount&&!tile.inventory.get(0).isEmpty();
				}
				else
					return ItemHandlerHelper.insertItem(cap, tile.inventory.get(0), true).isEmpty();
			}
			return false;
		}

		@Override
		public boolean execute(TileEntityInserterBase tile, World world, BlockPos posIn, BlockPos posOut, EnumFacing facingIn, EnumFacing facingOut, boolean in)
		{
			IItemHandler cap = null;

			//prioritize TEs first
			TileEntity te = world.getTileEntity(in?posIn: posOut);
			EnumFacing facing = (in?facingIn: facingOut);
			if(te!=null&&te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite()))
				cap = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite());
			else //if no TE, try to find entity with cap
			{
				Optional<Entity> first = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(in?posIn: posOut),
								input -> input.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite()))
						.stream()
						.findFirst();

				if(first.isPresent())
					cap = first.get().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite());
			}

			if(cap!=null)
			{
				if(in)
				{
					//take: overridden amount if specified
					//else : if isn't a job - inserter take amount,
					//else: the most you can, either ins. take amount or items left
					int toBeTaken = overrideTakeAmount!=-1?overrideTakeAmount: Math.min(tile.takeAmount, !isJob?stack.inputSize: tile.takeAmount);

					//iterate all the slots,
					for(int slot = 0; slot < cap.getSlots(); slot++)
					{
						ItemStack inSlot = cap.getStackInSlot(slot);
						if(!inSlot.isEmpty()&&("*".equals(stack.oreName)||stack.matchesItemStackIgnoringSize(inSlot)))
						{
							//amount that is finally taken
							int actualSize = Math.min(inSlot.getCount(), toBeTaken);
							//try to input the item, if it won't fit for some reason, try to put it back
							ItemStack left = tile.insertionHandler.insertItem(0, cap.extractItem(slot, actualSize, false), false);
							//correct the amount by how many items weren't taken
							actualSize -= left.getCount();
							//if isn't a job, subtract the items to take
							if(!isJob)
								stack.inputSize = Math.max(0, stack.inputSize-actualSize);

							//enough items
							if((toBeTaken -= actualSize)==0)
								return true;
						}
					}

					//hasn't taken enough items, but has *some* items, return whether it can take less
					return strictAmount&&!tile.inventory.get(0).isEmpty();
				}
				else
				{
					tile.inventory.set(0, ItemHandlerHelper.insertItem(cap, tile.inventory.get(0), false));
					return tile.inventory.get(0).isEmpty();
				}
			}

			return false;
		}

		@Override
		public boolean shouldContinue()
		{
			return stack.inputSize > 0;
		}

		@Override
		public NBTTagCompound toNBT()
		{
			NBTTagCompound nbt = super.toNBT();
			nbt.setTag("stack", stack.writeToNBT(new NBTTagCompound()));
			return nbt;
		}

		@Override
		String getName()
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
		public InserterTaskPlaceBlock(NBTTagCompound nbt)
		{
			super(nbt);
		}

		@Override
		public boolean canExecute(TileEntityInserterBase tile, World world, BlockPos posIn, BlockPos posOut, EnumFacing facingIn, EnumFacing facingOut, boolean in)
		{
			if(!in)
			{
				return true;
			}
			return super.canExecute(tile, world, posIn, posOut, facingIn, facingOut, in);
		}

		@Override
		public boolean execute(TileEntityInserterBase tile, World world, BlockPos posIn, BlockPos posOut, EnumFacing facingIn, EnumFacing facingOut, boolean in)
		{
			if(!in)
			{
				ItemStack stack = tile.inventory.get(0);
				if(stack.isEmpty()||!(stack.getItem() instanceof ItemBlock))
					return false;

				if(world.mayPlace(((ItemBlock)stack.getItem()).getBlock(), posOut, false, facingOut, null))
				{
					FakePlayer fakePlayer = FakePlayerUtil.getFakePlayer(world);
					IBlockState state = ((ItemBlock)stack.getItem()).getBlock().getStateForPlacement(world, posOut, facingOut, 0.5f, 0.5f, 0.5f, stack.getMetadata(), fakePlayer);
					boolean b = ((ItemBlock)stack.getItem()).placeBlockAt(
							tile.inventory.get(0),
							fakePlayer,
							world,
							posOut,
							facingOut,
							0.5f,
							0.5f,
							0.5f,
							state
					);
					if(b)
						tile.inventory.get(0).shrink(1);
					//if isn't a job, subtract the items to take
					if(!isJob)
						this.stack.inputSize = Math.max(0, this.stack.inputSize-1);

					return b;
				}
				return false;
			}
			return super.execute(tile, world, posIn, posOut, facingIn, facingOut, in);
		}

		@Override
		String getName()
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
		public InserterTaskFromMinecart(NBTTagCompound nbt)
		{
			super(nbt);
		}

		@Override
		public boolean canExecute(TileEntityInserterBase tile, World world, BlockPos posIn, BlockPos posOut, EnumFacing facingIn, EnumFacing facingOut, boolean in)
		{
			if(in)
			{
				Optional<EntityMinecart> first = world.getEntitiesWithinAABB(EntityMinecart.class, new AxisAlignedBB(posIn), entity -> entity instanceof IMinecartBlockPickable)
						.stream()
						.findFirst();

				return first.isPresent();
			}
			return super.canExecute(tile, world, posIn, posOut, facingIn, facingOut, in);
		}

		@Override
		public boolean execute(TileEntityInserterBase tile, World world, BlockPos posIn, BlockPos posOut, EnumFacing facingIn, EnumFacing facingOut, boolean in)
		{
			if(in)
			{
				Optional<EntityMinecart> first = world.getEntitiesWithinAABB(EntityMinecart.class, new AxisAlignedBB(posIn), entity -> entity instanceof IMinecartBlockPickable)
						.stream()
						.findFirst();

				if(first.isPresent())
				{
					EntityMinecart cart = first.get();
					Tuple<ItemStack, EntityMinecart> block = ((IMinecartBlockPickable)cart).getBlockForPickup();
					tile.inventory.set(0, block.getFirst().copy());

					EntityMinecart c = MinecartBlockHelper.getMinecartFromBlockStack(ItemStack.EMPTY, world);
					c.setPosition(cart.posX, cart.posY, cart.posZ);
					cart.setDead();
					world.spawnEntity(c);

					if(!isJob)
						this.stack.inputSize = Math.max(0, this.stack.inputSize-1);

					return true;
				}

				return false;
			}
			return super.execute(tile, world, posIn, posOut, facingIn, facingOut, in);
		}

		@Override
		String getName()
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
		public InserterTaskIntoMinecart(NBTTagCompound nbt)
		{
			super(nbt);
		}

		@Override
		public boolean canExecute(TileEntityInserterBase tile, World world, BlockPos posIn, BlockPos posOut, EnumFacing facingIn, EnumFacing facingOut, boolean in)
		{
			if(!in)
			{
				return world.getEntitiesWithinAABB(EntityMinecartEmpty.class, new AxisAlignedBB(posOut))
						.stream()
						.findFirst().isPresent();
			}
			return super.canExecute(tile, world, posIn, posOut, facingIn, facingOut, in);
		}

		@Override
		public boolean execute(TileEntityInserterBase tile, World world, BlockPos posIn, BlockPos posOut, EnumFacing facingIn, EnumFacing facingOut, boolean in)
		{
			if(!in)
			{
				Optional<EntityMinecartEmpty> first = world.getEntitiesWithinAABB(EntityMinecartEmpty.class, new AxisAlignedBB(posOut))
						.stream()
						.findFirst();

				if(first.isPresent())
				{
					EntityMinecartEmpty cart = first.get();
					ItemStack placed = tile.inventory.get(0).copy();
					placed.setCount(1);

					tile.inventory.get(0).shrink(1);

					EntityMinecart c = MinecartBlockHelper.getMinecartFromBlockStack(placed, world);
					c.setPosition(cart.posX, cart.posY, cart.posZ);
					((IMinecartBlockPickable)c).setMinecartBlock(placed);
					cart.setDead();
					world.spawnEntity(c);

					return true;
				}
				return false;
			}
			return super.execute(tile, world, posIn, posOut, facingIn, facingOut, in);
		}

		@Override
		String getName()
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
