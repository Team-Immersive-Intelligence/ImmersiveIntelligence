package pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.IEApi;
import blusunrize.immersiveengineering.api.crafting.IMultiblockRecipe;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.IConveyorAttachable;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedCollisionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedSelectionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.blocks.wooden.BlockTypes_WoodenDevice0;
import blusunrize.immersiveengineering.common.util.EnergyHelper;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import blusunrize.immersiveengineering.common.util.inventory.MultiFluidTank;
import blusunrize.immersiveengineering.common.util.network.MessageTileSync;
import net.minecraft.block.Block;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.Packer;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeItemStack;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;
import pl.pabilo8.immersiveintelligence.api.utils.MachineUpgrade;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IUpgradableMachine;
import pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.PackerRenderer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalDevice;
import pl.pabilo8.immersiveintelligence.common.items.ammunition.ItemIIBulletMagazine;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Pabilo8
 * @since 28-06-2019
 */
public class TileEntityPacker extends TileEntityMultiblockMetal<TileEntityPacker, IMultiblockRecipe> implements IDataDevice, IAdvancedCollisionBounds, IAdvancedSelectionBounds, IConveyorAttachable, IGuiTile, IUpgradableMachine
{
	/**
	 * Used for handling item I/O
	 */
	public static HashMap<Predicate<ItemStack>, Function<ItemStack, IItemHandler>> itemHandleMap = new HashMap<>();
	/**
	 * Used for handling fluid I/O
	 */
	public static HashMap<Predicate<ItemStack>, Function<ItemStack, FluidHandlerItemStack>> fluidHandleMap = new HashMap<>();

	static
	{
		final ItemStack[] crates = new ItemStack[]{
				new ItemStack(IEContent.blockWoodenDevice0, 1, BlockTypes_WoodenDevice0.CRATE.getMeta()),
				new ItemStack(IEContent.blockWoodenDevice0, 1, BlockTypes_WoodenDevice0.REINFORCED_CRATE.getMeta()),
				new ItemStack(IIContent.blockMetalDevice, 1, IIBlockTypes_MetalDevice.METAL_CRATE.getMeta()),

				new ItemStack(IIContent.blockSmallCrate, 1, 0),
				new ItemStack(IIContent.blockSmallCrate, 1, 1),
				new ItemStack(IIContent.blockSmallCrate, 1, 2),
				new ItemStack(IIContent.blockSmallCrate, 1, 3),
				new ItemStack(IIContent.blockSmallCrate, 1, 4),
				new ItemStack(IIContent.blockSmallCrate, 1, 5),
		};

		itemHandleMap.put(
				stack -> Arrays.stream(crates).anyMatch(s -> s.isItemEqual(stack)),
				stack -> new ItemStackHandler(Utils.readInventory(ItemNBTHelper.getTag(stack).getTagList("inventory", 10), 27))
				{
					final ItemStack ss = stack;

					@Override
					public boolean isItemValid(int slot, @Nonnull ItemStack stack)
					{
						return IEApi.isAllowedInCrate(stack);
					}

					@Override
					protected void onContentsChanged(int slot)
					{
						super.onContentsChanged(slot);
						ItemNBTHelper.getTag(ss).setTag("inventory", Utils.writeInventory(this.stacks));
					}
				}
		);

		final ItemStack[] shulkerBoxes = new ItemStack[]{
				new ItemStack(Blocks.WHITE_SHULKER_BOX),
				new ItemStack(Blocks.ORANGE_SHULKER_BOX),
				new ItemStack(Blocks.MAGENTA_SHULKER_BOX),
				new ItemStack(Blocks.LIGHT_BLUE_SHULKER_BOX),
				new ItemStack(Blocks.YELLOW_SHULKER_BOX),
				new ItemStack(Blocks.LIME_SHULKER_BOX),
				new ItemStack(Blocks.PINK_SHULKER_BOX),
				new ItemStack(Blocks.GRAY_SHULKER_BOX),
				new ItemStack(Blocks.SILVER_SHULKER_BOX),
				new ItemStack(Blocks.CYAN_SHULKER_BOX),
				new ItemStack(Blocks.PURPLE_SHULKER_BOX),
				new ItemStack(Blocks.BLUE_SHULKER_BOX),
				new ItemStack(Blocks.BROWN_SHULKER_BOX),
				new ItemStack(Blocks.GREEN_SHULKER_BOX),
				new ItemStack(Blocks.RED_SHULKER_BOX)
		};

		itemHandleMap.put(
				stack -> Arrays.stream(shulkerBoxes).anyMatch(s -> s.isItemEqual(stack)),
				stack -> new ItemStackHandler(Utils.readInventory(ItemNBTHelper.getTag(stack).getCompoundTag("BlockEntityTag").getTagList("Items", 10), 27))
				{
					final ItemStack ss = stack;

					@Override
					public boolean isItemValid(int slot, @Nonnull ItemStack stack)
					{
						return !(Block.getBlockFromItem(stack.getItem()) instanceof BlockShulkerBox);
					}

					@Override
					protected void onContentsChanged(int slot)
					{
						super.onContentsChanged(slot);
						NBTTagCompound tag = ItemNBTHelper.getTagCompound(stack, "BlockEntityTag");
						tag.setTag("Items", Utils.writeInventory(this.stacks));
						ItemNBTHelper.setTagCompound(stack, "BlockEntityTag", tag);
					}
				}
		);

		itemHandleMap.put(
				stack -> stack.getItem()==IIContent.itemBulletMagazine,
				stack -> new ItemStackHandler(ItemIIBulletMagazine.readInventory(ItemNBTHelper.getTag(stack).getCompoundTag("bullets"), ItemIIBulletMagazine.getBulletCapactity(stack)))
				{
					final ItemStack ss = stack;

					@Override
					public boolean isItemValid(int slot, @Nonnull ItemStack stack)
					{
						return stack.getItem()==ItemIIBulletMagazine.getMatchingType(ss);
					}

					@Override
					public int getSlotLimit(int slot)
					{
						return 1;
					}

					@Override
					protected void onContentsChanged(int slot)
					{
						super.onContentsChanged(slot);
						ItemNBTHelper.getTag(ss).setTag("bullets", ItemIIBulletMagazine.writeInventory(this.stacks));
					}
				}
		);


		itemHandleMap.put(
				stack -> stack.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null),
				stack -> stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)
		);
	}

	public boolean repeatActions = false;
	protected ArrayList<MachineUpgrade> upgrades = new ArrayList<>();
	MachineUpgrade currentlyInstalled = null;
	int upgradeProgress = 0;
	public int clientUpgradeProgress = 0;
	public int processTime = 0;

	public ArrayList<PackerTask> tasks = new ArrayList<>();

	/**
	 * Actual Inventory + [54] container item
	 */
	public NonNullList<ItemStack> inventory = NonNullList.withSize(54+1, ItemStack.EMPTY);

	public IItemHandler containerHandler = new IEInventoryHandler(1, this, 0, true, true);
	IItemHandler inventoryHandler = new IEInventoryHandler(54, this, 1, true, true);

	public FluxStorageAdvanced energyStorageUpgrade = new FluxStorageAdvanced(Packer.energyCapacityUpgrade);
	public MultiFluidTank fluidTank = new MultiFluidTank(Packer.fluidCapacityUpgrade);

	public TileEntityPacker()
	{
		super(MultiblockPacker.instance, MultiblockPacker.instance.getSize(), Packer.energyCapacity, true);
	}

	@Override
	public void readCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		if(isDummy())
			return;

		if(!descPacket)
			inventory = Utils.readInventory(nbt.getTagList("inventory", 10), inventory.size());
		getUpgradesFromNBT(nbt);
		repeatActions = nbt.getBoolean("repeatActions");
		if(hasUpgrade(IIContent.UPGRADE_PACKER_ENERGY))
			energyStorage.readFromNBT(nbt.getCompoundTag("energy_upgrade"));
		readTasks(nbt.getTagList("tasks", 10));
	}

	@Override
	public void writeCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);
		if(isDummy())
			return;

		if(!descPacket)
			nbt.setTag("inventory", Utils.writeInventory(inventory));
		saveUpgradesToNBT(nbt);
		nbt.setBoolean("repeatActions", repeatActions);

		if(hasUpgrade(IIContent.UPGRADE_PACKER_ENERGY))
			nbt.setTag("energy_upgrade", energyStorage.writeToNBT(new NBTTagCompound()));

		nbt.setTag("tasks", writeTasks());
	}

	@Override
	public void receiveMessageFromServer(@Nonnull NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);
		if(message.hasKey("inventory"))
			inventory = Utils.readInventory(message.getTagList("inventory", 10), inventory.size());
		if(message.hasKey("repeatActions"))
			repeatActions = message.getBoolean("repeatActions");
		if(message.hasKey("tasks"))
			readTasks(message.getTagList("tasks", 10));
	}

	@Override
	public void receiveMessageFromClient(@Nonnull NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);
		if(message.hasKey("repeatActions"))
			repeatActions = message.getBoolean("repeatActions");
		if(message.hasKey("tasks"))
			readTasks(message.getTagList("tasks", 10));
	}

	public NBTTagList writeTasks()
	{
		NBTTagList tagTasks = new NBTTagList();
		for(PackerTask task : tasks)
		{
			tagTasks.appendTag(task.toNBT());
		}
		return tagTasks;
	}

	private void readTasks(NBTTagList tagTasks)
	{
		tasks.clear();
		for(NBTBase task : tagTasks)
		{
			if(task instanceof NBTTagCompound)
				tasks.add(new PackerTask(((NBTTagCompound)task)));
		}
	}

	@Override
	public void update()
	{
		super.update();
		boolean update = false;

		if(!containerHandler.getStackInSlot(0).isEmpty())
		{
			if(processTime < Packer.actionTime)
			{
				if(processTime==0)
					update = true;

				processTime++;
				if(processTime==Packer.actionTime*0.5)
				{
					// TODO: 07.10.2021 play sounds
					// TODO: 07.10.2021 fill container item

					ItemStack packedItem = containerHandler.getStackInSlot(0);
					if(!packedItem.isEmpty()&&energyStorage.getEnergyStored() >= Packer.energyUsage)
					{
						energyStorage.extractEnergy(Packer.energyUsage, false);

						boolean unpacker = hasUpgrade(IIContent.UPGRADE_UNPACKER_CONVERSION);

						boolean repeat = true;
						int repeats = 0;
						while(repeat)
						{
							repeat = false;
							repeats++;

							if(repeats > 100)
								break;

							for(PackerTask task : tasks)
							{
								boolean isLimited = task.expirationAmount!=-1;

								//skip due to bugs
								// TODO: 31.12.2021 examine
								if(world.isRemote&&unpacker)
									break;

								switch(task.actionType)
								{
									case ITEM:
									{
										Optional<Function<ItemStack, IItemHandler>> handlerFunction = itemHandleMap.entrySet().stream().filter(p -> p.getKey().test(packedItem)).map(Entry::getValue).findFirst();
										if(!handlerFunction.isPresent())
											break;

										IItemHandler handler = handlerFunction.get().apply(packedItem);

										IItemHandler handlerIn = unpacker?handler: inventoryHandler;
										IItemHandler handlerOut = unpacker?inventoryHandler: handler;

										int slots = Math.min(task.mode==PackerPutMode.SLOT?task.stack.inputSize: Integer.MAX_VALUE, isLimited?task.expirationAmount: Integer.MAX_VALUE);
										int amount = Math.min(task.mode==PackerPutMode.AMOUNT?task.stack.inputSize: Integer.MAX_VALUE, isLimited?task.expirationAmount: Integer.MAX_VALUE);

										for(int i = 0; i < handlerIn.getSlots(); i++)
										{
											if("*".equals(task.stack.oreName)||task.stack.matchesItemStackIgnoringSize(handlerIn.extractItem(i, amount, true)))
											{
												ItemStack extracted = handlerIn.extractItem(i, amount, false);
												amount -= extracted.getCount();
												if(isLimited)
													task.expirationAmount = Math.max(0, task.expirationAmount-extracted.getCount());
												extracted = ItemHandlerHelper.insertItem(handlerOut, extracted, false);
												if(extracted.getCount()==0)
													repeat = repeatActions;
												amount += extracted.getCount();
												handlerIn.insertItem(i, extracted, false);

												slots--;
											}

											if((isLimited&&task.expirationAmount <= 0)||slots <= 0||amount <= 0)
												break;
										}

									}
									break;
									case FLUID:
									{
										if(unpacker)
										{

										}
										else
										{

										}
									}
									break;
									case ENERGY:
									{
										if(unpacker)
										{
											EnergyHelper.insertFlux(packedItem, energyStorageUpgrade.receiveEnergy(EnergyHelper.extractFlux(packedItem, 10000000, false), false), false);
										}
										else
										{
											energyStorageUpgrade.receiveEnergy(EnergyHelper.insertFlux(packedItem, energyStorageUpgrade.extractEnergy(10000000, false), false), false);
										}

									}
									break;
								}
							}

							tasks.removeIf(packerTask -> packerTask.expirationAmount==0);
						}

					}
				}
			}
			else
			{
				processTime = 0;

				if(!world.isRemote)
				{
					EnumFacing ff = mirrored?facing.rotateYCCW(): facing.rotateY();
					BlockPos pos = getBlockPosForPos(2).offset(ff, 1);
					ItemStack output = containerHandler.extractItem(0, 64, false);

					TileEntity inventoryTile = this.world.getTileEntity(pos);
					if(inventoryTile!=null)
						output = Utils.insertStackIntoInventory(inventoryTile, output, ff.getOpposite());
					if(!output.isEmpty())
						Utils.dropStackAtPos(world, pos, output);
				}
				update = true;
			}
		}

		if(world.isRemote)
		{
			if(clientUpgradeProgress < getMaxClientProgress())
				clientUpgradeProgress = (int)Math.min(clientUpgradeProgress+(Tools.wrench_upgrade_progress/2f), getMaxClientProgress());
		}
		else if(hasUpgrade(IIContent.UPGRADE_UNPACKER_CONVERSION))
		{
			BlockPos pos = getBlockPosForPos(23).offset(facing);
			TileEntity inventoryTile = this.world.getTileEntity(pos);
			if(inventoryTile!=null)
				for(int i = 0; i < inventory.size(); i++)
				{
					ItemStack output = inventory.get(i);
					if(output.isEmpty())
						continue;
					output = Utils.insertStackIntoInventory(inventoryTile, output, facing.getOpposite());
					inventory.set(i, output);
				}
		}

		if(update)
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setTag("inventory", Utils.writeInventory(inventory));
			ImmersiveEngineering.packetHandler.sendToAllAround(new MessageTileSync(this, tag), pl.pabilo8.immersiveintelligence.api.Utils.targetPointFromTile(this, 32));
		}
	}

	@Nonnull
	@Override
	public float[] getBlockBounds()
	{
		return new float[]{0, 0, 0, 1, 1, 1};
	}

	@Nonnull
	@Override
	public int[] getEnergyPos()
	{
		return new int[]{25};
	}

	@Nonnull
	@Override
	public int[] getRedstonePos()
	{
		return new int[]{10};
	}

	@Override
	public boolean isInWorldProcessingMachine()
	{
		return false;
	}

	@Override
	public void doProcessOutput(@Nonnull ItemStack output)
	{

	}

	@Override
	public void doProcessFluidOutput(@Nonnull FluidStack output)
	{
	}

	@Override
	public void onProcessFinish(@Nonnull MultiblockProcess<IMultiblockRecipe> process)
	{

	}

	@Override
	public int getMaxProcessPerTick()
	{
		return 1;
	}

	@Override
	public int getProcessQueueMaxLength()
	{
		return 1;
	}

	@Override
	public float getMinProcessDistance(@Nonnull MultiblockProcess<IMultiblockRecipe> process)
	{
		return 0;
	}

	@Override
	public NonNullList<ItemStack> getInventory()
	{
		return inventory;
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return true;
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return slot==0?1: 64;
	}

	@Nonnull
	@Override
	public int[] getOutputSlots()
	{
		return new int[]{};
	}

	@Nonnull
	@Override
	public int[] getOutputTanks()
	{
		return new int[]{};
	}

	@Override
	public boolean additionalCanProcessCheck(@Nonnull MultiblockProcess<IMultiblockRecipe> process)
	{
		return false;
	}

	@Nonnull
	@Override
	public IFluidTank[] getInternalTanks()
	{
		return new IFluidTank[]{};
	}

	@Nonnull
	@Override
	protected IFluidTank[] getAccessibleFluidTanks(@Nonnull EnumFacing side)
	{
		return new FluidTank[0];
	}

	@Override
	protected boolean canFillTankFrom(int iTank, @Nonnull EnumFacing side, @Nonnull FluidStack resource)
	{
		return false;
	}

	@Override
	protected boolean canDrainTankFrom(int iTank, @Nonnull EnumFacing side)
	{
		return false;
	}

	@Override
	public void doGraphicalUpdates(int slot)
	{

	}

	@Override
	@Nullable
	public IMultiblockRecipe findRecipeForInsertion(@Nonnull ItemStack inserting)
	{
		return null;
	}

	@Override
	@Nullable
	protected IMultiblockRecipe readRecipeFromNBT(@Nonnull NBTTagCompound tag)
	{
		return null;
	}

	@Override
	public void onReceive(DataPacket packet, EnumFacing side)
	{
		TileEntityPacker master = master();
		if(master==null)
			return;

		if(this.pos==9)
		{
			/*
			c: command: add/remove/clear
			a: action: pack/unpack/fill/drain/charge/discharge
			m: mode: amount, slot, all_possible
			e: (optional) expires after @e items/MBs
			s: (optional) {stack} or string
			*/
			IDataType a = packet.getPacketVariable('a');
			IDataType m = packet.getPacketVariable('m');
			IDataType e = packet.getPacketVariable('e');
			IDataType s = packet.getPacketVariable('s');
			IDataType c = packet.getPacketVariable('c');

			switch(c.valueToString())
			{
				case "add":
				{
					PackerPutMode mode = PackerPutMode.fromName(m.valueToString());
					PackerActionType action = PackerActionType.fromName(a.valueToString());
					IngredientStack stack = pl.pabilo8.immersiveintelligence.api.Utils.ingredientFromData(s);
					PackerTask packerTask = new PackerTask(mode, action, stack);
					if(packet.hasVariable('e'))
						packerTask.expirationAmount = packet.getVarInType(DataTypeInteger.class, e).value;
					master.tasks.add(packerTask);

				}
				break;
				case "remove":
				{
					/*uses action, but parameter is optional
					{
					 by stack
					 by ore
					}
					by id (int)
					*/
					if(a instanceof DataTypeInteger)
					{
						master.tasks.remove(((DataTypeInteger)a).value);
					}
					else
					{
						Predicate<PackerTask> p;
						if(s instanceof DataTypeString)
							p = packerTask -> packerTask.stack.oreName.equals(s.valueToString());
						else if(s instanceof DataTypeItemStack)
							p = packerTask -> packerTask.stack.equals(pl.pabilo8.immersiveintelligence.api.Utils.ingredientFromData(s));
						else
							p = packerTask -> true;

						if(packet.hasVariable('m'))
							p = p.and(packerTask -> packerTask.mode==PackerPutMode.fromName(m.valueToString()));
						if(packet.hasVariable('a'))
							p = p.and(packerTask -> packerTask.actionType==PackerActionType.fromName(a.valueToString()));
						master.tasks.removeIf(p);
					}
				}
				break;
				case "clear":
				{
					master.tasks.clear();
				}
				break;
			}
		}
	}

	@Nonnull
	@Override
	public List<AxisAlignedBB> getAdvancedSelectionBounds()
	{
		ArrayList<AxisAlignedBB> list = new ArrayList<>();
		switch(pos)
		{
			case 23:
			{
				list.add(new AxisAlignedBB(0, 0, 0, 1, 0.125f, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
				switch(facing)
				{
					case NORTH:
						list.add(new AxisAlignedBB(0, 0.125, 1-0.185, 1, 0.8125, 1-0.03125).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
						break;
					case SOUTH:
						list.add(new AxisAlignedBB(0, 0.125, 0.03125, 1, 0.8125, 0.185).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
						break;
					case EAST:
						list.add(new AxisAlignedBB(0.03125, 0.125, 0, 0.185, 0.8125, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
						break;
					case WEST:
						list.add(new AxisAlignedBB(1-0.185, 0.125, 0, 1-0.03125, 0.8125, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
						break;
				}
			}
			break;
			case 0:
			case 1:
			case 2:
			{
				list.add(new AxisAlignedBB(0, 0, 0, 1, 0.125f, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
				switch(facing)
				{
					case NORTH:
						list.add(new AxisAlignedBB(0, 0.125, 1-0.03125, 1, 1, 1-0.03125).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
						break;
					case SOUTH:
						list.add(new AxisAlignedBB(0, 0.125, 0.03125, 1, 1, 0.03125).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
						break;
					case EAST:
						list.add(new AxisAlignedBB(0.03125, 0.125, 0, 0.03125, 1, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
						break;
					case WEST:
						list.add(new AxisAlignedBB(1-0.03125, 0.125, 0, 1-0.03125, 1, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
						break;
				}
			}
			break;
			case 13:
				break;
			default:
				list.add(new AxisAlignedBB(0, 0, 0, 1, 1, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
				break;
		}

		if(pos==1||pos==13)
		{
			switch(facing)
			{
				case NORTH:
					list.add(new AxisAlignedBB(1-0.625f, 0, 0, 1-0.375f, 1, 0.125).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					break;
				case SOUTH:
					list.add(new AxisAlignedBB(0.375f, 0, 1-0.125, 0.625f, 1, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					break;
				case EAST:
					list.add(new AxisAlignedBB(1-0.125, 0, 0.375f, 1, 1, 0.625f).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					break;
				case WEST:
					list.add(new AxisAlignedBB(0, 0, 1-0.625f, 0.125, 1, 1-0.375f).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					break;
			}
		}

		return list;
	}

	@Override
	public boolean isOverrideBox(@Nonnull AxisAlignedBB box, @Nonnull EntityPlayer player, @Nonnull RayTraceResult mop, @Nonnull ArrayList<AxisAlignedBB> list)
	{
		return false;
	}

	@Nonnull
	@Override
	public List<AxisAlignedBB> getAdvancedColisionBounds()
	{
		return getAdvancedSelectionBounds();
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return master()!=null&&(pos==23||pos==0);
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			TileEntityPacker master = master();
			if(master==null)
				return null;
			if(pos==23)
				return (T)master.inventoryHandler;
			else if(pos==0)
				return (T)master.containerHandler;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public EnumFacing[] sigOutputDirections()
	{
		if(pos==23)
			return new EnumFacing[]{facing}; //i/o conveyor
		else if(pos==0)
			return new EnumFacing[]{facing.rotateY()}; //3x conveyors
		return new EnumFacing[0];
	}

	@Override
	public void onEntityCollision(@Nonnull World world, @Nonnull Entity entity)
	{
		/*if(pos==1&&!world.isRemote&&entity!=null&&!entity.isDead&&entity instanceof EntityItem&&!((EntityItem)entity).getItem().isEmpty() &&pl.pabilo8.immersiveintelligence.api.Utils.getDistanceBetweenPos(entity.getPosition(),this.getPos().offset(facing.getOpposite()),false)==0f)
		{
			ItemStack stack = ((EntityItem)entity).getItem();
			if(stack.isEmpty())
				return;
			if(inventory.get(0).isEmpty() && inventoryHandler.insertItem(0, stack, false).isEmpty())
			{
				((EntityItem)entity).setItem(ItemStack.EMPTY);
				entity.setDead();
				processTime=0;
				animation=60;
			}

		}*/
	}

	@Override
	public void onGuiOpened(EntityPlayer player, boolean clientside)
	{
		if(!clientside)
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setTag("inventory", Utils.writeInventory(inventory));
			tag.setTag("tasks", writeTasks());
			tag.setBoolean("repeatActions", repeatActions);
			ImmersiveEngineering.packetHandler.sendTo(new MessageTileSync(this, tag), ((EntityPlayerMP)player));
		}
	}

	@Override
	public boolean canOpenGui()
	{
		return true;
	}

	@Override
	public int getGuiID()
	{
		return IIGuiList.GUI_PACKER.ordinal();
	}

	@Nullable
	@Override
	public TileEntity getGuiMaster()
	{
		return master();
	}

	@Override
	public boolean addUpgrade(MachineUpgrade upgrade, boolean test)
	{
		boolean b = !hasUpgrade(upgrade);
		if(!test&&b)
			upgrades.add(upgrade);
		return b;
	}

	@Override
	public boolean hasUpgrade(MachineUpgrade upgrade)
	{
		return upgrades.stream().anyMatch(machineUpgrade -> machineUpgrade.getName().equals(upgrade.getName()));
	}

	@Override
	public boolean upgradeMatches(MachineUpgrade upgrade)
	{
		return upgrade==IIContent.UPGRADE_UNPACKER_CONVERSION||
				(upgrade==IIContent.UPGRADE_PACKER_FLUID&&!hasUpgrade(IIContent.UPGRADE_PACKER_ENERGY))||
				(upgrade==IIContent.UPGRADE_PACKER_ENERGY&&!hasUpgrade(IIContent.UPGRADE_PACKER_FLUID));
	}

	@Override
	public TileEntityPacker getUpgradeMaster()
	{
		return master();
	}

	@Override
	public void saveUpgradesToNBT(NBTTagCompound tag)
	{
		for(MachineUpgrade upgrade : upgrades)
			tag.setBoolean(upgrade.getName(), true);
	}

	@Override
	public void getUpgradesFromNBT(NBTTagCompound tag)
	{
		upgrades.clear();
		upgrades.addAll(MachineUpgrade.getUpgradesFromNBT(tag));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderWithUpgrades(MachineUpgrade... upgrades)
	{
		PackerRenderer.renderWithUpgrades(upgrades);
	}

	public ArrayList<MachineUpgrade> getUpgrades()
	{
		return upgrades;
	}

	@Nullable
	@Override
	public MachineUpgrade getCurrentlyInstalled()
	{
		return currentlyInstalled;
	}

	@Override
	public int getInstallProgress()
	{
		return upgradeProgress;
	}

	@Override
	public int getClientInstallProgress()
	{
		return clientUpgradeProgress;
	}

	@Override
	public boolean addUpgradeInstallProgress(int toAdd)
	{
		upgradeProgress += toAdd;
		return true;
	}

	@Override
	public boolean resetInstallProgress()
	{
		currentlyInstalled = null;
		if(upgradeProgress > 0)
		{
			upgradeProgress = 0;
			clientUpgradeProgress = 0;
			return true;
		}
		Predicate<PackerTask> task;
		if(hasUpgrade(IIContent.UPGRADE_PACKER_FLUID))
			task = p -> p.actionType==PackerActionType.ENERGY;
		else if(hasUpgrade(IIContent.UPGRADE_PACKER_ENERGY))
			task = p -> p.actionType==PackerActionType.FLUID;
		else
			task = p -> p.actionType!=PackerActionType.ITEM;
		tasks.removeIf(task);

		return false;
	}

	@Override
	public void startUpgrade(@Nonnull MachineUpgrade upgrade)
	{
		currentlyInstalled = upgrade;
		upgradeProgress = 0;
		clientUpgradeProgress = 0;
	}

	@Override
	public void removeUpgrade(MachineUpgrade upgrade)
	{
		upgrades.remove(upgrade);
	}

	public enum PackerPutMode implements IStringSerializable
	{
		ALL_POSSIBLE,
		SLOT,
		AMOUNT;

		@Nonnull
		@Override
		public String getName()
		{
			return this.toString().toLowerCase();
		}

		public static PackerPutMode fromName(String name)
		{
			switch(name.toLowerCase())
			{
				default:
				case "all_possible":
					return ALL_POSSIBLE;
				case "slot":
					return SLOT;
				case "amount":
					return AMOUNT;
			}
		}
	}

	public enum PackerActionType
	{
		ITEM,
		FLUID,
		ENERGY;

		public String getActionName(boolean unpacker)
		{
			switch(this)
			{
				default:
				case ITEM:
					return unpacker?"unpack": "pack";
				case FLUID:
					return unpacker?"discharge": "charge";
				case ENERGY:
					return unpacker?"drain": "fill";
			}
		}

		public static PackerActionType fromName(String name)
		{
			switch(name.toLowerCase())
			{
				default:
				case "item":
					return ITEM;
				case "fluid":
					return FLUID;
				case "energy":
					return ENERGY;
			}
		}
	}

	@ParametersAreNonnullByDefault
	public static class PackerTask
	{
		public PackerPutMode mode;
		public PackerActionType actionType;
		public IngredientStack stack;
		public int expirationAmount = -1;

		public PackerTask(PackerPutMode mode, PackerActionType actionType, IngredientStack stack)
		{
			this.actionType = actionType;
			this.mode = mode;
			this.stack = stack;
		}

		public PackerTask(NBTTagCompound nbt)
		{
			this(
					PackerPutMode.valueOf(nbt.getString("mode").toUpperCase()),
					PackerActionType.valueOf(nbt.getString("action_type").toUpperCase()),
					IngredientStack.readFromNBT(nbt.getCompoundTag("stack"))
			);

			if(nbt.hasKey("expiration_amount"))
				expirationAmount = nbt.getInteger("expiration_amount");
		}

		public NBTTagCompound toNBT()
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("mode", mode.getName());
			nbt.setString("action_type", actionType.name().toLowerCase());
			nbt.setTag("stack", stack.writeToNBT(new NBTTagCompound()));
			if(expirationAmount!=-1)
				nbt.setInteger("expiration_amount", expirationAmount);

			return nbt;
		}
	}
}
