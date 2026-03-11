package pl.pabilo8.immersiveintelligence.client.gui.tooltip;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import pl.pabilo8.immersiveintelligence.api.upgrade.IUpgradableDevice;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 09.10.2022
 */
public class TextOverlayUpgrade extends TextOverlayBase
{
	@ParametersAreNonnullByDefault
	@Override
	public boolean shouldDraw(EntityPlayer player, RayTraceResult mouseOver, @Nullable TileEntity te, @Nullable Entity entityHit)
	{
		return mouseOver.typeOfHit==Type.BLOCK&&te instanceof IUpgradableDevice
				&&IIItemUtils.isWrench(player.getHeldItem(EnumHand.MAIN_HAND));

	}

	@ParametersAreNonnullByDefault
	@Nullable
	@Override
	public String[] getText(EntityPlayer player, RayTraceResult mouseOver, @Nullable TileEntity te, @Nullable Entity entityHit)
	{
		IUpgradableDevice tile = IIUtils.requireMaster(((IUpgradableDevice)te), IUpgradableDevice::master);

		//Return null if no upgrade is being installed
		if(tile==null||tile.getCurrentUpgrade()==null)
			return null;
		//Return upgrade name and progress
		return new String[]{
				I18n.format(IIReference.INFO_KEY+"machineupgrade.name", tile.getCurrentUpgrade().getLocalizedName()),
				I18n.format(IIReference.INFO_KEY+"machineupgrade.progress",
						tile.getUpgradeInstallProgress(false),
						tile.getCurrentUpgrade().getProgressRequired())
		};
	}

	@Override
	public IIColor getDefaultFontColor()
	{
		return IIReference.COLOR_GOLD;
	}

	@Nonnull
	@Override
	public FontRenderer getFontRenderer()
	{
		return IIClientUtils.fontRegular;
	}
}
