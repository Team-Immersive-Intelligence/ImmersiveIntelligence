package pl.pabilo8.immersiveintelligence.common.bullets;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.EnumComponentRole;
import pl.pabilo8.immersiveintelligence.api.bullets.IBulletComponent;
import pl.pabilo8.immersiveintelligence.api.bullets.PenetrationHelper;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityBullet;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.MessageFireworks;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
public class BulletComponentFirework implements IBulletComponent
{
	@Override
	public String getName()
	{
		return "firework";
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack(new ItemStack(Items.FIREWORK_CHARGE));
	}

	@Override
	public float getDensity()
	{
		return 1f;
	}

	@Override
	public void onExplosion(float amount, NBTTagCompound tag, World world, BlockPos pos, EntityBullet bullet)
	{
		IIPacketHandler.INSTANCE.sendToAllAround(new MessageFireworks(tag, (float)bullet.posX, (float)bullet.posY+1, (float)bullet.posZ), Utils.targetPointFromEntity(bullet, 96));
		PenetrationHelper.supress(world,pos.getX(), pos.getY(), pos.getZ(),10f*amount,(int)(255*amount));
	}

	@Override
	public float getPenetrationModifier(NBTTagCompound tag)
	{
		return 0;
	}

	@Override
	public float getDamageModifier(NBTTagCompound tag)
	{
		return 0;
	}

	@Override
	public EnumComponentRole getRole()
	{
		return EnumComponentRole.FLARE;
	}

	@Override
	public int getColour()
	{
		return 0xcab1b1;
	}
}
