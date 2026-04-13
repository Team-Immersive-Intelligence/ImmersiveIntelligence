package pl.pabilo8.immersiveintelligence.common.compat.won;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkWatchEvent.Watch;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentEffectShape;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentRole;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIPotions;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AmmoComponentCorium extends AmmoComponent
/**
 * @author Carver (carver@iiteam.net)
 * @updated 13.04.2026
 */


{
	public AmmoComponentCorium()
	{
		super("corium", 1f, ComponentRole.SPECIAL, IIColor.fromPackedARGB(0x161414));
	}

	@Override

	public IngredientStack getMaterial()
	{
		Item corium = Item.REGISTRY.getObject(new ResourceLocation("wyrmsofnyrus", "corium"));

		return new IngredientStack(new ItemStack(corium, 1));

		//corium ("inertcorium" if regular one is unmineable?)
	}

	@Override
	public int getSlotsTaken()
	{
		return 3;
	}

	@Override

	public void onEffect(World world, Vec3d pos, Vec3d dir, ComponentEffectShape shape, NBTTagCompound tag, float componentSize, float multiplier, Entity owner)
	{

		BlockPos ppos = new BlockPos(pos);

		EntityLivingBase[] entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(ppos).grow(10*multiplier)).toArray(new EntityLivingBase[0]);
		for(EntityLivingBase e : entities)
		{
			Potion weakness = Potion.REGISTRY.getObject(ResLoc.of("minecraft:weakness"));

			e.addPotionEffect(new PotionEffect(weakness, 480, 5));
			e.addPotionEffect(new PotionEffect(IIPotions.radiation, 480, 4));

			e.hurtResistantTime = 0;
		}

		final int endRad = (int)(1);
		final int biomeWasteland = Biome.getIdForBiome(IIContent.biomeWasteland);

		int wastelandRadius = (int)(1)*16; //sizeable contamination

		List<Byte> forbiddenBiomes = Arrays.stream(IIConfig.wastelandBiomeBlacklist)
				.map(ResourceLocation::new)
				.map(Biome.REGISTRY::getObject)
				.filter(Objects::nonNull)
				.map(Biome::getIdForBiome)
				.map(Integer::byteValue)
				.collect(Collectors.toList());

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
				boolean result = !forbiddenBiomes.contains(ground[posID])&&MathHelper.getInt(Utils.RAND, 0, val/2)==0;

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
					MinecraftForge.EVENT_BUS.post(new Watch(radiatedChunk, ((EntityPlayerMP)player)));
				}
			}
		}
	}
}


