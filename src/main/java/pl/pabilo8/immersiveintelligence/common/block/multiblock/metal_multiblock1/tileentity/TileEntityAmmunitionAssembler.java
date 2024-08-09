package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.FuseType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.api.crafting.AmmunitionAssemblerRecipe;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.utils.IBooleanAnimatedPartsBlock;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.AmmunitionAssembler;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockAmmunitionAssembler;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBooleanAnimatedPartsSync;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionSingle;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @updated 09.07.2024
 * @ii-approved 0.3.1
 * @since 04.03.2021
 */
public class TileEntityAmmunitionAssembler extends TileEntityMultiblockProductionSingle<TileEntityAmmunitionAssembler, AmmunitionAssemblerRecipe> implements IBooleanAnimatedPartsBlock
{
	public static final int SLOT_CORE = 0, SLOT_CASING = 1, SLOT_OUTPUT = 2;
	public FuseType fuse = FuseType.CONTACT;
	@SyncNBT
	public int fuseConfig = 0; //depends on fuse type: time for timed fuse, distance for proximity fuse

	//inventory: core, casing
	IItemHandler coreInputHandler = getSingleInventoryHandler(SLOT_CORE, true, false);
	IItemHandler casingInputHandler = getSingleInventoryHandler(SLOT_CASING, true, false);

	@SyncNBT
	public MultiblockInteractablePart hatch;

	public TileEntityAmmunitionAssembler()
	{
		super(MultiblockAmmunitionAssembler.INSTANCE);
		energyStorage = new FluxStorageAdvanced(AmmunitionAssembler.energyCapacity);
		inventory = NonNullList.withSize(4, ItemStack.EMPTY);
		hatch = new MultiblockInteractablePart(10);
	}

	@Override
	protected int[] listAllPOI(MultiblockPOI poi)
	{
		switch(poi)
		{
			case ITEM_INPUT:
				return new int[]{18, 20};
			case ITEM_OUTPUT:
				return new int[]{34};
			case REDSTONE_INPUT:
				return new int[]{15};
			case DATA_INPUT:
				return new int[]{8};
			case ENERGY_INPUT:
				return new int[]{50};
		}
		return new int[0];
	}

	@Override
	public boolean isStackValid(int i, ItemStack stack)
	{
		if(i==0)
			return stack.getItem() instanceof IAmmoTypeItem&&((IAmmoTypeItem<?, ?>)stack.getItem()).isBulletCore(stack);
		else if(i==1)
			return AmmunitionAssemblerRecipe.RECIPES.stream().anyMatch(a -> a.casingInput.matchesItemStackIgnoringSize(stack));
		return false;
	}

	@Override
	public IIGuiList getGUI()
	{
		return IIGuiList.GUI_AMMUNITION_ASSEMBLER;
	}

	//--- Production ---//

	@Override
	public float getMinProductionOffset()
	{
		return 0;
	}

	@Override
	public int getMaxProductionQueue()
	{
		return 0;
	}

	@Override
	protected IIMultiblockProcess<AmmunitionAssemblerRecipe> findNewProductionProcess()
	{
		if(!inventory.get(SLOT_CORE).isEmpty()&&!inventory.get(SLOT_CASING).isEmpty())
			for(AmmunitionAssemblerRecipe recipe : AmmunitionAssemblerRecipe.RECIPES)
				if(recipe.casingInput.matchesItemStack(inventory.get(SLOT_CASING))&&recipe.coreInput.matches(inventory.get(SLOT_CORE)))
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
		inventory.get(SLOT_CORE).shrink(1);
		inventory.get(SLOT_CASING).shrink(1);
		outputOrDrop(process.recipe.process.apply(inventory.get(SLOT_CORE), inventory.get(SLOT_CASING)), null, facing.getOpposite(), 34);
	}

	//--- Data Handling ---//

	@Override
	public void receiveData(DataPacket packet, int pos)
	{
		if(packet.hasVariable('f'))
			fuse = FuseType.v(packet.getPacketVariable('f').valueToString());
	}

	@Override
	public void receiveMessageFromServer(@Nonnull NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);

		if(isDummy())
			return;

		if(message.hasKey("fuse"))
			this.fuse = FuseType.v(message.getString("fuse"));
		if(message.hasKey("fuse_config"))
			this.fuseConfig = message.getInteger("fuse_config");
	}

	@Override
	public void receiveMessageFromClient(NBTTagCompound message)
	{
		super.receiveMessageFromClient(message);

		if(isDummy())
			return;

		if(message.hasKey("fuse"))
			this.fuse = FuseType.v(message.getString("fuse"));
		if(message.hasKey("fuse_config"))
			this.fuseConfig = message.getInteger("fuse_config");
	}

	public ItemStack getProductionResult()
	{
		if(this.currentProcess==null)
			return ItemStack.EMPTY;
		return this.currentProcess.recipe.process.apply(
				this.inventory.get(TileEntityAmmunitionAssembler.SLOT_CORE),
				this.inventory.get(TileEntityAmmunitionAssembler.SLOT_CASING)
		);

	}

	@Override
	public void onAnimationChangeClient(boolean state, int part)
	{
		hatch.setState(state);
	}

	@Override
	public void onAnimationChangeServer(boolean state, int part)
	{
		hatch.setState(state);
		IIPacketHandler.sendToClient(getPos(), getWorld(), new MessageBooleanAnimatedPartsSync(state, part, getPos()));
	}
}
