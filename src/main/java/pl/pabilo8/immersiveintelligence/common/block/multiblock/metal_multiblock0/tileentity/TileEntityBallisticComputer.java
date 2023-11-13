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
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmo;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IDataConnector;
import pl.pabilo8.immersiveintelligence.api.data.types.*;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.BallisticComputer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockBallisticComputer;
import pl.pabilo8.immersiveintelligence.common.entity.bullet.EntityBullet;
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

		DataPacket new_packet = packet.clone();
		if(new_packet.hasAnyVariables('x', 'y', 'z'))
		{
			float x = packet.getVarInType(IDataTypeNumeric.class, new_packet.getPacketVariable('x')).floatValue();
			float y = packet.getVarInType(IDataTypeNumeric.class, new_packet.getPacketVariable('y')).floatValue();
			float z = packet.getVarInType(IDataTypeNumeric.class, new_packet.getPacketVariable('z')).floatValue();
			new_packet.removeVariables('x', 'y', 'z');

			float mass = 0;
			double force = IIContent.itemAmmoArtillery.getDefaultVelocity();

			if(new_packet.hasVariable('s'))
			{
				DataTypeItemStack t = packet.getVarInType(DataTypeItemStack.class, new_packet.getPacketVariable('s'));
				ItemStack stack = t.value;
				if(stack.getItem() instanceof IAmmo)
				{
					IAmmo bullet = (IAmmo)stack.getItem();
					force = bullet.getDefaultVelocity();
					mass = bullet.getMass(stack);
				}
				new_packet.removeVariable('s');
			}
			else
			{
				if(new_packet.hasVariable('m'))
					mass = packet.getVarInType(DataTypeInteger.class, new_packet.getPacketVariable('m')).value;
				if(new_packet.hasVariable('f'))
					force = packet.getVarInType(DataTypeInteger.class, new_packet.getPacketVariable('f')).value;
				if(new_packet.hasVariable('t'))
				{
					String bname = new_packet.getPacketVariable('t').valueToString();
					IAmmo bullet = AmmoRegistry.INSTANCE.getBulletItem(bname);
					if(bullet!=null)
						force = bullet.getDefaultVelocity();
				}


				new_packet.removeVariables('m', 'f', 't');
			}

			float distance = (float)new Vec3d(0, 0, 0).distanceTo(new Vec3d(x, 0, z));

			double drag = 0.99f;
			double gravity = EntityBullet.GRAVITY*mass;

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
				pitch = 90-IIUtils.getDirectFireAngle((float)force, mass, new Vec3d(x, y, z));
			else //ballistic
				pitch = IIUtils.calculateBallisticAngle(distance, y, (float)force, gravity, drag, 0.002);

			new_packet.setVariable('y', new DataTypeFloat(yaw));
			new_packet.setVariable('p', new DataTypeFloat(pitch));


			IDataConnector conn = IIUtils.findConnectorFacing(
					getBlockPosForPos(multiblock.getPointOfInterest("data_output")),
					world, mirrored?facing.rotateYCCW(): facing.rotateY());
			if(conn!=null)
				conn.sendPacket(new_packet);
		}
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
