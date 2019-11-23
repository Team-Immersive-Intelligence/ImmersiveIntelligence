package pl.pabilo8.immersiveintelligence.common.bullets.cores;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.EnumComponentRole;
import pl.pabilo8.immersiveintelligence.api.bullets.IBulletCoreType;
import pl.pabilo8.immersiveintelligence.common.entity.EntityBullet;

/**
 * Created by Pabilo8 on 30-08-2019.
 */
public class BulletCorePabilium implements IBulletCoreType
{
	@Override
	public String getName()
	{
		return "CorePabilium";
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("platePabilium");
	}

	@Override
	public float getDensity()
	{
		return 0.15f;
	}

	@Override
	public void onExplosion(float amount, NBTTagCompound tag, World world, BlockPos pos, EntityBullet bullet)
	{

	}

	@Override
	public float getPenetrationModifier(NBTTagCompound tag)
	{
		return 25f;
	}

	@Override
	public float getDamageModifier(NBTTagCompound tag)
	{
		return 25f;
	}

	@Override
	public EnumComponentRole getRole()
	{
		return EnumComponentRole.SPECIAL;
	}

	@Override
	public float getExplosionModifier()
	{
		return 6.5f;
	}

	@Override
	public int getColour()
	{
		//Weird stuff here
		if(FMLCommonHandler.instance().getEffectiveSide()==Side.CLIENT)
			return MathHelper.hsvToRGB(127f/255f, Minecraft.getMinecraft().world.getTotalWorldTime()%40/40, 0.88f);
		else
			return MathHelper.hsvToRGB(127f/255f, 1f, 0.88f);
	}
}
