package pl.pabilo8.immersiveintelligence.common.ammunition_system.explosives;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.util.IEPotions;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.EnumComponentRole;
import pl.pabilo8.immersiveintelligence.api.bullets.IBulletComponent;
import pl.pabilo8.immersiveintelligence.common.entity.EntityAtomicBoom;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityBullet;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
public class BulletComponentNuke implements IBulletComponent
{
	@Override
	public String getName()
	{
		return "nuke";
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("materialNuke");
	}

	@Override
	public float getDensity()
	{
		return 2f;
	}

	@Override
	public void onEffect(float amount, NBTTagCompound tag, World world, BlockPos pos, EntityBullet bullet)
	{
		Explosion[] explosions = new Explosion[]{
				new Explosion(world, bullet, bullet.posX, bullet.posY, bullet.posZ, 32*amount, false, true),
				new Explosion(world, bullet, bullet.posX+12, bullet.posY, bullet.posZ+12, 32*amount, false, true),
				new Explosion(world, bullet, bullet.posX-12, bullet.posY, bullet.posZ+12, 32*amount, false, true),
				new Explosion(world, bullet, bullet.posX-12, bullet.posY, bullet.posZ-12, 32*amount, false, true),
				new Explosion(world, bullet, bullet.posX+12, bullet.posY, bullet.posZ-12, 32*amount, false, true)
		};
		for(Explosion e : explosions)
			if(!net.minecraftforge.event.ForgeEventFactory.onExplosionStart(world, e))
			{
				e.doExplosionA();
				e.doExplosionB(false);
			}
		EntityLivingBase[] entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos).grow(75*amount)).toArray(new EntityLivingBase[0]);
		for(EntityLivingBase e : entities)
		{
			e.addPotionEffect(new PotionEffect(IEPotions.flashed, 40, 1));
		}
		entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos).grow(50*amount)).toArray(new EntityLivingBase[0]);
		for(EntityLivingBase e : entities)
		{
			e.addPotionEffect(new PotionEffect(IEPotions.flammable, 400, 8));
			e.setFire(30);
		}

		EntityAtomicBoom entityAtomicBoom = new EntityAtomicBoom(world, amount);
		entityAtomicBoom.setPosition(pos.getX(), pos.getY(), pos.getZ());
		world.spawnEntity(entityAtomicBoom);


	}

	@Override
	public EnumComponentRole getRole()
	{
		return EnumComponentRole.SPECIAL;
	}

	@Override
	public int getColour()
	{
		//Weird stuff here
		if(FMLCommonHandler.instance().getEffectiveSide()==Side.CLIENT)
		{
			float add = (Minecraft.getMinecraft().world.getTotalWorldTime()%60f)/60f;
			add = add > 0.5?1f-((add-0.5f)*2f): add*2f;
			return MathHelper.hsvToRGB(110f/255f, 0.75f*add, (0.5f+((1f-add)*0.45f)));
		}
		else
			return MathHelper.hsvToRGB(121f/255f, 0.75f, 0.88f);
	}
}
