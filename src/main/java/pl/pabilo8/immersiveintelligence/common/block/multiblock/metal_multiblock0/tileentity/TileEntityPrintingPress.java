package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.MultiFluidTank;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.api.crafting.PrintingRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.PrintingRecipe.PrintFunction;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataHandlingUtils;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.upgrade.IManagedUpgradableDevice;
import pl.pabilo8.immersiveintelligence.api.upgrade.Upgrade;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeManager;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedTextOverlay;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.PrintingPress;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockPrintingPress;
import pl.pabilo8.immersiveintelligence.common.entity.tactile.EntityAMTTactile;
import pl.pabilo8.immersiveintelligence.common.entity.tactile.TactileManager;
import pl.pabilo8.immersiveintelligence.common.entity.tactile.TactileManager.ITactileListener;
import pl.pabilo8.immersiveintelligence.common.util.IIDamageSources;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyCollection;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionMulti;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 13.12.2023
 * @since 28.06.2019
 */
public class TileEntityPrintingPress extends TileEntityMultiblockProductionMulti<TileEntityPrintingPress, PrintingRecipe>
		implements ITactileListener, IPlayerInteraction, IAdvancedTextOverlay, IManagedUpgradableDevice<TileEntityPrintingPress>
{
	@SyncNBT(time = 40, events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_RECIPE_CHANGED})
	public MultiFluidTank tank;
	@SyncNBT(name = "upgrades", events = SyncEvents.TILE_UPGRADES_MODIFIED)
	public UpgradeManager<TileEntityPrintingPress> upgradeManager;

	private IItemHandler inputHandler = getSingleInventoryHandler(MultiblockPrintingPress.SLOT_PAPER, true, true);
	private IItemHandler outputHandler = getSingleInventoryHandler(MultiblockPrintingPress.SLOT_OUTPUT, true, true);
	private TactileManager tactileManager = null;

	private EasyCollection<PrintingRequest, NBTTagCompound> printRequestsQueue;

	public TileEntityPrintingPress()
	{
		super(MultiblockPrintingPress.INSTANCE);
		this.tank = new MultiFluidTank(8000);
		this.energyStorage = new FluxStorageAdvanced(PrintingPress.energyCapacity);
		this.inventory = NonNullList.withSize(4, ItemStack.EMPTY);
		this.printRequestsQueue = new EasyCollection<>(PrintingRequest::new);
		this.upgradeManager = new UpgradeManager<>(this);
	}

	@Override
	public void onBeforeFirstTick()
	{
		super.onBeforeFirstTick();
		if(!world.isRemote)
			tactileManager = new TactileManager(multiblock, this);
	}

	@Override
	protected void dummyCleanup()
	{
		super.dummyCleanup();
		this.tank = null;
		this.inputHandler = null;
		this.outputHandler = null;
		this.tactileManager = null;
		this.upgradeManager = null;
	}

	@Override
	protected void onUpdate()
	{
		super.onUpdate();

		if(!world.isRemote)
			tactileManager.defaultize();

		if(IIUtils.handleBucketTankInteraction(tank, inventory, MultiblockPrintingPress.SLOT_BUCKET_IN, MultiblockPrintingPress.SLOT_BUCKET_OUT, true,
				fs -> IIContent.fluidInkBlack.equals(fs.getFluid())||
						IIContent.fluidInkCyan.equals(fs.getFluid())||
						IIContent.fluidInkMagenta.equals(fs.getFluid())||
						IIContent.fluidInkYellow.equals(fs.getFluid())))
			forceTileUpdate();
	}


	//--- Properties ---//

	@Override
	public void receiveData(DataPacket packet, int pos)
	{
		if(IIDataHandlingUtils.isCallbackPacket(packet))
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
			int amount = IIDataHandlingUtils.optionalInt('a', packet).orElse(1);
			if(amount <= 0)
				return;

			String printingMode = IIDataHandlingUtils.asString('m', packet);
			PrintingRecipe.streamRecipes(PrintingRecipe.class)
					.filter(recipe -> recipe.getInput().matchesItemStack(inventory.get(MultiblockPrintingPress.SLOT_PAPER)))
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
			case MultiblockPrintingPress.SLOT_PAPER:
				return Utils.compareToOreName(stack, "pageEmpty");
			case MultiblockPrintingPress.SLOT_OUTPUT:
				return Utils.compareToOreName(stack, "pageWritten")||Utils.compareToOreName(stack, "pageEmpty");
			case MultiblockPrintingPress.SLOT_BUCKET_IN:
			case MultiblockPrintingPress.SLOT_BUCKET_OUT:
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
		//Check for queued orders
		if(printRequestsQueue.isEmpty())
			return null;
		PrintingRequest found = printRequestsQueue.get(0);

		ItemStack input = inputHandler.extractItem(MultiblockPrintingPress.SLOT_PAPER, 1, true);
		if(!found.recipe.getInput().matchesItemStack(input))
			return null;

		//Check if there's enough paper
		PrintFunction function = found.recipe.getFunction();
		ItemStack result = function.apply(input, found.data);
		int[] inkCost = function.getInkTypesRequired(found.data);

		//Check if the required upgrade is installed
		Upgrade requiredUpgrade = function.getUpgradeRequired();
		if(requiredUpgrade!=null&&!isUpgradeInstalled(requiredUpgrade))
			return null;

		//Check if there's enough ink
		FluidStack[] fs = {
				new FluidStack(IIContent.fluidInkCyan, inkCost[0]),
				new FluidStack(IIContent.fluidInkMagenta, inkCost[1]),
				new FluidStack(IIContent.fluidInkYellow, inkCost[2]),
				new FluidStack(IIContent.fluidInkBlack, inkCost[3])
		};
		for(FluidStack f : fs)
			if(f.amount!=0&&!f.isFluidEqual(tank.drain(f, false)))
				return null;

		//Use resources
		inputHandler.extractItem(MultiblockPrintingPress.SLOT_PAPER, 1, false);
		for(FluidStack f : fs)
			tank.drain(f, true);

		//Decrease to-be-printed page amount, remove the task if all pages were printed
		if(found.amount-- <= 1)
			printRequestsQueue.remove(0);
		//Return the process
		return new IIMultiblockProcess<>(found.recipe)
				.withNBT(nbt -> nbt.withItemStack("result", result)
						.withInt("cyan", inkCost[0])
						.withInt("magenta", inkCost[1])
						.withInt("yellow", inkCost[2])
						.withInt("black", inkCost[3]));
	}

	@Override
	protected IIMultiblockProcess<PrintingRecipe> getProcessByName(String name)
	{
		PrintingRecipe recipe = IIMultiblockRecipe.getRecipe(PrintingRecipe.class, name);
		return recipe==null?null: new IIMultiblockProcess<>(recipe);
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
		ItemStack result = process.processData.getItemStack("result");
		outputOrDrop(result, outputHandler, facing, getPOI("output"));
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
		return slot==MultiblockPrintingPress.SLOT_OUTPUT?12: super.getSlotLimit(slot);
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
	public TactileManager getTactileHandler()
	{
		return tactileManager;
	}

	@Override
	public boolean onTactileCollide(EntityAMTTactile tactile, Entity entity)
	{
		entity.attackEntityFrom(IIDamageSources.PRINTING_PRESS_DAMAGE, 1.5f);
		return false;
	}

	//--- IPlayerInteraction ---//

	@Override
	public boolean interact(EnumFacing side, EntityPlayer player, EnumHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		if(isPOI("fluid_tank"))
		{
			TileEntityPrintingPress master = master();
			return master!=null&&FluidUtil.interactWithFluidHandler(player, hand, master.tank);
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

		TileEntityPrintingPress master = master();
		if(master!=null&&isPOI("fluid_tank"))
			return new String[]{IIUtils.getFluidNameOverlayText(master.tank.getFluid())};
		return new String[0];
	}

	//--- IManagedUpgradableDevice ---//

	@Nonnull
	@Override
	public UpgradeManager<TileEntityPrintingPress> getUpgradeManager()
	{
		return upgradeManager;
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

		public PrintingRequest()
		{

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

	//this.result = recipe.getFunction().apply(input, packet);

}
