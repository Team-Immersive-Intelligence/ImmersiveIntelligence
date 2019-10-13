package pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.ISoundTile;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import blusunrize.immersiveengineering.common.util.network.MessageTileSync;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.apache.commons.lang3.ArrayUtils;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.IBooleanAnimatedPartsBlock;
import pl.pabilo8.immersiveintelligence.api.IPrecissionTool;
import pl.pabilo8.immersiveintelligence.api.crafting.PrecissionAssemblerRecipe;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIAssemblyScheme;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.MessageBooleanAnimatedPartsSync;

import javax.annotation.Nullable;
import java.util.ArrayList;

import static pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.precissionAssembler;

/**
 * Created by Pabilo8 on 28-06-2019.
 */
public class TileEntityPrecissionAssembler extends TileEntityMultiblockMetal<TileEntityPrecissionAssembler, PrecissionAssemblerRecipe> implements IGuiTile, ISoundTile, IBooleanAnimatedPartsBlock
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

	public boolean isDrawer1Opened = false, isDrawer2Opened = false;
	public float drawer1Angle = 0, drawer2Angle = 0;

	IItemHandler insertionHandler = new IEInventoryHandler(5, this, 4, true, false);

	public TileEntityPrecissionAssembler()
	{
		super(MultiblockPrecissionAssembler.instance, new int[]{3, 3, 5}, precissionAssembler.energyCapacity, true);
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
			ImmersiveIntelligence.logger.info("received1");
			NBTTagList list = message.getTagList("toolOrder", NBT.TAG_STRING);
			toolOrder = new String[]{};
			ArrayList<String> l2 = new ArrayList<>();
			for(NBTBase base : list.tagList)
			{
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

			animationOrder = new String[]{};
			animationPrepared.clear();
			for(NBTBase base : list.tagList)
			{
				NBTTagString string = (NBTTagString)base;
				ArrayUtils.add(animationOrder, string.getString());
				int duration = PrecissionAssemblerRecipe.toolMap.get(string.getString().split(" ")[0]).getWorkTime(string.getString().split(" ")[0]);
				Tuple<Integer, String> tuple = new Tuple<Integer, String>(duration, string.getString());
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
			if(isDrawer1Opened&&drawer1Angle < 5f)
				drawer1Angle = Math.min(drawer1Angle+0.4f, 5f);
			else if(!isDrawer1Opened&&drawer1Angle > 0f)
				drawer1Angle = Math.max(drawer1Angle-0.5f, 0f);

			if(isDrawer2Opened&&drawer2Angle < 5f)
				drawer2Angle = Math.min(drawer2Angle+0.4f, 5f);
			else if(!isDrawer2Opened&&drawer2Angle > 0f)
				drawer2Angle = Math.max(drawer2Angle-0.5f, 0f);

			if(active&&processTime < processTimeMax)
				processTime += 1;
			return;
		}

		boolean wasActive = active;

		boolean beginning = false;

		if(energyStorage.getEnergyStored() > 0&&processQueue.size() < this.getProcessQueueMaxLength())
		{
			//Lots of stuffs happening(s)
			PrecissionAssemblerRecipe recipe = PrecissionAssemblerRecipe.findRecipe(new ItemStack[]{inventory.get(4), inventory.get(5), inventory.get(6), inventory.get(7)}, inventory.get(3), new ItemStack[]{inventory.get(0), inventory.get(1), inventory.get(2)});
			if(recipe!=null)
			{
				MultiblockProcessInMachine<PrecissionAssemblerRecipe> process = new MultiblockProcessInMachine(recipe, 4, 5, 6, 7);
				this.addProcessToQueue(process, false);
				update = true;
				processTime = 0;
				processTimeMax = recipe.getTotalProcessTime();

				String t0 = "", t1 = "", t2 = "";
				if(!inventory.get(0).isEmpty()&&inventory.get(0).getItem() instanceof IPrecissionTool)
				{
					t0 = ((IPrecissionTool)inventory.get(0).getItem()).getPrecissionToolType(inventory.get(0));
				}
				if(!inventory.get(1).isEmpty()&&inventory.get(1).getItem() instanceof IPrecissionTool)
				{
					t1 = ((IPrecissionTool)inventory.get(1).getItem()).getPrecissionToolType(inventory.get(1));
				}
				if(!inventory.get(2).isEmpty()&&inventory.get(2).getItem() instanceof IPrecissionTool)
				{
					t2 = ((IPrecissionTool)inventory.get(2).getItem()).getPrecissionToolType(inventory.get(2));
				}

				toolOrder = new String[]{t0, t1, t2};
				animationOrder = recipe.animations;
				effect = recipe.output;
				beginning = true;
			}
		}

		if(world.getTotalWorldTime()%20==0)
		{
			BlockPos pos = getBlockPosForPos(5).offset(facing.rotateYCCW(), 1);
			ItemStack output = inventory.get(8);
			TileEntity inventoryTile = this.world.getTileEntity(pos);
			if(inventoryTile!=null)
				output = Utils.insertStackIntoInventory(inventoryTile, output, facing.rotateY());
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

			this.markDirty();
			this.markContainingBlockForUpdate(null);
			NBTTagCompound tag = new NBTTagCompound();

			tag.setInteger("processTime", processTime);
			tag.setInteger("processTimeMax", processTimeMax);
			tag.setBoolean("active", this.active);
			tag.setTag("inventory", Utils.writeInventory(inventory));
			NBTTagCompound itemTag = new NBTTagCompound();
			effect.writeToNBT(itemTag);
			tag.setTag("output", itemTag);

			NBTTagList toolList = new NBTTagList();
			for(String tool : toolOrder)
				toolList.appendTag(new NBTTagString(tool));
			tag.setTag("toolOrder", toolList);

			NBTTagList animationList = new NBTTagList();
			for(String animation : animationOrder)
				animationList.appendTag(new NBTTagString(animation));
			tag.setTag("animationOrder", animationList);

			if(beginning)
				tag.setBoolean("beginning", true);

			ImmersiveEngineering.packetHandler.sendToAllAround(new MessageTileSync(this, tag), new TargetPoint(this.world.provider.getDimension(), this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), 32));
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
	public boolean additionalCanProcessCheck(MultiblockProcess<PrecissionAssemblerRecipe> process)
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
					ImmersiveIntelligence.logger.info("o1");
					check = false;
					break test;
				}
				if(!((IPrecissionTool)inventory.get(0).getItem()).getPrecissionToolType(inventory.get(0)).equals(toolOrder[0]))
				{
					ImmersiveIntelligence.logger.info("o2");
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
				if(!((IPrecissionTool)inventory.get(1).getItem()).getPrecissionToolType(inventory.get(1)).equals(toolOrder[1]))
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
				if(!((IPrecissionTool)inventory.get(2).getItem()).getPrecissionToolType(inventory.get(2)).equals(toolOrder[2]))
				{
					check = false;
				}
			}
		}

		if(!check)
		{
			ImmersiveIntelligence.logger.info(check);
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
	public void onProcessFinish(MultiblockProcess<PrecissionAssemblerRecipe> process)
	{

		if(!toolOrder[0].equals(""))
			((IPrecissionTool)inventory.get(0).getItem()).damagePrecissionTool(inventory.get(0), 1);
		if(!toolOrder[1].equals(""))
			((IPrecissionTool)inventory.get(1).getItem()).damagePrecissionTool(inventory.get(1), 1);
		if(!toolOrder[2].equals(""))
			((IPrecissionTool)inventory.get(2).getItem()).damagePrecissionTool(inventory.get(2), 1);

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
	public float getMinProcessDistance(MultiblockProcess<PrecissionAssemblerRecipe> process)
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
			return stack.getItem() instanceof IPrecissionTool;
		if(slot==3)
			return stack.getItem() instanceof ItemIIAssemblyScheme;
		if(slot >= 4&&slot <= 6)
		{
			if(inventory.get(3).getItem() instanceof ItemIIAssemblyScheme)
			{
				IngredientStack[] stacks = ImmersiveIntelligence.proxy.item_assembly_scheme.getRecipeForStack(inventory.get(3)).inputs;
				return stacks.length > slot-4&&stacks[slot-4].matchesItemStack(stack);
			}
			else
				return false;
		}

		return true;
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
	public PrecissionAssemblerRecipe findRecipeForInsertion(ItemStack inserting)
	{
		return null;
	}

	@Override
	protected PrecissionAssemblerRecipe readRecipeFromNBT(NBTTagCompound tag)
	{
		processQueue.clear();
		update = true;
		return PrecissionAssemblerRecipe.loadFromNBT(tag);
	}

	@Override
	public boolean canOpenGui()
	{
		return formed;
	}

	@Override
	public int getGuiID()
	{
		return IIGuiList.GUI_PRECISSION_ASSEMBLER;
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
		if(part==0)
			isDrawer1Opened = state;
		else if(part==1)
			isDrawer2Opened = state;
	}

	@Override
	public void onAnimationChangeServer(boolean state, int part)
	{
		if(part==0)
			isDrawer1Opened = state;
		else if(part==1)
			isDrawer2Opened = state;

		IIPacketHandler.INSTANCE.sendToAllAround(new MessageBooleanAnimatedPartsSync(isDrawer1Opened, 0, getPos()), pl.pabilo8.immersiveintelligence.api.Utils.targetPointFromPos(this.getPos(), this.world, 32));
		IIPacketHandler.INSTANCE.sendToAllAround(new MessageBooleanAnimatedPartsSync(isDrawer2Opened, 1, getPos()), pl.pabilo8.immersiveintelligence.api.Utils.targetPointFromPos(this.getPos(), this.world, 32));

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
			TileEntityPrecissionAssembler master = master();
			return (T)master.insertionHandler;
		}

		return super.getCapability(capability, facing);
	}
}
