package pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.network.MessageTileSync;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.Radar;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.utils.IAdvancedMultiblockTileEntity;
import pl.pabilo8.immersiveintelligence.api.utils.MachineUpgrade;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IUpgradableMachine;
import pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.RadarRenderer;
import pl.pabilo8.immersiveintelligence.common.IIContent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8
 * @since 04.03.2021
 */
public class TileEntityRadar extends TileEntityMultiblockMetal<TileEntityRadar, MultiblockRecipe> implements IAdvancedMultiblockTileEntity, IUpgradableMachine
{
	public static int PART_AMOUNT = 1;
	public int construction = 0, clientConstruction = 0;

	protected ArrayList<MachineUpgrade> upgrades = new ArrayList<>();
	MachineUpgrade currentlyInstalled = null;
	int upgradeProgress = 0;
	public int clientUpgradeProgress = 0;

	public int dishRotation = 0;
	public boolean active = false;

	public TileEntityRadar()
	{
		super(MultiblockRadar.instance, MultiblockRadar.instance.getSize(), Radar.energyCapacity, true);
	}

	@Override
	public void update()
	{
		super.update();

		active = (redstoneControlInverted^world.isBlockPowered(getBlockPosForPos(getRedstonePos()[0])))&&energyStorage.extractEnergy(Radar.energyUsage, false)==Radar.energyUsage;
		if(active)
			dishRotation++;

		if(world.isRemote)
		{
			float maxConstruction = Utils.getMaxClientProgress(construction, getConstructionCost(), PART_AMOUNT);
			if(clientConstruction < maxConstruction)
				clientConstruction = (int)Math.min(clientConstruction+(Tools.electric_hammer_energy_per_use_construction/4.25f), maxConstruction);
			if(clientUpgradeProgress < getMaxClientProgress())
				clientUpgradeProgress = (int)Math.min(clientUpgradeProgress+(Tools.wrench_upgrade_progress/2f), getMaxClientProgress());
		}
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		getConstructionNBT(nbt);
		clientConstruction = construction;
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);
		setConstructionNBT(nbt);
	}

	@Override
	public void receiveMessageFromServer(NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);
	}

	@Override
	protected MultiblockRecipe readRecipeFromNBT(NBTTagCompound tag)
	{
		return null;
	}

	@Override
	public int[] getEnergyPos()
	{
		return new int[]{83};
	}

	@Override
	public int[] getRedstonePos()
	{
		return new int[]{71};
	}

	@Override
	public IFluidTank[] getInternalTanks()
	{
		return new IFluidTank[0];
	}

	@Override
	public MultiblockRecipe findRecipeForInsertion(ItemStack inserting)
	{
		return null;
	}

	@Override
	public int[] getOutputSlots()
	{
		return new int[0];
	}

	@Override
	public int[] getOutputTanks()
	{
		return new int[0];
	}

	@Override
	public boolean additionalCanProcessCheck(MultiblockProcess<MultiblockRecipe> process)
	{
		return false;
	}

	@Override
	public void doProcessOutput(ItemStack output)
	{

	}

	@Override
	public void doProcessFluidOutput(FluidStack output)
	{

	}

	@Override
	public void onProcessFinish(MultiblockProcess<MultiblockRecipe> process)
	{

	}

	@Override
	public int getMaxProcessPerTick()
	{
		return 0;
	}

	@Override
	public int getProcessQueueMaxLength()
	{
		return 0;
	}

	@Override
	public float getMinProcessDistance(MultiblockProcess<MultiblockRecipe> process)
	{
		return 0;
	}

	@Override
	public boolean isInWorldProcessingMachine()
	{
		return false;
	}

	@Override
	protected IFluidTank[] getAccessibleFluidTanks(EnumFacing enumFacing)
	{
		return new IFluidTank[0];
	}

	@Override
	protected boolean canFillTankFrom(int i, EnumFacing enumFacing, FluidStack fluidStack)
	{
		return false;
	}

	@Override
	protected boolean canDrainTankFrom(int i, EnumFacing enumFacing)
	{
		return false;
	}

	@Override
	public float[] getBlockBounds()
	{
		return new float[0];
	}

	@Override
	public NonNullList<ItemStack> getInventory()
	{
		return NonNullList.create();
	}

	@Override
	public boolean isStackValid(int i, ItemStack itemStack)
	{
		return false;
	}

	@Override
	public int getSlotLimit(int i)
	{
		return 0;
	}

	@Override
	public void doGraphicalUpdates(int i)
	{

	}

	public void sendUpdate(int id)
	{
		NBTTagCompound tag = new NBTTagCompound();
		if(id==0)
		{
			//tag.setTag("flag", flag.serializeNBT());
		}
		if(!tag.hasNoTags())
			ImmersiveEngineering.packetHandler.sendToAllAround(new MessageTileSync(this, tag), pl.pabilo8.immersiveintelligence.api.Utils.targetPointFromTile(this, 32));
	}

	@Override
	public int getConstructionCost()
	{
		return Radar.constructionEnergy;
	}

	@Override
	public int getCurrentConstruction()
	{
		TileEntityRadar master = master();
		return master==null?0: master.construction;
	}

	@Override
	public void setCurrentConstruction(int construction)
	{
		TileEntityRadar master = master();
		if(master!=null)
			master.construction = construction;
	}

	@Override
	public void onConstructionFinish()
	{

	}

	@Override
	public boolean addUpgrade(MachineUpgrade upgrade, boolean test)
	{
		boolean b = !hasUpgrade(upgrade)&&upgrade.equals(IIContent.UPGRADE_RADIO_LOCATORS);
		if(!test&&b)
			upgrades.add(upgrade);
		return b;
	}

	@Override
	public boolean hasUpgrade(MachineUpgrade upgrade)
	{
		return upgrades.stream().anyMatch(machineUpgrade -> machineUpgrade.getName().equals(upgrade.getName()));
	}

	@Override
	public boolean upgradeMatches(MachineUpgrade upgrade)
	{
		return upgrade==IIContent.UPGRADE_RADIO_LOCATORS;
	}

	@Override
	public TileEntityRadar getUpgradeMaster()
	{
		return master();
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

	@Override
	@SideOnly(Side.CLIENT)
	public void renderWithUpgrades(MachineUpgrade... upgrades)
	{
		RadarRenderer.renderWithUpgrades(upgrades);
	}

	@Override
	public List<MachineUpgrade> getUpgrades()
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
}
