package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.utils.MachineUpgrade;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.metal.ModelRadar;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.util.ShaderUtil;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Coord2D;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Shape2D;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityRadar;

/**
 * @author Pabilo8
 * @since 21-06-2019
 */
public class RadarRenderer extends TileEntitySpecialRenderer<TileEntityRadar> implements IReloadableModelContainer<RadarRenderer>
{
	private static final String TEXTURE = ImmersiveIntelligence.MODID+":textures/blocks/multiblock/radar.png";
	private static ModelRadar model, modelFlipped;
	private static ModelRendererTurbo[] modelConstruction, modelConstructionFlipped;

	@Override
	public void render(TileEntityRadar te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null&&!te.isDummy())
		{
			ClientUtils.bindTexture(TEXTURE);
			GlStateManager.pushMatrix();
			GlStateManager.translate((float)x, (float)y, (float)z);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			if(te.hasWorld())
			{
				GlStateManager.translate(0f, 0, 1f);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
			}

			float ff = (te.dishRotation+(te.active?partialTicks: 0));

			float rotMod = te.mirrored?-1: 1;
			ModelRadar modelCurrent = te.mirrored?modelFlipped: model;
			modelCurrent.getBlockRotation(te.facing, te.mirrored);

			if(!te.hasWorld()||te.isConstructionFinished())
			{
				for(ModelRendererTurbo mod : modelCurrent.baseModel)
					mod.render();

				if(te.hasUpgrade(IIContent.UPGRADE_RADIO_LOCATORS))
					for(ModelRendererTurbo mod : modelCurrent.triangulatorsModel)
						mod.render();

				GlStateManager.translate(3, 0, -1*rotMod);
				GlStateManager.rotate(ff, 0, 1, 0);
				for(ModelRendererTurbo mod : modelCurrent.radarModel)
					mod.render();
			}
			else
			{
				renderConstruction(te, partialTicks);
			}

			GlStateManager.popMatrix();
		}
		else if(te==null)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(x-0.25, y-0.25, z);
			GlStateManager.rotate(7.5f, 0, 0, 1);
			GlStateManager.rotate(-7.5f, 1, 0, 0);
			GlStateManager.scale(0.23, 0.23, 0.23);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
			ClientUtils.bindTexture(TEXTURE);

			for(ModelRendererTurbo mod : model.baseModel)
				mod.render();

			//GlStateManager.translate(1.0625f, 0, 1.3125f);

			for(ModelRendererTurbo mod : model.radarModel)
				mod.render();

			GlStateManager.popMatrix();
		}
	}

	@Override
	public void reloadModels()
	{
		model = new ModelRadar();
		model.flipZ(model.baseModel);
		for(ModelRendererTurbo mod : model.baseModel)
		{
			mod.rotateAngleX = -mod.rotateAngleX;
			mod.rotateAngleY = -mod.rotateAngleY;
		}

		model.baseModel[129].setRotationPoint(48F, 0F, -64f);
		model.baseModel[130].setRotationPoint(0F, 0F, -16f);

		model.baseModel[132].setRotationPoint(48F, 8F, -48F);
		model.baseModel[133].setRotationPoint(17F, 8F, -16F);

		model.baseModel[90].rotationPointY += 40;
		model.baseModel[90].rotationPointZ += 32;

		model.baseModel[149].clear();
		model.baseModel[149].setMirrored(true);
		model.baseModel[149].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(15, 0, 15, 0), new Coord2D(15, 4, 15, 4), new Coord2D(6, 4, 6, 4)}), 12, 15, 4, 36, 12, ModelRendererTurbo.MR_FRONT, new float[]{8, 9, 4, 15}); // TableKeyboardMain
		model.baseModel[149].setRotationPoint(-15.5F, 14F, 15);
		model.baseModel[149].rotateAngleY = -3.14159265F;

		modelFlipped = new ModelRadar();

		model.parts.remove("triangulators");
		modelFlipped.parts.remove("triangulators");

		modelConstruction = IIClientUtils.createConstructionModel(null, model);
		modelConstructionFlipped = IIClientUtils.createConstructionModel(null, modelFlipped);
		TileEntityRadar.PART_AMOUNT = modelConstruction.length;

		//ah yes, even in my own code there is this cheeky maneuvering
		model.parts.put("triangulators", model.triangulatorsModel);
		modelFlipped.parts.put("triangulators", modelFlipped.triangulatorsModel);

		IIContent.UPGRADE_RADIO_LOCATORS.setRequiredSteps(model.triangulatorsModel.length);

	}

	public void renderConstruction(TileEntityRadar te, float partialTicks)
	{
		ModelRendererTurbo[] tt = te.mirrored?modelConstructionFlipped: modelConstruction;
		double cc = (int)Math.min(te.clientConstruction+((partialTicks*(Tools.electricHammerEnergyPerUseConstruction/4.25f))), IIUtils.getMaxClientProgress(te.construction, te.getConstructionCost(), TileEntityRadar.PART_AMOUNT));
		double progress = MathHelper.clamp(cc/(float)te.getConstructionCost(), 0f, 1f);

		for(int i = 0; i < TileEntityRadar.PART_AMOUNT*progress; i++)
		{
			if(1+i > Math.round(TileEntityRadar.PART_AMOUNT*progress))
			{
				GlStateManager.pushMatrix();
				double scale = 1f-(((progress*TileEntityRadar.PART_AMOUNT)%1f));
				GlStateManager.enableBlend();
				GlStateManager.color(1f, 1f, 1f, (float)Math.min(scale*2, 1));
				GlStateManager.translate(0, scale*1.5f, 0);

				tt[i].render(0.0625f);
				GlStateManager.color(1f, 1f, 1f, 1f);
				GlStateManager.popMatrix();
			}
			else
				tt[i].render(0.0625f);
		}

		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.disableLighting();
		GlStateManager.scale(0.98f, 0.98f, 0.98f);
		GlStateManager.translate(0.0625f/2f, 0f, -0.0265f/2f);
		//float flicker = (te.getWorld().rand.nextInt(10)==0)?0.75F: (te.getWorld().rand.nextInt(20)==0?0.5F: 1F);

		ShaderUtil.useBlueprint(0.35f, ClientUtils.mc().player.ticksExisted+partialTicks);
		for(int i = modelConstruction.length-1; i >= Math.max(((modelConstruction.length-1)*progress)-1, 0); i--)
		{
			tt[i].render(0.0625f);
		}
		ShaderUtil.releaseShader();
		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();

	}

	public static void renderWithUpgrades(MachineUpgrade[] upgrades)
	{
		ClientUtils.bindTexture(TEXTURE);
		GlStateManager.pushMatrix();
		GlStateManager.translate(-0.5, -0.25f, 0);
		GlStateManager.scale(0.25f, 0.25f, 0.25f);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

		for(ModelRendererTurbo mod : modelFlipped.baseModel)
			mod.render();

		for(MachineUpgrade upgrade : upgrades)
		{
			if(upgrade==IIContent.UPGRADE_RADIO_LOCATORS)
			{
				for(ModelRendererTurbo mod : modelFlipped.triangulatorsModel)
					mod.render();
			}
		}

		GlStateManager.translate(3, 0, 1);
		for(ModelRendererTurbo mod : modelFlipped.radarModel)
			mod.render();


		GlStateManager.popMatrix();
	}
}
