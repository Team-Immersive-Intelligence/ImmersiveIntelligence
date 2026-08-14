package pl.pabilo8.immersiveintelligence.client.fx.utils;

import blusunrize.immersiveengineering.client.ClientUtils;
import com.google.gson.JsonObject;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.toposort.TopologicalSort;
import net.minecraftforge.fml.common.toposort.TopologicalSort.DirectedGraph;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ammo.PenetrationRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentEffectShape;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.fx.factories.ParticleFactory;
import pl.pabilo8.immersiveintelligence.client.fx.particles.AbstractParticle;
import pl.pabilo8.immersiveintelligence.client.fx.utils.IIParticleUtils.PositionGenerator;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleProgram.ParticleArgumentException;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Graphics;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.ammo.components.nuke.AmmoComponentNuke;
import pl.pabilo8.immersiveintelligence.common.util.*;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nullable;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
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
					} catch(ParticleArgumentException e)
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
		DirectedGraph<String> graph = new DirectedGraph<>();
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

	//--- Pre-made Particle Creation Methods ---//

	/**
	 * Spawns the client-side explosion effect.
	 */
	public static void spawnExplosionBoomFX(World world, Vec3d pos, Vec3d dir,
	                                        float radius, float power, ComponentEffectShape shape,
	                                        List<BlockPos> affectedSurface)
	{
		float playerDistance = (float)ClientUtils.mc().player.getDistance(pos.x, pos.y, pos.z);
		float effectExtent = Math.max(1f, Math.min(radius, power+1f));
		float logSize = 1f+MathHelper.log2(Math.max(1, (int)effectExtent));
		boolean nearby = playerDistance < 64f;

		ParticleDetail particleDetail = IIParticleUtils.getParticleDetailLevel(Graphics.explosionParticlesDetail);
		ParticleDetail debrisDetail = IIParticleUtils.getParticleDetailLevel(Graphics.explosionDebrisDetail);
		if(!particleDetail.isEnabled()&&!debrisDetail.isEnabled())
			return;

		boolean spawnCore = particleDetail.isEnabled();
		boolean spawnDust = nearby&&particleDetail.isMedium();
		boolean spawnGlows = particleDetail.isEnabled();
		boolean spawnDebris = debrisDetail.isEnabled();
		boolean spawnDebrisTrails = debrisDetail.isMedium();
		boolean spawnRichDebris = nearby&&debrisDetail.isHigh();
		boolean adaptiveSurface = radius > 8f;
		boolean adaptiveDebris = radius > 12f&&power > 12f;

		float particleBudgetScale = IIParticleUtils.getParticleBudgetScale(
				particleDetail, playerDistance, 64f, 128f);
		float debrisBudgetScale = IIParticleUtils.getParticleBudgetScale(
				debrisDetail, playerDistance, 64f, 128f);

		Vec3d explosionDirection = dir;

		//If the direction is zero, set it to up (usual direction for explosions)
		if(dir.equals(Vec3d.ZERO))
			dir = new Vec3d(0, 1, 0);
		else
			dir = dir.scale(-1);
		//Make it a unit vector
		dir = IIParticleUtils.normalizeExplosionDirection(dir);
		Vector2f facing = IIParticleUtils.toVector2f(dir);

		if(spawnCore)
		{
			spawnParticle("explosion/shockwave", pos.add(dir), Vec3d.ZERO, facing)
					.withProperty(ParticleProperties.SIZE, effectExtent*0.6f)
					.withProperty(ParticleProperties.MAX_LIFETIME, (int)(4*logSize)+1);
			scheduleSpawnParticle("explosion/glow", pos.add(dir), Vec3d.ZERO, new Vector2f(0, 0), 1)
					.withProperty(ParticleProperties.SIZE, effectExtent);

			spawnParticle("explosion/main", pos.add(dir.scale(effectExtent/2f)), Vec3d.ZERO, facing)
					.withProperty(ParticleProperties.SIZE, effectExtent*0.75f)
					.withProperty(ParticleProperties.MAX_LIFETIME, (int)(4*logSize)+3);
		}

		List<BlockPos> effectBlocks;
		if(adaptiveSurface)
		{
			if(affectedSurface==null||affectedSurface.isEmpty())
				return;
			effectBlocks = new ArrayList<>(affectedSurface);
		}
		else
			effectBlocks = getExactExplosionSurface(world, pos, explosionDirection, radius, power, shape, dir);

		if(effectBlocks.isEmpty())
			return;

		float affectedExtent = getAffectedSurfaceExtent(pos, effectBlocks);
		float dustSize = adaptiveSurface?
				MathHelper.clamp(1.15f+0.35f*(float)Math.sqrt(Math.max(1f, affectedExtent/8f)), 1.25f, 2.75f):
				1.25f;
		float debrisSize = adaptiveDebris?
				MathHelper.clamp(1f+MathHelper.log2(Math.max(1, (int)(affectedExtent/12f)))*0.15f, 1f, 1.8f):
				1f;

		int dustBudget = spawnDust?
				(adaptiveSurface?
						IIParticleUtils.calculateAdaptiveParticleBudget(
								affectedExtent, 1f, 48f, 5.5f, 1f,
								particleBudgetScale, 16, 256):
						effectBlocks.size()):
				0;
		int glowBudget = spawnGlows?
				(adaptiveSurface?
						MathHelper.clamp(Math.round(72f*particleBudgetScale), 1, 72):
						effectBlocks.size()):
				0;
		int debrisBudget = spawnDebris?
				(adaptiveDebris?
						IIParticleUtils.calculateAdaptiveParticleBudget(
								affectedExtent, 12f, 14f, 16f, 0.5f,
								debrisBudgetScale, 8, 128):
						effectBlocks.size()):
				0;

		List<BlockPos> dustBlocks = IIParticleUtils.selectEvenlyDistributed(effectBlocks, dustBudget);
		List<BlockPos> glowBlocks = IIParticleUtils.selectEvenlyDistributed(effectBlocks, glowBudget);
		List<BlockPos> debrisBlocks = IIParticleUtils.selectEvenlyDistributed(effectBlocks, debrisBudget);

		for(BlockPos destroyed : dustBlocks)
		{
			IBlockState state = world.getBlockState(destroyed);
			if(state.getMaterial()==Material.AIR)
				continue;

			Vec3d destroyedCenter = new Vec3d(destroyed).addVector(0.5, 0.5, 0.5);
			scheduleSpawnParticle("smoke/dust_cloud", destroyedCenter,
					Vec3d.ZERO, new Vector2f(0, 0), 10)
					.withProperty(ParticleProperties.SIZE, dustSize)
					.withProperty(ParticleProperties.MAX_LIFETIME, (int)(4*logSize)+8);
		}

		for(BlockPos destroyed : glowBlocks)
		{
			IBlockState state = world.getBlockState(destroyed);
			if(state.getMaterial()!=Material.AIR)
				spawnParticle("explosion/glow_individual",
						new Vec3d(destroyed).addVector(0.5, 0.5, 0.5),
						Vec3d.ZERO, new Vector2f(0, 0));
		}

		int debrisIndex = 0;
		for(BlockPos destroyed : debrisBlocks)
		{
			IBlockState state = world.getBlockState(destroyed);
			if(state.getMaterial()==Material.AIR)
				continue;

			String debrisParticle = PenetrationRegistry.getPenetrationHandler(state).getDebrisParticle();
			if(debrisParticle==null)
				continue;

			Vec3d destroyedCenter = new Vec3d(destroyed).addVector(0.5, 0.5, 0.5);
			double distance = pos.distanceTo(destroyedCenter);
			double factor = MathHelper.clamp(
					distance/Math.max(1f, affectedExtent)+(IIParticleUtils.randFloat.get()*0.01), 0, 1);
			Vec3d offCenterDirection = destroyedCenter.subtract(pos).normalize();
			Vec3d debrisMotion = dir.scale(1-factor).add(offCenterDirection.scale(factor))
					.scale(1.05f*Math.max(1f, power/6f));
			Vector2f debrisFacing = IIParticleUtils.toVector2f(debrisMotion);
			ResourceLocation sideTexture = ClientUtils.getSideTexture(state, EnumFacing.WEST);

			scheduleSpawnParticle(debrisParticle, new Vec3d(destroyed),
					debrisMotion, new Vector2f(IIParticleUtils.randFloat.get()*4, IIParticleUtils.randFloat.get()*4), 3)
					.withProperty(ParticleProperties.SIZE, debrisSize)
					.withProperty(ParticleProperties.TEXTURES, new ResourceLocation[]{sideTexture});

			if(spawnDebrisTrails&&(!adaptiveDebris||debrisIndex%2==0))
				scheduleSpawnParticle("smoke/smoke_trace", destroyedCenter,
						Vec3d.ZERO, debrisFacing, 1)
						.withProperty(ParticleProperties.SIZE, logSize*(adaptiveDebris?0.5f: 0.4f))
						.withProperty(ParticleProperties.MAX_LIFETIME, (int)(4*logSize)+(adaptiveDebris?8: 1));

			if(!adaptiveDebris&&spawnRichDebris)
			{
				scheduleSpawnParticle("smoke/smoke_trace", destroyedCenter,
						Vec3d.ZERO, debrisFacing, 1)
						.withProperty(ParticleProperties.COLOR, IIColor.fromPackedRGB(0x3f3f3f))
						.withProperty(ParticleProperties.SIZE, logSize*0.4f)
						.withProperty(ParticleProperties.MAX_LIFETIME, (int)(4*logSize)+20);

				for(int i = 0; i < 2; i++)
				{
					double extraFactor = MathHelper.clamp(
							factor*IIParticleUtils.randFloat.get()*2f, 0, 1);
					Vec3d extraMotion = dir.scale(1-extraFactor)
							.add(offCenterDirection.scale(extraFactor))
							.scale(1.05f*Math.max(1f, power/6f));
					scheduleSpawnParticle(debrisParticle, new Vec3d(destroyed),
							extraMotion,
							new Vector2f(IIParticleUtils.randFloat.get()*4, IIParticleUtils.randFloat.get()*4),
							3*i)
							.withProperty(ParticleProperties.TEXTURES, new ResourceLocation[]{
									ClientUtils.getSideTexture(state, EnumFacing.DOWN)
							});
				}
			}
			debrisIndex++;
		}
	}

	private static List<BlockPos> getExactExplosionSurface(World world, Vec3d pos, Vec3d explosionDirection,
	                                                       float radius, float power, ComponentEffectShape shape,
	                                                       Vec3d visualDirection)
	{
		IIExplosion explosion = new IIExplosion(world, null, pos, explosionDirection,
				radius, power, shape, false, true, false);
		Set<BlockPos> topBlocks = IIExplosion.getTopBlocks(
				explosion.generateAffectedBlockPositions(),
				EnumFacing.getFacingFromVector(
						(float)visualDirection.x,
						(float)visualDirection.y,
						(float)visualDirection.z
				)
		);
		return new ArrayList<>(topBlocks);
	}

	private static float getAffectedSurfaceExtent(Vec3d center, List<BlockPos> blocks)
	{
		double maxDistanceSq = 1.0;
		for(BlockPos block : blocks)
		{
			double x = block.getX()+0.5-center.x;
			double y = block.getY()+0.5-center.y;
			double z = block.getZ()+0.5-center.z;
			maxDistanceSq = Math.max(maxDistanceSq, x*x+y*y+z*z);
		}
		return (float)Math.sqrt(maxDistanceSq);
	}

	/**
	 * Spawns the complete white phosphorus effect without client-side effect entities.
	 */
	public static void spawnWhitePhosphorusFX(World world, Vec3d centerPos, Vec3d direction,
	                                          ComponentEffectShape shape, float size)
	{
		Vec3d mainPosition = centerPos.subtract(direction);
		Vec3d mainMotion = direction.scale(-0.75);

		switch(shape)
		{
			case ORB:
			case STAR:
			case CONE:
				spawnParticle("phosphorus/orb", centerPos, Vec3d.ZERO, new Vector2f(0, 0));
				mainMotion = new Vec3d(0, 0.4, 0);

				float particlesInLayer = 10f*size;
				int fragmentCount = MathHelper.ceil(2f*particlesInLayer);
				for(int i = 0; i < fragmentCount; i++)
				{
					Vec3d fragmentDirection = direction
							.scale(i < size*10f?-1: -2)
							.rotateYaw(i/particlesInLayer*360f);
					Vec3d fragmentPosition = centerPos.addVector(
							fragmentDirection.x, -direction.y, fragmentDirection.z
					);
					fragmentDirection = fragmentDirection.scale(2);
					Vec3d fragmentMotion = new Vec3d(
							fragmentDirection.x*0.125f,
							(fragmentDirection.y+(i < size*10f?2: 0.5))*0.25f,
							fragmentDirection.z*0.125f
					);
					scheduleWhitePhosphorusTrace(world, fragmentPosition, fragmentMotion);
				}
				break;
			case LINE:
			default:
				break;
		}

		scheduleWhitePhosphorusTrace(world, mainPosition, mainMotion);
	}

	private static void scheduleWhitePhosphorusTrace(World world, Vec3d initialPosition, Vec3d initialMotion)
	{
		Vec3d position = initialPosition;
		Vec3d motion = initialMotion;
		for(int tick = 0; tick < 12; tick++)
		{
			float progress = (tick+1)/12f;
			scheduleSpawnParticle("phosphorus/ember", position, motion, new Vector2f(0, 0), tick)
					.withProperty(ParticleProperties.SIZE, Math.max(0.05f, 0.25f-0.07f*progress));
			scheduleSpawnParticle("phosphorus/smoke_trace", position, Vec3d.ZERO, new Vector2f(0, 0), tick)
					.withProperty(ParticleProperties.SIZE, Math.max(0.25f, 2.5f-(1.75f*progress)))
					.withProperty(ParticleProperties.TEXTURE_SHIFT, (int)(6*progress))
					.withProperty(ParticleProperties.MAX_LIFETIME, (int)(60+60*IIParticleUtils.randFloat.get()));

			if(tick < 6)
			{
				int gracefulCount = Math.floorMod(IIParticleUtils.randInt.get(), 3);
				for(int i = 0; i < gracefulCount; i++)
					scheduleSpawnParticle("phosphorus/smoke_graceful",
							position.addVector(0, 0.5, 0).add(IIParticleUtils.getRandXZ().scale(0.5f)),
							Vec3d.ZERO, new Vector2f(0, 0), tick);
			}

			Vec3d nextPosition = position.add(motion);
			RayTraceResult hit = world.rayTraceBlocks(position, nextPosition, false, true, false);
			if(hit!=null)
				break;

			position = nextPosition;
			motion = motion.scale(0.99).addVector(0, -0.004, 0);
		}
	}

	public static void spawnAtomicExplosionFX(World world, Vec3d centerPos, float size)
	{
		ParticleDetail particleDetail = IIParticleUtils.getParticleDetailLevel(Graphics.nukeParticlesDetail);
		if(!particleDetail.isEnabled())
			return;

		spawnParticle("nuke/glow", centerPos, Vec3d.ZERO, new Vector2f(0, 0))
				.withProperty(ParticleProperties.SIZE, size)
				.withProperty(ParticleProperties.MAX_LIFETIME, 400);

		//Compute the particle budgets
		float effectExtent = MathHelper.clamp(size*AmmoComponentNuke.EXPLOSION_SIZE, 1f, AmmoComponentNuke.EXPLOSION_POWER+1f);
		float logSize = 1f+MathHelper.log2(Math.max(1, (int)effectExtent));

		//Shockwave
		final int tick = particleDetail.isHigh()?4: particleDetail.isMedium()?6: 8;
		for(int i = 0; i < 40; i += tick)
			ParticleRegistry.scheduleSpawnParticle("nuke/shockwave_main",
					centerPos.addVector(0, 1.5*size, 0), Vec3d.ZERO, new Vector2f(0, 0),
					i);

		//Dust Cloud
		float cloudSize = 3.5f;
		int steps = (int)Math.ceil(AmmoComponentNuke.EXPLOSION_SIZE/cloudSize);
		for(int i = 0; i < steps; i++)
		{
			int delay = 2*i;
			int perRing = (int)((i+1)*2*Math.PI);
			for(int p = 0; p < perRing; p++)
			{
				Vec3d pos = PositionGenerator.CIRCLE_XZ.generatePosition(centerPos, p, i*cloudSize, perRing);
				scheduleSpawnParticle("nuke/dust_cloud", pos.addVector(0, (steps-i)*0.25f-2.0f, 0),
						Vec3d.ZERO, new Vector2f(0, 0), delay)
						.withProperty(ParticleProperties.SIZE, cloudSize)
						.withProperty(ParticleProperties.MAX_LIFETIME, (int)(100*logSize)+100);
			}
		}

		//Bright core
		float coreSize = 6f*size;
		int lifetime = (int)(65*logSize)+75;
		for(int p = 0; p < 8*size; p++)
		{
			Vec3d pos = PositionGenerator.CIRCLE_XZ.generatePosition(centerPos, p, coreSize+1, (int)(8*size));

			scheduleSpawnParticle("nuke/dust_cloud", pos.addVector(0, -1.0f, 0),
					Vec3d.ZERO, new Vector2f(0, 0), 60)
					.withProperty(ParticleProperties.SIZE, coreSize+1)
					.withProperty(ParticleProperties.MAX_LIFETIME, lifetime);
			scheduleSpawnParticle("nuke/nuke_core", pos.addVector(0, -2.0f, 0),
					Vec3d.ZERO, new Vector2f(0, 0), 60)
					.withProperty(ParticleProperties.SIZE, coreSize)
					.withProperty(ParticleProperties.MAX_LIFETIME, lifetime);

			scheduleSpawnParticle("nuke/post_cloud", pos.addVector(0, -1.0f, 0),
					Vec3d.ZERO, new Vector2f(0, 0), 60+lifetime-20)
					.withProperty(ParticleProperties.SIZE, coreSize)
					.withProperty(ParticleProperties.MAX_LIFETIME, lifetime+20);
		}

		//Nuclear mushroom pillar, rising from the explosion centre.
		float mushroomHeight = effectExtent*0.95f;
		for(float i = 0; i <= mushroomHeight; i += coreSize)
		{
			int pillarLifetime = lifetime-MathHelper.clamp((int)((i/mushroomHeight)*lifetime/2), 0, lifetime/2);
			scheduleSpawnParticle("nuke/nuke_core", centerPos.addVector(0, i-coreSize, 0),
					new Vec3d(0, 0.01f*size, 0), new Vector2f(0, 0),
					(int)(60+i))
					.withProperty(ParticleProperties.SIZE, coreSize)
					.withProperty(ParticleProperties.MAX_LIFETIME, pillarLifetime);

			scheduleSpawnParticle("nuke/post_cloud", centerPos.addVector(0, i-coreSize, 0),
					Vec3d.ZERO, new Vector2f(0, 0), (int)(60+i+pillarLifetime-20))
					.withProperty(ParticleProperties.SIZE, coreSize)
					.withProperty(ParticleProperties.MAX_LIFETIME, lifetime+20);
		}

		//Nuclear mushroom top
		int initialMushroomTime = 60+MathHelper.ceil(mushroomHeight);
		int mushroomLifetime = Math.max(20, lifetime/2);
		steps = (int)Math.ceil(AmmoComponentNuke.EXPLOSION_SIZE*2/coreSize);
		for(int i = 0; i < steps; i++)
		{
			int delay = initialMushroomTime+2*i;
			int ringLifetime = mushroomLifetime+4*(steps-1-i);
			int perRing = (int)((i+1)*2*Math.PI);
			for(int p = 0; p < perRing; p++)
			{
				Vec3d pos = PositionGenerator.CIRCLE_XZ.generatePosition(centerPos, p, i*coreSize*0.5f, perRing);
				Vec3d mushroomPos = pos.addVector(0, mushroomHeight+(steps-i)*0.25f-2.0f, 0);

				scheduleSpawnParticle("nuke/nuke_core", mushroomPos,
						Vec3d.ZERO, new Vector2f(0, 0), delay)
						.withProperty(ParticleProperties.SIZE, coreSize*0.5f)
						.withProperty(ParticleProperties.MAX_LIFETIME, ringLifetime);

				scheduleSpawnParticle("nuke/post_cloud", mushroomPos,
						Vec3d.ZERO, new Vector2f(0, 0), delay+ringLifetime-20)
						.withProperty(ParticleProperties.SIZE, coreSize*0.5f)
						.withProperty(ParticleProperties.MAX_LIFETIME, lifetime+20);
			}
		}
	}

	/**
	 * Spawns an EMP flash, shockwave, and one lightning arc for each affected target.
	 */
	public static void spawnEMPExplosionFX(World world, Vec3d centerPos, float radius, List<Vec3d> affectedTargets)
	{
		ParticleDetail particleDetail = IIParticleUtils.getParticleDetailLevel(Graphics.explosionParticlesDetail);
		if(!particleDetail.isEnabled())
			return;

		float effectRadius = Math.max(1f, radius);
		IIColor coreColor = IIColor.fromPackedRGB(0xDDF7FF);
		IIColor edgeColor = IIColor.fromPackedRGB(0x2F7FFF);

		AbstractParticle glow = spawnParticle("emp/glow", centerPos, Vec3d.ZERO, new Vector2f(0, 0));
		if(glow!=null)
			glow.withProperty(ParticleProperties.SIZE, MathHelper.clamp(effectRadius*0.015f, 0.125f, 0.75f))
					.withProperty(ParticleProperties.COLOR, coreColor)
					.withProperty(ParticleProperties.COLOR_SECONDARY, edgeColor);

		AbstractParticle shockwave = spawnParticle("explosion/shockwave", centerPos.addVector(0, 0.1, 0),
				Vec3d.ZERO, new Vector2f(0, 0));
		if(shockwave!=null)
			shockwave.withProperty(ParticleProperties.SIZE, effectRadius*0.75f)
					.withProperty(ParticleProperties.COLOR, edgeColor)
					.withProperty(ParticleProperties.MAX_LIFETIME, 12);

		if(affectedTargets==null)
			return;
		for(Vec3d target : affectedTargets)
		{
			if(target==null)
				continue;
			spawnLightning("emp/lightning", centerPos, target);
			spawnLightning("emp/lightning_core", centerPos, target);
		}
	}

	private static void spawnLightning(String particleName, Vec3d start, Vec3d end)
	{
		AbstractParticle lightning = spawnParticle(particleName, start, Vec3d.ZERO, new Vector2f(0, 0));
		if(lightning!=null)
			lightning.withProperty(ParticleProperties.STRETCH,
					new Vector3f((float)end.x, (float)end.y, (float)end.z));
	}

	/**
	 * Spawns a coloured shrapnel burst or a slow-falling glitter cloud.
	 */
	public static void spawnShrapnelFX(Vec3d centerPos, IIColor color, float size, boolean fallsSlowly)
	{
		ParticleDetail detail = IIParticleUtils.getParticleDetailLevel(Graphics.explosionParticlesDetail);
		if(!detail.isEnabled())
			return;

		float playerDistance = (float)ClientUtils.mc().player.getDistance(centerPos.x, centerPos.y, centerPos.z);
		float budgetScale = IIParticleUtils.getParticleBudgetScale(detail, playerDistance, 64f, 128f);
		int count = MathHelper.clamp(MathHelper.ceil(20f*Math.max(0.25f, size)*budgetScale), 4, 128);
		String particleName = fallsSlowly?"shrapnel/glitter": "shrapnel/burst";

		for(int i = 0; i < count; i++)
		{
			Vec3d position = centerPos;
			Vec3d motion;
			AbstractParticle particle;
			if(fallsSlowly)
			{
				position = position.add(IIParticleUtils.getRandXZ().scale(Math.max(0.5f, size)));
				motion = new Vec3d(
						ClientUtils.mc().world.rand.nextGaussian()*0.06,
						0.2+IIParticleUtils.randFloat.get()*0.2,
						ClientUtils.mc().world.rand.nextGaussian()*0.06
				);
				particle = scheduleSpawnParticle(particleName, position, motion, new Vector2f(0, 0), i%5);
			}
			else
			{
				motion = new Vec3d(
						ClientUtils.mc().world.rand.nextGaussian(),
						ClientUtils.mc().world.rand.nextGaussian(),
						ClientUtils.mc().world.rand.nextGaussian()
				).normalize().scale(0.15+IIParticleUtils.randFloat.get()*0.35);
				particle = spawnParticle(particleName, position, motion, new Vector2f(0, 0));
			}

			if(particle!=null)
				particle.withProperty(ParticleProperties.COLOR, color)
						.withProperty(ParticleProperties.SIZE, fallsSlowly?0.12f: 0.18f);
		}
	}

	public static void spawnGasCloud(Vec3d pos, float size, Fluid fluid)
	{
		//Check if fluid is not null
		if(fluid==null)
			return;

		IIColor color = IIClientUtils.getFluidTextureColor(fluid);

		for(int i = 0; i < 10; i++)
			spawnParticle("smoke/gas_cloud", pos.add(IIParticleUtils.getRandXZ().scale(size/2)), Vec3d.ZERO, new Vector2f(0, 0))
					.withProperty(ParticleProperties.SIZE, size*1.5f)
					.withProperty(ParticleProperties.MAX_LIFETIME, (int)size*55)
					.withProperty(ParticleProperties.COLOR, color);
	}

	//--- Utils ---//

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
