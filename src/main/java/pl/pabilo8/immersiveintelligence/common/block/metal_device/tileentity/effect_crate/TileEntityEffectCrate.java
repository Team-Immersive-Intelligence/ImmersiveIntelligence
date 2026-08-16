package pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.effect_crate;

import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorage;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.ITileDrop;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.ILootContainer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.api.upgrade.IManagedUpgradableDevice;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeManager;
import pl.pabilo8.immersiveintelligence.api.utils.IBooleanAnimatedPartsBlock;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EntityReference;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.NBTSerialisation;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.entity.IIEntityUtils;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IIIGuiMultiblockTile;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IIIInventory;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;
import pl.pabilo8.immersiveintelligence.common.util.tile.TileEntityIIDirectional.FacingLimitation;
import pl.pabilo8.immersiveintelligence.common.util.tile.TileEntityIIDirectional.FacingSettings;
import pl.pabilo8.immersiveintelligence.common.util.tile.TileEntityIIDirectionalConnectable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

import static blusunrize.immersiveengineering.api.energy.wires.WireType.LV_CATEGORY;
import static blusunrize.immersiveengineering.api.energy.wires.WireType.MV_CATEGORY;
import static pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.EffectCrates.energyDrain;
import static pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.EffectCrates.maxEnergyStored;

/**
 * Provides shared inventory, upgrade, wire, GUI, and entity-effect logic for effect crates.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 10.08.2026
 * @since 06.07.2020
 */
public abstract class TileEntityEffectCrate extends TileEntityIIDirectionalConnectable implements
		IBooleanAnimatedPartsBlock, ITickable, IManagedUpgradableDevice<TileEntityEffectCrate>, IPlayerInteraction,
		IBlockBounds, IIIInventory, IIIGuiMultiblockTile, ITileDrop, ILootContainer
{
	private static final FacingSettings FACING_SETTINGS = new FacingSettings(FacingLimitation.HORIZONTAL).withRotation(true);

	@SyncNBT(name = "lootTable")
	public String lootTable = "";
	@SyncNBT(nullable = true)
	public String name;
	@SyncNBT(events = SyncEvents.TILE_CUSTOM1)
	public MultiblockInteractablePart lid;
	@SyncNBT(events = {SyncEvents.TILE_ENERGY_CHANGED, SyncEvents.TILE_GUI_OPENED})
	public FluxStorage energyStorage = new FluxStorage(maxEnergyStored);
	@SyncNBT(events = {SyncEvents.TILE_CUSTOM1, SyncEvents.TILE_GUI_OPENED})
	public UpgradeManager<TileEntityEffectCrate> upgradeManager = new UpgradeManager<>(this);
	@SyncNBT(events = SyncEvents.TILE_CUSTOM2)
	public EntityReference<Entity> focusedEntity;
	@SyncNBT(events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_DROP_AS_ITEM})
	public NonNullList<ItemStack> inventory;
	@Nonnull
	public IItemHandler insertionHandler;

	//Client animation
	float inserterAnimation = 0f, inserterHeight = 0f, inserterAngle = 0f;

	protected TileEntityEffectCrate(NonNullList<ItemStack> inventory, BiFunction<Integer, IIEInventory, IItemHandler> insertionHandler)
	{
		this.inventory = inventory;
		this.insertionHandler = insertionHandler.apply(inventory.size(), this);
		this.lid = new MultiblockInteractablePart(0, 7.5f, 1.5f);
		this.focusedEntity = new EntityReference<>(this::getWorld);
	}

	//--- Facing ---//

	@Nonnull
	@Override
	protected FacingSettings getFacingSettings()
	{
		return FACING_SETTINGS;
	}

	//--- Inventory and drops ---//

	@Override
	public NonNullList<ItemStack> getInventory()
	{
		return inventory;
	}

	@Override
	public ItemStack getTileDrop(EntityPlayer player, IBlockState state)
	{
		ItemStack stack = new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));
		NBTTagCompound nbt = new NBTTagCompound();
		//noinspection unchecked
		NBTSerialisation.synchroniseFor(this, (tag, tile) ->
				tag.serializeForEvent(tile, nbt, SyncEvents.TILE_DROP_AS_ITEM));
		if(!nbt.hasNoTags())
			stack.setTagCompound(nbt);
		if(name!=null)
			stack.setStackDisplayName(name);
		return stack;
	}

	@Override
	public void readOnPlacement(EntityLivingBase placer, ItemStack stack)
	{
		//Read NBT
		NBTTagCompound nbt = ItemNBTHelper.getTag(stack);
		//noinspection unchecked
		NBTSerialisation.synchroniseFor(this, (tag, entity) -> tag.deserializeAll(this, nbt, true));

		//Set display name
		if(stack.hasDisplayName())
			name = stack.getDisplayName();
	}

	@Override
	public boolean preventInventoryDrop()
	{
		return true;
	}

	//--- Display ---//

	@Nullable
	@Override
	public ITextComponent getDisplayName()
	{
		return name!=null?new TextComponentString(name): null;
	}

	//--- Tick logic ---//

	@Override
	public void update()
	{
		lid.update();

		if(world.isRemote)
		{
			if(energyStorage.getEnergyStored() > 0&&isUpgradeInstalled(IIContent.UPGRADE_INSERTER))
			{
				inserterAnimation = calculateInserterAnimation(0);
				inserterHeight = calculateInserterHeight(0);
				inserterAngle = calculateInserterAngle(0);
			}
			return;
		}

		if(energyStorage.getEnergyStored() <= energyDrain||!isUpgradeInstalled(IIContent.UPGRADE_INSERTER)||!isSupplied())
		{
			setFocusedEntity(null);
			return;
		}
		if(world.getTotalWorldTime()%getEffectTime()!=0)
			return;

		List<Entity> entities = world.getEntitiesWithinAABB(Entity.class,
				new AxisAlignedBB(getPos()).offset(0.5, 0.5, 0.5).grow(getRange()));
		entities.removeIf(entity -> !checkEntity(entity));

		if(entities.isEmpty())
		{
			setFocusedEntity(null);
			return;
		}

		Entity target = entities.get(0);
		setFocusedEntity(target);
		if(affectEntityUpgraded(target))
			useSupplies();
	}

	public float calculateInserterAnimation(float partialTicks)
	{
		Entity focused = focusedEntity.get();
		if(focused!=null)
			return Math.min(inserterAnimation+(0.05f*(1+partialTicks)), 1f);
		return Math.max(inserterAnimation-(0.025f*(1+partialTicks)), 0f);
	}

	public float calculateInserterHeight(float partialTicks)
	{
		Entity focused = focusedEntity.get();
		if(focused!=null)
			return MathHelper.clamp((float)(pos.getY()+1.35f-focused.posY-(partialTicks*focused.motionY)), -1f, 1f);
		return Math.signum(inserterHeight)*Math.abs(inserterHeight-(0.1f*(1+partialTicks)));
	}

	public float calculateInserterAngle(float partialTicks)
	{
		Entity focused = focusedEntity.get();
		if(focused!=null)
		{
			Vec3d vec3d = IIEntityUtils.getEntityCenter(focused)
					.add(new Vec3d(focused.motionX, 0, focused.motionZ).scale(partialTicks))
					.subtract(new Vec3d(pos));
			float yaw;
			if(vec3d.x < 0&&vec3d.z >= 0)
				yaw = (float)(Math.atan(Math.abs(vec3d.x/vec3d.z))/Math.PI*180D);
			else if(vec3d.x <= 0&&vec3d.z <= 0)
				yaw = (float)(Math.atan(Math.abs(vec3d.z/vec3d.x))/Math.PI*180D)+90;
			else if(vec3d.x >= 0&&vec3d.z < 0)
				yaw = (float)(Math.atan(Math.abs(vec3d.x/vec3d.z))/Math.PI*180D)+180;
			else
				yaw = (float)(Math.atan(Math.abs(vec3d.z/vec3d.x))/Math.PI*180D)+270;
			return yaw;
		}
		return inserterAngle;
	}

	protected void setFocusedEntity(@Nullable Entity entity)
	{
		if(focusedEntity.get()==entity)
			return;
		focusedEntity.set(entity);
		if(world!=null&&!world.isRemote)
			updateTileForEvent(SyncEvents.TILE_CUSTOM2);
	}

	//--- Interaction and animation state ---//

	@Override
	public boolean interact(EnumFacing side, EntityPlayer player, EnumHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		if(isUpgradeInstalled(IIContent.UPGRADE_INSERTER))
		{
			if(lid.getState())
				setLidState(false);
			return false;
		}

		if(player.isSneaking())
		{
			setLidState(!lid.getState());
			return true;
		}
		if(lid.getState()&&isSupplied())
		{
			if(!world.isRemote&&affectEntityBasic(player))
				useSupplies();
			return true;
		}
		return false;
	}

	protected void setLidState(boolean state)
	{
		if(!lid.setState(state))
			return;
		if(world!=null&&!world.isRemote)
			updateTileForEvent(SyncEvents.TILE_CUSTOM1);
	}

	@Override
	public void onAnimationChangeClient(boolean state, int part)
	{
		MultiblockInteractablePart.setStates(state, part, lid);
	}

	@Override
	public void onAnimationChangeServer(boolean state, int part)
	{
		if(MultiblockInteractablePart.setStates(state, part, lid)!=null)
			updateTileForEvent(SyncEvents.TILE_CUSTOM1);
	}

	//--- Upgrades ---//

	@Override
	public TileEntityEffectCrate master()
	{
		return this;
	}

	@Nonnull
	@Override
	public UpgradeManager<TileEntityEffectCrate> getUpgradeManager()
	{
		return upgradeManager;
	}

	//--- Wire system ---//

	@Override
	public boolean acceptsWireType(WireType wireType)
	{
		String category = wireType.getCategory();
		return MV_CATEGORY.equals(category)||LV_CATEGORY.equals(category);
	}

	@Override
	public boolean isRelay()
	{
		return false;
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
		if(amount <= 0)
			return 0;

		boolean wasEmpty = energyStorage.getEnergyStored()==0;
		int received = energyStorage.receiveEnergy(Math.min(amount, energyDrain), simulate);
		if(!simulate&&received > 0&&wasEmpty)
			updateTileForEvent(SyncEvents.TILE_ENERGY_CHANGED);
		return received;
	}

	@Override
	public Vec3d getConnectionOffset(Connection con)
	{
		return new Vec3d(0.5, 0.9375, 0.5);
	}

	//--- Effect hooks ---//

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

	protected void consumeEnergy(int amount)
	{
		if(energyStorage.extractEnergy(amount, false) > 0&&world!=null&&!world.isRemote)
			updateTileForEvent(SyncEvents.TILE_ENERGY_CHANGED);
	}

	//--- Block and GUI ---//

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
	public boolean canOpenGui()
	{
		return !lid.getState();
	}

	@Override
	public void onGuiOpened(@Nullable EntityPlayer player, boolean clientside)
	{
		ResourceLocation lootTableLocation = getLootTable();
		if(lootTableLocation!=null&&!clientside)
		{
			LootTable table = world.getLootTableManager().getLootTableFromLocation(lootTableLocation);
			lootTable = "";
			LootContext.Builder contextBuilder = new LootContext.Builder((WorldServer)world);
			if(player!=null)
				contextBuilder.withLuck(player.getLuck());
			LootContext context = contextBuilder.build();
			Random random = new Random();

			List<ItemStack> loot = table.generateLootForPools(random, context);
			List<Integer> freeSlots = Lists.newArrayList();
			for(int i = 0; i < inventory.size(); i++)
				if(inventory.get(i).isEmpty())
					freeSlots.add(i);
			Collections.shuffle(freeSlots, random);
			if(!freeSlots.isEmpty())
			{
				Utils.shuffleLootItems(loot, freeSlots.size(), random);
				for(ItemStack stack : loot)
					inventory.set(freeSlots.remove(freeSlots.size()-1), stack);
			}
		}

	}

	//--- Capabilities ---//

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		return capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY||super.hasCapability(capability, facing);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T)insertionHandler;
		return super.getCapability(capability, facing);
	}

	//--- Loot container and upgrade context ---//

	@Override
	@Nullable
	public ResourceLocation getLootTable()
	{
		return lootTable.isEmpty()?null: new ResourceLocation(lootTable);
	}
}
