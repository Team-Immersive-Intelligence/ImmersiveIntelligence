package pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.IEApi;
import blusunrize.immersiveengineering.api.crafting.IMultiblockRecipe;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.IConveyorAttachable;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedCollisionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedSelectionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.blocks.wooden.BlockTypes_WoodenDevice0;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import blusunrize.immersiveengineering.common.util.network.MessageTileSync;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.Packer;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeString;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalDevice;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Pabilo8
 * @since 28-06-2019
 */
public class TileEntityPacker extends TileEntityMultiblockMetal<TileEntityPacker, IMultiblockRecipe> implements IDataDevice, IAdvancedCollisionBounds, IAdvancedSelectionBounds, IConveyorAttachable, IGuiTile
{
	//First - the crate to which items are inserted, second - stack to insert, returns the stack if failed
	public static HashMap<Predicate<ItemStack>, Function<Tuple<ItemStack, ItemStack>, ItemStack>> predicates = new HashMap<>();

	static
	{
		// TODO: 30.10.2020 readd loading magazines
		predicates.put(
				stack -> stack.getItem() instanceof ItemBlockIEBase,
				stack ->
				{
					Block block = ((ItemBlockIEBase)stack.getFirst().getItem()).getBlock();
					int s = stack.getFirst().getMetadata();
					if(IEApi.isAllowedInCrate(stack.getSecond())&&block==IEContent.blockWoodenDevice0&&(s==BlockTypes_WoodenDevice0.CRATE.getMeta()||s==BlockTypes_WoodenDevice0.REINFORCED_CRATE.getMeta()))
					{
						if(!ItemNBTHelper.hasKey(stack.getFirst(), "inventory"))
						{
							ItemNBTHelper.getTag(stack.getFirst()).setTag("inventory", Utils.writeInventory(NonNullList.withSize(27, ItemStack.EMPTY)));
						}
						NonNullList<ItemStack> inv = Utils.readInventory(ItemNBTHelper.getTag(stack.getFirst()).getTagList("inventory", 10), 27);
						for(int i = 0; i < 27; i += 1)
						{
							if(inv.get(i).isEmpty())
							{
								inv.set(i, stack.getSecond().copy());
								ItemNBTHelper.getTag(stack.getFirst()).setTag("inventory", Utils.writeInventory(inv));
								return ItemStack.EMPTY;
							}
							else if(ItemHandlerHelper.canItemStacksStack(stack.getFirst(), stack.getSecond()))
							{
								ItemStack s2 = inv.get(i);
								int c = Math.max(0, s2.getMaxStackSize()-(s2.getCount()+stack.getSecond().getCount()));
								s2.setCount(Math.min(s2.getMaxStackSize(), s2.getCount()+stack.getSecond().getCount()));
								inv.set(i, s2.copy());
								ItemNBTHelper.getTag(stack.getFirst()).setTag("inventory", Utils.writeInventory(inv));
								ItemStack s3 = s2.copy();
								s3.setCount(c);
								return s3;
							}
						}
					}
					return stack.getSecond();
				}
		);
		predicates.put(
				stack -> stack.getItem() instanceof ItemBlockIEBase,
				stack ->
				{
					Block block = ((ItemBlockIEBase)stack.getFirst().getItem()).getBlock();
					int s = stack.getFirst().getMetadata();
					if(IEApi.isAllowedInCrate(stack.getSecond())&&block==IIContent.block_metal_device&&(s==IIBlockTypes_MetalDevice.METAL_CRATE.getMeta()))
					{
						if(!ItemNBTHelper.hasKey(stack.getFirst(), "inventory"))
						{
							ItemNBTHelper.getTag(stack.getFirst()).setTag("inventory", Utils.writeInventory(NonNullList.withSize(27, ItemStack.EMPTY)));
						}
						NonNullList<ItemStack> inv = Utils.readInventory(ItemNBTHelper.getTag(stack.getFirst()).getTagList("inventory", 10), 27);
						for(int i = 0; i < 27; i += 1)
						{
							if(inv.get(i).isEmpty())
							{
								inv.set(i, stack.getSecond().copy());
								ItemNBTHelper.getTag(stack.getFirst()).setTag("inventory", Utils.writeInventory(inv));
								return ItemStack.EMPTY;
							}
							else if(ItemHandlerHelper.canItemStacksStack(stack.getFirst(), stack.getSecond()))
							{
								ItemStack s2 = inv.get(i);
								int c = Math.max(0, s2.getMaxStackSize()-(s2.getCount()+stack.getSecond().getCount()));
								s2.setCount(Math.min(s2.getMaxStackSize(), s2.getCount()+stack.getSecond().getCount()));
								inv.set(i, s2.copy());
								ItemNBTHelper.getTag(stack.getFirst()).setTag("inventory", Utils.writeInventory(inv));
								ItemStack s3 = s2.copy();
								s3.setCount(c);
								return s3;
							}
						}
					}
					return stack.getSecond();
				}
		);
		predicates.put(
				stack -> stack.getItem() instanceof ItemBlockIEBase,
				stack ->
				{
					Block block = ((ItemBlockIEBase)stack.getFirst().getItem()).getBlock();
					if(IEApi.isAllowedInCrate(stack.getSecond())&&block==IIContent.block_small_crate)
					{
						if(!ItemNBTHelper.hasKey(stack.getFirst(), "inventory"))
						{
							ItemNBTHelper.getTag(stack.getFirst()).setTag("inventory", Utils.writeInventory(NonNullList.withSize(27, ItemStack.EMPTY)));
						}
						NonNullList<ItemStack> inv = Utils.readInventory(ItemNBTHelper.getTag(stack.getFirst()).getTagList("inventory", 10), 27);
						for(int i = 0; i < 27; i += 1)
						{
							if(inv.get(i).isEmpty())
							{
								inv.set(i, stack.getSecond().copy());
								ItemNBTHelper.getTag(stack.getFirst()).setTag("inventory", Utils.writeInventory(inv));
								return ItemStack.EMPTY;
							}
							else if(ItemHandlerHelper.canItemStacksStack(stack.getFirst(), stack.getSecond()))
							{
								ItemStack s2 = inv.get(i);
								int c = Math.max(0, s2.getMaxStackSize()-(s2.getCount()+stack.getSecond().getCount()));
								s2.setCount(Math.min(s2.getMaxStackSize(), s2.getCount()+stack.getSecond().getCount()));
								inv.set(i, s2.copy());
								ItemNBTHelper.getTag(stack.getFirst()).setTag("inventory", Utils.writeInventory(inv));
								ItemStack s3 = s2.copy();
								s3.setCount(c);
								return s3;
							}
						}
					}
					return stack.getSecond();
				}
		);
	}

	public NonNullList<ItemStack> inventory = NonNullList.withSize(28, ItemStack.EMPTY);
	public int processTime = 0, animation = 0;
	public boolean active = false;
	public boolean ignoreEmptyStacks = false;
	public int packingMode = 0;
	public int itemsToPack = 0;
	public int currentItemsToPack = 0;
	public int insertQuantity = 64;
	IItemHandler crateHandler = new IEInventoryHandler(1, this, 0, true, true);
	IItemHandler inventoryHandler = new IEInventoryHandler(27, this, 1, true, true);

	public TileEntityPacker()
	{
		super(MultiblockPacker.instance, new int[]{3, 3, 6}, Packer.energyCapacity, false);
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		if(!isDummy())
		{
			if(!descPacket)
				inventory = Utils.readInventory(nbt.getTagList("inventory", 10), 28);
			itemsToPack = nbt.getInteger("itemsToPack");
			currentItemsToPack = nbt.getInteger("currentItemsToPack");
			packingMode = nbt.getInteger("packingMode");
			ignoreEmptyStacks = nbt.getBoolean("ignoreEmptyStacks");
			insertQuantity = nbt.getInteger("insertQuantity");

			processTime = nbt.getInteger("processTime");
			animation = nbt.getInteger("animation");

		}
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
				nbt.setTag("inventory", Utils.writeInventory(inventory));
			nbt.setInteger("itemsToPack", itemsToPack);
			nbt.setInteger("currentItemsToPack", currentItemsToPack);
			nbt.setInteger("packingMode", packingMode);
			nbt.setBoolean("ignoreEmptyStacks", ignoreEmptyStacks);
			nbt.setInteger("insertQuantity", insertQuantity);

			nbt.setInteger("processTime", processTime);
			nbt.setInteger("animation", animation);

		}
	}

	@Override
	public void receiveMessageFromServer(NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);
		if(message.hasKey("processTime"))
			this.processTime = message.getInteger("processTime");
		if(message.hasKey("animation"))
			this.animation = message.getInteger("animation");
		if(message.hasKey("active"))
			this.active = message.getBoolean("active");
		if(message.hasKey("inventory"))
			inventory = Utils.readInventory(message.getTagList("inventory", 10), 28);

		if(message.hasKey("itemsToPack"))
			itemsToPack = message.getInteger("itemsToPack");
		if(message.hasKey("currentItemsToPack"))
			currentItemsToPack = message.getInteger("currentItemsToPack");
		if(message.hasKey("packingMode"))
			packingMode = message.getInteger("packingMode");
		if(message.hasKey("ignoreEmptyStacks"))
			ignoreEmptyStacks = message.getBoolean("ignoreEmptyStacks");
		if(message.hasKey("insertQuantity"))
			insertQuantity = message.getInteger("insertQuantity");

		if(message.hasKey("processTime"))
			processTime = message.getInteger("processTime");
		if(message.hasKey("animation"))
			animation = message.getInteger("animation");
	}

	@Override
	public void update()
	{
		super.update();
		if(!isDummy())
		{
			if(!inventory.get(0).isEmpty())
			{
				switch(animation)
				{
					case 0:
					{
						if(!world.isRemote&&processTime==1)
							doGraphicalUpdates(1);
						if(processTime < Packer.conveyorTime)
							processTime += 1;
						else
						{
							animation = 1;
							processTime = 0;
							currentItemsToPack = itemsToPack;
							doGraphicalUpdates(1);
						}
					}
					break;
					case 1:
					{
						if(currentItemsToPack > 0)
						{
							if(energyStorage.getEnergyStored() >= Packer.energyUsage)
							{
								energyStorage.extractEnergy(Packer.energyUsage, false);
								if(processTime >= Packer.timeInsertion)
								{
									processTime = 0;
									currentItemsToPack -= 1;

								}
								else if(processTime > 0)
								{
									processTime += 1;
								}
								else
								{
									switch(packingMode)
									{
										//One by one
										case 0:
										{
											s:
											for(int i = 1; i < inventory.size(); i += 1)
											{
												if(!inventory.get(i).isEmpty())
												{
													ItemStack newStack = inventory.get(i).copy();
													int initialSize = Math.min(newStack.getCount(), insertQuantity);
													int left = inventory.get(i).getCount()-initialSize;
													newStack.setCount(initialSize);
													for(Entry<Predicate<ItemStack>, Function<Tuple<ItemStack, ItemStack>, ItemStack>> p : predicates.entrySet())
														if(p.getKey().test(inventory.get(0)))
														{
															newStack = p.getValue().apply(new Tuple<>(inventory.get(0), newStack));
															newStack.setCount(left+newStack.getCount());
															inventory.set(i, newStack);
															break s;
														}
												}
											}
										}
										break;
										//One of every
										case 1:
										{
											s:
											for(int i = 1; i < inventory.size(); i += 1)
											{
												int j = (i+27-(currentItemsToPack%inventory.size()))%inventory.size();
												if(j==0)
													j = 1;
												ItemStack newStack = inventory.get(j).copy();
												int initialSize = Math.min(newStack.getCount(), insertQuantity);
												int left = inventory.get(j).getCount()-initialSize;
												newStack.setCount(initialSize);
												for(Entry<Predicate<ItemStack>, Function<Tuple<ItemStack, ItemStack>, ItemStack>> p : predicates.entrySet())
													if(p.getKey().test(inventory.get(0)))
													{
														newStack = p.getValue().apply(new Tuple<>(inventory.get(0), newStack));
														newStack.setCount(left+newStack.getCount());
														inventory.set(j, newStack);
														break s;
													}
											}
										}
										break;
									}
									processTime = Packer.timeInsertion;
								}
							}
						}
						else
							animation = 2;
					}
					break;
					case 2:
					{
						if(processTime < Packer.conveyorTime)
							processTime += 1;
						else
						{
							if(!world.isRemote)
							{
								if(Utils.canInsertStackIntoInventory(world.getTileEntity(getBlockPosForPos(14).offset(facing)), inventory.get(0), facing))
								{
									ItemStack left = Utils.insertStackIntoInventory(world.getTileEntity(getBlockPosForPos(14).offset(facing)), inventory.get(0), facing, false);
									inventory.set(0, left);
								}
								else
								{
									Utils.dropStackAtPos(world, getBlockPosForPos(14).offset(facing), inventory.get(0));
									inventory.set(0, ItemStack.EMPTY);
								}
								animation = 0;
								processTime = 0;
								doGraphicalUpdates(1);
							}
						}
					}
					break;
				}
			}
			else
				animation = 0;
		}
	}

	@Override
	public float[] getBlockBounds()
	{
		return new float[]{0, 0, 0, 0, 0, 0};
	}

	@Override
	public int[] getEnergyPos()
	{
		return new int[]{41};
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
		{
			for(Predicate<ItemStack> p : predicates.keySet())
				if(p.test(stack))
					return true;
		}
		return true;
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return slot==0?1: 64;
	}

	@Override
	public int[] getOutputSlots()
	{
		return new int[]{1};
	}

	@Override
	public int[] getOutputTanks()
	{
		return new int[]{};
	}

	@Override
	public boolean additionalCanProcessCheck(MultiblockProcess<IMultiblockRecipe> process)
	{
		return false;
	}

	@Override
	public IFluidTank[] getInternalTanks()
	{
		return new IFluidTank[]{};
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
		return false;
	}

	@Override
	public void doGraphicalUpdates(int slot)
	{
		NBTTagCompound tag = new NBTTagCompound();

		if(slot==1)
		{
			tag.setTag("inventory", Utils.writeInventory(inventory));
			tag.setInteger("itemsToPack", itemsToPack);
			tag.setInteger("packingMode", packingMode);
			tag.setBoolean("ignoreEmptyStacks", ignoreEmptyStacks);
			tag.setInteger("insertQuantity", insertQuantity);
		}

		tag.setInteger("processTime", processTime);
		tag.setInteger("animation", animation);

		ImmersiveEngineering.packetHandler.sendToAllAround(new MessageTileSync(this, tag), pl.pabilo8.immersiveintelligence.api.Utils.targetPointFromTile(this, 32));
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
	public void onSend()
	{

	}

	@Override
	public void onReceive(DataPacket packet, EnumFacing side)
	{
		TileEntityPacker master = master();
		if(master==null)
			return;
		if(this.pos==30&&master.energyStorage.getEnergyStored() >= Packer.energyUsage)
		{
			master.energyStorage.extractEnergy(Packer.energyUsage, false);

			if(packet.getPacketVariable('c') instanceof DataPacketTypeInteger)
				master.itemsToPack = ((DataPacketTypeInteger)packet.getPacketVariable('c')).value;
			if(packet.variables.containsKey('m'))
			{
				if(packet.getPacketVariable('m') instanceof DataPacketTypeInteger)
				{
					master.packingMode = MathHelper.clamp(((DataPacketTypeInteger)packet.getPacketVariable('m')).value, 0, PackingModes.values().length-1);
				}
				else if(packet.getPacketVariable('m') instanceof DataPacketTypeString)
				{
					try
					{
						PackingModes mode = PackingModes.valueOf(((DataPacketTypeString)packet.getPacketVariable('m')).value.toUpperCase());
						master.packingMode = mode.ordinal();
					} catch(IllegalArgumentException e)
					{

					}
				}
			}

			if(packet.getPacketVariable('q') instanceof DataPacketTypeInteger)
				master.insertQuantity = ((DataPacketTypeInteger)packet.getPacketVariable('q')).value;

			if(packet.getPacketVariable('i') instanceof DataPacketTypeBoolean)
				master.ignoreEmptyStacks = ((DataPacketTypeBoolean)packet.getPacketVariable('i')).value;

		}
	}

	@Override
	public List<AxisAlignedBB> getAdvancedSelectionBounds()
	{
		List list = new ArrayList<AxisAlignedBB>();

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
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return master()!=null&&(pos==24||pos==2);
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			TileEntityPacker master = master();
			if(master==null)
				return null;
			if(pos==24)
				return (T)master.inventoryHandler;
			else if(pos==2)
				return (T)master.crateHandler;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public EnumFacing[] sigOutputDirections()
	{
		if(pos==2||pos==14)
			return new EnumFacing[]{facing};
		else if(pos==28)
			return new EnumFacing[]{facing.rotateY()};
		return new EnumFacing[0];
	}

	@Override
	public void onEntityCollision(World world, Entity entity)
	{
		/*if(pos==1&&!world.isRemote&&entity!=null&&!entity.isDead&&entity instanceof EntityItem&&!((EntityItem)entity).getItem().isEmpty() &&pl.pabilo8.immersiveintelligence.api.Utils.getDistanceBetweenPos(entity.getPosition(),this.getPos().offset(facing.getOpposite()),false)==0f)
		{
			ItemStack stack = ((EntityItem)entity).getItem();
			if(stack.isEmpty())
				return;
			if(inventory.get(0).isEmpty() && inventoryHandler.insertItem(0, stack, false).isEmpty())
			{
				((EntityItem)entity).setItem(ItemStack.EMPTY);
				entity.setDead();
				processTime=0;
				animation=60;
			}

		}*/
	}

	@Override
	public boolean canOpenGui()
	{
		return true;
	}

	@Override
	public int getGuiID()
	{
		return IIGuiList.GUI_PACKER.ordinal();
	}

	@Nullable
	@Override
	public TileEntity getGuiMaster()
	{
		return master();
	}

	enum PackingModes
	{
		ONE_BY_ONE,
		ONE_OF_ALL
	}
}
