package pl.pabilo8.immersiveintelligence.common.entity.ammo.component;

import blusunrize.immersiveengineering.common.entities.EntityIEProjectile;
import com.elytradev.mirage.event.GatherLightsEvent;
import com.elytradev.mirage.lighting.ILightEventConsumer;
import com.elytradev.mirage.lighting.Light;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.Optional.Interface;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.fx.utils.IIParticleUtils;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleProperties;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleRegistry;
import pl.pabilo8.immersiveintelligence.common.util.entity.IIEntityUtils;

import javax.vecmath.Vector2f;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 26.10.2019
 */
@Interface(iface = "com.elytradev.mirage.lighting.ILightEventConsumer", modid = "mirage")
public class EntityWhitePhosphorus extends EntityIEProjectile implements ILightEventConsumer
{
	public EntityWhitePhosphorus(World world)
	{
		super(world);
		setTickLimit(8);
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
		return 0.004F;
	}

	/**
	 * Gets called every tick from main Entity class
	 */
	@Override
	public void onEntityUpdate()
	{
		super.onEntityUpdate();
		if(world.isRemote)
			spawnTracerParticles();
	}

	@SideOnly(Side.CLIENT)
	private void spawnTracerParticles()
	{
		ParticleRegistry.spawnParticle("phosphorus/ember", getPositionVector(), IIEntityUtils.getEntityMotion(this), new Vector2f(0, 0))
				.withProperty(ParticleProperties.SIZE, 0.25f-(0.07f*(ticksExisted/7f)));
		ParticleRegistry.spawnParticle("phosphorus/smoke_trace", getPositionVector(), Vec3d.ZERO, new Vector2f(0, 0))
				.withProperty(ParticleProperties.SIZE, 0.8f-(0.5f*(ticksExisted/7f)));
		if(ticksExisted < 7)
			for(int i = 0; i < IIParticleUtils.randInt.get()%3; i++)
				ParticleRegistry.spawnParticle("phosphorus/smoke_graceful", getPositionVector()
								.addVector(0, 0.5, 0)
								.add(IIParticleUtils.getRandXZ().scale(0.5f)),
						Vec3d.ZERO, new Vector2f(0, 0));

	}

	/**
	 * Sets entity to burn for x scatter of seconds, cannot lower scatter of existing fire.
	 */
	@Override
	public void setFire(int seconds)
	{

	}

	@Override
	public void onImpact(RayTraceResult mop)
	{
		if(!this.world.isRemote&&mop.typeOfHit!=Type.MISS)
		{
			//Ignore other white phosphorus
			if(mop.typeOfHit==Type.ENTITY&&mop.entityHit instanceof EntityWhitePhosphorus)
				return;
			//Set hit entity to fire
			BlockPos hitPos = new BlockPos(mop.hitVec);
			world.playSound(null, hitPos, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.125f, 1f);
			world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(hitPos).grow(0.5f))
					.forEach(entityLivingBase -> entityLivingBase.setFire(40));
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
