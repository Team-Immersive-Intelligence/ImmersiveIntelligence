package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.crafting.IMultiblockRecipe;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.IConveyorAttachable;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedCollisionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedSelectionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.ISoundTile;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import blusunrize.immersiveengineering.common.util.inventory.MultiFluidTank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.PrintingPress;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IDataConnector;
import pl.pabilo8.immersiveintelligence.api.data.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockPrintingPress;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8
 * @since 28-06-2019
 */
public class TileEntityPrintingPress extends TileEntityMultiblockMetal<TileEntityPrintingPress, IMultiblockRecipe> implements IDataDevice, IAdvancedCollisionBounds, IAdvancedSelectionBounds, IGuiTile, IConveyorAttachable, ISoundTile
{
	public boolean active = false, hasPaper = false;
	public DataPacket dataToPrint = new DataPacket();
	public DataPacket newDataToPrint = new DataPacket();
	public int pagesLeft = 0;
	public int processTimeLeft = 0;
	public int rollerRotation = 0;
	public ItemStack renderStack0, renderStack1;

	public MultiFluidTank[] tanks = new MultiFluidTank[]{new MultiFluidTank(8000)};
	public NonNullList<ItemStack> inventory = NonNullList.withSize(4, ItemStack.EMPTY);
	IItemHandler inventoryHandler = new IEInventoryHandler(4, this, 0, true, true);
	IItemHandler insertionHandler = new IEInventoryHandler(1, this, 0, true, true);


	public TileEntityPrintingPress()
	{
		super(MultiblockPrintingPress.INSTANCE, new int[]{3, 5, 3}, PrintingPress.energyCapacity, false);
		renderStack0 = new ItemStack(IIContent.itemPrintedPage, 1, 0);
		renderStack1 = new ItemStack(IIContent.itemPrintedPage, 1, 1);
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		if(!isDummy())
		{
			if(!descPacket)
			{
				inventory = Utils.readInventory(nbt.getTagList("inventory", 10), 4);
				dataToPrint = new DataPacket();
				dataToPrint.fromNBT(nbt.getCompoundTag("dataToPrint"));
				newDataToPrint = new DataPacket();
				if(nbt.hasKey("newDataToPrint"))
					newDataToPrint.fromNBT(nbt.getCompoundTag("newDataToPrint"));
			}
			tanks[0].readFromNBT(nbt.getCompoundTag("tank"));
			active = nbt.getBoolean("active");
			processTimeLeft = nbt.getInteger("processTimeLeft");
		}
	}

	@Override
	public void receiveMessageFromClient(NBTTagCompound message)
	{
		super.receiveMessageFromClient(message);
	}

	@Override
	public void receiveMessageFromServer(NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);
		if(message.hasKey("processTimeLeft"))
			this.processTimeLeft = message.getInteger("processTimeLeft");
		if(message.hasKey("active"))
			this.active = message.getBoolean("active");
		if(message.hasKey("inventory"))
			inventory = Utils.readInventory(message.getTagList("inventory", 10), 4);
		if(message.hasKey("tank"))
			tanks[0] = tanks[0].readFromNBT(message.getCompoundTag("tank"));
	}

	@Override
	public void onChunkUnload()
	{
		super.onChunkUnload();
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);
		if(!isDummy())
		{
			if(!descPacket)
			{
				nbt.setTag("inventory", Utils.writeInventory(inventory));
				nbt.setTag("dataToPrint", dataToPrint.toNBT());
				if(newDataToPrint!=null)
					nbt.setTag("newDataToPrint", newDataToPrint.toNBT());
			}
			nbt.setTag("tank", tanks[0].writeToNBT(new NBTTagCompound()));
			nbt.setBoolean("active", active);
			nbt.setInteger("processTimeLeft", processTimeLeft);
		}
	}

	@Override
	public void update()
	{
		super.update();
		NBTTagCompound tag = new NBTTagCompound();

		if(world.isRemote)
		{
			ImmersiveEngineering.proxy.handleTileSound(IISounds.rolling, this, active, .5f, 1);
			if(processTimeLeft > 0&&active)
			{
				processTimeLeft -= 1;
				rollerRotation += 6;
				if(rollerRotation > 360)
					rollerRotation = 0;
			}
		}

		if(world.isRemote||isDummy())
			return;

		boolean update = false;

		if(world.getTotalWorldTime()%20==0)
		{
			BlockPos pos = getBlockPosForPos(2).offset(facing.getOpposite(), 1);
			ItemStack output = inventory.get(1);
			TileEntity inventoryTile = this.world.getTileEntity(pos);
			if(inventoryTile!=null)
				output = Utils.insertStackIntoInventory(inventoryTile, output, facing.getOpposite());
			inventory.set(1, output);
			update = true;
		}


		if(pagesLeft > 0)
		{
			if(processTimeLeft%(PrintingPress.printTime/4)==0)
				update = true;

			if(processTimeLeft > 0&&energyStorage.getEnergyStored() >= PrintingPress.energyUsage)
			{
				energyStorage.extractEnergy(PrintingPress.energyUsage, false);
				processTimeLeft -= 1;

				if(processTimeLeft%10==0)
				{
					update = true;
					active = true;
				}
			}
			else if(!inventory.get(0).isEmpty()&&processTimeLeft < 1)
			{
				if(newDataToPrint!=null)
				{
					dataToPrint = newDataToPrint;
					newDataToPrint = null;
				}
				active = true;
				hasPaper = true;
				inventoryHandler.extractItem(0, 1, false);
				processTimeLeft = PrintingPress.printTime;
				update = true;
			}
			else
				active = false;

			if(processTimeLeft < 1&&hasPaper)
			{
				onProcessFinishButInYellow();
				update = true;
				hasPaper = false;
				world.playSound(null, getBlockPosForPos(2), IISounds.paperEject, SoundCategory.BLOCKS, .3F, 1);

			}
			tag.setTag("inventory", Utils.writeInventory(inventory));

		}
		else
			active = false;

		if(update)
		{
			tag.setInteger("processTimeLeft", processTimeLeft);
			tag.setBoolean("active", active);
		}

		if(IIUtils.handleBucketTankInteraction(tanks, inventory, 2, 3, 0, false))
		{
			update = true;
			tag.setTag("tank", tanks[0].writeToNBT(new NBTTagCompound()));
		}

		if(update)
			IIPacketHandler.sendToClient(this, new MessageIITileSync(this, tag));


	}

	void onProcessFinishButInYellow()
	{
		//Name is a reference to the meme 'yes / yes but in yellow' (probably a bad practice)
		pagesLeft -= 1;
		processTimeLeft = 0;


		if(pagesLeft < 1)
			active = false;

		DataTypeString item_type = dataToPrint.getVarInType(DataTypeString.class, dataToPrint.getPacketVariable('m'));

		switch(item_type.value)
		{
			case "text":
			{
				String toPrint = dataToPrint.getPacketVariable('t').valueToString();
				StringBuilder printedChars = new StringBuilder();

				int black_amount = 0, cyan_amount = 0, magenta_amount = 0, yellow_amount = 0;

				for(FluidStack stack : tanks[0].fluids)
					if(stack.getFluid().getName().equals("ink"))
						black_amount += stack.amount;
					else if(stack.getFluid().getName().equals("ink_cyan"))
						cyan_amount += stack.amount;
					else if(stack.getFluid().getName().equals("ink_magenta"))
						magenta_amount += stack.amount;
					else if(stack.getFluid().getName().equals("ink_yellow"))
						yellow_amount += stack.amount;

				int black_amount_start = black_amount, cyan_amount_start = cyan_amount, magenta_amount_start = magenta_amount, yellow_amount_start = yellow_amount;

				ArrayList<Float> black_cost = new ArrayList<>(),
						cyan_cost = new ArrayList<>(),
						magenta_cost = new ArrayList<>(),
						yellow_cost = new ArrayList<>();

				black_cost.add(1.0f);
				cyan_cost.add(0.0f);
				magenta_cost.add(0.0f);
				yellow_cost.add(0.0f);

				int charnum = 0, shouldstartfrom = 0, tag_endings_needed = 0;

				for(char c : toPrint.toCharArray())
				{
					charnum += 1;
					if(charnum < shouldstartfrom)
						continue;

					if(c=='<')
					{
						//printedChars+="<";
						String fragment = toPrint.substring(charnum-1);

						if(fragment.startsWith("<br>"))
						{
							shouldstartfrom = charnum+4;
							printedChars.append("<br>");
						}
						else if(fragment.startsWith("<hexcol="))
						{
							if(black_cost.get(black_cost.size()-1)*PrintingPress.printInkUsage > black_amount)
								black_cost.set(black_cost.size()-1, (float)black_amount/(float)PrintingPress.printInkUsage);

							if(cyan_cost.get(cyan_cost.size()-1)*PrintingPress.printInkUsage > cyan_amount)
								cyan_cost.set(cyan_cost.size()-1, (float)cyan_amount/(float)PrintingPress.printInkUsage);

							if(magenta_cost.get(magenta_cost.size()-1)*PrintingPress.printInkUsage > magenta_amount)
								magenta_cost.set(magenta_cost.size()-1, (float)magenta_amount/(float)PrintingPress.printInkUsage);

							if(yellow_cost.get(yellow_cost.size()-1)*PrintingPress.printInkUsage > yellow_amount)
								yellow_cost.set(yellow_cost.size()-1, (float)yellow_amount/(float)PrintingPress.printInkUsage);

							try
							{
								int color = Integer.parseInt(fragment.substring(8, 14), 16);
								Color col = new Color(color);
								int[] colors = IIUtils.rgbToCmyk(col.getRed(), col.getGreen(), col.getBlue());
								cyan_cost.add(((float)colors[0])/255f);
								yellow_cost.add(((float)colors[1])/255f);
								magenta_cost.add(((float)colors[2])/255f);
								black_cost.add(((float)colors[3])/255f);
								tag_endings_needed += 1;

							} catch(NumberFormatException n)
							{

							}

							shouldstartfrom = charnum+15;
							printedChars.append("<hexcol=").append(fragment, 8, 14).append(":");

						}
					}
					else if(tag_endings_needed > 0&&c=='>')
					{
						printedChars.append(">");
						tag_endings_needed -= 1;
						black_cost.remove(black_cost.size()-1);
						cyan_cost.remove(cyan_cost.size()-1);
						magenta_cost.remove(magenta_cost.size()-1);
						yellow_cost.remove(yellow_cost.size()-1);
					}
					else
					{
						if(black_cost.get(black_cost.size()-1)==0&&cyan_cost.get(cyan_cost.size()-1)==0&&magenta_cost.get(magenta_cost.size()-1)==0&&yellow_cost.get(yellow_cost.size()-1)==0)
						{
							printedChars.append(" ");
							continue;
						}
						if(black_cost.get(black_cost.size()-1)*PrintingPress.printInkUsage <= black_amount&&
								cyan_cost.get(cyan_cost.size()-1)*PrintingPress.printInkUsage <= cyan_amount&&
								magenta_cost.get(magenta_cost.size()-1)*PrintingPress.printInkUsage <= magenta_amount&&
								yellow_cost.get(yellow_cost.size()-1)*PrintingPress.printInkUsage <= yellow_amount)
						{
							printedChars.append(c);
							black_amount -= black_cost.get(black_cost.size()-1)*PrintingPress.printInkUsage;
							cyan_amount -= cyan_cost.get(cyan_cost.size()-1)*PrintingPress.printInkUsage;
							magenta_amount -= magenta_cost.get(magenta_cost.size()-1)*PrintingPress.printInkUsage;
							yellow_amount -= yellow_cost.get(yellow_cost.size()-1)*PrintingPress.printInkUsage;


						}
						else
						{
							if(black_cost.get(black_cost.size()-1)*PrintingPress.printInkUsage > black_amount)
								black_cost.set(black_cost.size()-1, (float)black_amount/(float)PrintingPress.printInkUsage);

							if(cyan_cost.get(cyan_cost.size()-1)*PrintingPress.printInkUsage > cyan_amount)
								cyan_cost.set(cyan_cost.size()-1, (float)cyan_amount/(float)PrintingPress.printInkUsage);

							if(magenta_cost.get(magenta_cost.size()-1)*PrintingPress.printInkUsage > magenta_amount)
								magenta_cost.set(magenta_cost.size()-1, (float)magenta_amount/(float)PrintingPress.printInkUsage);

							if(yellow_cost.get(yellow_cost.size()-1)*PrintingPress.printInkUsage > yellow_amount)
								yellow_cost.set(yellow_cost.size()-1, (float)yellow_amount/(float)PrintingPress.printInkUsage);

							if(black_cost.get(black_cost.size()-1)==0&&cyan_cost.get(cyan_cost.size()-1)==0&&magenta_cost.get(magenta_cost.size()-1)==0&&yellow_cost.get(yellow_cost.size()-1)==0)
								printedChars.append(" ");
							else
							{
								printedChars.append("> <hexcol=");
								int[] colors = IIUtils.cmykToRgb(Math.round(cyan_cost.get(cyan_cost.size()-1)*255), Math.round(magenta_cost.get(magenta_cost.size()-1)*255), Math.round(yellow_cost.get(yellow_cost.size()-1)*255), Math.round(black_cost.get(black_cost.size()-1)*255));
								printedChars.append(String.format("%02x%02x%02x:", Math.round(colors[0]), Math.round(colors[1]), Math.round(colors[2])));
							}

						}
					}
				}
				while(tag_endings_needed > 0)
				{
					tag_endings_needed -= 1;
					printedChars.append(">");
				}

				tanks[0].drain(FluidRegistry.getFluidStack("ink", black_amount_start-black_amount), true);
				tanks[0].drain(FluidRegistry.getFluidStack("ink_cyan", cyan_amount_start-cyan_amount), true);
				tanks[0].drain(FluidRegistry.getFluidStack("ink_magenta", magenta_amount_start-magenta_amount), true);
				tanks[0].drain(FluidRegistry.getFluidStack("ink_yellow", yellow_amount_start-yellow_amount), true);

				//Finally! lol (i wrote this all without any debugging, so i count on you bug reporters ^^)
				ItemStack stack = new ItemStack(IIContent.itemPrintedPage, 1, 1);
				IIContent.itemPrintedPage.setText(stack, printedChars.toString());

				stack = inventoryHandler.insertItem(1, stack, false);
				if(!stack.isEmpty())
					Utils.dropStackAtPos(world, getBlockPosForPos(2), stack, this.facing);
			}
			break;
			case "blueprint":
			{
				inventoryHandler.insertItem(1, new ItemStack(IIContent.itemPrintedPage, 1, 3), false);
			}
			break;
			case "code":
			{
				inventoryHandler.insertItem(1, new ItemStack(IIContent.itemPrintedPage, 1, 2), false);
			}
			break;
			default:
			{
				inventoryHandler.insertItem(1, new ItemStack(IIContent.itemPrintedPage, 1, 0), false);
			}
			break;
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
		return new int[]{34};
	}

	@Override
	public int[] getRedstonePos()
	{
		return new int[]{};
	}

	@Override
	public boolean isInWorldProcessingMachine()
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
	public void onProcessFinish(MultiblockProcess<IMultiblockRecipe> process)
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
	public float getMinProcessDistance(MultiblockProcess<IMultiblockRecipe> process)
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
		if(slot==0)
			return Utils.compareToOreName(stack, "pageEmpty");
		else if(slot==1)
			return Utils.compareToOreName(stack, "pageWritten")||Utils.compareToOreName(stack, "pageEmpty");
		else
			return stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return slot==1?12: 64;
	}

	@Override
	public int[] getOutputSlots()
	{
		return new int[]{1};
	}

	@Override
	public int[] getOutputTanks()
	{
		return new int[0];
	}

	@Override
	public boolean additionalCanProcessCheck(MultiblockProcess<IMultiblockRecipe> process)
	{
		return false;
	}

	@Override
	public IFluidTank[] getInternalTanks()
	{
		return tanks;
	}

	@Override
	protected IFluidTank[] getAccessibleFluidTanks(EnumFacing side)
	{
		TileEntityPrintingPress master = this.master();
		if(master!=null)
			if(pos==29&&side.getAxis()==Axis.Y)
				return master.tanks;
		return new FluidTank[0];
	}

	@Override
	protected boolean canFillTankFrom(int iTank, EnumFacing side, FluidStack resource)
	{
		String fname = resource.getFluid().getName();
		if(pos==29&&side.getAxis()==Axis.Y&&(fname.equals("ink")||fname.equals("ink_cyan")||fname.equals("ink_magenta")||fname.equals("ink_yellow")))
		{
			TileEntityPrintingPress master = this.master();
			return !(master==null||master.tanks[iTank].getFluidAmount() >= master.tanks[iTank].getCapacity());
		}
		return false;
	}

	@Override
	protected boolean canDrainTankFrom(int iTank, EnumFacing side)
	{
		return (side.getAxis()==Axis.Y&&iTank==0);
	}

	@Override
	public void doGraphicalUpdates(int slot)
	{
		this.markDirty();
	}

	@Override
	public IMultiblockRecipe findRecipeForInsertion(ItemStack inserting)
	{
		return null;
	}

	@Override
	protected IMultiblockRecipe readRecipeFromNBT(NBTTagCompound tag)
	{
		return null;
	}

	@Override
	public void onReceive(DataPacket packet, EnumFacing side)
	{
		if(pos==6)
			master().onReceive(packet, side);

		if(!this.isDummy())
			if(packet.variables.containsKey('c'))
			{
				IDataType c = packet.getPacketVariable('c');
				if(c instanceof DataTypeString)
				{
					IDataConnector conn = IIUtils.findConnectorFacing(getBlockPosForPos(6), world, mirrored?facing.rotateY(): facing.rotateYCCW());
					if(conn==null)
						return;
					DataPacket p = new DataPacket();
					int amount;

					switch(((DataTypeString)c).value)
					{
						case "get_ink":
						case "get_ink_black":
							amount = tanks[0].fluids.stream().filter(fluidStack -> fluidStack.getFluid().getName().equals("ink")).mapToInt(fs -> fs.amount).sum();
							p.setVariable('c', new DataTypeString("ink_black"));
							p.setVariable('g', new DataTypeInteger(amount));
							conn.sendPacket(p);
							break;
						case "get_ink_cyan":
							amount = tanks[0].fluids.stream().filter(fluidStack -> fluidStack.getFluid().getName().equals("ink_cyan")).mapToInt(fs -> fs.amount).sum();
							p.setVariable('c', new DataTypeString("ink_cyan"));
							p.setVariable('g', new DataTypeInteger(amount));
							conn.sendPacket(p);
							break;
						case "get_ink_yellow":
							amount = tanks[0].fluids.stream().filter(fluidStack -> fluidStack.getFluid().getName().equals("ink_yellow")).mapToInt(fs -> fs.amount).sum();
							p.setVariable('c', new DataTypeString("ink_yellow"));
							p.setVariable('g', new DataTypeInteger(amount));
							conn.sendPacket(p);
							break;
						case "get_ink_magenta":
							amount = tanks[0].fluids.stream().filter(fluidStack -> fluidStack.getFluid().getName().equals("ink_magenta")).mapToInt(fs -> fs.amount).sum();
							p.setVariable('c', new DataTypeString("ink_magenta"));
							p.setVariable('g', new DataTypeInteger(amount));
							conn.sendPacket(p);
							break;

						case "get_energy":
							p.setVariable('c', new DataTypeString("energy"));
							p.setVariable('g', new DataTypeInteger(energyStorage.getEnergyStored()));
							conn.sendPacket(p);
							break;
						case "get_paper":
							p.setVariable('c', new DataTypeString("paper"));
							p.setVariable('g', new DataTypeInteger(inventory.get(0).getCount()));
							conn.sendPacket(p);
							break;
					}
				}
			}
			else
			{
				energyStorage.extractEnergy(PrintingPress.energyUsage, false);
				this.pagesLeft = packet.getVarInType(DataTypeInteger.class, packet.getPacketVariable('a')).value;
				this.newDataToPrint = packet.clone();
			}
	}

	@Override
	public List<AxisAlignedBB> getAdvancedSelectionBounds()
	{
		ArrayList<AxisAlignedBB> list = new ArrayList<>();

		if(pos==0||pos==1)
		{
			list.add(new AxisAlignedBB(0, 0.8125, 0, 1, 1, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));

			switch(mirrored^pos==1?facing.getOpposite(): facing)
			{
				case NORTH:
					list.add(new AxisAlignedBB(0.0625, 0, 0.0625, 0.25, 0.8125, 0.25).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					list.add(new AxisAlignedBB(0.0625, 0, 0.75, 0.25, 0.8125, 0.9375).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					break;
				case SOUTH:
					list.add(new AxisAlignedBB(0.75, 0, 0.0625, 0.9375, 0.8125, 0.25).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					list.add(new AxisAlignedBB(0.75, 0, 0.75, 0.9375, 0.8125, 0.9375).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					break;
				case EAST:
					list.add(new AxisAlignedBB(0.0625, 0, 0.0625, 0.25, 0.8125, 0.25).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					list.add(new AxisAlignedBB(0.75, 0, 0.0625, 0.9375, 0.8125, 0.25).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					break;
				case WEST:
					list.add(new AxisAlignedBB(0.0625, 0, 0.75, 0.25, 0.8125, 0.9375).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					list.add(new AxisAlignedBB(0.75, 0, 0.75, 0.9375, 0.8125, 0.9375).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					break;
			}
		}
		else if(pos==2)
		{
			list.add(new AxisAlignedBB(0.125, 0, 0.125, 0.875, 0.25, 0.875).expand(facing.getFrontOffsetX()*0.125, 0, facing.getFrontOffsetZ()*0.125).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
			switch(facing)
			{
				case NORTH:
					list.add(new AxisAlignedBB(0, 0, 0.875, 1, 1.125, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					list.add(new AxisAlignedBB(0, 0, 0, 0.125, 1.125, 0.875).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					list.add(new AxisAlignedBB(0.875, 0, 0, 1, 1.125, 0.875).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					break;
				case SOUTH:
					list.add(new AxisAlignedBB(0, 0, 0, 1, 1.125, 0.125).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					list.add(new AxisAlignedBB(0, 0, 0.125, 0.125, 1.125, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					list.add(new AxisAlignedBB(0.875, 0, 0.125, 1, 1.125, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					break;
				case EAST:
					list.add(new AxisAlignedBB(0, 0, 0, 0.125, 1.125, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					list.add(new AxisAlignedBB(0.125, 0, 0, 1, 1.125, 0.125).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					list.add(new AxisAlignedBB(0.125, 0, 0.875, 1, 1.125, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					break;
				case WEST:
					list.add(new AxisAlignedBB(0.875, 0, 0, 1, 1.125, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					list.add(new AxisAlignedBB(0, 0, 0, 0.875, 1.125, 0.125).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					list.add(new AxisAlignedBB(0, 0, 0.875, 0.875, 1.125, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					break;
			}
		}
		else if(pos==15)
			list.add(new AxisAlignedBB(0.125, 0, 0.125, 0.875, 0.4375, 0.875).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
		else if(pos==16)
			list.add(new AxisAlignedBB(0.0625, 0, 0.0625, 0.9375, 0.4375, 0.9375)
					.offset(new Vec3d((mirrored?facing.rotateY(): facing.rotateYCCW()).getDirectionVec()).scale(0.0625f))
					//.offset(new Vec3d(facing.getDirectionVec()).scale(-0.0625f))
					.offset(getPos().getX(), getPos().getY(), getPos().getZ()));
		else if(pos==34)
		{
			list.add(new AxisAlignedBB(0, 0, 0, 1, 0.5, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
			list.add(new AxisAlignedBB(0.125, 0.5, 0.125, 0.875, 1, 0.875).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
		}
		else if(pos==27)
			list.add(new AxisAlignedBB(0, 0, 0, 1, 1, 1).contract(facing.getFrontOffsetX()*0.0625, 0, facing.getFrontOffsetZ()*0.0625).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
		else if(pos==28)
			if(facing.getAxis()==Axis.X)
				list.add(new AxisAlignedBB(0.125, 0, 0, 0.875, 0.875, 1)
						.contract(0, 0, facing==EnumFacing.EAST^mirrored?-0.25: 0.25)
						.offset(getPos().getX(), getPos().getY(), getPos().getZ()));
			else
				list.add(new AxisAlignedBB(0, 0, 0.125, 1, 0.875, 0.875)
						.contract(facing==EnumFacing.NORTH^mirrored?-0.25: 0.25, 0, 0)
						.offset(getPos().getX(), getPos().getY(), getPos().getZ()));
		else if(pos==29)
		{
			if(facing.getAxis()==Axis.X)
				list.add(new AxisAlignedBB(0.125, 0, 0, 0.875, 0.875, 1)
						.contract(0, 0, facing==EnumFacing.EAST^mirrored?0.125: -0.125)
						.offset(getPos().getX(), getPos().getY(), getPos().getZ()));
			else
				list.add(new AxisAlignedBB(0, 0, 0.125, 1, 0.875, 0.875)
						.contract(facing==EnumFacing.NORTH^mirrored?0.125: -0.125, 0, 0)
						.offset(getPos().getX(), getPos().getY(), getPos().getZ()));

			list.add(new AxisAlignedBB(0.25, 0.875, 0.25, 0.75, 1, 0.75).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
		}
		else
			list.add(new AxisAlignedBB(0, 0, 0, 1, 1, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));

		return list;
	}

	@Override
	public boolean isOverrideBox(AxisAlignedBB box, EntityPlayer player, RayTraceResult mop, ArrayList<AxisAlignedBB> list)
	{
		return false;
	}

	@Override
	public List<AxisAlignedBB> getAdvancedColisionBounds()
	{
		return getAdvancedSelectionBounds();
	}

	@Override
	public boolean canOpenGui()
	{
		return true;
	}

	@Override
	public int getGuiID()
	{
		return IIGuiList.GUI_PRINTING_PRESS.ordinal();
	}

	@Nullable
	@Override
	public TileEntity getGuiMaster()
	{
		return master();
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		if(pos==27&&capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return true;
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY&&pos==27)
		{
			TileEntityPrintingPress master = master();
			return (T)master.insertionHandler;
		}

		return super.getCapability(capability, facing);
	}

	@Override
	public void onGuiOpened(EntityPlayer player, boolean clientside)
	{
		if(!clientside)
			IIPacketHandler.sendToClient(this, new MessageIITileSync(this));
	}

	@Override
	public EnumFacing[] sigOutputDirections()
	{
		if(pos==2)
			return new EnumFacing[]{mirrored?facing.rotateYCCW(): facing.rotateY()};
		return new EnumFacing[0];
	}

	@Override
	public boolean shoudlPlaySound(String sound)
	{
		return true;
	}
}
