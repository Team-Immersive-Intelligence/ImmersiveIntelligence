package pl.pabilo8.immersiveintelligence.common.util.item;


import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;
import pl.pabilo8.immersiveintelligence.common.IISounds;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * An interface for items that place entities, i.e. {@link pl.pabilo8.immersiveintelligence.common.item.tools.ItemIITripodPeriscope}
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 23.05.2026
 */
public interface IItemEntityPlacer<E extends Entity>
{
	default EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing,
	                                   float hitX, float hitY, float hitZ)
	{
		//Can't place on the same position as player
		if(facing==EnumFacing.DOWN)
			return EnumActionResult.FAIL;

		//Check if the entity can be placed on, or on top of the clicked block
		boolean canBePlaced = world.getBlockState(pos).getBlock().isReplaceable(world, pos);
		BlockPos blockpos = canBePlaced?pos: pos.offset(facing);
		ItemStack stack = player.getHeldItem(hand);
		if(!player.canPlayerEdit(blockpos, facing, stack))
			return EnumActionResult.FAIL;

		//Get taken space and go through blocks
		AxisAlignedBB takenSpace = getPlacedSpace(blockpos);
		for(int x = (int)Math.floor(takenSpace.minX), xMax = (int)Math.ceil(takenSpace.maxX); x < xMax; x++)
			for(int y = (int)Math.floor(takenSpace.minY), yMax = (int)Math.ceil(takenSpace.maxY); y < yMax; y++)
				for(int z = (int)Math.floor(takenSpace.minZ), zMax = (int)Math.ceil(takenSpace.maxZ); z < zMax; z++)
				{
					BlockPos freeSpacePos = new BlockPos(x, y, z);
					if(!world.isAirBlock(freeSpacePos)&&!world.getBlockState(freeSpacePos).getBlock().isReplaceable(world, freeSpacePos))
						return EnumActionResult.FAIL;
				}

		//Check if there aren't any entities there
		List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(null, takenSpace);
		if(!list.isEmpty())
			return EnumActionResult.FAIL;

		//Place the entity
		if(!world.isRemote)
		{
			ItemStack placedStack = ItemHandlerHelper.copyStackWithSize(stack, 1);
			E placed = getPlacedEntity(world, blockpos.getX()+0.5, blockpos.getY(), blockpos.getZ()+0.5, placedStack, player.rotationYaw, player.rotationPitch);
			//Clean the space
			for(int x = (int)Math.floor(takenSpace.minX), xMax = (int)Math.ceil(takenSpace.maxX); x < xMax; x++)
				for(int y = (int)Math.floor(takenSpace.minY), yMax = (int)Math.ceil(takenSpace.maxY); y < yMax; y++)
					for(int z = (int)Math.floor(takenSpace.minZ), zMax = (int)Math.ceil(takenSpace.maxZ); z < zMax; z++)
						world.setBlockToAir(new BlockPos(x, y, z));

			//Spawn the configured entity directly.
			world.spawnEntity(placed);

			//Play placing sound
			SoundEvent placedSound = getPlacedSound();
			if(placedSound!=null)
				world.playSound(null, blockpos, placedSound, SoundCategory.BLOCKS, 0.75f, 1f);

			//Mount the player on the entity
			if(shouldPlayerMountAfterPlacing())
				player.startRiding(placed);
		}
		stack.shrink(1);
		return EnumActionResult.SUCCESS;
	}

	@Nonnull
	AxisAlignedBB getPlacedSpace(BlockPos pos);

	@Nonnull
	E getPlacedEntity(World world, double x, double y, double z, ItemStack stack, float playerYaw, float playerPitch);

	@Nullable
	default SoundEvent getPlacedSound()
	{
		return IISounds.weaponPlaced;
	}

	default boolean shouldPlayerMountAfterPlacing()
	{
		return true;
	}
}
