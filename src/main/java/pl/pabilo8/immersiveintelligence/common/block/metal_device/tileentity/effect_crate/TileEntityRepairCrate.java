package pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.effect_crate;

import blusunrize.immersiveengineering.common.items.IEItemInterfaces.IItemDamageableIE;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.UpgradeTier;
import pl.pabilo8.immersiveintelligence.api.utils.IEntitySpecialRepairable;
import pl.pabilo8.immersiveintelligence.client.util.carversound.ConditionCompoundSound;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IIPotions;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;

import static pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.EffectCrates.repairCrateEnergyPerAction;

/**
 * Stores repair materials and repairs supported entities or worn equipment.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 10.08.2026
 * @since 06.07.2020
 */
public class TileEntityRepairCrate extends TileEntityEffectCrate
{
	static
	{
		UpgradeTechTree.getTreeFor(TileEntityRepairCrate.class)
				.withUpgrade(IIContent.UPGRADE_INSERTER, UpgradeTier.TIER_1);
	}

	@SyncNBT(events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_CLIENT_MESSAGE})
	public boolean shouldRepairArmor = true;
	@SyncNBT(events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_CLIENT_MESSAGE})
	public boolean shouldRepairVehicles = true;
	public boolean repaired = false;

	@SideOnly(Side.CLIENT)
	private ConditionCompoundSound<TileEntityRepairCrate> weldingSound;

	public TileEntityRepairCrate()
	{
		super(NonNullList.withSize(16, ItemStack.EMPTY), IEInventoryHandler::new);
	}

	@Override
	public IIGUI getGUI()
	{
		return IIGUI.REPAIR_CRATE;
	}

	@Override
	boolean isSupplied()
	{
		return (shouldRepairArmor||shouldRepairVehicles)&&inventory.stream().anyMatch(stack -> !stack.isEmpty());
	}

	@Override
	void useSupplies()
	{
		for(ItemStack stack : inventory)
			if(!stack.isEmpty())
			{
				stack.shrink(1);
				markDirty();
				break;
			}
	}

	@Override
	public void update()
	{
		super.update();
		if(world.isRemote)
			updateSound();
	}

	@SideOnly(Side.CLIENT)
	private void updateSound()
	{
		if(!isWelding())
			return;
		if(weldingSound==null||weldingSound.isDonePlaying())
		{
			weldingSound = new ConditionCompoundSound<>(IISounds.weldingLoop,
					new Vec3d(pos).addVector(0.5, 0.5, 0.5), this, TileEntityRepairCrate::isWelding);
			weldingSound.setVolume(0.5f);
		}
	}

	private boolean isWelding()
	{
		return !tileEntityInvalid&&isUpgradeInstalled(IIContent.UPGRADE_INSERTER)&&focusedEntity.get()!=null;
	}

	@Override
	boolean affectEntity(Entity entity, boolean upgraded)
	{
		if(upgraded&&energyStorage.getEnergyStored() < repairCrateEnergyPerAction)
			return false;

		repaired = false;
		if(entity instanceof IEntitySpecialRepairable)
		{
			IEntitySpecialRepairable repairable = (IEntitySpecialRepairable)entity;
			if(repairable.canRepair())
				repaired = repairable.repair(2);
		}
		else if(entity instanceof EntityLivingBase)
		{
			EntityLivingBase living = (EntityLivingBase)entity;
			if(!living.isPotionActive(IIPotions.undergoingRepairs))
			{
				living.addPotionEffect(new PotionEffect(IIPotions.undergoingRepairs, upgraded?200: 400, upgraded?1: 0, true, true));
				repaired = true;
			}
		}

		if(upgraded&&repaired)
			consumeEnergy(repairCrateEnergyPerAction);
		return repaired;
	}

	@Override
	boolean checkEntity(Entity entity)
	{
		if(entity instanceof IEntitySpecialRepairable)
			return true;
		if(!(entity instanceof EntityLivingBase))
			return false;

		EntityLivingBase living = (EntityLivingBase)entity;
		for(ItemStack stack : living.getEquipmentAndArmor())
		{
			if(stack.getItem() instanceof IItemDamageableIE)
			{
				IItemDamageableIE item = (IItemDamageableIE)stack.getItem();
				return item.getItemDamageIE(stack) < item.getMaxDamageIE(stack);
			}
			if(stack.isItemDamaged()&&stack.getItem().isRepairable())
				return true;
		}
		return false;
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return Utils.compareToOreName(stack, "plateSteel");
	}
}
