package pl.pabilo8.immersiveintelligence.common.entity.ammo.component;

import blusunrize.immersiveengineering.common.entities.EntityIEProjectile;
import com.elytradev.mirage.event.GatherLightsEvent;
import com.elytradev.mirage.lighting.IEntityLightEventConsumer;
import com.elytradev.mirage.lighting.Light;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.Optional.Interface;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ShrapnelHandler.Shrapnel;
import pl.pabilo8.immersiveintelligence.api.data.radio.RadioNetwork;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleProperties;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleRegistry;
import pl.pabilo8.immersiveintelligence.common.util.entity.IIEntityUtils;

import javax.annotation.Nullable;
import javax.vecmath.Vector2f;

import static pl.pabilo8.immersiveintelligence.common.util.IIDamageSources.causeShrapnelDamage;

/**
 * Simulates a {@link Shrapnel} fragment, applies damage and other effects on impacts.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 06.08.2026
 * @ii-approved 0.3.1
 * @since 26.10.2019
 */
@Interface(iface = "com.elytradev.mirage.lighting.IEntityLightEventConsumer", modid = "mirage")
public class EntityShrapnel extends EntityIEProjectile implements IEntityLightEventConsumer, IEntityAdditionalSpawnData
{
	private static final double FULL_DAMAGE_DISTANCE = 2d;
	private static final float MINIMUM_DAMAGE_FACTOR = 0.1f;
	private static final int NORMAL_LIFETIME = 40;
	private static final int SLOW_FALL_LIFETIME = 600;
	private static final int RADIO_DISRUPTION_INTERVAL = 10;
	private static final int RADIO_DISRUPTION_DURATION = 20;
	private static final float RADIO_DISRUPTION_RADIUS = 4f;

	@Nullable
	public Shrapnel shrapnel;
	private double travelledDistance;
	private double lastDistanceX, lastDistanceY, lastDistanceZ;
	private boolean distancePositionInitialized;

	public EntityShrapnel(World world)
	{
		super(world);
		this.setSize(0.25F, 0.25F);
	}

	public EntityShrapnel(World world, double x, double y, double z, double motionX, double motionY, double motionZ,
	                      @Nullable Shrapnel shrapnel)
	{
		super(world, x, y, z, motionX, motionY, motionZ);
		this.shrapnel = shrapnel;
		this.setSize(0.25F, 0.25F);
		resetDistancePosition();
		configureLifetime();
	}

	@Override
	public double getGravity()
	{
		if(shrapnel!=null)
			return shrapnel.fallsSlowly?0.0025d: shrapnel.mass*0.05d;
		return super.getGravity();
	}

	@Override
	protected float getMotionDecayFactor()
	{
		return shrapnel!=null&&shrapnel.fallsSlowly?0.96f: super.getMotionDecayFactor();
	}

	@Override
	public int getMaxTicksInGround()
	{
		return shrapnel!=null&&shrapnel.fallsSlowly?1: super.getMaxTicksInGround();
	}

	@Override
	public boolean canIgnite()
	{
		return shrapnel!=null&&shrapnel.flammable;
	}

	@Override
	public void onEntityUpdate()
	{
		updateTravelledDistance();

		IBlockState state = world.getBlockState(new BlockPos(posX, posY, posZ));
		Block block = state.getBlock();
		if(block!=null&&canIgnite()&&(state.getMaterial()==Material.FIRE||state.getMaterial()==Material.LAVA))
			setFire(6);

		if(shrapnel!=null)
		{
			if(world.isRemote)
			{
				if(shrapnel.fallsSlowly)
				{
					if(ticksExisted%4==0)
						ParticleRegistry.spawnParticle("shrapnel/glitter", getPositionVector(),
										motionY > 0?IIEntityUtils.getEntityMotion(this): Vec3d.ZERO, new Vector2f(0, 0))
								.withProperty(ParticleProperties.COLOR, shrapnel.color);
				}
				else if(!inGround&&ticksExisted%4==0)
					ParticleRegistry.spawnParticle("shrapnel/burst", getPositionVector(),
									IIEntityUtils.getEntityMotion(this), new Vector2f(0, 0))
							.withProperty(ParticleProperties.COLOR, shrapnel.color);
			}
			else if(shrapnel.isDisruptsRadio()&&ticksExisted%RADIO_DISRUPTION_INTERVAL==0)
				RadioNetwork.INSTANCE.disruptDevices(world, getPositionVector(), RADIO_DISRUPTION_RADIUS, RADIO_DISRUPTION_DURATION);
		}


		super.onEntityUpdate();
	}

	@Override
	public void setFire(int seconds)
	{
		if(canIgnite())
			super.setFire(seconds);
	}

	@Override
	public void onImpact(RayTraceResult result)
	{
		if(shrapnel==null)
			return;
		if(result.entityHit==null)
		{
			if(shrapnel.fallsSlowly)
				setDead();
			return;
		}
		if(world.isRemote)
			return;

		Entity target = result.entityHit;
		float distanceFactor = MINIMUM_DAMAGE_FACTOR+(1f-MINIMUM_DAMAGE_FACTOR)*
				MathHelper.clamp((float)(getDistanceAtImpact(result)/FULL_DAMAGE_DISTANCE), 0f, 1f);
		float damage = shrapnel.damage*distanceFactor;

		if(shrapnel.goodVsUndead&&target instanceof EntityLivingBase&&
				((EntityLivingBase)target).getCreatureAttribute()==EnumCreatureAttribute.UNDEAD)
			damage *= 2f;

		boolean damaged = target.attackEntityFrom(causeShrapnelDamage(this, shootingEntity, target), damage);
		if(damaged)
			target.hurtResistantTime *= .5;
		if(shrapnel.potion!=null&&target instanceof EntityLivingBase)
			((EntityLivingBase)target).addPotionEffect(new PotionEffect(shrapnel.potion));

		if(isBurning())
		{
			target.setFire(10);
			if(target.attackEntityFrom(DamageSource.IN_FIRE, 2))
				target.hurtResistantTime *= .75;
		}
	}

	@Override
	protected boolean allowFriendlyFire(EntityPlayer target)
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	@Optional.Method(modid = "mirage")
	public void gatherLights(GatherLightsEvent event, Entity entity)
	{
		if(shrapnel==null)
			return;
		int light = isBurning()?15: Math.round(shrapnel.brightness*15f);
		if(light > 0)
			event.add(Light.builder()
					.pos(this)
					.color(1, 1, 1)
					.radius(.05f)
					.build());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender()
	{
		if(shrapnel!=null)
		{
			int light = isBurning()?15: Math.round(shrapnel.brightness*15f);
			int superBrightness = super.getBrightnessForRender();
			light = (superBrightness&(0xff<<20))|(light<<4);
			if(light > 0)
				return Math.max(light, superBrightness);
		}
		return super.getBrightnessForRender();
	}

	@Override
	public float getBrightness()
	{
		if(shrapnel!=null)
		{
			int light = isBurning()?15: Math.round(shrapnel.brightness*15f);
			if(light > 0)
				return Math.max(light, super.getBrightness());
		}
		return super.getBrightness();
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tag)
	{
		super.writeEntityToNBT(tag);
		if(shrapnel!=null)
			tag.setString("shrapnel", shrapnel.name);
		tag.setDouble("travelledDistance", travelledDistance);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tag)
	{
		super.readEntityFromNBT(tag);
		this.shrapnel = pl.pabilo8.immersiveintelligence.api.ShrapnelHandler.registry.get(tag.getString("shrapnel"));
		this.travelledDistance = tag.getDouble("travelledDistance");
		resetDistancePosition();
		configureLifetime();
	}

	@Override
	public void writeSpawnData(ByteBuf data)
	{
		ByteBufUtils.writeUTF8String(data, shrapnel==null?"": shrapnel.name);
	}

	@Override
	public void readSpawnData(ByteBuf data)
	{
		shrapnel = pl.pabilo8.immersiveintelligence.api.ShrapnelHandler.registry.get(ByteBufUtils.readUTF8String(data));
		resetDistancePosition();
		configureLifetime();
	}

	private void configureLifetime()
	{
		setTickLimit(shrapnel!=null&&shrapnel.fallsSlowly?SLOW_FALL_LIFETIME: NORMAL_LIFETIME);
	}

	private void resetDistancePosition()
	{
		lastDistanceX = posX;
		lastDistanceY = posY;
		lastDistanceZ = posZ;
		distancePositionInitialized = true;
	}

	private void updateTravelledDistance()
	{
		if(!distancePositionInitialized)
		{
			resetDistancePosition();
			return;
		}
		double x = posX-lastDistanceX;
		double y = posY-lastDistanceY;
		double z = posZ-lastDistanceZ;
		travelledDistance += Math.sqrt(x*x+y*y+z*z);
		lastDistanceX = posX;
		lastDistanceY = posY;
		lastDistanceZ = posZ;
	}

	private double getDistanceAtImpact(RayTraceResult result)
	{
		Vec3d current = getPositionVector();
		Vec3d hit = result.hitVec==null?current.addVector(motionX, motionY, motionZ): result.hitVec;
		double motionLength = Math.sqrt(motionX*motionX+motionY*motionY+motionZ*motionZ);
		return travelledDistance+Math.min(current.distanceTo(hit), motionLength);
	}
}
