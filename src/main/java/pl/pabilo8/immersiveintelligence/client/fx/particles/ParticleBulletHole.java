package pl.pabilo8.immersiveintelligence.client.fx.particles;

import blusunrize.immersiveengineering.api.DimensionBlockPos;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.client.fx.IIParticle;
import pl.pabilo8.immersiveintelligence.client.fx.ParticleRenderer.DrawingStages;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 31.12.2022
 */
public class ParticleBulletHole extends IIParticle
{
	private final BlockPos pos;
	private final EnumFacing facing;

	public ParticleBulletHole(World world, DimensionBlockPos dPos, EnumFacing facing, Vec3d dir)
	{
		super(world, dir.add(new Vec3d(facing.getFrontOffsetX(), facing.getFrontOffsetY(), facing.getFrontOffsetZ()).scale(0.012)), Vec3d.ZERO);
		this.pos = new BlockPos(dPos);
		this.facing = facing;

		this.particleRed = 0.05F;
		this.particleGreen = 0.05F;
		this.particleBlue = 0.05F;
		this.particleAlpha = 1f;
		this.particleMaxAge = 280;
		this.particleScale *= 0.9F;
		this.setSize(0.1F, 0.1F);
		this.setParticleTextureIndex(0);
	}

	public void onUpdate()
	{
		this.motionX = 0;
		this.motionY = 0;
		this.motionZ = 0;
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		if(this.world.isAirBlock(pos))
		{
			this.setExpired();
		}
	}

	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
	{
		switch(this.facing)
		{
			case UP:
				super.renderParticle(buffer, entityIn, partialTicks, 1, 0, 0, 0, 1);
				break;
			case DOWN:
				super.renderParticle(buffer, entityIn, partialTicks, 1, 0, 0, 0, -1);
				break;
			case NORTH:
				super.renderParticle(buffer, entityIn, partialTicks, 1, 1, 0, 0, 0);
				break;
			case SOUTH:
				super.renderParticle(buffer, entityIn, partialTicks, -1, 1, 0, 0, 0);
				break;
			case EAST:
				super.renderParticle(buffer, entityIn, partialTicks, 0, 1, 1, 0, 0);
				break;
			case WEST:
				super.renderParticle(buffer, entityIn, partialTicks, 0, 1, -1, 0, 0);
				break;
		}
	}

	@Nonnull
	@Override
	public DrawingStages getDrawStage()
	{
		return DrawingStages.NORMAL;
	}
}