package pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.crafting.IMultiblockRecipe;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.IConveyorAttachable;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedCollisionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedSelectionBounds;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import blusunrize.immersiveengineering.common.util.network.MessageTileSync;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.ArtilleryHowitzer;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletHelper;
import pl.pabilo8.immersiveintelligence.api.bullets.IBullet;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IDataConnector;
import pl.pabilo8.immersiveintelligence.api.data.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.types.*;
import pl.pabilo8.immersiveintelligence.api.utils.IBooleanAnimatedPartsBlock;
import pl.pabilo8.immersiveintelligence.client.fx.ParticleUtils;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityBullet;
import pl.pabilo8.immersiveintelligence.common.items.ammunition.ItemIIAmmoArtillery;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.MessageBooleanAnimatedPartsSync;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8
 * @since 28-06-2019
 */
public class TileEntityArtilleryHowitzer extends TileEntityMultiblockMetal<TileEntityArtilleryHowitzer, IMultiblockRecipe> implements IDataDevice, IAdvancedCollisionBounds, IAdvancedSelectionBounds, IBooleanAnimatedPartsBlock, IConveyorAttachable
{
	public boolean active = false;

	//0 - nothing, 1 - loading, 2 - unloading, 3 - shooting
	public int animation = 0, fuse = -1;
	public int animationTime = 0, animationTimeMax = 0, shellLoadTime = 0, shellExpellTime = 0;
	public boolean isDoorOpened;
	public float turretYaw = 0, turretPitch = 0, plannedYaw = 0, plannedPitch = 0, platformHeight = 0, doorAngle = 0;
	public ItemStack bullet = ItemStack.EMPTY;
	public NonNullList<ItemStack> inventory = NonNullList.withSize(12, ItemStack.EMPTY);
	//0, 1, 2, 3, 4, 5 - 6, 7, 8, 9, 10, 11
	//0 - input, 11 - output 0-> 5 6->11
	public IItemHandler inventoryHandler = new IEInventoryHandler(12, this, 0, true, true);
	boolean update = false;

	public TileEntityArtilleryHowitzer()
	{
		super(MultiblockArtilleryHowitzer.instance, new int[]{7, 9, 9}, ArtilleryHowitzer.energyCapacity, true);
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		if(!isDummy())
		{
			if(!descPacket)
			{
				inventory = Utils.readInventory(nbt.getTagList("inventory", 10), 12);
			}
			active = nbt.getBoolean("active");
			doorAngle = nbt.getFloat("doorAngle");
			platformHeight = nbt.getFloat("platformHeight");
			turretYaw = nbt.getFloat("turretYaw");
			plannedYaw = nbt.getFloat("plannedYaw");
			turretPitch = nbt.getFloat("turretPitch");
			plannedPitch = nbt.getFloat("plannedPitch");
			isDoorOpened = nbt.getBoolean("isDoorOpened");

			animation = nbt.getInteger("animation");
			animationTime = nbt.getInteger("animationTime");
			animationTimeMax = nbt.getInteger("animationTimeMax");

			shellLoadTime = nbt.getInteger("shellLoadTime");
			shellExpellTime = nbt.getInteger("shellExpellTime");

			bullet = new ItemStack(nbt.getCompoundTag("bullet"));

		}
	}

	@Override
	public void receiveMessageFromClient(NBTTagCompound message)
	{
		super.receiveMessageFromClient(message);
	}

	@Override
	public void receiveMessageFromServer(NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);
		if(message.hasKey("active"))
			this.active = message.getBoolean("active");
		if(message.hasKey("isDoorOpened"))
			this.isDoorOpened = message.getBoolean("isDoorOpened");
		if(message.hasKey("inventory"))
			inventory = Utils.readInventory(message.getTagList("inventory", 10), 12);
		if(message.hasKey("doorAngle"))
			doorAngle = message.getFloat("doorAngle");
		if(message.hasKey("platformHeight"))
			platformHeight = message.getFloat("platformHeight");
		if(message.hasKey("turretYaw"))
			turretYaw = message.getFloat("turretYaw");
		if(message.hasKey("turretPitch"))
			turretPitch = message.getFloat("turretPitch");
		if(message.hasKey("plannedYaw"))
			plannedYaw = message.getFloat("plannedYaw");
		if(message.hasKey("plannedPitch"))
			plannedPitch = message.getFloat("plannedPitch");
		if(message.hasKey("animation"))
			animation = message.getInteger("animation");
		if(message.hasKey("animationTime"))
			animationTime = message.getInteger("animationTime");
		if(message.hasKey("animationTimeMax"))
			animationTimeMax = message.getInteger("animationTimeMax");
		if(message.hasKey("bullet"))
			bullet = new ItemStack(message.getCompoundTag("bullet"));
		if(message.hasKey("shellLoadTime"))
			shellLoadTime = message.getInteger("shellLoadTime");
		if(message.hasKey("shellExpellTime"))
			shellExpellTime = message.getInteger("shellExpellTime");
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
			{
				nbt.setTag("inventory", Utils.writeInventory(inventory));
			}
			nbt.setBoolean("active", active);
			nbt.setBoolean("isDoorOpened", isDoorOpened);
			nbt.setFloat("doorAngle", doorAngle);
			nbt.setFloat("platformHeight", platformHeight);
			nbt.setFloat("turretYaw", turretYaw);
			nbt.setFloat("turretPitch", turretPitch);
			nbt.setFloat("plannedYaw", plannedYaw);
			nbt.setFloat("plannedPitch", plannedPitch);

			nbt.setInteger("animation", animation);
			nbt.setInteger("animationTime", animationTime);
			nbt.setInteger("animationTimeMax", animationTimeMax);

			nbt.setInteger("shellLoadTime", shellLoadTime);
			nbt.setInteger("shellExpellTime", shellExpellTime);

			nbt.setTag("bullet", bullet.serializeNBT());
		}
	}

	@Override
	public void update()
	{
		super.update();

		if(isDummy())
			return;

		if(world.isRemote)
		{
			handleSounds();
		}

		if(!world.isRemote&&(isDoorOpened^world.isBlockPowered(getBlockPosForPos(getRedstonePos()[0]))))
		{
			isDoorOpened = world.isBlockPowered(getBlockPosForPos(getRedstonePos()[0]));
			IIPacketHandler.INSTANCE.sendToAllAround(new MessageBooleanAnimatedPartsSync(isDoorOpened, 0, this.getPos()), pl.pabilo8.immersiveintelligence.api.Utils.targetPointFromTile(this, 48));
		}


		if(isDoorOpened&&doorAngle < 155f)
			doorAngle = Math.min(doorAngle+0.25f, 155f);

		if(!isDoorOpened)
		{
			platformHeight = Math.max(0, platformHeight-(5/(float)ArtilleryHowitzer.platformTime));
			animationTime = 0;
			animationTimeMax = 0;
			animation = 0;
			plannedYaw = 0;
			plannedPitch = 0;
			if(world.getTotalWorldTime()%8==0)
				update = true;
			if(doorAngle > 0f)
				doorAngle = Math.max(doorAngle-0.5f, 0f);

		}

		if(turretYaw!=plannedYaw&&!(animation==3&&platformHeight!=5.25f))
		{
			if(energyStorage.getEnergyStored() >= ArtilleryHowitzer.energyUsagePlatform)
			{
				if(turretYaw < plannedYaw)
					turretYaw += Math.round(90f/ArtilleryHowitzer.rotateTime);
				else if(turretYaw > plannedYaw)
					turretYaw -= Math.round(90f/ArtilleryHowitzer.rotateTime);

				if(Math.round(turretYaw/10f)*10f==Math.round(plannedYaw/10f)*10f)
					turretYaw = plannedYaw;

				turretYaw = turretYaw%360;
				update = true;
			}

		}
		else if(turretPitch!=plannedPitch&&!(animation==3&&platformHeight!=5.25f))
		{
			if(energyStorage.getEnergyStored() >= ArtilleryHowitzer.energyUsagePlatform)
			{
				if(turretPitch < plannedPitch)
					turretPitch += Math.round(90f/ArtilleryHowitzer.rotateTime);
				else if(turretPitch > plannedPitch)
					turretPitch -= Math.round(90f/ArtilleryHowitzer.rotateTime);

				if(Math.round(turretPitch/10f)*10f==Math.round(plannedPitch/10f)*10f)
					turretPitch = plannedPitch;

				turretPitch = turretPitch%360;
				update = true;
			}
		}

		work:
		if(isDoorOpened&&(animation!=0))
		{
			if(animation==1||animation==2)
			{
				plannedPitch = 0;
				plannedYaw = facing.getOpposite().getHorizontalAngle() > 180?360f-facing.getOpposite().getHorizontalAngle(): facing.getOpposite().getHorizontalAngle();

				if(turretYaw==plannedYaw&&turretPitch==plannedPitch&&energyStorage.getEnergyStored() >= ArtilleryHowitzer.energyUsageLoader)
				{
					if(platformHeight > 0)
					{
						if(energyStorage.getEnergyStored() >= ArtilleryHowitzer.energyUsagePlatform)
						{
							platformHeight = Math.max(0, platformHeight-(5/(float)ArtilleryHowitzer.platformTime));
							energyStorage.extractEnergy(ArtilleryHowitzer.energyUsagePlatform, false);
							update = true;
						}

						break work;
					}


					if(animationTimeMax!=ArtilleryHowitzer.loadTime)
					{
						animationTimeMax = ArtilleryHowitzer.loadTime;
						update = true;

					}

					if(animationTime==animationTimeMax/2)
					{
						world.playSound(null, getPos(), animation==1?IISounds.howitzer_load: IISounds.howitzer_unload, SoundCategory.BLOCKS, 0.5F, 1);
					}

					if(animationTime >= animationTimeMax)
					{
						if(!world.isRemote)
						{
							if(animation==1)
							{
								bullet = inventoryHandler.extractItem(5, 1, false);
								animation = 0;
								animationTimeMax = 0;
								animationTime = 0;
							}
							else
							{
								//Idk why, but I can't use insertItem, because it does nothing
								if(inventoryHandler.getStackInSlot(6).isEmpty())
								{
									inventory.set(6, bullet.copy());
									bullet = ItemStack.EMPTY;
								}

								if(bullet.isEmpty())
								{
									animation = 0;
									animationTimeMax = 0;
									animationTime = 0;
								}
							}
						}


						update = true;

					}
					else
					{
						if(animation==0||animation%20==0)
							update = true;
						animationTime += 1;
					}

					//update = true;
					energyStorage.extractEnergy(ArtilleryHowitzer.energyUsageLoader, false);
				}
			}
			else if(animation==3)
			{
				if(platformHeight < 5.25f)
				{
					if(energyStorage.getEnergyStored() >= ArtilleryHowitzer.energyUsagePlatform)
					{
						platformHeight = Math.min(5.25f, platformHeight+(5/(float)ArtilleryHowitzer.platformTime));
						energyStorage.extractEnergy(ArtilleryHowitzer.energyUsagePlatform, false);
					}
					break work;
				}

				if(plannedPitch==turretPitch&&plannedYaw==turretYaw)
				{
					if(animationTimeMax!=ArtilleryHowitzer.fireTime&&bullet.getItem() instanceof ItemIIAmmoArtillery&&bullet.getMetadata()==ItemIIAmmoArtillery.BULLET)
						animationTimeMax = ArtilleryHowitzer.fireTime;

					if(animationTime==Math.round(animationTimeMax*0.25f))
					{

						if(bullet.getItem() instanceof ItemIIAmmoArtillery&&bullet.getMetadata()==ItemIIAmmoArtillery.BULLET)
						{
							double true_angle = Math.toRadians((-turretYaw) > 180?360f-(-turretYaw): (-turretYaw));
							double true_angle2 = Math.toRadians(-(-90-turretPitch));

							Vec3d gun_end = pl.pabilo8.immersiveintelligence.api.Utils.offsetPosDirection(3f, true_angle, true_angle2);
							if(world.isRemote)
								ParticleUtils.spawnGunfireFX(getGunPosition().x+gun_end.x, getGunPosition().y+gun_end.y, getGunPosition().z+gun_end.z, 0, 0, 0, 8f);
							//world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, getGunPosition().x+gun_end.x, getGunPosition().y+gun_end.y, getGunPosition().z+gun_end.z, 0, 0, 0);
							world.playSound(null, getPos(), IISounds.howitzer_shot, SoundCategory.BLOCKS, 1F, 1);

							if(!world.isRemote)
							{
								EntityBullet a = BulletHelper.createBullet(world, bullet, getGunPosition().add(gun_end), gun_end.scale(0.33f));
								if(this.fuse > 0)
								{
									a.fuse = this.fuse;
									this.fuse = -1;
								}
								ArrayList<BlockPos> pp = new ArrayList<>();
								for(int i = 0; i < structureDimensions[0]*structureDimensions[1]*structureDimensions[2]; i++)
									pp.add(getBlockPosForPos(i));
								a.setShootPos(pp.toArray(new BlockPos[0]));
								a.world.spawnEntity(a);

								bullet = ((IBullet)bullet.getItem()).getCasingStack(1);
							}

						}

						update = true;
						animationTime += 1;

					}
					else if(animationTime >= animationTimeMax)
					{
						animation = 0;
						animationTimeMax = 0;
						animationTime = 0;
						update = true;

					}
					else
					{
						if(animation==0)
							update = true;

						animationTime += 1;
					}

					if(world.getTotalWorldTime()%40==0)
						update = true;
				}


			}

		}

		if(world.getTotalWorldTime()%40==0&&inventoryHandler.getStackInSlot(0).isEmpty())
		{

			if(getTileForPos(mirrored?327: 329)!=null)
			{
				List<EntityItem> itemsIn = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(getTileForPos(mirrored?327: 329).getPos().offset(EnumFacing.UP)));
				for(EntityItem ent : itemsIn)
				{
					if(!(ent.getItem().getItem() instanceof ItemIIAmmoArtillery))
						continue;
					//ImmersiveIntelligence.logger.info(ent);

					ItemStack stack = inventoryHandler.insertItem(0, ent.getItem().copy(), false);

					update = true;
					if(stack.isEmpty())
					{
						ent.setItem(ItemStack.EMPTY);
						break;
					}
				}
			}

		}

		if(shellLoadTime < ArtilleryHowitzer.conveyorTime)
		{
			shellLoadTime += 1;
		}
		else
		{
			if(inventoryHandler.getStackInSlot(5).isEmpty())
			{
				ItemStack stack = inventoryHandler.extractItem(4, 1, false);
				inventoryHandler.insertItem(5, stack, false);
			}

			if(inventoryHandler.getStackInSlot(4).isEmpty())
			{
				ItemStack stack = inventoryHandler.extractItem(3, 1, false);
				inventoryHandler.insertItem(4, stack, false);
			}

			if(inventoryHandler.getStackInSlot(3).isEmpty())
			{
				ItemStack stack = inventoryHandler.extractItem(2, 1, false);
				inventoryHandler.insertItem(3, stack, false);
			}

			if(inventoryHandler.getStackInSlot(2).isEmpty())
			{
				ItemStack stack = inventoryHandler.extractItem(1, 1, false);
				inventoryHandler.insertItem(2, stack, false);
			}

			if(inventoryHandler.getStackInSlot(1).isEmpty())
			{
				ItemStack stack = inventoryHandler.extractItem(0, 1, false);
				inventoryHandler.insertItem(1, stack, false);
			}

			update = true;
			shellLoadTime = 0;
		}

		if(shellExpellTime < ArtilleryHowitzer.conveyorTime)
		{
			shellExpellTime += 1;
		}
		else
		{
			if(!world.isRemote&&!inventoryHandler.getStackInSlot(11).isEmpty())
				Utils.dropStackAtPos(world, getBlockPosForPos(mirrored?329: 327).offset(EnumFacing.UP), inventoryHandler.extractItem(11, 1, false));

			if(inventoryHandler.getStackInSlot(11).isEmpty())
			{
				ItemStack stack = inventory.get(10).copy();
				inventory.set(11, stack);
				inventory.set(10, ItemStack.EMPTY);
			}

			if(inventoryHandler.getStackInSlot(10).isEmpty())
			{
				ItemStack stack = inventory.get(9).copy();
				inventory.set(10, stack);
				inventory.set(9, ItemStack.EMPTY);
			}

			if(inventoryHandler.getStackInSlot(9).isEmpty())
			{
				ItemStack stack = inventory.get(8).copy();
				inventory.set(9, stack);
				inventory.set(8, ItemStack.EMPTY);
			}

			if(inventoryHandler.getStackInSlot(8).isEmpty())
			{
				ItemStack stack = inventory.get(7).copy();
				inventory.set(8, stack);
				inventory.set(7, ItemStack.EMPTY);
			}

			if(inventoryHandler.getStackInSlot(7).isEmpty())
			{
				ItemStack stack = inventory.get(6).copy();
				inventory.set(7, stack);
				inventory.set(6, ItemStack.EMPTY);
			}

			update = true;
			shellExpellTime = 0;
		}

		if(world.isRemote)
			return;

		if(update)
		{
			this.markDirty();
			NBTTagCompound tag = new NBTTagCompound();
			tag.setBoolean("active", active);
			tag.setBoolean("isDoorOpened", isDoorOpened);
			tag.setFloat("doorAngle", doorAngle);
			tag.setFloat("platformHeight", platformHeight);
			tag.setFloat("turretYaw", turretYaw);
			tag.setFloat("turretPitch", turretPitch);
			tag.setFloat("plannedYaw", plannedYaw);
			tag.setFloat("plannedPitch", plannedPitch);
			tag.setFloat("platformHeight", platformHeight);
			tag.setInteger("animation", animation);
			tag.setFloat("animationTime", animationTime);
			tag.setFloat("animationTimeMax", animationTimeMax);
			tag.setInteger("shellLoadTime", shellLoadTime);
			tag.setInteger("shellExpellTime", shellExpellTime);

			tag.setTag("inventory", Utils.writeInventory(inventory));
			tag.setTag("bullet", bullet.serializeNBT());
			ImmersiveEngineering.packetHandler.sendToAllAround(new MessageTileSync(this, tag), new TargetPoint(this.world.provider.getDimension(), this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), 48));
		}
	}

	@SideOnly(Side.CLIENT)
	private void handleSounds()
	{
		boolean platform_ok = animation==0||platformHeight==((animation==1||animation==2)?0f: 5.25f);
		boolean yaw_ok = turretYaw==plannedYaw;
		boolean pitch_ok = turretPitch==plannedPitch;
		if(platform_ok)
		{
			if(!yaw_ok)
			{
				if(world.getTotalWorldTime()%20==0)
					world.playSound(ClientUtils.mc().player, getBlockPosForPos(525), IISounds.howitzer_rotation_v, SoundCategory.BLOCKS, .5f, 1);
			}
			else if(!pitch_ok)
			{
				if(world.getTotalWorldTime()%20==0)
					world.playSound(ClientUtils.mc().player, getBlockPosForPos(525), IISounds.howitzer_rotation_h, SoundCategory.BLOCKS, .5f, 1);
			}
		}
		else
		{
			if(world.getTotalWorldTime()%20==0)
				world.playSound(ClientUtils.mc().player, getBlockPosForPos(525), IISounds.howitzer_rotation_h, SoundCategory.BLOCKS, .5f, 1);
		}

		if(isDoorOpened&&doorAngle < 155f)
		{
			if(world.getTotalWorldTime()%20==0)
				world.playSound(ClientUtils.mc().player, getBlockPosForPos(525).up(), IISounds.howitzer_door_open, SoundCategory.BLOCKS, .5f, 1);
		}
		else if(!isDoorOpened&&doorAngle > 0f)
		{
			if(world.getTotalWorldTime()%20==0)
				world.playSound(ClientUtils.mc().player, getBlockPosForPos(525).up(), IISounds.howitzer_door_close, SoundCategory.BLOCKS, .5f, 1);
		}
	}

	@Override
	public float[] getBlockBounds()
	{
		if(isDoorOpened&&((pos >= 524&&pos <= 528)||(pos >= 533&&pos <= 537)||(pos >= 542&&pos <= 546)||(pos >= 515&&pos <= 519)||(pos >= 506&&pos <= 510)))
		{
			return new float[]{0, 0, 0, 1, 1, 1};
		}
		return new float[0];
	}

	@Override
	public int[] getEnergyPos()
	{
		return new int[]{mirrored?449: 441};
	}

	@Override
	public int[] getRedstonePos()
	{
		return new int[]{481};
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
		return inventory;
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		//Matches caliber
		return stack.getItem() instanceof ItemIIAmmoArtillery;
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
		return new int[0];
	}

	@Override
	public boolean additionalCanProcessCheck(MultiblockProcess<IMultiblockRecipe> process)
	{
		return false;
	}

	@Override
	public IFluidTank[] getInternalTanks()
	{
		return new FluidTank[0];
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
		return (side.getAxis()==Axis.Y&&iTank==0);
	}

	@Override
	public void doGraphicalUpdates(int slot)
	{
		this.markDirty();
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
	public void onReceive(DataPacket packet, EnumFacing side)
	{
		TileEntityArtilleryHowitzer master = master();
		if(pos==(mirrored?441: 449)&master!=null)
		{
			IDataConnector conn = pl.pabilo8.immersiveintelligence.api.Utils.findConnectorFacing(getBlockPosForPos(6), world, mirrored?facing.rotateY(): facing.rotateYCCW());
			DataPacket p = new DataPacket();

			//Command
			if(!active&&packet.getPacketVariable('c') instanceof DataPacketTypeString)
			{
				String command = packet.getPacketVariable('c').valueToString();
				switch(command)
				{
					case "fire":
					{
						master.animation = 3;
						master.animationTimeMax = ArtilleryHowitzer.fireTime;
						master.animationTime = 0;
					}
					break;
					case "load":
					{
						if(master.bullet.isEmpty())
						{
							master.animation = 1;
							master.animationTimeMax = ArtilleryHowitzer.loadTime;
							master.animationTime = 0;
						}
					}
					break;
					case "unload":
					{
						if(!master.bullet.isEmpty())
						{
							master.animation = 2;
							master.animationTimeMax = ArtilleryHowitzer.loadTime;
							master.animationTime = 0;
						}
					}
					break;
					case "stop":
					{
						master.animation = 0;
						master.animationTimeMax = 0;
						master.animationTime = 0;
					}
					break;
					//callback
					case "get_energy":
						if(conn==null)
							return;
						p.setVariable('c', new DataPacketTypeString("energy"));
						p.setVariable('g', new DataPacketTypeInteger(energyStorage.getEnergyStored()));
						conn.sendPacket(p);
						break;
					case "get_state":
					case "get_state_num":
					{
						//0 - nothing, 1 - loading, 2 - unloading, 3 - shooting
						if(conn==null)
							return;
						p.setVariable('c', new DataPacketTypeString("state"));
						if(command.equals("get_state_num"))
							p.setVariable('g', new DataPacketTypeInteger(animation));
						else
							p.setVariable('g', new DataPacketTypeString(new String[]{"idle","loading","unloading","shooting"}[animation]));
						conn.sendPacket(p);
					}
					case "get_state_progress":
					{
						if(conn==null)
							return;
						p.setVariable('c', new DataPacketTypeString("state_progress"));
						p.setVariable('g', new DataPacketTypeInteger((int)(animationTime/(float)animationTimeMax*100)));
						conn.sendPacket(p);
					}
					case "get_yaw":
					{
						if(conn==null)
							return;
						p.setVariable('c', new DataPacketTypeString("yaw"));
						p.setVariable('g', new DataPacketTypeInteger((int)turretYaw));
						conn.sendPacket(p);
					}
					break;
					case "get_pitch":
					{
						if(conn==null)
							return;
						p.setVariable('c', new DataPacketTypeString("pitch"));
						p.setVariable('g', new DataPacketTypeInteger((int)turretPitch));
						conn.sendPacket(p);
					}
					break;
					case "get_planned_yaw":
					{
						if(conn==null)
							return;
						p.setVariable('c', new DataPacketTypeString("planned_yaw"));
						p.setVariable('g', new DataPacketTypeInteger((int)plannedYaw));
						conn.sendPacket(p);
					}
					break;
					case "get_planned_pitch":
					{
						if(conn==null)
							return;
						p.setVariable('c', new DataPacketTypeString("planned_pitch"));
						p.setVariable('g', new DataPacketTypeInteger((int)plannedPitch));
						conn.sendPacket(p);
					}
					break;
					case "get_platform_height":
					{
						if(conn==null)
							return;
						p.setVariable('c', new DataPacketTypeString("platform_height"));
						p.setVariable('g', new DataPacketTypeInteger((int)platformHeight));
						conn.sendPacket(p);
					}
					break;
					case "get_door_opened":
					{
						if(conn==null)
							return;
						p.setVariable('c', new DataPacketTypeString("door_opened"));
						p.setVariable('g', new DataPacketTypeBoolean(isDoorOpened&&doorAngle==155f));
						conn.sendPacket(p);
					}
					break;
					case "get_door_closed":
					{
						if(conn==null)
							return;
						p.setVariable('c', new DataPacketTypeString("door_closed"));
						p.setVariable('g', new DataPacketTypeBoolean(!isDoorOpened&&doorAngle==0f));
						conn.sendPacket(p);
					}
					break;
					case "get_door_opening":
					{
						if(conn==null)
							return;
						p.setVariable('c', new DataPacketTypeString("door_opening"));
						p.setVariable('g', new DataPacketTypeBoolean(doorAngle!=155f&&doorAngle!=0f));
						conn.sendPacket(p);
					}
					break;
					case "get_loaded_shell":
					{
						if(conn==null)
							return;
						p.setVariable('c', new DataPacketTypeString("loaded_shell"));
						p.setVariable('g', new DataPacketTypeItemStack(bullet));
						conn.sendPacket(p);
					}
					break;
					case "get_stored_shell":
					{
						if(conn==null)
							return;
						IDataType i = packet.getPacketVariable('i');
						if(i instanceof DataPacketTypeInteger)
						{
							int value = ((DataPacketTypeInteger)i).value;
							if(value >= 0&&value <= 5)
							{
								p.setVariable('c', new DataPacketTypeString("stored_shell"));
								p.setVariable('g', new DataPacketTypeItemStack(bullet));
								conn.sendPacket(p);
							}

						}
					}
					break;

				}
			}

			if(master.animation!=0)
			{
				if(packet.getPacketVariable('y') instanceof DataPacketTypeInteger)
				{
					master.plannedYaw = ((DataPacketTypeInteger)packet.getPacketVariable('y')).value%360;
					if(master.plannedYaw < 0)
						master.plannedYaw = 360f-master.plannedYaw;
				}

				if(packet.getPacketVariable('p') instanceof DataPacketTypeInteger)
				{
					master.plannedPitch = Math.min(Math.max(-Math.abs((((DataPacketTypeInteger)packet.getPacketVariable('p')).value)%360), -105), 0);
				}

				if(packet.getPacketVariable('f') instanceof DataPacketTypeInteger)
				{
					master.fuse = Math.max(-1, ((DataPacketTypeInteger)packet.getPacketVariable('f')).value);
				}
			}

			master.update = true;
		}
	}

	@Override
	public List<AxisAlignedBB> getAdvancedSelectionBounds()
	{
		TileEntityArtilleryHowitzer master = master();
		ArrayList<AxisAlignedBB> list = new ArrayList<>();
		if((pos >= 524&&pos <= 528)||(pos >= 533&&pos <= 537)||(pos >= 542&&pos <= 546)||(pos >= 515&&pos <= 519)||(pos >= 506&&pos <= 510))
		{
			if(master!=null&&master.doorAngle > 5)
			{
				if(pos==544||pos==508)
				{
					double true_angle2 = Math.toRadians(pos==508?-master.doorAngle: master.doorAngle);
					AxisAlignedBB aabb = new AxisAlignedBB(-0.125, -0.125, -0.125, 0.125, 0.125, 0.125)
							.offset(facing.getFrontOffsetX(),0,facing.getFrontOffsetZ())
							.grow(facing.rotateY().getFrontOffsetX()*3,0,facing.rotateY().getFrontOffsetZ()*3)
							.offset(0.5,0,0.5);
					Vec3d vv = pl.pabilo8.immersiveintelligence.api.Utils.offsetPosDirection(1f, Math.toRadians(180-facing.getHorizontalAngle()), true_angle2);
					for(float x = 0; x < 3f; x += 0.25)
						list.add(aabb.offset(getPos().getX(), getPos().getY(), getPos().getZ()).offset(vv.scale(x)));

					for(AxisAlignedBB axisAlignedBB : list)
					{
						world.getEntitiesWithinAABB(EntityLivingBase.class,axisAlignedBB,EntityLivingBase::canBePushed).forEach(entityLivingBase -> entityLivingBase.move(MoverType.PISTON,0,0.002,0));
					}
				}
				else
					list.add(new AxisAlignedBB(0, 0, 0, 0, 0, 0).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
			}
			else
				list.add(new AxisAlignedBB(0, 0, 0, 1, 0.125, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
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
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return master()!=null;
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			TileEntityArtilleryHowitzer master = master();
			if(master==null)
				return null;
			return (T)this.inventoryHandler;
		}
		return super.getCapability(capability, facing);
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
	public EnumFacing[] sigOutputDirections()
	{
		if(pos==2)
			return new EnumFacing[]{mirrored?facing.rotateYCCW(): facing.rotateY()};
		return new EnumFacing[0];
	}

	@Override
	public void onEntityCollision(World world, Entity entity)
	{

	}

	private Vec3d getGunPosition()
	{
		BlockPos shoot_pos = getBlockPosForPos(526).offset(EnumFacing.UP, 1);
		return new Vec3d(shoot_pos.getX()+.5, shoot_pos.getY()+1.5, shoot_pos.getZ()+.5);
	}
}
