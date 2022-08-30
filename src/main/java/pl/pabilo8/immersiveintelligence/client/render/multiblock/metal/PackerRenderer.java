package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import blusunrize.immersiveengineering.api.tool.ConveyorHandler;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.ConveyorDirection;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.IConveyorBelt;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.models.ModelConveyor;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.Packer;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.api.utils.MachineUpgrade;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.metal.ModelPacker;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblocks.metal.tileentities.first.TileEntityPacker;

import java.util.List;

/**
 * @author Pabilo8
 * @since 21-06-2019
 */
public class PackerRenderer extends TileEntitySpecialRenderer<TileEntityPacker> implements IReloadableModelContainer<PackerRenderer>
{
	static RenderItem renderItem = ClientUtils.mc().getRenderItem();
	private static ModelPacker model;
	private static ModelPacker modelFlipped;
	private static final ResourceLocation TEXTURE = new ResourceLocation("immersiveintelligence:textures/blocks/multiblock/packer.png");

	@Override
	public void render(TileEntityPacker te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null&&!te.isDummy())
		{
			IIClientUtils.bindTexture(TEXTURE);
			GlStateManager.pushMatrix();
			GlStateManager.translate((float)x+1, (float)y-1, (float)z+2);
			GlStateManager.rotate(180F, 0F, 1F, 0F);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			double pp = (te.processTime+partialTicks)/(double)Packer.actionTime;

			boolean conveyorRunning = pp==0;
			float conveyorProgress = 0.5f, clampProgress = 0.5f, clampGrab = 0;

			//conveyor action
			if(pp < 0.125f)
			{
				conveyorProgress = (float)((pp/0.125)*0.5f);
				conveyorRunning = true;
			}
			//clamps drop 0
			else if(pp < 0.2)
			{
				clampProgress = (float)((1d-((pp-0.125)/0.075))*0.5);
			}
			//clamps grab
			else if(pp < 0.25)
			{
				clampProgress = 0;
				clampGrab = (float)((pp-0.2)/0.05);
			}
			//clamps up 1
			else if(pp < 0.4)
			{
				clampProgress = (float)(((pp-0.25)/0.15));
				clampGrab = 1;
			}
			//packer action
			else if(pp < 0.65)
			{
				clampProgress = 1;
				clampGrab = 1;
			}
			//clamps down 0
			else if(pp < 0.75)
			{
				clampProgress = (float)(1d-((pp-0.65)/0.1));
				clampGrab = 1;
			}
			//clamps drop, doors shut
			else if(pp < 0.8)
			{
				clampProgress = 0;
				clampGrab = 1f-(float)((pp-0.75)/0.05);
			}
			//clamp up 0.5
			else if(pp < 0.875)
			{
				clampProgress = (float)((((pp-0.8)/0.075))*0.5);
			}
			//conveyor action
			else
			{
				conveyorProgress = 0.5f+((float)((pp-(0.875))/0.125)*0.5f);
				conveyorRunning = true;
			}

			if(te.hasWorld())
			{
				GlStateManager.translate(0f, 1f, 1f);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
			}

			ModelPacker modelCurrent = te.mirrored?modelFlipped: model;

			modelCurrent.getBlockRotation(te.facing, te.mirrored);
			for(ModelRendererTurbo mod : modelCurrent.baseModel)
				mod.render();

			GlStateManager.pushMatrix();
			GlStateManager.translate(0, clampProgress*1.4725, 0);
			for(ModelRendererTurbo mod : modelCurrent.clampModel)
				mod.render();
			GlStateManager.translate(clampGrab*(te.mirrored?-0.1:0.1), 0, 0);
			for(ModelRendererTurbo mod : modelCurrent.clampLeftModel)
				mod.render();
			GlStateManager.translate(clampGrab*(te.mirrored?0.2:-0.2), 0, 0);
			for(ModelRendererTurbo mod : modelCurrent.clampRightModel)
				mod.render();
			GlStateManager.popMatrix();

			for(ModelRendererTurbo mod : modelCurrent.loaderLidLeftModel)
			{
				mod.rotateAngleZ = clampGrab*(te.mirrored?1.65f:-1.65f);
				mod.render();
			}
			for(ModelRendererTurbo mod : modelCurrent.loaderLidRightModel)
			{
				mod.rotateAngleZ = clampGrab*(te.mirrored?-1.65f:1.65f);
				mod.render();
			}

			for(ModelRendererTurbo mod : modelCurrent.markerInputModel)
				mod.render();

			boolean uDir = te.hasUpgrade(IIContent.UPGRADE_UNPACKER_CONVERSION);

			IConveyorBelt con = ConveyorHandler.getConveyor(new ResourceLocation("immersiveengineering:conveyor"), null);
			List<BakedQuad> quads = ModelConveyor.getBaseConveyor(uDir?EnumFacing.NORTH:EnumFacing.SOUTH, 1, new Matrix4(uDir?EnumFacing.NORTH:EnumFacing.SOUTH), ConveyorDirection.HORIZONTAL,
					ClientUtils.getSprite(conveyorRunning?con.getActiveTexture(): con.getInactiveTexture()), new boolean[]{true, true}, new boolean[]{true, true}, null, 0);


			ClientUtils.bindTexture("textures/atlas/blocks.png");
			GlStateManager.pushMatrix();
			GlStateManager.translate(te.mirrored?-1.0f: 0, 0f, -2.0f);
			ClientUtils.renderQuads(quads, 1, 1, 1, 1);
			GlStateManager.translate(te.mirrored?0.001f: -0.001f, 0, 1);
			ClientUtils.renderQuads(quads, 1, 1, 1, 1);
			GlStateManager.popMatrix();

			ClientUtils.bindTexture("textures/atlas/blocks.png");
			GlStateManager.pushMatrix();
			GlStateManager.translate(te.mirrored?1f: 0, -1.0f, 1f);

			quads = ModelConveyor.getBaseConveyor(te.mirrored?EnumFacing.WEST: EnumFacing.EAST, 1, new Matrix4(te.mirrored?EnumFacing.WEST: EnumFacing.EAST), ConveyorDirection.HORIZONTAL,
					ClientUtils.getSprite(conveyorRunning?con.getActiveTexture(): con.getInactiveTexture()), new boolean[]{true, true}, new boolean[]{true, true}, null, 0);

			for(int i = 0; i < 3; i++)
			{
				ClientUtils.renderQuads(quads, 1, 1, 1, 1);
				GlStateManager.translate(-1.0f, 0, 0);

			}

			if(!te.inventory.get(0).isEmpty())
			{
				GlStateManager.translate(1, 0.5625f, 0.5f);

				GlStateManager.translate((te.mirrored?1f-conveyorProgress:conveyorProgress)*3, clampGrab==1?(clampProgress*1.4725): 0, 0);

				GlStateManager.pushMatrix();
				GlStateManager.scale(0.85, 0.85, 0.85);
				renderItem.renderItem(te.inventory.get(0), TransformType.NONE);
				GlStateManager.popMatrix();
			}

			GlStateManager.popMatrix();


			GlStateManager.popMatrix();

			//IILogger.info(ImmersiveEngineering.proxy.drawConveyorInGui("immersiveengineering:conveyor", te.facing));
		}
	}

	// TODO: 30.12.2021 actually model the upgrades
	public static void renderWithUpgrades(MachineUpgrade[] upgrades)
	{
		IIClientUtils.bindTexture(TEXTURE);
		GlStateManager.pushMatrix();
		GlStateManager.scale(0.5, 0.5, 0.5);
		GlStateManager.translate(0.5, 1, 0.5);

		for(ModelRendererTurbo mod : model.baseModel)
			mod.render();
		GlStateManager.pushMatrix();
		for(ModelRendererTurbo mod : model.clampModel)
			mod.render();
		for(ModelRendererTurbo mod : model.clampLeftModel)
			mod.render();
		for(ModelRendererTurbo mod : model.clampRightModel)
			mod.render();
		GlStateManager.popMatrix();

		IConveyorBelt con = ConveyorHandler.getConveyor(new ResourceLocation("immersiveengineering:conveyor"), null);
		List<BakedQuad> quads = ModelConveyor.getBaseConveyor(EnumFacing.SOUTH, 1, new Matrix4(EnumFacing.SOUTH), ConveyorDirection.HORIZONTAL,
				ClientUtils.getSprite(con.getActiveTexture()), new boolean[]{true, true}, new boolean[]{true, true}, null, 0);


		ClientUtils.bindTexture("textures/atlas/blocks.png");
		GlStateManager.pushMatrix();
		GlStateManager.translate(0, 0f, -2.0f);
		ClientUtils.renderQuads(quads, 1, 1, 1, 1);
		GlStateManager.translate(-0.001f, 0, 1);
		ClientUtils.renderQuads(quads, 1, 1, 1, 1);
		GlStateManager.popMatrix();

		ClientUtils.bindTexture("textures/atlas/blocks.png");
		GlStateManager.pushMatrix();
		GlStateManager.translate(0, -1.0f, 1f);

		quads = ModelConveyor.getBaseConveyor(EnumFacing.EAST, 1, new Matrix4(EnumFacing.EAST), ConveyorDirection.HORIZONTAL,
				ClientUtils.getSprite(con.getActiveTexture()), new boolean[]{true, true}, new boolean[]{true, true}, null, 0);

		for(int i = 0; i < 3; i++)
		{
			ClientUtils.renderQuads(quads, 1, 1, 1, 1);
			GlStateManager.translate(-1.0f, 0, 0);

		}

		GlStateManager.popMatrix();

		GlStateManager.popMatrix();
	}

	@Override
	public void reloadModels()
	{
		model = new ModelPacker();
		model.flipAllX();
		model.baseModel[28].rotationPointY *= -1;
		model.baseModel[29].rotationPointY *= -1;
		model.flipX(new ModelRendererTurbo[]{model.baseModel[23], model.baseModel[51]});

		model.baseModel[23].rotateAngleY += 3.14f;
		model.baseModel[51].rotateAngleY += 3.14f;
		model.baseModel[23].rotationPointX -= 16;
		model.baseModel[51].rotationPointX -= 16;
		model.loaderLidLeftModel[0].rotationPointY *= -1;

		model.translate(model.clampModel, 0, -14, 0);
		model.translate(model.clampLeftModel, 0, -14, 0);
		model.translate(model.clampRightModel, 0, -14, 0);

		modelFlipped = new ModelPacker();

		modelFlipped.translate(modelFlipped.clampModel, 0, -14, 0);
		modelFlipped.translate(modelFlipped.clampLeftModel, 0, -14, 0);
		modelFlipped.translate(modelFlipped.clampRightModel, 0, -14, 0);


	}
}
