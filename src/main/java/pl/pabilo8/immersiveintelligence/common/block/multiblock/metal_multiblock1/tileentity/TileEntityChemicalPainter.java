package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.ISoundTile;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.IESounds;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import com.elytradev.mirage.event.GatherLightsEvent;
import com.elytradev.mirage.lighting.ILightEventConsumer;
import com.elytradev.mirage.lighting.Light;
import net.minecraft.client.particle.ParticleRedstone;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import pl.pabilo8.immersiveintelligence.api.crafting.PaintingRecipe;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataHandlingUtils;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeFloat;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.ChemicalPainter;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockChemicalPainter;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IAdvancedBounds;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Pabilo8
 * @since 28-06-2019
 */
@net.minecraftforge.fml.common.Optional.Interface(iface = "com.elytradev.mirage.lighting.ILightEventConsumer", modid = "mirage")
public class TileEntityChemicalPainter extends TileEntityMultiblockMetal<TileEntityChemicalPainter, PaintingRecipe> implements IGuiTile, ISoundTile, IAdvancedBounds, ILightEventConsumer, IDataDevice
{
	private static final Predicate<FluidStack> CYAN = fluidStack -> fluidStack!=null&&fluidStack.getFluid().getName().equals("ink_cyan");
	private static final Predicate<FluidStack> MAGENTA = fluidStack -> fluidStack!=null&&fluidStack.getFluid().getName().equals("ink_magenta");
	private static final Predicate<FluidStack> YELLOW = fluidStack -> fluidStack!=null&&fluidStack.getFluid().getName().equals("ink_yellow");
	private static final Predicate<FluidStack> BLACK = fluidStack -> fluidStack!=null&&fluidStack.getFluid().getName().equals("ink");

	public FluidTank[] tanks =
			{
					new FluidTank(ChemicalPainter.fluidCapacity),
					new FluidTank(ChemicalPainter.fluidCapacity),
					new FluidTank(ChemicalPainter.fluidCapacity),
					new FluidTank(ChemicalPainter.fluidCapacity)
			};
	public NonNullList<ItemStack> inventory = NonNullList.withSize(4, ItemStack.EMPTY);
	public int processTime, processTimeMax;
	public ItemStack effect = ItemStack.EMPTY;
	public boolean active = false;
	public IIColor color = IIColor.fromPackedRGB(0xff00ff);

	IItemHandler insertionHandler = new IEInventoryHandler(1, this, 0, true, false);
	IItemHandler outputHandler = new IEInventoryHandler(1, this, 1, true, false);

	public TileEntityChemicalPainter()
	{
		super(MultiblockChemicalPainter.INSTANCE, MultiblockChemicalPainter.INSTANCE.getSize(), ChemicalPainter.energyCapacity, true);
	}

	@Override
	public void readCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);

		if(isDummy())
			return;

		tanks[0].readFromNBT(nbt.getCompoundTag("tank1"));
		tanks[1].readFromNBT(nbt.getCompoundTag("tank2"));
		tanks[2].readFromNBT(nbt.getCompoundTag("tank3"));
		tanks[3].readFromNBT(nbt.getCompoundTag("tank4"));

		processTime = nbt.getInteger("processTime");
		processTimeMax = nbt.getInteger("processTimeMax");
		effect = new ItemStack(nbt.getCompoundTag("effect"));
		color = IIColor.fromPackedRGB(nbt.getInteger("color"));

		if(!descPacket)
			inventory = Utils.readInventory(nbt.getTagList("inventory", 10), 4);
	}

	@Override
	public void writeCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);

		if(isDummy())
			return;

		nbt.setTag("tank1", tanks[0].writeToNBT(new NBTTagCompound()));
		nbt.setTag("tank2", tanks[1].writeToNBT(new NBTTagCompound()));
		nbt.setTag("tank3", tanks[2].writeToNBT(new NBTTagCompound()));
		nbt.setTag("tank4", tanks[3].writeToNBT(new NBTTagCompound()));

		nbt.setInteger("color", color.getPackedRGB());
		nbt.setInteger("processTime", processTime);
		nbt.setInteger("processTimeMax", processTimeMax);
		nbt.setTag("effect", effect.serializeNBT());

		if(!descPacket)
			nbt.setTag("inventory", Utils.writeInventory(inventory));
	}

	@Override
	public void receiveMessageFromServer(@Nonnull NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);

		if(isDummy())
			return;

		if(message.hasKey("processTime"))
			this.processTime = message.getInteger("processTime");
		if(message.hasKey("processTimeMax"))
			this.processTimeMax = message.getInteger("processTimeMax");
		if(message.hasKey("active"))
			this.active = message.getBoolean("active");

		if(message.hasKey("inventory"))
			inventory = Utils.readInventory(message.getTagList("inventory", 10), 4);

		if(message.hasKey("tank1"))
			tanks[0].readFromNBT(message.getCompoundTag("tank1"));
		if(message.hasKey("tank2"))
			tanks[1].readFromNBT(message.getCompoundTag("tank2"));
		if(message.hasKey("tank3"))
			tanks[2].readFromNBT(message.getCompoundTag("tank3"));
		if(message.hasKey("tank4"))
			tanks[3].readFromNBT(message.getCompoundTag("tank4"));

		if(message.hasKey("output"))
			effect = new ItemStack(message.getCompoundTag("output"));

		if(message.hasKey("color"))
			color = IIColor.fromPackedRGB(message.getInteger("color"));
	}

	@Override
	public void receiveMessageFromClient(NBTTagCompound message)
	{
		super.receiveMessageFromClient(message);
		if(message.hasKey("color"))
			color = IIColor.fromPackedRGB(message.getInteger("color"));
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


		if(active&&processTime < processTimeMax)
			processTime += 1;
		if(world.isRemote)
		{
			TileEntityChemicalPainter t1 = getTileForPos(52);
			TileEntityChemicalPainter t2 = getTileForPos(47);
			TileEntityChemicalPainter t3 = getTileForPos(53);

			// TODO: 16.10.2021 replace 
			ImmersiveEngineering.proxy.handleTileSound(IISounds.chemicalPainterLights, this, shoudlPlaySound("immersiveintelligence:chemical_painter_lights"), 1.5f, 1f);
			if(t1!=null)
				ImmersiveEngineering.proxy.handleTileSound(IESounds.spray, t1, shoudlPlaySound("immersiveengineering:spray"), 1.5f, 0.5f);
			if(t2!=null)
				ImmersiveEngineering.proxy.handleTileSound(IISounds.chemicalPainterLiftUp, t2, shoudlPlaySound("immersiveintelligence:chemical_painter_lift_up"), 1.5f, 0.75f);
			if(t3!=null)
				ImmersiveEngineering.proxy.handleTileSound(IISounds.chemicalPainterLiftDown, t2, shoudlPlaySound("immersiveintelligence:chemical_painter_lift_down"), 1.5f, 0.75f);

			if(active&&shoudlPlaySound("immersiveengineering:spray"))
				spawnPaintParticle();

			return;
		}


		boolean wasActive = active;
		boolean update = false;

		active = shouldRenderAsActive();

		if(energyStorage.getEnergyStored() > 0&&processQueue.size() < this.getProcessQueueMaxLength())
			if(Arrays.stream(tanks).anyMatch(ft -> ft.getFluidAmount() > 0))
			{
				PaintingRecipe recipe = PaintingRecipe.findRecipe(inventory.get(0));
				if(recipe!=null)
				{
					float[] cmyk = color.getCMYK();
					for(int i = 0; i < cmyk.length; i++)
						cmyk[i] *= recipe.getPaintAmount();

					boolean goAheadMrJoestar = true;
					for(int i = 0; i < cmyk.length; i++)
						if(tanks[i].getFluidAmount() < cmyk[i])
						{
							goAheadMrJoestar = false;
							break;
						}

					if(goAheadMrJoestar)
					{
						PaintingProcess process = new PaintingProcess(recipe, 0);
						this.addProcessToQueue(process, false);
						update = true;
						processTime = 0;
						processTimeMax = recipe.getTotalProcessTime();
						effect = recipe.process.apply(color, inventory.get(0).copy());

						for(int i = 0; i < tanks.length; i++)
							tanks[i].drain((int)cmyk[i], true);
					}

				}
			}

		if(processTime >= processTimeMax&&processQueue.size() > 0)
		{
			MultiblockProcess<PaintingRecipe> process = processQueue.get(0);
			if(process instanceof PaintingProcess)
				if(ItemHandlerHelper.insertItemStacked(outputHandler, process.recipe.getActualItemOutputs(this).get(0), true).isEmpty())
					((PaintingProcess)process).processFinish(this);
		}

		if(active)
			if(processTime==Math.ceil(0.1*processTimeMax))
				world.playSound(null, getBlockPosForPos(70), IISounds.vulcanizerPullStart, SoundCategory.BLOCKS, .65F, 1.5f);
			else if(processTime==Math.ceil(0.2*processTimeMax))
				world.playSound(null, getBlockPosForPos(70), IISounds.vulcanizerPullEnd, SoundCategory.BLOCKS, .65F, 1.5f);
			else if(processTime==Math.ceil(0.7*processTimeMax))
				world.playSound(null, getBlockPosForPos(70), IISounds.vulcanizerPullStart, SoundCategory.BLOCKS, .65F, 1.5f);
			else if(processTime==Math.ceil(0.8*processTimeMax))
				world.playSound(null, getBlockPosForPos(70), IISounds.vulcanizerPullEnd, SoundCategory.BLOCKS, .65F, 1.5f);

		IIUtils.handleBucketTankInteraction(tanks, inventory, 2, 3, 0, false, CYAN);
		IIUtils.handleBucketTankInteraction(tanks, inventory, 2, 3, 1, false, MAGENTA);
		IIUtils.handleBucketTankInteraction(tanks, inventory, 2, 3, 2, false, YELLOW);
		IIUtils.handleBucketTankInteraction(tanks, inventory, 2, 3, 3, false, BLACK);

		if(world.getTotalWorldTime()%20==0)
		{
			BlockPos pos = getBlockPosForPos(25).offset(mirrored?facing.rotateY(): facing.rotateYCCW());
			ItemStack output = inventory.get(1);
			TileEntity inventoryTile = this.world.getTileEntity(pos);
			if(inventoryTile!=null)
				output = Utils.insertStackIntoInventory(inventoryTile, output, mirrored?facing.rotateY(): facing.rotateYCCW());
			inventory.set(1, output);
		}

		if(update||wasActive!=active)
			IIPacketHandler.sendToClient(this, new MessageIITileSync(this, EasyNBT.newNBT()
					.withInt("color", color.getPackedRGB())
					.withInt("processTime", processTime)
					.withInt("processTimeMax", processTimeMax)
					.withBoolean("active", this.active)
					.withTag("inventory", Utils.writeInventory(inventory))
					.withItemStack("output", effect)
					.withTag("tank1", tanks[0].writeToNBT(new NBTTagCompound()))
					.withTag("tank2", tanks[1].writeToNBT(new NBTTagCompound()))
					.withTag("tank3", tanks[2].writeToNBT(new NBTTagCompound()))
					.withTag("tank4", tanks[3].writeToNBT(new NBTTagCompound()))
			));
	}

	@SideOnly(Side.CLIENT)
	private void spawnPaintParticle()
	{
		Vec3d pos = new Vec3d(getBlockPosForPos(52)).addVector(0.5, 0.5, 0.5);
		Vec3d facing = new Vec3d(getFacing().getOpposite().getDirectionVec());
		facing = facing.scale(0.65f);

		float mod = (float)(Math.random()*2f);
		float[] rgb = color.getFloatRGB();
		float ff = (getWorld().getTotalWorldTime()%200)/200f;
		float ny = (-0.275f+((Math.abs(((ff%0.2f)/0.2f)-0.5f)*2f)*0.55f));
		float nx = ((Math.abs(((ff%0.33f)/0.33f)-0.5f)*2f)*0.55f);

		facing = facing.add(facing.rotateYaw(90).scale(ny)).addVector(0, -nx, 0);

		for(int i = 2; i < 4; i++)
		{
			Vec3d vv = facing.scale(i*0.5);
			ParticleRedstone particle = (ParticleRedstone)ClientUtils.mc().effectRenderer.spawnEffectParticle(EnumParticleTypes.REDSTONE.getParticleID(), pos.x+vv.x, pos.y+vv.y, pos.z+vv.z, facing.x, facing.y, facing.z);
			if(particle!=null)
			{
				particle.setMaxAge(6);
				particle.reddustParticleScale = 3.25f;
				particle.setRBGColorF(rgb[0]*mod, rgb[1]*mod, rgb[2]*mod);
			}
		}
	}

	@Nonnull
	@Override
	public float[] getBlockBounds()
	{
		return new float[0];
	}

	@Nonnull
	@Override
	public int[] getEnergyPos()
	{
		return new int[]{47};
	}

	@Nonnull
	@Override
	public int[] getRedstonePos()
	{
		return new int[]{14};
	}

	@Override
	public boolean isInWorldProcessingMachine()
	{
		return true;
	}

	@Override
	public boolean additionalCanProcessCheck(@Nonnull MultiblockProcess<PaintingRecipe> process)
	{
		return true;
	}

	@Override
	public void doProcessOutput(@Nonnull ItemStack output)
	{
		BlockPos pos = getBlockPosForPos(25).offset(mirrored?facing.rotateY(): facing.rotateYCCW());
		TileEntity inventoryTile = this.world.getTileEntity(pos);
		if(inventoryTile!=null)
			output = Utils.insertStackIntoInventory(inventoryTile, output, mirrored?facing.rotateY(): facing.rotateYCCW());
		if(!output.isEmpty())
			Utils.dropStackAtPos(world, pos, output, facing);
	}

	@Override
	public void doProcessFluidOutput(@Nonnull FluidStack output)
	{
	}

	@Override
	public void onProcessFinish(@Nonnull MultiblockProcess<PaintingRecipe> process)
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
	public float getMinProcessDistance(@Nonnull MultiblockProcess<PaintingRecipe> process)
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
		return true;
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return 64;
	}

	@Nonnull
	@Override
	public int[] getOutputSlots()
	{
		return new int[]{1};
	}

	@Nonnull
	@Override
	public int[] getOutputTanks()
	{
		return new int[]{};
	}

	@Nonnull
	@Override
	public IFluidTank[] getInternalTanks()
	{
		return tanks;
	}

	@Nonnull
	@Override
	protected IFluidTank[] getAccessibleFluidTanks(@Nonnull EnumFacing side)
	{
		TileEntityChemicalPainter master = this.master();
		if(master!=null)
			switch(pos)
			{
				case 19:
					return new FluidTank[]{master.tanks[0]};
				case 18:
					return new FluidTank[]{master.tanks[1]};
				case 16:
					return new FluidTank[]{master.tanks[2]};
				case 15:
					return new FluidTank[]{master.tanks[3]};
			}
		return new FluidTank[0];
	}

	@Override
	protected boolean canFillTankFrom(int iTank, @Nonnull EnumFacing side, @Nonnull FluidStack resource)
	{
		if((side==facing))
		{
			TileEntityChemicalPainter master = this.master();
			if(master==null)
				return false;

			switch(pos)
			{
				case 19:
					return master.tanks[0].getFluidAmount() < master.tanks[0].getCapacity()&&CYAN.test(resource);
				case 18:
					return master.tanks[1].getFluidAmount() < master.tanks[1].getCapacity()&&MAGENTA.test(resource);
				case 16:
					return master.tanks[2].getFluidAmount() < master.tanks[2].getCapacity()&&YELLOW.test(resource);
				case 15:
					return master.tanks[3].getFluidAmount() < master.tanks[3].getCapacity()&&BLACK.test(resource);
				default:
					return false;
			}

		}
		return false;
	}

	@Override
	protected boolean canDrainTankFrom(int iTank, @Nonnull EnumFacing side)
	{
		return false;
	}

	@Override
	public void doGraphicalUpdates(int slot)
	{
		this.markDirty();
		this.markContainingBlockForUpdate(null);
	}

	@Nullable
	@Override
	public PaintingRecipe findRecipeForInsertion(@Nonnull ItemStack inserting)
	{
		return PaintingRecipe.findRecipe(inserting);
	}

	@Nullable
	@Override
	protected PaintingRecipe readRecipeFromNBT(@Nonnull NBTTagCompound tag)
	{
		return PaintingRecipe.loadFromNBT(tag);
	}

	@Override
	public void onGuiOpened(EntityPlayer player, boolean clientside)
	{
		if(!clientside)
			IIPacketHandler.sendToClient(this, new MessageIITileSync(this, EasyNBT.newNBT()
					.withInt("color", color.getPackedRGB())
					.withInt("processTime", processTime)
					.withInt("processTimeMax", processTimeMax)
			));
	}

	@Override
	public boolean canOpenGui()
	{
		return formed;
	}

	@Override
	public int getGuiID()
	{
		return IIGuiList.GUI_CHEMICAL_PAINTER.ordinal();
	}

	@Override
	public TileEntity getGuiMaster()
	{
		return master();
	}

	@Override
	public boolean shoudlPlaySound(@Nonnull String sound)
	{
		TileEntityChemicalPainter master = master();
		if(master!=null&&master.processQueue.size() > 0)
			switch(sound)
			{
				case "immersiveintelligence:chemical_painter_lights":
					return IIMath.inRange(master.processTime, master.processTimeMax, 0.65f, 0.95f);
				case "immersiveengineering:spray":
					return IIMath.inRange(master.processTime, master.processTimeMax, 0.25, 0.75);
				case "immersiveintelligence:chemical_painter_lift_up":
					return IIMath.inRange(master.processTime, master.processTimeMax, 0.05, 0.2);
				case "immersiveintelligence:chemical_painter_lift_down":
					return IIMath.inRange(master.processTime, master.processTimeMax, 0.7, 0.85);
			}

		return false;
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
	{
		if(pos==29&&capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY&&facing==(mirrored?this.facing.rotateYCCW(): this.facing.rotateY()))
			return true;
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY&&pos==29)
		{
			TileEntityChemicalPainter master = master();
			return (T)master.insertionHandler;
		}

		return super.getCapability(capability, facing);
	}

	@Nonnull
	@Override
	public List<AxisAlignedBB> getBounds(boolean collision)
	{
		ArrayList<AxisAlignedBB> list = new ArrayList<>();
		switch(pos)
		{
			case 43:
			case 42:
			case 41:
			{
				switch(facing)
				{
					case NORTH:
						list.add(new AxisAlignedBB(0, 0, 0, 1, 0.5, 0.5).offset(getPos()));
						break;
					case SOUTH:
						list.add(new AxisAlignedBB(0, 0, 0.5, 1, 0.5, 1).offset(getPos()));
						break;
					case EAST:
						list.add(new AxisAlignedBB(0.5, 0, 0, 1, 0.5, 1).offset(getPos()));
						break;
					case WEST:
						list.add(new AxisAlignedBB(0, 0, 0, 0.5, 0.5, 1).offset(getPos()));
						break;
				}
			}
			break;
			case 35:
			case 36:
			case 38:
			case 39:
				list.add(new AxisAlignedBB(0.0625, 0, 0.0625, 1-0.0625, 1, 1-0.0625).offset(getPos()));
				break;
			case 55:
			case 56:
			case 58:
			case 59:
				list.add(new AxisAlignedBB(0.0625, 0, 0.0625, 1-0.0625, 0.625, 1-0.0625).offset(getPos()));
				break;

		}
		return list;
	}

	@Override
	public void onReceive(DataPacket packet, @Nullable EnumFacing side)
	{
		TileEntityChemicalPainter master = master();
		if(pos==10&&master!=null)
		{
			DataType c = packet.getPacketVariable('c');
			DataType p = packet.getPacketVariable('p');

			if(c.toString().equals("callback"))
			{
				DataPacket callback = IIDataHandlingUtils.handleCallback(packet, var -> {
					switch(var)
					{
						case "get_energy":
							return new DataTypeInteger(master.energyStorage.getEnergyStored());
						case "get_progress":
							return new DataTypeFloat(master.processTime/(float)master.processTimeMax);
						case "get_ink_black":
							return new DataTypeInteger(master.tanks[0].getFluidAmount());
						case "get_ink_cyan":
							return new DataTypeInteger(master.tanks[1].getFluidAmount());
						case "get_ink_yellow":
							return new DataTypeInteger(master.tanks[2].getFluidAmount());
						case "get_ink_magenta":
							return new DataTypeInteger(master.tanks[3].getFluidAmount());
						case "get_color":
							return new DataTypeInteger(master.color.getPackedRGB());
					}
					return null;
				});
				//TODO: 09.10.2024 change to use sendData after moving to new system
				IIDataHandlingUtils.sendPacketAdjacently(callback, world, getBlockPosForPos(10), mirrored?facing.rotateY(): facing.rotateYCCW());
			}
			else if(p instanceof DataTypeInteger)
				master.color = IIColor.fromPackedRGB(MathHelper.clamp(((DataTypeInteger)p).value, 0, 0xffffff));
			else if(p instanceof DataTypeString)
				master.color = IIColor.fromHex(((DataTypeString)p).value);

		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	@Optional.Method(modid = "mirage")
	public void gatherLights(GatherLightsEvent gatherLightsEvent)
	{
		if(pos==47)
		{
			TileEntityChemicalPainter master = master();
			if(master!=null&&master.active)
				if(IIMath.inRange(master.processTime, master.processTimeMax, 0.65f, 0.95f))
					gatherLightsEvent.add(Light.builder().pos(getPos()).color(0.8235294f, 0.20392157f, 0.92156863f).radius(5f).build());
		}
	}

	public static class PaintingProcess extends MultiblockProcessInMachine<PaintingRecipe>
	{
		public PaintingProcess(PaintingRecipe recipe, int... inputSlots)
		{
			super(recipe, inputSlots);
		}

		@Override
		public void processFinish(TileEntityMultiblockMetal multiblock)
		{
			super.processFinish(multiblock);
		}
	}
}
