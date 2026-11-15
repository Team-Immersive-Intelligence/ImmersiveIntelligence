package pl.pabilo8.immersiveintelligence.common.item.tools.backpack;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.common.entity.EntityParachute;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;

/**
 * Deploys a rideable parachute for a falling player.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 13.11.2026
 */
@IIItemProperties(category = IICategory.TOOLS)
@GeneratedItemModels(itemName = "parachute_backpack", texturePath = "backpack/parachute_backpack")
public class ItemIIParachutePack extends ItemIIBackpackBase
{
	public ItemIIParachutePack()
	{
		super("parachute_backpack");
	}

	/**
	 * Deploys the parachute on the server if the player can use it.
	 */
	public void deployParachute(EntityPlayer player)
	{
		if(player.world.isRemote||!player.isEntityAlive()||player.isSpectator()||player.onGround||player.isRiding()
				||player.capabilities.isFlying||player.isElytraFlying()||player.isInWater()||player.isInLava()||player.motionY >= 0)
			return;

		EntityParachute parachute = new EntityParachute(player.world);
		//Keep the rider at the same height when the canopy is deployed.
		parachute.setPositionAndRotation(player.posX, player.posY-parachute.getMountedYOffset()-player.getYOffset(), player.posZ,
				player.rotationYaw, 0);
		parachute.motionX = player.motionX;
		parachute.motionZ = player.motionZ;
		if(player.world.spawnEntity(parachute))
		{
			if(player.startRiding(parachute))
				player.fallDistance = 0;
			else
				parachute.setDead();
		}
	}
}
