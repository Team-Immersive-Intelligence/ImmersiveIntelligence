package pl.pabilo8.immersiveintelligence.client.render.metal_device;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.misc.ModelTankTrap;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.blocks.fortification.TileEntityTankTrap;

/**
 * @author Pabilo8
 * @since 15-06-2019
 */

@SideOnly(Side.CLIENT)
public class TankTrapRenderer extends TileEntitySpecialRenderer<TileEntityTankTrap> implements IReloadableModelContainer<TankTrapRenderer>
{
	private static ModelTankTrap model;

	@Override
	public void render(TileEntityTankTrap te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		String texture = ImmersiveIntelligence.MODID+":textures/blocks/fortification/tank_trap.png";
		ClientUtils.bindTexture(texture);
		GlStateManager.pushMatrix();
		GlStateManager.translate((float)x+0.5, (float)y-0.875, (float)z+0.5);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

		GlStateManager.disableLighting();
		RenderHelper.enableStandardItemLighting();

		for(ModelRendererTurbo mod : model.baseModel)
		{
			mod.render(0.0625f, true);
		}

		GlStateManager.popMatrix();
	}

	@Override
	public void reloadModels()
	{
		model = new ModelTankTrap();
	}
}
