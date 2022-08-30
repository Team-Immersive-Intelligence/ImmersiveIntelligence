package pl.pabilo8.immersiveintelligence.common.block.multiblocks.wooden;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedCollisionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedSelectionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.ISoundTile;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import blusunrize.immersiveengineering.common.util.network.MessageTileSync;
import net.minecraft.client.particle.ParticleRedstone;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.*;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.Sawmill;
import pl.pabilo8.immersiveintelligence.api.crafting.SawmillRecipe;
import pl.pabilo8.immersiveintelligence.api.rotary.CapabilityRotaryEnergy;
import pl.pabilo8.immersiveintelligence.api.rotary.IRotaryEnergy;
import pl.pabilo8.immersiveintelligence.api.rotary.RotaryStorage;
import pl.pabilo8.immersiveintelligence.api.utils.IRotationalEnergyBlock;
import pl.pabilo8.immersiveintelligence.api.utils.ISawblade;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIDamageSources;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.MessageRotaryPowerSync;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
// TODO: 13.02.2022 fix/rework
/**
 * @author Pabilo8
 * @since 13-04-2020
 */
public class TileEntitySawmill extends TileEntityMultiblockMetal<TileEntitySawmill, SawmillRecipe> implements IGuiTile, ISoundTile, IRotationalEnergyBlock, IAdvancedSelectionBounds, IAdvancedCollisionBounds
{
	public NonNullList<ItemStack> inventory = NonNullList.withSize(4, ItemStack.EMPTY);
	public int processTime, processTimeMax;
	public ItemStack processPrimary = ItemStack.EMPTY, processSecondary = ItemStack.EMPTY;
	public boolean active = false, spawnLast = false;
	public RotaryStorage rotation = new RotaryStorage(0, 0)
	{
		@Override
		public RotationSide getSide(@Nullable EnumFacing facing)
		{
			return facing==getFacing()?RotationSide.INPUT: RotationSide.NONE;
		}
	};
	IItemHandler insertionHandler = new IEInventoryHandler(1, this, 0, true, false);
	IItemHandler dustExtractionHandler = new IEInventoryHandler(1, this, 3, false, true);

	public TileEntitySawmill()
	{
		super(MultiblockSawmill.instance, new int[]{2, 2, 4}, 0, false);
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		if(!descPacket)
			inventory = Utils.readInventory(nbt.getTagList("inventory", 10), 4);

		if(nbt.hasKey("processTime"))
			this.processTime = nbt.getInteger("processTime");
		if(nbt.hasKey("processTimeMax"))
			this.processTimeMax = nbt.getInteger("processTimeMax");
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);

		if(!descPacket)
		{
			nbt.setTag("inventory", Utils.writeInventory(inventory));
			nbt.setInteger("processTime", processTime);
			nbt.setInteger("processTimeMax", processTimeMax);
			nbt.setTag("processPrimary", processPrimary.serializeNBT());
			nbt.setTag("processSecondary", processSecondary.serializeNBT());
		}

	}

	@Override
	public void receiveMessageFromServer(NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);
		if(message.hasKey("processTime"))
			this.processTime = message.getInteger("processTime");
		if(message.hasKey("processTimeMax"))
			this.processTimeMax = message.getInteger("processTimeMax");
		if(message.hasKey("inventory"))
			inventory = Utils.readInventory(message.getTagList("inventory", 10), 4);
		if(message.hasKey("processPrimary"))
			processPrimary = new ItemStack(message.getCompoundTag("processPrimary"));
		if(message.hasKey("processSecondary"))
			processSecondary = new ItemStack(message.getCompoundTag("processSecondary"));
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void update()
	{
		super.update();
		if(isDummy())
			return;

		if(world.isRemote)
		{
			if(inventory.get(0).isEmpty())
			{
				processPrimary = ItemStack.EMPTY;
				processSecondary = ItemStack.EMPTY;
				processTime = 0;
				processTimeMax = 0;
			}
			if(!processPrimary.isEmpty()&&processTime < processTimeMax)
				processTime += 1;

			if(active)
				spawnDustParticle();
			if(spawnLast)
				spawnDustParticleLast();
			return;
		}

		active = shouldRenderAsActive();

		if(!isDummy()&&!world.isRemote)
		{
			boolean b = false;
			if(rotation.getRotationSpeed() > Sawmill.rpmBreakingMax||rotation.getTorque() > Sawmill.torqueBreakingMax)
			{
				selfDestruct();
			}

			if(world.getTileEntity(getBlockPosForPos(6).offset(facing))!=null)
			{
				TileEntity te = world.getTileEntity(getBlockPosForPos(6).offset(facing));
				if(te.hasCapability(CapabilityRotaryEnergy.ROTARY_ENERGY, facing.getOpposite()))
				{
					IRotaryEnergy cap = te.getCapability(CapabilityRotaryEnergy.ROTARY_ENERGY, facing.getOpposite());
					if(rotation.handleRotation(cap, facing.getOpposite()))
					{
						IIPacketHandler.INSTANCE.sendToAllAround(new MessageRotaryPowerSync(rotation, 0, master().getPos()), IIUtils.targetPointFromTile(master(), 24));
					}
				}
				else
					b = true;

			}
			else
				b = true;
			if((rotation.getTorque() > 0||rotation.getRotationSpeed() > 0))
			{
				// TODO: 26.12.2021 investigate
				if(b)
				{
					rotation.grow(0, 0, 0.98f);
				}
				IIPacketHandler.INSTANCE.sendToAllAround(new MessageRotaryPowerSync(rotation, 0, master().getPos()), IIUtils.targetPointFromTile(master(), 24));
			}

			if(getCurrentEfficiency() >= 0.95&&processQueue.size() < this.getProcessQueueMaxLength())
			{
				if(!inventory.get(0).isEmpty()&&inventory.get(1).getItem() instanceof ISawblade&&(inventory.get(3).isEmpty()||inventory.get(3).getCount() < inventory.get(3).getMaxStackSize()))
				{
					ISawblade sawblade = (ISawblade)inventory.get(1).getItem();
					SawmillRecipe recipe = SawmillRecipe.findRecipe(inventory.get(0));
					if(recipe!=null&&sawblade.getHardness(inventory.get(1)) >= recipe.getHardness()&&rotation.getTorque() >= recipe.getTorque())
					{
						MultiblockProcessInMachine<SawmillRecipe> process = new MultiblockProcessInMachine<>(recipe, 0);
						this.addProcessToQueue(process, false);
						processTime = 0;
						processTimeMax = (int)((float)recipe.getTotalProcessTime());
						//process.t
						processPrimary = processQueue.get(0).recipe.itemOutput;
						processSecondary = processQueue.get(0).recipe.itemSecondaryOutput;
						sendUpdate(2);
					}
				}
			}

			if(rotation.getRotationSpeed() > 0&&inventory.get(1).getItem() instanceof ISawblade&&(world.getTotalWorldTime()%Math.ceil(4/MathHelper.clamp(rotation.getRotationSpeed()/360, 0, 1))==0))
			{
				ISawblade sawblade = (ISawblade)inventory.get(1).getItem();
				int hardness = sawblade.getHardness(inventory.get(1));
				Vec3i v = facing.getDirectionVec();
				List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(getBlockPosForPos(2).offset(EnumFacing.UP)).offset(v.getX()*0.5, v.getY()*0.5, v.getZ()*0.5));
				for(EntityLivingBase l : entities)
				{
					l.attackEntityFrom(IIDamageSources.SAWMILL_DAMAGE, hardness);
				}
			}

			if(world.getTotalWorldTime()%20==0)
			{
				BlockPos pos = getBlockPosForPos(4).offset(facing.getOpposite(), 1);
				ItemStack output = inventory.get(2);
				TileEntity inventoryTile = this.world.getTileEntity(pos);
				if(inventoryTile!=null)
				{
					output = Utils.insertStackIntoInventory(inventoryTile, output, facing.getOpposite());
				}
				inventory.set(2, output);

				sendUpdate(2);

			}
		}
	}

	@SideOnly(Side.CLIENT)
	private void spawnDustParticleLast()
	{
		Vec3d pos = new Vec3d(getBlockPosForPos(2)).addVector(0.5, 0.75, 0.5);
		Vec3d facing = new Vec3d(getFacing().getDirectionVec());
		facing = facing.scale(0.65f);

		float mod = (float)(Math.random()*2f);
		float[] rgb = getCurrentProcessColor();

		ParticleRedstone particle = (ParticleRedstone)ClientUtils.mc().effectRenderer.spawnEffectParticle(EnumParticleTypes.REDSTONE.getParticleID(), pos.x+facing.x, pos.y+facing.y, pos.z+facing.z, 0, -4, 0);
		if(particle!=null)
		{
			//particle.setMaxAge(25);
			particle.reddustParticleScale = 3.25f;
			particle.setRBGColorF(rgb[0]*mod, rgb[1]*mod, rgb[2]*mod);
		}
		spawnLast = false;
	}

	@SideOnly(Side.CLIENT)
	private void spawnDustParticle()
	{
		Vec3d pos = new Vec3d(getBlockPosForPos(2)).addVector(0.5, 0, 0.5);
		Vec3d facing = new Vec3d(getFacing().getDirectionVec());
		facing = facing.scale(0.65f);

		float mod = (float)(Math.random()*2f);

		ParticleRedstone particle = (ParticleRedstone)ClientUtils.mc().effectRenderer.spawnEffectParticle(EnumParticleTypes.REDSTONE.getParticleID(), pos.x+facing.x, pos.y+1.125+facing.y, pos.z+facing.z, 0, -4, 0);
		ParticleRedstone particle2 = (ParticleRedstone)ClientUtils.mc().effectRenderer.spawnEffectParticle(EnumParticleTypes.REDSTONE.getParticleID(), pos.x+facing.x, pos.y+0.65+facing.y, pos.z+facing.z, 0, -4, 0);
		ParticleRedstone particle3 = (ParticleRedstone)ClientUtils.mc().effectRenderer.spawnEffectParticle(EnumParticleTypes.REDSTONE.getParticleID(), pos.x+facing.x, pos.y+facing.y, pos.z+facing.z, 0, -4, 0);

		float[] rgb = getCurrentProcessColor();
		final float dmod = 1.3043479f;

		if(particle!=null)
		{
			particle.reddustParticleScale = 3;
			particle.setRBGColorF(rgb[0]*mod, rgb[1]*mod, rgb[2]*mod);
		}
		if(particle2!=null)
		{
			particle2.reddustParticleScale = 4;
			particle2.setRBGColorF(rgb[0]*dmod*mod, rgb[1]*dmod*mod, rgb[2]*dmod*mod);
		}
		if(particle3!=null)
		{
			particle3.reddustParticleScale = 3;
			particle3.setRBGColorF(rgb[0]*mod, rgb[1]*mod, rgb[2]*mod);
		}


		active = false;
	}

	private float[] getCurrentProcessColor()
	{
		if(!processPrimary.isEmpty())
		{
			SawmillRecipe recipe = SawmillRecipe.findRecipe(inventory.get(0));
			if(recipe!=null)
			{
				return recipe.getDustColor();
			}
		}
		return new float[]{1, 1, 1};
	}

	public float getCurrentEfficiency()
	{
		float e1, e2;
		e1 = MathHelper.clamp(this.rotation.getRotationSpeed()/(float)Sawmill.rpmMin, 0, 1);
		e2 = MathHelper.clamp(this.rotation.getTorque()/(float)Sawmill.torqueMin, 0, 1);
		return (e1+e2)/2f;
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
	public boolean isInWorldProcessingMachine()
	{
		return true;
	}

	@Override
	public boolean additionalCanProcessCheck(MultiblockProcess<SawmillRecipe> process)
	{
		boolean condition = inventory.get(1).getItem() instanceof ISawblade&&getCurrentEfficiency() > 0.95&&inventory.get(2).getCount()+process.recipe.itemOutput.getCount() <= getSlotLimit(2);
		if(!condition)
			sendUpdate(2);
		return condition;
	}

	@Override
	public void doProcessOutput(ItemStack output)
	{
		BlockPos pos = getBlockPosForPos(4).offset(facing.getOpposite(), 1);
		TileEntity inventoryTile = this.world.getTileEntity(pos);
		if(inventoryTile!=null)
			output = Utils.insertStackIntoInventory(inventoryTile, output, facing);
		if(!output.isEmpty())
			Utils.dropStackAtPos(world, pos, output, facing.getOpposite());
	}

	@Override
	public void doProcessFluidOutput(FluidStack output)
	{
	}

	@Override
	public void onProcessFinish(MultiblockProcess<SawmillRecipe> process)
	{
		if(!(process.recipe).itemSecondaryOutput.isEmpty())
		{
			if(this.inventory.get(3).isEmpty())
			{
				this.inventory.set(3, (process.recipe).itemSecondaryOutput.copy());
			}
			else if(ItemHandlerHelper.canItemStacksStack(this.inventory.get(3), (process.recipe).itemSecondaryOutput)||this.inventory.get(3).getCount()+(process.recipe).itemSecondaryOutput.getCount() > this.getSlotLimit(3))
			{
				this.inventory.get(3).grow((process.recipe).itemSecondaryOutput.getCount());
			}
			if(this.inventory.get(3).getMaxStackSize() > getSlotLimit(3))
			{
				int take = inventory.get(3).getMaxStackSize()-getSlotLimit(3);
				inventory.get(3).shrink(take);
				ItemStack stack = inventory.get(3).copy();
				stack.setCount(take);
				Utils.dropStackAtPos(world, getPos().offset(facing.getOpposite()), stack, facing.getOpposite());
			}
		}
		if(inventory.get(1).getItem() instanceof ISawblade)
			((ISawblade)inventory.get(1).getItem()).damageSawblade(inventory.get(1), process.recipe.getHardness());

		processPrimary = ItemStack.EMPTY;
		processSecondary = ItemStack.EMPTY;
		int time = processTime;
		processTime = -1;
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
	public float getMinProcessDistance(MultiblockProcess<SawmillRecipe> process)
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
		return true;
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return 64;
	}

	@Override
	public int[] getOutputSlots()
	{
		return new int[]{2, 3};
	}

	@Override
	public int[] getOutputTanks()
	{
		return new int[]{};
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
		return (pos==14&&side==facing);
	}

	@Override
	public void doGraphicalUpdates(int slot)
	{

	}

	public void sendUpdate(int id)
	{
		if(!world.isRemote)
		{
			NBTTagCompound tag = new NBTTagCompound();

			tag.setTag("inventory", Utils.writeInventory(inventory));

			tag.setTag("processPrimary", processPrimary.serializeNBT());
			tag.setTag("processSecondary", processSecondary.serializeNBT());

			tag.setInteger("processTime", processTime);
			tag.setInteger("processTimeMax", processTimeMax);

			ImmersiveEngineering.packetHandler.sendToAllAround(new MessageTileSync(this, tag), IIUtils.targetPointFromTile(this, 32));
		}
	}

	@Override
	public SawmillRecipe findRecipeForInsertion(ItemStack inserting)
	{
		return null;
	}

	@Override
	protected SawmillRecipe readRecipeFromNBT(NBTTagCompound tag)
	{
		return SawmillRecipe.loadFromNBT(tag);
	}

	@Override
	public boolean canOpenGui()
	{
		return formed;
	}

	@Override
	public void onGuiOpened(EntityPlayer player, boolean clientside)
	{
		if(!clientside)
			sendUpdate(2);
	}

	@Override
	public int getGuiID()
	{
		return IIGuiList.GUI_SAWMILL.ordinal();
	}

	@Override
	public TileEntity getGuiMaster()
	{
		return master();
	}

	@Override
	public boolean shoudlPlaySound(String sound)
	{
		return false;
	}

	private void selfDestruct()
	{
		world.createExplosion(null, getPos().getX(), getPos().getY(), getPos().getZ(), 4, true);
	}

	//7 out
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		if((pos==15||pos==2||pos==6)&&capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return true;
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			TileEntitySawmill master = master();
			if(pos==15)
				return (T)master.insertionHandler;
			else if(pos==2||pos==6)
				return (T)master.dustExtractionHandler;
		}


		return super.getCapability(capability, facing);
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
	public List<AxisAlignedBB> getAdvancedColisionBounds()
	{
		// TODO: 18.03.2021 fix scheiss for NORTH/mirrored
		ArrayList<AxisAlignedBB> aabb = new ArrayList<>();
		if(pos==4)
		{
			aabb.add(new AxisAlignedBB(0, 0, 0, 1, 0.0625, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
			switch(facing)
			{
				case NORTH:
					aabb.add(new AxisAlignedBB(0, 0.0625, 0, 0.0625, 0.75, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					aabb.add(new AxisAlignedBB(1-0.0625, 0.0625, 0, 1, 0.75, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					aabb.add(new AxisAlignedBB(0.0625, 0.0625, 0, 0.9375, 0.75, 0.0625).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					break;
				case SOUTH:
					aabb.add(new AxisAlignedBB(0, 0.0625, 0, 0.0625, 0.75, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					aabb.add(new AxisAlignedBB(1-0.0625, 0.0625, 0, 1, 0.75, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					aabb.add(new AxisAlignedBB(0.0625, 0.0625, 1-0.0625, 0.9375, 0.75, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					break;
				case EAST:
					aabb.add(new AxisAlignedBB(0, 0.0625, 0, 1, 0.75, 0.0625).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					aabb.add(new AxisAlignedBB(0, 0.0625, 1-0.0625, 1, 0.75, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					aabb.add(new AxisAlignedBB(1-0.0625, 0.0625, 0.0625, 1, 0.75, 0.9375).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					break;
				case WEST:
					aabb.add(new AxisAlignedBB(0, 0.0625, 0, 1, 0.75, 0.0625).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					aabb.add(new AxisAlignedBB(0, 0.0625, 1-0.0625, 1, 0.75, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					aabb.add(new AxisAlignedBB(0, 0.0625, 0.0625, 0.0625, 0.75, 0.9375).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					break;
			}
		}
		else if(pos==11)
		{
			aabb.add(new AxisAlignedBB(0, 0, 0, 1, 0.0625, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
			EnumFacing ff = mirrored?facing.rotateY(): facing.rotateYCCW();
			switch(facing)
			{
				case NORTH:
					aabb.add(new AxisAlignedBB(0, 0.0625, 0.5-0.0625, 1, 0.1875, 0.75)
							.expand(ff.getFrontOffsetX(), 0, ff.getFrontOffsetZ())
							.offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					break;
				case SOUTH:
					aabb.add(new AxisAlignedBB(0, 0.0625, 0.25, 1, 0.1875, 0.5+0.0625)
							.expand(ff.getFrontOffsetX(), 0, ff.getFrontOffsetZ())
							.offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					break;
				case EAST:
					aabb.add(new AxisAlignedBB(0.25, 0.0625, 0, 0.5+0.0625, 0.1875, 1)
							.expand(ff.getFrontOffsetX(), 0, ff.getFrontOffsetZ())
							.offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					break;
				case WEST:
					aabb.add(new AxisAlignedBB(0.5-0.0625, 0.0625, 0, 0.75, 0.1875, 1)
							.expand(ff.getFrontOffsetX(), 0, ff.getFrontOffsetZ())
							.offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					break;
			}
		}
		else if(pos==15)
		{
			switch(facing)
			{
				case NORTH:
					aabb.add(new AxisAlignedBB(0.3125, 0, -0.375, 1-0.3125, 0.3125, 0.375)
							.offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					break;
				case SOUTH:
					aabb.add(new AxisAlignedBB(0.3125, 0, 1-0.3125, 1-0.3125, 0.375, 1+0.375)
							.offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					break;
				case EAST:
					aabb.add(new AxisAlignedBB(1-0.3125, 0, 0.3125, 1+0.375, 0.375, 1-0.3125)
							.offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					break;
				case WEST:
					aabb.add(new AxisAlignedBB(-0.375, 0, 0.3125, 0.375, 0.3125, 1-0.3125)
							.offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					break;
			}
		}
		else if(offset[1]==0)
		{
			aabb.add(new AxisAlignedBB(0, 0.8125, 0, 1, 1, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
			if(pos==6)
			{
				switch(facing)
				{
					case WEST:
						aabb.add(new AxisAlignedBB(0, 0.3125, 0.25, 0.35, 0.8125, 0.75).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
						break;
					case EAST:
						aabb.add(new AxisAlignedBB(1-0.35, 0.3125, 0.25, 1, 0.8125, 0.75).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
						break;
					case SOUTH:
						aabb.add(new AxisAlignedBB(0.25, 0.3125, 1-0.35, 0.75, 0.8125, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
						break;
					case NORTH:
						aabb.add(new AxisAlignedBB(0.25, 0.3125, 0, 0.75, 0.8125, 0.35).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
						break;
				}
			}
			int pp = pos;
			if(mirrored)
				pp = pos==3?1: (pos==1?3: pos==7?5: 7);

			if(pp==3)
			{
				switch(facing)
				{
					case SOUTH:
						aabb.add(new AxisAlignedBB(0.0625, 0, 0.0625, 0.0625+0.1875, 0.8125, 0.0625+0.1875).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
						break;
					case NORTH:
						aabb.add(new AxisAlignedBB(0.75, 0, 0.0625, 1-0.0625, 0.8125, 0.0625+0.1875).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
						break;
					case WEST:
						aabb.add(new AxisAlignedBB(0.75, 0, 0.0625, 1-0.0625, 0.8125, 0.0625+0.1875).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
						break;
					case EAST:
						aabb.add(new AxisAlignedBB(0.0625, 0, 0.75, 0.0625+0.1875, 0.8125, 1-0.0625).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
						break;
				}
			}
			else if(pp==5)
			{
				switch(facing)
				{
					case SOUTH:
						aabb.add(new AxisAlignedBB(0.75, 0, 0.75, 1-0.0625, 0.8125, 0.9375).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
						break;
					case NORTH:
						aabb.add(new AxisAlignedBB(0.0625, 0, 0.0625, 0.0625+0.1875, 0.8125, 0.0625+0.1875).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
						break;
					case WEST:
						aabb.add(new AxisAlignedBB(0.0625, 0, 0.75, 0.0625+0.1875, 0.8125, 1-0.0625).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
						break;
					case EAST:
						aabb.add(new AxisAlignedBB(0.75, 0, 0.0625, 1-0.0625, 0.8125, 0.0625+0.1875).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
						break;
				}
			}
			else if(pp==7)
			{
				switch(facing)
				{
					case SOUTH:
						aabb.add(new AxisAlignedBB(0.0625, 0, 0.75, 0.0625+0.1875, 0.8125, 1-0.0625).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
						break;
					case NORTH:
						aabb.add(new AxisAlignedBB(0.0625, 0, 0.75, 1-0.0625, 0.8125, 0.0625+0.1875).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
						break;
					case WEST:
						aabb.add(new AxisAlignedBB(0.0625, 0, 0.0625, 0.0625+0.1875, 0.8125, 0.0625+0.1875).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
						break;
					case EAST:
						aabb.add(new AxisAlignedBB(0.75, 0, 0.75, 0.9375, 0.8125, 1-0.0625).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
						break;
				}
			}
			else if(pp==1)
			{
				switch(facing)
				{
					case SOUTH:
						aabb.add(new AxisAlignedBB(0.75, 0, 0.0625, 1-0.0625, 0.8125, 0.0625+0.1875).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
						break;
					case NORTH:
						aabb.add(new AxisAlignedBB(0.75, 0, 0.0625+0.1875, 1-0.0625, 0.8125, 0.0625).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
						break;
					case WEST:
						aabb.add(new AxisAlignedBB(0.75, 0, 0.75, 0.9375, 0.8125, 1-0.0625).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
						break;
					case EAST:
						aabb.add(new AxisAlignedBB(0.0625, 0, 0.0625, 0.0625+0.1875, 0.8125, 0.0625+0.1875).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
						break;
				}
			}

		}
		return aabb;
	}

	@Override
	public List<AxisAlignedBB> getAdvancedSelectionBounds()
	{
		return getAdvancedColisionBounds();
	}

	@Override
	public boolean isOverrideBox(AxisAlignedBB box, EntityPlayer player, RayTraceResult mop, ArrayList<AxisAlignedBB> list)
	{
		return false;
	}
}
