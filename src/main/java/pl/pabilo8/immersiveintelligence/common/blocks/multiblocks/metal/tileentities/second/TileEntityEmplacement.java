package pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.Emplacement;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;
import pl.pabilo8.immersiveintelligence.api.utils.IBooleanAnimatedPartsBlock;
import pl.pabilo8.immersiveintelligence.common.ammunition_system.emplacement_weapons.EmplacementWeaponAutocannon;
import pl.pabilo8.immersiveintelligence.common.ammunition_system.emplacement_weapons.EmplacementWeaponCPDS;
import pl.pabilo8.immersiveintelligence.common.ammunition_system.emplacement_weapons.EmplacementWeaponHeavyChemthrower;
import pl.pabilo8.immersiveintelligence.common.ammunition_system.emplacement_weapons.EmplacementWeaponInfraredObserver;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.MessageBooleanAnimatedPartsSync;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;

public class TileEntityEmplacement extends TileEntityMultiblockMetal<TileEntityEmplacement, MultiblockRecipe> implements IBooleanAnimatedPartsBlock, IDataDevice
{
	public static final HashMap<String, Function<String, EmplacementWeapon>> weaponRegistry = new HashMap<>();

	static
	{
		weaponRegistry.put("autocannon", s -> new EmplacementWeaponAutocannon());
		weaponRegistry.put("infrared_observer", s -> new EmplacementWeaponInfraredObserver());
		weaponRegistry.put("cpds", s -> new EmplacementWeaponCPDS());
		weaponRegistry.put("heavy_chemthrower", s -> new EmplacementWeaponHeavyChemthrower());
	}

	public boolean isDoorOpened = false;
	public int progress = 0;
	public EmplacementWeapon currentWeapon = null;
	BlockPos[] allBlocks = null;
	public boolean isShooting = false;

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

		/*if(world.isRemote)
		{
			handleSounds();
		}*/

		if(!world.isRemote&&(isDoorOpened^world.isBlockPowered(getBlockPosForPos(getRedstonePos()[0]))))
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
						Optional<Entity> first = world.getEntitiesInAABBexcluding(null, new AxisAlignedBB(getPos()).grow(5f).expand(0, 40, 0), input -> input instanceof EntityPlayer&&!input.isDead).stream().findFirst();
						if(first.isPresent())
						{
							float[] target = currentWeapon.getAnglePrediction(new Vec3d(getBlockPosForPos(49).up(2)).subtract(-0.5, 0, -0.5),
									first.get().getPositionVector().addVector(first.get().width/2f,first.get().height/2f,first.get().width/2f),
									new Vec3d(first.get().motionX, first.get().motionY, first.get().motionZ)
							);
							currentWeapon.aimAt(target[0], target[1]);

							if(currentWeapon.isAimedAt(target[0], target[1]))
							{
								if(currentWeapon.canShoot(this))
								{
									isShooting = true;
									currentWeapon.shoot(this);
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
		return null;
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
		this.isDoorOpened = nbt.getBoolean("isDoorOpened");

		if(!isDummy()&&nbt.hasKey("currentWeapon"))
		{
			currentWeapon = getWeaponFromName(nbt.getString("weaponName"));
			if(currentWeapon!=null)
				currentWeapon.readFromNBT(nbt.getCompoundTag("currentWeapon"));
		}
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);
		nbt.setBoolean("isDoorOpened", this.isDoorOpened);

		nbt.setInteger("progress", this.progress);

		if(!isDummy()&&currentWeapon!=null)
		{
			nbt.setString("weaponName", currentWeapon.getName());
			nbt.setTag("currentWeapon", currentWeapon.saveToNBT());
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
		}
	}

	private EmplacementWeapon getWeaponFromName(String weaponName)
	{
		if(weaponName==null)
			return null;
		if(weaponRegistry.containsKey(weaponName))
			return weaponRegistry.get(weaponName).apply(weaponName);
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
		if(c instanceof DataPacketTypeString)
		{
			switch(((DataPacketTypeString)c).value)
			{
				case "reload":
				case "rscontrol":
				case "repair":
				case "fire":
				case "fireentity":
				case "firepos":
					break;
				case "weapon":
				{
					// TODO: 25.02.2021 REMOVE CHEATS
					TileEntityEmplacement master = master();
					if(master==null)
						break;
					master.currentWeapon = getWeaponFromName(w.valueToString());
					if(master.currentWeapon!=null)
					{
						master.markDirty();
						master.markBlockForUpdate(master.getPos(), null);
					}
				}
				break;
			}
		}
	}

	@Override
	public void onSend()
	{

	}

	public static abstract class EmplacementWeapon
	{
		protected float pitch = 0, yaw = 0;
		protected float nextPitch = 0, nextYaw = 0;

		/**
		 * @return name of the emplacement, must be the same as the name in the weapon registry
		 */
		public abstract String getName();

		/**
		 * @return ingredients required for constructing weapon in the emplacement
		 */
		public abstract IngredientStack[] getIngredientsRequired();

		protected float yawTurnSpeed = 2, pitchTurnSpeed = 2;

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
			// TODO: 28.02.2021 find out how to predict angles
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

			/*
			this.yaw = MathHelper.wrapDegrees(yaw+(Math.signum(y)*MathHelper.clamp(Math.abs(y), 0, this.yawTurnSpeed)));
			 */

			nextPitch = pitch;
			nextYaw = MathHelper.wrapDegrees(yaw);
			float p = pitch-this.pitch;
			this.pitch += Math.signum(p)*MathHelper.clamp(Math.abs(p), 0, this.pitchTurnSpeed);
			float y = MathHelper.wrapDegrees(360+nextYaw-this.yaw);
			if(Math.abs(y) < 0.01)
				this.yaw = this.nextYaw;
			else
				this.yaw = MathHelper.wrapDegrees(this.yaw+(Math.signum(y)*MathHelper.clamp(Math.abs(y), 0, this.yawTurnSpeed)));
			this.pitch = this.pitch%180;
			//this.yaw = this.yaw%360;

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

		public void render(TileEntityEmplacement te, float partialTicks)
		{

		}
	}
}
