package pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.tileentity;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.utils.IBooleanAnimatedPartsBlock;
import pl.pabilo8.immersiveintelligence.api.utils.MachineUpgrade;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IUpgradableMachine;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBooleanAnimatedPartsSync;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Pabilo8
 * @updated 06.12.2023
 * @since 28-06-2019
 */
public abstract class TileEntityGateBase<T extends TileEntityGateBase<T>> extends TileEntityMultiblockIIBase<T> implements IBooleanAnimatedPartsBlock, IPlayerInteraction, IUpgradableMachine
{
	public static final int MAX_OPEN_PROGRESS = 40;

	public boolean open = false;
	public int openProgress = 0;

	protected ArrayList<MachineUpgrade> upgrades = new ArrayList<>();
	MachineUpgrade currentlyInstalled = null;
	int upgradeProgress = 0;
	public int clientUpgradeProgress = 0;

	public TileEntityGateBase(MultiblockStuctureBase<T> multiblock)
	{
		super(multiblock);
	}

	@Override
	public void readCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);

		open = nbt.getBoolean("open");
	}

	@Override
	public void writeCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);

		nbt.setBoolean("open", this.open);
		nbt.setInteger("gateAngle", openProgress);
	}

	@Override
	public void receiveMessageFromServer(@Nonnull NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);

		this.open = message.getBoolean("open");
		this.openProgress = message.getInteger("gateAngle");
	}

	@Override
	protected void onUpdate()
	{
		openProgress = MathHelper.clamp(openProgress+(open?1: -2), 0, MAX_OPEN_PROGRESS);

		if(world.isRemote&&clientUpgradeProgress < getMaxClientProgress())
			clientUpgradeProgress = (int)Math.min(clientUpgradeProgress+(Tools.wrenchUpgradeProgress/2f), getMaxClientProgress());
	}

	@Override
	public List<AxisAlignedBB> getBounds(boolean collision)
	{
		if(isPOI("gate")&&master().open)
			return Collections.singletonList(new AxisAlignedBB(0, 0, 0, 0, 0, 0));
		return super.getBounds(collision);
	}

	@Override
	protected int[] listAllPOI(MultiblockPOI poi)
	{
		switch(poi)
		{
			case REDSTONE_INPUT:
				return getPOI("redstone");
			case MISC_DOOR:
				return getPOI("gate");
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
		open = state;
	}

	@Override
	public void onAnimationChangeServer(boolean state, int part)
	{
		if(state!=open)
		{
			open = state;
			IIPacketHandler.INSTANCE.sendToAllAround(new MessageBooleanAnimatedPartsSync(open, 0, getPos()), IIPacketHandler.targetPointFromPos(this.getPos(), this.world, 32));
		}
	}

	public abstract IBlockState getFenceState(@Nullable EnumFacing facingConnected);

	//TODO: 03.12.2023 rework the upgrade system
	@Override
	public boolean addUpgrade(MachineUpgrade upgrade, boolean test)
	{
		if(!test&&!hasUpgrade(upgrade))
		{
			upgrades.add(upgrade);
			return true;
		}
		return false;
	}

	@Override
	public boolean hasUpgrade(MachineUpgrade upgrade)
	{
		return upgrades.stream().anyMatch(machineUpgrade -> machineUpgrade.getName().equals(upgrade.getName()));
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

	@Override
	public void saveUpgradesToNBT(NBTTagCompound tag)
	{
		for(MachineUpgrade upgrade : upgrades)
			tag.setBoolean(upgrade.getName(), true);
	}

	@Override
	public void getUpgradesFromNBT(NBTTagCompound tag)
	{
		upgrades.clear();
		upgrades.addAll(MachineUpgrade.getUpgradesFromNBT(tag));
	}

	public ArrayList<MachineUpgrade> getUpgrades()
	{
		return upgrades;
	}

	@Nullable
	@Override
	public MachineUpgrade getCurrentlyInstalled()
	{
		return currentlyInstalled;
	}

	@Override
	public int getInstallProgress()
	{
		return upgradeProgress;
	}

	@Override
	public int getClientInstallProgress()
	{
		return clientUpgradeProgress;
	}

	@Override
	public boolean addUpgradeInstallProgress(int toAdd)
	{
		upgradeProgress += toAdd;
		return true;
	}

	@Override
	public boolean resetInstallProgress()
	{
		currentlyInstalled = null;
		if(upgradeProgress > 0)
		{
			upgradeProgress = 0;
			clientUpgradeProgress = 0;
			return true;
		}
		return false;
	}

	@Override
	public void startUpgrade(@Nonnull MachineUpgrade upgrade)
	{
		currentlyInstalled = upgrade;
		upgradeProgress = 0;
		clientUpgradeProgress = 0;
	}

	@Override
	public void removeUpgrade(MachineUpgrade upgrade)
	{
		upgrades.remove(upgrade);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderWithUpgrades(MachineUpgrade... upgrades)
	{

	}

	@Override
	public boolean interact(EnumFacing side, EntityPlayer player, EnumHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		T master = master();
		if(!IIUtils.isWrench(player.getHeldItem(hand))&&master!=null&&!master.hasUpgrade(IIContent.UPGRADE_REDSTONE_ACTIVATION))
		{
			master.open = !master.open;
			return true;
		}
		return false;
	}
}
