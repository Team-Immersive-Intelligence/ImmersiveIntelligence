package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.IConveyorAttachable;
import blusunrize.immersiveengineering.common.Config.IEConfig.Machines;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import blusunrize.immersiveengineering.common.util.EnergyHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import blusunrize.immersiveengineering.common.util.inventory.MultiFluidTank;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
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
import pl.pabilo8.immersiveintelligence.api.PackerHandler.LabelingTask;
import pl.pabilo8.immersiveintelligence.api.PackerHandler.PackerActionType;
import pl.pabilo8.immersiveintelligence.api.PackerHandler.PackerPutMode;
import pl.pabilo8.immersiveintelligence.api.PackerHandler.PackerTask;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataHandlingUtils;
import pl.pabilo8.immersiveintelligence.api.upgrade.IManagedUpgradableDevice;
import pl.pabilo8.immersiveintelligence.api.upgrade.Upgrade;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeManager;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.MachineStyle;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedTextOverlay;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Packer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockPacker;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyCollection;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IIIGuiMultiblockTile;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIGeneric;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 28.06.2019
 */
public class TileEntityPacker extends TileEntityMultiblockIIGeneric<TileEntityPacker>
		implements IConveyorAttachable, IManagedUpgradableDevice<TileEntityPacker>, IPlayerInteraction, IAdvancedTextOverlay, IIIGuiMultiblockTile
{
	@SyncNBT(name = "upgrades", events = SyncEvents.TILE_UPGRADES_MODIFIED)
	public UpgradeManager<TileEntityPacker> upgradeManager;
	@SyncNBT(events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_CLIENT_MESSAGE})
	public boolean repeatActions = false;
	@SyncNBT(events = SyncEvents.TILE_RECIPE_CHANGED)
	public int processTime = 0;
	@SyncNBT(events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_CLIENT_MESSAGE})
	public EasyCollection<PackerTask, NBTTagCompound> tasks;
	@SyncNBT(events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_CLIENT_MESSAGE})
	public EasyCollection<LabelingTask, NBTTagCompound> labels;
	@SyncNBT(events = {SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_UPGRADES_MODIFIED})
	public MultiFluidTank fluidTankUpgrade;
	@SyncNBT(events = {SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_UPGRADES_MODIFIED})
	public FluxStorageAdvanced energyStorageUpgrade;

	private final IItemHandler containerHandler = new IEInventoryHandler(1, this, 0, true, true);
	private final IItemHandler inventoryInHandler = new IEInventoryHandler(54, this, 1, true, true);
	private final IItemHandler inventoryOutHandler = new IEInventoryHandler(54, this, 55, true, true);

	public TileEntityPacker()
	{
		super(MultiblockPacker.INSTANCE);

		this.energyStorage = new FluxStorageAdvanced(Packer.energyCapacity);
		this.inventory = NonNullList.withSize(1+108, ItemStack.EMPTY);
		this.upgradeManager = new UpgradeManager<>(this);
		this.tasks = new EasyCollection<>(PackerTask::new);
		this.labels = new EasyCollection<>(LabelingTask::new);

		this.energyStorageUpgrade = new FluxStorageAdvanced(Packer.energyCapacityUpgrade);
		this.fluidTankUpgrade = new MultiFluidTank(Packer.fluidCapacityUpgrade);
	}

	@Override
	protected void dummyCleanup()
	{
		super.dummyCleanup();
		this.tasks = null;
		this.labels = null;
		this.upgradeManager = null;
		this.energyStorageUpgrade = null;
		this.fluidTankUpgrade = null;
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
												int accepted = fluidTankUpgrade.fill(fs.copy(), true);
												fs.amount -= accepted;
												handler.fill(fs, true);
											}

										}
										else
										{
											for(FluidStack fluid : fluidTankUpgrade.fluids)
											{
												if("*".equals(task.stack.oreName)||fluid.isFluidEqual(task.stack.fluid))
												{
													FluidStack fs = fluidTankUpgrade.drain(amount, true);
													assert fs!=null;

													//check how much fluid was transferred, then subtract to get what's left and put it back in machine tank
													int accepted = handler.fill(fs.copy(), true);
													fs.amount -= accepted;
													fluidTankUpgrade.fill(fs, true);
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

		if(!world.isRemote)
		{
			BlockPos pos = getBlockPosForPos(15)
					.offset(mirrored?facing.rotateY(): facing.rotateYCCW());
			TileEntity te = this.world.getTileEntity(pos);
			EnumFacing outputFacing = mirrored?this.facing.rotateYCCW(): this.facing.rotateY();

			if(te!=null)
			{
				if(isUpgradeInstalled(IIContent.UPGRADE_PACKER_FLUID))
				{
					if(te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, outputFacing))
					{
						IFluidHandler cap = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, outputFacing);
						assert cap!=null;

						for(FluidStack fluid : fluidTankUpgrade.fluids)
						{
							FluidStack fs = fluidTankUpgrade.drain(fluid.copy(), false);
							assert fs!=null;

							fs.amount -= cap.fill(fs, false);
							fluidTankUpgrade.fill(fs, false);
						}
					}
				}
				else if(isUpgradeInstalled(IIContent.UPGRADE_PACKER_ENERGY))
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

	@Override
	public void onEntityCollision(World world, Entity entity)
	{
		//Accept containers (crates, barrels, etc.) lying on the conveyor
		TileEntityPacker master = master();
		if(master!=null)
			handleItemEntityInput(entity, stack -> {
				if(isPOI("conveyor_in"))
					return master.containerHandler.insertItem(0, stack, false);
				return ItemStack.EMPTY;
			});
	}

	@Override
	protected int[] listAllPOI(MultiblockPOI poi)
	{
		TileEntityPacker master = master();
		assert master!=null;
		boolean fluid = master.isUpgradeInstalled(IIContent.UPGRADE_PACKER_FLUID);
		boolean energy = master.isUpgradeInstalled(IIContent.UPGRADE_PACKER_ENERGY);

		switch(poi)
		{
			case ENERGY_INPUT:
				return energy?getPOI("all_energy_input"): getPOI("energy");
			case ITEM_INPUT:
				return !fluid&&!energy?getPOI("inputs_items"): getPOI("conveyor_in");
			case FLUID_INPUT:
				return fluid?getPOI("input"): new int[0];
			case ITEM_OUTPUT:
				return !fluid&&!energy?getPOI("outputs_items"): getPOI("conveyor_out");
			case FLUID_OUTPUT:
				return fluid?getPOI("output"): new int[0];
			case ENERGY_OUTPUT:
				return energy?getPOI("output"): new int[0];
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
		return slot==MultiblockPacker.SLOT_CRATE?1: 64;
	}

	@Nonnull
	@Override
	protected IFluidTank[] getFluidTanks(int pos, EnumFacing side)
	{
		return new IFluidTank[]{fluidTankUpgrade};
	}

	//--- Data ---//

	@Override
	public void receiveData(DataPacket packet, int pos)
	{
			/*
			c: command: add/remove/clear
			a: action: item/fluid/energy
			m: mode: amount, slot, all_possible
			e: (optional) expires after @e items/MBs
			s: (optional) {stack} or string
			u: unload true/false
			*/
		IngredientStack stack = IIDataHandlingUtils.asIngredient('s', packet);
		PackerActionType action = IIDataHandlingUtils.asEnum('a', packet, PackerActionType.class);
		PackerPutMode mode = IIDataHandlingUtils.asEnum('m', packet, PackerPutMode.class);
		boolean unpack = IIDataHandlingUtils.asBoolean('u', packet);

		IIDataHandlingUtils.expectingStringParam('c', packet, command -> {
			switch(command)
			{
				case "add":
				{
					if(mode==null||action==null)
						return;
					PackerTask packerTask = new PackerTask(mode, action, stack);
					packerTask.unpack = unpack;
					if(packet.has('e'))
						packerTask.expirationAmount = IIDataHandlingUtils.asInt('e', packet);
					tasks.add(packerTask);

				}
				break;
				case "remove":
				{
					Optional<Integer> pid = IIDataHandlingUtils.optionalInt('a', packet);
					if(pid.isPresent())
						tasks.remove((int)pid.get());
					else
					{
						Predicate<PackerTask> p = "*".equals(stack.oreName)?(packerTask -> true):
								(packerTask -> packerTask.stack.matches(stack));
						if(packet.has('m'))
							p = p.and(packerTask -> packerTask.mode==mode);
						if(packet.has('a'))
							p = p.and(packerTask -> packerTask.actionType==action);
						tasks.removeIf(p);
					}
				}
				break;
				case "clear":
					tasks.clear();
					break;
			}
		});

	}

	//--- Colision ---//

	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing)
	{
		TileEntityPacker master = master();
		if(master!=null)
		{
			if(isPOI("input"))
			{
				if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
					return (T)master.inventoryInHandler;
				else if(capability==CapabilityEnergy.ENERGY)
					return (T)this.getCapabilityWrapper(facing);
			}
			else if(isPOI("output"))
			{
				if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
					return (T)master.inventoryOutHandler;
				else if(capability==CapabilityEnergy.ENERGY)
					return (T)this.getCapabilityWrapper(facing);
			}
		}

		return super.getCapability(capability, facing);
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

	//--- IManagedUpgradableDevice ---//

	@Nonnull
	@Override
	public UpgradeManager<TileEntityPacker> getUpgradeManager()
	{
		return upgradeManager;
	}

	@Override
	public MachineStyle getUpgradableMachineStyle()
	{
		return MachineStyle.STEEL;
	}

	@Override
	public boolean removeUpgrade(Upgrade upgrade)
	{
		if(IManagedUpgradableDevice.super.removeUpgrade(upgrade))
		{
			Predicate<PackerTask> task;
			if(isUpgradeInstalled(IIContent.UPGRADE_PACKER_FLUID))
				task = p -> p.actionType==PackerHandler.PackerActionType.FLUID;
			else if(isUpgradeInstalled(IIContent.UPGRADE_PACKER_ENERGY))
				task = p -> p.actionType==PackerHandler.PackerActionType.ENERGY;
			else
				task = p -> p.actionType==PackerHandler.PackerActionType.ITEM;
			tasks.removeIf(task.negate());
			return true;
		}
		return false;
	}

	//--- IIIGuiMultiblockTile ---//

	@Override
	public boolean canOpenGui()
	{
		return true;
	}

	@Nullable
	@Override
	public TileEntity getGuiMaster()
	{
		return master();
	}

	@Override
	public IIGUI getGUI()
	{
		return IIGUI.PACKER;
	}

	//--- IPlayerInteraction ---//

	@Override
	public boolean interact(EnumFacing side, EntityPlayer player, EnumHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		if(isPOI("container"))
		{
			TileEntityPacker master = master();
			return master!=null&&master.isUpgradeInstalled(IIContent.UPGRADE_PACKER_FLUID)&&FluidUtil.interactWithFluidHandler(player, hand, master.fluidTankUpgrade);
		}
		return false;
	}

	//--- IAdvancedTextOverlay ---//

	@SideOnly(Side.CLIENT)
	@Override
	public String[] getOverlayText(EntityPlayer player, RayTraceResult mop)
	{
		if(!Utils.isFluidRelatedItemStack(player.getHeldItem(EnumHand.MAIN_HAND)))
			return new String[0];
		TileEntityPacker master = master();
		if(master!=null&&master.isUpgradeInstalled(IIContent.UPGRADE_PACKER_FLUID)&&isPOI("container"))
			return new String[]{IIUtils.getFluidNameOverlayText(master.fluidTankUpgrade.getFluid())};
		return new String[0];
	}
}
