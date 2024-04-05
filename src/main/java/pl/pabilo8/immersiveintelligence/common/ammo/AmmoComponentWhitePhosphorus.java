package pl.pabilo8.immersiveintelligence.common.ammo;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.util.IEPotions;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentRole;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreTypes;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.common.IIPotions;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.component.EntityWhitePhosphorus;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageParticleEffect;

/**
 * @author Pabilo8
 * @updated 06.03.2024
 * @ii-approved 0.3.1
 * @since 10.07.2021
 */
public class AmmoComponentWhitePhosphorus extends AmmoComponent
{
	public AmmoComponentWhitePhosphorus()
	{
		super("white_phosphorus", 1f, ComponentRole.SPECIAL, 0x6b778a);
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("dustWhitePhosphorus");
	}

	@Override
	public void onEffect(World world, Vec3d pos, Vec3d dir, CoreTypes coreType, NBTTagCompound tag, float componentAmount, float multiplier, Entity owner)
	{
		if(world.isRemote)
			return;

		//if using shaped core, make the effect face the bullet direction
		Vec3d v = coreType==CoreTypes.SHAPED?dir: dir.scale(-1);
		IIPacketHandler.playRangedSound(world, pos, IISounds.explosionIncendiary, SoundCategory.NEUTRAL, (int)(40*multiplier), 1f, 1f);

		//fragments
		for(int i = 0; i < 30*multiplier; i++)
		{
			Vec3d vecDir = new Vec3d(1, 0, 0).rotateYaw(i/(30f*multiplier)*360f).add(v);
			EntityWhitePhosphorus shrap = new EntityWhitePhosphorus(world,
					pos.x+vecDir.x, pos.y+v.y+1f, pos.z+vecDir.z,
					0, 0, 0);

			shrap.motionX = vecDir.x*0.35f;
			shrap.motionY = 0.1f+(Utils.RAND.nextDouble()*0.2f);
			shrap.motionZ = vecDir.z*0.35f;


			world.spawnEntity(shrap);
		}

		IIPacketHandler.INSTANCE.sendToAllAround(new MessageParticleEffect(pos.addVector(0, 1, 0), "white_phosphorus"), IIPacketHandler.targetPointFromPos(pos, world, (48)));

		//main
		EntityAreaEffectCloud cloud = new EntityAreaEffectCloud(world, pos.x+v.x, pos.y+v.y+1f, pos.z+v.z);
		cloud.addEffect(new PotionEffect(IIPotions.brokenArmor, Math.round(240), 2));
		cloud.addEffect(new PotionEffect(IEPotions.flammable, Math.round(240), 4));
		cloud.addEffect(new PotionEffect(IEPotions.stunned, Math.round(160), 2));
		cloud.addEffect(new PotionEffect(IEPotions.flashed, Math.round(270), 1));
		cloud.setRadius(3f*multiplier);
		cloud.setDuration(Math.round(20f+(10f*Utils.RAND.nextFloat())));
		cloud.setParticle(EnumParticleTypes.CLOUD);
		world.spawnEntity(cloud);

	}
}
