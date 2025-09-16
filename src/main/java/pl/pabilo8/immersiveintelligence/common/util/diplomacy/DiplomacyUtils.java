package pl.pabilo8.immersiveintelligence.common.util.diplomacy;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.IISaveData;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageDiplomacySync;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 03.09.2025
 */
public class DiplomacyUtils
{
	private static final HashMap<String, OwnerIdentity> OWNER_IDENTITIES = new HashMap<>();
	private static final HashMap<UUID, IOwnableProperty> PROPERTIES = new HashMap<>();

	public static OwnerIdentity NEUTRAL, GLOBAL_ENEMY;
	public static boolean diplomacyInitialized = false;

	private static final String KEY_IDENTITIES = "identities";

	//--- NBT ---//

	public static void loadAllFromNBT(EasyNBT nbt)
	{
		diplomacyInitialized = true;
		//Create the neutral faction
		if(NEUTRAL==null)
			NEUTRAL = new OwnerIdentity("Neutral")
					.withMember("[ImmersiveEngineering]", true)
					.withLawForm(LawForm.COMMUNE)
					.withColor(IIColor.MC_GRAY)
					.withPermission(PermissionCategory.CONTAINER_ACCESS, PermissionLevel.OTHERS_ALLOW)
					.withPermission(PermissionCategory.MILITARY_AID, PermissionLevel.OTHERS_ALLOW)
					.withPermission(PermissionCategory.LOGISTICS, PermissionLevel.OTHERS_ALLOW)
					.withPermission(PermissionCategory.TRADE, PermissionLevel.OTHERS_ALLOW);
		//Create the global enemy faction
		if(GLOBAL_ENEMY==null)
			GLOBAL_ENEMY = new OwnerIdentity("GlobalEnemy")
					.withMember("[ImmersiveEngineering]", true)
					.withColor(IIColor.MC_BLACK)
					.withLawForm(LawForm.COMMISARIAT)
					.withAllPermissions(PermissionLevel.OWNER_ALLOW)
					.withPermission(PermissionCategory.CONTAINER_ACCESS, PermissionLevel.MEMBER_ALLOW);

		//Load factions from NBT
		nbt.streamList(NBTTagCompound.class, KEY_IDENTITIES)
				.map(OwnerIdentity::new)
				.distinct()
				.forEach(id -> OWNER_IDENTITIES.compute(id.getDisplayName(),
						//Merge with existing placeholder identity if present
						(oid, identity) -> {
							if(identity==null)
								return id;
							identity.loadFromNBT(id.toNBT());
							return identity;
						}));

		//Remove identities that are invalid
		OWNER_IDENTITIES.values().removeIf(OwnerIdentity::isInvalid);
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
	}

	public static void validateProperty(IOwnableProperty property)
	{
		UUID uuid = property.getUUID();
		IILogger.debug("Validating IOwnableProperty: "+uuid);
		PROPERTIES.put(uuid, property);
	}

	public static void invalidateProperty(IOwnableProperty property)
	{
		UUID uuid = property.getUUID();
		IILogger.debug("Invalidating IOwnableProperty: "+uuid);
		PROPERTIES.remove(uuid);
	}

	//--- Getters ---//

	@Nonnull
	public static OwnerIdentity getIdentityByName(String name)
	{
		//Return a placeholder identity
		if(!diplomacyInitialized)
			return OWNER_IDENTITIES.computeIfAbsent(name, OwnerIdentity::new);
		//Get an existing identity
		return OWNER_IDENTITIES.getOrDefault(name, NEUTRAL);
	}

	@Nonnull
	public static OwnerIdentity getOwnerIdentityForEntity(EntityLivingBase player)
	{
		//Try to get an existing identity
		for(OwnerIdentity identity : OWNER_IDENTITIES.values())
			if(identity.isMember(player))
				return identity;

		if(!player.world.isRemote)
		{
			//Create new identity
			OwnerIdentity identity = new OwnerIdentity(player);
			OWNER_IDENTITIES.put(identity.getDisplayName(), identity);
			//Save and update clients
			IISaveData.setDirty(0);
			IIPacketHandler.sendToAllClients(MessageDiplomacySync.updateIdentityMessage(identity));

			return identity;
		}
		return NEUTRAL;
	}

	public static IOwnableProperty getPropertyByUUID(UUID uuid)
	{
		return PROPERTIES.get(uuid);
	}

	//--- Utilities ---//

	public static void claimProperty(OwnerIdentity identity, IOwnableProperty property)
	{
		//Set new property owner
		property.master().setOwnerIdentity(identity);

		//Save and update clients
		IISaveData.setDirty(0);
		IIPacketHandler.sendToAllClients(MessageDiplomacySync.updateIdentityMessage(identity));
	}

	public static OwnerIdentity merge(OwnerIdentity a, OwnerIdentity b)
	{
		//Merge two identities
		OwnerIdentity merged = new OwnerIdentity(a, b);
		OWNER_IDENTITIES.remove(a.getDisplayName());
		OWNER_IDENTITIES.remove(b.getDisplayName());
		OWNER_IDENTITIES.put(merged.getDisplayName(), merged);

		//Save and update clients
		IISaveData.setDirty(0);
		IIPacketHandler.sendToAllClients(MessageDiplomacySync.removeIdentityMessage(a));
		IIPacketHandler.sendToAllClients(MessageDiplomacySync.removeIdentityMessage(b));
		IIPacketHandler.sendToAllClients(MessageDiplomacySync.updateIdentityMessage(merged));
		return merged;
	}

	public static OwnerIdentity[] split(OwnerIdentity identity, EntityLivingBase... betweem)
	{
		//Save and update clients
		IISaveData.setDirty(0);
		IIPacketHandler.sendToAllClients(MessageDiplomacySync.updateIdentityMessage(identity));
		return new OwnerIdentity[]{identity};
	}

	//--- Client Sync Methods ---//

	public static void clientRemoveIdentity(String identityName)
	{
		OWNER_IDENTITIES.remove(identityName);
	}

	public static void clientUpdateIdentity(String identityName, EasyNBT tagCompound)
	{
		OwnerIdentity identity = getIdentityByName(identityName);
		if(identity!=NEUTRAL)
			identity.loadFromNBT(tagCompound);
		else
		{
			OwnerIdentity updated = new OwnerIdentity(tagCompound.unwrap());
			OWNER_IDENTITIES.put(identityName, updated);
		}
	}
}
