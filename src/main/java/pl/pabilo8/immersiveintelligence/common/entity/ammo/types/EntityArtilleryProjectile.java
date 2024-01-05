package pl.pabilo8.immersiveintelligence.common.entity.ammo.types;

import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;

public class EntityArtilleryProjectile extends EntityProjectile
{
	//--- Properties ---//

	/**
	 * Whether this artillery shell played its flyby sound
	 */
	boolean flybySound = false;
	/**
	 * The chunk ticket for this shell
	 */
	private Ticket ticket = null;

	//--- Constructor ---//

	public EntityArtilleryProjectile(World world)
	{
		super(world);
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();


		//Play artillery shell flyby sound
		if(Weapons.artilleryImpactSound&&!flybySound&&ticksExisted > 5&&bullet.shouldLoadChunks())
		{
			if(motionY < 0)
			{
				BlockPos top = world.getTopSolidOrLiquidBlock(new BlockPos(getNextPositionVector())).up();
				IIPacketHandler.playRangedSound(world, new Vec3d(top), IISounds.artilleryImpact, SoundCategory.PLAYERS, 24, 0.75f, 1.3f);
				flybySound = true;
			}
		}

		if(!world.isRemote)
			ForgeChunkManager.forceChunk(this.ticket, this.world.getChunkFromBlockCoords(this.getPosition()).getPos());
	}

	@Override
	protected boolean shouldDecay()
	{
		return posY < 0;
	}
}
