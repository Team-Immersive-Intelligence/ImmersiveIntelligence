package pl.pabilo8.immersiveintelligence.common;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces.IItemDamageableIE;
import blusunrize.immersiveengineering.common.util.IEPotions;
import blusunrize.immersiveengineering.common.util.IEPotions.IEPotion;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.CorrosionHandler;
import pl.pabilo8.immersiveintelligence.api.utils.armor.IGasmask;
import pl.pabilo8.immersiveintelligence.api.utils.armor.IRadiationProtectionEquipment;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.component.EntityGasCloud;
import pl.pabilo8.immersiveintelligence.common.util.IIDamageSources;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 03.03.2020
 */
public class IIPotions
{
	public static Potion suppression, brokenArmor, corrosion, infraredVision, ironWill, wellSupplied, concealed;
	public static Potion exposed, medicalTreatment, undergoingRepairs, radiation, nuclearHeat, movementAssist;
	public static Potion homeShores, homeland, heartland, foreignShores, enemySoil, enemysNest;
	public static Potion neuroparalitic, poisonirritant, suffocator, suffocatordelayed1, suffocatordelayed2, suffocatordelayed3, fullparalysis;
	public static Potion bioweapon1, bioweapon2_1, bioweapon2_2, bioweapon2_3, bioweapon2_4, bioweapon2_5;
	public static Potion bioweapon3_1, bioweapon3_2, bioweapon3_3;
	public static Potion bioweapon4_1, bioweapon4_2, bioweapon4_3, bioweapon4_4;
	public static Potion bioweapon5_1, bioweapon5_2;

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
		//Should also give movement to target in random horizontal but small directions.
		//Countered by BOTH hazmat and gasmask.

		neuroparalitic = new IIPotion("neuroparalitic", true, 0x7d1b19)
		{
			@Override
			public void performEffect(EntityLivingBase living, int amplifier)
			{
				if(living.ticksExisted%10==0)
					return;

				living.jumpMovementFactor = 0;
				living.hurtResistantTime = 0;
				//living.moveStrafing = (float)(Math.random() * (Math.PI * 2D));
				//living.moveForward = (float)(Math.random() * (Math.PI * 2D));
				living.moveVertical = (float)(Math.random()*(Math.PI*2D));
				living.rotationYaw = (float)(Math.random()*(Math.PI*2D));
				//living.cameraPitch = (float)(Math.random()*(Math.PI*2D));
				living.rotationYawHead = (float)(Math.random()*(Math.PI*2D));
				living.motionX = (float)(Math.random()*(Math.PI*2D));
				living.motionZ = (float)(Math.random()*(Math.PI*2D));
				living.limbSwingAmount = 0;
				living.limbSwing = 0;

				if(living instanceof EntityPlayer)
				{
					EntityPlayer player = (EntityPlayer)living;
					// These fields are public in EntityLivingBase and control movement input.
					player.moveForward = (float)(Math.random()*(Math.PI*2D));
					player.moveStrafing = (float)(Math.random()*(Math.PI*2D));
				}

				//living.prevRotationYawHead = 0;

				living.attackEntityFrom(IIDamageSources.NEUROPARALITIC_GAS, 2+amplifier);
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

		//23.04.2026 Carver: experiment: full paralysis potion effect. Inspired by s&r effect (taken motion and parameters).
		// Used as a complimentary effect by other potions. Cannot be obtained from other sources.

		fullparalysis = new IIPotion("fullparalysis", true, 0x7d1b19)
		{
			@Override
			public void performEffect(EntityLivingBase living, int amplifier)
			{
				if(living.ticksExisted%5!=0)
					return;

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
			@Override
			public List<ItemStack> getCurativeItems()
			{
				return new ArrayList<>();
			}
		};

		//Annoying poison and blindness. Typically not very lethal by itself. Countered by gasmask and hazmat, but with gasmask only - just the stun as it irritates skin.
		//No gasmask: blindness and higher poison. If gasmask but no hazmat: just big suppression and some poison.

		poisonirritant = new IIPotion("irritant", true, 0x948d13)
		{
			@Override
			public void performEffect(EntityLivingBase living, int amplifier)
			{
				if(living.ticksExisted%10!=0)
					return;

				boolean apply = true;
				for(ItemStack s : living.getArmorInventoryList())
				{
					if(!(s.getItem() instanceof IGasmask))
						apply = false;
					else if(!((IGasmask)s.getItem()).protectsFromGasses(s))
						apply = false;
				}
				if(apply)
				{
					living.hurtResistantTime = 0;
					living.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 880, 10, false, false));
					living.addPotionEffect(new PotionEffect(MobEffects.POISON, 840, amplifier, false, false));
					living.addPotionEffect(new PotionEffect(IEPotions.stunned, 480, amplifier, false, false));
					living.addPotionEffect(new PotionEffect(IIPotions.suppression, 880, amplifier, false, false));
				}
				boolean apply2 = true;
				for(ItemStack s : living.getArmorInventoryList())
				{
					if(!(s.getItem() instanceof IRadiationProtectionEquipment))
						apply2 = false;
					else if(!((IRadiationProtectionEquipment)s.getItem()).protectsFromRadiation(s))
						apply2 = false;
				}
				if(apply2)
				{
					living.hurtResistantTime = 0;
					living.addPotionEffect(new PotionEffect(IEPotions.stunned, 280, amplifier, false, false));
					living.addPotionEffect(new PotionEffect(MobEffects.POISON, 140, 0, false, false));
					living.addPotionEffect(new PotionEffect(IIPotions.suppression, 280, amplifier, false, false));
				}
			}

			@Override
			public List<ItemStack> getCurativeItems()
			{
				return new ArrayList<>();
			}
		};
		poisonirritant.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, Utils.generateNewUUID().toString(), -0.3921569f, 2);
		poisonirritant.registerPotionAttributeModifier(SharedMonsterAttributes.FOLLOW_RANGE, Utils.generateNewUUID().toString(), -0.8921569f, 2);
		poisonirritant.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, Utils.generateNewUUID().toString(), -0.53921569f, 2);
		poisonirritant.registerPotionAttributeModifier(SharedMonsterAttributes.FLYING_SPEED, Utils.generateNewUUID().toString(), -0.23921569f, 2);
		poisonirritant.registerPotionAttributeModifier(SharedMonsterAttributes.MAX_HEALTH, Utils.generateNewUUID().toString(), -0.2f, 2);

		//Will suffocate player. Countered by gasmask. Unlike other effects, still applies but does no damage if there is gasmask.
		// Slows down though.

		suffocator = new IIPotion("suffocating", true, 0x1e9413)
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
				}
				if(apply)
				{
					living.hurtResistantTime = 0;
					living.jumpMovementFactor = 0;
					living.attackEntityFrom(IIDamageSources.SUFFOCATION_GAS, 2*amplifier);
				}
			}

			@Override
			public List<ItemStack> getCurativeItems()
			{
				return new ArrayList<>();
			}
		};
		suffocator.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, Utils.generateNewUUID().toString(), -0.3921569f, 2);
		suffocator.registerPotionAttributeModifier(SharedMonsterAttributes.FOLLOW_RANGE, Utils.generateNewUUID().toString(), -0.003921569f, 2);
		suffocator.registerPotionAttributeModifier(SharedMonsterAttributes.FLYING_SPEED, Utils.generateNewUUID().toString(), -0.003921569f, 2);
		suffocator.registerPotionAttributeModifier(SharedMonsterAttributes.MAX_HEALTH, Utils.generateNewUUID().toString(), -0.2f, 2);

		//Used in gases like Phosgene.
		//A delayed suffication effect that takes effect only after a while.  More of an invisible effect. Do not display effect.
		//optimal delay time is 288000 ticks.

		suffocatordelayed1 = new IIPotion("suffocating_delayed_onset", true, 0x1e9413)
		{
			@Override
			public void performEffect(EntityLivingBase living, int amplifier)
			{
				if(living.ticksExisted%780==0)
					return;
				living.addPotionEffect(new PotionEffect(IIPotions.suffocatordelayed2, 308000,0, false, false));
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
				if(living.ticksExisted%288000==0)
					return;
				living.addPotionEffect(new PotionEffect(IIPotions.suffocatordelayed3, 10000,0, false, false));
				}

			@Override
			public List<ItemStack> getCurativeItems()
			{
				return new ArrayList<>();
			}
		};

		suffocatordelayed2.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, Utils.generateNewUUID().toString(), -0.321569f, 2);
		suffocatordelayed2.registerPotionAttributeModifier(SharedMonsterAttributes.FOLLOW_RANGE, Utils.generateNewUUID().toString(), -0.4921569f, 2);
		suffocatordelayed2.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, Utils.generateNewUUID().toString(), -0.43921569f, 2);
		suffocatordelayed2.registerPotionAttributeModifier(SharedMonsterAttributes.FLYING_SPEED, Utils.generateNewUUID().toString(), -0.43921569f, 2);

		//invisible effect
		suffocatordelayed3 = new IIPotion("suffocating_delayed_terminal", true, 0x1e9413)
		{
			@Override
			public void performEffect(EntityLivingBase living, int amplifier)
			{
				if(living.ticksExisted%5!=0)
					return;
				living.hurtResistantTime = 0;
				living.attackEntityFrom(IIDamageSources.SUFFOCATION_GAS, 4);
			}

			@Override
			public List<ItemStack> getCurativeItems()
			{
				return new ArrayList<>();
			}
		};

		suffocatordelayed3.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, Utils.generateNewUUID().toString(), -0.721569f, 2);
		suffocatordelayed3.registerPotionAttributeModifier(SharedMonsterAttributes.FOLLOW_RANGE, Utils.generateNewUUID().toString(), -0.6921569f, 2);
		suffocatordelayed3.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, Utils.generateNewUUID().toString(), -0.83921569f, 2);
		suffocatordelayed3.registerPotionAttributeModifier(SharedMonsterAttributes.FLYING_SPEED, Utils.generateNewUUID().toString(), -0.83921569f, 2);

		//End of gas code experiments


		//23.04.2026 Carver: Biologicalviral agents.
		//Taken from/based upon s&r COTH effect (for spread ability mainly).

		//Can be either aerosolized (put into entitygascloud) or used on a storage tile entity to apply NBT tag to edible itemshat apply the effect of level 0.

		//TODO: add player entity check to also apply coughing (sound sfx). Take position of the entity and have it play sound.
		//Todo: remove these commented lines when all is done and bring them to the document for notation.

		//This variant (lineavirus) does not "upgrade" in stages, instead just damages overtime and spreads. Needs both hazmat and gasmask for protection.
		//Essentially, very aggressive direct effect that does damage on the onset.



		bioweapon1 = new IIPotion("lineavirus", true, 0x1e9413)
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

				int dur = Objects.requireNonNull(living.getActivePotionEffect(IIPotions.bioweapon1)).getDuration();

				if(!flagPr)
					return;

				boolean tickFlag = living.ticksExisted%20==0;

				//when below X amount of health, paralizes and blinds target

				living.hurtResistantTime = 0;
				living.attackEntityFrom(IIDamageSources.VIRAL_DAMAGE, 1*amplifier);

				if(living.getMaxHealth()/2 > living.getHealth()
						&&living.getHealth() > 0) {

					living.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 500,10, false, false));
					living.addPotionEffect(new PotionEffect(IIPotions.fullparalysis, 500,1));
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

					int effDur = 180 * 20;
					amplifier = Math.min(amplifier + 1, 2);

					if(flagPr) {
						entity.addPotionEffect(new PotionEffect(IIPotions.bioweapon1, effDur, amplifier, false, false));

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

		//28.04.2026 Carver: bioweapon 2

		//Botulism.

		//Ingradients are from fermented fish. Resilient spores in soil, dust, freshwater and marine sediments. Does not spread from person to person.

		//Curative item: Penicillin (Type B)
		//Stage 0: incubation. 30 min.
		//Stage 1: Minor symptoms. 20 min. Weakness 1, Mining fatigue 1
		//Stage 2: Intermediate symptoms. 15 min Weakness 2, Mining fatigue 2, Slowness 1.
		//Stage 3: Critical symptoms. 10 min Weakness 3, Mining fatigue 3 Slowness 2, respiratory failure. When ends Respiratory failure. Until death.

		bioweapon2_1 = new IIPotion("botulism_s0", true, 0x1e9413)
		{
			@Override
			public void performEffect(EntityLivingBase living, int amplifier)
			{
				if(living.ticksExisted%35990==0)
					return;
				living.addPotionEffect(new PotionEffect(IIPotions.bioweapon2_2, 24000,0, false, false));
			}

			@Override
			public List<ItemStack> getCurativeItems()
			{
				return new ArrayList<>();
			}
		};

		bioweapon2_2 = new IIPotion("botulism_s1", true, 0x1e9413)
		{
			@Override
			public void performEffect(EntityLivingBase living, int amplifier)
			{
				if(living.ticksExisted%23990==0)
					return;
				living.addPotionEffect(new PotionEffect(IIPotions.bioweapon2_3, 18000,0, false, false));

				if(living.ticksExisted%40==0)
					return;
				living.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 40,0, false, false));
				living.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 40,0, false, false));
			}

			@Override
			public List<ItemStack> getCurativeItems()
			{
				return new ArrayList<>();
			}
		};

		bioweapon2_2.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, Utils.generateNewUUID().toString(), -0.121569f, 2);
		bioweapon2_2.registerPotionAttributeModifier(SharedMonsterAttributes.FOLLOW_RANGE, Utils.generateNewUUID().toString(), -0.1921569f, 2);
		bioweapon2_2.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, Utils.generateNewUUID().toString(), -0.13921569f, 2);
		bioweapon2_2.registerPotionAttributeModifier(SharedMonsterAttributes.FLYING_SPEED, Utils.generateNewUUID().toString(), -0.13921569f, 2);

		bioweapon2_3 = new IIPotion("botulism_s2", true, 0x1e9413)
		{
			@Override
			public void performEffect(EntityLivingBase living, int amplifier)
			{
				if(living.ticksExisted%17990==0)
					return;
				living.addPotionEffect(new PotionEffect(IIPotions.bioweapon2_4, 12000,0, false, false));

				if(living.ticksExisted%40==0)
					return;
				living.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 40,0, false, false));
				living.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 40,2, false, false));
				living.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 40,2, false, false));
			}

			@Override
			public List<ItemStack> getCurativeItems()
			{
				return new ArrayList<>();
			}
		};

		bioweapon2_3.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, Utils.generateNewUUID().toString(), -0.221569f, 2);
		bioweapon2_3.registerPotionAttributeModifier(SharedMonsterAttributes.FOLLOW_RANGE, Utils.generateNewUUID().toString(), -0.2921569f, 2);
		bioweapon2_3.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, Utils.generateNewUUID().toString(), -0.23921569f, 2);
		bioweapon2_3.registerPotionAttributeModifier(SharedMonsterAttributes.FLYING_SPEED, Utils.generateNewUUID().toString(), -0.23921569f, 2);


		bioweapon2_4 = new IIPotion("botulism_s3_terminal", true, 0x1e9413)
		{
			@Override
			public void performEffect(EntityLivingBase living, int amplifier)
			{
				if(living.ticksExisted%11990==0)
					return;
				living.addPotionEffect(new PotionEffect(IIPotions.bioweapon2_4, 12000,0, false, false));
				living.addPotionEffect(new PotionEffect(IIPotions.bioweapon2_5, 12000,0, false, false));

				if(living.ticksExisted%40==0)
					return;
				living.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 40,2, false, false));
				living.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 40,3, false, false));
				living.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 40,3, false, false));
			}

			@Override
			public List<ItemStack> getCurativeItems()
			{
				return new ArrayList<>();
			}
		};

		bioweapon2_4.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, Utils.generateNewUUID().toString(), -0.321569f, 2);
		bioweapon2_4.registerPotionAttributeModifier(SharedMonsterAttributes.FOLLOW_RANGE, Utils.generateNewUUID().toString(), -0.3921569f, 2);
		bioweapon2_4.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, Utils.generateNewUUID().toString(), -0.33921569f, 2);
		bioweapon2_4.registerPotionAttributeModifier(SharedMonsterAttributes.FLYING_SPEED, Utils.generateNewUUID().toString(), -0.33921569f, 2);

		bioweapon2_5 = new IIPotion("deathfailure", true, 0x1e9413)
		{
			@Override
			public void performEffect(EntityLivingBase living, int amplifier)
			{
				if(living.ticksExisted%5!=0)
					return;
				living.hurtResistantTime = 0;
				living.attackEntityFrom(IIDamageSources.VIRAL_DAMAGE, 4);
			}

			@Override
			public List<ItemStack> getCurativeItems()
			{
				return new ArrayList<>();
			}
		};
		//bioweapon 3

		//Brucellosis

		//made from milk, or meat of cow, pig, sheep. Does not spread person to person.

		//Curative item: Penicillin (Type A)
		//Does not spead. Nonlethal. More for incapacitation.

		//Stage 0: Incubation. 20 min.
		//Stage 1: Minor symptoms. 20 min. Intermediate muscle pain, Intermediate  fever,  Nausea 1 for 15 seconds intervals.
		//Stage 2: Intermediate symptoms. 10 min.  Crippling muscle pain, Critically high fever, Nausea 2 for 20 seconds intervals.

		bioweapon3_1 = new IIPotion("brucellosis_s0", true, 0x1e9413)
		{
			@Override
			public void performEffect(EntityLivingBase living, int amplifier)
			{
				if(living.ticksExisted%23990==0)
					return;
				living.addPotionEffect(new PotionEffect(IIPotions.bioweapon3_2, 24000,0, false, false));
			}

			@Override
			public List<ItemStack> getCurativeItems()
			{
				return new ArrayList<>();
			}
		};

		bioweapon3_2 = new IIPotion("brucellosis_s1", true, 0x1e9413)
		{
			@Override
			public void performEffect(EntityLivingBase living, int amplifier)
			{
				if(living.ticksExisted%23990==0)
					return;
				living.addPotionEffect(new PotionEffect(IIPotions.bioweapon3_3, 24000,0, false, false));

				if(living.ticksExisted%2000==0)
					return;
				living.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 300,0, false, false));
			}

			@Override
			public List<ItemStack> getCurativeItems()
			{
				return new ArrayList<>();
			}
		};

		bioweapon3_2.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, Utils.generateNewUUID().toString(), -0.121569f, 2);
		bioweapon3_2.registerPotionAttributeModifier(SharedMonsterAttributes.FOLLOW_RANGE, Utils.generateNewUUID().toString(), -0.1921569f, 2);
		bioweapon3_2.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, Utils.generateNewUUID().toString(), -0.13921569f, 2);
		bioweapon3_2.registerPotionAttributeModifier(SharedMonsterAttributes.FLYING_SPEED, Utils.generateNewUUID().toString(), -0.13921569f, 2);

		bioweapon3_3 = new IIPotion("brucellosis_s2", true, 0x1e9413)
		{
			@Override
			public void performEffect(EntityLivingBase living, int amplifier)
			{
				if(living.ticksExisted%23990==0)
					return;
				living.addPotionEffect(new PotionEffect(IIPotions.bioweapon3_3, 24000,0, false, false));
				living.addPotionEffect(new PotionEffect(IIPotions.suppression, 24000,2, false, false));

				if(living.ticksExisted%1500==0)
					return;
				living.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 400,0, false, false));
			}

			@Override
			public List<ItemStack> getCurativeItems()
			{
				return new ArrayList<>();
			}
		};

		bioweapon3_3.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, Utils.generateNewUUID().toString(), -0.321569f, 2);
		bioweapon3_3.registerPotionAttributeModifier(SharedMonsterAttributes.FOLLOW_RANGE, Utils.generateNewUUID().toString(), -0.2921569f, 2);
		bioweapon3_3.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, Utils.generateNewUUID().toString(), -0.23921569f, 2);
		bioweapon3_3.registerPotionAttributeModifier(SharedMonsterAttributes.FLYING_SPEED, Utils.generateNewUUID().toString(), -0.23921569f, 2);

		//bioweapon 4

		//Anthrax

		//Beef, or wool, or sheep meat. Or mycellium (as it is more hazardous as spores)

		//Curative item: Penicillin (Type C)
		//Does not spead. Otherwise identical to bioweapon 2-3.

		//Stage 0: Incubation. 25 min.
		//Stage 1: Minor symptoms. 15 min. Minor cough, Light muscle pain,
		//Stage 2: Intermediate symptoms. Intermediate cough, Intermediate muscle pain, Intermediate  fever, Less oxygen if swimming, Gets hunger from time to time in 5 sec.
		//Stage 3: Critical symptoms. 8 min.  Debilitating cough, Crippling muscle pain, Critically high fever, can’t run, can’t hold breath
		// Death after 8 minutes.

		bioweapon4_1 = new IIPotion("anthrax_s0", true, 0x1e9413)
		{
			@Override
			public void performEffect(EntityLivingBase living, int amplifier)
			{
				if(living.ticksExisted%29999==0)
					return;
				living.addPotionEffect(new PotionEffect(IIPotions.bioweapon4_2, 18000,0, false, false));
			}

			@Override
			public List<ItemStack> getCurativeItems()
			{
				return new ArrayList<>();
			}
		};

		bioweapon4_2 = new IIPotion("anthrax_s1", true, 0x1e9413)
		{
			@Override
			public void performEffect(EntityLivingBase living, int amplifier)
			{
				if(living.ticksExisted%17990==0)
					return;
				living.addPotionEffect(new PotionEffect(IIPotions.bioweapon4_3, 18000,0, false, false));

				if(living.ticksExisted%40==0)
					return;
				living.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 40,0, false, false));

			if(living.ticksExisted%6000==0)
					return;
				if(living instanceof EntityPlayer)
				{
					EntityPlayer player = (EntityPlayer)living;
					//Coughing sound sfx

					BlockPos pos = new BlockPos(player.posX, player.posY - 1, player.posZ);
					player.world.playSound(null, pos, IISounds.explosionFlare, null, 1.0F, 1.0F);
					living.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 60,0, false, false));
				}
			}

			@Override
			public List<ItemStack> getCurativeItems()
			{
				return new ArrayList<>();
			}
		};

		bioweapon4_2.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, Utils.generateNewUUID().toString(), -0.021569f, 2);
		bioweapon4_2.registerPotionAttributeModifier(SharedMonsterAttributes.FOLLOW_RANGE, Utils.generateNewUUID().toString(), -0.0921569f, 2);
		bioweapon4_2.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, Utils.generateNewUUID().toString(), -0.23921569f, 2);
		bioweapon4_2.registerPotionAttributeModifier(SharedMonsterAttributes.FLYING_SPEED, Utils.generateNewUUID().toString(), -0.03921569f, 2);

		bioweapon4_3 = new IIPotion("anthrax_s2", true, 0x1e9413)
		{
			@Override
			public void performEffect(EntityLivingBase living, int amplifier)
			{
				if(living.ticksExisted%17990==0)
					return;
				living.addPotionEffect(new PotionEffect(IIPotions.bioweapon4_4, 9600,0, false, false));
				living.addPotionEffect(new PotionEffect(IIPotions.suppression, 9600,2, false, false));

				if(living.ticksExisted%1500==0)
					return;
				living.addPotionEffect(new PotionEffect(MobEffects.HUNGER, 400,2, false, false));

				if(living.ticksExisted%20==0)
					return;
				living.addPotionEffect(new PotionEffect(MobEffects.SATURATION, 20,-1, false, false));

				if(living.ticksExisted%3000==0)
					return;
				if(living instanceof EntityPlayer)
				{
					EntityPlayer player = (EntityPlayer)living;
					//Coughing sound sfx

					BlockPos pos = new BlockPos(player.posX, player.posY-1, player.posZ);
					player.world.playSound(null, pos, IISounds.explosionFlare, null, 1.0F, 1.0F);
					living.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 60, 0, false, false));
				}
			}

			@Override
			public List<ItemStack> getCurativeItems()
			{
				return new ArrayList<>();
			}
		};

		bioweapon4_3.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, Utils.generateNewUUID().toString(), -0.121569f, 2);
		bioweapon4_3.registerPotionAttributeModifier(SharedMonsterAttributes.FOLLOW_RANGE, Utils.generateNewUUID().toString(), -0.1921569f, 2);
		bioweapon4_3.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, Utils.generateNewUUID().toString(), -0.53921569f, 2);
		bioweapon4_3.registerPotionAttributeModifier(SharedMonsterAttributes.FLYING_SPEED, Utils.generateNewUUID().toString(), -0.13921569f, 2);


		bioweapon4_4 = new IIPotion("anthrax_s3", true, 0x1e9413)
		{
			@Override
			public void performEffect(EntityLivingBase living, int amplifier)
			{
				if(living.ticksExisted%9590==0)
					return;
				living.addPotionEffect(new PotionEffect(IIPotions.bioweapon2_5, 9600,0, false, false));
				living.addPotionEffect(new PotionEffect(IIPotions.suppression, 10000,2, false, false));
				living.addPotionEffect(new PotionEffect(IIPotions.bioweapon4_4, 9600,0, false, false));

				if(living.ticksExisted%1000==0)
					return;
				living.addPotionEffect(new PotionEffect(MobEffects.HUNGER, 400,4, false, false));

				if(living.ticksExisted%20==0)
					return;
				living.addPotionEffect(new PotionEffect(MobEffects.SATURATION, 20,-2, false, false));

				if(living.ticksExisted%2000==0)
					return;
				if(living instanceof EntityPlayer)
				{
					EntityPlayer player = (EntityPlayer)living;
					//Coughing sound sfx

					BlockPos pos = new BlockPos(player.posX, player.posY-1, player.posZ);
					player.world.playSound(null, pos, IISounds.explosionFlare, null, 1.0F, 1.0F);
					living.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 60, 0, false, false));
				}
			}

			@Override
			public List<ItemStack> getCurativeItems()
			{
				return new ArrayList<>();
			}
		};

		bioweapon4_4.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, Utils.generateNewUUID().toString(), -0.621569f, 2);
		bioweapon4_4.registerPotionAttributeModifier(SharedMonsterAttributes.FOLLOW_RANGE, Utils.generateNewUUID().toString(), -0.5921569f, 2);
		bioweapon4_4.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, Utils.generateNewUUID().toString(), -0.83921569f, 2);
		bioweapon4_4.registerPotionAttributeModifier(SharedMonsterAttributes.FLYING_SPEED, Utils.generateNewUUID().toString(), -0.63921569f, 2);


		//Bubonic plague. Transmits between entities. Made from massive amounts of rotten flesh. Cured by antibiotics.

		bioweapon5_1 = new IIPotion("bubonicplague_initial", true, 0x1e9413)
		{
			@Override
			public void performEffect(EntityLivingBase living, int amplifier)
			{
				//2 days period
				if(living.ticksExisted%3456000==0)
					return;
				living.addPotionEffect(new PotionEffect(IIPotions.bioweapon5_2, 10000,0, false, false));


				if(living.ticksExisted%20==0)
					return;
				living.addPotionEffect(new PotionEffect(MobEffects.SATURATION, 20,-1, false, false));
				living.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 20,0, false, false));
				living.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 20,1, false, false));

				if(living.ticksExisted%7000==0)
					return;
				if(living instanceof EntityPlayer)
				{
					EntityPlayer player = (EntityPlayer)living;
					//Coughing sound sfx

					BlockPos pos = new BlockPos(player.posX, player.posY-1, player.posZ);
					player.world.playSound(null, pos, IISounds.explosionFlare, null, 1.0F, 1.0F);
					living.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 60, 0, false, false));
				}
			}

			@Override
			public List<ItemStack> getCurativeItems()
			{
				return new ArrayList<>();
			}
		};

		//Pneumonic stage

		bioweapon5_2 = new IIPotion("bubonicplague_infectious", true, 0x1e9413)
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

				int dur = Objects.requireNonNull(living.getActivePotionEffect(IIPotions.bioweapon5_2)).getDuration();

				if(!flagPr)
					return;

				boolean tickFlag = living.ticksExisted%20==0;


				living.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 20,amplifier, false, false));
				living.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 20,5, false, false));
				//becomes two times more vulnerable to damage. Buboes.
				living.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 20,-2, false, false));


				//TODO: FIX

				if(living.ticksExisted%3000==0)
					return;
				if(living instanceof EntityPlayer)
				{
					EntityPlayer player = (EntityPlayer)living;
					//Coughing sound sfx

					BlockPos pos = new BlockPos(player.posX, player.posY-1, player.posZ);
					player.world.playSound(null, pos, IISounds.explosionFlare, null, 1.0F, 1.0F);
					living.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 60, 0, false, false));

					living.hurtResistantTime = 0;
					living.attackEntityFrom(IIDamageSources.VIRAL_DAMAGE, 5);

				}
				//TODO: FIX
				//when below X amount of health, spawns a cloud that contaminates the area

			/*	if(living.getMaxHealth()/2 > living.getHealth()
						&&living.getHealth() > 0) {

					BlockPos pos = new BlockPos(living.posX, living.posY+1, living.posZ);
					Fluid fluid = FluidRegistry.getFluidStack("plague", 40000).getFluid();
					Block blockGasPlagueGas = fluid.getBlock();
					World world;

					boolean flag2 = living.world.isRemote;

					if(flag2)
						return;

					Vec3d v = new Vec3d(0, -1, 0);
					BlockPos p = new BlockPos(pos);
					Vec3d throwerPos = new Vec3d(p.offset(EnumFacing.UP, 3));

					EntityGasCloud gasCloud = new EntityGasCloud(world, throwerPos.x+v.x*2, throwerPos.y+v.y*2,
							throwerPos.z+v.z*2, new FluidStack(fluid, (int)(1000)));
					world.spawnEntity(gasCloud);

				}*/
				switch(amplifier) {
					case 0:
						if(flag)
							effectExtend(dur, 20*10, living, amplifier, flagPr);
						break;

					case 1:
						if(flag) {
							if(tickFlag) {
								//entity living base, radius of spread around entity
								InfectNearby(living, 5);
								effectExtend(dur, 20*10, living, amplifier, flagPr);
							}
						}
						break;

					default:
						if(flag) {
							if(tickFlag) {
								InfectNearby(living, 6);
								effectExtend(dur, 20*10, living, amplifier, flagPr);
							}
						}
						break;
				}
			}

			private void effectExtend(int duration, int durationCheck, EntityLivingBase entity, int amplifier, boolean flagPr) {
				if(duration < durationCheck) {

					int effDur = 180 * 20;
					amplifier = Math.min(amplifier + 1, 2);

					if(flagPr) {
						entity.addPotionEffect(new PotionEffect(IIPotions.bioweapon5_2, effDur, amplifier, false, false));

					}else entity.removeActivePotionEffect(IIPotions.bioweapon5_2);
				}
			}

			private void InfectNearby(EntityLivingBase entity, int range)
			{
				if(3 == 0) return;
				if(entity.isPotionActive(IIPotions.bioweapon5_2)) return;

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
						if(mob!=entity&&!mob.isPotionActive(IIPotions.bioweapon5_1))
						{
							if(!mob.isPotionActive(IIPotions.bioweapon5_1))
								mob.addPotionEffect(new PotionEffect(IIPotions.bioweapon5_1, 20*240, 0, false, false));
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
