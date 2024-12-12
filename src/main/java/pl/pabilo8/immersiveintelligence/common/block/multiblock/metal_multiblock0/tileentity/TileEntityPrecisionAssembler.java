package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.ISoundTile;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.apache.commons.lang3.ArrayUtils;
import pl.pabilo8.immersiveintelligence.api.crafting.PrecisionAssemblerRecipe;
import pl.pabilo8.immersiveintelligence.api.utils.IBooleanAnimatedPartsBlock;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IPrecisionTool;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.PrecisionAssembler;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockPrecisionAssembler;
import pl.pabilo8.immersiveintelligence.common.item.crafting.ItemIIAssemblyScheme;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBooleanAnimatedPartsSync;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nullable;
import java.util.ArrayList;

/**
 * @author Pabilo8
 * @since 28-06-2019
 */
public class TileEntityPrecisionAssembler extends TileEntityMultiblockMetal<TileEntityPrecisionAssembler, PrecisionAssemblerRecipe> implements IGuiTile, ISoundTile, IBooleanAnimatedPartsBlock
{
	//3 x tool slots, 1x scheme slot, 1x main component slot, 3 x secondary component slots, 1 x output slot, 1 x trash output slot
	//0 1 2, 3, 4, 5 6 7, 8, 9
	public NonNullList<ItemStack> inventory = NonNullList.withSize(10, ItemStack.EMPTY);
	public int processTime, processTimeMax;
	public String[] animationOrder = new String[]{};
	public String[] toolOrder = new String[]{};
	public ArrayList<Tuple<Integer, String>> animationPrepared = new ArrayList<>();
	public boolean stack1Visible, stack2Visible, stack3Visible;
	public ItemStack stackPicked1, stackPicked2, stackPicked3;
	public ItemStack effect;
	private boolean update = false;
	public boolean active = false;

	public boolean[] isDrawerOpened = {false, false};
	public float[] drawerAngle = {0, 0};

	IItemHandler insertionHandler = new IEInventoryHandler(5, this, 4, true, false);

	public TileEntityPrecisionAssembler()
	{
		super(MultiblockPrecisionAssembler.INSTANCE, new int[]{3, 3, 5}, PrecisionAssembler.energyCapacity, true);
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		if(!descPacket)
			inventory = Utils.readInventory(nbt.getTagList("inventory", 10), 10);
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);

		if(!descPacket)
			nbt.setTag("inventory", Utils.writeInventory(inventory));
	}

	@Override
	public void receiveMessageFromServer(NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);
		if(message.hasKey("processTime"))
			this.processTime = message.getInteger("processTime");
		if(message.hasKey("processTimeMax"))
			this.processTimeMax = message.getInteger("processTimeMax");
		if(message.hasKey("active"))
			this.active = message.getBoolean("active");
		if(message.hasKey("inventory"))
			inventory = Utils.readInventory(message.getTagList("inventory", 10), 10);
		if(message.hasKey("output"))
			effect = new ItemStack(message.getCompoundTag("output"));
		if(message.hasKey("toolOrder"))
		{
			NBTTagList list = message.getTagList("toolOrder", NBT.TAG_STRING);
			toolOrder = new String[]{};
			ArrayList<String> l2 = new ArrayList<>();
			for(int i = 0; i < list.tagCount(); i += 1)
			{
				NBTBase base = list.get(i);
				NBTTagString string = (NBTTagString)base;
				l2.add(string.getString());
			}
			toolOrder = new String[l2.size()];
			for(int i = 0; i < l2.size(); i += 1)
			{
				toolOrder[i] = l2.get(i);
			}
		}
		if(message.hasKey("animationOrder"))
		{
			NBTTagList list = message.getTagList("animationOrder", NBT.TAG_STRING);
			float modifier = 1f;
			if(message.hasKey("animationTimeMod"))
				modifier = message.getFloat("animationTimeMod");

			animationOrder = new String[]{};
			animationPrepared.clear();
			for(int i = 0; i < list.tagCount(); i += 1)
			{
				NBTBase base = list.get(i);
				NBTTagString string = (NBTTagString)base;
				ArrayUtils.add(animationOrder, string.getString());
				int duration = (int)(PrecisionAssemblerRecipe.toolMap.get(string.getString().split(" ")[0]).getWorkTime(string.getString().split(" ")[0])*modifier);
				Tuple<Integer, String> tuple = new Tuple<>(duration, string.getString());
				animationPrepared.add(tuple);


			}
		}

		if(message.hasKey("beginning"))
		{
			stackPicked1 = ItemStack.EMPTY;
			stackPicked2 = ItemStack.EMPTY;
			stackPicked3 = ItemStack.EMPTY;

			stack1Visible = true;
			stack2Visible = true;
			stack3Visible = true;
		}
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void update()
	{
		super.update();
		if(isDummy())
			return;

		if(world.isRemote)
		{
			for(int i = 0; i < 2; i++)
				drawerAngle[i] = MathHelper.clamp(drawerAngle[i]+(isDrawerOpened[i]?0.4f: -0.5f), 0f, 5f);

			if(active&&processTime < processTimeMax)
				processTime += 1;
			return;
		}

		boolean wasActive = active;

		boolean beginning = false;

		if(energyStorage.getEnergyStored() > 0&&processQueue.size() < this.getProcessQueueMaxLength())
		{
			//Lots of stuffs happening(s)
			PrecisionAssemblerRecipe recipe = PrecisionAssemblerRecipe.findRecipe(new ItemStack[]{inventory.get(4), inventory.get(5), inventory.get(6), inventory.get(7)}, inventory.get(3), new ItemStack[]{inventory.get(0), inventory.get(1), inventory.get(2)});
			if(recipe!=null)
			{
				MultiblockProcessInMachine<PrecisionAssemblerRecipe> process = new MultiblockProcessInMachine<>(recipe, 4, 5, 6, 7);
				this.addProcessToQueue(process, false);
				update = true;
				processTime = 0;
				processTimeMax = recipe.getTotalProcessTime();

				String t0 = "", t1 = "", t2 = "";
				if(!inventory.get(0).isEmpty()&&inventory.get(0).getItem() instanceof IPrecisionTool)
				{
					t0 = ((IPrecisionTool)inventory.get(0).getItem()).getPrecisionToolType(inventory.get(0));
				}
				if(!inventory.get(1).isEmpty()&&inventory.get(1).getItem() instanceof IPrecisionTool)
				{
					t1 = ((IPrecisionTool)inventory.get(1).getItem()).getPrecisionToolType(inventory.get(1));
				}
				if(!inventory.get(2).isEmpty()&&inventory.get(2).getItem() instanceof IPrecisionTool)
				{
					t2 = ((IPrecisionTool)inventory.get(2).getItem()).getPrecisionToolType(inventory.get(2));
				}

				toolOrder = new String[]{t0, t1, t2};
				animationOrder = recipe.animations;
				effect = recipe.output;
				float timeMod = recipe.timeModifier;
				beginning = true;
			}
		}

		if(world.getTotalWorldTime()%20==0)
		{
			EnumFacing ff = mirrored?facing.rotateY(): facing.rotateYCCW();
			BlockPos pos = getBlockPosForPos(5).offset(ff, 1);
			ItemStack output = inventory.get(8);
			TileEntity inventoryTile = this.world.getTileEntity(pos);
			if(inventoryTile!=null)
				output = Utils.insertStackIntoInventory(inventoryTile, output, ff.getOpposite());
			inventory.set(8, output);

			output = inventory.get(9);
			if(inventoryTile!=null)
				output = Utils.insertStackIntoInventory(inventoryTile, output, facing.rotateY());
			inventory.set(9, output);
		}

		active = shouldRenderAsActive();

		if(wasActive!=active)
			update = true;

		if(update)
		{
			if(effect==null)
			{
				this.processQueue.clear();
				return;
			}

			// TODO: 23.09.2022 rework needed
			IIPacketHandler.sendToClient(this, new MessageIITileSync(this, EasyNBT.newNBT()
					.withInt("processTime", processTime)
					.withInt("processTimeMax", processTimeMax)
					.withBoolean("active", this.active)
					.withTag("inventory", Utils.writeInventory(inventory))
					.withItemStack("output", effect)
					.withList("toolOrder", (Object[])toolOrder)
					.withList("animationOrder", (Object[])animationOrder)
					.withFloat("animationTimeMod", this.processQueue.size() > 0?this.processQueue.get(0).recipe.timeModifier: 1)
					.conditionally(beginning, easyNBT -> easyNBT.withBoolean("beginning", true))
			));

			update = false;
		}
	}

	@Override
	public float[] getBlockBounds()
	{
		return new float[]{0, 0, 0, 1, 1, 1};
	}

	@Override
	public int[] getEnergyPos()
	{
		return new int[]{44};
	}

	@Override
	public int[] getRedstonePos()
	{
		return new int[]{25};
	}

	@Override
	public boolean isInWorldProcessingMachine()
	{
		return true;
	}

	@Override
	public boolean additionalCanProcessCheck(MultiblockProcess<PrecisionAssemblerRecipe> process)
	{
		boolean check = true;
		//Finally found ye ^^
		test:
		{
			if(!(!inventory.get(3).isEmpty()&&(inventory.get(3).getItem() instanceof ItemIIAssemblyScheme)&&(((ItemIIAssemblyScheme)inventory.get(3).getItem()).getRecipeForStack(inventory.get(3)).equals(process.recipe))))
			{
				check = false;
				break test;
			}

			if(!toolOrder[0].equals(""))
			{
				if(inventory.get(0).isEmpty())
				{
					check = false;
					break test;
				}
				if(!((IPrecisionTool)inventory.get(0).getItem()).getPrecisionToolType(inventory.get(0)).equals(toolOrder[0]))
				{
					check = false;
					break test;
				}
			}

			if(!toolOrder[1].equals(""))
			{
				if(inventory.get(1).isEmpty())
				{
					check = false;
					break test;
				}
				if(!((IPrecisionTool)inventory.get(1).getItem()).getPrecisionToolType(inventory.get(1)).equals(toolOrder[1]))
				{
					check = false;
					break test;
				}
			}

			if(!toolOrder[2].equals(""))
			{
				if(inventory.get(2).isEmpty())
				{
					check = false;
					break test;
				}
				if(!((IPrecisionTool)inventory.get(2).getItem()).getPrecisionToolType(inventory.get(2)).equals(toolOrder[2]))
				{
					check = false;
				}
			}
		}

		if(!check)
		{
			processQueue.clear();
			update = true;
		}

		return check;
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
	public void onProcessFinish(MultiblockProcess<PrecisionAssemblerRecipe> process)
	{
		if(((IPrecisionTool)inventory.get(0).getItem()).getPrecisionToolType(inventory.get(0)).equals(toolOrder[0]))
			((IPrecisionTool)inventory.get(0).getItem()).damagePrecisionTool(inventory.get(0), 1);
		if(((IPrecisionTool)inventory.get(0).getItem()).getPrecisionToolType(inventory.get(0)).equals(toolOrder[1]))
			((IPrecisionTool)inventory.get(1).getItem()).damagePrecisionTool(inventory.get(1), 1);
		if(((IPrecisionTool)inventory.get(0).getItem()).getPrecisionToolType(inventory.get(0)).equals(toolOrder[2]))
			((IPrecisionTool)inventory.get(2).getItem()).damagePrecisionTool(inventory.get(2), 1);

		((ItemIIAssemblyScheme)inventory.get(3).getItem()).increaseCreatedItems(inventory.get(3), process.recipe.output.getCount());

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
	public float getMinProcessDistance(MultiblockProcess<PrecisionAssemblerRecipe> process)
	{
		return 0;
	}


	@Override
	public NonNullList<ItemStack> getInventory()
	{
		return inventory;
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		if(slot==0||slot==1||slot==2)
			return stack.getItem() instanceof IPrecisionTool;
		if(slot==3)
			return stack.getItem() instanceof ItemIIAssemblyScheme;
		if(slot >= 4&&slot <= 7)
		{
			if(inventory.get(3).getItem() instanceof ItemIIAssemblyScheme)
			{
				IngredientStack[] stacks = IIContent.itemAssemblyScheme.getRecipeForStack(inventory.get(3)).inputs;
				return stacks.length > slot-4&&stacks[slot-4].matchesItemStack(stack);
			}
			else
				return false;
		}

		return slot < 8;
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return 64;
	}

	@Override
	public int[] getOutputSlots()
	{
		return new int[]{8, 9};
	}

	@Override
	public int[] getOutputTanks()
	{
		return new int[]{};
	}

	@Override
	public IFluidTank[] getInternalTanks()
	{
		return new FluidTank[0];
	}

	@Override
	protected IFluidTank[] getAccessibleFluidTanks(EnumFacing side)
	{
		return new FluidTank[0];
	}

	@Override
	protected boolean canFillTankFrom(int iTank, EnumFacing side, FluidStack resource)
	{
		return false;
	}

	@Override
	protected boolean canDrainTankFrom(int iTank, EnumFacing side)
	{
		return (pos==14&&side==facing);
	}

	@Override
	public void doGraphicalUpdates(int slot)
	{
		this.markDirty();
		this.markContainingBlockForUpdate(null);
	}

	@Override
	public PrecisionAssemblerRecipe findRecipeForInsertion(ItemStack inserting)
	{
		return null;
	}

	@Override
	protected PrecisionAssemblerRecipe readRecipeFromNBT(NBTTagCompound tag)
	{
		processQueue.clear();
		update = true;
		return PrecisionAssemblerRecipe.loadFromNBT(tag);
	}

	@Override
	public boolean canOpenGui()
	{
		return formed;
	}

	@Override
	public int getGuiID()
	{
		return IIGuiList.GUI_PRECISION_ASSEMBLER.ordinal();
	}

	@Override
	public TileEntity getGuiMaster()
	{
		return master();
	}

	@Override
	public boolean shoudlPlaySound(String sound)
	{
		return false;
	}

	@Override
	public void onAnimationChangeClient(boolean state, int part)
	{
		isDrawerOpened[part] = state;
	}

	@Override
	public void onAnimationChangeServer(boolean state, int part)
	{
		if(part >= 2)
			return;
		if(state!=isDrawerOpened[part])
			world.playSound(null, getPos(), state?IISounds.drawerOpen: IISounds.drawerClose, SoundCategory.BLOCKS, 0.25F, 1f);

		isDrawerOpened[part] = state;

		for(int i = 0; i < 2; i++)
			IIPacketHandler.INSTANCE.sendToAllAround(new MessageBooleanAnimatedPartsSync(isDrawerOpened[i], i, getPos()), IIPacketHandler.targetPointFromPos(this.getPos(), this.world, 32));
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		if(pos==9&&capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return true;
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY&&pos==9)
		{
			TileEntityPrecisionAssembler master = master();
			return (T)master.insertionHandler;
		}

		return super.getCapability(capability, facing);
	}
}
