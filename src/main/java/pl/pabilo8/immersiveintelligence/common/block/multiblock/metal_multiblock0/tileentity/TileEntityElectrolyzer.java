package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.crafting.ElectrolyzerRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedTextOverlay;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Electrolyzer;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockElectrolyzer;
import pl.pabilo8.immersiveintelligence.common.util.IIEnergyStorage;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.fluid.FilteredFluidTank;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionSingle;
import static pl.pabilo8.immersiveintelligence.common.IIUtils.handleBucketTankInteraction;
import static pl.pabilo8.immersiveintelligence.common.IIUtils.outputFluidToTank;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 07.10.2026
 * @ii-approved 0.3.1
 * @since 28.06.2019
 */
public class TileEntityElectrolyzer extends TileEntityMultiblockProductionSingle<TileEntityElectrolyzer, ElectrolyzerRecipe> implements IPlayerInteraction, IAdvancedTextOverlay
{
	@SyncNBT(name = "tank0", events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_CUSTOM1})
	public FilteredFluidTank tankInput;
	@SyncNBT(name = "tank1", events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_CUSTOM1})
	public FilteredFluidTank tankOutput1;
	@SyncNBT(name = "tank2", events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_CUSTOM1})
	public FilteredFluidTank tankOutput2;

	public TileEntityElectrolyzer()
	{
		super(MultiblockElectrolyzer.INSTANCE);

		this.tankInput = new FilteredFluidTank(Electrolyzer.fluidCapacity);
		this.tankOutput1 = new FilteredFluidTank(Electrolyzer.fluidCapacity);
		this.tankOutput2 = new FilteredFluidTank(Electrolyzer.fluidCapacity);

		this.inventory = NonNullList.withSize(6, ItemStack.EMPTY);
		this.energyStorage = new IIEnergyStorage(Electrolyzer.energyCapacity);
	}

	@Override
	protected void dummyCleanup()
	{
		super.dummyCleanup();
		this.tankInput = tankOutput1 = tankOutput2 = null;
	}

	@Override
	protected IFluidTank[] getFluidTanks(int pos, EnumFacing side)
	{
		return new FilteredFluidTank[]{tankInput, tankOutput1, tankOutput2};
	}

	@Override
	protected boolean isTankAvailable(int pos, int tank)
	{
		switch(tank)
		{
			//Already Checked
			case 0:
				return true;
			case 1:
				return multiblock.isPointOfInterest(pos, "output1");
			case 2:
				return multiblock.isPointOfInterest(pos, "output2");
		}
		return false;
	}

	@Override
	protected void onUpdate()
	{
		super.onUpdate();

		if(!world.isRemote&&world.getTotalWorldTime()%10==0)
		{
			boolean update = handleBucketTankInteraction(tankInput, inventory, MultiblockElectrolyzer.SLOT_T0_BUCKET_INPUT, MultiblockElectrolyzer.SLOT_T0_BUCKET_OUTPUT, true);
			if(outputFluidToTank(tankOutput1, 100, getPOIPos("output1"), this.world, this.facing.getOpposite()))
				update = true;
			if(outputFluidToTank(tankOutput2, 100, getPOIPos("output2"), this.world, this.facing.getOpposite()))
				update = true;

			if(handleBucketTankInteraction(tankOutput1, inventory, MultiblockElectrolyzer.SLOT_T1_BUCKET_INPUT, MultiblockElectrolyzer.SLOT_T1_BUCKET_OUTPUT, true))
				update = true;
			if(handleBucketTankInteraction(tankOutput2, inventory, MultiblockElectrolyzer.SLOT_T2_BUCKET_INPUT, MultiblockElectrolyzer.SLOT_T2_BUCKET_OUTPUT, true))
				update = true;

			if(update)
				updateTileForEvent(SyncEvents.TILE_CUSTOM1);
		}

	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return true;
	}

	@Override
	public IIGUI getGUI()
	{
		return IIGUI.ELECTROLYZER;
	}

	@Override
	protected IIMultiblockProcess<ElectrolyzerRecipe> findNewProductionProcess()
	{
		if(getRedstoneAtPos(0))
			return null;
		return ElectrolyzerRecipe.streamRecipes(ElectrolyzerRecipe.class)
				.filter(recipe -> recipe.fluidInput.isFluidStackIdentical(tankInput.drain(recipe.fluidInput, false)))
				.findFirst().map(recipe -> {
					IIMultiblockProcess<ElectrolyzerRecipe> process = new IIMultiblockProcess<>(recipe);
					return reserveInput(process, false)?process: null;
				}).orElse(null);
	}

	@Override
	protected IIMultiblockProcess<ElectrolyzerRecipe> getProcessByName(String name)
	{
		ElectrolyzerRecipe recipe = IIMultiblockRecipe.getRecipe(ElectrolyzerRecipe.class, name);
		return recipe==null?null: new IIMultiblockProcess<>(recipe);
	}

	@Override
	public float getProductionStep(IIMultiblockProcess<ElectrolyzerRecipe> process, boolean simulate)
	{
		if(getRedstoneAtPos(0))
			return 0;
		if(!reserveInput(process, simulate)||!energyStorage.hasEnergy(process.recipe.getEnergyPerTick()))
			return 0;
		return energyStorage.tryConsumeEnergy(process.recipe.getEnergyPerTick(), simulate)?1: 0;
	}

	@Override
	protected boolean attemptProductionOutput(IIMultiblockProcess<ElectrolyzerRecipe> process)
	{
		if(!reserveInput(process, false))
			return false;
		//Cannot fill tanks
		ElectrolyzerRecipe recipe = process.recipe;

		if(tankOutput1.fill(recipe.fluidOutputs[0], false)!=recipe.fluidOutputs[0].amount)
			return false;

		return recipe.fluidOutputs[1]==null||tankOutput2.fill(recipe.fluidOutputs[1], false)==recipe.fluidOutputs[1].amount;
	}

	@Override
	protected void onProductionFinish(IIMultiblockProcess<ElectrolyzerRecipe> process)
	{
		ElectrolyzerRecipe recipe = process.recipe;

		tankOutput1.fill(recipe.fluidOutputs[0], true);
		if(recipe.fluidOutputs[1]!=null)
			tankOutput2.fill(recipe.fluidOutputs[1], true);
	}

	private boolean reserveInput(IIMultiblockProcess<ElectrolyzerRecipe> process, boolean simulate)
	{
		if(process.processData.unwrap().getBoolean("inputReserved"))
			return true;
		FluidStack input = process.recipe.fluidInput;
		if(!input.isFluidStackIdentical(tankInput.drain(input, false)))
			return false;
		if(!simulate)
		{
			tankInput.drain(input, true);
			process.processData.withBoolean("inputReserved", true);
			markDirty();
		}
		return true;
	}

	//--- IPlayerInteraction ---//

	@Override
	public boolean interact(EnumFacing side, EntityPlayer player, EnumHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		if(isPOI("visible_tank"))
		{
			TileEntityElectrolyzer master = master();
			return master!=null&&!master.tankInput.interactWithItem(player, hand, heldItem, () -> {
				master.markDirty();
				master.updateTileForEvent(SyncEvents.TILE_CUSTOM1);
			});
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

		TileEntityElectrolyzer master = master();
		if(master!=null&&isPOI("visible_tank"))
			return new String[]{IIUtils.getFluidNameOverlayText(master.tankInput.getFluid())};

		return new String[0];
	}
}
