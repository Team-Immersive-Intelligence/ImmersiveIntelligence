package pl.pabilo8.immersiveintelligence.client.fx.utils;

import blusunrize.immersiveengineering.client.ClientUtils;
import com.google.gson.JsonObject;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleCloud;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.toposort.TopologicalSort;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ammo.PenetrationRegistry;
import pl.pabilo8.immersiveintelligence.client.fx.factories.ParticleFactory;
import pl.pabilo8.immersiveintelligence.client.fx.particles.AbstractParticle;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.util.*;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nullable;
import javax.vecmath.Vector2f;
import java.util.*;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class for registering II particle effects, effect types and programs.
 * Allows loading particle effects from {@link ResLoc#EXT_FX_AMT .fx.amt} (JSON) files.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 05.04.2024
 * @ii-approved 0.3.1
 * @since 17.07.2020
 */
@SideOnly(Side.CLIENT)
public class ParticleRegistry
{
	//--- Static ---//

	private static final Pattern PROGRAM_PATTERN = Pattern.compile("([a-zA-Z_][a-zA-Z0-9_]*)(\\(([^)]*)\\))?");

	/**
	 * Stores particle factories
	 */
	private static final HashMap<String, ParticleFactory<?>> FACTORIES_REGISTRY = new HashMap<>();
	/**
	 * Stores particle types (factory class constructors)
	 */
	private static final HashMap<String, Supplier<? extends ParticleFactory<?>>> TYPE_REGISTRY = new HashMap<>();
	/**
	 * Stores compiled (with parameters) programs that can be used to modify particles during their lifetime
	 */
	private static final HashMap<String, ParticleProgram> PROGRAMS_REGISTRY = new HashMap<>();
	/**
	 * Stores particle program providers (uncompiled programs)
	 */
	private static final HashMap<String, Supplier<ParticleProgram>> PROGRAM_PROVIDER_REGISTRY = new HashMap<>();
	private static final HashMap<String, ParticleFileEntry> FILELOADER_ENTRIES = new HashMap<>();

	//--- Particle Registry ---//

	/**
	 * Clears the particle registry. Called before assets are reloaded.
	 */
	public static void cleanBuilderRegistry()
	{
		TYPE_REGISTRY.clear();
		FACTORIES_REGISTRY.clear();
		PROGRAM_PROVIDER_REGISTRY.clear();
		PROGRAMS_REGISTRY.clear();
		FILELOADER_ENTRIES.clear();
	}

	//--- Registry Methods ---//

	/**
	 * Registers a new particle type.
	 *
	 * @param name               The name of the particle
	 * @param factoryConstructor The constructor method of the particle factory
	 */
	public static void registerParticleType(String name, Supplier<? extends ParticleFactory<?>> factoryConstructor)
	{
		TYPE_REGISTRY.put(name, factoryConstructor);
	}

	/**
	 * Registers a new particle effect to be loaded from a file.
	 *
	 * @param fileName The name of the file to load the particle from
	 */
	public static void registerParticle(String fileName)
	{
		FILELOADER_ENTRIES.put(fileName, new ParticleFileEntry(IIReference.RES_II.with(fileName)));
	}

	/**
	 * Retrieves a particle factory from the registry.
	 *
	 * @param particleName name of the particle
	 * @return the particle factory
	 */
	@Nullable
	public static ParticleFactory<? extends AbstractParticle> getParticle(String particleName)
	{
		return FACTORIES_REGISTRY.get(particleName);
	}

	/**
	 * Registers a new {@link ParticleProgram particle program}.
	 *
	 * @param program The supplier of the particle program to register.
	 * @param <T>     The type of the particle that the program will be applied to.
	 */
	public static <T extends AbstractParticle> void registerProgram(Supplier<ParticleProgram> program)
	{
		ParticleProgram defaultProgram = program.get();
		PROGRAM_PROVIDER_REGISTRY.put(defaultProgram.getProgramName(), program);
		PROGRAMS_REGISTRY.put(defaultProgram.getProgramName(), defaultProgram);
	}

	public static ParticleProgram getProgram(String name)
	{
		//Regex to match program name with parameters, f.e. "program(1, 2)"
		Matcher matcher = PROGRAM_PATTERN.matcher(name);

		//Return the program if it exists in the registry
		if(matcher.matches())
		{
			String baseName = matcher.group(1);
			String params = matcher.group(3);

			//Check if the program with parameters is already cached
			if(PROGRAMS_REGISTRY.containsKey(name))
				return PROGRAMS_REGISTRY.get(name);

			//Initialize a new program with the supplier
			Supplier<ParticleProgram> supplier = PROGRAM_PROVIDER_REGISTRY.get(baseName);
			if(supplier!=null)
			{
				ParticleProgram program = supplier.get();
				if(params!=null)
					try
					{
						program.initArguments(params.split(","));
					} catch(ParticleProgram.ParticleArgumentException e)
					{
						IILogger.error("Error initializing program arguments for "+name+": "+e.getMessage());
					}
				PROGRAMS_REGISTRY.put(name, program);
				return program;
			}
		}
		else if(PROGRAMS_REGISTRY.containsKey(name))
			return PROGRAMS_REGISTRY.get(name);
		return null;
	}

	//--- Loading ---//

	public static void loadAllParticleFiles()
	{
		TopologicalSort.DirectedGraph<String> graph = new TopologicalSort.DirectedGraph<>();
		FILELOADER_ENTRIES.keySet().forEach(graph::addNode);

		for(String name : FILELOADER_ENTRIES.keySet())
			try
			{
				JsonObject json = IIFileUtils.readJSONFile(IIReference.RES_PARTICLES.with(FILELOADER_ENTRIES.get(name).path.withExtension(ResLoc.EXT_FX_AMT).getResourcePath()));
				if(json.has("parent"))
				{
					String parent = json.get("parent").getAsString();
					graph.addEdge(parent, name);
				}
			} catch(Exception e)
			{
				IILogger.error("Couldn't load particle file "+FILELOADER_ENTRIES.get(name).path+", "+e.getMessage());
			}

		List<String> sortedNames = TopologicalSort.topologicalSort(graph);
		for(String name : sortedNames)
		{
			ParticleFileEntry entry = FILELOADER_ENTRIES.get(name);
			try
			{
				//Parse particle's JSON
				JsonObject json = IIFileUtils.readJSONFile(IIReference.RES_PARTICLES.with(entry.path.withExtension(ResLoc.EXT_FX_AMT).getResourcePath()));
				EasyNBT nbt = EasyNBT.wrapNBT(json);
				ParticleFactory<?> factory = TYPE_REGISTRY.get(nbt.getString("type")).get();
				nbt.checkSetString("parent", s -> factory.withParent(FILELOADER_ENTRIES.get(s).factory));
				factory.parseNBT(nbt);

				//Register the particle
				FACTORIES_REGISTRY.put(name, entry.factory = factory);

				//Register for model reload, if applicable
				if(factory instanceof IReloadableModelContainer)
					((IReloadableModelContainer<?>)factory).subscribeToList("particle/"+name);

			} catch(Exception e)
			{
				IILogger.error("Couldn't load particle file "+entry.path+", "+e.getMessage());
			}
		}
	}

	//--- Spawning Particles ---//

	/**
	 * Spawns a registered particle effect at the given position. Used by server messages.
	 *
	 * @param name The name of the particle
	 * @param nbt  The NBT data of the particle
	 */
	public static void spawnParticle(String name, EasyNBT nbt)
	{
		//check if contained in registry
		ParticleFactory<?> factory = FACTORIES_REGISTRY.get(name);
		if(factory==null)
			return;

		//saved by default in IIParticle
		factory.spawn(
				nbt.getVec3d(ParticleProperties.POSITION.getName()),
				nbt.getVec3d(ParticleProperties.MOTION.getName()),
				nbt.getVector2f(ParticleProperties.ROTATION.getName())
		).deserializeNBT(nbt.unwrap());
	}

	/**
	 * Spawns a registered particle effect at the given position, moving towards and facing the same direction.
	 * Overload of {@link #spawnParticle(String, Vec3d, Vec3d, Vector2f)}.
	 *
	 * @param name      The name of the particle
	 * @param pos       The position to spawn the particle at
	 * @param direction The direction the particle should move in
	 * @return The spawned particle
	 */
	@Nullable
	public static AbstractParticle spawnParticle(String name, Vec3d pos, Vector2f direction)
	{
		Vec3d motion = IIMath.offsetPosDirection(1, direction.x, direction.y).normalize();
		return spawnParticle(name, pos, motion, direction);
	}

	/**
	 * Spawns a registered particle effect at the given position, with different motion and rotation.
	 *
	 * @param name      The name of the particle
	 * @param pos       The position to spawn the particle at
	 * @param direction The direction the particle should move in
	 * @param motion    The motion of the particle
	 * @return The spawned particle
	 */
	@Nullable
	public static AbstractParticle spawnParticle(String name, Vec3d pos, Vec3d motion, Vector2f direction)
	{
		ParticleFactory<?> factory = FACTORIES_REGISTRY.get(name);
		if(factory!=null)
			return factory.spawn(pos, motion, direction);
		return null;
	}

	/**
	 * Spawns a registered particle effect at the given position, with different motion and facing directions.
	 *
	 * @param name           The name of the particle
	 * @param pos            The position to spawn the particle at
	 * @param motion         The motion of the particle
	 * @param directionYaw   The yaw direction the particle should face
	 * @param directionPitch The pitch direction the particle should face
	 * @return The spawned particle
	 */
	@Nullable
	@SuppressWarnings("unchecked")
	public static <T extends AbstractParticle> T spawnParticle(Class<T> klass, String name, Vec3d pos, Vec3d motion, float directionYaw, float directionPitch)
	{
		ParticleFactory<?> factory = FACTORIES_REGISTRY.get(name);
		if(factory!=null)
			return ((T)factory.spawn(pos, motion, directionYaw, directionPitch));
		return null;
	}

	@Nullable
	public static AbstractParticle scheduleSpawnParticle(String name, Vec3d pos, Vec3d motion, Vector2f direction, int delay)
	{
		ParticleFactory<?> factory = FACTORIES_REGISTRY.get(name);
		if(factory!=null)
			return factory.scheduleSpawn(pos, motion, direction, delay);
		return null;
	}

	//--- External Info Methods ---//

	public static List<String> getRegisteredNames()
	{
		return new ArrayList<>(FACTORIES_REGISTRY.keySet());
	}

	//--- Old Methods ---//

	public static void spawnExplosionBoomFX(World world, Vec3d pos, Vec3d dir, IIExplosion explosion)
	{
		float playerDistance = (float)ClientUtils.mc().player.getDistance(pos.x, pos.y, pos.z);
		float size = (float)Math.min(explosion.getSize(), explosion.getPower()+1);
		float logSize = 1f+MathHelper.log2((int)(size));
		boolean detailed = playerDistance < 64;

		//If the direction is zero, set it to up (usual direction for explosions)
		if(dir.equals(Vec3d.ZERO))
			dir = new Vec3d(0, 1, 0);
		else
			dir = dir.scale(-1);
		//Make it a unit vector
		dir = IIParticleUtils.normalizeExplosionDirection(dir);
		Vector2f facing = IIParticleUtils.toVector2f(dir);

		//Spawn a shockwave
		spawnParticle("explosion/shockwave", pos.add(dir), Vec3d.ZERO, facing)
				.withProperty(ParticleProperties.SIZE, size*0.6f)
				.withProperty(ParticleProperties.MAX_LIFETIME, (int)(4*logSize)+1);
		scheduleSpawnParticle("explosion/glow", pos.add(dir), Vec3d.ZERO, new Vector2f(0, 0), 1)
				.withProperty(ParticleProperties.SIZE, size);

		spawnParticle("explosion/main", pos.add(dir.scale(size/2f)), Vec3d.ZERO, facing)
				.withProperty(ParticleProperties.SIZE, size*0.75f)
				.withProperty(ParticleProperties.MAX_LIFETIME, (int)(4*logSize)+3);

		Set<BlockPos> topBlocks = IIExplosion.getTopBlocks(explosion.generateAffectedBlockPositions(), EnumFacing.getFacingFromVector((float)dir.x, (float)dir.y, (float)dir.z));

		for(BlockPos destroyed : topBlocks)
		{
			//Calculate distance factor
			double distance = pos.distanceTo(new Vec3d(destroyed).addVector(0.5, 0, 0.5));

			if(detailed)
				scheduleSpawnParticle("smoke/dust_cloud", new Vec3d(destroyed).addVector(0.5, 0, 0.5),
						Vec3d.ZERO, new Vector2f(0, 0), 10)
						.withProperty(ParticleProperties.SIZE, 1.25f);
			spawnParticle("explosion/glow_individual", new Vec3d(destroyed).addVector(0.5, 0, 0.5),
					Vec3d.ZERO, new Vector2f(0, 0));

			String debrisParticle = PenetrationRegistry.getPenetrationHandler(world.getBlockState(destroyed))
					.getDebrisParticle();
			if(debrisParticle!=null)
			{
				double factor = MathHelper.clamp(distance/size+(IIParticleUtils.randFloat.get()*0.01), 0, 1);
				//Calculate direction vector
				Vec3d offCenterDirection = new Vec3d(destroyed).addVector(0.5, 0, 0.5)
						.subtract(pos).normalize();
				Vec3d debrisMotion = dir.scale(1-factor).add(offCenterDirection.scale(factor))
						.scale(1.05f*Math.max(1f, explosion.getPower()/6f));

				//Spawn the debris particle
				scheduleSpawnParticle(debrisParticle, new Vec3d(destroyed).addVector(0, 0f, 0),
						debrisMotion, new Vector2f(IIParticleUtils.randFloat.get()*4, IIParticleUtils.randFloat.get()*4), 3)
						.withProperty(ParticleProperties.TEXTURES, new ResourceLocation[]{
								ClientUtils.getSideTexture(world.getBlockState(destroyed), EnumFacing.WEST)
						});

				//Spawn a smoke trace in the same direction
				scheduleSpawnParticle("smoke/smoke_trace", new Vec3d(destroyed).addVector(0.5, 0, 0.5),
						Vec3d.ZERO, IIParticleUtils.toVector2f(debrisMotion), 1)
						.withProperty(ParticleProperties.SIZE, logSize*0.4f)
						.withProperty(ParticleProperties.MAX_LIFETIME, (int)(4*(logSize))+1);

				//Spawn a smoke trace
				scheduleSpawnParticle("smoke/smoke_trace", new Vec3d(destroyed).addVector(0.5, 0, 0.5),
						Vec3d.ZERO, IIParticleUtils.toVector2f(debrisMotion), 1)
						.withProperty(ParticleProperties.COLOR, IIColor.fromPackedRGB(0x3f3f3f))
						.withProperty(ParticleProperties.SIZE, logSize*0.4f)
						.withProperty(ParticleProperties.MAX_LIFETIME, (int)(4*(logSize))+20);

				//Spawn additional debris particles for large explosions
				if(detailed)
					for(int i = 0; i < 2; i++)
					{
						factor *= IIParticleUtils.randFloat.get()*2f;
						debrisMotion = dir.scale(1-factor).add(offCenterDirection.scale(factor))
								.scale(1.05f*Math.max(1f, explosion.getPower()/6f));
						scheduleSpawnParticle(debrisParticle, new Vec3d(destroyed).addVector(0, 0f, 0),
								debrisMotion, new Vector2f(IIParticleUtils.randFloat.get()*4, IIParticleUtils.randFloat.get()*4), 3*i)
								.withProperty(ParticleProperties.TEXTURES, new ResourceLocation[]{
										ClientUtils.getSideTexture(world.getBlockState(destroyed), EnumFacing.DOWN)
								});
					}

			}
		}

	}

	public static void spawnGunfireFX(Vec3d pos, Vec3d direction, float size)
	{
		ParticleRegistry.spawnParticle("ammo/gunfire", pos, Vec3d.ZERO, IIParticleUtils.toVector2f(direction))
				.withProperty(ParticleProperties.SIZE, size*0.25f);
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

			ParticleCloud particle = (ParticleCloud)spawnVanillaParticle(EnumParticleTypes.CLOUD, pos, IIParticleUtils.withY(v.scale(0.25), 0.125));
			if(particle!=null)
			{
				particle.setRBGColorF(rand.nextFloat()*0.125f, rand.nextFloat()*0.125f, 0);
				particle.multipleParticleScaleBy(2.5f);
				particle.setMaxAge(10);
			}

		}


	}

	public static void spawnGasCloud(Vec3d pos, float size, Fluid fluid)
	{
		//Check if fluid is not null
		if(fluid==null) return;

		//Get the color of the fluid
		int color = fluid.getColor(); //Assuming the Fluid class has this method
		float red = ((color>>16)&255)/255.0F;
		float green = ((color>>8)&255)/255.0F;
		float blue = (color&255)/255.0F;

		//Spawn multiple particles for the gas cloud effect
		for(int i = 0; i < 40*size; i++)
		{
			//Randomly distribute particles in a larger spherical area
			double offsetX = (Math.random()-0.5)*size*3.5; //Increased spread
			double offsetY = (Math.random()-0.5)*size*2; //Increased vertical spread
			double offsetZ = (Math.random()-0.5)*size*3.5; //Increased spread

			Vec3d particlePos = pos.add(new Vec3d(offsetX, offsetY-1.0, offsetZ)); //Lower spawn point by 1 block

			ParticleCloud particle = (ParticleCloud)spawnVanillaParticle(EnumParticleTypes.CLOUD, particlePos, Vec3d.ZERO);
			if(particle!=null)
			{
				particle.setRBGColorF(red, green, blue); //Set the color of the particle
				particle.setMaxAge(160); //Adjust lifespan as needed
				particle.multipleParticleScaleBy(5f); //Adjust scale if necessary
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

	private static class ParticleFileEntry
	{
		ResLoc path;
		ParticleFactory<?> factory;

		ParticleFileEntry(ResLoc path)
		{
			this.path = path;
		}
	}
}
