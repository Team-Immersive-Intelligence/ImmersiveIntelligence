package pl.pabilo8.immersiveintelligence.common.item.tools.backpack;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.tool.IElectricEquipment;
import blusunrize.immersiveengineering.common.Config.IEConfig.Machines;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces.IColouredItem;
import blusunrize.immersiveengineering.common.items.ItemPowerpack;
import blusunrize.immersiveengineering.common.util.EnergyHelper;
import blusunrize.immersiveengineering.common.util.EnergyHelper.IIEEnergyItem;
import blusunrize.immersiveengineering.common.util.IEDamageSources.ElectricDamageSource;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * Supplies energy to the wearer's equipment.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 04.11.2026
 * @since 23.01.2021
 */
@IIItemProperties(category = IICategory.TOOLS)
public class ItemIIAdvancedPowerPack extends ItemIIBackpackBase implements IIEEnergyItem, IElectricEquipment, IColouredItem
{
	public static final String NBT_COLOR = "II:AdvancedPowerpackColour";

	public ItemIIAdvancedPowerPack()
	{
		super("advanced_powerpack");
	}

	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag)
	{
		String stored = this.getEnergyStored(stack)+"/"+this.getMaxEnergyStored(stack);
		list.add(I18n.format(Lib.DESC+"info.energyStored", TextFormatting.GOLD+stored+TextFormatting.RESET));
		String hexCol = Integer.toHexString(getColor(stack));
		list.add(I18n.format(Lib.DESC_INFO+"colour", "<hexcol="+hexCol+":#"+hexCol+">"));
	}

	@Override
	public boolean onWornTick(EntityLivingBase wearer, ItemStack stack)
	{
		int stored = getEnergyStored(stack);
		int remaining = stored;
		for(EntityEquipmentSlot slot : EntityEquipmentSlot.values())
		{
			if(remaining <= 0)
				break;
			ItemStack equipment = wearer.getItemStackFromSlot(slot);
			if(!(equipment.getItem() instanceof ItemIIAdvancedPowerPack||equipment.getItem() instanceof ItemPowerpack)
					&&EnergyHelper.isFluxItem(equipment))
				remaining -= EnergyHelper.insertFlux(equipment, Math.min(remaining, 4096), false);
		}
		if(remaining==stored)
			return false;
		EnergyHelper.extractFlux(stack, stored-remaining, false);
		return true;
	}

	@Override
	public int getMaxEnergyStored(ItemStack container)
	{
		return Tools.advancedPowerpackCapacity;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
	{
		if(!stack.isEmpty())
			return new ICapabilityProvider()
			{
				final EnergyHelper.ItemEnergyStorage energyStorage = new EnergyHelper.ItemEnergyStorage(stack);

				@Override
				public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
				{
					return capability==CapabilityEnergy.ENERGY;
				}

				@Nullable
				@Override
				public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
				{
					return capability==CapabilityEnergy.ENERGY?(T)energyStorage: null;
				}
			};
		else
			return super.initCapabilities(stack, nbt);
	}

	@Override
	public void onStrike(ItemStack stack, EntityEquipmentSlot eqSlot, EntityLivingBase entity, Map<String, Object> cache,
						 @Nullable DamageSource dSource, ElectricSource eSource)
	{
		if(!(dSource instanceof ElectricDamageSource))
			return;
		ElectricDamageSource dmg = (ElectricDamageSource)dSource;
		receiveEnergy(stack, (int)(Math.floor(dmg.dmg)*Machines.teslacoil_consumption_active), false);
		dmg.dmg = 0;
	}

	@Override
	public boolean hasCustomItemColours()
	{
		return true;
	}

	@Override
	public int getColourForIEItem(ItemStack stack, int renderPass)
	{
		if(renderPass==0)
			return 0xffffff;
		if(!ItemNBTHelper.hasKey(stack, NBT_COLOR))
			return 0x486c94;
		return ItemNBTHelper.getInt(stack, NBT_COLOR);
	}

	/**
	 * Return whether the specified armor ItemStack has a color.
	 */
	@Override
	public boolean hasColor(ItemStack stack)
	{
		return true;
	}

	/**
	 * Return the color for the specified armor ItemStack.
	 */
	@Override
	public int getColor(ItemStack stack)
	{
		if(!ItemNBTHelper.hasKey(stack, NBT_COLOR))
			return 0x486c94;
		return ItemNBTHelper.getInt(stack, NBT_COLOR);
	}

	/**
	 * Remove the color from the specified armor ItemStack.
	 */
	@Override
	public void removeColor(ItemStack stack)
	{
		if(ItemNBTHelper.hasKey(stack, NBT_COLOR))
			ItemNBTHelper.remove(stack, NBT_COLOR);
	}

	/**
	 * Sets the color of the specified armor ItemStack
	 */
	@Override
	public void setColor(ItemStack stack, int color)
	{
		ItemNBTHelper.setInt(stack, NBT_COLOR, color);
	}
}
