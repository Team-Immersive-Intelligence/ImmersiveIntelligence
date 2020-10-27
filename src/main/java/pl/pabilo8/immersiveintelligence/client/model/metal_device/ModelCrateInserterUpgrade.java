package pl.pabilo8.immersiveintelligence.client.model.metal_device;

import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.ModelBlockBase;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;

/**
 * @author Pabilo8
 * @since 06.09.2020
 */
public class ModelCrateInserterUpgrade extends ModelBlockBase
{
	public static final String texture = ImmersiveIntelligence.MODID+":textures/blocks/metal_device/crate_inserter_upgrade.png";

	int textureX = 64;
	int textureY = 32;

	public ModelCrateInserterUpgrade() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[12];
		baseModel[0] = new ModelRendererTurbo(this, 6, 0, textureX, textureY); // Box 0
		baseModel[1] = new ModelRendererTurbo(this, 6, 0, textureX, textureY); // Box 0
		baseModel[2] = new ModelRendererTurbo(this, 6, 0, textureX, textureY); // Box 0
		baseModel[3] = new ModelRendererTurbo(this, 6, 0, textureX, textureY); // Box 0
		baseModel[4] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[5] = new ModelRendererTurbo(this, 0, 20, textureX, textureY); // Box 5
		baseModel[6] = new ModelRendererTurbo(this, 40, 15, textureX, textureY); // Box 5
		baseModel[7] = new ModelRendererTurbo(this, 42, 4, textureX, textureY); // Box 8
		baseModel[8] = new ModelRendererTurbo(this, 40, 11, textureX, textureY); // Box 0
		baseModel[9] = new ModelRendererTurbo(this, 40, 11, textureX, textureY); // Box 0
		baseModel[10] = new ModelRendererTurbo(this, 24, 22, textureX, textureY); // Box 8
		baseModel[11] = new ModelRendererTurbo(this, 42, 0, textureX, textureY); // Box 8

		baseModel[0].addBox(0F, 0F, 0F, 2, 12, 2, 0F); // Box 0
		baseModel[0].setRotationPoint(0F, -12F, 0F);

		baseModel[1].addBox(0F, 0F, 0F, 2, 12, 2, 0F); // Box 0
		baseModel[1].setRotationPoint(14F, -12F, 0F);

		baseModel[2].addBox(0F, 0F, 0F, 2, 12, 2, 0F); // Box 0
		baseModel[2].setRotationPoint(0F, -12F, 14F);

		baseModel[3].addBox(0F, 0F, 0F, 2, 12, 2, 0F); // Box 0
		baseModel[3].setRotationPoint(14F, -12F, 14F);

		baseModel[4].addBox(0F, 0F, 0F, 14, 1, 14, 0F); // Box 0
		baseModel[4].setRotationPoint(1F, -11F, 1F);

		baseModel[5].addBox(0F, 0F, 0F, 8, 4, 8, 0F); // Box 5
		baseModel[5].setRotationPoint(4F, -12F, 4F);

		baseModel[6].addBox(0F, 0F, 0F, 6, 2, 6, 0F); // Box 5
		baseModel[6].setRotationPoint(5F, -14F, 5F);

		baseModel[7].addBox(0F, 0F, 0F, 8, 8, 2, 0F); // Box 8
		baseModel[7].setRotationPoint(4F, -12F, 0F);

		baseModel[8].addBox(0F, 0F, 0F, 0, 9, 12, 0F); // Box 0
		baseModel[8].setRotationPoint(15F, -10F, 2F);

		baseModel[9].addBox(0F, 0F, 0F, 0, 9, 12, 0F); // Box 0
		baseModel[9].setRotationPoint(1F, -10F, 2F);

		baseModel[10].addBox(0F, 0F, 0F, 6, 4, 2, 0F); // Box 8
		baseModel[10].setRotationPoint(5F, -10F, 2F);

		baseModel[11].addBox(0F, 0F, 0F, 4, 3, 1, 0F); // Box 8
		baseModel[11].setRotationPoint(6F, -4F, 1F);

		flipAll();
	}
}
