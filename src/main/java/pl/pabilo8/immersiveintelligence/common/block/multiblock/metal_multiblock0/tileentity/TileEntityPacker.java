package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.IConveyorAttachable;
import blusunrize.immersiveengineering.common.Config.IEConfig.Machines;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import blusunrize.immersiveengineering.common.util.EnergyHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import blusunrize.immersiveengineering.common.util.inventory.MultiFluidTank;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import pl.pabilo8.immersiveintelligence.api.PackerHandler;
import pl.pabilo8.immersiveintelligence.api.PackerHandler.PackerActionType;
import pl.pabilo8.immersiveintelligence.api.PackerHandler.PackerPutMode;
import pl.pabilo8.immersiveintelligence.api.PackerHandler.PackerTask;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeItemStack;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.utils.IUpgradableMachine;
import pl.pabilo8.immersiveintelligence.api.utils.MachineUpgrade;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Packer;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockPacker;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIGeneric;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Pabilo8
 * @since 28-06-2019
 */
public class TileEntityPacker extends TileEntityMultiblockIIGeneric<TileEntityPacker> implements IConveyorAttachable, IGuiTile, IUpgradableMachine, IPlayerInteraction
{
	public boolean repeatActions = false;
	protected ArrayList<MachineUpgrade> upgrades = new ArrayList<>();
	MachineUpgrade currentlyInstalled = null;
	int upgradeProgress = 0;
	public int clientUpgradeProgress = 0;
	public int processTime = 0;

	public ArrayList<PackerTask> tasks = new ArrayList<>();

	public IItemHandler containerHandler = new IEInventoryHandler(1, this, 0, true, true);
	IItemHandler inventoryInHandler = new IEInventoryHandler(54, this, 1, true, true);
	IItemHandler inventoryOutHandler = new IEInventoryHandler(54, this, 55, true, true);

	public FluxStorageAdvanced energyStorageUpgrade;
	public MultiFluidTank fluidTank;

	public TileEntityPacker()
	{
		super(MultiblockPacker.INSTANCE);

		this.energyStorage = new FluxStorageAdvanced(Packer.energyCapacity);
		inventory = NonNullList.withSize(109, ItemStack.EMPTY);

		energyStorageUpgrade = new FluxStorageAdvanced(Packer.energyCapacityUpgrade);
		fluidTank = new MultiFluidTank(Packer.fluidCapacityUpgrade);
	}

	@Override
	protected void onUpdate()
	{
		boolean update = false;

		if(!getRedstoneAtPos(0)&&!containerHandler.getStackInSlot(0).isEmpty())
			if(processTime < Packer.actionTime)
			{
				if(processTime==0)
					update = true;

				processTime++;
				if(processTime==Packer.actionTime*0.5)
				{
					ItemStack packedItem = containerHandler.getStackInSlot(0);
					//energy handling
					if(!packedItem.isEmpty()&&energyStorage.getEnergyStored() >= Packer.energyUsage)
					{
						energyStorage.extractEnergy(Packer.energyUsage, false);

//						boolean unpacker = hasUpgrade(IIContent.UPGRADE_UNPACKER_CONVERSION);

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
								if(world.isRemote&&task.unpack)
									break;

								switch(task.actionType)
								{
									case ITEM:
									{
										Optional<Function<ItemStack, IItemHandler>> handlerFunction = PackerHandler.streamItems()
												.filter(p -> p.getKey().test(packedItem))
												.map(Entry::getValue)
												.findFirst();

										if(!handlerFunction.isPresent())
											break;

										IItemHandler handler = handlerFunction.get().apply(packedItem);

										//common handling, just replace in and out inventory handler
										IItemHandler handlerIn = task.unpack?handler: inventoryInHandler;
										IItemHandler handlerOut = task.unpack?inventoryOutHandler: handler;

										int slots = Math.min(task.mode==PackerHandler.PackerPutMode.SLOT?task.stack.inputSize: Integer.MAX_VALUE, isLimited?task.expirationAmount: Integer.MAX_VALUE);
										int amount = Math.min(task.mode==PackerHandler.PackerPutMode.AMOUNT?task.stack.inputSize: Integer.MAX_VALUE, isLimited?task.expirationAmount: Integer.MAX_VALUE);

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
										Optional<Function<ItemStack, IFluidHandlerItem>> handlerFunction = PackerHandler.streamFluids()
												.filter(p -> p.getKey().test(packedItem))
												.map(Entry::getValue)
												.findFirst();

										if(!handlerFunction.isPresent())
											break;

										IFluidHandlerItem handler = handlerFunction.get().apply(packedItem);

										// TODO: 26.08.2022 proper extraction amount
										int amount = Math.min(task.mode==PackerHandler.PackerPutMode.AMOUNT?task.stack.inputSize: Integer.MAX_VALUE, isLimited?task.expirationAmount: Integer.MAX_VALUE);

										if(task.unpack)
										{
											FluidStack fs = handler.drain(amount, true);
											if(fs!=null&&("*".equals(task.stack.oreName)||fs.isFluidEqual(task.stack.fluid)))
											{
												//check how much fluid was transferred, then subtract to get what's left and put it back in machine tank
												int accepted = fluidTank.fill(fs.copy(), true);
												fs.amount -= accepted;
												handler.fill(fs, true);
											}

										}
										else
										{
											for(FluidStack fluid : fluidTank.fluids)
											{
												if("*".equals(task.stack.oreName)||fluid.isFluidEqual(task.stack.fluid))
												{
													FluidStack fs = fluidTank.drain(amount, true);
													assert fs!=null;

													//check how much fluid was transferred, then subtract to get what's left and put it back in machine tank
													int accepted = handler.fill(fs.copy(), true);
													fs.amount -= accepted;
													fluidTank.fill(fs, true);
												}
											}
										}
									}
									break;
									case ENERGY:
									{
										if(task.unpack)
										{
											//ah, yes, the inconsistency
											int extracted = EnergyHelper.extractFlux(packedItem, 10000000, false);
											int accepted = energyStorageUpgrade.receiveEnergy(extracted, false);

											EnergyHelper.insertFlux(packedItem, accepted-extracted, false);
										}
										else
										{
											int extracted = energyStorageUpgrade.extractEnergy(10000000, false);
											int accepted = EnergyHelper.insertFlux(packedItem, extracted, false);

											energyStorageUpgrade.receiveEnergy(accepted-extracted, false);
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

		if(world.isRemote)
		{
			if(clientUpgradeProgress < getMaxClientProgress())
				clientUpgradeProgress = (int)Math.min(clientUpgradeProgress+(Tools.wrenchUpgradeProgress/2f), getMaxClientProgress());
		}
		else //storage output
		{
			BlockPos pos = getBlockPosForPos(15)
					.offset(mirrored?facing.rotateY(): facing.rotateYCCW());
			TileEntity te = this.world.getTileEntity(pos);
			EnumFacing outputFacing = mirrored?this.facing.rotateYCCW(): this.facing.rotateY();

			if(te!=null)
			{
				if(hasUpgrade(IIContent.UPGRADE_PACKER_FLUID))
				{
					if(te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, outputFacing))
					{
						IFluidHandler cap = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, outputFacing);
						assert cap!=null;

						for(FluidStack fluid : fluidTank.fluids)
						{
							FluidStack fs = fluidTank.drain(fluid.copy(), false);
							assert fs!=null;

							fs.amount -= cap.fill(fs, false);
							fluidTank.fill(fs, false);
						}
					}
				}
				else if(hasUpgrade(IIContent.UPGRADE_PACKER_ENERGY))
				{
					if(te.hasCapability(CapabilityEnergy.ENERGY, outputFacing))
					{
						IEnergyStorage cap = te.getCapability(CapabilityEnergy.ENERGY, outputFacing);
						assert cap!=null;

						int extracted = energyStorageUpgrade.extractEnergy(Machines.capacitorHV_output, false);
						int accepted = cap.receiveEnergy(extracted, false);
						energyStorageUpgrade.receiveEnergy(accepted-extracted, false);
					}
				}
				else
				{
					//output items
					for(int i = 55; i < inventory.size(); i++)
					{
						ItemStack output = inventory.get(i);
						if(output.isEmpty())
							continue;
						output = Utils.insertStackIntoInventory(te, output, this.facing.getOpposite());
						inventory.set(i, output);
					}
				}
			}

		}

		if(update)
			forceTileUpdate();
	}

	//--- NBT ---//

	@Override
	public void readCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		if(isDummy())
			return;

		getUpgradesFromNBT(nbt);
		repeatActions = nbt.getBoolean("repeatActions");

		processTime = nbt.getInteger("process_time");

		if(hasUpgrade(IIContent.UPGRADE_PACKER_ENERGY))
			energyStorage.readFromNBT(nbt.getCompoundTag("energy_upgrade"));
		else if(hasUpgrade(IIContent.UPGRADE_PACKER_FLUID))
			fluidTank.readFromNBT(nbt.getCompoundTag("fluid_tank"));

		readTasks(nbt.getTagList("tasks", 10));
	}

	@Override
	public void writeCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);
		if(isDummy())
			return;

		saveUpgradesToNBT(nbt);
		nbt.setBoolean("repeatActions", repeatActions);

		nbt.setInteger("process_time", processTime);

		if(hasUpgrade(IIContent.UPGRADE_PACKER_ENERGY))
			nbt.setTag("energy_upgrade", energyStorage.writeToNBT(new NBTTagCompound()));
		else if(hasUpgrade(IIContent.UPGRADE_PACKER_FLUID))
			nbt.setTag("fluid_tank", fluidTank.writeToNBT(new NBTTagCompound()));

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

		if(message.hasKey("energy_upgrade"))
			processTime = message.getInteger("process_time");

		if(message.hasKey("energy_upgrade"))
			energyStorage.readFromNBT(message.getCompoundTag("energy_upgrade"));
		else if(message.hasKey("fluid_tank"))
			fluidTank.readFromNBT(message.getCompoundTag("fluid_tank"));
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
			tagTasks.appendTag(task.toNBT());
		return tagTasks;
	}

	private void readTasks(NBTTagList tagTasks)
	{
		tasks.clear();
		for(NBTBase task : tagTasks)
			if(task instanceof NBTTagCompound)
				tasks.add(new PackerTask(((NBTTagCompound)task)));
	}

	@Override
	protected int[] listAllPOI(MultiblockPOI poi)
	{
		switch(poi)
		{
			case ENERGY_INPUT:
				return getPOI("energy");
			case ITEM_INPUT:
			case FLUID_INPUT:
				return getPOI("input");
			case ITEM_OUTPUT:
			case FLUID_OUTPUT:
			case ENERGY_OUTPUT:
				return getPOI("output");
			case REDSTONE_INPUT:
				return getPOI("redstone");
			case DATA_INPUT:
				return getPOI("data");
		}
		return new int[0];
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

	// TODO: 19.08.2022 fluidTank

	@Nonnull
	@Override
	protected IFluidTank[] getAccessibleFluidTanks(@Nonnull EnumFacing side)
	{
		if(pos==23||pos==15)
		{
			TileEntityPacker master = master();
			if(master!=null&&master.hasUpgrade(IIContent.UPGRADE_PACKER_FLUID))
				return new IFluidTank[]{master.fluidTank};
		}
		return new FluidTank[0];
	}

	@Override
	protected boolean canFillTankFrom(int iTank, @Nonnull EnumFacing side, @Nonnull FluidStack resource)
	{
		if(pos==23)
		{
			TileEntityPacker master = master();
			return master!=null&&master.hasUpgrade(IIContent.UPGRADE_PACKER_FLUID);
		}
		return super.canFillTankFrom(iTank, side, resource);
	}

	@Override
	protected boolean canDrainTankFrom(int iTank, @Nonnull EnumFacing side)
	{
		if(pos==15)
		{
			TileEntityPacker master = master();
			return master!=null&&master.hasUpgrade(IIContent.UPGRADE_PACKER_FLUID);
		}
		return super.canDrainTankFrom(iTank, side);
	}

	//--- Data ---//

	@Override
	public void receiveData(DataPacket packet, int pos)
	{
			/*
			c: command: add/remove/clear
			a: action: pack/unpack/fill/drain/charge/discharge
			m: mode: amount, slot, all_possible
			e: (optional) expires after @e items/MBs
			s: (optional) {stack} or string
			*/
		DataType a = packet.getPacketVariable('a');
		DataType m = packet.getPacketVariable('m');
		DataType e = packet.getPacketVariable('e');
		DataType s = packet.getPacketVariable('s');
		DataType c = packet.getPacketVariable('c');

		switch(c.toString())
		{
			case "add":
			{
				PackerPutMode mode = PackerHandler.PackerPutMode.fromName(m.toString());
				PackerActionType action = PackerHandler.PackerActionType.fromName(a.toString());
				IngredientStack stack = IIUtils.ingredientFromData(s);
				PackerTask packerTask = new PackerTask(mode, action, stack);
				if(packet.hasVariable('e'))
					packerTask.expirationAmount = packet.getVarInType(DataTypeInteger.class, e).value;
				tasks.add(packerTask);

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
					tasks.remove(((DataTypeInteger)a).value);
				else
				{
					Predicate<PackerTask> p;
					if(s instanceof DataTypeString)
						p = packerTask -> packerTask.stack.oreName.equals(s.toString());
					else if(s instanceof DataTypeItemStack)
						p = packerTask -> packerTask.stack.equals(IIUtils.ingredientFromData(s));
					else
						p = packerTask -> true;

					if(packet.hasVariable('m'))
						p = p.and(packerTask -> packerTask.mode==PackerHandler.PackerPutMode.fromName(m.toString()));
					if(packet.hasVariable('a'))
						p = p.and(packerTask -> packerTask.actionType==PackerHandler.PackerActionType.fromName(a.toString()));
					tasks.removeIf(p);
				}
			}
			break;
			case "clear":
				tasks.clear();
				break;
		}

	}

	//--- Colision ---//

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
		TileEntityPacker master = master();
		if(master!=null)
		{
			if(pos==0&&capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
				return (T)master.containerHandler;

			if(hasUpgrade(IIContent.UPGRADE_PACKER_ENERGY))
			{
				/*if(pos==23)
					return */
			}
			else if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			{
				if(!hasUpgrade(IIContent.UPGRADE_PACKER_ENERGY)&&!hasUpgrade(IIContent.UPGRADE_PACKER_FLUID))
				{
					if(pos==23)
						return (T)master.inventoryInHandler;
					if(pos==15)
						return (T)master.inventoryOutHandler;
				}
			}
		}


		return super.getCapability(capability, facing);
	}

	@Override
	public boolean interact(EnumFacing side, EntityPlayer player, EnumHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		if(world.isRemote)
			return false;

		TileEntityPacker master = master();
		if(master!=null&&(pos==18||pos==21||pos==22||(pos > 29&&pos < 35)||(pos > 41&&pos < 47))&&master.hasUpgrade(IIContent.UPGRADE_PACKER_FLUID))
		{
			if(heldItem.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null))
			{
				if(FluidUtil.interactWithFluidHandler(player, hand, master.fluidTank))
				{
					forceTileUpdate();
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public EnumFacing[] sigOutputDirections()
	{
		if(pos==23)
			return new EnumFacing[]{facing}; //i/o conveyor
		if(pos==15)
			return new EnumFacing[]{mirrored?facing.rotateYCCW(): facing.rotateY()}; //i/o conveyor
		else if(pos==0)
			return new EnumFacing[]{facing.rotateY()}; //3x conveyors
		return new EnumFacing[0];
	}

	// TODO: 19.08.2022 add
	@Override
	public void onEntityCollision(@Nonnull World world, @Nonnull Entity entity)
	{
		/*if(pos==1&&!world.isRemote&&entity!=null&&!entity.isDead&&entity instanceof EntityItem&&!((EntityItem)entity).getItem().isEmpty() &&IIUtils.getDistanceBetweenPos(entity.getPosition(),this.getPos().offset(facing.getOpposite()),false)==0f)
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
			forceTileUpdate();
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
		return upgrade==IIContent.UPGRADE_PACKER_RAILWAY||upgrade==IIContent.UPGRADE_PACKER_NAMING||
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
		// TODO: 19.08.2022 upgrade rendering
		//PackerRenderer.renderWithUpgrades(upgrades);
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
			task = p -> p.actionType==PackerHandler.PackerActionType.FLUID;
		else if(hasUpgrade(IIContent.UPGRADE_PACKER_ENERGY))
			task = p -> p.actionType==PackerHandler.PackerActionType.ENERGY;
		else
			task = p -> p.actionType==PackerHandler.PackerActionType.ITEM;
		tasks.removeIf(task.negate());

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

}
