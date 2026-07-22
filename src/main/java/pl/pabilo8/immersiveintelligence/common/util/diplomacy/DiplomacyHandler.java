package pl.pabilo8.immersiveintelligence.common.util.diplomacy;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBanner;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Unload;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Factions;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.IIPotions;
import pl.pabilo8.immersiveintelligence.common.IISaveData;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageDiplomacySync;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIIChunkClaimData;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.agreement.term.DiplomaticAgreement;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.permission.PermissionCategory;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.property.IOwnableProperty;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.property.chunk.chunk.CapabilityChunkOwnership;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.property.chunk.chunk.ChunkClaimData;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.property.chunk.chunk.IChunkOwnership;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Handles operations on Owner Identities and Properties. Use the
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @updated 22.07.2026
 * @since 03.09.2025
 */
public class DiplomacyHandler
{
	public static final UUID NEUTRAL_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
	public static final UUID GLOBAL_ENEMY_UUID = UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff");
	public static OwnerIdentity NEUTRAL, GLOBAL_ENEMY;
	public static PlayerInfo DEFAULT_PLAYER_INFO = new PlayerInfo(UUID.fromString("00000000-0000-0000-0000-000000000000"), "Unknown");

	private static final String KEY_IDENTITIES = "identities", KEY_CHUNKLOADERS = "chunkloaders", KEY_PLAYERS = "players";

	private static final DiplomacyHandler INSTANCE_SERVER = new DiplomacyHandler(false);
	private static final DiplomacyHandler INSTANCE_CLIENT = new DiplomacyHandler(true);
	private static final Comparator<OwnerIdentity> IDENTITY_AGE_COMPARATOR = Comparator
			.comparingLong(OwnerIdentity::getFoundingDate)
			.thenComparing(identity -> identity.getUUID().toString());

	private final HashMap<UUID, OwnerIdentity> ownerIdentities = new HashMap<>();
	private final HashMap<UUID, IOwnableProperty> properties = new HashMap<>();
	private final HashMap<UUID, PlayerInfo> playerInfos = new HashMap<>();
	private final Map<UUID, Ticket> propertyTickets = new HashMap<>();
	private final Map<UUID, Ticket> pendingTickets = new HashMap<>();

	private int pendingTicketCheckTimer = 0;
	private boolean pendingIntegritySave = false;
	public boolean diplomacyInitialized = false;
	public final boolean isRemote;

	private DiplomacyHandler(boolean isRemote)
	{
		this.isRemote = isRemote;
	}

	public static DiplomacyHandler getInstance(boolean isRemote)
	{
		return isRemote?INSTANCE_CLIENT: INSTANCE_SERVER;
	}

	public static OwnerIdentity getIdentityByUUIDStatic(String string)
	{
		boolean client = FMLCommonHandler.instance().getEffectiveSide().isClient();
		return getInstance(client).getIdentityByUUID(string);
	}

	//--- NBT ---//

	public void init()
	{
		IILogger.info("Loading Diplomacy Handler on side: "+(isRemote?"Client": "Server"));
		UUID ieFakePlayerID = UUID.fromString("99562b85-bd1a-4ded-bb1a-c307bf0c0133");

		//Create the neutral faction
		NEUTRAL = new OwnerIdentity(NEUTRAL_UUID, "Neutral")
				.withMember(ieFakePlayerID, LawForm.DEFAULT.getOwnerRole())
				.withBanner(ItemBanner.makeBanner(EnumDyeColor.WHITE, null))
				.withLawForm(LawForm.COMMUNE)
				.withColor(IIColor.MC_GRAY);
		//Create the global enemy faction
		GLOBAL_ENEMY = new OwnerIdentity(GLOBAL_ENEMY_UUID, "GlobalEnemy")
				.withMember(ieFakePlayerID, LawForm.DEFAULT.getOwnerRole())
				.withBanner(ItemBanner.makeBanner(EnumDyeColor.BLACK, null))
				.withColor(IIColor.MC_BLACK)
				.withLawForm(LawForm.COMMISARIAT);

		//Add the default factions to the map
		ownerIdentities.put(NEUTRAL.getUUID(), NEUTRAL);
		ownerIdentities.put(GLOBAL_ENEMY.getUUID(), GLOBAL_ENEMY);

		//Initialization is on after the factions are first loaded from NBT
		diplomacyInitialized = false;
	}

	public void loadAllFromNBT(EasyNBT nbt)
	{
		diplomacyInitialized = true;
		//Load factions from NBT
		nbt.streamList(NBTTagCompound.class, KEY_IDENTITIES)
				.map(EasyNBT::wrapNBT)
				.map(OwnerIdentity::new)
				.distinct()
				.forEach(loaded -> ownerIdentities.compute(loaded.getUUID(),
						//Merge with existing placeholder identity if present
						(oid, identity) -> {
							if(identity==null)
								return loaded;
							identity.deserializeNBT(loaded.serializeNBT());
							return identity;
						}));

		//Remove placeholder identities
		ownerIdentities.values().removeIf(OwnerIdentity::isInvalid);

		//Load player infos before reporting integrity repairs.
		playerInfos.clear();
		nbt.streamList(NBTTagCompound.class, KEY_PLAYERS)
				.map(PlayerInfo::new)
				.forEach(playerInfo -> playerInfos.put(playerInfo.uuid, playerInfo));


		//Ensure that player belongs to only one identity, move them and invalidate identities without players left
		boolean repaired = validateIdentityIntegrity();
		ownerIdentities.entrySet().removeIf(entry -> {
			OwnerIdentity identity = entry.getValue();
			return identity.isInvalid()&&identity.getUUID()!=NEUTRAL_UUID&&identity.getUUID()!=GLOBAL_ENEMY_UUID;
		});

		//Fix loaded properties and canonicalize their identity references.
		properties.values().stream()
				.map(IOwnableProperty::master)
				.forEach(property -> {
					OwnerIdentity identity = property.getOwnerIdentity();
					OwnerIdentity canonical = identity==null?null: ownerIdentities.get(identity.getUUID());
					property.setOwnerIdentity(canonical==null||canonical.isInvalid()?NEUTRAL: canonical);
				});

		diplomacyInitialized = true;
		if(repaired&&!isRemote)
			pendingIntegritySave = true;

		if(!isRemote)
			for(MessageDiplomacySync message : MessageDiplomacySync.updateAllMessage())
				IIPacketHandler.sendToAllClients(message);
	}

	private boolean validateIdentityIntegrity()
	{
		Map<UUID, OwnerIdentity> memberships = new HashMap<>();
		List<OwnerIdentity> identities = ownerIdentities.values().stream()
				.filter(identity -> identity.getUUID()!=NEUTRAL_UUID)
				.filter(identity -> identity.getUUID()!=GLOBAL_ENEMY_UUID)
				.filter(identity -> !identity.isInvalid())
				.sorted(IDENTITY_AGE_COMPARATOR)
				.collect(Collectors.toList());
		boolean repaired = false;

		for(OwnerIdentity identity : identities)
			for(UUID member : new ArrayList<>(identity.getMembers()))
			{
				OwnerIdentity oldestIdentity = memberships.putIfAbsent(member, identity);
				if(oldestIdentity!=null&&identity.removeMemberForIntegrityCheck(member))
				{
					repaired = true;
					IILogger.warn("Removed duplicate member "+member+" from identity "+identity.getUUID()
							+"; keeping oldest identity "+oldestIdentity.getUUID()+".");
				}
			}

		for(OwnerIdentity identity : identities)
			if(!identity.hasAnyPlayers())
			{
				identity.invalidateForIntegrityCheck();
				repaired = true;
				IILogger.warn("Invalidated empty owner identity "+identity.getUUID()+".");
			}

		return repaired;
	}

	public EasyNBT saveAllToNBT()
	{
		EasyNBT enbt = EasyNBT.newNBT();
		//Save factions
		enbt.withList(KEY_IDENTITIES, OwnerIdentity::serializeNBT, ownerIdentities.values());
		//Save player infos
		enbt.withList(KEY_PLAYERS, PlayerInfo::serializeNBT, playerInfos.values());
		return enbt;
	}

	public void cleanup()
	{
		IILogger.info("Unloading Diplomacy Handler on side: "+(isRemote?"Client": "Server"));
		diplomacyInitialized = false;
		ownerIdentities.clear();
		properties.clear();
		propertyTickets.clear();
		pendingTickets.clear();
		pendingTicketCheckTimer = 0;
		pendingIntegritySave = false;
	}

	//--- Update Loop ---//

	public void update(World world)
	{
		if(isRemote)
		{
			//Request update from server until diplomacy is initialized, fixes some rare cases
			if(!diplomacyInitialized)
			{
				if(pendingTicketCheckTimer%240==0)
					IIPacketHandler.sendToServer(MessageDiplomacySync.requestUpdateMessage());
				else
					pendingTicketCheckTimer++;
			}
			return;
		}
		if(!diplomacyInitialized)
			return;

		//WorldSavedData is fully installed by the first server tick, so persist any
		//migration repairs here rather than during readFromNBT.
		if(pendingIntegritySave)
		{
			IISaveData.setDirty();
			pendingIntegritySave = false;
		}

		//Load chunks
		pendingTicketCheckTimer++;
		if(pendingTicketCheckTimer >= Factions.chunkloaderTickDelay)
		{
			//Remove invalid properties
			if(properties.entrySet().removeIf(entry -> !entry.getValue().isValid()))
				IILogger.info("Invalid property removed.");

			//Release pending tickets whose property never appeared
			Iterator<Map.Entry<UUID, Ticket>> iter = pendingTickets.entrySet().iterator();
			while(iter.hasNext())
			{
				Map.Entry<UUID, Ticket> entry = iter.next();
				UUID uuid = entry.getKey();
				//If the property still doesn't exist or is invalid, release
				if(!properties.containsKey(uuid)||getIdentityByUUID(uuid).isInvalid())
				{
					ForgeChunkManager.releaseTicket(entry.getValue());
					iter.remove();
				}
			}

			//Reset timer
			pendingTicketCheckTimer = 0;
		}

		//Check chunk claims validity
		if(world.getTotalWorldTime()%Factions.claimTickDelay==0)
		{
			IILogger.debug("Updating chunk claims for world "+world.provider.getDimension());
			properties.values().stream()
					.filter(property -> property.getIIWorld()==world)
					.forEach(this::claimChunks);
		}
	}

	//--- Properties ---//

	public void validateProperty(IOwnableProperty property)
	{
		UUID uuid = property.getUUID();
		IILogger.debug("Validating IOwnableProperty: "+uuid);
		properties.put(uuid, property);
		claimChunks(property);
		setupChunkLoading(property);
	}

	private void claimChunks(IOwnableProperty property)
	{
		if(!diplomacyInitialized||property==null||property.getOwnerIdentity()==null)
			return;
		IILogger.debug("Claiming chunks for property: "+property.getUUID());
		World world = property.getIIWorld();
		BlockPos pos = property.getIIPos();
		int ownedChunksRadius = property.getChunkOwnershipRadius();

		//center chunk coordinates
		int centerChunkX = pos.getX()>>4;
		int centerChunkZ = pos.getZ()>>4;

		//iterate over a square of chunks around the property
		for(int cx = centerChunkX-ownedChunksRadius; cx <= centerChunkX+ownedChunksRadius; cx++)
			for(int cz = centerChunkZ-ownedChunksRadius; cz <= centerChunkZ+ownedChunksRadius; cz++)
			{
				Chunk chunk = world.getChunkFromChunkCoords(cx, cz);
				IChunkOwnership ownership = getChunkOwnership(chunk);
				if(ownership==null)
				{
					IILogger.error("Could not claim chunk at "+cx+", "+cz+" for property "+property.getUUID()+", missing IChunkOwnership capability!");
					break;
				}

				ChunkClaimData claimData = ownership.getClaimData();
				if((diplomacyInitialized^ownership.getOwner().isInvalid())&&claimData!=null&&ownership.getOwner()!=NEUTRAL)
				{
					//Already claimed chunk
					long existingClaimTime = claimData.getClaimTime();
					//Preferm older claims
					if(existingClaimTime > property.getTicksExisted())
					{
						ChunkClaimData chunkClaimData = new ChunkClaimData(property);
						ownership.setOwner(property.getOwnerIdentity());
						ownership.setClaimData(chunkClaimData);
						if(!world.isRemote)
							IIPacketHandler.INSTANCE.sendToDimension(new MessageIIChunkClaimData(world, pos, property.getOwnerIdentity(), chunkClaimData),
									world.provider.getDimension());
					}
				}
				else
				{
					ChunkClaimData chunkClaimData = new ChunkClaimData(property);
					ownership.setOwner(property.getOwnerIdentity());
					ownership.setClaimData(chunkClaimData);
					if(!world.isRemote)
						IIPacketHandler.INSTANCE.sendToDimension(new MessageIIChunkClaimData(world, pos, property.getOwnerIdentity(), chunkClaimData),
								world.provider.getDimension());
				}
			}
	}

	//--- Chunkloading ---//

	/**
	 * Called by Forge loading callback when saved tickets are reloaded.
	 */
	public void onTicketsLoaded(List<Ticket> tickets, World world)
	{
		if(isRemote)
			return;
		for(Ticket ticket : tickets)
		{
			String uuidString = ticket.getModData().getString("propertyUUID");
			if(uuidString.isEmpty())
			{
				//This ticket doesn't belong to us – release it (safety)
				ForgeChunkManager.releaseTicket(ticket);
				continue;
			}
			UUID propUuid;
			try
			{
				propUuid = UUID.fromString(uuidString);
			} catch(IllegalArgumentException e)
			{
				ForgeChunkManager.releaseTicket(ticket);
				continue;
			}

			//Check if the property is already known and valid
			OwnerIdentity owner = getIdentityByUUID(propUuid);
			boolean propertyExists = properties.containsKey(propUuid);
			//Not yet validated – keep in pending list
			if(propertyExists&&owner!=NEUTRAL&&!owner.isInvalid())
			{
				//Property already loaded – take over the ticket
				propertyTickets.put(propUuid, ticket);
				//Re-apply correct chunk forces (just to be safe)
				IOwnableProperty prop = properties.get(propUuid);
				int radius = prop.getChunkLoadingRange();
				int cx = prop.getIIPos().getX()>>4;
				int cz = prop.getIIPos().getZ()>>4;
				//Unforce old saved positions
				for(int x = cx-radius; x <= cx+radius; x++)
					for(int z = cz-radius; z <= cz+radius; z++)
						ForgeChunkManager.forceChunk(ticket, new net.minecraft.util.math.ChunkPos(x, z));
			}
			else
				pendingTickets.put(propUuid, ticket);
		}
	}

	private void setupChunkLoading(IOwnableProperty property)
	{
		if(!diplomacyInitialized||isRemote)
			return;
		int radius = property.getChunkLoadingRange();
		if(radius <= 0)
			return;

		World world = property.getIIWorld();
		UUID propUuid = property.getUUID();

		//If we already have an active ticket for this property, skip
		if(propertyTickets.containsKey(propUuid))
			return;

		Ticket ticket = pendingTickets.remove(propUuid); //pick up a pending ticket first
		if(ticket==null)
		{
			//Request a new ticket
			ticket = ForgeChunkManager.requestTicket(ImmersiveIntelligence.INSTANCE, world, ForgeChunkManager.Type.NORMAL);
			if(ticket==null)
			{
				IILogger.warn("Could not get chunkloading ticket for property "+propUuid);
				return;
			}
			//Store the property UUID in the ticket's mod data for persistence
			ticket.getModData().setString("propertyUUID", propUuid.toString());
		}

		//Force chunks inside the loading radius
		int cx = property.getIIPos().getX()>>4;
		int cz = property.getIIPos().getZ()>>4;
		for(int x = cx-radius; x <= cx+radius; x++)
			for(int z = cz-radius; z <= cz+radius; z++)
				ForgeChunkManager.forceChunk(ticket, new net.minecraft.util.math.ChunkPos(x, z));

		propertyTickets.put(propUuid, ticket);
	}

	private void releaseChunkLoading(IOwnableProperty property)
	{
		if(isRemote) return;
		UUID propUuid = property.getUUID();
		Ticket ticket = propertyTickets.remove(propUuid);
		if(ticket==null) return;

		//Release all forced chunks from this ticket
		ForgeChunkManager.releaseTicket(ticket);
	}

	//--- Getters ---//

	@Nonnull
	@SideOnly(Side.CLIENT)
	public static OwnerIdentity getLocalPlayerIdentity()
	{
		return getInstance(true).getOwnerIdentityForEntity(ClientUtils.mc().player);
	}

	/*public static OwnerIdentity getIdentityByUUID(String uuid)
	{

	}*/

	public OwnerIdentity getIdentityByUUID(String uuid)
	{
		try
		{
			UUID parsed = UUID.fromString(uuid);
			return getIdentityByUUID(parsed);
		} catch(IllegalArgumentException e)
		{
			return NEUTRAL;
		}
	}

	public OwnerIdentity getIdentityByUUID(@Nonnull UUID uuid)
	{
		//Return a placeholder identity
		if(!diplomacyInitialized)
			return ownerIdentities.computeIfAbsent(uuid, OwnerIdentity::new);
		return ownerIdentities.getOrDefault(uuid, NEUTRAL);
	}

	@Nullable
	public OwnerIdentity getIdentityByName(@Nonnull String name)
	{
		if(name.equals("neutral"))
			return NEUTRAL;
		if(name.equals("global_enemy"))
			return GLOBAL_ENEMY;
		for(OwnerIdentity value : ownerIdentities.values())
			if(value.getDisplayName().equals(name))
				return value;
		return null;
	}

	@Nonnull
	public OwnerIdentity getOwnerIdentityForEntity(EntityLivingBase player)
	{
		if(!isRemote&&player instanceof EntityPlayer&&!playerInfos.containsKey(player.getUniqueID()))
			updatePlayerInfo(new PlayerInfo(player));

		//Try to get an existing identity. The age ordering keeps this deterministic even
		//if invalid runtime state is introduced before the next save/load integrity pass.
		Optional<OwnerIdentity> existing = ownerIdentities.values().stream()
				.filter(oi -> oi.getUUID()!=NEUTRAL_UUID)
				.filter(oi -> oi.getUUID()!=GLOBAL_ENEMY_UUID)
				.filter(identity -> !identity.isInvalid())
				.filter(identity -> identity.isMember(player))
				.min(IDENTITY_AGE_COMPARATOR);
		if(existing.isPresent())
			return existing.get();

		//Only players should be able to create a new indentity
		if(!player.world.isRemote&&player instanceof EntityPlayer)
		{
			//Create new identity
			OwnerIdentity identity = new OwnerIdentity(player);
			ownerIdentities.put(identity.getUUID(), identity);

			//Save and update clients
			saveAndSyncIdentity(identity);

			return identity;
		}
		return NEUTRAL;
	}

	public IOwnableProperty getPropertyByUUID(UUID uuid)
	{
		return properties.get(uuid);
	}

	@Nullable
	public IChunkOwnership getChunkOwnership(Chunk chunk)
	{
		if(chunk.hasCapability(CapabilityChunkOwnership.CHUNK_OWNERSHIP_CAP, null))
			return chunk.getCapability(CapabilityChunkOwnership.CHUNK_OWNERSHIP_CAP, null);
		return null;
	}

	public void setChunkOwnership(Chunk chunk, IChunkOwnership ownership)
	{

	}

	public IChunkOwnership getPositionOwnership(World world, BlockPos pos)
	{
		Chunk chunk = world.getChunkFromBlockCoords(pos);
		return getChunkOwnership(chunk);
	}

	//--- Utilities ---//

	public void claimProperty(OwnerIdentity identity, IOwnableProperty property)
	{
		property.master().setOwnerIdentity(identity);
		saveAndSyncIdentity(identity);
	}

	public void proposeAgreement(OwnerIdentity from, OwnerIdentity to, DiplomaticAgreement proposal)
	{
		//Save proposal to both factions' pending lists
		from.addGrantorAgreement(proposal);
		to.addTargetAgreement(proposal);
		IIPacketHandler.sendToAllClients(MessageDiplomacySync.updateIdentityMessage(from));
		IIPacketHandler.sendToAllClients(MessageDiplomacySync.updateIdentityMessage(to));
		saveAndSyncIdentity(from);
		saveAndSyncIdentity(to);
	}

	//In accept/deny of an agreement (when the target faction accepts/denies a proposal):
	public void acceptAgreement(OwnerIdentity acceptingFaction, DiplomaticAgreement proposal)
	{
		if(!proposal.isPending())
			return;
		proposal.accept();
		//Remove from pending lists
		acceptingFaction.removeAgreement(proposal);
		OwnerIdentity sourceFaction = getIdentityByUUID(proposal.getSourceFaction());
		if(sourceFaction!=NEUTRAL)
		{
			sourceFaction.removeAgreement(proposal);
			//Apply terms
			sourceFaction.addGrantorAgreement(proposal);
			acceptingFaction.addTargetAgreement(proposal);
			//Execute chunk transfers
			for(UUID propId : proposal.getTransferredProperties())
			{
				IOwnableProperty prop = getPropertyByUUID(propId);
				if(prop!=null)
					prop.master().setOwnerIdentity(sourceFaction); //whichever direction
			}
		}
		IISaveData.setDirty();
		IIPacketHandler.sendToAllClients(MessageDiplomacySync.updateIdentityMessage(sourceFaction));
		IIPacketHandler.sendToAllClients(MessageDiplomacySync.updateIdentityMessage(acceptingFaction));
	}

	public OwnerIdentity merge(OwnerIdentity a, OwnerIdentity b)
	{
		//Merge two identities
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		long foundingDate = server==null?Math.max(a.getFoundingDate(), b.getFoundingDate()): server.getEntityWorld().getTotalWorldTime();
		OwnerIdentity merged = new OwnerIdentity(a, b, foundingDate);
		ownerIdentities.remove(a.getUUID());
		ownerIdentities.remove(b.getUUID());
		ownerIdentities.put(merged.getUUID(), merged);

		//Save and update clients
		IISaveData.setDirty();
		IIPacketHandler.sendToAllClients(MessageDiplomacySync.removeIdentityMessage(a));
		IIPacketHandler.sendToAllClients(MessageDiplomacySync.removeIdentityMessage(b));
		IIPacketHandler.sendToAllClients(MessageDiplomacySync.updateIdentityMessage(merged));
		return merged;
	}

	public OwnerIdentity[] split(OwnerIdentity identity, EntityLivingBase... between)
	{
		//Save and update clients
		saveAndSyncIdentity(identity);
		return new OwnerIdentity[]{identity};
	}

	//--- Player Invitation ---//

	public Set<String> getPendingInvitationsForPlayer(UUID playerUUID)
	{
		return ownerIdentities.values().stream()
				.filter(oi -> !oi.isInvalid())
				.filter(oi -> oi.getUUID()!=NEUTRAL_UUID)
				.filter(oi -> oi.getUUID()!=GLOBAL_ENEMY_UUID)
				.filter(oi -> oi.isInvited(playerUUID))
				.map(OwnerIdentity::getDisplayName)
				.collect(Collectors.toSet());
	}

	public Set<UUID> getPendingInvitationsForFaction(UUID factionUUID)
	{
		OwnerIdentity faction = getIdentityByUUID(factionUUID);
		return faction!=NEUTRAL?faction.getInvitedPlayers(): Collections.emptySet();
	}

	public boolean acceptInvitation(OwnerIdentity identity, UUID playerUUID)
	{
		if(identity.getUUID()!=NEUTRAL_UUID&&identity.getUUID()!=GLOBAL_ENEMY_UUID&&identity.isInvalid())
			return false;
		if(identity.isInvited(playerUUID))
		{
			//Add to new identity
			identity.removeInvitation(playerUUID);
			identity.withMember(playerUUID, identity.getStartingMemberRole());

			//Remove from old identity
			Optional<OwnerIdentity> first = ownerIdentities.values().stream()
					.filter(oi -> oi!=identity)
					.filter(oi -> oi.isMember(playerUUID))
					.findFirst();
			if(first.isPresent())
			{
				//Check if the player is the last remaining player in the faction
				OwnerIdentity previousIdentity = first.get();
				boolean isLastOwner = previousIdentity.isOwner(playerUUID)&&previousIdentity.getMembers().size()==1;
				previousIdentity.removeMember(playerUUID);

				//Pass all the properties owned by the previous identity to the new identity
				if(isLastOwner)
				{
					IILogger.info("Passed all properties of faction %s to new owner %s after player %s accepted invitation.",
							previousIdentity.getDisplayName(), identity.getDisplayName(), playerUUID);
					for(IOwnableProperty value : properties.values())
					{
						IOwnableProperty master = value.master();
						try
						{
							assert master!=null;
							master.setOwnerIdentity(identity);
						} catch(Exception e)
						{
							IILogger.error("Failed to transfer property %s from %s to %s after player %s accepted invitation.",
									value.getUUID(), previousIdentity.getUUID(), identity.getUUID(), playerUUID);
						}
					}

					//Reclaim chunks
					for(IOwnableProperty value : properties.values())
						claimChunks(value.master());
				}
			}


			return true;
		}
		return false;
	}

	public boolean denyInvitation(OwnerIdentity identity, UUID playerUUID)
	{
		if(identity.isInvited(playerUUID))
		{
			identity.removeInvitation(playerUUID);
			saveAndSyncIdentity(identity);
			return true;
		}
		return false;
	}

	//--- Server Sync Methods ---//

	public void saveAndSyncIdentity(OwnerIdentity identity)
	{
		if(isRemote)
			return;
		IISaveData.setDirty();
		IIPacketHandler.sendToAllClients(MessageDiplomacySync.updateIdentityMessage(identity));
	}

	//--- Client Sync Methods ---//

	public void removeIdentity(UUID uuid)
	{
		if(uuid==NEUTRAL_UUID||uuid==GLOBAL_ENEMY_UUID)
			return;
		OwnerIdentity removed = ownerIdentities.remove(uuid);
		if(removed!=null)
		{
			//Release all tickets of properties owned by this identity
			for(IOwnableProperty prop : properties.values())
				if(prop.getOwnerIdentity().equals(removed))
					releaseChunkLoading(prop);
			//Also release any pending ticket linked to this identity? Not needed, pending will be cleaned later.
			if(!isRemote)
				IIPacketHandler.sendToAllClients(MessageDiplomacySync.removeIdentityMessage(removed));
		}
	}

	public void updateIdentity(UUID uuid, EasyNBT tagCompound)
	{
		if(!isRemote)
			return;
		OwnerIdentity identity = getIdentityByUUID(uuid);
		if(identity!=NEUTRAL)
			identity.deserializeNBT(tagCompound.unwrap());
		else
		{
			OwnerIdentity updated = new OwnerIdentity(tagCompound);
			ownerIdentities.put(uuid, updated);
		}
	}

	//--- Player Utils ---//

	public PlayerInfo getPlayerInfo(@Nonnull EntityPlayer player)
	{
		return getPlayerInfo(player.getUniqueID());
	}

	public PlayerInfo getPlayerInfo(@Nonnull UUID uuid)
	{
		return playerInfos.getOrDefault(uuid, DEFAULT_PLAYER_INFO);
	}

	public void updatePlayerInfo(PlayerInfo playerInfo)
	{
		playerInfos.put(playerInfo.uuid, playerInfo);
	}

	/**
	 * Holds resolved player information.
	 */
	public static class PlayerInfo implements INBTSerializable<NBTTagCompound>
	{
		private UUID uuid;
		private String name;
		private ResourceLocation skinLocation = null;

		private PlayerInfo(UUID uuid, String name)
		{
			this.uuid = uuid;
			this.name = name;
		}

		public PlayerInfo(EntityLivingBase player)
		{
			this(player.getUniqueID(), player.getName());
		}

		public PlayerInfo(NBTTagCompound tagCompound)
		{
			deserializeNBT(tagCompound);
		}

		public String getName()
		{
			return name;
		}

		@SideOnly(Side.CLIENT)
		public ResourceLocation getSkin()
		{
			if(skinLocation!=null)
				return skinLocation;

			NetHandlerPlayClient connection = ClientUtils.mc().getConnection();
			if(connection!=null)
			{
				NetworkPlayerInfo networkplayerinfo = connection.getPlayerInfo(uuid);
				//noinspection ConstantValue
				if(networkplayerinfo!=null)
					return skinLocation = networkplayerinfo.getLocationSkin();
			}

			return skinLocation = DefaultPlayerSkin.getDefaultSkinLegacy();
		}

		@Override
		public NBTTagCompound serializeNBT()
		{
			return EasyNBT.newNBT()
					.withUUID("uuid", uuid)
					.withString("name", name)
					.unwrap();
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			EasyNBT enbt = EasyNBT.wrapNBT(nbt);
			uuid = enbt.getUUID("uuid");
			name = enbt.getString("name");
		}
	}

	//--- Event Subscriber ---//

	private boolean isSideMatched(World other)
	{
		return other.isRemote==this.isRemote;
	}

	@SubscribeEvent
	public void onWorldLoad(Load event)
	{
		if(isRemote&&isSideMatched(event.getWorld()))
			init();
	}

	@SubscribeEvent
	public void onWorldUnload(Unload event)
	{
		if(isRemote&&isSideMatched(event.getWorld()))
			cleanup();
	}

	@SubscribeEvent
	public void onWorldTick(WorldTickEvent event)
	{
		if(!isRemote&&isSideMatched(event.world))
			update(event.world);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onTickClientTick(ClientTickEvent event)
	{
		if(isRemote&&ClientUtils.mc().world!=null)
			update(ClientUtils.mc().world);
	}


	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent event)
	{
		if(isRemote)
			return;

		//Sync player info
		PlayerInfo playerInfo = new PlayerInfo(event.player);
		updatePlayerInfo(new PlayerInfo(event.player));
		IIPacketHandler.sendToAllClients(MessageDiplomacySync.syncPlayerInfo(playerInfo));
	}

	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event)
	{
		EntityLivingBase living = event.getEntityLiving();
		if(isRemote||!isSideMatched(living.world)||!diplomacyInitialized)
			return;
		if(!(living instanceof EntityPlayer))
			return;

		//Apply faction chunk status effects
		Chunk chunk = living.world.getChunkFromBlockCoords(living.getPosition());
		if(chunk.hasCapability(CapabilityChunkOwnership.CHUNK_OWNERSHIP_CAP, null))
		{
			IChunkOwnership cap = chunk.getCapability(CapabilityChunkOwnership.CHUNK_OWNERSHIP_CAP, null);
			assert cap!=null;
			switch(cap.getOwner().getRelationTowards(living))
			{
				case ENEMY:
					living.addPotionEffect(new PotionEffect(IIPotions.enemySoil, 40, 0, false, false));
					break;
				case MEMBER:
				case ALLIED:
					living.addPotionEffect(new PotionEffect(IIPotions.homeland, 40, 0, false, false));
					break;
				default:
					break;
			}
		}
	}


	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onItemUse(RightClickBlock event)
	{
		if(!isSideMatched(event.getWorld()))
			return;
		TileEntity tile = event.getWorld().getTileEntity(event.getPos());
		EntityLivingBase living = event.getEntityLiving();
		//Prevent accessing GUI
		if(Factions.preventContainerAccess&&tile instanceof IGuiTile)
		{
			TileEntity master = ((IGuiTile)tile).getGuiMaster();
			if(master!=null)
			{
				//The property itself has an owner, check it
				OwnerIdentity owner = null;
				if(master instanceof IOwnableProperty)
					owner = ((IOwnableProperty)master).getOwnerIdentity();
				else
				{
					//Check for the chunk the property is on
					IChunkOwnership ownership = getPositionOwnership(master.getWorld(), master.getPos());
					if(ownership!=null)
						owner = ownership.getOwner();
				}
				if(owner==null)
					owner = NEUTRAL;

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
	}

}
