package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.ISoundTile;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Vulcanizer;
import pl.pabilo8.immersiveintelligence.api.crafting.VulcanizerRecipe;
import pl.pabilo8.immersiveintelligence.client.fx.ParticleUtils;
import pl.pabilo8.immersiveintelligence.client.fx.nuke.ParticleShockwave;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockVulcanizer;

import javax.annotation.Nullable;
import java.util.Iterator;

/**
 * @author Pabilo8
 * @since 04.03.2021
 */
public class TileEntityVulcanizer extends TileEntityMultiblockMetal<TileEntityVulcanizer, VulcanizerRecipe> implements IPlayerInteraction, ISoundTile, IGuiTile
{
	public ItemStack mold = ItemStack.EMPTY;
	public NonNullList<ItemStack> inventory = NonNullList.withSize(3, ItemStack.EMPTY);
	public boolean recipePerformed = false;

	IItemHandler insertionHandlerRubber = new IEInventoryHandler(1, this, 0, true, false);
	IItemHandler insertionHandlerCompound = new IEInventoryHandler(1, this, 1, true, false);
	IItemHandler insertionHandlerSulfur = new IEInventoryHandler(1, this, 2, true, false);


	public TileEntityVulcanizer()
	{
		super(MultiblockVulcanizer.INSTANCE, MultiblockVulcanizer.INSTANCE.getSize(), Vulcanizer.energyCapacity, true);
	}

	@Override
	public void update()
	{
		//TODO: 14.04.2023 refactor
		super.update();

		if(isDummy()||isRSDisabled())
			return;
		processQueue.removeIf(p -> p.processTick >= p.maxTicks-1||p.clearProcess);

		if(world.isRemote)
		{
			/*TileEntityVulcanizer t1 = getTileForPos(70);
			TileEntityVulcanizer t2 = getTileForPos(55);
			TileEntityVulcanizer t3 = getTileForPos(49);
			TileEntityVulcanizer t4 = getTileForPos(25);
			//REFACTOR: 25.04.2023 sounds

			ImmersiveEngineering.proxy.handleTileSound(IISounds.heating, this, shoudlPlaySound("immersiveintelligence:vulcanizer_heating"), 1.5f, 1f);
			if(t1!=null)
				ImmersiveEngineering.proxy.handleTileSound(IISounds.rolling, t1, shoudlPlaySound("immersiveintelligence:printing_press"), 1.5f, 0.5f);
			if(t2!=null)
				ImmersiveEngineering.proxy.handleTileSound(IISounds.howitzerRotationH, t2, shoudlPlaySound("immersiveintelligence:howitzer_rotation_h"), 1.5f, 0.5f);
			if(t3!=null)
				ImmersiveEngineering.proxy.handleTileSound(IISounds.inserterBackward, t3, shoudlPlaySound("immersiveintelligence:inserter_backward"), 1.5f, 0.5f);
			if(t4!=null)
				ImmersiveEngineering.proxy.handleTileSound(IISounds.inserterForward, t4, shoudlPlaySound("immersiveintelligence:inserter_forward"), 1.5f, 0.5f);*/

			int max = getMaxProcessPerTick();
			int i = 0;
			Iterator<MultiblockProcess<VulcanizerRecipe>> processIterator = processQueue.iterator();
			tickedProcesses = 0;
			while(processIterator.hasNext()&&i++ < max)
			{
				MultiblockProcess<VulcanizerRecipe> process = processIterator.next();
				if(process.canProcess(this))
				{
					int energyExtracted = process.energyPerTick;
					int ticksAdded = 1;
					if(process.recipe.getMultipleProcessTicks() > 1)
					{
						//Average Insertion, tracked by the advanced flux storage
						int averageInsertion = this.energyStorage.getAverageInsertion();
						//Average Insertion musn'T be greater than possible extraction
						averageInsertion = this.energyStorage.extractEnergy(averageInsertion, true);
						if(averageInsertion > energyExtracted)
						{
							int possibleTicks = Math.min(averageInsertion/process.energyPerTick, Math.min(process.recipe.getMultipleProcessTicks(), process.maxTicks-process.processTick));
							if(possibleTicks > 1)
							{
								ticksAdded = possibleTicks;
								energyExtracted *= ticksAdded;
							}
						}
					}
					this.energyStorage.extractEnergy(energyExtracted, false);
					process.processTick += ticksAdded;

					if(process.processTick > Math.ceil(0.86*process.maxTicks)&&process.processTick < Math.ceil(0.885*process.maxTicks)&&world.getTotalWorldTime()%4==0)
						spawnParticles();

					tickedProcesses++;
				}
			}

			return;
		}

		for(MultiblockProcess<VulcanizerRecipe> process : processQueue)
		{
			//REFACTOR: 25.04.2023 sounds

			/*if(process.processTick==Math.ceil(0.16*process.maxTicks))
				world.playSound(null, getBlockPosForPos(70), IISounds.vulcanizerPullStart, SoundCategory.BLOCKS, .65F, 1.5f);
			if(process.processTick==Math.ceil(0.86*process.maxTicks))
				world.playSound(null, getBlockPosForPos(49), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, .65F, 0.75f);
			else if(process.processTick==Math.ceil(0.77*process.maxTicks))
				world.playSound(null, getBlockPosForPos(49), IISounds.vulcanizerPullEnd, SoundCategory.BLOCKS, 1, 1f);
			else if(process.processTick==Math.ceil(0.83*process.maxTicks))
				world.playSound(null, getBlockPosForPos(49), IISounds.vulcanizerPullStart, SoundCategory.BLOCKS, 1, 1f);
			else if(process.processTick==Math.ceil(0.835*process.maxTicks))
				world.playSound(null, getBlockPosForPos(73), IISounds.vulcanizerPullStart, SoundCategory.BLOCKS, 1, 0.5f);
			else if(process.processTick==Math.ceil(0.85*process.maxTicks))
				world.playSound(null, getBlockPosForPos(73), IISounds.vulcanizerPullEnd, SoundCategory.BLOCKS, 1, 0.5f);
			else if(process.processTick==Math.ceil(0.91*process.maxTicks))
				world.playSound(null, getBlockPosForPos(49), IISounds.vulcanizerPullEnd, SoundCategory.BLOCKS, 1, 1f);
			else if(process.processTick==Math.ceil(0.93*process.maxTicks))
				world.playSound(null, getBlockPosForPos(73), IISounds.vulcanizerPullStart, SoundCategory.BLOCKS, 1, 1f);
			else if(process.processTick==Math.ceil(0.95*process.maxTicks))
				world.playSound(null, getBlockPosForPos(73), IISounds.vulcanizerPullStart, SoundCategory.BLOCKS, 1, 0.5f);*/

			if(process.processTick==Math.floor(process.maxTicks*0.9)&&process.canProcess(this))
			{
				ItemStack output = process.recipe.output;
				//actual output
				int count = output.getCount();
				for(int i = 0; i < count; i++)
				{
					ItemStack stack = output.copy();
					stack.setCount(1);

					BlockPos pos = getBlockPosForPos(i).offset(getOutFacing());
					TileEntity inventoryTile = this.world.getTileEntity(pos);
					if(inventoryTile!=null)
						stack = Utils.insertStackIntoInventory(inventoryTile, stack, getOutFacing().getOpposite());
					if(!stack.isEmpty())
						Utils.dropStackAtPos(world, pos, stack, facing);
				}
			}

		}

		if(energyStorage.getEnergyStored() > 0&&processQueue.size() < this.getProcessQueueMaxLength())
		{
			if(processQueue.stream().noneMatch(proc -> (proc.processTick/(float)proc.maxTicks) < 0.84f))
			{
				VulcanizerRecipe recipe = VulcanizerRecipe.findRecipe(mold, inventory.get(0));
				if(recipe!=null&&
						(inventory.get(0).getCount() >= recipe.input.inputSize)&&
						(inventory.get(1).getCount() >= recipe.compoundInput.inputSize)&&
						(inventory.get(2).getCount() >= recipe.sulfurInput.inputSize)
				)
				{
					ItemStack rubber = inventory.get(0).copy();
					rubber.setCount(recipe.input.inputSize);
					ItemStack comp = inventory.get(1).copy();
					rubber.setCount(recipe.compoundInput.inputSize);
					ItemStack sulfur = inventory.get(2).copy();
					rubber.setCount(recipe.sulfurInput.inputSize);

					MultiblockProcessInWorld<VulcanizerRecipe> process = new MultiblockProcessInWorld<>(recipe, 0.84f, NonNullList.from(rubber, comp, sulfur));
					if(this.addProcessToQueue(process, true, false))
					{
						inventory.get(0).shrink(recipe.input.inputSize);
						inventory.get(1).shrink(recipe.compoundInput.inputSize);
						inventory.get(2).shrink(recipe.sulfurInput.inputSize);

						this.addProcessToQueue(process, false, false);
						this.markDirty();
						this.markContainingBlockForUpdate(null);

					}
				}
			}

		}
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		if(nbt.hasKey("mold"))
			mold = new ItemStack(nbt.getCompoundTag("mold"));
		if(!descPacket)
			inventory = Utils.readInventory(nbt.getTagList("inventory", 10), 3);
		recipePerformed = nbt.getBoolean("recipePerformed");
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);
		if(!this.mold.isEmpty())
			nbt.setTag("mold", this.mold.writeToNBT(new NBTTagCompound()));
		if(!descPacket)
			nbt.setTag("inventory", Utils.writeInventory(inventory));
		nbt.setBoolean("recipePerformed", recipePerformed);
	}

	@Override
	public boolean interact(EnumFacing side, EntityPlayer player, EnumHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		TileEntityVulcanizer master = master();
		if(master!=null&&master.processQueue.isEmpty())
		{
			if(player.isSneaking()&&!master.mold.isEmpty())
			{
				if(heldItem.isEmpty())
					player.setHeldItem(hand, master.mold.copy());
				else if(!world.isRemote)
					player.entityDropItem(master.mold.copy(), 0);
				master.mold = ItemStack.EMPTY;
				this.updateMasterBlock(null, true);
				return true;
			}
			else if(VulcanizerRecipe.isValidMold(heldItem))
			{
				ItemStack tempMold = !master.mold.isEmpty()?master.mold.copy(): ItemStack.EMPTY;
				master.mold = Utils.copyStackWithAmount(heldItem, 1);
				heldItem.shrink(1);
				if(heldItem.getCount() <= 0)
					heldItem = ItemStack.EMPTY;
				else
					player.setHeldItem(hand, heldItem);
				if(!tempMold.isEmpty())
					if(heldItem.isEmpty())
						player.setHeldItem(hand, tempMold);
					else if(!world.isRemote)
						player.entityDropItem(tempMold, 0);
				this.updateMasterBlock(null, true);
				return true;
			}
		}
		return false;
	}

	@Override
	public void receiveMessageFromServer(NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);
		if(message.hasKey("recipePerformed"))
			recipePerformed = message.getBoolean("recipePerformed");
	}

	@Override
	protected VulcanizerRecipe readRecipeFromNBT(NBTTagCompound tag)
	{
		return VulcanizerRecipe.loadFromNBT(tag);
	}

	@Override
	public int[] getEnergyPos()
	{
		return new int[]{22};
	}

	@Override
	public int[] getRedstonePos()
	{
		return new int[]{4};
	}

	@Override
	public IFluidTank[] getInternalTanks()
	{
		return null;
	}

	@Override
	public VulcanizerRecipe findRecipeForInsertion(ItemStack inserting)
	{
		return null;
	}

	@Override
	public int[] getOutputSlots()
	{
		return null;
	}

	@Override
	public int[] getOutputTanks()
	{
		return null;
	}

	@Override
	public boolean additionalCanProcessCheck(MultiblockProcess<VulcanizerRecipe> process)
	{
		return ApiUtils.createComparableItemStack(mold, true).equals(process.recipe.mold);
	}

	@Override
	public void doProcessOutput(ItemStack output)
	{
		recipePerformed = !recipePerformed;
	}

	@Override
	public void doProcessFluidOutput(FluidStack output)
	{

	}

	@Override
	public void onProcessFinish(MultiblockProcess<VulcanizerRecipe> process)
	{

	}

	@Override
	public int getMaxProcessPerTick()
	{
		return 2;
	}

	@Override
	public int getProcessQueueMaxLength()
	{
		return 2;
	}

	@Override
	public float getMinProcessDistance(MultiblockProcess<VulcanizerRecipe> process)
	{
		return 0.84f;
	}

	@Override
	public boolean isInWorldProcessingMachine()
	{
		return true;
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
		return new float[]{0, 0, 0, 1, 1, 1};
	}

	@Override
	public NonNullList<ItemStack> getInventory()
	{
		return inventory;
	}

	@Override
	public boolean isStackValid(int i, ItemStack itemStack)
	{
		switch(i)
		{
			case 0:
				return VulcanizerRecipe.recipeList.values().stream().anyMatch(vulcanizerRecipe -> vulcanizerRecipe.input.matchesItemStackIgnoringSize(itemStack));
			case 1:
				return VulcanizerRecipe.recipeList.values().stream().anyMatch(vulcanizerRecipe -> vulcanizerRecipe.compoundInput.matchesItemStackIgnoringSize(itemStack));
			case 2:
				return VulcanizerRecipe.recipeList.values().stream().anyMatch(vulcanizerRecipe -> vulcanizerRecipe.sulfurInput.matchesItemStackIgnoringSize(itemStack));
		}
		return false;
	}

	@Override
	public int getSlotLimit(int i)
	{
		return 64;
	}

	@Override
	public void doGraphicalUpdates(int i)
	{
		this.markDirty();
		this.markContainingBlockForUpdate(null);
	}

	private EnumFacing getOutFacing()
	{
		return this.facing.getOpposite();
	}

	private EnumFacing getInFacing()
	{
		return this.mirrored?this.facing.rotateYCCW(): this.facing.rotateY();
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			TileEntityVulcanizer master = master();
			if(master==null)
				return false;
			return (pos==53||pos==71||pos==95)&&facing==getInFacing();
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			TileEntityVulcanizer master = master();
			if(master==null)
				return null;
			switch(pos)
			{
				case 53:
					return (T)master.insertionHandlerSulfur;
				case 71:
					return (T)master.insertionHandlerCompound;
				case 95:
					return (T)master.insertionHandlerRubber;
			}

			return null;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean shoudlPlaySound(String sound)
	{

		//REFACTOR: 25.04.2023 sounds

		/*TileEntityVulcanizer master = master();
		if(master!=null&&master.processQueue.size() > 0)
		{
			MultiblockProcess<VulcanizerRecipe> process = master.processQueue.get(0);
			switch(sound)
			{
				case "immersiveintelligence:printing_press":
				{
					if(master.processQueue.size() > 1)
					{
						if(IIUtils.inRange(master.processQueue.get(1).processTick, master.processQueue.get(1).maxTicks, 0, 0.16))
							return true;
					}
					return IIUtils.inRange(process.processTick, process.maxTicks, 0, 0.165);
				}
				case "immersiveintelligence:vulcanizer_heating":
					return IIUtils.inRange(process.processTick, process.maxTicks, 0.2, 0.8);
				case "immersiveintelligence:howitzer_rotation_h":
					return IIUtils.inRange(process.processTick, process.maxTicks, 0.78, 0.84);
				case "immersiveintelligence:inserter_forward":
					return IIUtils.inRange(process.processTick, process.maxTicks, 0.93, 0.96);
				case "immersiveintelligence:inserter_backward":
					return IIUtils.inRange(process.processTick, process.maxTicks, 0.85, 0.86);
			}
		}*/

		return false;
	}

	@SideOnly(Side.CLIENT)
	private void spawnParticles()
	{
		Vec3d facing = new Vec3d(getFacing().getOpposite().getDirectionVec()).scale(0.25f);
		facing = facing.scale(0.65f);

		for(int p = 48; p <= 50; p++)
			for(int i = 0; i < 6; i++)
			{
				Vec3d bpos = new Vec3d(getBlockPosForPos(p)).addVector(0.5, -0.5, 0.5).add(facing);
				float mod = Utils.RAND.nextFloat();

				ParticleShockwave particle = new ParticleShockwave(ClientUtils.mc().world,
						bpos.addVector(0, 0.125*i, 0),
						facing.scale(mod).addVector(0, -mod*0.1, 0),
						i+mod);
				ParticleUtils.particleRenderer.addEffect(particle);
			}
	}

	@Override
	public boolean canOpenGui()
	{
		return formed;
	}

	@Override
	public int getGuiID()
	{
		return IIGuiList.GUI_VULCANIZER.ordinal();
	}

	@Nullable
	@Override
	public TileEntity getGuiMaster()
	{
		return master();
	}

	@Override
	public NonNullList<ItemStack> getDroppedItems()
	{
		return NonNullList.from(inventory.get(0), inventory.get(1), inventory.get(2), mold);
	}
}
