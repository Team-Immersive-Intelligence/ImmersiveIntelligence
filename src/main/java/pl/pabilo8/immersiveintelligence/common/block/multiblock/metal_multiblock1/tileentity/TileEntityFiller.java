package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.IConveyorAttachable;
import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.particle.ParticleRedstone;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.api.crafting.DustStack;
import pl.pabilo8.immersiveintelligence.api.crafting.DustUtils;
import pl.pabilo8.immersiveintelligence.api.crafting.FillerRecipe;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Filler;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockFiller;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionMulti;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Pabilo8
 * @since 04.03.2021
 */
public class TileEntityFiller extends TileEntityMultiblockProductionMulti<TileEntityFiller, FillerRecipe> implements IConveyorAttachable
{
	public static int SLOT_DUST = 0, SLOT_INPUT = 1;

	@SyncNBT(time = 40, events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_RECIPE_CHANGED})
	public DustStack dustStorage;
	private IItemHandler insertionHandlerDust, insertionHandlerStack;

	public TileEntityFiller()
	{
		super(MultiblockFiller.INSTANCE);
		this.energyStorage = new FluxStorageAdvanced(Filler.energyCapacity);
		this.inventory = NonNullList.withSize(2, ItemStack.EMPTY);
		this.dustStorage = DustStack.getEmptyStack();
		this.insertionHandlerDust = getSingleInventoryHandler(SLOT_DUST, true, false);
		this.insertionHandlerStack = getSingleInventoryHandler(SLOT_INPUT, true, false);
	}

	@Override
	protected void dummyCleanup()
	{
		super.dummyCleanup();
		dustStorage = null;
		insertionHandlerDust = insertionHandlerStack = null;
	}

	@Override
	protected void onUpdate()
	{
		super.onUpdate();

		//Insert dust into the tank
		if(dustStorage.amount < Filler.dustCapacity&&world.getTotalWorldTime()%4==0&&!inventory.get(SLOT_DUST).isEmpty())
		{
			ItemStack copy = inventory.get(SLOT_DUST).copy();
			copy.setCount(1);
			DustStack dustStack = DustUtils.fromItemStack(copy);
			if(!dustStack.isEmpty()&&dustStorage.mergeWith(dustStack)!=dustStorage)
			{
				inventory.get(SLOT_DUST).shrink(1);
				dustStorage = dustStorage.mergeWith(dustStack);
			}
		}
	}

	@Override
	protected int[] listAllPOI(MultiblockPOI poi)
	{
		switch(poi)
		{
			case ITEM_INPUT:
				return getPOI("inputs");
			case ITEM_OUTPUT:
				return getPOI("conveyor_out");
			case ENERGY_INPUT:
				return getPOI("energy");
			case REDSTONE_INPUT:
				return getPOI("redstone");
		}
		return new int[0];
	}

	private EnumFacing getOutFacing()
	{
		return this.mirrored?this.facing.rotateYCCW(): this.facing.rotateY();
	}

	@Override
	public void onEntityCollision(World world, Entity entity)
	{
		if(!world.isRemote&&entity instanceof EntityItem)
		{
			ItemStack stack = ((EntityItem)entity).getItem();
			if(stack.isEmpty()) return;

			if(isPOI("dust_input"))
				((EntityItem)entity).setItem(master().insertionHandlerDust.insertItem(0, stack, false));
			else if(isPOI("conveyor_in"))
				((EntityItem)entity).setItem(master().insertionHandlerStack.insertItem(0, stack, false));
		}
	}

	@Override
	public EnumFacing[] sigOutputDirections()
	{
		if(isPOI("conveyor_out")) return new EnumFacing[]{getOutFacing()};
		return new EnumFacing[0];
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		//TODO: 24.12.2023 use positions instead
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			TileEntityFiller master = master();
			if(master==null) return false;
			if(isPOI("dust_input")&&facing==EnumFacing.UP) return true;
			return isPOI("conveyor_in")&&facing==getOutFacing().getOpposite();
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(capability!=CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return super.getCapability(capability, facing);

		if(isPOI("dust_input")&&facing==EnumFacing.UP) return (T)master().insertionHandlerDust;
		if(isPOI("conveyor_in")&&facing==getOutFacing().getOpposite()) return (T)master().insertionHandlerStack;
		return null;
	}

	@SideOnly(Side.CLIENT)
	private void spawnDustParticle(IIMultiblockProcess<FillerRecipe> process)
	{
		Vec3d pos = new Vec3d(getBlockPosForPos(10)).addVector(0.5, 0, 0.5);

		float mod = (float)(Math.random()*2f);

		ParticleRedstone particle = (ParticleRedstone)ClientUtils.mc().effectRenderer.spawnEffectParticle(EnumParticleTypes.REDSTONE.getParticleID(), pos.x, pos.y+0.85, pos.z, 0, -4, 0);
		ParticleRedstone particle2 = (ParticleRedstone)ClientUtils.mc().effectRenderer.spawnEffectParticle(EnumParticleTypes.REDSTONE.getParticleID(), pos.x, pos.y+0.65, pos.z, 0, -4, 0);
		ParticleRedstone particle3 = (ParticleRedstone)ClientUtils.mc().effectRenderer.spawnEffectParticle(EnumParticleTypes.REDSTONE.getParticleID(), pos.x, pos.y, pos.z, 0, -4, 0);

		float[] rgb = getCurrentProcessColor(process.recipe);
		final float dmod = 1.3043479f;

		if(particle!=null)
		{
			particle.reddustParticleScale = 2;
			particle.setRBGColorF(rgb[0]*mod, rgb[1]*mod, rgb[2]*mod);
		}
		if(particle2!=null)
		{
			particle2.reddustParticleScale = 2;
			particle2.setRBGColorF(rgb[0]*dmod*mod, rgb[1]*dmod*mod, rgb[2]*dmod*mod);
		}
		if(particle3!=null)
		{
			particle3.reddustParticleScale = 2;
			particle3.setRBGColorF(rgb[0]*mod, rgb[1]*mod, rgb[2]*mod);
		}
	}

	private float[] getCurrentProcessColor(FillerRecipe recipe)
	{
		return DustUtils.getColor(recipe.getDust()).getFloatRGB();
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return slot==SLOT_INPUT?1: super.getSlotLimit(slot);
	}

	@Override
	public boolean isStackValid(int i, ItemStack itemStack)
	{
		if(i==SLOT_INPUT) return FillerRecipe.findRecipe(itemStack, dustStorage)!=null;
		return DustUtils.isDustStack(itemStack);
	}

	@Override
	public NonNullList<ItemStack> getDroppedItems()
	{
		NonNullList<ItemStack> droppedItems = super.getDroppedItems();
		if(!isDummy()&&!dustStorage.isEmpty())
		{
			List<ItemStack> itemStacks = new ArrayList<>(droppedItems);
			itemStacks.addAll(Arrays.asList(DustUtils.fromDustStack(dustStorage)));
			NonNullList<ItemStack> list = NonNullList.withSize(itemStacks.size(), ItemStack.EMPTY);
			for(int i = 0; i < list.size(); i++)
				list.set(0, list.get(i));
			return list;
		}
		return droppedItems;
	}

	@Override
	public float getMinProductionOffset()
	{
		return 0.66f;
	}

	@Override
	public int getMaxProductionQueue()
	{
		return 2;
	}

	@Override
	protected IIMultiblockProcess<FillerRecipe> findNewProductionProcess()
	{
		updateTileForEvent(SyncEvents.TILE_RECIPE_CHANGED);
		FillerRecipe recipe = FillerRecipe.findRecipe(inventory.get(1), dustStorage);
		if(recipe!=null)
		{
			inventory.get(1).shrink(recipe.itemInput.inputSize);
			dustStorage = dustStorage.subtract(recipe.dust);
			return new IIMultiblockProcess<>(recipe);
		}

		return null;
	}

	@Override
	protected IIMultiblockProcess<FillerRecipe> getProcessFromNBT(EasyNBT nbt)
	{
		FillerRecipe recipe = FillerRecipe.findRecipe(nbt.getIngredientStack("item_input"), new DustStack(nbt.getCompound("dust")));
		return recipe==null?null: new IIMultiblockProcess<>(recipe);
	}

	@Override
	public float getProductionStep(IIMultiblockProcess<FillerRecipe> process, boolean simulate)
	{
		int perTick = process.recipe.getTotalProcessEnergy()/process.maxTicks;

		return energyStorage.extractEnergy(perTick, simulate)==perTick?1: 0;
	}

	@Override
	protected boolean attemptProductionOutput(IIMultiblockProcess<FillerRecipe> process)
	{
		return true;
	}

	@Override
	protected void onProductionFinish(IIMultiblockProcess<FillerRecipe> process)
	{
		outputOrDrop(process.recipe.itemOutput.copy(), null, getOutFacing().getOpposite(), getPOI(MultiblockPOI.ITEM_OUTPUT));
	}

	@Override
	public IIGuiList getGUI()
	{
		return IIGuiList.GUI_FILLER;
	}
}
