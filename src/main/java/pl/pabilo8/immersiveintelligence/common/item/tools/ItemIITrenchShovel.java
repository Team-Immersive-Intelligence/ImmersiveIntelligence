package pl.pabilo8.immersiveintelligence.common.item.tools;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.items.tools.ItemIEShovel;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

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
public class ItemIITrenchShovel extends ItemIEShovel
{
	public ItemIITrenchShovel()
	{
		super(Lib.MATERIAL_Steel, "trench_shovel", "shovel", "plateSteel");
		fixupItem();
	}

	public void fixupItem()
	{
		//First, get the item out of IE's registries.
		Item rItem = IEContent.registeredIEItems.remove(IEContent.registeredIEItems.size()-1);
		if(rItem!=this) throw new IllegalStateException("fixupItem was not called at the appropriate time");

		//Now, reconfigure the block to match our mod.
		this.setUnlocalizedName(ImmersiveIntelligence.MODID+".trench_shovel");
		this.setCreativeTab(IIContent.II_CREATIVE_TAB);

		//And add it to our registries.
		IIContent.ITEMS.add(this);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add(IIUtils.getItalicString(I18n.format(IIReference.DESCRIPTION_KEY+"electric_hammer")));
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
