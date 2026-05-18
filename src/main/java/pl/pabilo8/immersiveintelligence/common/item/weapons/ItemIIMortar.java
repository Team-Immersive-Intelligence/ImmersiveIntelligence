package pl.pabilo8.immersiveintelligence.common.item.weapons;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.entity.mounted_weapon.EntityMortar;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIIBase;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 23.01.2021
 */
@IIItemProperties(category = IICategory.WARFARE)
public class ItemIIMortar extends ItemIIBase
{
	public ItemIIMortar()
	{
		super("mortar", 1);
	}

	/**
	 * Called when a Block is right-clicked with this Item
	 */
	@Nonnull
	@Override
	public EnumActionResult onItemUse(@Nonnull EntityPlayer player, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumHand hand,
									  @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(facing==EnumFacing.DOWN)
			return EnumActionResult.FAIL;
		else
		{
			//Check if block can be replaced, if not - check if the block above can be replaced. If not - fail
			boolean flag = world.getBlockState(pos).getBlock().isReplaceable(world, pos);
			BlockPos blockpos = flag?pos: pos.offset(facing);
			ItemStack itemstack = player.getHeldItem(hand);

			//Check if player can place on the block
			if(!player.canPlayerEdit(blockpos, facing, itemstack))
				return EnumActionResult.FAIL;
			else
			{
				BlockPos blockpos1 = blockpos.up();
				//Check bottom air block
				boolean canBePlaced = !world.isAirBlock(blockpos)&&!world.getBlockState(blockpos).getBlock().isReplaceable(world, blockpos);
				//Check top air block
				canBePlaced = canBePlaced|(!world.isAirBlock(blockpos1)&&!world.getBlockState(blockpos1).getBlock().isReplaceable(world, blockpos1));

				if(canBePlaced)
					return EnumActionResult.FAIL;
				else
				{
					List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(blockpos, blockpos.up()));
					if(!list.isEmpty())
						return EnumActionResult.FAIL;
					else
					{
						if(!world.isRemote)
						{
							EntityMortar mortar = new EntityMortar(world);
							mortar.setPosition(blockpos.getX(), blockpos.getY(), blockpos.getZ());
							mortar.rotationYaw = player.rotationYaw;

							world.spawnEntity(mortar);
							world.playSound(null, mortar.posX, mortar.posY, mortar.posZ, SoundEvents.ENTITY_ARMORSTAND_PLACE, SoundCategory.BLOCKS, 0.75F, 0.8F);
						}
						itemstack.shrink(1);
						return EnumActionResult.SUCCESS;
					}
				}
			}
		}
	}
}
