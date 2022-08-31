package pl.pabilo8.immersiveintelligence.common.ammo;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumComponentRole;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmoComponent;
import pl.pabilo8.immersiveintelligence.client.fx.ParticleUtils;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.entity.bullet.EntityBullet;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
public class AmmoComponentTracerPowder implements IAmmoComponent
{
	@Override
	public String getName()
	{
		return "tracer_powder";
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack(new ItemStack(IIContent.itemTracerPowder, 1, 0));
	}

	@Override
	public float getDensity()
	{
		return 0.25f;
	}

	@Override
	public void onEffect(float amount, EnumCoreTypes coreType, NBTTagCompound tag, Vec3d pos, Vec3d dir, World world)
	{

	}

	@Override
	public EnumComponentRole getRole()
	{
		return EnumComponentRole.TRACER;
	}

	@Override
	public int getColour()
	{
		return 0xffffff;
	}

	@Override
	public boolean hasTrail()
	{
		return true;
	}

	@Override
	public void spawnParticleTrail(EntityBullet bullet, NBTTagCompound nbt)
	{
		int color = nbt.hasKey("colour")?nbt.getInteger("colour"): 0xffffff;
		ParticleUtils.spawnTracerFX(bullet.getPositionVector(), IIUtils.getEntityMotion(bullet), bullet.bullet.getCaliber()/16f, color);
	}

	@Override
	public int getNBTColour(NBTTagCompound nbt)
	{
		return nbt.hasKey("colour")?nbt.getInteger("colour"): 0xffffff;
	}
}
