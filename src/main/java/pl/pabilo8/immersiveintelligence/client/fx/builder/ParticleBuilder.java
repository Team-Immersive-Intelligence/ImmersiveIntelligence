package pl.pabilo8.immersiveintelligence.client.fx.builder;

import blusunrize.immersiveengineering.client.ClientUtils;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.fx.particles.IIParticle;
import pl.pabilo8.immersiveintelligence.client.fx.prefab.IProgrammableParticle;
import pl.pabilo8.immersiveintelligence.client.fx.utils.DrawStages;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleRegistry;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleSystem;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Used to build and spawn particle effects.
 *
 * @author Pabilo8
 * @ii-approved 0.3.1
 * @since 05.04.2024
 */
@SideOnly(Side.CLIENT)
public class ParticleBuilder<T extends IIParticle>
{
	/**
	 * Particles that are spawned after this one dies
	 */
	private final Set<Consumer<T>> chained = new HashSet<>();
	/**
	 * Particles that are spawned after a delay
	 */
	private final Multimap<Integer, Consumer<T>> scheduled = HashMultimap.create();
	/**
	 * The type of particle to spawn
	 */
	private final BiFunction<World, Vec3d, T> particleType;
	/**
	 * How many ticks should the particle be displayed for
	 */
	private Supplier<Integer> maxLifeTime;
	/**
	 * Additional properties of the particle
	 */
	private EasyNBT otherProperties = EasyNBT.newNBT();
	/**
	 * Dynamically provided additional properties of the particle
	 */
	private final Set<BiConsumer<T, EasyNBT>> propertyProviders = new HashSet<>();
	/**
	 * The stage at which the particle should be drawn
	 */
	private DrawStages drawStage = DrawStages.VANILLA;
	/**
	 * The bounding box of the particle
	 */
	private AxisAlignedBB boundingBox;
	/**
	 * The programs run by the particle, they can be used to animate and alter the particle
	 */
	private final List<BiConsumer<T, Float>> programs = new ArrayList<>();

	public ParticleBuilder(BiFunction<World, Vec3d, T> particleConstructor)
	{
		this.particleType = particleConstructor;
	}

	//--- Other Properties ---//

	public ParticleBuilder<T> withLifeTime(int time)
	{
		return withLifeTime(() -> time);
	}

	public ParticleBuilder<T> withLifeTime(int min, int max)
	{
		return withLifeTime(() -> min+ClientUtils.mc().world.rand.nextInt(max-min));
	}

	public ParticleBuilder<T> withLifeTime(Supplier<Integer> time)
	{
		this.maxLifeTime = time;
		return this;
	}

	public ParticleBuilder<T> withDrawStage(DrawStages drawStage)
	{
		this.drawStage = drawStage;
		return this;
	}

	public ParticleBuilder<T> withBoundingBox(AxisAlignedBB boundingBox)
	{
		this.boundingBox = boundingBox;
		return this;
	}

	public ParticleBuilder<T> withBoundingBox(float radius)
	{
		return withBoundingBox(new AxisAlignedBB(-radius, -radius, -radius, radius, radius, radius));
	}

	//--- Common NBT Properties ---//

	public ParticleBuilder<T> withProperty(String name, Object property)
	{
		otherProperties.withAny(name, property);
		return this;
	}

	public ParticleBuilder<T> withProperty(Consumer<EasyNBT> consumer)
	{
		consumer.accept(otherProperties);
		return this;
	}

	public ParticleBuilder<T> withDynamicProperty(BiConsumer<T, EasyNBT> function)
	{
		propertyProviders.add(function);
		return this;
	}


	//--- Chained Particles ---//

	public ParticleBuilder<T> withChained(Consumer<T> particleFunction)
	{
		chained.add(particleFunction);
		return this;
	}

	public ParticleBuilder<T> withScheduled(Consumer<T> particleFunction, int delay)
	{
		scheduled.put(delay, particleFunction);
		return this;
	}

	//--- Programs / Animations ---//

	public ParticleBuilder<T> withProgram(BiConsumer<T, Float> program)
	{
		this.programs.add(program);
		return this;
	}

	//--- Parsing Files ---//

	/**
	 * Sets builder values using NBT (JSON file)
	 *
	 * @param nbt the NBT to parse
	 */
	public void parseBuilderFromJSON(EasyNBT nbt)
	{
		nbt.checkSetInt("life_time", this::withLifeTime);
		if(nbt.hasKey("max_life_time")&&nbt.hasKey("min_life_time"))
		{
			int minLifeTime = nbt.getInt("min_life_time");
			int maxLifeTime = nbt.getInt("max_life_time");
			withLifeTime(minLifeTime, maxLifeTime);
		}

		nbt.checkSetEnum("draw_stage", DrawStages.class, this::withDrawStage);
		nbt.checkSetFloat("aabb", this::withBoundingBox);

		nbt.streamList(NBTTagString.class, "programs", EasyNBT.TAG_STRING)
				.map(NBTTagString::getString)
				.map(ParticleRegistry::getProgram)
				.filter(Objects::nonNull)
				.forEach(consumer -> programs.add(((BiConsumer<T, Float>)consumer)));

		nbt.checkSetCompound("properties", otherProperties::mergeWith);

		//TODO: 19.05.2024 include all tags
		/*if(nbt.hasKey("chained"))
			nbt.getSubTags("chained").forEach(subTag -> chained.add(IIParticle.parseParticleFromJSON(subTag)));
		if(nbt.hasKey("scheduled"))
			nbt.getSubTags("scheduled").forEach(subTag -> scheduled.put(subTag.getInteger("delay"), IIParticle.parseParticleFromJSON(subTag)));
		if(nbt.hasKey("dynamicProperties"))
			nbt.getSubTags("dynamicProperties").forEach(subTag -> propertyProviders.add(IIParticle.parseParticleFromJSON(subTag)));*/
	}

	//--- Creating Particles ---//

	/**
	 * Creates an instance of the particle without spawning it
	 *
	 * @param position  position of the particle
	 * @param motion    motion of the particle
	 * @param direction facing direction of the particle
	 * @return the created particle
	 */
	@Nonnull
	public T buildParticle(Vec3d position, Vec3d motion, Vec3d direction)
	{
		T particle = particleType.apply(ClientUtils.mc().world, position);
		particle.setPMR(position, motion, direction);
		//set collision
		particle.setAABB(boundingBox);
		//set chained
		particle.setChainedParticles(chained);
		particle.setScheduledParticles(scheduled);

		//set program
		if(particle instanceof IProgrammableParticle)
			programs.forEach(prog -> ((IProgrammableParticle<T>)particle).setProgram(prog));

		//set life
		particle.setLifeTime(0, maxLifeTime.get());
		//set draw stage
		particle.setDrawStage(drawStage);
		//set additional properties

		EasyNBT nbt = EasyNBT.newNBT().mergeWith(otherProperties);
		propertyProviders.forEach(consumer -> consumer.accept(particle, nbt));
		particle.setProperties(nbt);
		return particle;
	}

	/**
	 * Creates and spawns a particle in the world
	 *
	 * @param position position of the particle
	 * @param motion   motion and direction of the particle
	 * @return the created particle
	 */
	@Nonnull
	public T spawnParticle(Vec3d position, Vec3d motion)
	{
		return spawnParticle(position, motion, motion.normalize());
	}

	/**
	 * Creates and spawns a particle in the world
	 *
	 * @param position  position of the particle
	 * @param motion    motion of the particle
	 * @param direction direction of the particle
	 * @return the created particle
	 */
	@Nonnull
	public T spawnParticle(Vec3d position, Vec3d motion, Vec3d direction)
	{
		T particle = buildParticle(position, motion, direction);
		ParticleSystem.INSTANCE.addEffect(particle);
		return particle;
	}

	/**
	 * Schedules a particle to be spawned after a delay
	 *
	 * @param position position of the particle
	 * @param motion   motion of the particle
	 * @param delay    delay in ticks
	 * @return the created particle
	 */
	@Nonnull
	public T scheduleSpawnParticle(Vec3d position, Vec3d motion, int delay)
	{
		return scheduleSpawnParticle(position, motion, motion.normalize(), delay);
	}

	/**
	 * Schedules a particle to be spawned after a delay
	 *
	 * @param position  position of the particle
	 * @param motion    motion of the particle
	 * @param direction direction of the particle
	 * @param delay     delay in ticks
	 * @return the created particle
	 */
	@Nonnull
	public T scheduleSpawnParticle(Vec3d position, Vec3d motion, Vec3d direction, int delay)
	{
		T particle = buildParticle(position, motion, direction);
		ParticleSystem.INSTANCE.scheduleEffect(particle, delay);
		return particle;
	}
}
