package pl.pabilo8.immersiveintelligence.common.item.tools;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.entity.EntityTripodPeriscope;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIIBase;

import java.util.List;

/**
 * @author Pabilo8
 * @since 23.01.2021
 */
public class ItemIITripodPeriscope extends ItemIIBase
{
	public ItemIITripodPeriscope()
	{
		super("tripod_periscope", 1);
	}

	/**
	 * Called when a Block is right-clicked with this Item
	 */
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(facing==EnumFacing.DOWN)
		{
			return EnumActionResult.FAIL;
		}
		else
		{
			boolean flag = worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);
			BlockPos blockpos = flag?pos: pos.offset(facing);
			ItemStack itemstack = player.getHeldItem(hand);

			if(!player.canPlayerEdit(blockpos, facing, itemstack))
			{
				return EnumActionResult.FAIL;
			}
			else
			{
				BlockPos blockpos1 = blockpos.up();
				boolean flag1 = !worldIn.isAirBlock(blockpos)&&!worldIn.getBlockState(blockpos).getBlock().isReplaceable(worldIn, blockpos);
				flag1 = flag1|(!worldIn.isAirBlock(blockpos1)&&!worldIn.getBlockState(blockpos1).getBlock().isReplaceable(worldIn, blockpos1));

				if(flag1)
				{
					return EnumActionResult.FAIL;
				}
				else
				{
					double d0 = blockpos.getX();
					double d1 = blockpos.getY();
					double d2 = blockpos.getZ();
					List<Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(d0, d1, d2, d0+1.0D, d1+2.0D, d2+1.0D));

					if(!list.isEmpty())
					{
						return EnumActionResult.FAIL;
					}
					else
					{
						if(!worldIn.isRemote)
						{
							worldIn.setBlockToAir(blockpos);
							worldIn.setBlockToAir(blockpos1);
							EntityTripodPeriscope periscope = new EntityTripodPeriscope(worldIn);
							periscope.setPosition(d0+0.5D, d1, d2+0.5D);
							float f = (float)MathHelper.floor((MathHelper.wrapDegrees(player.rotationYaw-180.0F)+22.5F)/45.0F)*45.0F;
							periscope.setLocationAndAngles(d0+0.5D, d1, d2+0.5D, f, 0.0F);
							periscope.periscopeYaw = f;
							ItemMonsterPlacer.applyItemEntityDataToEntity(worldIn, player, itemstack, periscope);
							worldIn.spawnEntity(periscope);
							worldIn.playSound(null, periscope.posX, periscope.posY, periscope.posZ, SoundEvents.ENTITY_ARMORSTAND_PLACE, SoundCategory.BLOCKS, 0.75F, 0.8F);
						}
						itemstack.shrink(1);
						return EnumActionResult.SUCCESS;
					}
				}
			}
		}
	}
}
