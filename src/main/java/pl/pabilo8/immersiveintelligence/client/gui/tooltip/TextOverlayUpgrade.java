package pl.pabilo8.immersiveintelligence.client.gui.tooltip;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import pl.pabilo8.immersiveintelligence.api.utils.IUpgradableMachine;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author Pabilo8
 * @since 09.10.2022
 */
public class TextOverlayUpgrade extends TextOverlayBase
{
	@ParametersAreNonnullByDefault
	@Override
	public boolean shouldDraw(EntityPlayer player, RayTraceResult mouseOver, @Nullable TileEntity te, @Nullable Entity entityHit)
	{
		if(mouseOver.typeOfHit!=Type.BLOCK)
			return false;

		return te instanceof IUpgradableMachine&&
				IIItemUtil.isWrench(player.getHeldItem(EnumHand.MAIN_HAND));
	}

	@ParametersAreNonnullByDefault
	@Nullable
	@Override
	public String[] getText(EntityPlayer player, RayTraceResult mouseOver, @Nullable TileEntity te, @Nullable Entity entityHit)
	{
		assert te!=null;
		IUpgradableMachine teU = (IUpgradableMachine)te;

		teU = teU.getUpgradeMaster();
		if(teU!=null&&teU.getCurrentlyInstalled()!=null)
			return new String[]{
					I18n.format(IIReference.INFO_KEY+"machineupgrade.name", I18n.format("machineupgrade.immersiveintelligence."+teU.getCurrentlyInstalled().getName())),
					I18n.format(IIReference.INFO_KEY+"machineupgrade.progress", teU.getInstallProgress(), teU.getCurrentlyInstalled().getProgressRequired())
			};
		return null;
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
