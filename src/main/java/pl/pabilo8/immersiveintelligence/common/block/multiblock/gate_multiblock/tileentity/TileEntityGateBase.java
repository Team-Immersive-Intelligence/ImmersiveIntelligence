package pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.tileentity;

import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.api.energy.wires.redstone.IRedstoneConnector;
import blusunrize.immersiveengineering.api.energy.wires.redstone.RedstoneWireNetwork;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import blusunrize.immersiveengineering.common.util.IEDamageSources;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.upgrade.IManagedUpgradableDevice;
import pl.pabilo8.immersiveintelligence.api.upgrade.Upgrade;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeManager;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree;
import pl.pabilo8.immersiveintelligence.api.utils.IBooleanAnimatedPartsBlock;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.multiblock.MultiblockFenceGateBase;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBooleanAnimatedPartsSync;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemUtils;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIConnectable;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockRedstoneNetwork;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 06.12.2023
 * @since 28.06.2019
 */
public abstract class TileEntityGateBase<T extends TileEntityGateBase<T>> extends TileEntityMultiblockIIConnectable<T>
		implements IBooleanAnimatedPartsBlock, IPlayerInteraction, IManagedUpgradableDevice<TileEntityGateBase<T>>, IRedstoneConnector
{
	@SyncNBT
	public MultiblockInteractablePart gate;
	protected MultiblockRedstoneNetwork<T> redstoneNetwork;
	@SyncNBT(name = "upgrades", events = SyncEvents.TILE_UPGRADES_MODIFIED)
	public UpgradeManager<TileEntityGateBase<T>> upgradeManager;

	public TileEntityGateBase(MultiblockFenceGateBase<T> multiblock)
	{
		super(multiblock);
		//Use common upgrades for all sorts of gates
		upgradeManager = new UpgradeManager<>(this, UpgradeTechTree.getTreeFor(getClass()));
		gate = new MultiblockInteractablePart(40);
		redstoneNetwork = new MultiblockRedstoneNetwork<>(((T)this));
	}

	@Override
	protected void dummyCleanup()
	{
		gate = null;
		redstoneNetwork = null;
		upgradeManager = null;
	}

	@Override
	protected void onUpdate()
	{
		gate.update();
		upgradeManager.update();
	}

	@Override
	public List<AxisAlignedBB> getBounds(boolean collision)
	{
		T master = master();
		if((isPOI("gate")&&master.gate.getProgress(0) > 0)||
				(isPOI("redstone")&&!master.isUpgradeInstalled(IIContent.UPGRADE_REDSTONE_ACTIVATION)))
			return Collections.singletonList(new AxisAlignedBB(0, 0, 0, 0, 0, 0));

		return super.getBounds(collision);
	}


	public boolean isDoorPart()
	{
		return isPOI("gate");
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return true;
	}

	@Override
	public void onAnimationChangeClient(boolean state, int part)
	{
		gate.setState(state);
	}

	@Override
	public void onAnimationChangeServer(boolean state, int part)
	{
		if(gate.setState(state))
		{
			world.playSound(null, getPos(), state?getOpeningSound(): getClosingSound(), SoundCategory.BLOCKS, 1, 1);
			IIPacketHandler.sendToClient(new MessageBooleanAnimatedPartsSync(0, state, this));
		}
	}

	protected abstract SoundEvent getOpeningSound();

	protected abstract SoundEvent getClosingSound();

	public abstract IBlockState getFenceState(@Nullable EnumFacing facingConnected);

	//--- IUpgradeStorageMachine ---//


	@Nonnull
	@Override
	public UpgradeManager<TileEntityGateBase<T>> getUpgradeManager()
	{
		return upgradeManager;
	}

	@Override
	public boolean removeUpgrade(Upgrade upgrade)
	{
		if(IManagedUpgradableDevice.super.removeUpgrade(upgrade))
		{
			if(upgrade==IIContent.UPGRADE_REDSTONE_ACTIVATION)
				ImmersiveNetHandler.INSTANCE.clearAllConnectionsFor(getPOIPos("redstone"), world, true);
			return true;
		}
		return false;
	}

	//--- IPlayerInteraction ---//

	@Override
	public boolean interact(EnumFacing side, EntityPlayer player, EnumHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		T master = master();
		if(!IIItemUtils.isWrench(player.getHeldItem(hand))&&master!=null&&!master.isUpgradeInstalled(IIContent.UPGRADE_REDSTONE_ACTIVATION))
		{
			if(!world.isRemote)
				master.onAnimationChangeServer(!master.gate.getState(), 0);
			return true;
		}
		return false;
	}

	@Override
	public void onEntityCollision(World world, Entity entity)
	{
		if(isPOI("razor")&&master().isUpgradeInstalled(IIContent.UPGRADE_RAZOR_WIRE)&&!entity.isDead)
			entity.attackEntityFrom(IEDamageSources.razorWire, 3f);
		super.onEntityCollision(world, entity);
	}

	//--- IRedstoneConnector ---//

	@Override
	public boolean canConnect()
	{
		return super.canConnect()&&master().isUpgradeInstalled(IIContent.UPGRADE_REDSTONE_ACTIVATION);
	}

	@Override
	protected boolean isMatchingCable(WireType cableType)
	{
		return Objects.equals(cableType.getCategory(), WireType.REDSTONE_CATEGORY);
	}

	@Override
	public RedstoneWireNetwork getNetwork()
	{
		return master().redstoneNetwork.getNetwork();
	}

	@Override
	public void setNetwork(RedstoneWireNetwork net)
	{
		master().redstoneNetwork.setNetwork(net);
	}

	@Override
	public void onChange()
	{
		master().onAnimationChangeServer(master().redstoneNetwork.getNetwork().channelValues[0] > 0, 0);
	}

	@Override
	public World getConnectorWorld()
	{
		return world;
	}

	@Override
	public void updateInput(byte[] signals)
	{

	}

	@Override
	public Vec3d getConnectionOffset(Connection con)
	{
		return new Vec3d(facing.getDirectionVec()).scale(0.25).addVector(0.5, 0.725, 0.5);
	}
}
