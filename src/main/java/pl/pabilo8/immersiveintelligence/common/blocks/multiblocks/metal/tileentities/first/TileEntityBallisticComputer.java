package pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first;

import blusunrize.immersiveengineering.api.crafting.IMultiblockRecipe;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedCollisionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedSelectionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.BallisticComputer;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IDataConnector;
import pl.pabilo8.immersiveintelligence.api.data.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeInteger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8
 * @since 28-06-2019
 */
public class TileEntityBallisticComputer extends TileEntityMultiblockMetal<TileEntityBallisticComputer, IMultiblockRecipe> implements IDataDevice, IAdvancedCollisionBounds, IAdvancedSelectionBounds, IPlayerInteraction
{
	public int progress = 0;

	public TileEntityBallisticComputer()
	{
		super(MultiblockBallisticComputer.instance, new int[]{2, 2, 2}, BallisticComputer.energyCapacity, true);
	}

	@Override
	public float[] getBlockBounds()
	{
		return new float[]{0, 0, 0, 0, 0, 0};
	}

	@Override
	public int[] getEnergyPos()
	{
		return new int[]{6};
	}

	@Override
	public int[] getRedstonePos()
	{
		return new int[]{};
	}

	@Override
	public boolean isInWorldProcessingMachine()
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
	public void onProcessFinish(MultiblockProcess<IMultiblockRecipe> process)
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
	public float getMinProcessDistance(MultiblockProcess<IMultiblockRecipe> process)
	{
		return 0;
	}

	@Override
	public NonNullList<ItemStack> getInventory()
	{
		return new NonNullList<ItemStack>()
		{
		};
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
	public int[] getOutputSlots()
	{
		return new int[]{1};
	}

	@Override
	public int[] getOutputTanks()
	{
		return new int[]{};
	}

	@Override
	public boolean additionalCanProcessCheck(MultiblockProcess<IMultiblockRecipe> process)
	{
		return false;
	}

	@Override
	public IFluidTank[] getInternalTanks()
	{
		return new IFluidTank[]{};
	}

	@Override
	protected IFluidTank[] getAccessibleFluidTanks(EnumFacing side)
	{
		return new FluidTank[0];
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
	public void doGraphicalUpdates(int slot)
	{
		this.markDirty();
		this.markContainingBlockForUpdate(null);
	}

	@Override
	public IMultiblockRecipe findRecipeForInsertion(ItemStack inserting)
	{
		return null;
	}

	@Override
	protected IMultiblockRecipe readRecipeFromNBT(NBTTagCompound tag)
	{
		return null;
	}

	@Override
	public void onSend()
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
	public void onReceive(DataPacket packet, EnumFacing side)
	{
		TileEntityBallisticComputer master = master();
		if(this.pos==2&&master!=null&&master.energyStorage.getEnergyStored() > BallisticComputer.energyUsage)
		{
			master.energyStorage.extractEnergy(BallisticComputer.energyUsage, false);

			DataPacket new_packet = packet.clone();

			if(new_packet.getPacketVariable('x') instanceof DataPacketTypeInteger&&
					new_packet.getPacketVariable('y') instanceof DataPacketTypeInteger&&
					new_packet.getPacketVariable('z') instanceof DataPacketTypeInteger&&
					new_packet.getPacketVariable('m') instanceof DataPacketTypeInteger)
			{
				float x = ((DataPacketTypeInteger)new_packet.getPacketVariable('x')).value;
				float y = ((DataPacketTypeInteger)new_packet.getPacketVariable('y')).value;
				float z = ((DataPacketTypeInteger)new_packet.getPacketVariable('z')).value;

				float mass = ((DataPacketTypeInteger)new_packet.getPacketVariable('m')).value;

				new_packet.removeVariable('x');
				new_packet.removeVariable('y');
				new_packet.removeVariable('z');
				new_packet.removeVariable('m');

				float distance = (float)new Vec3d(0, 0, 0).distanceTo(new Vec3d(x, 0, z));

				//Yaw calculation method borrowed from Pneumaticcraft (https://github.com/TeamPneumatic/pnc-repressurized/blob/master/src/main/java/me/desht/pneumaticcraft/common/tileentity/TileEntityAirCannon.java)
				//Huge thanks to desht and MineMaarten for this amazing code!

				double drag = 0.99F;
				double gravity = 0.02F*mass;

				float yaw;
				if(x < 0&&z >= 0)
					yaw = (float)(Math.atan(Math.abs((double)x/(double)z))/Math.PI*180D);
				else if(x <= 0&&z <= 0)
					yaw = (float)(Math.atan(Math.abs((double)z/(double)x))/Math.PI*180D)+90;
				else if(x >= 0&&z < 0)
					yaw = (float)(Math.atan(Math.abs((double)x/(double)z))/Math.PI*180D)+180;
				else
					yaw = (float)(Math.atan(Math.abs((double)z/(double)x))/Math.PI*180D)+270;

				float pitch = pl.pabilo8.immersiveintelligence.api.Utils.calculateBallisticAngle(distance, y, 6f, gravity, drag);

				new_packet.setVariable('y', new DataPacketTypeInteger(Math.round(yaw)));
				new_packet.setVariable('p', new DataPacketTypeInteger(Math.round(pitch)));

				IDataConnector conn = pl.pabilo8.immersiveintelligence.api.Utils.findConnectorFacing(getTileForPos(3).getPos(), world, mirrored?facing.rotateYCCW(): facing.rotateY());
				if(conn!=null)
				{
					conn.sendPacket(new_packet);
				}
			}
		}
	}

	@Override
	public List<AxisAlignedBB> getAdvancedSelectionBounds()
	{
		List list = new ArrayList<AxisAlignedBB>();

		if(pos==4)
		{
			switch(facing)
			{
				case NORTH:
					list.add(new AxisAlignedBB(0.125, 0.125, 0, 0.875, 0.75, 0.0625).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					break;
				case SOUTH:
					list.add(new AxisAlignedBB(0.125, 0.125, 0.9375, 0.875, 0.75, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					break;
				case EAST:
					list.add(new AxisAlignedBB(0.9375, 0.125, 0.125, 1, 0.75, 0.875).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					break;
				case WEST:
					list.add(new AxisAlignedBB(0, 0.125, 0.125, 0.0625, 0.75, 0.875).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					break;
			}
		}
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
	public List<AxisAlignedBB> getAdvancedColisionBounds()
	{
		return getAdvancedSelectionBounds();
	}

	@Override
	public boolean interact(EnumFacing side, EntityPlayer player, EnumHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		if(pos==1||pos==5)
		{
			TileEntityBallisticComputer master = master();
			if(master==null)
				return false;

			if(master.progress < 2&&heldItem.getItem()==Items.MILK_BUCKET)
			{
				player.setItemStackToSlot(hand==EnumHand.MAIN_HAND?EntityEquipmentSlot.MAINHAND: EntityEquipmentSlot.OFFHAND, new ItemStack(Items.BUCKET));
				master.progress += 1;
				master.doGraphicalUpdates(0);
				return true;
			}
			else if(master.progress > 1&&master.progress < 6&&heldItem.getItem()==Items.DYE&&heldItem.getMetadata()==3)
			{
				heldItem.shrink(1);
				master.progress += 1;
				master.doGraphicalUpdates(0);
				return true;
			}
			else if(master.progress > 5&&master.progress < 10&&heldItem.getItem()==Items.SUGAR)
			{
				heldItem.shrink(1);
				master.progress += 1;
				master.doGraphicalUpdates(0);
				return true;
			}
			else if(master.progress > 9&&master.progress < 32&&Utils.compareToOreName(heldItem, "stickSteel"))
			{
				master.progress += 1;
				master.doGraphicalUpdates(0);
				return true;
			}
			else if(master.progress >= 32&&heldItem.isEmpty())
			{
				player.addPotionEffect(new PotionEffect(MobEffects.HASTE, 360, 2));
				player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 360, 1));
				player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 10, 127));
				player.addPotionEffect(new PotionEffect(MobEffects.SATURATION, 1, 127));
				master.progress = 0;
				if(!pl.pabilo8.immersiveintelligence.api.Utils.hasUnlockedIIAdvancement(player, "main/secret_cocoa"))
					pl.pabilo8.immersiveintelligence.api.Utils.unlockIIAdvancement(player, "main/secret_cocoa");
				master.doGraphicalUpdates(0);
				return true;
			}
		}
		return false;
	}
}
