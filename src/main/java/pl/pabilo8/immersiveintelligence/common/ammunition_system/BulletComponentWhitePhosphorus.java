package pl.pabilo8.immersiveintelligence.common.ammunition_system;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.util.IEPotions;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.EnumComponentRole;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.IBulletComponent;
import pl.pabilo8.immersiveintelligence.common.IIPotions;
import pl.pabilo8.immersiveintelligence.common.IISounds;
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
	public void onEffect(float amount, EnumCoreTypes coreType, NBTTagCompound tag, Vec3d pos, Vec3d dir, World world)
	{
		if(world.isRemote)
			return;
		BlockPos ppos = new BlockPos(pos);
		Vec3d v = coreType==EnumCoreTypes.SHAPED?dir: dir.scale(-1);
		world.playSound(null, ppos, IISounds.explosion_incendiary_low, SoundCategory.BLOCKS, 8f, 1f/amount);
		world.playSound(null, ppos, IISounds.explosion_incendiary_high, SoundCategory.BLOCKS, 4f, 1f/amount);

		//fragments
		for(int i = 0; i < 30*amount; i++)
		{
			Vec3d vecDir = new Vec3d(1, 0, 0).rotateYaw(i/(30f*amount)*360f).add(v);
			EntityWhitePhosphorus shrap = new EntityWhitePhosphorus(world,
					pos.x+vecDir.x, pos.y+v.y+1f, pos.z+vecDir.z,
					0, 0, 0);

			shrap.motionX = vecDir.x*0.35f;
			shrap.motionY = 0.1f+(Utils.RAND.nextDouble()*0.2f);
			shrap.motionZ = vecDir.z*0.35f;


			world.spawnEntity(shrap);
		}

		IIPacketHandler.INSTANCE.sendToAllAround(new MessageParticleEffect(pos.x+v.x, pos.y+v.y+1f, pos.z+v.z, "white_phosphorus"), pl.pabilo8.immersiveintelligence.api.Utils.targetPointFromPos(pos, world, (48)));

		//main
		EntityAreaEffectCloud cloud = new EntityAreaEffectCloud(world, pos.x+v.x, pos.y+v.y+1f, pos.z+v.z);
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
