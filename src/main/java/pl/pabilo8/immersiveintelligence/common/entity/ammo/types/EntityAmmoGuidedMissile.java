package pl.pabilo8.immersiveintelligence.common.entity.ammo.types;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleProperties;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleRegistry;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 02.02.2024
 */
public class EntityAmmoGuidedMissile extends EntityAmmoMissile
{
	public EntityAmmoGuidedMissile(World world)
	{
		super(world);
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void spawnTrailParticles()
	{
		super.spawnTrailParticles();
		ParticleRegistry.spawnParticle("ammo/missile_wire", getPositionVector(), Vec3d.ZERO, new Vector2f())
				.withProperty(ParticleProperties.STRETCH, new Vector3f((float)prevPosX, (float)prevPosY, (float)prevPosZ));
	}
}
