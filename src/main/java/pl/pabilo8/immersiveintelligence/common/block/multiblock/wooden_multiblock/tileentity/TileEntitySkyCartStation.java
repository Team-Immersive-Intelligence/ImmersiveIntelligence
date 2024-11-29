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
import blusunrize.immersiveengineering.common.util.Utils;
import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.SkyCartStation;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.SkyCrateStation;
import pl.pabilo8.immersiveintelligence.api.rotary.*;
import pl.pabilo8.immersiveintelligence.api.utils.ISkyCrateConnector;
import pl.pabilo8.immersiveintelligence.api.utils.MinecartBlockHelper;
import pl.pabilo8.immersiveintelligence.api.utils.minecart.IMinecartBlockPickable;
import pl.pabilo8.immersiveintelligence.api.utils.tools.ISkycrateMount;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.multiblock.MultiblockSkyCartStation;
import pl.pabilo8.immersiveintelligence.common.entity.EntitySkyCrate;
import pl.pabilo8.immersiveintelligence.common.entity.EntitySkycrateInternal;
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
 * @author Avalon
 * @since 22-10-2024
 */
public class TileEntitySkyCartStation extends TileEntityMultiblockConnectable<TileEntitySkyCartStation, IMultiblockRecipe> implements IAdvancedCollisionBounds, IAdvancedSelectionBounds, ISkyCrateConnector, IPlayerInteraction, IGuiTile, IRotationalEnergyBlock
{
	public boolean occupied = false;
	public EntityMinecart cart = null;
	//none, minecart ,minecart in, minecart out, minecart load, minecart unload
	public int animation = 0;
	public float progress = 0;
	public ItemStack banner = ItemStack.EMPTY;
	public ItemStack crate = ItemStack.EMPTY;
	public ItemStack mount = ItemStack.EMPTY;
	public RotaryStorage rotation = new RotaryStorage(0, 0)
	{
		@Override
		public RotationSide getSide(@Nullable EnumFacing facing)
		{
			return facing==getFacing().rotateYCCW()?RotationSide.INPUT: RotationSide.NONE;
		}
	};

	public EntitySkycrateInternal internalEntity = null;

	NonNullList<ItemStack> inventory = NonNullList.withSize(3, ItemStack.EMPTY);

	public TileEntitySkyCartStation()
	{
		super(MultiblockSkyCartStation.INSTANCE, new int[]{3, 3, 3}, 0, true);
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		if(!isDummy())
		{
			if(!descPacket&&nbt.hasKey("inventory"))
				inventory = Utils.readInventory(nbt.getTagList("inventory", 10), 3);

			animation = nbt.getInteger("animation");
			progress = nbt.getFloat("progress");
			occupied = nbt.getBoolean("occupied");
			banner = new ItemStack(nbt.getCompoundTag("banner"));
			crate = new ItemStack(nbt.getCompoundTag("crate"));
			mount = new ItemStack(nbt.getCompoundTag("mount"));
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
			nbt.setBoolean("occupied", occupied);
			nbt.setTag("banner", banner.serializeNBT());
			nbt.setTag("crate", crate.serializeNBT());
			nbt.setTag("mount", mount.serializeNBT());
			nbt.setTag("rotation", rotation.toNBT());

			if(!world.isRemote)
				getInternalEntity();
		}
	}

	@Override
	public void receiveMessageFromServer(NBTTagCompound message)
	{
		if(message.hasKey("banner"))
			banner = new ItemStack(message.getCompoundTag("banner"));
		if(message.hasKey("crate"))
			crate = new ItemStack(message.getCompoundTag("crate"));
		if(message.hasKey("mount"))
			mount = new ItemStack(message.getCompoundTag("mount"));
		if(message.hasKey("inventory"))
			inventory = Utils.readInventory(message.getTagList("inventory", 10), 3);

		if(message.hasKey("animation"))
			animation = message.getInteger("animation");
		if(message.hasKey("progress"))
			progress = message.getFloat("progress");
		if(message.hasKey("occupied"))
			occupied = message.getBoolean("occupied");
		if(message.hasKey("rotation"))
			rotation.fromNBT(message.getCompoundTag("rotation"));

		super.receiveMessageFromServer(message);
	}

	@Override
	public void update()
	{
		super.update();

		handleRotation();

		if(!isDummy()&&!world.isRemote)
		{

			if(internalEntity==null)
				getInternalEntity();

			if(cart==null||!cart.isEntityAlive())
			{
				animation = 0;
				cart = null;
				doGraphicalUpdates(1);
			}
			else if(cart.getRidingEntity()==null)
				cart.startRiding(internalEntity, true);

			if(animation==1)
				if(world.getRedstonePower(getBlockPosForPos(8).offset((mirrored?this.facing.rotateYCCW(): this.facing.rotateY())), facing.rotateYCCW()) > 0)
				{
					animation = 3;
					progress = 0;
					doGraphicalUpdates(1);
				}
				else if(!crate.isEmpty()&&!mount.isEmpty()&&cart.getClass()==EntityMinecartEmpty.class)
				{
					animation = 4;
					progress = 0;
					doGraphicalUpdates(1);
					doGraphicalUpdates(2);
				}
				else if(cart instanceof IMinecartBlockPickable&&world.getTileEntity(getBlockPosForPos(7).offset(facing))!=null)
				{
					TileEntity te = world.getTileEntity(getBlockPosForPos(7).offset(facing));
					if(te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite()))
					{
						IItemHandler cap = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite());
						for(int i = 0; i < cap.getSlots(); i += 1)
							if(cap.getStackInSlot(i).getItem() instanceof ISkycrateMount)
							{
								mount = cap.extractItem(i, 1, false);
								break;
							}
						if(!mount.isEmpty())
						{
							IMinecartBlockPickable m = (IMinecartBlockPickable)cart;
							animation = 5;
							progress = 0;
							Tuple<ItemStack, EntityMinecart> a = ((IMinecartBlockPickable)cart).getBlockForPickup();
							crate = a.getFirst();
							cart = a.getSecond();
							cart.startRiding(internalEntity, true);
							internalEntity.updatePassenger(cart);
							doGraphicalUpdates(1);
							doGraphicalUpdates(2);
						}

					}
				}

			if(cart!=null&&internalEntity!=null)
			{
				Vec3i v = facing.rotateYCCW().getDirectionVec();

				if(animation==1||animation==4||animation==5)
				{
					BlockPos p = getBlockPosForPos(1);
					internalEntity.riding_x = p.getX()+0.5f;
					internalEntity.riding_y = p.getY()+0.125f;
					internalEntity.riding_z = p.getZ()+0.5f;
					internalEntity.updateValues();
				}
				if(animation==2)
				{
					float time = Math.min(1, progress/(SkyCartStation.minecartInTime*0.25f));
					BlockPos p = getBlockPosForPos(1).offset((mirrored?this.facing.rotateYCCW(): this.facing.rotateY()));
					internalEntity.riding_x = p.getX()+0.5f+(v.getX()*time);
					internalEntity.riding_y = p.getY()+0.125f+(v.getY()*time);
					internalEntity.riding_z = p.getZ()+0.5f+(v.getZ()*time);
					internalEntity.updateValues();

				}
				else if(animation==3)
				{
					float prog = progress/SkyCartStation.minecartOutTime;
					float time = prog > 0.45f?MathHelper.clamp((prog-0.45f)/0.15f, 0, 1): 0;

					BlockPos p = getBlockPosForPos(1);
					internalEntity.riding_x = p.getX()+0.5f-(v.getX()*time);
					internalEntity.riding_y = p.getY()+0.125f+(v.getY()*time);
					internalEntity.riding_z = p.getZ()+0.5f-(v.getZ()*time);
					internalEntity.updateValues();

					if(cart!=null&&time > 0.65)
						dismountCart();

				}
				if(cart!=null)
				{
					cart.rotationYaw = facing.getHorizontalAngle();
					cart.setCanUseRail(false);
				}

			}
			if(cart==null&&world.getTotalWorldTime()%4==0)
			{
				List<EntityMinecart> e = world.getEntitiesWithinAABB(EntityMinecart.class, new AxisAlignedBB(getBlockPosForPos(2).offset((mirrored?this.facing.rotateYCCW(): this.facing.rotateY()))), (EntityMinecart input) ->
						EnumFacing.getFacingFromVector((float)input.motionX, (float)input.motionY, (float)input.motionZ)==facing.rotateYCCW());
				if(e.size() > 0)
				{
					getInternalEntity();
					EntityMinecart c = e.get(0);
					master().cart = c;
					c.startRiding(internalEntity, true);
					animation = 2;
					progress = 0;
					doGraphicalUpdates(1);
				}
			}
		}

		if(!isDummy())
			if(animation > 1)
				if(progress < getAnimationLength())
					progress += getEffectiveEnergy()*IIRotaryUtils.getGearEffectiveness(getInventory(), getEfficiencyMultiplier(), 3);
				else
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
								dismountCart();
							progress = 0;
						}
						break;
						case 4:
						{
							if(!world.isRemote)
							{
								EntityMinecart c = MinecartBlockHelper.getMinecartFromBlockStack(crate, world);
								c.setPosition(cart.posX, cart.posY, cart.posZ);
								world.spawnEntity(c);
								cart.setDead();
								c.startRiding(internalEntity, true);
								cart = c;

								if(cart instanceof IMinecartBlockPickable)
									((IMinecartBlockPickable)cart).setMinecartBlock(crate);
								crate = ItemStack.EMPTY;

								TileEntity te = world.getTileEntity(getBlockPosForPos(7).offset(facing));
								mount = Utils.insertStackIntoInventory(te, mount, facing.getOpposite());
								if(!mount.isEmpty())
									Utils.dropStackAtPos(world, getBlockPosForPos(7).offset(facing), mount, facing);

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
										animation = 3;
										EntitySkyCrate s = new EntitySkyCrate(world, c, mount.copy(), crate.copy(), getBlockPosForPos(getConnectionPos()[0]));
										world.spawnEntity(s);
										crate = ItemStack.EMPTY;
										mount = ItemStack.EMPTY;
										progress = 0;
										doGraphicalUpdates(1);
										doGraphicalUpdates(2);
										break;
									}
									progress = getAnimationLength();
								}
							}
						}
						break;
					}
	}

	private void handleRotation() {
		boolean hasIssues = false;

		// If rotation speed or torque exceeds the maximum allowed values, trigger self-destruction.
		if (rotation.getRotationSpeed() > SkyCrateStation.rpmBreakingMax || rotation.getTorque() > SkyCrateStation.torqueBreakingMax) {
			selfDestruct();
			return; // Exit early after self-destruct.
		}

		// Get the position of the tile entity based on the machine's orientation.
		BlockPos rotationPos = getBlockPosForPos(6).offset((mirrored ? this.facing.rotateY() : this.facing.rotateYCCW()));
		TileEntity te = world.getTileEntity(rotationPos);

		// Check if there is a valid TileEntity at the specified position.
		if (te != null) {
			// Check if the TileEntity has the rotary energy capability.
			if (te.hasCapability(CapabilityRotaryEnergy.ROTARY_ENERGY, mirrored ? this.facing.rotateYCCW() : this.facing.rotateY())) {
				IRotaryEnergy cap = te.getCapability(CapabilityRotaryEnergy.ROTARY_ENERGY, mirrored ? this.facing.rotateYCCW() : this.facing.rotateY());

				// Ensure the capability is valid before processing.
				if (cap != null && rotation.handleRotation(cap, mirrored ? this.facing.rotateYCCW() : this.facing.rotateY())) {
					// Synchronize the rotary power state with the clients.
					IIPacketHandler.INSTANCE.sendToAllAround(new MessageRotaryPowerSync(rotation, 0, master().getPos()), IIPacketHandler.targetPointFromTile(master(), 24));
				}
			} else {
				// No rotary energy capability found on the tile entity.
				hasIssues = true;
			}
		} else {
			// No valid tile entity found at the specified position.
			hasIssues = true;
		}

		// If there are issues (missing capability or tile entity), grow rotation values more slowly.
		if (rotation.getTorque() > 0 || rotation.getRotationSpeed() > 0) {
			if (hasIssues) {
				rotation.grow(0, 0, 0.98f); // Reduce growth due to issues.
			}
			// Always sync rotary power state, even with reduced growth.
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
		return stack.getItem() instanceof IMotorGear;
	}

	@Override
	public void disassemble()
	{
		if(!isDummy())
		{
			if(internalEntity!=null)
				internalEntity.setDead();
			if(cart!=null)
				cart.setCanUseRail(true);
		}
		super.disassemble();
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
		EasyNBT tag = EasyNBT.newNBT();

		switch(slot)
		{
			case 0:
				tag.withTag("banner", banner.serializeNBT());
				break;
			case 1:
				tag.withInt("animation", animation).withFloat("progress", progress).withBoolean("occupied", occupied);
				break;
			case 2:
				tag.withTag("rotation", rotation.toNBT()).withItemStack("crate", crate).withItemStack("mount", mount);
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
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{

		if(pos==6&&capability==CapabilityRotaryEnergy.ROTARY_ENERGY&&facing==(mirrored?this.facing.rotateY(): this.facing.rotateYCCW()))
			return (T)rotation;
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
			if(!(other instanceof ISkyCrateConnector))
			{
				Set<Connection> conns = ImmersiveNetHandler.INSTANCE.getConnections(world, getPos());
				if(conns!=null)
					for(Connection conn : conns)
						ImmersiveNetHandler.INSTANCE.removeConnectionAndDrop(conn, world, getBlockPosForPos(10));
			}
	}

	@Override
	public boolean interact(EnumFacing side, EntityPlayer player, EnumHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		TileEntitySkyCartStation master = master();
		if(pos==20&&master!=null&&!world.isRemote)
			if(master.banner.isEmpty()&&heldItem.getItem()==Items.BANNER)
			{
				master.banner = heldItem.copy();
				master.banner.setCount(1);
				heldItem.shrink(1);
				master.doGraphicalUpdates(0);
				return true;
			}
			else if(!master.banner.isEmpty()&&Utils.isWirecutter(heldItem))
			{
				player.inventory.addItemStackToInventory(master.banner.copy());
				master.banner = ItemStack.EMPTY;
				master.doGraphicalUpdates(0);
				return true;
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
		return IIGuiList.GUI_SKYCART_STATION.ordinal();
	}

	@Nullable
	@Override
	public TileEntity getGuiMaster()
	{
		return master();
	}

	void getInternalEntity()
	{
		if(internalEntity==null||!internalEntity.isEntityAlive())
		{
			List<EntitySkycrateInternal> e = world.getEntitiesWithinAABB(EntitySkycrateInternal.class, new AxisAlignedBB(master().getPos()), (EntitySkycrateInternal input) ->
					true);
			if(e.size() > 0)
			{
				internalEntity = e.get(0);
				if(!internalEntity.getPassengers().isEmpty()&&internalEntity.getPassengers().get(0) instanceof EntityMinecart)
				{
					cart = (EntityMinecart)internalEntity.getPassengers().get(0);
					if(animation==0&&progress==0)
						animation = 2;
				}
			}
			else
			{
				EntitySkycrateInternal ent = new EntitySkycrateInternal(world, master().getPos());
				internalEntity = ent;
				world.spawnEntity(ent);
				ent.origin_pos = master().getPos();
			}
		}
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
				return SkyCartStation.minecartInTime;
			case 3:
				return SkyCartStation.minecartOutTime;
			case 4:
				return SkyCrateStation.inputTime;
			case 5:
				return SkyCrateStation.outputTime;

		}
		return 0;
	}

	void dismountCart()
	{
		if(!isDummy()&&cart!=null)
		{
			cart.dismountRidingEntity();
			BlockPos p = getBlockPosForPos(2).offset((mirrored?this.facing.rotateYCCW(): this.facing.rotateY()), 2);
			cart.setPosition(p.getX()+0.5, p.getY(), p.getZ()+0.5);
			cart.setCanUseRail(true);
			Vec3i yaw = (mirrored?this.facing.rotateYCCW(): this.facing.rotateY()).getDirectionVec();
			cart.motionX = yaw.getX();
			cart.motionZ = yaw.getZ();
			cart = null;
		}
	}

	@Override
	public boolean onSkycrateMeeting(EntitySkyCrate skyCrate)
	{
		if(master().crate.isEmpty())
		{
			master().crate = skyCrate.crate.copy();
			master().mount = skyCrate.mount.copy();
			master().doGraphicalUpdates(2);
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
}

