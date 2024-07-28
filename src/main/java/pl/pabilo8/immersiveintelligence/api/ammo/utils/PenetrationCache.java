package pl.pabilo8.immersiveintelligence.api.ammo.utils;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.ammo.penetration.DamageBlockPos;
import pl.pabilo8.immersiveintelligence.api.ammo.penetration.IPenetrationHandler;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Graphics;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBlockDamageSync;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pabilo8
 * @ii-approved 0.3.1
 * @since 29.03.2024
 */
public class PenetrationCache
{
	/**
	 * List of blocks that have been damaged by a projectile
	 */
	public static ArrayList<DamageBlockPos> blockDamage = new ArrayList<>();
	/**
	 * Client side version of {@link #blockDamage}
	 */
	public static ArrayList<DamageBlockPos> blockDamageClient = new ArrayList<DamageBlockPos>()
	{
		@Override
		public boolean add(DamageBlockPos damageBlockPos)
		{
			if(size() > Graphics.maxPenetratedBlocks)
				remove(0);
			return super.add(damageBlockPos);
		}
	};

	public static float getBlockHitpoints(IPenetrationHandler pen, BlockPos pos, World world)
	{
		float hp = pen.getIntegrity()/pen.getThickness();
		DamageBlockPos blockHitPos = new DamageBlockPos(pos, world, pen.getIntegrity());

		for(DamageBlockPos damageBlockPos : blockDamage)
		{
			if(damageBlockPos.equals(blockHitPos))
				return damageBlockPos.damage;
		}

		blockDamage.add(new DamageBlockPos(blockHitPos, hp));
		return hp;
	}

	//--- Block Damage ---//

	public static void dealBlockDamage(World world, Vec3d direction, float bulletDamage, BlockPos pos, IPenetrationHandler pen)
	{
		if(!IIAmmoUtils.ammoBreaksBlocks||!pen.canBeDamaged())
			return;

		DamageBlockPos dimensionBlockPos = new DamageBlockPos(pos, world, pen.getIntegrity());
		float newHp = getBlockHitpoints(pen, pos, world)-(bulletDamage*pen.getThickness());
		if(newHp > 0)
		{
			List<DamageBlockPos> list = blockDamage.stream().filter(damageBlockPos -> damageBlockPos.equals(dimensionBlockPos)).collect(Collectors.toList());
			if(!list.isEmpty())
				list.forEach(damageBlockPos -> damageBlockPos.damage = newHp);
			else
				blockDamage.add(new DamageBlockPos(dimensionBlockPos, newHp));

			IIPacketHandler.sendToClient(dimensionBlockPos, world,
					new MessageBlockDamageSync(new DamageBlockPos(dimensionBlockPos, newHp/(pen.getIntegrity()/pen.getThickness())), direction));
		}
		else if(newHp <= 0)
		{
			blockDamage.removeIf(damageBlockPos -> damageBlockPos.equals(dimensionBlockPos));
			world.getBlockState(pos).getBlock().breakBlock(world, pos, world.getBlockState(pos));
			world.destroyBlock(dimensionBlockPos, false);

			IIPacketHandler.sendToClient(dimensionBlockPos, world,
					new MessageBlockDamageSync(new DamageBlockPos(dimensionBlockPos, newHp/(pen.getIntegrity()/pen.getThickness())), direction));
		}
	}
}
