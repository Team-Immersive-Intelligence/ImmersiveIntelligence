package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.utils.upgrade.IManagedUpgradableDevice;
import pl.pabilo8.immersiveintelligence.api.utils.upgrade.UpgradeManager;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockFlagpole;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIGeneric;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 30.08.2025
 * @ii-approved 0.3.1
 * @since 04.03.2021
 */
public class TileEntityFlagpole extends TileEntityMultiblockIIGeneric<TileEntityFlagpole> implements IPlayerInteraction, IManagedUpgradableDevice<TileEntityFlagpole>
{
	@SyncNBT(events = SyncEvents.TILE_CUSTOM1)
	public ItemStack flag = ItemStack.EMPTY;
	@SyncNBT(name = "upgrades", events = SyncEvents.TILE_UPGRADES_MODIFIED)
	public UpgradeManager<TileEntityFlagpole> upgradeManager;
	private Ticket ticket = null;

	public TileEntityFlagpole()
	{
		super(MultiblockFlagpole.INSTANCE);
		this.upgradeManager = new UpgradeManager<>(this);
	}

	@Override
	public void validate()
	{
		super.validate();
		if(!isDummy())
		{
			if(!world.isRemote)
			{
				if(ticket!=null)
					ticket.getChunkList();
				else
					ticket = ForgeChunkManager.requestTicket(ImmersiveIntelligence.INSTANCE, this.getWorld(), Type.NORMAL);
			}
		}
	}

	@Override
	protected void onUpdate()
	{
		if(!world.isRemote&&ticket!=null)
			ForgeChunkManager.forceChunk(ticket, this.world.getChunkFromBlockCoords(getPos()).getPos());
	}

	@Override
	protected int[] listAllPOI(MultiblockPOI poi)
	{
		if(poi==MultiblockPOI.MISC_FLAGPOLE)
			return getPOI("post");
		return new int[0];
	}

	@Override
	public boolean interact(EnumFacing side, EntityPlayer player, EnumHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		TileEntityFlagpole master = master();
		if(!world.isRemote&&master!=null&&isPOI("post"))
		{
			if(master.flag.isEmpty()&&heldItem.getItem()==Items.BANNER)
			{
				master.flag = heldItem.copy();
				master.flag.setCount(1);
				heldItem.shrink(1);
				master.updateTileForEvent(SyncEvents.TILE_CUSTOM1);
				return true;
			}
			else if(!master.flag.isEmpty()&&Utils.isWirecutter(heldItem))
			{
				player.inventory.addItemStackToInventory(master.flag.copy());
				master.flag = ItemStack.EMPTY;
				master.updateTileForEvent(SyncEvents.TILE_CUSTOM1);
				return true;
			}
		}

		return false;
	}

	@Override
	public void disassemble()
	{
		super.disassemble();
		if(isDummy()||flag.isEmpty())
			return;

		Utils.dropStackAtPos(world, getBlockPosForPos(67), flag.copy());
		flag = ItemStack.EMPTY;
		ForgeChunkManager.releaseTicket(ticket);
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return false;
	}

	@Nonnull
	@Override
	public UpgradeManager<TileEntityFlagpole> getUpgradeManager()
	{
		return upgradeManager;
	}
}
