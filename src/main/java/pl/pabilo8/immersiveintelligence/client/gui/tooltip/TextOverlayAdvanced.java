package pl.pabilo8.immersiveintelligence.client.gui.tooltip;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.RayTraceResult;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedTextOverlay;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.common.item.tools.backpack.ItemIIBackpackBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 09.10.2022
 */
public class TextOverlayAdvanced extends TextOverlayBase
{
	@ParametersAreNonnullByDefault
	@Override
	public boolean shouldDraw(EntityPlayer player, RayTraceResult mouseOver, @Nullable TileEntity te, @Nullable Entity entityHit)
	{
		return te instanceof IAdvancedTextOverlay||mouseOver.entityHit instanceof IAdvancedTextOverlay
				||entityHit instanceof EntityLivingBase&&ItemIIBackpackBase.getWornBackpack((EntityLivingBase)entityHit)!=null;
	}

	@ParametersAreNonnullByDefault
	@Nullable
	@Override
	public String[] getText(EntityPlayer player, RayTraceResult mouseOver, @Nullable TileEntity te, @Nullable Entity entityHit)
	{
		if(entityHit instanceof EntityLivingBase)
		{
			ItemIIBackpackBase.WornBackpack worn = ItemIIBackpackBase.getWornBackpack((EntityLivingBase)entityHit);
			if(worn!=null)
			{
				ItemStack stack = worn.getStack();
				return ((ItemIIBackpackBase)stack.getItem()).getBackpackOverlayText(stack);
			}
		}
		if(mouseOver.entityHit instanceof IAdvancedTextOverlay)
			return ((IAdvancedTextOverlay)mouseOver.entityHit).getOverlayText(player, mouseOver);

		assert te!=null;
		return ((IAdvancedTextOverlay)te).getOverlayText(player, mouseOver);
	}

	@Nonnull
	@Override
	public FontRenderer getFontRenderer()
	{
		return IIClientUtils.fontRegular;
	}
}
