package pl.pabilo8.immersiveintelligence.common;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces.IItemDamageableIE;
import blusunrize.immersiveengineering.common.util.IEPotions;
import blusunrize.immersiveengineering.common.util.IEPotions.IEPotion;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.CorrosionHandler;
import pl.pabilo8.immersiveintelligence.api.utils.armor.IGasmask;
import pl.pabilo8.immersiveintelligence.api.utils.armor.IRadiationProtectionEquipment;
import pl.pabilo8.immersiveintelligence.common.util.IIDamageSources;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 03.03.2020
 */
public class IIPotions
{
	public static Potion suppression, brokenArmor, corrosion, infraredVision, ironWill, wellSupplied, concealed;
	public static Potion exposed, medicalTreatment, undergoingRepairs, radiation, nuclearHeat, movementAssist;
	public static Potion homeShores, homeland, heartland, foreignShores, enemySoil, enemysNest;
	public static Potion neuroparalitic, poisonirritant, suffocator, suffocatordelayed1, suffocatordelayed2, fullparalysis;
	public static Potion bioweapon1, bioweapon2;

	public static void init()
	{
		suppression = new IIPotion("suppression", true, 0xe3bb19);
		suppression.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, Utils.generateNewUUID().toString(), -0.003921569f, 2);
		suppression.registerPotionAttributeModifier(SharedMonsterAttributes.LUCK, Utils.generateNewUUID().toString(), -0.007843138f, 2);
		suppression.registerPotionAttributeModifier(SharedMonsterAttributes.FOLLOW_RANGE, Utils.generateNewUUID().toString(), -0.007843138f, 2);
		suppression.registerPotionAttributeModifier(SharedMonsterAttributes.FLYING_SPEED, Utils.generateNewUUID().toString(), -0.125, 2);
		suppression.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, Utils.generateNewUUID().toString(), -0.003921569f, 2);

		brokenArmor = new IIPotion("broken_armor", true, 0x755959);
		brokenArmor.registerPotionAttributeModifier(SharedMonsterAttributes.ARMOR_TOUGHNESS, Utils.generateNewUUID().toString(), -0.003921569f, 2);

		corrosion = new IIPotion("corrosion", true, 0x567b46)
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

		infraredVision = new IIPotion("infrared_vision", false, 0x7b0000);

		ironWill = new IIPotion("iron_will", false, 0xe2c809);
		ironWill.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, Utils.generateNewUUID().toString(), 0.003921569f, 1);
		ironWill.registerPotionAttributeModifier(SharedMonsterAttributes.LUCK, Utils.generateNewUUID().toString(), 0.007843138f, 2);

		wellSupplied = new IIPotion("well_supplied", false, 0xa49e66);

		medicalTreatment = new IIPotion("medical_treatment", false, 0xe13eb8)
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
		undergoingRepairs = new IIPotion("undergoing_repairs", false, 0xc0c0c0)
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

		concealed = new IIPotion("concealed", false, 0x558858)
		{
			@Override
			public void performEffect(EntityLivingBase living, int amplifier)
			{

				if(!living.isPotionActive(IIPotions.concealed))
					living.setInvisible(true);
			}
		};
		exposed = new IIPotion("exposed", true, 0x558858)
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
		exposed.registerPotionAttributeModifier(SharedMonsterAttributes.FOLLOW_RANGE, Utils.generateNewUUID().toString(), -0.007843138f, 2);


		radiation = new IIPotion("radiation", true, 0xd2a846)
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

			@Override
			public List<ItemStack> getCurativeItems()
			{
				return new ArrayList<>();
			}
		};
		radiation.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, Utils.generateNewUUID().toString(), -0.003921569f, 2);
		radiation.registerPotionAttributeModifier(SharedMonsterAttributes.FOLLOW_RANGE, Utils.generateNewUUID().toString(), -0.003921569f, 2);
		radiation.registerPotionAttributeModifier(SharedMonsterAttributes.FLYING_SPEED, Utils.generateNewUUID().toString(), -0.003921569f, 2);

		nuclearHeat = new IIPotion("nuclear_heat", true, 0x9d5919)
		{
			@Override
			public void performEffect(EntityLivingBase living, int amplifier)
			{
				living.hurtResistantTime = 0;
				living.getArmorInventoryList().forEach(stack -> stack.damageItem(stack.getMaxDamage(), living));
				living.attackEntityFrom(IIDamageSources.NUCLEAR_HEAT_DAMAGE, 2000);
			}

			@Override
			public List<ItemStack> getCurativeItems()
			{
				return new ArrayList<>();
			}
		};
		nuclearHeat.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, Utils.generateNewUUID().toString(), -1, 2);
		nuclearHeat.registerPotionAttributeModifier(SharedMonsterAttributes.FOLLOW_RANGE, Utils.generateNewUUID().toString(), -1, 2);
		nuclearHeat.registerPotionAttributeModifier(SharedMonsterAttributes.FLYING_SPEED, Utils.generateNewUUID().toString(), -1, 2);

		IIPotion.iconID = 13;
		movementAssist = new IIPotion("movement_assist", false, 0x9d5919);
		movementAssist.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, Utils.generateNewUUID().toString(), 0.5, 1);
		movementAssist.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, Utils.generateNewUUID().toString(), 0.5, 1);

		IIPotion.iconID = 16;
		homeShores = new IIPotion("home_shores", true, 0x9d5919);
		homeland = new IIPotion("homeland", true, 0x9d5919);
		heartland = new IIPotion("heartland", true, 0x9d5919);
		foreignShores = new IIPotion("foreign_shores", true, 0x9d5919);
		enemySoil = new IIPotion("enemy_soil", true, 0x9d5919);
		enemysNest = new IIPotion("enemys_nest", true, 0x9d5919);


		//21.04.2026 Carver: unique gas sub-effects for categories. Experiments.

		//Neuroparalitic should mess with player and entity controls, or at least immobilize them. Also should prevent item use.
		// Should also give velocity to target in random horizontal but small directions.
		//Otherwise, simply lock target in place and their camera as well.
		//Countered by BOTH hazmat and gasmask.

		neuroparalitic = new IIPotion("neuroparalitic", true, 0x7d1b19)
		{
			@Override
			public void performEffect(EntityLivingBase living, int amplifier)
			{
				if(living.ticksExisted%10!=0)
					return;
				boolean apply = false;
				for(ItemStack s : living.getArmorInventoryList())
				{
					if(!(s.getItem() instanceof IGasmask))
						apply = true;
					else if(!((IGasmask)s.getItem()).protectsFromGasses(s))
						apply = true;
					else if(!(s.getItem() instanceof IRadiationProtectionEquipment))
						apply = true;
					else if(!((IRadiationProtectionEquipment)s.getItem()).protectsFromRadiation(s))
						apply = true;
				}
				if(apply)
				{
					living.jumpMovementFactor = 0;
					living.hurtResistantTime = 0;
					//living.moveStrafing = (float)(Math.random() * (Math.PI * 2D));
					//living.moveForward = (float)(Math.random() * (Math.PI * 2D));
					living.moveVertical = (float)(Math.random() * (Math.PI * 2D));
					living.rotationYaw = (float)(Math.random() * (Math.PI * 2D));
					living.cameraPitch = (float)(Math.random() * (Math.PI * 2D));
					living.rotationYawHead = (float)(Math.random() * (Math.PI * 2D));
					living.motionX = (float)(Math.random() * (Math.PI * 2D));
					living.motionZ = (float)(Math.random() * (Math.PI * 2D));
					living.limbSwingAmount = 0;
					living.limbSwing = 0;

					if(living instanceof EntityPlayer)
					{
						EntityPlayer player = (EntityPlayer)living;
						// These fields are public in EntityLivingBase and control movement input.
						player.moveForward = (float)(Math.random() * (Math.PI * 2D));
						player.moveStrafing = (float)(Math.random() * (Math.PI * 2D));
					}

					//living.prevRotationYawHead = 0;

					living.attackEntityFrom(IIDamageSources.NEUROPARALITIC_GAS, 2+amplifier);
				}
			}

			@Override
			public List<ItemStack> getCurativeItems()
			{
				return new ArrayList<>();
			}
		};
		neuroparalitic.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, Utils.generateNewUUID().toString(), -0.62342342f, 2);
		neuroparalitic.registerPotionAttributeModifier(SharedMonsterAttributes.FOLLOW_RANGE, Utils.generateNewUUID().toString(), -0.003921569f, 2);
		neuroparalitic.registerPotionAttributeModifier(SharedMonsterAttributes.FLYING_SPEED, Utils.generateNewUUID().toString(), -1f, 2);
		neuroparalitic.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, Utils.generateNewUUID().toString(), -1f, 2);
		neuroparalitic.registerPotionAttributeModifier(SharedMonsterAttributes.KNOCKBACK_RESISTANCE, Utils.generateNewUUID().toString(), -1f, 2);
		neuroparalitic.registerPotionAttributeModifier(SharedMonsterAttributes.MAX_HEALTH, Utils.generateNewUUID().toString(), -0.2f, 2);

		//23.04.2026 Carver: experiment: full paralysis potion effect. Inspired by s&r effect (taken motion and parameters). Used as a complimentary effect.
		fullparalysis = new IIPotion("fullparalysis", true, 0x7d1b19)
		{
			@Override
			public void performEffect(EntityLivingBase living, int amplifier)
			{
				if(living.ticksExisted%20!=0)
					return;
				boolean apply = false;
				for(ItemStack s : living.getArmorInventoryList())
				{
					if(!(s.getItem() instanceof IGasmask))
						apply = true;
					else if(!((IGasmask)s.getItem()).protectsFromGasses(s))
						apply = true;
					else if(!(s.getItem() instanceof IRadiationProtectionEquipment))
						apply = true;
					else if(!((IRadiationProtectionEquipment)s.getItem()).protectsFromRadiation(s))
						apply = true;
				}
				if(apply)
				{
					living.motionX = 0.0D;
					living.motionZ = 0.0D;
					if(living instanceof EntityPlayer)
					{
						EntityPlayer player = (EntityPlayer)living;
						// These fields are public in EntityLivingBase and control movement input.
						player.moveForward = 0;
						player.moveStrafing = 0;
					}
				}
			}
			@Override
			public List<ItemStack> getCurativeItems()
			{
				return new ArrayList<>();
			}
		};

		//Annoying poison and blindness. Typically not very lethal by itself. Countered by gasmask and hazmat, but with gasmask only - just the stun as it irritates skin.

		poisonirritant = new IIPotion("irritant", true, 0x948d13)
		{
			@Override
			public void performEffect(EntityLivingBase living, int amplifier)
			{
				if(living.ticksExisted%20!=0)
					return;
				boolean apply = false;
				boolean apply2 = false;
				for(ItemStack s : living.getArmorInventoryList())
				{
					if(!(s.getItem() instanceof IGasmask))
						apply = true;
					else if(!((IGasmask)s.getItem()).protectsFromGasses(s))
						apply = true;
					else if(!(s.getItem() instanceof IRadiationProtectionEquipment))
						apply2 = true;
					else if(!((IRadiationProtectionEquipment)s.getItem()).protectsFromRadiation(s))
						apply2 = true;
				}
				if(apply)
				{
					living.hurtResistantTime = 0;
					living.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 180,10));
					living.addPotionEffect(new PotionEffect(MobEffects.POISON, 640,amplifier));
					living.addPotionEffect(new PotionEffect(IEPotions.stunned, 380,amplifier));
					living.addPotionEffect(new PotionEffect(IIPotions.suppression, 380,amplifier));
				}
				if(apply2)
				{
					living.addPotionEffect(new PotionEffect(IEPotions.stunned, 120,amplifier));
					living.addPotionEffect(new PotionEffect(IIPotions.suppression, 380,amplifier));
				}
			}

			@Override
			public List<ItemStack> getCurativeItems()
			{
				return new ArrayList<>();
			}
		};
		poisonirritant.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, Utils.generateNewUUID().toString(), -0.4921569f, 2);
		poisonirritant.registerPotionAttributeModifier(SharedMonsterAttributes.FOLLOW_RANGE, Utils.generateNewUUID().toString(), -0.8921569f, 2);
		poisonirritant.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, Utils.generateNewUUID().toString(), -0.53921569f, 2);
		poisonirritant.registerPotionAttributeModifier(SharedMonsterAttributes.FLYING_SPEED, Utils.generateNewUUID().toString(), -0.23921569f, 2);
		poisonirritant.registerPotionAttributeModifier(SharedMonsterAttributes.MAX_HEALTH, Utils.generateNewUUID().toString(), -0.2f, 2);

		//Will suffocate player. Countered by gasmask.

		suffocator = new IIPotion("suffocating", true, 0x1e9413)
		{
			@Override
			public void performEffect(EntityLivingBase living, int amplifier)
			{
				if(living.ticksExisted%20!=0)
					return;
				boolean apply = false;
				for(ItemStack s : living.getArmorInventoryList())
				{
					if(!(s.getItem() instanceof IGasmask))
						apply = true;
					else if(!((IGasmask)s.getItem()).protectsFromGasses(s))
						apply = true;
				}
				if(apply)
				{
					living.hurtResistantTime = 0;
					living.attackEntityFrom(IIDamageSources.SUFFOCATION_GAS, 2*amplifier);
				}
			}

			@Override
			public List<ItemStack> getCurativeItems()
			{
				return new ArrayList<>();
			}
		};
		suffocator.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, Utils.generateNewUUID().toString(), -0.2921569f, 2);
		suffocator.registerPotionAttributeModifier(SharedMonsterAttributes.FOLLOW_RANGE, Utils.generateNewUUID().toString(), -0.003921569f, 2);
		suffocator.registerPotionAttributeModifier(SharedMonsterAttributes.FLYING_SPEED, Utils.generateNewUUID().toString(), -0.003921569f, 2);
		suffocator.registerPotionAttributeModifier(SharedMonsterAttributes.MAX_HEALTH, Utils.generateNewUUID().toString(), -0.2f, 2);

		//a delayed suffication effect that takes effect only after a while. Used in gases like Phosgene. More of an invisible effect. Do not display effect.
		//optimal delay time is 288000 ticks.

		suffocatordelayed1 = new IIPotion("suffocating_delayed_onset", true, 0x1e9413)
		{
			@Override
			public void performEffect(EntityLivingBase living, int amplifier)
			{
				if(living.ticksExisted%40!=0)
					return;
				boolean apply = false;
				for(ItemStack s : living.getArmorInventoryList())
				{
					if(!(s.getItem() instanceof IGasmask))
						apply = true;
					else if(!((IGasmask)s.getItem()).protectsFromGasses(s))
						apply = true;
				}
				if(apply)
				{
					living.addPotionEffect(new PotionEffect(IIPotions.suffocatordelayed2, 288000,0));
				}
			}

			@Override
			public List<ItemStack> getCurativeItems()
			{
				return new ArrayList<>();
			}
		};

		//invisible effect
		suffocatordelayed2 = new IIPotion("suffocating_delayed_accumulating", true, 0x1e9413)
		{
			@Override
			public void performEffect(EntityLivingBase living, int amplifier)
			{
				if(living.ticksExisted%288000!=0)
					return;
				boolean apply = false;
				for(ItemStack s : living.getArmorInventoryList())
				{
					if(!(s.getItem() instanceof IGasmask))
						apply = true;
					else if(!((IGasmask)s.getItem()).protectsFromGasses(s))
						apply = true;
				}
				if(apply)
				{
					living.addPotionEffect(new PotionEffect(IIPotions.suffocator, 4000,4));
				}
			}

			@Override
			public List<ItemStack> getCurativeItems()
			{
				return new ArrayList<>();
			}
		};

		suffocatordelayed2.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, Utils.generateNewUUID().toString(), -0.121569f, 2);
		suffocatordelayed2.registerPotionAttributeModifier(SharedMonsterAttributes.FOLLOW_RANGE, Utils.generateNewUUID().toString(), -0.1921569f, 2);
		suffocatordelayed2.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, Utils.generateNewUUID().toString(), -0.23921569f, 2);
		suffocatordelayed2.registerPotionAttributeModifier(SharedMonsterAttributes.FLYING_SPEED, Utils.generateNewUUID().toString(), -0.23921569f, 2);

		//Potential to do: healing inhibition, blocking any sort of healing isntead of doing damage. For now replaced with -20% maximum health for afflicted targets.

		//TODO: Restructure the effect condition application. Utilize hazmat protection from compat to make effects like gas or  viruses not applicable initially.

		//End of gas code experiments

		//23.04.2026 Carver: Biological/viral agents.
		// This one should spread to nearby entities and etc, making it viral. WIP. Names are subject to  change.
		//Taken from/based upon s&r COTH effect (for spread ability mainly).

		//This variant does not "upgrade" in stages, instead just damages overtime and spreads. Needs both hazmat and gasmask for protection.

		bioweapon1 = new IIPotion("viral1", true, 0x1e9413)
		{

			@Override
			public List<ItemStack> getCurativeItems()
			{
				return new ArrayList<>();
			}

			@Override
			public void performEffect(EntityLivingBase living, int amplifier)
			{
				boolean flagPr = living instanceof EntityLiving;
				boolean flag = !living.world.isRemote;

				int dur = living.getActivePotionEffect(IIPotions.bioweapon1).getDuration();

				if(!flagPr)
					return;

				boolean tickFlag = living.ticksExisted%20==0;

				boolean apply = false;
				for(ItemStack s : living.getArmorInventoryList())
				{
					if(!(s.getItem() instanceof IGasmask))
						apply = true;
					else if(!((IGasmask)s.getItem()).protectsFromGasses(s))
						apply = true;
					else if(!(s.getItem() instanceof IRadiationProtectionEquipment))
						apply = true;
					else if(!((IRadiationProtectionEquipment)s.getItem()).protectsFromRadiation(s))
						apply = true;
				}
				if(apply)
				{
					living.hurtResistantTime = 0;
					living.attackEntityFrom(IIDamageSources.VIRAL_DAMAGE, 1*amplifier);

					//when below X amount of health, paralizes and blinds target

					if(living.getMaxHealth()/2 > living.getHealth()
							&&living.getHealth() > 0) {

						living.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 500,10));
						living.addPotionEffect(new PotionEffect(IIPotions.fullparalysis, 500,1));
					}
				}
				switch(amplifier) {
					case 0:
						if(flag)
							effectExtend(dur, 20*10, living, amplifier, flagPr);
						break;

					case 1:
						if(flag) {
							if(tickFlag) {
								//entity living base, radius of spread around entity
								InfectNearby(living, 3);
								effectExtend(dur, 20*10, living, amplifier, flagPr);
							}
						}
						break;

					default:
						if(flag) {
							if(tickFlag) {
								InfectNearby(living, 4);
								effectExtend(dur, 20*10, living, amplifier, flagPr);
							}
						}
						break;
				}
			}

			private void effectExtend(int duration, int durationCheck, EntityLivingBase entity, int amplifier, boolean flagPr) {
				if(duration < durationCheck) {

					int cothDur = 180 * 20;
					amplifier = Math.min(amplifier + 1, 2);
					NBTTagCompound tags = entity.getEntityData();

					if(flagPr) {
						entity.addPotionEffect(new PotionEffect(IIPotions.bioweapon1, cothDur, amplifier, false, false));

					}else entity.removeActivePotionEffect(IIPotions.bioweapon1);
				}
			}

			private void InfectNearby(EntityLivingBase entity, int range)
			{
				if(3 == 0) return;
				if(entity.isPotionActive(IIPotions.bioweapon1)) return;

				if(entity instanceof EntityPlayer) {
					EntityPlayer player = (EntityPlayer) entity;
					if(player.capabilities.isCreativeMode) return;
				}
				AxisAlignedBB axisalignedbb = (
						new AxisAlignedBB(entity.posX, entity.posY, entity.posZ, entity.posX + 1, entity.posY + 1, entity.posZ + 1)).grow(range);
				List<EntityLivingBase> moblist = entity.world.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);

				for (EntityLivingBase mob : moblist)
				{
					boolean apply3 = false;
					for(ItemStack s2 : entity.getArmorInventoryList())
					{
						if(!(s2.getItem() instanceof IGasmask))
							apply3 = true;
						else if(!((IGasmask)s2.getItem()).protectsFromGasses(s2))
							apply3 = true;
						else if(!(s2.getItem() instanceof IRadiationProtectionEquipment))
							apply3 = true;
						else if(!((IRadiationProtectionEquipment)s2.getItem()).protectsFromRadiation(s2))
							apply3 = true;
					}
					if(apply3)
					{
						if(mob!=entity&&!mob.isPotionActive(IIPotions.bioweapon1))
						{
							if(!mob.isPotionActive(IIPotions.bioweapon1))
								mob.addPotionEffect(new PotionEffect(IIPotions.bioweapon1, 20*240, 0, false, false));
						}
					}
				}
			}
		};
		//Bioweapon experiments end
	}


	public static class IIPotion extends IEPotion
	{
		static ResourceLocation tex = new ResourceLocation(ImmersiveIntelligence.MODID, "textures/gui/potioneffects.png");
		static int iconID = 0;

		public IIPotion(String name, boolean isBad, int colour)
		{
			this(name, isBad, colour, 0, false, true, true);
		}

		public IIPotion(String name, boolean isBad, int colour, int tick, boolean halveTick, boolean showInInventory, boolean showInHud)
		{
			super(new ResourceLocation(ImmersiveIntelligence.MODID, name), isBad, colour, tick, halveTick, iconID++, showInInventory, showInHud);
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
