package pl.pabilo8.immersiveintelligence.common.ammo.components.incendiary;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.util.IEPotions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentEffectShape;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentRole;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.common.IIPotions;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.component.EntityAtomicBoom;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.component.EntityWhitePhosphorus;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIDamageSources;
import pl.pabilo8.immersiveintelligence.common.util.IIExplosion;

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
		super("white_phosphorus", 1f, ComponentRole.INCENDIARY, IIColor.fromPackedRGBA(0x6b778a));
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("dustWhitePhosphorus");
	}

	@Override
	public void onEffect(World world, Vec3d pos, Vec3d dir, ComponentEffectShape shape, NBTTagCompound tag, float componentAmount, float multiplier, Entity owner)
	{

		BlockPos ppos = new BlockPos(pos);
		new IIExplosion(world, owner, pos, null, 6*multiplier, 6, ComponentEffectShape.ORB, true, false, false)
				.doExplosion();



		EntityLivingBase[] entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(ppos).grow(6*multiplier)).toArray(new EntityLivingBase[0]);
		for(EntityLivingBase e : entities)
		{
			e.addPotionEffect(new PotionEffect(IEPotions.flashed, 40, 1));
			e.addPotionEffect(new PotionEffect(IEPotions.stunned, 40, 1));
			e.addPotionEffect(new PotionEffect(IEPotions.flammable, 40, 1));
			e.addPotionEffect(new PotionEffect(IIPotions.brokenArmor, 40, 0));
		}

		IIPacketHandler.playRangedSound(world, pos, IISounds.explosionIncendiary, SoundCategory.NEUTRAL, 16, 1f, 0f);

		EntityWhitePhosphorus entityWhitePhosphorus = new EntityWhitePhosphorus(world);
		entityWhitePhosphorus.setPosition(pos.x, pos.y, pos.z);
		world.spawnEntity(entityWhitePhosphorus);

	}
}
