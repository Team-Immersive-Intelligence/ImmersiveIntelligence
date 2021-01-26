package pl.pabilo8.immersiveintelligence.common.entity.bullets;

import blusunrize.immersiveengineering.common.entities.EntityIEProjectile;
import blusunrize.immersiveengineering.common.util.IEPotions;
import blusunrize.immersiveengineering.common.util.Utils;
import elucent.albedo.lighting.ILightProvider;
import elucent.albedo.lighting.Light;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.fx.ParticleUtils;
import pl.pabilo8.immersiveintelligence.common.IIPotions;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 26-10-2019
 */
@net.minecraftforge.fml.common.Optional.Interface(iface = "elucent.albedo.lighting.ILightProvider", modid = "albedo")
public class EntityWhitePhosphorus extends EntityIEProjectile implements ILightProvider
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
		ParticleUtils.spawnTracerFX(posX, posY, posZ, motionX, motionY, motionZ, 0.2f, 0xffffff);
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
			if(this.world.isAirBlock(mop.getBlockPos()))
				world.setBlockState(mop.getBlockPos(), Blocks.FIRE.getDefaultState());

			world.playSound(null, mop.getBlockPos(), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.125f, 1f);

			EntityAreaEffectCloud cloud = new EntityAreaEffectCloud(world, mop.getBlockPos().getX(), mop.getBlockPos().getY()+1f, mop.getBlockPos().getZ());
			cloud.addEffect(new PotionEffect(IIPotions.broken_armor, Math.round(180), 1));
			cloud.addEffect(new PotionEffect(IEPotions.flammable, Math.round(180), 2));
			cloud.addEffect(new PotionEffect(IEPotions.stunned, Math.round(80), 1));
			cloud.addEffect(new PotionEffect(IEPotions.flashed, Math.round(120), 1));
			cloud.setRadius(1.25f);
			cloud.setDuration(Math.round(20f+(10f*Utils.RAND.nextFloat())));
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

	@Nullable
	@Override
	public Light provideLight()
	{
		return Light.builder().pos(this).radius(.05f).color(1, 1, 1).build();
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
}