package pl.pabilo8.immersiveintelligence.common.items.weapons;

import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.gui.IESlot;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces.IAdvancedFluidItem;
import blusunrize.immersiveengineering.common.items.ItemUpgradeableTool;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import com.google.common.collect.Multimap;
import net.minecraft.client.resources.I18n;
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
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Weapons.Submachinegun;
import pl.pabilo8.immersiveintelligence.CustomSkinHandler;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.ISkinnable;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletHelper;
import pl.pabilo8.immersiveintelligence.api.bullets.IBullet;
import pl.pabilo8.immersiveintelligence.client.ClientProxy;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityBullet;
import pl.pabilo8.immersiveintelligence.common.items.ammunition.ItemIIBulletMagazine;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.MessageItemKeybind;
import pl.pabilo8.immersiveintelligence.common.network.MessageParticleGunfire;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Pabilo8
 * @since 01-11-2019
 */
public class ItemIISubmachinegun extends ItemUpgradeableTool implements IAdvancedFluidItem, ISkinnable
{
	public ItemIISubmachinegun()
	{
		super("submachinegun", 1, "SUBMACHINEGUN");
		//Use interfaces pls Blu
		fixupItem();
	}

	@Override
	public int getSlotCount(ItemStack stack)
	{
		return 3;
	}

	@Override
	public boolean canModify(ItemStack stack)
	{
		return true;
	}

	@Override
	public Slot[] getWorkbenchSlots(Container container, ItemStack stack)
	{
		IItemHandler inv = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		return new Slot[]
				{
						new IESlot.Upgrades(container, inv, 0, 80, 32, "SUBMACHINEGUN", stack, true),
						new IESlot.Upgrades(container, inv, 1, 100, 32, "SUBMACHINEGUN", stack, true),
						new IESlot.Upgrades(container, inv, 2, 120, 32, "SUBMACHINEGUN", stack, true)
				};
	}

	@Override
	public void addInformation(@Nonnull ItemStack stack, @Nullable World worldIn, @Nonnull List<String> tooltip, @Nonnull ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		String skin = getSkinnableCurrentSkin(stack);
		if(!skin.isEmpty()&&CustomSkinHandler.specialSkins.containsKey(skin))
		{
			tooltip.add(TextFormatting.WHITE+I18n.format(String.format("skin.%1$s.%2$s.name", ImmersiveIntelligence.MODID, skin)));
			tooltip.add(TextFormatting.GRAY.toString()+TextFormatting.ITALIC+I18n.format(String.format("skin.%1$s.%2$s.desc", ImmersiveIntelligence.MODID, skin)));
		}
	}

	@Nonnull
	@Override
	public IRarity getForgeRarity(@Nonnull ItemStack stack)
	{
		String skin = getSkinnableCurrentSkin(stack);
		if(!skin.isEmpty()&&CustomSkinHandler.specialSkins.containsKey(skin))
			return CustomSkinHandler.specialSkins.get(skin).rarity;
		return super.getForgeRarity(stack);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		boolean b = !(entityIn instanceof EntityLivingBase)||!((EntityLivingBase)entityIn).getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).equals(stack);
		if(b)
			return;

		if(isSelected)
		{
			float recoilH = ItemNBTHelper.getFloat(stack, "recoilH");
			float recoilV = ItemNBTHelper.getFloat(stack, "recoilV");

			ItemNBTHelper.setFloat(stack, "recoilH", MathHelper.clamp(recoilH-(Math.signum(recoilH)*(Math.min(Math.abs(recoilH), 1f))), -Submachinegun.recoilHorizontal, Submachinegun.recoilHorizontal));
			ItemNBTHelper.setFloat(stack, "recoilV", MathHelper.clamp(recoilV-(Math.signum(recoilV)*(Math.min(Math.abs(recoilV), 1f))), -Submachinegun.recoilVertical, Submachinegun.recoilVertical));

			if(worldIn.isRemote)
				if(!ItemNBTHelper.getBoolean(stack, "shouldReload")&&ClientProxy.keybind_manualReload.isKeyDown())
					IIPacketHandler.INSTANCE.sendToServer(new MessageItemKeybind(0));

			int currentAim = ItemNBTHelper.getInt(stack, "aiming");
			int fireDelay = ItemNBTHelper.getInt(stack, "fireDelay");
			int reloading = ItemNBTHelper.getInt(stack, "reloading");
			boolean shouldReload = ItemNBTHelper.getBoolean(stack, "shouldReload");

			if(shouldReload)
			{
				ItemStack magazine = ItemNBTHelper.getItemStack(stack, "magazine");
				if(reloading==1)
					worldIn.playSound(null, entityIn.posX, entityIn.posY, entityIn.posZ, magazine.isEmpty()?IISounds.submachinegun_reload: IISounds.submachinegun_unload, SoundCategory.PLAYERS, 0.35f, 1f);

				if(!magazine.isEmpty())
				{
					reloading += 1;
					if(reloading >= (ItemNBTHelper.getBoolean(stack, "isDrum")?Submachinegun.drumReloadTime: Submachinegun.clipReloadTime))
					{
						shouldReload = false;
						reloading = 0;
						if(!worldIn.isRemote)
						{
							ItemIIBulletMagazine.makeDefault(magazine);
							if(entityIn.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
							{
								IItemHandler capability = entityIn.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
								magazine = ItemHandlerHelper.insertItem(capability, magazine, false);
							}
							if(!magazine.isEmpty())
								blusunrize.immersiveengineering.common.util.Utils.dropStackAtPos(worldIn, entityIn.getPosition(), magazine);
							ItemNBTHelper.remove(stack, "magazine");
						}
					}
				}
				else if(!findMagazine(entityIn, stack).isEmpty())
				{
					reloading += 1;
					ItemNBTHelper.setBoolean(stack, "isDrum", findMagazine(entityIn, stack).getMetadata()==3);
					if(reloading >= (ItemNBTHelper.getBoolean(stack, "isDrum")?Submachinegun.drumReloadTime: Submachinegun.clipReloadTime))
					{
						shouldReload = false;
						reloading = 0;
						if(!worldIn.isRemote)
						{
							magazine = findMagazine(entityIn, stack);
							if(!magazine.isEmpty())
							{
								ItemNBTHelper.setItemStack(stack, "magazine", magazine);
								magazine.shrink(1);
							}
						}
					}
				}
				else
				{
					shouldReload = false;
					reloading = 0;
				}
			}
			else
				reloading = 0;

			ItemNBTHelper.setInt(stack, "reloading", reloading);
			ItemNBTHelper.setBoolean(stack, "shouldReload", shouldReload);

			boolean foldingStock = getUpgrades(stack).hasKey("folding_stock");
			if(reloading==0&&entityIn.isSneaking())
				ItemNBTHelper.setInt(stack, "aiming", MathHelper.clamp(currentAim+1, 0, foldingStock?Submachinegun.aimTimeFoldedStock: Submachinegun.aimTime));
			else if(currentAim > 0)
				ItemNBTHelper.setInt(stack, "aiming", MathHelper.clamp(currentAim-3, 0, foldingStock?Submachinegun.aimTimeFoldedStock: Submachinegun.aimTime));

			if(fireDelay > 0)
				ItemNBTHelper.setInt(stack, "fireDelay", fireDelay-1);
		}
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}

	@Override
	public void onUsingTick(@Nonnull ItemStack stack, EntityLivingBase player, int count)
	{
		World worldIn = player.getEntityWorld();

		ItemStack magazine = ItemNBTHelper.getItemStack(stack, "magazine");
		if(!isAmmo(magazine, stack))
			return;

		if(ItemNBTHelper.getInt(stack, "fireDelay")==0)
		{
			ItemNBTHelper.setInt(stack, "fireDelay", Submachinegun.bulletFireTime);

			if(!worldIn.isRemote)
			{
				boolean foldingStock = getUpgrades(stack).hasKey("folding_stock");
				boolean isAimed = ItemNBTHelper.getInt(stack, "aiming") > (foldingStock?Submachinegun.aimTimeFoldedStock: Submachinegun.aimTime*0.9);
				float recoilH = ItemNBTHelper.getFloat(stack, "recoilH");
				float recoilV = ItemNBTHelper.getFloat(stack, "recoilV");

				Vec3d vec = Utils.getVectorForRotation(player.rotationPitch-recoilH, player.getRotationYawHead()+recoilV);
				Vec3d vv = player.getPositionVector().addVector(0, (double)player.getEyeHeight()-0.10000000149011612D, 0);


				ItemStack s2 = ItemIIBulletMagazine.takeBullet(magazine, true);
				boolean sturdyBarrel = getUpgrades(stack).hasKey("sturdy_barrel");

				EntityBullet a = BulletHelper.createBullet(worldIn, s2, vv, vec, sturdyBarrel?Submachinegun.sturdyBarrelVelocityMod: 1f);
				a.setShooters(player);
				worldIn.spawnEntity(a);

				boolean supressor = getUpgrades(stack).hasKey("suppressor");
				float noise = supressor?0.25f: 1f;
				blusunrize.immersiveengineering.common.util.Utils.attractEnemies(player, 36*noise);

				worldIn.playSound(null, player.posX+vec.x, player.posY+vec.y, player.posZ+vec.z, IISounds.submachinegun_shot, SoundCategory.PLAYERS, 1.5f*noise, supressor?0.75f: 0f);

				IIPacketHandler.INSTANCE.sendToAllAround(
						new MessageParticleGunfire(player, supressor?0.5f: 1.5f),
						Utils.targetPointFromEntity(player, 32));


				if(count%3==0)
				{
					float signum = Math.signum(recoilV);
					recoilV = MathHelper.clamp(Math.abs(recoilV)+(isAimed?0.65f*Submachinegun.recoilVertical: Submachinegun.recoilVertical), 0, Submachinegun.maxRecoilVertical);
					recoilV *= signum > 0?-1: 1;
					ItemNBTHelper.setFloat(stack, "recoilV", recoilV);
				}
				else
				{
					float signum = Math.signum(recoilV);
					recoilH = MathHelper.clamp(Math.abs(recoilH)+(isAimed?0.65f*Submachinegun.recoilHorizontal: Submachinegun.recoilHorizontal), 0, Submachinegun.maxRecoilHorizontal*-Math.signum(recoilH));
					recoilH *= signum > 0?-1: 1;
					ItemNBTHelper.setFloat(stack, "recoilH", recoilH);
				}

				ItemStack cc = ((IBullet)s2.getItem()).getCasingStack(1);
				if(player.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
				{
					IItemHandler capability = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
					cc = ItemHandlerHelper.insertItem(capability, cc, false);
				}
				if(!cc.isEmpty())
					blusunrize.immersiveengineering.common.util.Utils.dropStackAtPos(worldIn, player.getPosition(), cc);

			}

		}
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
		return Submachinegun.bulletFireTime+1;
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, EntityPlayer player, @Nonnull EnumHand hand)
	{
		ItemStack itemstack = player.getHeldItem(hand);
		if(hand==EnumHand.MAIN_HAND)
		{
			player.setActiveHand(hand);
			if(isAmmo(ItemNBTHelper.getItemStack(itemstack, "magazine"), itemstack))
				return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);

		}
		return new ActionResult<>(EnumActionResult.FAIL, itemstack);
	}

	@Override
	public void onPlayerStoppedUsing(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull EntityLivingBase entityLiving, int timeLeft)
	{

	}

	public void fixupItem()
	{
		//First, get the item out of IE's registries.
		Item rItem = IEContent.registeredIEItems.remove(IEContent.registeredIEItems.size()-1);
		if(rItem!=this) throw new IllegalStateException("fixupItem was not called at the appropriate time");

		//Now, reconfigure the block to match our mod.
		this.setUnlocalizedName(ImmersiveIntelligence.MODID+"."+this.itemName);
		this.setCreativeTab(ImmersiveIntelligence.creativeTab);

		//And add it to our registries.
		IIContent.ITEMS.add(this);
	}

	@Override
	public void removeFromWorkbench(EntityPlayer player, ItemStack stack)
	{
		//NBTTagCompound upgrades = getUpgrades(stack);
		// TODO: 09.08.2020 advancements
	}

	@Nonnull
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(@Nonnull EntityEquipmentSlot slot, @Nonnull ItemStack stack)
	{
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
		if(slot==EntityEquipmentSlot.MAINHAND)
		{
			double melee = getUpgrades(stack).getFloat("melee");
			if(melee!=0)
			{
				multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", melee, 0));
				multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4000000953674316D, 0));
			}
		}
		return multimap;
	}

	@Override
	public int getCapacity(ItemStack stack, int baseCapacity)
	{
		return 0;
	}

	@Override
	public boolean allowFluid(ItemStack container, FluidStack fluid)
	{
		return false;
	}

	@Override
	public String getSkinnableName()
	{
		return "submachinegun";
	}

	@Override
	public String getSkinnableDefaultTextureLocation()
	{
		return ImmersiveIntelligence.MODID+":textures/items/weapons/";
	}

	public ItemStack findMagazine(Entity entity, ItemStack weapon)
	{
		if(!(entity instanceof EntityLivingBase))
			return ItemStack.EMPTY;
		if(entity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
		{
			final IItemHandler capability = entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			if(capability==null)
				return ItemStack.EMPTY;

			for(int i = 0; i < capability.getSlots(); i++)
			{
				ItemStack itemstack = capability.getStackInSlot(i);
				if(isAmmo(itemstack, weapon))
					return itemstack;
			}
		}
		return ItemStack.EMPTY;
	}

	public boolean isAmmo(ItemStack stack, ItemStack weapon)
	{
		if(stack.isEmpty())
			return false;
		if(stack.getItem() instanceof ItemIIBulletMagazine)
			return ItemIIBulletMagazine.getMatchingType(stack)==IIContent.itemAmmoSubmachinegun&&!ItemIIBulletMagazine.hasNoBullets(stack)&&
					(stack.getMetadata()!=3||getUpgrades(weapon).hasKey("bottom_loading"));
		return false;
	}
}
