package pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second;

import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedCollisionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedSelectionBounds;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.*;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.ServerWorldEventHandler;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.Emplacement;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.MultipleRayTracer;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;
import pl.pabilo8.immersiveintelligence.api.utils.IBooleanAnimatedPartsBlock;
import pl.pabilo8.immersiveintelligence.api.utils.MachineUpgrade;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IUpgradableMachine;
import pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.EmplacementRenderer;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.TileEntityEmplacement.EmplacementWeapon.MachineUpgradeEmplacementWeapon;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityBullet;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.MessageBooleanAnimatedPartsSync;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class TileEntityEmplacement extends TileEntityMultiblockMetal<TileEntityEmplacement, MultiblockRecipe> implements IBooleanAnimatedPartsBlock, IDataDevice, IUpgradableMachine, IAdvancedCollisionBounds, IAdvancedSelectionBounds
{
	public static final HashMap<String, Supplier<EmplacementWeapon>> weaponRegistry = new HashMap<>();
	public static final HashMap<String, BiFunction<NBTTagCompound, TileEntityEmplacement, EmplacementTarget>> targetRegistry = new HashMap<>();

	static
	{
		targetRegistry.put("target_mobs", (tagCompound, emplacement) -> new EmplacementTargetMobs());
		targetRegistry.put("target_shells", (tagCompound, emplacement) -> new EmplacementTargetShells());
		targetRegistry.put("target_position", (tagCompound, emplacement) -> new EmplacementTargetPosition(tagCompound));
		targetRegistry.put("target_entity", (tagCompound, emplacement) -> new EmplacementTargetEntity(emplacement, tagCompound));
	}

	public boolean isDoorOpened = false;
	public int progress = 0, upgradeProgress = 0, clientUpgradeProgress = 0;
	public EmplacementWeapon currentWeapon = null;
	BlockPos[] allBlocks = null;
	public boolean isShooting = false;
	private MachineUpgrade currentlyInstalled = null;
	EmplacementTarget task = new EmplacementTargetMobs();

	public TileEntityEmplacement()
	{
		super(MultiblockEmplacement.instance, new int[]{6, 3, 3}, Emplacement.energyCapacity, true);
	}

	@Override
	public void update()
	{
		super.update();

		if(isDummy())
			return;

		if(world.isRemote)
		{
			if(!isDummy()&&world.isRemote&&clientUpgradeProgress < getMaxClientProgress())
				clientUpgradeProgress = (int)Math.min(clientUpgradeProgress+(Tools.wrench_upgrade_progress/2f), getMaxClientProgress());
			//handleSounds();
		}
		if(currentlyInstalled!=null)
		{
			isDoorOpened = true;
			IIPacketHandler.INSTANCE.sendToAllAround(new MessageBooleanAnimatedPartsSync(true, 0, this.getPos()), pl.pabilo8.immersiveintelligence.api.Utils.targetPointFromTile(this, 48));
		}
		else if(!world.isRemote&&(isDoorOpened^world.isBlockPowered(getBlockPosForPos(getRedstonePos()[0]))))
		{
			isDoorOpened = world.isBlockPowered(getBlockPosForPos(getRedstonePos()[0]));
			IIPacketHandler.INSTANCE.sendToAllAround(new MessageBooleanAnimatedPartsSync(isDoorOpened, 0, this.getPos()), pl.pabilo8.immersiveintelligence.api.Utils.targetPointFromTile(this, 48));
		}

		if(isDoorOpened)
		{
			if(progress < Emplacement.lidTime)
				progress++;
			else
			{
				if(currentWeapon!=null)
				{
					if(currentWeapon.isSetUp(true))
					{
						currentWeapon.tick();
						//currentWeapon.reloadFrom(this);
						/*
						!
						 */
						float[] target = this.task.getPositionVector(this);
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

						if(!task.shouldContinue())
							task = new EmplacementTargetMobs();
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
				if(currentWeapon.isSetUp(false))
				{
					if(progress > 0)
						progress--;
					currentWeapon.aimAt(0, -90);
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
		return NonNullList.create();
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
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		this.progress = nbt.getInteger("progress");
		this.upgradeProgress = nbt.getInteger("upgradeProgress");
		this.isDoorOpened = nbt.getBoolean("isDoorOpened");

		if(!isDummy())
		{
			if(nbt.hasKey("currentWeapon"))
			{
				currentWeapon = getWeaponFromName(nbt.getString("weaponName"));
				if(currentWeapon!=null)
					currentWeapon.readFromNBT(nbt.getCompoundTag("currentWeapon"));
			}

			if(nbt.hasKey("task"))
			{
				NBTTagCompound taskNBT = nbt.getCompoundTag("task");
				BiFunction<NBTTagCompound, TileEntityEmplacement, EmplacementTarget> name = targetRegistry.get(taskNBT.getString("name"));
				if(name!=null)
					this.task=name.apply(taskNBT,this);
			}
		}
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);
		nbt.setBoolean("isDoorOpened", this.isDoorOpened);
		nbt.setInteger("upgradeProgress",this.upgradeProgress);

		nbt.setInteger("progress", this.progress);

		if(!isDummy())
		{
			if(currentWeapon!=null)
			{
				nbt.setString("weaponName", currentWeapon.getName());
				nbt.setTag("currentWeapon", currentWeapon.saveToNBT());
			}
			if(task!=null)
			{
				nbt.setTag("task",task.saveToNBT());
			}
		}
	}

	@Override
	public void receiveMessageFromServer(NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);
		if(message.hasKey("isDoorOpened"))
			this.isDoorOpened = message.getBoolean("isDoorOpened");
		if(message.hasKey("progress"))
			this.progress = message.getInteger("progress");

		if(!isDummy())
		{
			currentWeapon = getWeaponFromName(message.getString("weaponName"));
			if(currentWeapon!=null)
				currentWeapon.readFromNBT(message.getCompoundTag("currentWeapon"));

			if(message.hasKey("task"))
			{
				NBTTagCompound taskNBT = message.getCompoundTag("task");
				BiFunction<NBTTagCompound, TileEntityEmplacement, EmplacementTarget> name = targetRegistry.get(taskNBT.getString("name"));
				if(name!=null)
					this.task=name.apply(taskNBT,this);
			}
		}
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
		IIPacketHandler.INSTANCE.sendToAllAround(new MessageBooleanAnimatedPartsSync(isDoorOpened, 1, getPos()), pl.pabilo8.immersiveintelligence.api.Utils.targetPointFromPos(this.getPos(), this.world, 32));
	}

	@Override
	public void onReceive(DataPacket packet, EnumFacing side)
	{
		IDataType c = packet.variables.get('c');
		IDataType w = packet.variables.get('w');
		IDataType e = packet.variables.get('e');
		IDataType a = packet.variables.get('a');
		IDataType x = packet.variables.get('x');
		IDataType y = packet.variables.get('y');
		IDataType z = packet.variables.get('z');

		TileEntityEmplacement master = master();
		if(master==null)
			return;

		if(c instanceof DataPacketTypeString)
		{
			switch(((DataPacketTypeString)c).value)
			{
				case "reload":
				case "rscontrol":
				case "repair":
					break;
				case "targetmobs":
					master.task = new EmplacementTargetMobs();
					break;
				case "targetshells":
					master.task = new EmplacementTargetShells();
					break;
				case "fire":
					if(e instanceof DataPacketTypeInteger)
					{
						Entity entityByID = world.getEntityByID(((DataPacketTypeInteger)e).value);
						if(entityByID!=null)
							master.task = new EmplacementTargetEntity(entityByID);
					}
					else if(x instanceof DataPacketTypeInteger&&y instanceof DataPacketTypeInteger&&z instanceof DataPacketTypeInteger)
					{
						int xx = ((DataPacketTypeInteger)x).value;
						int yy = ((DataPacketTypeInteger)y).value;
						int zz = ((DataPacketTypeInteger)z).value;
						int amount = a instanceof DataPacketTypeInteger?((DataPacketTypeInteger)a).value: 1;

						master.task = new EmplacementTargetPosition(new BlockPos(xx, yy, zz), amount);
					}

					break;
				case "fireentity":
					if(e instanceof DataPacketTypeInteger)
					{
						Entity entityByID = world.getEntityByID(((DataPacketTypeInteger)e).value);
						if(entityByID!=null)
							master.task = new EmplacementTargetEntity(entityByID);
					}
					break;
				case "firepos":
					if(x instanceof DataPacketTypeInteger&&y instanceof DataPacketTypeInteger&&z instanceof DataPacketTypeInteger)
					{
						int xx = ((DataPacketTypeInteger)x).value;
						int yy = ((DataPacketTypeInteger)y).value;
						int zz = ((DataPacketTypeInteger)z).value;
						int amount = a instanceof DataPacketTypeInteger?((DataPacketTypeInteger)a).value: 1;

						master.task = new EmplacementTargetPosition(new BlockPos(xx, yy, zz), amount);
					}
					break;
			}
		}
	}

	@Override
	public void onSend()
	{

	}

	@Override
	public boolean addUpgrade(MachineUpgrade upgrade, boolean test)
	{
		if(upgrade instanceof MachineUpgradeEmplacementWeapon)
		{
			if(currentWeapon==null)
			{
				currentWeapon = getWeaponFromName(upgrade.getName());
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
		return currentWeapon==null&&upgrade instanceof MachineUpgradeEmplacementWeapon;
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
		ClientUtils.bindTexture(EmplacementRenderer.texture);
		GlStateManager.translate(-0.5,-3.0625,1.5);
		EmplacementRenderer.model.platformModel[0].render();
		EmplacementRenderer.model.platformModel[2].render();
		GlStateManager.translate(0.5,3.0625,-1.5);
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

	public static abstract class EmplacementWeapon
	{
		protected float pitch = 0, yaw = 0;
		protected float nextPitch = 0, nextYaw = 0;

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
			return pitch==this.pitch&&MathHelper.wrapDegrees(yaw)==this.yaw;
		}

		public float[] getAnglePrediction(Vec3d posTurret, Vec3d posTarget, Vec3d motion)
		{
			Vec3d vv = posTurret.subtract(posTarget);
			float motionXZ = MathHelper.sqrt(vv.x*vv.x+vv.z*vv.z);
			Vec3d motionVec = new Vec3d(motion.x, motion.y, motion.z).scale(2f).addVector(0, 0, 0f);
			vv = vv.add(motionVec).normalize();
			float yy = (float)((Math.atan2(vv.x, vv.z)*180D)/3.1415927410125732D);
			float pp = (float)((Math.atan2(vv.y, motionXZ)*180D));
			pp = MathHelper.clamp(pp, -90, 75);

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

		/**
		 * Used for reloading and other actions
		 * For setup delay use {@link #doSetUp(boolean)}
		 */
		public void tick()
		{

		}

		/**
		 * Used for shooting action
		 */
		public void shoot(TileEntityEmplacement te)
		{

		}

		/**
		 * @return nbt tag with weapon's saved data
		 */
		@Nonnull
		public abstract NBTTagCompound saveToNBT();

		public abstract boolean canShoot(TileEntityEmplacement te);

		/**
		 * Reads from NBT tag
		 *
		 * @param tagCompound provided nbt tag (saved in tile's NBT tag "emplacement")
		 */
		public abstract void readFromNBT(NBTTagCompound tagCompound);

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
	}

	public abstract static class EmplacementTarget
	{
		public abstract float[] getPositionVector(TileEntityEmplacement emplacement);

		public void onShot()
		{

		}

		public abstract boolean shouldContinue();

		public abstract String getName();

		public NBTTagCompound saveToNBT()
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("name", getName());
			return nbt;
		}
	}

	private static abstract class EmplacementTargetEntities extends EmplacementTarget
	{
		private final com.google.common.base.Predicate<Entity> predicate;

		public EmplacementTargetEntities(com.google.common.base.Predicate<Entity> predicate)
		{
			this.predicate = predicate;
		}

		Entity currentTarget = null;

		@Override
		public float[] getPositionVector(TileEntityEmplacement emplacement)
		{
			final BlockPos[] allBlocks = emplacement.getAllBlocks();
			final Vec3d vEmplacement = emplacement.getWeaponCenter();

			if(currentTarget!=null)
			{
				if(currentTarget.isEntityAlive()&&canEntityBeSeen(currentTarget, vEmplacement, allBlocks, 2))
					return getPosForEntityTask(emplacement, currentTarget);
			}

			Entity[] entities = emplacement.world.getEntitiesWithinAABB(Entity.class,new AxisAlignedBB(emplacement.getPos()).offset(-0.5, 0, -0.5).grow(40f).expand(0, 40, 0), predicate).stream().sorted((o1, o2) -> (int)((o1.width*o1.height)-(o2.width*o2.height))*10).toArray(Entity[]::new);

			for(Entity entity : entities)
			{
				if(canEntityBeSeen(entity, vEmplacement, allBlocks, 2))
				{
					currentTarget = entity;
					return getPosForEntityTask(emplacement, entity);
				}
			}

			return null;
		}

		private boolean canEntityBeSeen(Entity entity,Vec3d vEmplacement, BlockPos[] allBlocks, int maxBlocks)
		{
			Vec3d vEntity = entity.getPositionVector().addVector(-entity.width/2f, entity.height/2f, -entity.width/2f);

			ArrayList<RayTraceResult> hits = MultipleRayTracer.volumetricTrace(entity.world, vEmplacement, vEntity, new AxisAlignedBB(-0.00625, -0.00625, -0.00625, 0.00625, 0.00625, 0.00625), true, false, false, Collections.singletonList(entity), Arrays.asList(allBlocks)).hits;
			int h=0;
			for(RayTraceResult hit : hits)
			{
				if(hit.typeOfHit==Type.BLOCK)
					h++;

				if(h>maxBlocks)
					return false;
			}

			return true;
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
	}

	private static class EmplacementTargetMobs extends EmplacementTargetEntities
	{
		public EmplacementTargetMobs()
		{
			super(input -> input instanceof EntityLivingBase&&input instanceof IMob&&
					input.isEntityAlive());
		}

		@Override
		public String getName()
		{
			return "target_mobs";
		}
	}

	private static class EmplacementTargetShells extends EmplacementTargetEntities
	{
		public EmplacementTargetShells()
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

	private static class EmplacementTargetEntity extends EmplacementTarget
	{
		Entity entity;

		public EmplacementTargetEntity(Entity entity)
		{
			this.entity = entity;
		}

		public EmplacementTargetEntity(TileEntityEmplacement emplacement, NBTTagCompound tagCompound)
		{
			this(emplacement.world.getMinecraftServer().getEntityFromUuid(UUID.fromString(tagCompound.getString("target_uuid"))));
		}

		@Override
		public float[] getPositionVector(TileEntityEmplacement emplacement)
		{
			return getPosForEntityTask(emplacement, entity);
		}

		@Override
		public boolean shouldContinue()
		{
			return entity.isEntityAlive();
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
			compound.setString("target_uuid",entity.getUniqueID().toString());
			return compound;
		}
	}

	private static class EmplacementTargetPosition extends EmplacementTarget
	{
		int shotAmount;
		final BlockPos pos;

		public EmplacementTargetPosition(BlockPos pos, int shotAmount)
		{
			this.pos = pos;
			this.shotAmount = shotAmount;
		}

		public EmplacementTargetPosition(NBTTagCompound tagCompound)
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
			compound.setInteger("x",pos.getX());
			compound.setInteger("y",pos.getY());
			compound.setInteger("z",pos.getZ());
			compound.setInteger("shotAmount",shotAmount);
			return compound;
		}
	}

	public Vec3d getWeaponCenter()
	{
		return new Vec3d(this.getBlockPosForPos(49).up()).addVector(0.5, 0, 0.5);
	}

	private static float[] getPosForEntityTask(TileEntityEmplacement emplacement, Entity entity)
	{
		return emplacement.currentWeapon.getAnglePrediction(emplacement.getWeaponCenter(),
				entity.getPositionVector().addVector(-entity.width/2f, entity.height/2f, -entity.width/2f),
				new Vec3d(entity.motionX, entity.motionY, entity.motionZ));
	}
}
