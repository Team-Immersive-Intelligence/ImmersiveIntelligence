package pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.Emplacement;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;
import pl.pabilo8.immersiveintelligence.api.utils.IBooleanAnimatedPartsBlock;
import pl.pabilo8.immersiveintelligence.common.ammunition_system.emplacement_weapons.EmplacementWeaponAutocannon;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.MessageBooleanAnimatedPartsSync;

import javax.annotation.Nonnull;

public class TileEntityEmplacement extends TileEntityMultiblockMetal<TileEntityEmplacement, MultiblockRecipe> implements IBooleanAnimatedPartsBlock, IDataDevice
{
	public boolean isDoorOpened = false;
	public int progress = 0;
	public EmplacementWeapon currentWeapon = new EmplacementWeaponAutocannon();

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
					currentWeapon.tick();
					//currentWeapon.reloadFrom(this);
					currentWeapon.aimAt(-5, 25);
				}
			}
		}
		else
		{
			if(progress > 0)
				progress--;
			if(currentWeapon!=null)
				currentWeapon.aimAt(-90, 0);

		}

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

		if(nbt.hasKey("currentWeapon"))
		{
			// TODO: 11.10.2020 get weapon from nbt
		}
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);
		nbt.setBoolean("isDoorOpened", this.isDoorOpened);

		nbt.setInteger("progress", this.progress);
		if(currentWeapon!=null)
			nbt.setTag("currentWeapon", currentWeapon.saveToNBT());
	}

	@Override
	public void receiveMessageFromServer(NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);
		if(message.hasKey("isDoorOpened"))
			this.isDoorOpened = message.getBoolean("isDoorOpened");
		if(message.hasKey("progress"))
			this.progress = message.getInteger("progress");
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
		if(c instanceof DataPacketTypeString)
		{
			switch(((DataPacketTypeString)c).value)
			{
				case "reload":
					break;
				case "firepos":
					break;
				case "fireentity":
					break;
				case "fire":
					break;
				case "repair":
					break;
				case "rscontrol":
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
		protected float shootDelay = 0;
		protected int reloadDelay = 0;

		public abstract String getName();

		public abstract IngredientStack[] getIngredientsRequired();

		protected float yawTurnSpeed = 2, pitchTurnSpeed = 2;

		public abstract boolean isReloaded();

		/**
		 *
		 * @param pitch destination
		 * @param yaw destination
		 * @return whether the gun is pointing to the pitch and yaw given
		 */
		public boolean isAimedAt(float pitch, float yaw)
		{
			return pitch==this.pitch&&yaw==this.yaw;
		}

		/**
		 * Rotate the gun
		 *
		 * @param pitch destination
		 * @param yaw destination
		 */
		public void aimAt(float pitch, float yaw)
		{
			nextPitch = pitch;
			nextYaw = yaw;
			float p = pitch-this.pitch;
			this.pitch += Math.signum(p)*MathHelper.clamp(Math.abs(p), 0, 1);
			float y = yaw-this.yaw;
			this.yaw += Math.signum(y)*MathHelper.clamp(Math.abs(y), 0, 1);
			this.pitch = this.pitch%180;
			this.yaw = this.yaw%360;

		}

		//Whether has ammunition in temporary (platform) storage
		public abstract boolean hasAmmunitionInTempStorage();

		public void tick()
		{

		}

		/**
		 *
		 * @param te the weapon is mounted on
		 * @return whether should perform {@link EmplacementWeapon#reloadFrom(TileEntityEmplacement)}
		 *
		 */
		public abstract boolean canReloadFrom(TileEntityEmplacement te);

		/**
		 *
		 * @param te the weapon is mounted on
		 * @return reload time (0 if no time needed, -1 if unable to reload)
		 */
		public abstract int reloadFrom(TileEntityEmplacement te);

		/**
		 * @return nbt tag with weapon's saved data
		 */
		@Nonnull
		public abstract NBTTagCompound saveToNBT();

		public abstract boolean willShoot(TileEntityEmplacement te);

		public boolean isShooting()
		{
			return shootDelay > 0;
		}

		/**
		 * Reads from NBT tag
		 * @param tagCompound provided nbt tag (saved in tile's NBT tag "emplacement")
		 */
		public abstract void readFromNBT(NBTTagCompound tagCompound);

		public void render(TileEntityEmplacement te, float partialTicks)
		{

		}
	}
}
