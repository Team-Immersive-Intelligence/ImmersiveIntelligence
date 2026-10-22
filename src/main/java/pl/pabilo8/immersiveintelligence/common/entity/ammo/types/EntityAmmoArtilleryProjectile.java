package pl.pabilo8.immersiveintelligence.common.entity.ammo.types;

import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;

/**
 * Keeps an artillery shell loaded and plays its approach sound.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 06.10.2026
 * @ii-approved 0.3.1
 * @since 02.02.2024
 */
public class EntityAmmoArtilleryProjectile extends EntityAmmoChunkLoadingProjectile
{
	//--- Properties ---//

	/**
	 * Whether this artillery shell played its flyby sound
	 */
	boolean flybySound = false;

	//--- Constructor ---//

	public EntityAmmoArtilleryProjectile(World world)
	{
		super(world);
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		if(isDead||(world.isRemote&&!clientLoaded))
			return;

		//Play artillery shell impact sound
		if(Weapons.artilleryImpactSound&&!flybySound&&ticksExisted > 5)
		{
			if(motionY < 0)
			{
				BlockPos top = world.getTopSolidOrLiquidBlock(new BlockPos(getNextPositionVector())).up();
				IIPacketHandler.playRangedSound(world, new Vec3d(top), IISounds.artilleryImpact, SoundCategory.PLAYERS, 24, 0.75f, 1.3f);
				flybySound = true;
			}
		}
	}

	@Override
	protected boolean shouldDecay()
	{
		return posY < 0;
	}
}
