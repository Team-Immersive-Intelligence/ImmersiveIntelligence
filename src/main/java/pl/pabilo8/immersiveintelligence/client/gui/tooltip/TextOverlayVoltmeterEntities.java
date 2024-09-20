package pl.pabilo8.immersiveintelligence.client.gui.tooltip;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.energy.immersiveflux.IFluxReceiver;
import blusunrize.immersiveengineering.client.ClientProxy;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.RayTraceResult;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author Pabilo8
 * @since 09.10.2022
 */
public class TextOverlayVoltmeterEntities extends TextOverlayBase
{
	@ParametersAreNonnullByDefault
	@Override
	public boolean shouldDraw(EntityPlayer player, RayTraceResult mouseOver, @Nullable TileEntity te, @Nullable Entity entityHit)
	{
		return mouseOver.entityHit instanceof IFluxReceiver;
	}

	@ParametersAreNonnullByDefault
	@Nullable
	@Override
	public String[] getText(EntityPlayer player, RayTraceResult mouseOver, @Nullable TileEntity te, @Nullable Entity entityHit)
	{
		IFluxReceiver energy = (IFluxReceiver)mouseOver.entityHit;

		int maxStorage = (energy).getMaxEnergyStored(null);
		int storage = (energy).getEnergyStored(null);
		if(maxStorage > 0)
			return I18n.format(Lib.DESC_INFO+"energyStored",
					"<br>"+Utils.toScientificNotation(storage, "0##", 100000)+
							" / "+
							Utils.toScientificNotation(maxStorage, "0##", 100000)).split("<br>");
		return null;
	}

	@Override
	public IIColor getDefaultFontColor()
	{
		return IIReference.COLOR_IMMERSIVE_ORANGE;
	}

	@Nonnull
	@Override
	public FontRenderer getFontRenderer()
	{
		return ClientProxy.nixieFont;
	}
}
