package pl.pabilo8.immersiveintelligence.common.ammunition_system;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.util.IEPotions;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.EnumComponentRole;
import pl.pabilo8.immersiveintelligence.api.bullets.IBulletComponent;
import pl.pabilo8.immersiveintelligence.common.IIPotions;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityBullet;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityWhitePhosphorus;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.MessageParticleEffect;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
public class BulletComponentWhitePhosphorus implements IBulletComponent
{
	@Override
	public String getName()
	{
		return "white_phosphorus";
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("dustWhitePhosphorus");
	}

	@Override
	public float getDensity()
	{
		return 1f;
	}

	@Override
	public void onEffect(float amount, NBTTagCompound tag, World world, BlockPos pos, EntityBullet bullet)
	{
		if(world.isRemote)
			return;

		Vec3d v = new Vec3d(bullet.baseMotionX, bullet.baseMotionY, bullet.baseMotionZ).scale(-1);
		world.playSound(null, pos, SoundEvents.ENTITY_FIREWORK_BLAST, SoundCategory.BLOCKS, 1f, 1.5f);

		//fragments
		for(int i = 0; i < 40*amount; i++)
		{
			Vec3d vecDir = new Vec3d(1, 0, 0).rotateYaw(i/(40f*amount)*360f).add(v);
			EntityWhitePhosphorus shrap = new EntityWhitePhosphorus(world,
					bullet.posX+vecDir.x, bullet.posY+v.y+1f, bullet.posZ+vecDir.z,
					0, 0, 0);

			shrap.motionX = vecDir.x*0.35f;
			shrap.motionY = (Utils.RAND.nextDouble()*0.35f);
			shrap.motionZ = vecDir.z*0.35f;


			world.spawnEntity(shrap);
		}

		IIPacketHandler.INSTANCE.sendToAllAround(new MessageParticleEffect(bullet.posX+v.x, bullet.posY+v.y+1f, bullet.posZ+v.z, "white_phosphorus"), pl.pabilo8.immersiveintelligence.api.Utils.targetPointFromPos(pos, world, (48)));

		//main
		EntityAreaEffectCloud cloud = new EntityAreaEffectCloud(world, bullet.posX+v.x, bullet.posY+v.y+1f, bullet.posZ+v.z);
		cloud.addEffect(new PotionEffect(IIPotions.broken_armor, Math.round(240), 2));
		cloud.addEffect(new PotionEffect(IEPotions.flammable, Math.round(240), 4));
		cloud.addEffect(new PotionEffect(IEPotions.stunned, Math.round(160), 2));
		cloud.addEffect(new PotionEffect(IEPotions.flashed, Math.round(270), 1));
		cloud.setRadius(3f*amount);
		cloud.setDuration(Math.round(20f+(10f*Utils.RAND.nextFloat())));
		cloud.setParticle(EnumParticleTypes.CLOUD);
		world.spawnEntity(cloud);

	}

	@Override
	public EnumComponentRole getRole()
	{
		return EnumComponentRole.INCENDIARY;
	}

	@Override
	public int getColour()
	{
		return 0xd3dbac;
	}
}
