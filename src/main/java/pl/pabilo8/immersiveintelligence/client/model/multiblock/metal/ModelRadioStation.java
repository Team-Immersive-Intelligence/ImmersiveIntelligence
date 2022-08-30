package pl.pabilo8.immersiveintelligence.client.model.multiblock.metal;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;

/**
 * @author Pabilo8
 * @since 20-06-2019
 */
public class ModelRadioStation extends ModelIIBase
{
	int textureX = 128;
	int textureY = 256;

	public ModelRadioStation() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[51];
		baseModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // InputBoxMain
		baseModel[1] = new ModelRendererTurbo(this, 32, 16, textureX, textureY); // BaseBox
		baseModel[2] = new ModelRendererTurbo(this, 0, 56, textureX, textureY); // BaseBox
		baseModel[3] = new ModelRendererTurbo(this, 0, 96, textureX, textureY); // BaseBox
		baseModel[4] = new ModelRendererTurbo(this, 48, 2, textureX, textureY); // BaseBoxStand
		baseModel[5] = new ModelRendererTurbo(this, 48, 2, textureX, textureY); // BaseBoxStand
		baseModel[6] = new ModelRendererTurbo(this, 48, 2, textureX, textureY); // BaseBoxStand
		baseModel[7] = new ModelRendererTurbo(this, 96, 29, textureX, textureY); // BasePole
		baseModel[8] = new ModelRendererTurbo(this, 96, 29, textureX, textureY); // BasePole
		baseModel[9] = new ModelRendererTurbo(this, 96, 29, textureX, textureY); // BasePole
		baseModel[10] = new ModelRendererTurbo(this, 96, 29, textureX, textureY); // BasePole
		baseModel[11] = new ModelRendererTurbo(this, 96, 29, textureX, textureY); // BasePole
		baseModel[12] = new ModelRendererTurbo(this, 96, 29, textureX, textureY); // BasePole
		baseModel[13] = new ModelRendererTurbo(this, 96, 29, textureX, textureY); // BasePole
		baseModel[14] = new ModelRendererTurbo(this, 96, 29, textureX, textureY); // BasePole
		baseModel[15] = new ModelRendererTurbo(this, 96, 29, textureX, textureY); // BasePole
		baseModel[16] = new ModelRendererTurbo(this, 96, 29, textureX, textureY); // BasePole
		baseModel[17] = new ModelRendererTurbo(this, 96, 29, textureX, textureY); // BasePole
		baseModel[18] = new ModelRendererTurbo(this, 96, 29, textureX, textureY); // BasePole
		baseModel[19] = new ModelRendererTurbo(this, 96, 29, textureX, textureY); // BasePole
		baseModel[20] = new ModelRendererTurbo(this, 96, 29, textureX, textureY); // BasePole
		baseModel[21] = new ModelRendererTurbo(this, 96, 29, textureX, textureY); // BasePole
		baseModel[22] = new ModelRendererTurbo(this, 0, 120, textureX, textureY); // InputBoxTop
		baseModel[23] = new ModelRendererTurbo(this, 80, 96, textureX, textureY); // InputBoxTop
		baseModel[24] = new ModelRendererTurbo(this, 64, 127, textureX, textureY); // EnergyInput
		baseModel[25] = new ModelRendererTurbo(this, 80, 107, textureX, textureY); // EnergyCable
		baseModel[26] = new ModelRendererTurbo(this, 16, 140, textureX, textureY); // EnergyCable
		baseModel[27] = new ModelRendererTurbo(this, 40, 120, textureX, textureY); // EnergyCable
		baseModel[28] = new ModelRendererTurbo(this, 28, 128, textureX, textureY); // BasePoleHorizontal
		baseModel[29] = new ModelRendererTurbo(this, 28, 128, textureX, textureY); // BasePoleHorizontal
		baseModel[30] = new ModelRendererTurbo(this, 28, 128, textureX, textureY); // BasePoleHorizontal
		baseModel[31] = new ModelRendererTurbo(this, 28, 128, textureX, textureY); // BasePoleHorizontal
		baseModel[32] = new ModelRendererTurbo(this, 96, 29, textureX, textureY); // BasePole
		baseModel[33] = new ModelRendererTurbo(this, 96, 29, textureX, textureY); // BasePole
		baseModel[34] = new ModelRendererTurbo(this, 96, 29, textureX, textureY); // BasePole
		baseModel[35] = new ModelRendererTurbo(this, 96, 29, textureX, textureY); // BasePole
		baseModel[36] = new ModelRendererTurbo(this, 28, 159, textureX, textureY); // BasePoleHorizontal2
		baseModel[37] = new ModelRendererTurbo(this, 28, 159, textureX, textureY); // BasePoleHorizontal2
		baseModel[38] = new ModelRendererTurbo(this, 28, 159, textureX, textureY); // BasePoleHorizontal2
		baseModel[39] = new ModelRendererTurbo(this, 28, 159, textureX, textureY); // BasePoleHorizontal2
		baseModel[40] = new ModelRendererTurbo(this, 96, 29, textureX, textureY); // BasePole
		baseModel[41] = new ModelRendererTurbo(this, 96, 29, textureX, textureY); // BasePole
		baseModel[42] = new ModelRendererTurbo(this, 96, 29, textureX, textureY); // BasePole
		baseModel[43] = new ModelRendererTurbo(this, 96, 29, textureX, textureY); // BasePole
		baseModel[44] = new ModelRendererTurbo(this, 96, 29, textureX, textureY); // BasePole
		baseModel[45] = new ModelRendererTurbo(this, 96, 29, textureX, textureY); // BasePole
		baseModel[46] = new ModelRendererTurbo(this, 96, 29, textureX, textureY); // BasePole
		baseModel[47] = new ModelRendererTurbo(this, 96, 29, textureX, textureY); // BasePole
		baseModel[48] = new ModelRendererTurbo(this, 0, 78, textureX, textureY); // TopOrb
		baseModel[49] = new ModelRendererTurbo(this, 0, 78, textureX, textureY); // TopOrb
		baseModel[50] = new ModelRendererTurbo(this, 0, 56, textureX, textureY); // TopOrb

		baseModel[0].addBox(0F, 0F, 0F, 16, 32, 16, 0F); // InputBoxMain
		baseModel[0].setRotationPoint(0F, -32F, 0F);

		baseModel[1].addBox(0F, 0F, 0F, 16, 8, 32, 0F); // BaseBox
		baseModel[1].setRotationPoint(0F, -8F, 16F);

		baseModel[2].addBox(0F, 0F, 0F, 32, 8, 32, 0F); // BaseBox
		baseModel[2].setRotationPoint(16F, -8F, 0F);

		baseModel[3].addBox(0F, 0F, 0F, 32, 8, 16, 0F); // BaseBox
		baseModel[3].setRotationPoint(16F, -8F, 32F);

		baseModel[4].addBox(0F, 0F, 0F, 6, 8, 6, 0F); // BaseBoxStand
		baseModel[4].setRotationPoint(40F, -16F, 2F);

		baseModel[5].addBox(0F, 0F, 0F, 6, 8, 6, 0F); // BaseBoxStand
		baseModel[5].setRotationPoint(40F, -16F, 40F);

		baseModel[6].addBox(0F, 0F, 0F, 6, 8, 6, 0F); // BaseBoxStand
		baseModel[6].setRotationPoint(2F, -16F, 40F);

		baseModel[7].addBox(-1.5F, -16F, -1.5F, 3, 16, 3, 0F); // BasePole
		baseModel[7].setRotationPoint(43F, -13F, 43F);
		baseModel[7].rotateAngleY = 0.76794487F;
		baseModel[7].rotateAngleZ = 0.20943951F;

		baseModel[8].addBox(-1.5F, -32F, -1.5F, 3, 16, 3, 0F); // BasePole
		baseModel[8].setRotationPoint(43F, -13F, 43F);
		baseModel[8].rotateAngleY = 0.78539816F;
		baseModel[8].rotateAngleZ = 0.20943951F;

		baseModel[9].addBox(-1.5F, -48F, -1.5F, 3, 16, 3, 0F); // BasePole
		baseModel[9].setRotationPoint(43F, -13F, 43F);
		baseModel[9].rotateAngleY = 0.76794487F;
		baseModel[9].rotateAngleZ = 0.20943951F;

		baseModel[10].addBox(-1.5F, -64F, -1.5F, 3, 16, 3, 0F); // BasePole
		baseModel[10].setRotationPoint(43F, -13F, 43F);
		baseModel[10].rotateAngleY = 0.78539816F;
		baseModel[10].rotateAngleZ = 0.20943951F;

		baseModel[11].addBox(-1.5F, -16F, -1.5F, 3, 16, 3, 0F); // BasePole
		baseModel[11].setRotationPoint(43F, -13F, 5F);
		baseModel[11].rotateAngleY = -0.78539816F;
		baseModel[11].rotateAngleZ = 0.20943951F;

		baseModel[12].addBox(-1.5F, -32F, -1.5F, 3, 16, 3, 0F); // BasePole
		baseModel[12].setRotationPoint(43F, -13F, 5F);
		baseModel[12].rotateAngleY = -0.76794487F;
		baseModel[12].rotateAngleZ = 0.20943951F;

		baseModel[13].addBox(-1.5F, -48F, -1.5F, 3, 16, 3, 0F); // BasePole
		baseModel[13].setRotationPoint(43F, -13F, 5F);
		baseModel[13].rotateAngleY = -0.78539816F;
		baseModel[13].rotateAngleZ = 0.20943951F;

		baseModel[14].addBox(-1.5F, -64F, -1.5F, 3, 16, 3, 0F); // BasePole
		baseModel[14].setRotationPoint(43F, -13F, 5F);
		baseModel[14].rotateAngleY = -0.76794487F;
		baseModel[14].rotateAngleZ = 0.20943951F;

		baseModel[15].addBox(-1.5F, -16F, -1.5F, 3, 16, 3, 0F); // BasePole
		baseModel[15].setRotationPoint(5F, -13F, 43F);
		baseModel[15].rotateAngleY = 2.3387412F;
		baseModel[15].rotateAngleZ = 0.20943951F;

		baseModel[16].addBox(-1.5F, -32F, -1.5F, 3, 16, 3, 0F); // BasePole
		baseModel[16].setRotationPoint(5F, -13F, 43F);
		baseModel[16].rotateAngleY = 2.35619449F;
		baseModel[16].rotateAngleZ = 0.20943951F;

		baseModel[17].addBox(-1.5F, -48F, -1.5F, 3, 16, 3, 0F); // BasePole
		baseModel[17].setRotationPoint(5F, -13F, 43F);
		baseModel[17].rotateAngleY = 2.3387412F;
		baseModel[17].rotateAngleZ = 0.20943951F;

		baseModel[18].addBox(-1.5F, -64F, -1.5F, 3, 16, 3, 0F); // BasePole
		baseModel[18].setRotationPoint(5F, -13F, 43F);
		baseModel[18].rotateAngleY = 2.35619449F;
		baseModel[18].rotateAngleZ = 0.20943951F;

		baseModel[19].addBox(-1.5F, -32F, -1.5F, 3, 16, 3, 0F); // BasePole
		baseModel[19].setRotationPoint(5F, -13F, 5F);
		baseModel[19].rotateAngleY = -2.35619449F;
		baseModel[19].rotateAngleZ = 0.20943951F;

		baseModel[20].addBox(-1.5F, -48F, -1.5F, 3, 16, 3, 0F); // BasePole
		baseModel[20].setRotationPoint(5F, -13F, 5F);
		baseModel[20].rotateAngleY = -2.37364778F;
		baseModel[20].rotateAngleZ = 0.20943951F;

		baseModel[21].addBox(-1.5F, -64F, -1.5F, 3, 16, 3, 0F); // BasePole
		baseModel[21].setRotationPoint(5F, -13F, 5F);
		baseModel[21].rotateAngleY = -2.35619449F;
		baseModel[21].rotateAngleZ = 0.20943951F;

		baseModel[22].addBox(0F, 0F, 0F, 10, 3, 10, 0F); // InputBoxTop
		baseModel[22].setRotationPoint(3F, -35F, 3F);

		baseModel[23].addTrapezoid(0F, 0F, 0F, 10, 1, 10, 0F, -1.00F, ModelRendererTurbo.MR_TOP); // InputBoxTop
		baseModel[23].setRotationPoint(3F, -36F, 3F);

		baseModel[24].addBox(0F, 0F, 0F, 16, 8, 16, 0F); // EnergyInput
		baseModel[24].setRotationPoint(16F, -16F, 32F);

		baseModel[25].addBox(0F, 0F, 0F, 4, 4, 16, 0F); // EnergyCable
		baseModel[25].setRotationPoint(22F, -12F, 16F);

		baseModel[26].addBox(0F, 0F, 0F, 16, 4, 4, 0F); // EnergyCable
		baseModel[26].setRotationPoint(6F, -12F, 16F);

		baseModel[27].addBox(0F, 0F, 0F, 4, 16, 4, 0F); // EnergyCable
		baseModel[27].setRotationPoint(6F, -28F, 16F);

		baseModel[28].addBox(0F, 0F, 0F, 3, 3, 28, 0F); // BasePoleHorizontal
		baseModel[28].setRotationPoint(36F, -48F, 10F);

		baseModel[29].addBox(0F, 0F, 0F, 3, 3, 28, 0F); // BasePoleHorizontal
		baseModel[29].setRotationPoint(8F, -48F, 10F);

		baseModel[30].addBox(0F, 0F, 0F, 3, 3, 28, 0F); // BasePoleHorizontal
		baseModel[30].setRotationPoint(36F, -48F, 10F);
		baseModel[30].rotateAngleY = 1.57079633F;

		baseModel[31].addBox(0F, 0F, 0F, 3, 3, 28, 0F); // BasePoleHorizontal
		baseModel[31].setRotationPoint(38F, -48F, 36F);
		baseModel[31].rotateAngleY = 1.57079633F;

		baseModel[32].addBox(-1.5F, -80F, -1.5F, 3, 16, 3, 0F); // BasePole
		baseModel[32].setRotationPoint(43F, -13F, 43F);
		baseModel[32].rotateAngleY = 0.78539816F;
		baseModel[32].rotateAngleZ = 0.20943951F;

		baseModel[33].addBox(-1.5F, -80F, -1.5F, 3, 16, 3, 0F); // BasePole
		baseModel[33].setRotationPoint(43F, -13F, 5F);
		baseModel[33].rotateAngleY = -0.76794487F;
		baseModel[33].rotateAngleZ = 0.20943951F;

		baseModel[34].addBox(-1.5F, -80F, -1.5F, 3, 16, 3, 0F); // BasePole
		baseModel[34].setRotationPoint(5F, -13F, 43F);
		baseModel[34].rotateAngleY = 2.35619449F;
		baseModel[34].rotateAngleZ = 0.20943951F;

		baseModel[35].addBox(-1.5F, -80F, -1.5F, 3, 16, 3, 0F); // BasePole
		baseModel[35].setRotationPoint(5F, -13F, 5F);
		baseModel[35].rotateAngleY = -2.35619449F;
		baseModel[35].rotateAngleZ = 0.20943951F;

		baseModel[36].addBox(0F, 0F, 0F, 3, 3, 16, 0F); // BasePoleHorizontal2
		baseModel[36].setRotationPoint(31F, -84F, 16F);

		baseModel[37].addBox(0F, 0F, 0F, 3, 3, 16, 0F); // BasePoleHorizontal2
		baseModel[37].setRotationPoint(14F, -84F, 17F);

		baseModel[38].addBox(0F, 0F, 0F, 3, 3, 16, 0F); // BasePoleHorizontal2
		baseModel[38].setRotationPoint(32F, -84F, 14F);
		baseModel[38].rotateAngleY = 1.57079633F;

		baseModel[39].addBox(0F, 0F, 0F, 3, 3, 16, 0F); // BasePoleHorizontal2
		baseModel[39].setRotationPoint(32F, -84F, 31F);
		baseModel[39].rotateAngleY = 1.57079633F;

		baseModel[40].addBox(-1.5F, -96F, -1.5F, 3, 16, 3, 0F); // BasePole
		baseModel[40].setRotationPoint(43F, -13F, 43F);
		baseModel[40].rotateAngleY = 0.78539816F;
		baseModel[40].rotateAngleZ = 0.20943951F;

		baseModel[41].addBox(-1.5F, -96F, -1.5F, 3, 16, 3, 0F); // BasePole
		baseModel[41].setRotationPoint(43F, -13F, 5F);
		baseModel[41].rotateAngleY = -0.76794487F;
		baseModel[41].rotateAngleZ = 0.20943951F;

		baseModel[42].addBox(-1.5F, -96F, -1.5F, 3, 16, 3, 0F); // BasePole
		baseModel[42].setRotationPoint(5F, -13F, 43F);
		baseModel[42].rotateAngleY = 2.35619449F;
		baseModel[42].rotateAngleZ = 0.20943951F;

		baseModel[43].addBox(-1.5F, -96F, -1.5F, 3, 16, 3, 0F); // BasePole
		baseModel[43].setRotationPoint(5F, -13F, 5F);
		baseModel[43].rotateAngleY = -2.35619449F;
		baseModel[43].rotateAngleZ = 0.20943951F;

		baseModel[44].addBox(-1.5F, -112F, -1.5F, 3, 16, 3, 0F); // BasePole
		baseModel[44].setRotationPoint(43F, -13F, 43F);
		baseModel[44].rotateAngleY = 0.78539816F;
		baseModel[44].rotateAngleZ = 0.20943951F;

		baseModel[45].addBox(-1.5F, -112F, -1.5F, 3, 16, 3, 0F); // BasePole
		baseModel[45].setRotationPoint(43F, -13F, 5F);
		baseModel[45].rotateAngleY = -0.76794487F;
		baseModel[45].rotateAngleZ = 0.20943951F;

		baseModel[46].addBox(-1.5F, -112F, -1.5F, 3, 16, 3, 0F); // BasePole
		baseModel[46].setRotationPoint(5F, -13F, 43F);
		baseModel[46].rotateAngleY = 2.35619449F;
		baseModel[46].rotateAngleZ = 0.20943951F;

		baseModel[47].addBox(-1.5F, -112F, -1.5F, 3, 16, 3, 0F); // BasePole
		baseModel[47].setRotationPoint(5F, -13F, 5F);
		baseModel[47].rotateAngleY = -2.35619449F;
		baseModel[47].rotateAngleZ = 0.20943951F;

		baseModel[48].addTrapezoid(0F, 0F, 0F, 8, 2, 8, 0F, -1.00F, ModelRendererTurbo.MR_BOTTOM); // TopOrb
		baseModel[48].setRotationPoint(20F, -123F, 20F);

		baseModel[49].addTrapezoid(0F, 0F, 0F, 8, 2, 8, 0F, -1.00F, ModelRendererTurbo.MR_TOP); // TopOrb
		baseModel[49].setRotationPoint(20F, -129F, 20F);

		baseModel[50].addBox(0F, 0F, 0F, 8, 4, 8, 0F); // TopOrb
		baseModel[50].setRotationPoint(20F, -127F, 20F);

		flipAll();
	}

	@Override
	public void getBlockRotation(EnumFacing facing, boolean mirrored)
	{
		switch(facing)
		{
			case NORTH:
			{
				GlStateManager.rotate(mirrored?270f: 90F, 0F, 1F, 0F);
				GlStateManager.translate(mirrored?-1f: 0f, -2f, mirrored?-2f: 2f);
			}
			break;
			case SOUTH:
			{
				GlStateManager.rotate(mirrored?90f: 270F, 0F, 1F, 0F);
				GlStateManager.translate(mirrored?0f: -1f, -2f, mirrored?1f: -1f);
			}
			break;
			case EAST:
			{
				if(mirrored)
					GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.translate(mirrored?-2f: 1f, -2f, 0f);
			}
			break;
			case WEST:
			{
				if(!mirrored)
					GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.translate(mirrored?1: -2f, -2f, mirrored?-1f: 1f);
			}
			break;
		}
	}
}
