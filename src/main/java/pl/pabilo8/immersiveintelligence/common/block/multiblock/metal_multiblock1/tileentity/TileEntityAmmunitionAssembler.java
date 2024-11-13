package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.FuseType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.api.crafting.AmmunitionAssemblerRecipe;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.utils.IBooleanAnimatedPartsBlock;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.AmmunitionAssembler;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockAmmunitionAssembler;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBooleanAnimatedPartsSync;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionMulti;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @updated 09.07.2024
 * @ii-approved 0.3.1
 * @since 04.03.2021
 */
public class TileEntityAmmunitionAssembler extends TileEntityMultiblockProductionMulti<TileEntityAmmunitionAssembler, AmmunitionAssemblerRecipe> implements IBooleanAnimatedPartsBlock
{
	public String NBT_KEY_EFFECT = "effect";

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
	protected void onUpdate()
	{
		super.onUpdate();
		hatch.update();
	}

	@Override
	protected int[] listAllPOI(MultiblockPOI poi)
	{
		switch(poi)
		{
			case ITEM_INPUT:
				return getPOI("item_input");
			case ITEM_OUTPUT:
				return getPOI("item_output");
			case REDSTONE_INPUT:
				return getPOI("redstone_input");
			case DATA_INPUT:
				return getPOI("data_input");
			case ENERGY_INPUT:
				return getPOI("energy_input");
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
				return AmmunitionAssemblerRecipe.RECIPES.stream().anyMatch(a -> a.casingInput.matchesItemStackIgnoringSize(stack));
			default:
				return false;
		}
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
			for(AmmunitionAssemblerRecipe recipe : AmmunitionAssemblerRecipe.RECIPES)
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
	protected IIMultiblockProcess<AmmunitionAssemblerRecipe> getProcessFromNBT(EasyNBT nbt)
	{
		AmmunitionAssemblerRecipe recipe = AmmunitionAssemblerRecipe.RECIPES.stream()
				.filter(r -> !r.advanced)
				.filter(r -> r.ammoItem.getName().equals(nbt.getString("ammo")))
				.findFirst().orElse(null);
		if(recipe!=null)
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
		if(packet.hasVariable('f'))
			fuse = FuseType.v(packet.getPacketVariable('f').toString());
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

	public ItemStack getProductionResult(int processID)
	{
		if(processQueue.size() <= processID)
			return ItemStack.EMPTY;
		return this.processQueue.get(processID).processData.getItemStack(NBT_KEY_EFFECT);

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

	@Override
	public void onAnimationChangeClient(boolean state, int part)
	{
		hatch.setState(state);
	}

	@Override
	public void onAnimationChangeServer(boolean state, int part)
	{
		if(hatch.setState(state))
		{
			world.playSound(null, getPOIPos("lid"), state?IISounds.metalSlideOpen: IISounds.metalSlideClose, SoundCategory.BLOCKS, 1f, 1f);
			IIPacketHandler.sendToClient(getPos(), getWorld(), new MessageBooleanAnimatedPartsSync(state, part, getPos()));
		}
	}
}
