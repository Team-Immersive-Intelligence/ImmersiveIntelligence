package pl.pabilo8.immersiveintelligence.client.model.metal_device;

import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Coord2D;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Shape2D;

/**
 * Created by Pabilo8 on 2019-05-26.'
 * Created using SMP-Toolbox 2.0 (Old stuff, probably considered dead ^^)
 * Thanks to The Flan's Mod Team for the Turbo Model Thingy used in there
 */

public class ModelAmmunitionCrate extends ModelIIBase //Same as Filename
{
	int textureX = 64;
	int textureY = 32;

	public ModelRendererTurbo[] lidModel;

	public ModelAmmunitionCrate()
	{
		baseModel = new ModelRendererTurbo[14];
		baseModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // MainBox
		baseModel[1] = new ModelRendererTurbo(this, 0, 15, textureX, textureY); // BorderBox
		baseModel[2] = new ModelRendererTurbo(this, 0, 15, textureX, textureY); // BorderBox
		baseModel[3] = new ModelRendererTurbo(this, 40, 1, textureX, textureY); // BorderBox
		baseModel[4] = new ModelRendererTurbo(this, 40, 1, textureX, textureY); // BorderBox
		baseModel[5] = new ModelRendererTurbo(this, 0, 17, textureX, textureY); // Ammo
		baseModel[6] = new ModelRendererTurbo(this, 18, 17, textureX, textureY); // Holder
		baseModel[7] = new ModelRendererTurbo(this, 18, 17, textureX, textureY); // Holder
		baseModel[8] = new ModelRendererTurbo(this, 18, 19, textureX, textureY); // Holder2
		baseModel[9] = new ModelRendererTurbo(this, 18, 19, textureX, textureY); // Holder2
		baseModel[10] = new ModelRendererTurbo(this, 18, 19, textureX, textureY); // Holder2
		baseModel[11] = new ModelRendererTurbo(this, 18, 19, textureX, textureY); // Holder2
		baseModel[12] = new ModelRendererTurbo(this, 22, 19, textureX, textureY); // Shape 13
		baseModel[13] = new ModelRendererTurbo(this, 22, 19, textureX, textureY); // Shape 13

		baseModel[0].addBox(0F, 0F, 0F, 16, 7, 8, 0F); // MainBox
		baseModel[0].setRotationPoint(0F, -7F, 4F);

		baseModel[1].addBox(0F, 0F, 0F, 16, 1, 1, 0F); // BorderBox
		baseModel[1].setRotationPoint(0F, -8F, 4F);

		baseModel[2].addBox(0F, 0F, 0F, 16, 1, 1, 0F); // BorderBox
		baseModel[2].setRotationPoint(0F, -8F, 11F);

		baseModel[3].addBox(0F, 0F, 0F, 1, 1, 6, 0F); // BorderBox
		baseModel[3].setRotationPoint(0F, -8F, 5F);

		baseModel[4].addBox(0F, 0F, 0F, 1, 1, 6, 0F); // BorderBox
		baseModel[4].setRotationPoint(15F, -8F, 5F);

		baseModel[5].addBox(0F, 0F, 0F, 6, 1, 6, 0F); // Ammo
		baseModel[5].setRotationPoint(1F, -7F, 5F);
		baseModel[5].rotateAngleZ = 0.05235988F;

		baseModel[6].addBox(0F, 0F, 0F, 6, 1, 1, 0F); // Holder
		baseModel[6].setRotationPoint(5F, -6F, 11.5F);

		baseModel[7].addBox(0F, 0F, 0F, 6, 1, 1, 0F); // Holder
		baseModel[7].setRotationPoint(5F, -6F, 3.5F);

		baseModel[8].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // Holder2
		baseModel[8].setRotationPoint(5F, -5F, 3.5F);

		baseModel[9].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // Holder2
		baseModel[9].setRotationPoint(10F, -5F, 3.5F);

		baseModel[10].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // Holder2
		baseModel[10].setRotationPoint(5F, -5F, 11.5F);

		baseModel[11].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // Holder2
		baseModel[11].setRotationPoint(10F, -5F, 11.5F);

		baseModel[12].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[] { new Coord2D(1, 0, 1, 0), new Coord2D(5, 0, 5, 0), new Coord2D(6, 1, 6, 1), new Coord2D(0, 1, 0, 1) }), 1, 6, 1, 14, 1, ModelRendererTurbo.MR_FRONT, new float[] {2 ,6 ,2 ,4}); // Shape 13
		baseModel[12].setRotationPoint(11F, -3F, 12.5F);

		baseModel[13].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(1, 0, 1, 0), new Coord2D(5, 0, 5, 0), new Coord2D(6, 1, 6, 1), new Coord2D(0, 1, 0, 1)}), 1, 6, 1, 14, 1, ModelRendererTurbo.MR_FRONT, new float[]{2, 6, 2, 4}); // Shape 13
		baseModel[13].setRotationPoint(11F, -3F, 4.5F);

		lidModel = new ModelRendererTurbo[2];
		lidModel[0] = new ModelRendererTurbo(this, 16, 21, textureX, textureY); // BorderBox
		lidModel[1] = new ModelRendererTurbo(this, 0, 24, textureX, textureY); // BorderBoxHandle

		lidModel[0].addBox(-1F, -1F, -4F, 16, 1, 8, 0F); // TopLid
		lidModel[1].addBox(14.5F, -0.5F, -2F, 1, 2, 4, 0F); // TopLidHandle

		parts.put("base", baseModel);
		parts.put("lid", lidModel);

		flipAll();
	}
}