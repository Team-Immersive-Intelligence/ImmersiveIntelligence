package pl.pabilo8.immersiveintelligence.api.ammo.utils;

import blusunrize.immersiveengineering.common.blocks.TileEntityMultiblockPart;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.ammo.penetration.DamageBlockPos;
import pl.pabilo8.immersiveintelligence.api.ammo.penetration.IPenetrationHandler;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Graphics;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBlockDamageSync;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IDamageResistantMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.tile.TileEntityIIBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores projectile block damage and block-burning progress.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @updated 14.08.2026
 * @since 29.03.2024
 */
public class PenetrationCache
{
	/**
	 * Blocks damaged by projectiles.
	 */
	public static ArrayList<DamageBlockPos> blockDamage = new ArrayList<>();
	/**
	 * Blocks being turned into burned variants.
	 */
	public static ArrayList<DamageBlockPos> blockBurnDamage = new ArrayList<>();
	/**
	 * Client-side version of {@link #blockDamage}.
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

	/**
	 * Gets remaining hit points for projectile damage.
	 */
	public static float getBlockHitpoints(IPenetrationHandler pen, BlockPos pos, World world)
	{
		return getCachedHitpoints(blockDamage, new DamageBlockPos(pos, world, pen.getIntegrity()),
				pen.getIntegrity()/pen.getThickness());
	}

	//--- Block Damage ---//

	/**
	 * Applies projectile damage to a block.
	 */
	public static void dealBlockDamage(World world, Vec3d direction, float bulletDamage, BlockPos pos, IPenetrationHandler pen)
	{
		if(!IIAmmoUtils.ammoBreaksBlocks||!pen.canBeDamaged())
			return;

		DamageBlockPos dimensionBlockPos = new DamageBlockPos(pos, world, pen.getIntegrity());
		if(world.getTileEntity(pos) instanceof IDamageResistantMultiblock)
		{
			IDamageResistantMultiblock mb = (IDamageResistantMultiblock)world.getTileEntity(pos);
			if(mb instanceof TileEntityMultiblockPart)
				mb = (IDamageResistantMultiblock)((TileEntityMultiblockPart<?>)mb).master();
			if(mb!=null)
			{
				if(mb.damageHealth(bulletDamage*pen.getThickness()))
				{
					clearBlockCaches(dimensionBlockPos);
					IBlockState state = world.getBlockState(pos);
					state.getBlock().breakBlock(world, pos, state);
					world.destroyBlock(dimensionBlockPos, false);
				}
				else if(mb instanceof TileEntityMultiblockIIBase)
					((TileEntityMultiblockIIBase<?>)mb).updateTileForEvent(SyncEvents.TILE_DAMAGED);
				else if(mb instanceof TileEntityIIBase)
					((TileEntityIIBase)mb).updateTileForEvent(SyncEvents.TILE_DAMAGED);
			}
			return;
		}

		float maxHp = pen.getIntegrity()/pen.getThickness();
		float newHp = getCachedHitpoints(blockDamage, dimensionBlockPos, maxHp)-(bulletDamage*pen.getThickness());
		if(newHp > 0)
			setCachedHitpoints(blockDamage, dimensionBlockPos, newHp);
		else
		{
			clearBlockCaches(dimensionBlockPos);
			IBlockState state = world.getBlockState(pos);
			state.getBlock().breakBlock(world, pos, state);
			world.destroyBlock(dimensionBlockPos, false);
		}

		IIPacketHandler.sendToClient(dimensionBlockPos, world,
				new MessageBlockDamageSync(new DamageBlockPos(dimensionBlockPos, newHp/maxHp), direction));
	}

	//--- Block Burning ---//

	/**
	 * Applies burning damage and converts a block when the burn threshold is reached.
	 *
	 * @return true if the block has a valid burnt variant
	 */
	public static boolean dealBlockBurnDamage(World world, float burnDamage, BlockPos pos, IPenetrationHandler pen)
	{
		if(world.isRemote||burnDamage <= 0||!pen.hasFlammableVariant())
			return false;

		IBlockState currentState = world.getBlockState(pos);
		IBlockState burntState = pen.getFlammableVariant(currentState);
		if(burntState==null)
			return false;

		DamageBlockPos dimensionBlockPos = new DamageBlockPos(pos, world, pen.getIntegrity());
		float maxHp = pen.getIntegrity()/Math.max(0.001f, pen.getThickness());
		float newHp = getCachedHitpoints(blockBurnDamage, dimensionBlockPos, maxHp)-burnDamage;
		if(newHp > 0)
			setCachedHitpoints(blockBurnDamage, dimensionBlockPos, newHp);
		else
		{
			clearBlockCaches(dimensionBlockPos);
			world.setBlockState(pos, burntState, 3);
		}
		return true;
	}

	private static float getCachedHitpoints(List<DamageBlockPos> cache, DamageBlockPos pos, float defaultHitpoints)
	{
		for(DamageBlockPos cached : cache)
			if(cached.equals(pos))
				return cached.damage;

		cache.add(new DamageBlockPos(pos, defaultHitpoints));
		return defaultHitpoints;
	}

	private static void setCachedHitpoints(List<DamageBlockPos> cache, DamageBlockPos pos, float hitpoints)
	{
		for(DamageBlockPos cached : cache)
			if(cached.equals(pos))
			{
				cached.damage = hitpoints;
				return;
			}
		cache.add(new DamageBlockPos(pos, hitpoints));
	}

	private static void clearBlockCaches(DamageBlockPos pos)
	{
		blockDamage.removeIf(cached -> cached.equals(pos));
		blockBurnDamage.removeIf(cached -> cached.equals(pos));
	}
}
