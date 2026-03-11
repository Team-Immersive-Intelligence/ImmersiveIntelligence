package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import pl.pabilo8.immersiveintelligence.api.crafting.CoagulatorRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Coagulator;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockCoagulator;
import pl.pabilo8.immersiveintelligence.common.util.FilteredFluidTank;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionSingle;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 22.12.2025
 * @ii-approved 0.3.1
 * @since 04.03.2021
 */
public class TileEntityCoagulator extends TileEntityMultiblockProductionSingle<TileEntityCoagulator, CoagulatorRecipe>
{
	@SyncNBT(events = {SyncEvents.TILE_CUSTOM1, SyncEvents.TILE_RECIPE_CHANGED})
	public FluidTank tankCoagulant;
	@SyncNBT(events = {SyncEvents.TILE_CUSTOM2, SyncEvents.TILE_RECIPE_CHANGED})
	public FluidTank tankInput;
	@SyncNBT(events = {SyncEvents.TILE_CUSTOM2, SyncEvents.TILE_RECIPE_CHANGED})
	public NonNullList<ItemStack> bucketStacks;
	@SyncNBT(events = SyncEvents.TILE_RECIPE_CHANGED)
	public CraneAnimation craneAnimation = CraneAnimation.NONE;
	@SyncNBT(events = SyncEvents.TILE_RECIPE_CHANGED)
	public int[] bucketProgress;
	@SyncNBT(events = SyncEvents.TILE_RECIPE_CHANGED)
	public int cranePosition = 0, craneCurrentBucket = -1, craneAnimationProgress = 0;
	private IEInventoryHandler outputHandler;

	public TileEntityCoagulator()
	{
		super(MultiblockCoagulator.INSTANCE);
		this.inventory = NonNullList.withSize(5, ItemStack.EMPTY);
		this.bucketStacks = NonNullList.withSize(6, ItemStack.EMPTY);
		this.bucketProgress = new int[]{0, 0, 0, 0, 0, 0};
		this.energyStorage = new FluxStorageAdvanced(Coagulator.energyCapacity);
		this.tankInput = new FilteredFluidTank(Coagulator.fluidCapacity)
				.withInputFilter(fluidStack -> IIMultiblockRecipe.streamRecipes(CoagulatorRecipe.class)
						.anyMatch(recipe -> recipe.fluidInput.isFluidEqual(fluidStack)));
		this.tankCoagulant = new FilteredFluidTank(Coagulator.fluidCapacity)
				.withInputFilter(fluidStack -> IIMultiblockRecipe.streamRecipes(CoagulatorRecipe.class)
						.anyMatch(recipe -> recipe.coagulantInput.isFluidEqual(fluidStack)));
		this.outputHandler = getSingleInventoryHandler(MultiblockCoagulator.SLOT_OUTPUT, true, true);
	}

	@Override
	protected void dummyCleanup()
	{
		super.dummyCleanup();
		this.bucketStacks = null;
		this.bucketProgress = null;
		this.tankInput = this.tankCoagulant = null;
	}

	@Override
	protected void onUpdate()
	{
		//Handle tank interaction
		if(!world.isRemote)
		{
			//Left tank
			if(IIUtils.handleBucketTankInteraction(tankCoagulant, inventory,
					MultiblockCoagulator.SLOT_INPUT1, MultiblockCoagulator.SLOT_OUTPUT1, true))
				updateTileForEvent(SyncEvents.TILE_CUSTOM1);
			//Right tank
			if(IIUtils.handleBucketTankInteraction(tankInput, inventory,
					MultiblockCoagulator.SLOT_INPUT2, MultiblockCoagulator.SLOT_OUTPUT2, true))
				updateTileForEvent(SyncEvents.TILE_CUSTOM2);
		}

		//Handle coagulate production logic
		super.onUpdate();

		//Handle drying
		for(int i = 0; i < bucketProgress.length; i++)
		{
			bucketProgress[i] = Math.max(0, bucketProgress[i]-1);
			//Drying output
			if(!world.isRemote&&bucketProgress[i]==0&&!bucketStacks.get(i).isEmpty())
			{
				bucketProgress[i] = 0;
				outputOrDrop(bucketStacks.get(i), null, getDirection("item_outputs"), getPOI(MultiblockPOI.ITEM_OUTPUT)[i]);
				bucketStacks.set(i, ItemStack.EMPTY);
			}

		}

		//Handle crane logic
		if(!inventory.get(MultiblockCoagulator.SLOT_OUTPUT).isEmpty())
		{
			//Set crane target
			if(craneAnimation==CraneAnimation.NONE&&craneCurrentBucket==-1)
				for(int i = 0; i < bucketStacks.size(); i++)
					if(bucketStacks.get(i).isEmpty())
					{
						craneCurrentBucket = i;
						craneAnimationProgress = 0;
						updateTileForEvent(SyncEvents.TILE_RECIPE_CHANGED);
						break;
					}

			if(craneCurrentBucket!=-1)
			{
				if(craneAnimationProgress > 0)
					craneAnimationProgress--;
				else
				{
					//Progress the animation
					switch(craneAnimation)
					{
						case MOVE_BUCKET:
						case MOVE_BACK:
						{
							cranePosition += Integer.compare(craneCurrentBucket, cranePosition);
							if(cranePosition==craneCurrentBucket)
								craneAnimation = IIUtils.cycleEnum(true, CraneAnimation.class, craneAnimation);
						}
						break;
						case MOVE_MIXER:
						{
							cranePosition += Integer.compare(2, cranePosition);
							if(cranePosition==2)
								craneAnimation = IIUtils.cycleEnum(true, CraneAnimation.class, craneAnimation);
						}
						break;
						case RETURN:
						{
							//Place down the bucket and begin its drying process
							if(!world.isRemote)
							{
								ItemStack eff = outputHandler.extractItem(0, 1, false);
								bucketProgress[craneCurrentBucket] = CoagulatorRecipe.getDryingTimeFor(eff);
								bucketStacks.set(craneCurrentBucket, eff);
								updateTileForEvent(SyncEvents.TILE_RECIPE_CHANGED);
							}

							this.craneCurrentBucket = -1;
							this.craneAnimationProgress = 0;
							this.craneAnimation = CraneAnimation.NONE;
						}
						break;
						//Cycle animation forward
						default:
							craneAnimation = IIUtils.cycleEnum(true, CraneAnimation.class, craneAnimation);
							break;
					}
					//set time after change
					switch(craneAnimation)
					{
						case NONE:
							break;
						case MOVE_BUCKET:
						case MOVE_BACK:
							craneAnimationProgress = (cranePosition==craneCurrentBucket)?0: Coagulator.craneMoveTime;
							break;
						case MOVE_MIXER:
							craneAnimationProgress = (cranePosition==2)?0: Coagulator.craneMoveTime;
							break;
						case ROTATE_IN:
						case ROTATE_OUT:
							craneAnimationProgress = Coagulator.craneMoveTime;
							break;
						default:
							craneAnimationProgress = Coagulator.craneGrabTime;
							break;
					}

				}
			}
		}
	}

	@Override
	protected int[] listAllPOI(MultiblockPOI poi)
	{
		switch(poi)
		{
			case FLUID_INPUT:
				return getPOI("fluid_inputs");
			case ITEM_OUTPUT:
				return getPOI("item_outputs");
			case ENERGY_INPUT:
				return getPOI("energy");
			case REDSTONE_INPUT:
				return getPOI("redstone");
			case MISC_CONTROL_PANEL:
				return getPOI("control_panel");
			default:
				return new int[0];
		}
	}

	@Override
	protected IIMultiblockProcess<CoagulatorRecipe> findNewProductionProcess()
	{
		//Both tanks must have some fluid
		if(tankInput.getFluidAmount()==0||tankCoagulant.getFluidAmount()==0)
			return null;
		//Do not overfill the tank
		if(inventory.get(MultiblockCoagulator.SLOT_OUTPUT).getCount() >= 64)
			return null;

		return IIMultiblockRecipe.streamRecipes(CoagulatorRecipe.class)
				.filter(recipe -> recipe.fluidInput.isFluidStackIdentical(tankInput.drain(recipe.fluidInput, false)))
				.filter(recipe -> recipe.coagulantInput.isFluidStackIdentical(tankCoagulant.drain(recipe.coagulantInput, false)))
				.findFirst()
				.map(IIMultiblockProcess::new).orElse(null);
	}

	@Override
	protected IIMultiblockProcess<CoagulatorRecipe> getProcessByName(String name)
	{
		CoagulatorRecipe recipe = IIMultiblockRecipe.getRecipe(CoagulatorRecipe.class, name);
		return recipe==null?null: new IIMultiblockProcess<>(recipe);
	}

	@Override
	public float getProductionStep(IIMultiblockProcess<CoagulatorRecipe> process, boolean simulate)
	{
		if(energyStorage.extractEnergy(process.recipe.getEnergyPerTick(), true)!=process.recipe.getEnergyPerTick())
			return 0;
		energyStorage.extractEnergy(process.recipe.getEnergyPerTick(), simulate);
		return 1f;
	}

	@Override
	protected boolean attemptProductionOutput(IIMultiblockProcess<CoagulatorRecipe> process)
	{
		return outputHandler.insertItem(0, process.recipe.itemOutput, true).isEmpty();
	}

	@Override
	protected void onProductionFinish(IIMultiblockProcess<CoagulatorRecipe> process)
	{
		outputHandler.insertItem(0, process.recipe.itemOutput, false);
	}

	public float getDryingProgressForSlot(int slotID)
	{
		if(bucketStacks.get(slotID).isEmpty())
			return 0;
		int totalTime = CoagulatorRecipe.getDryingTimeFor(bucketStacks.get(slotID));
		if(totalTime <= 0)
			return 0;
		return 1f-(float)bucketProgress[slotID]/(float)totalTime;
	}

	@Override
	protected IFluidTank[] getFluidTanks(int pos, EnumFacing side)
	{
		if(pos==multiblock.getPointOfInterest("tank_coagulant"))
			return new IFluidTank[]{tankCoagulant};
		else if(pos==multiblock.getPointOfInterest("tank_input"))
			return new IFluidTank[]{tankInput};
		return new IFluidTank[0];
	}

	@Nullable
	@Override
	public IIGUI getGUI()
	{
		return IIGUI.COAGULATOR;
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return true;
	}

	@Override
	public NonNullList<ItemStack> getDroppedItems()
	{
		if(this.isDummy())
			return super.getDroppedItems();
		NonNullList<ItemStack> drops = NonNullList.withSize(inventory.size(), ItemStack.EMPTY);
		for(int i = 0; i < inventory.size(); i++)
			if(i!=MultiblockCoagulator.SLOT_OUTPUT)
				drops.set(i, inventory.get(i));
		return drops;
	}

	public enum CraneAnimation implements ISerializableEnum
	{
		NONE,
		MOVE_BUCKET,
		REACH,
		PICK,
		MOVE_MIXER,
		ROTATE_IN,
		PUT,
		PULL,
		ROTATE_OUT,
		MOVE_BACK,
		PLACE,
		RETURN
	}
}
