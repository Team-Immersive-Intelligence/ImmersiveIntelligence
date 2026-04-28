package pl.pabilo8.immersiveintelligence.common.util.diplomacy;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBanner;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.IISaveData;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageDiplomacySync;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIIChunkClaimData;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.agreement.term.DiplomaticAgreement;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.chunk.CapabilityChunkOwnership;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.chunk.ChunkClaimData;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.chunk.IChunkOwnership;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 03.09.2025
 */
public class DiplomacyUtils
{
	private static final HashMap<UUID, OwnerIdentity> OWNER_IDENTITIES = new HashMap<>();
	private static final HashMap<UUID, IOwnableProperty> PROPERTIES = new HashMap<>();

	public static OwnerIdentity NEUTRAL, GLOBAL_ENEMY;
	public static boolean diplomacyInitialized = false;

	private static final String KEY_IDENTITIES = "identities";

	//--- NBT ---//

	public static void init()
	{
		UUID ieFakePlayerID = UUID.fromString("99562b85-bd1a-4ded-bb1a-c307bf0c0133");

		//Create the neutral faction
		if(NEUTRAL==null)
			NEUTRAL = new OwnerIdentity(UUID.fromString("00000000-0000-0000-0000-000000000000"), "Neutral")
					.withMember(ieFakePlayerID, LawForm.DEFAULT.getOwnerRole(), false)
					.withBanner(ItemBanner.makeBanner(EnumDyeColor.WHITE, null))
					.withLawForm(LawForm.COMMUNE)
					.withColor(IIColor.MC_GRAY);
		//Create the global enemy faction
		if(GLOBAL_ENEMY==null)
			GLOBAL_ENEMY = new OwnerIdentity(UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff"), "GlobalEnemy")
					.withMember(ieFakePlayerID, LawForm.DEFAULT.getOwnerRole(), false)
					.withBanner(ItemBanner.makeBanner(EnumDyeColor.BLACK, null))
					.withColor(IIColor.MC_BLACK)
					.withLawForm(LawForm.COMMISARIAT);

		//Add the default factions to the map
		OWNER_IDENTITIES.put(NEUTRAL.getUUID(), NEUTRAL);
		OWNER_IDENTITIES.put(GLOBAL_ENEMY.getUUID(), GLOBAL_ENEMY);

		//Initialization is on after the factions are first loaded from NBT
		diplomacyInitialized = false;
	}

	public static void loadAllFromNBT(EasyNBT nbt)
	{
		diplomacyInitialized = true;
		//Load factions from NBT
		nbt.streamList(NBTTagCompound.class, KEY_IDENTITIES)
				.map(EasyNBT::wrapNBT)
				.map(OwnerIdentity::new)
				.distinct()
				.forEach(loaded -> OWNER_IDENTITIES.compute(loaded.getUUID(),
						//Merge with existing placeholder identity if present
						(oid, identity) -> {
							if(identity==null)
								return loaded;
							identity.loadFromNBT(loaded.toNBT());
							return identity;
						}));

		//Remove identities that are invalid
		OWNER_IDENTITIES.values().removeIf(OwnerIdentity::isInvalid);

		//Fix loaded properties
		PROPERTIES.values().stream()
				.map(IOwnableProperty::master)
				.forEach(property -> {
					OwnerIdentity identity = property.getOwnerIdentity();
					property.setOwnerIdentity(identity.isInvalid()?NEUTRAL: identity);
				});
	}

	public static EasyNBT saveAllToNBT()
	{
		EasyNBT enbt = EasyNBT.newNBT();
		//Load factions from NBT
		enbt.withList(KEY_IDENTITIES, o -> o.toNBT().unwrap(), OWNER_IDENTITIES.values());

		return enbt;
	}

	public static void unload()
	{
		diplomacyInitialized = false;
		OWNER_IDENTITIES.clear();
		PROPERTIES.clear();
		NEUTRAL = null;
		GLOBAL_ENEMY = null;
	}

	public static void validateProperty(IOwnableProperty property)
	{
		UUID uuid = property.getUUID();
		IILogger.debug("Validating IOwnableProperty: "+uuid);
		PROPERTIES.put(uuid, property);
		claimChunks(property);
	}

	public static void invalidateProperty(IOwnableProperty property)
	{
		UUID uuid = property.getUUID();
		IILogger.debug("Invalidating IOwnableProperty: "+uuid);
		PROPERTIES.remove(uuid);
	}

	public static void claimChunks(IOwnableProperty property)
	{
		IILogger.debug("Claiming chunks for property: "+property.getUUID());
		World world = property.getIIWorld();
		BlockPos pos = property.getIIPos();
		int ownedChunksRadius = property.getChunkOwnershipRadius();

		// center chunk coordinates
		int centerChunkX = pos.getX()>>4;
		int centerChunkZ = pos.getZ()>>4;

		// iterate over a square of chunks around the property
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
							IIPacketHandler.sendToClient(new MessageIIChunkClaimData(world, pos, property.getOwnerIdentity(), chunkClaimData));
					}
				}
				else
				{
					ChunkClaimData chunkClaimData = new ChunkClaimData(property);
					ownership.setOwner(property.getOwnerIdentity());
					ownership.setClaimData(chunkClaimData);
					if(!world.isRemote)
						IIPacketHandler.sendToClient(new MessageIIChunkClaimData(world, pos, property.getOwnerIdentity(), chunkClaimData));
				}
			}
	}

	//--- Getters ---//

	@Nonnull
	@SideOnly(Side.CLIENT)
	public static OwnerIdentity getLocalPlayerIdentity()
	{
		return getOwnerIdentityForEntity(ClientUtils.mc().player);
	}

	public static OwnerIdentity getIdentityByUUID(String uuid)
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

	public static OwnerIdentity getIdentityByUUID(UUID uuid)
	{
		//Return a placeholder identity
		if(!diplomacyInitialized)
			return OWNER_IDENTITIES.computeIfAbsent(uuid, OwnerIdentity::new);
		return OWNER_IDENTITIES.getOrDefault(uuid, NEUTRAL);
	}

	@Nullable
	public static OwnerIdentity getIdentityByName(String name)
	{
		if(name.equals("neutral"))
			return NEUTRAL;
		if(name.equals("global_enemy"))
			return GLOBAL_ENEMY;
		for(OwnerIdentity value : OWNER_IDENTITIES.values())
			if(value.getDisplayName().equals(name))
				return value;
		return null;
	}

	@Nonnull
	public static OwnerIdentity getOwnerIdentityForEntity(EntityLivingBase player)
	{
		//Try to get an existing identity
		for(OwnerIdentity identity : OWNER_IDENTITIES.values())
			if(identity.isMember(player))
				return identity;

		//Only players should be able to create a new indentity
		if(!player.world.isRemote&&player instanceof EntityPlayer)
		{
			//Create new identity
			OwnerIdentity identity = new OwnerIdentity(player);
			OWNER_IDENTITIES.put(identity.getUUID(), identity);

			//Save and update clients
			saveAndSyncIdentity(identity);

			return identity;
		}
		return NEUTRAL;
	}

	public static IOwnableProperty getPropertyByUUID(UUID uuid)
	{
		return PROPERTIES.get(uuid);
	}

	@Nullable
	public static IChunkOwnership getChunkOwnership(Chunk chunk)
	{
		if(chunk.hasCapability(CapabilityChunkOwnership.CHUNK_OWNERSHIP_CAP, null))
			return chunk.getCapability(CapabilityChunkOwnership.CHUNK_OWNERSHIP_CAP, null);
		return null;
	}

	public static void setChunkOwnership(Chunk chunk, IChunkOwnership ownership)
	{

	}

	public static IChunkOwnership getPositionOwnership(World world, BlockPos pos)
	{
		Chunk chunk = world.getChunkFromBlockCoords(pos);
		return getChunkOwnership(chunk);
	}

	//--- Utilities ---//

	public static void claimProperty(OwnerIdentity identity, IOwnableProperty property)
	{
		property.master().setOwnerIdentity(identity);
		saveAndSyncIdentity(identity);
	}

	public static void proposeAgreement(OwnerIdentity from, OwnerIdentity to, DiplomaticAgreement proposal)
	{
		// Save proposal to both factions' pending lists
		from.addGrantorAgreement(proposal);
		to.addTargetAgreement(proposal);
		IIPacketHandler.sendToAllClients(MessageDiplomacySync.updateIdentityMessage(from));
		IIPacketHandler.sendToAllClients(MessageDiplomacySync.updateIdentityMessage(to));
		saveAndSyncIdentity(from);
		saveAndSyncIdentity(to);
	}

	// In accept/deny of an agreement (when the target faction accepts/denies a proposal):
	public static void acceptAgreement(OwnerIdentity acceptingFaction, DiplomaticAgreement proposal)
	{
		if(!proposal.isPending()) return;
		proposal.accept();
		// Remove from pending lists
		acceptingFaction.removeAgreement(proposal);
		OwnerIdentity sourceFaction = DiplomacyUtils.getIdentityByUUID(proposal.getSourceFaction());
		if(sourceFaction!=DiplomacyUtils.NEUTRAL)
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
					prop.master().setOwnerIdentity(sourceFaction); // whichever direction
			}
		}
		IISaveData.setDirty();
		IIPacketHandler.sendToAllClients(MessageDiplomacySync.updateIdentityMessage(sourceFaction));
		IIPacketHandler.sendToAllClients(MessageDiplomacySync.updateIdentityMessage(acceptingFaction));
	}

	public static OwnerIdentity merge(OwnerIdentity a, OwnerIdentity b)
	{
		//Merge two identities
		OwnerIdentity merged = new OwnerIdentity(a, b);
		OWNER_IDENTITIES.remove(a.getUUID());
		OWNER_IDENTITIES.remove(b.getUUID());
		OWNER_IDENTITIES.put(merged.getUUID(), merged);

		//Save and update clients
		IISaveData.setDirty();
		IIPacketHandler.sendToAllClients(MessageDiplomacySync.removeIdentityMessage(a));
		IIPacketHandler.sendToAllClients(MessageDiplomacySync.removeIdentityMessage(b));
		IIPacketHandler.sendToAllClients(MessageDiplomacySync.updateIdentityMessage(merged));
		return merged;
	}

	public static OwnerIdentity[] split(OwnerIdentity identity, EntityLivingBase... between)
	{
		//Save and update clients
		saveAndSyncIdentity(identity);
		return new OwnerIdentity[]{identity};
	}

	//--- Player Invitation ---//

	public static Set<String> getPendingInvitationsForPlayer(UUID playerUUID)
	{
		return OWNER_IDENTITIES.values().stream()
				.filter(oi -> oi.isInvited(playerUUID))
				.map(OwnerIdentity::getDisplayName)
				.collect(Collectors.toSet());
	}

	public static Set<UUID> getPendingInvitationsForFaction(UUID factionUUID)
	{
		OwnerIdentity faction = getIdentityByUUID(factionUUID);
		return faction!=NEUTRAL?faction.getInvitedPlayers(): Collections.emptySet();
	}

	public static boolean acceptInvitation(OwnerIdentity identity, UUID playerUUID)
	{
		if(identity.isInvited(playerUUID))
		{
			//Add to new identity
			identity.removeInvitation(playerUUID);
			identity.withMember(playerUUID, identity.getStartingMemberRole(), true);

			//Remove from old identity
			OWNER_IDENTITIES.values().stream()
					.filter(oi -> oi!=identity)
					.filter(oi -> oi.isMember(playerUUID))
					.forEach(faction -> {
						//Remove player from old identity
						faction.removeMember(playerUUID, true);
					});


			return true;
		}
		return false;
	}

	public static boolean denyInvitation(OwnerIdentity identity, UUID playerUUID)
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

	public static void saveAndSyncIdentity(OwnerIdentity identity)
	{
		IISaveData.setDirty();
		IIPacketHandler.sendToAllClients(MessageDiplomacySync.updateIdentityMessage(identity));
	}

	//--- Client Sync Methods ---//

	public static void clientRemoveIdentity(UUID uuid)
	{
		OWNER_IDENTITIES.remove(uuid);
	}

	public static void clientUpdateIdentity(UUID uuid, EasyNBT tagCompound)
	{
		OwnerIdentity identity = getIdentityByUUID(uuid);
		if(identity!=NEUTRAL)
			identity.loadFromNBT(tagCompound);
		else
		{
			OwnerIdentity updated = new OwnerIdentity(tagCompound);
			OWNER_IDENTITIES.put(uuid, updated);
		}
	}
}
