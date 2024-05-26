package pl.pabilo8.immersiveintelligence.common.ammo;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentRole;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreTypes;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleRegistry;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.EntityAmmoBase;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

/**
 * @author Pabilo8
 * @updated 06.03.2024
 * @ii-approved 0.3.1
 * @since 10.07.2021
 */
public class AmmoComponentTracerPowder extends AmmoComponent
{
	public AmmoComponentTracerPowder()
	{
		super("tracer_powder", 1f, ComponentRole.SPECIAL, IIColor.fromPackedRGB(0x6b778a));
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack(new ItemStack(IIContent.itemTracerPowder, 1, 0));
	}

	@Override
	public void onEffect(World world, Vec3d pos, Vec3d dir, CoreTypes coreType, NBTTagCompound tag, float componentAmount, float multiplier, Entity owner)
	{

	}

	@Override
	public boolean spawnParticleTrail(EntityAmmoBase ammo, NBTTagCompound nbt)
	{
		int color = nbt.hasKey("colour")?nbt.getInteger("colour"): 0xffffff;
		ParticleRegistry.spawnTracerFX(ammo.getPositionVector(), IIUtils.getEntityMotion(ammo), ammo.getAmmoType().getCaliber()/16f, color);
		return true;
	}

	@Override
	public IIColor getColour(NBTTagCompound nbt)
	{
		return nbt!=null&&nbt.hasKey("colour")?IIColor.fromPackedRGB(nbt.getInteger("colour")): IIColor.WHITE;
	}
}
