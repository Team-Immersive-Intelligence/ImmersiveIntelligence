package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity;

import blusunrize.immersiveengineering.api.tool.ConveyorHandler.IConveyorAttachable;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedCollisionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedSelectionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityConveyorBelt;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleRedstone;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Filler;
import pl.pabilo8.immersiveintelligence.api.crafting.DustStack;
import pl.pabilo8.immersiveintelligence.api.crafting.DustUtils;
import pl.pabilo8.immersiveintelligence.api.crafting.FillerRecipe;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockFiller;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Pabilo8
 * @since 04.03.2021
 */
public class TileEntityFiller extends TileEntityMultiblockMetal<TileEntityFiller, FillerRecipe> implements IConveyorAttachable, IGuiTile, IAdvancedCollisionBounds, IAdvancedSelectionBounds
{
	public DustStack dustStorage = DustStack.getEmptyStack();
	public final int dustCapacity = Filler.dustCapacity;
	NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);

	IItemHandler insertionHandlerDust = new IEInventoryHandler(1, this, 0, true, false);
	IItemHandler insertionHandlerContainer = new MultiblockInventoryHandler_DirectProcessing(this);


	public TileEntityFiller()
	{
		super(MultiblockFiller.INSTANCE, MultiblockFiller.INSTANCE.getSize(), Filler.energyCapacity, true);
	}

	@Override
	public void update()
	{
		super.update();
		if(isDummy()||isRSDisabled())
			return;

		if(dustStorage.amount < dustCapacity&&world.getTotalWorldTime()%4==0&&!inventory.get(0).isEmpty())
		{
			ItemStack copy = inventory.get(0).copy();
			copy.setCount(1);
			DustStack dustStack = DustUtils.fromItemStack(copy);
			if(!dustStack.isEmpty()&&dustStorage.mergeWith(dustStack).amount < dustCapacity&&(dustStorage.isEmpty()||dustStorage.canMergeWith(dustStack)))
			{
				inventory.get(0).shrink(1);
				dustStorage = dustStorage.mergeWith(dustStack);
			}
		}

		if(world.isRemote)
		{
			for(MultiblockProcess<FillerRecipe> process : processQueue)
			{
				float tick = 1/(float)process.maxTicks;
				float transportTime = 52.5f/120f;
				float fProcess = process.processTick*tick;
				if(fProcess >= transportTime&&fProcess < 1f-transportTime)
				{
					spawnDustParticle(process);
					break;
				}
			}
			return;
		}

		if(world.getTotalWorldTime()%20==0)
			IIPacketHandler.sendToClient(this, new MessageIITileSync(this, EasyNBT.newNBT().withTag("dustStorage", dustStorage.serializeNBT())));

		for(MultiblockProcess<FillerRecipe> process : processQueue)
		{
			float tick = 1/(float)process.maxTicks;
			float transportTime = 52.5f/120f;
			float pressTime = 3.75f/120f;
			float fProcess = process.processTick*tick;
			if(fProcess >= (transportTime+pressTime)&&fProcess < (transportTime+pressTime+tick))
				world.playSound(null, getPos(), SoundEvents.BLOCK_SAND_PLACE, SoundCategory.BLOCKS, .3F, 1);
		}
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);

		if(isDummy())
			return;

		if(!descPacket)
		{
			dustStorage = new DustStack(nbt.getCompoundTag("dustStorage"));
			inventory = Utils.readInventory(nbt.getTagList("inventory", 10), inventory.size());
		}
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);

		if(isDummy())
			return;

		if(!descPacket)
		{
			nbt.setTag("dustStorage", dustStorage.serializeNBT());
			nbt.setTag("inventory", Utils.writeInventory(inventory));
		}
	}

	@Override
	public void receiveMessageFromServer(NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);

		if(isDummy())
			return;

		if(message.hasKey("dustStorage"))
			dustStorage = new DustStack(message.getCompoundTag("dustStorage"));
		if(message.hasKey("inventory"))
			inventory = Utils.readInventory(message.getTagList("inventory", 10), inventory.size());
	}

	@Override
	protected FillerRecipe readRecipeFromNBT(NBTTagCompound tag)
	{
		return FillerRecipe.loadFromNBT(tag);
	}

	@Override
	public int[] getEnergyPos()
	{
		return new int[]{21};
	}

	@Override
	public int[] getRedstonePos()
	{
		return new int[0];
	}

	@Override
	public IFluidTank[] getInternalTanks()
	{
		return null;
	}

	@Override
	public FillerRecipe findRecipeForInsertion(ItemStack inserting)
	{
		return FillerRecipe.findRecipe(inserting, dustStorage);
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
	public boolean additionalCanProcessCheck(MultiblockProcess<FillerRecipe> process)
	{
		return true;
	}

	@Override
	public void doProcessOutput(ItemStack output)
	{
		BlockPos pos = getBlockPosForPos(11).offset(getOutFacing());
		TileEntity inventoryTile = this.world.getTileEntity(pos);
		if(inventoryTile!=null)
			output = Utils.insertStackIntoInventory(inventoryTile, output, getOutFacing().getOpposite());
		if(!output.isEmpty())
			Utils.dropStackAtPos(world, pos, output, facing);
	}

	@Override
	public void doProcessFluidOutput(FluidStack output)
	{

	}

	@Override
	public void onProcessFinish(MultiblockProcess<FillerRecipe> process)
	{

	}

	@Override
	public int getMaxProcessPerTick()
	{
		return 5;
	}

	@Override
	public int getProcessQueueMaxLength()
	{
		return 5;
	}

	@Override
	public float getMinProcessDistance(MultiblockProcess<FillerRecipe> process)
	{
		return process.maxTicks/320f;
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
		return DustUtils.isDustStack(itemStack);
	}

	@Override
	public int getSlotLimit(int i)
	{
		return 64;
	}

	@Override
	public void doGraphicalUpdates(int i)
	{

	}

	@Override
	public void replaceStructureBlock(BlockPos pos, IBlockState state, ItemStack stack, int h, int l, int w)
	{
		super.replaceStructureBlock(pos, state, stack, h, l, w);
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TileEntityConveyorBelt)
			((TileEntityConveyorBelt)tile).setFacing(getOutFacing());
	}

	private EnumFacing getOutFacing()
	{
		return this.mirrored?this.facing.rotateYCCW(): this.facing.rotateY();
	}

	@Override
	public void onEntityCollision(World world, Entity entity)
	{
		if((pos==24||pos==9)&&!world.isRemote&&entity!=null&&!entity.isDead&&entity instanceof EntityItem&&!((EntityItem)entity).getItem().isEmpty())
		{
			TileEntityFiller master = master();
			if(master==null)
				return;
			ItemStack stack = ((EntityItem)entity).getItem();
			if(stack.isEmpty())
				return;

			if(pos==24)
			{
				stack = master.insertionHandlerDust.insertItem(0, stack, false);

				((EntityItem)entity).setItem(stack);
				if(stack.getCount() <= 0)
					entity.setDead();
			}
			else if(pos==9)
			{
				FillerRecipe recipe = master.findRecipeForInsertion(stack);
				if(recipe==null)
					return;
				ItemStack displayStack = recipe.getDisplayStack(stack);
				float transformationPoint = 56.25f/120f;
				MultiblockProcessInWorld<FillerRecipe> process = new MultiblockProcessInWorld<>(recipe, transformationPoint, Utils.createNonNullItemStackListFromItemStack(displayStack));
				if(master.addProcessToQueue(process, true))
				{
					master.addProcessToQueue(process, false);
					stack.shrink(displayStack.getCount());
					if(stack.getCount() <= 0)
						entity.setDead();
				}
			}


		}
	}

	@Override
	public boolean addProcessToQueue(MultiblockProcess<FillerRecipe> process, boolean simulate, boolean addToPrevious)
	{
		if(!(this.dustStorage.canMergeWith(process.recipe.dust)&&this.dustStorage.amount >= process.recipe.dust.amount))
			return false;
		boolean b = super.addProcessToQueue(process, simulate, addToPrevious);
		if(b&&!simulate)
		{
			this.dustStorage = this.dustStorage.getSubtracted(process.recipe.dust);
		}
		return b;
	}

	@Override
	public EnumFacing[] sigOutputDirections()
	{
		if(pos==11)
			return new EnumFacing[]{getOutFacing()};
		return new EnumFacing[0];
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			TileEntityFiller master = master();
			if(master==null)
				return false;
			if(pos==24&&facing==EnumFacing.UP)
				return true;
			return pos==9&&facing==getOutFacing().getOpposite();
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			TileEntityFiller master = master();
			if(master==null)
				return null;
			if(pos==24&&facing==EnumFacing.UP)
				return (T)master.insertionHandlerDust;
			if(pos==9&&facing==getOutFacing().getOpposite())
				return (T)master.insertionHandlerContainer;
			return null;
		}
		return super.getCapability(capability, facing);
	}

	@SideOnly(Side.CLIENT)
	private void spawnDustParticle(MultiblockProcess<FillerRecipe> process)
	{
		Vec3d pos = new Vec3d(getBlockPosForPos(10)).addVector(0.5, 0, 0.5);

		float mod = (float)(Math.random()*2f);

		ParticleRedstone particle = (ParticleRedstone)ClientUtils.mc().effectRenderer.spawnEffectParticle(EnumParticleTypes.REDSTONE.getParticleID(), pos.x, pos.y+0.85, pos.z, 0, -4, 0);
		ParticleRedstone particle2 = (ParticleRedstone)ClientUtils.mc().effectRenderer.spawnEffectParticle(EnumParticleTypes.REDSTONE.getParticleID(), pos.x, pos.y+0.65, pos.z, 0, -4, 0);
		ParticleRedstone particle3 = (ParticleRedstone)ClientUtils.mc().effectRenderer.spawnEffectParticle(EnumParticleTypes.REDSTONE.getParticleID(), pos.x, pos.y, pos.z, 0, -4, 0);

		float[] rgb = getCurrentProcessColor(process.recipe);
		final float dmod = 1.3043479f;

		if(particle!=null)
		{
			particle.reddustParticleScale = 2;
			particle.setRBGColorF(rgb[0]*mod, rgb[1]*mod, rgb[2]*mod);
		}
		if(particle2!=null)
		{
			particle2.reddustParticleScale = 2;
			particle2.setRBGColorF(rgb[0]*dmod*mod, rgb[1]*dmod*mod, rgb[2]*dmod*mod);
		}
		if(particle3!=null)
		{
			particle3.reddustParticleScale = 2;
			particle3.setRBGColorF(rgb[0]*mod, rgb[1]*mod, rgb[2]*mod);
		}
	}

	private float[] getCurrentProcessColor(FillerRecipe recipe)
	{
		return IIUtils.rgbIntToRGB(DustUtils.getColor(recipe.dust));
	}

	@Override
	public NonNullList<ItemStack> getDroppedItems()
	{
		NonNullList<ItemStack> droppedItems = super.getDroppedItems();
		if(!isDummy()&&!dustStorage.isEmpty())
		{
			List<ItemStack> itemStacks = new ArrayList<>(droppedItems);
			itemStacks.addAll(Arrays.asList(DustUtils.fromDustStack(dustStorage)));
			NonNullList<ItemStack> list = NonNullList.withSize(itemStacks.size(), ItemStack.EMPTY);
			for(int i = 0; i < list.size(); i++)
				list.set(0, list.get(i));
			return list;
		}
		return droppedItems;
	}

	@Override
	public boolean canOpenGui()
	{
		return true;
	}

	@Override
	public int getGuiID()
	{
		return IIGuiList.GUI_FILLER.ordinal();
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
			case 9:
			case 10:
			case 11:
				list.add(new AxisAlignedBB(0, 0, 0, 1, 0.125f, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
				break;
			case 24:
			{
				Vec3d d = new Vec3d((mirrored?facing.rotateYCCW(): facing.rotateY()).getDirectionVec());
				Vec3d z = new Vec3d(facing.getDirectionVec());
				Vec3d zz = z.scale(0.5f);
				Vec3d zzF = z.scale(0.5f-0.0625f);
				Vec3d dd = d.scale(0.5f-0.0625f);
				Vec3d ddF = d.scale(0.3125f-0.0625f);
				Vec3d ddFF = d.scale(0.125f);

				Vec3d zzz = z.scale(0.0625f);
				Vec3d ddd = d.scale(0.0625f);


				list.add(new AxisAlignedBB(0.5, 0, 0.5, 0.5, 1, 0.5)
						.offset(getPos().getX(), getPos().getY(), getPos().getZ())
						.offset(d.scale(0.5f-0.0625f))
						.grow(zz.x, zz.y, zz.z)
						.expand(ddd.x, ddd.y, ddd.z)
				);

				list.add(new AxisAlignedBB(0.5, 0.375, 0.5, 0.5, 1, 0.5)
						.offset(getPos().getX(), getPos().getY(), getPos().getZ())
						.offset(d.scale(-0.5+0.0625f))
						.grow(zzF.x, zzF.y, zzF.z)
						.expand(ddd.x, ddd.y, ddd.z)
				);

				list.add(new AxisAlignedBB(0.5, 0.375, 0.5, 0.5, 1, 0.5)
						.offset(getPos().getX(), getPos().getY(), getPos().getZ())
						.offset(z.scale(0.5f-0.0625f))
						.grow(dd.x, dd.y, dd.z)
						.expand(zzz.x, zzz.y, zzz.z)
				);

				list.add(new AxisAlignedBB(0.5, 0.375, 0.5, 0.5, 1, 0.5)
						.offset(getPos().getX(), getPos().getY(), getPos().getZ())
						.offset(z.scale(-0.5f))
						.grow(dd.x, dd.y, dd.z)
						.expand(zzz.x, zzz.y, zzz.z)
				);

				list.add(new AxisAlignedBB(0.5, 0, 0.5, 0.5, 0.375, 0.5)
						.offset(getPos().getX(), getPos().getY(), getPos().getZ())
						.offset(d.scale(0.125f))
						.grow(zz.x, zz.y, zz.z)
						.grow(ddF.x, ddF.y, ddF.z)
						.expand(ddd.x, ddd.y, ddd.z)
				);

				list.add(new AxisAlignedBB(0.5, 0.125f, 0.5, 0.5, 0.375, 0.5)
						.offset(getPos().getX(), getPos().getY(), getPos().getZ())
						.offset(d.scale(-0.25f))
						.grow(zz.x, zz.y, zz.z)
						.grow(ddFF.x, ddFF.y, ddFF.z)
				);


				/*
				list.add(new AxisAlignedBB(0, 0, 0.0625, 0.5, 0.0625, 1-0.0625).offset(getPos().getX(), getPos().getY(), getPos().getZ()));

						list.add(new AxisAlignedBB(0, 0, 0, 0.5, 1, 0.0625).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
						list.add(new AxisAlignedBB(0, 0, 1-0.0625, 0.5, 1, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));

						list.add(new AxisAlignedBB(0.5, 0.375, 0, 1-0.0625, 1, 0.0625).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
						list.add(new AxisAlignedBB(0.5, 0.375, 1-0.0625, 1-0.0625, 1, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));

						list.add(new AxisAlignedBB(0, 0.0625, 0.0625, 0.0625, 1, 1-0.0625).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
						list.add(new AxisAlignedBB(1-0.125, 0.375, 0.0625, 1-0.0625, 1, 1-0.0625).offset(getPos().getX(), getPos().getY(), getPos().getZ()));

						list.add(new AxisAlignedBB(1-0.5, 0, 0.0625, 1-0.0625, 0.375, 1-0.0625).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
				 */

			}
			break;
			case 21:
			{
				Vec3d d = new Vec3d((mirrored?facing.rotateYCCW(): facing.rotateY()).getDirectionVec()).scale(0.0625f);
				list.add(new AxisAlignedBB(0.125, 0, 0.125, 0.75+0.125, 0.625, 0.75+0.125)
						.offset(getPos().getX(), getPos().getY(), getPos().getZ())
						.offset(d)
						.expand(d.x, d.y, d.z)
				);

				list.add(new AxisAlignedBB(0.5, 0.625, 0.5, 0.5, 1, 0.5)
								.offset(getPos().getX(), getPos().getY(), getPos().getZ())
								.grow(0.25, 0, 0.25)
						//.offset(d)
						//.expand(d.x, d.y, d.z)
				);
			}
			break;
			case 12:
			{
				Vec3d d = new Vec3d((mirrored?facing.rotateYCCW(): facing.rotateY()).getDirectionVec());
				Vec3d dd = d.scale(0.065f);
				Vec3d ddd = d.scale(0.125f);
				Vec3d z = new Vec3d(facing.getDirectionVec());
				list.add(new AxisAlignedBB(0.5, 0, 0.5, 0.5, 1, 0.5)
						.grow(0.125, 0, 0.125)
						.offset(getPos().getX(), getPos().getY(), getPos().getZ())
						.offset(d.scale(0.25f+0.125f))
						.offset(z.scale(0.125f+0.0625f))
				);
				list.add(new AxisAlignedBB(0.5, 0, 0.5, 0.5, 1, 0.5)
						.grow(0.125, 0, 0.125)
						.offset(getPos().getX(), getPos().getY(), getPos().getZ())
						.offset(d.scale(0.25f+0.125f))
						.offset(z.scale(-(0.125f+0.0625f)))
				);

				list.add(new AxisAlignedBB(0.5, 0.75, 0.5, 0.5, 1, 0.5)
						.grow(0.125, 0, 0.125)
						.expand(ddd.x, ddd.y, ddd.z)
						.offset(getPos().getX(), getPos().getY(), getPos().getZ())
						.offset(z.scale(0.125f+0.0625f))
				);
				list.add(new AxisAlignedBB(0.5, 0.75, 0.5, 0.5, 1, 0.5)
						.grow(0.125, 0, 0.125)
						.expand(dd.x, dd.y, dd.z)
						.offset(getPos().getX(), getPos().getY(), getPos().getZ())
						.offset(d.scale(0.0625f))
						.offset(z.scale(-(0.125f+0.0625f)))
				);
			}
			break;
			default:
				list.add(new AxisAlignedBB(0, 0, 0, 1, 1, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
				break;
		}
		return list;
	}

	@Override
	public boolean isOverrideBox(AxisAlignedBB box, EntityPlayer player, RayTraceResult mop, ArrayList<AxisAlignedBB> list)
	{
		return false;
	}
}
