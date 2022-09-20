package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.CullFace;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.metal.ModelFuelStation;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.metal.vehicle_workshop.ModelHeavyInserter;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityFuelStation;

/**
 * @author Pabilo8
 * @since 07.04.2021
 */
public class FuelStationRenderer extends TileEntitySpecialRenderer<TileEntityFuelStation> implements IReloadableModelContainer<FuelStationRenderer>
{
	private static ModelFuelStation model;
	private static final String TEXTURE = ImmersiveIntelligence.MODID+":textures/blocks/multiblock/fuel_station.png";
	private static ModelHeavyInserter modelInserter;

	@Override
	public void render(TileEntityFuelStation te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null&&!te.isDummy())
		{
			ClientUtils.bindTexture(TEXTURE);
			GlStateManager.pushMatrix();
			GlStateManager.translate((float)x+2, (float)y-2, (float)z);
			GlStateManager.rotate(270F, 0F, 1F, 0F);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			if(te.hasWorld())
			{
				GlStateManager.translate(0f, 1f, 1f);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
			}

			model.getBlockRotation(te.facing, false);

			if(te.mirrored)
			{
				GlStateManager.scale(-1,1,1);
				GlStateManager.translate(-1,0,0);
				GlStateManager.cullFace(CullFace.FRONT);
			}

			model.render();

			GlStateManager.translate(1f, 1f, 0);
			float f = te.calculateInserterAnimation(partialTicks);

			if(te.mirrored)
			{
				GlStateManager.scale(-1,1,1);
				GlStateManager.translate(-1,0,0);
				GlStateManager.cullFace(CullFace.BACK);
			}

			modelInserter.renderProgress(f*0.5f, te.facing.getHorizontalAngle()-te.calculateInserterAngle(partialTicks)+180, te.calculateDistance(partialTicks));

			GlStateManager.popMatrix();
		}

	}

	@Override
	public void reloadModels()
	{
		model = new ModelFuelStation();
		modelInserter = new ModelHeavyInserter();
	}
}
