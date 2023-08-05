package pl.pabilo8.immersiveintelligence.client.gui.tooltip;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.energy.immersiveflux.IFluxReceiver;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.RayTraceResult;
import pl.pabilo8.immersiveintelligence.client.ClientEventHandler;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author Pabilo8
 * @since 09.10.2022
 */
public class TextOverlayHeadgear extends TextOverlayBase
{
	@ParametersAreNonnullByDefault
	@Override
	public boolean shouldDraw(EntityPlayer player, RayTraceResult mouseOver, @Nullable TileEntity te, @Nullable Entity entityHit)
	{
		return ClientEventHandler.gotTheDrip&&te instanceof IFluxReceiver;
	}

	@ParametersAreNonnullByDefault
	@Nullable
	@Override
	public String [] getText(EntityPlayer player, RayTraceResult mouseOver, @Nullable TileEntity te, @Nullable Entity entityHit)
	{
		assert te!=null;

		int maxStorage = ((IFluxReceiver)te).getMaxEnergyStored(mouseOver.sideHit);
		int storage = ((IFluxReceiver)te).getEnergyStored(mouseOver.sideHit);
		if(maxStorage > 0)
			return I18n.format(Lib.DESC_INFO+"energyStored",
					"<br>"+Utils.toScientificNotation(storage, "0##", 100000)+
							" / "+
							Utils.toScientificNotation(maxStorage, "0##", 100000)).split("<br>");
		return null;
	}

	@Nonnull
	@Override
	public FontRenderer getFontRenderer()
	{
		return IIClientUtils.fontRegular;
	}
}
