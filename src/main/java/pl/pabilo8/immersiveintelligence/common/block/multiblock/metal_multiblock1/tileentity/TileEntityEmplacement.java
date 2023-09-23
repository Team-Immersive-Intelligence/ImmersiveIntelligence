package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity;

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
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.INpc;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Emplacement;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IDataConnector;
import pl.pabilo8.immersiveintelligence.api.data.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.types.*;
import pl.pabilo8.immersiveintelligence.api.utils.IBooleanAnimatedPartsBlock;
import pl.pabilo8.immersiveintelligence.api.utils.MachineUpgrade;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IUpgradableMachine;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IVehicleMultiPart;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.fx.ParticleUtils;
import pl.pabilo8.immersiveintelligence.client.gui.block.emplacement.GuiEmplacementPageStorage;
import pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.EmplacementRenderer;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.ammo.emplacement_weapons.EmplacementWeaponMachinegun;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityEmplacement.EmplacementWeapon.MachineUpgradeEmplacementWeapon;
import pl.pabilo8.immersiveintelligence.common.entity.EntityEmplacementWeapon;
import pl.pabilo8.immersiveintelligence.common.entity.EntityEmplacementWeapon.EmplacementHitboxEntity;
import pl.pabilo8.immersiveintelligence.common.entity.bullet.EntityBullet;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBooleanAnimatedPartsSync;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.raytracer.BlacklistedRayTracer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

// TODO: 26.09.2021 improve task sync and fix GUI
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
	BlockPos[] allBlocks = null;
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

		if(world.isRemote&&currentWeapon!=null)
			currentWeapon.handleSounds(getTileForPos(49), this);

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

	public BlockPos[] getAllBlocks()
	{
		TileEntityEmplacement master = master();
		if(master==this||master==null)
		{
			if(allBlocks==null)
			{
				ArrayList<BlockPos> pp = new ArrayList<>();
				for(int i = 0; i < structureDimensions[0]*structureDimensions[1]*structureDimensions[2]; i++)
					pp.add(getBlockPosForPos(i));
				allBlocks = pp.toArray(new BlockPos[0]);
			}
			return allBlocks;
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

		IDataType i = packet.variables.get('i');
		IDataType b = packet.variables.get('b');
		IDataType c = packet.variables.get('c');
		IDataType w = packet.variables.get('w');
		IDataType e = packet.variables.get('e');
		IDataType a = packet.variables.get('a');
		IDataType x = packet.variables.get('x');
		IDataType y = packet.variables.get('y');
		IDataType p = packet.variables.get('p');
		IDataType z = packet.variables.get('z');

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

						IDataType d = packet.getPacketVariable('d');
						int distance = 40;
						if(d instanceof DataTypeInteger)
							distance = ((DataTypeInteger)d).value;

						master.task = new EmplacementTaskPosition(new BlockPos(IIUtils.offsetPosDirection(distance, true_angle, true_angle2)).add(this.getBlockPosForPos(49)), amount);
						master.syncTask();

					}
					break;
			}
		}
	}

	private void handleSendingEnemyPos(Entity[] spottedEntity)
	{
		DataPacket packet = new DataPacket();
		final BlockPos center = this.getBlockPosForPos(49);
		DataTypeEntity[] entities = Arrays.stream(spottedEntity).map(entity -> new DataTypeEntity(entity, center)).toArray(DataTypeEntity[]::new);
		DataTypeArray arr = new DataTypeArray(entities);

		packet.setVariable('e', arr);

		IDataConnector conn = IIUtils.findConnectorFacing(getBlockPosForPos(0), world, facing.rotateYCCW());
		if(conn!=null)
			conn.sendPacket(packet);
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

	public static abstract class EmplacementWeapon
	{
		public EntityEmplacementWeapon entity = null;

		protected float pitch = 0, yaw = 0;
		protected float nextPitch = 0, nextYaw = 0;
		protected int health = 0;

		/**
		 * @return name of the emplacement, must be the same as the name in the weapon registry
		 */
		public abstract String getName();

		public float getYawTurnSpeed()
		{
			return 2;
		}

		public float getPitchTurnSpeed()
		{
			return 2;
		}

		/**
		 * @param yaw   destination
		 * @param pitch destination
		 * @return whether the gun is pointing to the pitch and yaw given
		 */
		public boolean isAimedAt(float yaw, float pitch)
		{
			return pitch==this.pitch&&MathHelper.wrapDegrees(yaw)==MathHelper.wrapDegrees(this.yaw);
		}

		// TODO: 10.07.2021 optimize

		/**
		 * Calculates final aiming angle of the weapon
		 *
		 * @param posTurret of the emplacement
		 * @param posTarget to be attacked
		 * @param motion    for moving entities, {@link Vec3d#ZERO} for other cases
		 * @return the final aiming angle
		 */
		public float[] getAnglePrediction(Vec3d posTurret, Vec3d posTarget, Vec3d motion)
		{
			Vec3d vv = posTurret.subtract(posTarget).add(motion).normalize();
			float yy = (float)((Math.atan2(vv.x, vv.z)*180D)/3.1415927410125732D);
			float pp = (float)Math.toDegrees((Math.atan2(vv.y, vv.distanceTo(new Vec3d(0, vv.y, 0)))));

			return new float[]{yy, pp};
		}

		/**
		 * Rotate the gun
		 *
		 * @param yaw   destination
		 * @param pitch destination
		 */
		public void aimAt(float yaw, float pitch)
		{
			nextPitch = pitch;
			nextYaw = MathHelper.wrapDegrees(yaw);
			float p = pitch-this.pitch;
			this.pitch += Math.signum(p)*MathHelper.clamp(Math.abs(p), 0, this.getPitchTurnSpeed());
			float y = MathHelper.wrapDegrees(360+nextYaw-this.yaw);

			if(Math.abs(p) < this.getPitchTurnSpeed()*0.5f)
				this.pitch = this.nextPitch;
			if(Math.abs(y) < this.getYawTurnSpeed()*0.5f)
				this.yaw = this.nextYaw;
			else
				this.yaw = MathHelper.wrapDegrees(this.yaw+(Math.signum(y)*MathHelper.clamp(Math.abs(y), 0, this.getYawTurnSpeed())));

			this.pitch = this.pitch%180;
		}

		/**
		 * @param door whether the emplacement door is open
		 * @return whether turret is setup
		 */
		public abstract boolean isSetUp(boolean door);

		/**
		 * Used for turret setup
		 *
		 * @param door whether the emplacement door is open
		 */
		public void doSetUp(boolean door)
		{

		}

		public abstract boolean requiresPlatformRefill();

		/**
		 * Called after the weapon is installed or loaded from NBT
		 * Initialize sight AABB here
		 */
		public void init(TileEntityEmplacement te, boolean firstTime)
		{
			//Default action, in most cases sending "I'm attacking xyz" signals is unnecessary
			//Exception is the IR Observer, which overrides this method
			if(firstTime)
			{
				health = getMaxHealth();
				te.sendAttackSignal = false;
				this.nextPitch = this.pitch = -90;
				this.nextYaw = this.yaw = te.facing.getHorizontalAngle();


				if(!te.world.isRemote)
				{
					//Spawn weapon entity for fancy hitboxes
					Vec3d vv = te.getWeaponCenter().subtract(0, 1, 0);
					entity = new EntityEmplacementWeapon(te.world);
					entity.setPosition(vv.x, vv.y, vv.z);
					te.world.spawnEntity(entity);
				}
			}
		}

		/**
		 * Used for reloading and other actions
		 * For setup delay use {@link #doSetUp(boolean)}
		 *
		 * @param te
		 * @param active
		 */
		public void tick(TileEntityEmplacement te, boolean active)
		{

		}

		/**
		 * Used for shooting action
		 */
		public void shoot(TileEntityEmplacement te)
		{
			if(entity!=null)
				blusunrize.immersiveengineering.common.util.Utils.attractEnemies(entity, 24);
		}

		/**
		 * @param forClient
		 * @return nbt tag with weapon's saved data
		 */
		@Nonnull
		public NBTTagCompound saveToNBT(boolean forClient)
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setFloat("yaw", yaw);
			tag.setFloat("pitch", pitch);
			tag.setInteger("health", health);
			return tag;
		}

		/**
		 * Reads from NBT tag
		 *
		 * @param tagCompound provided nbt tag (saved in tile's NBT tag "emplacement")
		 */
		public void readFromNBT(NBTTagCompound tagCompound)
		{
			yaw = tagCompound.getFloat("yaw");
			pitch = tagCompound.getFloat("pitch");
			health = tagCompound.hasKey("health")?tagCompound.getInteger("health"): getMaxHealth();
		}

		public void syncWithClient(TileEntityEmplacement te)
		{
			if(!te.getWorld().isRemote)
				IIPacketHandler.sendToClient(te, new MessageIITileSync(te, EasyNBT.newNBT()
						.withString("weaponName", getName())
						.withTag("currentWeapon", saveToNBT(true))
				));
		}

		public abstract boolean canShoot(TileEntityEmplacement te);

		@Nullable
		public IFluidHandler getFluidHandler(boolean in)
		{
			return null;
		}

		@Nullable
		public IItemHandler getItemHandler(boolean in)
		{
			return null;
		}

		public void handleDataPacket(DataPacket packet)
		{

		}

		@SideOnly(Side.CLIENT)
		public void render(TileEntityEmplacement te, float partialTicks)
		{

		}

		@SideOnly(Side.CLIENT)
		public void renderUpgradeProgress(int clientProgress, int serverProgress, float partialTicks)
		{

		}

		public static MachineUpgrade register(Supplier<EmplacementWeapon> supplier)
		{
			//hacky way, but works
			EmplacementWeapon w = supplier.get();
			weaponRegistry.put(w.getName(), supplier);
			return new MachineUpgradeEmplacementWeapon(w);
		}

		public void handleSounds(@Nullable TileEntityEmplacement tile, TileEntityEmplacement master)
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

		@Nonnull
		public abstract AxisAlignedBB getVisionAABB();

		public boolean canSeeEntity(Entity entity)
		{
			return !entity.isInvisible();
		}

		public void syncWithEntity(EntityEmplacementWeapon entity)
		{
			if(this.entity==null)
				this.entity = entity;
			if(this.entity!=entity)
				return;

			entity.rotationYaw = yaw;
			entity.rotationPitch = pitch;
			entity.setHealth(health);
			entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(getMaxHealth());

		}

		public abstract EmplacementHitboxEntity[] getCollisionBoxes();

		public void syncWeaponHealth(TileEntityEmplacement te)
		{
			IIPacketHandler.sendToClient(te, new MessageIITileSync(te, EasyNBT.newNBT().withInt("health", health)));
		}

		public abstract NonNullList<ItemStack> getBaseInventory();

		@SideOnly(Side.CLIENT)
		public abstract void renderStorageInventory(GuiEmplacementPageStorage gui, int mx, int my, float partialTicks, boolean first);

		public abstract void performPlatformRefill(TileEntityEmplacement te);

		public static class MachineUpgradeEmplacementWeapon extends MachineUpgrade
		{
			private final EmplacementWeapon weapon;

			public MachineUpgradeEmplacementWeapon(EmplacementWeapon weapon)
			{
				super(weapon.getName(), new ResourceLocation(ImmersiveIntelligence.MODID, "textures/gui/upgrade/"+weapon.getName()+".png"));
				this.weapon = weapon;
			}

			@SideOnly(Side.CLIENT)
			public void render(TileEntityEmplacement te)
			{
				weapon.render(te, 0);
			}

			@SideOnly(Side.CLIENT)
			public void renderUpgradeProgress(int clientProgress, int serverProgress, float partialTicks)
			{
				weapon.renderUpgradeProgress(clientProgress, serverProgress, partialTicks);
			}
		}

		public abstract int getEnergyUpkeepCost();

		public abstract int getMaxHealth();

		public int getHealth()
		{
			return health;
		}

		public void applyDamage(float damage)
		{
			this.health -= damage;

		}

		public boolean isDead()
		{
			return health <= 0;
		}

		@SideOnly(Side.CLIENT)
		public void spawnDebrisExplosion(TileEntityEmplacement te)
		{
			double true_angle = Math.toRadians((-yaw) > 180?360f-(-yaw): (-yaw));
			double true_angle2 = Math.toRadians((-yaw-90) > 180?360f-(-yaw-90): (-yaw-90));
			Random rand = new Random(431L);

			Tuple<ResourceLocation, List<ModelRendererTurbo>> stuff = getDebris();
			List<ModelRendererTurbo> models = stuff.getSecond();
			ResourceLocation texture = stuff.getFirst();

			Vec3d weaponCenter = te.getWeaponCenter();
			ParticleUtils.spawnExplosionBoomFX(entity.world, weaponCenter, 5, 4, true, true);

			for(ModelRendererTurbo mod : models)
			{
				Vec3d vx = IIUtils.offsetPosDirection((float)(mod.rotationPointX*0.0625), true_angle, 0);
				Vec3d vz = IIUtils.offsetPosDirection((float)(-mod.rotationPointZ*0.0625), true_angle+90, 0);
				Vec3d vo = weaponCenter
						.add(vx)
						.add(IIUtils.offsetPosDirection((float)(mod.rotationPointY*0.0625), -true_angle2, 0))
						.add(vz);
				Vec3d vecDir = new Vec3d(rand.nextGaussian()*0.075, rand.nextGaussian()*0.15, rand.nextGaussian()*0.075);

				ParticleUtils.spawnTMTModelFX(vo, vx.add(vz).addVector(0, 0.25+vecDir.y, 0).scale(0.66), 0.0625f, mod, texture);
			}
		}

		@SideOnly(Side.CLIENT)
		protected abstract Tuple<ResourceLocation, List<ModelRendererTurbo>> getDebris();
	}

	public abstract static class EmplacementTask
	{
		public abstract float[] getPositionVector(TileEntityEmplacement emplacement);

		public void onShot()
		{

		}

		/**
		 * @return whether the task should continue execution, should be always true if it is a permanent detection task like {@link EmplacementTaskEntities}
		 */
		public abstract boolean shouldContinue();

		/**
		 * @return target name/id for loading from nbt and init
		 */
		public abstract String getName();

		public NBTTagCompound saveToNBT()
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("name", getName());
			return nbt;
		}

		/**
		 * Reduces lag.
		 * Used to update visible targets list on target seeking tasks, like {@link EmplacementTaskEntities}
		 * Leave empty if it is a single target and you're not doing any detection
		 *
		 * @param emplacement to which the task belongs
		 */
		public abstract void updateTargets(TileEntityEmplacement emplacement);
	}

	private static abstract class EmplacementTaskEntities extends EmplacementTask
	{
		private final Predicate<Entity> predicate;

		public EmplacementTaskEntities(Predicate<Entity> predicate)
		{
			this.predicate = predicate;
		}

		Entity[] spottedEntities = new Entity[0];
		Entity currentTarget = null;

		@Override
		public float[] getPositionVector(TileEntityEmplacement emplacement)
		{
			final BlockPos[] allBlocks = emplacement.getAllBlocks();
			final Vec3d vEmplacement = emplacement.getWeaponCenter();

			if(currentTarget!=null)
			{
				if(predicate.test(currentTarget)&&currentTarget.isEntityAlive()&&canEntityBeSeen(currentTarget, vEmplacement, allBlocks, 2))
					return getPosForEntityTask(emplacement, currentTarget);
			}

			for(Entity entity : spottedEntities)
			{
				if(canEntityBeSeen(entity, vEmplacement, allBlocks, 2))
				{
					currentTarget = entity;
					return getPosForEntityTask(emplacement, entity);
				}
			}

			return null;
		}

		/**
		 * Scans for entity using volumetric raytrace
		 *
		 * @param entity       to be traced
		 * @param vEmplacement starting position
		 * @param allBlocks    all blocks of the emplacement, ignored in raytracing
		 * @param maxBlocks    how many blocks of wall can the entity be behind
		 * @return whether entity is visible
		 */
		private boolean canEntityBeSeen(Entity entity, Vec3d vEmplacement, BlockPos[] allBlocks, int maxBlocks)
		{
			Vec3d vEntity = entity.getPositionVector().addVector(-entity.width/2f, entity.height/2f, -entity.width/2f);
			RayTraceResult rt = BlacklistedRayTracer.traceIgnoringBlocks(entity.world, vEmplacement, vEntity, Arrays.asList(allBlocks), maxBlocks);

			return rt==null||rt.typeOfHit==Type.MISS;
		}

		@Override
		public boolean shouldContinue()
		{
			return true;
		}

		@Override
		public NBTTagCompound saveToNBT()
		{
			return super.saveToNBT();
		}

		@Override
		public void updateTargets(TileEntityEmplacement emplacement)
		{
			spottedEntities = emplacement.world.getEntitiesWithinAABB(Entity.class, emplacement.currentWeapon.getVisionAABB(), input -> predicate.test(input)&&emplacement.currentWeapon.canSeeEntity(input)).stream().sorted((o1, o2) -> (int)((o1.width*o1.height)-(o2.width*o2.height))*10).toArray(Entity[]::new);
			if(!emplacement.world.isRemote&&emplacement.sendAttackSignal)
				emplacement.handleSendingEnemyPos(spottedEntities);
		}
	}

	private static class EmplacementTaskCustom extends EmplacementTaskEntities
	{
		private final NBTTagCompound tagCompound;

		public EmplacementTaskCustom(NBTTagCompound tagCompound)
		{
			//input -> input instanceof EntityLivingBase&&(input instanceof IMob||input.getTeam()!=null)&&input.isEntityAlive()
			super(buildPredicate(tagCompound));
			this.tagCompound = tagCompound;
		}

		private static Predicate<Entity> buildPredicate(NBTTagCompound nbt)
		{
			Predicate<Entity> mainFilter = entity -> entity!=null&&entity.isEntityAlive();
			ArrayList<Predicate<Entity>> typeFilter = new ArrayList<>();
			ArrayList<Predicate<Entity>> nameFilter = new ArrayList<>();
			ArrayList<Predicate<Entity>> teamFilter = new ArrayList<>();

			NBTTagList tagList = nbt.getTagList("filters", 10);
			for(NBTBase nbtBase : tagList)
				if(nbtBase instanceof NBTTagCompound)
				{
					NBTTagCompound tag = (NBTTagCompound)nbtBase;
					final String filter = tag.getString("filter");
					final boolean negation = tag.getBoolean("negation");

					switch(tag.getString("type"))
					{
						case "mobs":
						{
							if(!filter.isEmpty())
								teamFilter.add(entity -> {
									EntityEntry entry = EntityRegistry.getEntry(entity.getClass());
									if(entry==null)
										return false;
									return negation^new ResourceLocation(filter).equals(entry.getRegistryName());
								});
							else
								typeFilter.add(entity -> entity instanceof IMob);
						}
						break;
						case "animals":
						{
							if(!filter.isEmpty())
								teamFilter.add(entity -> {
									EntityEntry entry = EntityRegistry.getEntry(entity.getClass());
									if(entry==null)
										return false;
									return negation^new ResourceLocation(filter).equals(entry.getRegistryName());
								});
							else
								typeFilter.add(entity -> entity instanceof EntityAnimal);
						}
						break;
						case "players":
						{
							typeFilter.add(entity -> entity instanceof EntityPlayer);
						}
						case "npcs":
						{
							typeFilter.add(entity -> entity instanceof INpc);
						}
						break;
						case "vehicles":
						{
							typeFilter.add(entity -> entity instanceof IVehicleMultiPart);
						}
						case "shells":
						{
							typeFilter.add(entity -> entity instanceof EntityBullet&&((EntityBullet)entity).bullet.getCaliber() >= 6f);
						}
						break;
						case "team":
						{
							if(!filter.isEmpty())
								teamFilter.add(entity -> entity.getTeam()!=null&&(negation^entity.getTeam().getName().equals(filter)));
							else
								teamFilter.add(entity -> entity.getTeam()==null);
						}
						break;
						case "name":
						{
							if(!filter.isEmpty())
								nameFilter.add(entity -> negation^entity.getName().equals(filter));
							else
								nameFilter.add(entity -> entity.getName().isEmpty());
						}
						break;
					}
				}
			if(typeFilter.size() > 0)
				mainFilter = mainFilter.and(predicateFromList(typeFilter));
			if(nameFilter.size() > 0)
				mainFilter = mainFilter.and(predicateFromList(nameFilter));
			if(teamFilter.size() > 0)
				mainFilter = mainFilter.and(predicateFromList(teamFilter));


			return mainFilter;
		}

		private static Predicate<Entity> predicateFromList(ArrayList<Predicate<Entity>> list)
		{
			Predicate<Entity> pred = list.get(0);
			if(list.size() > 1)
				for(int i = 1; i < list.size(); i++)
				{
					pred = pred.or(list.get(i));
				}

			return pred;
		}

		@Override
		public String getName()
		{
			return "target_custom";
		}

		@Override
		public NBTTagCompound saveToNBT()
		{
			NBTTagCompound nbt = super.saveToNBT();
			nbt.merge(tagCompound);
			return nbt;
		}
	}

	private static class EmplacementTaskShells extends EmplacementTaskEntities
	{
		public EmplacementTaskShells()
		{
			/*
			&&
					(
							((EntityBullet)input).getShooter()==null//||(((EntityBullet)input).getShooter() instanceof EntityPlayer)
					)
			 */
			super(input -> input instanceof EntityBullet
					&&!input.isDead
					&&((EntityBullet)input).mass > 0.4);
		}

		@Override
		public String getName()
		{
			return "target_shells";
		}
	}

	private static class EmplacementTaskEntity extends EmplacementTask
	{
		Entity entity;

		public EmplacementTaskEntity(Entity entity)
		{
			this.entity = entity;
		}

		public EmplacementTaskEntity(TileEntityEmplacement emplacement, NBTTagCompound tagCompound)
		{
			this(emplacement.world.getEntityByID(tagCompound.getInteger("target_uuid")));
		}

		@Override
		public float[] getPositionVector(TileEntityEmplacement emplacement)
		{
			return getPosForEntityTask(emplacement, entity);
		}

		@Override
		public boolean shouldContinue()
		{
			return entity!=null&&entity.isEntityAlive();
		}

		@Override
		public String getName()
		{
			return "target_entity";
		}

		@Override
		public NBTTagCompound saveToNBT()
		{
			NBTTagCompound compound = super.saveToNBT();
			compound.setInteger("target_uuid", entity.getEntityId());
			return compound;
		}

		@Override
		public void updateTargets(TileEntityEmplacement emplacement)
		{

		}
	}

	private static class EmplacementTaskPosition extends EmplacementTask
	{
		int shotAmount;
		final BlockPos pos;

		public EmplacementTaskPosition(BlockPos pos, int shotAmount)
		{
			this.pos = pos;
			this.shotAmount = shotAmount;
		}

		public EmplacementTaskPosition(NBTTagCompound tagCompound)
		{
			this(new BlockPos(tagCompound.getInteger("x"), tagCompound.getInteger("y"), tagCompound.getInteger("z")),
					tagCompound.getInteger("shotAmount")
			);
		}

		@Override
		public float[] getPositionVector(TileEntityEmplacement emplacement)
		{
			return emplacement.currentWeapon.getAnglePrediction(emplacement.getWeaponCenter(),
					new Vec3d(pos).addVector(0.5, 0, 0.5),
					Vec3d.ZERO);
		}

		@Override
		public void onShot()
		{
			shotAmount--;
		}

		@Override
		public boolean shouldContinue()
		{
			return shotAmount > 0;
		}

		@Override
		public String getName()
		{
			return "target_position";
		}

		@Override
		public NBTTagCompound saveToNBT()
		{
			NBTTagCompound compound = super.saveToNBT();
			compound.setInteger("x", pos.getX());
			compound.setInteger("y", pos.getY());
			compound.setInteger("z", pos.getZ());
			compound.setInteger("shotAmount", shotAmount);
			return compound;
		}

		@Override
		public void updateTargets(TileEntityEmplacement emplacement)
		{

		}
	}

	public Vec3d getWeaponCenter()
	{
		return new Vec3d(this.getBlockPosForPos(49).up()).addVector(0.5, 0, 0.5);
	}

	private static float[] getPosForEntityTask(TileEntityEmplacement emplacement, Entity entity)
	{
		if(entity!=null&&entity.isEntityAlive())
		{
			if(entity instanceof IEntityMultiPart)
			{
				Entity[] parts = entity.getParts();
				if(parts!=null&&parts.length > 0)
				{
					//target the biggest hitbox
					Entity t = Arrays.stream(parts).max((o1, o2) -> (int)((o1.width*o1.height)-(o2.width*o2.height))).orElse(parts[0]);
					return emplacement.currentWeapon.getAnglePrediction(emplacement.getWeaponCenter(),
							t.getPositionVector().addVector(-t.width/2f, t.height/2f, -t.width/2f),
							new Vec3d(entity.motionX, entity.motionY, entity.motionZ));
				}
			}
			return emplacement.currentWeapon.getAnglePrediction(emplacement.getWeaponCenter(),
					entity.getPositionVector().addVector(-entity.width/2f, entity.height/2f, -entity.width/2f),
					new Vec3d(entity.motionX, entity.motionY, entity.motionZ));
		}
		return new float[]{emplacement.currentWeapon.yaw, emplacement.currentWeapon.pitch};
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
