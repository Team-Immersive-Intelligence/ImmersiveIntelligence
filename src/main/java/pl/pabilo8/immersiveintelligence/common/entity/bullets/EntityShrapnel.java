package pl.pabilo8.immersiveintelligence.common.entity.bullets;

import blusunrize.immersiveengineering.common.entities.EntityIEProjectile;
import elucent.albedo.lighting.ILightProvider;
import elucent.albedo.lighting.Light;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ShrapnelHandler;
import pl.pabilo8.immersiveintelligence.api.ShrapnelHandler.Shrapnel;

import javax.annotation.Nullable;

import static pl.pabilo8.immersiveintelligence.common.IIDamageSources.causeShrapnelDamage;

/**
 * @author Pabilo8
 * @since 26-10-2019
 */
@net.minecraftforge.fml.common.Optional.Interface(iface = "elucent.albedo.lighting.ILightProvider", modid = "albedo")
public class EntityShrapnel extends EntityIEProjectile implements ILightProvider, IEntityAdditionalSpawnData
{
	public String shrapnel;

	public EntityShrapnel(World world)
	{
		super(world);
	}

	public EntityShrapnel(World world, double x, double y, double z, double ax, double ay, double az, String shrapnel)
	{
		super(world, x, y, z, ax, ay, az);
		this.shrapnel = shrapnel;
	}

	public EntityShrapnel(World world, EntityLivingBase living, double ax, double ay, double az, String shrapnel)
	{
		super(world, living, ax, ay, az);
		this.shrapnel = shrapnel;
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
	}

	@Override
	public double getGravity()
	{
		if(ShrapnelHandler.registry.get(shrapnel)!=null)
		{
			return ShrapnelHandler.registry.get(shrapnel).mass*0.05d;
		}
		return super.getGravity();
	}

	@Override
	public boolean canIgnite()
	{
		return (ShrapnelHandler.registry.get(shrapnel)!=null&&ShrapnelHandler.registry.get(shrapnel).flammable);
	}

	/**
	 * Gets called every tick from main Entity class
	 */
	@Override
	public void onEntityUpdate()
	{
		IBlockState state = world.getBlockState(new BlockPos(posX, posY, posZ));
		Block b = state.getBlock();
		if(b!=null&&this.canIgnite()&&(state.getMaterial()==Material.FIRE||state.getMaterial()==Material.LAVA))
			this.setFire(6);
		super.onEntityUpdate();
	}

	/**
	 * Sets entity to burn for x scatter of seconds, cannot lower scatter of existing fire.
	 */
	@Override
	public void setFire(int seconds)
	{
		if(!canIgnite())
			return;
		super.setFire(seconds);
	}

	@Override
	public void onImpact(RayTraceResult mop)
	{
		if(!this.world.isRemote&&ShrapnelHandler.registry.get(shrapnel)!=null)
		{
			Shrapnel s = ShrapnelHandler.registry.get(shrapnel);
			if(mop.entityHit!=null)
			{
				mop.entityHit.attackEntityFrom(causeShrapnelDamage(this, shootingEntity,mop.entityHit), s.damage);
				mop.entityHit.hurtResistantTime *= .5;
				if(this.isBurning())
				{
					mop.entityHit.setFire(10);
					if(mop.entityHit.attackEntityFrom(DamageSource.IN_FIRE, 2))
						mop.entityHit.hurtResistantTime *= .75;
				}
			}
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
		Shrapnel s = ShrapnelHandler.registry.get(shrapnel);
		if(s!=null)
		{
			int light = this.isBurning()?15: Math.round(s.brightness*15f);
			if(light > 0)
				return Light.builder().pos(this).radius(.05f*light).color(1, 1, 1).build();
		}
		return null;
	}


	@Override
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender()
	{
		Shrapnel s = ShrapnelHandler.registry.get(shrapnel);
		if(s!=null)
		{
			int light = this.isBurning()?15: Math.round(s.brightness*15f);
			int superBrightness = super.getBrightnessForRender();
			light = (superBrightness&(0xff<<20))|(light<<4);
			if(light > 0)
				return Math.max(light, superBrightness);
		}
		return super.getBrightnessForRender();
	}

	/**
	 * Gets how bright this entity is.
	 */
	@Override
	public float getBrightness()
	{
		Shrapnel s = ShrapnelHandler.registry.get(shrapnel);
		if(s!=null)
		{
			int light = this.isBurning()?15: Math.round(s.brightness*15f);
			if(light > 0)
				return Math.max(light, super.getBrightness());
		}
		return super.getBrightness();
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tag)
	{
		tag.setString("shrapnel", shrapnel);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tag)
	{
		this.shrapnel = tag.getString("shrapnel");
	}

	@Override
	public void writeSpawnData(ByteBuf data)
	{
		ByteBufUtils.writeUTF8String(data, shrapnel);
	}

	@Override
	public void readSpawnData(ByteBuf data)
	{
		shrapnel = ByteBufUtils.readUTF8String(data);
	}
}