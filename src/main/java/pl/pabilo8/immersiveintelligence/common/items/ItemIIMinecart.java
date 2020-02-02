package pl.pabilo8.immersiveintelligence.common.items;

import net.minecraft.block.BlockRailBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.entity.minecarts.*;

/**
 * Created by Pabilo8 on 2019-06-01.
 */
public class ItemIIMinecart extends ItemIIBase
{
	public static final int META_MINECART_WOODEN_CRATE = 0;
	public static final int META_MINECART_REINFORCED_CRATE = 1;
	public static final int META_MINECART_STEEL_CRATE = 2;
	public static final int META_MINECART_WOODEN_BARREL = 3;
	public static final int META_MINECART_METAL_BARREL = 4;

	public ItemIIMinecart()
	{
		super("minecart", 1, "wooden_crate", "reinforced_crate", "steel_crate", "wooden_barrel", "metal_barrel");
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		IBlockState iblockstate = world.getBlockState(pos);

		if(!BlockRailBase.isRailBlock(iblockstate))
		{
			return EnumActionResult.FAIL;
		}
		else
		{
			ItemStack stack = player.getHeldItem(hand);

			if(!world.isRemote)
			{
				BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = iblockstate.getBlock() instanceof BlockRailBase?((BlockRailBase)iblockstate.getBlock()).getRailDirection(world, pos, iblockstate, null): BlockRailBase.EnumRailDirection.NORTH_SOUTH;
				double d0 = 0.0D;

				if(blockrailbase$enumraildirection.isAscending())
				{
					d0 = 0.5D;
				}

				EntityMinecart ent = null;

				switch(stack.getMetadata())
				{
					case META_MINECART_WOODEN_CRATE:
					{
						ent = new EntityMinecartCrateWooden(world, (double)pos.getX()+0.5D, (double)pos.getY()+0.0625D+d0, (double)pos.getZ()+0.5D);
					}
					break;
					case META_MINECART_REINFORCED_CRATE:
					{
						ent = new EntityMinecartCrateReinforced(world, (double)pos.getX()+0.5D, (double)pos.getY()+0.0625D+d0, (double)pos.getZ()+0.5D);
					}
					break;
					case META_MINECART_STEEL_CRATE:
					{
						ent = new EntityMinecartCrateSteel(world, (double)pos.getX()+0.5D, (double)pos.getY()+0.0625D+d0, (double)pos.getZ()+0.5D);
					}
					break;
					case META_MINECART_WOODEN_BARREL:
					{
						ent = new EntityMinecartBarrelWooden(world, (double)pos.getX()+0.5D, (double)pos.getY()+0.0625D+d0, (double)pos.getZ()+0.5D);
					}
					break;
					case META_MINECART_METAL_BARREL:
					{
						ent = new EntityMinecartBarrelSteel(world, (double)pos.getX()+0.5D, (double)pos.getY()+0.0625D+d0, (double)pos.getZ()+0.5D);
					}
					break;
				}

				if(ent!=null)
				{
					if(stack.hasDisplayName())
					{
						ent.setCustomNameTag(stack.getDisplayName());
					}

					world.spawnEntity(ent);
				}
			}

			stack.shrink(1);
			return EnumActionResult.SUCCESS;
		}
	}
}
