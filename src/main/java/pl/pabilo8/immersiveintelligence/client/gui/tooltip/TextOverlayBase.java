package pl.pabilo8.immersiveintelligence.client.gui.tooltip;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.RayTraceResult;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author Pabilo8
 * @since 09.10.2022
 */
public abstract class TextOverlayBase
{
	@ParametersAreNonnullByDefault
	public abstract boolean shouldDraw(EntityPlayer player, RayTraceResult mouseOver, @Nullable TileEntity te, @Nullable Entity entityHit);

	@ParametersAreNonnullByDefault
	@Nullable
	public abstract String[] getText(EntityPlayer player, RayTraceResult mouseOver, @Nullable TileEntity te, @Nullable Entity entityHit);

	@Nonnull
	public abstract FontRenderer getFontRenderer();

	public IIColor getDefaultFontColor()
	{
		return IIColor.WHITE;
	}
}
