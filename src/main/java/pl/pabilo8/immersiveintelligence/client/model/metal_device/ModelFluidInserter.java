package pl.pabilo8.immersiveintelligence.client.model.metal_device;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;

/**
 * @author Pabilo8
 * @since 17-07-2019
 */
public class ModelFluidInserter extends ModelIIBase
{
	public ModelRendererTurbo[] inserterGaugeArrow, inserterOutput, inserterInput;
	int textureX = 64;
	int textureY = 64;

	public ModelFluidInserter() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[5];
		baseModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // BaseBox
		baseModel[1] = new ModelRendererTurbo(this, 0, 45, textureX, textureY); // BaseBoxTopMain
		baseModel[2] = new ModelRendererTurbo(this, 0, 19, textureX, textureY); // BaseBoxGauge
		baseModel[3] = new ModelRendererTurbo(this, 0, 23, textureX, textureY); // BaseBoxGaugePipe
		baseModel[4] = new ModelRendererTurbo(this, 8, 34, textureX, textureY); // BaseBoxTop

		baseModel[0].addBox(0F, 0F, 0F, 16, 3, 16, 0F); // BaseBox
		baseModel[0].setRotationPoint(0F, -3F, 0F);

		baseModel[1].addBox(-4F, 0F, -4F, 8, 10, 8, 0F); // BaseBoxTopMain
		baseModel[1].setRotationPoint(8F, -14F, 8F);

		baseModel[2].addBox(-4F, 0F, -4F, 8, 1, 8, 0F); // BaseBoxGauge
		baseModel[2].setRotationPoint(8F, -16F, 8F);

		baseModel[3].addBox(-1F, 0F, -1F, 2, 1, 2, 0F); // BaseBoxGaugePipe
		baseModel[3].setRotationPoint(8F, -15F, 8F);

		baseModel[4].addBox(-5F, 0F, -5F, 10, 1, 10, 0F); // BaseBoxTop
		baseModel[4].setRotationPoint(8F, -4F, 8F);


		inserterGaugeArrow = new ModelRendererTurbo[1];
		inserterGaugeArrow[0] = new ModelRendererTurbo(this, 0, 19, textureX, textureY); // BaseBoxGaugeArrow

		inserterGaugeArrow[0].addBox(-0.5F, 0F, -3F, 1, 1, 3, 0F); // BaseBoxGaugeArrow
		inserterGaugeArrow[0].setRotationPoint(8F, -16.5F, 8F);


		inserterOutput = new ModelRendererTurbo[2];
		inserterOutput[0] = new ModelRendererTurbo(this, 38, 28, textureX, textureY); // BaseOutputBox
		inserterOutput[1] = new ModelRendererTurbo(this, 28, 22, textureX, textureY); // BaseOutputBox

		inserterOutput[0].addBox(7F, -4F, -4F, 1, 8, 8, 0F); // BaseOutputBox
		inserterOutput[0].setRotationPoint(8F, -8F, 8F);

		inserterOutput[1].addBox(4F, -3F, -3F, 3, 6, 6, 0F); // BaseOutputBox
		inserterOutput[1].setRotationPoint(8F, -8F, 8F);


		inserterInput = new ModelRendererTurbo[2];
		inserterInput[0] = new ModelRendererTurbo(this, 0, 28, textureX, textureY); // BaseInputBox
		inserterInput[1] = new ModelRendererTurbo(this, 28, 22, textureX, textureY); // BaseInputBox

		inserterInput[0].addBox(7F, -4F, -4F, 1, 8, 8, 0F); // BaseInputBox
		inserterInput[0].setRotationPoint(8F, -8F, 8F);

		inserterInput[1].addBox(4F, -3F, -3F, 3, 6, 6, 0F); // BaseInputBox
		inserterInput[1].setRotationPoint(8F, -8F, 8F);

		flipAll();

	}

	@Override
	public void flipAll()
	{
		super.flipAll();
		flip(inserterGaugeArrow);
		flip(inserterOutput);
		flip(inserterInput);
	}

	@Override
	public void translateAll(float x, float y, float z)
	{
		//super.translateAll(x, y, z);
		translate(inserterGaugeArrow, x, y, z);
		translate(inserterOutput, x, y, z);
		translate(inserterInput, x, y, z);
	}

	@Override
	public void rotateAll(float x, float y, float z)
	{
		//super.rotateAll(x, y, z);
	}

	@Override
	public void render()
	{
		super.render();
		float f5 = 1F/16F;
		//for(ModelRendererTurbo model : inserterBaseTurntable)
		//	model.render(f5);

	}

	@Override
	public void getBlockRotation(EnumFacing facing, boolean mirrored)
	{
		GlStateManager.rotate(180F, 0F, 1F, 0F);
	}
}
