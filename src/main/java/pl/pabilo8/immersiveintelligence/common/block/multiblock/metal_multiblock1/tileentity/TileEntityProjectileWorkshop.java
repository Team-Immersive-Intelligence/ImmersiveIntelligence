package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.api.crafting.BulletComponentStack;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.utils.IBooleanAnimatedPartsBlock;
import pl.pabilo8.immersiveintelligence.api.utils.IUpgradableMachine;
import pl.pabilo8.immersiveintelligence.api.utils.MachineUpgrade;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.ProjectileWorkshop;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockProjectileWorkshop;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityProjectileWorkshop.ProjectileWorkshopRecipe;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBooleanAnimatedPartsSync;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.IIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionSingle;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;
import pl.pabilo8.immersiveintelligence.common.util.upgrade_system.IUpgradeStorageMachine;
import pl.pabilo8.immersiveintelligence.common.util.upgrade_system.UpgradeStorage;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author Pabilo8
 * @updated 09.07.2024
 * @ii-approved 0.3.1
 * @since 04.03.2021
 */
public class TileEntityProjectileWorkshop extends TileEntityMultiblockProductionSingle<TileEntityProjectileWorkshop, ProjectileWorkshopRecipe>
		implements IUpgradeStorageMachine<TileEntityProjectileWorkshop>, IBooleanAnimatedPartsBlock
{
	public static final int SLOT_INPUT = 0, SLOT_COMPONENT_INPUT = 1, SLOT_OUTPUT = 2;

	//for core production
	@Nonnull
	public IAmmoTypeItem<?, ?> producedAmmo = IIContent.itemAmmoHeavyArtillery;
	@Nonnull
	public CoreType coreType = producedAmmo.getAllowedCoreTypes()[0];
	//how many slots to fill
	@SyncNBT
	public int fillAmount = 1;
	public BulletComponentStack componentInside = new BulletComponentStack();

	/**
	 * Stores fluids to be converted to ammo components
	 */
	@SyncNBT
	public FluidTank tanksFiller = new FluidTank(ProjectileWorkshop.componentTankCapacity);
	IItemHandler inputHandler = new IEInventoryHandler(1, this, 0, true, false); //pos 15
	IItemHandler componentInputHandler = new IEInventoryHandler(1, this, 1, true, false); //pos 22

	public MultiblockInteractablePart lid1 = new MultiblockInteractablePart(14), lid2 = new MultiblockInteractablePart(16);
	public UpgradeStorage<TileEntityProjectileWorkshop> upgradeStorage;

	public TileEntityProjectileWorkshop()
	{
		super(MultiblockProjectileWorkshop.INSTANCE);
		energyStorage = new FluxStorageAdvanced(ProjectileWorkshop.energyCapacity);
		inventory = NonNullList.withSize(3, ItemStack.EMPTY); //input, componentInput
		upgradeStorage = new UpgradeStorage<>(this);
	}

	@Override
	protected void dummyCleanup()
	{
		super.dummyCleanup();
		lid1 = lid2 = null;
		tanksFiller = null;
		upgradeStorage = null;
		componentInside = null;
	}

	@Override
	protected void onUpdate()
	{
		//Update breadbox lid animations
		lid1.update();
		lid2.update();

		//fill component tank
		if(hasUpgrade(IIContent.UPGRADE_CORE_FILLER)&&!world.isRemote)
		{
			if(!componentInputHandler.getStackInSlot(0).isEmpty())
			{
				ItemStack stack = componentInputHandler.getStackInSlot(0);
				if(componentInside.isEmpty())
				{
					Optional<AmmoComponent> matching = AmmoRegistry.getAllComponents().stream().filter(comp -> comp.getMaterial().matchesItemStackIgnoringSize(stack)).findFirst();
					matching.ifPresent(component -> componentInside = new BulletComponentStack(component, componentInputHandler.extractItem(0, 1, false).getTagCompound()));
				}
				else
				{
					if(componentInside.matches(stack)&&componentInside.amount+16 <= ProjectileWorkshop.componentCapacity)
					{
						componentInside.amount += 16;
						componentInputHandler.extractItem(0, 1, false);
					}
				}
			}
		}

		//Stop working when the machine is disabled
		if(getRedstoneAtPos(0)^redstoneControlInverted)
			return;

		super.onUpdate();
	}

	@Override
	public void readCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);

		if(isDummy())
			return;

		componentInside.deserializeNBT(nbt.getCompoundTag("component_inside"));
		coreType = CoreType.v(nbt.getString("core_type"));
		upgradeStorage.getUpgradesFromNBT(nbt.getCompoundTag("upgrades"));
	}

	@Override
	public void writeCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);

		if(isDummy())
			return;

		nbt.setTag("component_inside", componentInside.serializeNBT());
		nbt.setString("produced_bullet", producedAmmo.getName());
		nbt.setString("core_type", coreType.getName());
		nbt.setTag("upgrades", upgradeStorage.saveUpgradesToNBT());

	}

	@Override
	public void receiveMessageFromServer(@Nonnull NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);

		if(isDummy())
			return;

		if(message.hasKey("component_inside"))
			componentInside.deserializeNBT(message.getCompoundTag("component_inside"));
		if(message.hasKey("produced_bullet"))
		{
			IAmmoTypeItem bb = AmmoRegistry.getAmmoItem(message.getString("produced_bullet"));
			producedAmmo = bb==null?IIContent.itemAmmoHeavyArtillery: bb;
		}
		if(message.hasKey("core_type"))
			coreType = CoreType.v(message.getString("core_type"));
	}

	@Override
	public void receiveMessageFromClient(NBTTagCompound message)
	{
		super.receiveMessageFromClient(message);
		if(message.hasKey("core_type"))
			coreType = CoreType.v(message.getString("core_type"));
		if(message.hasKey("produced_bullet"))
		{
			IAmmoTypeItem bb = AmmoRegistry.getAmmoItem(message.getString("produced_bullet"));
			producedAmmo = bb==null?IIContent.itemAmmoHeavyArtillery: bb;
		}
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
	public boolean isStackValid(int slot, ItemStack stack)
	{
		if(slot==SLOT_COMPONENT_INPUT&&hasUpgrade(IIContent.UPGRADE_CORE_FILLER))
			return AmmoRegistry.getAllComponents().stream().anyMatch(comp -> comp.getMaterial().matchesItemStackIgnoringSize(stack));
		else if(slot==SLOT_INPUT)
		{
			if(hasUpgrade(IIContent.UPGRADE_CORE_FILLER))
				return stack.getItem() instanceof IAmmoTypeItem&&((IAmmoTypeItem<?, ?>)stack.getItem()).isBulletCore(stack);
			else
				return AmmoRegistry.getAllCores().stream().anyMatch(core -> core.getMaterial().matchesItemStackIgnoringSize(stack));
		}
		return false;
	}

	@Override
	public int getSlotLimit(int i)
	{
		return i==1?1: 64;
	}

	@Override
	protected IIMultiblockProcess<ProjectileWorkshopRecipe> findNewProductionProcess()
	{
		//filling
		if(hasUpgrade(IIContent.UPGRADE_CORE_FILLER))
		{
			//check for valid component and ammo core
			if(componentInside.isEmpty()||inventory.get(SLOT_INPUT).isEmpty())
				return null;

			ItemStack stack = inventory.get(SLOT_INPUT);
			IAmmoTypeItem<?, ?> ammo = (IAmmoTypeItem<?, ?>)stack.getItem();
			int componentSlotsTaken = componentInside.component.getSlotsTaken()*fillAmount;
			int remainingSlots = ammo.getCoreType(stack).getComponentSlots()-
					Arrays.stream(ammo.getComponents(stack))
							.map(AmmoComponent::getSlotsTaken)
							.reduce(0, Integer::sum);

			//check if there is enough space in the ammo
			if(!componentInside.component.matchesBullet(ammo)||remainingSlots-componentSlotsTaken < 0)
				return null;

			IIMultiblockProcess<ProjectileWorkshopRecipe> out = new IIMultiblockProcess<>(new ProjectileWorkshopRecipe(stack, componentInside));
			stack.shrink(1);
			componentInside.subtract(ammo.getCoreMaterialNeeded());
			return out;

		}
		else //production
		{
			ItemStack stack = inventory.get(SLOT_INPUT);
			if(stack.isEmpty())
				return null;

			Optional<AmmoCore> first = AmmoRegistry.getAllCores()
					.stream()
					.filter(core -> core.getMaterial().matchesItemStackIgnoringSize(inventory.get(0)))
					.findFirst();

			if(!first.isPresent()||stack.getCount() < producedAmmo.getCoreMaterialNeeded())
				return null;

			IIMultiblockProcess<ProjectileWorkshopRecipe> out = new IIMultiblockProcess<>(new ProjectileWorkshopRecipe(producedAmmo, first.get(), coreType));
			stack.shrink(producedAmmo.getCoreMaterialNeeded());
			return out;
		}
	}

	@Override
	protected IIMultiblockProcess<ProjectileWorkshopRecipe> getProcessFromNBT(EasyNBT nbt)
	{
		return null;
	}

	@Override
	public float getProductionStep(IIMultiblockProcess<ProjectileWorkshopRecipe> process, boolean simulate)
	{
		return (energyStorage.extractEnergy(process.recipe.getEnergyPerTick(), simulate)==process.recipe.getEnergyPerTick())?1: 0;
	}

	@Override
	protected boolean attemptProductionOutput(IIMultiblockProcess<ProjectileWorkshopRecipe> process)
	{
		outputOrDrop(process.recipe.getEffect(), null, mirrored?facing.rotateYCCW(): facing.rotateY(),
				getPOI(MultiblockPOI.ITEM_OUTPUT)
		);
		return true;
	}

	@Override
	protected void onProductionFinish(IIMultiblockProcess<ProjectileWorkshopRecipe> process)
	{

	}

	@Override
	public IIGuiList getGUI()
	{
		return IIGuiList.GUI_PROJECTILE_WORKSHOP;
	}

	@Override
	public void onAnimationChangeClient(boolean state, int part)
	{
		if(part==0)
			lid1.setState(state);
		else
			lid2.setState(state);
	}

	@Override
	public void onAnimationChangeServer(boolean state, int part)
	{
		if(part==0)
			lid1.setState(state);
		else
			lid2.setState(state);

		if((part==0?lid1: lid2).setState(state))
			world.playSound(null, getPos(), state?IISounds.metalBreadboxOpen: IISounds.metalBreadboxClose, SoundCategory.BLOCKS, 0.5F, 1f);

		IIPacketHandler.INSTANCE.sendToAllAround(new MessageBooleanAnimatedPartsSync(state, part, getPos()),
				IIPacketHandler.targetPointFromPos(this.getPos(), this.world, 32));
	}

	@Override
	public void receiveData(DataPacket packet, int pos)
	{
		if(packet.hasVariable('b'))
		{
			IAmmoTypeItem<?, ?> ammoItem = AmmoRegistry.getAmmoItem(packet.getPacketVariable('b').toString());
			this.producedAmmo = ammoItem==null?IIContent.itemAmmoHeavyArtillery: ammoItem;
		}
		if(packet.hasVariable('t'))
			this.coreType = CoreType.v(packet.getPacketVariable('t').toString());

		if(Arrays.stream(producedAmmo.getAllowedCoreTypes()).noneMatch(ct -> ct==coreType))
			this.coreType = this.producedAmmo.getAllowedCoreTypes()[0];

		if(packet.hasVariable('a'))
			this.fillAmount = packet.getVarInType(DataTypeInteger.class, packet.getPacketVariable('a')).value;
	}

	//TODO: 09.07.2024 conveyor interaction
	@Override
	public void onEntityCollision(World world, Entity entity)
	{
		if(pos==15&&!world.isRemote&&entity instanceof EntityItem&&!entity.isDead)
		{
			EntityItem ei = (EntityItem)entity;
			if(ei.getItem().isEmpty())
				return;

			TileEntityProjectileWorkshop master = master();
			if(master==null)
				return;
			ItemStack stack = ei.getItem();
			if(stack.isEmpty())
				return;

			stack = master.inputHandler.insertItem(0, stack, false);

			ei.setItem(stack);
			if(stack.getCount() <= 0)
				entity.setDead();
		}
	}

	//--- IUpgradeStorageMachine ---//

	@Override
	public UpgradeStorage<TileEntityProjectileWorkshop> getUpgradeStorage()
	{
		return upgradeStorage;
	}

	@Override
	public boolean upgradeMatches(MachineUpgrade upgrade)
	{
		return upgrade==IIContent.UPGRADE_CORE_FILLER;
	}

	@Override
	public <T extends TileEntity & IUpgradableMachine> T getUpgradeMaster()
	{
		return (T)master();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderWithUpgrades(MachineUpgrade... upgrades)
	{

	}

	public static class ProjectileWorkshopRecipe extends IIMultiblockRecipe
	{
		private int energyPerTick;

		public IAmmoTypeItem<?, ?> ammo;
		public boolean isFilling;
		public ItemStack effect, ingredient;

		/**
		 * Production recipe
		 *
		 * @param ammo     Ammo to be produced
		 * @param coreType Core type
		 */
		public ProjectileWorkshopRecipe(IAmmoTypeItem<?, ?> ammo, AmmoCore core, CoreType coreType)
		{
			this.ammo = ammo;
			this.isFilling = false;

			this.totalProcessTime = ProjectileWorkshop.productionTime*ammo.getCaliber();
			this.energyPerTick = ProjectileWorkshop.productionEnergyUsage*ammo.getCaliber();

			this.ingredient = core.getMaterial().getExampleStack();
			this.effect = ammo.getAmmoCoreStack(core, coreType);
		}

		/**
		 * Filling recipe
		 *
		 * @param inputStack Ammo to be filled
		 */
		public ProjectileWorkshopRecipe(ItemStack inputStack, BulletComponentStack component)
		{
			assert inputStack.getItem() instanceof IAmmoTypeItem;

			this.ammo = (IAmmoTypeItem<?, ?>)inputStack.getItem();
			this.isFilling = true;

			this.totalProcessTime = ProjectileWorkshop.fillingTime;
			this.energyPerTick = ProjectileWorkshop.fillingEnergyUsage;

			this.effect = inputStack.copy();
			this.effect.setCount(1);
			ammo.addComponents(this.effect, component.getComponent(), component.tagCompound);
		}

		@Override
		public int getTotalProcessTime()
		{
			return totalProcessTime;
		}

		@Override
		public int getTotalProcessEnergy()
		{
			return energyPerTick*totalProcessTime;
		}

		public int getEnergyPerTick()
		{
			return energyPerTick;
		}

		public ItemStack getEffect()
		{
			return effect;
		}

		@Override
		public int getMultipleProcessTicks()
		{
			return 0;
		}

		@Override
		public EasyNBT writeToNBT()
		{
			return EasyNBT.newNBT()
					.withBoolean("filling", isFilling)
					.withItemStack("effect", effect);
		}
	}
}
