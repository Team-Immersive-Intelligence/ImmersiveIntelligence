package pl.pabilo8.immersiveintelligence.client.model.multiblock.metal;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;

/**
 * @author Pabilo8
 * @since 30.09.2020
 */
public class ModelAmmunitionWorkshop extends ModelIIBase
{
	int textureX = 128;
	int textureY = 128;

	public ModelAmmunitionWorkshop() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[99];
		baseModel[0] = new ModelRendererTurbo(this, 18, 65, textureX, textureY); // MainPART01
		baseModel[1] = new ModelRendererTurbo(this, 94, 48, textureX, textureY); // MainPART03
		baseModel[2] = new ModelRendererTurbo(this, 10, 84, textureX, textureY); // MainPART04
		baseModel[3] = new ModelRendererTurbo(this, 10, 84, textureX, textureY); // MainPART05
		baseModel[4] = new ModelRendererTurbo(this, 54, 48, textureX, textureY); // MainPART06
		baseModel[5] = new ModelRendererTurbo(this, 94, 48, textureX, textureY); // MainPART11
		baseModel[6] = new ModelRendererTurbo(this, 10, 84, textureX, textureY); // MainPART13
		baseModel[7] = new ModelRendererTurbo(this, 10, 84, textureX, textureY); // MainPART14
		baseModel[8] = new ModelRendererTurbo(this, 48, 3, textureX, textureY); // INSERETERERETRER01
		baseModel[9] = new ModelRendererTurbo(this, 48, 3, textureX, textureY); // INSERETERERETRER02
		baseModel[10] = new ModelRendererTurbo(this, 106, 3, textureX, textureY); // INSERETERERETRER03
		baseModel[11] = new ModelRendererTurbo(this, 106, 3, textureX, textureY); // INSERETERERETRER04
		baseModel[12] = new ModelRendererTurbo(this, 120, 96, textureX, textureY); // INSERETERERETRER07
		baseModel[13] = new ModelRendererTurbo(this, 18, 61, textureX, textureY); // INSERETERERETRER08
		baseModel[14] = new ModelRendererTurbo(this, 120, 96, textureX, textureY); // INSERETERERETRER09
		baseModel[15] = new ModelRendererTurbo(this, 18, 61, textureX, textureY); // INSERETERERETRER10
		baseModel[16] = new ModelRendererTurbo(this, 89, 0, textureX, textureY); // INSERETERERETRER11
		baseModel[17] = new ModelRendererTurbo(this, 89, 0, textureX, textureY); // INSERETERERETRER12
		baseModel[18] = new ModelRendererTurbo(this, 0, 2, textureX, textureY); // MainPART23
		baseModel[19] = new ModelRendererTurbo(this, 106, 3, textureX, textureY); // MainPART24
		baseModel[20] = new ModelRendererTurbo(this, 58, 43, textureX, textureY); // AmmoBoxes03
		baseModel[21] = new ModelRendererTurbo(this, 58, 43, textureX, textureY); // AmmoBoxes06
		baseModel[22] = new ModelRendererTurbo(this, 98, 1, textureX, textureY); // PAPERSTANDO01
		baseModel[23] = new ModelRendererTurbo(this, 8, 48, textureX, textureY); // PAPERSTANDO02
		baseModel[24] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // PAPERSTANDO03
		baseModel[25] = new ModelRendererTurbo(this, 90, 36, textureX, textureY); // UVIOLATEDTHELAW01
		baseModel[26] = new ModelRendererTurbo(this, 0, 82, textureX, textureY); // UVIOLATEDTHELAW02
		baseModel[27] = new ModelRendererTurbo(this, 0, 82, textureX, textureY); // UVIOLATEDTHELAW03
		baseModel[28] = new ModelRendererTurbo(this, 6, 78, textureX, textureY); // CupIDFK01
		baseModel[29] = new ModelRendererTurbo(this, 6, 78, textureX, textureY); // CupIDFK02
		baseModel[30] = new ModelRendererTurbo(this, 0, 51, textureX, textureY); // CupIDFK03
		baseModel[31] = new ModelRendererTurbo(this, 0, 51, textureX, textureY); // CupIDFK04
		baseModel[32] = new ModelRendererTurbo(this, 9, 94, textureX, textureY); // CupIDFK05
		baseModel[33] = new ModelRendererTurbo(this, 78, 11, textureX, textureY); // CupIDFK06
		baseModel[34] = new ModelRendererTurbo(this, 86, 22, textureX, textureY); // CupIDFK07
		baseModel[35] = new ModelRendererTurbo(this, 64, 30, textureX, textureY); // eLECTRICdOOR01
		baseModel[36] = new ModelRendererTurbo(this, 0, 16, textureX, textureY); // eLECTRICdOOR02
		baseModel[37] = new ModelRendererTurbo(this, 0, 14, textureX, textureY); // eLECTRICdOOR03
		baseModel[38] = new ModelRendererTurbo(this, 16, 96, textureX, textureY); // MainPART01
		baseModel[39] = new ModelRendererTurbo(this, 92, 14, textureX, textureY); // AmmoBoxes06
		baseModel[40] = new ModelRendererTurbo(this, 58, 43, textureX, textureY); // AmmoBoxes06
		baseModel[41] = new ModelRendererTurbo(this, 106, 3, textureX, textureY); // MainPART24
		baseModel[42] = new ModelRendererTurbo(this, 82, 4, textureX, textureY); // MainPART23
		baseModel[43] = new ModelRendererTurbo(this, 82, 4, textureX, textureY); // MainPART23
		baseModel[44] = new ModelRendererTurbo(this, 54, 48, textureX, textureY); // MainPART06
		baseModel[45] = new ModelRendererTurbo(this, 54, 48, textureX, textureY); // MainPART06
		baseModel[46] = new ModelRendererTurbo(this, 54, 48, textureX, textureY); // MainPART06
		baseModel[47] = new ModelRendererTurbo(this, 54, 48, textureX, textureY); // MainPART06
		baseModel[48] = new ModelRendererTurbo(this, 54, 48, textureX, textureY); // MainPART06
		baseModel[49] = new ModelRendererTurbo(this, 54, 48, textureX, textureY); // MainPART06
		baseModel[50] = new ModelRendererTurbo(this, 54, 48, textureX, textureY); // MainPART06
		baseModel[51] = new ModelRendererTurbo(this, 54, 48, textureX, textureY); // MainPART06
		baseModel[52] = new ModelRendererTurbo(this, 54, 48, textureX, textureY); // MainPART06
		baseModel[53] = new ModelRendererTurbo(this, 54, 48, textureX, textureY); // MainPART06
		baseModel[54] = new ModelRendererTurbo(this, 54, 48, textureX, textureY); // MainPART06
		baseModel[55] = new ModelRendererTurbo(this, 54, 48, textureX, textureY); // MainPART06
		baseModel[56] = new ModelRendererTurbo(this, 0, 61, textureX, textureY); // MainPART23
		baseModel[57] = new ModelRendererTurbo(this, 42, 48, textureX, textureY); // MainPART23
		baseModel[58] = new ModelRendererTurbo(this, 4, 107, textureX, textureY); // MainPART23
		baseModel[59] = new ModelRendererTurbo(this, 42, 49, textureX, textureY); // MainPART23
		baseModel[60] = new ModelRendererTurbo(this, 4, 11, textureX, textureY); // MainPART23
		baseModel[61] = new ModelRendererTurbo(this, 16, 65, textureX, textureY); // CupIDFK01
		baseModel[62] = new ModelRendererTurbo(this, 120, 65, textureX, textureY); // CupIDFK01
		baseModel[63] = new ModelRendererTurbo(this, 81, 0, textureX, textureY); // CupIDFK01
		baseModel[64] = new ModelRendererTurbo(this, 1, 11, textureX, textureY); // CupIDFK01
		baseModel[65] = new ModelRendererTurbo(this, 2, 2, textureX, textureY); // DiePapiereBitte
		baseModel[66] = new ModelRendererTurbo(this, 2, 2, textureX, textureY); // DiePapiereBitte
		baseModel[67] = new ModelRendererTurbo(this, 2, 2, textureX, textureY); // DiePapiereBitte
		baseModel[68] = new ModelRendererTurbo(this, 2, 2, textureX, textureY); // DiePapiereBitte
		baseModel[69] = new ModelRendererTurbo(this, 4, 113, textureX, textureY); // PAPERSTANDO01
		baseModel[70] = new ModelRendererTurbo(this, 18, 65, textureX, textureY); // MainPART01
		baseModel[71] = new ModelRendererTurbo(this, 16, 96, textureX, textureY); // MainPART01
		baseModel[72] = new ModelRendererTurbo(this, 16, 96, textureX, textureY); // MainPART01
		baseModel[73] = new ModelRendererTurbo(this, 16, 96, textureX, textureY); // MainPART01
		baseModel[74] = new ModelRendererTurbo(this, 18, 65, textureX, textureY); // MainPART01
		baseModel[75] = new ModelRendererTurbo(this, 18, 65, textureX, textureY); // MainPART01
		baseModel[76] = new ModelRendererTurbo(this, 54, 48, textureX, textureY); // MainPART06
		baseModel[77] = new ModelRendererTurbo(this, 0, 98, textureX, textureY); // MainFence00
		baseModel[78] = new ModelRendererTurbo(this, 0, 98, textureX, textureY); // MainFence00
		baseModel[79] = new ModelRendererTurbo(this, 0, 82, textureX, textureY); // MainFence00
		baseModel[80] = new ModelRendererTurbo(this, 0, 82, textureX, textureY); // MainFence00
		baseModel[81] = new ModelRendererTurbo(this, 4, 113, textureX, textureY); // PAPERSTANDO01
		baseModel[82] = new ModelRendererTurbo(this, 4, 113, textureX, textureY); // PAPERSTANDO01
		baseModel[83] = new ModelRendererTurbo(this, 80, 106, textureX, textureY); // PAPERSTANDO02
		baseModel[84] = new ModelRendererTurbo(this, 80, 106, textureX, textureY); // PAPERSTANDO02
		baseModel[85] = new ModelRendererTurbo(this, 80, 106, textureX, textureY); // PAPERSTANDO02
		baseModel[86] = new ModelRendererTurbo(this, 80, 106, textureX, textureY); // PAPERSTANDO02
		baseModel[87] = new ModelRendererTurbo(this, 10, 84, textureX, textureY); // MainPART13
		baseModel[88] = new ModelRendererTurbo(this, 10, 84, textureX, textureY); // MainPART13
		baseModel[89] = new ModelRendererTurbo(this, 0, 98, textureX, textureY); // MainFence00
		baseModel[90] = new ModelRendererTurbo(this, 0, 98, textureX, textureY); // MainFence00
		baseModel[91] = new ModelRendererTurbo(this, 0, 82, textureX, textureY); // MainFence00
		baseModel[92] = new ModelRendererTurbo(this, 0, 82, textureX, textureY); // MainFence00
		baseModel[93] = new ModelRendererTurbo(this, 0, 98, textureX, textureY); // MainFence00
		baseModel[94] = new ModelRendererTurbo(this, 0, 98, textureX, textureY); // MainFence00
		baseModel[95] = new ModelRendererTurbo(this, 0, 98, textureX, textureY); // MainFence00
		baseModel[96] = new ModelRendererTurbo(this, 0, 98, textureX, textureY); // MainFence00
		baseModel[97] = new ModelRendererTurbo(this, 0, 48, textureX, textureY); // PAPERSTANDO02
		baseModel[98] = new ModelRendererTurbo(this, 0, 48, textureX, textureY); // PAPERSTANDO02

		baseModel[0].addShapeBox(0F, 0F, 0F, 47, 23, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPART01
		baseModel[0].setRotationPoint(1F, 8F, 1F);
		baseModel[0].rotateAngleX = 1.57079633F;

		baseModel[1].setFlipped(true);
		baseModel[1].addShapeBox(0F, 0F, 0F, 16, 16, 1, 0F, 0.05F, 0.05F, -0.95F, 0.05F, 0.05F, -0.95F, 0.05F, 0.05F, -0.95F, 0.05F, 0.05F, -0.95F, 0.05F, 0.05F, -0.95F, 0.05F, 0.05F, -0.95F, 0.05F, 0.05F, -0.95F, 0.05F, 0.05F, -0.95F); // MainPART03
		baseModel[1].setRotationPoint(32F, 0F, 47.1F);

		baseModel[2].addShapeBox(0F, 0F, 0F, 2, 8, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPART04
		baseModel[2].setRotationPoint(0F, 0F, 46F);

		baseModel[3].addShapeBox(0F, 0F, 0F, 2, 8, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPART05
		baseModel[3].setRotationPoint(0F, 0F, 0F);

		baseModel[4].addShapeBox(0F, 0F, 0F, 1, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPART06
		baseModel[4].setRotationPoint(2F, 1F, 0F);
		baseModel[4].rotateAngleZ = 1.57079633F;

		baseModel[5].addShapeBox(0F, 0F, 0F, 16, 16, 1, 0F, 0.05F, 0.05F, 0.05F, 0.05F, 0.05F, 0.05F, 0.05F, 0.05F, 0.05F, 0.05F, 0.05F, 0.05F, 0.05F, 0.05F, 0.05F, 0.05F, 0.05F, 0.05F, 0.05F, 0.05F, 0.05F, 0.05F, 0.05F, 0.05F); // MainPART11
		baseModel[5].setRotationPoint(64F, 0F, 47F);

		baseModel[6].addShapeBox(0F, 0F, 0F, 2, 8, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPART13
		baseModel[6].setRotationPoint(94F, 0F, 0F);

		baseModel[7].addShapeBox(0F, 0F, 0F, 2, 8, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPART14
		baseModel[7].setRotationPoint(94F, 0F, 46F);

		baseModel[8].addShapeBox(0F, 0F, 0F, 10, 10, 5, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // INSERETERERETRER01
		baseModel[8].setRotationPoint(35F, 0F, 35F);
		baseModel[8].rotateAngleX = 1.57079633F;

		baseModel[9].addShapeBox(0F, 0F, 0F, 10, 10, 5, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // INSERETERERETRER02
		baseModel[9].setRotationPoint(51F, 0F, 35F);
		baseModel[9].rotateAngleX = 1.57079633F;

		baseModel[10].addShapeBox(0F, 0F, 0F, 10, 10, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, -1F, 0F, -1F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, -1F, 0F, -1F, -1F, 0F); // INSERETERERETRER03
		baseModel[10].setRotationPoint(35F, -5F, 35F);
		baseModel[10].rotateAngleX = 1.57079633F;

		baseModel[11].addShapeBox(0F, 0F, 0F, 10, 10, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, -1F, 0F, -1F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, -1F, 0F, -1F, -1F, 0F); // INSERETERERETRER04
		baseModel[11].setRotationPoint(51F, -5F, 35F);
		baseModel[11].rotateAngleX = 1.57079633F;

		baseModel[12].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // INSERETERERETRER07
		baseModel[12].setRotationPoint(36F, -4F, 45F);

		baseModel[13].addShapeBox(0F, 0F, 0F, 10, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // INSERETERERETRER08
		baseModel[13].setRotationPoint(36F, -2F, 45F);

		baseModel[14].setFlipped(true);
		baseModel[14].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, -1F, 0F, -2F, -1F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F); // INSERETERERETRER09
		baseModel[14].setRotationPoint(58F, -4F, 45F);

		baseModel[15].setFlipped(true);
		baseModel[15].addShapeBox(0F, 0F, 0F, 10, 2, 2, 0F, -10F, 0F, 0F, -10F, 0F, 0F, -10F, 0F, 0F, -10F, 0F, 0F, -10F, 0F, 0F, -10F, 0F, 0F, -10F, 0F, 0F, -10F, 0F, 0F); // INSERETERERETRER10
		baseModel[15].setRotationPoint(50F, -2F, 45F);

		baseModel[16].setFlipped(true);
		baseModel[16].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, -2F, 0F, 0F, -2F, -1.5F, 0F, -2F, -1.5F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F); // INSERETERERETRER11
		baseModel[16].setRotationPoint(48F, -2F, 45F);

		baseModel[17].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, -1.5F, 0F, 0F, -1.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // INSERETERERETRER12
		baseModel[17].setRotationPoint(46F, -2F, 45F);

		baseModel[18].addShapeBox(0F, 0F, 0F, 16, 30, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPART23
		baseModel[18].setRotationPoint(64F, -30F, 32F);

		baseModel[19].addShapeBox(0F, 0F, 0F, 10, 10, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, -1F, 0F, -1F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, -1F, 0F, -1F, -1F, 0F); // MainPART24
		baseModel[19].setRotationPoint(67F, -31F, 35F);
		baseModel[19].rotateAngleX = 1.57079633F;

		baseModel[20].addShapeBox(0F, 0F, 0F, 12, 16, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // AmmoBoxes03
		baseModel[20].setRotationPoint(82F, 0F, 45F);
		baseModel[20].rotateAngleX = 1.57079633F;
		baseModel[20].rotateAngleY = -1.25663706F;

		baseModel[21].addShapeBox(0F, 0F, 0F, 12, 16, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // AmmoBoxes06
		baseModel[21].setRotationPoint(86F, 0F, -2F);
		baseModel[21].rotateAngleX = 1.57079633F;
		baseModel[21].rotateAngleY = 0.29670597F;

		baseModel[22].addBox(0F, 0F, 0F, 3, 12, 1, 0F); // PAPERSTANDO01
		baseModel[22].setRotationPoint(68F, -1F, 3.5F);
		baseModel[22].rotateAngleX = 1.57079633F;
		baseModel[22].rotateAngleY = -1.57079633F;

		baseModel[23].addShapeBox(0F, -12F, 0F, 16, 12, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // PAPERSTANDO02
		baseModel[23].setRotationPoint(66F, 0F, -2F);
		baseModel[23].rotateAngleX = -0.78539816F;
		baseModel[23].rotateAngleZ = -0.01745329F;

		baseModel[24].addShapeBox(0F, -1F, 0F, 16, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // PAPERSTANDO03
		baseModel[24].setRotationPoint(66F, 0F, -2F);
		baseModel[24].rotateAngleX = 0.78539816F;
		baseModel[24].rotateAngleZ = -0.01745329F;

		baseModel[25].addShapeBox(0F, 0F, 0F, 16, 10, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // UVIOLATEDTHELAW01
		baseModel[25].setRotationPoint(48F, -10F, 16F);
		baseModel[25].rotateAngleY = -1.57079633F;

		baseModel[26].addShapeBox(0F, 0F, 0F, 2, 8, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -4F, 0F, 0F, 4F, 0F, 0F, 4F, 0F, 0F, -4F, 0F, 0F); // UVIOLATEDTHELAW02
		baseModel[26].setRotationPoint(49F, -8F, 2F);

		baseModel[27].addShapeBox(0F, 0F, 0F, 2, 8, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -4F, 0F, 0F, 4F, 0F, 0F, 4F, 0F, 0F, -4F, 0F, 0F); // UVIOLATEDTHELAW03
		baseModel[27].setRotationPoint(49F, -8F, 12F);

		baseModel[28].addShapeBox(0F, 0F, -1F, 5, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // CupIDFK01
		baseModel[28].setRotationPoint(59F, -6F, 7F);
		baseModel[28].rotateAngleY = 1.1693706F;

		baseModel[29].addShapeBox(0F, 0F, -5F, 5, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // CupIDFK02
		baseModel[29].setRotationPoint(59F, -6F, 7F);
		baseModel[29].rotateAngleY = 1.1693706F;

		baseModel[30].addShapeBox(1F, 0F, 0F, 3, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // CupIDFK03
		baseModel[30].setRotationPoint(59F, -6F, 7F);
		baseModel[30].rotateAngleY = -0.40142573F;

		baseModel[31].addShapeBox(1F, 0F, 4F, 3, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // CupIDFK04
		baseModel[31].setRotationPoint(59F, -6F, 7F);
		baseModel[31].rotateAngleY = -0.40142573F;

		baseModel[32].addShapeBox(1F, 0F, 1F, 3, 1, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // CupIDFK05
		baseModel[32].setRotationPoint(59F, -2F, 7F);
		baseModel[32].rotateAngleY = -0.40142573F;

		baseModel[33].addShapeBox(0F, 0F, 0F, 1, 7, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // CupIDFK06
		baseModel[33].setRotationPoint(60F, -8F, 8F);
		baseModel[33].rotateAngleY = -0.59341195F;
		baseModel[33].rotateAngleZ = 0.19198622F;

		baseModel[34].addShapeBox(0F, 0F, 0F, 1, 7, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // CupIDFK07
		baseModel[34].setRotationPoint(63F, -8F, 8F);
		baseModel[34].rotateAngleZ = -0.13962634F;

		baseModel[35].addShapeBox(0F, 0F, 0F, 12, 12, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // eLECTRICdOOR01
		baseModel[35].setRotationPoint(66F, -19F, 31F);

		baseModel[36].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // eLECTRICdOOR02
		baseModel[36].setRotationPoint(65F, -17F, 31F);

		baseModel[37].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // eLECTRICdOOR03
		baseModel[37].setRotationPoint(65F, -10F, 31F);

		baseModel[38].addShapeBox(0F, 0F, 0F, 48, 24, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPART01
		baseModel[38].setRotationPoint(0F, 16F, 0F);
		baseModel[38].rotateAngleX = 1.57079633F;

		baseModel[39].addShapeBox(0F, 0F, 0F, 12, 16, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // AmmoBoxes06
		baseModel[39].setRotationPoint(98F, -6F, 2F);
		baseModel[39].rotateAngleX = 1.57079633F;
		baseModel[39].rotateAngleY = 1.48352986F;

		baseModel[40].addShapeBox(0F, 0F, 0F, 12, 16, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // AmmoBoxes06
		baseModel[40].setRotationPoint(81F, -12F, 4F);
		baseModel[40].rotateAngleX = 1.57079633F;
		baseModel[40].rotateAngleY = -0.57595865F;

		baseModel[41].addBox(0F, 0F, 0F, 10, 10, 1, 0F); // MainPART24
		baseModel[41].setRotationPoint(67F, -30F, 45F);
		baseModel[41].rotateAngleX = 1.57079633F;
		baseModel[41].rotateAngleY = -1.57079633F;

		baseModel[42].addShapeBox(0F, 0F, 0F, 4, 4, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPART23
		baseModel[42].setRotationPoint(80F, -29F, 37F);
		baseModel[42].rotateAngleY = -1.57079633F;

		baseModel[43].addShapeBox(0F, 0F, 0F, 4, 4, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPART23
		baseModel[43].setRotationPoint(80F, -12F, 47F);
		baseModel[43].rotateAngleY = -1.57079633F;

		baseModel[44].addShapeBox(0F, 0F, 0F, 1, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPART06
		baseModel[44].setRotationPoint(18F, 1F, 0F);
		baseModel[44].rotateAngleZ = 1.57079633F;

		baseModel[45].addShapeBox(0F, 0F, 0F, 1, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPART06
		baseModel[45].setRotationPoint(34F, 1F, 0F);
		baseModel[45].rotateAngleZ = 1.57079633F;

		baseModel[46].addShapeBox(0F, 0F, 0F, 1, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPART06
		baseModel[46].setRotationPoint(2F, 1F, 47F);
		baseModel[46].rotateAngleZ = 1.57079633F;

		baseModel[47].addShapeBox(0F, 0F, 0F, 1, 14, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPART06
		baseModel[47].setRotationPoint(18F, 1F, 47F);
		baseModel[47].rotateAngleZ = 1.57079633F;

		baseModel[48].addShapeBox(0F, 0F, 0F, 1, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPART06
		baseModel[48].setRotationPoint(48F, 1F, 47F);
		baseModel[48].rotateAngleZ = 1.57079633F;

		baseModel[49].addShapeBox(0F, 0F, 0F, 1, 14, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPART06
		baseModel[49].setRotationPoint(80F, 1F, 47F);
		baseModel[49].rotateAngleZ = 1.57079633F;

		baseModel[50].addShapeBox(0F, 0F, 0F, 1, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPART06
		baseModel[50].setRotationPoint(0F, 1F, 46F);
		baseModel[50].rotateAngleY = -1.57079633F;
		baseModel[50].rotateAngleZ = 1.57079633F;

		baseModel[51].addShapeBox(0F, 0F, 0F, 1, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPART06
		baseModel[51].setRotationPoint(0F, 1F, 30F);
		baseModel[51].rotateAngleY = -1.57079633F;
		baseModel[51].rotateAngleZ = 1.57079633F;

		baseModel[52].addShapeBox(0F, 0F, 0F, 1, 12, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPART06
		baseModel[52].setRotationPoint(0F, 1F, 14F);
		baseModel[52].rotateAngleY = -1.57079633F;
		baseModel[52].rotateAngleZ = 1.57079633F;

		baseModel[53].addShapeBox(0F, 0F, 0F, 1, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPART06
		baseModel[53].setRotationPoint(95F, 1F, 46F);
		baseModel[53].rotateAngleY = -1.57079633F;
		baseModel[53].rotateAngleZ = 1.57079633F;

		baseModel[54].addShapeBox(0F, 0F, 0F, 1, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPART06
		baseModel[54].setRotationPoint(95F, 1F, 30F);
		baseModel[54].rotateAngleY = -1.57079633F;
		baseModel[54].rotateAngleZ = 1.57079633F;

		baseModel[55].addShapeBox(0F, 0F, 0F, 1, 12, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPART06
		baseModel[55].setRotationPoint(95F, 1F, 14F);
		baseModel[55].rotateAngleY = -1.57079633F;
		baseModel[55].rotateAngleZ = 1.57079633F;

		baseModel[56].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPART23
		baseModel[56].setRotationPoint(81F, -28.5F, 33.5F);

		baseModel[57].addShapeBox(0F, 0F, 0F, 3, 14, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPART23
		baseModel[57].setRotationPoint(81F, -25.5F, 33.5F);

		baseModel[58].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPART23
		baseModel[58].setRotationPoint(81F, -11.5F, 33.5F);

		baseModel[59].addShapeBox(0F, 0F, 0F, 3, 7, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPART23
		baseModel[59].setRotationPoint(81F, -8.5F, 36.5F);
		baseModel[59].rotateAngleX = 1.57079633F;

		baseModel[60].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F); // MainPART23
		baseModel[60].setRotationPoint(81F, -11.5F, 43.5F);

		baseModel[61].addShapeBox(0F, 0F, 0F, 4, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // CupIDFK01
		baseModel[61].setRotationPoint(55F, -1F, 5F);
		baseModel[61].rotateAngleX = 1.57079633F;
		baseModel[61].rotateAngleY = -1.46607657F;

		baseModel[62].addShapeBox(0F, 0F, 0F, 3, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // CupIDFK01
		baseModel[62].setRotationPoint(55.5F, -2F, 4.5F);
		baseModel[62].rotateAngleX = 1.57079633F;
		baseModel[62].rotateAngleY = -1.46607657F;

		baseModel[63].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // CupIDFK01
		baseModel[63].setRotationPoint(57F, -4F, 4.5F);
		baseModel[63].rotateAngleX = 1.57079633F;
		baseModel[63].rotateAngleY = -1.46607657F;

		baseModel[64].addShapeBox(0F, 0F, 0F, 2, 2, 1, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F); // CupIDFK01
		baseModel[64].setRotationPoint(57F, -3F, 4.5F);
		baseModel[64].rotateAngleX = 1.57079633F;
		baseModel[64].rotateAngleY = -1.46607657F;

		baseModel[65].addShapeBox(0F, 0F, 0F, 6, 8, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DiePapiereBitte
		baseModel[65].setRotationPoint(52F, -1F, 9F);
		baseModel[65].rotateAngleX = 1.57079633F;
		baseModel[65].rotateAngleY = -0.73303829F;

		baseModel[66].addShapeBox(0F, 0F, 0F, 6, 8, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DiePapiereBitte
		baseModel[66].setRotationPoint(52.5F, -2F, 7.5F);
		baseModel[66].rotateAngleX = 1.57079633F;
		baseModel[66].rotateAngleY = -0.38397244F;

		baseModel[67].addShapeBox(0F, 0F, 0F, 6, 8, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DiePapiereBitte
		baseModel[67].setRotationPoint(52F, -3F, 9F);
		baseModel[67].rotateAngleX = 1.57079633F;
		baseModel[67].rotateAngleY = -0.73303829F;

		baseModel[68].addShapeBox(0F, 0F, 0F, 6, 8, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DiePapiereBitte
		baseModel[68].setRotationPoint(52.5F, -4F, 7.5F);
		baseModel[68].rotateAngleX = 1.57079633F;
		baseModel[68].rotateAngleY = -0.38397244F;

		baseModel[69].addBox(0F, 0F, 0F, 3, 12, 3, 0F); // PAPERSTANDO01
		baseModel[69].setRotationPoint(68F, -1F, 9.5F);
		baseModel[69].rotateAngleY = -0.03490659F;
		baseModel[69].rotateAngleZ = 1.57079633F;

		baseModel[70].setFlipped(true);
		baseModel[70].addShapeBox(0F, 0F, 0F, 47, 23, 8, 0F, -47F, 0F, 0F, -47F, 0F, 0F, -47F, 0F, 0F, -47F, 0F, 0F, -47F, 0F, 0F, -47F, 0F, 0F, -47F, 0F, 0F, -47F, 0F, 0F); // MainPART01
		baseModel[70].setRotationPoint(48F, 8F, 1F);
		baseModel[70].rotateAngleX = 1.57079633F;

		baseModel[71].setFlipped(true);
		baseModel[71].addShapeBox(0F, 0F, 0F, 48, 24, 8, 0F, -48F, 0F, 0F, -48F, 0F, 0F, -48F, 0F, 0F, -48F, 0F, 0F, -48F, 0F, 0F, -48F, 0F, 0F, -48F, 0F, 0F, -48F, 0F, 0F); // MainPART01
		baseModel[71].setRotationPoint(48F, 16F, 0F);
		baseModel[71].rotateAngleX = 1.57079633F;

		baseModel[72].setFlipped(true);
		baseModel[72].addShapeBox(0F, 0F, 0F, 48, 24, 8, 0F, 0F, -24F, 0F, 0F, -24F, 0F, 0F, -24F, 0F, 0F, -24F, 0F, 0F, -24F, 0F, 0F, -24F, 0F, 0F, -24F, 0F, 0F, -24F, 0F); // MainPART01
		baseModel[72].setRotationPoint(0F, 16F, 24F);
		baseModel[72].rotateAngleX = 1.57079633F;

		baseModel[73].addShapeBox(0F, 0F, 0F, 48, 24, 8, 0F, -48F, -24F, 0F, -48F, -24F, 0F, -48F, -24F, 0F, -48F, -24F, 0F, -48F, -24F, 0F, -48F, -24F, 0F, -48F, -24F, 0F, -48F, -24F, 0F); // MainPART01
		baseModel[73].setRotationPoint(48F, 16F, 24F);
		baseModel[73].rotateAngleX = 1.57079633F;

		baseModel[74].setFlipped(true);
		baseModel[74].addShapeBox(0F, 0F, 0F, 47, 23, 8, 0F, 0F, -23F, 0F, 0F, -23F, 0F, 0F, -23F, 0F, 0F, -23F, 0F, 0F, -23F, 0F, 0F, -23F, 0F, 0F, -23F, 0F, 0F, -23F, 0F); // MainPART01
		baseModel[74].setRotationPoint(1F, 8F, 24F);
		baseModel[74].rotateAngleX = 1.57079633F;

		baseModel[75].addShapeBox(0F, 0F, 0F, 47, 23, 8, 0F, -47F, -23F, 0F, -47F, -23F, 0F, -47F, -23F, 0F, -47F, -23F, 0F, -47F, -23F, 0F, -47F, -23F, 0F, -47F, -23F, 0F, -47F, -23F, 0F); // MainPART01
		baseModel[75].setRotationPoint(48F, 8F, 24F);
		baseModel[75].rotateAngleX = 1.57079633F;

		baseModel[76].addShapeBox(0F, 0F, 0F, 1, 12, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPART06
		baseModel[76].setRotationPoint(82F, 1F, 0F);
		baseModel[76].rotateAngleZ = 1.57079633F;

		baseModel[77].addShapeBox(0F, 0F, 0F, 12, 6, 0, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainFence00
		baseModel[77].setRotationPoint(1F, 0F, 18F);
		baseModel[77].rotateAngleX = 1.57079633F;
		baseModel[77].rotateAngleZ = 1.57079633F;

		baseModel[78].addShapeBox(0F, 0F, 0F, 12, 6, 0, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainFence00
		baseModel[78].setRotationPoint(1F, 0F, 24F);
		baseModel[78].rotateAngleX = 1.57079633F;
		baseModel[78].rotateAngleZ = 1.57079633F;

		baseModel[79].addShapeBox(0F, 0F, 0F, 2, 14, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainFence00
		baseModel[79].setRotationPoint(0F, -14F, 16F);

		baseModel[80].addShapeBox(0F, 0F, 0F, 2, 14, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainFence00
		baseModel[80].setRotationPoint(0F, -14F, 30F);

		baseModel[81].addBox(0F, 0F, 0F, 3, 12, 3, 0F); // PAPERSTANDO01
		baseModel[81].setRotationPoint(69F, -1F, 5.5F);
		baseModel[81].rotateAngleY = -0.03490659F;
		baseModel[81].rotateAngleZ = 1.57079633F;

		baseModel[82].addBox(0F, 0F, 0F, 3, 12, 3, 0F); // PAPERSTANDO01
		baseModel[82].setRotationPoint(67F, -4F, 10.5F);
		baseModel[82].rotateAngleY = -0.38397244F;
		baseModel[82].rotateAngleZ = 1.57079633F;

		baseModel[83].setFlipped(true);
		baseModel[83].addShapeBox(0F, 0F, 0F, 16, 16, 1, 0F, -16F, 0F, 0F, -16F, 0F, 0F, -16F, 0F, 0F, -16F, 0F, 0F, -16F, 0F, 0F, -16F, 0F, 0F, -16F, 0F, 0F, -16F, 0F, 0F); // PAPERSTANDO02
		baseModel[83].setRotationPoint(66F, 0F, 0F);
		baseModel[83].rotateAngleX = 1.57079633F;

		baseModel[84].addShapeBox(0F, 0F, 0F, 16, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // PAPERSTANDO02
		baseModel[84].setRotationPoint(50F, 0F, 0F);
		baseModel[84].rotateAngleX = 1.57079633F;

		baseModel[85].addShapeBox(0F, 0F, 0F, 16, 8, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // PAPERSTANDO02
		baseModel[85].setRotationPoint(66F, 0F, 0.95F);

		baseModel[86].addShapeBox(0F, 0F, 0F, 16, 8, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // PAPERSTANDO02
		baseModel[86].setRotationPoint(50F, 0F, 0.95F);

		baseModel[87].addShapeBox(0F, 0F, 0F, 2, 8, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPART13
		baseModel[87].setRotationPoint(82F, 0F, 0F);

		baseModel[88].addShapeBox(0F, 0F, 0F, 2, 8, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPART13
		baseModel[88].setRotationPoint(48F, 0F, 0F);

		baseModel[89].addShapeBox(0F, 0F, 0F, 12, 6, 0, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainFence00
		baseModel[89].setRotationPoint(15F, 0F, 18F);
		baseModel[89].rotateAngleX = 1.57079633F;
		baseModel[89].rotateAngleZ = 1.57079633F;

		baseModel[90].addShapeBox(0F, 0F, 0F, 12, 6, 0, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainFence00
		baseModel[90].setRotationPoint(15F, 0F, 24F);
		baseModel[90].rotateAngleX = 1.57079633F;
		baseModel[90].rotateAngleZ = 1.57079633F;

		baseModel[91].addShapeBox(0F, 0F, 0F, 2, 14, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainFence00
		baseModel[91].setRotationPoint(14F, -14F, 16F);

		baseModel[92].addShapeBox(0F, 0F, 0F, 2, 14, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainFence00
		baseModel[92].setRotationPoint(14F, -14F, 30F);

		baseModel[93].addShapeBox(0F, 0F, 0F, 12, 6, 0, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainFence00
		baseModel[93].setRotationPoint(2F, 0F, 17F);
		baseModel[93].rotateAngleX = 1.57079633F;
		baseModel[93].rotateAngleY = -1.57079633F;
		baseModel[93].rotateAngleZ = 1.57079633F;

		baseModel[94].addShapeBox(0F, 0F, 0F, 12, 6, 0, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainFence00
		baseModel[94].setRotationPoint(8F, 0F, 17F);
		baseModel[94].rotateAngleX = 1.57079633F;
		baseModel[94].rotateAngleY = -1.57079633F;
		baseModel[94].rotateAngleZ = 1.57079633F;

		baseModel[95].addShapeBox(0F, 0F, 0F, 12, 6, 0, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainFence00
		baseModel[95].setRotationPoint(2F, 0F, 31F);
		baseModel[95].rotateAngleX = 1.57079633F;
		baseModel[95].rotateAngleY = -1.57079633F;
		baseModel[95].rotateAngleZ = 1.57079633F;

		baseModel[96].addShapeBox(0F, 0F, 0F, 12, 6, 0, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainFence00
		baseModel[96].setRotationPoint(8F, 0F, 31F);
		baseModel[96].rotateAngleX = 1.57079633F;
		baseModel[96].rotateAngleY = -1.57079633F;
		baseModel[96].rotateAngleZ = 1.57079633F;

		baseModel[97].addShapeBox(0F, 0F, 0F, 2, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // PAPERSTANDO02
		baseModel[97].setRotationPoint(57F, 2F, -0.05F);

		baseModel[98].addShapeBox(0F, 0F, 0F, 2, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // PAPERSTANDO02
		baseModel[98].setRotationPoint(73F, 2F, -0.05F);

		flipAll();
	}

	@Override
	public void getBlockRotation(EnumFacing facing, boolean mirrored)
	{
		switch(facing)
		{
			case NORTH:
			{
				GlStateManager.rotate(0, 0F, 1F, 0F);
				GlStateManager.translate(-1, 0f, mirrored?2f: -1f);
			}
			break;
			case SOUTH:
			{
				GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.translate(-2, 0f, mirrored?1f:-2f);

			}
			break;
			case EAST:
			{
				GlStateManager.rotate(-90F, 0F, 1F, 0F);
				GlStateManager.translate(-1, 0f, mirrored?1f: -2f);

			}
			break;
			case WEST:
			{
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				GlStateManager.translate(-2, 0f, mirrored?2f: -1f);

			}
			break;
		}
	}
}
