package pl.pabilo8.immersiveintelligence.api.utils.camera;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedZoom;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 15.05.2026
 */
public class ZoomSettings implements IAdvancedZoom
{
	private boolean zoomEnabled = false;
	private final float[] zoomSteps;
	private final ResourceLocation overlayTexture;

	public ZoomSettings(float[] zoomSteps, ResourceLocation overlayTexture)
	{
		this.zoomSteps = zoomSteps;
		this.overlayTexture = overlayTexture;
	}

	public ZoomSettings withZoomEnabled(boolean zoomEnabled)
	{
		this.zoomEnabled = zoomEnabled;
		return this;
	}

	@Override
	public boolean shouldZoom(ItemStack stack, EntityPlayer player)
	{
		return zoomEnabled;
	}

	@Override
	public float[] getZoomSteps(ItemStack stack, EntityPlayer player)
	{
		return zoomSteps;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public ResourceLocation getZoomOverlayTexture(ItemStack stack, EntityPlayer player)
	{
		return overlayTexture;
	}
}
