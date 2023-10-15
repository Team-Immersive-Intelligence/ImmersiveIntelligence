package pl.pabilo8.immersiveintelligence.common.item.weapons;

import blusunrize.immersiveengineering.client.models.IOBJModelCallback;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.gui.IESlot;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import com.google.common.collect.Multimap;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoUtils;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmo;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler.IAdvancedTooltipItem;
import pl.pabilo8.immersiveintelligence.api.utils.tools.ISkinnable;
import pl.pabilo8.immersiveintelligence.client.ClientProxy;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIUpgradableItemRendererAMT;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.entity.bullet.EntityBullet;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ammohandler.AmmoHandler;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageItemKeybind;
import pl.pabilo8.immersiveintelligence.common.util.AdvancedSounds.RangedSound;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIIUpgradableTool;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class ItemIIGunBase extends ItemIIUpgradableTool implements ISkinnable, IAdvancedTooltipItem, IOBJModelCallback<ItemStack>
{
	//--- NBT Values Reference ---//
	public static final String RELOADING = "reloading";
	public static final String AIMING = "aiming";
	public static final String SHOULD_RELOAD = "shouldReload";
	public static final String RECOIL_H = "recoilH";
	public static final String RECOIL_V = "recoilV";
	public static final String FIRE_DELAY = "fireDelay";

	public static final String MAGAZINE = "magazine";
	public static final String BULLETS = "bullets";

	public ItemIIGunBase(String name)
	{
		super(name, 1, name.toUpperCase());
		//Use interfaces pls Blu
		fixupItem();
	}

	//--- Base ---//

	/**
	 * Standard method for foreign item classes
	 */
	public void fixupItem()
	{
		//First, get the item out of IE's registries.
		Item rItem = IEContent.registeredIEItems.remove(IEContent.registeredIEItems.size()-1);
		if(rItem!=this) throw new IllegalStateException("fixupItem was not called at the appropriate time");

		//Now, reconfigure the block to match our mod.
		this.setUnlocalizedName(ImmersiveIntelligence.MODID+"."+this.itemName);
		this.setCreativeTab(IIContent.II_CREATIVE_TAB);

		//And add it to our registries.
		IIContent.ITEMS.add(this);
	}

	//--- ItemUpgradeableTool ---//

	@Override
	public boolean canModify(ItemStack stack)
	{
		return getSlotCount(stack) > 0;
	}

	@Override
	public Slot[] getWorkbenchSlots(Container container, ItemStack stack)
	{
		IItemHandler inv = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		final String upgradeType = itemName.toUpperCase();

		ArrayList<Slot> list = new ArrayList<>();
		for(int i = 0; i < getSlotCount(stack); i++)
			list.add(new IESlot.Upgrades(container, inv, i,
					80+((i%3)*20), 32+((i/3)*20), upgradeType,
					stack, true
			));

		return list.toArray(new Slot[0]);
	}

	//--- ISkinnable ---//
	@Nonnull
	@Override
	public IRarity getForgeRarity(@Nonnull ItemStack stack)
	{
		IRarity skin = getSkinRarity(stack);
		return skin!=null?skin: super.getForgeRarity(stack);
	}

	@Override
	public String getSkinnableName()
	{
		return itemName;
	}

	@Override
	public String getSkinnableDefaultTextureLocation()
	{
		return ImmersiveIntelligence.MODID+":textures/items/weapons/";
	}

	//--- Vanilla ---//

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(@Nonnull ItemStack stack, @Nullable World world, @Nonnull List<String> tooltip, @Nonnull ITooltipFlag flag)
	{
		super.addInformation(stack, world, tooltip, flag);

		//Add stored bullets info
		getAmmoHandler(stack).addAmmoInformation(stack, world, tooltip, flag);

		//Add II Contributor Skin tooltip
		addSkinTooltip(stack, tooltip);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addAdvancedInformation(ItemStack stack, int offsetX, List<Integer> offsetsY)
	{
		ItemTooltipHandler.drawItemList(offsetX, offsetsY.get(0), getAmmoList(stack));
	}

	@SideOnly(Side.CLIENT)
	@Nullable
	@Override
	public FontRenderer getFontRenderer(ItemStack stack)
	{
		return IIClientUtils.fontRegular;
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
	{
		return slotChanged;
	}

	@Nonnull
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(@Nonnull EntityEquipmentSlot slot, @Nonnull ItemStack stack)
	{
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
		if(slot!=EntityEquipmentSlot.MAINHAND)
			return multimap;

		//slot switching / melee attack speed
		multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER,
				"Weapon modifier", getEquipSpeed(stack, getNBT(stack))*-3f, 0));

		double melee = getUpgrades(stack).getFloat("melee");
		if(melee!=0)
		{
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER,
					"Weapon modifier", melee, 0));
		}
		return multimap;
	}

	@Nonnull
	@Override
	public EnumAction getItemUseAction(@Nonnull ItemStack stack)
	{
		return EnumAction.NONE;
	}

	@Override
	public int getMaxItemUseDuration(@Nonnull ItemStack stack)
	{
		return getFireDelay(stack, EasyNBT.wrapNBT(stack.getTagCompound()))+1;
	}

	//--- Gun Handling ---//

	@Override
	public void onUpdate(ItemStack stack, World world, Entity user, int itemSlot, boolean isSelected)
	{
		super.onUpdate(stack, world, user, itemSlot, isSelected);

		if(!isSelected||!(user instanceof EntityLivingBase)||!((EntityLivingBase)user).getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).equals(stack))
			return;

		//Setup variables
		EasyNBT nbt = getNBT(stack);
		EasyNBT upgrades = getUpgradesNBT(stack);
		AmmoHandler ammoHandler = getAmmoHandler(stack);

		int currentAim = nbt.getInt(AIMING), fireDelay = nbt.getInt(FIRE_DELAY);
		float recoilH = nbt.getFloat(RECOIL_H), recoilV = nbt.getFloat(RECOIL_V);
		int reloading = nbt.getInt(RELOADING);
		boolean shouldReload = nbt.getBoolean(SHOULD_RELOAD);
		boolean isAimed = currentAim > getAimingTime(stack, upgrades);
		float recoilDecay = getRecoilDecay(stack, nbt, isAimed);

		//Increase/decrease aiming time counter
		if(reloading==0&&user.isSneaking())
			currentAim = MathHelper.clamp(currentAim+1, 0, getAimingTime(stack, upgrades));
		else if(currentAim > 0)
			currentAim = MathHelper.clamp(currentAim-3, 0, getAimingTime(stack, upgrades));

		//Decrease time until next shot is possible
		if(fireDelay > 0)
			fireDelay--;

		//Decrease previous recoil
		recoilH = Math.max(recoilH-recoilDecay, 0);
		recoilV = Math.max(recoilV-recoilDecay, 0);

		//Handle reloading request
		if(world.isRemote)
			if(!shouldReload&&ClientProxy.keybind_manualReload.isKeyDown())
				IIPacketHandler.sendToServer(new MessageItemKeybind(MessageItemKeybind.KEYBIND_GUN_RELOAD));

		//handle reloading
		if(shouldReload)
		{
			reloading = ammoHandler.reloadWeapon(stack, world, user, nbt, upgrades, reloading);
			if(reloading==0)
				shouldReload = false;
		}
		else
			reloading = 0;


		//Save parameters
		nbt.withInt(RELOADING, reloading);
		nbt.withInt(AIMING, currentAim);
		nbt.withBoolean(SHOULD_RELOAD, shouldReload);
		nbt.withFloat(RECOIL_H, recoilH);
		nbt.withFloat(RECOIL_V, recoilV);
		nbt.withInt(FIRE_DELAY, fireDelay);

	}

	@Override
	public void onUsingTick(@Nonnull ItemStack stack, EntityLivingBase user, int count)
	{
		switch(getFireMode(stack))
		{
			case SINGULAR:
				if(!(user instanceof EntityPlayer)) //for hanses
					shoot(stack, user, count);
				break;
			case AUTOMATIC:
				shoot(stack, user, count);
				break;
			case SINGULAR_CHARGED:
			{
				EasyNBT nbt = getNBT(stack);
				if(!getAmmoHandler(stack).canFire(stack, nbt))
					break;

				if(count==getMaxItemUseDuration(stack))
				{
					nbt.withInt(FIRE_DELAY, getFireDelay(stack, nbt));
					SoundEvent sound = getChargeFireSound(stack, nbt);
					if(sound!=null)
						user.world.playSound(null, user.posX, user.posY, user.posZ, sound, SoundCategory.PLAYERS, 0.5f, 0.9f);
				}
				shoot(stack, user, count);
			}
			break;
			default:
				break;
		}

		//stack.setTagCompound(nbt.unwrap());
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, EntityPlayer player, @Nonnull EnumHand hand)
	{
		ItemStack weapon = player.getHeldItem(hand);
		AmmoHandler ammoHandler = getAmmoHandler(weapon);

		if(hand==EnumHand.MAIN_HAND)
		{
			player.setActiveHand(hand);
			if(getFireMode(weapon)==FireModeType.SINGULAR)
			{
				shoot(weapon, player, 1);
				return new ActionResult<>(EnumActionResult.FAIL, weapon);
			}

			if(ammoHandler.canFire(weapon, getNBT(weapon)))
				return new ActionResult<>(EnumActionResult.FAIL, weapon);

		}
		return new ActionResult<>(EnumActionResult.FAIL, weapon);
	}

	protected void shoot(@Nonnull ItemStack weapon, EntityLivingBase user, int count)
	{
		//Setup variables
		World world = user.getEntityWorld();
		EasyNBT nbt = getNBT(weapon);
		EasyNBT upgrades = getUpgradesNBT(weapon);
		AmmoHandler ammoHandler = getAmmoHandler(weapon);

		//Gun cannot shoot if it isn't loaded
		if(!ammoHandler.canFire(weapon, nbt))
		{
			SoundEvent sound = getDryfireSound(weapon, upgrades);
			if(sound!=null&&count==1)
				world.playSound(null, user.posX, user.posY, user.posZ, sound, SoundCategory.PLAYERS, 0.25f, 0.9f);
			return;
		}

		//Try to shoot
		if(nbt.getInt(FIRE_DELAY)==0)
		{
			//Set shot delay
			nbt.withInt(FIRE_DELAY, getFireDelay(weapon, nbt));

			if(!world.isRemote)
			{
				boolean isAimed = nbt.getInt(AIMING) > getAimingTime(weapon, upgrades);
				float recoilH = nbt.getFloat(RECOIL_H), recoilHAdded = getHorizontalRecoil(weapon, upgrades, isAimed);
				float recoilV = nbt.getFloat(RECOIL_V), recoilVAdded = getVerticalRecoil(weapon, upgrades, isAimed);

				//Get the aim (look) vector
				Vec3d vec = IIUtils.getVectorForRotation(
						user.rotationPitch+getActualRecoil(recoilV, recoilVAdded),
						user.getRotationYawHead()+getActualRecoil(recoilH, recoilHAdded)
				);
				Vec3d vv = user.getPositionVector().addVector(0, (double)user.getEyeHeight()-0.10000000149011612D, 0);

				//Extract the bullet from the magazine or gun directly
				ItemStack ammo = ammoHandler.getNextAmmo(weapon, nbt, true);

				//Shoot the bullet
				createProjectile(user, world, vec, vv, weapon, nbt, ammo);

				//Attract the enemies
				int enemyAttractRange = getEnemyAttractRange(weapon, upgrades);
				if(enemyAttractRange > 0)
					Utils.attractEnemies(user, enemyAttractRange);

				//Play the shot sound
				RangedSound sound = getFireSound(weapon, upgrades);
				if(sound!=null)
					IIPacketHandler.playRangedSound(world, user.getPositionVector().add(vec),
							sound, SoundCategory.PLAYERS, 75, 1.5f,
							1f+(float)(Utils.RAND.nextGaussian()*0.02)
					);

				//Create the gunfire particle effect
				/*IIPacketHandler.INSTANCE.sendToAllAround(
						new MessageParticleGunfire(user, 1.5f),
						IIPacketHandler.targetPointFromEntity(user, 32));*/

				//Add recoil
				nbt.withFloat(RECOIL_H, MathHelper.clamp(
						recoilH+getHorizontalRecoil(weapon, upgrades, isAimed),
						0,
						getMaxHorizontalRecoil(weapon, upgrades)
				));
				nbt.withFloat(RECOIL_V, MathHelper.clamp(
						recoilV+getVerticalRecoil(weapon, upgrades, isAimed),
						0,
						getMaxVerticalRecoil(weapon, upgrades)
				));

				//Return the casing
				ItemStack cc = getCasingStack(ammo);
				IIUtils.giveOrDropCasingStack(user, cc);
			}

		}
	}

	/**
	 * @param entity entity holding the gun
	 * @param weapon the gun itemstack
	 * @return valid ammo or {@link ItemStack#EMPTY}
	 */
	public ItemStack findAmmo(Entity entity, ItemStack weapon)
	{
		if(!(entity instanceof EntityLivingBase))
			return ItemStack.EMPTY;

		if(entity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
		{
			final IItemHandler capability = entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			if(capability==null)
				return ItemStack.EMPTY;
			AmmoHandler handler = getAmmoHandler(weapon);

			for(int i = 0; i < capability.getSlots(); i++)
			{
				ItemStack ammo = capability.getStackInSlot(i);
				if(ammo.isEmpty())
					continue;

				if(handler.isValidAmmo(weapon, ammo))
					return ammo;
			}
		}
		return ItemStack.EMPTY;
	}

	protected void createProjectile(EntityLivingBase user, World world, Vec3d dir, Vec3d pos, ItemStack weapon, EasyNBT nbt, ItemStack ammo)
	{
		EntityBullet a = AmmoUtils.createBullet(world, ammo, pos, dir, getVelocityModifier(weapon, nbt, ammo));
		a.setShooters(user);
		world.spawnEntity(a);
	}

	//--- Gun Abstracts ---//

	public abstract AmmoHandler getAmmoHandler(ItemStack weapon);

	protected abstract FireModeType getFireMode(ItemStack weapon);

	/**
	 * @param weapon the gun ItemStack
	 * @return the base melee damage dealt by the weapon
	 */
	protected double getAttackDamage(ItemStack weapon)
	{
		return 0;
	}

	/**
	 * @param weapon the gun ItemStack
	 * @param nbt
	 * @return equip speed (slot change delay) multiplier
	 */
	protected abstract double getEquipSpeed(ItemStack weapon, EasyNBT nbt);

	/**
	 * @param weapon the gun ItemStack
	 * @param nbt    upgrade tag
	 * @return delay between shots (in ticks)
	 */
	public abstract int getFireDelay(ItemStack weapon, EasyNBT nbt);

	/**
	 * @param weapon the gun ItemStack
	 * @param nbt    upgrade tag
	 * @return radius in which enemies will be attracted to the gunshot
	 */
	protected abstract int getEnemyAttractRange(ItemStack weapon, EasyNBT nbt);

	/**
	 * @param weapon the gun ItemStack
	 * @param nbt    upgrade tag
	 * @return aiming time of the gun
	 */
	public abstract int getAimingTime(ItemStack weapon, EasyNBT nbt);

	/**
	 * @param weapon the gun ItemStack
	 * @param loaded
	 * @param nbt    upgrade tag
	 * @return reload time of the gun (in ticks)
	 */
	public abstract int getReloadTime(ItemStack weapon, ItemStack loaded, EasyNBT nbt);

	/**
	 * @param weapon the gun ItemStack
	 * @param nbt    upgrade tag
	 * @return amount the gun recoil is decreased every tick
	 */
	public float getRecoilDecay(ItemStack weapon, EasyNBT nbt, boolean isAimed)
	{
		return isAimed?0.2f: 0.125f;
	}

	/**
	 * @param weapon the gun ItemStack
	 * @param nbt    upgrade tag
	 * @return horizontal recoil added after shooting (in degrees)
	 */
	public abstract float getHorizontalRecoil(ItemStack weapon, EasyNBT nbt, boolean isAimed);

	/**
	 * @param weapon the gun ItemStack
	 * @param nbt    upgrade tag
	 * @return vertical recoil added after shooting (in degrees)
	 */
	public abstract float getVerticalRecoil(ItemStack weapon, EasyNBT nbt, boolean isAimed);

	/**
	 * @param weapon the gun ItemStack
	 * @param nbt    upgrade tag
	 * @return maximum horizontal recoil value, positive and negative (in degrees)
	 */
	public abstract float getMaxHorizontalRecoil(ItemStack weapon, EasyNBT nbt);

	/**
	 * @param weapon the gun ItemStack
	 * @param nbt    upgrade tag
	 * @return maximum vertical recoil value, positive and negative (in degrees)
	 */
	public abstract float getMaxVerticalRecoil(ItemStack weapon, EasyNBT nbt);

	/**
	 * @param weapon the gun ItemStack
	 * @param nbt    upgrade tag
	 * @return size of the muzzle flash particle spawned after the shot is fired
	 */
	protected abstract float getGunfireParticleSize(ItemStack weapon, EasyNBT nbt);

	/**
	 * @param ammo the ammo ItemStack
	 * @return casing to be ejected when the gun fires
	 */
	protected ItemStack getCasingStack(ItemStack ammo)
	{
		return ((IAmmo)ammo.getItem()).getCasingStack(1);
	}

	/**
	 * @param weapon the gun ItemStack
	 * @param nbt    upgrade tag
	 * @return size of the muzzle flash particle spawned after the shot is fired
	 */
	protected float getVelocityModifier(ItemStack weapon, EasyNBT nbt, ItemStack ammo)
	{
		return 1f;
	}

	//--- Firing Sounds ---//

	/**
	 * @param weapon the gun ItemStack
	 * @param nbt    upgrade tag
	 * @return sound played when trying to fire with no ammo loaded
	 */
	@Nullable
	protected abstract SoundEvent getDryfireSound(ItemStack weapon, EasyNBT nbt);

	/**
	 * @param weapon the gun ItemStack
	 * @param nbt    upgrade tag
	 * @return sound played when firing the gun
	 */
	@Nullable
	protected abstract RangedSound getFireSound(ItemStack weapon, EasyNBT nbt);

	/**
	 * @param weapon the gun ItemStack
	 * @param nbt    upgrade tag
	 * @return sound played when firing the gun
	 */
	@Nullable
	protected SoundEvent getChargeFireSound(ItemStack weapon, EasyNBT nbt)
	{
		return null;
	}


	//--- Utility Methods ---//

	private EasyNBT getNBT(ItemStack weapon)
	{
		return EasyNBT.wrapNBT(ItemNBTHelper.getTag(weapon));
	}

	private EasyNBT getUpgradesNBT(ItemStack weapon)
	{
		return EasyNBT.wrapNBT(getUpgrades(weapon));
	}

	public final boolean isAmmo(ItemStack ammo, ItemStack weapon)
	{
		return getAmmoHandler(weapon).isValidAmmo(weapon, ammo);
	}

	public boolean isAimed(ItemStack weapon)
	{
		return ItemNBTHelper.getInt(weapon, AIMING)==getAimingTime(weapon, EasyNBT.wrapNBT(getUpgrades(weapon)));
	}

	/**
	 * @param current current recoil value
	 * @param added   recoil added per step
	 * @return the actual positive-negative recoil value
	 */
	public float getActualRecoil(float current, float added)
	{
		return current*MathHelper.sin(current/added);
	}

	public NBTTagList getAmmoList(ItemStack stack)
	{
		return getAmmoHandler(stack).getAmmoList(stack);
	}

	public enum FireModeType
	{
		AUTOMATIC,
		SINGULAR,
		SINGULAR_CHARGED
	}

	@SideOnly(Side.CLIENT)
	public IIUpgradableItemRendererAMT<?> getItemRenderer()
	{
		return ((IIUpgradableItemRendererAMT<?>)getTileEntityItemStackRenderer());
	}
}
