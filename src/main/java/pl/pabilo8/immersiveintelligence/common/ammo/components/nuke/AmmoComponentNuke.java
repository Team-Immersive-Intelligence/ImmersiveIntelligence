package pl.pabilo8.immersiveintelligence.common.ammo.components.nuke;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.util.IEPotions;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentEffectShape;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentRole;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIPotions;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.component.EntityAtomicBoom;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageExplosion;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIDamageSources;
import pl.pabilo8.immersiveintelligence.common.util.IIExplosion;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 06.03.2024
 * @ii-approved 0.3.1
 * @since 10.07.2021
 */
public class AmmoComponentNuke extends AmmoComponent
{
	public static final int EXPLOSION_SIZE = 56, EXPLOSION_POWER = 64;
	private static final int BIOME_ARRAY_SIZE = 16*16;
	private static final int FULL_CHUNK_PACKET_MASK = 65535;

	public AmmoComponentNuke()
	{
		super("nuke", 2.5f, ComponentRole.TERRAIN_DENIAL, IIColor.fromPackedRGB(0x6b778a));
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("materialNuke");
	}

	@Override
	public void onEffect(World world, Vec3d pos, Vec3d dir, ComponentEffectShape shape, NBTTagCompound tag, float size, float multiplier, Entity owner)
	{
		//The server sends visuals before it starts the expensive terrain work.
		if(world.isRemote)
			return;

		IIPacketHandler.playRangedSound(world, pos, IISounds.explosionNuke, SoundCategory.NEUTRAL, 72, 1f, 0f);
		IIPacketHandler.sendToClient(MessageExplosion.createNukeMessage(world, pos, multiplier));

		BlockPos centre = new BlockPos(pos);
		new IIExplosion(world, owner, pos, null, EXPLOSION_SIZE*multiplier, EXPLOSION_POWER,
				ComponentEffectShape.ORB, false, true, false)
				.doExplosion(false);

		applyEntityEffects(world, centre, multiplier);

		EntityAtomicBoom entityAtomicBoom = new EntityAtomicBoom(world, multiplier);
		entityAtomicBoom.setPosition(pos.x, pos.y, pos.z);
		world.spawnEntity(entityAtomicBoom);

		if(world instanceof WorldServer)
			radiateWastelandBiomes((WorldServer)world, centre, multiplier);
	}

	private void applyEntityEffects(World world, BlockPos centre, float multiplier)
	{
		AxisAlignedBB heatBox = new AxisAlignedBB(centre).grow(75*multiplier);
		AxisAlignedBB radiationBox = new AxisAlignedBB(centre).grow(50*multiplier);

		for(EntityLivingBase entity : world.getEntitiesWithinAABB(EntityLivingBase.class, heatBox))
		{
			entity.addPotionEffect(new PotionEffect(IEPotions.flashed, 40, 1));
			entity.addPotionEffect(new PotionEffect(IIPotions.nuclearHeat, 40, 0));
			if(radiationBox.intersects(entity.getEntityBoundingBox()))
				entity.addPotionEffect(new PotionEffect(IIPotions.radiation, 4000, 0));

			entity.hurtResistantTime = 0;
			for(ItemStack stack : entity.getArmorInventoryList())
				if(!stack.isEmpty())
					stack.damageItem(stack.getMaxDamage(), entity);
			entity.attackEntityFrom(IIDamageSources.NUCLEAR_HEAT_DAMAGE, 2000);
		}
	}

	private void radiateWastelandBiomes(WorldServer world, BlockPos centre, float multiplier)
	{
		final int wastelandBiome = Biome.getIdForBiome(IIContent.biomeWasteland);
		if(wastelandBiome < 0||wastelandBiome >= 256)
			return;

		final int radius = (int)(5*multiplier)*16;
		if(radius <= 0)
			return;

		final int endRadius = (int)(24*multiplier);
		final int fadeStart = Math.max(radius-endRadius, 0);
		final int radiusSq = radius*radius;
		final int fadeStartSq = fadeStart*fadeStart;
		final byte wastelandBiomeByte = (byte)wastelandBiome;
		final boolean[] forbiddenBiomes = getForbiddenBiomeMap();

		final int minChunkX = (centre.getX()-radius)>>4;
		final int maxChunkX = (centre.getX()+radius)>>4;
		final int minChunkZ = (centre.getZ()-radius)>>4;
		final int maxChunkZ = (centre.getZ()+radius)>>4;

		List<Chunk> changedChunks = new ArrayList<>((maxChunkX-minChunkX+1)*(maxChunkZ-minChunkZ+1));

		for(int chunkX = minChunkX; chunkX <= maxChunkX; chunkX++)
			for(int chunkZ = minChunkZ; chunkZ <= maxChunkZ; chunkZ++)
			{
				//Do not generate/load far chunks just to repaint their biome array.
				Chunk chunk = world.getChunkProvider().getLoadedChunk(chunkX, chunkZ);
				if(chunk==null)
					continue;

				byte[] biomes = chunk.getBiomeArray();
				if(biomes==null||biomes.length < BIOME_ARRAY_SIZE)
					continue;

				boolean changed = false;
				for(int localX = 0; localX < 16; localX++)
				{
					int x = (chunkX<<4)+localX;
					int dx = x-centre.getX();
					int dxSq = dx*dx;
					if(dxSq > radiusSq)
						continue;

					for(int localZ = 0; localZ < 16; localZ++)
					{
						int z = (chunkZ<<4)+localZ;
						int dz = z-centre.getZ();
						int distSq = dxSq+dz*dz;
						if(distSq > radiusSq)
							continue;

						int index = (localZ<<4)|localX;
						int currentBiome = biomes[index]&255;
						if(currentBiome==wastelandBiome||forbiddenBiomes[currentBiome])
							continue;

						if(!shouldConvertBiome(distSq, fadeStart, fadeStartSq))
							continue;

						biomes[index] = wastelandBiomeByte;
						changed = true;
					}
				}

				if(changed)
				{
					chunk.setBiomeArray(biomes);
					chunk.setModified(true);
					changedChunks.add(chunk);
				}
			}

		sendChangedChunkBiomes(world, changedChunks);
	}

	private boolean shouldConvertBiome(int distSq, int fadeStart, int fadeStartSq)
	{
		if(distSq <= fadeStartSq)
			return true;

		int fade = (int)Math.max(MathHelper.sqrt(distSq)-fadeStart, 0);
		return Utils.RAND.nextInt((fade>>1)+1)==0;
	}

	private boolean[] getForbiddenBiomeMap()
	{
		boolean[] forbidden = new boolean[256];
		for(String biomeName : IIConfig.wastelandBiomeBlacklist)
		{
			Biome biome = Biome.REGISTRY.getObject(new ResourceLocation(biomeName));
			if(biome==null)
				continue;

			int id = Biome.getIdForBiome(biome);
			if(id >= 0&&id < forbidden.length)
				forbidden[id] = true;
		}
		return forbidden;
	}

	private void sendChangedChunkBiomes(WorldServer world, List<Chunk> chunks)
	{
		for(Chunk chunk : chunks)
		{
			PlayerChunkMapEntry entry = world.getPlayerChunkMap().getEntry(chunk.x, chunk.z);
			if(entry==null)
				continue;

			Packet<?> packet = new SPacketChunkData(chunk, FULL_CHUNK_PACKET_MASK);
			entry.sendPacket(packet);
		}
	}

	@Override
	public boolean matchesBullet(IAmmoTypeItem<?, ?> bullet)
	{
		return bullet.getCaliber() >= 6;
	}
}
