package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeFloat;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeItemStack;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.BallisticComputer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockBallisticComputer;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIGeneric;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

/**
 * @author Pabilo8
 * @since 28-06-2019
 */
public class TileEntityBallisticComputer extends TileEntityMultiblockIIGeneric<TileEntityBallisticComputer> implements IPlayerInteraction
{
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
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		progress = nbt.getInteger("progress");
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);
		nbt.setInteger("progress", progress);
	}

	@Override
	public void receiveData(DataPacket packet, int pos)
	{
		if(energyStorage.getEnergyStored() < BallisticComputer.energyUsage)
			return;
		energyStorage.extractEnergy(BallisticComputer.energyUsage, false);
		//TODO: 08.02.2024 is this necessary?
		packet = packet.clone();

		//No target
		if(!packet.hasAnyVariables('x', 'y', 'z'))
			return;

		float x = IIDataHandlingUtils.asFloat('x', packet);
		float y = IIDataHandlingUtils.asFloat('y', packet);
		float z = IIDataHandlingUtils.asFloat('z', packet);
		packet.removeVariables('x', 'y', 'z');

		double mass = 0;
		double force = IIContent.itemAmmoHeavyArtillery.getVelocity();

		//Get info from item
		if(packet.hasVariable('s'))
		{
			DataTypeItemStack t = packet.getVarInType(DataTypeItemStack.class, packet.getPacketVariable('s'));
			ItemStack stack = t.value;
			if(stack.getItem() instanceof IAmmoTypeItem)
			{
				IAmmoTypeItem<?, ?> bullet = (IAmmoTypeItem<?, ?>)stack.getItem();
				force = bullet.getVelocity();
				mass = bullet.getMass(stack);
			}
			packet.removeVariable('s');
		}
		//Get info from variables
		else
		{
			if(packet.hasVariable('m'))
				mass = packet.getVarInType(DataTypeInteger.class, packet.getPacketVariable('m')).value;
			if(packet.hasVariable('f'))
				force = packet.getVarInType(DataTypeInteger.class, packet.getPacketVariable('f')).value;
			if(packet.hasVariable('t'))
			{
				String bname = packet.getPacketVariable('t').toString();
				IAmmoTypeItem<?, ?> bullet = AmmoRegistry.getAmmoItem(bname);
				if(bullet!=null)
					force = bullet.getVelocity();
			}


			packet.removeVariables('m', 'f', 't');
		}

		float distance = (float)new Vec3d(0, 0, 0).distanceTo(new Vec3d(x, 0, z));

		double drag = 0.99f;
		double gravity = EntityAmmoProjectile.GRAVITY*mass;

		float yaw;
		if(x < 0&&z >= 0)
			yaw = (float)(Math.atan(Math.abs((double)x/(double)z))/Math.PI*180D);
		else if(x <= 0&&z <= 0)
			yaw = (float)(Math.atan(Math.abs((double)z/(double)x))/Math.PI*180D)+90;
		else if(z < 0)
			yaw = (float)(Math.atan(Math.abs((double)x/(double)z))/Math.PI*180D)+180;
		else
			yaw = (float)(Math.atan(Math.abs((double)z/(double)x))/Math.PI*180D)+270;

		float pitch;

		//direct
		if(packet.getVarInType(DataTypeBoolean.class, packet.getPacketVariable('d')).value)
			pitch = 90-IIAmmoUtils.getDirectFireAngle((float)force, mass, new Vec3d(x, y, z));
		else //ballistic
			pitch = IIAmmoUtils.calculateBallisticAngle(distance, y, (float)force, gravity, drag, 0.002);

		packet.setVariable('y', new DataTypeFloat(yaw));
		packet.setVariable('p', new DataTypeFloat(pitch));

		sendData(packet, facing, multiblock.getPointOfInterest("data_output"));
	}

	@Override
	protected int[] listAllPOI(MultiblockPOI poi)
	{
		switch(poi)
		{
			case ENERGY_INPUT:
				return getPOI("energy");
			case DATA_INPUT:
				return getPOI("data_input");
			case DATA_OUTPUT:
				return getPOI("data_output");
		}
		return new int[0];
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
				forceTileUpdate();
				return true;
			}
			else if(master.progress > 1&&master.progress < 6&&heldItem.getItem()==Items.DYE&&heldItem.getMetadata()==3)
			{
				heldItem.shrink(1);
				master.progress += 1;
				forceTileUpdate();
				return true;
			}
			else if(master.progress > 5&&master.progress < 10&&heldItem.getItem()==Items.SUGAR)
			{
				heldItem.shrink(1);
				master.progress += 1;
				forceTileUpdate();
				return true;
			}
			else if(master.progress > 9&&master.progress < 32&&Utils.compareToOreName(heldItem, "stickSteel"))
			{
				master.progress += 1;
				forceTileUpdate();
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
				forceTileUpdate();
				return true;
			}
		}
		return false;
	}
}
