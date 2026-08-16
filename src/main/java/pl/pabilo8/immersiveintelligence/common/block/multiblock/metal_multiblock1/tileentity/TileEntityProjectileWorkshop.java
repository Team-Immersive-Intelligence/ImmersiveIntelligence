package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.*;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.api.crafting.BulletComponentStack;
import pl.pabilo8.immersiveintelligence.api.crafting.ProjectileWorkshopRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.upgrade.IManagedUpgradableDevice;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeManager;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.MachineStyle;
import pl.pabilo8.immersiveintelligence.api.utils.IBooleanAnimatedPartsBlock;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.ProjectileWorkshop;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockProjectileWorkshop;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBooleanAnimatedPartsSync;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionSingle;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Optional;

/**
 * Machine that handles ammunition core production and filling.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 09.07.2024
 * @ii-approved 0.3.1
 * @since 04.03.2021
 */
public class TileEntityProjectileWorkshop extends TileEntityMultiblockProductionSingle<TileEntityProjectileWorkshop, ProjectileWorkshopRecipe>
		implements IManagedUpgradableDevice<TileEntityProjectileWorkshop>, IBooleanAnimatedPartsBlock
{
	@Nonnull
	public IAmmoTypeItem<?, ?> producedAmmo = IIContent.itemAmmoHeavyArtillery;
	@Nonnull
	@SyncNBT(events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_CLIENT_MESSAGE})
	public CoreType coreType = producedAmmo.getAllowedCoreTypes()[0];
	@SyncNBT(events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_CLIENT_MESSAGE})
	public int componentFillAmount = 1;
	@SyncNBT(events = SyncEvents.TILE_CUSTOM1)
	public BulletComponentStack componentInside = new BulletComponentStack();

	/**
	 * Stores fluids to be converted to ammo components
	 */
	@SyncNBT
	public FluidTank tanksFiller = new FluidTank(ProjectileWorkshop.componentTankCapacity)
	{
		@Override
		public boolean canFillFluidType(FluidStack fluid)
		{
			return fluid!=null
					&&getComponentForFluid(fluid).isPresent()
					&&(componentInside.isEmpty()||componentInside.matches(fluid));
		}
	};
	@SyncNBT
	public MultiblockInteractablePart lid1, lid2;
	@SyncNBT(name = "upgrades", events = SyncEvents.TILE_UPGRADES_MODIFIED)
	public UpgradeManager<TileEntityProjectileWorkshop> upgrades;

	private IItemHandler inputHandler = new IEInventoryHandler(1, this, 0, true, false);
	private IItemHandler componentInputHandler = new IEInventoryHandler(1, this, 1, true, false);

	public TileEntityProjectileWorkshop()
	{
		super(MultiblockProjectileWorkshop.INSTANCE);
		this.energyStorage = new FluxStorageAdvanced(ProjectileWorkshop.energyCapacity);
		this.inventory = NonNullList.withSize(3, ItemStack.EMPTY);
		this.upgrades = new UpgradeManager<>(this);

		this.lid1 = new MultiblockInteractablePart(14);
		this.lid2 = new MultiblockInteractablePart(16);
	}

	@Override
	protected void dummyCleanup()
	{
		super.dummyCleanup();
		lid1 = lid2 = null;
		tanksFiller = null;
		upgrades = null;
		componentInside = null;
		inputHandler = componentInputHandler = null;
	}

	@Override
	protected void onUpdate()
	{
		//Update breadbox lid animations
		lid1.update();
		lid2.update();
		upgrades.update();

		//Fill the internal ammunition component store from both item and fluid input.
		if(isUpgradeInstalled(IIContent.UPGRADE_CORE_FILLER)&&!world.isRemote)
		{
			pullComponentItem();
			pullComponentFluid();
		}

		//Stop working when the machine is disabled
		if(getRedstoneAtPos(0)^redstoneControlInverted)
			return;

		super.onUpdate();
	}

	//--- NBT ---//

	@Override
	public void readCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);

		if(isDummy())
			return;
		IAmmoTypeItem<?, ?> bb = AmmoRegistry.getAmmoItem(nbt.getString("produced_bullet"));
		producedAmmo = bb==null?IIContent.itemAmmoHeavyArtillery: bb;
	}

	@Override
	public void writeCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);

		if(isDummy())
			return;
		nbt.setString("produced_bullet", producedAmmo.getName());
	}

	@Override
	public void receiveMessageFromServer(@Nonnull NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);

		if(isDummy())
			return;
		if(message.hasKey("produced_bullet"))
		{
			IAmmoTypeItem<?, ?> bb = AmmoRegistry.getAmmoItem(message.getString("produced_bullet"));
			producedAmmo = bb==null?IIContent.itemAmmoHeavyArtillery: bb;
		}
		componentFillAmount = Math.max(1, componentFillAmount);
		validateCoreType();
	}

	@Override
	public void receiveMessageFromClient(NBTTagCompound message)
	{
		super.receiveMessageFromClient(message);
		if(message.hasKey("produced_bullet"))
		{
			IAmmoTypeItem<?, ?> bb = AmmoRegistry.getAmmoItem(message.getString("produced_bullet"));
			producedAmmo = bb==null?IIContent.itemAmmoHeavyArtillery: bb;
		}
		componentFillAmount = Math.max(1, componentFillAmount);
		validateCoreType();
	}

	//--- IEInventoryHandler ---//

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		if(slot==MultiblockProjectileWorkshop.SLOT_COMPONENT_INPUT&&isUpgradeInstalled(IIContent.UPGRADE_CORE_FILLER))
			return AmmoRegistry.getAllComponents().stream()
					.filter(this::canUseComponent)
					.anyMatch(comp -> comp.getMaterial().matchesItemStackIgnoringSize(stack));
		else if(slot==MultiblockProjectileWorkshop.SLOT_INPUT)
		{
			if(isUpgradeInstalled(IIContent.UPGRADE_CORE_FILLER))
				return stack.getItem() instanceof IAmmoTypeItem&&((IAmmoTypeItem<?, ?>)stack.getItem()).isBulletCore(stack);
			else
				return AmmoRegistry.getAllCores().stream().anyMatch(core -> core.getMaterial().matchesItemStackIgnoringSize(stack));
		}
		return false;
	}

	//--- Ammo Component Handling ---//

	private int getFluidPerComponentAmount()
	{
		return Math.max(1, ProjectileWorkshop.componentTankCapacity/ProjectileWorkshop.componentCapacity);
	}

	private Optional<AmmoComponent> getComponentForFluid(FluidStack fluid)
	{
		if(fluid==null)
			return Optional.empty();

		return AmmoRegistry.getAllComponents().stream()
				.filter(this::canUseComponent)
				.filter(component -> component.getMaterial().fluid!=null)
				.filter(component -> fluid.isFluidEqual(component.getMaterial().fluid))
				.findFirst();
	}

	/**
	 * Checks if the machine can install the specified component.
	 */
	private boolean canUseComponent(AmmoComponent component)
	{
		return component!=null&&(!component.isSpaciousComponent()||isUpgradeInstalled(IIContent.UPGRADE_INSERTER));
	}

	private void pullComponentItem()
	{
		ItemStack stack = componentInputHandler.getStackInSlot(0);
		if(stack.isEmpty())
			return;

		Optional<AmmoComponent> matching = AmmoRegistry.getAllComponents().stream()
				.filter(this::canUseComponent)
				.filter(comp -> comp.getMaterial().matchesItemStackIgnoringSize(stack))
				.findFirst();

		if(!matching.isPresent())
			return;

		AmmoComponent component = matching.get();
		if(component.getMaterial().fluid!=null)
		{
			FluidActionResult result = FluidUtil.tryEmptyContainer(stack.copy(), tanksFiller, Integer.MAX_VALUE, null, true);
			if(result.isSuccess())
			{
				inventory.set(MultiblockProjectileWorkshop.SLOT_COMPONENT_INPUT, result.getResult());
				updateTileForEvent(SyncEvents.TILE_RECIPE_CHANGED);
			}
			return;
		}

		if(componentInside.isEmpty())
		{
			if(MultiblockProjectileWorkshop.COMPONENT_AMOUNT_PER_ITEM > ProjectileWorkshop.componentCapacity)
				return;

			componentInside = new BulletComponentStack(component, MultiblockProjectileWorkshop.COMPONENT_AMOUNT_PER_ITEM, stack.getTagCompound());
		}
		else if(componentInside.matches(stack)
				&&componentInside.amount+MultiblockProjectileWorkshop.COMPONENT_AMOUNT_PER_ITEM <= ProjectileWorkshop.componentCapacity)
			componentInside.amount += MultiblockProjectileWorkshop.COMPONENT_AMOUNT_PER_ITEM;
		else
			return;

		//The exposed component handler is insert-only. Consume the item from the backing inventory directly.
		stack.shrink(1);
		if(stack.isEmpty())
			inventory.set(MultiblockProjectileWorkshop.SLOT_COMPONENT_INPUT, ItemStack.EMPTY);
		updateTileForEvent(SyncEvents.TILE_RECIPE_CHANGED);
	}

	private void pullComponentFluid()
	{
		FluidStack fluid = tanksFiller.getFluid();
		if(fluid==null)
			return;

		Optional<AmmoComponent> matching = getComponentForFluid(fluid);
		if(!matching.isPresent())
			return;

		if(!componentInside.isEmpty()&&!componentInside.matches(fluid))
			return;

		int fluidPerAmount = getFluidPerComponentAmount();
		int freeAmount = ProjectileWorkshop.componentCapacity-componentInside.amount;
		int availableAmount = fluid.amount/fluidPerAmount;
		int acceptedAmount = Math.min(freeAmount, availableAmount);

		if(acceptedAmount <= 0)
			return;

		if(componentInside.isEmpty())
		{
			NBTTagCompound tag = fluid.tag==null?new NBTTagCompound(): fluid.tag.copy();
			componentInside = new BulletComponentStack(matching.get(), 0, tag);
		}

		componentInside.amount += acceptedAmount;
		tanksFiller.drain(acceptedAmount*fluidPerAmount, true);
	}

	private int getRemainingComponentSlots(IAmmoTypeItem<?, ?> ammo, ItemStack stack)
	{
		return ammo.getCoreType(stack).getComponentSlots()
				-Arrays.stream(ammo.getComponents(stack))
				.mapToInt(AmmoComponent::getSlotsTaken)
				.sum();
	}

	private boolean canApplyComponent(ItemStack stack, IAmmoTypeItem<?, ?> ammo, AmmoComponent component, int fillAmount)
	{
		if(!canUseComponent(component)||!component.matchesBullet(ammo))
			return false;

		int componentSlotsTaken = component.getSlotsTaken()*fillAmount;
		return getRemainingComponentSlots(ammo, stack) >= componentSlotsTaken;
	}

	//--- Ammo Core Handling ---//

	private void validateCoreType()
	{
		if(Arrays.stream(producedAmmo.getAllowedCoreTypes()).noneMatch(type -> type==coreType))
			coreType = producedAmmo.getAllowedCoreTypes()[0];
	}

	private String getCoreProductionRecipeName(IAmmoTypeItem<?, ?> ammo, AmmoCore core, CoreType type)
	{
		return ProjectileWorkshopRecipe.CORE_PRODUCTION_RECIPE_PREFIX+ammo.getName()+"|"+core.getName()+"|"+type.getName();
	}

	private ProjectileWorkshopRecipe getOrCreateCoreProductionRecipe(IAmmoTypeItem<?, ?> ammo, AmmoCore core, CoreType type)
	{
		String name = getCoreProductionRecipeName(ammo, core, type);
		ProjectileWorkshopRecipe recipe = IIMultiblockRecipe.streamRecipes(ProjectileWorkshopRecipe.class)
				.filter(existing -> name.equals(existing.getName()))
				.findFirst()
				.orElse(null);
		if(recipe==null)
		{
			recipe = new ProjectileWorkshopRecipe(ammo, core, type);
			recipe.setName(name);
		}
		return recipe;
	}

	@Nullable
	private ProjectileWorkshopRecipe restoreCoreProductionRecipe(String name)
	{
		if(!name.startsWith(ProjectileWorkshopRecipe.CORE_PRODUCTION_RECIPE_PREFIX))
			return null;

		String[] parts = name.substring(ProjectileWorkshopRecipe.CORE_PRODUCTION_RECIPE_PREFIX.length()).split("\\|", -1);
		if(parts.length!=3)
			return null;

		IAmmoTypeItem<?, ?> ammo = AmmoRegistry.getAmmoItem(parts[0]);
		AmmoCore core = AmmoRegistry.getCore(parts[1]);
		CoreType type = CoreType.v(parts[2]);
		if(ammo==null||core==null||Arrays.stream(ammo.getAllowedCoreTypes()).noneMatch(allowed -> allowed==type))
			return null;

		return getOrCreateCoreProductionRecipe(ammo, core, type);
	}

	//--- TileEntityMultiblockProductionSingle ---//

	@Override
	protected IIMultiblockProcess<ProjectileWorkshopRecipe> findNewProductionProcess()
	{
		if(isUpgradeInstalled(IIContent.UPGRADE_CORE_FILLER))
			return findNewFillingProcess();
		return findNewCoreProductionProcess();
	}

	private IIMultiblockProcess<ProjectileWorkshopRecipe> findNewCoreProductionProcess()
	{
		ItemStack stack = inventory.get(MultiblockProjectileWorkshop.SLOT_INPUT);
		if(stack.isEmpty())
			return null;

		Optional<AmmoCore> first = AmmoRegistry.getAllCores()
				.stream()
				.filter(core -> core.getMaterial().matchesItemStackIgnoringSize(stack))
				.findFirst();

		if(!first.isPresent()||stack.getCount() < producedAmmo.getCoreMaterialNeeded())
			return null;

		validateCoreType();
		ProjectileWorkshopRecipe recipe = getOrCreateCoreProductionRecipe(producedAmmo, first.get(), coreType);
		stack.shrink(producedAmmo.getCoreMaterialNeeded());
		return new IIMultiblockProcess<>(recipe);
	}

	private IIMultiblockProcess<ProjectileWorkshopRecipe> findNewFillingProcess()
	{
		ItemStack stack = inventory.get(MultiblockProjectileWorkshop.SLOT_INPUT);
		if(stack.isEmpty()||!(stack.getItem() instanceof IAmmoTypeItem))
			return null;

		IAmmoTypeItem<?, ?> ammo = (IAmmoTypeItem<?, ?>)stack.getItem();
		if(!ammo.isBulletCore(stack))
			return null;

		if(componentInside.isEmpty()||componentInside.component==null)
			return null;

		int fillAmount = Math.max(1, componentFillAmount);
		int componentAmount = fillAmount*ammo.getComponentUsed();
		if(componentInside.amount < componentAmount)
			return null;

		ItemStack effect = stack.copy();
		effect.setCount(1);

		AmmoComponent component = componentInside.component;
		if(!canApplyComponent(effect, ammo, component, fillAmount))
			return null;

		BulletComponentStack usedComponent = new BulletComponentStack(component, componentAmount, componentInside.tagCompound.copy());
		for(int i = 0; i < fillAmount; i++)
			ammo.addComponents(effect, component, componentInside.tagCompound.copy());

		componentInside.subtract(componentAmount);
		stack.shrink(1);

		IIMultiblockProcess<ProjectileWorkshopRecipe> process = new IIMultiblockProcess<>(ProjectileWorkshopRecipe.CORE_FILLING)
				.withNBT(nbt -> nbt
						.withItemStack("effect", effect)
						.withBoolean("filled", true)
						.withSerializable("component", usedComponent)
				);
		process.maxTicks *= ammo.getCaliber();
		return process;
	}

	@Override
	protected IIMultiblockProcess<ProjectileWorkshopRecipe> getProcessByName(String name)
	{
		if(ProjectileWorkshopRecipe.FILLING_RECIPE_NAME.equals(name))
			return new IIMultiblockProcess<>(ProjectileWorkshopRecipe.CORE_FILLING);

		ProjectileWorkshopRecipe recipe = IIMultiblockRecipe.streamRecipes(ProjectileWorkshopRecipe.class)
				.filter(existing -> name.equals(existing.getName()))
				.findFirst()
				.orElse(null);
		if(recipe==null)
			recipe = restoreCoreProductionRecipe(name);
		return recipe==null?null: new IIMultiblockProcess<>(recipe);
	}

	@Override
	public float getProductionStep(IIMultiblockProcess<ProjectileWorkshopRecipe> process, boolean simulate)
	{
		return (energyStorage.extractEnergy(process.recipe.getEnergyPerTick(), simulate)==process.recipe.getEnergyPerTick())?1: 0;
	}

	@Override
	protected boolean attemptProductionOutput(IIMultiblockProcess<ProjectileWorkshopRecipe> process)
	{
		ItemStack effect = process.recipe.isFilling?process.processData.getItemStack("effect"): process.recipe.getEffect();
		if(effect.isEmpty())
			return true;

		outputOrDrop(effect, null, mirrored?facing.rotateYCCW(): facing.rotateY(),
				getPOI(MultiblockPOI.ITEM_OUTPUT)
		);
		return true;
	}

	@Override
	protected void onProductionFinish(IIMultiblockProcess<ProjectileWorkshopRecipe> process)
	{

	}

	@Override
	public IIGUI getGUI()
	{
		return IIGUI.PROJECTILE_WORKSHOP;
	}

	//--- IBooleanAnimatedPartsBlock ---//

	@Override
	public void onAnimationChangeClient(boolean state, int part)
	{
		MultiblockInteractablePart.setStates(state, part, lid1, lid2);
	}

	@Override
	public void onAnimationChangeServer(boolean state, int part)
	{
		MultiblockInteractablePart changed = MultiblockInteractablePart.setStates(state, part, lid1, lid2);
		if(changed!=null)
		{
			world.playSound(null, getPos(), state?IISounds.metalBreadboxOpen: IISounds.metalBreadboxClose, SoundCategory.BLOCKS, 0.5F, 1f);
			IIPacketHandler.sendToClient(this, new MessageBooleanAnimatedPartsSync(changed, this));
		}
	}

	//--- Capabilities ---//
	@Override
	protected IFluidTank[] getFluidTanks(int pos, EnumFacing side)
	{
		if(isUpgradeInstalled(IIContent.UPGRADE_CORE_FILLER)
				&&Arrays.stream(getPOI("component_fluid_in")).anyMatch(i -> i==pos))
			return new FluidTank[]{tanksFiller};

		return super.getFluidTanks(pos, side);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			TileEntityProjectileWorkshop master = master();
			assert master!=null;
			if(isPOI("item_in"))
				return (T)master.inputHandler;
			else if(isPOI("component_in"))
				return (T)master.componentInputHandler;
		}
		return super.getCapability(capability, facing);
	}

	//--- Data Handling ---//

	@Override
	public void receiveData(DataPacket packet, int pos)
	{
		if(packet.has('b'))
		{
			IAmmoTypeItem<?, ?> ammoItem = AmmoRegistry.getAmmoItem(packet.get('b').toString());
			this.producedAmmo = ammoItem==null?IIContent.itemAmmoHeavyArtillery: ammoItem;
		}
		if(packet.has('t'))
			this.coreType = CoreType.v(packet.get('t').toString());

		validateCoreType();

		if(packet.has('a'))
			this.componentFillAmount = Math.max(1, packet.getVarInType(DataTypeInteger.class, packet.get('a')).value);
	}

	//--- Collision / Conveyor Input ---//

	@Override
	public void onEntityCollision(World world, Entity entity)
	{
		if(isPOI("item_in")&&!world.isRemote&&entity instanceof EntityItem ei&&!entity.isDead)
		{
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

	@Nonnull
	@Override
	public UpgradeManager<TileEntityProjectileWorkshop> getUpgradeManager()
	{
		return upgrades;
	}

	@Override
	public MachineStyle getUpgradableMachineStyle()
	{
		return MachineStyle.STEEL;
	}
}
