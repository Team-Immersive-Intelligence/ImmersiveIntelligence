package pl.pabilo8.immersiveintelligence.common.items.armor;

import blusunrize.immersiveengineering.api.tool.IElectricEquipment;
import blusunrize.immersiveengineering.common.Config.IEConfig.Machines;
import blusunrize.immersiveengineering.common.util.IEDamageSources.ElectricDamageSource;
import com.google.common.collect.Multimap;
import net.minecraft.block.material.Material;
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
import pl.pabilo8.immersiveintelligence.api.CorrosionHandler.IAcidProtectionEquipment;
import pl.pabilo8.immersiveintelligence.api.CorrosionHandler.ICorrosionProtectionEquipment;
import pl.pabilo8.immersiveintelligence.api.utils.IInfraredProtectionEquipment;
import pl.pabilo8.immersiveintelligence.api.utils.IRadiationProtectionEquipment;
import pl.pabilo8.immersiveintelligence.client.model.armor.ModelLightEngineerArmor;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIPotions;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * @author Pabilo8
 * @since 13.09.2020
 */
public class ItemIILightEngineerChestplate extends ItemIIUpgradeableArmor implements IElectricEquipment, ICorrosionProtectionEquipment, IRadiationProtectionEquipment, IAcidProtectionEquipment, IInfraredProtectionEquipment
{
	public ItemIILightEngineerChestplate()
	{
		super(IIContent.ARMOR_MATERIAL_LIGHT_ENGINEER, EntityEquipmentSlot.CHEST, "LIGHT_ENGINEER_CHESTPLATE");
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack)
	{
		super.onArmorTick(world, player, itemStack);
		if(getUpgrades(itemStack).hasKey("camo_mesh")&&world.getTotalWorldTime()%10==0)
		{
			Material material = world.getBlockState(player.getPosition()).getMaterial();
			if(player.isSneaking()&&(material==Material.GRASS||material==Material.LEAVES||material==Material.VINE))
			{
				player.addPotionEffect(new PotionEffect(IIPotions.concealed, 15, 0, false, false));
				player.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY,15,0,true,false));
			}
		}
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
			//multimap.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Power Armor Movement Speed Debuff", -.03, 1));
		}
		return multimap;
	}

	@Override
	public void onStrike(ItemStack s, EntityEquipmentSlot eqSlot, EntityLivingBase p, Map<String, Object> cache,
						 @Nullable DamageSource dSource, ElectricSource eSource)
	{
		if(!(dSource instanceof ElectricDamageSource))
			return;
		if(!getUpgrades(s).hasKey("anti_static_mesh"))
			return;

		ElectricDamageSource dmg = (ElectricDamageSource)dSource;
		dmg.dmg = 0;
		// TODO: 13.09.2020 tesla coil interaction
	}

	@Override
	public int getSlotCount()
	{
		return 4;
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

	@Override
	public boolean invisibleToInfrared(ItemStack stack)
	{
		return getUpgrades(stack).hasKey("ir_mesh");
	}
}
