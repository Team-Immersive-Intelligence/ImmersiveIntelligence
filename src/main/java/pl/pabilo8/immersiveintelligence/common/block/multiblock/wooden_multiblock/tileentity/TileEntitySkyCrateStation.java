package pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity;

import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.crafting.IMultiblockRecipe;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedCollisionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedSelectionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityConveyorBelt;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.SkyCrateStation;
import pl.pabilo8.immersiveintelligence.api.rotary.*;
import pl.pabilo8.immersiveintelligence.api.utils.ISkyCrateConnector;
import pl.pabilo8.immersiveintelligence.api.utils.MinecartBlockHelper;
import pl.pabilo8.immersiveintelligence.api.utils.tools.ISkycrateMount;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.multiblock.MultiblockSkyCrateStation;
import pl.pabilo8.immersiveintelligence.common.entity.EntitySkyCrate;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageRotaryPowerSync;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockConnectable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static blusunrize.immersiveengineering.api.energy.wires.WireType.STRUCTURE_CATEGORY;

/**
 * @author Pabilo8
 * @since 28-06-2019
 */
public class TileEntitySkyCrateStation extends TileEntityMultiblockConnectable<TileEntitySkyCrateStation, IMultiblockRecipe> implements IAdvancedCollisionBounds, IAdvancedSelectionBounds, ISkyCrateConnector, IPlayerInteraction, IGuiTile, IRotationalEnergyBlock
{
	//none, crate, crate in, crate out, crate load, crate unload
	public int animation = 0;
	public float progress = 0;
	public RotaryStorage rotation = new RotaryStorage(0, 0)
	{
		@Override
		public RotationSide getSide(@Nullable EnumFacing facing)
		{
			return facing==getFacing().rotateYCCW()?RotationSide.INPUT: RotationSide.NONE;
		}
	};

	//Crate, Mount, Banner
	NonNullList<ItemStack> inventory = NonNullList.withSize(6, ItemStack.EMPTY);

	IItemHandler insertionHandler = new IEInventoryHandler(1, this, 3, true, false);

	public TileEntitySkyCrateStation()
	{
		super(MultiblockSkyCrateStation.INSTANCE, new int[]{3, 3, 3}, 0, true);
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		if(!isDummy())
		{
			if(!descPacket&&nbt.hasKey("inventory"))
				inventory = Utils.readInventory(nbt.getTagList("inventory", 10), 6);

			animation = nbt.getInteger("animation");
			progress = nbt.getFloat("progress");
			if(nbt.hasKey("rotation"))
				rotation.fromNBT(nbt.getCompoundTag("rotation"));
		}
	}

	@Override
	public void onChunkUnload()
	{
		super.onChunkUnload();
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);
		if(!isDummy())
		{
			if(!descPacket)
				nbt.setTag("inventory", Utils.writeInventory(getInventory()));

			nbt.setInteger("animation", animation);
			nbt.setFloat("progress", progress);
			nbt.setTag("rotation", rotation.toNBT());
		}
	}

	@Override
	public void receiveMessageFromServer(NBTTagCompound message)
	{
		if(message.hasKey("inventory"))
			inventory = Utils.readInventory(message.getTagList("inventory", 10), 6);

		if(message.hasKey("animation"))
			animation = message.getInteger("animation");
		if(message.hasKey("progress"))
			progress = message.getFloat("progress");
		if(message.hasKey("rotation"))
			rotation.fromNBT(message.getCompoundTag("rotation"));

		super.receiveMessageFromServer(message);
	}

	@Override
	public void update()
	{
		super.update();

		if(!isDummy()&&!world.isRemote)
		{
			handleRotation();

			if(animation==0)
			{
				if(!inventory.get(3).isEmpty())
				{
					animation = 2;
					progress = 0;
					sendUpdate(1);
					sendUpdate(2);
				}
			}
			else if(animation==1)
			{
				if(getInventory().get(4).isEmpty()&&world.getTotalWorldTime()%4==0)
				{
					TileEntity te = world.getTileEntity(getBlockPosForPos(7).offset(facing));

					if(te!=null&&te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite()))
					{
						IItemHandler cap = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite());
						for(int i = 0; i < cap.getSlots(); i += 1)
						{
							if(cap.getStackInSlot(i).getItem() instanceof ISkycrateMount)
							{
								getInventory().set(4, cap.extractItem(i, 1, false));
								break;
							}
						}
					}
				}
				if(world.getRedstonePower(getBlockPosForPos(8).offset(mirrored?this.facing.rotateYCCW(): this.facing.rotateY()), (mirrored?this.facing.rotateY(): this.facing.rotateYCCW())) > 0)
				{
					animation = 3;
					progress = 0;
					sendUpdate(1);
					sendUpdate(2);
				}
				else if(!inventory.get(3).isEmpty()&&!inventory.get(4).isEmpty())
				{
					animation = 5;
					progress = 0;
					sendUpdate(1);
					sendUpdate(2);
				}
			}
		}

		if(!isDummy())
		{
			if(animation > 1)
			{
				if(progress < getAnimationLength())
					progress += getEffectiveEnergy()*RotaryUtils.getGearEffectiveness(getInventory(), getEfficiencyMultiplier(), 3);
				else
				{
					switch(animation)
					{
						case 2:
						{
							animation = 1;
							progress = 0;
						}
						break;
						case 3:
						{
							animation = 0;
							if(!world.isRemote)
							{
								ItemStack crate = Utils.insertStackIntoInventory(world.getTileEntity(getBlockPosForPos(9).offset((mirrored?this.facing.rotateY(): this.facing.rotateYCCW()))), inventory.get(3), mirrored?this.facing.rotateYCCW(): this.facing.rotateY());
								if(!crate.isEmpty())
									Utils.dropStackAtPos(world, getBlockPosForPos(9).offset(facing), crate);
								inventory.set(3, ItemStack.EMPTY);
							}
							progress = 0;
						}
						break;
						case 4:
						{
							if(!world.isRemote)
							{
								TileEntity te = world.getTileEntity(getBlockPosForPos(7).offset(facing));
								inventory.set(4, Utils.insertStackIntoInventory(te, inventory.get(4), facing.getOpposite()));
								if(!inventory.get(4).isEmpty())
									Utils.dropStackAtPos(world, getBlockPosForPos(7).offset(facing), inventory.get(4), facing);
								inventory.set(4, ItemStack.EMPTY);

								doGraphicalUpdates(1);
								doGraphicalUpdates(2);

							}

							animation = 3;
							progress = 0;
						}
						break;
						case 5:
						{
							out:
							if(!world.isRemote)
							{

								Set<Connection> conns = ImmersiveNetHandler.INSTANCE.getConnections(getWorld(), getBlockPosForPos(getConnectionPos()[0]));
								if(conns!=null&&conns.size() > 0)
								{
									for(Connection c : conns)
									{
										if(c==null)
											break out;
										animation = 0;
										EntitySkyCrate s = new EntitySkyCrate(world, c, inventory.get(4).copy(), inventory.get(3).copy(), getBlockPosForPos(getConnectionPos()[0]));
										world.spawnEntity(s);
										inventory.set(3, ItemStack.EMPTY);
										inventory.set(4, ItemStack.EMPTY);
										progress = 0;
										sendUpdate(1);
										sendUpdate(2);
										break;
									}
									progress = getAnimationLength();
								}
							}
						}
						break;
					}

				}
			}
		}
	}

	private void handleRotation()
	{
		boolean b = false;
		if(rotation.getRotationSpeed() > SkyCrateStation.rpmBreakingMax||rotation.getTorque() > SkyCrateStation.torqueBreakingMax)
		{
			selfDestruct();
		}

		if(world.getTileEntity(getBlockPosForPos(6).offset((mirrored?this.facing.rotateY(): this.facing.rotateYCCW())))!=null)
		{
			TileEntity te = world.getTileEntity(getBlockPosForPos(6).offset((mirrored?this.facing.rotateY(): this.facing.rotateYCCW())));
			if(te.hasCapability(CapabilityRotaryEnergy.ROTARY_ENERGY, mirrored?this.facing.rotateYCCW(): this.facing.rotateY()))
			{
				IRotaryEnergy cap = te.getCapability(CapabilityRotaryEnergy.ROTARY_ENERGY, mirrored?this.facing.rotateYCCW(): this.facing.rotateY());
				if(rotation.handleRotation(cap, mirrored?this.facing.rotateYCCW(): this.facing.rotateY()))
				{
					IIPacketHandler.INSTANCE.sendToAllAround(new MessageRotaryPowerSync(rotation, 0, master().getPos()), IIPacketHandler.targetPointFromTile(master(), 24));
				}
			}
			else
				b = true;

		}
		else
			b = true;

		// TODO: 26.12.2021 investigate
		if((rotation.getTorque() > 0||rotation.getRotationSpeed() > 0))
		{
			if(b)
			{
				rotation.grow(0, 0, 0.98f);
			}
			IIPacketHandler.INSTANCE.sendToAllAround(new MessageRotaryPowerSync(rotation, 0, master().getPos()), IIPacketHandler.targetPointFromTile(master(), 24));
		}
	}

	public float getEffectiveEnergy()
	{
		float eff_rpm = (rotation.getRotationSpeed() > SkyCrateStation.rpmMin?Math.min(rotation.getRotationSpeed(), SkyCrateStation.rpmEffectiveMax): 0)/SkyCrateStation.rpmEffectiveMax;
		float eff_torque = (rotation.getTorque() > SkyCrateStation.torqueMin?Math.min(rotation.getTorque(), SkyCrateStation.torqueEffectiveMax): 0)/SkyCrateStation.torqueEffectiveMax;
		return eff_rpm*eff_torque;
	}

	private void selfDestruct()
	{
		world.createExplosion(null, getPos().getX(), getPos().getY(), getPos().getZ(), 4, true);
	}

	@Override
	public void onEntityCollision(World world, Entity entity)
	{
		super.onEntityCollision(world, entity);
		//
	}

	@Override
	public float[] getBlockBounds()
	{
		return new float[]{0, 0, 0, 1, 1, 1};
	}

	@Override
	public int[] getEnergyPos()
	{
		return new int[]{};
	}

	@Override
	public int[] getRedstonePos()
	{
		return new int[]{};
	}

	@Override
	public int[] getConnectionPos()

	{
		return new int[]{19};
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
		return master().inventory;
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		if(slot < 3)
			return stack.getItem() instanceof IMotorGear;
		if(slot==3)
		{
			return MinecartBlockHelper.blocks.keySet().stream().anyMatch(itemStackPredicate -> itemStackPredicate.test(stack));
		}
		return false;
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return 1;
	}

	@Override
	public int[] getOutputSlots()
	{
		return new int[]{};
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

	public void sendUpdate(int id)
	{
		EasyNBT tag = EasyNBT.newNBT();

		switch(id)
		{
			case 0:
				tag.withTag("inventory", Utils.writeInventory(inventory));
				break;
			case 1:
				tag.withInt("animation", animation).withFloat("progress", progress);
				break;
			case 2:
				tag.withTag("rotation", rotation.toNBT()).withTag("inventory", Utils.writeInventory(inventory));
				break;
		}

		if(tag.size() > 0)
			IIPacketHandler.sendToClient(this, new MessageIITileSync(this, tag));
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
	public List<AxisAlignedBB> getAdvancedSelectionBounds()
	{
		List<AxisAlignedBB> list = new ArrayList<>();

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
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if(pos==6&&capability==CapabilityRotaryEnergy.ROTARY_ENERGY&&facing==(mirrored?this.facing.rotateY(): this.facing.rotateYCCW()))
			return true;
		if(pos==2&&capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY&&facing==(mirrored?this.facing.rotateYCCW(): this.facing.rotateY()))
			return true;
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		TileEntitySkyCrateStation master = master();
		if(master==null)
			return super.getCapability(capability, facing);

		if(pos==6&&capability==CapabilityRotaryEnergy.ROTARY_ENERGY&&facing==(mirrored?this.facing.rotateY(): this.facing.rotateYCCW()))
			return (T)rotation;
		if(pos==2&&master.animation==0&&capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY&&facing==(mirrored?this.facing.rotateYCCW(): this.facing.rotateY()))
			return (T)insertionHandler;
		return super.getCapability(capability, facing);
	}

	@Override
	protected boolean canTakeMV()
	{
		return false;
	}

	@Override
	protected boolean canTakeLV()
	{
		return false;
	}

	@Override
	protected boolean canTakeHV()
	{
		return false;
	}

	@Override
	public boolean canConnectCable(WireType cableType, TargetingInfo target, Vec3i offset)
	{
		if(!STRUCTURE_CATEGORY.equals(cableType.getCategory()))
			return false;
		return limitType==null;
	}

	@Override
	public Vec3d getConnectionOffset(Connection con)
	{
		return new Vec3d(.5, .625, .5);
	}

	@Override
	public Set<BlockPos> getIgnored(IImmersiveConnectable other)
	{
		return ImmutableSet.of(getPos(), getPos().offset(facing.getOpposite(), 1));
	}

	@Override
	public boolean isEnergyOutput()
	{
		return false;
	}

	@Override
	public void connectCable(WireType cableType, TargetingInfo target, IImmersiveConnectable other)
	{
		super.connectCable(cableType, target, other);
		if(!world.isRemote)
		{
			if(!(other instanceof ISkyCrateConnector))
			{
				Set<Connection> conns = ImmersiveNetHandler.INSTANCE.getConnections(world, getPos());
				if(conns!=null)
					for(Connection conn : conns)
						ImmersiveNetHandler.INSTANCE.removeConnectionAndDrop(conn, world, getBlockPosForPos(10));
			}
		}
	}

	@Override
	public boolean interact(EnumFacing side, EntityPlayer player, EnumHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		TileEntitySkyCrateStation master = master();
		if(pos==20&&master!=null&&!world.isRemote)
		{
			if(master.getInventory().get(5).isEmpty()&&heldItem.getItem()==Items.BANNER)
			{
				master.getInventory().set(5, heldItem.copy());
				master.getInventory().get(5).setCount(1);
				heldItem.shrink(1);
				master.sendUpdate(0);
				return true;
			}
			else if(!master.getInventory().get(5).isEmpty()&&Utils.isWirecutter(heldItem))
			{
				player.inventory.addItemStackToInventory(master.getInventory().get(5).copy());
				master.getInventory().set(5, ItemStack.EMPTY);
				master.sendUpdate(0);
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean canOpenGui()
	{
		return true;
	}

	@Override
	public int getGuiID()
	{
		return IIGuiList.GUI_SKYCRATE_STATION.ordinal();
	}

	@Nullable
	@Override
	public TileEntity getGuiMaster()
	{
		return master();
	}

	public float getEfficiencyMultiplier()
	{
		return 1.0f;
	}

	public int getAnimationLength()
	{
		switch(animation)
		{
			case 2:
				return SkyCrateStation.crateInTime;
			case 3:
				return SkyCrateStation.crateOutTime;
			case 4:
				return SkyCrateStation.inputTime;
			case 5:
				return SkyCrateStation.outputTime;

		}
		return 0;
	}

	@Override
	public boolean onSkycrateMeeting(EntitySkyCrate skyCrate)
	{
		TileEntitySkyCrateStation master = master();
		if(master!=null&&master.animation==0&&master.getInventory().get(3).isEmpty())
		{
			master.getInventory().set(3, skyCrate.crate.copy());
			master.getInventory().set(4, skyCrate.mount.copy());
			master.animation = 4;
			master.progress = 0;
			master.sendUpdate(1);
			master.sendUpdate(2);
			skyCrate.crate = ItemStack.EMPTY;
			skyCrate.mount = ItemStack.EMPTY;
			skyCrate.setDead();
			return false;
		}
		return true;
	}

	@Override
	public void updateRotationStorage(float rpm, float torque, int part)
	{
		if(world.isRemote)
		{
			if(part==0)
			{
				rotation.setRotationSpeed(rpm);
				rotation.setTorque(torque);
			}
		}
	}

	@Override
	public void replaceStructureBlock(BlockPos pos, IBlockState state, ItemStack stack, int h, int l, int w)
	{
		super.replaceStructureBlock(pos, state, stack, h, l, w);

		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TileEntityConveyorBelt)
			((TileEntityConveyorBelt)tile).setFacing((mirrored?this.facing.rotateY(): this.facing.rotateYCCW()));
	}
}
