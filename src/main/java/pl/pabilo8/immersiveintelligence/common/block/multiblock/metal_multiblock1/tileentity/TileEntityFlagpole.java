package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import pl.pabilo8.immersiveintelligence.api.style.IStyleCustomizable;
import pl.pabilo8.immersiveintelligence.api.style.StyleCustomization;
import pl.pabilo8.immersiveintelligence.api.upgrade.IManagedUpgradableDevice;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeManager;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.MachineStyle;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Flagpole;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockFlagpole;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.property.IOwnableProperty;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IIIGuiMultiblockTile;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IManagedDamageResistantMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.MultiblockHealth;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 30.08.2025
 * @ii-approved 0.3.1
 * @since 04.03.2021
 */
public class TileEntityFlagpole extends TileEntityMultiblockIIBase<TileEntityFlagpole> implements IPlayerInteraction, IManagedUpgradableDevice<TileEntityFlagpole>,
		IStyleCustomizable, IOwnableProperty, IIIGuiMultiblockTile, IManagedDamageResistantMultiblock
{
	@SyncNBT(events = SyncEvents.TILE_CUSTOM1)
	public ItemStack flag = ItemStack.EMPTY;
	@SyncNBT(name = "upgrades", events = SyncEvents.TILE_UPGRADES_MODIFIED)
	public UpgradeManager<TileEntityFlagpole> upgradeManager;
	@SyncNBT(events = {SyncEvents.TILE_UPGRADES_MODIFIED, SyncEvents.TILE_CLIENT_MESSAGE})
	public StyleCustomization style;
	@SyncNBT(events = SyncEvents.TILE_DAMAGED)
	public MultiblockHealth health;
	//private Ticket ticket = null;
	@SyncNBT(events = SyncEvents.TILE_OWNERSHIP_MODIFIED)
	public OwnerIdentity ownerIdentity;

	public TileEntityFlagpole()
	{
		super(MultiblockFlagpole.INSTANCE);
		this.upgradeManager = new UpgradeManager<>(this);
		this.ownerIdentity = DiplomacyHandler.NEUTRAL;
		this.style = new StyleCustomization(MultiblockFlagpole.STYLE_CONSTRAINTS);
		this.health = new MultiblockHealth(this, Flagpole.baseHealth);
	}

	@Override
	protected void dummyCleanup()
	{
		this.flag = null;
		this.upgradeManager = null;
		this.ownerIdentity = null;
		this.style = null;
		this.health = null;
	}

	@Override
	protected void onUpdate()
	{
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
			if(master.flag.isEmpty()&&heldItem.getItem()==Items.BANNER)
			{
				master.flag = heldItem.copy();
				master.flag.setCount(1);
				heldItem.shrink(1);
				master.updateTileForEvent(SyncEvents.TILE_CUSTOM1);
				//forceTileUpdate();
				return true;
			}
			else if(!master.flag.isEmpty()&&Utils.isWirecutter(heldItem))
			{
				player.inventory.addItemStackToInventory(master.flag.copy());
				master.flag = ItemStack.EMPTY;
				master.updateTileForEvent(SyncEvents.TILE_CUSTOM1);
				//forceTileUpdate();
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
	public MachineStyle getUpgradableMachineStyle()
	{
		switch(style.getStyle())
		{
			case "sandbags":
				return MachineStyle.SANDBAGS;
			case "wooden":
				return MachineStyle.WOODEN;
			case "steel":
				return MachineStyle.STEEL;
			case "bricks":
				return MachineStyle.BRICKS;
			case "concrete":
				return MachineStyle.CONCRETE;
		}
		return MachineStyle.STEEL;
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
		if(!world.isRemote)
			updateTileForEvent(SyncEvents.TILE_OWNERSHIP_MODIFIED);
		IILogger.debug("Owner Identity for "+uuid+" : "+ownerIdentity+" / world is "+(world.isRemote?"remote": "local"));
	}

	@Override
	public int getChunkOwnershipRadius()
	{
		return Flagpole.chunkClaimRadius;
	}

	@Override
	public int getChunkLoadingRange()
	{
		return Math.min(Flagpole.chunkClaimRadius, Flagpole.maxChunksLoadedRadius);
	}

	//--- IStyleCustomizable ---//

	@Override
	public StyleCustomization getStyle()
	{
		return style;
	}

	//--- IManagedDamageResistantMultiblock ---//

	@Override
	public MultiblockHealth getHealthManager()
	{
		return health;
	}

	@Override
	public float getExplosionResistance()
	{
		return 3;
	}

	//--- IIIGuiMultiblockTile ---//

	@Override
	public boolean canOpenGui()
	{
		return true;
	}

	@Override
	public IIGUI getGUI()
	{
		return IIGUI.FLAGPOLE;
	}
}
