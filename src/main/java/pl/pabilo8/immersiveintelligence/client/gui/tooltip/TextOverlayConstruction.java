package pl.pabilo8.immersiveintelligence.client.gui.tooltip;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemUtils;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IConstructionRequiringDevice;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 09.10.2022
 */
public class TextOverlayConstruction extends TextOverlayBase
{
	@ParametersAreNonnullByDefault
	@Override
	public boolean shouldDraw(EntityPlayer player, RayTraceResult mouseOver, @Nullable TileEntity te, @Nullable Entity entityHit)
	{
		if(mouseOver.typeOfHit!=Type.BLOCK)
			return false;

		return te instanceof IConstructionRequiringDevice
				&&IIItemUtils.isAdvancedHammer(player.getHeldItem(EnumHand.MAIN_HAND));
	}

	@ParametersAreNonnullByDefault
	@Nullable
	@Override
	public String[] getText(EntityPlayer player, RayTraceResult mouseOver, @Nullable TileEntity te, @Nullable Entity entityHit)
	{
		assert te!=null;
		IConstructionRequiringDevice constructionTile = (IConstructionRequiringDevice)te;
		constructionTile = constructionTile.master();

		if(constructionTile!=null&&!constructionTile.isConstructionFinished())
			return new String[]{
					I18n.format(IIReference.INFO_KEY+"machineupgrade.progress",
							constructionTile.getCurrentConstruction(0),
							constructionTile.getConstructionCost())
			};
		return null;
	}

	@Nonnull
	@Override
	public FontRenderer getFontRenderer()
	{
		return IIClientUtils.fontRegular;
	}
}
