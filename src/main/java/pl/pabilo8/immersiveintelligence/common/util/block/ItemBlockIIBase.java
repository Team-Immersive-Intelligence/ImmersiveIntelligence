package pl.pabilo8.immersiveintelligence.common.util.block;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Pabilo8
 * @since 04.09.2022
 */
public class ItemBlockIIBase extends ItemBlock
{
	protected final BlockIIBase<?> block;

	public ItemBlockIIBase(BlockIIBase block)
	{
		super(block);
		this.block = block;
		setHasSubtypes(true);
	}

	/**
	 * Converts the given ItemStack damage value into a metadata value to be placed in the world when this Item is placed
	 * as a Block (mostly used with ItemBlocks).
	 */
	@Override
	public int getMetadata(int damageValue)
	{
		return damageValue;
	}

	/**
	 * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
	 */
	@Override
	public void getSubItems(@Nullable CreativeTabs tab, @Nonnull NonNullList<ItemStack> itemList)
	{
		if(this.isInCreativeTab(tab))
			this.block.getSubBlocks(tab, itemList);
	}

	/**
	 * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have different
	 * names based on their damage or NBT.
	 */
	@Override
	@Nonnull
	public String getUnlocalizedName(ItemStack stack)
	{
		return this.block.getTranslationKey(stack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public FontRenderer getFontRenderer(ItemStack stack)
	{
		return IIClientUtils.fontRegular;
	}

	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	@Override
	public void addInformation(@Nonnull ItemStack stack, @Nullable World world, @Nonnull List<String> tooltip, @Nonnull ITooltipFlag tooltipFlag)
	{
		if(!block.description[stack.getMetadata()].isEmpty())
			tooltip.add(TextFormatting.GRAY+block.description[stack.getMetadata()]);

		super.addInformation(stack, world, tooltip, tooltipFlag);
		if(ItemNBTHelper.hasKey(stack, "energyStorage"))
			tooltip.add(I18n.format(Lib.DESC+"info.energyStored",
					TextFormatting.GOLD.toString()+ItemNBTHelper.getInt(stack, "energyStorage")+TextFormatting.RESET
			));
		if(ItemNBTHelper.hasKey(stack, "tank"))
		{
			FluidStack fs = FluidStack.loadFluidStackFromNBT(ItemNBTHelper.getTagCompound(stack, "tank"));
			if(fs!=null)
				tooltip.add(fs.getLocalizedName()+": "+fs.amount+"mB");
		}
	}

	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState)
	{
		if(!this.block.canIEBlockBePlaced(world, pos, newState, side, hitX, hitY, hitZ, player, stack))
			return false;
		boolean ret = super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);
		if(ret)
			this.block.onIEBlockPlacedBy(world, pos, newState, side, hitX, hitY, hitZ, player, stack);
		return ret;
	}

	/**
	 * Called when a Block is right-clicked with this Item
	 */
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = player.getHeldItem(hand);
		IBlockState iblockstate = world.getBlockState(pos);
		Block block = iblockstate.getBlock();
		if(!block.isReplaceable(world, pos))
			pos = pos.offset(side);
		if(stack.getCount() > 0&&player.canPlayerEdit(pos, side, stack)&&canBlockBePlaced(world, pos, side, stack))
		{
			int i = this.getMetadata(stack.getMetadata());
			IBlockState iblockstate1 = this.block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, i, player, hand);
			if(placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, iblockstate1))
			{
				SoundType soundtype = world.getBlockState(pos).getBlock().getSoundType(world.getBlockState(pos), world, pos, player);
				world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume()+1.0F)/2.0F, soundtype.getPitch()*0.8F);
				if(!player.capabilities.isCreativeMode)
					stack.shrink(1);
			}
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.FAIL;
	}

	@Override
	public boolean canPlaceBlockOnSide(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull EnumFacing side, @Nullable EntityPlayer player, @Nonnull ItemStack stack)
	{
		Block block = worldIn.getBlockState(pos).getBlock();

		if(block==Blocks.SNOW_LAYER&&block.isReplaceable(worldIn, pos))
			side = EnumFacing.UP;
		else if(!block.isReplaceable(worldIn, pos))
			pos = pos.offset(side);

		return canBlockBePlaced(worldIn, pos, side, stack);
	}

	private boolean canBlockBePlaced(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing side, @Nonnull ItemStack stack)
	{
		Block block = world.getBlockState(pos).getBlock();

		AxisAlignedBB axisalignedbb = this.block.getCollisionBoundingBox(this.block.getStateFromMeta(stack.getItemDamage()), world, pos);
		if(axisalignedbb!=null&&!world.checkNoEntityCollision(axisalignedbb.offset(pos), null)) return false;
		return block.isReplaceable(world, pos)&&this.block.canPlaceBlockOnSide(world, pos, side);
	}
}
