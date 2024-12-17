package pl.pabilo8.immersiveintelligence.common.block.simple.tileentity;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IDirectionalTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.wooden.TileEntityWoodenCrate;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIISmallCrate.IIBlockTypes_SmallCrate;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 2019-05-17
 * @author Avalon
 * @since 2024-12-17
 */
public class TileEntitySmallCrate extends TileEntityWoodenCrate implements IGuiTile, IDirectionalTile, IBlockBounds {

	private static final float[] BOX_X = {0.1875f, 0, 0.0625f, 0.8125f, 0.625f, 0.9375f};
	private static final float[] BOX_Z = {0.0625f, 0, 0.1875f, 0.9375f, 0.625f, 0.8125f};
	private static final float[] CUBE = {0.0625f, 0, 0.0625f, 0.9375f, 0.875f, 0.9375f};
	private static final float[] WIDE_X = {0.1875f, 0, 0.0625f, 0.8125f, 0.3125f, 0.9375f};
	private static final float[] WIDE_Z = {0.0625f, 0, 0.1875f, 0.9375f, 0.3125f, 0.8125f};

	public EnumFacing facing = EnumFacing.NORTH;

	@Override
	public void readCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket) {
		super.readCustomNBT(nbt, descPacket);
		if (nbt.hasKey("facing")) {
			this.facing = EnumFacing.getFront(nbt.getInteger("facing"));
		}
	}

	@Override
	public void writeCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket) {
		super.writeCustomNBT(nbt, descPacket);
		nbt.setInteger("facing", this.facing.getIndex());
	}

	@Override
	@Nullable
	public ITextComponent getDisplayName() {
		return name != null ? new TextComponentString(name) :
				new TextComponentTranslation(IIReference.BLOCK_KEY + "small_crate." +
						IIBlockTypes_SmallCrate.values()[getBlockMetadata()].getName() + ".name");
	}

	@Override
	public boolean canOpenGui() {
		return true;
	}

	@Override
	public int getGuiID() {
		return IIGuiList.GUI_SMALL_CRATE.ordinal();
	}

	@Override
	public TileEntity getGuiMaster() {
		return this;
	}

	@Override
	@Nonnull
	public EnumFacing getFacing() {
		return facing;
	}

	@Override
	public void setFacing(EnumFacing facing) {
		this.facing = (facing.getAxis() == Axis.Y) ? EnumFacing.NORTH : facing;
	}

	@Override
	public int getFacingLimitation() {
		return 2;
	}

	@Override
	public boolean mirrorFacingOnPlacement(@Nonnull EntityLivingBase placer) {
		return true;
	}

	@Override
	public boolean canHammerRotate(@Nonnull EnumFacing side, float hitX, float hitY, float hitZ, @Nonnull EntityLivingBase entity) {
		return true;
	}

	@Override
	public boolean canRotate(EnumFacing axis) {
		return axis.getAxis().isHorizontal();
	}

	@Override
	@Nonnull
	public float[] getBlockBounds() {
		switch (world.getBlockState(pos).getValue(IIContent.blockSmallCrate.property)) {
			case METAL_CRATE_BOX:
			case WOODEN_CRATE_BOX:
			case REINFORCED_CRATE_BOX:
				return facing.getAxis() == Axis.X ? BOX_X : BOX_Z;
			case METAL_CRATE_CUBE:
			case WOODEN_CRATE_CUBE:
			case REINFORCED_CRATE_CUBE:
				return CUBE;
			case METAL_CRATE_WIDE:
			case WOODEN_CRATE_WIDE:
			case REINFORCED_CRATE_WIDE:
				return facing.getAxis() == Axis.X ? WIDE_X : WIDE_Z;
			default:
				return new float[0];
		}
	}
}