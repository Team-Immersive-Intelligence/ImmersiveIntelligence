package pl.pabilo8.immersiveintelligence.common;

import blusunrize.immersiveengineering.api.MultiblockHandler.MultiblockFormEvent;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import pl.pabilo8.immersiveintelligence.api.bullets.DamageBlockPos;
import pl.pabilo8.immersiveintelligence.api.bullets.PenetrationRegistry;
import pl.pabilo8.immersiveintelligence.api.utils.IAdvancedMultiblock;
import pl.pabilo8.immersiveintelligence.common.compat.BaublesHelper;
import pl.pabilo8.immersiveintelligence.common.compat.IICompatModule;
import pl.pabilo8.immersiveintelligence.common.crafting.IIRecipes;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;
import pl.pabilo8.immersiveintelligence.common.entity.EntityMachinegun;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIBulletMagazine;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBlockDamageSync;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIIUpgradeableArmor;

/**
 * Handles events for server side.
 *
 * @author Pabilo8
 * @since 23.09.2023
 */
public class EventHandler
{
	@SubscribeEvent
	public void onMultiblockForm(MultiblockFormEvent.Post event)
	{
		if(event.isCancelable()&&!event.isCanceled()&&event.getMultiblock().getClass().isAnnotationPresent(IAdvancedMultiblock.class))
		{
			//Required by Advanced Structures!
			if(!IIUtils.isAdvancedHammer(event.getHammer()))
			{
				if(!event.getEntityPlayer().getEntityWorld().isRemote)
					IIPacketHandler.sendChatTranslation(event.getEntityPlayer(), "info.immersiveintelligence.requires_advanced_hammer");
				event.setCanceled(true);
			}
		}
	}


	//Cancel when using a machinegun
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onItemUse(PlayerInteractEvent.RightClickBlock event)
	{
		if(event.getEntity().isRiding()&&event.getEntity().getRidingEntity() instanceof EntityMachinegun)
		{
			event.setResult(Result.DENY);
			event.setCanceled(true);
		}
	}

	//Cancel when using a machinegun
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onBlockUse(PlayerInteractEvent.RightClickItem event)
	{
		if(event.getEntity().isRiding()&&event.getEntity().getRidingEntity() instanceof EntityMachinegun)
		{
			event.setResult(Result.DENY);
			event.setCanceled(true);
		}
	}

	//Shooting
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onEmptyRightclick(PlayerInteractEvent.RightClickEmpty event)
	{
		if(event.getEntity().isRiding()&&event.getEntity().getRidingEntity() instanceof EntityMachinegun)
		{
			event.setResult(Result.DENY);
		}
	}

	@SubscribeEvent
	public void onBreakBlock(BreakEvent event)
	{
		DamageBlockPos dpos = null;
		for(DamageBlockPos g : PenetrationRegistry.blockDamage)
		{
			if(g.dimension==event.getWorld().provider.getDimension()&&event.getPos().equals(g)) ;
			{
				dpos = g;
				break;
			}
		}
		if(dpos!=null)
		{
			PenetrationRegistry.blockDamage.remove(dpos);
			dpos.damage = 0;
			IIPacketHandler.INSTANCE.sendToAllAround(new MessageBlockDamageSync(dpos), IIPacketHandler.targetPointFromPos(dpos, event.getWorld(), 32));
		}
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onLivingUpdate(LivingUpdateEvent event)
	{
		if(!(event.getEntityLiving() instanceof EntityPlayer&&((EntityPlayer)event.getEntityLiving()).isCreative())&&event.getEntityLiving().world.getTotalWorldTime()%20==0&&event.getEntityLiving().world.getBiome(event.getEntityLiving().getPosition())==IIContent.biomeWasteland)
			event.getEntityLiving().addPotionEffect(new PotionEffect(IIPotions.radiation, 2000, 0, false, false));
		if(event.getEntityLiving() instanceof EntityPlayer&&!event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty()&&ItemNBTHelper.hasKey(event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.CHEST), IIContent.NBT_AdvancedPowerpack))
		{
			ItemStack powerpack = ItemNBTHelper.getItemStack(event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.CHEST), IIContent.NBT_AdvancedPowerpack);
			if(!powerpack.isEmpty())
				powerpack.getItem().onArmorTick(event.getEntityLiving().getEntityWorld(), (EntityPlayer)event.getEntityLiving(), powerpack);
		}
	}

	@SubscribeEvent
	public void spawnEvent(EntityJoinWorldEvent event)
	{
		if(event.getEntity() instanceof EntityMob)
		{
			EntityMob e = (EntityMob)event.getEntity();
			e.targetTasks.addTask(4, new EntityAINearestAttackableTarget<>(e, EntityHans.class, true));
		}
	}

	@SubscribeEvent
	public void onLivingAttack(LivingAttackEvent event)
	{
		EntityLivingBase entity = event.getEntityLiving();
		ItemStack head, chest, legs, boots;
		head = entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
		chest = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		legs = entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
		boots = entity.getItemStackFromSlot(EntityEquipmentSlot.FEET);

		//plates
		if(event.getSource()==DamageSource.CACTUS||(event.getSource() instanceof EntityDamageSourceIndirect&&event.getSource().getImmediateSource() instanceof EntityArrow))
		{
			if(ItemIIUpgradeableArmor.isArmorWithUpgrade(boots, "toughness_increase"))
				event.setCanceled(true);
		}
		//heat resist
		else if(event.getSource()==DamageSource.IN_FIRE||event.getSource()==DamageSource.HOT_FLOOR)
		{
			if(ItemIIUpgradeableArmor.isArmorWithUpgrade(chest, "heat_coating")&&ItemIIUpgradeableArmor.isArmorWithUpgrade(boots, "reinforced"))
				event.setCanceled(true);
		}
		//springs
		else if(event.getSource()==DamageSource.FALL)
		{
			if(ItemIIUpgradeableArmor.isArmorWithUpgrade(boots, "springs"))
				event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void hurtEvent(LivingHurtEvent event)
	{
		EntityLivingBase entity = event.getEntityLiving();
		ItemStack head, chest, legs, boots;
		head = entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
		chest = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		legs = entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
		boots = entity.getItemStackFromSlot(EntityEquipmentSlot.FEET);

		//plates
		if(event.getSource()==DamageSource.CACTUS||(event.getSource() instanceof EntityDamageSourceIndirect&&event.getSource().getImmediateSource() instanceof EntityArrow))
		{
			if(ItemIIUpgradeableArmor.isArmorWithUpgrade(boots, "toughness_increase"))
				event.setCanceled(true);
		}
		//heat resist
		else if(event.getSource()==DamageSource.IN_FIRE||event.getSource()==DamageSource.HOT_FLOOR)
		{
			if(ItemIIUpgradeableArmor.isArmorWithUpgrade(chest, "heat_coating")&&ItemIIUpgradeableArmor.isArmorWithUpgrade(boots, "reinforced"))
				event.setCanceled(true);
		}
		//springs
		else if(event.getSource()==DamageSource.FALL)
		{
			if(ItemIIUpgradeableArmor.isArmorWithUpgrade(boots, "springs"))
				event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onItemPickup(EntityItemPickupEvent event)
	{
		ItemStack stack = event.getItem().getItem();
		EntityPlayer player = event.getEntityPlayer();

		if(!IIRecipes.AMMO_CASINGS.matchesItemStackIgnoringSize(stack)&&!(stack.getItem() instanceof ItemIIBulletMagazine))
			return;

		for(int i = 0; i < 10; i++)
		{
			ItemStack pouchStack;
			if(i==0)
				pouchStack = IICompatModule.baubles?BaublesHelper.getWornPouch(player): ItemStack.EMPTY;
			else
				pouchStack = player.inventory.getStackInSlot(i-1);

			//Attempt storing in pouch
			ItemStack output = storeInPouch(pouchStack, stack);
			if(!output.equals(stack))
			{
				//Full or partial success
				event.getItem().setItem(output);
				player.world.playSound(null, player.getPosition(), IISounds.casingPickup, SoundCategory.PLAYERS, 1f, 0.88f);
				event.setCanceled(true);
				stack = output;
			}

			if(stack.isEmpty())
				return;
		}

	}

	public ItemStack storeInPouch(ItemStack pouchStack, ItemStack stack)
	{
		if(!pouchStack.getItem().equals(IIContent.itemCasingPouch))
			return stack;

		IItemHandler pouchCap = pouchStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		if(pouchCap==null)
			return stack;

		return ItemHandlerHelper.insertItem(pouchCap, stack, false);
	}
}
