package pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.effect_crate;

import blusunrize.immersiveengineering.api.tool.BulletHandler;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces.IBulletContainer;
import blusunrize.immersiveengineering.common.items.ItemBullet;
import blusunrize.immersiveengineering.common.items.ItemRevolver;
import blusunrize.immersiveengineering.common.items.ItemSpeedloader;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.UpgradeTier;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IIPotions;
import pl.pabilo8.immersiveintelligence.common.item.ammo.gun.ItemIIAmmoMachinegun;

import static pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.EffectCrates.ammoCrateEnergyPerAction;

/**
 * Stores ammunition and resupplies living entities or compatible bullet containers.
 *
 * @author Pabilo8(pabilo@iiteam.net)
 * @updated 10.08.2026
 * @since 17.05.2019
 */
public class TileEntityAmmunitionCrate extends TileEntityEffectCrate
{
	static
	{
		UpgradeTechTree.getTreeFor(TileEntityAmmunitionCrate.class)
				.withUpgrade(IIContent.UPGRADE_MG_LOADER, UpgradeTier.TIER_1);
	}

	public TileEntityAmmunitionCrate()
	{
		super(NonNullList.withSize(50, ItemStack.EMPTY), IEInventoryHandler::new);
	}

	@Override
	public IIGUI getGUI()
	{
		return IIGUI.AMMUNITION_CRATE;
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		if(slot < 20)
			return stack.getItem() instanceof ItemBullet&&!(stack.isItemEqual(BulletHandler.emptyCasing)||stack.isItemEqual(BulletHandler.emptyShell));
		if(slot < 29)
			return stack.getItem() instanceof ItemBullet&&stack.isItemEqual(BulletHandler.emptyCasing)||stack.isItemEqual(BulletHandler.emptyShell)&&!stack.hasTagCompound();
		if(slot < 37)
			return stack.getItem() instanceof ItemBullet&&!(stack.equals(BulletHandler.emptyCasing)||stack.equals(BulletHandler.emptyShell));
		if(slot==37)
			return stack.getItem() instanceof ItemRevolver||stack.getItem() instanceof ItemSpeedloader;
		return isUpgradeInstalled(IIContent.UPGRADE_MG_LOADER)&&stack.getItem() instanceof ItemIIAmmoMachinegun;
	}

	@Override
	public boolean interact(EnumFacing side, EntityPlayer player, EnumHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		if(player.isSneaking())
		{
			setLidState(!lid.getState());
			return true;
		}
		if(lid.getState())
		{
			if(!world.isRemote)
				reloadRevolver(player, hand);
			return true;
		}
		return false;
	}

	@Override
	boolean isSupplied()
	{
		return true;
	}

	@Override
	void useSupplies()
	{
	}

	@Override
	boolean affectEntity(Entity entity, boolean upgraded)
	{
		if(upgraded&&energyStorage < ammoCrateEnergyPerAction)
			return false;

		boolean supplied = false;
		if(entity instanceof EntityLivingBase)
		{
			EntityLivingBase living = (EntityLivingBase)entity;
			if(!living.isPotionActive(IIPotions.wellSupplied))
			{
				living.addPotionEffect(new PotionEffect(IIPotions.wellSupplied, 80, 0, true, true));
				supplied = true;
			}
		}

		if(upgraded&&supplied)
			consumeEnergy(ammoCrateEnergyPerAction);
		return supplied;
	}

	@Override
	boolean checkEntity(Entity entity)
	{
		return entity instanceof EntityLivingBase;
	}

	private void reloadRevolver(EntityPlayer player, EnumHand hand)
	{
		ItemStack heldItem = player.getHeldItem(hand);
		if(heldItem.isEmpty()||!(heldItem.getItem() instanceof IBulletContainer))
			return;

		if(heldItem.getItem() instanceof ItemRevolver)
			ItemNBTHelper.setInt(heldItem, "reload", Math.round(30*Tools.ammunitionCrateResupplyTime));

		IItemHandler bulletHandler = heldItem.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		if(bulletHandler==null)
			return;

		for(int i = 0; i < 8; i++)
		{
			ItemStack stack = bulletHandler.extractItem(i, 1, false);
			if(stack.isItemEqual(BulletHandler.emptyCasing)||stack.isItemEqual(BulletHandler.emptyShell))
				for(int j = 20; j < 29&&!stack.isEmpty(); j++)
					stack = insertionHandler.insertItem(j, stack, false);
			else
				for(int j = 0; j < 20&&!stack.isEmpty(); j++)
					stack = insertionHandler.insertItem(j, stack, false);
		}

		for(int slot = 0; slot < 8; slot++)
		{
			ItemStack required = inventory.get(29+slot);
			if(required.isEmpty())
				continue;

			ItemStack round = ItemStack.EMPTY;
			for(int source = 0; source < 20; source++)
			{
				ItemStack available = insertionHandler.getStackInSlot(source);
				if(!available.isEmpty()&&ItemStack.areItemStackTagsEqual(available, required))
				{
					round = insertionHandler.extractItem(source, 1, false);
					break;
				}
			}
			bulletHandler.insertItem(slot, round, false);
		}
		markDirty();
	}
}
