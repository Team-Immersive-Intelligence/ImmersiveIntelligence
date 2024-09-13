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
import pl.pabilo8.immersiveintelligence.api.ShrapnelHandler;
import pl.pabilo8.immersiveintelligence.api.ShrapnelHandler.Shrapnel;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentEffectShape;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentRole;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.common.IIPotions;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.component.EntityShrapnel;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.component.EntityWhitePhosphorus;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIExplosion;

/**
 * @author Pabilo8
 * @updated 06.03.2024
 * @ii-approved 0.3.1
 * @since 10.07.2021
 * @author Avalon
 * @since 9.13.2024
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
		new IIExplosion(world, owner, pos, null, 3*multiplier, 6, ComponentEffectShape.STAR, true, false, false)
				.doExplosion();



		EntityLivingBase[] entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(ppos).grow(3*multiplier)).toArray(new EntityLivingBase[0]);
		for(EntityLivingBase e : entities)
		{
			e.addPotionEffect(new PotionEffect(IEPotions.flashed, 40, 1));
			e.addPotionEffect(new PotionEffect(IEPotions.stunned, 40, 1));
			e.addPotionEffect(new PotionEffect(IEPotions.flammable, 40, 1));
			e.addPotionEffect(new PotionEffect(IIPotions.brokenArmor, 40, 0));
		}

		IIPacketHandler.playRangedSound(world, pos, IISounds.explosionIncendiary, SoundCategory.NEUTRAL, 16, 1f, 0f);

		// Get the shrapnel component from ShrapnelHandler
		Shrapnel shrapnel = ShrapnelHandler.registry.get("white_phosphorus");

		if (shrapnel != null) {
			int shrapnelCount = 10 + world.rand.nextInt(10); // Randomly spawn between 10-20 shrapnel pieces

			for (int i = 0; i < shrapnelCount; i++) {
				// Generate a random angle for the direction within a half-sphere
				double theta = world.rand.nextDouble() * Math.PI; // Angle from the vertical axis (0 to π)
				double phi = world.rand.nextDouble() * 2 * Math.PI; // Angle in the x-z plane (0 to 2π)

				// Convert spherical coordinates to Cartesian coordinates
				double x = Math.sin(theta) * Math.cos(phi);
				double y = Math.cos(theta); // Adjust this if shrapnel appears to go too high
				double z = Math.sin(theta) * Math.sin(phi);

				// Create a vector with some upward motion
				Vec3d randomizedDir = new Vec3d(x, y, z).normalize().scale(1.0); // Adjust speed scaling as necessary

				// Create and spawn the shrapnel
				EntityShrapnel entityShrapnel = new EntityShrapnel(world, pos.x, pos.y, pos.z, randomizedDir.x, randomizedDir.y, randomizedDir.z, shrapnel);
				entityShrapnel.setNoGravity(false); // Ensure gravity is applied
				entityShrapnel.canIgnite();
				entityShrapnel.setFire(10); // Set the shrapnel on fire for 10 seconds
				world.spawnEntity(entityShrapnel);
			}
		}

		EntityWhitePhosphorus entityWhitePhosphorus = new EntityWhitePhosphorus(world);
		entityWhitePhosphorus.setPosition(pos.x, pos.y, pos.z);
		world.spawnEntity(entityWhitePhosphorus);
		
	}
}
