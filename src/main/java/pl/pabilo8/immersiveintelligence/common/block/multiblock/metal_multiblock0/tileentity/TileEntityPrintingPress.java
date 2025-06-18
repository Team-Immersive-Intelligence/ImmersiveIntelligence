package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import blusunrize.immersiveengineering.common.util.inventory.MultiFluidTank;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.api.crafting.PrintingRecipe;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataHandlingUtils;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.PrintingPress;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockPrintingPress;
import pl.pabilo8.immersiveintelligence.common.entity.tactile.EntityAMTTactile;
import pl.pabilo8.immersiveintelligence.common.entity.tactile.TactileHandler;
import pl.pabilo8.immersiveintelligence.common.entity.tactile.TactileHandler.ITactileListener;
import pl.pabilo8.immersiveintelligence.common.util.IIDamageSources;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.lambda.NBTTagCollector;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionMulti;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayDeque;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 13.12.2023
 * @since 28.06.2019
 */
public class TileEntityPrintingPress extends TileEntityMultiblockProductionMulti<TileEntityPrintingPress, PrintingRecipe> implements ITactileListener
{
	/**
	 * Inventory slots IDs
	 */
	public static final int SLOT_PAPER = 0, SLOT_OUTPUT = 1, SLOT_BUCKET_IN = 2, SLOT_BUCKET_OUT = 3;

	@SyncNBT(time = 40, events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_RECIPE_CHANGED})
	public MultiFluidTank tank;

	private IItemHandler inputHandler = new IEInventoryHandler(1, this, SLOT_PAPER, true, true);
	private IItemHandler outputHandler = new IEInventoryHandler(1, this, SLOT_OUTPUT, true, true);
	private TactileHandler tactileHandler = null;

	private ArrayDeque<PrintingRequest> printRequestsQueue;

	public TileEntityPrintingPress()
	{
		super(MultiblockPrintingPress.INSTANCE);
		this.tank = new MultiFluidTank(8000);
		this.energyStorage = new FluxStorageAdvanced(PrintingPress.energyCapacity);
		this.inventory = NonNullList.withSize(4, ItemStack.EMPTY);
		this.printRequestsQueue = new ArrayDeque<>();
	}

	@Override
	protected void dummyCleanup()
	{
		super.dummyCleanup();
		this.tank = null;
		this.inputHandler = null;
		this.outputHandler = null;
		this.tactileHandler = null;
	}

	@Override
	protected void onUpdate()
	{
		super.onUpdate();

		if(tactileHandler==null)
			tactileHandler = new TactileHandler(multiblock, this);
		tactileHandler.defaultize();

		if(IIUtils.handleBucketTankInteraction(tank, inventory, SLOT_BUCKET_IN, SLOT_BUCKET_OUT, true,
				fs -> IIContent.fluidInkBlack.equals(fs.getFluid())||
						IIContent.fluidInkCyan.equals(fs.getFluid())||
						IIContent.fluidInkMagenta.equals(fs.getFluid())||
						IIContent.fluidInkYellow.equals(fs.getFluid())))
			forceTileUpdate();
	}

	//--- NBT Handling ---//

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		if(isDummy())
			return;

		if(nbt.hasKey("print_queue"))
		{
			printRequestsQueue.clear();
			for(NBTBase entry : nbt.getTagList("print_queue", EasyNBT.TAG_COMPOUND))
				printRequestsQueue.add(new PrintingRequest((NBTTagCompound)entry));
			printRequestsQueue.removeIf(printingRequest -> printingRequest.recipe==null);
		}
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);
		if(isDummy())
			return;
		EasyNBT.wrapNBT(nbt).withTag("print_queue",
				printRequestsQueue.stream()
						.map(PrintingRequest::serializeNBT)
						.collect(new NBTTagCollector())
		);
	}


	//--- Properties ---//

	@Override
	protected int[] listAllPOI(MultiblockPOI poi)
	{
		switch(poi)
		{
			case DATA_INPUT:
				return getPOI("data");
			case ENERGY_INPUT:
				return getPOI("energy");
			case ITEM_INPUT:
				return getPOI("paper_input");
			case FLUID_INPUT:
				return getPOI("fluid_input");
			case ITEM_OUTPUT:
				return getPOI("output");
		}
		return new int[0];
	}

	@Override
	public void receiveData(DataPacket packet, int pos)
	{
		if(packet.getPacketVariable('c').toString().equals("callback"))
		{
			DataPacket response = IIDataHandlingUtils.handleCallback(packet,
					var -> {
						switch(var)
						{
							case "get_total":
								return new DataTypeInteger(processQueue.size());

							case "get_ink":
							case "get_black":
								return new DataTypeInteger(getStoredFluidAmount(IIContent.fluidInkBlack));
							case "get_cyan":
								return new DataTypeInteger(getStoredFluidAmount(IIContent.fluidInkCyan));
							case "get_magenta":
								return new DataTypeInteger(getStoredFluidAmount(IIContent.fluidInkMagenta));
							case "get_yellow":
								return new DataTypeInteger(getStoredFluidAmount(IIContent.fluidInkYellow));

							case "get_energy":
								return new DataTypeInteger(energyStorage.getEnergyStored());
						}
						return null;
					});
			IIDataHandlingUtils.sendPacketAdjacently(response, world, getBlockPosForPos(pos), facing);
		}
		else
		{
			int amount = IIDataHandlingUtils.asInt('a', packet);
			if(amount <= 0)
				return;

			String printingMode = IIDataHandlingUtils.asString('m', packet);
			PrintingRecipe.streamRecipes(PrintingRecipe.class)
					.filter(recipe -> recipe.getInput().matchesItemStackIgnoringSize(inventory.get(SLOT_PAPER)))
					.filter(recipe -> recipe.getCategoryName().equals(printingMode))
					.findFirst()
					.ifPresent(printingRecipe -> {
						this.printRequestsQueue.add(new PrintingRequest(printingRecipe, packet, amount));
						forceTileUpdate();
					});
		}
	}

	private int getStoredFluidAmount(Fluid fluid)
	{
		return tank.fluids.stream()
				.filter(fluidStack -> fluidStack.getFluid()==fluid)
				.mapToInt(fs -> fs.amount).sum();
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		switch(slot)
		{
			case SLOT_PAPER:
				return Utils.compareToOreName(stack, "pageEmpty");
			case SLOT_OUTPUT:
				return Utils.compareToOreName(stack, "pageWritten")||Utils.compareToOreName(stack, "pageEmpty");
			case SLOT_BUCKET_IN:
			case SLOT_BUCKET_OUT:
				return stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
			default:
				return false;
		}

	}

	@Override
	public float getMinProductionOffset()
	{
		return 0.55f;
	}

	@Override
	public int getMaxProductionQueue()
	{
		return 2;
	}

	@Override
	protected IIMultiblockProcess<PrintingRecipe> findNewProductionProcess()
	{
		// Check for queued orders
		if(printRequestsQueue.isEmpty())
			return null;
		PrintingRequest found = printRequestsQueue.getFirst();
		if(found.amount-- <= 1)
			printRequestsQueue.remove();

		if(!found.recipe.getInput().matchesItemStack(inputHandler.extractItem(SLOT_PAPER, 1, true)))
			return null;

		//Check if there's enough paper
		PrintingProcess process = new PrintingProcess(found.recipe, found.data, inventory.get(SLOT_PAPER));

		//Check if there's enough ink
		FluidStack[] fs = {
				new FluidStack(IIContent.fluidInkBlack, process.blackCost),
				new FluidStack(IIContent.fluidInkCyan, process.cyanCost),
				new FluidStack(IIContent.fluidInkMagenta, process.magentaCost),
				new FluidStack(IIContent.fluidInkYellow, process.yellowCost)
		};
		for(FluidStack f : fs)
			if(f.amount!=0&&!f.isFluidEqual(tank.drain(f, false)))
				return null;

		//Use resources
		inputHandler.extractItem(SLOT_PAPER, 1, false);
		for(FluidStack f : fs)
			tank.drain(f, true);

		//Return process
		return process;
	}

	@Override
	protected IIMultiblockProcess<PrintingRecipe> getProcessByName(String name)
	{
		return null;
	}

	@Override
	public float getProductionStep(IIMultiblockProcess<PrintingRecipe> process, boolean simulate)
	{
		int perTick = process.recipe.getTotalProcessEnergy()/process.maxTicks;
		if(energyStorage.extractEnergy(perTick, simulate) < perTick)
			return 0;

		return 1;
	}

	@Override
	protected boolean attemptProductionOutput(IIMultiblockProcess<PrintingRecipe> process)
	{
		assert process instanceof PrintingProcess;
		PrintingProcess printingProcess = (PrintingProcess)process;

		outputOrDrop(printingProcess.result, outputHandler, facing, getPOI("output"));
		return true;
	}

	@Override
	protected void onProductionFinish(IIMultiblockProcess<PrintingRecipe> process)
	{

	}

	@Override
	public IIGUI getGUI()
	{
		return IIGUI.PRINTING_PRESS;
	}

	//--- Fluid Handling ---//

	@Override
	protected IFluidTank[] getFluidTanks(int pos, EnumFacing side)
	{
		return new IFluidTank[]{tank};
	}

	@Override
	protected boolean canDrainTankFrom(int iTank, EnumFacing side)
	{
		return false;
	}

	@Override
	protected boolean canFillTankFrom(int iTank, EnumFacing side, FluidStack resource)
	{
		if(super.canFillTankFrom(iTank, side, resource))
			return IIContent.fluidInkBlack.equals(resource.getFluid())||
					IIContent.fluidInkCyan.equals(resource.getFluid())||
					IIContent.fluidInkMagenta.equals(resource.getFluid())||
					IIContent.fluidInkYellow.equals(resource.getFluid());
		return false;
	}

	@Override
	protected boolean isTankAvailable(int pos, int tank)
	{
		return true;
	}

	//--- Inventory ---//

	@Override
	public int getSlotLimit(int slot)
	{
		return slot==SLOT_OUTPUT?12: super.getSlotLimit(slot);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T)master().inputHandler;

		return super.getCapability(capability, facing);
	}

	//--- Tactile ---//

	@Override
	@Nullable
	public TactileHandler getTactileHandler()
	{
		return tactileHandler;
	}


	@Nonnull
	@Override
	public World getTactileWorld()
	{
		return world;
	}

	@Nonnull
	@Override
	public BlockPos getTactilePos()
	{
		return this.getPos();
	}

	@Nonnull
	@Override
	public EnumFacing getTactileFacing()
	{
		return facing;
	}

	@Override
	public boolean getIsTactileMirrored()
	{
		return mirrored;
	}

	@Override
	public boolean onTactileCollide(EntityAMTTactile tactile, Entity entity)
	{
		entity.attackEntityFrom(IIDamageSources.PRINTING_PRESS_DAMAGE, 1.5f);
		return false;
	}

	//--- Utilities ---//

	public static class PrintingRequest implements INBTSerializable<NBTTagCompound>
	{
		PrintingRecipe recipe;
		DataPacket data;
		int amount;

		public PrintingRequest(PrintingRecipe recipe, DataPacket data, int amount)
		{
			this.recipe = recipe;
			this.data = data;
			this.amount = amount;
		}

		public PrintingRequest(NBTTagCompound tag)
		{
			this.deserializeNBT(tag);
		}

		@Override
		public NBTTagCompound serializeNBT()
		{
			return EasyNBT.newNBT()
					.withInt("amount", amount)
					.withString("recipe", recipe.getName())
					.withTag("data", data.serializeNBT())
					.unwrap();
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			EasyNBT enbt = EasyNBT.wrapNBT(nbt);
			this.amount = enbt.getInt("amount");
			this.recipe = PrintingRecipe.getRecipe(PrintingRecipe.class, enbt.getString("recipe"));
			this.data = new DataPacket(enbt.getCompound("data"));
		}
	}

	/**
	 * An order for a page to be printed that's placed in the Printing Press' queue.
	 */
	public static class PrintingProcess extends IIMultiblockProcess<PrintingRecipe>
	{
		int blackCost, cyanCost, magentaCost, yellowCost;
		ItemStack result;

		public PrintingProcess(PrintingRecipe recipe, DataPacket packet, ItemStack input)
		{
			super(recipe);
			this.result = recipe.getFunction().apply(input, packet);

			int[] inks = recipe.getFunction().getInkTypesRequired(packet);
			this.cyanCost = inks[0];
			this.magentaCost = inks[1];
			this.yellowCost = inks[2];
			this.blackCost = inks[3];
		}

		@Override
		public NBTTagCompound serializeNBT()
		{
			return EasyNBT.wrapNBT(super.serializeNBT())
					.withItemStack("result", result)
					.withInt("black", blackCost)
					.withInt("cyan", cyanCost)
					.withInt("magenta", magentaCost)
					.withInt("yellow", yellowCost)
					.unwrap();
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			super.deserializeNBT(nbt);
			EasyNBT enbt = EasyNBT.wrapNBT(nbt);

			result = enbt.getItemStack("result");
			blackCost = enbt.getInt("black");
			cyanCost = enbt.getInt("cyan");
			magentaCost = enbt.getInt("magenta");
			yellowCost = enbt.getInt("yellow");
		}
	}

}
