package pl.pabilo8.immersiveintelligence.common.blocks.metal;

import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.ITileDrop;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.utils.MachineUpgrade;
import pl.pabilo8.immersiveintelligence.client.render.metal_device.MedicalCrateRenderer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.IIPotions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Predicate;

import static pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.EffectCrates.*;

/**
 * @author Pabilo8
 * @since 06.07.2020
 */
public class TileEntityMedicalCrate extends TileEntityEffectCrate implements ITileDrop
{
	public static final Predicate<FluidStack> HEALTH_POTION = resource -> resource.getFluid()==IEContent.fluidPotion&&resource.tag!=null&&resource.tag.getString("Potion").equals("minecraft:regeneration");
	public static final Predicate<FluidStack> BOOST_POTION = resource -> resource.getFluid()==IEContent.fluidPotion&&resource.tag!=null&&resource.tag.getString("Potion").equals("minecraft:absorption");
	public static final Predicate<ItemStack> BOOST_POTION_ITEM = resource -> resource.getItem()==Items.GOLDEN_APPLE;

	public TileEntityMedicalCrate()
	{
		inventory = NonNullList.withSize(4, ItemStack.EMPTY);
		insertionHandler = new IEInventoryHandler(4, this);
	}

	public FluidTank[] tanks = new FluidTank[]{
			new FluidTank(mediCrateTankSize),
			new FluidTank(mediCrateTankSize)
	};
	FluidWrapper fluidWrapper = new FluidWrapper(this);
	public boolean shouldHeal = true;
	public boolean shouldBoost = true;

	@Override
	@Nonnull
	public ItemStack getTileDrop(EntityPlayer player, @Nonnull IBlockState state)
	{
		ItemStack tileDrop = super.getTileDrop(player, state);
		ItemNBTHelper.setTagCompound(tileDrop, "tank", tanks[0].writeToNBT(new NBTTagCompound()));
		ItemNBTHelper.setTagCompound(tileDrop, "potion_tank", tanks[1].writeToNBT(new NBTTagCompound()));
		return tileDrop;

	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		if(!descPacket)
			tanks[0].readFromNBT(nbt.getCompoundTag("tank"));
		tanks[1].readFromNBT(nbt.getCompoundTag("potion_tank"));
		shouldHeal = nbt.getBoolean("shouldHeal");
		shouldBoost = nbt.getBoolean("shouldBoost");
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);
		nbt.setTag("tank", tanks[0].writeToNBT(new NBTTagCompound()));
		nbt.setTag("potion_tank", tanks[1].writeToNBT(new NBTTagCompound()));
		nbt.setBoolean("shouldHeal", shouldHeal);
		nbt.setBoolean("shouldBoost", shouldBoost);
	}

	@Override
	boolean isSupplied()
	{
		return (shouldHeal&&tanks[0].getFluidAmount() >= mediCrateFluidDrain)||(shouldBoost&&tanks[1].getFluidAmount() >= mediCrateFluidDrain);
	}

	@Override
	void useSupplies()
	{

	}

	@Override
	public void update()
	{
		super.update();
		boolean u = Utils.handleBucketTankInteraction(tanks, inventory, 0, 1, 0, false, HEALTH_POTION);
		if(tanks[1].getFluidAmount() < tanks[1].getCapacity()-200&&BOOST_POTION_ITEM.test(inventory.get(2)))
		{
			NBTTagCompound potionNBT = new NBTTagCompound();
			potionNBT.setString("Potion", "minecraft:absorption");
			if(tanks[1].fill(new FluidStack(IEContent.fluidPotion, 250, potionNBT), true) > 0)
				inventory.get(2).shrink(1);
			u = true;
		}
		if(u)
		{
			this.markContainingBlockForUpdate(null);
			doGraphicalUpdates(0);
		}
	}

	@Override
	boolean affectEntity(Entity entity, boolean upgraded)
	{
		if(!upgraded||(repairCrateEnergyPerAction <= energyStorage))
		{
			boolean healed = false;
			if(entity instanceof EntityLivingBase)
			{
				if(!((EntityLivingBase)entity).isPotionActive(IIPotions.medical_treatment))
				{
					if(shouldHeal&&tanks[0].getFluidAmount() >= mediCrateFluidDrain)
					{
						tanks[0].drain(mediCrateFluidDrain, true);
						//upgraded: 10s / 6s effect, non: 20s, 10s effect
						((EntityLivingBase)entity).addPotionEffect(new PotionEffect(IIPotions.medical_treatment, upgraded?200: 400, upgraded?1: 0, true, true));
					}
					if(shouldBoost&&tanks[1].getFluidAmount() >= mediCrateFluidDrain)
					{
						tanks[1].drain(mediCrateFluidDrain, true);
						((EntityLivingBase)entity).addPotionEffect(new PotionEffect(IIPotions.iron_will, upgraded?200: 400, upgraded?1: 0, true, true));
					}

					healed = true;
				}
			}
			if(!upgraded&&healed)
				energyStorage -= mediCrateEnergyPerAction;
			return healed;
		}
		return false;
	}

	@Override
	boolean checkEntity(Entity entity)
	{
		return entity instanceof EntityLivingBase&&
				((EntityLivingBase)entity).getHealth()!=((EntityLivingBase)entity).getMaxHealth();
	}

	@Override
	public int getGuiID()
	{
		return IIGuiList.GUI_MEDICRATE.ordinal();
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderWithUpgrades(MachineUpgrade... upgrades)
	{
		MedicalCrateRenderer.renderWithUpgrade(upgrades);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if(hasUpgrade(IIContent.UPGRADE_INSERTER)&&facing==this.facing.getOpposite()&&capability==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return true;
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(hasUpgrade(IIContent.UPGRADE_INSERTER)&&facing==this.facing.getOpposite()&&capability==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return (T)fluidWrapper;
		return super.getCapability(capability, facing);
	}

	public static class FluidWrapper implements IFluidHandler
	{
		final TileEntityMedicalCrate tile;

		public FluidWrapper(TileEntityMedicalCrate tile)
		{
			this.tile = tile;
		}

		@Override
		public IFluidTankProperties[] getTankProperties()
		{
			IFluidTankProperties[] array = new IFluidTankProperties[2];
			for(int i = 0; i < tile.tanks.length; i++)
				array[i] = new FluidTankProperties(tile.tanks[i].getFluid(), tile.tanks[i].getCapacity());
			return array;
		}

		@Override
		public int fill(FluidStack resource, boolean doFill)
		{
			int fill = -1;
			for(int i = 0; i < tile.tanks.length; i++)
			{
				IFluidTank tank = tile.tanks[i];
				if(tank!=null&&tile.canFillTankFrom(i, resource)&&tank.getFluid()!=null&&tank.getFluid().isFluidEqual(resource))
				{
					fill = tank.fill(resource, doFill);
					if(fill > 0)
						break;
				}
			}
			if(fill==-1)
				for(int i = 0; i < tile.tanks.length; i++)
				{
					IFluidTank tank = tile.tanks[i];
					if(tank!=null&&this.tile.canFillTankFrom(i, resource))
					{
						fill = tank.fill(resource, doFill);
						if(fill > 0)
							break;
					}
				}
			if(fill > 0)
				this.tile.markBlockForUpdate(tile.pos, null);
			return Math.max(fill, 0);
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

	private boolean canFillTankFrom(int i, FluidStack resource)
	{
		return i==0?HEALTH_POTION.test(resource): BOOST_POTION.test(resource);
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
		if(part==1)
			shouldHeal = state;
		else if(part==2)
			shouldBoost = state;
		else
			super.onAnimationChangeServer(state, part);
	}
}
