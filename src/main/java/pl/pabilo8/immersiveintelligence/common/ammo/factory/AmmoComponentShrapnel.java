package pl.pabilo8.immersiveintelligence.common.ammo.factory;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ShrapnelHandler;
import pl.pabilo8.immersiveintelligence.api.ShrapnelHandler.Shrapnel;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentRole;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreTypes;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.component.EntityShrapnel;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
public class AmmoComponentShrapnel extends AmmoComponent
{
	Shrapnel shrapnel;
	IngredientStack stack;

	public AmmoComponentShrapnel(String material)
	{
		super("shrapnel_"+material, 1f, ComponentRole.SHRAPNEL, ShrapnelHandler.registry.get(material).color);
		shrapnel = ShrapnelHandler.registry.get(material);
		stack = new IngredientStack("dust"+Character.toUpperCase(material.charAt(0))+material.substring(1));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public String getTranslatedName()
	{
		return I18n.format("ie.manual.entry.bullet_component."+getName());
	}

	@Override
	public IngredientStack getMaterial()
	{
		return stack;
	}

	@Override
	public void onEffect(World world, Vec3d pos, Vec3d dir, float multiplier, NBTTagCompound tag, CoreTypes coreType, Entity owner)
	{
		Vec3d v = new Vec3d(0, -1, 0);
		Vec3d throwerPos = pos.addVector(0, 3, 0);
		for(int i = 0; i < 50*multiplier; i++)
		{
			Vec3d vecDir = v.addVector(Utils.RAND.nextGaussian()*.25f, Utils.RAND.nextGaussian()*.25f, Utils.RAND.nextGaussian()*.25f);

			EntityShrapnel shrap = new EntityShrapnel(world, throwerPos.x+v.x*2, throwerPos.y+v.y*2,
					throwerPos.z+v.z*2, 0, 0, 0, shrapnel);
			shrap.motionX = vecDir.x*2;
			shrap.motionY = vecDir.y*0.05f;
			shrap.motionZ = vecDir.z*2;
			if(!world.isRemote)
				world.spawnEntity(shrap);
		}
	}
}
