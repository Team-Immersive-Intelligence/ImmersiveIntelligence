package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.metal.ModelVehicleWorkshop;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.metal.vehicle_workshop.ModelCrane;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.metal.vehicle_workshop.ModelHeavyInserter;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.util.tmt.TmtUtil;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityVehicleWorkshop;

import static pl.pabilo8.immersiveintelligence.client.IIClientUtils.drawRope;

/**
 * @author Pabilo8
 * @since 07.04.2021
 */
public class VehicleWorkshopRenderer extends TileEntitySpecialRenderer<TileEntityVehicleWorkshop> implements IReloadableModelContainer<VehicleWorkshopRenderer>
{
	private static ModelVehicleWorkshop model;
	private static ModelVehicleWorkshop modelFlipped;
	private static final String TEXTURE = ImmersiveIntelligence.MODID+":textures/blocks/multiblock/vehicle_workshop.png";
	private static final String TEXTURE_CRANE = ImmersiveIntelligence.MODID+":textures/blocks/multiblock/vehicle_workshop/crane.png";
	private static final String TEXTURE_INSERTER = ImmersiveIntelligence.MODID+":textures/blocks/multiblock/vehicle_workshop/inserter.png";
	private static ModelHeavyInserter modelInserter;
	private static ModelCrane modelCrane;

	@Override
	public void render(TileEntityVehicleWorkshop te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null&&!te.isDummy())
		{
			ClientUtils.bindTexture(TEXTURE);
			GlStateManager.enableLighting();

			GlStateManager.pushMatrix();
			GlStateManager.translate((float)x, (float)y-1, (float)z);
			GlStateManager.rotate(270f, 0F, 1F, 0F);

			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			if(te.hasWorld())
			{
				GlStateManager.translate(0f, 1f, 0f);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
			}

			ModelVehicleWorkshop model = te.mirrored?VehicleWorkshopRenderer.modelFlipped: VehicleWorkshopRenderer.model;

			model.getBlockRotation(te.facing, te.mirrored);
			for(ModelRendererTurbo mod : model.baseModel)
				mod.render();

			for(ModelRendererTurbo mod : model.drawer1Model)
				mod.render();

			for(ModelRendererTurbo mod : model.drawer2Model)
				mod.render();

			for(ModelRendererTurbo mod : model.engineModel)
				mod.render();

			for(ModelRendererTurbo mod : model.craneShaftModel)
				mod.render();

			for(ModelRendererTurbo mod : model.engineShaftModel)
				mod.render();

			for(ModelRendererTurbo mod : model.railModel)
				mod.render();

			for(ModelRendererTurbo mod : model.winchModel)
				mod.render();

			double ff = Math.abs((((te.getWorld().getTotalWorldTime()%50)+partialTicks)/50f)-0.5f)/0.5f;

			GlStateManager.pushMatrix();

			final float fh = (float)((Math.min((ff > 0.5?(ff-0.5): 0)/0.5, 1))*TmtUtil.AngleToTMT(-135))*(te.mirrored?-1: 1);
			for(ModelRendererTurbo mod : model.doorLeftModel)
			{
				mod.rotateAngleY = fh;
				mod.render();
			}

			GlStateManager.translate(0, 0, Math.min(ff/0.25f, 1f)*0.0625f);
			GlStateManager.translate(-Math.min((ff > 0.25f?(ff-0.25f): 0)/0.25f, 1f)*(te.mirrored?-1: 1), 0, 0);
			for(ModelRendererTurbo mod : model.doorRightModel)
			{
				mod.rotateAngleY = fh;
				mod.render();
			}

			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			final float fg = (float)(ff*(0.34528023f));
			for(ModelRendererTurbo mod : model.scissor1Model)
			{
				//mod.rotateAngleX=0.10471976F;
				mod.rotateAngleX = 0.10471976F+fg;
				//mod.rotateAngleX=-0.5f;
				mod.render();
			}

			for(ModelRendererTurbo mod : model.scissor2Model)
			{
				mod.rotateAngleX = 0.10471976F+fg;
				mod.render();
			}

			GlStateManager.translate(0f, ff*0.5f, 0);

			for(ModelRendererTurbo mod : model.platformModel)
				mod.render();


			GlStateManager.popMatrix();

			if(te.mirrored)
			{
				GlStateManager.translate(-2.5, 1f, -3.375);
				renderCrane(0, 1, 0, 0, () -> {
				});
				GlStateManager.translate(-1f, 0, 0);
				renderInserter(0, 0, 0, 0, () -> {
				});
			}
			else
			{
				GlStateManager.translate(2.5, 1f, -3.375);
				renderCrane(0, 1.25f, 0, 0, () -> {
				});
				GlStateManager.translate(1f, 0, 0);
				renderInserter(0, 0, 0, 0, () -> {
				});
			}

			//float f = te.calculateInserterAnimation(partialTicks);


			GlStateManager.popMatrix();
		}

	}

	@Override
	public void reloadModels()
	{
		model = new ModelVehicleWorkshop();
		modelFlipped = new ModelVehicleWorkshop();
		modelFlipped.flipAllX();

		modelInserter = new ModelHeavyInserter();
		modelCrane = new ModelCrane();
	}

	public static void renderCrane(float yaw, float distance, float drop, float grabProgress, Runnable function)
	{
		GlStateManager.pushMatrix();
		GlStateManager.rotate(180+yaw, 0, 1, 0);
		ClientUtils.bindTexture(TEXTURE_CRANE);

		for(ModelRendererTurbo mod : modelCrane.craneMainModel)
			mod.render(0.0625f);
		for(ModelRendererTurbo mod : modelCrane.exhaustModel)
			mod.render(0.0625f);
		for(ModelRendererTurbo mod : modelCrane.shaftModel)
			mod.render(0.0625f);

		GlStateManager.translate(0, 0, 1f-distance);

		for(ModelRendererTurbo mod : modelCrane.craneArmModel)
			mod.render(0.0625f);
		for(ModelRendererTurbo mod : modelCrane.craneArmShaftModel)
			mod.render(0.0625f);

		GlStateManager.translate(0, -drop, 0);

		for(ModelRendererTurbo mod : modelCrane.grabberModel)
			mod.render(0.0625f);

		GlStateManager.translate(0, 1.25+0.0625, 0);

		ClientUtils.mc().getTextureManager().bindTexture(new ResourceLocation("immersiveengineering:textures/blocks/wire.png"));
		GlStateManager.disableCull();
		BufferBuilder buffer = Tessellator.getInstance().getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		drawRope(buffer, 0, 0, -1.59375, 0, drop+0.25, -1.59375, 0.0625, 0);
		drawRope(buffer, 0, 0, -1.59375, 0, drop+0.25, -1.59375, 0, 0.0625);
		Tessellator.getInstance().draw();
		GlStateManager.enableCull();

		GlStateManager.popMatrix();
	}

	public static void renderInserter(float yaw, float pitch1, float pitch2, float progress, Runnable function)
	{
		GlStateManager.pushMatrix();
		ClientUtils.bindTexture(TEXTURE_INSERTER);

		GlStateManager.rotate(yaw, 0, 1, 0);
		for(ModelRendererTurbo mod : modelInserter.baseModel)
			mod.render(0.0625f);

		GlStateManager.translate(0f, 0.385f, 0);
		GlStateManager.rotate(15+55*progress, 1, 0, 0);

		for(ModelRendererTurbo mod : modelInserter.inserterLowerArmModel)
			mod.render(0.0625f);

		GlStateManager.translate(0f, 1.0f, -0.0625f);
		GlStateManager.rotate(135-(95f*progress), 1, 0, 0);
		GlStateManager.translate(0f, 0.0625f, 0.03125f);

		for(ModelRendererTurbo mod : modelInserter.inserterUpperArmModel)
			mod.render(0.0625f);

		for(ModelRendererTurbo mod : modelInserter.boxDoorLeftModel)
			mod.render(0.0625f);
		for(ModelRendererTurbo mod : modelInserter.boxDoorRightModel)
			mod.render(0.0625f);

		GlStateManager.pushMatrix();

		GlStateManager.translate(0.125f, -0.03125f, -0.03125f);

		GlStateManager.rotate(-45f*progress, 0f, 0f, 1f);

		GlStateManager.popMatrix();

		function.run();

		GlStateManager.popMatrix();
	}
}
