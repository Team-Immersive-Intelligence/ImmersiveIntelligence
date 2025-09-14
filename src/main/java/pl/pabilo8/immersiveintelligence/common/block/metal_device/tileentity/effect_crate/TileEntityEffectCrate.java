package pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.effect_crate;

import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.TileEntityImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.*;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.ILootContainer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.upgrade.IManagedUpgradableDevice;
import pl.pabilo8.immersiveintelligence.api.upgrade.IUpgradableDevice;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeManager;
import pl.pabilo8.immersiveintelligence.api.utils.IBooleanAnimatedPartsBlock;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBooleanAnimatedPartsSync;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;
import pl.pabilo8.immersiveintelligence.common.util.entity.IIEntityUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.EffectCrates.energyDrain;
import static pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.EffectCrates.maxEnergyStored;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 06.07.2020
 */
public abstract class TileEntityEffectCrate extends TileEntityImmersiveConnectable implements IDirectionalTile, IBooleanAnimatedPartsBlock, ITickable, IManagedUpgradableDevice<TileEntityEffectCrate>, IPlayerInteraction, IBlockBounds, IIEInventory, IGuiTile, ITileDrop, IComparatorOverride, ILootContainer
{
	public ResourceLocation lootTable;
	public EnumFacing facing = EnumFacing.NORTH;
	public String name;
	public boolean open = false;
	public float lidAngle = 0;
	public int energyStorage = 0;
	@Nonnull
	public IItemHandler insertionHandler;
	public UpgradeManager<TileEntityEffectCrate> upgradeManager = new UpgradeManager<>(this);

	//Client only
	float inserterAnimation = 0f;
	float inserterHeight = 0f;
	float inserterAngle = 0f;
	Entity focusedEntity = null;

	@Nonnull
	NonNullList<ItemStack> inventory;

	@Override
	public EnumFacing getFacing()
	{
		return facing;
	}

	@Override
	public void setFacing(EnumFacing facing)
	{
		this.facing = facing;
		if(facing.getAxis().isVertical())
			this.facing = EnumFacing.NORTH;
	}

	@Override
	public int getFacingLimitation()
	{
		return 2;
	}

	@Override
	public boolean mirrorFacingOnPlacement(EntityLivingBase placer)
	{
		return false;
	}

	@Override
	public boolean canHammerRotate(EnumFacing side, float hitX, float hitY, float hitZ, EntityLivingBase entity)
	{
		return !entity.isSneaking();
	}

	@Override
	public boolean canRotate(EnumFacing axis)
	{
		return !axis.getAxis().isVertical();
	}

	@Override
	@Nullable
	public ITextComponent getDisplayName()
	{
		return name!=null?new TextComponentString(name): new TextComponentTranslation("tile."+ImmersiveIntelligence.MODID+".metal_device.metal_crate.name");
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return 64;
	}

	@Override
	public NonNullList<ItemStack> getInventory()
	{
		return inventory;
	}

	@Override
	public void doGraphicalUpdates(int slot)
	{
		this.markDirty();
	}

	@Override
	public ItemStack getTileDrop(EntityPlayer player, IBlockState state)
	{
		ItemStack stack = new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));
		NBTTagCompound tag = new NBTTagCompound();
		tag.setTag("inventory", Utils.writeInventory(inventory));
		if(!tag.hasNoTags())
			stack.setTagCompound(tag);
		if(this.name!=null)
			stack.setStackDisplayName(this.name);
		return stack;
	}

	@Override
	public void readOnPlacement(EntityLivingBase placer, ItemStack stack)
	{
		if(stack.hasTagCompound())
		{
			readCustomNBT(stack.getTagCompound(), false);
			if(stack.hasDisplayName())
				this.name = stack.getDisplayName();
		}
	}

	@Override
	public boolean preventInventoryDrop()
	{
		return true;
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		if(nbt.hasKey("name"))
			this.name = nbt.getString("name");
		if(nbt.hasKey("open"))
			open = nbt.getBoolean("open");
		if(nbt.hasKey("facing"))
			setFacing(EnumFacing.getFront(nbt.getInteger("facing")));

		energyStorage = nbt.getInteger("energyStorage");
		if(!descPacket)
		{
			if(nbt.hasKey("lootTable", 8))
				this.lootTable = new ResourceLocation(nbt.getString("lootTable"));
			else
				inventory = Utils.readInventory(nbt.getTagList("inventory", 10), inventory.size());
		}

	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		if(name!=null)
			nbt.setString("name", name);
		nbt.setBoolean("open", open);
		nbt.setInteger("facing", facing.getIndex());
		nbt.setInteger("energyStorage", energyStorage);
		if(!descPacket)
		{
			if(lootTable!=null)
				nbt.setString("lootTable", lootTable.toString());
			else
				nbt.setTag("inventory", Utils.writeInventory(inventory));
		}
	}

	@Override
	public void onAnimationChangeClient(boolean state, int part)
	{
		open = state;
	}

	@Override
	public void onAnimationChangeServer(boolean state, int part)
	{
		open = state;
	}

	@Override
	public void receiveMessageFromServer(NBTTagCompound message)
	{
		if(message.hasKey("focused"))
			focusedEntity = world.getEntityByID(message.getInteger("focused"));
		else
			focusedEntity = null;
	}

	@Override
	public void update()
	{
		updateLid();
		if(focusedEntity!=null)
		{
			focusedEntity = null;
			IIPacketHandler.sendToClient(this, new MessageIITileSync(this, makeSyncEntity()));
		}

		//TODO: 23.05.2023 animation
		if(world.isRemote)
		{
			if(energyStorage > 0&&isUpgradeInstalled(IIContent.UPGRADE_INSERTER))
			{
				inserterAnimation = calculateInserterAnimation(0);
				inserterHeight = calculateInserterHeight(0);
				inserterAngle = calculateInserterAngle(0);
			}
		}
		else if(energyStorage > energyDrain&&isUpgradeInstalled(IIContent.UPGRADE_INSERTER)&&isSupplied()&&world.getTotalWorldTime()%getEffectTime()==0)
		{
			//get all in range
			//effect
			List<Entity> entitiesWithinAABB = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(getPos()).offset(0.5, 0.5, 0.5).grow(getRange()));
			entitiesWithinAABB.removeIf(entity -> !checkEntity(entity));
			if(entitiesWithinAABB.size() > 0)
			{

				if(affectEntityUpgraded(entitiesWithinAABB.get(0)))
					useSupplies();
				if(!entitiesWithinAABB.contains(focusedEntity))
				{
					focusedEntity = entitiesWithinAABB.get(0);
					inserterAnimation = 0f;
					inserterHeight = 0f;
					IIPacketHandler.sendToClient(this, new MessageIITileSync(this, makeSyncEntity()));
				}
			}
			else if(focusedEntity!=null)
			{
				focusedEntity = null;
				inserterAnimation = 0f;
				inserterHeight = 0f;
				IIPacketHandler.sendToClient(this, new MessageIITileSync(this, makeSyncEntity()));
			}
		}
	}

	public float calculateInserterAnimation(float partialTicks)
	{
		if(focusedEntity!=null)
			return Math.min(inserterAnimation+(0.05f*(1+partialTicks)), 1f);
		else
			return Math.max(inserterAnimation-(0.025f*(1+partialTicks)), 0f);
	}

	public float calculateInserterHeight(float partialTicks)
	{
		if(focusedEntity!=null)
			return MathHelper.clamp((float)(pos.getY()+1.35f-focusedEntity.posY-(partialTicks*focusedEntity.motionY)), -1f, 1f);
		else
			return Math.signum(inserterHeight)*Math.abs(inserterHeight-(0.1f*(1+partialTicks)));
	}

	public float calculateInserterAngle(float partialTicks)
	{
		if(focusedEntity!=null)
		{
			//Subtracts two vector and calculates angle (in degrees) using atan
			Vec3d vec3d = IIEntityUtils.getEntityCenter(focusedEntity)
					.add(new Vec3d(focusedEntity.motionX, 0, focusedEntity.motionZ).scale(partialTicks))
					.subtract(new Vec3d(this.pos));
			float yaw;
			if(vec3d.x < 0&&vec3d.z >= 0)
				yaw = (float)(Math.atan(Math.abs(vec3d.x/vec3d.z))/Math.PI*180D);
			else if(vec3d.x <= 0&&vec3d.z <= 0)
				yaw = (float)(Math.atan(Math.abs(vec3d.z/vec3d.x))/Math.PI*180D)+90;
			else if(vec3d.x >= 0&&vec3d.z < 0)
				yaw = (float)(Math.atan(Math.abs(vec3d.x/vec3d.z))/Math.PI*180D)+180;
			else
				yaw = (float)(Math.atan(Math.abs(vec3d.z/vec3d.x))/Math.PI*180D)+270;
			// TODO: 04.10.2020 calculates nearest path (+/-) to angle, and goes toward
			//float angle = inserterAngle;

			return yaw;
		}
		return inserterAngle;
	}

	protected NBTTagCompound makeSyncEntity()
	{
		NBTTagCompound tag = new NBTTagCompound();
		if(focusedEntity!=null)
			tag.setInteger("focused", focusedEntity.getEntityId());
		else
			tag.setBoolean("hasNoFocus", true);
		return tag;
	}

	@Override
	public boolean interact(EnumFacing side, EntityPlayer player, EnumHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		if(isUpgradeInstalled(IIContent.UPGRADE_INSERTER))
		{
			if(open)
				IIPacketHandler.INSTANCE.sendToDimension(new MessageBooleanAnimatedPartsSync(0, open = false, this.pos), this.world.provider.getDimension());

			return false;
		}

		if(player.isSneaking())
		{
			open = !open;
			IIPacketHandler.INSTANCE.sendToDimension(new MessageBooleanAnimatedPartsSync(0, open, this.pos), this.world.provider.getDimension());
			return true;
		}
		else if(open&&isSupplied())
		{
			affectEntityBasic(player);
			useSupplies();
			return true;
		}

		return false;
	}

	@Override
	public IUpgradableDevice master()
	{
		return this;
	}

	@Nonnull
	@Override
	public UpgradeManager<TileEntityEffectCrate> getUpgradeManager()
	{
		return upgradeManager;
	}

	@Override
	protected boolean canTakeLV()
	{
		return true;
	}

	@Override
	protected boolean canTakeMV()
	{
		return true;
	}

	@Override
	public boolean isEnergyOutput()
	{
		return true;
	}

	@Override
	public boolean canConnectCable(WireType cableType, TargetingInfo target, Vec3i offset)
	{
		return isUpgradeInstalled(IIContent.UPGRADE_INSERTER)&&super.canConnectCable(cableType, target, offset);
	}

	@Override
	public int outputEnergy(int amount, boolean simulate, int energyType)
	{
		if(amount > 0&&energyStorage < maxEnergyStored)
		{
			if(!simulate)
			{
				int rec = Math.min(maxEnergyStored-energyStorage, energyDrain);
				energyStorage += rec;
				return rec;
			}
			return Math.min(maxEnergyStored-energyStorage, energyDrain);
		}
		return 0;
	}

	@Override
	public Vec3d getConnectionOffset(Connection con)
	{
		return new Vec3d(0.5, 0.9375, 0.5);
	}

	private void updateLid()
	{
		if(open&&lidAngle < 1.5f)
			lidAngle = Math.min(lidAngle+0.2f, 1.5f);
		else if(!open&&lidAngle > 0f)
			lidAngle = Math.max(lidAngle-0.3f, 0f);
	}

	abstract boolean isSupplied();

	abstract void useSupplies();

	int getEffectTime()
	{
		return 20;
	}

	int getRange()
	{
		return 3;
	}

	private boolean affectEntityUpgraded(Entity entity)
	{
		return affectEntity(entity, true);
	}

	private boolean affectEntityBasic(Entity entity)
	{
		return affectEntity(entity, false);
	}

	abstract boolean affectEntity(Entity entity, boolean upgraded);

	abstract boolean checkEntity(Entity entity);

	@Nonnull
	@Override
	public float[] getBlockBounds()
	{
		if(isUpgradeInstalled(IIContent.UPGRADE_INSERTER))
			return new float[]{0f, 0f, 0f, 1f, 0.8125f, 1f};
		if(facing==EnumFacing.NORTH||facing==EnumFacing.SOUTH)
			return new float[]{0f, 0f, .25f, 1f, .58f, .75f};

		return new float[]{.25f, 0f, 0f, .75f, .58f, 1f};
	}

	@Override
	public int getComparatorInputOverride()
	{
		return Utils.calcRedstoneFromInventory(this);
	}

	@Override
	public boolean canOpenGui()
	{
		return !open;
	}

	@Override
	public TileEntity getGuiMaster()
	{
		return this;
	}

	@Override
	public void onGuiOpened(@Nullable EntityPlayer player, boolean clientside)
	{
		if(this.lootTable!=null&&!clientside)
		{
			LootTable loottable = this.world.getLootTableManager().getLootTableFromLocation(this.lootTable);
			this.lootTable = null;
			LootContext.Builder contextBuilder = new LootContext.Builder((WorldServer)this.world);
			if(player!=null)
				contextBuilder.withLuck(player.getLuck());
			LootContext context = contextBuilder.build();
			Random rand = new Random();

			List<ItemStack> list = loottable.generateLootForPools(rand, context);
			List<Integer> listSlots = Lists.newArrayList();
			for(int i = 0; i < inventory.size(); i++)
				if(inventory.get(i).isEmpty())
					listSlots.add(i);
			Collections.shuffle(listSlots, rand);
			if(listSlots.isEmpty())
				return;
			Utils.shuffleLootItems(list, listSlots.size(), rand);
			for(ItemStack itemstack : list)
			{
				int slot = listSlots.remove(listSlots.size()-1);
				inventory.set(slot, itemstack);
			}
			this.markDirty();
		}
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return true;
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T)insertionHandler;
		return super.getCapability(capability, facing);
	}

	@Override
	public ResourceLocation getLootTable()
	{
		return this.lootTable;
	}
}
