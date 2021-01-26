package pl.pabilo8.immersiveintelligence.common.ammunition_system;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.EnumComponentRole;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.entity.EntityFlare;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityBullet;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
public class BulletComponentFlarePowder extends BulletComponentTracerPowder
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
	public EnumComponentRole getRole()
	{
		return EnumComponentRole.FLARE;
	}

	@Override
	public void onEffect(float amount, NBTTagCompound tag, World world, BlockPos pos, EntityBullet bullet)
	{
		if(world.isRemote)
			return;
		EntityFlare flare = new EntityFlare(world, getNBTColour(tag));
		flare.setPosition(bullet.posX, bullet.posY+1f, bullet.posZ);
		world.spawnEntity(flare);
	}
}
