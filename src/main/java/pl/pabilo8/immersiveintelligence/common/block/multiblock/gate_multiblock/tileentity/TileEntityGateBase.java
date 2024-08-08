package pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.tileentity;

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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.utils.IBooleanAnimatedPartsBlock;
import pl.pabilo8.immersiveintelligence.api.utils.IUpgradableMachine;
import pl.pabilo8.immersiveintelligence.api.utils.MachineUpgrade;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.multiblock.MultiblockFenceGateBase;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBooleanAnimatedPartsSync;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemUtil;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIConnectable;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockRedstoneNetwork;
import pl.pabilo8.immersiveintelligence.common.util.upgrade_system.IUpgradeStorageMachine;
import pl.pabilo8.immersiveintelligence.common.util.upgrade_system.UpgradeStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Pabilo8
 * @updated 06.12.2023
 * @since 28-06-2019
 */
public abstract class TileEntityGateBase<T extends TileEntityGateBase<T>> extends TileEntityMultiblockIIConnectable<T> implements IBooleanAnimatedPartsBlock, IPlayerInteraction, IUpgradeStorageMachine<TileEntityGateBase<T>>, IRedstoneConnector
{
	public MultiblockInteractablePart gate = new MultiblockInteractablePart(40);
	protected MultiblockRedstoneNetwork<T> redstoneNetwork = new MultiblockRedstoneNetwork<>(((T)this));
	protected UpgradeStorage<TileEntityGateBase<T>> upgradeStorage = new UpgradeStorage<>(this);

	public TileEntityGateBase(MultiblockFenceGateBase<T> multiblock)
	{
		super(multiblock);
	}

	@Override
	protected void dummyCleanup()
	{
		gate = null;
		redstoneNetwork = null;
		upgradeStorage = null;
	}

	@Override
	public void readCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);

		if(isDummy())
			return;
		this.gate.readFromNBT(nbt.getCompoundTag("gate"));
		this.upgradeStorage.getUpgradesFromNBT(nbt.getCompoundTag("upgrades"));
	}

	@Override
	public void writeCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);

		if(isDummy())
			return;
		nbt.setTag("gate", this.gate.writeToNBT());
		nbt.setTag("upgrades", this.upgradeStorage.saveUpgradesToNBT());
	}

	@Override
	public void receiveMessageFromServer(@Nonnull NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);

		if(!isDummy())
			this.gate.readFromNBT(message.getCompoundTag("gate"));
	}

	@Override
	protected void onUpdate()
	{
		gate.update();
		upgradeStorage.update();
	}

	@Override
	public List<AxisAlignedBB> getBounds(boolean collision)
	{
		if(isPOI("gate")&&master().gate.getProgress(0) > 0)
			return Collections.singletonList(new AxisAlignedBB(0, 0, 0, 0, 0, 0));
		return super.getBounds(collision);
	}

	@Override
	protected int[] listAllPOI(MultiblockPOI poi)
	{
		switch(poi)
		{
			case MISC_DOOR:
				return getPOI("gate");
			case REDSTONE_CABLE_MOUNT:
				return getPOI("redstone");
		}
		return new int[0];
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
	public int getSlotLimit(int slot)
	{
		return 0;
	}

	@Override
	public void doGraphicalUpdates(int slot)
	{

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
			IIPacketHandler.INSTANCE.sendToAllAround(new MessageBooleanAnimatedPartsSync(state, 0, getPos()), IIPacketHandler.targetPointFromPos(this.getPos(), this.world, 32));
		}
	}

	protected abstract SoundEvent getOpeningSound();

	protected abstract SoundEvent getClosingSound();

	public abstract IBlockState getFenceState(@Nullable EnumFacing facingConnected);

	//--- IUpgradeStorageMachine ---//


	@Override
	public UpgradeStorage<TileEntityGateBase<T>> getUpgradeStorage()
	{
		return upgradeStorage;
	}

	@Override
	public boolean upgradeMatches(MachineUpgrade upgrade)
	{
		return upgrade==IIContent.UPGRADE_REDSTONE_ACTIVATION||upgrade==IIContent.UPGRADE_RAZOR_WIRE;
	}

	@Override
	public <T extends TileEntity & IUpgradableMachine> T getUpgradeMaster()
	{
		return (T)master();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderWithUpgrades(MachineUpgrade... upgrades)
	{

	}

	//--- IPlayerInteraction ---//

	@Override
	public boolean interact(EnumFacing side, EntityPlayer player, EnumHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		T master = master();
		if(!IIItemUtil.isWrench(player.getHeldItem(hand))&&master!=null&&!master.hasUpgrade(IIContent.UPGRADE_REDSTONE_ACTIVATION))
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
		if(isPOI("razor")&&master().hasUpgrade(IIContent.UPGRADE_RAZOR_WIRE)&&!entity.isDead)
			entity.attackEntityFrom(IEDamageSources.razorWire, 3f);
		super.onEntityCollision(world, entity);
	}

	//--- IRedstoneConnector ---//


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
