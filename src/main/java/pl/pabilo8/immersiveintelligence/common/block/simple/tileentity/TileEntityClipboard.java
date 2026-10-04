package pl.pabilo8.immersiveintelligence.common.block.simple.tileentity;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.ITileDrop;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.item.tools.ItemIIClipboard;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IIIGuiMultiblockTile;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IIIInventory;
import pl.pabilo8.immersiveintelligence.common.util.raytracer.AxisAlignedFacingBB;
import pl.pabilo8.immersiveintelligence.common.util.tile.TileEntityIIDirectional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Stores clipboard entries while the Engineer's Clipboard is placed in the world.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 21.09.2026
 */
public class TileEntityClipboard extends TileEntityIIDirectional implements IIIGuiMultiblockTile, IIIInventory,
		IBlockBounds, ITileDrop
{
	private static final FacingSettings FACING_SETTINGS = new FacingSettings(FacingLimitation.SIDE_CLICKED).withRotation(true);
	private static final AxisAlignedFacingBB BOUNDS = new AxisAlignedFacingBB(0.1875f, 0.0625f, 0.875f, 0.8125f, 1f, 1f);


	private NBTTagList entries = new NBTTagList();

	@Override
	public void readCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		if(nbt.hasKey("entries", 9))
			entries = ItemIIClipboard.sanitiseEntries(nbt.getTagList("entries", 10));
	}

	@Override
	public void writeCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);
		nbt.setTag("entries", entries.copy());
	}

	@Override
	public void receiveMessageFromServer(@Nonnull NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);
		if(message.hasKey("entries", 9))
			entries = ItemIIClipboard.sanitiseEntries(message.getTagList("entries", 10));
	}

	@Override
	public void receiveMessageFromClient(@Nonnull NBTTagCompound message)
	{
		NBTTagCompound safe = message.copy();
		if(safe.hasKey("entries", 9))
		{
			entries = ItemIIClipboard.sanitiseEntries(safe.getTagList("entries", 10));
			safe.setTag("entries", entries.copy());
			markDirty();
		}
		super.receiveMessageFromClient(safe);
	}

	public NBTTagList getEntries()
	{
		return (NBTTagList)entries.copy();
	}

	@Override
	public boolean canOpenGui()
	{
		if(!world.isRemote)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setTag("entries", entries.copy());
			IIPacketHandler.sendToClient(new MessageIITileSync(this, nbt));
		}
		return true;
	}

	@Override
	public IIGUI getGUI()
	{
		return IIGUI.CLIPBOARD_BLOCK;
	}

	@Override
	public TileEntity master()
	{
		return this;
	}

	@Nonnull
	@Override
	protected FacingSettings getFacingSettings()
	{
		return FACING_SETTINGS;
	}

	@Override
	public float[] getBlockBounds()
	{
		return BOUNDS.getFacingBounds(facing, false);
	}

	@Override
	public NonNullList<ItemStack> getInventory()
	{
		return NonNullList.withSize(0, ItemStack.EMPTY);
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return false;
	}

	@Override
	public ItemStack getTileDrop(@Nullable EntityPlayer player, IBlockState state)
	{
		ItemStack stack = new ItemStack(IIContent.itemClipboard);
		ItemIIClipboard.setEntries(stack, entries);
		return stack;
	}

	@Override
	public void readOnPlacement(@Nullable EntityLivingBase placer, ItemStack stack)
	{
		if(stack.getItem()==IIContent.itemClipboard)
			entries = ItemIIClipboard.getEntries(stack);
	}

	@Nullable
	@Override
	public ITextComponent getDisplayName()
	{
		return new TextComponentTranslation("tile.immersiveintelligence.clipboard.clipboard.name");
	}
}
