package pl.pabilo8.immersiveintelligence.api.bullets;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumComponentRole;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.common.entity.bullet.EntityBullet;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
public interface IAmmoComponent
{
	//Gets the name of the component
	String getName();

	@SideOnly(Side.CLIENT)
	default String getTranslatedName()
	{
		return I18n.format("item.immersiveintelligence.bullet.component."+getName()+".name");
	}

	//Material used to create the component
	IngredientStack getMaterial();

	//Gets the density (density * amount = mass)
	float getDensity();

	//Runs when a bullet is exploding
	void onEffect(float amount, EnumCoreTypes coreType, NBTTagCompound tag, Vec3d pos, Vec3d dir, World world);

	//Gets the component role
	EnumComponentRole getRole();

	//Gets the component colour
	int getColour();

	default boolean showInManual()
	{
		return true;
	}

	default int getNBTColour(NBTTagCompound nbt)
	{
		return getColour();
	}

	default boolean hasTrail()
	{
		return false;
	}

	default void spawnParticleTrail(EntityBullet bullet, NBTTagCompound nbt)
	{

	}

	default boolean matchesBullet(IAmmo bullet)
	{
		return true;
	}
}
