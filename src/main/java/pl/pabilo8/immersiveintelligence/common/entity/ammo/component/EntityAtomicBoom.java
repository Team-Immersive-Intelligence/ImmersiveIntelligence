package pl.pabilo8.immersiveintelligence.common.entity.ammo.component;

import com.elytradev.mirage.event.GatherLightsEvent;
import com.elytradev.mirage.lighting.IEntityLightEventConsumer;
import com.elytradev.mirage.lighting.Light;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.Optional.Interface;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.protection.protection.RadiationHandler;
import pl.pabilo8.immersiveintelligence.api.protection.protection.capability.IRadiationEmitter;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIPotions;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 19.12.2020
 */
@Interface(iface = "com.elytradev.mirage.lighting.IEntityLightEventConsumer", modid = "mirage")
public class EntityAtomicBoom extends Entity implements IEntityAdditionalSpawnData, IEntityLightEventConsumer, IRadiationEmitter
{
	public float size = 0;
	public int progress = 0;
	private boolean falloutRegistered = false;

	public EntityAtomicBoom(World world)
	{
		super(world);
	}

	public EntityAtomicBoom(World world, float size)
	{
		this(world);
		this.size = size;
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		if(world.isRemote)
			return;

		if(!falloutRegistered)
		{
			//Create radioactive zone
			RadiationHandler.INSTANCE.addOrIncreaseRadiationCenter(world, getPosition(), 72*size, Math.max(1f, size));
			falloutRegistered = true;
		}

		if(!world.isRemote)
		{
			//Apply nuclear heat server-side; radiation exposure is handled centrally.
			if(progress%10==0)
			{
				AxisAlignedBB aabb = new AxisAlignedBB(getPosition()).grow(40*size);
				for(EntityLivingBase entity : world.getEntitiesWithinAABB(EntityLivingBase.class, aabb))
					if(!entity.isPotionActive(IIPotions.nuclearHeat)) //Do not apply twice to not break the visual effect
						entity.addPotionEffect(new PotionEffect(IIPotions.nuclearHeat, 400, 0, false, false));
			}
			//Destroy / burn blocks with nuclear heat
			if(progress > 20&&progress < 60)
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
		}

		progress++;

		if(!world.isRemote&&progress > 400)
			setDead();
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
		progress = compound.getInteger("progress");
		falloutRegistered = compound.getBoolean("falloutRegistered");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setFloat("size", size);
		compound.setInteger("progress", progress);
		compound.setBoolean("falloutRegistered", falloutRegistered);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean isInRangeToRenderDist(double distance)
	{
		return true;
	}

	//--- IRadiationEmitter ---//

	@Override
	public float getRadiationRadius()
	{
		return 60*size;
	}

	@Override
	public float getRadiationStrength()
	{
		return Math.max(1f, size*4f);
	}

	@Override
	public boolean isRadiationActive()
	{
		return !isDead&&progress <= 400;
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
