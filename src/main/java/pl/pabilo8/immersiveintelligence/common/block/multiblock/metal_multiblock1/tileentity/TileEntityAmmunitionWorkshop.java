package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedCollisionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedSelectionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.ISoundTile;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.AmmunitionWorkshop;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumFuseTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmo;
import pl.pabilo8.immersiveintelligence.api.crafting.AmmunitionWorkshopRecipe;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IDataDevice;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockAmmunitionWorkshop;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8
 * @since 04.03.2021
 */
public class TileEntityAmmunitionWorkshop extends TileEntityMultiblockMetal<TileEntityAmmunitionWorkshop, AmmunitionWorkshopRecipe> implements ISoundTile, IGuiTile, IAdvancedSelectionBounds, IAdvancedCollisionBounds, IDataDevice
{
	public EnumFuseTypes fuse = EnumFuseTypes.CONTACT;
	public int fuseConfig = 0; //dependent on fuse type: time for timed fuse, distance for proximity fuse

	public NonNullList<ItemStack> inventory = NonNullList.withSize(2, ItemStack.EMPTY); //core, casing
	IItemHandler coreInputHandler = new IEInventoryHandler(1, this, 0, true, false); //pos 18
	IItemHandler casingInputHandler = new IEInventoryHandler(1, this, 1, true, false); //pos 20

	public int processTime = 0, processTimeMax = 0;
	public ItemStack effect = ItemStack.EMPTY;
	public boolean active = false;

	//required calculation, so pre-calculated here
	private static final AxisAlignedBB[] BLOCKER_AABB = new AxisAlignedBB[4];

	static
	{
		for(int i = 0; i < 4; i++)
		{
			EnumFacing f = EnumFacing.getHorizontal(i);

			Vec3i vv = f.getDirectionVec();
			Vec3i vz = (f.rotateY().getAxisDirection()==AxisDirection.POSITIVE?f.rotateY(): f.rotateYCCW()).getDirectionVec();

			AxisAlignedBB aabb = new AxisAlignedBB(0, 0, 0, 0, 0.625, 0)
					.expand(vz.getX(), vz.getY(), vz.getZ())
					.expand(vv.getX()*0.25, vv.getY()*0.25, vv.getZ()*0.25);

			if(f.getAxisDirection()==AxisDirection.NEGATIVE)
				aabb = aabb.offset(new Vec3d(f.getDirectionVec()).scale(-1));

			BLOCKER_AABB[i] = aabb;
		}
	}

	public TileEntityAmmunitionWorkshop()
	{
		super(MultiblockAmmunitionWorkshop.INSTANCE, MultiblockAmmunitionWorkshop.INSTANCE.getSize(), AmmunitionWorkshop.energyCapacity, true);
	}

	@Override
	public void update()
	{
		super.update();

		if(isDummy()||isRSDisabled())
			return;

		if(processTime < processTimeMax)
			processTime += 1;
		if(world.isRemote)
		{
			return;
		}
		boolean wasActive = active;
		boolean update = false;

		active = shouldRenderAsActive();

		if(energyStorage.getEnergyStored() > 0&&processQueue.size() < this.getProcessQueueMaxLength())
		{
			AmmunitionWorkshopRecipe recipe = AmmunitionWorkshopRecipe.findRecipe(inventory.get(0), inventory.get(1));
			if(recipe!=null)
			{
				AmmoProductionProcess process = new AmmoProductionProcess(recipe, 0, 1);
				this.addProcessToQueue(process, false);
				update = true;
				processTime = 0;
				processTimeMax = recipe.getTotalProcessTime();
				effect = recipe.process.apply(inventory.get(0).copy(), inventory.get(1).copy());
				if(effect.getItem() instanceof IAmmo)
				{
					((IAmmo)effect.getItem()).setFuseType(effect, fuse);
					((IAmmo)effect.getItem()).setFuseParameter(effect, fuseConfig);
				}
			}
		}

		if(processTime >= processTimeMax&&processQueue.size() > 0)
		{
			MultiblockProcess<AmmunitionWorkshopRecipe> process = processQueue.get(0);
			if(process instanceof AmmoProductionProcess)
			{
				outputItem(effect);
				effect = ItemStack.EMPTY;
				((AmmoProductionProcess)process).processFinish(this);
				processTime = 0;
				processTimeMax = 0;
			}
			else
			{
				processQueue.remove(process);
				processQueue.add(processQueue.size(), new AmmoProductionProcess(process.recipe, 0, 1));
			}
		}

		if(active)
		{

			/*if(processTime==Math.ceil(0.1*processTimeMax))
				world.playSound(null, getBlockPosForPos(70), IISounds.vulcanizer_pull_start, SoundCategory.BLOCKS, .65F, 1.5f);
			else if(processTime==Math.ceil(0.2*processTimeMax))
				world.playSound(null, getBlockPosForPos(70), IISounds.vulcanizer_pull_end, SoundCategory.BLOCKS, .65F, 1.5f);
			else if(processTime==Math.ceil(0.7*processTimeMax))
				world.playSound(null, getBlockPosForPos(70), IISounds.vulcanizer_pull_start, SoundCategory.BLOCKS, .65F, 1.5f);
			else if(processTime==Math.ceil(0.8*processTimeMax))
				world.playSound(null, getBlockPosForPos(70), IISounds.vulcanizer_pull_end, SoundCategory.BLOCKS, .65F, 1.5f);*/
		}

		if(update||wasActive!=active)
		{
			IIPacketHandler.sendToClient(this, new MessageIITileSync(this, EasyNBT.newNBT()
					.withString("fuse", fuse.getName())
					.withInt("fuse_config", fuseConfig)
					.withInt("process_time", processTime)
					.withInt("process_time_max", processTimeMax)
					.withTag("effect", effect.serializeNBT())
			));
		}
	}

	private void outputItem(ItemStack stack)
	{
		EnumFacing ff = this.facing;
		BlockPos pp = getBlockPosForPos(34).offset(ff);
		TileEntity inventoryTile = this.world.getTileEntity(pp);
		if(inventoryTile!=null)
			stack = Utils.insertStackIntoInventory(inventoryTile, stack, ff.getOpposite());
		if(!stack.isEmpty())
			Utils.dropStackAtPos(world, pp, stack);
	}

	@Override
	public void readCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);

		if(isDummy())
			return;

		this.fuse = EnumFuseTypes.v(nbt.getString("fuse"));
		this.fuseConfig = nbt.getInteger("fuse_config");
		this.inventory = Utils.readInventory(nbt.getTagList("inventory", 10), inventory.size());
		this.processTime = nbt.getInteger("process_time");
		this.processTimeMax = nbt.getInteger("process_time_max");
		this.effect = new ItemStack(nbt.getCompoundTag("effect"));
		this.active = nbt.getBoolean("active");

	}

	@Override
	public void writeCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);

		if(isDummy())
			return;

		nbt.setString("fuse", fuse.getName());
		nbt.setInteger("fuse_config", fuseConfig);
		nbt.setTag("inventory", Utils.writeInventory(inventory));
		nbt.setInteger("process_time", processTime);
		nbt.setInteger("process_time_max", processTimeMax);
		nbt.setTag("effect", effect.serializeNBT());
		nbt.setBoolean("active", active);

	}

	@Override
	public void receiveMessageFromServer(@Nonnull NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);

		if(isDummy())
			return;

		if(message.hasKey("fuse"))
			this.fuse = EnumFuseTypes.v(message.getString("fuse"));
		if(message.hasKey("fuse_config"))
			this.fuseConfig = message.getInteger("fuse_config");
	}

	@Override
	public void receiveMessageFromClient(NBTTagCompound message)
	{
		super.receiveMessageFromClient(message);

		if(isDummy())
			return;

		if(message.hasKey("fuse"))
			this.fuse = EnumFuseTypes.v(message.getString("fuse"));
		if(message.hasKey("fuse_config"))
			this.fuseConfig = message.getInteger("fuse_config");
	}

	@Override
	protected AmmunitionWorkshopRecipe readRecipeFromNBT(@Nonnull NBTTagCompound tag)
	{
		return AmmunitionWorkshopRecipe.loadFromNBT(tag);
	}

	@Nonnull
	@Override
	public int[] getEnergyPos()
	{
		return new int[]{50};
	}

	@Nonnull
	@Override
	public int[] getRedstonePos()
	{
		return new int[]{14};
	}

	@Nonnull
	@Override
	public IFluidTank[] getInternalTanks()
	{
		return new IFluidTank[0];
	}

	@Nonnull
	@SuppressWarnings("MethodsReturnNonNullByDefault")
	@Override
	public AmmunitionWorkshopRecipe findRecipeForInsertion(@Nonnull ItemStack inserting)
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

	@Override
	public boolean additionalCanProcessCheck(@Nonnull MultiblockProcess<AmmunitionWorkshopRecipe> process)
	{
		return true;
	}

	@Override
	public void doProcessOutput(@Nonnull ItemStack stack)
	{

	}

	@Override
	public void doProcessFluidOutput(@Nonnull FluidStack output)
	{

	}

	@Override
	public void onProcessFinish(@Nonnull MultiblockProcess<AmmunitionWorkshopRecipe> process)
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
	public float getMinProcessDistance(@Nonnull MultiblockProcess<AmmunitionWorkshopRecipe> process)
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
		return inventory;
	}

	@Override
	public boolean isStackValid(int i, ItemStack stack)
	{
		if(i==0)
			return stack.getItem() instanceof IAmmo&&((IAmmo)stack.getItem()).isBulletCore(stack);
		else if(i==1)
			return AmmunitionWorkshopRecipe.recipeList.stream().anyMatch(a -> a.casingInput.matchesItemStackIgnoringSize(stack));
		return false;
	}

	@Override
	public int getSlotLimit(int i)
	{
		return 1;
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
		TileEntityAmmunitionWorkshop master = master();
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
	public void onGuiOpened(EntityPlayer player, boolean clientside)
	{
		if(!clientside)
			IIPacketHandler.sendToClient(this, new MessageIITileSync(this));
	}

	@Override
	public boolean canOpenGui()
	{
		return true;
	}

	@Override
	public int getGuiID()
	{
		return IIGuiList.GUI_AMMUNITION_WORKSHOP.ordinal();
	}

	@Nullable
	@Override
	public TileEntity getGuiMaster()
	{
		return master();
	}

	@Override
	public List<AxisAlignedBB> getAdvancedColisionBounds()
	{
		return getAdvancedSelectionBounds();
	}

	@Override
	public List<AxisAlignedBB> getAdvancedSelectionBounds()
	{
		ArrayList<AxisAlignedBB> list = new ArrayList<>();

		switch(pos)
		{
			case 18:
			case 21:
			case 24:
			case 22:
			case 25:
			case 28:
			case 31:
			case 34:
			case 20:
			case 23:
				list.add(new AxisAlignedBB(0, 0, 0, 1, 0.125, 1).offset(getPos()));
				break;
			case 44:
			case 47:
				list.add(new AxisAlignedBB(0.25, -0.5, 0.25, 0.75, 0.25, 0.75).offset(getPos()));
				break;
			case 29:
			case 26:
				list.add(new AxisAlignedBB(0.25, 0.375, 0.25, 0.75, 1.25, 0.75).offset(getPos()));
				list.add(new AxisAlignedBB(0.125, 0, 0.125, 0.875, 0.375, 0.875).offset(getPos()));
				break;
			case 35:
				list.add(new AxisAlignedBB(0, 0, 0, 1, 0.375, 1).offset(getPos())
						.offset(new Vec3d(facing.getDirectionVec()).scale(0.25f))
						.offset(new Vec3d((mirrored?facing.rotateYCCW(): facing.rotateY()).getDirectionVec()).scale(0.125f))
				);
				break;
			case 33:
				list.add(new AxisAlignedBB(0, 0, 0, 1, 1.125, 1).offset(getPos()));
				break;
			case 27:
				list.add(new AxisAlignedBB(0.125, 0, 0.125, 0.875, 0.375, 0.875).offset(getPos())
						.offset(new Vec3d(facing.getDirectionVec()).scale(0.25f))
				);
				list.add(BLOCKER_AABB[facing.getHorizontalIndex()].offset(getPos()));

				break;
			case 30:
				list.add(new AxisAlignedBB(0.125, 0, 0.125, 0.875, 0.375, 0.875).offset(getPos())
						.offset(new Vec3d(facing.getDirectionVec()).scale(0.125f))
				);
				break;
		}

		return list;
	}

	@Override
	public boolean isOverrideBox(AxisAlignedBB box, EntityPlayer player, RayTraceResult mop, ArrayList<AxisAlignedBB> list)
	{
		return false;
	}

	@Override
	public void onReceive(DataPacket packet, @Nullable EnumFacing side)
	{
		if(pos==8)
		{
			TileEntityAmmunitionWorkshop master = master();
			if(master==null)
				return;

			if(packet.hasVariable('f'))
				master.fuse = EnumFuseTypes.v(packet.getPacketVariable('f').valueToString());
		}
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		TileEntityAmmunitionWorkshop master = master();
		if(master!=null&&capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			if(pos==20&&facing==(this.facing.getOpposite()))
			{
				return true;
			}
			else if(pos==18&&facing==(this.facing.getOpposite()))
			{
				return true;
			}
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		TileEntityAmmunitionWorkshop master = master();
		if(master!=null&&capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			if(pos==20&&facing==(this.facing.getOpposite()))
			{
				return ((T)master.coreInputHandler);
			}
			else if(pos==18&&facing==(this.facing.getOpposite()))
			{
				return ((T)master.casingInputHandler);
			}
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void onEntityCollision(World world, Entity entity)
	{
		if((pos==18||pos==20)&&!world.isRemote&&entity instanceof EntityItem&&!entity.isDead)
		{
			EntityItem ei = (EntityItem)entity;
			if(ei.getItem().isEmpty())
				return;

			TileEntityAmmunitionWorkshop master = master();
			if(master==null)
				return;
			ItemStack stack = ei.getItem();
			if(stack.isEmpty())
				return;

			if(pos==20)
				stack = master.coreInputHandler.insertItem(0, stack, false);
			else
				stack = master.casingInputHandler.insertItem(0, stack, false);

			ei.setItem(stack);
			if(stack.getCount() <= 0)
				entity.setDead();
		}
	}

	public static class AmmoProductionProcess extends MultiblockProcessInMachine<AmmunitionWorkshopRecipe>
	{
		public AmmoProductionProcess(AmmunitionWorkshopRecipe recipe, int... inputSlots)
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
