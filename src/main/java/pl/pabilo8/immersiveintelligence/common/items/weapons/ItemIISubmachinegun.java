package pl.pabilo8.immersiveintelligence.common.items.weapons;

import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.gui.IESlot;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces.IAdvancedFluidItem;
import blusunrize.immersiveengineering.common.items.ItemUpgradeableTool;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
import pl.pabilo8.immersiveintelligence.common.network.MessageItemReloadMagazine;

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
		return 2;
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
						new IESlot.Upgrades(container, inv, 1, 100, 32, "SUBMACHINEGUN", stack, true)
				};
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		String skin = getSkinnableCurrentSkin(stack);
		if(!skin.isEmpty()&&CustomSkinHandler.specialSkins.containsKey(skin))
		{
			tooltip.add(TextFormatting.WHITE+I18n.format(String.format("skin.%1$s.%2$s.name", ImmersiveIntelligence.MODID, skin)));
			tooltip.add(TextFormatting.GRAY.toString()+TextFormatting.ITALIC+I18n.format(String.format("skin.%1$s.%2$s.desc", ImmersiveIntelligence.MODID, skin)));
		}
	}

	@Override
	public IRarity getForgeRarity(ItemStack stack)
	{
		String skin = getSkinnableCurrentSkin(stack);
		if(!skin.isEmpty()&&CustomSkinHandler.specialSkins.containsKey(skin))
		{
			return CustomSkinHandler.specialSkins.get(skin).rarity;
		}
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
			{
				if(!ItemNBTHelper.getBoolean(stack, "shouldReload")&&ClientProxy.keybind_motorbikeEngine.isKeyDown())
					IIPacketHandler.INSTANCE.sendToServer(new MessageItemReloadMagazine());
			}

			int currentAim = ItemNBTHelper.getInt(stack, "aiming");
			int fireDelay = ItemNBTHelper.getInt(stack, "fireDelay");
			int reloading = ItemNBTHelper.getInt(stack, "reloading");
			boolean shouldReload = ItemNBTHelper.getBoolean(stack, "shouldReload");

			if(shouldReload)
			{
				ItemStack magazine = ItemNBTHelper.getItemStack(stack, "magazine");
				if(reloading==1)
					worldIn.playSound(null, entityIn.posX, entityIn.posY, entityIn.posZ, magazine.isEmpty()?IISounds.machinegun_reload: IISounds.machinegun_unload, SoundCategory.PLAYERS, 0.35f, 1f);

				if(!magazine.isEmpty())
				{
					reloading += 1;
					if(reloading >= Submachinegun.clipReloadTime)
					{
						shouldReload = false;
						reloading = 0;
						if(!worldIn.isRemote)
						{
							ItemIIBulletMagazine.makeDefault(magazine);
							boolean c = false;
							if(entityIn instanceof EntityPlayer)
								c = ((EntityPlayer)entityIn).addItemStackToInventory(magazine);
							if(!c)
								blusunrize.immersiveengineering.common.util.Utils.dropStackAtPos(worldIn, entityIn.getPosition(), magazine);
							ItemNBTHelper.remove(stack, "magazine");
						}
					}
				}
				else if(!findMagazine(entityIn, stack).isEmpty())
				{
					reloading += 1;
					ItemNBTHelper.setBoolean(stack, "isDrum", findMagazine(entityIn, stack).getMetadata()==3);
					if(reloading >= Submachinegun.clipReloadTime)
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
			{
				shouldReload = false;
				reloading = 0;
			}

			ItemNBTHelper.setInt(stack, "reloading", reloading);
			ItemNBTHelper.setBoolean(stack, "shouldReload", shouldReload);

			if(reloading==0&&entityIn.isSneaking())
			{
				// TODO: 29.01.2021 reevaluate
				/*
				if(!worldIn.isRemote&&currentAim%10==0)
					IIPacketHandler.INSTANCE.sendToDimension(new MessagePlayerAimAnimationSync(entityIn, true), worldIn.provider.getDimension());
				 */
				ItemNBTHelper.setInt(stack, "aiming", MathHelper.clamp(currentAim+1, 0, Submachinegun.aimTime));
			}
			else if(currentAim > 0)
			{
				/*
				if(!worldIn.isRemote&&currentAim%5==0)
					IIPacketHandler.INSTANCE.sendToDimension(new MessagePlayerAimAnimationSync(entityIn, false), worldIn.provider.getDimension());
				 */
				ItemNBTHelper.setInt(stack, "aiming", MathHelper.clamp(currentAim-3, 0, Submachinegun.aimTime));
			}

			if(fireDelay > 0)
				ItemNBTHelper.setInt(stack, "fireDelay", fireDelay-1);
		}
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count)
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
				boolean isAimed = ItemNBTHelper.getInt(stack, "aiming") > Submachinegun.aimTime*0.9;
				float recoilH = ItemNBTHelper.getFloat(stack, "recoilH");
				float recoilV = ItemNBTHelper.getFloat(stack, "recoilV");

				Vec3d vec = Utils.getVectorForRotation(player.rotationPitch-recoilH, player.rotationYaw+recoilV);
				Vec3d vv = player.getPositionVector().addVector(0, (double)player.getEyeHeight()-0.10000000149011612D, 0);
				ItemStack s2 = ItemIIBulletMagazine.takeBullet(magazine);

				float noise = getUpgrades(stack).hasKey("suppressor")?0.25f: 1f;
				blusunrize.immersiveengineering.common.util.Utils.attractEnemies(player, 36*noise);

				worldIn.playSound(null, player.posX+vec.x, player.posY+vec.y, player.posZ+vec.z, IISounds.submachinegun_shot, SoundCategory.PLAYERS, 1.5f*noise, 0.125f);
				EntityBullet a = BulletHelper.createBullet(worldIn, s2, vv, vec, 6.5f);
				a.setShooters(player);
				worldIn.spawnEntity(a);

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

				boolean c = false;
				ItemStack cc = ((IBullet)s2.getItem()).getCasingStack(1);
				if(player instanceof EntityPlayer)
					c = ((EntityPlayer)player).addItemStackToInventory(cc);
				if(!c)
					blusunrize.immersiveengineering.common.util.Utils.dropStackAtPos(worldIn, player.getPosition(), cc);
			}

		}
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.NONE;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return Submachinegun.bulletFireTime+1;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
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
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entityLiving, int timeLeft)
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
		NBTTagCompound upgrades = getUpgrades(stack);
		// TODO: 09.08.2020 advancements
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
	public String getSkinnableDefaultTextureLocation()
	{
		return ImmersiveIntelligence.MODID+":textures/items/weapons/";
	}

	public ItemStack findMagazine(Entity entity, ItemStack weapon)
	{
		if(!(entity instanceof EntityPlayer))
			return ItemStack.EMPTY;
		EntityPlayer player = (EntityPlayer)entity;
		if(isAmmo(player.getHeldItem(EnumHand.OFF_HAND), weapon))
			return player.getHeldItem(EnumHand.OFF_HAND);
		else if(isAmmo(player.getHeldItem(EnumHand.MAIN_HAND), weapon))
			return player.getHeldItem(EnumHand.MAIN_HAND);
		else
			for(int i = 0; i < player.inventory.getSizeInventory(); i++)
			{
				ItemStack itemstack = player.inventory.getStackInSlot(i);
				if(isAmmo(itemstack, weapon))
					return itemstack;
			}
		return ItemStack.EMPTY;
	}

	public boolean isAmmo(ItemStack stack, ItemStack weapon)
	{
		if(stack.isEmpty())
			return false;
		if(stack.getItem() instanceof ItemIIBulletMagazine)
		{
			return ItemIIBulletMagazine.getMatchingType(stack)==IIContent.itemAmmoSubmachinegun&&!ItemIIBulletMagazine.hasNoBullets(stack)&&
					(stack.getMetadata()!=3||getUpgrades(weapon).hasKey("bottom_loading"));
		}
		return false;
	}
}
