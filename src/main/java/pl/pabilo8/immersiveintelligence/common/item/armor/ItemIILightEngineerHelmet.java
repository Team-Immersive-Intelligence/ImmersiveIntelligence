package pl.pabilo8.immersiveintelligence.common.item.armor;

import blusunrize.immersiveengineering.api.tool.IElectricEquipment;
import blusunrize.immersiveengineering.common.util.IEDamageSources.ElectricDamageSource;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import com.google.common.collect.Multimap;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.LightEngineerArmor;
import pl.pabilo8.immersiveintelligence.api.utils.armor.IGasmask;
import pl.pabilo8.immersiveintelligence.client.ClientProxy;
import pl.pabilo8.immersiveintelligence.client.model.armor.ModelLightEngineerArmor;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageItemKeybind;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * @author Pabilo8
 * @since 13.09.2020
 */
public class ItemIILightEngineerHelmet extends ItemIILightEngineerArmorBase implements IElectricEquipment, IGasmask
{
	public ItemIILightEngineerHelmet()
	{
		super(EntityEquipmentSlot.HEAD, "LIGHT_ENGINEER_HELMET");
	}

	@Nullable
	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default)
	{
		return ModelLightEngineerArmor.getModel(armorSlot, itemStack);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(@Nonnull ItemStack stack, @Nullable World world, List<String> list, @Nonnull ITooltipFlag flag)
	{
		super.addInformation(stack, world, list, flag);
	}

	@Override
	public float getXpRepairRatio(ItemStack stack)
	{
		return 0.1f;
	}

	@Nonnull
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(@Nonnull EntityEquipmentSlot equipmentSlot, @Nonnull ItemStack stack)
	{
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(equipmentSlot, stack);

		if(equipmentSlot==this.armorType)
		{
			//multimap.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Power Armor Movement Speed Debuff", -.03, 1));
		}
		return multimap;
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack)
	{
		super.onArmorTick(world, player, stack);
		boolean hasEngineer = hasUpgrade(stack, "engineer_gear");
		boolean hasIR = hasEngineer||hasUpgrade(stack, "infiltrator_gear");
		boolean hasTech = hasEngineer||hasUpgrade(stack, "technician_gear");

		if(world.isRemote)
		{
			if(hasIR&&ClientProxy.keybind_armorHelmet.isPressed())
				IIPacketHandler.sendToServer(new MessageItemKeybind(MessageItemKeybind.KEYBIND_HEADGEAR));
		}
		else if((hasIR||hasTech)&&world.getTotalWorldTime()%20==0&&ItemNBTHelper.getBoolean(stack, "headgearActive"))
		{
			int drain = hasEngineer?LightEngineerArmor.irHeadgearEnergyUsage:
					(hasTech?LightEngineerArmor.technicianHeadgearEnergyUsage: LightEngineerArmor.irHeadgearEnergyUsage);

			if(extractEnergy(stack, drain, false) < drain)
				return;

			if(hasIR)
			{
				boolean isInDarkness = world.getLightBrightness(player.getPosition()) <= 0.5f;
				player.addPotionEffect(new PotionEffect(isInDarkness?MobEffects.NIGHT_VISION: MobEffects.BLINDNESS, 240, 1, true, false));
			}
			/*if(hasTech)
			{

			}*/
		}

	}

	@Override
	public void onStrike(ItemStack s, EntityEquipmentSlot eqSlot, EntityLivingBase p, Map<String, Object> cache,
						 @Nullable DamageSource dSource, ElectricSource eSource)
	{
		if(!(dSource instanceof ElectricDamageSource))
		{

		}
	}

	@Override
	public int getSlotCount()
	{
		return 4;
	}

	@Override
	public boolean protectsFromGasses(ItemStack stack)
	{
		return getUpgrades(stack).hasKey("gasmask");
	}
}
