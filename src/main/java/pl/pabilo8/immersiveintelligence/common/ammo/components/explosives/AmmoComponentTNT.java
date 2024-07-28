package pl.pabilo8.immersiveintelligence.common.ammo.components.explosives;

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
 * @author Pabilo8
 * @updated 06.03.2024
 * @ii-approved 0.3.1
 * @since 10.07.2021
 */
public class AmmoComponentTNT extends AmmoComponent
{
	public AmmoComponentTNT()
	{
		super("tnt", 1f, ComponentRole.EXPLOSIVE, IIColor.fromPackedRGB(0x282828));
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("materialTNT");
	}

	@Override
	public void onEffect(World world, Vec3d pos, Vec3d dir, ComponentEffectShape shape, NBTTagCompound tag, float componentAmount, float multiplier, Entity owner)
	{
		float size = (float)Math.floor(6*componentAmount*multiplier);
		new IIExplosion(world, owner, pos, dir,
				size, 4, shape, false, componentAmount > 0.125f, false)
				.doExplosion();
	}
}