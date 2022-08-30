package pl.pabilo8.immersiveintelligence.client.model.multiblock.metal;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Coord2D;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Shape2D;

/**
 * @author Pabilo8
 * @since 29.04.2021
 */
public class ModelFiller extends ModelIIBase
{
	public final ModelRendererTurbo[] fanModel;
	int textureX = 256;
	int textureY = 256;

	public ModelFiller() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[60];
		baseModel[0] = new ModelRendererTurbo(this, 64, 200, textureX, textureY); // Box 0
		baseModel[1] = new ModelRendererTurbo(this, 28, 84, textureX, textureY); // Box 0
		baseModel[2] = new ModelRendererTurbo(this, 40, 111, textureX, textureY); // Box 0
		baseModel[3] = new ModelRendererTurbo(this, 40, 111, textureX, textureY); // Box 0
		baseModel[4] = new ModelRendererTurbo(this, 40, 111, textureX, textureY); // Box 0
		baseModel[5] = new ModelRendererTurbo(this, 52, 102, textureX, textureY); // Box 0
		baseModel[6] = new ModelRendererTurbo(this, 102, 147, textureX, textureY); // Box 0
		baseModel[7] = new ModelRendererTurbo(this, 20, 137, textureX, textureY); // Box 0
		baseModel[8] = new ModelRendererTurbo(this, 74, 35, textureX, textureY); // Box 0
		baseModel[9] = new ModelRendererTurbo(this, 40, 111, textureX, textureY); // Box 0
		baseModel[10] = new ModelRendererTurbo(this, 48, 110, textureX, textureY); // Box 0
		baseModel[11] = new ModelRendererTurbo(this, 48, 110, textureX, textureY); // Box 0
		baseModel[12] = new ModelRendererTurbo(this, 48, 110, textureX, textureY); // Box 0
		baseModel[13] = new ModelRendererTurbo(this, 48, 110, textureX, textureY); // Box 0
		baseModel[14] = new ModelRendererTurbo(this, 118, 111, textureX, textureY); // Box 0
		baseModel[15] = new ModelRendererTurbo(this, 52, 102, textureX, textureY); // Box 0
		baseModel[16] = new ModelRendererTurbo(this, 88, 66, textureX, textureY); // Box 0
		baseModel[17] = new ModelRendererTurbo(this, 122, 1, textureX, textureY); // Shape 19
		baseModel[18] = new ModelRendererTurbo(this, 122, 18, textureX, textureY); // Shape 19
		baseModel[19] = new ModelRendererTurbo(this, 92, 11, textureX, textureY); // Box 0
		baseModel[20] = new ModelRendererTurbo(this, 78, 22, textureX, textureY); // Box 0
		baseModel[21] = new ModelRendererTurbo(this, 78, 31, textureX, textureY); // Box 0
		baseModel[22] = new ModelRendererTurbo(this, 130, 84, textureX, textureY); // Box 0
		baseModel[23] = new ModelRendererTurbo(this, 90, 40, textureX, textureY); // Box 0
		baseModel[24] = new ModelRendererTurbo(this, 130, 103, textureX, textureY); // Box 0
		baseModel[25] = new ModelRendererTurbo(this, 130, 103, textureX, textureY); // Box 0
		baseModel[26] = new ModelRendererTurbo(this, 58, 91, textureX, textureY); // Box 0
		baseModel[27] = new ModelRendererTurbo(this, 32, 123, textureX, textureY); // Box 0
		baseModel[28] = new ModelRendererTurbo(this, 58, 99, textureX, textureY); // Box 0
		baseModel[29] = new ModelRendererTurbo(this, 104, 112, textureX, textureY); // Box 0
		baseModel[30] = new ModelRendererTurbo(this, 130, 103, textureX, textureY); // Box 0
		baseModel[31] = new ModelRendererTurbo(this, 130, 103, textureX, textureY); // Box 0
		baseModel[32] = new ModelRendererTurbo(this, 58, 91, textureX, textureY); // Box 0
		baseModel[33] = new ModelRendererTurbo(this, 104, 112, textureX, textureY); // Box 0
		baseModel[34] = new ModelRendererTurbo(this, 146, 103, textureX, textureY); // Box 0
		baseModel[35] = new ModelRendererTurbo(this, 146, 103, textureX, textureY); // Box 0
		baseModel[36] = new ModelRendererTurbo(this, 46, 116, textureX, textureY); // Box 0
		baseModel[37] = new ModelRendererTurbo(this, 104, 112, textureX, textureY); // Box 0
		baseModel[38] = new ModelRendererTurbo(this, 64, 68, textureX, textureY); // Box 0
		baseModel[39] = new ModelRendererTurbo(this, 64, 68, textureX, textureY); // Box 0
		baseModel[40] = new ModelRendererTurbo(this, 90, 54, textureX, textureY); // Box 0
		baseModel[41] = new ModelRendererTurbo(this, 150, 18, textureX, textureY); // Box 0
		baseModel[42] = new ModelRendererTurbo(this, 150, 18, textureX, textureY); // Box 0
		baseModel[43] = new ModelRendererTurbo(this, 72, 40, textureX, textureY); // Box 0
		baseModel[44] = new ModelRendererTurbo(this, 176, 84, textureX, textureY); // Box 0
		baseModel[45] = new ModelRendererTurbo(this, 104, 84, textureX, textureY); // Box 0
		baseModel[46] = new ModelRendererTurbo(this, 76, 35, textureX, textureY); // Box 0
		baseModel[47] = new ModelRendererTurbo(this, 76, 35, textureX, textureY); // Box 0
		baseModel[48] = new ModelRendererTurbo(this, 76, 35, textureX, textureY); // Box 0
		baseModel[49] = new ModelRendererTurbo(this, 76, 35, textureX, textureY); // Box 0
		baseModel[50] = new ModelRendererTurbo(this, 74, 54, textureX, textureY); // Box 0
		baseModel[51] = new ModelRendererTurbo(this, 72, 75, textureX, textureY); // Box 0
		baseModel[52] = new ModelRendererTurbo(this, 74, 26, textureX, textureY); // Box 0
		baseModel[53] = new ModelRendererTurbo(this, 117, 147, textureX, textureY); // Box 0
		baseModel[54] = new ModelRendererTurbo(this, 150, 26, textureX, textureY); // Box 0
		baseModel[55] = new ModelRendererTurbo(this, 150, 26, textureX, textureY); // Box 0
		baseModel[56] = new ModelRendererTurbo(this, 60, 107, textureX, textureY); // Box 0
		baseModel[57] = new ModelRendererTurbo(this, 52, 94, textureX, textureY); // Box 0
		baseModel[58] = new ModelRendererTurbo(this, 44, 120, textureX, textureY); // Box 0
		baseModel[59] = new ModelRendererTurbo(this, 68, 196, textureX, textureY); // Box 0

		baseModel[0].addBox(0F, 0F, 0F, 48, 8, 48, 0F); // Box 0
		baseModel[0].setRotationPoint(0F, -8F, 0F);

		baseModel[1].addBox(0F, 0F, 0F, 15, 7, 46, 0F); // Box 0
		baseModel[1].setRotationPoint(1F, -15F, 1F);

		baseModel[2].addBox(0F, 0F, 0F, 2, 7, 2, 0F); // Box 0
		baseModel[2].setRotationPoint(0F, -15F, 0F);

		baseModel[3].addBox(0F, 0F, 0F, 2, 7, 2, 0F); // Box 0
		baseModel[3].setRotationPoint(46F, -15F, 0F);

		baseModel[4].addBox(0F, 0F, 0F, 2, 7, 2, 0F); // Box 0
		baseModel[4].setRotationPoint(0F, -15F, 46F);

		baseModel[5].addBox(0F, 0F, 0F, 2, 7, 1, 0F); // Box 0
		baseModel[5].setRotationPoint(15F, -15F, 47F);

		baseModel[6].addBox(0F, 0F, 0F, 31, 7, 46, 0F); // Box 0
		baseModel[6].setRotationPoint(16F, -15F, 1F);

		baseModel[7].addBox(0F, 0F, 0F, 32, 24, 32, 0F); // Box 0
		baseModel[7].setRotationPoint(16F, -48F, 0F);

		baseModel[8].addBox(0F, 0F, 0F, 32, 1, 48, 0F); // Box 0
		baseModel[8].setRotationPoint(16F, -16F, 0F);

		baseModel[9].addBox(0F, 0F, 0F, 2, 7, 2, 0F); // Box 0
		baseModel[9].setRotationPoint(46F, -15F, 46F);

		baseModel[10].addBox(0F, 0F, 0F, 2, 8, 2, 0F); // Box 0
		baseModel[10].setRotationPoint(17F, -24F, 1F);

		baseModel[11].addBox(0F, 0F, 0F, 2, 8, 2, 0F); // Box 0
		baseModel[11].setRotationPoint(45F, -24F, 1F);

		baseModel[12].addBox(0F, 0F, 0F, 2, 8, 2, 0F); // Box 0
		baseModel[12].setRotationPoint(17F, -24F, 29F);

		baseModel[13].addBox(0F, 0F, 0F, 2, 8, 2, 0F); // Box 0
		baseModel[13].setRotationPoint(45F, -24F, 29F);

		baseModel[14].addShapeBox(0F, 0F, 0F, 32, 4, 32, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -4F, 0F, -4F, -4F, 0F, -4F, -4F, 0F, -4F, -4F, 0F, -4F); // Box 0
		baseModel[14].setRotationPoint(16F, -24F, 0F);

		baseModel[15].addBox(0F, 0F, 0F, 2, 7, 1, 0F); // Box 0
		baseModel[15].setRotationPoint(15F, -15F, 0F);

		baseModel[16].addBox(0F, 0F, 0F, 16, 16, 1, 0F); // Box 0
		baseModel[16].setRotationPoint(32F, -48F, 32F);

		baseModel[17].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[] { new Coord2D(0, 0, 0, 0), new Coord2D(8, 0, 8, 0), new Coord2D(14, 6, 14, 6), new Coord2D(14, 16, 14, 16), new Coord2D(0, 16, 0, 16) }), 1, 14, 16, 57, 1, ModelRendererTurbo.MR_FRONT, new float[] {16 ,14 ,10 ,9 ,8}); // Shape 19
		baseModel[17].setRotationPoint(33F, -32F, 33F);
		baseModel[17].rotateAngleY = -1.57079633F;

		baseModel[18].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[] { new Coord2D(0, 0, 0, 0), new Coord2D(8, 0, 8, 0), new Coord2D(14, 6, 14, 6), new Coord2D(14, 16, 14, 16), new Coord2D(0, 16, 0, 16) }), 1, 14, 16, 57, 1, ModelRendererTurbo.MR_FRONT, new float[] {16 ,14 ,10 ,9 ,8}); // Shape 19
		baseModel[18].setRotationPoint(48F, -32F, 33F);
		baseModel[18].rotateAngleY = -1.57079633F;

		baseModel[19].addBox(0F, 0F, 0F, 14, 10, 1, 0F); // Box 0
		baseModel[19].setRotationPoint(33F, -48F, 46F);

		baseModel[20].addBox(0F, 0F, 0F, 14, 1, 8, 0F); // Box 0
		baseModel[20].setRotationPoint(33F, -33F, 33F);

		baseModel[21].addShapeBox(0F, -0.25F, -0.5F, 14, 1, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0.25F, 0F, 0F, 0.25F, 0F, 0F, 0.25F, 0F, 0F, 0.25F); // Box 0
		baseModel[21].setRotationPoint(33F, -33F, 41F);
		baseModel[21].rotateAngleX = 0.78539816F;

		baseModel[22].addBox(0F, 0F, 0F, 12, 8, 11, 0F); // Box 0
		baseModel[22].setRotationPoint(18F, -42F, 32F);

		baseModel[23].addBox(0F, 0F, 0F, 8, 6, 8, 0F); // Box 0
		baseModel[23].setRotationPoint(20F, -48F, 36F);

		baseModel[24].addBox(0F, 0F, 0F, 4, 8, 4, 0F); // Box 0
		baseModel[24].setRotationPoint(19F, -28F, 32F);

		baseModel[25].addBox(0F, 0F, 0F, 4, 8, 4, 0F); // Box 0
		baseModel[25].setRotationPoint(25F, -28F, 32F);

		baseModel[26].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[26].setRotationPoint(19F, -32F, 32F);

		baseModel[27].addBox(0F, 0F, 0F, 4, 4, 3, 0F); // Box 0
		baseModel[27].setRotationPoint(19F, -32F, 36F);

		baseModel[28].addBox(0F, 0F, 0F, 4, 4, 4, 0F); // Box 0
		baseModel[28].setRotationPoint(25F, -32F, 36F);

		baseModel[29].addShapeBox(0F, 0F, 0F, 4, 4, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F); // Box 0
		baseModel[29].setRotationPoint(19F, -32F, 39F);

		baseModel[30].addBox(0F, 0F, 0F, 4, 1, 4, 0F); // Box 0
		baseModel[30].setRotationPoint(19F, -33F, 37F);

		baseModel[31].addBox(0F, 0F, 0F, 4, 1, 4, 0F); // Box 0
		baseModel[31].setRotationPoint(25F, -33F, 38F);

		baseModel[32].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[32].setRotationPoint(25F, -32F, 32F);

		baseModel[33].addShapeBox(0F, 0F, 0F, 4, 4, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F); // Box 0
		baseModel[33].setRotationPoint(25F, -32F, 40F);

		baseModel[34].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F); // Box 0
		baseModel[34].setRotationPoint(19F, -20F, 32F);

		baseModel[35].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F); // Box 0
		baseModel[35].setRotationPoint(25F, -20F, 32F);

		baseModel[36].addBox(0F, 0F, 0F, 4, 4, 10, 0F); // Box 0
		baseModel[36].setRotationPoint(19F, -20F, 22F);

		baseModel[37].addBox(0F, 0F, 0F, 4, 4, 13, 0F); // Box 0
		baseModel[37].setRotationPoint(25F, -20F, 19F);

		baseModel[38].addBox(0F, 0F, 0F, 6, 1, 6, 0F); // Box 0
		baseModel[38].setRotationPoint(18F, -34F, 36F);

		baseModel[39].addBox(0F, 0F, 0F, 6, 1, 6, 0F); // Box 0
		baseModel[39].setRotationPoint(24F, -34F, 37F);

		baseModel[40].addBox(0F, 0F, 0F, 8, 4, 8, 0F); // Box 0
		baseModel[40].setRotationPoint(28F, -20F, 12F);

		baseModel[41].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[41].setRotationPoint(25F, -20F, 15F);

		baseModel[42].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[42].setRotationPoint(19F, -20F, 18F);

		baseModel[43].addBox(0F, 0F, 0F, 3, 8, 6, 0F); // Box 0
		baseModel[43].setRotationPoint(16F, -24F, 17F);

		baseModel[44].addBox(0F, 0F, 0F, 8, 12, 8, 0F); // Box 0
		baseModel[44].setRotationPoint(4F, -47F, 20F);

		baseModel[45].addBox(0F, 0F, 0F, 1, 16, 12, 0F); // Box 0
		baseModel[45].setRotationPoint(15F, -48F, 17F);

		baseModel[46].addBox(0F, 0F, 0F, 3, 2, 2, 0F); // Box 0
		baseModel[46].setRotationPoint(12F, -46F, 21F);

		baseModel[47].addBox(0F, 0F, 0F, 3, 2, 2, 0F); // Box 0
		baseModel[47].setRotationPoint(12F, -46F, 25F);

		baseModel[48].addBox(0F, 0F, 0F, 3, 2, 2, 0F); // Box 0
		baseModel[48].setRotationPoint(12F, -38F, 21F);

		baseModel[49].addBox(0F, 0F, 0F, 3, 2, 2, 0F); // Box 0
		baseModel[49].setRotationPoint(12F, -38F, 25F);

		baseModel[50].addBox(0F, 0F, 0F, 6, 1, 6, 0F); // Box 0
		baseModel[50].setRotationPoint(5F, -35F, 21F);

		baseModel[51].addBox(0F, 0F, 0F, 4, 4, 4, 0F); // Box 0
		baseModel[51].setRotationPoint(6F, -33F, 22F);

		baseModel[52].addBox(0F, 0F, 0F, 3, 1, 3, 0F); // Box 0
		baseModel[52].setRotationPoint(6.5F, -34F, 22.5F);

		baseModel[53].addBox(0F, 0F, 0F, 1, 14, 6, 0F); // Box 0
		baseModel[53].setRotationPoint(15F, -32F, 17F);

		baseModel[54].addBox(0F, 0F, 0F, 12, 1, 2, 0F); // Box 0
		baseModel[54].setRotationPoint(18F, -42F, 43F);

		baseModel[55].addBox(0F, 0F, 0F, 12, 1, 2, 0F); // Box 0
		baseModel[55].setRotationPoint(18F, -35F, 43F);

		baseModel[56].addBox(0F, 0F, 0F, 5, 6, 2, 0F); // Box 0
		baseModel[56].setRotationPoint(18F, -41F, 43F);

		baseModel[57].addBox(0F, 0F, 0F, 1, 6, 2, 0F); // Box 0
		baseModel[57].setRotationPoint(29F, -41F, 43F);

		baseModel[58].addBox(0F, 0F, 0F, 6, 6, 0, 0F); // Box 0
		baseModel[58].setRotationPoint(23F, -41F, 44.5F);

		baseModel[59].addBox(0F, 0F, 0F, 16, 48, 1, 0F); // Box 0
		baseModel[59].setRotationPoint(0F, -15F, 0F);
		baseModel[59].rotateAngleX = 1.57079633F;


		fanModel = new ModelRendererTurbo[2];
		fanModel[0] = new ModelRendererTurbo(this, 88, 16, textureX, textureY); // Box 0
		fanModel[1] = new ModelRendererTurbo(this, 54, 88, textureX, textureY); // Box 0

		fanModel[0].addBox(-0.5F, -2.5F, 0F, 1, 5, 1, 0F); // Box 0

		fanModel[1].addBox(-0.5F, -2.5F, -0.01F, 1, 5, 1, 0F); // Box 0
		fanModel[1].rotateAngleZ = 1.57079633F;

		parts.put("base",baseModel);
		parts.put("fan",fanModel);

		flipAll();
	}

	public void getLegacyBlockRotation(EnumFacing facing, boolean mirrored)
	{
		super.getBlockRotation(facing,mirrored);
	}

	@Override
	public void getBlockRotation(EnumFacing facing, boolean mirrored)
	{
		switch(facing)
		{
			case NORTH:
			{
				GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.translate(-1, 0f, mirrored?2f:0f);

			}
			break;
			case SOUTH:
			{
				GlStateManager.translate(-2f, 0f, 1f);
				//GlStateManager.rotate(180F, 0F, 1F, 0F);

			}
			break;
			case EAST:
			{
				GlStateManager.rotate(mirrored?270f:-270f, 0F, 1F, 0F);
				//GlStateManager.rotate(270F, 0F, 1F, 0F);
				GlStateManager.translate(-2f, 0f, mirrored?2f:0f);

			}
			break;
			case WEST:
			{
				GlStateManager.rotate(mirrored?90F:-90f, 0F, 1F, 0F);
				GlStateManager.translate(-1f, 0f, 1f);

			}
			break;
		}
	}
}
