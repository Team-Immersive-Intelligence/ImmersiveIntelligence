package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import pl.pabilo8.immersiveintelligence.api.style.IStyleCustomizable;
import pl.pabilo8.immersiveintelligence.api.style.StyleCustomization;
import pl.pabilo8.immersiveintelligence.api.upgrade.IManagedUpgradableDevice;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeManager;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.DeviceTier;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Flagpole;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockFlagpole;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyUtils;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.IOwnableProperty;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IIIGuiMultiblockTile;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 30.08.2025
 * @ii-approved 0.3.1
 * @since 04.03.2021
 */
public class TileEntityFlagpole extends TileEntityMultiblockIIBase<TileEntityFlagpole> implements IPlayerInteraction, IManagedUpgradableDevice<TileEntityFlagpole>,
		IStyleCustomizable, IOwnableProperty, IIIGuiMultiblockTile
{
	@SyncNBT(events = SyncEvents.TILE_CUSTOM1, nullable = true)
	public ItemStack flag = ItemStack.EMPTY;
	@SyncNBT(name = "upgrades", events = SyncEvents.TILE_UPGRADES_MODIFIED)
	public UpgradeManager<TileEntityFlagpole> upgradeManager;
	@SyncNBT(events = {SyncEvents.TILE_UPGRADES_MODIFIED, SyncEvents.TILE_CLIENT_MESSAGE})
	public StyleCustomization style;
	//private Ticket ticket = null;
	@SyncNBT(events = SyncEvents.TILE_OWNERSHIP_MODIFIED)
	public OwnerIdentity ownerIdentity;

	public TileEntityFlagpole()
	{
		super(MultiblockFlagpole.INSTANCE);
		this.upgradeManager = new UpgradeManager<>(this);
		this.style = new StyleCustomization(MultiblockFlagpole.STYLE_CONSTRAINTS);
		this.ownerIdentity = DiplomacyUtils.NEUTRAL;
	}

	@Override
	protected void dummyCleanup()
	{
		this.flag = ItemStack.EMPTY;
		this.upgradeManager = null;
		this.ownerIdentity = null;
		this.style = null;
	}

	@Override
	protected void onUpdate()
	{
		//Claim neighbouring chunks
		if(!world.isRemote&&world.getTotalWorldTime()%240==0)
			DiplomacyUtils.claimChunks(this);

		/*if(!world.isRemote&&ownerIdentity!=DiplomacyUtils.NEUTRAL)
			IILogger.info("Owner Identity for "+uuid+" : "+ownerIdentity);*/
		/*if(!world.isRemote)
		{
			if(ticket!=null)
				ticket.getChunkList();
			else
				ticket = ForgeChunkManager.requestTicket(ImmersiveIntelligence.INSTANCE, this.getWorld(), Type.NORMAL);
		}

		if(!world.isRemote&&ticket!=null)
			ForgeChunkManager.forceChunk(ticket, this.world.getChunkFromBlockCoords(getPos()).getPos());*/
	}

	@Override
	protected int[] listAllPOI(MultiblockPOI poi)
	{
		if(poi==MultiblockPOI.MISC_FLAGPOLE)
			return getPOI("pole");
		return new int[0];
	}

	@Override
	public boolean interact(EnumFacing side, EntityPlayer player, EnumHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		TileEntityFlagpole master = master();
		if(!world.isRemote&&master!=null&&isPOI("pole"))
		{
			if(player.isSneaking())
			{
				ArrayList<String> styles = new ArrayList<>(MultiblockFlagpole.STYLE_CONSTRAINTS.getStyles());
				String nextStyle = styles.get((styles.indexOf(master.style.getStyle())+1)%styles.size());
				master.style.withStyle(nextStyle);
				master.updateTileForEvent(SyncEvents.TILE_UPGRADES_MODIFIED);
				return true;
			}

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
		//ForgeChunkManager.releaseTicket(ticket);
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return false;
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return 1;
	}

	@Override
	public void doGraphicalUpdates(int slot)
	{

	}

	//--- IManagedUpgradableDevice ---//

	@Nonnull
	@Override
	public UpgradeManager<TileEntityFlagpole> getUpgradeManager()
	{
		return upgradeManager;
	}

	@Override
	public DeviceTier getUpgradableMachineTier()
	{
		return DeviceTier.STEEL;
	}

	//--- IOwnableProperty ---//

	@Override
	public OwnerIdentity getOwnerIdentity()
	{
		return ownerIdentity;
	}

	@Override
	public void setOwnerIdentity(OwnerIdentity ownerIdentity)
	{
		this.ownerIdentity = ownerIdentity;
		updateTileForEvent(SyncEvents.TILE_OWNERSHIP_MODIFIED);
		IILogger.info("Owner Identity for "+uuid+" : "+ownerIdentity+" / world is "+(world.isRemote?"remote": "local"));
	}

	@Override
	public int getChunkOwnershipRadius()
	{
		return Flagpole.chunkClaimRadius;
	}

	//--- IStyleCustomizable ---//

	@Override
	public StyleCustomization getStyle()
	{
		return style;
	}

	//--- IIIGuiMultiblockTile ---//

	@Override
	public boolean canOpenGui()
	{
		return true;
	}

	@Nullable
	@Override
	public TileEntity getGuiMaster()
	{
		return master();
	}

	@Override
	public IIGUI getGUI()
	{
		return IIGUI.FLAGPOLE;
	}
}
