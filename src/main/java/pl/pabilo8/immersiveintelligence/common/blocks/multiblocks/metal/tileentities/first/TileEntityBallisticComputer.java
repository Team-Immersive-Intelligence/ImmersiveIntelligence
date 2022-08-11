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
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry;
import pl.pabilo8.immersiveintelligence.api.bullets.IBullet;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IDataConnector;
import pl.pabilo8.immersiveintelligence.api.data.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeFloat;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeItemStack;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataTypeNumeric;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityBullet;

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
		return new float[]{0, 0, 0, 1, 1, 1};
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
		if(this.pos!=2)
			return;

		TileEntityBallisticComputer master = master();
		TileEntityBallisticComputer output = getTileForPos(3);

		if(master==null||output==null)
			return;

		if(master.energyStorage.getEnergyStored() < BallisticComputer.energyUsage)
			return;
		master.energyStorage.extractEnergy(BallisticComputer.energyUsage, false);


		DataPacket new_packet = packet.clone();
		if(new_packet.hasAnyVariables('x','y','z'))
		{
			float x = packet.getVarInType(IDataTypeNumeric.class,new_packet.getPacketVariable('x')).floatValue();
			float y = packet.getVarInType(IDataTypeNumeric.class,new_packet.getPacketVariable('y')).floatValue();
			float z = packet.getVarInType(IDataTypeNumeric.class,new_packet.getPacketVariable('z')).floatValue();
			new_packet.removeVariables('x','y','z');

			float mass = 0;
			double force = IIContent.itemAmmoArtillery.getDefaultVelocity();

			if(new_packet.hasVariable('s'))
			{
				DataTypeItemStack t = packet.getVarInType(DataTypeItemStack.class, new_packet.getPacketVariable('s'));
				ItemStack stack = t.value;
				if(stack.getItem() instanceof IBullet)
				{
					IBullet bullet = (IBullet)stack.getItem();
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
					IBullet bullet = BulletRegistry.INSTANCE.getBulletItem(bname);
					if(bullet!=null)
						force = bullet.getDefaultVelocity();
				}


				new_packet.removeVariables('m','f','t');
			}

			float distance = (float)new Vec3d(0, 0, 0).distanceTo(new Vec3d(x, 0, z));

			double drag = 1f-EntityBullet.DRAG;
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

			float pitch = pl.pabilo8.immersiveintelligence.api.Utils.calculateBallisticAngle(distance, y, (float)force, gravity, drag, 0.002);

			new_packet.setVariable('y', new DataTypeFloat(yaw));
			new_packet.setVariable('p', new DataTypeFloat(pitch));


			IDataConnector conn = pl.pabilo8.immersiveintelligence.api.Utils.findConnectorFacing(output.getPos(), world, mirrored?facing.rotateYCCW(): facing.rotateY());
			if(conn!=null)
			{
				conn.sendPacket(new_packet);
			}
		}
	}

	@Override
	public List<AxisAlignedBB> getAdvancedSelectionBounds()
	{
		ArrayList<AxisAlignedBB> list = new ArrayList<>();

		switch(pos)
		{
			case 4:
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
				break;
			case 1:
				list.add(new AxisAlignedBB(0, 0, 0, 1, 0.5, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
				break;
			case 7:
			{
				Vec3d vv = new Vec3d(facing.getDirectionVec()).scale(0.25);
				Vec3d vvv = new Vec3d((mirrored?facing.rotateY(): facing.rotateYCCW()).getDirectionVec());
				list.add(new AxisAlignedBB(0, 0, 0, 1, 0.5, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
				list.add(new AxisAlignedBB(0.5-0.125f, 0.5, 0.5-0.125f, 0.5+0.125f, 1-3*0.0625f, 0.5+0.125f)
						.expand(vvv.x, vvv.y, vvv.z)
						.expand(-vvv.x*0.1875f, -vvv.y*0.1875f, -vvv.z*0.1875f)
						.offset(vv)
						.offset(getPos().getX(), getPos().getY(), getPos().getZ())
				);
				list.add(new AxisAlignedBB(0.5-0.125f, 0.5, 0.5-0.125f, 0.5+0.125f, 1-3*0.0625f, 0.5+0.125f)
						.expand(vvv.x, vvv.y, vvv.z)
						.expand(-vvv.x*0.1875f, -vvv.y*0.1875f, -vvv.z*0.1875f)
						.offset(vv.scale(-1))
						.offset(getPos().getX(), getPos().getY(), getPos().getZ())
				);
			}
			break;
			case 5:
			{
				Vec3d vv = new Vec3d(facing.getDirectionVec()).scale(0.25);
				Vec3d vvv = new Vec3d((mirrored?facing.rotateYCCW(): facing.rotateY()).getDirectionVec()).scale(0.125);
				list.add(new AxisAlignedBB(0.25f, -0.125, 0.25f, 0.75, 0.5-0.125, 0.75f)
						//.expand(vvv.x, vvv.y, vvv.z)
						//.expand(-vvv.x*0.1875f, -vvv.y*0.1875f, -vvv.z*0.1875f)
						.offset(vv)
						.offset(vvv)
						.expand(vvv.x, vvv.y, vvv.z)
						.offset(getPos().getX(), getPos().getY(), getPos().getZ())
				);
			}
			break;

			default:
				list.add(new AxisAlignedBB(0, 0, 0, 1, 1, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
				break;
		}

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
