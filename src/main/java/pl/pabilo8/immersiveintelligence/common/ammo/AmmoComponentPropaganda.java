package pl.pabilo8.immersiveintelligence.common.ammo;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoUtils;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumComponentRole;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmoComponent;
import pl.pabilo8.immersiveintelligence.common.IIContent;

/**
 * @author Pabilo8
 * @since 10.07.2021
 */
public class AmmoComponentPropaganda implements IAmmoComponent
{
	@Override
	public String getName()
	{
		return "propaganda";
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack(new ItemStack(IIContent.itemPrintedPage, 1, 1));
	}

	@Override
	public float getDensity()
	{
		return 0.65f;
	}

	@Override
	public void onEffect(float amount, EnumCoreTypes coreType, NBTTagCompound tag, Vec3d pos, Vec3d dir, World world)
	{
		ItemStack stack = new ItemStack(IIContent.itemPrintedPage, 1, 1);
		stack.setTagCompound(tag);
		for(int i = 0; i < amount*8; i++)
		{
			Vec3d v = new Vec3d(1, 0, 0).rotateYaw(i/(amount*16)*360f);
			EntityItem ei = new EntityItem(world, pos.x+.5+v.x*2, pos.y+.5, pos.z+.5+v.z*2, stack.copy());
			ei.motionY = 0.035;
			ei.motionX = 0.075F*v.x;
			ei.motionZ = 0.075F*v.z;

			world.spawnEntity(ei);
		}
		AmmoUtils.suppress(world, pos.x, pos.y, pos.z, 4f*amount, (int)(4*amount));

		// TODO: 11.07.2021 add the advancement
	}

	@Override
	public EnumComponentRole getRole()
	{
		return EnumComponentRole.SPECIAL;
	}

	@Override
	public int getColour()
	{
		return 0xbaafa4;
	}
}
