package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.Emplacement;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.metal_device.ModelInserter;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.metal.ModelEmplacement;
import pl.pabilo8.immersiveintelligence.client.model.weapon.emplacement.ModelAutocannon;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.tmt.TmtUtil;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.TileEntityEmplacement;

public class EmplacementRenderer extends TileEntitySpecialRenderer<TileEntityEmplacement> implements IReloadableModelContainer<EmplacementRenderer>
{
	private static ModelEmplacement model;
	public static ModelAutocannon modelAutocannon;
	public static ModelInserter modelInserter;

	private static final String texture = ImmersiveIntelligence.MODID+":textures/blocks/multiblock/emplacement.png";
	public static final String textureAutocannon = ImmersiveIntelligence.MODID+":textures/blocks/multiblock/emplacement/flak.png";
	public static final String textureInserter = ImmersiveIntelligence.MODID+":textures/blocks/multiblock/emplacement/inserter.png";

	private static final float doorAngle = TmtUtil.AngleToTMT(165f);

	@Override
	public void render(TileEntityEmplacement te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null)
		{
			if(te.isDummy())
				return;
			ClientUtils.bindTexture(texture);
			GlStateManager.pushMatrix();
			GlStateManager.translate(x, y-2, z);
			GlStateManager.rotate(180F, 0F, 1F, 0F);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			//float f = ((getWorld().getTotalWorldTime()+partialTicks)%240)/240f;
			float f = MathHelper.clamp((te.progress+(te.isDoorOpened?partialTicks: -partialTicks))/(float)Emplacement.lidTime, 0f, 1f);
			float door = 0;
			float turretHeight = 0;
			if(f <= 0.25f)
				door = f/0.25f;
			else if(f <= 0.65)
			{
				door = 1;
				turretHeight = (f-0.25f)/0.4f;
			}
			else if(f <= 0.85)
			{
				door = 1f-((f-0.65f)/0.2f);
				turretHeight = 1;
			}
			else if(f!=1)
			{
				turretHeight = 1f-(0.35f*((f-0.85f)/0.15f));
			}
			else
				turretHeight = 0.65f;

			if(te.hasWorld())
			{
				GlStateManager.translate(0f, 1f, 1f);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
			}

			model.getBlockRotation(te.facing, false);
			for(ModelRendererTurbo mod : model.baseModel)
				mod.render();

			//door
			for(ModelRendererTurbo mod : model.doorLeftModel)
			{
				mod.rotateAngleX = -door*doorAngle;
				mod.render();
			}
			for(ModelRendererTurbo mod : model.doorRightModel)
			{
				mod.rotateAngleX = door*doorAngle;
				mod.render();
			}
			GlStateManager.translate(0, 4.5f*turretHeight-4f, 0);
			for(ModelRendererTurbo mod : model.platformModel)
			{
				mod.render();
			}

			GlStateManager.translate(0.5, 3.0625f, -1.5);
			if(turretHeight <= 0.25f)
				GlStateManager.scale(1f, 0.85f+turretHeight, 1f);
			if(te.currentWeapon!=null)
				te.currentWeapon.render(te, partialTicks);



			GlStateManager.popMatrix();

		}
		else
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(x-0.35, y-1.1, z-0.35);
			GlStateManager.rotate(90, 0, 1, 0);
			GlStateManager.rotate(-7.5f, 0, 0, 1);
			GlStateManager.rotate(-7.5f, 1, 0, 0);
			GlStateManager.scale(0.4, 0.4, 0.4);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			ClientUtils.bindTexture(texture);
			model.render();
			for(ModelRendererTurbo mod : model.doorLeftModel)
			{
				mod.rotateAngleX = 0;
				mod.render();
			}
			for(ModelRendererTurbo mod : model.doorRightModel)
			{
				mod.rotateAngleX = 0;
				mod.render();
			}

			GlStateManager.popMatrix();
		}
	}

	@Override
	public void reloadModels()
	{
		model = new ModelEmplacement();
		modelAutocannon = new ModelAutocannon();
		modelInserter = new ModelInserter();
	}

	public static void renderInserter(float yaw, float pitch1, float pitch2, float progress, Runnable function)
	{
		GlStateManager.pushMatrix();
		ClientUtils.bindTexture(textureInserter);

		GlStateManager.translate(-0.5, -0.5, 0.5);
		modelInserter.baseModel[1].render();
		GlStateManager.translate(0.5, 0.385, -0.5);

		GlStateManager.rotate(yaw, 0, 1, 0);
		for(ModelRendererTurbo mod : modelInserter.inserterBaseTurntable)
			mod.render(0.0625f);

		GlStateManager.translate(0f, 0.125f, 0);

		GlStateManager.rotate(pitch1, 1, 0, 0);
		for(ModelRendererTurbo mod : modelInserter.inserterLowerArm)
			mod.render(0.0625f);

		GlStateManager.translate(0f, 0.875f, 0);
		GlStateManager.rotate(-pitch1, 1, 0, 0);
		GlStateManager.translate(0f, 0.0625f, 0.03125f);

		for(ModelRendererTurbo mod : modelInserter.inserterMidAxle)
			mod.render(0.0625f);

		GlStateManager.rotate(pitch2, 1, 0, 0);

		for(ModelRendererTurbo mod : modelInserter.inserterUpperArm)
			mod.render(0.0625f);

		GlStateManager.translate(0f, 0.625f, 0.03125f);

		GlStateManager.pushMatrix();

		GlStateManager.translate(0.125f, -0.03125f, -0.03125f);

		GlStateManager.rotate(-45f*progress, 0f, 0f, 1f);

		for(ModelRendererTurbo mod : modelInserter.inserterItemPicker1)
			mod.render(0.0625f);

		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();

		GlStateManager.translate(-0.125f, -0.03125f, -0.03125f);

		GlStateManager.rotate(45f*progress, 0f, 0f, 1f);

		for(ModelRendererTurbo mod : modelInserter.inserterItemPicker2)
			mod.render(0.0625f);

		GlStateManager.popMatrix();

		function.run();

		GlStateManager.popMatrix();
	}
}
