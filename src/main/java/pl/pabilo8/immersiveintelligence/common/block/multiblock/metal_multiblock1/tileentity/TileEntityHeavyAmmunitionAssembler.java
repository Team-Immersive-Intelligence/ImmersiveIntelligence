package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.FuseType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.api.crafting.AmmunitionAssemblerRecipe;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.AmmunitionAssembler;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockHeavyAmmunitionAssembler;
import pl.pabilo8.immersiveintelligence.common.entity.tactile.TactileManager;
import pl.pabilo8.immersiveintelligence.common.entity.tactile.TactileManager.ITactileListener;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionSingle;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

import javax.annotation.Nullable;

public class TileEntityHeavyAmmunitionAssembler extends TileEntityMultiblockProductionSingle<TileEntityHeavyAmmunitionAssembler, AmmunitionAssemblerRecipe> implements ITactileListener
{
	public static final int SLOT_CORE = 0, SLOT_CASING = 1, SLOT_OUTPUT = 2;
	@SyncNBT
	public MultiblockInteractablePart drawer1 = new MultiblockInteractablePart(20);
	@SyncNBT
	public MultiblockInteractablePart drawer2 = new MultiblockInteractablePart(22);
	@SyncNBT
	public MultiblockInteractablePart drawer3 = new MultiblockInteractablePart(19);
	@SyncNBT
	public MultiblockInteractablePart drawer4 = new MultiblockInteractablePart(21);
	public String NBT_KEY_EFFECT = "effect";

	@SyncNBT(events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_CLIENT_MESSAGE})
	public FuseType fuse = FuseType.CONTACT;
	@SyncNBT(events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_CLIENT_MESSAGE})
	public int fuseConfig = 0;

	//inventory: core, casing
	IItemHandler coreInputHandler = getSingleInventoryHandler(SLOT_CORE, true, false);
	IItemHandler casingInputHandler = getSingleInventoryHandler(SLOT_CASING, true, false);
	private TactileManager tactileManager;

	public TileEntityHeavyAmmunitionAssembler()
	{
		super(MultiblockHeavyAmmunitionAssembler.INSTANCE);
		energyStorage = new FluxStorageAdvanced(AmmunitionAssembler.energyCapacity);
		inventory = NonNullList.withSize(4, ItemStack.EMPTY);
		tactileManager = new TactileManager(multiblock, this);
	}

	@Override
	protected void dummyCleanup()
	{
		super.dummyCleanup();
		tactileManager = null;
	}

	@Override
	protected void onUpdate()
	{
		//Handle Tactile AMT on server side
		tactileManager.defaultize();
	}

	@Override
	protected int[] listAllPOI(MultiblockPOI poi)
	{
		switch(poi)
		{
			case ITEM_INPUT:
				return getPOI("item_inputs");
			case ITEM_OUTPUT:
				return getPOI("item_out");
			case FLUID_INPUT:
				return getPOI("component_fluid_in");
			case ENERGY_INPUT:
				return getPOI("energy");
			case REDSTONE_INPUT:
				return getPOI("redstone");
		}
		return new int[0];
	}

	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T)(isPOI("input_core")?master().coreInputHandler: master().casingInputHandler);
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean isStackValid(int i, ItemStack stack)
	{
		switch(i)
		{
			case SLOT_CORE:
				return stack.getItem() instanceof IAmmoTypeItem&&((IAmmoTypeItem<?, ?>)stack.getItem()).isBulletCore(stack);
			case SLOT_CASING:
				return AmmunitionAssemblerRecipe.streamRecipes(AmmunitionAssemblerRecipe.class)
						.anyMatch(a -> a.casingInput.matchesItemStackIgnoringSize(stack));
			default:
				return false;
		}
	}

	@Override
	public IIGUI getGUI()
	{
		return null;
	}

	//--- Production ---//

	@Override
	public float getMinProductionOffset()
	{
		return 0.8f;
	}

	@Override
	public int getMaxProductionQueue()
	{
		return 2;
	}

	@Override
	protected IIMultiblockProcess<AmmunitionAssemblerRecipe> findNewProductionProcess()
	{
		if(!inventory.get(SLOT_CORE).isEmpty()&&!inventory.get(SLOT_CASING).isEmpty())
			for(AmmunitionAssemblerRecipe recipe : AmmunitionAssemblerRecipe.getRecipes(AmmunitionAssemblerRecipe.class))
				if(!recipe.advanced&&recipe.casingInput.matchesItemStack(inventory.get(SLOT_CASING))&&recipe.coreInput.matches(inventory.get(SLOT_CORE)))
				{
					IIMultiblockProcess<AmmunitionAssemblerRecipe> process = new IIMultiblockProcess<>(recipe)
							.withNBT(nbt -> nbt.withItemStack(NBT_KEY_EFFECT, recipe.process.apply(inventory.get(SLOT_CORE), inventory.get(SLOT_CASING)))
									.withItemStack("core", inventory.get(SLOT_CORE))
									.withString("ammo", recipe.ammoItem.getName())
							);
					inventory.get(SLOT_CORE).shrink(1);
					inventory.get(SLOT_CASING).shrink(1);
					return process;
				}
		return null;
	}

	@Override
	protected IIMultiblockProcess<AmmunitionAssemblerRecipe> getProcessByName(String name)
	{
		AmmunitionAssemblerRecipe recipe = AmmunitionAssemblerRecipe.getRecipe(AmmunitionAssemblerRecipe.class, name);
		if(recipe!=null&&!recipe.advanced)
			return new IIMultiblockProcess<>(recipe);
		return null;
	}

	@Override
	public float getProductionStep(IIMultiblockProcess<AmmunitionAssemblerRecipe> process, boolean simulate)
	{
		int perTick = process.recipe.getTotalProcessEnergy()/process.maxTicks;
		if(energyStorage.extractEnergy(perTick, simulate) < perTick)
			return 0;
		return 1;
	}

	@Override
	protected boolean attemptProductionOutput(IIMultiblockProcess<AmmunitionAssemblerRecipe> process)
	{
		return true;
	}

	@Override
	protected void onProductionFinish(IIMultiblockProcess<AmmunitionAssemblerRecipe> process)
	{
		outputOrDrop(process.processData.getItemStack(NBT_KEY_EFFECT), null, facing.getOpposite(), 34);
	}

	//--- Data Handling ---//

	@Override
	public void receiveData(DataPacket packet, int pos)
	{
		if(packet.has('f'))
			fuse = FuseType.v(packet.get('f').toString());
	}

	public ItemStack getProductionResult()
	{
		if(currentProcess==null)
			return ItemStack.EMPTY;
		return currentProcess.processData.getItemStack(NBT_KEY_EFFECT);
	}

	@Override
	public void onEntityCollision(World world, Entity entity)
	{
		if(!world.isRemote&&entity instanceof EntityItem)
		{
			ItemStack stack = ((EntityItem)entity).getItem();
			if(stack.isEmpty()) return;

			if(isPOI("input_core"))
				((EntityItem)entity).setItem(master().coreInputHandler.insertItem(0, stack, false));
			else if(isPOI("input_casing"))
				((EntityItem)entity).setItem(master().casingInputHandler.insertItem(0, stack, false));
		}
	}

	@Nullable
	@Override
	public TactileManager getTactileHandler()
	{
		return tactileManager;
	}
}
