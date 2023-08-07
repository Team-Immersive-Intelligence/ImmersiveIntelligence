package pl.pabilo8.immersiveintelligence.common.ammo.explosives;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumComponentRole;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmoComponent;
import pl.pabilo8.immersiveintelligence.common.util.IIExplosion;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
public class AmmoComponentRDX implements IAmmoComponent
{
	@Override
	public String getName()
	{
		return "rdx";
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("materialHexogen");
	}

	@Override
	public float getDensity()
	{
		return 1.25f;
	}

	@Override
	public void onEffect(float amount, EnumCoreTypes coreType, NBTTagCompound tag, Vec3d pos, Vec3d dir, World world)
	{
		new IIExplosion(world, null, pos.x, pos.y, pos.z, 9*amount, 8, false, true)
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
		return 0xd2c294;
	}
}
