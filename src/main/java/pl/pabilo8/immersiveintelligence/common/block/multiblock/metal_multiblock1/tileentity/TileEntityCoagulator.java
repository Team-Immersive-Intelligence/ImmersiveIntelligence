package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.ISoundTile;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Coagulator;
import pl.pabilo8.immersiveintelligence.api.crafting.CoagulatorRecipe;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockCoagulator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 04.03.2021
 */
public class TileEntityCoagulator extends TileEntityMultiblockMetal<TileEntityCoagulator, CoagulatorRecipe> implements ISoundTile, IGuiTile
{
	public FluidTank[] tanks = new FluidTank[]{
			new FluidTank(Coagulator.fluidCapacity),
			new FluidTank(Coagulator.fluidCapacity)
	};
	//This stores the "crafting" effect
	public NonNullList<ItemStack> effect = NonNullList.withSize(1, ItemStack.EMPTY);
	public int[] bucketProgress = new int[]{0, 0, 0, 0, 0, 0};
	public NonNullList<ItemStack> bucketStacks = NonNullList.withSize(6, ItemStack.EMPTY);

	public int cranePosition = 0, craneBucket = -1, craneProgress = 0;
	public CraneAnimation craneAnimation = CraneAnimation.NONE;

	public TileEntityCoagulator()
	{
		super(MultiblockCoagulator.INSTANCE, MultiblockCoagulator.INSTANCE.getSize(), Coagulator.energyCapacity, true);
	}

	@Override
	public void update()
	{
		super.update();

		if(isDummy()||isRSDisabled())
			return;

		if(craneBucket!=-1)
		{
			if(craneProgress > 0)
				craneProgress--;
			else
			{
				//set new animation
				switch(craneAnimation)
				{
					case MOVE_BUCKET:
					case MOVE_BACK:
					{
						cranePosition += Integer.compare(craneBucket, cranePosition);
						if(cranePosition==craneBucket)
							craneAnimation = IIUtils.cycleEnum(true, CraneAnimation.class, craneAnimation);
					}
					break;
					case MOVE_MIXER:
					{
						cranePosition += Integer.compare(2, cranePosition);
						if(cranePosition==2)
							craneAnimation = IIUtils.cycleEnum(true, CraneAnimation.class, craneAnimation);
					}
					break;

					default:
					case NONE:
					case PICK:
					case PUT:
					case PULL:
					case ROTATE_IN:
					case ROTATE_OUT:
					case REACH:
					case PLACE:
						craneAnimation = IIUtils.cycleEnum(true, CraneAnimation.class, craneAnimation);
						break;
					case RETURN:
					{
						//Eto Konets )))
						if(!world.isRemote)
						{
							bucketProgress[craneBucket] = CoagulatorRecipe.getBucketProgressForStack(this.effect.get(0));
							bucketStacks.set(craneBucket, this.effect.get(0).copy());
							bucketStacks.get(craneBucket).setCount(1);
							this.effect.get(0).shrink(1);

							markDirty();
							markContainingBlockForUpdate(null);
						}

						this.craneBucket = -1;
						this.craneProgress = 0;
						this.craneAnimation=CraneAnimation.NONE;
					}
					break;
				}
				//set time after change
				switch(craneAnimation)
				{
					default:
					case NONE:
						break;
					case MOVE_BUCKET:
					case MOVE_BACK:
						craneProgress = (cranePosition==craneBucket)?0: Coagulator.craneMoveTime;
						break;
					case MOVE_MIXER:
						craneProgress = (cranePosition==2)?0: Coagulator.craneMoveTime;
						break;
					case ROTATE_IN:
					case ROTATE_OUT:
						craneProgress = Coagulator.craneMoveTime;
						break;
					case PICK:
					case PUT:
					case PULL:
					case PLACE:
					case REACH:
					case RETURN:
						craneProgress = Coagulator.craneGrabTime;
						break;
				}

			}
		}

		if(world.isRemote)
		{
			for(int i = 0; i < bucketProgress.length; i++)
				bucketProgress[i] = Math.max(0, bucketProgress[i]-1);
			return;
		}

		if(processQueue.isEmpty()&&tanks[0].getFluidAmount() > 0&&tanks[1].getFluidAmount() > 0)
		{
			CoagulatorRecipe recipe = CoagulatorRecipe.findRecipe(tanks[0].getFluid(), tanks[1].getFluid());
			if(recipe!=null&&(this.effect.get(0).isEmpty()||OreDictionary.itemMatches(this.effect.get(0), recipe.itemOutput, false)))
			{
				MultiblockProcessInMachine<CoagulatorRecipe> process = new MultiblockProcessInMachine<>(recipe);
				process.setInputTanks(0, 1);
				this.addProcessToQueue(process, false);
				this.markDirty();
				this.markContainingBlockForUpdate(null);
			}
		}

		for(int i = 0; i < bucketStacks.size(); i++)
		{
			if(!bucketStacks.get(i).isEmpty())
			{
				if(--bucketProgress[i] <= 0)
				{
					bucketProgress[i] = 0;
					ItemStack copy = bucketStacks.get(i);
					bucketStacks.set(i, ItemStack.EMPTY);
					Utils.dropStackAtPos(world, getBlockPosForPos(28+i).offset(facing), copy, null);
					this.markDirty();
					this.markContainingBlockForUpdate(null);
				}
			}
		}

		if(!effect.get(0).isEmpty()&&craneAnimation==CraneAnimation.NONE&&craneBucket==-1)
		{
			for(int i = 0; i < bucketStacks.size(); i++)
				if(bucketStacks.get(i).isEmpty())
				{
					craneBucket = i;
					craneProgress = 0;

					markDirty();
					markContainingBlockForUpdate(null);
					break;
				}
		}
	}

	@Override
	public void readCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);

		if(isDummy())
			return;

		tanks[0] = tanks[0].readFromNBT(nbt.getCompoundTag("tank0"));
		tanks[1] = tanks[1].readFromNBT(nbt.getCompoundTag("tank1"));


		effect = Utils.readInventory(nbt.getTagList("effect", 10), 1);
		bucketStacks = Utils.readInventory(nbt.getTagList("bucketStacks", 10), 6);

		bucketProgress = nbt.getIntArray("bucketProgress");
		if(bucketProgress.length!=6)
			bucketProgress = new int[]{0, 0, 0, 0, 0, 0};

		cranePosition = nbt.getInteger("cranePosition");
		craneBucket = nbt.getInteger("craneBucket");
		craneProgress = nbt.getInteger("craneProgress");
		craneAnimation = CraneAnimation.values()[MathHelper.clamp(nbt.getInteger("craneAnimation"), 0, CraneAnimation.values().length)];

	}

	@Override
	public void writeCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);

		if(isDummy())
			return;

		nbt.setTag("tank0", tanks[0].writeToNBT(new NBTTagCompound()));
		nbt.setTag("tank1", tanks[1].writeToNBT(new NBTTagCompound()));

		nbt.setTag("effect", Utils.writeInventory(effect));
		nbt.setTag("bucketStacks", Utils.writeInventory(bucketStacks));
		nbt.setIntArray("bucketProgress", bucketProgress);

		nbt.setInteger("cranePosition", cranePosition);
		nbt.setInteger("craneBucket", craneBucket);
		nbt.setInteger("craneProgress", craneProgress);
		nbt.setInteger("craneAnimation", craneAnimation.ordinal());
	}

	@Override
	public void receiveMessageFromServer(@Nonnull NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);

		if(isDummy())
			return;

		if(message.hasKey("tank0"))
			tanks[0] = tanks[0].readFromNBT(message.getCompoundTag("tank0"));
		if(message.hasKey("tank1"))
			tanks[1] = tanks[1].readFromNBT(message.getCompoundTag("tank1"));
		if(message.hasKey("effect"))
			effect = Utils.readInventory(message.getTagList("effect", 10), 1);
		if(message.hasKey("bucketStacks"))
			bucketStacks = Utils.readInventory(message.getTagList("bucketStacks", 10), 6);
		if(message.hasKey("bucketProgress"))
		{
			bucketProgress = message.getIntArray("bucketProgress");
			if(bucketProgress.length!=6)
				bucketProgress = new int[]{0, 0, 0, 0, 0, 0};
		}
		if(message.hasKey("craneAnimation"))
		{
			cranePosition = message.getInteger("cranePosition");
			craneBucket = message.getInteger("craneBucket");
			craneProgress = message.getInteger("craneProgress");
			craneAnimation = CraneAnimation.values()[MathHelper.clamp(message.getInteger("craneAnimation"), 0, CraneAnimation.values().length)];
		}
	}

	// TODO: 31.10.2021 recipe
	@Nonnull
	@Override
	protected CoagulatorRecipe readRecipeFromNBT(@Nonnull NBTTagCompound tag)
	{
		return CoagulatorRecipe.loadFromNBT(tag);
	}

	@Nonnull
	@Override
	public int[] getEnergyPos()
	{
		return new int[]{97};
	}

	@Nonnull
	@Override
	public int[] getRedstonePos()
	{
		return new int[]{77};
	}

	@Nonnull
	@Override
	public IFluidTank[] getInternalTanks()
	{
		return tanks;
	}

	@Nonnull
	@SuppressWarnings("MethodsReturnNonNullByDefault")
	@Override
	public CoagulatorRecipe findRecipeForInsertion(@Nonnull ItemStack inserting)
	{
		return null;
	}

	@Nonnull
	@Override
	public int[] getOutputSlots()
	{
		return new int[]{0};
	}

	@Nonnull
	@Override
	public int[] getOutputTanks()
	{
		return new int[0];
	}

	// TODO: 31.10.2021 investigate
	@Override
	public boolean additionalCanProcessCheck(@Nonnull MultiblockProcess<CoagulatorRecipe> process)
	{
		return true;
	}

	@Override
	public void doProcessOutput(@Nonnull ItemStack output)
	{

	}

	@Override
	public void doProcessFluidOutput(@Nonnull FluidStack output)
	{

	}

	@Override
	public void onProcessFinish(@Nonnull MultiblockProcess<CoagulatorRecipe> process)
	{

	}

	@Override
	public int getMaxProcessPerTick()
	{
		return 1;
	}

	@Override
	public int getProcessQueueMaxLength()
	{
		return 1;
	}

	@Override
	public float getMinProcessDistance(@Nonnull MultiblockProcess<CoagulatorRecipe> process)
	{
		return 0.84f;
	}

	@Override
	public boolean isInWorldProcessingMachine()
	{
		return true;
	}

	@Nonnull
	@Override
	protected IFluidTank[] getAccessibleFluidTanks(@Nonnull EnumFacing enumFacing)
	{
		if(pos==146||pos==140)
		{
			TileEntityCoagulator master = master();
			if(master!=null)
			{
				return new IFluidTank[]{pos==146?master.tanks[0]: master.tanks[1]};
			}
		}

		return new IFluidTank[0];
	}

	@Override
	protected boolean canFillTankFrom(int i, @Nonnull EnumFacing enumFacing, @Nonnull FluidStack fluidStack)
	{
		return true;
	}

	@Override
	protected boolean canDrainTankFrom(int i, @Nonnull EnumFacing enumFacing)
	{
		return false;
	}

	@Nonnull
	@Override
	public float[] getBlockBounds()
	{
		return new float[]{0, 0, 0, 1, 1, 1};
	}

	@Override
	public NonNullList<ItemStack> getInventory()
	{
		return effect;
	}

	@Override
	public boolean isStackValid(int i, ItemStack itemStack)
	{
		return true;
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

	@Override
	public boolean shoudlPlaySound(@Nonnull String sound)
	{
		TileEntityCoagulator master = master();
		// TODO: 31.10.2021 sounds
		/*
		if(master!=null&&master.processQueue.size() > 0)
		{
			MultiblockProcess<VulcanizerRecipe> process = master.processQueue.get(0);
			switch(sound)
			{
				case "immersiveintelligence:printing_press":
				{
					if(master.processQueue.size() > 1)
					{
						if(pl.pabilo8.immersiveintelligence.common.Utils.inRange(master.processQueue.get(1).processTick, master.processQueue.get(1).maxTicks, 0, 0.16))
							return true;
					}
					return pl.pabilo8.immersiveintelligence.common.Utils.inRange(process.processTick, process.maxTicks, 0, 0.165);
				}
				case "immersiveintelligence:vulcanizer_heating":
					return pl.pabilo8.immersiveintelligence.common.Utils.inRange(process.processTick, process.maxTicks, 0.2, 0.8);
				case "immersiveintelligence:howitzer_rotation_h":
					return pl.pabilo8.immersiveintelligence.common.Utils.inRange(process.processTick, process.maxTicks, 0.78, 0.84);
				case "immersiveintelligence:inserter_forward":
					return pl.pabilo8.immersiveintelligence.common.Utils.inRange(process.processTick, process.maxTicks, 0.93, 0.96);
				case "immersiveintelligence:inserter_backward":
					return pl.pabilo8.immersiveintelligence.common.Utils.inRange(process.processTick, process.maxTicks, 0.85, 0.86);
			}
		}
		 */

		return false;
	}

	@Override
	public boolean canOpenGui()
	{
		return false;//formed;
	}

	// TODO: 31.10.2021 gui
	@Override
	public int getGuiID()
	{
		return IIGuiList.GUI_COAGULATOR.ordinal();
	}

	@Nullable
	@Override
	public TileEntity getGuiMaster()
	{
		return master();
	}

	public enum CraneAnimation
	{
		NONE,
		MOVE_BUCKET,
		REACH,
		PICK,
		MOVE_MIXER,
		ROTATE_IN,
		PUT,
		PULL,
		ROTATE_OUT,
		MOVE_BACK,
		PLACE,
		RETURN
	}
}
