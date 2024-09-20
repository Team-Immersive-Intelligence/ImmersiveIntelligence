package pl.pabilo8.immersiveintelligence.api.ammo.parts;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentEffectShape;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentRole;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.EntityAmmoBase;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @updated 06.03.2024
 * @ii-approved 0.3.1
 * @since 30-08-2019
 */
public abstract class AmmoComponent extends AmmoPart
{
	private final ComponentRole role;
	private final int slotsTaken;

	public AmmoComponent(String name, float density, ComponentRole role, IIColor color)
	{
		super(name, density, color);
		this.role = role;
		this.slotsTaken = 1;
	}

	//--- Properties ---//

	/**
	 * @return the translated name of the component
	 */
	@SideOnly(Side.CLIENT)
	public String getTranslatedName()
	{
		return I18n.format("item.immersiveintelligence.bullet.component."+getName()+".name");
	}

	/**
	 * Called when the ammo explodes
	 *
	 * @param world           the world
	 * @param pos             the position of the bullet
	 * @param dir             the direction of the explosion
	 * @param shape           the core type
	 * @param tag             the NBT tag of the bullet
	 * @param componentAmount
	 * @param multiplier      the component effect multiplier
	 * @param owner           the owner of the bullet
	 */
	public abstract void onEffect(World world, Vec3d pos, Vec3d dir, ComponentEffectShape shape, NBTTagCompound tag, float componentAmount, float multiplier, @Nullable Entity owner);

	/**
	 * @return the component role
	 */
	public ComponentRole getRole()
	{
		return role;
	}


	//--- Defaults ---//

	/**
	 * @implNote Override if your component has animated color
	 */
	public IIColor getColor(@Nullable NBTTagCompound nbt)
	{
		return getColor();
	}

	/**
	 * @param ammo ammo this component is in
	 * @param nbt  nbt of this component
	 * @return whether this component spawned a trail
	 */
	public boolean spawnParticleTrail(EntityAmmoBase<?> ammo, NBTTagCompound nbt)
	{
		return false;
	}

	/**
	 * @param bullet bullet type to match
	 * @return whether this component matches a bullet type
	 */
	public boolean matchesBullet(IAmmoTypeItem<?, ?> bullet)
	{
		return true;
	}

	public int getSlotsTaken()
	{
		return slotsTaken;
	}
}
