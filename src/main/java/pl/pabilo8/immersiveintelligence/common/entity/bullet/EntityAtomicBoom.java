package pl.pabilo8.immersiveintelligence.common.entity.bullet;

import com.elytradev.mirage.event.GatherLightsEvent;
import com.elytradev.mirage.lighting.IEntityLightEventConsumer;
import com.elytradev.mirage.lighting.Light;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.fx.ParticleUtils;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIPotions;

/**
 * @author Pabilo8
 * @since 19.12.2020
 */
@net.minecraftforge.fml.common.Optional.Interface(iface = "com.elytradev.mirage.lighting.IEntityLightEventConsumer", modid = "mirage")
public class EntityAtomicBoom extends Entity implements IEntityAdditionalSpawnData, IEntityLightEventConsumer
{
	public float size;
	public int progress = 0;

	public EntityAtomicBoom(World worldIn)
	{
		super(worldIn);
	}

	public EntityAtomicBoom(World worldIn, float size)
	{
		this(worldIn);
		this.size = size;
		this.ignoreFrustumCheck = true;
		setRenderDistanceWeight(32.0);
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		progress++;
		if(world.isRemote&&world.getTotalWorldTime()%4==0)
		{
			Vec3d pos = getPositionVector();
			if(progress < 40)
			{
				ParticleUtils.spawnShockwave(pos.addVector(0, 1.5*size, 0), 20f, 2.5f);
			}
			if(progress > 10&&progress < 360)
			{
				//ParticleUtils.spawnFog(posX, posY+(0.5*size), posZ, 12f, 0.85f, -0.125f);

				if(progress < 30)
					ParticleUtils.spawnAtomicBoomCore(this, pos.addVector(0, 0.5*size, 0), 10f, 0, 0.5f);
			}
			if(progress > 20&&progress < 340)
			{

				if(progress < 320)
					ParticleUtils.spawnAtomicBoomCore(this, pos.addVector(0, 4+(3.5*size), 0), 20f, 0.05f, -0.25f);
				ParticleUtils.spawnAtomicBoomCore(this, pos.addVector(0, 4+(1.5*size), 0), 10f, 0, 0.5f);
			}
			if(progress > 20&&progress < 320)
			{
				//ParticleUtils.spawnFog(posX, posY+(0.5*size), posZ, 25f, 1, -0.25f);
				ParticleUtils.spawnAtomicBoomCore(this, pos.addVector(0, 4+(1.5*size), 0), 20f, -0.01f, 0.25f);
				if(progress < 35)
					ParticleUtils.spawnAtomicBoomCore(this, pos.addVector(0, 4+(3.5*size), 0), 10f, 0, 0.5f);
			}
			if(progress > 25&&progress < 300)
			{
				//ParticleUtils.spawnFog(posX, posY+(6.5*size), posZ, 20f, 0.1f, -0.25f);
				ParticleUtils.spawnAtomicBoomCore(this, pos.addVector(0, 4+(6.5*size), 0), 20f, -0.01f, 0.25f);
				if(progress < 40)
					ParticleUtils.spawnAtomicBoomCore(this, pos.addVector(0, 4+(6.5*size), 0), 10f, 0, 0.5f);
			}
			if(progress > 30&&progress < 280)
			{
				ParticleUtils.spawnAtomicBoomCore(this, pos.addVector(0, 4+(12.5*size), 0), 15f, -0.01f, 0.125f);
			}
			if(progress > 40&&progress < 280)
			{
				ParticleUtils.spawnAtomicBoomRing(this, pos.addVector(0, 4+(18.5*size), 0), 25f, 0.25f, -0.05f);
			}
			/*
			if(progress > 20)
				ParticleUtils.spawnFog(posX, posY+(0.5*size), posZ, 20f, 0.5f, -0.25f);
			 */
			return;
		}
		else if(!world.isRemote&&progress > 20&&progress < 60)
		{
			final int border = (int)((8*size*16)/40);
			final int prog = (progress-20)*border;
			final int heightDiff = (int)(70*size);
			BlockPos position = getPosition();

			for(int hh = position.getY()-heightDiff; hh <= position.getY()+heightDiff; hh++)
			{
				for(int x = position.getX()-prog; x <= position.getX()+prog; x++)
				{
					for(int z = position.getZ()-prog; z <= position.getZ()-prog+border; z++)
						destroyFoliage(x, hh, z, EnumFacing.SOUTH);
					for(int z = position.getZ()+prog-border; z <= position.getZ()+prog; z++)
						destroyFoliage(x, hh, z, EnumFacing.NORTH);
				}

				for(int z = position.getZ()-prog; z <= position.getZ()+prog; z++)
				{
					for(int x = position.getX()-prog; x <= position.getX()-prog+border; x++)
						destroyFoliage(x, hh, z, EnumFacing.EAST);
					for(int x = position.getX()+prog-border; x <= position.getX()+prog; x++)
						destroyFoliage(x, hh, z, EnumFacing.WEST);
				}

			}
		}

		if(!world.isRemote&&progress > 400)
		{
			setDead();
			return;
		}

		//apply half a second
		if(world.getTotalWorldTime()%10==0)
		{
			AxisAlignedBB aabb = new AxisAlignedBB(getPosition()).grow(40*size);
			EntityLivingBase[] entities = world.getEntitiesWithinAABB(EntityLivingBase.class, aabb).toArray(new EntityLivingBase[0]);
			for(EntityLivingBase e : entities)
			{
				//if(e instanceof EntityPlayer&&((EntityPlayer)e).isCreative())
				//	continue;
				e.addPotionEffect(new PotionEffect(IIPotions.nuclearHeat, 400, 0, false, false));
			}
			entities = world.getEntitiesWithinAABB(EntityLivingBase.class, aabb.grow(20*size)).toArray(new EntityLivingBase[0]);
			for(EntityLivingBase e : entities)
			{
				if(e instanceof EntityPlayer&&((EntityPlayer)e).isCreative())
					continue;
				e.addPotionEffect(new PotionEffect(IIPotions.radiation, 2000, 0, false, false));
			}
		}
	}

	private void destroyFoliage(int x, int y, int z, EnumFacing facing)
	{
		BlockPos pp = new BlockPos(x, y, z);
		if(!world.isOutsideBuildHeight(pp))
		{
			IBlockState state = world.getBlockState(pp);
			Material material = state.getMaterial();
			boolean b = material==Material.WOOD||
					material==Material.CLOTH||
					material==Material.CARPET||
					material==Material.CACTUS||
					material==Material.CORAL||
					material==Material.GLASS||
					material==Material.PLANTS||
					material==Material.SNOW||
					material==Material.CRAFTED_SNOW||
					material==Material.LEAVES||
					material==Material.WEB||
					material==Material.CAKE||
					material==Material.VINE||
					material==Material.CIRCUITS||
					material==Material.PACKED_ICE||
					material==Material.ICE;


			if(progress < 38)
			{
				if(b)
					world.setBlockToAir(pp);
			}
			else
			{
				if(progress < 52&&material==Material.WOOD&&state.getPropertyKeys().contains(BlockLog.LOG_AXIS))
					world.setBlockState(pp, IIContent.blockCharredLog.getDefaultState().withProperty(BlockLog.LOG_AXIS, state.getValue(BlockLog.LOG_AXIS)));
				else if(b)
					world.setBlockToAir(pp);

				if(material==Material.PLANTS||material==Material.SNOW||material==Material.CRAFTED_SNOW||material==Material.LEAVES||material==Material.WEB||material==Material.CAKE||material==Material.VINE||material==Material.CIRCUITS||material==Material.PACKED_ICE||material==Material.ICE)
					world.setBlockToAir(pp);
			}


		}
	}

	@Override
	public float getBrightness()
	{
		return 15;
	}

	@Override
	public int getBrightnessForRender()
	{
		return super.getBrightnessForRender();
	}

	@Override
	protected void entityInit()
	{

	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound)
	{
		size = compound.getFloat("size");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setFloat("size", size);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean isInRangeToRenderDist(double distance)
	{
		return true;
	}

	@Override
	public void writeSpawnData(ByteBuf buffer)
	{
		buffer.writeFloat(size);
		buffer.writeInt(progress);
	}

	@Override
	public void readSpawnData(ByteBuf buffer)
	{
		size = buffer.readFloat();
		progress = buffer.readInt();
	}

	@Override
	@SideOnly(Side.CLIENT)
	@Optional.Method(modid = "mirage")
	public void gatherLights(GatherLightsEvent gatherLightsEvent, Entity entity)
	{
		gatherLightsEvent.add(Light.builder().pos(this).radius(32*size).color(1, 1, 1).build());
	}
}
