package pl.pabilo8.immersiveintelligence.common.items.weapons;

import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.gui.IESlot;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces.IAdvancedFluidItem;
import blusunrize.immersiveengineering.common.items.ItemUpgradeableTool;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.CustomSkinHandler;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.ISkinnable;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityBullet;

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
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 2;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack itemstack = playerIn.getHeldItem(handIn);

		if(!playerIn.world.isRemote&&playerIn.world.getTotalWorldTime()%2==0)
		{
			Vec3d vec = playerIn.getLookVec().scale(1f);

			ItemStack stack = CommonProxy.item_bullet.getAmmoStack(1, "submachinegun_1bCal", "CoreSteel", "empty", "empty", 0, 0, 0);

			worldIn.playSound(null, playerIn.posX+vec.x, playerIn.posY+vec.y, playerIn.posZ+vec.z, IISounds.machinegun_shot, SoundCategory.PLAYERS, 0.5f, 0.85f);

			EntityBullet a = new EntityBullet(playerIn.world, playerIn.posX+vec.x, playerIn.posY+1f+vec.y, playerIn.posZ+vec.z, playerIn, stack);
			//blocks per tick
			a.motionX = vec.x*2.5;
			a.motionY = vec.y*2.5;
			a.motionZ = vec.z*2.5;
			a.world.spawnEntity(a);

			return new ActionResult<>(EnumActionResult.FAIL, itemstack);
		}

		return new ActionResult<>(EnumActionResult.FAIL, itemstack);
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
		CommonProxy.items.add(this);
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


}
