package pl.pabilo8.immersiveintelligence.client.render;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.metal_device.ModelAmmunitionCrate;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.TileEntityAmmunitionCrate;

/**
 * Created by Pabilo8 on 2019-05-26.
 */
@SideOnly(Side.CLIENT)
public class AmmunitionCrateRenderer extends TileEntitySpecialRenderer<TileEntityAmmunitionCrate>
{
	private static ModelAmmunitionCrate model = new ModelAmmunitionCrate();

	private static String texture = ImmersiveIntelligence.MODID+":textures/blocks/metal_device/ammunition_crate.png";

	@Override
	public void render(TileEntityAmmunitionCrate te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null)
		{
			ClientUtils.bindTexture(texture);
			GlStateManager.pushMatrix();
			GlStateManager.translate(x+1, y, z);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			float angle = Math.min(1.5f, Math.max(te.lidAngle+(te.open?0.2f*partialTicks: -0.3f*partialTicks), 0f));
			model.getBlockRotation(te.facing, model);
			model.rotate(model.lidModel, 0, 0, angle);
			model.translate(model.lidModel, 0, angle/1.5f, 0);
			model.render();
			model.translate(model.lidModel, 0, -angle/1.5f, 0);

			GlStateManager.popMatrix();
			return;

		}
		else
		{

			GlStateManager.pushMatrix();
			GlStateManager.translate(x+1, y, z);
			GlStateManager.enableBlend();
			GlStateManager.enableAlpha();

			ClientUtils.bindTexture(texture);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			model.getBlockRotation(EnumFacing.NORTH, model);
			model.rotate(model.lidModel, 0, 0, 0);
			model.render();

			GlStateManager.popMatrix();
			return;
		}
	}
}
