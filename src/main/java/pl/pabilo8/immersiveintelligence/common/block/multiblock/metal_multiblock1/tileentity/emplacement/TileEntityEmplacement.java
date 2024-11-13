package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedCollisionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedSelectionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.ISoundTile;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.ChatUtils;
import com.elytradev.mirage.event.GatherLightsEvent;
import com.elytradev.mirage.lighting.ILightEventConsumer;
import com.elytradev.mirage.lighting.Light;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.*;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataHandlingUtils;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.types.*;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.utils.IBooleanAnimatedPartsBlock;
import pl.pabilo8.immersiveintelligence.api.utils.IUpgradableMachine;
import pl.pabilo8.immersiveintelligence.api.utils.MachineUpgrade;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.EmplacementRenderer;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Emplacement;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.*;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon.EmplacementWeapon;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon.EmplacementWeapon.MachineUpgradeEmplacementWeapon;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon.EmplacementWeaponMachinegun;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBooleanAnimatedPartsSync;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Supplier;

// TODO: 26.09.2021 improve task sync and fix GUI
//TODO: 15.02.2024 move to new multiblock technology and AMT
@net.minecraftforge.fml.common.Optional.Interface(iface = "com.elytradev.mirage.lighting.ILightEventConsumer", modid = "mirage")
public class TileEntityEmplacement extends TileEntityMultiblockMetal<TileEntityEmplacement, MultiblockRecipe> implements IBooleanAnimatedPartsBlock, IDataDevice, IUpgradableMachine, IAdvancedCollisionBounds, IAdvancedSelectionBounds, ISoundTile, IGuiTile, ILightEventConsumer
{
	public static final HashMap<String, Supplier<EmplacementWeapon>> weaponRegistry = new HashMap<>();
	public static final HashMap<String, BiFunction<NBTTagCompound, TileEntityEmplacement, EmplacementTask>> targetRegistry = new HashMap<>();

	static
	{
		targetRegistry.put("target_custom", (tagCompound, emplacement) -> new EmplacementTaskCustom(tagCompound));
		targetRegistry.put("target_shells", (tagCompound, emplacement) -> new EmplacementTaskShells());
		targetRegistry.put("target_position", (tagCompound, emplacement) -> new EmplacementTaskPosition(tagCompound));
		targetRegistry.put("target_entity", (tagCompound, emplacement) -> new EmplacementTaskEntity(emplacement, tagCompound));
	}

	public String owner = "";

	public boolean redstoneControl = true, dataControl = true;
	public int defaultTargetMode = 0;
	public NBTTagCompound[] defaultTaskNBT = new NBTTagCompound[]{
			createDefaultTask(),
			createDefaultTask(),
			createDefaultTask(),
			createDefaultTask()
	};

	public boolean isDoorOpened = false;

	public int repairTick = Emplacement.repairDelay;
	public boolean forcedRepair = false, firstRepairTick = true;
	public float autoRepairAmount = 0.25f;

	public int progress = 0, upgradeProgress = 0, clientUpgradeProgress = 0;
	public EmplacementWeapon currentWeapon = null;
	public boolean isShooting = false;
	@Nullable
	private MachineUpgrade currentlyInstalled = null;
	EmplacementTask task = new EmplacementTaskCustom(defaultTaskNBT[defaultTargetMode]);
	private float[] target = null;
	public boolean sendAttackSignal = false;

	//Config, -1 is null, 0-3 are valid
	//public String defaultTargetMode = "target_mobs";

	public TileEntityEmplacement()
	{
		super(MultiblockEmplacement.INSTANCE, new int[]{6, 3, 3}, Emplacement.energyCapacity, true);
	}

	@Override
	public void update()
	{
		super.update();

		if(isDummy()||!hasWorld())
			return;

		if(world.isRemote)
		{
			if(clientUpgradeProgress < getMaxClientProgress())
				clientUpgradeProgress = (int)Math.min(clientUpgradeProgress+(Tools.wrenchUpgradeProgress/2f), getMaxClientProgress());
		}

		boolean wasDoorOpened = isDoorOpened;

		if(currentlyInstalled!=null)
		{
			isDoorOpened = true;
		}
		else if(currentWeapon!=null&&((forcedRepair&&currentWeapon.getHealth()!=currentWeapon.getMaxHealth())||currentWeapon.requiresPlatformRefill()))
		{
			isDoorOpened = false;
		}
		else if(currentWeapon!=null&&(currentWeapon.getHealth()/(float)currentWeapon.getMaxHealth() <= autoRepairAmount))
		{
			forcedRepair = true;
			isDoorOpened = false;
		}
		else if(!world.isRemote&&redstoneControl)
		{
			if(isDoorOpened^world.isBlockPowered(getBlockPosForPos(getRedstonePos()[0])))
			{
				isDoorOpened = world.isBlockPowered(getBlockPosForPos(getRedstonePos()[0]));
			}
		}

		if(!world.isRemote&&wasDoorOpened^isDoorOpened)
			IIPacketHandler.INSTANCE.sendToAllAround(new MessageBooleanAnimatedPartsSync(isDoorOpened, 0, this.getPos()), IIPacketHandler.targetPointFromTile(this, 48));

		if(currentWeapon!=null)
		{
			if(forcedRepair)
				forcedRepair = currentWeapon.getHealth()!=currentWeapon.getMaxHealth();

			if(!world.isRemote&&world.getTotalWorldTime()%60==0)
				currentWeapon.syncWeaponHealth(this);
			if(currentWeapon.isDead())
			{
				if(world.isRemote)
					currentWeapon.spawnDebrisExplosion(this);
				else
					currentWeapon.syncWeaponHealth(this);

				if(currentWeapon.entity!=null)
					currentWeapon.entity.setDead();
				currentWeapon = null;
			}
		}

		if(world.isRemote)
			handleSounds(master());

		if(isDoorOpened)
		{
			if(progress < Emplacement.lidTime)
				progress++;
			else
			{
				if(currentWeapon!=null&&energyStorage.extractEnergy(currentWeapon.getEnergyUpkeepCost(), true) >= currentWeapon.getEnergyUpkeepCost())
				{
					if(!world.isRemote)
						energyStorage.modifyEnergyStored(-currentWeapon.getEnergyUpkeepCost());

					if(currentWeapon.isSetUp(true))
					{
						currentWeapon.tick(this, true);
						if(this.task!=null)
						{

							if(world.getTotalWorldTime()%Emplacement.sightUpdateTime==0)
								this.task.updateTargets(this);
							target = this.task.getPositionVector(this);

							if(target!=null)
							{
								target[0] = MathHelper.wrapDegrees(target[0]);
								target[1] = MathHelper.wrapDegrees(target[1]);

								currentWeapon.aimAt(target[0], target[1]);

								if(currentWeapon.isAimedAt(target[0], target[1]))
								{
									if(currentWeapon.canShoot(this))
									{
										isShooting = true;
										currentWeapon.shoot(this);
										task.onShot();
									}
									else
										isShooting = false;
								}
								else
									isShooting = false;
							}
							else
							{
								isShooting = false;
								currentWeapon.aimAt(currentWeapon.yaw, currentWeapon.pitch);
							}
						}
						if(task==null||!task.shouldContinue())
						{
							if(defaultTargetMode==-1)
								task = null;
							else
								task = new EmplacementTaskCustom(defaultTaskNBT[defaultTargetMode]);
						}
					}
					else
					{
						currentWeapon.doSetUp(true);
					}
				}
			}
		}
		else
		{
			if(currentWeapon!=null)
			{
				currentWeapon.tick(this, false);
				if(progress==0&&!forcedRepair&&currentWeapon.requiresPlatformRefill())
				{
					if(!world.isRemote)
						currentWeapon.performPlatformRefill(this);
				}
				else if(currentWeapon.health!=currentWeapon.getMaxHealth())
				{
					if(progress==0)
					{

						if(world.isRemote&&(energyStorage.getEnergyStored() >= Emplacement.repairCost))
						{
							ImmersiveEngineering.proxy.handleTileSound(IISounds.weldingMid, getTileForPos(31),
									true,
									5f, 1f);
						}

						if(firstRepairTick)
						{
							BlockPos repairPos = getBlockPosForPos(31);
							if(!world.isRemote)
								world.playSound(null, repairPos.getX(), repairPos.getY()+1, repairPos.getZ(), IISounds.weldingStart, SoundCategory.BLOCKS, 4f, 1f);
							firstRepairTick = false;
						}
						if(repairTick > 0)
							repairTick--;
						else if(energyStorage.getEnergyStored() >= Emplacement.repairCost)
						{
							energyStorage.extractEnergy(Emplacement.repairCost, false);
							currentWeapon.health = Math.min(currentWeapon.health+Emplacement.repairAmount, currentWeapon.getMaxHealth());
							repairTick = Emplacement.repairDelay;
						}

						if(currentWeapon.health==currentWeapon.getMaxHealth())
						{
							BlockPos repairPos = getBlockPosForPos(31);
							if(!world.isRemote)
								world.playSound(null, repairPos.getX(), repairPos.getY()+1, repairPos.getZ(), IISounds.weldingEnd, SoundCategory.BLOCKS, 4f, 1f);
							forcedRepair = false;
							firstRepairTick = true;
						}
					}
				}

				if(currentWeapon.isSetUp(false))
				{
					if(progress > 0)
						progress--;
					if(currentWeapon instanceof EmplacementWeaponMachinegun)
					{ //machine gun yaw is limited, use special method
						((EmplacementWeaponMachinegun)currentWeapon).aimAtUnrestricted(facing.getHorizontalAngle(), -90);
					}
					else
					{
						currentWeapon.aimAt(facing.getHorizontalAngle(), -90);
					}
				}
				else
					currentWeapon.doSetUp(false);
			}
			else if(progress > 0)
				progress--;
		}

	}

	public List<BlockPos> getAllBlocks()
	{
		TileEntityEmplacement master = master();
		if(master==this||master==null)
		{
			ArrayList<BlockPos> blocks = new ArrayList<>();
			for(int i = 0; i < structureDimensions[0]*structureDimensions[1]*structureDimensions[2]; i++)
				blocks.add(getBlockPosForPos(i));
			return blocks;
		}
		else
			return master.getAllBlocks();
	}

	public boolean finishedDoorAction()
	{
		return isDoorOpened?(progress==Emplacement.lidTime): (progress==0);
	}

	@Override
	protected MultiblockRecipe readRecipeFromNBT(NBTTagCompound tag)
	{
		return null;
	}

	@Override
	public int[] getEnergyPos()
	{
		return new int[]{1};
	}

	@Override
	public int[] getRedstonePos()
	{
		return new int[]{6};
	}

	@Override
	public IFluidTank[] getInternalTanks()
	{
		return new IFluidTank[0];
	}

	@Override
	public MultiblockRecipe findRecipeForInsertion(ItemStack inserting)
	{
		return null;
	}

	@Override
	public int[] getOutputSlots()
	{
		return new int[0];
	}

	@Override
	public int[] getOutputTanks()
	{
		return new int[0];
	}

	@Override
	public boolean additionalCanProcessCheck(MultiblockProcess<MultiblockRecipe> process)
	{
		return false;
	}

	@Override
	public void doProcessOutput(ItemStack output)
	{
		if(output.isEmpty())
			return;
		BlockPos pos = getBlockPosForPos(8).offset(facing.rotateY());
		TileEntity inventoryTile = this.world.getTileEntity(pos);
		if(inventoryTile!=null)
			output = blusunrize.immersiveengineering.common.util.Utils.insertStackIntoInventory(inventoryTile, output, facing.rotateYCCW());
		if(!output.isEmpty())
			blusunrize.immersiveengineering.common.util.Utils.dropStackAtPos(world, pos, output, facing);
	}

	@Override
	public void doProcessFluidOutput(FluidStack output)
	{

	}

	@Override
	public void onProcessFinish(MultiblockProcess<MultiblockRecipe> process)
	{

	}

	@Override
	public int getMaxProcessPerTick()
	{
		return 0;
	}

	@Override
	public int getProcessQueueMaxLength()
	{
		return 0;
	}

	@Override
	public float getMinProcessDistance(MultiblockProcess<MultiblockRecipe> process)
	{
		return 0;
	}

	@Override
	public boolean isInWorldProcessingMachine()
	{
		return false;
	}

	@Nonnull
	@Override
	protected IFluidTank[] getAccessibleFluidTanks(EnumFacing side)
	{
		return new IFluidTank[0];
	}

	@Override
	protected boolean canFillTankFrom(int iTank, EnumFacing side, FluidStack resource)
	{
		return false;
	}

	@Override
	protected boolean canDrainTankFrom(int iTank, EnumFacing side)
	{
		return false;
	}

	@Override
	public float[] getBlockBounds()
	{
		return new float[]{0, 0, 0, 1, 1, 1};
	}

	@Override
	public NonNullList<ItemStack> getInventory()
	{
		return currentWeapon==null?NonNullList.create(): currentWeapon.getBaseInventory();
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return false;
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return 0;
	}

	@Override
	public void disassemble()
	{
		super.disassemble();
		if(!isDummy()&&currentWeapon!=null&&currentWeapon.entity!=null)
			currentWeapon.entity.setDead();
		currentWeapon = null;
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);

		if(isDummy())
			return;

		this.progress = nbt.getInteger("progress");
		this.upgradeProgress = nbt.getInteger("upgradeProgress");
		this.isDoorOpened = nbt.getBoolean("isDoorOpened");
		this.redstoneControl = nbt.getBoolean("redstoneControl");
		this.forcedRepair = nbt.getBoolean("forcedRepair");
		this.autoRepairAmount = nbt.getFloat("autoRepairAmount");
		this.dataControl = nbt.getBoolean("dataControl");
		this.sendAttackSignal = nbt.getBoolean("sendAttackSignal");

		this.currentlyInstalled = MachineUpgrade.getUpgradeByID(nbt.getString("currentlyInstalled"));
		this.upgradeProgress = nbt.getInteger("upgradeProgress");

		this.owner = nbt.getString("owner");

		this.defaultTargetMode = nbt.getInteger("defaultTargetMode");

		this.defaultTaskNBT[0] = nbt.getCompoundTag("defaultTaskNBT1");
		this.defaultTaskNBT[1] = nbt.getCompoundTag("defaultTaskNBT2");
		this.defaultTaskNBT[2] = nbt.getCompoundTag("defaultTaskNBT3");
		this.defaultTaskNBT[3] = nbt.getCompoundTag("defaultTaskNBT4");

		if(!isDummy()&&!descPacket)
		{
			if(nbt.hasKey("currentWeapon"))
			{
				currentWeapon = getWeaponFromName(nbt.getString("weaponName"));
				if(currentWeapon!=null)
				{
					currentWeapon.readFromNBT(nbt.getCompoundTag("currentWeapon"));
					currentWeapon.init(this, false);
				}
			}

			if(nbt.hasKey("task"))
			{
				NBTTagCompound taskNBT = nbt.getCompoundTag("task");
				BiFunction<NBTTagCompound, TileEntityEmplacement, EmplacementTask> name = targetRegistry.get(taskNBT.getString("name"));
				if(name!=null)
					this.task = name.apply(taskNBT, this);
			}
		}
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);

		if(isDummy())
			return;

		nbt.setBoolean("isDoorOpened", this.isDoorOpened);
		nbt.setBoolean("redstoneControl", this.redstoneControl);
		nbt.setBoolean("forcedRepair", this.forcedRepair);
		nbt.setFloat("autoRepairAmount", this.autoRepairAmount);
		nbt.setBoolean("dataControl", this.dataControl);
		nbt.setBoolean("sendAttackSignal", this.sendAttackSignal);
		nbt.setInteger("upgradeProgress", this.upgradeProgress);
		nbt.setString("currentlyInstalled", this.currentlyInstalled==null?"": currentlyInstalled.toString());

		nbt.setString("owner", owner);

		nbt.setInteger("defaultTargetMode", this.defaultTargetMode);

		nbt.setInteger("progress", this.progress);

		nbt.setTag("defaultTaskNBT1", this.defaultTaskNBT[0]);
		nbt.setTag("defaultTaskNBT2", this.defaultTaskNBT[1]);
		nbt.setTag("defaultTaskNBT3", this.defaultTaskNBT[2]);
		nbt.setTag("defaultTaskNBT4", this.defaultTaskNBT[3]);

		if(!isDummy())
		{
			if(currentWeapon!=null)
			{
				nbt.setString("weaponName", currentWeapon.getName());
				nbt.setTag("currentWeapon", currentWeapon.saveToNBT(false));
			}
			if(task!=null)
			{
				nbt.setTag("task", task.saveToNBT());
			}
		}
	}

	@Override
	public void receiveMessageFromServer(NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);

		if(isDummy())
			return;

		if(message.hasKey("isDoorOpened"))
			this.isDoorOpened = message.getBoolean("isDoorOpened");
		if(message.hasKey("redstoneControl"))
			this.redstoneControl = message.getBoolean("redstoneControl");
		if(message.hasKey("autoRepairAmount"))
			this.autoRepairAmount = message.getFloat("autoRepairAmount");
		if(message.hasKey("dataControl"))
			this.dataControl = message.getBoolean("dataControl");
		if(message.hasKey("sendAttackSignal"))
			this.sendAttackSignal = message.getBoolean("sendAttackSignal");
		if(message.hasKey("progress"))
			this.progress = message.getInteger("progress");

		if(message.hasKey("currentlyInstalled"))
		{
			this.currentlyInstalled = MachineUpgrade.getUpgradeByID(message.getString("currentlyInstalled"));
			this.upgradeProgress = message.getInteger("upgradeProgress");
			this.clientUpgradeProgress = this.upgradeProgress;
		}

		if(message.hasKey("defaultTaskNBT1"))
			this.defaultTaskNBT[0] = message.getCompoundTag("defaultTaskNBT1");
		if(message.hasKey("defaultTaskNBT2"))
			this.defaultTaskNBT[1] = message.getCompoundTag("defaultTaskNBT2");
		if(message.hasKey("defaultTaskNBT3"))
			this.defaultTaskNBT[2] = message.getCompoundTag("defaultTaskNBT3");
		if(message.hasKey("defaultTaskNBT4"))
			this.defaultTaskNBT[3] = message.getCompoundTag("defaultTaskNBT4");

		if(message.hasKey("sendAttackSignal"))
			this.sendAttackSignal = message.getBoolean("sendAttackSignal");

		if(message.hasKey("health")&&this.currentWeapon!=null)
		{
			this.currentWeapon.health = message.getInteger("health");
		}

		if(!isDummy())
		{
			if(message.hasKey("weaponName"))
			{
				if(message.getString("weaponName").isEmpty())
					this.currentWeapon = null;
				else
				{
					if(currentWeapon==null||!currentWeapon.getName().equals(message.getString("weaponName")))
						currentWeapon = getWeaponFromName(message.getString("weaponName"));
					if(currentWeapon!=null)
					{
						if(currentlyInstalled instanceof MachineUpgradeEmplacementWeapon)
							resetInstallProgress();
						currentWeapon.readFromNBT(message.getCompoundTag("currentWeapon"));
						currentWeapon.init(this, false);
					}
				}

			}

			if(message.hasKey("task"))
			{
				NBTTagCompound taskNBT = message.getCompoundTag("task");
				BiFunction<NBTTagCompound, TileEntityEmplacement, EmplacementTask> name = targetRegistry.get(taskNBT.getString("name"));
				if(name!=null)
					this.task = name.apply(taskNBT, this);
				else
					this.task = null;
			}
		}
	}

	@Override
	public void receiveMessageFromClient(NBTTagCompound message)
	{
		super.receiveMessageFromClient(message);

		if(isDummy())
			return;

		if(message.hasKey("redstoneControl"))
			this.redstoneControl = message.getBoolean("redstoneControl");
		if(message.hasKey("dataControl"))
			this.dataControl = message.getBoolean("dataControl");
		if(message.hasKey("autoRepairAmount"))
			this.autoRepairAmount = message.getFloat("autoRepairAmount");
		if(message.hasKey("sendAttackSignal"))
			this.sendAttackSignal = message.getBoolean("sendAttackSignal");

		if(message.hasKey("defaultTargetMode"))
			this.defaultTargetMode = message.getInteger("defaultTargetMode");

		boolean taskChanged = false;

		if(message.hasKey("defaultTaskNBT1"))
		{
			this.defaultTaskNBT[0] = message.getCompoundTag("defaultTaskNBT1");
			taskChanged = true;
		}
		if(message.hasKey("defaultTaskNBT2"))
		{
			this.defaultTaskNBT[1] = message.getCompoundTag("defaultTaskNBT2");
			taskChanged = true;
		}
		if(message.hasKey("defaultTaskNBT3"))
		{
			this.defaultTaskNBT[2] = message.getCompoundTag("defaultTaskNBT3");
			taskChanged = true;
		}
		if(message.hasKey("defaultTaskNBT4"))
		{
			this.defaultTaskNBT[3] = message.getCompoundTag("defaultTaskNBT4");
			taskChanged = true;
		}

		if(taskChanged&&task instanceof EmplacementTaskCustom)
		{
			if(defaultTargetMode==-1)
				task = null;
			else
				task = new EmplacementTaskCustom(defaultTaskNBT[defaultTargetMode]);
			syncTask();
		}

		IIPacketHandler.sendToClient(this, new MessageIITileSync(this, message));
	}

	private EmplacementWeapon getWeaponFromName(String weaponName)
	{
		if(weaponName==null)
			return null;
		if(weaponRegistry.containsKey(weaponName))
			return weaponRegistry.get(weaponName).get();
		return null;
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox()
	{
		if(!isDummy())
		{
			return new AxisAlignedBB(
					getPos().getX()-1,
					getPos().getY()+1,
					getPos().getZ()-1,
					getPos().getX()+1,
					getPos().getY()-8,
					getPos().getZ()+1
			);
		}
		return super.getRenderBoundingBox();
	}

	@Override
	public void doGraphicalUpdates(int slot)
	{

	}

	public void syncTask()
	{
		//sends an empty tag when no task
		NBTTagCompound taskTag = new NBTTagCompound();
		if(task!=null)
			taskTag.setTag("task", task.saveToNBT());
		IIPacketHandler.sendToClient(this, new MessageIITileSync(this, taskTag));
	}

	@Override
	public void onAnimationChangeClient(boolean state, int part)
	{
		if(part==0)
			isDoorOpened = state;
	}

	@Override
	public void onAnimationChangeServer(boolean state, int part)
	{
		if(part==0)
			isDoorOpened = state;
		IIPacketHandler.INSTANCE.sendToAllAround(new MessageBooleanAnimatedPartsSync(isDoorOpened, 1, getPos()), IIPacketHandler.targetPointFromTile(this, 32));
	}

	@Override
	public void onReceive(DataPacket packet, EnumFacing side)
	{
		if(pos!=0)
			return;

		DataType i = packet.variables.get('i');
		DataType b = packet.variables.get('b');
		DataType c = packet.variables.get('c');
		DataType w = packet.variables.get('w');
		DataType e = packet.variables.get('e');
		DataType a = packet.variables.get('a');
		DataType x = packet.variables.get('x');
		DataType y = packet.variables.get('y');
		DataType p = packet.variables.get('p');
		DataType z = packet.variables.get('z');

		TileEntityEmplacement master = master();
		if(master==null||!master.dataControl)
			return;

		if(master.currentWeapon!=null)
			master.currentWeapon.handleDataPacket(packet.clone());

		if(c instanceof DataTypeString)
		{
			switch(((DataTypeString)c).value)
			{
				case "opendoor":
				{
					IIPacketHandler.INSTANCE.sendToAllAround(new MessageBooleanAnimatedPartsSync(master.isDoorOpened = true, 0, master.getPos()),
							IIPacketHandler.targetPointFromTile(master, 48));
				}
				break;
				case "closedoor":
				{
					IIPacketHandler.INSTANCE.sendToAllAround(new MessageBooleanAnimatedPartsSync(master.isDoorOpened = false, 0, master.getPos()),
							IIPacketHandler.targetPointFromTile(master, 48));
				}
				break;
				case "door":
				{
					if(b instanceof DataTypeBoolean)
					{
						IIPacketHandler.INSTANCE.sendToAllAround(new MessageBooleanAnimatedPartsSync(master.isDoorOpened = ((DataTypeBoolean)b).value, 0, master.getPos()),
								IIPacketHandler.targetPointFromTile(master, 48));
					}
				}
				break;
				case "rscontrol":
				{
					if(b instanceof DataTypeBoolean)
						master.redstoneControl = ((DataTypeBoolean)b).value;
				}
				break;
				case "reload":
					break;
				case "stop":
				{
					master.forcedRepair = false;
					if(master.defaultTargetMode==-1)
						master.task = null;
					else
						master.task = new EmplacementTaskCustom(defaultTaskNBT[defaultTargetMode]);
				}
				break;
				case "repair":
				{
					if(master.currentWeapon!=null)
					{
						master.forcedRepair = master.currentWeapon.getHealth()!=master.currentWeapon.getMaxHealth();
						if(master.forcedRepair)
							master.firstRepairTick = true;
					}
				}
				break;
				case "target":
				{
					if(i instanceof DataTypeInteger)
					{
						//0,1,2,3,4
						master.defaultTargetMode = MathHelper.clamp(((DataTypeInteger)i).value, 0, 4)-1;

						if(master.defaultTargetMode==-1)
							master.task = null;
						else
							master.task = new EmplacementTaskCustom(defaultTaskNBT[defaultTargetMode]);
						master.syncTask();
					}
				}
				break;
				case "targetreset":
				{
					if(master.defaultTargetMode==-1)
						master.task = null;
					else if(!(task instanceof EmplacementTaskCustom))
						master.task = new EmplacementTaskCustom(defaultTaskNBT[defaultTargetMode]);
					master.syncTask();
				}
				break;
				case "targetshells":
				{
					master.task = new EmplacementTaskShells();
					master.syncTask();
				}
				break;
				case "fire":
					if(e instanceof DataTypeNull)
					{
						if(master.defaultTargetMode==-1)
							master.task = null;
						else if(!(task instanceof EmplacementTaskCustom))
							master.task = new EmplacementTaskCustom(defaultTaskNBT[defaultTargetMode]);
						master.syncTask();
					}
					else if(e instanceof DataTypeInteger||(e instanceof DataTypeEntity&&((DataTypeEntity)e).dimensionID==world.provider.getDimension()))
					{
						int id = e instanceof DataTypeInteger?((DataTypeInteger)e).value: ((DataTypeEntity)e).entityID;
						Entity entityByID = world.getEntityByID(id);
						if(entityByID!=null)
						{
							master.task = new EmplacementTaskEntity(entityByID);
							master.syncTask();
						}
					}
					else if(x instanceof DataTypeInteger&&y instanceof DataTypeInteger&&z instanceof DataTypeInteger)
					{
						int xx = ((DataTypeInteger)x).value;
						int yy = ((DataTypeInteger)y).value;
						int zz = ((DataTypeInteger)z).value;
						int amount = a instanceof DataTypeInteger?((DataTypeInteger)a).value: 1;

						//Same as in howitzer
						master.task = new EmplacementTaskPosition(new BlockPos(xx, yy, zz).add(this.getBlockPosForPos(49)), amount);
						master.syncTask();
					}
					else if(y instanceof DataTypeInteger&&p instanceof DataTypeInteger)
					{
						int yy = ((DataTypeInteger)y).value;
						int pp = ((DataTypeInteger)p).value;

						double true_angle = Math.toRadians(-yy);
						double true_angle2 = Math.toRadians(pp);

						int amount = a instanceof DataTypeInteger?((DataTypeInteger)a).value: 1;

						DataType d = packet.getPacketVariable('d');
						int distance = 40;
						if(d instanceof DataTypeInteger)
							distance = ((DataTypeInteger)d).value;

						master.task = new EmplacementTaskPosition(new BlockPos(IIMath.offsetPosDirection(distance, true_angle, true_angle2)).add(this.getBlockPosForPos(49)), amount);
						master.syncTask();

					}
					break;
			}
		}
	}

	public void handleSendingEnemyPos(Entity[] spottedEntity)
	{
		DataPacket packet = new DataPacket();
		final BlockPos center = this.getBlockPosForPos(49);
		DataTypeEntity[] entities = Arrays.stream(spottedEntity).map(entity -> new DataTypeEntity(entity, center)).toArray(DataTypeEntity[]::new);
		DataTypeArray arr = new DataTypeArray(entities);

		packet.setVariable('e', arr);

		IIDataHandlingUtils.sendPacketAdjacently(packet, world, getBlockPosForPos(0), facing.rotateYCCW());
	}

	@Override
	public boolean addUpgrade(MachineUpgrade upgrade, boolean test)
	{
		if(upgrade instanceof MachineUpgradeEmplacementWeapon)
		{
			if(currentWeapon==null)
			{
				currentWeapon = getWeaponFromName(upgrade.getName());
				currentWeapon.init(this, true);
				if(!world.isRemote)
					currentWeapon.syncWithClient(this);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean hasUpgrade(MachineUpgrade upgrade)
	{
		return currentWeapon!=null&&upgrade.getName().equals(currentWeapon.getName());
	}

	@Override
	public boolean upgradeMatches(MachineUpgrade upgrade)
	{
		if(currentWeapon==null)
			return upgrade instanceof MachineUpgradeEmplacementWeapon;
		// TODO: 02.08.2021 machinegun
		/*
		if(currentWeapon instanceof EmplacementWeaponAutocannon)
		{
			if((upgrade==IIContent.UPGRADE_EMPLACEMENT_MACHINEGUN_HEAVYBARREL&&!hasUpgrade(IIContent.UPGRADE_EMPLACEMENT_MACHINEGUN_WATERCOOLED))
					||(upgrade==IIContent.UPGRADE_EMPLACEMENT_MACHINEGUN_WATERCOOLED&&!hasUpgrade(IIContent.UPGRADE_EMPLACEMENT_MACHINEGUN_HEAVYBARREL))
					||upgrade==IIContent.UPGRADE_EMPLACEMENT_MACHINEGUN_BUNKER)
				return true;
		}
		return (upgrade==IIContent.UPGRADE_EMPLACEMENT_FALLBACK_GRENADES||upgrade==IIContent.UPGRADE_EMPLACEMENT_STURDY_BEARINGS);
		 */
		return false;
	}

	@Override
	public <T extends TileEntity & IUpgradableMachine> T getUpgradeMaster()
	{
		return (T)master();
	}

	@Deprecated
	@Override
	public void saveUpgradesToNBT(NBTTagCompound tag)
	{

	}

	@Deprecated
	@Override
	public void getUpgradesFromNBT(NBTTagCompound tag)
	{

	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderWithUpgrades(MachineUpgrade... upgrades)
	{
		GlStateManager.pushMatrix();
		GlStateManager.scale(0.75, 0.75, 0.75);
		IIClientUtils.bindTexture(EmplacementRenderer.texture);
		GlStateManager.translate(-0.5, -3.0625, 1.5);
		EmplacementRenderer.model.platformModel[0].render();
		EmplacementRenderer.model.platformModel[2].render();
		GlStateManager.translate(0.5, 3.0625, -1.5);
		for(MachineUpgrade upgrade : upgrades)
		{
			if(upgrade instanceof MachineUpgradeEmplacementWeapon)
				((MachineUpgradeEmplacementWeapon)upgrade).render(this);
		}
		GlStateManager.popMatrix();
	}

	@Override
	public List<MachineUpgrade> getUpgrades()
	{
		if(isDummy())
			return master().getUpgrades();

		if(currentWeapon!=null)
			return Collections.singletonList(weaponToUpgrade());
		else
			return new ArrayList<>();
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
		return clientUpgradeProgress;
	}

	@Override
	public boolean addUpgradeInstallProgress(int toAdd)
	{
		if(finishedDoorAction())
		{
			upgradeProgress += toAdd;
			return true;
		}
		return false;
	}

	@Override
	public boolean resetInstallProgress()
	{
		currentlyInstalled = null;
		if(upgradeProgress > 0)
		{
			upgradeProgress = 0;
			clientUpgradeProgress = 0;
			return true;
		}

		if(!world.isRemote)
		{
			markDirty();
			markContainingBlockForUpdate(null);
		}

		return false;
	}

	@Override
	public void startUpgrade(@Nonnull MachineUpgrade upgrade)
	{
		currentlyInstalled = upgrade;
		upgradeProgress = 0;
		clientUpgradeProgress = 0;
	}

	@Override
	public void removeUpgrade(MachineUpgrade upgrade)
	{
		if(currentWeapon==null)
			return;

		if(currentWeapon.entity!=null)
			currentWeapon.entity.setDead();
		currentWeapon = null;
		upgradeProgress = 0;
		clientUpgradeProgress = 0;
	}

	private MachineUpgradeEmplacementWeapon weaponToUpgrade()
	{
		return (MachineUpgradeEmplacementWeapon)MachineUpgrade.getUpgradeByID(currentWeapon.getName());
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
		if(offset[1]==1)
			list.add(new AxisAlignedBB(0, 0, 0, 1, 0.0625f, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
		else
			list.add(new AxisAlignedBB(0, 0, 0, 1, 1, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
		return list;
	}

	@Override
	public boolean isOverrideBox(AxisAlignedBB box, EntityPlayer player, RayTraceResult mop, ArrayList<AxisAlignedBB> list)
	{
		return false;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		TileEntityEmplacement master = master();
		if(master!=null&&master.currentWeapon!=null)
		{
			if(pos==2||pos==8)
			{
				boolean in = pos==2;
				if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY&&facing==this.facing.rotateY())
					return master.currentWeapon.getItemHandler(in)!=null;
				else if(capability==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY&&facing==this.facing.rotateY())
					return master.currentWeapon.getFluidHandler(in)!=null;
			}
		}

		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		TileEntityEmplacement master = master();
		if(master!=null&&master.currentWeapon!=null)
		{
			if(pos==2||pos==8)
			{
				boolean in = pos==2;
				if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY&&facing==this.facing.rotateY())
					return (T)master.currentWeapon.getItemHandler(in);
				else if(capability==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY&&facing==this.facing.rotateY())
					return (T)master.currentWeapon.getFluidHandler(in);
			}
		}

		return super.getCapability(capability, facing);
	}

	public void handleSounds(TileEntityEmplacement master)
	{
		TileEntityEmplacement t1 = master.getTileForPos(49);
		TileEntityEmplacement t2 = master.getTileForPos(48);
		TileEntityEmplacement t3 = master.getTileForPos(47);
		TileEntityEmplacement t4 = master.getTileForPos(46);
		TileEntityEmplacement t5 = master.getTileForPos(45);

		float[] target = master.target;
		boolean n = master.task!=null&&target!=null;

		if(t1!=null)
			ImmersiveEngineering.proxy.handleTileSound(IISounds.emplacementRotationH, t1, t1.shoudlPlaySound("immersiveintelligence:emplacement_rotation_h"), 1.5f, 0.5f);
		if(t2!=null)
			ImmersiveEngineering.proxy.handleTileSound(IISounds.emplacementRotationV, t2, t2.shoudlPlaySound("immersiveintelligence:emplacement_rotation_v"), 1.5f, 0.5f);
		if(t3!=null)
			ImmersiveEngineering.proxy.handleTileSound(IISounds.emplacementPlatform, t3, t3.shoudlPlaySound("immersiveintelligence:emplacement_platform"), 4f, 0.5f);
		if(t4!=null)
			ImmersiveEngineering.proxy.handleTileSound(IISounds.emplacementDoorOpen, t4, t4.shoudlPlaySound("immersiveintelligence:emplacement_door_open"), 4f, 0.5f);
		if(t5!=null)
			ImmersiveEngineering.proxy.handleTileSound(IISounds.emplacementDoorClose, t5, t5.shoudlPlaySound("immersiveintelligence:emplacement_door_close"), 4f, 0.5f);
	}

	@Override
	public boolean shoudlPlaySound(String sound)
	{
		TileEntityEmplacement master = master();
		if(master!=null)
		{
			switch(sound)
			{
				case "immersiveintelligence:emplacement_rotation_h":
					return master.target!=null&&master.currentWeapon!=null&&
							MathHelper.wrapDegrees(Math.round(master.currentWeapon.yaw))==MathHelper.wrapDegrees(Math.round(master.target[0]));
				case "immersiveintelligence:emplacement_rotation_v":
					return master.target!=null&&master.currentWeapon!=null&&
							MathHelper.wrapDegrees(Math.round(master.currentWeapon.pitch))==MathHelper.wrapDegrees(Math.round(master.target[1]));
				case "immersiveintelligence:welding_mid":
					return (progress==0&&master.currentWeapon!=null&&master.currentWeapon.health!=master.currentWeapon.getMaxHealth()&&(master.energyStorage.getEnergyStored() >= Emplacement.repairCost));
				//h - 0.25-0.65, 0.85-1
				//door - 0-0.25, 0.65-0.85
				case "immersiveintelligence:emplacement_platform":
				{
					float v = master.progress/(float)Emplacement.lidTime;
					return !master.finishedDoorAction()&&((v > 0.25f&&v < 0.65f)||(v > 0.85f));
				}
				case "immersiveintelligence:emplacement_door_open":
				{
					float v = master.progress/(float)Emplacement.lidTime;
					return !master.finishedDoorAction()&&(master.isDoorOpened?(v < 0.25f): (v > 0.65f&&v < 0.85f));
				}
				case "immersiveintelligence:emplacement_door_close":
				{
					float v = master.progress/(float)Emplacement.lidTime;
					return !master.finishedDoorAction()&&(!master.isDoorOpened?(v < 0.25f): (v > 0.65f&&v < 0.85f));
				}
				default:
					return false;
			}
		}
		return false;
	}

	@Override
	public boolean canOpenGui(EntityPlayer player)
	{
		if(hasOwnerRights(player))
			return true;
		ChatUtils.sendServerNoSpamMessages(player, new TextComponentTranslation(Lib.CHAT_INFO+"notOwner", owner));
		return false;
	}

	protected boolean hasOwnerRights(EntityPlayer player)
	{
		if(owner.isEmpty())
			owner = player.getName();

		if(player.capabilities.isCreativeMode||owner.isEmpty())
			return true;
		return owner.equalsIgnoreCase(player.getName());
	}

	@Override
	public boolean canOpenGui()
	{
		return false;
	}

	@Override
	public int getGuiID()
	{
		return IIGuiList.GUI_EMPLACEMENT_STORAGE.ordinal();
	}

	@Nullable
	@Override
	public TileEntity getGuiMaster()
	{
		return master();
	}

	@Override
	@SideOnly(Side.CLIENT)
	@Optional.Method(modid = "mirage")
	public void gatherLights(GatherLightsEvent gatherLightsEvent)
	{
		if(isDummy())
			return;

		if(currentWeapon!=null&&forcedRepair&&progress==0)
		{
			BlockPos pp = getBlockPosForPos(31);
			float f = Math.abs(((world.getTotalWorldTime()%6)/6f)-0.5f)*2f;

			gatherLightsEvent.add(Light.builder()
					.pos(pp)
					.color((159+f*40)/255f, (213+f*40)/255f, (215+f*40)/255f, 1f)
					.intensity(3f)
					.build()
			);

		}
	}

	public Vec3d getWeaponCenter()
	{
		return new Vec3d(this.getBlockPosForPos(49).up()).addVector(0.5, 0, 0.5);
	}

	private NBTTagCompound createDefaultTask()
	{
		NBTTagCompound compound = new NBTTagCompound();
		NBTTagList list = new NBTTagList();
		NBTTagCompound taskCompound = new NBTTagCompound();

		taskCompound.setString("type", "mobs");
		taskCompound.setBoolean("negation", false);

		list.appendTag(taskCompound);
		compound.setTag("filters", list);
		return compound;
	}
}
