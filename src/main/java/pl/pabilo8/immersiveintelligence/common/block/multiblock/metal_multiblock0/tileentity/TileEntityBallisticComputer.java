package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import blusunrize.immersiveengineering.common.util.Utils;
import lombok.Value;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.IIAmmoUtils;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataHandlingUtils;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeFloat;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeItemStack;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.BallisticComputer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockBallisticComputer;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIGeneric;

/**
 * Machine that converts 3D coordinates into artillery angles. Also makes good cocoa.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 15.08.2026
 * @since 28.06.2019
 */
public class TileEntityBallisticComputer extends TileEntityMultiblockIIGeneric<TileEntityBallisticComputer> implements IPlayerInteraction
{
	@SyncNBT(events = SyncEvents.TILE_CUSTOM1)
	public int progress = 0;

	public TileEntityBallisticComputer()
	{
		super(MultiblockBallisticComputer.INSTANCE);

		this.energyStorage = new FluxStorageAdvanced(BallisticComputer.energyCapacity);
		inventory = NonNullList.withSize(0, ItemStack.EMPTY);
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return false;
	}

	@Override
	protected void onUpdate()
	{

	}

	@Override
	public void receiveData(DataPacket packet, int pos)
	{
		if(energyStorage.getEnergyStored() < BallisticComputer.energyUsage)
			return;

		final BallisticResult[] result = {null};
		boolean validTarget = IIDataHandlingUtils.expectingVectorParam(packet,
				target -> result[0] = calculateTarget(packet, target),
				angles -> result[0] = IIMath.isNumberFinite(angles.x, angles.y)?
						new BallisticResult(angles.x, angles.y, -1): null
		);
		if(!validTarget||result[0]==null)
			return;

		energyStorage.extractEnergy(BallisticComputer.energyUsage, false);

		//The player can use a Data Merger anyway
		DataPacket output = packet.clone();
		output.remove('v', 'x', 'y', 'z', 'p', 's', 'm', 'f', 't', 'd');
		output.set('y', new DataTypeFloat(result[0].yaw));
		output.set('p', new DataTypeFloat(result[0].pitch));
		if(result[0].impactTime >= 0)
			output.set('t', new DataTypeInteger(result[0].impactTime));

		sendData(output, getDirection("data_output"), multiblock.getPointOfInterest("data_output"));
	}

	private BallisticResult calculateTarget(DataPacket packet, Vec3d target)
	{
		if(!IIMath.isNumberFinite(target.x, target.y, target.z))
			return null;

		BallisticParameters parameters = getBallisticParameters(packet);
		if(parameters==null)
			return null;

		double distance = Math.hypot(target.x, target.z);
		double gravity = EntityAmmoProjectile.GRAVITY*parameters.mass;
		float yaw = (float)((Math.toDegrees(Math.atan2(-target.x, target.z))+360D)%360D);
		boolean direct = IIDataHandlingUtils.optionalBoolean('d', packet).orElse(false);
		float pitch;
		int impactTime;

		if(direct)
		{
			pitch = 90-IIAmmoUtils.getDirectFireAngle(parameters.velocity, parameters.mass, target);
			impactTime = IIAmmoUtils.calculateDirectImpactTime(distance, pitch, parameters.velocity);
		}
		else
		{
			pitch = IIAmmoUtils.calculateBallisticAngle(distance, target.y, (float)parameters.velocity,
					gravity, 1D-EntityAmmoProjectile.DRAG, 0.002D);
			impactTime = IIAmmoUtils.calculateBallisticImpactTime(target.y, pitch,
					(float)parameters.velocity, gravity, 1D-EntityAmmoProjectile.DRAG);
		}

		return impactTime >= 0&&IIMath.isNumberFinite(yaw, pitch)?
				new BallisticResult(yaw, pitch, impactTime): null;
	}

	private BallisticParameters getBallisticParameters(DataPacket packet)
	{
		//Extract info from an ammo ItemStack
		if(packet.get('s') instanceof DataTypeItemStack)
		{
			ItemStack stack = ((DataTypeItemStack)packet.get('s')).value;
			if(!stack.isEmpty()&&stack.getItem() instanceof IAmmoTypeItem<?, ?> bullet)
				return validateParameters(bullet.getMass(stack), bullet.getVelocity());
		}

		//Use mass and velocity (force)
		Float mass = IIDataHandlingUtils.optionalFloat('m', packet).orElse(null);
		if(mass==null)
			return null;
		double force = IIDataHandlingUtils.optionalFloat('f', packet)
				.map(Float::doubleValue)
				.orElse((double)IIContent.itemAmmoHeavyArtillery.getVelocity());

		//Use ammo type to get velocity
		String ammoType = IIDataHandlingUtils.optionalString('t', packet).orElse(null);
		if(ammoType!=null)
		{
			IAmmoTypeItem<?, ?> bullet = AmmoRegistry.getAmmoItem(ammoType);
			if(bullet!=null)
				force = bullet.getVelocity();
		}

		return validateParameters(mass, force);
	}

	private BallisticParameters validateParameters(double mass, double velocity)
	{
		//Give to Carver what is Carver's
		if(!IIMath.isNumberFinite(mass, velocity)||mass <= 0||velocity <= 0)
			return null;
		//and to the computer what won't explode the maths
		return new BallisticParameters(mass, velocity);
	}

	@Override
	public boolean interact(EnumFacing side, EntityPlayer player, EnumHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		if(multiblock.isPointOfInterest(pos, "cocoa"))
		{
			TileEntityBallisticComputer master = master();
			if(master==null)
				return false;

			if(master.progress < 2&&heldItem.getItem()==Items.MILK_BUCKET)
			{
				player.setItemStackToSlot(hand==EnumHand.MAIN_HAND?EntityEquipmentSlot.MAINHAND: EntityEquipmentSlot.OFFHAND, new ItemStack(Items.BUCKET));
				master.progress += 1;
				updateTileForEvent(SyncEvents.TILE_CUSTOM1);
				return true;
			}
			else if(master.progress > 1&&master.progress < 6&&heldItem.getItem()==Items.DYE&&heldItem.getMetadata()==3)
			{
				heldItem.shrink(1);
				master.progress += 1;
				updateTileForEvent(SyncEvents.TILE_CUSTOM1);
				return true;
			}
			else if(master.progress > 5&&master.progress < 10&&heldItem.getItem()==Items.SUGAR)
			{
				heldItem.shrink(1);
				master.progress += 1;
				updateTileForEvent(SyncEvents.TILE_CUSTOM1);
				return true;
			}
			else if(master.progress > 9&&master.progress < 32&&Utils.compareToOreName(heldItem, "stickSteel"))
			{
				master.progress += 1;
				updateTileForEvent(SyncEvents.TILE_CUSTOM1);
				return true;
			}
			else if(master.progress >= 32&&heldItem.isEmpty())
			{
				player.addPotionEffect(new PotionEffect(MobEffects.HASTE, 360, 2));
				player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 360, 1));
				player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 10, 127));
				player.addPotionEffect(new PotionEffect(MobEffects.SATURATION, 1, 127));
				master.progress = 0;
				if(!IIUtils.hasUnlockedIIAdvancement(player, "main/secret_cocoa"))
					IIUtils.unlockIIAdvancement(player, "main/secret_cocoa");
				updateTileForEvent(SyncEvents.TILE_CUSTOM1);
				return true;
			}
		}
		return false;
	}

	/**
	 * Inner value class to store angles and impact time.
	 */
	@Value
	private static class BallisticResult
	{
		float yaw, pitch;
		int impactTime;
	}

	/**
	 * Inner value class to store mass and velocity.
	 */
	@Value
	private static class BallisticParameters
	{
		double mass;
		double velocity;
	}
}
