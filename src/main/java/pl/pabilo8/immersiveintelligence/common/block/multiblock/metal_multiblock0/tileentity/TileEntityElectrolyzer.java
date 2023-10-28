package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import pl.pabilo8.immersiveintelligence.api.crafting.ElectrolyzerRecipe;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Electrolyzer;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockElectrolyzer;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionSingle;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

import static pl.pabilo8.immersiveintelligence.common.IIUtils.handleBucketTankInteraction;
import static pl.pabilo8.immersiveintelligence.common.IIUtils.outputFluidToTank;

/**
 * @author Pabilo8
 * @since 28-06-2019
 */
public class TileEntityElectrolyzer extends TileEntityMultiblockProductionSingle<TileEntityElectrolyzer, ElectrolyzerRecipe>
{
	public FluidTank[] tanks;

	public TileEntityElectrolyzer()
	{
		super(MultiblockElectrolyzer.INSTANCE);

		tanks = new FluidTank[]{new FluidTank(Electrolyzer.fluidCapacity), new FluidTank(Electrolyzer.fluidCapacity), new FluidTank(Electrolyzer.fluidCapacity)};
		inventory = NonNullList.withSize(6, ItemStack.EMPTY);
		this.energyStorage = new FluxStorageAdvanced(Electrolyzer.energyCapacity);
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);

		for(int i = 0; i < tanks.length; i++)
			tanks[i].readFromNBT(nbt.getCompoundTag("tank"+i));
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);

		for(int i = 0; i < tanks.length; i++)
			nbt.setTag("tank"+i, tanks[i].writeToNBT(new NBTTagCompound()));

	}

	@Override
	public void receiveMessageFromServer(NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);

		for(int i = 0; i < tanks.length; i++)
			if(message.hasKey("tank"+i))
				tanks[i].readFromNBT(message.getCompoundTag("tank"+i));
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
		return tanks;
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
			boolean update = handleBucketTankInteraction(tanks, inventory, 0, 1, 0, false);
			if(outputFluidToTank(tanks[1], 100, getPOIPos("output1"), this.world, this.facing.getOpposite()))
				update = true;
			if(outputFluidToTank(tanks[2], 100, getPOIPos("output2"), this.world, this.facing.getOpposite()))
				update = true;

			if(handleBucketTankInteraction(tanks, inventory, 2, 4, 1, true))
				update = true;
			if(handleBucketTankInteraction(tanks, inventory, 3, 5, 2, true))
				update = true;

			if(update&&world.getTotalWorldTime()%40==0)
				forceTileUpdate();
		}

	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return true;
	}

	@Override
	public IIGuiList getGUI()
	{
		return IIGuiList.GUI_ELECTROLYZER;
	}

	@Override
	public ElectrolyzerRecipe loadRecipeFromNBT(NBTTagCompound nbt)
	{
		return ElectrolyzerRecipe.loadFromNBT(nbt);
	}

	@Override
	protected IIMultiblockProcess<ElectrolyzerRecipe> findNewProductionProcess()
	{
		if(tanks[0].getFluidAmount() > 0&&energyStorage.getEnergyStored() > 0)
		{
			ElectrolyzerRecipe recipe = ElectrolyzerRecipe.findRecipe(tanks[0].getFluid());
			if(recipe!=null)
				return new IIMultiblockProcess<>(recipe);
		}
		return null;
	}

	@Override
	public float getProductionStep(IIMultiblockProcess<ElectrolyzerRecipe> process, boolean simulate)
	{
		return energyStorage.extractEnergy(process.recipe.energyPerTick, simulate)/(float)process.recipe.energyPerTick;
	}

	@Override
	protected boolean attemptProductionOutput(IIMultiblockProcess<ElectrolyzerRecipe> process)
	{
		//Cannot fill tanks
		ElectrolyzerRecipe recipe = process.recipe;
		for(int i = 1; i < 2; i++)
		{
			FluidStack output = recipe.fluidOutputs[i-1];
			int added = output==null?0: output.amount;

			if(tanks[i].getFluidAmount()+added > tanks[i].getCapacity())
				return false;
		}

		return true;
	}

	@Override
	protected void onProductionFinish(IIMultiblockProcess<ElectrolyzerRecipe> process)
	{
		ElectrolyzerRecipe recipe = process.recipe;
		tanks[0].drain(recipe.fluidInput, true);
		tanks[1].fill(recipe.fluidOutputs[0], true);
		tanks[2].fill(recipe.fluidOutputs[1], true);
	}
}
