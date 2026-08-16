package pl.pabilo8.immersiveintelligence.common.compat.thaum;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentEffectShape;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentRole;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIExplosion;

/**
 * @author Carver (carver@iiteam.net)
 * @since 11.04.2026
 */
public class AmmoComponentAlumentum extends AmmoComponent
{
	public AmmoComponentAlumentum()
	{
		super("alumentum", 1f, ComponentRole.EXPLOSIVE, IIColor.fromPackedRGB(0xe2ed68), 1);
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("alumentum");
	}

	@Override
	public void onEffect(World world, Vec3d pos, Vec3d dir, ComponentEffectShape shape, NBTTagCompound tag, float componentSize, float multiplier, Entity owner)
	{
		new IIExplosion(world, owner, pos, dir,
				4*componentSize, 6*multiplier, shape, false, componentSize > 0.125f, false)
				.doExplosion();
	}
}
