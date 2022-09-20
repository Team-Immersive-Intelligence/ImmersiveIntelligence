package pl.pabilo8.immersiveintelligence.client.model.multiblock.metal.precision_assembler;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Coord2D;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Shape2D;

/**
 * @author Pabilo8
 * @since 17-07-2019
 */
public class ModelPrecisionDrill extends ModelIIBase
{
	public ModelRendererTurbo[] inserterLowerArm, inserterMidAxle, inserterUpperArm, inserterBaseTurntable, inserterDrill;
	int textureX = 64;
	int textureY = 32;

	public ModelPrecisionDrill() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[1];
		baseModel[0] = new ModelRendererTurbo(this, 0, 19, textureX, textureY); // BaseBoxTop

		baseModel[0].addBox(-4F, 0F, -4F, 8, 1, 8, 0F); // BaseBoxTop
		baseModel[0].setRotationPoint(8F, -1F, 8F);

		inserterBaseTurntable = new ModelRendererTurbo[4];
		inserterBaseTurntable[0] = new ModelRendererTurbo(this, 24, 20, textureX, textureY); // BaseTurntable
		inserterBaseTurntable[1] = new ModelRendererTurbo(this, 0, 24, textureX, textureY); // BaseTurning
		inserterBaseTurntable[2] = new ModelRendererTurbo(this, 0, 28, textureX, textureY); // Holder1
		inserterBaseTurntable[3] = new ModelRendererTurbo(this, 0, 28, textureX, textureY); // Holder2

		inserterBaseTurntable[0].addBox(-3F, 0F, -3F, 6, 1, 6, 0F); // BaseTurntable
		inserterBaseTurntable[1].addBox(-1F, 1F, -1F, 2, 1, 2, 0F); // BaseTurning
		inserterBaseTurntable[2].addShape3D(1.5F, 0F, 2F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(3, 0, 3, 0), new Coord2D(3, 2, 3, 2), new Coord2D(2, 3, 2, 3), new Coord2D(1, 3, 1, 3), new Coord2D(0, 2, 0, 2)}), 1, 3, 3, 12, 1, ModelRendererTurbo.MR_FRONT, new float[]{2, 2, 1, 2, 2, 3}); // Holder1
		inserterBaseTurntable[2].rotateAngleY = -1.57079633F;
		inserterBaseTurntable[3].addShape3D(1.5F, 0F, 2F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(3, 0, 3, 0), new Coord2D(3, 2, 3, 2), new Coord2D(2, 3, 2, 3), new Coord2D(1, 3, 1, 3), new Coord2D(0, 2, 0, 2)}), 1, 3, 3, 12, 1, ModelRendererTurbo.MR_FRONT, new float[]{2, 2, 1, 2, 2, 3}); // Holder2
		inserterBaseTurntable[3].rotateAngleY = -4.71238898F;

		inserterLowerArm = new ModelRendererTurbo[15];
		inserterLowerArm[0] = new ModelRendererTurbo(this, 18, 29, textureX, textureY); // InserterAxle
		inserterLowerArm[1] = new ModelRendererTurbo(this, 42, 23, textureX, textureY); // InserterArmBottom
		inserterLowerArm[2] = new ModelRendererTurbo(this, 42, 23, textureX, textureY); // InserterArmBottom
		inserterLowerArm[3] = new ModelRendererTurbo(this, 42, 23, textureX, textureY); // InserterArmBottom
		inserterLowerArm[4] = new ModelRendererTurbo(this, 42, 23, textureX, textureY); // InserterArmBottom
		inserterLowerArm[5] = new ModelRendererTurbo(this, 42, 23, textureX, textureY); // InserterArmBottom
		inserterLowerArm[6] = new ModelRendererTurbo(this, 42, 23, textureX, textureY); // InserterArmBottom
		inserterLowerArm[7] = new ModelRendererTurbo(this, 42, 19, textureX, textureY); // InserterArmBottom
		inserterLowerArm[8] = new ModelRendererTurbo(this, 42, 19, textureX, textureY); // InserterArmBottom
		inserterLowerArm[9] = new ModelRendererTurbo(this, 56, 19, textureX, textureY); // InserterArm1
		inserterLowerArm[10] = new ModelRendererTurbo(this, 56, 19, textureX, textureY); // InserterArm1
		inserterLowerArm[11] = new ModelRendererTurbo(this, 12, 28, textureX, textureY); // Holder2
		inserterLowerArm[12] = new ModelRendererTurbo(this, 12, 28, textureX, textureY); // Holder2
		inserterLowerArm[13] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // InserterArmBackplate
		inserterLowerArm[14] = new ModelRendererTurbo(this, 0, 9, textureX, textureY); // InserterArmBackplateProcessor

		inserterLowerArm[0].addBox(-3.5F, -0.5F, -0.5F, 7, 1, 1, 0F); // InserterAxle
		inserterLowerArm[1].addBox(-1.5F, -1F, -1.5F, 1, 2, 1, 0F); // InserterArmBottom
		inserterLowerArm[2].addBox(0.5F, -1F, -1.5F, 1, 2, 1, 0F); // InserterArmBottom
		inserterLowerArm[3].addBox(-1.5F, -1F, 0.5F, 1, 2, 1, 0F); // InserterArmBottom
		inserterLowerArm[4].addBox(0.5F, -1F, 0.5F, 1, 2, 1, 0F); // InserterArmBottom
		inserterLowerArm[5].addBox(-1.5F, -1F, 0.5F, 1, 2, 1, 0F); // InserterArmBottom
		inserterLowerArm[6].addBox(0.5F, -1F, 0.5F, 1, 2, 1, 0F); // InserterArmBottom
		inserterLowerArm[7].addFlexTrapezoid(-1.5F, -2F, -1.5F, 1, 1, 3, 0F, 0.00F, 0.00F, -1.00F, -1.00F, -1.00F, -1.00F, ModelRendererTurbo.MR_TOP); // InserterArmBottom
		inserterLowerArm[8].addFlexTrapezoid(0.5F, -2F, -1.5F, 1, 1, 3, 0F, 0.00F, 0.00F, -1.00F, -1.00F, -1.00F, -1.00F, ModelRendererTurbo.MR_TOP); // InserterArmBottom
		inserterLowerArm[9].addBox(-1.5F, -12F, -0.5F, 1, 10, 1, 0F); // InserterArm1
		inserterLowerArm[10].addBox(0.5F, -12F, -0.5F, 1, 10, 1, 0F); // InserterArm1

		inserterLowerArm[11].addShape3D(1.5F, 12F, 0.5F, new Shape2D(new Coord2D[]{new Coord2D(2, 0, 2, 0), new Coord2D(3, 1, 3, 1), new Coord2D(3, 2, 3, 2), new Coord2D(2, 3, 2, 3), new Coord2D(1, 3, 1, 3), new Coord2D(0, 2, 0, 2), new Coord2D(0, 1, 0, 1), new Coord2D(1, 0, 1, 0)}), 1, 3, 3, 12, 1, ModelRendererTurbo.MR_FRONT, new float[]{1, 2, 1, 2, 1, 2, 1, 2}); // Holder2
		inserterLowerArm[11].rotateAngleY = -4.71238898F;

		inserterLowerArm[12].addShape3D(1.5F, 12F, -1.5F, new Shape2D(new Coord2D[]{new Coord2D(2, 0, 2, 0), new Coord2D(3, 1, 3, 1), new Coord2D(3, 2, 3, 2), new Coord2D(2, 3, 2, 3), new Coord2D(1, 3, 1, 3), new Coord2D(0, 2, 0, 2), new Coord2D(0, 1, 0, 1), new Coord2D(1, 0, 1, 0)}), 1, 3, 3, 12, 1, ModelRendererTurbo.MR_FRONT, new float[]{1, 2, 1, 2, 1, 2, 1, 2}); // Holder2
		inserterLowerArm[12].rotateAngleY = -4.71238898F;

		inserterLowerArm[13].addBox(-3F, -11F, 0.5F, 6, 8, 1, 0F); // InserterArmBackplate
		inserterLowerArm[14].addBox(-2.5F, -9F, 1F, 5, 3, 1, 0F); // InserterArmBackplateProcessor

		inserterMidAxle = new ModelRendererTurbo[1];
		inserterMidAxle[0] = new ModelRendererTurbo(this, 34, 29, textureX, textureY); // InserterAxle2

		inserterMidAxle[0].addBox(-2F, 0F, -0.5F, 4, 1, 1, 0F); // InserterAxle2

		inserterUpperArm = new ModelRendererTurbo[2];
		inserterUpperArm[0] = new ModelRendererTurbo(this, 60, 19, textureX, textureY); // InserterArm2Top
		inserterUpperArm[1] = new ModelRendererTurbo(this, 14, 0, textureX, textureY); // InserterArm2Top

		inserterUpperArm[0].addBox(-0.5F, -7.5F, -0.5F, 1, 7, 1, 0F); // InserterArm2Top

		inserterUpperArm[1].addBox(-1.5F, -10.5F, -1.5F, 3, 3, 3, 0F); // InserterArm2Top


		inserterDrill = new ModelRendererTurbo[5];
		inserterDrill[0] = new ModelRendererTurbo(this, 26, 0, textureX, textureY); // InserterArmDrill
		inserterDrill[1] = new ModelRendererTurbo(this, 35, 4, textureX, textureY); // InserterArmDrill
		inserterDrill[2] = new ModelRendererTurbo(this, 36, 0, textureX, textureY); // InserterArmDrill
		inserterDrill[3] = new ModelRendererTurbo(this, 42, 0, textureX, textureY); // InserterArmDrill
		inserterDrill[4] = new ModelRendererTurbo(this, 42, 2, textureX, textureY); // InserterArmDrill


		inserterDrill[0].addBox(-2F, -2F, -1.5F, 4, 4, 1, 0F); // InserterArmDrill

		inserterDrill[1].addBox(-1.5F, -1.5F, -2.5F, 3, 3, 1, 0F); // InserterArmDrill
		inserterDrill[1].rotateAngleZ = 0.78539816F;

		inserterDrill[2].addBox(-1F, -1F, -3.5F, 2, 2, 1, 0F); // InserterArmDrill

		inserterDrill[3].addBox(-0.5F, -0.5F, -4.5F, 1, 1, 1, 0F); // InserterArmDrill
		inserterDrill[3].rotateAngleZ = 0.78539816F;

		inserterDrill[4].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, 0F); // InserterArmDrill
		inserterDrill[4].rotateAngleZ = 0.78539816F;

		flipAll();

	}

	@Override
	public void flipAll()
	{
		super.flipAll();
		flip(inserterMidAxle);
		flip(inserterLowerArm);
		flip(inserterUpperArm);
		flip(inserterBaseTurntable);
		flip(inserterDrill);
	}

	@Override
	public void translateAll(float x, float y, float z)
	{
		//super.translateAll(x, y, z);
		translate(inserterMidAxle, x, y, z);
		translate(inserterLowerArm, x, y, z);
		translate(inserterUpperArm, x, y, z);
		translate(inserterBaseTurntable, x, y, z);
		translate(inserterDrill, x, y, z);
	}

	@Override
	public void rotateAll(float x, float y, float z)
	{
		//super.rotateAll(x, y, z);
		rotate(inserterMidAxle, x, y, z);
		rotate(inserterLowerArm, x, y, z);
		rotate(inserterUpperArm, x, y, z);
		rotate(inserterBaseTurntable, x, y, z);
		rotate(inserterDrill, x, y, z);
	}

	@Override
	public void render()
	{
		super.render();
		//for(ModelRendererTurbo model : inserterBaseTurntable)
		//	model.render(f5);

	}

	public void renderProgress(float progress, float angle, float maxProgress)
	{
		GlStateManager.pushMatrix();

		ClientUtils.bindTexture(ImmersiveIntelligence.MODID+":textures/blocks/multiblock/precission_assembler/drill.png");

		render();

		float progress2 = progress < 0.25f?0f: progress > 0.75f?0f: (progress-0.25f)/0.5f;
		progress = progress < 0.33?progress*3: progress > 0.66?1f-((progress-0.66f)*3): 1f;

		GlStateManager.translate(0.5f, 0.125f, -0.5);
		GlStateManager.rotate(angle*progress, 0f, 1f, 0f);
		progress *= maxProgress;

		for(ModelRendererTurbo mod : inserterBaseTurntable)
			mod.render(0.0625f);

		GlStateManager.translate(0f, 0.125f, 0);
		GlStateManager.rotate(15+55*progress, 1, 0, 0);

		for(ModelRendererTurbo mod : inserterLowerArm)
			mod.render(0.0625f);

		GlStateManager.translate(0f, 0.875f, 0);
		GlStateManager.rotate(135-(95f*progress), 1, 0, 0);
		GlStateManager.translate(0f, 0.0625f, 0.03125f);

		for(ModelRendererTurbo mod : inserterMidAxle)
			mod.render(0.0625f);

		for(ModelRendererTurbo mod : inserterUpperArm)
			mod.render(0.0625f);

		GlStateManager.translate(0f, 0.625f, 0.03125f);

		GlStateManager.pushMatrix();

		GlStateManager.translate(0f, -0.03125f*2, 0.0625*1.5);

		GlStateManager.rotate(progress2*2880, 0f, 0f, 1f);
		for(ModelRendererTurbo mod : inserterDrill)
			mod.render(0.0625f);

		GlStateManager.popMatrix();

		GlStateManager.popMatrix();
	}

	@Override
	public void getBlockRotation(EnumFacing facing, boolean mirrored)
	{
		GlStateManager.rotate(180F, 0F, 1F, 0F);
	}
}
