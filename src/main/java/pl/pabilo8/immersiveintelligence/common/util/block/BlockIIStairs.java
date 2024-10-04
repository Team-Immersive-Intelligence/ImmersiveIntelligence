package pl.pabilo8.immersiveintelligence.common.util.block;

import blusunrize.immersiveengineering.api.Lib;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.util.block.IIIStateMappings.IIISingleMetaStateMappings;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Pabilo8
 * @since 28.04.2021
 */
public class BlockIIStairs extends BlockStairs implements IIISingleMetaStateMappings
{
	public boolean hasFlavour = false;
	public boolean isFlammable = false;
	public String name, blockstateName;
	float explosionResistance;
	BlockRenderLayer renderLayer = BlockRenderLayer.SOLID;

	public BlockIIStairs(String name, @Nullable String subName, IBlockState state)
	{
		super(state);
		this.name = name+(subName!=null?("_"+subName): "");
		this.blockstateName = name+(subName!=null?("/"+subName): "");
		this.setUnlocalizedName(ImmersiveIntelligence.MODID+"."+this.name);
		this.setCreativeTab(IIContent.II_CREATIVE_TAB);
		this.useNeighborBrightness = true;
		this.explosionResistance = this.blockResistance/5f;
		IIContent.BLOCKS.add(this);
		IIContent.ITEMS.add(new ItemBlockIIStairs(this));
	}

	public BlockIIStairs setFlammable(boolean b)
	{
		this.isFlammable = b;
		return this;
	}

	public BlockIIStairs setHasFlavour(boolean hasFlavour)
	{
		this.hasFlavour = hasFlavour;
		return this;
	}

	/**
	 * Returns how much this block can resist explosions from the passed in entity.
	 */
	@Override
	public float getExplosionResistance(Entity exploder)
	{
		return explosionResistance;
	}

	public BlockIIStairs setExplosionResistance(float explosionResistance)
	{
		this.explosionResistance = explosionResistance;
		return this;
	}

	@Override
	public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		if(this.renderLayer!=BlockRenderLayer.SOLID)
			return false;
		return super.doesSideBlockRendering(state, world, pos, face);
	}

	public BlockIIStairs setRenderLayer(BlockRenderLayer layer)
	{
		this.renderLayer = layer;
		return this;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return renderLayer;
	}

	@Override
	public String getMappingsName()
	{
		return blockstateName;
	}

	public static class ItemBlockIIStairs extends ItemBlock
	{
		public ItemBlockIIStairs(Block b)
		{
			super(b);
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
		public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> itemList)
		{
			if(this.isInCreativeTab(tab))
				this.block.getSubBlocks(tab, itemList);
		}

		/**
		 * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have different
		 * names based on their damage or NBT.
		 */
		@Override
		public String getUnlocalizedName(ItemStack itemstack)
		{
			return super.getUnlocalizedName(itemstack);
		}

		/**
		 * allows items to add custom lines of information to the mouseover description
		 */
		@Override
		public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag tooltipFlag)
		{
			if(((BlockIIStairs)block).hasFlavour)
				tooltip.add(I18n.format(Lib.DESC_FLAVOUR+((BlockIIStairs)block).name));
		}
	}
}
