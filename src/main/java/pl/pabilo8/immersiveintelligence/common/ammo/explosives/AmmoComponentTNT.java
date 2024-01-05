package pl.pabilo8.immersiveintelligence.common.ammo.explosives;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.EnumComponentRole;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoComponent;
import pl.pabilo8.immersiveintelligence.common.util.IIExplosion;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
public class AmmoComponentTNT implements IAmmoComponent
{
	@Override
	public String getName()
	{
		return "tnt";
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("materialTNT");
	}

	@Override
	public float getDensity()
	{
		return 1f;
	}

	@Override
	public void onEffect(World world, Vec3d pos, Vec3d dir, float multiplier, NBTTagCompound tag, EnumCoreTypes coreType, Entity owner)
	{
		new IIExplosion(world, null, pos.x, pos.y, pos.z, 6*multiplier, 4, false, true)
				.doExplosion();
	}

	@Override
	public EnumComponentRole getRole()
	{
		return EnumComponentRole.EXPLOSIVE;
	}

	@Override
	public int getColour()
	{
		return 0x282828;
	}
}
