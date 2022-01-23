package pl.pabilo8.immersiveintelligence.common.items.armor;

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
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.CorrosionHandler.IAcidProtectionEquipment;
import pl.pabilo8.immersiveintelligence.api.CorrosionHandler.ICorrosionProtectionEquipment;
import pl.pabilo8.immersiveintelligence.api.utils.IRadiationProtectionEquipment;
import pl.pabilo8.immersiveintelligence.client.model.armor.ModelLightEngineerArmor;
import pl.pabilo8.immersiveintelligence.common.IIContent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * @author Pabilo8
 * @since 13.09.2020
 */
public class ItemIILightEngineerBoots extends ItemIIUpgradeableArmor implements IElectricEquipment, ICorrosionProtectionEquipment, IRadiationProtectionEquipment, IAcidProtectionEquipment
{
	public ItemIILightEngineerBoots()
	{
		super(IIContent.ARMOR_MATERIAL_LIGHT_ENGINEER, EntityEquipmentSlot.FEET, "LIGHT_ENGINEER_BOOTS");
	}

	@Nullable
	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default)
	{
		return ModelLightEngineerArmor.getModel(armorSlot, itemStack);
	}

	@Override
	String getMaterialName(ArmorMaterial material)
	{
		return "light_engineer_armor";
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag)
	{
		super.addInformation(stack, world, list, flag);
	}

	@Override
	public float getXpRepairRatio(ItemStack stack)
	{
		return 0.1f;
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot equipmentSlot, ItemStack stack)
	{
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(equipmentSlot, stack);

		if(equipmentSlot==this.armorType)
		{
			if(ItemNBTHelper.hasKey(stack, "flippin"))
			{
				multimap.put(EntityLivingBase.SWIM_SPEED.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Flippers", 4, 2));
			}
			if(ItemNBTHelper.hasKey(stack, "rackets"))
			{
				multimap.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Rackets", 0.5, 1));
			}
			//if(getUpgrades(stack).hasKey(""))
			//multimap.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Power Armor Movement Speed Debuff", -.03, 1));
			//multimap.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Power Armor Movement Speed Debuff", -.03, 1));
		}
		return multimap;
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack)
	{
		super.onArmorTick(world, player, stack);
		if(getUpgrades(stack).hasKey("flippers")&&(player.isInWater())&&player.isSprinting())
			ItemNBTHelper.setBoolean(stack, "flippin", true);
		else if(ItemNBTHelper.hasKey(stack, "flippin"))
			ItemNBTHelper.remove(stack, "flippin");

		//(mat==Material.ICE||mat==Material.PACKED_ICE) soon
		Material mat = world.getBlockState(player.getPosition()).getMaterial();
		if(getUpgrades(stack).hasKey("snow_rackets")&&(mat==Material.SNOW||mat==Material.CRAFTED_SNOW))
		{
			ItemNBTHelper.setBoolean(stack, "rackets", true);
		}
		else if(ItemNBTHelper.hasKey(stack, "rackets"))
			ItemNBTHelper.remove(stack, "rackets");
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

	@Override
	public boolean canCorrode(ItemStack stack)
	{
		return !getUpgrades(stack).hasKey("hazmat");
	}

	@Override
	public boolean protectsFromRadiation(ItemStack stack)
	{
		return getUpgrades(stack).hasKey("hazmat");
	}

	@Override
	public boolean protectsFromAcid(ItemStack stack)
	{
		return getUpgrades(stack).hasKey("hazmat");
	}
}
