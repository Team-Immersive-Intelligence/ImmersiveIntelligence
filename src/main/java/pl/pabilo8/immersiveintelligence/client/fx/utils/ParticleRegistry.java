package pl.pabilo8.immersiveintelligence.client.fx.utils;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.Utils;
import com.google.gson.JsonObject;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleCloud;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ammo.PenetrationRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.penetration.IPenetrationHandler;
import pl.pabilo8.immersiveintelligence.client.fx.IIParticles;
import pl.pabilo8.immersiveintelligence.client.fx.builder.ParticleBuilder;
import pl.pabilo8.immersiveintelligence.client.fx.builder.ParticleModelBuilder;
import pl.pabilo8.immersiveintelligence.client.fx.builder.ParticleVanillaBuilder;
import pl.pabilo8.immersiveintelligence.client.fx.particles.*;
import pl.pabilo8.immersiveintelligence.client.fx.prefab.ParticleAbstractModel;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleUtils.PositionGenerator;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.util.*;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static pl.pabilo8.immersiveintelligence.client.fx.utils.IIParticleProperties.*;

/**
 * A class for creating II particle effects
 *
 * @author Pabilo8
 * @updated 05.04.2024
 * @ii-approved 0.3.1
 * @since 17.07.2020
 */
@SideOnly(Side.CLIENT)
public class ParticleRegistry
{
	//--- Static ---//
	/**
	 * Used for manual particle creation
	 */
	private static final HashMap<String, ParticleBuilder<?>> BUILDER_REGISTRY = new HashMap<>();
	/**
	 * Used for loading JSONs
	 */
	private static final HashMap<String, Supplier<? extends ParticleBuilder<?>>> TYPE_REGISTRY = new HashMap<>();
	/**
	 * Used for loading JSONs
	 */
	private static final HashMap<String, BiConsumer<? extends IIParticle, Float>> PROGRAMS_REGISTRY = new HashMap<>();

	static
	{
		TYPE_REGISTRY.put("ParticleBasicGravity", () -> new ParticleModelBuilder<>(ParticleBasicGravity::new));
		TYPE_REGISTRY.put("ParticleModel", () -> new ParticleModelBuilder<>(ParticleModel::new));
		TYPE_REGISTRY.put("ParticleDebris", () -> new ParticleModelBuilder<>(ParticleDebris::new));
		TYPE_REGISTRY.put("ParticleVanilla", () -> new ParticleVanillaBuilder(ParticleVanilla::new));
	}

	//--- Particle Registry ---//

	public static void cleanBuilderRegistry()
	{
		BUILDER_REGISTRY.clear();
		PROGRAMS_REGISTRY.clear();
	}

	public static BiConsumer<? extends IIParticle, Float> getProgram(String name)
	{
		return PROGRAMS_REGISTRY.get(name);
	}

	//--- Registry Methods ---//

	/**
	 * Registers a new particle builder.
	 *
	 * @param name                The name of the particle
	 * @param particleConstructor Particle's constructor method
	 * @param <T>                 Particle type
	 * @return The particle builder
	 */
	public static <T extends IIParticle> ParticleBuilder<T> registerParticle(String name, BiFunction<World, Vec3d, T> particleConstructor)
	{
		ParticleBuilder<T> builder = new ParticleBuilder<>(particleConstructor);
		BUILDER_REGISTRY.put(name, builder);
		return builder;
	}

	/**
	 * Registers a new particle builder for particles using 3D models.
	 *
	 * @param name The name of the particle
	 * @return The particle model builder
	 */
	public static ParticleModelBuilder<ParticleModel> registerModelParticle(String name)
	{
		return registerModelParticle(name, ParticleModel::new);
	}

	/**
	 * Registers a new particle builder for particles using 3D models.
	 *
	 * @param name                The name of the particle
	 * @param particleConstructor Particle's constructor method
	 * @param <T>                 Particle type
	 * @return The particle model builder
	 */
	public static <T extends ParticleAbstractModel> ParticleModelBuilder<T> registerModelParticle(String name, BiFunction<World, Vec3d, T> particleConstructor)
	{
		ParticleModelBuilder<T> builder = new ParticleModelBuilder<>(particleConstructor).subscribeToList("particle/"+name);
		BUILDER_REGISTRY.put(name, builder);
		return builder;
	}


	@Nullable
	public static ParticleBuilder<? extends IIParticle> getParticleBuilder(String particleName)
	{
		return BUILDER_REGISTRY.get(particleName);
	}

	public static void registerParticleFile(String particleName)
	{
		registerParticleFile(particleName, IIReference.RES_II.with(particleName));
	}

	/**
	 * Registers a particle by loading data from a {@link ResLoc#EXT_FX_AMT} (JSON) file.
	 *
	 * @param particleName The name of the particle
	 * @param particlePath The path to the particle file
	 */
	public static void registerParticleFile(String particleName, ResLoc particlePath)
	{
		try
		{
			//Load file
			JsonObject json = FileUtils.readJSONFile(
					IIReference.RES_PARTICLES.with(particlePath.withExtension(ResLoc.EXT_FX_AMT).getResourcePath())
			);
			//Attempt to create builder
			ParticleBuilder<?> builder = TYPE_REGISTRY.get(json.get("type").getAsString()).get();
			//Parse JSON data into builder
			builder.parseBuilderFromJSON(EasyNBT.wrapNBT(json));
			//Register for model reload, if applicable
			if(builder instanceof IReloadableModelContainer)
				((IReloadableModelContainer<?>)builder).subscribeToList("particle/"+particleName);
			//Register the particle
			BUILDER_REGISTRY.put(particleName, builder);
		} catch(Exception e)
		{
			IILogger.error("Couldn't load particle file "+particlePath+", "+e.getMessage());
		}
	}

	public static <T extends IIParticle> void registerProgram(String name, BiConsumer<T, Float> program)
	{
		PROGRAMS_REGISTRY.put(name, program);
	}

	//--- Spawning Particles ---//

	/**
	 * Spawns a registered particle effect at the given position. Used by server messages.
	 *
	 * @param name The name of the particle
	 * @param nbt  The NBT data of the particle
	 */
	public static void spawnParticle(String name, NBTTagCompound nbt)
	{
		//check if contained in registry
		ParticleBuilder<?> builder = BUILDER_REGISTRY.get(name);
		if(builder==null)
			return;

		//set particle params
		EasyNBT eNBT = EasyNBT.wrapNBT(nbt);
		builder.withProperty(easyNBT -> easyNBT.mergeWith(nbt));

		//saved by default in IIParticle
		builder.spawnParticle(eNBT.getVec3d(POSITION), eNBT.getVec3d(MOTION), eNBT.getVec3d(ROTATION));
	}

	/**
	 * Spawns a registered particle effect at the given position, moving at and facing the same direction.
	 * Overload of {@link #spawnParticle(String, Vec3d, Vec3d, Vec3d)}.
	 *
	 * @param name      The name of the particle
	 * @param pos       The position to spawn the particle at
	 * @param direction The direction the particle should move in
	 * @return The spawned particle
	 */
	@Nullable
	public static IIParticle spawnParticle(String name, Vec3d pos, Vec3d direction)
	{
		return spawnParticle(name, pos, direction, direction);
	}

	/**
	 * Spawns a registered particle effect at the given position, with different motion and facing directions.
	 *
	 * @param name      The name of the particle
	 * @param pos       The position to spawn the particle at
	 * @param direction The direction the particle should move in
	 * @param motion    The motion of the particle
	 * @return The spawned particle
	 */
	@Nullable
	public static IIParticle spawnParticle(String name, Vec3d pos, Vec3d direction, Vec3d motion)
	{
		ParticleBuilder<?> builder = BUILDER_REGISTRY.get(name);
		if(builder!=null)
			return builder.spawnParticle(pos, motion, direction);
		return null;
	}

	/**
	 * Spawns a registered particle effect at the given position, with different motion and facing directions.
	 *
	 * @param name      The name of the particle
	 * @param pos       The position to spawn the particle at
	 * @param direction The direction the particle should move in
	 * @param motion    The motion of the particle
	 * @return The spawned particle
	 */
	@Nullable
	public static <T extends IIParticle> T spawnParticle(Class<T> klass, String name, Vec3d pos, Vec3d direction, Vec3d motion)
	{
		ParticleBuilder<?> builder = BUILDER_REGISTRY.get(name);
		if(builder!=null)
			return ((T)builder.spawnParticle(pos, motion, direction));
		return null;
	}

	//--- External Info Methods ---//

	public static List<String> getRegisteredNames()
	{
		return new ArrayList<>(BUILDER_REGISTRY.keySet());
	}

	//--- Old Methods ---//

	public static void spawnExplosionBoomFX(World world, Vec3d pos, Vec3d dir, IIExplosion explosion)
	{
		double powerFraction = explosion.getPower()*0.0625;
		int maxFractions = (int)Math.max(1, 4-Math.ceil(explosion.getSize()/4d));

		//normalize direction
		Vec3d explosionParticleDir = ParticleUtils.normalizeExplosionDirection(dir);

		//spawn explosion center + smoke
		IIParticle explosionParticle = ParticleRegistry.spawnParticle(IIParticles.PARTICLE_EXPLOSION_TNT, pos,
				explosionParticleDir, Vec3d.ZERO);
		explosionParticle.setProperties(EasyNBT.newNBT().withDouble(SIZE, explosion.getSize()/5f));

		//defaultize direction
		dir = new Vec3d(0, 1, 0);

		//Custom sounds
		world.playSound(Minecraft.getMinecraft().player, pos.x, pos.y, pos.z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F+(world.rand.nextFloat()-world.rand.nextFloat())*0.2F)*0.7F);

		//Create debris for destroyed blocks at the end of explosion ray scan
		for(BlockPos destroyed : explosion.generateAffectedBlockPositions(true))
		{
			//Get the parameters of the destroyed block
			IBlockState state = world.getBlockState(destroyed);
			IPenetrationHandler penetrationHandler = PenetrationRegistry.getPenetrationHandler(state);
			String debrisName = penetrationHandler.getDebrisParticle();

			//Handler has no debris particle or it's not registered
			if(debrisName.isEmpty()||ParticleRegistry.getParticleBuilder(debrisName)==null)
				continue;

			//Get position and direction relative to the center of the explosion
			ResLoc resLoc = ResLoc.of(ClientUtils.getSideTexture(state, EnumFacing.DOWN));
			Vec3d desPos = new Vec3d(destroyed);
			Vec3d desDir = desPos.subtract(pos).normalize();

			//Random additional offset
			Vec3d offset = ParticleUtils.getRandXZ().scale(0.05);

			//Create debris particle
			IIParticle particle = ParticleRegistry.spawnParticle(debrisName,
					desPos.add(offset).addVector(0, 0.5, 0),
					desDir,
					desDir.scale(0.0625)
							.add(dir.scale(powerFraction+ParticleUtils.randFloat.get()*powerFraction))
			);
			assert particle instanceof ParticleAbstractModel;
			((ParticleAbstractModel)particle).retexture(0, resLoc);

			//Create smoke trace particle
			ParticleRegistry.spawnParticle(IIParticles.PARTICLE_SMOKE_TRACE,
							desPos.add(offset).addVector(0, 0.5, 0), desDir, Vec3d.ZERO)
					.setProperties(EasyNBT.newNBT().withColor(COLOR, IIColor.fromPackedRGB(0x373130))
							.withVec3d(SCALE, new Vec3d(2, 7, 2)));

			//Create block chunk particles
			Vec3d finalDir = dir;
			ParticleUtils.position(PositionGenerator.RAND_XZ, desPos.addVector(0, 0.5, 0), 1+Utils.RAND.nextInt(maxFractions), 0.05,
					(p, d) -> {
						IIParticle chunk = ParticleRegistry.spawnParticle(
								IIParticles.PARTICLE_BLOCK_CHUNK, p, desDir,
								desDir.scale(0.25*ParticleUtils.randFloat.get())
										.add(finalDir.scale(powerFraction+ParticleUtils.randFloat.get()*powerFraction))
						);
						assert chunk instanceof ParticleAbstractModel;
						((ParticleAbstractModel)chunk).retexture(0, resLoc);
					}
			);
		}

	}

	public static void spawnTracerFX(Vec3d pos, Vec3d motion, float size, int color)
	{
		/*ParticleTracer particle = new ParticleTracer(getWorld(), pos, motion, size, color);
		ParticleSystem.addEffect(particle);*/
	}

	//TODO: 04.05.2024 replace with AMT models
	public static void spawnGunfireFX(Vec3d pos, Vec3d motion, float size)
	{
		/*ParticleGunfire particle = new ParticleGunfire(getWorld(), pos, motion, size);
		ParticleSystem.addEffect(particle);*/
	}

	//TODO: 04.05.2024 replace with AMT models
	public static void spawnTMTModelFX(Vec3d pos, Vec3d motion, float size, ModelRendererTurbo model, ResourceLocation texture)
	{
		/*Particle particle = new ParticleTMTModel(getWorld(), pos, motion, size, model, texture);
		Minecraft.getMinecraft().effectRenderer.addEffect(particle);*/
	}

	public static void spawnFlameFX(Vec3d pos, Vec3d motion, float size, int lifeTime)
	{
		/*ParticleFlame particle = new ParticleFlame(getWorld(), pos, motion, size, lifeTime);
		ParticleSystem.addEffect(particle);*/
	}

	public static void spawnFlameExplosion(Vec3d pos, float size, Random rand)
	{

		for(int i = 0; i < 20*size; i += 1)
		{
			Vec3d v = new Vec3d(1, 0, 0).rotateYaw(i/20f*360f);

			ParticleCloud particle = (ParticleCloud)spawnVanillaParticle(EnumParticleTypes.CLOUD, pos, ParticleUtils.withY(v.scale(0.25), 0.125));
			if(particle!=null)
			{
				particle.setRBGColorF(rand.nextFloat()*0.125f, rand.nextFloat()*0.125f, 0);
				particle.multipleParticleScaleBy(2.5f);
				particle.setMaxAge(10);
			}

		}


	}

	public static void spawnGasCloud(Vec3d pos, float size, Fluid fluid) {
		// Check if fluid is not null
		if (fluid == null) return;

		// Get the color of the fluid
		int color = fluid.getColor(); // Assuming the Fluid class has this method
		float red = ((color >> 16) & 255) / 255.0F;
		float green = ((color >> 8) & 255) / 255.0F;
		float blue = (color & 255) / 255.0F;

		// Spawn multiple particles for the gas cloud effect
		for (int i = 0; i < 40 * size; i++) {
			// Randomly distribute particles in a larger spherical area
			double offsetX = (Math.random() - 0.5) * size * 3.5; // Increased spread
			double offsetY = (Math.random() - 0.5) * size * 2; // Increased vertical spread
			double offsetZ = (Math.random() - 0.5) * size * 3.5; // Increased spread

			Vec3d particlePos = pos.add(new Vec3d(offsetX, offsetY - 1.0, offsetZ)); // Lower spawn point by 1 block

			ParticleCloud particle = (ParticleCloud) spawnVanillaParticle(EnumParticleTypes.CLOUD, particlePos, Vec3d.ZERO);
			if (particle != null) {
				particle.setRBGColorF(red, green, blue); // Set the color of the particle
				particle.setMaxAge(160); // Adjust lifespan as needed
				particle.multipleParticleScaleBy(5f); // Adjust scale if necessary
			}
		}
	}

	//--- Utils ---//

	private static Particle spawnVanillaParticle(EnumParticleTypes particle, Vec3d pos, Vec3d motion)
	{
		return ClientUtils.mc().effectRenderer.spawnEffectParticle(particle.getParticleID(),
				pos.x, pos.y, pos.z,
				motion.x, motion.y, motion.z);
	}
}