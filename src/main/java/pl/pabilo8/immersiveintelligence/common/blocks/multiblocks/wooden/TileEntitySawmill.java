package pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.wooden;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.ISoundTile;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import blusunrize.immersiveengineering.common.util.network.MessageTileSync;
import net.minecraft.client.particle.ParticleRedstone;
import net.minecraft.entity.EntityLivingBase;
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
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines;
import pl.pabilo8.immersiveintelligence.api.crafting.SawmillRecipe;
import pl.pabilo8.immersiveintelligence.api.rotary.CapabilityRotaryEnergy;
import pl.pabilo8.immersiveintelligence.api.rotary.IRotaryEnergy;
import pl.pabilo8.immersiveintelligence.api.rotary.RotaryStorage;
import pl.pabilo8.immersiveintelligence.api.rotary.RotaryUtils;
import pl.pabilo8.immersiveintelligence.api.utils.IRotationalEnergyBlock;
import pl.pabilo8.immersiveintelligence.api.utils.ISawblade;
import pl.pabilo8.immersiveintelligence.common.IIDamageSources;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.MessageRotaryPowerSync;

import javax.annotation.Nullable;
import java.util.List;

import static pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.sawmill;

/**
 * Created by Pabilo8 on 13-04-2020.
 */
public class TileEntitySawmill extends TileEntityMultiblockMetal<TileEntitySawmill, SawmillRecipe> implements IGuiTile, ISoundTile, IRotationalEnergyBlock
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
			nbt.setInteger("processTime",processTime);
			nbt.setInteger("processTimeMax",processTimeMax);
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
			if(rotation.getRotationSpeed() > Machines.sawmill.rpmBreakingMax||rotation.getTorque() > Machines.sawmill.torqueBreakingMax)
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
						IIPacketHandler.INSTANCE.sendToAllAround(new MessageRotaryPowerSync(rotation, 0, master().getPos()), pl.pabilo8.immersiveintelligence.api.Utils.targetPointFromTile(master(), 24));
					}
				}
				else
					b = true;

			}
			else
				b = true;
			if((rotation.getTorque() > 0||rotation.getRotationSpeed() > 0))
			{
				if(world.getTotalWorldTime()%20==0)
					RotaryUtils.damageGears(inventory, rotation);
				if(b)
				{
					rotation.grow(0, 0, 0.98f);
				}
				IIPacketHandler.INSTANCE.sendToAllAround(new MessageRotaryPowerSync(rotation, 0, master().getPos()), pl.pabilo8.immersiveintelligence.api.Utils.targetPointFromTile(master(), 24));
			}

			if(getCurrentEfficiency() >= 0.95&&processQueue.size() < this.getProcessQueueMaxLength())
			{
				if(!inventory.get(0).isEmpty()&&inventory.get(1).getItem() instanceof ISawblade&&(inventory.get(3).isEmpty()||inventory.get(3).getCount() < inventory.get(3).getMaxStackSize()))
				{
					ISawblade sawblade = (ISawblade)inventory.get(1).getItem();
					SawmillRecipe recipe = SawmillRecipe.findRecipe(inventory.get(0));
					if(recipe!=null&&sawblade.getHardness(inventory.get(1)) >= recipe.getHardness()&&rotation.getTorque() >= recipe.getTorque())
					{
						MultiblockProcessInMachine<SawmillRecipe> process = new MultiblockProcessInMachine(recipe, 0);
						this.addProcessToQueue(process, false);
						processTime = 0;
						processTimeMax = (int)((float)recipe.getTotalProcessTime());
						//process.t
						sendUpdate(1);
					}
				}
			}

			if(rotation.getRotationSpeed()>0&&inventory.get(1).getItem() instanceof ISawblade&&(world.getTotalWorldTime()%Math.ceil(4/MathHelper.clamp(rotation.getRotationSpeed()/360,0,1))==0))
			{
				ISawblade sawblade = (ISawblade)inventory.get(1).getItem();
				int hardness = sawblade.getHardness(inventory.get(1));
				Vec3i v = facing.getDirectionVec();
				List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class,new AxisAlignedBB(getBlockPosForPos(2).offset(EnumFacing.UP)).offset(v.getX()*0.5,v.getY()*0.5,v.getZ()*0.5));
				for(EntityLivingBase l: entities)
				{
					l.attackEntityFrom(IIDamageSources.SAWMILL_DAMAGE,hardness);
				}
			}

			if(world.getTotalWorldTime()%20==0)
			{
				BlockPos pos = getBlockPosForPos(4).offset(facing.getOpposite(), 1);
				ItemStack output = inventory.get(2);
				ItemStack output_secondary = inventory.get(3);
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
		BlockPos pos = getBlockPosForPos(2);

		float mod = (float)(Math.random()*2f);

		ParticleRedstone particle = (ParticleRedstone)ClientUtils.mc().effectRenderer.spawnEffectParticle(EnumParticleTypes.REDSTONE.getParticleID(), pos.getX()+0.5, pos.getY()+0.75, pos.getZ()+0.5, 0, -4, 0);
		if(particle!=null)
		{
			//particle.setMaxAge(25);
			particle.reddustParticleScale = 3.25f;
			particle.setRBGColorF(0.22392157f*mod*1.15f, 0.21372549019607842f*mod*1.15f, 0.15176470588235294f*mod);
		}
		spawnLast = false;
	}

	@SideOnly(Side.CLIENT)
	private void spawnDustParticle()
	{
		//Hardcoded for now :D, might make it configurable later on.
		BlockPos pos = getBlockPosForPos(2);
		Vec3d facing = new Vec3d(getFacing().getDirectionVec());
		Vec3d facing2 = new Vec3d(getFacing().rotateY().getDirectionVec());
		facing = facing.scale(1.15f);
		facing2 = facing2.scale(-0.5f);

		float mod = (float)(Math.random()*2f);

		ParticleRedstone particle = (ParticleRedstone)ClientUtils.mc().effectRenderer.spawnEffectParticle(EnumParticleTypes.REDSTONE.getParticleID(), pos.getX()+facing.x+facing2.x, pos.getY()+1.125+facing.y+facing2.y, pos.getZ()+facing.z+facing2.z, 0, -4, 0);
		ParticleRedstone particle2 = (ParticleRedstone)ClientUtils.mc().effectRenderer.spawnEffectParticle(EnumParticleTypes.REDSTONE.getParticleID(), pos.getX()+facing.x+facing2.x, pos.getY()+0.65+facing.y+facing2.y, pos.getZ()+facing.z+facing2.z, 0, -4, 0);
		ParticleRedstone particle3 = (ParticleRedstone)ClientUtils.mc().effectRenderer.spawnEffectParticle(EnumParticleTypes.REDSTONE.getParticleID(), pos.getX()+facing.x+facing2.x, pos.getY()+facing.y+facing2.y, pos.getZ()+facing.z+facing2.z, 0, -4, 0);


		if(particle!=null)
		{
			particle.reddustParticleScale = 3;
			particle.setRBGColorF(0.22392157f*mod*1.15f, 0.21372549019607842f*mod*1.15f, 0.15176470588235294f*mod);
		}
		if(particle2!=null)
		{
			particle2.reddustParticleScale = 4;
			particle2.setRBGColorF(0.22392157f*mod*1.5f, 0.21372549019607842f*mod*1.5f, 0.15176470588235294f*mod*1.5f);
		}
		if(particle3!=null)
		{
			particle3.reddustParticleScale = 3;
			particle3.setRBGColorF(0.22392157f*mod*1.15f, 0.21372549019607842f*mod*1.15f, 0.15176470588235294f*mod);
		}


		active = false;
	}

	public float getCurrentEfficiency()
	{
		float e1, e2;
		e1 = MathHelper.clamp(this.rotation.getRotationSpeed()/(float)sawmill.rpmMin, 0, 1);
		e2 = MathHelper.clamp(this.rotation.getTorque()/(float)sawmill.torqueMin, 0, 1);
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
			sendUpdate(1);
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
		sendUpdate(1);
		processTime = time;
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
			if(id==1)
			{
				tag.setTag("inventory", Utils.writeInventory(inventory));
				if(processTime!=-1&&processQueue.size() > 0&&inventory.get(1).getItem() instanceof ISawblade&&getCurrentEfficiency() > 0.95&&inventory.get(2).getCount()+processQueue.get(0).recipe.itemOutput.getCount() <= getSlotLimit(2))
				{
					processPrimary = processQueue.get(0).recipe.itemOutput;
					processSecondary = processQueue.get(0).recipe.itemSecondaryOutput;
				}
				else
				{
					processPrimary = ItemStack.EMPTY;
					processSecondary = ItemStack.EMPTY;
					processTime = 0;
					processTimeMax = 0;
				}
			}
			else if(id==2)
			{
				tag.setTag("inventory", Utils.writeInventory(inventory));
			}
			if(id!=2)
			{
				tag.setTag("processPrimary", processPrimary.serializeNBT());
				tag.setTag("processSecondary", processSecondary.serializeNBT());


				tag.setInteger("processTime", processTime);
				tag.setInteger("processTimeMax", processTimeMax);

			}

			ImmersiveEngineering.packetHandler.sendToAllAround(new MessageTileSync(this, tag), pl.pabilo8.immersiveintelligence.api.Utils.targetPointFromTile(this, 32));
		}
	}

	@Override
	public SawmillRecipe findRecipeForInsertion(ItemStack inserting)
	{
		return SawmillRecipe.findRecipe(inserting);
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
}
