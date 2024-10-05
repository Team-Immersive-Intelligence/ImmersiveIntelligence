package pl.pabilo8.immersiveintelligence.api.ammo.utils;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentEffectShape;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoType;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.EntityAmmoBase;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @param <E> The ammo entity class
 * @author Pabilo8
 * @ii-approved 0.3.1
 * @since 02.01.2024
 */
public class AmmoFactory<E extends EntityAmmoBase<? super E>>
{
//--- Parameters ---//

	/**
	 * The world to spawn the ammo in
	 */
	private final Supplier<World> world;
	/**
	 * The ammo to create, taken from stack or passed directly
	 */
	private IAmmoType<?, E> ammo;
	/**
	 * The stack to create the ammo from
	 */
	private ItemStack stack;
	/**
	 * The position to spawn the ammo at<br>
	 * The direction to spawn the ammo in
	 */
	private Vec3d pos, dir;
	/**
	 * The velocity multiplier of the ammo
	 */
	private float velocityModifier;
	/**
	 * The owner (shooter, placer of a mine or explosive, etc.)
	 */
	private Entity owner;
	/**
	 * The list of blocks a projectile should ignore, used by turrets
	 */
	private List<BlockPos> ignoredBlocks;
	/**
	 * The list of entities a projectile should ignore<br>
	 * the owner is always ignored and doesn't have to be added to this list
	 */
	private List<Entity> ignoredEntities;

//--- Constructor ---//

	/**
	 * Creates a new ammo factory that will be attached to the passed entity
	 *
	 * @param entity The entity to attach the factory to
	 */
	public AmmoFactory(Entity entity)
	{
		this(entity::getEntityWorld);
		setOwner(entity);
	}

	public AmmoFactory(Supplier<World> worldSupplier)
	{
		this.world = worldSupplier;
		this.pos = null;
		this.ammo = null;
		this.stack = ItemStack.EMPTY;
		this.owner = null;
		this.ignoredBlocks = null;
		this.ignoredEntities = null;
		this.velocityModifier = 1f;
		this.dir = Vec3d.ZERO;
	}

	public AmmoFactory(World world)
	{
		this(() -> world);
	}

//--- Factory Methods ---//

	/**
	 * Sets the stack to create the ammo from
	 *
	 * @param stack The stack to create the ammo from
	 * @return The factory
	 */
	public AmmoFactory<E> setStack(ItemStack stack)
	{
		this.stack = stack;
		this.ammo = ((IAmmoType<?, E>)stack.getItem());
		return this;
	}

	/**
	 * Sets the position to spawn the ammo at
	 *
	 * @param pos The position to spawn the ammo at
	 * @return The factory
	 */
	public AmmoFactory<E> setPosition(Vec3d pos)
	{
		this.pos = pos;
		return this;
	}

	/**
	 * Sets the position to spawn the ammo at
	 *
	 * @param pos The position to spawn the ammo at
	 * @return The factory
	 */
	public AmmoFactory<E> setPosition(BlockPos pos)
	{
		this.pos = new Vec3d(pos).addVector(0.5, 0.5, 0.5);
		return this;
	}

	/**
	 * Sets the direction to spawn the ammo in
	 *
	 * @param dir The direction to spawn the ammo in
	 * @return The factory
	 */
	public AmmoFactory<E> setDirection(Vec3d dir)
	{
		this.dir = dir;
		return this;
	}

	/**
	 * Sets the direction to spawn the ammo in
	 *
	 * @param facing The direction to spawn the ammo in
	 * @return The factory
	 */
	public AmmoFactory<E> setDirection(EnumFacing facing)
	{
		this.dir = new Vec3d(facing.getDirectionVec());
		return this;
	}

	/**
	 * Sets the velocity multiplier of the ammo
	 *
	 * @param velocityModifier The velocity multiplier of the ammo
	 * @return The factory
	 */
	public AmmoFactory<E> setVelocityModifier(float velocityModifier)
	{
		this.velocityModifier = velocityModifier;
		return this;
	}

	/**
	 * Overloaded method for setting position and direction at once
	 *
	 * @param pos              The position to spawn the ammo at
	 * @param dir              The direction to spawn the ammo in
	 * @param velocityModifier The velocity multiplier of the ammo
	 * @return The factory
	 */
	public AmmoFactory<E> setPositionAndVelocity(Vec3d pos, Vec3d dir, float velocityModifier)
	{
		setPosition(pos);
		setDirection(dir);
		setVelocityModifier(velocityModifier);
		return this;
	}

	/**
	 * Sets the owner of the ammo
	 *
	 * @param owner The owner of the ammo
	 * @return The factory
	 */
	public AmmoFactory<E> setOwner(Entity owner)
	{
		this.owner = owner;
		return this;
	}

	/**
	 * Sets the owner of the ammo
	 *
	 * @param shooter The entity controlling the gun
	 * @param gun     The gun entity
	 * @return The factory
	 */
	public AmmoFactory<E> setShooterAndGun(Entity shooter, Entity gun)
	{
		this.owner = shooter;
		this.ignoredEntities = new ArrayList<>(gun.getRecursivePassengers());
		return this;
	}

	/**
	 * Sets the list of blocks a projectile should ignore, used by turrets
	 *
	 * @param ignoredBlocks The list of blocks a projectile should ignore
	 * @return The factory
	 */
	public AmmoFactory<E> setIgnoredBlocks(Collection<BlockPos> ignoredBlocks)
	{
		this.ignoredBlocks = new ArrayList<>(ignoredBlocks);
		return this;
	}

	/**
	 * Sets the list of entities a projectile should ignore<br>
	 * the owner is always ignored and doesn't have to be added to this list
	 *
	 * @param ignoredEntities The list of entities a projectile should ignore
	 * @return The factory
	 */
	public <I extends Entity> AmmoFactory<E> setIgnoredEntities(Collection<I> ignoredEntities)
	{
		this.ignoredEntities = new ArrayList<>(ignoredEntities);
		return this;
	}

	/**
	 * Builds the ammo based on passed data and spawns it in the world.
	 *
	 * @return The ammo entity
	 */
	public E create()
	{
		return create(null);
	}

	/**
	 * Builds the ammo based on passed data and spawns it in the world.
	 *
	 * @param action The action to perform on the ammo entity before it is spawned
	 * @return The ammo entity
	 */
	@Nullable
	public E create(@Nullable Consumer<E> action)
	{
		//Invalid ammo type
		if(ammo==null)
			return null;

		//No position or direction passed
		if(pos==null||dir==null)
		{
			IILogger.error("Couldn't create ammo entity, missing position or direction");
			return null;
		}

		//Create the entity
		World currentWorld = this.world.get();
		E entity = ammo.getAmmoEntity(currentWorld);
		entity.setFromStack(stack);
		entity.setOwner(owner);

		//Set projectile-only properties
		if(entity instanceof EntityAmmoProjectile)
		{
			EntityAmmoProjectile projectile = (EntityAmmoProjectile)entity;
			projectile.setPositionAndVelocity(pos, dir, velocityModifier);
			projectile.setIgnored(ignoredBlocks, ignoredEntities);
		}
		//Perform custom action
		if(action!=null)
			action.accept(entity);
		//Spawn the entity in the world
		currentWorld.spawnEntity(entity);
		return entity;
	}

	/**
	 * Detonates the ammo in the world without creating an entity
	 */
	public void detonate()
	{
		World currentWorld = this.world.get();
		AmmoComponent[] components = ammo.getComponents(stack);
		NBTTagCompound[] componentsNBT = ammo.getComponentsNBT(stack);
		AmmoCore core = ammo.getCore(stack);
		CoreType coreType = ammo.getCoreType(stack);
		ComponentEffectShape effectShape = coreType.getEffectShape();

		float componentMultiplier = this.ammo.getComponentMultiplier();
		float effectivenessMultiplier = core.getExplosionModifier()*coreType.getComponentEffectivenessMod();

		for(int i = 0; i < components.length; i++)
			components[i].onEffect(currentWorld, pos, dir,
					effectShape, componentsNBT[i], componentMultiplier, effectivenessMultiplier,
					owner);
	}
}
