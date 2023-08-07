package pl.pabilo8.immersiveintelligence.common.ammo.explosives;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.util.IEPotions;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumComponentRole;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmo;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmoComponent;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.util.IIDamageSources;
import pl.pabilo8.immersiveintelligence.common.IIPotions;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.entity.bullet.EntityAtomicBoom;
import pl.pabilo8.immersiveintelligence.common.util.IIExplosion;

import java.util.ArrayList;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
public class AmmoComponentNuke implements IAmmoComponent
{
	@Override
	public String getName()
	{
		return "nuke";
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("materialNuke");
	}

	@Override
	public float getDensity()
	{
		return 2f;
	}

	@Override
	public void onEffect(float amount, EnumCoreTypes coreType, NBTTagCompound tag, Vec3d pos, Vec3d dir, World world)
	{
		BlockPos ppos = new BlockPos(pos);
		new IIExplosion(world, null, pos, 56*amount, 60, false, true, false)
				.doExplosion();

		EntityLivingBase[] entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(ppos).grow(75*amount)).toArray(new EntityLivingBase[0]);
		for(EntityLivingBase e : entities)
		{
			e.addPotionEffect(new PotionEffect(IEPotions.flashed, 40, 1));
			e.addPotionEffect(new PotionEffect(IIPotions.nuclearHeat, 40, 0));
			e.hurtResistantTime = 0;
			e.getArmorInventoryList().forEach(stack -> stack.damageItem(stack.getMaxDamage(), e));
			e.attackEntityFrom(IIDamageSources.NUCLEAR_HEAT_DAMAGE, 2000);
		}
		entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(ppos).grow(50*amount)).toArray(new EntityLivingBase[0]);
		for(EntityLivingBase e : entities)
			e.addPotionEffect(new PotionEffect(IIPotions.radiation, 4000, 0));

		IIPacketHandler.playRangedSound(world, pos, IISounds.explosionNuke, SoundCategory.NEUTRAL, 72, 1f, 0f);

		EntityAtomicBoom entityAtomicBoom = new EntityAtomicBoom(world, amount);
		entityAtomicBoom.setPosition(pos.x, pos.y, pos.z);
		world.spawnEntity(entityAtomicBoom);


		final int endRad = (int)(24*amount);
		final int biomeWasteland = Biome.getIdForBiome(IIContent.biomeWasteland);

		int wastelandRadius = (int)(5*amount)*16; //16 blocks in chunk

		/*
		char[][] bloks = new char[wastelandRadius*2+1][wastelandRadius*2+1];
		for(int i = 0; i < wastelandRadius*2+1; i++)
			for(int j = 0; j < wastelandRadius*2+1; j++)
				bloks[i][j] = ' ';
		*/

		ArrayList<Chunk> radiatedChunks = new ArrayList<>();

		for(int i = -wastelandRadius; i <= wastelandRadius; i++)
			for(int j = -wastelandRadius; j <= wastelandRadius; j++)
			{
				float dist = MathHelper.sqrt(i*i+j*j);
				if(dist > wastelandRadius)
					continue;

				Chunk chunk = world.getChunkFromChunkCoords((ppos.getX()+i)>>4, (ppos.getZ()+j)>>4);
				if(!radiatedChunks.contains(chunk))
					radiatedChunks.add(chunk);

				byte[] ground = chunk.getBiomeArray();

				int posID = ((ppos.getZ()+j)&15)<<4|(ppos.getX()+i)&15;
				int val = (int)Math.max(dist-(wastelandRadius-endRad), 0);
				boolean result = MathHelper.getInt(Utils.RAND, 0, val/2)==0;

				//bloks[i+wastelandRadius][j+wastelandRadius] = result?' ': 'o';
				ground[posID] = result?(byte)biomeWasteland: ground[posID];

				chunk.setBiomeArray(ground);
				chunk.setModified(true);
			}

		for(Chunk radiatedChunk : radiatedChunks)
		{
			radiatedChunk.onTick(false);
			Packet<?> packet = new SPacketChunkData(radiatedChunk, 65535);
			for(EntityPlayer player : world.playerEntities)
			{
				if(player instanceof EntityPlayerMP)
				{
					((EntityPlayerMP)player).connection.sendPacket(packet);
					//this.playerChunkMap.getWorldServer().getEntityTracker().sendLeashedEntitiesInChunk(entityplayermp, this.chunk);
					// chunk watch event - delayed to here as the chunk wasn't ready in addPlayer
					net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.world.ChunkWatchEvent.Watch(radiatedChunk, ((EntityPlayerMP)player)));
				}
			}
		}

		//if you want to nuke your console turn this feature on :)
		//Arrays.stream(bloks).forEachOrdered(y -> IILogger.info(Arrays.toString(y)));

		/*
		int cx = pos.getX() >> 4, cy = pos.getZ() >> 4;
		for(int i = cx-wastelandRadius; i <= cx+wastelandRadius; i++)
			for(int j = cy-wastelandRadius; j <= cy+wastelandRadius; j++)
			{
				Chunk chunk = world.getChunkFromChunkCoords(i, j);
				byte[] wasteland = chunk.getBiomeArray();
				if(i==cx-wastelandRadius||i==cx+wastelandRadius||j==cy-wastelandRadius||j==cy+wastelandRadius)
				{
					for(int k = 0; k < 256; k++)
					{
						int dir=0;
						if(i==cx-wastelandRadius)
							dir=16-(k%16);
						else if(i==cx+wastelandRadius)
							dir=k%16;

						if(j==cy-wastelandRadius)
							dir=Math.max(dir,16-(k/16));
						else if(i==cyt+wastelandRadius)
							dir=Math.max(dir,k/16);


						wasteland[k] = MathHelper.getInt(Utils.RAND, 0, dir)==0?wasteland[k]: biomeArray[k];
					}
				}
				else
					wasteland = biomeArray;

				chunk.setBiomeArray(wasteland);
				chunk.markDirty();
			}
		 */
	}

	@Override
	public EnumComponentRole getRole()
	{
		return EnumComponentRole.SPECIAL;
	}

	@Override
	public int getColour()
	{
		//Weird stuff here
		if(FMLCommonHandler.instance().getEffectiveSide()==Side.CLIENT)
		{
			float add = (Minecraft.getMinecraft().world.getTotalWorldTime()%60f)/60f;
			add = add > 0.5?1f-((add-0.5f)*2f): add*2f;
			return MathHelper.hsvToRGB(110f/255f, 0.75f*add, (0.5f+((1f-add)*0.45f)));
		}
		else
			return MathHelper.hsvToRGB(121f/255f, 0.75f, 0.88f);
	}

	@Override
	public boolean matchesBullet(IAmmo bullet)
	{
		return bullet.getCaliber() >= 6;
	}
}
