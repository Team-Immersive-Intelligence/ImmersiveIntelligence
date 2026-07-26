package pl.pabilo8.immersiveintelligence.common.item.armor;

import blusunrize.immersiveengineering.api.tool.IElectricEquipment;
import blusunrize.immersiveengineering.common.util.IEDamageSources.ElectricDamageSource;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import com.google.common.collect.Multimap;
import net.minecraft.block.material.Material;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
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
import pl.pabilo8.immersiveintelligence.client.model.armor.ModelLightEngineerArmor;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIIUpgradeableArmor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 13.09.2020
 */
@IIItemProperties(category = IICategory.WARFARE)
public class ItemIILightEngineerBoots extends ItemIILightEngineerArmorBase implements IElectricEquipment
{
	// Unique UUIDs for attribute modifiers to avoid access conflicts
	private static final UUID RACKETS_MODIFIER_UUID = UUID.fromString("C8E5F2A1-7D9C-4F6A-8E3B-A4C7D2E8F9B1");

	public ItemIILightEngineerBoots()
	{
		super(EntityEquipmentSlot.FEET, "LIGHT_ENGINEER_BOOTS");
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
			if(getUpgrades(stack).hasKey("flippers"))
			{
				multimap.put(EntityLivingBase.SWIM_SPEED.getName(),
						new AttributeModifier(ItemIIUpgradeableArmor.ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Flippers", 4, 2));
				if(ItemNBTHelper.hasKey(stack, "rackets"))
				{
					multimap.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new AttributeModifier(RACKETS_MODIFIER_UUID, "Rackets", 0.5, 1));
					//if(getUpgrades(stack).hasKey(""))
					//multimap.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Power Armor Movement Speed Debuff", -.03, 1));
					//multimap.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Power Armor Movement Speed Debuff", -.03, 1));
				}

			}
		}
		return multimap;
	}


	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack)
	{
		if(getUpgrades(stack).hasKey("flippers")&&player.isInWater())
		{
			ItemNBTHelper.setBoolean(stack, "flippin", true);
			player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 15, 1, true, false)); // Speed level 1 for 15 ticks
		}
		else if(ItemNBTHelper.hasKey(stack, "flippin"))
		{
			ItemNBTHelper.remove(stack, "flippin");
		}

		Material mat = world.getBlockState(player.getPosition()).getMaterial();
		Material matDown = world.getBlockState(player.getPosition().down()).getMaterial();

		boolean rackets = getUpgrades(stack).hasKey("snow_rackets");
		if(rackets&&(matDown==Material.SNOW||mat==Material.CRAFTED_SNOW||matDown==Material.ICE||matDown==Material.PACKED_ICE))
		{
			ItemNBTHelper.setBoolean(stack, "rackets", true);
			player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 15, 0, true, false));
		}
		else if (ItemNBTHelper.hasKey(stack, "rackets"))
		{
			ItemNBTHelper.remove(stack, "rackets");
		}

		boolean springs = getUpgrades(stack).hasKey("internal_springs");
		if(!world.isRemote&&springs)
		{
			ItemNBTHelper.setBoolean(stack, "internal_springs", true);
			player.stepHeight = 0;  // Ensures no sound is made during stepping
		}


		boolean reinforcement = getUpgrades(stack).hasKey("boot_reinforcement");
		if(reinforcement&&player.isBurning())
		{
			ItemNBTHelper.setBoolean(stack, "boot_reinforcement", true);
			player.extinguish();
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
		return 3;
	}
}
