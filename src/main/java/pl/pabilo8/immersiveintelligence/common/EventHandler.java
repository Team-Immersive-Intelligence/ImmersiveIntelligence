package pl.pabilo8.immersiveintelligence.common;

import blusunrize.immersiveengineering.api.MultiblockHandler.MultiblockFormEvent.Post;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.ValueType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.GameRuleChangeEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickEmpty;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Save;
import net.minecraftforge.event.world.WorldEvent.Unload;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import pl.pabilo8.immersiveintelligence.api.ammo.penetration.DamageBlockPos;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.IIAmmoUtils;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.PenetrationCache;
import pl.pabilo8.immersiveintelligence.api.utils.IAdvancedMultiblock;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Ammunition;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Factions;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons;
import pl.pabilo8.immersiveintelligence.common.compat.BaublesHelper;
import pl.pabilo8.immersiveintelligence.common.compat.IICompatModule;
import pl.pabilo8.immersiveintelligence.common.crafting.IIRecipes;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;
import pl.pabilo8.immersiveintelligence.common.entity.EntityMachinegun;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIBulletMagazine;
import pl.pabilo8.immersiveintelligence.common.item.armor.ItemIILightEngineerBoots;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBlockDamageSync;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageDiplomacySync;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIIGameruleUpdate;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIIRequestChunkClaimData;
import pl.pabilo8.immersiveintelligence.common.util.IIExplosion;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.IIStringUtil;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyUtils;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.IOwnableProperty;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.PermissionCategory;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.chunk.CapabilityChunkOwnership;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.chunk.ChunkOwnership;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.chunk.IChunkOwnership;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemUtils;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIIUpgradeableArmor;

import java.util.ArrayList;

/**
 * Handles events for server side.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 23.09.2023
 */
public class EventHandler
{
	public static ArrayList<IIExplosion> pendingExplosions = new ArrayList<>();
	private static ArrayList<String> registeredGameRules = new ArrayList<>();

	@SubscribeEvent
	public static void onSave(Save event)
	{
		IISaveData.setDirty(event.getWorld().provider.getDimension());
	}

	@SubscribeEvent
	public static void onUnload(Unload event)
	{
		IISaveData.setDirty(event.getWorld().provider.getDimension());
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
			if(ItemIIUpgradeableArmor.isArmorWithUpgrade(head, "toughness_increase")
					||ItemIIUpgradeableArmor.isArmorWithUpgrade(chest, "toughness_increase")
					||ItemIIUpgradeableArmor.isArmorWithUpgrade(legs, "toughness_increase"))
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

	//--- World Load Handling ---//
	@SubscribeEvent
	public void onWorldLoad(Load event)
	{
		//Apply default config
		IIAmmoUtils.ammoBreaksBlocks = Weapons.blockDamage;
		IIAmmoUtils.ammoExplodesBlocks = Ammunition.blockDamage;
		IIAmmoUtils.ammoRicochets = true;
		EntityAmmoProjectile.setSlowmo(1);
		EntityAmmoProjectile.MAX_TICKS = 600;
		EntityHans.INFINITE_AMMO = false;

		GameRules rules = event.getWorld().getGameRules();
		//Whether ammo can break blocks
		initGamerule(IIReference.GAMERULE_AMMO_BREAKS_BLOCKS, rules, ValueType.BOOLEAN_VALUE, IIAmmoUtils.ammoBreaksBlocks);
		//Whether ammo components can explode blocks
		initGamerule(IIReference.GAMERULE_AMMO_EXPLODES_BLOCKS, rules, ValueType.BOOLEAN_VALUE, IIAmmoUtils.ammoExplodesBlocks);
		//Whether the ammo can ricochet
		initGamerule(IIReference.GAMERULE_AMMO_RICOCHETS, rules, ValueType.BOOLEAN_VALUE, IIAmmoUtils.ammoRicochets);
		//After how many ticks ammunition despawns
		initGamerule(IIReference.GAMERULE_AMMO_DECAY, rules, ValueType.NUMERICAL_VALUE, EntityAmmoProjectile.MAX_TICKS);
		//The speed multiplier for ammo movement (0 - 100)
		initGamerule(IIReference.GAMERULE_AMMO_SLOWMO, rules, ValueType.NUMERICAL_VALUE, 100);
		//Whether Hanses have infinite ammo
		initGamerule(IIReference.GAMERULE_HANS_INFINITE_AMMO, rules, ValueType.BOOLEAN_VALUE, EntityHans.INFINITE_AMMO);
	}

	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent event)
	{
		EntityPlayer player = event.player;
		//Execute only on server
		if(event.player.world.isRemote||!(player instanceof EntityPlayerMP))
			return;

		//Sync GameRules
		GameRules rules = event.player.world.getGameRules();
		for(String gamerule : registeredGameRules)
			IIPacketHandler.sendToClient(player, new MessageIIGameruleUpdate(gamerule, rules));

		//Sync Diplomacy data
		IIPacketHandler.sendToClient(player, new MessageDiplomacySync(
				true, null, false, DiplomacyUtils.saveAllToNBT()));
	}

	@SubscribeEvent
	public void attachCapability(AttachCapabilitiesEvent<Chunk> event)
	{
		if(event.getObject()!=null)
		{
			event.addCapability(IIReference.RES_II.with("chunk_ownership"), new CapabilityChunkOwnership(new ChunkOwnership(event.getObject())));
			if(event.getObject().getWorld().isRemote)
				IIPacketHandler.sendToServer(new MessageIIRequestChunkClaimData(event.getObject()));
		}
	}


	private void initGamerule(String ruleName, GameRules rules, ValueType valueType, Object defaultValue)
	{
		if(!rules.hasRule(ruleName))
			rules.addGameRule(ruleName, String.valueOf(defaultValue), valueType);
		if(!registeredGameRules.contains(ruleName))
			registeredGameRules.add(ruleName);
		applyGameRuleValue(rules, ruleName);
	}

	public static void applyGameRuleValue(GameRules rules, String ruleName)
	{
		switch(ruleName)
		{
			case IIReference.GAMERULE_AMMO_BREAKS_BLOCKS:
				IIAmmoUtils.ammoBreaksBlocks = rules.getBoolean(IIReference.GAMERULE_AMMO_BREAKS_BLOCKS);
				break;
			case IIReference.GAMERULE_AMMO_EXPLODES_BLOCKS:
				IIAmmoUtils.ammoExplodesBlocks = rules.getBoolean(IIReference.GAMERULE_AMMO_EXPLODES_BLOCKS);
				break;
			case IIReference.GAMERULE_AMMO_DECAY:
				EntityAmmoProjectile.MAX_TICKS = rules.getInt(IIReference.GAMERULE_AMMO_DECAY);
				break;
			case IIReference.GAMERULE_AMMO_SLOWMO:
				EntityAmmoProjectile.setSlowmo(rules.getInt(IIReference.GAMERULE_AMMO_SLOWMO)/100f);
				break;
			case IIReference.GAMERULE_AMMO_RICOCHETS:
				IIAmmoUtils.ammoRicochets = rules.getBoolean(IIReference.GAMERULE_AMMO_RICOCHETS);
				break;
		}
	}

	@SubscribeEvent
	public void onGameRuleChange(GameRuleChangeEvent event)
	{
		applyGameRuleValue(event.getRules(), event.getRuleName());
		//Sync to players
		IIPacketHandler.sendToAllClients(new MessageIIGameruleUpdate(event.getRuleName(),
				event.getRules().getString(event.getRuleName())));
	}


	//--- Multiblocks ---//

	@SubscribeEvent
	public void onWorldTick(WorldTickEvent event)
	{
		pendingExplosions.removeIf(IIExplosion::explodeBlocks);
	}

	//--- Vehicle or Gun Mounts ---//

	@SubscribeEvent
	public void onMultiblockForm(Post event)
	{
		if(event.isCancelable()&&!event.isCanceled()&&event.getMultiblock().getClass().isAnnotationPresent(IAdvancedMultiblock.class))
		{
			//Required by Advanced Structures!
			if(!IIItemUtils.isAdvancedHammer(event.getHammer()))
			{
				if(!event.getEntityPlayer().getEntityWorld().isRemote)
					IIPacketHandler.sendChatTranslation(event.getEntityPlayer(), "info.immersiveintelligence.requires_advanced_hammer",
							IIStringUtil.getItemStackTextComponent(IIContent.itemHammer.getStack(1)));
				event.setCanceled(true);
			}
		}
	}

	//Cancel when using a machinegun
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onItemUse(RightClickBlock event)
	{
		EntityLivingBase living = event.getEntityLiving();
		TileEntity tile = event.getWorld().getTileEntity(event.getPos());
		//Prevent accessing GUI
		if(Factions.preventContainerAccess&&tile instanceof IGuiTile)
		{
			TileEntity master = ((IGuiTile)tile).getGuiMaster();
			if(master!=null)
			{
				//The property itself has an owner, check it
				OwnerIdentity owner = DiplomacyUtils.NEUTRAL;
				if(master instanceof IOwnableProperty)
					owner = ((IOwnableProperty)master).getOwnerIdentity();
				else
				{
					//Check for the chunk the property is on
					IChunkOwnership ownership = DiplomacyUtils.getPositionOwnership(master.getWorld(), master.getPos());
					if(ownership!=null)
						owner = ownership.getOwner();
				}

				//Deny container access when on an enemy chunk
				if(!owner.isPermitted(living, PermissionCategory.CONTAINER_ACCESS))
				{
					TextComponentTranslation text = new TextComponentTranslation(IIReference.INFO_KEY+"diplomacy.ownership.container_cannot_open");
					text.getStyle().setColor(TextFormatting.RED);

					event.getEntityPlayer().sendStatusMessage(text, true);
					event.setResult(Result.DENY);
					event.setCanceled(true);
				}
			}
		}
		if(living.isRiding()&&living.getRidingEntity() instanceof EntityMachinegun)
		{
			event.setResult(Result.DENY);
			event.setCanceled(true);
		}
	}

	//Cancel when using a machinegun
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onBlockUse(RightClickItem event)
	{
		//Machinegun
		if(event.getEntity().isRiding()&&event.getEntity().getRidingEntity() instanceof EntityMachinegun)
		{
			event.setResult(Result.DENY);
			event.setCanceled(true);
		}
	}

	//Shooting
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onEmptyRightclick(RightClickEmpty event)
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
		for(DamageBlockPos g : PenetrationCache.blockDamage)
		{
			if(g.dimension==event.getWorld().provider.getDimension()&&event.getPos().equals(g)) ;
			{
				dpos = g;
				break;
			}
		}
		if(dpos!=null)
		{
			PenetrationCache.blockDamage.remove(dpos);
			dpos.damage = 0;
			IIPacketHandler.INSTANCE.sendToAllAround(new MessageBlockDamageSync(dpos), IIPacketHandler.targetPointFromPos(dpos, event.getWorld(), 32));
		}
	}

	//--- Hanses ---//

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onLivingUpdate(LivingUpdateEvent event)
	{
		EntityLivingBase living = event.getEntityLiving();
		World world = living.world;
		Biome biome = world.getBiome(living.getPosition());
		if(living instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)living;

			//Potion effects
			if(world.getTotalWorldTime()%20==0)
			{
				//Apply radiation
				if(!player.isCreative()&&biome==IIContent.biomeWasteland)
					living.addPotionEffect(new PotionEffect(IIPotions.radiation, 2000, 0, false, false));

				//Apply faction chunk status effects
				Chunk chunk = player.world.getChunkFromBlockCoords(player.getPosition());
				if(chunk.hasCapability(CapabilityChunkOwnership.CHUNK_OWNERSHIP_CAP, null))
				{
					IChunkOwnership cap = chunk.getCapability(CapabilityChunkOwnership.CHUNK_OWNERSHIP_CAP, null);
					assert cap!=null;
					switch(cap.getOwner().getRelationTowards(player))
					{
						case ENEMY:
							player.addPotionEffect(new PotionEffect(IIPotions.enemySoil, 40, 0, false, false));
							break;
						case MEMBER:
						case ALLIED:
							player.addPotionEffect(new PotionEffect(IIPotions.homeland, 40, 0, false, false));
							break;
						default:
							break;
					}
				}
			}

			//Handle powerpack crafted with armor
			if(!living.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty()
					&&ItemNBTHelper.hasKey(living.getItemStackFromSlot(EntityEquipmentSlot.CHEST), IIContent.NBT_AdvancedPowerpack))
			{
				ItemStack powerpack = ItemNBTHelper.getItemStack(living.getItemStackFromSlot(EntityEquipmentSlot.CHEST), IIContent.NBT_AdvancedPowerpack);
				if(!powerpack.isEmpty())
					powerpack.getItem().onArmorTick(living.getEntityWorld(), player, powerpack);
			}
		}
		else if(world.getTotalWorldTime()%20==0&&biome==IIContent.biomeWasteland)
			living.addPotionEffect(new PotionEffect(IIPotions.radiation, 2000, 0, false, false));

	}

	//--- Armor ---//

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

		//plates - deflect arrows like a shield
		if(event.getSource() instanceof EntityDamageSourceIndirect&&event.getSource().getImmediateSource() instanceof EntityArrow)
		{
			if(ItemIIUpgradeableArmor.isArmorWithUpgrade(head, "toughness_increase")
					||ItemIIUpgradeableArmor.isArmorWithUpgrade(chest, "toughness_increase")
					||ItemIIUpgradeableArmor.isArmorWithUpgrade(legs, "toughness_increase"))
			{
				EntityArrow arrow = (EntityArrow)event.getSource().getImmediateSource();
				//Reflect the arrow away from the wearer
				arrow.motionX *= -0.5;
				arrow.motionY = Math.abs(arrow.motionY)*0.25+0.15;
				arrow.motionZ *= -0.5;
				arrow.velocityChanged = true;
				arrow.shootingEntity = null;
				//Play metallic ricochet sound
				entity.world.playSound(null, entity.posX, entity.posY, entity.posZ,
						IISounds.hitMetal.getRicochetSound(), SoundCategory.PLAYERS, 1.0f, 0.9f+(entity.world.rand.nextFloat()*0.2f));
				event.setCanceled(true);
			}
		}
		//plates - cactus protection
		else if(event.getSource()==DamageSource.CACTUS)
		{
			if(ItemIIUpgradeableArmor.isArmorWithUpgrade(head, "toughness_increase")
					||ItemIIUpgradeableArmor.isArmorWithUpgrade(chest, "toughness_increase")
					||ItemIIUpgradeableArmor.isArmorWithUpgrade(legs, "toughness_increase"))
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

	/**
	 * @author GabrielV (gabriel@iiteam.net)
	 * @since 27.10.2023
	 */
	@SubscribeEvent
	public void onLivingFallEvent(LivingFallEvent event)
	{
		if(event.getEntityLiving() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)event.getEntityLiving();
			Iterable<ItemStack> armor = player.getArmorInventoryList();

			for(ItemStack piece : armor)
			{
				if(!(piece.getItem() instanceof ItemIILightEngineerBoots)) continue;
				ItemIILightEngineerBoots boots = (ItemIILightEngineerBoots)piece.getItem();
				if(boots.hasUpgrade(piece, "internal_springs"))
				{
					event.setDistance(0);
				}
			}
		}
	}

	//--- Casing Pouch ---//

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

	public static ItemStack storeInPouch(ItemStack pouchStack, ItemStack stack)
	{
		if(!pouchStack.getItem().equals(IIContent.itemCasingPouch))
			return stack;

		IItemHandler pouchCap = pouchStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		if(pouchCap==null)
			return stack;

		return ItemHandlerHelper.insertItem(pouchCap, stack, false);
	}

	public static boolean gotGasProtect;
	//26.04.2026 Carver: gas protections from certain gasses. Only for II effects.

	public void onPotionApplicable(PotionApplicableEvent event, EntityLivingBase entity)
	{
		if(entity==null)
			return;

		if(event.getPotionEffect().getPotion()==IIPotions.neuroparalitic)
		{
			if(gotGasProtect==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD),
					"gasmask", "hazmat")&&
					gotGasProtect==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST),
							"hazmat")&&
					gotGasProtect==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS),
							"hazmat")&&
					gotGasProtect==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.FEET),
							"hazmat"))
			{
				event.setCanceled(true);
			}
		}

		if(event.getPotionEffect().getPotion()==IIPotions.poisonirritant)
		{
			if(gotGasProtect==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD),
					"gasmask", "hazmat")&&
					gotGasProtect==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST),
							"hazmat")&&
					gotGasProtect==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS),
							"hazmat")&&
					gotGasProtect==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.FEET),
							"hazmat"))
			{
				event.setCanceled(true);
			}
		}
		//Regular suffocator is already protected from by gasmask.
		if(event.getPotionEffect().getPotion()==IIPotions.suffocatordelayed1)
		{
			if(gotGasProtect==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD),
					"gasmask", "hazmat")&&
					gotGasProtect==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST),
							"hazmat")&&
					gotGasProtect==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS),
							"hazmat")&&
					gotGasProtect==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.FEET),
							"hazmat"))
			{
				event.setCanceled(true);
			}
		}

		if(event.getPotionEffect().getPotion()==IIPotions.bioweapon1)
		{
			if(gotGasProtect==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD),
					"gasmask", "hazmat")&&
					gotGasProtect==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST),
							"hazmat")&&
					gotGasProtect==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS),
							"hazmat")&&
					gotGasProtect==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.FEET),
							"hazmat"))
			{
				event.setCanceled(true);
			}
		}

		if(event.getPotionEffect().getPotion()==IIPotions.bioweapon2_1)
		{
			if(gotGasProtect==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD),
					"gasmask", "hazmat")&&
					gotGasProtect==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST),
							"hazmat")&&
					gotGasProtect==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS),
							"hazmat")&&
					gotGasProtect==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.FEET),
							"hazmat"))
			{
				event.setCanceled(true);
			}
		}

		if(event.getPotionEffect().getPotion()==IIPotions.bioweapon3_1)
		{
			if(gotGasProtect==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD),
					"gasmask", "hazmat")&&
					gotGasProtect==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST),
							"hazmat")&&
					gotGasProtect==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS),
							"hazmat")&&
					gotGasProtect==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.FEET),
							"hazmat"))
			{
				event.setCanceled(true);
			}
		}

		if(event.getPotionEffect().getPotion()==IIPotions.bioweapon4_1)
		{
			if(gotGasProtect==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD),
					"gasmask", "hazmat")&&
					gotGasProtect==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST),
							"hazmat")&&
					gotGasProtect==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS),
							"hazmat")&&
					gotGasProtect==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.FEET),
							"hazmat"))
			{
				event.setCanceled(true);
			}
		}

		if(event.getPotionEffect().getPotion()==IIPotions.bioweapon5_1)
		{
			if(gotGasProtect==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD),
					"gasmask", "hazmat")&&
					gotGasProtect==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST),
							"hazmat")&&
					gotGasProtect==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS),
							"hazmat")&&
					gotGasProtect==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.FEET),
							"hazmat"))
			{
				event.setCanceled(true);
			}
		}

		if(event.getPotionEffect().getPotion()==IIPotions.bioweapon5_2)
		{
			if(gotGasProtect==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD),
					"gasmask", "hazmat")&&
					gotGasProtect==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST),
							"hazmat")&&
					gotGasProtect==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS),
							"hazmat")&&
					gotGasProtect==ItemIIUpgradeableArmor.isArmorWithUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.FEET),
							"hazmat"))
			{
				event.setCanceled(true);
			}
		}
	}
}
