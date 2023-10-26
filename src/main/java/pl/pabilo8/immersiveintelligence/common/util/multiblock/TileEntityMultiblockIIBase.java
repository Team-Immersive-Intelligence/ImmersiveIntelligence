package pl.pabilo8.immersiveintelligence.common.util.multiblock;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.api.IEProperties.PropertyBoolInverted;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IMirrorAble;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IUsesBooleanProperty;
import blusunrize.immersiveengineering.common.blocks.TileEntityMultiblockPart;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * A new beginning!<br>
 * A lightweight alternative to using {@link blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal}
 *
 * @author Pabilo8
 * @since 04.08.2022
 */
public abstract class TileEntityMultiblockIIBase<T extends TileEntityMultiblockIIBase<T>> extends TileEntityMultiblockPart<T> implements IMirrorAble, IIEInventory
{
	//The multiblock INSTANCE, for easy access
	protected final MultiblockStuctureBase<T> multiblock;


	//--- Reference Variables ---//

	public static final String KEY_SYNC_ALL_VALUES = "_sync_all_values";

	protected TileEntityMultiblockIIBase(MultiblockStuctureBase<T> multiblock)
	{
		super(multiblock.getSize());
		this.multiblock = multiblock;
	}

	//--- Update Function, Optimization ---//

	@Override
	public final void update()
	{
		//Optimize
		ApiUtils.checkForNeedlessTicking(this);
		if(isDummy())
			return;

		//Tick
		onUpdate();
	}

	@Override
	public void onLoad()
	{

	}

	/**
	 * Override <b>THIS</b>.<br>
	 * You <b>CAN NOT</b> override the {@link #update()} method<br>
	 * Called every tick.
	 */
	protected abstract void onUpdate();

	//--- NBT ---//

	@Override
	public void receiveMessageFromServer(@Nonnull NBTTagCompound message)
	{
		if(isFullSyncMessage(message))
			readCustomNBT(message, false);
	}

	/**
	 * Checks if the message has the "sync all values" tag.<br>
	 * Check before parsing a message to avoid doing the same thing 2 times.
	 */
	protected final boolean isFullSyncMessage(@Nonnull NBTTagCompound message)
	{
		return message.hasKey(KEY_SYNC_ALL_VALUES);
	}

	//--- Inventory & Fluid Handling ---//
	//--- Override if the machine uses fluids or has an inventory ---//

	@Nonnull
	@Override
	protected IFluidTank[] getAccessibleFluidTanks(EnumFacing side)
	{
		return new IFluidTank[0];
	}

	@Override
	@Nonnull
	public NonNullList<ItemStack> getInventory()
	{
		return NonNullList.create();
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

	//--- Mirroring, Positions, Internal Stuff ---//

	@Override
	public float[] getBlockBounds()
	{
		return new float[]{0, 0, 0, 1, 1, 1};
	}

	/**
	 * @return Whether this multiblock is mirrored (flipped)
	 */
	@Override
	public boolean getIsMirrored()
	{
		return this.mirrored;
	}

	/**
	 * The Mirrored property
	 */
	@Override
	public PropertyBoolInverted getBoolProperty(Class<? extends IUsesBooleanProperty> inf)
	{
		return IEProperties.BOOLEANS[0];
	}


	/**
	 * Copied from {@link blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal}
	 *
	 * @return original block at this position, which was used to create this multiblock
	 */
	@Override
	public ItemStack getOriginalBlock()
	{
		if(pos < 0)
			return ItemStack.EMPTY;
		ItemStack s = ItemStack.EMPTY;
		try
		{
			int blocksPerLevel = structureDimensions[1]*structureDimensions[2];
			int h = (pos/blocksPerLevel);
			int l = (pos%blocksPerLevel/structureDimensions[2]);
			int w = (pos%structureDimensions[2]);
			s = this.multiblock.getStructureManual()[h][l][w];
		} catch(Exception e) {IILogger.info(e);}
		return s.copy();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public AxisAlignedBB getRenderBoundingBox()
	{
		if(!isDummy())
		{
			if(multiblock.isMassiveStructure())
				return INFINITE_EXTENT_AABB;

			BlockPos nullPos = this.getBlockPosForPos(0);
			return new AxisAlignedBB(
					nullPos,
					nullPos.offset(facing,
									structureDimensions[1]).offset(mirrored?facing.rotateYCCW(): facing.rotateY(),
									structureDimensions[2])
							.up(structureDimensions[0])
			);
		}
		return super.getRenderBoundingBox();
	}

	public BlockPos[] getMultiblockBlocks()
	{
		ArrayList<BlockPos> blocks = new ArrayList<>();
		BlockPos origin = getPos().subtract(new Vec3i(offset[0], offset[1], offset[2]));

		for(int y = 0; y < structureDimensions[0]; y++)
			for(int x = 0; x < structureDimensions[1]; x++)
				for(int z = 0; z < structureDimensions[2]; z++)
					blocks.add(origin.offset(facing, x).offset(facing.rotateY(), z).add(0, y, 0));

		return blocks.toArray(new BlockPos[0]);
	}

	/**
	 * @param tileAction action performed for each tile entity of this multiblock
	 */
	public void forMultiblockBlocks(Consumer<T> tileAction)
	{
//		BlockPos origin = getPos().subtract(new Vec3i(offset[0], offset[1], offset[2]));

		for(int i = 0; i < structureDimensions[0]*structureDimensions[1]*structureDimensions[2]; i++)
		{
			TileEntity te = world.getTileEntity(getBlockPosForPos(i));
			if(te!=null&&te.getClass()==this.getClass())
				tileAction.accept((T)te);
		}

/*
		for(int y = 0; y < structureDimensions[0]; y++)
			for(int x = 0; x < structureDimensions[1]; x++)
				for(int z = 0; z < structureDimensions[2]; z++)
				{
					TileEntity te = world.getTileEntity(origin.offset(facing, x).offset(facing.rotateY(), mirrored?-z: z).add(0, y, 0));
					if(te!=null&&te.getClass()==this.getClass())
						tileAction.accept((T)te);
				}*/
	}

	/**
	 * Fix for breaking animation
	 */
	@Override
	public boolean canRenderBreaking()
	{
		return !isDummy();
	}

	//--- Easy Networking/Updating ---//

	/**
	 * Forces update of the master TileEntity on all clients
	 */
	public void forceTileUpdate()
	{
		T master = master();
		if(master!=null)
		{
			//vanilla stuff
			master.markDirty();
			master.markContainingBlockForUpdate(null);

			//II stuff
			NBTTagCompound nbt = new NBTTagCompound();
			master.writeCustomNBT(nbt, false);
			nbt.setBoolean(KEY_SYNC_ALL_VALUES, true);
			master.sendNBTMessageClient(nbt);
		}
	}

	/**
	 * Sends an update message to clients (players) at a point.<br>
	 * Can be used at both client and server.
	 *
	 * @param message to send
	 */
	public void sendNBTMessageClient(NBTTagCompound message)
	{
		if(!message.hasNoTags())
			IIPacketHandler.sendToClient(this, new MessageIITileSync(this, message));
	}

	/**
	 * Sends an update message from client to server
	 *
	 * @param message to send
	 */
	public void sendNBTMessageServer(NBTTagCompound message)
	{
		if(!message.hasNoTags())
			IIPacketHandler.sendToServer(new MessageIITileSync(this, message));
	}
}
