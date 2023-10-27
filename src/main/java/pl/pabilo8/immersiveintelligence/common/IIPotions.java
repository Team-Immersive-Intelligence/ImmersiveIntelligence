package pl.pabilo8.immersiveintelligence.common;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces.IItemDamageableIE;
import blusunrize.immersiveengineering.common.util.IEPotions.IEPotion;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.CorrosionHandler;
import pl.pabilo8.immersiveintelligence.api.utils.armor.IRadiationProtectionEquipment;
import pl.pabilo8.immersiveintelligence.common.util.IIDamageSources;

/**
 * @author Pabilo8
 * @since 03-03-2020
 */
public class IIPotions
{
	public static Potion suppression, brokenArmor, corrosion, infraredVision, ironWill, wellSupplied, concealed;
	public static Potion exposed, medicalTreatment, undergoingRepairs, radiation, nuclearHeat, movementAssist;

	public static void init()
	{
		suppression = new IIPotion("suppression", true, 0xe3bb19, 0, false, 0, true, true);
		suppression.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, Utils.generateNewUUID().toString(), -0.003921569f, 2);
		suppression.registerPotionAttributeModifier(SharedMonsterAttributes.LUCK, Utils.generateNewUUID().toString(), -0.007843138f, 2);
		suppression.registerPotionAttributeModifier(SharedMonsterAttributes.FOLLOW_RANGE, Utils.generateNewUUID().toString(), -0.007843138f, 2);
		suppression.registerPotionAttributeModifier(SharedMonsterAttributes.FLYING_SPEED, Utils.generateNewUUID().toString(), -0.125, 2);
		suppression.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, Utils.generateNewUUID().toString(), -0.003921569f, 2);

		brokenArmor = new IIPotion("broken_armor", true, 0x755959, 0, false, 1, true, true);
		brokenArmor.registerPotionAttributeModifier(SharedMonsterAttributes.ARMOR_TOUGHNESS, Utils.generateNewUUID().toString(), -0.003921569f, 2);

		corrosion = new IIPotion("corrosion", true, 0x567b46, 0, false, 2, true, true)
		{
			@Override
			public void performEffect(EntityLivingBase living, int amplifier)
			{
				living.getArmorInventoryList().forEach(stack -> {
					if(CorrosionHandler.canCorrode(stack))
						stack.damageItem(amplifier, living);
				});
			}
		};
		corrosion.registerPotionAttributeModifier(SharedMonsterAttributes.ARMOR_TOUGHNESS, Utils.generateNewUUID().toString(), -0.003921569f, 2);

		infraredVision = new IIPotion("infrared_vision", false, 0x7b0000, 0, false, 3, true, true);

		ironWill = new IIPotion("iron_will", false, 0xe2c809, 0, false, 4, true, true);
		ironWill.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, Utils.generateNewUUID().toString(), 0.003921569f, 1);
		ironWill.registerPotionAttributeModifier(SharedMonsterAttributes.LUCK, Utils.generateNewUUID().toString(), 0.007843138f, 2);

		wellSupplied = new IIPotion("well_supplied", false, 0xa49e66, 0, false, 5, true, true);

		concealed = new IIPotion("concealed", false, 0x558858, 0, false, 6, true, true)
		{
			@Override
			public void performEffect(EntityLivingBase living, int amplifier)
			{

				if(!living.isPotionActive(IIPotions.concealed))
					living.setInvisible(true);
			}
		};
		exposed = new IIPotion("exposed", true, 0x558858, 0, false, 12, true, true)
		{
			@Override
			public void performEffect(EntityLivingBase living, int amplifier)
			{
				living.setGlowing(true);
				living.setInvisible(false);
				living.removePotionEffect(MobEffects.INVISIBILITY);
				living.removePotionEffect(IIPotions.concealed);
			}

			@Override
			public void removeAttributesModifiersFromEntity(EntityLivingBase entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier)
			{
				super.removeAttributesModifiersFromEntity(entityLivingBaseIn, attributeMapIn, amplifier);
				entityLivingBaseIn.setGlowing(false);
			}
		};
		exposed.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, Utils.generateNewUUID().toString(), -0.05f, 1);
		exposed.registerPotionAttributeModifier(SharedMonsterAttributes.LUCK, Utils.generateNewUUID().toString(), -1f, 1);
		suppression.registerPotionAttributeModifier(SharedMonsterAttributes.FOLLOW_RANGE, Utils.generateNewUUID().toString(), -0.007843138f, 2);


		medicalTreatment = new IIPotion("medical_treatment", false, 0xe13eb8, 0, false, 7, true, true)
		{
			@Override
			public boolean isReady(int duration, int amplifier)
			{
				return amplifier==0?(duration > 200): (duration > 120);
			}

			@Override
			public void performEffect(EntityLivingBase living, int amplifier)
			{
				if(living.getEntityWorld().getTotalWorldTime()%4==0)
					living.heal((amplifier+1)/4f);
			}
		};
		undergoingRepairs = new IIPotion("undergoing_repairs", false, 0xc0c0c0, 0, false, 8, true, true)
		{
			@Override
			public boolean isReady(int duration, int amplifier)
			{
				return amplifier==0?(duration > 200): (duration > 120);
			}

			@Override
			public void performEffect(EntityLivingBase living, int amplifier)
			{
				if(living.getEntityWorld().getTotalWorldTime()%4==0)
					for(ItemStack stack : living.getEquipmentAndArmor())
					{
						if(stack.getItem() instanceof IItemDamageableIE)
						{
							IItemDamageableIE damageable = (IItemDamageableIE)stack.getItem();
							ItemNBTHelper.setInt(stack, Lib.NBT_DAMAGE, Math.max(damageable.getItemDamageIE(stack)-(amplifier+1), 0));
						}
						else if(stack.isItemStackDamageable()&&stack.getItem().isRepairable())
						{
							stack.setItemDamage(Math.max(stack.getItemDamage()-(amplifier+1), 0));
						}
					}
			}


		};

		radiation = new IIPotion("radiation", true, 0xd2a846, 0, false, 9, true, true)
		{
			@Override
			public void performEffect(EntityLivingBase living, int amplifier)
			{
				if(living.ticksExisted%20!=0)
					return;
				boolean apply = false;
				for(ItemStack s : living.getArmorInventoryList())
				{
					if(!(s.getItem() instanceof IRadiationProtectionEquipment))
						apply = true;
					else if(!((IRadiationProtectionEquipment)s.getItem()).protectsFromRadiation(s))
						apply = true;
				}
				if(apply)
				{
					living.hurtResistantTime = 0;
					living.attackEntityFrom(IIDamageSources.RADIATION_DAMAGE, 2);
				}
			}
		};
		radiation.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, Utils.generateNewUUID().toString(), -0.003921569f, 2);
		radiation.registerPotionAttributeModifier(SharedMonsterAttributes.FOLLOW_RANGE, Utils.generateNewUUID().toString(), -0.003921569f, 2);
		radiation.registerPotionAttributeModifier(SharedMonsterAttributes.FLYING_SPEED, Utils.generateNewUUID().toString(), -0.003921569f, 2);

		nuclearHeat = new IIPotion("nuclear_heat", true, 0x9d5919, 0, false, 10, true, true)
		{
			@Override
			public void performEffect(EntityLivingBase living, int amplifier)
			{
				living.hurtResistantTime = 0;
				living.getArmorInventoryList().forEach(stack -> stack.damageItem(stack.getMaxDamage(), living));
				living.attackEntityFrom(IIDamageSources.NUCLEAR_HEAT_DAMAGE, 2000);
			}
		};
		nuclearHeat.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, Utils.generateNewUUID().toString(), -1, 2);
		nuclearHeat.registerPotionAttributeModifier(SharedMonsterAttributes.FOLLOW_RANGE, Utils.generateNewUUID().toString(), -1, 2);
		nuclearHeat.registerPotionAttributeModifier(SharedMonsterAttributes.FLYING_SPEED, Utils.generateNewUUID().toString(), -1, 2);

		movementAssist = new IIPotion("movement_assist", false, 0x9d5919, 0, false, 13, true, true);
		movementAssist.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, Utils.generateNewUUID().toString(), 0.5, 1);
		movementAssist.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, Utils.generateNewUUID().toString(), 0.5, 1);

	}


	public static class IIPotion extends IEPotion
	{
		static ResourceLocation tex = new ResourceLocation(ImmersiveIntelligence.MODID, "textures/gui/potioneffects.png");

		public IIPotion(String name, boolean isBad, int colour, int tick, boolean halveTick, int icon, boolean showInInventory, boolean showInHud)
		{
			super(new ResourceLocation(ImmersiveIntelligence.MODID, name), isBad, colour, tick, halveTick, icon, showInInventory, showInHud);
			this.setPotionName(name);
		}

		@Override
		public Potion setPotionName(String nameIn)
		{
			return super.setPotionName("potion."+ImmersiveIntelligence.MODID+"."+nameIn);
		}

		@Override
		public int getStatusIconIndex()
		{
			//An absolute trick
			int iconindex = super.getStatusIconIndex();
			Minecraft.getMinecraft().getTextureManager().bindTexture(tex);
			return iconindex;
		}

		@Override
		public void renderHUDEffect(PotionEffect effect, Gui gui, int x, int y, float z, float alpha)
		{
			super.renderHUDEffect(effect, gui, x, y, z, alpha);
		}
	}
}
