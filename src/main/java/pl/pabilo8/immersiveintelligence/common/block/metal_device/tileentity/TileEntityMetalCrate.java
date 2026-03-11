package pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.wooden.TileEntityWoodenCrate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.compat.CratesFeltBlueHelper;

import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon (avalon@iiteam.net)
 * @since 17.05.2019
 * @since 03.03.2026
 */
public class TileEntityMetalCrate extends TileEntityWoodenCrate implements IGuiTile
{
	@Override
	public boolean canOpenGui()
	{
		return true;
	}

	@Override
	public int getGuiID()
	{
		return IIGUI.METAL_CRATE.ordinal();
	}

	@Override
	public TileEntity getGuiMaster()
	{
		return this;
	}

	@Override
	@Nullable
	public ITextComponent getDisplayName()
	{
		return name!=null?new TextComponentString(name): new TextComponentTranslation("tile."+ImmersiveIntelligence.MODID+".metal_device.metal_crate.name");
	}


	@Override
	public NonNullList<ItemStack> getTileDrops(EntityPlayer player, IBlockState state)
	{
		if(CratesFeltBlueHelper.loaded)
		{
			NonNullList<ItemStack> drops = NonNullList.create();
			ItemStack stack = new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));
			if(this.name!=null)
				stack.setStackDisplayName(this.name);
			drops.add(stack);
			for(ItemStack item : getInventory())
				if(!item.isEmpty())
					drops.add(item.copy());
			return drops;
		}
		return NonNullList.from(ItemStack.EMPTY, getTileDrop(player, state));
	}
}
