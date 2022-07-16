package pl.pabilo8.immersiveintelligence.common.items.tools;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.tool.IElectricEquipment;
import blusunrize.immersiveengineering.client.ClientProxy;
import blusunrize.immersiveengineering.common.Config.IEConfig.Machines;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces.IColouredItem;
import blusunrize.immersiveengineering.common.items.ItemPowerpack;
import blusunrize.immersiveengineering.common.util.EnergyHelper;
import blusunrize.immersiveengineering.common.util.EnergyHelper.IIEEnergyItem;
import blusunrize.immersiveengineering.common.util.IEDamageSources.ElectricDamageSource;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.armor.ModelAdvancedPowerpack;
import pl.pabilo8.immersiveintelligence.common.IIContent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * @author Pabilo8
 * @since 23.01.2021
 */
public class ItemIIAdvancedPowerPack extends ItemArmor implements ISpecialArmor, IIEEnergyItem, IElectricEquipment, IColouredItem
{
	public static final String NBT_Colour = "II:AdvancedPowerpackColour";

	public ItemIIAdvancedPowerPack()
	{
		super(ArmorMaterial.IRON, 0, EntityEquipmentSlot.CHEST);
		setMaxDamage(0);
		String name = "advanced_powerpack";
		this.setUnlocalizedName(ImmersiveIntelligence.MODID+"."+name);
		this.setCreativeTab(ImmersiveIntelligence.creativeTab);
		IIContent.ITEMS.add(this);
	}

	//Armor should use a model instead of layered textures
	@Nullable
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type)
	{
		//Whatever you say, Blu, I know the real reason why you removed it
		//return ImmersiveEngineering.MODID+":textures/models/maneuver_gear.png";
		return ImmersiveIntelligence.MODID+":textures/armor/empty.png";
	}

	@Nullable
	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default)
	{
		//And yes, I really like AoT in its current state
		//return ModelManeuverGear.getModel();
		return ModelAdvancedPowerpack.getModel(armorSlot, itemStack);
	}

	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag)
	{
		String stored = this.getEnergyStored(stack)+"/"+this.getMaxEnergyStored(stack);
		list.add(I18n.format(Lib.DESC+"info.energyStored", stored));
		String hexCol = Integer.toHexString(getColor(stack));
		list.add(I18n.format(Lib.DESC_INFO+"colour", "<hexcol="+hexCol+":#"+hexCol+">"));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public FontRenderer getFontRenderer(ItemStack stack)
	{
		return ClientProxy.itemFont;
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack)
	{
		int energy = getEnergyStored(itemStack);
		if(energy > 0)
		{
			int pre = energy;
			for(EntityEquipmentSlot slot : EntityEquipmentSlot.values())
				if(EnergyHelper.isFluxItem(player.getItemStackFromSlot(slot))&&!(player.getItemStackFromSlot(slot).getItem() instanceof ItemPowerpack))
					energy -= EnergyHelper.insertFlux(player.getItemStackFromSlot(slot), Math.min(energy, 4096), false);
			if(pre!=energy)
				EnergyHelper.extractFlux(itemStack, pre-energy, false);
		}
	}

	/**
	 * Gets a map of item attribute modifiers, used by ItemSword to increase hit damage.
	 */
	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
	{
		return HashMultimap.create();
	}

	@Override
	public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot)
	{
		return new ArmorProperties(0, 0, 0);
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot)
	{
		return 0;
	}

	@Override
	public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot)
	{
	}

	@Override
	public int getMaxEnergyStored(ItemStack container)
	{
		return Tools.advanced_powerpack_capacity;
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
		if(!ItemNBTHelper.hasKey(stack, NBT_Colour))
			return 0x486c94;
		return ItemNBTHelper.getInt(stack, NBT_Colour);
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
		if(!ItemNBTHelper.hasKey(stack, NBT_Colour))
			return 0x486c94;
		return ItemNBTHelper.getInt(stack, NBT_Colour);
	}

	/**
	 * Remove the color from the specified armor ItemStack.
	 */
	@Override
	public void removeColor(ItemStack stack)
	{
		if(ItemNBTHelper.hasKey(stack, NBT_Colour))
			ItemNBTHelper.remove(stack, NBT_Colour);
	}

	/**
	 * Sets the color of the specified armor ItemStack
	 */
	@Override
	public void setColor(ItemStack stack, int color)
	{
		ItemNBTHelper.setInt(stack, NBT_Colour, color);
	}
}
