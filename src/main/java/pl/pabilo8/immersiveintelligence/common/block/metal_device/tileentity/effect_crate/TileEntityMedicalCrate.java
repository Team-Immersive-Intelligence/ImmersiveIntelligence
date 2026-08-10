package pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.effect_crate;

import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.UpgradeTier;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IIPotions;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;

import javax.annotation.Nullable;
import java.util.function.Predicate;

import static pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.EffectCrates.*;

/**
 * Stores medical fluids and applies configured treatment effects to living entities.
 *
 * @author Pabilo8(pabilo@iiteam.net)
 * @updated 10.08.2026
 * @since 06.07.2020
 */
public class TileEntityMedicalCrate extends TileEntityEffectCrate
{
	public static final Predicate<FluidStack> HEALTH_POTION = resource -> resource.getFluid()==IEContent.fluidPotion&&resource.tag!=null&&resource.tag.getString("Potion").equals("minecraft:regeneration");
	public static final Predicate<FluidStack> BOOST_POTION = resource -> resource.getFluid()==IEContent.fluidPotion&&resource.tag!=null&&resource.tag.getString("Potion").equals("minecraft:absorption");
	public static final Predicate<ItemStack> BOOST_POTION_ITEM = resource -> resource.getItem()==Items.GOLDEN_APPLE;

	static
	{
		UpgradeTechTree.getTreeFor(TileEntityMedicalCrate.class)
				.withUpgrade(IIContent.UPGRADE_INSERTER, UpgradeTier.TIER_1);
	}

	@SyncNBT(name = "tank", events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_DROP_AS_ITEM})
	public FluidTank healthTank = new FluidTank(mediCrateTankSize);
	@SyncNBT(name = "potion_tank", events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_DROP_AS_ITEM})
	public FluidTank boostTank = new FluidTank(mediCrateTankSize);
	public FluidTank[] tanks = {healthTank, boostTank};
	@SyncNBT(name = "shouldHeal", events = SyncEvents.TILE_CUSTOM1)
	public boolean shouldHeal = true;
	@SyncNBT(name = "shouldBoost", events = SyncEvents.TILE_CUSTOM1)
	public boolean shouldBoost = true;

	private final FluidWrapper fluidWrapper = new FluidWrapper(this);

	public TileEntityMedicalCrate()
	{
		super(NonNullList.withSize(4, ItemStack.EMPTY), IEInventoryHandler::new);
	}

	@Override
	public IIGUI getGUI()
	{
		return IIGUI.MEDIC_CRATE;
	}

	@Override
	boolean isSupplied()
	{
		return shouldHeal&&healthTank.getFluidAmount() >= mediCrateFluidDrain||
				shouldBoost&&boostTank.getFluidAmount() >= mediCrateFluidDrain;
	}

	@Override
	void useSupplies()
	{
	}

	@Override
	public void update()
	{
		super.update();
		if(world.isRemote)
			return;

		boolean changed = IIUtils.handleBucketTankInteraction(tanks, inventory, 0, 1, 0, false, HEALTH_POTION);
		if(boostTank.getFluidAmount() < boostTank.getCapacity()-200&&BOOST_POTION_ITEM.test(inventory.get(2)))
		{
			NBTTagCompound potionNBT = new NBTTagCompound();
			potionNBT.setString("Potion", "minecraft:absorption");
			if(boostTank.fill(new FluidStack(IEContent.fluidPotion, 250, potionNBT), true) > 0)
			{
				inventory.get(2).shrink(1);
				changed = true;
			}
		}

		if(changed)
			updateTileForEvent(SyncEvents.TILE_GUI_OPENED);
	}

	@Override
	boolean affectEntity(Entity entity, boolean upgraded)
	{
		if(upgraded&&energyStorage < mediCrateEnergyPerAction||!(entity instanceof EntityLivingBase))
			return false;

		EntityLivingBase living = (EntityLivingBase)entity;
		if(living.isPotionActive(IIPotions.medicalTreatment))
			return false;

		boolean treated = false;
		if(shouldHeal&&healthTank.getFluidAmount() >= mediCrateFluidDrain)
		{
			healthTank.drain(mediCrateFluidDrain, true);
			living.addPotionEffect(new PotionEffect(IIPotions.medicalTreatment, upgraded?200: 400, upgraded?1: 0, true, true));
			treated = true;
		}
		if(shouldBoost&&boostTank.getFluidAmount() >= mediCrateFluidDrain)
		{
			boostTank.drain(mediCrateFluidDrain, true);
			living.addPotionEffect(new PotionEffect(IIPotions.ironWill, upgraded?200: 400, upgraded?1: 0, true, true));
			treated = true;
		}

		if(treated)
		{
			if(upgraded)
				consumeEnergy(mediCrateEnergyPerAction);
			updateTileForEvent(SyncEvents.TILE_GUI_OPENED);
		}
		return treated;
	}

	@Override
	boolean checkEntity(Entity entity)
	{
		if(!(entity instanceof EntityLivingBase))
			return false;
		EntityLivingBase living = (EntityLivingBase)entity;
		return living.getHealth()!=living.getMaxHealth();
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return true;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		if(isUpgradeInstalled(IIContent.UPGRADE_INSERTER)&&facing==this.facing.getOpposite()&&capability==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return true;
		return super.hasCapability(capability, facing);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if(isUpgradeInstalled(IIContent.UPGRADE_INSERTER)&&facing==this.facing.getOpposite()&&capability==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return (T)fluidWrapper;
		return super.getCapability(capability, facing);
	}

	private boolean canFillTankFrom(int index, FluidStack resource)
	{
		return index==0?HEALTH_POTION.test(resource): BOOST_POTION.test(resource);
	}

	@Override
	public void onAnimationChangeClient(boolean state, int part)
	{
		if(part==1)
			shouldHeal = state;
		else if(part==2)
			shouldBoost = state;
		else
			super.onAnimationChangeClient(state, part);
	}

	@Override
	public void onAnimationChangeServer(boolean state, int part)
	{
		boolean changed;
		if(part==1)
		{
			changed = shouldHeal!=state;
			shouldHeal = state;
		}
		else if(part==2)
		{
			changed = shouldBoost!=state;
			shouldBoost = state;
		}
		else
		{
			super.onAnimationChangeServer(state, part);
			return;
		}
		if(changed)
			updateTileForEvent(SyncEvents.TILE_CUSTOM1);
	}

	public static class FluidWrapper implements IFluidHandler
	{
		private final TileEntityMedicalCrate tile;

		public FluidWrapper(TileEntityMedicalCrate tile)
		{
			this.tile = tile;
		}

		@Override
		public IFluidTankProperties[] getTankProperties()
		{
			IFluidTankProperties[] properties = new IFluidTankProperties[tile.tanks.length];
			for(int i = 0; i < tile.tanks.length; i++)
				properties[i] = new FluidTankProperties(tile.tanks[i].getFluid(), tile.tanks[i].getCapacity());
			return properties;
		}

		@Override
		public int fill(FluidStack resource, boolean doFill)
		{
			int filled = fillMatching(resource, doFill, true);
			if(filled==0)
				filled = fillMatching(resource, doFill, false);
			if(doFill&&filled > 0&&tile.getWorld()!=null&&!tile.getWorld().isRemote)
				tile.updateTileForEvent(SyncEvents.TILE_GUI_OPENED);
			return filled;
		}

		private int fillMatching(FluidStack resource, boolean doFill, boolean requireSameFluid)
		{
			for(int i = 0; i < tile.tanks.length; i++)
			{
				IFluidTank tank = tile.tanks[i];
				if(!tile.canFillTankFrom(i, resource))
					continue;
				if(requireSameFluid&&(tank.getFluid()==null||!tank.getFluid().isFluidEqual(resource)))
					continue;

				int filled = tank.fill(resource, doFill);
				if(filled > 0)
					return filled;
			}
			return 0;
		}

		@Nullable
		@Override
		public FluidStack drain(FluidStack resource, boolean doDrain)
		{
			return null;
		}

		@Nullable
		@Override
		public FluidStack drain(int maxDrain, boolean doDrain)
		{
			return null;
		}
	}
}
