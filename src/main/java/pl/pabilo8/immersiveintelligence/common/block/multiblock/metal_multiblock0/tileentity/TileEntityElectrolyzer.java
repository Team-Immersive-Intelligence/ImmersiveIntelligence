package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
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
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionSingle;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

import static pl.pabilo8.immersiveintelligence.common.IIUtils.handleBucketTankInteraction;
import static pl.pabilo8.immersiveintelligence.common.IIUtils.outputFluidToTank;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 08.06.2025
 * @ii-approved 0.3.1
 * @since 28.06.2019
 */
public class TileEntityElectrolyzer extends TileEntityMultiblockProductionSingle<TileEntityElectrolyzer, ElectrolyzerRecipe> implements IPlayerInteraction, IAdvancedTextOverlay
{
	@SyncNBT(name = "tank0", events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_CUSTOM1})
	public FluidTank tankInput;
	@SyncNBT(name = "tank1", events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_CUSTOM1})
	public FluidTank tankOutput1;
	@SyncNBT(name = "tank2", events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_CUSTOM1})
	public FluidTank tankOutput2;

	public TileEntityElectrolyzer()
	{
		super(MultiblockElectrolyzer.INSTANCE);

		this.tankInput = new FluidTank(Electrolyzer.fluidCapacity);
		this.tankOutput1 = new FluidTank(Electrolyzer.fluidCapacity);
		this.tankOutput2 = new FluidTank(Electrolyzer.fluidCapacity);

		this.inventory = NonNullList.withSize(6, ItemStack.EMPTY);
		this.energyStorage = new FluxStorageAdvanced(Electrolyzer.energyCapacity);
	}

	@Override
	protected void dummyCleanup()
	{
		super.dummyCleanup();
		this.tankInput = tankOutput1 = tankOutput2 = null;
	}

	@Override
	protected int[] listAllPOI(MultiblockPOI poi)
	{
		switch(poi)
		{
			case ENERGY_INPUT:
				return getPOI("energy_input");
			case REDSTONE_INPUT:
				return getPOI("redstone_input");
			case FLUID_INPUT:
				return getPOI("fluid_input");
			case FLUID_OUTPUT:
				return getPOI("fluid_output");
		}
		return new int[0];
	}

	@Override
	protected IFluidTank[] getFluidTanks(int pos, EnumFacing side)
	{
		return new FluidTank[]{tankInput, tankOutput1, tankOutput2};
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
		if(tankInput.getFluidAmount() > 0&&energyStorage.getEnergyStored() > 0)
		{
			return ElectrolyzerRecipe.streamRecipes(ElectrolyzerRecipe.class)
					.filter(recipe -> recipe.fluidInput.isFluidStackIdentical(tankInput.drain(recipe.fluidInput, false)))
					.findFirst()
					.map(IIMultiblockProcess::new).orElse(null);
		}
		return null;
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
		return energyStorage.extractEnergy(process.recipe.getEnergyPerTick(), simulate)/(float)process.recipe.getEnergyPerTick();
	}

	@Override
	protected boolean attemptProductionOutput(IIMultiblockProcess<ElectrolyzerRecipe> process)
	{
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
		tankInput.drain(recipe.fluidInput, true);
		tankOutput1.fill(recipe.fluidOutputs[0], true);
		tankOutput2.fill(recipe.fluidOutputs[1], true);
	}

	@Override
	public boolean interact(EnumFacing side, EntityPlayer player, EnumHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		if(isPOI("visible_tank"))
		{
			TileEntityElectrolyzer master = master();
			return master!=null&&FluidUtil.interactWithFluidHandler(player, hand, master.tankInput);
		}
		return false;
	}

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
