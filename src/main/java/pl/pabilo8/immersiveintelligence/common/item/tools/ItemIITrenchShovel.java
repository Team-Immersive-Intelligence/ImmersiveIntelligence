package pl.pabilo8.immersiveintelligence.common.item.tools;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.common.items.tools.ItemIEShovel;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.IIStringUtil;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Pabilo8
 * @since 29.08.2020
 * <p>
 * A tool used in construction of barricades, fortifications
 * Also required for burying mines
 * Can 'field reinforce' selected blocks to make them stronger / upgrade them
 */
@IIItemProperties(category = IICategory.TOOLS)
public class ItemIITrenchShovel extends ItemIEShovel
{
	public ItemIITrenchShovel()
	{
		super(Lib.MATERIAL_Steel, "trench_shovel", "shovel", "plateSteel");
		IIUtils.fixupItem(this, "trench_shovel");
	}


	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add(IIStringUtil.getItalicString(I18n.format(IIReference.DESCRIPTION_KEY+"trench_shovel")));
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		ItemStack itemstack = player.getHeldItem(hand);

		if(!player.canPlayerEdit(pos.offset(facing), facing, itemstack))
			return EnumActionResult.FAIL;
		else
		{
			IBlockState iblockstate = worldIn.getBlockState(pos);
			Block block = iblockstate.getBlock();

			if(facing!=EnumFacing.DOWN&&worldIn.getBlockState(pos.up()).getMaterial()==Material.AIR&&block==Blocks.GRASS)
			{
				IBlockState iblockstate1 = Blocks.GRASS_PATH.getDefaultState();
				worldIn.playSound(player, pos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);

				if(!worldIn.isRemote)
				{
					worldIn.setBlockState(pos, iblockstate1, 11);
					itemstack.damageItem(1, player);
				}

				return EnumActionResult.SUCCESS;
			}
			else
				return EnumActionResult.PASS;
		}
	}
}
