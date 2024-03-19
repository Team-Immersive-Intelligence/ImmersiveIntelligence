package pl.pabilo8.immersiveintelligence.api.ammo.parts;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.EnumComponentRole;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.EntityAmmoBase;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @updated 06.03.2024
 * @ii-approved 0.3.1
 * @since 30-08-2019
 */
public abstract class AmmoComponent
{
	private final String name;
	private final float density;
	private final EnumComponentRole role;
	private final int colour;

	public AmmoComponent(String name, float density, EnumComponentRole role, int colour)
	{
		this.name = name;
		this.density = density;
		this.role = role;
		this.colour = colour;
	}

	//--- Properties ---//

	/**
	 * @return the name of the component
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return the translated name of the component
	 */
	@SideOnly(Side.CLIENT)
	public String getTranslatedName()
	{
		return I18n.format("item.immersiveintelligence.bullet.component."+getName()+".name");
	}

	/**
	 * @return the material used to create the component
	 */
	public abstract IngredientStack getMaterial();

	/**
	 * @return the density (density * amount = mass) of the component
	 */
	public float getDensity()
	{
		return density;
	}

	/**
	 * Called when the ammo explodes
	 *
	 * @param world      the world
	 * @param pos        the position of the bullet
	 * @param dir        the direction of the explosion
	 * @param multiplier the component effect multiplier
	 * @param tag        the NBT tag of the bullet
	 * @param coreType   the core type
	 * @param owner      the owner of the bullet
	 */
	public abstract void onEffect(World world, Vec3d pos, Vec3d dir, float multiplier, NBTTagCompound tag, EnumCoreTypes coreType, @Nullable Entity owner);

	/**
	 * @return the component role
	 */
	public EnumComponentRole getRole()
	{
		return role;
	}


	//--- Defaults ---//

	/**
	 * @return if this component should be shown in the manual
	 */
	public boolean showInManual()
	{
		return true;
	}

	/**
	 * @implNote Override if your component has animated colour
	 */
	public int getColour(@Nullable NBTTagCompound nbt)
	{
		return colour;
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
}
