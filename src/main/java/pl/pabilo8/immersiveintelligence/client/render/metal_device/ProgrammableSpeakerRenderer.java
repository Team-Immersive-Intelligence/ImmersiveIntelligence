package pl.pabilo8.immersiveintelligence.client.render.metal_device;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.metal_device.ModelProgrammableSpeaker;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.TileEntityProgrammableSpeaker;

/**
 * @author Pabilo8
 * @since 15-06-2019
 */

@SideOnly(Side.CLIENT)
public class ProgrammableSpeakerRenderer extends TileEntitySpecialRenderer<TileEntityProgrammableSpeaker>
{
	private static ModelProgrammableSpeaker model = new ModelProgrammableSpeaker();

	private static String texture = ImmersiveIntelligence.MODID+":textures/blocks/metal_device/alarm_siren.png";

	@Override
	public void render(TileEntityProgrammableSpeaker te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z+1);
		GlStateManager.enableBlend();
		GlStateManager.enableAlpha();

		ClientUtils.bindTexture(texture);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

		if(te!=null)
		{
			GlStateManager.disableLighting();
			RenderHelper.enableStandardItemLighting();
		}

		model.render();

		GlStateManager.popMatrix();

	}
}
