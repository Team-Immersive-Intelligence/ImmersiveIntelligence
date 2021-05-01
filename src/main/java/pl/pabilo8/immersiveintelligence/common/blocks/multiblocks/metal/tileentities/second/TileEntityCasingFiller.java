package pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.crafting.IMultiblockRecipe;
import blusunrize.immersiveengineering.api.crafting.MetalPressRecipe;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.IConveyorAttachable;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityConveyorBelt;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMetalPress;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.IESounds;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleRedstone;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.Emplacement;
import pl.pabilo8.immersiveintelligence.api.crafting.CasingFillerRecipe;

import java.util.Iterator;

/**
 * @author Pabilo8
 * @since 04.03.2021
 */
public class TileEntityCasingFiller extends TileEntityMultiblockMetal<TileEntityCasingFiller, CasingFillerRecipe> implements IConveyorAttachable
{
	int gunpowder = 0;

	public TileEntityCasingFiller()
	{
		super(MultiblockCasingFiller.instance, new int[]{3, 3, 3}, Emplacement.energyCapacity, true);
	}

	@Override
	public void update()
	{
		super.update();
		if(isDummy()||isRSDisabled()||world.isRemote)
			return;

		for(MultiblockProcess process : processQueue)
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
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);
	}

	@Override
	public void receiveMessageFromServer(NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);
	}

	@Override
	protected CasingFillerRecipe readRecipeFromNBT(NBTTagCompound tag)
	{
		return CasingFillerRecipe.loadFromNBT(tag);
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
	public CasingFillerRecipe findRecipeForInsertion(ItemStack inserting)
	{
		return CasingFillerRecipe.findRecipe(inserting);
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
	public boolean additionalCanProcessCheck(MultiblockProcess<CasingFillerRecipe> process)
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
	public void onProcessFinish(MultiblockProcess<CasingFillerRecipe> process)
	{

	}

	@Override
	public int getMaxProcessPerTick()
	{
		return 3;
	}

	@Override
	public int getProcessQueueMaxLength()
	{
		return 3;
	}

	@Override
	public float getMinProcessDistance(MultiblockProcess<CasingFillerRecipe> process)
	{
		return 63.75f/120f;
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
		if(pos>=9&&pos<=11)
			return new float[]{0, 0, 0, 1, .125f, 1};
		return new float[]{0, 0, 0, 1, 1, 1};
	}

	@Override
	public NonNullList<ItemStack> getInventory()
	{
		return null;
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
		if(pos==9&&!world.isRemote&&entity!=null&&!entity.isDead&&entity instanceof EntityItem&&!((EntityItem)entity).getItem().isEmpty())
		{
			TileEntityCasingFiller master = master();
			if(master==null)
				return;
			ItemStack stack = ((EntityItem)entity).getItem();
			if(stack.isEmpty())
				return;
			IMultiblockRecipe recipe = master.findRecipeForInsertion(stack);
			if(recipe==null)
				return;
			ItemStack displayStack = recipe.getDisplayStack(stack);
			float transformationPoint = 56.25f/120f;
			MultiblockProcess process = new MultiblockProcessInWorld(recipe, transformationPoint, Utils.createNonNullItemStackListFromItemStack(displayStack));
			if(master.addProcessToQueue(process, true))
			{
				master.addProcessToQueue(process, false);
				stack.shrink(displayStack.getCount());
				if(stack.getCount() <= 0)
					entity.setDead();
			}
		}
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
			TileEntityCasingFiller master = master();
			if(master==null)
				return false;
			return pos==9&&facing==getOutFacing().getOpposite();
		}
		return super.hasCapability(capability, facing);
	}

	IItemHandler insertionHandler = new MultiblockInventoryHandler_DirectProcessing(this);

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			TileEntityCasingFiller master = master();
			if(master==null)
				return null;
			if(pos==9&&facing==getOutFacing().getOpposite())
				return (T)master.insertionHandler;
			return null;
		}
		return super.getCapability(capability, facing);
	}

	@SideOnly(Side.CLIENT)
	private void spawnDustParticle()
	{
		//Hardcoded for now :D, might make it configurable later on.
		BlockPos pos = getBlockPosForPos(10);
		Vec3d facing = new Vec3d(getFacing().getDirectionVec());
		Vec3d facing2 = new Vec3d(getFacing().rotateY().getDirectionVec());
		facing = facing.scale(1.15f);
		facing2 = facing2.scale(-0.5f);

		float mod = (float)(Math.random()*2f);

		ParticleRedstone particle = (ParticleRedstone)ClientUtils.mc().effectRenderer.spawnEffectParticle(EnumParticleTypes.REDSTONE.getParticleID(), pos.getX()+facing.x+facing2.x, pos.getY()+1.125+facing.y+facing2.y, pos.getZ()+facing.z+facing2.z, 0, -4, 0);
		ParticleRedstone particle2 = (ParticleRedstone)ClientUtils.mc().effectRenderer.spawnEffectParticle(EnumParticleTypes.REDSTONE.getParticleID(), pos.getX()+facing.x+facing2.x, pos.getY()+0.65+facing.y+facing2.y, pos.getZ()+facing.z+facing2.z, 0, -4, 0);
		ParticleRedstone particle3 = (ParticleRedstone)ClientUtils.mc().effectRenderer.spawnEffectParticle(EnumParticleTypes.REDSTONE.getParticleID(), pos.getX()+facing.x+facing2.x, pos.getY()+facing.y+facing2.y, pos.getZ()+facing.z+facing2.z, 0, -4, 0);


		if(particle!=null)
		{
			particle.reddustParticleScale = 3;
			particle.setRBGColorF(0.22392157f*mod*1.15f, 0.21372549019607842f*mod*1.15f, 0.15176470588235294f*mod);
		}
		if(particle2!=null)
		{
			particle2.reddustParticleScale = 4;
			particle2.setRBGColorF(0.22392157f*mod*1.5f, 0.21372549019607842f*mod*1.5f, 0.15176470588235294f*mod*1.5f);
		}
		if(particle3!=null)
		{
			particle3.reddustParticleScale = 3;
			particle3.setRBGColorF(0.22392157f*mod*1.15f, 0.21372549019607842f*mod*1.15f, 0.15176470588235294f*mod);
		}
	}
}
