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
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataHandlingUtils;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.PrintingPress;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockPrintingPress;
import pl.pabilo8.immersiveintelligence.common.entity.tactile.EntityAMTTactile;
import pl.pabilo8.immersiveintelligence.common.entity.tactile.TactileHandler;
import pl.pabilo8.immersiveintelligence.common.entity.tactile.TactileHandler.ITactileListener;
import pl.pabilo8.immersiveintelligence.common.item.ItemIIPrintedPage.SubItems;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIDamageSources;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.lambda.NBTTagCollector;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionBase;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionMulti;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Pabilo8
 * @updated 13.12.2023
 * @since 28.06.2019
 */
public class TileEntityPrintingPress extends TileEntityMultiblockProductionMulti<TileEntityPrintingPress, TileEntityPrintingPress.PrintOrder> implements ITactileListener
{
	/**
	 * Print order parser registry
	 */
	public static final HashMap<String, Function<DataPacket, PrintOrder>> PRINT_ORDER_PARSERS = new HashMap<>();
	public static final Function<DataPacket, PrintOrder> ORDER_PARSER_TEXT;

	/**
	 * Inventory slots IDs
	 */
	public static final int SLOT_PAPER = 0, SLOT_OUTPUT = 1, SLOT_BUCKET_IN = 2, SLOT_BUCKET_OUT = 3;

	static
	{
		PRINT_ORDER_PARSERS.put("text", ORDER_PARSER_TEXT = (packet -> {
			String text = packet.getPacketVariable('t').toString();
			float c = 0, m = 0, y = 0, k = 0;

			if(!text.isEmpty())
			{
				Pattern pattern = Pattern.compile("<hexcol;([A-Fa-f0-9]{6});(.*?)>");
				Matcher matcher = pattern.matcher(text);

				while(matcher.find())
				{
					// Get CMYK proportions
					float[] cmykValues = IIColor.fromHex(matcher.group(1)).getCMYK();

					// Calculate ink usage based on CMYK percentages
					c += PrintingPress.printInkUsage*cmykValues[0]; // Fraction of 2 units for cyan
					m += PrintingPress.printInkUsage*cmykValues[1]; // Fraction of 2 units for magenta
					y += PrintingPress.printInkUsage*cmykValues[2]; // Fraction of 2 units for yellow
					k += PrintingPress.printInkUsage*cmykValues[3]; // Fraction of 2 units for black
				}

				//For all other text calculate the black ink cost
				k += pattern.matcher(text)
						.replaceAll("")
						.replaceAll(" ", "")
						.length()*PrintingPress.printInkUsage;
			}

			ItemStack stack = IIContent.itemPrintedPage.getStack(SubItems.TEXT);
			EasyNBT.wrapNBT(stack).withString("text", text);

			return new PrintOrder(stack, (int)k, (int)c, (int)m, (int)y);
		}));
	}

	public ArrayDeque<QueuedPrintOrder> printQueue = new ArrayDeque<>();
	@SyncNBT(time = 40, events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_RECIPE_CHANGED})
	public MultiFluidTank tank;

	IItemHandler inputHandler = new IEInventoryHandler(1, this, SLOT_PAPER, true, true);
	IItemHandler outputHandler = new IEInventoryHandler(1, this, SLOT_OUTPUT, true, true);
	TactileHandler tactileHandler = null;

	public TileEntityPrintingPress()
	{
		super(MultiblockPrintingPress.INSTANCE);
		energyStorage = new FluxStorageAdvanced(PrintingPress.energyCapacity);
		tank = new MultiFluidTank(8000);
		inventory = NonNullList.withSize(4, ItemStack.EMPTY);
	}

	//--- NBT Handling ---//


	@Override
	protected void onUpdate()
	{
		super.onUpdate();

		if(tactileHandler==null)
			tactileHandler = new TactileHandler(multiblock, this);
		tactileHandler.defaultize();

		if(IIUtils.handleBucketTankInteraction(tank, inventory, SLOT_BUCKET_IN, SLOT_BUCKET_OUT, false,
				fs -> IIContent.fluidInkBlack.equals(fs.getFluid())||
						IIContent.fluidInkCyan.equals(fs.getFluid())||
						IIContent.fluidInkMagenta.equals(fs.getFluid())||
						IIContent.fluidInkYellow.equals(fs.getFluid())))
			forceTileUpdate();
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		if(isDummy())
			return;
		if(nbt.hasKey("print_queue"))
		{
			printQueue.clear();
			for(NBTBase entry : nbt.getTagList("print_queue", EasyNBT.TAG_COMPOUND))
			{
				NBTTagCompound tag = (NBTTagCompound)entry;
				printQueue.add(new QueuedPrintOrder(
						tag.getInteger("amount"),
						new PrintOrder(tag.getCompoundTag("order"))
				));
			}
		}
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);
		if(isDummy())
			return;
		EasyNBT.wrapNBT(nbt).withTag("print_queue",
				printQueue.stream()
						.map(tuple -> EasyNBT.newNBT()
								.withInt("amount", tuple.amount)
								.withTag("order", tuple.order.writeToNBT())
						)
						.map(EasyNBT::unwrap)
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


			PrintOrder printOrder = PRINT_ORDER_PARSERS.getOrDefault(IIDataHandlingUtils.asString('m', packet), ORDER_PARSER_TEXT).apply(packet);
			if(printOrder!=null)
				printQueue.add(new QueuedPrintOrder(amount, printOrder));
			forceTileUpdate();
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
	protected IIMultiblockProcess<PrintOrder> findNewProductionProcess()
	{
		// Check for queued orders
		if(printQueue.isEmpty())
			return null;
		QueuedPrintOrder found = printQueue.getFirst();
		if(found.amount-- <= 1)
			printQueue.remove();

		//Check if there's enough paper
		if(!isStackValid(SLOT_PAPER, inventory.get(SLOT_PAPER)))
			return null;

		//Check if there's enough ink
		FluidStack[] fs = {
				new FluidStack(IIContent.fluidInkBlack, found.order.blackCost),
				new FluidStack(IIContent.fluidInkCyan, found.order.cyanCost),
				new FluidStack(IIContent.fluidInkMagenta, found.order.magentaCost),
				new FluidStack(IIContent.fluidInkYellow, found.order.yellowCost)
		};
		for(FluidStack f : fs)
			if(f.amount!=0&&!f.isFluidEqual(tank.drain(f, false)))
				return null;

		//Use resources
		inputHandler.extractItem(SLOT_PAPER, 1, false);
		for(FluidStack f : fs)
			tank.drain(f, true);

		//Return process
		return new IIMultiblockProcess<>(found.order);
	}

	@Override
	protected IIMultiblockProcess<PrintOrder> getProcessFromNBT(EasyNBT nbt)
	{
		return null;
	}

	@Override
	public float getProductionStep(IIMultiblockProcess<PrintOrder> process, boolean simulate)
	{
		int perTick = process.recipe.getTotalProcessEnergy()/process.maxTicks;
		if(energyStorage.extractEnergy(perTick, simulate) < perTick)
			return 0;

		return 1;
	}

	@Override
	protected boolean attemptProductionOutput(IIMultiblockProcess<PrintOrder> process)
	{
		outputOrDrop(process.recipe.result, outputHandler, facing, getPOI("output"));
		return true;
	}

	@Override
	protected void onProductionFinish(IIMultiblockProcess<PrintOrder> process)
	{

	}

	@Override
	public IIGuiList getGUI()
	{
		return IIGuiList.GUI_PRINTING_PRESS;
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

	/**
	 * An order for a page to be printed that's placed in the Printing Press' queue.
	 */
	public static class QueuedPrintOrder
	{
		int amount;
		PrintOrder order;

		public QueuedPrintOrder(int amount, PrintOrder order)
		{
			this.amount = amount;
			this.order = order;
		}
	}

	/**
	 * Data of a singular print order
	 */
	public static class PrintOrder implements TileEntityMultiblockProductionBase.IIIMultiblockRecipe
	{
		int blackCost, cyanCost, magentaCost, yellowCost;
		ItemStack result;

		public PrintOrder(ItemStack result, int blackCost, int cyanCost, int magentaCost, int yellowCost)
		{
			this.result = result;
			this.blackCost = blackCost;
			this.cyanCost = cyanCost;
			this.magentaCost = magentaCost;
			this.yellowCost = yellowCost;
		}

		public PrintOrder(NBTTagCompound nbt)
		{
			loadFromNBT(nbt);
		}

		PrintOrder loadFromNBT(NBTTagCompound nbt)
		{
			result = new ItemStack(nbt.getCompoundTag("result"));
			blackCost = nbt.getInteger("black");
			cyanCost = nbt.getInteger("cyan");
			magentaCost = nbt.getInteger("magenta");
			yellowCost = nbt.getInteger("yellow");
			return this;
		}

		@Override
		public int getTotalProcessTime()
		{
			return PrintingPress.printTime;
		}

		@Override
		public int getTotalProcessEnergy()
		{
			return PrintingPress.energyUsage;
		}

		@Override
		public int getMultipleProcessTicks()
		{
			return 0;
		}

		@Override
		public EasyNBT writeToNBT()
		{
			return EasyNBT.newNBT()
					.withItemStack("result", result)
					.withInt("black", blackCost)
					.withInt("cyan", cyanCost)
					.withInt("magenta", magentaCost)
					.withInt("yellow", yellowCost);
		}
	}
}
