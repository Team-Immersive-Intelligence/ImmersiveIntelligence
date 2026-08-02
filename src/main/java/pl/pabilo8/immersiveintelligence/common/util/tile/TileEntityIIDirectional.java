package pl.pabilo8.immersiveintelligence.common.util.tile;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IDirectionalTile;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 16.07.2026
 */
public abstract class TileEntityIIDirectional extends TileEntityIIBase implements IDirectionalTile
{
	@SyncNBT
	public EnumFacing facing = EnumFacing.NORTH;

	@Nonnull
	@Override
	public final EnumFacing getFacing()
	{
		return facing;
	}

	@Override
	public final void setFacing(@Nonnull EnumFacing facing)
	{
		this.facing = facing;
	}

	@Nonnull
	protected abstract FacingSettings getFacingSettings();

	@Override
	public final int getFacingLimitation()
	{
		return getFacingSettings().facingLimitation.ordinal();
	}

	@Override
	public final boolean mirrorFacingOnPlacement(@Nonnull EntityLivingBase placer)
	{
		return getFacingSettings().shouldMirrorOnPlacement;
	}

	@Override
	public final boolean canHammerRotate(@Nonnull EnumFacing side, float hitX, float hitY, float hitZ, @Nonnull EntityLivingBase entity)
	{
		return getFacingSettings().canHammerRotate;
	}

	@Override
	public final boolean canRotate(@Nonnull EnumFacing axis)
	{
		return getFacingSettings().canRotate;
	}

	public enum FacingLimitation
	{
		SIDE_CLICKED,
		PISTON_LIKE,
		HORIZONTAL,
		VERTICAL,
		XZ_AXIS,
		HORIZONTAL_QUADRANT,
		HORIZONTAL_TOWARDS_CLICKED
	}

	public static class FacingSettings
	{
		boolean canRotate = false, canHammerRotate = false, shouldMirrorOnPlacement = false;
		boolean mirrorable = false;
		final FacingLimitation facingLimitation;

		public FacingSettings()
		{
			this.facingLimitation = FacingLimitation.SIDE_CLICKED;
		}

		public FacingSettings(FacingLimitation limitation)
		{
			this.facingLimitation = limitation;
		}

		public FacingSettings withRotation(boolean canRotate)
		{
			this.canRotate = this.canHammerRotate = canRotate;
			return this;
		}

		public FacingSettings withHammerRotation(boolean canHammerRotate)
		{
			this.canHammerRotate = canHammerRotate;
			return this;
		}

		public FacingSettings withMirroringOnPlacement(boolean shouldMirrorOnPlacement)
		{
			this.shouldMirrorOnPlacement = shouldMirrorOnPlacement;
			return this;
		}

		public FacingSettings withMirroring(boolean mirrorable)
		{
			this.mirrorable = mirrorable;
			return this;
		}
	}
}
