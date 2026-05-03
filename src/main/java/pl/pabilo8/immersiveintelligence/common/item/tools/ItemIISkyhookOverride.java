package pl.pabilo8.immersiveintelligence.common.item.tools;

import blusunrize.immersiveengineering.api.tool.IElectricEquipment.ElectricSource;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.entities.EntitySkylineHook;
import blusunrize.immersiveengineering.common.gui.IESlot;
import blusunrize.immersiveengineering.common.items.ItemSkyhook;
import blusunrize.immersiveengineering.common.util.IEDamageSources.ElectricDamageSource;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.api.upgrade.Upgrade;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

import static pl.pabilo8.immersiveintelligence.client.render.entity.weapon.MachinegunRenderer.upgrades;

public class ItemIISkyhookOverride extends ItemSkyhook
{
	public ItemIISkyhookOverride()
	{
		super();
		IEContent.registeredIEItems.removeIf(item -> item instanceof ItemSkyhook);
		IEContent.registeredIEItems.add(this);
	}

	@Override
	public Slot[] getWorkbenchSlots(Container container, ItemStack stack)
	{
		IItemHandler inv = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		return new Slot[]
				{
						new IESlot.Upgrades(container, inv, 0, 102, 42, "SKYHOOK", stack, true),
						new IESlot.Upgrades(container, inv, 1, 102, 22, "SKYHOOK", stack, true),
						//new IESlot.Upgrades(container, inv, 3, 102, 2, "SKYHOOK", stack, true)
				};
	}


	//todo: convert the actual upgrades to a useable state.

	/*@SubscribeEvent
	public static void criticalHit(CriticalHitEvent ev)
	{
		ItemStack heldItem = ev.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND);

		if(heldItem.is(IEContent.Misc.SKYHOOK.asItem())&&getUpgradesStatic(heldItem).has(UpgradeEffect.MACE_ATTACK)&&ev.isVanillaCritical())
		{
			float fallDistance = ev.getEntity().fallDistance;
			if(fallDistance < 1.5)
				return;
			float damageBonus;
			if(fallDistance <= 3)
				damageBonus = 0.66f*fallDistance; // 66% / 4 damage for the first 3 blocks
			else if(fallDistance <= 8)
				damageBonus = 2f+0.33f*(fallDistance-3); // 33% / 2 damage for the next 5 blocks
			else
				damageBonus = 3.65f+0.165f*(fallDistance-8); // 16.5% / 1 damage for the rest of the way
			ev.setDamageMultiplier(ev.getDamageMultiplier()+damageBonus);

			ev.getEntity().fallDistance = 0;
		}
	}

	public float getSlopeModifier(ItemStack stack)
	{
		List SkyhookUpgrades;
		upgrades = getUpgrades();
		return Math.max(upgrades.get(Upgrade.SLOPE_MODIFIER), 0.5f);
	}


	public void onStrike(ItemStack equipped, EntityEquipmentSlot eqSlot, EntityPlayer owner, Map<String, Object> cache, @Nullable DamageSource dmg, ElectricSource desc)
	{
		if(dmg instanceof ElectricDamageSource eds&&dmg.is(DamageTypes.WIRE_SHOCK)&&this.getUpgrades(equipped).has(UpgradeEffect.INSULATED)
				&&(owner.getVehicle() instanceof EntitySkylineHook||owner.isUsingItem())) // either on a wire or trying to attach
		{
			eds.dmg = 0;
		}
	} */
}
