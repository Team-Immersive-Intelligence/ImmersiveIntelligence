package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.api.tool.ChemthrowerHandler;
import blusunrize.immersiveengineering.api.tool.ChemthrowerHandler.ChemthrowerEffect;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.api.crafting.BathingRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.ChemicalBath;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockChemicalBath;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionSingle;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 11.12.2025
 * @ii-approved 0.3.1
 * @since 28.06.2019
 */
public class TileEntityChemicalBath extends TileEntityMultiblockProductionSingle<TileEntityChemicalBath, BathingRecipe> implements IPlayerInteraction
{
	@SyncNBT(events = {SyncEvents.TILE_CUSTOM1, SyncEvents.TILE_RECIPE_CHANGED})
	public FluidTank tank;
	private IItemHandler inputHandler = getSingleInventoryHandler(MultiblockChemicalBath.ITEM_IN, true, true);
	private IItemHandler outputHandler = getSingleInventoryHandler(MultiblockChemicalBath.ITEM_OUT, true, true);

	public TileEntityChemicalBath()
	{
		super(MultiblockChemicalBath.INSTANCE);
		this.inventory = NonNullList.withSize(4, ItemStack.EMPTY);
		this.tank = new FluidTank(ChemicalBath.fluidCapacity);
		this.energyStorage = new FluxStorageAdvanced(ChemicalBath.energyCapacity);
	}

	@Override
	protected void dummyCleanup()
	{
		super.dummyCleanup();
		this.tank = null;
		this.inputHandler = null;
		this.outputHandler = null;
	}

	@Override
	protected void onUpdate()
	{
		super.onUpdate();

		//Handle buckets
		IIUtils.handleBucketTankInteraction(tank, inventory,
				MultiblockChemicalBath.BUCKET_IN, MultiblockChemicalBath.BUCKET_OUT, true,
				BathingRecipe::isValidFluid
		);

		//Output items
		if(!world.isRemote)
			attemptStackOutput(outputHandler, getDirection("item_output"), getPOI(MultiblockPOI.ITEM_OUTPUT));

		//Handle entities inside the bath
		if(world.getTotalWorldTime()%4==0&&tank.getFluid()!=null)
		{
			final ChemthrowerEffect effect = ChemthrowerHandler.getEffect(tank.getFluid().getFluid());
			if(effect!=null)
			{
				AxisAlignedBB aabb = new AxisAlignedBB(getPOIPos("bath_center")).grow(1, 0, 1)
						.contract(0, 1-(tank.getFluidAmount()/(float)tank.getCapacity()), 0);
				world.getEntitiesWithinAABB(EntityLivingBase.class, aabb).forEach(
						entityLivingBase -> effect.applyToEntity(entityLivingBase, null, ItemStack.EMPTY, tank.getFluid()));
			}
		}
	}

	@Override
	protected IIMultiblockProcess<BathingRecipe> findNewProductionProcess()
	{
		if(this.tank.getFluidAmount()==0||this.inventory.get(MultiblockChemicalBath.ITEM_IN).isEmpty())
			return null;

		//Find recipe
		BathingRecipe found = IIMultiblockRecipe.streamRecipes(BathingRecipe.class)
				.filter(r -> r.itemInput.matchesItemStack(this.inventory.get(MultiblockChemicalBath.ITEM_IN)))
				.filter(r -> r.fluidInput.equals(this.tank.drain(r.fluidInput, false)))
				.findFirst()
				.orElse(null);
		if(found==null)
			return null;

		//Drain inputs
		this.tank.drain(found.fluidInput, true);
		this.inputHandler.extractItem(0, found.itemInput.inputSize, false);

		return new IIMultiblockProcess<>(found);
	}

	@Override
	protected IIMultiblockProcess<BathingRecipe> getProcessByName(String name)
	{
		BathingRecipe recipe = IIMultiblockRecipe.getRecipe(BathingRecipe.class, name);
		return recipe==null?null: new IIMultiblockProcess<>(recipe);
	}

	@Override
	public float getProductionStep(IIMultiblockProcess<BathingRecipe> process, boolean simulate)
	{
		if(energyStorage.extractEnergy(process.recipe.getEnergyPerTick(), true) < process.recipe.getEnergyPerTick())
			return 0;
		energyStorage.extractEnergy(process.recipe.getEnergyPerTick(), simulate);
		return 1f;
	}

	@Override
	protected boolean attemptProductionOutput(IIMultiblockProcess<BathingRecipe> process)
	{
		return this.outputHandler.insertItem(0, process.recipe.itemOutput, true).isEmpty();
	}

	@Override
	protected void onProductionFinish(IIMultiblockProcess<BathingRecipe> process)
	{
		this.outputHandler.insertItem(0, process.recipe.itemOutput, false);
	}

	@Override
	protected int[] listAllPOI(MultiblockPOI poi)
	{
		switch(poi)
		{
			case ENERGY_INPUT:
				return getPOI("energy");
			case REDSTONE:
				return getPOI("redstone");
			case ITEM_INPUT:
				return getPOI("item_in");
			case FLUID_INPUT:
				return getPOI("fluid");
			case ITEM_OUTPUT:
				return getPOI("item_out");

			default:
				return new int[0];
		}
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY&&isPOI("item_in"))
			return true;
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY&&isPOI("item_in"))
			//noinspection unchecked,DataFlowIssue
			return (T)master().inputHandler;
		return super.getCapability(capability, facing);
	}

	@Override
	protected IFluidTank[] getFluidTanks(int pos, EnumFacing side)
	{
		return new IFluidTank[]{tank};
	}

	@Nullable
	@Override
	public IIGUI getGUI()
	{
		return IIGUI.CHEMICAL_BATH;
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		switch(slot)
		{
			case MultiblockChemicalBath.ITEM_IN:
				return IIMultiblockRecipe.streamRecipes(BathingRecipe.class)
						.anyMatch(r -> r.itemInput.matchesItemStack(stack));
			case MultiblockChemicalBath.BUCKET_IN:
				return stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
			case MultiblockChemicalBath.ITEM_OUT:
			case MultiblockChemicalBath.BUCKET_OUT:
				return true;
			default:
				return false;
		}
	}

	@Override
	public boolean interact(@Nonnull EnumFacing side, @Nonnull EntityPlayer player, @Nonnull EnumHand hand,
	                        @Nonnull ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		if(!world.isRemote&&this.isPOI("tank_bucket"))
		{
			TileEntityChemicalBath master = master();
			if(master==null)
				return false;

			//Check for bucket interaction
			if(heldItem.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null))
				if(FluidUtil.interactWithFluidHandler(player, hand, master.tank))
				{
					master.updateTileForEvent(SyncEvents.TILE_CUSTOM1);
					return true;
				}
		}

		return false;
	}
}
