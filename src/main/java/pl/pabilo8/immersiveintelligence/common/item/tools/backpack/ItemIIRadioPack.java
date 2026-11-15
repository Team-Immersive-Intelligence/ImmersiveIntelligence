package pl.pabilo8.immersiveintelligence.common.item.tools.backpack;

import blusunrize.immersiveengineering.api.DimensionBlockPos;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.radio.IRadioDevice;
import pl.pabilo8.immersiveintelligence.api.data.radio.RadioNetwork;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig;
import pl.pabilo8.immersiveintelligence.common.item.tools.ItemIIRadioTuner;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Provides a mobile radio relay on the wearer's frequency.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 04.11.2026
 * @since 23.01.2021
 */
@IIItemProperties(category = IICategory.TOOLS)
@GeneratedItemModels(itemName = "radio_backpack", texturePath = "backpack/radiopack")
public class ItemIIRadioPack extends ItemIIBackpackBase
{
	private static final String RADIO_COOLDOWN = "radio_cooldown";
	private static final int RANGE = 32;

	public ItemIIRadioPack()
	{
		super("radio_backpack");
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
	{
		return new BackpackRadio(stack, null, stack);
	}

	@Override
	public boolean onWornTick(EntityLivingBase wearer, ItemStack stack)
	{
		int cooldown = ItemNBTHelper.getInt(stack, RADIO_COOLDOWN);
		if(cooldown <= 0)
			return false;
		ItemNBTHelper.setInt(stack, RADIO_COOLDOWN, cooldown-1);
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag)
	{
		tooltip.add(I18n.format(IIReference.DESCRIPTION_KEY+"radio_configurator_frequency",
				ItemNBTHelper.getInt(stack, ItemIIRadioTuner.TAG_FREQUENCY)));
	}

	/**
	 * Binds a radio stack to its current equipment slot.
	 *
	 * @author Pabilo8 (pabilo@iiteam.net)
	 * @since 04.11.2026
	 */
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	private static final class BackpackRadio implements IRadioDevice, ICapabilityProvider
	{
		private final ItemStack stack;
		@Nullable
		private final Entity wearer;
		private final ItemStack equipment;

		@Override
		public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing side)
		{
			return capability==RadioNetwork.RADIO_CAPABILITY;
		}

		@Nullable
		@Override
		public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing side)
		{
			return hasCapability(capability, side)?RadioNetwork.RADIO_CAPABILITY.cast(this): null;
		}

		@Override
		public IRadioDevice bindTo(Entity wearer, ItemStack equipment)
		{
			return new BackpackRadio(stack, wearer, equipment);
		}

		@Override
		public boolean isRadioAvailable()
		{
			return wearer!=null&&!wearer.isDead&&IRadioDevice.super.isRadioAvailable();
		}

		@Override
		public void onRadioSend(DataPacket packet)
		{
		}

		@Override
		public boolean onRadioReceive(DataPacket packet)
		{
			return true;
		}

		@Override
		public boolean isBasicRadio()
		{
			return true;
		}

		@Override
		public int getFrequency()
		{
			return ItemNBTHelper.getInt(stack, ItemIIRadioTuner.TAG_FREQUENCY);
		}

		@Override
		public void setFrequency(int frequency)
		{
			ItemNBTHelper.setInt(stack, ItemIIRadioTuner.TAG_FREQUENCY, MathHelper.clamp(frequency, 0, IIConfig.radioBasicMaxFrequency));
			if(equipment!=stack)
				setAttachedBackpack(equipment, stack);
		}

		@Override
		public float getRange()
		{
			return RANGE;
		}

		@Override
		public DimensionBlockPos getRadioDevicePosition()
		{
			return new DimensionBlockPos(wearer.getPosition(), wearer.world);
		}

		@Override
		public World getIIWorld()
		{
			return wearer==null?null: wearer.world;
		}

		@Override
		public int getRadioCooldown()
		{
			return ItemNBTHelper.getInt(stack, RADIO_COOLDOWN);
		}

		@Override
		public void setRadioCooldown(int ticks)
		{
			ItemNBTHelper.setInt(stack, RADIO_COOLDOWN, MathHelper.clamp(ticks, 0, MAX_USABILITY_COOLDOWN));
			if(equipment!=stack)
				setAttachedBackpack(equipment, stack);
		}

		@Override
		public boolean equals(Object other)
		{
			return other instanceof BackpackRadio&&((BackpackRadio)other).wearer==wearer
					&&((BackpackRadio)other).equipment==equipment;
		}

		@Override
		public int hashCode()
		{
			return 31*System.identityHashCode(wearer)+System.identityHashCode(equipment);
		}
	}
}
