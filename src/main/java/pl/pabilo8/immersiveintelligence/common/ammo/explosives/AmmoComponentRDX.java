package pl.pabilo8.immersiveintelligence.common.ammo.explosives;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentRole;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreTypes;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.common.util.IIExplosion;

/**
 * @author Pabilo8
 * @updated 06.03.2024
 * @ii-approved 0.3.1
 * @since 10.07.2021
 */
public class AmmoComponentRDX extends AmmoComponent
{
	public AmmoComponentRDX()
	{
		super("rdx", 1.25f, ComponentRole.EXPLOSIVE, 0xd2c294);
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("materialHexogen");
	}

	@Override
	public void onEffect(World world, Vec3d pos, Vec3d dir, float multiplier, NBTTagCompound tag, CoreTypes coreType, Entity owner)
	{
		new IIExplosion(world, null, pos.x, pos.y, pos.z, 9*multiplier, 8, false, true)
				.doExplosion();
	}
}