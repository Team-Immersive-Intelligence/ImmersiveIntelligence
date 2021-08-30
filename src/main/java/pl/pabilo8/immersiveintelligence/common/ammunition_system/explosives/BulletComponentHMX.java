package pl.pabilo8.immersiveintelligence.common.ammunition_system.explosives;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.EnumComponentRole;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.IBulletComponent;
import pl.pabilo8.immersiveintelligence.common.util.IIExplosion;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
public class BulletComponentHMX implements IBulletComponent
{
	@Override
	public String getName()
	{
		return "hmx";
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("materialHMX");
	}

	@Override
	public float getDensity()
	{
		return 1.75f;
	}

	@Override
	public void onEffect(float amount, EnumCoreTypes coreType, NBTTagCompound tag, Vec3d pos, Vec3d dir, World world)
	{
		IIExplosion e = new IIExplosion(world, null, pos.x, pos.y, pos.z, 12*amount, 16, false, true);
		if(!net.minecraftforge.event.ForgeEventFactory.onExplosionStart(world, e))
		{
			e.doExplosionA();
			e.doExplosionB(true);
		}
	}

	@Override
	public EnumComponentRole getRole()
	{
		return EnumComponentRole.EXPLOSIVE;
	}

	@Override
	public int getColour()
	{
		return 0xfbfbfb;
	}
}
