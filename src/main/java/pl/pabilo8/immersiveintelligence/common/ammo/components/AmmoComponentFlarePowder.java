package pl.pabilo8.immersiveintelligence.common.ammo.components;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentEffectShape;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentRole;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.component.EntityFlare;

/**
 * @author Pabilo8
 * @ii-approved 0.3.1
 * @updated 06.03.2024
 * @since 30-08-2019
 */
public class AmmoComponentFlarePowder extends AmmoComponentTracerPowder
{
	@Override
	public String getName()
	{
		return "flare_powder";
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack(new ItemStack(IIContent.itemTracerPowder, 1, 1));
	}

	@Override
	public ComponentRole getRole()
	{
		return ComponentRole.FLARE;
	}

	@Override
	public void onEffect(World world, Vec3d pos, Vec3d dir, ComponentEffectShape shape, NBTTagCompound tag, float componentAmount, float multiplier, Entity owner)
	{
		if(world.isRemote)
			return;
		world.playSound(null, pos.x, pos.y+1f, pos.z, IISounds.explosionFlare, SoundCategory.NEUTRAL, 4, 0.5f);
		EntityFlare flare = new EntityFlare(world, getColor(tag));
		flare.setPosition(pos.x, pos.y+1f, pos.z);
		world.spawnEntity(flare);
	}
}
