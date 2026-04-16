package pl.pabilo8.immersiveintelligence.common.compat.nb;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentEffectShape;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentRole;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIExplosion;

public class AmmoComponentNetherFire extends AmmoComponent
{
	public AmmoComponentNetherFire()
	{
		super("nether_fire", 1f, ComponentRole.SPECIAL, IIColor.fromPackedRGB(0x36D9E6));
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("soul_lantern");
	}

	@Override
	public void onEffect(World world, Vec3d pos, Vec3d dir, ComponentEffectShape shape, NBTTagCompound tag, float componentSize, float multiplier, Entity owner)
	{
		IIPacketHandler.playRangedSound(world, pos, IISounds.explosionIncendiary, SoundCategory.NEUTRAL, (int)(20*multiplier), 1f, 1f);

		new IIExplosion(world, owner, pos, dir,
				4*componentSize, 4*multiplier, shape, false, componentSize > 0.125f, false)
				.doExplosion();

		BlockPos ppos = new BlockPos(pos);
		EntityLivingBase[] entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(ppos).grow(5*multiplier)).toArray(new EntityLivingBase[0]);
		for(EntityLivingBase e : entities)
		{
			e.setFire(800);
		}

		//TODO: Make it place "soul_fire" of BlockSoulFire blocks in orb shape without replacing blocks with fire blocks. Remove regular explosive.
	}
}
