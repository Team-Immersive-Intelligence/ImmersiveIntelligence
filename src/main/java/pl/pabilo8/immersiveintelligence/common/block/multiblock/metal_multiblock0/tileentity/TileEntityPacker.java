package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity;

import blusunrize.immersiveengineering.api.IEApi;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorage;
import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.IConveyorAttachable;
import blusunrize.immersiveengineering.common.Config.IEConfig.Machines;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import pl.pabilo8.immersiveintelligence.api.LogisticTag;
import pl.pabilo8.immersiveintelligence.api.PackerHandler;
import pl.pabilo8.immersiveintelligence.api.PackerHandler.LabelingTask;
import pl.pabilo8.immersiveintelligence.api.PackerHandler.PackerActionType;
import pl.pabilo8.immersiveintelligence.api.PackerHandler.PackerPutMode;
import pl.pabilo8.immersiveintelligence.api.PackerHandler.PackerTask;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataHandlingUtils;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeLogisticTag;
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
import java.util.Objects;
import java.util.Optional;
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
	@SyncNBT(events = SyncEvents.TILE_RECIPE_CHANGED)
	public int processTime = 0;
	@SyncNBT(events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_CLIENT_MESSAGE})
	public EasyCollection<PackerTask, NBTTagCompound> tasks;
	@SyncNBT(events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_CLIENT_MESSAGE})
	public EasyCollection<LabelingTask, NBTTagCompound> labels;
	@SyncNBT(events = {SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_UPGRADES_MODIFIED})
	public MultiFluidTank fluidTankUpgradeInput;
	@SyncNBT(events = {SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_UPGRADES_MODIFIED})
	public MultiFluidTank fluidTankUpgradeOutput;
	@SyncNBT(events = {SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_UPGRADES_MODIFIED, SyncEvents.TILE_CUSTOM1})
	public FluxStorageAdvanced energyStorageUpgrade;

	private IItemHandler containerHandler = new IEInventoryHandler(1, this, 0, true, true);
	private IItemHandler inventoryInHandler = new IEInventoryHandler(54, this, 1, true, true);
	private IItemHandler inventoryOutHandler = new IEInventoryHandler(54, this, 55, true, true);

	public TileEntityPacker()
	{
		super(MultiblockPacker.INSTANCE);

		this.energyStorage = new FluxStorageAdvanced(Packer.energyCapacity);
		this.inventory = NonNullList.withSize(1+108, ItemStack.EMPTY);
		this.upgradeManager = new UpgradeManager<>(this);
		this.tasks = new EasyCollection<>(PackerTask::new);
		this.labels = new EasyCollection<>(LabelingTask::new);

		this.energyStorageUpgrade = new FluxStorageAdvanced(Packer.energyCapacityUpgrade);
		this.fluidTankUpgradeInput = new MultiFluidTank(Packer.fluidCapacityUpgrade);
		this.fluidTankUpgradeOutput = new MultiFluidTank(Packer.fluidCapacityUpgrade);
	}

	@Override
	protected void dummyCleanup()
	{
		super.dummyCleanup();
		this.tasks = null;
		this.labels = null;
		this.upgradeManager = null;
		this.energyStorageUpgrade = null;
		this.fluidTankUpgradeInput = null;
		this.fluidTankUpgradeOutput = null;
		this.containerHandler = this.inventoryInHandler = this.inventoryOutHandler = null;
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
					if(!packedItem.isEmpty()&&energyStorage.getEnergyStored() >= Packer.energyUsage)
						performPackerAction(packedItem);
					//TODO: 30.01.2026 minecarts
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
			if(isUpgradeInstalled(IIContent.UPGRADE_PACKER_FLUID))
			{
				//Handle bucket interaction
				if(IIUtils.handleBucketTankInteraction(fluidTankUpgradeInput, inventory,
						MultiblockPacker.SLOT_BUCKET1_IN, MultiblockPacker.SLOT_BUCKET1_OUT, true))
					updateTileForEvent(SyncEvents.TILE_RECIPE_CHANGED);
				if(IIUtils.handleBucketTankInteraction(fluidTankUpgradeOutput, inventory,
						MultiblockPacker.SLOT_BUCKET2_IN, MultiblockPacker.SLOT_BUCKET2_OUT, true))
					updateTileForEvent(SyncEvents.TILE_RECIPE_CHANGED);
			}

			EnumFacing outputDir = getDirection("output");
			if(outputDir!=null)
			{
				BlockPos pos = getPOIPos("output").offset(outputDir);
				TileEntity te = this.world.getTileEntity(pos);
				EnumFacing outputFacing = outputDir.getOpposite();
				if(te!=null)
					if(isUpgradeInstalled(IIContent.UPGRADE_PACKER_FLUID))
					{
						if(te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, outputFacing))
						{
							IFluidHandler cap = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, outputFacing);
							assert cap!=null;

							//Attempt output from tank
							for(FluidStack fluid : fluidTankUpgradeOutput.fluids)
							{
								FluidStack fs = fluidTankUpgradeOutput.drain(fluid.copy(), true);
								assert fs!=null;

								fs.amount -= cap.fill(fs, true);
								fluidTankUpgradeOutput.fill(fs, true);
								update = true;
							}
						}
					}
					else //output items
						if(isUpgradeInstalled(IIContent.UPGRADE_PACKER_ENERGY))
						{
							if(te.hasCapability(CapabilityEnergy.ENERGY, outputFacing))
							{
								IEnergyStorage cap = te.getCapability(CapabilityEnergy.ENERGY, outputFacing);
								assert cap!=null;

								int extracted = energyStorageUpgrade.extractEnergy(Machines.capacitorHV_output, false);
								int accepted = cap.receiveEnergy(extracted, false);
								energyStorageUpgrade.receiveEnergy(accepted-extracted, false);
								update = true;
							}
						}
						else
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

		if(update)
			updateTileForEvent(SyncEvents.TILE_RECIPE_CHANGED);
	}

	@Override
	protected int[] listAllPOI(MultiblockPOI poi)
	{
		TileEntityPacker master = master();
		assert master!=null;

		boolean fluid = master.isUpgradeInstalled(IIContent.UPGRADE_PACKER_FLUID);
		boolean energy = master.isUpgradeInstalled(IIContent.UPGRADE_PACKER_ENERGY);
		boolean railway = master.isUpgradeInstalled(IIContent.UPGRADE_PACKER_RAILWAY);

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

	private void performPackerAction(ItemStack packedItem)
	{
		if(tasks==null||tasks.isEmpty())
			return;

		IItemHandler item = null;
		IFluidHandler fluid = null;
		IEnergyStorage energy = null;

		if(isUpgradeInstalled(IIContent.UPGRADE_PACKER_FLUID))
			fluid = PackerHandler.streamFluidHandlers()
					.filter(p -> p.getKey().test(packedItem))
					.map(Entry::getValue)
					.findFirst()
					.map(f -> f.apply(packedItem))
					.orElse(null);
		else if(isUpgradeInstalled(IIContent.UPGRADE_PACKER_ENERGY))
			energy = PackerHandler.streamEnergyHandlers()
					.filter(p -> p.getKey().test(packedItem))
					.map(Entry::getValue)
					.findFirst()
					.map(f -> f.apply(packedItem))
					.orElse(null);
		else
			item = PackerHandler.streamItemHandlers()
					.filter(p -> p.getKey().test(packedItem))
					.map(Entry::getValue)
					.findFirst()
					.map(f -> f.apply(packedItem))
					.orElse(null);

		performPackerAction(item, fluid, energy, packedItem);
	}

	private void performPackerAction(@Nullable IItemHandler itemHandler, @Nullable IFluidHandler fluidHandler, @Nullable IEnergyStorage energyHandler, @Nonnull ItemStack containerStack)
	{
		//No tasks to perform
		if(tasks==null||tasks.isEmpty())
			return;

		boolean repeat = true;
		int repeats = 0;
		boolean energyConsumed = false;

		//Repeat until no progress is made (i.e. container is full, nothing can be output)
		while(repeat)
		{
			repeat = false;
			if(++repeats > 100)
				break;

			boolean anyProgressThisPass = false;
			for(PackerTask task : tasks)
			{
				boolean isLimited = task.expirationAmount!=-1;
				//Client-side block for unpacking tasks, so it doesn't cause desync
				if(world.isRemote&&task.unpack)
					continue;

				//Container filter gating ("*" = any)
				if(task.containerFilter!=null&&!"*".equals(task.containerFilter.oreName))
					if(!task.containerFilter.matchesItemStackIgnoringSize(containerStack))
						continue;
				//Container logistic tag gating (if present)
				if(task.logiTag!=null&&!task.logiTag.itemMatches(containerStack))
					continue;

				//Do not repeat non-repeatable tasks in later passes
				if(repeats > 1&&!task.repeat)
					continue;

				boolean progressForTask = false;
				switch(task.actionType)
				{
					case ITEM:
					{
						if(itemHandler==null)
							break;

						IItemHandler handlerIn = task.unpack?itemHandler: inventoryInHandler;
						IItemHandler handlerOut = task.unpack?inventoryOutHandler: itemHandler;

						//Calculate limits
						int slots = Math.min(task.mode==PackerPutMode.SLOT?task.stack.inputSize: Integer.MAX_VALUE,
								isLimited?task.expirationAmount: Integer.MAX_VALUE);
						int amount = Math.min(task.mode==PackerPutMode.AMOUNT?task.stack.inputSize: Integer.MAX_VALUE,
								isLimited?task.expirationAmount: Integer.MAX_VALUE);

						//Iterate handler slots and try to move items
						for(int i = 0; i < handlerIn.getSlots(); i++)
						{
							//"*" means a wildcard, else use standard ingredientstack matching
							if("*".equals(task.stack.oreName)||task.stack.matchesItemStackIgnoringSize(handlerIn.extractItem(i, amount, true)))
							{
								ItemStack extracted = handlerIn.extractItem(i, amount, false);
								if(extracted.isEmpty())
									continue;

								int movedOut = extracted.getCount();
								extracted = ItemHandlerHelper.insertItem(handlerOut, extracted, false);
								int remainder = extracted.getCount();
								int accepted = movedOut-remainder;

								if(remainder > 0)
									handlerIn.insertItem(i, extracted, false);

								if(accepted > 0)
								{
									progressForTask = true;
									anyProgressThisPass = true;

									amount -= accepted;
									if(isLimited)
										task.expirationAmount = Math.max(0, task.expirationAmount-accepted);

									//only repeat if fully moved
									if(remainder==0)
										repeat = repeat||task.repeat;
								}

								slots--;
							}

							if((isLimited&&task.expirationAmount <= 0)||slots <= 0||amount <= 0)
								break;
						}
					}
					break;

					case FLUID:
					{
						if(fluidHandler==null)
							break;

						int amount = Math.min(task.mode==PackerPutMode.AMOUNT?task.stack.inputSize: Integer.MAX_VALUE,
								isLimited?task.expirationAmount: Integer.MAX_VALUE);
						if(amount <= 0)
							break;

						//Drain fluid from container
						//Output fluid to container
						if(task.unpack)
						{
							FluidStack simulated = fluidHandler.drain(amount, false);
							if(simulated!=null&&simulated.amount > 0&&("*".equals(task.stack.oreName)||simulated.isFluidEqual(task.stack.fluid)))
							{
								int accepted = fluidTankUpgradeOutput.fill(simulated.copy(), true);
								if(accepted > 0)
								{
									FluidStack drained = fluidHandler.drain(accepted, true);
									if(drained!=null&&drained.amount > 0)
									{
										progressForTask = true;
										anyProgressThisPass = true;

										if(isLimited)
											task.expirationAmount = Math.max(0, task.expirationAmount-drained.amount);

										repeat = repeat||task.repeat;
									}
								}
							}
						}
						else
							for(FluidStack fluid : fluidTankUpgradeInput.fluids)
							{
								if(fluid==null||fluid.amount <= 0)
									continue;
								if(!("*".equals(task.stack.oreName)||fluid.isFluidEqual(task.stack.fluid)))
									continue;

								int request = Math.min(amount, fluid.amount);
								if(request <= 0)
									continue;

								FluidStack toDrain = fluid.copy();
								toDrain.amount = request;

								FluidStack drained = fluidTankUpgradeInput.drain(toDrain, true);
								if(drained==null||drained.amount <= 0)
									continue;

								int accepted = fluidHandler.fill(drained.copy(), true);
								int remainder = drained.amount-accepted;

								if(accepted > 0)
								{
									progressForTask = true;
									anyProgressThisPass = true;

									if(isLimited)
										task.expirationAmount = Math.max(0, task.expirationAmount-accepted);

									repeat = repeat||task.repeat;
								}

								if(remainder > 0)
								{
									FluidStack back = drained.copy();
									back.amount = remainder;
									fluidTankUpgradeInput.fill(back, true);
								}
								break;
							}
					}
					break;

					//Output energy
					case ENERGY:
					{
						if(energyHandler==null)
							break;

						int limit = isLimited?task.expirationAmount: Packer.energyCapacityUpgradeMaxTransfer;
						if(limit <= 0)
							break;

						if(task.unpack)
						{
							int extracted = energyHandler.extractEnergy(limit, false);
							if(extracted > 0)
							{
								//Extract from packer only what can be accepted
								int accepted = energyStorageUpgrade.receiveEnergy(extracted, false);
								int leftover = extracted-accepted;
								if(leftover > 0)
									energyHandler.receiveEnergy(leftover, false);

								if(accepted > 0)
								{
									progressForTask = true;
									anyProgressThisPass = true;

									if(isLimited)
										task.expirationAmount = Math.max(0, task.expirationAmount-accepted);

									repeat = repeat||task.repeat;
								}
							}
						}
						else
						{
							int extracted = energyStorageUpgrade.extractEnergy(limit, false);
							if(extracted > 0)
							{
								//Extract from storage only what can be accepted
								int accepted = energyHandler.receiveEnergy(extracted, false);
								int leftover = extracted-accepted;
								if(leftover > 0)
									energyStorageUpgrade.receiveEnergy(leftover, false);

								if(accepted > 0)
								{
									progressForTask = true;
									anyProgressThisPass = true;
									updateTileForEvent(SyncEvents.TILE_CUSTOM2);

									if(isLimited)
										task.expirationAmount = Math.max(0, task.expirationAmount-accepted);

									repeat = repeat||task.repeat;
								}
							}
						}
					}
					break;
				}

				//only pay energy if something actually happened in this call
				if(progressForTask)
				{
					anyProgressThisPass = true;
					if(!energyConsumed)
					{
						energyStorage.extractEnergy(Packer.energyUsage, false);
						energyConsumed = true;

						// Labeling should come after packing is done for the container item
						if(!world.isRemote)
							applyLabelingToContainer(containerStack);
					}
				}
			}

			//Remove jobs that expired
			tasks.removeIf(packerTask -> packerTask.expirationAmount!=-1&&packerTask.expirationAmount <= 0);
			if(!anyProgressThisPass)
				break;
		}
	}

	private void applyLabelingToContainer(@Nonnull ItemStack containerStack)
	{
		if(labels==null||labels.isEmpty()||containerStack.isEmpty())
			return;

		for(LabelingTask label : labels)
		{
			boolean isLimited = label.expirationAmount!=-1;
			if(isLimited&&label.expirationAmount <= 0)
				continue;

			//Filter match ("*" = any)
			if(label.filter!=null&&!"*".equals(label.filter.oreName))
				if(!label.filter.matchesItemStackIgnoringSize(containerStack))
					continue;

			//Incoming logistic tag match (if present)
			if(label.logiTagIn!=null&&!label.logiTagIn.itemMatches(containerStack))
				continue;

			//Process exactly one matching labeling task per container per pack action
			label.serialBatch++;

			//Apply outgoing tag + batch number
			LogisticTag out = label.logiTagOut==null?new LogisticTag(): label.logiTagOut.clone();
			out.withBatchNumber(label.serialBatch);
			out.applyToStack(containerStack);

			//Rename container to name / logiTagOut name + "-serial"
			String baseName = label.name.isEmpty()?out.getName(): label.name;
			if(baseName!=null&&!baseName.isEmpty())
				containerStack.setStackDisplayName(baseName+"-"+label.serialBatch);

			//Expire
			if(isLimited)
				label.expirationAmount = Math.max(0, label.expirationAmount-1);

			//Sync changes
			updateTileForEvent(SyncEvents.TILE_CUSTOM1);
			break;
		}

		labels.removeIf(l -> l.expirationAmount!=-1&&l.expirationAmount <= 0);
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		switch(slot)
		{
			case MultiblockPacker.SLOT_CRATE:
			{
				TileEntityPacker master = master();
				return master!=null&&!master.isUpgradeInstalled(IIContent.UPGRADE_PACKER_RAILWAY);
			}
			case MultiblockPacker.SLOT_BUCKET1_IN:
			case MultiblockPacker.SLOT_BUCKET2_IN:
				return !master().isUpgradeInstalled(IIContent.UPGRADE_PACKER_FLUID)||
						stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
			default:
				return IEApi.isAllowedInCrate(stack);
		}
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
		if(getPOI("input")[0]==pos)
			return new IFluidTank[]{fluidTankUpgradeInput};
		else if(getPOI("output")[0]==pos)
			return new IFluidTank[]{fluidTankUpgradeOutput};
		return new IFluidTank[0];
	}

	//--- Data ---//

	@Override
	public void receiveData(DataPacket packet, int pos)
	{
		/*
			c: command: add/remove/clear/add_label/remove_label/clear_label
			a: action: item/fluid/energy
			m: mode: amount, slot, all_possible
			e: (optional) expires after @e items/MBs/IF (tasks) or @e labeled containers (labels)
			s: (optional) {stack} or string (task payload filter)
			u: unload true/false

			--- labeling ---
			f: (optional) {stack} or string (label container filter), default "*"
			b: (optional) serial batch start (int)
			i: (optional) logiTagIn (LogisticTag variable)
			o: (optional) logiTagOut (LogisticTag variable)
			t: (optional) ItemStack carrying logiTagIn (if used with i absent)
			T: (optional) ItemStack carrying logiTagOut (if used with o absent)
			x: (optional) index for remove/remove_label
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

				case "add_label":
				{
					//Output tag is non-optional, because it is applied to the processed container
					IIDataHandlingUtils.optionalLogisticTag('o', packet).ifPresent(logisticTagOutput -> {
						IngredientStack filter = packet.has('f')?IIDataHandlingUtils.asIngredient('f', packet): new IngredientStack("*");
						LabelingTask lt = new LabelingTask();
						lt.filter = filter;

						if(packet.has('e'))
							lt.expirationAmount = IIDataHandlingUtils.asInt('e', packet);
						if(packet.has('b'))
							lt.serialBatch = IIDataHandlingUtils.asInt('b', packet);
						lt.logiTagOut = logisticTagOutput;

						//Input tag is optional, works as a filter
						if(packet.has('i'))
						{
							DataTypeLogisticTag variable = packet.getVarInType(DataTypeLogisticTag.class, packet.get('i'));
							lt.logiTagIn = variable.value.clone();
						}

						labels.add(lt);
						updateTileForEvent(SyncEvents.TILE_CUSTOM1);
					});
				}
				break;
				case "remove_label":
				{
					Optional<Integer> pid = IIDataHandlingUtils.optionalInt('x', packet);
					if(pid.isPresent())
						labels.remove((int)pid.get());
					else
					{
						IngredientStack filter = packet.has('f')?IIDataHandlingUtils.asIngredient('f', packet): new IngredientStack("*");

						Predicate<LabelingTask> p = "*".equals(filter.oreName)?(l -> true): (l -> l.filter.matches(filter));

						Optional<LogisticTag> outputLogiTag = IIDataHandlingUtils.optionalLogisticTag('o', packet);
						if(outputLogiTag.isPresent())
							p = p.and(l -> Objects.equals(l.logiTagOut, outputLogiTag.get()));
						labels.removeIf(p);
					}
					updateTileForEvent(SyncEvents.TILE_CUSTOM1);
				}
				break;
				case "clear_label":
					labels.clear();
					updateTileForEvent(SyncEvents.TILE_CUSTOM1);
					break;
			}
		});

	}

	@Override
	@SuppressWarnings({"unchecked"})
	public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing)
	{
		TileEntityPacker master = master();
		if(master!=null)
			if(isPOI("input"))
			{
				if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
					return (T)master.inventoryInHandler;
				else if(capability==CapabilityEnergy.ENERGY)
					return (T)this.getCapabilityWrapper(facing);
			}
			else if(isPOI("output"))
				if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
					return (T)master.inventoryOutHandler;
				else if(capability==CapabilityEnergy.ENERGY)
					return (T)this.getCapabilityWrapper(facing);

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

	//--- Colision ---//

	@Override
	public void onEntityCollision(@Nonnull World world, @Nonnull Entity entity)
	{
		TileEntityPacker master = master();
		if(master!=null)
			handleItemEntityInput(entity, stack -> {
				if(isPOI("conveyor_in"))
					return master.containerHandler.insertItem(0, stack, false);
				return ItemStack.EMPTY;
			});
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

	@Override
	public IIGUI getGUI()
	{
		return IIGUI.PACKER;
	}

	//--- IPlayerInteraction ---//

	@Override
	public boolean interact(@Nonnull EnumFacing side, @Nonnull EntityPlayer player, @Nonnull EnumHand hand, @Nonnull ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		if(isPOI("container"))
		{
			TileEntityPacker master = master();
			//container interaction = filling/emptying against INPUT tank (loading containers)
			return master!=null&&master.isUpgradeInstalled(IIContent.UPGRADE_PACKER_FLUID)
					&&FluidUtil.interactWithFluidHandler(player, hand, master.fluidTankUpgradeInput);
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
			return new String[]{IIUtils.getFluidNameOverlayText(master.fluidTankUpgradeInput.getFluid())};
		return new String[0];
	}

	//--- IFluxStorage ---//

	@Nonnull
	public FluxStorage getFluxStorage()
	{
		TileEntityPacker master = master();
		if(master!=null&&master.isUpgradeInstalled(IIContent.UPGRADE_PACKER_ENERGY)&&isPOI("energy_container"))
			return master.energyStorageUpgrade;
		return super.getFluxStorage();
	}

	@Override
	public void postEnergyTransferUpdate(int energy, boolean simulate)
	{
		if(!simulate&&!world.isRemote&&energy!=0)
		{
			TileEntityPacker master = master();
			if(master!=null&&master.isUpgradeInstalled(IIContent.UPGRADE_PACKER_ENERGY)&&isPOI("energy_container"))
				master.updateTileForEvent(SyncEvents.TILE_CUSTOM1);
			super.postEnergyTransferUpdate(energy, simulate);
		}
	}
}
