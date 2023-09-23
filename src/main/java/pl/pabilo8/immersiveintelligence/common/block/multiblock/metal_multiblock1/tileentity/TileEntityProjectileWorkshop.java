package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity;

import blusunrize.immersiveengineering.api.crafting.IMultiblockRecipe;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.IConveyorAttachable;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedCollisionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedSelectionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.ISoundTile;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.ProjectileWorkshop;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmo;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmoComponent;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmoCore;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.utils.IBooleanAnimatedPartsBlock;
import pl.pabilo8.immersiveintelligence.api.utils.MachineUpgrade;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IUpgradableMachine;
import pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.ProjectileWorkshopRenderer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockProjectileWorkshop;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBooleanAnimatedPartsSync;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * @author Pabilo8
 * @since 04.03.2021
 */
public class TileEntityProjectileWorkshop extends TileEntityMultiblockMetal<TileEntityProjectileWorkshop, IMultiblockRecipe> implements ISoundTile, IGuiTile, IAdvancedSelectionBounds, IAdvancedCollisionBounds, IUpgradableMachine, IBooleanAnimatedPartsBlock, IConveyorAttachable, IDataDevice
{
	MachineUpgrade currentlyInstalled = null;
	int upgradeProgress = 0;

	//for core production
	@Nonnull
	public IAmmo producedBullet = IIContent.itemAmmoArtillery;
	@Nonnull
	public EnumCoreTypes coreType = producedBullet.getAllowedCoreTypes()[0];

	//for core filling
	public boolean fillerUpgrade = false;
	//how many slots to fill
	public int fillAmount = 1;
	public BulletComponentStack componentInside = new BulletComponentStack();
	public int productionProgress = 0;

	NonNullList<ItemStack> inventory = NonNullList.withSize(2, ItemStack.EMPTY); //input, componentInput
	public FluidTank[] tanksFactory = new FluidTank[]{new FluidTank(ProjectileWorkshop.coolantTankCapacity)};
	public FluidTank[] tanksFiller = new FluidTank[]{new FluidTank(ProjectileWorkshop.componentTankCapacity)};
	IItemHandler inputHandler = new IEInventoryHandler(1, this, 0, true, false); //pos 15
	IItemHandler componentInputHandler = new IEInventoryHandler(1, this, 1, true, false); //pos 22

	//for client
	public boolean[] isDrawerOpened = {false, false};
	public float[] drawerAngle = {0f, 0f};
	public ItemStack effect = ItemStack.EMPTY;

	public TileEntityProjectileWorkshop()
	{
		super(MultiblockProjectileWorkshop.INSTANCE, MultiblockProjectileWorkshop.INSTANCE.getSize(), ProjectileWorkshop.energyCapacity, true);
	}

	@Override
	public void update()
	{
		super.update();

		if(isDummy()||isRSDisabled())
			return;

		//drawer action
		if(world.isRemote)
		{
			for(int i = 0; i < 2; i++)
				drawerAngle[i] = MathHelper.clamp(drawerAngle[i]+(isDrawerOpened[i]?1: -1), 0, 20);
		}

		boolean update = false;

		if(world.isBlockPowered(getBlockPosForPos(getRedstonePos()[0]))^redstoneControlInverted)
			return;

		if(fillerUpgrade) //filling component storage from inventory/tanks
		{
			if(tanksFiller[0].getFluidAmount() >= 1000||!inventory.get(1).isEmpty())
			{
				if(componentInside.amount < ProjectileWorkshop.componentCapacity)
				{
					if(componentInside.isEmpty())
					{
						if(tanksFiller[0].getFluidAmount() >= 1000)
						{
							String name = Objects.requireNonNull(tanksFiller[0].getFluid()).getUnlocalizedName();
							AmmoRegistry.INSTANCE.registeredComponents.values().stream()
									.filter(comp -> name.equals(comp.getName()))
									.findFirst()
									.ifPresent(iBulletComponent -> componentInside = new BulletComponentStack(iBulletComponent, tanksFiller[0].getFluid().tag));
							update = true;
						}
						else
						{
							AmmoRegistry.INSTANCE.registeredComponents.values().stream()
									.filter(comp -> comp.getMaterial().matchesItemStackIgnoringSize(inventory.get(1)))
									.findFirst()
									.ifPresent(comp -> componentInside = new BulletComponentStack(comp, inventory.get(1).getTagCompound()));
							update = true;
						}
					}
					else
					{
						//fluid
						if(componentInside.isFluid())
						{
							if(tanksFiller[0].getFluidAmount() >= 1000&&componentInside.matches(tanksFiller[0].getFluid()))
							{
								this.componentInside.amount += 16;
								tanksFiller[0].drain(1000, true);
								update = true;
							}
						}
						else if(componentInside.matches(inventory.get(1)))
						{
							this.componentInside.amount += 16;
							inventory.get(1).shrink(1);
							update = true;
						}
					}
				}


				if(productionProgress <= 0)
				{
					if(!inventory.get(0).isEmpty()&&!componentInside.isEmpty())
					{
						effect = inventory.get(0).copy();
						effect.setCount(1);

						IAmmo bullet = (IAmmo)effect.getItem();
						if(componentInside.matches(bullet))
						{
							productionProgress = (int)(ProjectileWorkshop.fillingTime+ProjectileWorkshop.fillingTime*0.3*bullet.getCaliber());

							int i = 0;
							while(i < fillAmount&&bullet.hasFreeComponentSlots(effect))
							{
								bullet.addComponents(effect, AmmoRegistry.INSTANCE.getComponent(componentInside.name), componentInside.tagCompound);
								componentInside.subtract(1);
								i++;
							}

						}
						else
							productionProgress = 1;


						inventory.get(0).shrink(1);
						update = true;
					}
				}
				else
				{
					productionProgress--;
					if(productionProgress==0)
					{
						if(!world.isRemote)
						{
							outputItem(effect);
							effect = ItemStack.EMPTY;
							update = true;
						}

						productionProgress = 0;
					}
				}
			}
		}
		else //production
		{

			if(productionProgress <= 0)
			{
				if(!inventory.get(0).isEmpty())
				{
					Optional<IAmmoCore> first = AmmoRegistry.INSTANCE.registeredBulletCores.values()
							.stream()
							.filter(core -> core.getMaterial().matchesItemStackIgnoringSize(inventory.get(0)))
							.findFirst();

					if(first.isPresent()&&inventory.get(0).getCount() >= producedBullet.getCoreMaterialNeeded())
					{
						IAmmo bullet = producedBullet;
						EnumCoreTypes coreType = this.coreType;
						effect = bullet.getBulletCore(first.get(), coreType);
						productionProgress = (int)(ProjectileWorkshop.productionTime+ProjectileWorkshop.productionTime*0.3*bullet.getCaliber());
						inventory.get(0).shrink(producedBullet.getCoreMaterialNeeded());
						update = true;
					}
				}
			}
			else
			{
				productionProgress--;
				if(productionProgress==0)
				{
					if(!world.isRemote)
					{
						outputItem(effect);
						effect = ItemStack.EMPTY;
						update = true;
					}
					productionProgress = 0;
				}
			}

		}

		if(update)
		{
			this.markDirty();
			this.markContainingBlockForUpdate(null);

			IIPacketHandler.sendToClient(this, new MessageIITileSync(this, EasyNBT.newNBT()
					.withTag("component_inside", componentInside.serializeNBT())
					.withString("produced_bullet", producedBullet.getName())
					.withEnum("core_type", coreType)
					.withInt("production_progress", productionProgress)
					.withItemStack("effect", effect)
			));
		}


	}

	private void outputItem(ItemStack stack)
	{
		EnumFacing ff = mirrored?facing.rotateY(): facing.rotateYCCW();
		BlockPos pp = getBlockPosForPos(12).offset(ff);
		TileEntity inventoryTile = this.world.getTileEntity(pp);
		if(inventoryTile!=null)
			stack = Utils.insertStackIntoInventory(inventoryTile, stack, ff.getOpposite());
		if(!stack.isEmpty())
			Utils.dropStackAtPos(world, pp, stack);
	}

	@Override
	public void readCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);

		if(isDummy())
			return;

		upgradeProgress = nbt.getInteger("upgrade_progress");

		inventory = Utils.readInventory(nbt.getTagList("inventory", 10), inventory.size());
		tanksFiller[0] = tanksFiller[0].readFromNBT(nbt.getCompoundTag("tanks_filler"));
		tanksFactory[0] = tanksFactory[0].readFromNBT(nbt.getCompoundTag("tanks_factory"));

		fillerUpgrade = nbt.getBoolean("filler_upgrade");
		fillAmount = nbt.getInteger("fill_amount");
		componentInside = new BulletComponentStack(nbt.getCompoundTag("component_inside"));
		if(nbt.hasKey("produced_bullet"))
		{
			IAmmo bb = AmmoRegistry.INSTANCE.getBulletItem(nbt.getString("produced_bullet"));
			producedBullet = bb==null?IIContent.itemAmmoArtillery: bb;
		}
		if(nbt.hasKey("core_type"))
			coreType = EnumCoreTypes.v(nbt.getString("core_type"));

		productionProgress = nbt.getInteger("production_progress");
		effect = new ItemStack(nbt.getCompoundTag("effect"));

	}

	@Override
	public void writeCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);

		if(isDummy())
			return;

		nbt.setBoolean("filler_upgrade", fillerUpgrade);
		nbt.setInteger("upgrade_progress", upgradeProgress);

		nbt.setTag("inventory", Utils.writeInventory(inventory));
		nbt.setTag("tanks_filler", tanksFiller[0].writeToNBT(new NBTTagCompound()));
		nbt.setTag("tanks_factory", tanksFactory[0].writeToNBT(new NBTTagCompound()));

		nbt.setInteger("fill_amount", fillAmount);
		nbt.setTag("component_inside", componentInside.serializeNBT());
		nbt.setString("produced_bullet", producedBullet.getName());
		nbt.setString("core_type", coreType.getName());

		nbt.setInteger("production_progress", productionProgress);
		nbt.setTag("effect", effect.serializeNBT());
	}

	@Override
	public void receiveMessageFromServer(@Nonnull NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);

		if(isDummy())
			return;

		if(message.hasKey("filler_upgrade"))
			fillerUpgrade = message.getBoolean("filler_upgrade");
		if(message.hasKey("inventory"))
			inventory = Utils.readInventory(message.getTagList("inventory", 10), inventory.size());
		if(message.hasKey("tanks_filler"))
			tanksFiller[0] = tanksFiller[0].readFromNBT(message.getCompoundTag("tanks_filler"));
		if(message.hasKey("tanks_factory"))
			tanksFactory[0] = tanksFactory[0].readFromNBT(message.getCompoundTag("tanks_factory"));
		if(message.hasKey("upgrade_progress"))
			upgradeProgress = message.getInteger("upgrade_progress");
		if(message.hasKey("fill_amount"))
			fillAmount = message.getInteger("fill_amount");
		if(message.hasKey("component_inside"))
			componentInside = new BulletComponentStack(message.getCompoundTag("component_inside"));
		if(message.hasKey("produced_bullet"))
		{
			IAmmo bb = AmmoRegistry.INSTANCE.getBulletItem(message.getString("produced_bullet"));
			producedBullet = bb==null?IIContent.itemAmmoArtillery: bb;
		}
		if(message.hasKey("core_type"))
			coreType = EnumCoreTypes.v(message.getString("core_type"));
		if(message.hasKey("production_progress"))
			productionProgress = message.getInteger("production_progress");
		if(message.hasKey("effect"))
			effect = new ItemStack(message.getCompoundTag("effect"));
	}

	@Override
	public void receiveMessageFromClient(NBTTagCompound message)
	{
		super.receiveMessageFromClient(message);
		if(message.hasKey("core_type"))
			coreType = EnumCoreTypes.v(message.getString("core_type"));
		if(message.hasKey("produced_bullet"))
		{
			IAmmo bb = AmmoRegistry.INSTANCE.getBulletItem(message.getString("produced_bullet"));
			producedBullet = bb==null?IIContent.itemAmmoArtillery: bb;
		}
		if(message.hasKey("fill_amount"))
			fillAmount = MathHelper.clamp(message.getInteger("fill_amount"), 0, 4);
	}

	@Nullable
	@Override
	protected IMultiblockRecipe readRecipeFromNBT(@Nonnull NBTTagCompound tag)
	{
		return null;
	}

	@Nonnull
	@Override
	public int[] getEnergyPos()
	{
		return new int[]{46};
	}

	@Nonnull
	@Override
	public int[] getRedstonePos()
	{
		return new int[]{9};
	}

	@Nonnull
	@Override
	public IFluidTank[] getInternalTanks()
	{
		return fillerUpgrade?tanksFiller: tanksFactory;
	}

	@Nonnull
	@SuppressWarnings("MethodsReturnNonNullByDefault")
	@Override
	public IMultiblockRecipe findRecipeForInsertion(@Nonnull ItemStack inserting)
	{
		return null;
	}

	@Nonnull
	@Override
	public int[] getOutputSlots()
	{
		return new int[]{0};
	}

	@Nonnull
	@Override
	public int[] getOutputTanks()
	{
		return new int[0];
	}

	@Override
	public boolean additionalCanProcessCheck(@Nonnull MultiblockProcess<IMultiblockRecipe> process)
	{
		return true;
	}

	@Override
	public void doProcessOutput(@Nonnull ItemStack output)
	{

	}

	@Override
	public void doProcessFluidOutput(@Nonnull FluidStack output)
	{

	}

	@Override
	public void onProcessFinish(@Nonnull MultiblockProcess<IMultiblockRecipe> process)
	{

	}

	@Override
	public int getMaxProcessPerTick()
	{
		return 1;
	}

	@Override
	public int getProcessQueueMaxLength()
	{
		return 1;
	}

	@Override
	public float getMinProcessDistance(@Nonnull MultiblockProcess<IMultiblockRecipe> process)
	{
		return 1f;
	}

	@Override
	public boolean isInWorldProcessingMachine()
	{
		return true;
	}

	@Nonnull
	@Override
	protected IFluidTank[] getAccessibleFluidTanks(@Nonnull EnumFacing side)
	{
		TileEntityProjectileWorkshop master = this.master();
		if(master!=null)
		{
			if(pos==22&&side==facing)
				return new FluidTank[]{master.fillerUpgrade?master.tanksFiller[0]: master.tanksFactory[0]};
		}
		return new FluidTank[0];
	}

	@Override
	protected boolean canFillTankFrom(int i, @Nonnull EnumFacing side, @Nonnull FluidStack fluidStack)
	{
		return pos==22&&side==facing;
	}

	@Override
	protected boolean canDrainTankFrom(int i, @Nonnull EnumFacing enumFacing)
	{
		return false;
	}

	@Nonnull
	@Override
	public float[] getBlockBounds()
	{
		return new float[]{0, 0, 0, 1, 1, 1};
	}

	@Override
	public NonNullList<ItemStack> getInventory()
	{
		return this.inventory;
	}

	@Override
	public boolean isStackValid(int i, ItemStack stack)
	{
		if(i==1&&fillerUpgrade)
		{
			return AmmoRegistry.INSTANCE.registeredComponents.values().stream().anyMatch(comp -> comp.getMaterial().matchesItemStackIgnoringSize(stack));
		}
		else if(i==0)
		{
			if(fillerUpgrade)
				return stack.getItem() instanceof IAmmo&&((IAmmo)stack.getItem()).isBulletCore(stack);
			else
				return AmmoRegistry.INSTANCE.registeredBulletCores.values().stream().anyMatch(core -> core.getMaterial().matchesItemStackIgnoringSize(stack));
		}
		return false;
	}

	@Override
	public int getSlotLimit(int i)
	{
		return i==1?1: 64;
	}

	@Override
	public void doGraphicalUpdates(int i)
	{

	}

	@Override
	public boolean shoudlPlaySound(@Nonnull String sound)
	{
		TileEntityProjectileWorkshop master = master();

		// TODO: 31.10.2021 sounds
		/*
		if(master!=null&&master.processQueue.size() > 0)
		{
			MultiblockProcess<VulcanizerRecipe> process = master.processQueue.get(0);
			switch(sound)
			{
				case "immersiveintelligence:printing_press":
				{
					if(master.processQueue.size() > 1)
					{
						if(pl.pabilo8.immersiveintelligence.common.Utils.inRange(master.processQueue.get(1).processTick, master.processQueue.get(1).maxTicks, 0, 0.16))
							return true;
					}
					return pl.pabilo8.immersiveintelligence.common.Utils.inRange(process.processTick, process.maxTicks, 0, 0.165);
				}
				case "immersiveintelligence:vulcanizer_heating":
					return pl.pabilo8.immersiveintelligence.common.Utils.inRange(process.processTick, process.maxTicks, 0.2, 0.8);
				case "immersiveintelligence:howitzer_rotation_h":
					return pl.pabilo8.immersiveintelligence.common.Utils.inRange(process.processTick, process.maxTicks, 0.78, 0.84);
				case "immersiveintelligence:inserter_forward":
					return pl.pabilo8.immersiveintelligence.common.Utils.inRange(process.processTick, process.maxTicks, 0.93, 0.96);
				case "immersiveintelligence:inserter_backward":
					return pl.pabilo8.immersiveintelligence.common.Utils.inRange(process.processTick, process.maxTicks, 0.85, 0.86);
			}
		}
		 */

		return false;
	}

	@Override
	public void onGuiOpened(EntityPlayer player, boolean clientside)
	{
		if(!clientside)
			IIPacketHandler.sendToClient(this, new MessageIITileSync(this));
	}

	@Override
	public boolean canOpenGui()
	{
		return true;
	}

	@Override
	public int getGuiID()
	{
		return IIGuiList.GUI_PROJECTILE_WORKSHOP.ordinal();
	}

	@Nullable
	@Override
	public TileEntity getGuiMaster()
	{
		return master();
	}

	@Override
	public List<AxisAlignedBB> getAdvancedColisionBounds()
	{
		return getAdvancedSelectionBounds();
	}

	@Override
	public List<AxisAlignedBB> getAdvancedSelectionBounds()
	{
		ArrayList<AxisAlignedBB> list = new ArrayList<>();
		// TODO: 01.12.2021 make aabb

		return list;
	}

	@Override
	public boolean isOverrideBox(AxisAlignedBB box, EntityPlayer player, RayTraceResult mop, ArrayList<AxisAlignedBB> list)
	{
		return false;
	}

	@Override
	public boolean addUpgrade(MachineUpgrade upgrade, boolean test)
	{
		if(upgrade==IIContent.UPGRADE_CORE_FILLER)
			return this.fillerUpgrade = true;
		return false;
	}

	@Override
	public boolean hasUpgrade(MachineUpgrade upgrade)
	{
		return this.fillerUpgrade&&upgrade==IIContent.UPGRADE_CORE_FILLER;
	}

	@Override
	public boolean upgradeMatches(MachineUpgrade upgrade)
	{
		return upgrade==IIContent.UPGRADE_CORE_FILLER;
	}

	@Override
	public TileEntityProjectileWorkshop getUpgradeMaster()
	{
		return master();
	}

	@Override
	public void saveUpgradesToNBT(NBTTagCompound tag)
	{

	}

	@Override
	public void getUpgradesFromNBT(NBTTagCompound tag)
	{

	}

	@Override
	public void renderWithUpgrades(MachineUpgrade... upgrades)
	{
		ProjectileWorkshopRenderer.renderWithUpgrades(upgrades);
	}

	@Override
	public List<MachineUpgrade> getUpgrades()
	{
		return this.fillerUpgrade?Collections.singletonList(IIContent.UPGRADE_CORE_FILLER): Collections.emptyList();
	}

	@Nullable
	@Override
	public MachineUpgrade getCurrentlyInstalled()
	{
		return currentlyInstalled;
	}

	@Override
	public int getInstallProgress()
	{
		return upgradeProgress;
	}

	@Override
	public int getClientInstallProgress()
	{
		return upgradeProgress;
	}

	@Override
	public boolean addUpgradeInstallProgress(int toAdd)
	{
		upgradeProgress += toAdd;
		return true;
	}

	@Override
	public boolean resetInstallProgress()
	{
		if(upgradeProgress > 0)
		{
			currentlyInstalled = null;
			upgradeProgress = 0;
			return true;
		}
		return false;
	}

	@Override
	public void startUpgrade(@Nonnull MachineUpgrade upgrade)
	{
		currentlyInstalled = upgrade;
		upgradeProgress = 0;
	}

	@Override
	public void removeUpgrade(MachineUpgrade upgrade)
	{
		if(upgrade==IIContent.UPGRADE_CORE_FILLER)
			this.fillerUpgrade = false;
	}

	@Override
	public void onAnimationChangeClient(boolean state, int part)
	{
		if(part >= 2)
			return;
		this.isDrawerOpened[part] = state;
	}

	@Override
	public void onAnimationChangeServer(boolean state, int part)
	{
		if(part >= 2)
			return;
		if(state!=isDrawerOpened[part])
			world.playSound(null, getPos(), state?IISounds.metalBreadboxOpen: IISounds.metalBreadboxClose, SoundCategory.BLOCKS, 0.5F, 1f);

		this.isDrawerOpened[part] = state;

		for(int i = 0; i < 2; i++)
			IIPacketHandler.INSTANCE.sendToAllAround(new MessageBooleanAnimatedPartsSync(this.isDrawerOpened[i], i, getPos()), IIPacketHandler.targetPointFromPos(this.getPos(), this.world, 32));
	}

	@Override
	public EnumFacing[] sigOutputDirections()
	{
		if(pos==12)
			return new EnumFacing[]{mirrored?facing.rotateY(): facing.rotateYCCW()};
		return new EnumFacing[0];
	}

	@Override
	public void onReceive(DataPacket packet, @Nullable EnumFacing side)
	{
		if(pos==8)
		{
			TileEntityProjectileWorkshop master = master();
			if(master==null)
				return;

			//b - bullet, t - type, a -amount

			if(packet.hasVariable('b'))
				master.producedBullet = AmmoRegistry.INSTANCE.registeredBulletItems.getOrDefault(packet.getPacketVariable('b').valueToString(), IIContent.itemAmmoArtillery);
			if(packet.hasVariable('t'))
				master.coreType = EnumCoreTypes.v(packet.getPacketVariable('t').valueToString());

			if(Arrays.stream(master.producedBullet.getAllowedCoreTypes()).noneMatch(ct -> ct==master.coreType))
				master.coreType = master.producedBullet.getAllowedCoreTypes()[0];

			if(packet.hasVariable('a'))
				master.fillAmount = packet.getVarInType(DataTypeInteger.class, packet.getPacketVariable('a')).value;
		}
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		TileEntityProjectileWorkshop master = master();
		if(master!=null&&capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			if(pos==22&&(facing==this.facing))
			{
				if(master.fillerUpgrade)
					return true;
			}
			else if(pos==15)
			{
				return true;
			}
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		TileEntityProjectileWorkshop master = master();
		if(master!=null&&capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			if(pos==22&&(facing==this.facing))
			{
				if(master.fillerUpgrade)
					return (T)master.componentInputHandler;
			}
			else if(pos==15)
			{
				return (T)master.inputHandler;
			}
		}
		return super.getCapability(capability, facing);
	}

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

	public static class BulletComponentStack
	{
		@Nullable
		private IAmmoComponent component;
		private String name;
		private int amount;
		@Nonnull
		private NBTTagCompound tagCompound;

		private BulletComponentStack(String name, int amount, @Nonnull NBTTagCompound tag)
		{
			this.name = name;
			this.amount = amount;
			this.tagCompound = tag;

			Optional<IAmmoComponent> first = AmmoRegistry.INSTANCE.registeredComponents.values().stream()
					.filter(comp -> this.name.equals(comp.getName()))
					.findFirst();

			component = first.orElse(null);
		}

		private BulletComponentStack(IAmmoComponent component, @Nullable NBTTagCompound tag)
		{
			this(component.getName(), 16, tag==null?new NBTTagCompound(): tag);
		}

		public BulletComponentStack()
		{
			this("", 0, new NBTTagCompound());
		}

		public BulletComponentStack(NBTTagCompound tag)
		{
			this(tag.getString("name"), tag.getInteger("amount"), tag.getCompoundTag("nbt"));
		}

		public NBTTagCompound serializeNBT()
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("name", name);
			nbt.setInteger("amount", amount);
			nbt.setTag("nbt", tagCompound);

			return nbt;
		}

		public boolean isEmpty()
		{
			return name.isEmpty()||amount==0;
		}

		public boolean isFluid()
		{
			return component!=null&&component.getMaterial().fluid!=null;
		}

		public boolean matches(ItemStack stack)
		{
			if(isEmpty()||component==null)
				return true;

			NBTTagCompound stackTag = stack.getTagCompound();
			if(stackTag==null)
				stackTag = new NBTTagCompound();

			return component.getName().equals(name)&&this.tagCompound.equals(stackTag);
		}

		public boolean matches(FluidStack fs)
		{
			if(isEmpty())
				return true;

			NBTTagCompound stackTag = fs.tag==null?new NBTTagCompound(): fs.tag;

			return fs.getFluid().getName().equals(name)&&this.tagCompound.equals(stackTag);
		}

		public void subtract(int amount)
		{
			this.amount = Math.max(0, this.amount-amount);
			if(amount==0)
			{
				tagCompound = new NBTTagCompound();
				name = "";
				component = null;
			}
		}

		public boolean matches(IAmmo bullet)
		{
			return component!=null&&component.matchesBullet(bullet);
		}

		public int getColour()
		{
			return component!=null?component.getColour(): 0xffffff;
		}

		public float getAmountPercentage()
		{
			return amount/(float)ProjectileWorkshop.componentCapacity;
		}

		@SideOnly(Side.CLIENT)
		public String getTranslatedName()
		{
			return I18n.format("ie.manual.entry.bullet_component."+name);
		}

		public IAmmoComponent getComponent()
		{
			return component;
		}
	}
}
