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
import pl.pabilo8.immersiveintelligence.common.entity.ammo.EntityBullet;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @updated 30.12.2023
 * @ii-approved 0.3.1
 * @since 30-08-2019
 */
public interface IAmmoComponent
{
	//--- Properties ---//

	/**
	 * @return the name of the component
	 */
	String getName();

	/**
	 * @return the translated name of the component
	 */
	@SideOnly(Side.CLIENT)
	default String getTranslatedName()
	{
		return I18n.format("item.immersiveintelligence.bullet.component."+getName()+".name");
	}

	/**
	 * @return the material used to create the component
	 */
	IngredientStack getMaterial();

	/**
	 * @return the density (density * amount = mass) of the component
	 */
	float getDensity();

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
	void onEffect(World world, Vec3d pos, Vec3d dir, float multiplier, NBTTagCompound tag, EnumCoreTypes coreType, @Nullable Entity owner);

	/**
	 * @return the component role
	 */
	EnumComponentRole getRole();

	/**
	 * @return the colour of the component
	 */
	int getColour();


	//--- Defaults ---//

	/**
	 * @return if this component should be shown in the manual
	 */
	default boolean showInManual()
	{
		return true;
	}

	default int getNBTColour(NBTTagCompound nbt)
	{
		return getColour();
	}

	default boolean spawnParticleTrail(EntityBullet bullet, NBTTagCompound nbt)
	{
		return false;
	}

	default boolean matchesBullet(IAmmoItem bullet)
	{
		return true;
	}
}
