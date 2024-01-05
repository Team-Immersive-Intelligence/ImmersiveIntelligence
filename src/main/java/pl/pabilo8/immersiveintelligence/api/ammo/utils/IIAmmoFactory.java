package pl.pabilo8.immersiveintelligence.api.ammo.utils;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmo;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.EntityAmmoBase;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityProjectile;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

/**
 * @param <T> The ammo entity class
 * @author Pabilo8
 * @ii-approved 0.3.1
 * @since 02.01.2024
 */
public class IIAmmoFactory<T extends EntityAmmoBase>
{
	//--- Parameters ---//
	/**
	 * The world to spawn the ammo in
	 */
	private final World world;
	/**
	 * The ammo to create, taken from stack or passed directly
	 */
	private IAmmo<T> ammo;
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

	public IIAmmoFactory(World world)
	{
		this.world = world;
		this.pos = null;
		this.ammo = null;
		this.stack = ItemStack.EMPTY;
		this.owner = null;
		this.ignoredBlocks = null;
		this.ignoredEntities = null;
		this.velocityModifier = 1f;
		this.dir = Vec3d.ZERO;
	}

	//--- Factory Methods ---//

	/**
	 * Sets the stack to create the ammo from
	 *
	 * @param stack The stack to create the ammo from
	 * @return The factory
	 */
	public IIAmmoFactory<T> setStack(ItemStack stack)
	{
		this.stack = stack;

		return this;
	}

	/**
	 * Sets the position to spawn the ammo at
	 *
	 * @param pos The position to spawn the ammo at
	 * @return The factory
	 */
	public void setPosition(Vec3d pos)
	{
		this.pos = pos;
	}

	/**
	 * Sets the direction to spawn the ammo in
	 *
	 * @param dir The direction to spawn the ammo in
	 * @return The factory
	 */
	public IIAmmoFactory<T> setDirection(Vec3d dir)
	{
		this.dir = dir;
		return this;
	}

	/**
	 * Sets the velocity multiplier of the ammo
	 *
	 * @param velocityModifier The velocity multiplier of the ammo
	 * @return The factory
	 */
	public IIAmmoFactory<T> setVelocityModifier(float velocityModifier)
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
	public void setPositionAndVelocity(Vec3d pos, Vec3d dir, float velocityModifier)
	{
		setPosition(pos);
		setDirection(dir);
		setVelocityModifier(velocityModifier);
	}

	/**
	 * Sets the owner of the ammo
	 *
	 * @param owner The owner of the ammo
	 * @return The factory
	 */
	public IIAmmoFactory<T> setOwner(Entity owner)
	{
		this.owner = owner;
		return this;
	}

	/**
	 * Sets the list of blocks a projectile should ignore, used by turrets
	 *
	 * @param ignoredBlocks The list of blocks a projectile should ignore
	 * @return The factory
	 */
	public IIAmmoFactory<T> setIgnoredBlocks(List<BlockPos> ignoredBlocks)
	{
		this.ignoredBlocks = ignoredBlocks;
		return this;
	}

	/**
	 * Sets the list of entities a projectile should ignore<br>
	 * the owner is always ignored and doesn't have to be added to this list
	 *
	 * @param ignoredEntities The list of entities a projectile should ignore
	 * @return The factory
	 */
	public IIAmmoFactory<T> setIgnoredEntities(List<Entity> ignoredEntities)
	{
		this.ignoredEntities = ignoredEntities;
		return this;
	}

	/**
	 * Creates the ammo
	 *
	 * @return The ammo entity
	 */
	@Nullable
	public T create(@Nullable Consumer<T> action)
	{
		//Invalid ammo type
		if(ammo==null)
			return null;

		//Create the entity
		T t = ammo.getBulletEntity(world);
		t.setFromStack(stack);
		t.setOwner(owner);

		//Set projectile-only properties
		if(t instanceof EntityProjectile)
		{
			EntityProjectile projectile = (EntityProjectile)t;
			projectile.setPositionAndVelocity(pos, dir, velocityModifier);
			projectile.setIgnored(ignoredBlocks, ignoredEntities);
		}
		//Perform custom action
		if(action!=null)
			action.accept(t);
		//Spawn the entity in the world
		world.spawnEntity(t);
		return t;
	}
}
