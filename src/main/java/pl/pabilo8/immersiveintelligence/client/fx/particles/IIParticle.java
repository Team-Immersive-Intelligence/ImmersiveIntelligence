package pl.pabilo8.immersiveintelligence.client.fx.particles;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.fx.builder.ParticleBuilder;
import pl.pabilo8.immersiveintelligence.client.fx.utils.DrawStages;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleSystem;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static pl.pabilo8.immersiveintelligence.client.fx.utils.IIParticleProperties.*;

/**
 * @author Pabilo8
 * @updated 05.04.2024
 * @ii-approved 0.3.1
 * @since 23.12.2020
 */
@SideOnly(Side.CLIENT)
public abstract class IIParticle implements INBTSerializable<NBTTagCompound>
{
	//--- Constants ---//
	/**
	 * Default bounding box for particles
	 */
	private static final AxisAlignedBB DEFAULT_AABB = new AxisAlignedBB(-0.25f, -0.25f, -0.25f, 0.25f, 0.25f, 0.25f);

	//--- Static Fields ---//
	/**
	 * Camera view position for current draw frame.<br>
	 * Updated by {@link ParticleSystem}
	 */
	public static double interpPosX, interpPosY, interpPosZ;
	/**
	 * Camera view direction for current draw frame.<br>
	 * Updated by {@link ParticleSystem}
	 */
	public static Vec3d cameraViewDir;
	public static float interpTicks;

	//--- Instance Fields ---//
	/**
	 * the world this particle is in
	 */
	protected World world;
	/**
	 * previous tick position of this particle
	 */
	protected double prevPosX, prevPosY, prevPosZ;
	/**
	 * current position of the particle
	 */
	protected double posX, posY, posZ;
	/**
	 * motion of the particle
	 */
	protected double motionX, motionY, motionZ;
	/**
	 * rotation of the particle
	 */
	protected double rotationX, rotationY, rotationZ;
	/**
	 * how many ticks should the particle be displayed for
	 */
	protected int lifeTime, maxLifeTime;
	/**
	 * whether the particle is on ground
	 */
	protected boolean onGround;
	/**
	 * this particle's bounding box without offsets
	 */
	private AxisAlignedBB baseBoundingBox;
	/**
	 * the collision / bounding box of this particle
	 */
	protected AxisAlignedBB boundingBox;

	//--- Chaining Fields ---//
	private Multimap<Integer, Consumer<? super IIParticle>> scheduledParticles;
	private Set<Consumer<? super IIParticle>> chainedParticles;

	//--- Drawing Related Fields ---//
	/**
	 * at which stage of the rendering process should this particle be drawn
	 */
	protected DrawStages drawStage;
	/**
	 * whether this particle is drawn by {@link pl.pabilo8.immersiveintelligence.client.util.amt.AMTParticle}, disables position offset
	 */
	private boolean amtDrawMode = false;

	/**
	 * Extending constructors should be passed as a parameter to {@link ParticleBuilder}
	 */
	public IIParticle(World world, Vec3d pos)
	{
		this.world = world;
		setPMR(pos, Vec3d.ZERO, Vec3d.ZERO);
		this.baseBoundingBox = null;
		this.boundingBox = null;
	}

	//--- Handling Methods ---//

	/**
	 * Called every tick to update the particle's position and motion
	 */
	public void onUpdate()
	{
		if(isAlive())
		{
			this.prevPosX = this.posX;
			this.prevPosY = this.posY;
			this.prevPosZ = this.posZ;

			updateMotionVector();
			move();

			if(scheduledParticles!=null)
				for(Consumer<? super IIParticle> consumer : scheduledParticles.get(this.lifeTime))
					consumer.accept(this);

			this.lifeTime++;
		}
	}

	/**
	 * Override for non-standard motion vector updates
	 */
	protected void updateMotionVector()
	{

	}

	/**
	 * Performs movement of the particle using the motion vector
	 */
	private void move()
	{
		if(motionX==0&&motionY==0&&motionZ==0)
			return;

		//check for collisions against other bounding boxes in motion area
		if(this.boundingBox!=null)
		{
			//initial motion values
			double initX = motionX, initY = motionY, initZ = motionZ;
			List<AxisAlignedBB> list = this.world.getCollisionBoxes(null, this.boundingBox.expand(motionX, motionY, motionZ));

			//if boxes intersect, offset the box, so it doesn't
			for(AxisAlignedBB axisalignedbb : list)
			{
				motionY = axisalignedbb.calculateYOffset(this.boundingBox, motionY);
				this.boundingBox = this.baseBoundingBox.offset(posX, posY+motionY, posZ);
				motionX = axisalignedbb.calculateXOffset(this.boundingBox, motionX);
				this.boundingBox = this.baseBoundingBox.offset(posX+motionX, posY+motionY, posZ);
				motionZ = axisalignedbb.calculateZOffset(this.boundingBox, motionZ);
				this.boundingBox = this.baseBoundingBox.offset(posX+motionX, posY+motionY, posZ+motionZ);
			}
			if(list.isEmpty())
				this.boundingBox = this.baseBoundingBox.offset(posX+motionX, posY+motionY, posZ+motionZ);

			//check Y axis motion
			this.onGround = initY!=motionY&&initY < 0.0D;

			//stop X and/or Z axis motion if colision was present (partial movement)
			if(initX!=motionX)
				this.motionX = 0.0D;
			if(initZ!=motionZ)
				this.motionZ = 0.0D;
		}

		this.posX += motionX;
		this.posY += motionY;
		this.posZ += motionZ;
	}

	/**
	 * Always called when rotation is changed, used to update 3D models' rotation
	 */
	protected void notifyUpdateRotation()
	{

	}

	//--- Chained and Delayed Particles ---//

	/**
	 * Sets particles that will appear after this one dies
	 *
	 * @param chained particles to be spawned
	 */
	public <T extends IIParticle> void setChainedParticles(Set<Consumer<T>> chained)
	{
		//java generics can go to hell
		this.chainedParticles = chained.stream().map(tConsumer -> (Consumer<IIParticle>)tConsumer).collect(Collectors.toSet());
	}

	/**
	 * Sets particles that will appear after a delay
	 *
	 * @param delayed map of time in ticks and particles to be spawned
	 */
	public <T extends IIParticle> void setScheduledParticles(Multimap<Integer, Consumer<T>> delayed)
	{
		this.scheduledParticles = HashMultimap.create();
		//java generics can go to hell mk.2
		delayed.forEach((integer, tConsumer) ->
				this.scheduledParticles.put(integer, iiParticle -> tConsumer.accept((T)iiParticle))
		);
	}

	//--- Getters and Setters ---//

	/**
	 * @return the current position of this particle
	 */
	public Vec3d getPosition()
	{
		return new Vec3d(posX, posY, posZ);
	}

	/**
	 * @return the current motion of this particle
	 */
	public Vec3d getMotion()
	{
		return new Vec3d(motionX, motionY, motionZ);
	}

	/**
	 * @return the current rotation of this particle
	 */
	public Vec3d getRotation()
	{
		return new Vec3d(rotationX, rotationY, rotationZ);
	}

	/**
	 * @param partialTicks partial render ticks
	 * @return lifetime fraction of this particle (0.0 to 1.0)
	 */
	public float getProgress(float partialTicks)
	{
		return MathHelper.clamp((this.lifeTime+partialTicks)/(float)this.maxLifeTime,
				0.0F, 1.0F);
	}

	/**
	 * @param property fraction of lifetime (0.0 to 1.0)
	 */
	public void setProgress(float property)
	{
		this.lifeTime = (int)(MathHelper.clamp(property, 0, 1)*maxLifeTime);
	}

	/**
	 * @return whether this particle should be displayed
	 */
	public boolean isAlive()
	{
		return this.lifeTime < this.maxLifeTime;
	}

	/**
	 * Sets the position, motion and rotation of this particle
	 *
	 * @param pos      position of this particle
	 * @param motion   motion of this particle
	 * @param rotation rotation of this particle
	 */
	public final void setPMR(Vec3d pos, Vec3d motion, @Nullable Vec3d rotation)
	{
		this.posX = pos.x;
		this.posY = pos.y;
		this.posZ = pos.z;
		this.prevPosX = pos.x;
		this.prevPosY = pos.y;
		this.prevPosZ = pos.z;
		this.motionX = motion.x;
		this.motionY = motion.y;
		this.motionZ = motion.z;

		if(rotation==null)
		{
			//substitute rotation with motion
			this.rotationX = this.motionX;
			this.rotationY = this.motionY;
			this.rotationZ = this.motionZ;
		}
		else
		{
			//use rotation
			this.rotationX = rotation.x;
			this.rotationY = rotation.y;
			this.rotationZ = rotation.z;
		}
		notifyUpdateRotation();
	}

	/**
	 * Sets the rotation of this particle
	 *
	 * @param rotationX X-axis rotation
	 * @param rotationY Y-axis rotation
	 * @param rotationZ Z-axis rotation
	 */
	public final void setRotation(double rotationX, double rotationY, double rotationZ)
	{
		this.rotationX = rotationX;
		this.rotationY = rotationY;
		this.rotationZ = rotationZ;
		notifyUpdateRotation();
	}

	/**
	 * Sets the lifetime of this particle
	 *
	 * @param lifetime    current lifetime
	 * @param maxLifetime maximum lifetime
	 */
	public final void setLifeTime(int lifetime, int maxLifetime)
	{
		this.lifeTime = lifetime;
		this.maxLifeTime = maxLifetime;
	}

	/**
	 * @return the max lifetime of this particle
	 */
	public final int getMaxLifeTime()
	{
		return maxLifeTime;
	}

	/**
	 * @return current lifetime of this particle
	 */
	public final int getLifeTime()
	{
		return lifeTime;
	}

	/**
	 * Sets the particle world
	 */
	public final void setWorld(World world)
	{
		this.world = world;
	}

	/**
	 * Sets this particle's bounding box
	 *
	 * @param aabb bounding box centered at {@link Vec3d#ZERO}
	 */
	public void setAABB(AxisAlignedBB aabb)
	{
		this.baseBoundingBox = aabb;
		if(this.baseBoundingBox!=null)
			this.boundingBox = this.baseBoundingBox.offset(posX, posY, posZ);
	}

	/**
	 * Sets drawing stage for this particle for {@link ParticleSystem} to sort it.
	 *
	 * @param drawStage stage to draw this particle at
	 */
	@Nonnull
	public void setDrawStage(DrawStages drawStage)
	{
		this.drawStage = drawStage;
	}

	/**
	 * @return at which stage of the rendering process should this particle be drawn
	 */
	@Nonnull
	public DrawStages getDrawStage()
	{
		return drawStage;
	}

	/**
	 * Fired when this particle has been passed new properties.
	 *
	 * @param properties new property NBT
	 */
	public void setProperties(EasyNBT properties)
	{

	}

	public void inherit(IIParticle parent, String... parameters)
	{
		this.setProperties(parent.getProperties(EasyNBT.newNBT())
				.filter(parameters));
	}

	//--- NBT Handling ---//

	@Override
	public final NBTTagCompound serializeNBT()
	{
		return getProperties(EasyNBT.newNBT()
				.withVec3d(POSITION, posX, posY, posZ)
				.withVec3d(MOTION, motionX, motionY, motionZ)
				.withVec3d(ROTATION, rotationX, rotationY, rotationZ)
				.withInt(LIFETIME_MAX, maxLifeTime)
				.withInt(LIFETIME, lifeTime)
		).unwrap();
	}

	@Override
	public final void deserializeNBT(NBTTagCompound nbt)
	{
		EasyNBT eNBT = EasyNBT.wrapNBT(nbt);
		setPMR(eNBT.getVec3d(POSITION), eNBT.getVec3d(MOTION), eNBT.getVec3d(ROTATION));
		setLifeTime(eNBT.getInt(LIFETIME), eNBT.getInt(LIFETIME_MAX));
		setProperties(eNBT);
	}

	/**
	 * @return array of properties to be saved to NBT
	 */
	public EasyNBT getProperties(EasyNBT eNBT)
	{
		return eNBT;
	}

	//--- Abstract Methods ---//

	/**
	 * Used to render this particle to the buffer.
	 *
	 * @param buffer       buffer to render to
	 * @param partialTicks partial render ticks
	 * @param x            X rotation angle
	 * @param xz           XZ rotation angle
	 * @param z            Z rotation angle
	 * @param yz           YZ rotation angle
	 * @param xy           XY rotation angle
	 * @implNote Using {@link Tessellator#draw()} directly is not advised (performance issues), but possible, if you do: <pre>{@code
	 * tes.draw();
	 * GlStateManager.pushMatrix();
	 * buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	 * //...
	 * Tessellator.getInstance().draw();
	 * buffer.begin(GL11.GL_QUADS, format);
	 * }</pre>
	 */
	public abstract void render(BufferBuilder buffer, float partialTicks, float x, float xz, float z, float yz, float xy);

	//--- Utility Methods ---//

	/**
	 * @return the current position of this particle
	 */
	public Vec3d getRenderPosition()
	{
		if(amtDrawMode)
			return Vec3d.ZERO;
		return new Vec3d(posX+(motionX*interpTicks)-interpPosX, posY+(motionY*interpTicks)-interpPosY, posZ+(motionZ*interpTicks)-interpPosZ);
	}

	public void enableAMTDrawMode()
	{
		this.amtDrawMode = true;
	}

	public void onDeath()
	{
		if(chainedParticles!=null)
			this.chainedParticles.forEach(consumer -> consumer.accept(this));
	}

	public World getWorld()
	{
		return world;
	}
}
