package pl.pabilo8.immersiveintelligence.common.entity.ammo.component;

import blusunrize.immersiveengineering.common.entities.EntityIEProjectile;
import blusunrize.immersiveengineering.common.util.IEPotions;
import blusunrize.immersiveengineering.common.util.Utils;
import com.elytradev.mirage.event.GatherLightsEvent;
import com.elytradev.mirage.lighting.ILightEventConsumer;
import com.elytradev.mirage.lighting.Light;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleRegistry;
import pl.pabilo8.immersiveintelligence.common.IIPotions;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.util.entity.IIEntityUtils;

/**
 * @author Pabilo8
 * @since 26-10-2019
 */
@net.minecraftforge.fml.common.Optional.Interface(iface = "com.elytradev.mirage.lighting.ILightEventConsumer", modid = "mirage")
public class EntityWhitePhosphorus extends EntityIEProjectile implements ILightEventConsumer
{
	public EntityWhitePhosphorus(World world)
	{
		super(world);
	}

	public EntityWhitePhosphorus(World world, double x, double y, double z, double ax, double ay, double az)
	{
		super(world, x, y, z, ax, ay, az);
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
	}

	@Override
	public double getGravity()
	{
		return 0.07F;
	}

	@Override
	public boolean canIgnite()
	{
		return false;
	}

	/**
	 * Gets called every tick from main Entity class
	 */
	@Override
	public void onEntityUpdate()
	{
		super.onEntityUpdate();
		if(getEntityWorld().isRemote)
			spawnTracerParticles();
	}

	@SideOnly(Side.CLIENT)
	private void spawnTracerParticles()
	{
		ParticleRegistry.spawnTracerFX(getPositionVector(), IIEntityUtils.getEntityMotion(this), 0.2f, 0xffffff);
	}

	/**
	 * Sets entity to burn for x scatter of seconds, cannot lower scatter of existing fire.
	 */
	@Override
	public void setFire(int seconds)
	{
		super.setFire(seconds);
	}

	@Override
	public void onImpact(RayTraceResult mop)
	{
		if(!this.world.isRemote&&mop.typeOfHit!=Type.MISS&&world!=null&&mop.getBlockPos()!=null)
		{
			Explosion explosion = new Explosion(world, this, posX, posY, posZ, 1, true, false);
			explosion.doExplosionA();
			explosion.doExplosionB(false);

			world.playSound(null, mop.getBlockPos(), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.125f, 1f);

			EntityAreaEffectCloud cloud = new EntityAreaEffectCloud(world, mop.getBlockPos().getX(), mop.getBlockPos().getY()+1f, mop.getBlockPos().getZ());
			cloud.addEffect(new PotionEffect(IIPotions.brokenArmor, Math.round(180), 1));
			cloud.addEffect(new PotionEffect(IEPotions.flammable, Math.round(180), 2));
			cloud.addEffect(new PotionEffect(IEPotions.stunned, Math.round(80), 1));
			cloud.addEffect(new PotionEffect(IEPotions.flashed, Math.round(120), 1));
			cloud.setRadius(6f);
			cloud.setDuration(Math.round(30f+(10f*Utils.RAND.nextFloat())));
			cloud.setParticle(EnumParticleTypes.CLOUD);
			world.spawnEntity(cloud);

			world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(getPosition()).grow(0.5f)).forEach(entityLivingBase -> entityLivingBase.setFire(40));

			setDead();
		}
	}

	@Override
	protected boolean allowFriendlyFire(EntityPlayer target)
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender()
	{
		return 15;
	}

	/**
	 * Gets how bright this entity is.
	 */
	@Override
	public float getBrightness()
	{
		return 15;
	}

	@Override
	@SideOnly(Side.CLIENT)
	@Optional.Method(modid = "mirage")
	public void gatherLights(GatherLightsEvent evt)
	{
		evt.add(Light.builder()
				.pos(this)
				.color(1, 1, 1)
				.radius(.05f)
				.build());
	}
}