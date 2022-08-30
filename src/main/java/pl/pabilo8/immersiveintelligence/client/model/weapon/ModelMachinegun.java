package pl.pabilo8.immersiveintelligence.client.model.weapon;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.render.MachinegunRenderer;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Coord2D;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Shape2D;
import pl.pabilo8.immersiveintelligence.client.util.tmt.TmtNamedBoxGroup;

/**
 * Created by Pabilo8 on 07-06-2019.
 * Created using SMP-Toolbox 2.0 (Old stuff, probably considered dead ^^)
 * Thanks to The Flan's Mod Team for the Turbo Model Thingy used in there
 */
public class ModelMachinegun extends ModelIIBase
{
	int textureX = 64;
	int textureY = 128;

	ModelRendererTurbo[] barrelModel, sightsModel, triggerModel, ammoModel, slideModel, gripModel, bipodModel;
	public TmtNamedBoxGroup baseBox, barrelBox, sightsBox, triggerBox, ammoBox, slideBox, gripBox, bipodBox;
	ModelRendererTurbo[] heavyBarrelModel, waterCoolingModel, secondMagazineMainModel, secondMagazineMagModel, beltFedLoaderModel, scopeModel, infraredScopeModel, hastyBipodModel, preciseBipodModel, shieldModel, baubleModel;
	ModelRendererTurbo[] tripodBaseModel, tripodLeg1Model, tripodLeg2Model, tripodLeg3Model;
	public TmtNamedBoxGroup heavyBarrelBox, waterCoolingBox, secondMagazineMainBox, secondMagazineMagBox, beltFedLoaderBox, scopeBox, infraredScopeBox, hastyBipodBox, preciseBipodBox, shieldBox, baubleBox, tripodBox;

	private static final String texture = ImmersiveIntelligence.MODID+":textures/items/weapons/machinegun.png";

	public ModelMachinegun() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[17];
		baseModel[0] = new ModelRendererTurbo(this, 14, 55, textureX, textureY); // BoxMain
		baseModel[1] = new ModelRendererTurbo(this, 0, 68, textureX, textureY); // BoxMainUpper
		baseModel[2] = new ModelRendererTurbo(this, 16, 42, textureX, textureY); // BoxMainFront
		baseModel[3] = new ModelRendererTurbo(this, 29, 36, textureX, textureY); // BoxMainBetweenGrip
		baseModel[4] = new ModelRendererTurbo(this, 0, 49, textureX, textureY); // BoxMainVent
		baseModel[5] = new ModelRendererTurbo(this, 38, 54, textureX, textureY); // BoxMainSidePlate
		baseModel[6] = new ModelRendererTurbo(this, 36, 37, textureX, textureY); // AmmoBox
		baseModel[7] = new ModelRendererTurbo(this, 17, 25, textureX, textureY); // AmmoBoxLower
		baseModel[8] = new ModelRendererTurbo(this, 26, 41, textureX, textureY); // AmmoBoxTop
		baseModel[9] = new ModelRendererTurbo(this, 0, 31, textureX, textureY); // BoxMainGrip
		baseModel[10] = new ModelRendererTurbo(this, 36, 35, textureX, textureY); // BoxTriggerPart
		baseModel[11] = new ModelRendererTurbo(this, 20, 47, textureX, textureY); // BoxTriggerPart
		baseModel[12] = new ModelRendererTurbo(this, 10, 47, textureX, textureY); // BoxTriggerPart
		baseModel[13] = new ModelRendererTurbo(this, 6, 47, textureX, textureY); // BoxTriggerPart
		baseModel[14] = new ModelRendererTurbo(this, 8, 37, textureX, textureY); // BoxBipodRodEnd
		baseModel[15] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // BoxBipodRod
		baseModel[16] = new ModelRendererTurbo(this, 18, 32, textureX, textureY); // BoxBipod

		baseModel[0].addBox(0F, 0F, 0F, 4, 5, 8, 0F); // BoxMain
		baseModel[0].setRotationPoint(6F, -10F, 0F);

		baseModel[1].addFlexTrapezoid(0F, 0F, 0F, 4, 1, 16, 0F, -1.00F, -1.00F, 0F, 0F, 0F, 0F, ModelRendererTurbo.MR_TOP); // BoxMainUpper
		baseModel[1].setRotationPoint(6F, -11F, 0F);

		baseModel[2].addBox(0F, 0F, 0F, 4, 4, 1, 0F); // BoxMainFront
		baseModel[2].setRotationPoint(6F, -10F, 15F);

		baseModel[3].addShapeBox(0F, 0F, 0F, 3, 4, 1, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F); // BoxMainBetweenGrip
		baseModel[3].setRotationPoint(6.5F, -10F, -1F);

		baseModel[4].addShapeBox(0F, 0F, 0F, 4, 4, 7, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F); // BoxMainVent
		baseModel[4].setRotationPoint(6F, -10F, 8F);

		baseModel[5].addShapeBox(0F, 0F, 0F, 1, 4, 6, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F); // BoxMainSidePlate
		baseModel[5].setRotationPoint(5.25F, -9.5F, 1F);

		baseModel[6].addBox(0F, 0F, 0F, 2, 3, 6, 0F); // AmmoBox
		baseModel[6].setRotationPoint(10F, -10F, 1F);

		baseModel[7].addFlexTrapezoid(0F, 0F, 0F, 2, 1, 6, 0F, 0.00F, -1.00F, 0.00F, 0.00F, 0.00F, 0.00F, ModelRendererTurbo.MR_BOTTOM); // AmmoBoxLower
		baseModel[7].setRotationPoint(10F, -7F, 1F);

		baseModel[8].addShapeBox(0F, 0F, 0F, 2, 1, 5, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F); // AmmoBoxTop
		baseModel[8].setRotationPoint(10F, -10.5F, 1.5F);

		baseModel[9].addTrapezoid(0F, 0F, 0F, 4, 5, 1, 0F, -0.50F, ModelRendererTurbo.MR_FRONT); // BoxMainGrip
		baseModel[9].setRotationPoint(6F, -10.5F, -1.5F);

		baseModel[10].addFlexBox(0F, 0F, 0F, 2, 1, 1, 0F, -0.50F, -0.50F, 0F, 0F, ModelRendererTurbo.MR_BOTTOM); // BoxTriggerPart
		baseModel[10].setRotationPoint(7.5F, -5F, 6F);

		baseModel[11].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F); // BoxTriggerPart
		baseModel[11].setRotationPoint(8F, -4.5F, 5F);

		baseModel[12].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BoxTriggerPart
		baseModel[12].setRotationPoint(8F, -4.5F, 4F);

		baseModel[13].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0.5F, -0.25F, 0F, 0.5F, -0.25F, 0F, 0.5F, -0.25F, 0F, 0.5F, -0.25F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F); // BoxTriggerPart
		baseModel[13].setRotationPoint(8F, -4.5F, 3F);

		baseModel[14].addShapeBox(-1F, 0F, 0F, 2, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F); // BoxBipodRodEnd
		baseModel[14].setRotationPoint(8F, -6.75F, 27F);

		baseModel[15].addBox(-1F, 0F, 0F, 1, 1, 12, 0F); // BoxBipodRod
		baseModel[15].setRotationPoint(8.5F, -6F, 15F);

		baseModel[16].addShapeBox(-1F, 0F, 0F, 2, 2, 2, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F); // BoxBipod
		baseModel[16].setRotationPoint(8F, -6.5F, 25.5F);


		barrelModel = new ModelRendererTurbo[8];
		barrelModel[0] = new ModelRendererTurbo(this, 0, 24, textureX, textureY); // BarrelBigBox
		barrelModel[1] = new ModelRendererTurbo(this, 43, 27, textureX, textureY); // BarrelBack
		barrelModel[2] = new ModelRendererTurbo(this, 38, 0, textureX, textureY); // BoxMain
		barrelModel[3] = new ModelRendererTurbo(this, 35, 27, textureX, textureY); // BarrelEnd2
		barrelModel[4] = new ModelRendererTurbo(this, 14, 38, textureX, textureY); // BarrelEnd3
		barrelModel[5] = new ModelRendererTurbo(this, 40, 8, textureX, textureY); // BoxMain
		barrelModel[6] = new ModelRendererTurbo(this, 40, 8, textureX, textureY); // BoxBipodRod
		barrelModel[7] = new ModelRendererTurbo(this, 40, 8, textureX, textureY); // BoxBipodRod

		barrelModel[0].addShapeBox(0F, 0F, 0F, 4, 4, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -1F, 0F, -0.5F, -1F, 0F); // BarrelBigBox
		barrelModel[0].setRotationPoint(6F, -10F, 16F);

		barrelModel[1].addBox(0F, 0F, 0F, 3, 3, 1, 0F); // BarrelBack
		barrelModel[1].setRotationPoint(6.5F, -10F, 19F);

		barrelModel[2].addShapeBox(0F, 0F, 0F, 2, 2, 6, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F); // BoxMain
		barrelModel[2].setRotationPoint(7F, -10F, 28F);

		barrelModel[3].addShapeBox(0F, 0F, 0F, 3, 3, 1, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F); // BarrelEnd2
		barrelModel[3].setRotationPoint(6.5F, -10.5F, 34F);

		barrelModel[4].addShapeBox(0F, 0F, 0F, 3, 3, 1, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F); // BarrelEnd3
		barrelModel[4].setRotationPoint(6.5F, -10.5F, 35F);

		barrelModel[5].addShapeBox(0F, 0F, 0F, 2, 3, 8, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F); // BoxMain
		barrelModel[5].setRotationPoint(7F, -10F, 20F);

		barrelModel[6].addBox(-1F, 0F, 0F, 1, 1, 1, 0F); // BoxBipodRod
		barrelModel[6].setRotationPoint(8.5F, -6F, 28F);

		barrelModel[7].addBox(-1F, 0F, 0F, 1, 2, 1, 0F); // BoxBipodRod
		barrelModel[7].setRotationPoint(8.5F, -8.25F, 26F);

		sightsModel = new ModelRendererTurbo[6];
		sightsModel[0] = new ModelRendererTurbo(this, 4, 42, textureX, textureY); // BoxMainSightsPlate
		sightsModel[1] = new ModelRendererTurbo(this, 24, 47, textureX, textureY); // BoxMainSightsFront
		sightsModel[2] = new ModelRendererTurbo(this, 21, 36, textureX, textureY); // BoxMainSightsPlateBack
		sightsModel[3] = new ModelRendererTurbo(this, 26, 32, textureX, textureY); // BoxMainSightsPlate
		sightsModel[4] = new ModelRendererTurbo(this, 14, 47, textureX, textureY); // BoxMainSightsBack
		sightsModel[5] = new ModelRendererTurbo(this, 0, 47, textureX, textureY); // BoxMainSightsBack

		sightsModel[0].addShapeBox(0F, 0F, 0F, 2, 1, 4, 0F, -0.25F, -0.5F, 0F, -0.25F, -0.5F, 0F, -0.25F, -0.5F, 0F, -0.25F, -0.5F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F); // BoxMainSightsPlate
		sightsModel[0].setRotationPoint(7F, -12F, 11F);

		sightsModel[1].addShapeBox(-0.5F, -0.5F, 0F, 1, 1, 1, 0F, -0.375F, 0F, 0F, -0.375F, 0F, 0F, -0.375F, 0F, 0F, -0.375F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F); // BoxMainSightsFront
		sightsModel[1].setRotationPoint(8F, -12F, 12F);

		sightsModel[2].addShapeBox(0F, 0F, -2F, 2, 1, 2, 0F, -0.25F, -0.5F, 0F, -0.25F, -0.5F, 0F, -0.25F, -0.5F, 0F, -0.25F, -0.5F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F); // BoxMainSightsPlateBack
		sightsModel[2].setRotationPoint(7F, -12F, 0.5F);
		sightsModel[2].rotateAngleX = 0.29670597F;

		sightsModel[3].addShapeBox(0F, 0F, 0F, 2, 1, 3, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F); // BoxMainSightsPlate
		sightsModel[3].setRotationPoint(7F, -12F, 1F);

		sightsModel[4].addShapeBox(0F, 0F, 0F, 2, 1, 1, 0F, -0.25F, -0.5F, 0F, -1.25F, -0.5F, 0F, -1.25F, -0.5F, 0F, -0.25F, -0.5F, 0F, -0.5F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, -0.5F, 0F, 0F); // BoxMainSightsBack
		sightsModel[4].setRotationPoint(7F, -12.5F, 1F);

		sightsModel[5].addShapeBox(0F, 0F, 0F, 2, 1, 1, 0F, -1.25F, -0.5F, 0F, -0.25F, -0.5F, 0F, -0.25F, -0.5F, 0F, -1.25F, -0.5F, 0F, -1F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -1F, 0F, 0F); // BoxMainSightsBack
		sightsModel[5].setRotationPoint(7F, -12.5F, 1F);


		triggerModel = new ModelRendererTurbo[1];
		triggerModel[0] = new ModelRendererTurbo(this, 14, 36, textureX, textureY); // BoxTrigger

		triggerModel[0].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0.5F, -0.25F, 0F, 0.5F, -0.25F, 0F, 0.5F, -0.25F, 0F, 0.5F, -0.25F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F); // BoxTrigger
		triggerModel[0].setRotationPoint(8F, -5.25F, 4F);


		gripModel = new ModelRendererTurbo[5];
		gripModel[0] = new ModelRendererTurbo(this, 34, 46, textureX, textureY); // GripLowerFront
		gripModel[1] = new ModelRendererTurbo(this, 21, 47, textureX, textureY); // GripLowerFrontLower
		gripModel[2] = new ModelRendererTurbo(this, 10, 31, textureX, textureY); // GripBackFront
		gripModel[3] = new ModelRendererTurbo(this, 0, 16, textureX, textureY); // GripBack
		gripModel[4] = new ModelRendererTurbo(this, 33, 31, textureX, textureY); // GripBottom

		gripModel[0].addBox(0F, 0F, 0F, 3, 1, 7, 0F); // GripLowerFront
		gripModel[0].setRotationPoint(6.5F, -6F, 8F);

		gripModel[1].addFlexBox(0F, 0F, 0F, 3, 1, 7, 0F, -0.50F, -0.50F, 0F, 0F, ModelRendererTurbo.MR_BOTTOM); // GripLowerFrontLower
		gripModel[1].setRotationPoint(6.5F, -5F, 8F);

		gripModel[2].addShapeBox(0F, 0F, 0F, 3, 4, 1, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GripBackFront
		gripModel[2].setRotationPoint(6.5F, -10F, -2.5F);

		gripModel[3].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 2, 0, 2), new Coord2D(4, 1, 4, 1), new Coord2D(5, 1, 5, 1), new Coord2D(6, 1, 6, 1), new Coord2D(5, 4, 5, 4), new Coord2D(6, 6, 6, 6), new Coord2D(5, 7, 5, 7), new Coord2D(0, 6, 0, 6)}), 1, 6, 7, 26, 1, ModelRendererTurbo.MR_FRONT, new float[]{4, 6, 2, 3, 4, 1, 1, 5}); // GripBack
		gripModel[3].setRotationPoint(7.5F, -4F, -2.5F);
		gripModel[3].rotateAngleY = -4.71238898F;

		gripModel[4].addShape3D(0.5F, 0F, -0.5F, new Shape2D(new Coord2D[]{new Coord2D(0, 3, 0, 3), new Coord2D(1, 2, 1, 2), new Coord2D(3, 0, 3, 0), new Coord2D(4, 0, 4, 0), new Coord2D(5, 1, 5, 1), new Coord2D(3, 3, 3, 3)}), 1, 5, 3, 14, 1, ModelRendererTurbo.MR_FRONT, new float[]{3, 3, 2, 1, 3, 2}); // GripBottom
		gripModel[4].setRotationPoint(8.5F, -2F, 3F);
		gripModel[4].rotateAngleY = -4.71238898F;


		ammoModel = new ModelRendererTurbo[1];
		ammoModel[0] = new ModelRendererTurbo(this, 0, 85, textureX, textureY); // AmmoCartridge

		ammoModel[0].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(4, 0, 4, 0), new Coord2D(4, 2, 4, 2), new Coord2D(5, 5, 5, 5), new Coord2D(6, 8, 6, 8), new Coord2D(3, 9, 3, 9), new Coord2D(1, 6, 1, 6), new Coord2D(0, 3, 0, 3)}), 1, 6, 9, 29, 1, ModelRendererTurbo.MR_FRONT, new float[]{3, 4, 4, 4, 4, 4, 2, 4}); // AmmoCartridge
		ammoModel[0].setRotationPoint(11.5F, -10.25F, 2F);
		ammoModel[0].rotateAngleY = -1.57079633F;


		slideModel = new ModelRendererTurbo[2];
		slideModel[0] = new ModelRendererTurbo(this, 14, 28, textureX, textureY); // SlideRod
		slideModel[1] = new ModelRendererTurbo(this, 14, 25, textureX, textureY); // SlideHandle

		slideModel[0].addShapeBox(-2F, 0F, -0.5F, 2, 1, 1, 0F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F); // SlideRod
		slideModel[0].setRotationPoint(6.75F, -11F, 7F);
		slideModel[0].rotateAngleY = 0.2268928F;

		slideModel[1].addShapeBox(-3F, 0F, -1F, 1, 1, 2, 0F, -0.125F, -0.25F, -0.25F, -0.125F, -0.25F, -0.25F, -0.125F, -0.25F, -0.25F, -0.125F, -0.25F, -0.25F, -0.125F, -0.25F, -0.25F, -0.125F, -0.25F, -0.25F, -0.125F, -0.25F, -0.25F, -0.125F, -0.25F, -0.25F); // SlideHandle
		slideModel[1].setRotationPoint(6.88F, -11F, 7F);
		slideModel[1].rotateAngleY = 0.2268928F;


		bipodModel = new ModelRendererTurbo[2];
		bipodModel[0] = new ModelRendererTurbo(this, 4, 37, textureX, textureY); // BoxBipodLeg
		bipodModel[1] = new ModelRendererTurbo(this, 0, 37, textureX, textureY); // BoxBipodLeg
		bipodModel[0].addBox(-1F, 0F, 0F, 1, 8, 1, 0F); // BoxBipodLeg
		bipodModel[1].addBox(0F, 0F, 0F, 1, 8, 1, 0F); // BoxBipodLeg

		parts.put("base", baseModel);
		parts.put("barrel", barrelModel);
		parts.put("sights", sightsModel);
		parts.put("trigger", triggerModel);
		parts.put("ammo", ammoModel);
		parts.put("slide", slideModel);
		parts.put("grip", gripModel);
		parts.put("bipod", bipodModel);

		//Heavy Barrel

		heavyBarrelModel = new ModelRendererTurbo[7];
		heavyBarrelModel[0] = new ModelRendererTurbo(this, 0, 24, textureX, textureY); // BarrelBigBox
		heavyBarrelModel[1] = new ModelRendererTurbo(this, 43, 27, textureX, textureY); // BarrelBack
		heavyBarrelModel[2] = new ModelRendererTurbo(this, 22, 8, textureX, textureY); // Barrel
		heavyBarrelModel[3] = new ModelRendererTurbo(this, 27, 25, textureX, textureY); // BarrelEnd1
		heavyBarrelModel[4] = new ModelRendererTurbo(this, 35, 27, textureX, textureY); // BarrelEnd2
		heavyBarrelModel[5] = new ModelRendererTurbo(this, 14, 38, textureX, textureY); // BarrelEnd3
		heavyBarrelModel[6] = new ModelRendererTurbo(this, 38, 115, textureX, textureY); // BoxMain

		heavyBarrelModel[0].addShapeBox(0F, 0F, 0F, 4, 4, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F); // BarrelBigBox
		heavyBarrelModel[0].setRotationPoint(6F, -10F, 16F);

		heavyBarrelModel[1].addBox(0F, 0F, 0F, 3, 3, 1, 0F); // BarrelBack
		heavyBarrelModel[1].setRotationPoint(6.5F, -9.5F, 19F);

		heavyBarrelModel[2].addShapeBox(0F, 0F, 0F, 3, 3, 12, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F); // Barrel
		heavyBarrelModel[2].setRotationPoint(6.5F, -9.5F, 20F);
		heavyBarrelModel[2].flip = true;

		heavyBarrelModel[3].addShapeBox(0F, 0F, 0F, 3, 3, 1, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F); // BarrelEnd1
		heavyBarrelModel[3].setRotationPoint(6.5F, -9.5F, 32F);

		heavyBarrelModel[4].addShapeBox(0F, 0F, 0F, 3, 3, 1, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F); // BarrelEnd2
		heavyBarrelModel[4].setRotationPoint(6.5F, -9.5F, 33F);

		heavyBarrelModel[5].addShapeBox(0F, 0F, 0F, 3, 3, 1, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F); // BarrelEnd3
		heavyBarrelModel[5].setRotationPoint(6.5F, -9.5F, 34F);

		heavyBarrelModel[6].addBox(0F, 0F, 0F, 1, 1, 12, 0F); // BoxMain
		heavyBarrelModel[6].setRotationPoint(7.5F, -8.5F, 20F);

		parts.put("heavyBarrel", heavyBarrelModel);

		//Water Cooled Barrel
		waterCoolingModel = new ModelRendererTurbo[14];
		waterCoolingModel[0] = new ModelRendererTurbo(this, 8, 95, textureX, textureY); // BarrelEnd1
		waterCoolingModel[1] = new ModelRendererTurbo(this, 16, 95, textureX, textureY); // BarrelEnd2
		waterCoolingModel[2] = new ModelRendererTurbo(this, 14, 38, textureX, textureY); // BarrelEnd3
		waterCoolingModel[3] = new ModelRendererTurbo(this, 0, 95, textureX, textureY); // Shape 41
		waterCoolingModel[4] = new ModelRendererTurbo(this, 0, 116, textureX, textureY); // BarrelBarrel
		waterCoolingModel[5] = new ModelRendererTurbo(this, 18, 107, textureX, textureY); // BarrelBarrel
		waterCoolingModel[6] = new ModelRendererTurbo(this, 18, 99, textureX, textureY); // BarrelBarrel
		waterCoolingModel[7] = new ModelRendererTurbo(this, 14, 116, textureX, textureY); // BarrelBarrel
		waterCoolingModel[8] = new ModelRendererTurbo(this, 18, 103, textureX, textureY); // BarrelBarrel
		waterCoolingModel[9] = new ModelRendererTurbo(this, 16, 116, textureX, textureY); // BarrelBarrel
		waterCoolingModel[10] = new ModelRendererTurbo(this, 0, 118, textureX, textureY); // BarrelBarrel
		waterCoolingModel[11] = new ModelRendererTurbo(this, 20, 122, textureX, textureY); // BarrelBarrel
		waterCoolingModel[12] = new ModelRendererTurbo(this, 0, 116, textureX, textureY); // BarrelBarrel
		waterCoolingModel[13] = new ModelRendererTurbo(this, 18, 111, textureX, textureY); // BarrelBarrel

		waterCoolingModel[0].addShapeBox(0F, 0F, 0F, 3, 3, 1, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F); // BarrelEnd1
		waterCoolingModel[0].setRotationPoint(6.5F, -10.5F, 32F);

		waterCoolingModel[1].addShapeBox(0F, 0F, 0F, 3, 3, 1, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F); // BarrelEnd2
		waterCoolingModel[1].setRotationPoint(6.5F, -10.5F, 33F);

		waterCoolingModel[2].addShapeBox(0F, 0F, 0F, 3, 3, 1, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F); // BarrelEnd3
		waterCoolingModel[2].setRotationPoint(6.5F, -10.5F, 34F);

		waterCoolingModel[3].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(1, 0, 1, 0), new Coord2D(3, 0, 3, 0), new Coord2D(4, 1, 4, 1), new Coord2D(4, 4, 4, 4), new Coord2D(3, 5, 3, 5), new Coord2D(1, 5, 1, 5), new Coord2D(0, 4, 0, 4), new Coord2D(0, 1, 0, 1)}), 16, 4, 5, 18, 16, ModelRendererTurbo.MR_FRONT, new float[]{2, 3, 2, 2, 2, 3, 2, 2}); // Shape 41
		waterCoolingModel[3].setRotationPoint(10F, -6F, 32F);

		waterCoolingModel[4].addBox(0F, 0F, 0F, 4, 4, 6, 0F); // BarrelBarrel
		waterCoolingModel[4].setRotationPoint(10F, -10F, 11F);

		waterCoolingModel[5].addShapeBox(0F, 0F, 0F, 3, 3, 1, 0F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F); // BarrelBarrel
		waterCoolingModel[5].setRotationPoint(10.5F, -9.5F, 16.5F);

		waterCoolingModel[6].addShapeBox(0F, 0F, 0F, 1, 2, 2, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F); // BarrelBarrel
		waterCoolingModel[6].setRotationPoint(9.75F, -9.5F, 21.5F);

		waterCoolingModel[7].addShapeBox(0F, 0F, 0F, 1, 2, 2, 0F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F); // BarrelBarrel
		waterCoolingModel[7].setRotationPoint(10.5F, -9.5F, 21.5F);

		waterCoolingModel[8].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, -0.25F, -0.25F, -0.75F, -0.25F, -0.25F, -1F, -0.25F, -0.5F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, -0.75F, -0.25F, -0.25F, -1F, -0.25F, -0.5F, 0F, -0.25F, -0.25F); // BarrelBarrel
		waterCoolingModel[8].setRotationPoint(11.5F, -9.5F, 21.5F);

		waterCoolingModel[9].addShapeBox(0F, 0F, 0F, 2, 2, 4, 0F, 0F, -0.25F, -0.25F, -0.5F, -0.25F, -0.25F, -0.75F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, -0.5F, -0.25F, -0.25F, -0.75F, -0.25F, -0.25F, 0F, -0.25F, -0.25F); // BarrelBarrel
		waterCoolingModel[9].setRotationPoint(11.5F, -9F, 17F);

		waterCoolingModel[10].addShapeBox(0F, 0F, 0F, 2, 2, 1, 0F, 0F, -0.25F, 0F, -0.75F, -0.25F, 0F, -0.75F, 0.25F, 0F, 0F, 0.25F, 0F, 0F, -0.25F, 0F, -0.75F, -0.25F, 0F, -0.75F, -0.75F, 0F, 0F, -0.75F, 0F); // BarrelBarrel
		waterCoolingModel[10].setRotationPoint(11.5F, -9F, 20.75F);

		waterCoolingModel[11].addShapeBox(0F, 0F, 0F, 1, 3, 3, 0F, -0.25F, -0.5F, -0.5F, -0.25F, -0.5F, -0.5F, -0.25F, -0.5F, -0.5F, -0.25F, -0.5F, -0.5F, -0.25F, -0.5F, -0.5F, -0.25F, -0.5F, -0.5F, -0.25F, -0.5F, -0.5F, -0.25F, -0.5F, -0.5F); // BarrelBarrel
		waterCoolingModel[11].setRotationPoint(13F, -9.5F, 18F);

		waterCoolingModel[12].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F); // BarrelBarrel
		waterCoolingModel[12].setRotationPoint(12.5F, -8.5F, 19F);

		waterCoolingModel[13].addShapeBox(0F, 0F, 0F, 2, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BarrelBarrel
		waterCoolingModel[13].setRotationPoint(11F, -6.5F, 12F);

		parts.put("waterCooling", waterCoolingModel);

		//Second Magazine

		secondMagazineMainModel = new ModelRendererTurbo[2];

		secondMagazineMainModel[0] = new ModelRendererTurbo(this, 12, 85, textureX, textureY); // AmmoBoxAddon
		secondMagazineMainModel[1] = new ModelRendererTurbo(this, 26, 41, textureX, textureY); // AmmoBoxTopAddon

		secondMagazineMainModel[0].addBox(0F, 0F, 0F, 2, 3, 6, 0F); // AmmoBoxAddon
		secondMagazineMainModel[0].setRotationPoint(12F, -10F, 1F);

		secondMagazineMainModel[1].addShapeBox(0F, 0F, 0F, 2, 1, 5, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F); // AmmoBoxTopAddon
		secondMagazineMainModel[1].setRotationPoint(12F, -10.5F, 1.5F);

		secondMagazineMagModel = new ModelRendererTurbo[1];
		secondMagazineMagModel[0] = new ModelRendererTurbo(this, 0, 85, textureX, textureY); // AmmoCartridgeAddon
		secondMagazineMagModel[0].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(4, 0, 4, 0), new Coord2D(4, 2, 4, 2), new Coord2D(5, 5, 5, 5), new Coord2D(6, 8, 6, 8), new Coord2D(3, 9, 3, 9), new Coord2D(1, 6, 1, 6), new Coord2D(0, 3, 0, 3)}), 1, 6, 9, 29, 1, ModelRendererTurbo.MR_FRONT, new float[]{3, 4, 4, 4, 4, 4, 2, 4}); // AmmoCartridgeAddon
		secondMagazineMagModel[0].setRotationPoint(13.5F, -10.25F, 2F);
		secondMagazineMagModel[0].rotateAngleY = -1.57079633F;

		parts.put("secondMagazine", secondMagazineMainModel);
		parts.put("secondMagazineMag", secondMagazineMagModel);

		//Scope

		scopeModel = new ModelRendererTurbo[8];
		scopeModel[0] = new ModelRendererTurbo(this, 26, 32, textureX, textureY); // BoxMainScopePlate
		scopeModel[1] = new ModelRendererTurbo(this, 8, 62, textureX, textureY); // BoxMainScopeUpper
		scopeModel[2] = new ModelRendererTurbo(this, 0, 64, textureX, textureY); // BoxMainScopeUpper
		scopeModel[3] = new ModelRendererTurbo(this, 8, 60, textureX, textureY); // BoxMainScopeUpper
		scopeModel[4] = new ModelRendererTurbo(this, 0, 60, textureX, textureY); // BoxMainScopeUpper
		scopeModel[5] = new ModelRendererTurbo(this, 12, 60, textureX, textureY); // BoxMainScopeUpper
		scopeModel[6] = new ModelRendererTurbo(this, 0, 72, textureX, textureY); // BoxMainScopeUpper
		scopeModel[7] = new ModelRendererTurbo(this, 0, 60, textureX, textureY); // BoxMainScopeUpper

		scopeModel[0].addShapeBox(0F, 0F, 0F, 2, 1, 3, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F); // BoxMainScopePlate
		scopeModel[0].setRotationPoint(7F, -12F, 6F);

		scopeModel[1].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F); // BoxMainScopeUpper
		scopeModel[1].setRotationPoint(7.5F, -12.25F, 7F);

		scopeModel[2].addShapeBox(0F, 0F, 0F, 2, 2, 6, 0F, -0.25F, -0.25F, -1.25F, -0.25F, -0.25F, -1.25F, -0.5F, -0.5F, -1.5F, -0.5F, -0.5F, -1.5F, -0.25F, -0.25F, -1.25F, -0.25F, -0.25F, -1.25F, -0.5F, -0.5F, -1.5F, -0.5F, -0.5F, -1.5F); // BoxMainScopeUpper
		scopeModel[2].setRotationPoint(7F, -13.5F, 4.5F);

		scopeModel[3].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BoxMainScopeUpper
		scopeModel[3].setRotationPoint(7.5F, -13F, 4.25F);

		scopeModel[4].addShapeBox(0F, 0F, 0F, 3, 3, 1, 0F, -0.75F, -0.75F, -0.25F, -0.75F, -0.75F, -0.25F, -0.75F, -0.75F, -0.25F, -0.75F, -0.75F, -0.25F, -0.75F, -0.75F, -0.25F, -0.75F, -0.75F, -0.25F, -0.75F, -0.75F, -0.25F, -0.75F, -0.75F, -0.25F); // BoxMainScopeUpper
		scopeModel[4].setRotationPoint(6.5F, -14F, 10F);

		scopeModel[5].addShapeBox(0F, 0F, 0F, 2, 2, 1, 0F, -0.375F, -0.375F, -0.25F, -0.375F, -0.375F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.375F, -0.25F, -0.375F, -0.375F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F); // BoxMainScopeUpper
		scopeModel[5].setRotationPoint(7F, -13.5F, 5F);

		scopeModel[6].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, -0.5F, -0.5F, -0.25F, -0.5F, -0.5F, -0.25F, -0.25F, -0.25F, -0.5F, -0.25F, -0.25F, -0.5F, -0.5F, -0.5F, -0.25F, -0.5F, -0.5F, -0.25F, -0.25F, -0.25F, -0.5F, -0.25F, -0.25F, -0.5F); // BoxMainScopeUpper
		scopeModel[6].setRotationPoint(7F, -13.5F, 8.75F);

		scopeModel[7].addShapeBox(0F, 0F, 0F, 3, 3, 1, 0F, -0.875F, -0.875F, -0.25F, -0.875F, -0.875F, -0.25F, -0.875F, -0.875F, -0.25F, -0.875F, -0.875F, -0.25F, -0.875F, -0.875F, -0.25F, -0.875F, -0.875F, -0.25F, -0.875F, -0.875F, -0.25F, -0.875F, -0.875F, -0.25F); // BoxMainScopeUpper
		scopeModel[7].setRotationPoint(6.5F, -14F, 3.5F);

		parts.put("scope", scopeModel);

		infraredScopeModel = new ModelRendererTurbo[23];
		infraredScopeModel[0] = new ModelRendererTurbo(this, 26, 32, textureX, textureY); // BoxMainScopePlate
		infraredScopeModel[1] = new ModelRendererTurbo(this, 8, 62, textureX, textureY); // BoxMainScopeUpper
		infraredScopeModel[2] = new ModelRendererTurbo(this, 32, 85, textureX, textureY); // BoxMainScopeUpper
		infraredScopeModel[3] = new ModelRendererTurbo(this, 22, 89, textureX, textureY); // BoxMainScopeUpper
		infraredScopeModel[4] = new ModelRendererTurbo(this, 30, 85, textureX, textureY); // BoxMainScopeUpper
		infraredScopeModel[5] = new ModelRendererTurbo(this, 29, 93, textureX, textureY); // BoxMainScopeUpper
		infraredScopeModel[6] = new ModelRendererTurbo(this, 35, 93, textureX, textureY); // BoxMainScopeUpper
		infraredScopeModel[7] = new ModelRendererTurbo(this, 22, 85, textureX, textureY); // BoxMainScopeUpper
		infraredScopeModel[8] = new ModelRendererTurbo(this, 24, 97, textureX, textureY); // BoxMainScopeUpper
		infraredScopeModel[9] = new ModelRendererTurbo(this, 26, 104, textureX, textureY); // BoxMainScopeUpper
		infraredScopeModel[10] = new ModelRendererTurbo(this, 22, 0, textureX, textureY); // BoxMainScopeUpper
		infraredScopeModel[11] = new ModelRendererTurbo(this, 38, 97, textureX, textureY); // BoxMainScopeUpper
		infraredScopeModel[12] = new ModelRendererTurbo(this, 38, 97, textureX, textureY); // BoxMainScopeUpper
		infraredScopeModel[13] = new ModelRendererTurbo(this, 44, 107, textureX, textureY); // BarrelBarrel
		infraredScopeModel[14] = new ModelRendererTurbo(this, 24, 113, textureX, textureY); // BarrelBarrel
		infraredScopeModel[15] = new ModelRendererTurbo(this, 32, 113, textureX, textureY); // BarrelBarrel
		infraredScopeModel[16] = new ModelRendererTurbo(this, 40, 113, textureX, textureY); // BarrelBarrel
		infraredScopeModel[17] = new ModelRendererTurbo(this, 39, 98, textureX, textureY); // AmmoBoxAddon
		infraredScopeModel[18] = new ModelRendererTurbo(this, 41, 104, textureX, textureY); // AmmoBoxTopAddon
		infraredScopeModel[19] = new ModelRendererTurbo(this, 42, 117, textureX, textureY); // BarrelBarrel
		infraredScopeModel[20] = new ModelRendererTurbo(this, 41, 104, textureX, textureY); // AmmoBoxTopAddon
		infraredScopeModel[21] = new ModelRendererTurbo(this, 34, 118, textureX, textureY); // BarrelBarrel
		infraredScopeModel[22] = new ModelRendererTurbo(this, 28, 117, textureX, textureY); // BarrelBarrel

		infraredScopeModel[0].addShapeBox(0F, 0F, 0F, 2, 1, 3, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F); // BoxMainScopePlate
		infraredScopeModel[0].setRotationPoint(7F, -12F, 6F);

		infraredScopeModel[1].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F); // BoxMainScopeUpper
		infraredScopeModel[1].setRotationPoint(7.5F, -12.25F, 7F);

		infraredScopeModel[2].addShapeBox(0F, 0F, 0F, 2, 2, 6, 0F, -0.25F, -0.25F, -1.25F, -0.25F, -0.25F, -1.25F, -0.5F, -0.5F, -1.5F, -0.5F, -0.5F, -1.5F, -0.25F, -0.25F, -1.25F, -0.25F, -0.25F, -1.25F, -0.5F, -0.5F, -1.5F, -0.5F, -0.5F, -1.5F); // BoxMainScopeUpper
		infraredScopeModel[2].setRotationPoint(7F, -13.5F, 4.5F);

		infraredScopeModel[3].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BoxMainScopeUpper
		infraredScopeModel[3].setRotationPoint(7.5F, -13F, 4.25F);

		infraredScopeModel[4].addShapeBox(0F, 0F, 0F, 3, 3, 1, 0F, -0.75F, -0.75F, -0.25F, -0.75F, -0.75F, -0.25F, -0.75F, -0.75F, -0.25F, -0.75F, -0.75F, -0.25F, -0.75F, -0.75F, -0.25F, -0.75F, -0.75F, -0.25F, -0.75F, -0.75F, -0.25F, -0.75F, -0.75F, -0.25F); // BoxMainScopeUpper
		infraredScopeModel[4].setRotationPoint(6.5F, -14F, 10F);

		infraredScopeModel[5].addShapeBox(0F, 0F, 0F, 2, 2, 1, 0F, -0.375F, -0.375F, -0.25F, -0.375F, -0.375F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.375F, -0.25F, -0.375F, -0.375F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F); // BoxMainScopeUpper
		infraredScopeModel[5].setRotationPoint(7F, -13.5F, 5F);

		infraredScopeModel[6].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, -0.5F, -0.5F, -0.25F, -0.5F, -0.5F, -0.25F, -0.25F, -0.25F, -0.5F, -0.25F, -0.25F, -0.5F, -0.5F, -0.5F, -0.25F, -0.5F, -0.5F, -0.25F, -0.25F, -0.25F, -0.5F, -0.25F, -0.25F, -0.5F); // BoxMainScopeUpper
		infraredScopeModel[6].setRotationPoint(7F, -13.5F, 8.75F);

		infraredScopeModel[7].addShapeBox(0F, 0F, 0F, 3, 3, 1, 0F, -0.875F, -0.875F, -0.25F, -0.875F, -0.875F, -0.25F, -0.875F, -0.875F, -0.25F, -0.875F, -0.875F, -0.25F, -0.875F, -0.875F, -0.25F, -0.875F, -0.875F, -0.25F, -0.875F, -0.875F, -0.25F, -0.875F, -0.875F, -0.25F); // BoxMainScopeUpper
		infraredScopeModel[7].setRotationPoint(6.5F, -14F, 3.5F);

		infraredScopeModel[8].addShapeBox(0F, 0F, 0F, 6, 6, 1, 0F, -0.75F, -0.75F, -0.25F, -0.75F, -0.75F, -0.25F, -0.75F, -0.75F, -0.25F, -0.75F, -0.75F, -0.25F, -0.75F, -0.75F, -0.25F, -0.75F, -0.75F, -0.25F, -0.75F, -0.75F, -0.25F, -0.75F, -0.75F, -0.25F); // BoxMainScopeUpper
		infraredScopeModel[8].setRotationPoint(5F, -19F, 8.5F);

		infraredScopeModel[9].addShapeBox(0F, 0F, 0F, 6, 6, 3, 0F, -1F, -1F, -0.25F, -1F, -1F, -0.25F, -0.75F, -0.75F, -0.5F, -0.75F, -0.75F, -0.5F, -1F, -1F, -0.25F, -1F, -1F, -0.25F, -0.75F, -0.75F, -0.5F, -0.75F, -0.75F, -0.5F); // BoxMainScopeUpper
		infraredScopeModel[9].setRotationPoint(5F, -19F, 6.25F);

		infraredScopeModel[10].addShapeBox(0F, 0F, 0F, 5, 5, 3, 0F, -1.5F, -1.5F, -0.25F, -1.5F, -1.5F, -0.25F, -0.75F, -0.75F, -0.5F, -0.75F, -0.75F, -0.5F, -1.5F, -1.5F, -0.25F, -1.5F, -1.5F, -0.25F, -0.75F, -0.75F, -0.5F, -0.75F, -0.75F, -0.5F); // BoxMainScopeUpper
		infraredScopeModel[10].setRotationPoint(5.5F, -18.5F, 4F);

		infraredScopeModel[11].addShapeBox(0F, 0F, 0F, 1, 3, 1, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F); // BoxMainScopeUpper
		infraredScopeModel[11].setRotationPoint(6.75F, -14F, 7F);

		infraredScopeModel[12].addShapeBox(0F, 0F, 0F, 1, 3, 1, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F); // BoxMainScopeUpper
		infraredScopeModel[12].setRotationPoint(8.25F, -14F, 7F);

		infraredScopeModel[13].addShapeBox(0F, 0F, 0F, 1, 2, 2, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F); // BarrelBarrel
		infraredScopeModel[13].setRotationPoint(8.75F, -17F, 4.75F);
		infraredScopeModel[13].rotateAngleY = -0.17453293F;

		infraredScopeModel[14].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, -0.375F, -0.25F, -0.375F, -0.375F, -0.5F, -0.375F, -0.375F, -0.5F, -0.375F, -0.375F, -0.25F, -0.375F, -0.375F, -0.25F, -0.375F, -0.375F, -0.25F, -0.375F, -0.375F, -0.25F, -0.375F, -0.375F, -0.25F, -0.375F); // BarrelBarrel
		infraredScopeModel[14].setRotationPoint(9.25F, -17F, 4.65F);
		infraredScopeModel[14].rotateAngleY = -0.15707963F;

		infraredScopeModel[15].addShapeBox(0F, 0F, 0F, 2, 3, 2, 0F, -0.375F, -0.25F, -0.375F, -0.375F, -0.25F, -0.375F, -0.375F, -0.25F, -0.375F, -0.375F, -0.25F, -0.375F, -0.125F, -0.25F, -0.375F, -0.625F, -0.25F, -0.375F, -0.625F, -0.25F, -0.375F, -0.125F, -0.25F, -0.375F); // BarrelBarrel
		infraredScopeModel[15].setRotationPoint(9.25F, -15.5F, 4.65F);
		infraredScopeModel[15].rotateAngleY = -0.15707963F;

		infraredScopeModel[16].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, -0.375F, -0.25F, -0.375F, -0.375F, -0.25F, -0.375F, -0.375F, -0.25F, -0.275F, -0.375F, -0.25F, -0.375F, -0.375F, -0.25F, -0.625F, -0.375F, -0.25F, -0.625F, -0.375F, -0.25F, -0.275F, -0.375F, -0.25F, -0.375F); // BarrelBarrel
		infraredScopeModel[16].setRotationPoint(9F, -13F, 4.65F);
		infraredScopeModel[16].rotateAngleY = -0.17453293F;

		infraredScopeModel[17].addBox(0F, 0F, 0F, 2, 3, 3, 0F); // AmmoBoxAddon
		infraredScopeModel[17].setRotationPoint(10F, -10F, 7.5F);

		infraredScopeModel[18].addShapeBox(0F, 0F, 0F, 2, 1, 2, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F); // AmmoBoxTopAddon
		infraredScopeModel[18].setRotationPoint(10F, -7.5F, 8F);

		infraredScopeModel[19].addShapeBox(0F, 0F, 0F, 2, 2, 3, 0F, -0.375F, -0.25F, -0.375F, -0.375F, -0.25F, -0.375F, -0.375F, -0.25F, -0.375F, -0.375F, -0.25F, -0.375F, -0.375F, -0.25F, -0.375F, -0.375F, -0.25F, -0.375F, -0.375F, -0.25F, -0.375F, -0.375F, -0.25F, -0.375F); // BarrelBarrel
		infraredScopeModel[19].setRotationPoint(9.25F, -13F, 5.65F);

		infraredScopeModel[20].addShapeBox(0F, 0F, 0F, 2, 1, 2, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F); // AmmoBoxTopAddon
		infraredScopeModel[20].setRotationPoint(10F, -10.75F, 8F);

		infraredScopeModel[21].addShapeBox(0F, 0F, 0F, 2, 3, 2, 0F, -0.375F, -0.25F, -0.375F, -0.375F, -0.25F, -0.375F, -0.375F, -0.5F, -0.375F, -0.375F, -0.5F, -0.375F, -0.375F, -0.25F, -0.375F, -0.375F, -0.25F, -0.375F, -0.375F, -0.25F, -0.375F, -0.375F, -0.25F, -0.375F); // BarrelBarrel
		infraredScopeModel[21].setRotationPoint(9.25F, -13F, 7.9F);

		infraredScopeModel[22].addShapeBox(0F, 0F, 0F, 1, 2, 2, 0F, -0.125F, -0.375F, -0.375F, -0.125F, -0.625F, -0.375F, -0.125F, -0.625F, -0.375F, -0.125F, -0.375F, -0.375F, -0.125F, -0.375F, -0.375F, -0.125F, -0.375F, -0.375F, -0.125F, -0.375F, -0.375F, -0.125F, -0.375F, -0.375F); // BarrelBarrel
		infraredScopeModel[22].setRotationPoint(10.75F, -12F, 7.9F);

		parts.put("infraredScope", infraredScopeModel);

		hastyBipodModel = new ModelRendererTurbo[2];
		hastyBipodModel[0] = new ModelRendererTurbo(this, 4, 0, textureX, textureY); // BoxBipodLeg
		hastyBipodModel[1] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // BoxBipodLeg
		hastyBipodModel[0].addBox(-1F, 0F, 0F, 1, 8, 1, 0F); // BoxBipodLeg
		hastyBipodModel[1].addBox(0F, 0F, 0F, 1, 8, 1, 0F); // BoxBipodLeg
		parts.put("hastyBipod", hastyBipodModel);

		preciseBipodModel = new ModelRendererTurbo[2];
		preciseBipodModel[0] = new ModelRendererTurbo(this, 14, 0, textureX, textureY); // BoxBipodLeg
		preciseBipodModel[1] = new ModelRendererTurbo(this, 18, 0, textureX, textureY); // BoxBipodLeg

		preciseBipodModel[0].addBox(-1F, 0F, 0F, 1, 8, 1, 0F); // BoxBipodLeg
		preciseBipodModel[1].addBox(0F, 0F, 0F, 1, 8, 1, 0F); // BoxBipodLeg
		parts.put("preciseBipod", preciseBipodModel);

		beltFedLoaderModel = new ModelRendererTurbo[3];
		beltFedLoaderModel[0] = new ModelRendererTurbo(this, 38, 64, textureX, textureY); // AmmoBoxAddon
		beltFedLoaderModel[1] = new ModelRendererTurbo(this, 38, 73, textureX, textureY); // AmmoBoxTopAddon
		beltFedLoaderModel[2] = new ModelRendererTurbo(this, 38, 73, textureX, textureY); // AmmoBoxBottomAddon

		beltFedLoaderModel[0].addBox(0F, 0F, 0F, 2, 3, 6, 0F); // AmmoBoxAddon
		beltFedLoaderModel[0].setRotationPoint(12F, -10F, 1F);

		beltFedLoaderModel[1].addShapeBox(0F, 0F, 0F, 2, 1, 5, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F); // AmmoBoxTopAddon
		beltFedLoaderModel[1].setRotationPoint(12F, -10.5F, 1.5F);

		beltFedLoaderModel[2].addShapeBox(0F, 0F, 0F, 2, 1, 5, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F); // AmmoBoxBottomAddon
		beltFedLoaderModel[2].setRotationPoint(12F, -7.5F, 1.5F);

		parts.put("beltFedLoader", beltFedLoaderModel);

		shieldModel = new ModelRendererTurbo[10];
		shieldModel[0] = new ModelRendererTurbo(this, 22, 13, textureX, textureY); // Box 42
		shieldModel[1] = new ModelRendererTurbo(this, 12, 13, textureX, textureY); // Box 42
		shieldModel[2] = new ModelRendererTurbo(this, 16, 13, textureX, textureY); // Box 42
		shieldModel[3] = new ModelRendererTurbo(this, 24, 70, textureX, textureY); // Shape 45
		shieldModel[4] = new ModelRendererTurbo(this, 22, 13, textureX, textureY); // Box 42
		shieldModel[5] = new ModelRendererTurbo(this, 12, 13, textureX, textureY); // Box 42
		shieldModel[6] = new ModelRendererTurbo(this, 16, 13, textureX, textureY); // Box 42
		shieldModel[7] = new ModelRendererTurbo(this, 24, 70, textureX, textureY); // Shape 45
		shieldModel[8] = new ModelRendererTurbo(this, 0, 76, textureX, textureY); // CustomPaint
		shieldModel[9] = new ModelRendererTurbo(this, 0, 76, textureX, textureY); // CustomPaint

		shieldModel[0].addShapeBox(0F, 0F, 0F, 1, 4, 1, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 42
		shieldModel[0].setRotationPoint(5F, -10F, 13F);

		shieldModel[1].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // Box 42
		shieldModel[1].setRotationPoint(5F, -10F, 14F);

		shieldModel[2].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // Box 42
		shieldModel[2].setRotationPoint(5F, -7F, 14F);

		shieldModel[3].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(6, 0, 6, 0), new Coord2D(6, 10, 6, 10), new Coord2D(4, 12, 4, 12), new Coord2D(0, 12, 0, 12)}), 1, 6, 12, 35, 1, ModelRendererTurbo.MR_FRONT, new float[]{12, 4, 3, 10, 6}); // Shape 45
		shieldModel[3].setRotationPoint(7.05F, -4F, 16F);
		shieldModel[3].rotateAngleX = 0.06981317F;

		shieldModel[4].addShapeBox(0F, 0F, 0F, 1, 4, 1, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 42
		shieldModel[4].setRotationPoint(10F, -10F, 13F);

		shieldModel[5].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // Box 42
		shieldModel[5].setRotationPoint(10F, -10F, 14F);

		shieldModel[6].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // Box 42
		shieldModel[6].setRotationPoint(10F, -7F, 14F);

		shieldModel[7].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(6, 0, 6, 0), new Coord2D(6, 10, 6, 10), new Coord2D(4, 12, 4, 12), new Coord2D(0, 12, 0, 12)}), 1, 6, 12, 35, 1, ModelRendererTurbo.MR_FRONT, new float[]{12, 4, 3, 10, 6}); // Shape 45
		shieldModel[7].setRotationPoint(9.05F, -4F, 15F);
		shieldModel[7].rotateAngleX = -0.06981317F;
		shieldModel[7].rotateAngleY = -3.14159265F;

		shieldModel[8].addShapeBox(0F, 0F, 0F, 6, 6, 0, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // CustomPaint
		shieldModel[8].setRotationPoint(1F, -13F, 15.4F);
		shieldModel[8].rotateAngleX = 0.08726646F;

		shieldModel[9].addShapeBox(0F, 0F, 0F, 6, 6, 0, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // CustomPaint
		shieldModel[9].setRotationPoint(15F, -13F, 15.4F);
		shieldModel[9].rotateAngleX = -0.08726646F;
		shieldModel[9].rotateAngleY = -3.14159265F;

		parts.put("shield", shieldModel);

		baubleModel = new ModelRendererTurbo[5];
		baubleModel[0] = new ModelRendererTurbo(this, 28, 122, textureX, textureY); // BoxClock
		baubleModel[1] = new ModelRendererTurbo(this, 33, 122, textureX, textureY); // BoxClock
		baubleModel[2] = new ModelRendererTurbo(this, 44, 121, textureX, textureY); // BoxClock
		baubleModel[3] = new ModelRendererTurbo(this, 42, 121, textureX, textureY); // BoxClock
		baubleModel[4] = new ModelRendererTurbo(this, 36, 122, textureX, textureY); // BoxClock

		baubleModel[0].addBox(0F, 1F, -2F, 1, 3, 3, 0F); // BoxClock
		baubleModel[0].setRotationPoint(6F, -10F, 0F);
		baubleModel[0].rotateAngleX = 0.27925268F;
		baubleModel[0].rotateAngleY = 3.4906585F;
		baubleModel[0].rotateAngleZ = 0.01745329F;

		baubleModel[1].addBox(0.5F, -1.5F, -0.5F, 0, 2, 1, 0F); // BoxClock
		baubleModel[1].setRotationPoint(6F, -9F, 0F);
		baubleModel[1].rotateAngleX = 0.06981317F;
		baubleModel[1].rotateAngleY = 2.65290046F;
		baubleModel[1].rotateAngleZ = 0.17453293F;

		baubleModel[2].addBox(0.5F, -4.5F, -0.5F, 0, 5, 1, 0F); // BoxClock
		baubleModel[2].setRotationPoint(6F, -10F, 0F);
		baubleModel[2].rotateAngleX = 0.06981317F;
		baubleModel[2].rotateAngleY = 2.23402144F;
		baubleModel[2].rotateAngleZ = 1.67551608F;

		baubleModel[3].addBox(-3.75F, -1.5F, 2F, 0, 5, 1, 0F); // BoxClock
		baubleModel[3].setRotationPoint(6F, -8.5F, 0F);
		baubleModel[3].rotateAngleX = 0.13962634F;
		baubleModel[3].rotateAngleY = 2.87979327F;
		baubleModel[3].rotateAngleZ = 0.06981317F;

		baubleModel[4].addBox(0.5F, -4.5F, 0F, 0, 5, 1, 0F); // BoxClock
		baubleModel[4].setRotationPoint(6F, -5.25F, 0F);
		baubleModel[4].rotateAngleX = 0.06981317F;
		baubleModel[4].rotateAngleY = 2.23402144F;
		baubleModel[4].rotateAngleZ = 1.67551608F;

		parts.put("bauble", baubleModel);

		tripodBaseModel = new ModelRendererTurbo[4];
		tripodBaseModel[0] = new ModelRendererTurbo(this, 47, 103, textureX, textureY); // Tripod02
		tripodBaseModel[1] = new ModelRendererTurbo(this, 49, 100, textureX, textureY); // Tripod03
		tripodBaseModel[2] = new ModelRendererTurbo(this, 49, 100, textureX, textureY); // Tripod04
		tripodBaseModel[3] = new ModelRendererTurbo(this, 60, 107, textureX, textureY); // Tripod05

		tripodBaseModel[0].addShapeBox(0F, 0F, 0F, 3, 1, 2, 0F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F); // Tripod02
		tripodBaseModel[0].setRotationPoint(-1F, -21.01F, -2F);

		tripodBaseModel[1].addShapeBox(0F, 0F, 0F, 3, 1, 2, 0F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F); // Tripod03
		tripodBaseModel[1].setRotationPoint(0F, -21.02F, 1F);
		tripodBaseModel[1].rotateAngleY = -0.78539816F;

		tripodBaseModel[2].addShapeBox(0F, 0F, 0F, 3, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tripod04
		tripodBaseModel[2].setRotationPoint(-1F, -21F, -1F);
		tripodBaseModel[2].rotateAngleY = 0.78539816F;

		tripodBaseModel[3].addShapeBox(0F, 0F, 0F, 1, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tripod05
		tripodBaseModel[3].setRotationPoint(0F, -22F, 0F);

		parts.put("tripodBase", tripodBaseModel);

		tripodLeg1Model = new ModelRendererTurbo[6];
		tripodLeg1Model[0] = new ModelRendererTurbo(this, 54, 106, textureX, textureY); // Tripod14
		tripodLeg1Model[1] = new ModelRendererTurbo(this, 50, 106, textureX, textureY); // Tripod15
		tripodLeg1Model[2] = new ModelRendererTurbo(this, 50, 112, textureX, textureY); // Tripod16
		tripodLeg1Model[3] = new ModelRendererTurbo(this, 60, 111, textureX, textureY); // Tripod15
		tripodLeg1Model[4] = new ModelRendererTurbo(this, 56, 114, textureX, textureY); // Tripod15
		tripodLeg1Model[5] = new ModelRendererTurbo(this, 52, 114, textureX, textureY); // Tripod15

		tripodLeg1Model[0].addShapeBox(-2.7F, -0.5F, 0F, 1, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tripod14
		tripodLeg1Model[0].setRotationPoint(0F, -20F, 2F);
		tripodLeg1Model[0].rotateAngleX = 0.29670597F;
		tripodLeg1Model[0].rotateAngleY = 0.78539816F;

		tripodLeg1Model[1].addShapeBox(-0.7F, -0.5F, 0F, 1, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tripod15
		tripodLeg1Model[1].setRotationPoint(0F, -20F, 2F);
		tripodLeg1Model[1].rotateAngleX = 0.29670597F;
		tripodLeg1Model[1].rotateAngleY = 0.78539816F;

		tripodLeg1Model[2].addShapeBox(-3.2F, 4.5F, 0F, 4, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tripod16
		tripodLeg1Model[2].setRotationPoint(0F, -20F, 2F);
		tripodLeg1Model[2].rotateAngleX = 0.29670597F;
		tripodLeg1Model[2].rotateAngleY = 0.78539816F;

		tripodLeg1Model[3].addShapeBox(-1.7F, 7.5F, 0F, 1, 15, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tripod15
		tripodLeg1Model[3].setRotationPoint(0F, -20F, 2F);
		tripodLeg1Model[3].rotateAngleX = 0.29670597F;
		tripodLeg1Model[3].rotateAngleY = 0.78539816F;

		tripodLeg1Model[4].addShapeBox(-0.7F, 5.5F, 0F, 1, 12, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tripod15
		tripodLeg1Model[4].setRotationPoint(0F, -20F, 2F);
		tripodLeg1Model[4].rotateAngleX = 0.29670597F;
		tripodLeg1Model[4].rotateAngleY = 0.78539816F;

		tripodLeg1Model[5].addShapeBox(-2.7F, 5.5F, 0F, 1, 12, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tripod15
		tripodLeg1Model[5].setRotationPoint(0F, -20F, 2F);
		tripodLeg1Model[5].rotateAngleX = 0.29670597F;
		tripodLeg1Model[5].rotateAngleY = 0.78539816F;

		parts.put("tripodLeg1", tripodLeg1Model);

		tripodLeg2Model = new ModelRendererTurbo[6];
		tripodLeg2Model[0] = new ModelRendererTurbo(this, 60, 111, textureX, textureY); // Tripod01
		tripodLeg2Model[1] = new ModelRendererTurbo(this, 50, 106, textureX, textureY); // Tripod07
		tripodLeg2Model[2] = new ModelRendererTurbo(this, 50, 112, textureX, textureY); // Tripod08
		tripodLeg2Model[3] = new ModelRendererTurbo(this, 54, 106, textureX, textureY); // Tripod09
		tripodLeg2Model[4] = new ModelRendererTurbo(this, 52, 114, textureX, textureY); // Tripod01
		tripodLeg2Model[5] = new ModelRendererTurbo(this, 56, 114, textureX, textureY); // Tripod01

		tripodLeg2Model[0].addShapeBox(0F, 8F, 0F, 1, 15, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tripod01
		tripodLeg2Model[0].setRotationPoint(0F, -21F, -2F);
		tripodLeg2Model[0].rotateAngleX = -0.29670597F;

		tripodLeg2Model[1].addShapeBox(0F, 0F, 0F, 1, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tripod07
		tripodLeg2Model[1].setRotationPoint(-1F, -21F, -2F);
		tripodLeg2Model[1].rotateAngleX = -0.29670597F;

		tripodLeg2Model[2].addShapeBox(0F, 5F, 0F, 4, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tripod08
		tripodLeg2Model[2].setRotationPoint(-1.5F, -21F, -2F);
		tripodLeg2Model[2].rotateAngleX = -0.29670597F;

		tripodLeg2Model[3].addShapeBox(0F, 0F, 0F, 1, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tripod09
		tripodLeg2Model[3].setRotationPoint(1F, -21F, -2F);
		tripodLeg2Model[3].rotateAngleX = -0.29670597F;

		tripodLeg2Model[4].addShapeBox(1F, 6F, 0F, 1, 12, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tripod01
		tripodLeg2Model[4].setRotationPoint(0F, -21F, -2F);
		tripodLeg2Model[4].rotateAngleX = -0.29670597F;

		tripodLeg2Model[5].addShapeBox(-1F, 6F, 0F, 1, 12, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tripod01
		tripodLeg2Model[5].setRotationPoint(0F, -21F, -2F);
		tripodLeg2Model[5].rotateAngleX = -0.29670597F;

		parts.put("tripodLeg2", tripodLeg2Model);

		tripodLeg3Model = new ModelRendererTurbo[6];
		tripodLeg3Model[0] = new ModelRendererTurbo(this, 54, 106, textureX, textureY); // Tripod06
		tripodLeg3Model[1] = new ModelRendererTurbo(this, 50, 106, textureX, textureY); // Tripod10
		tripodLeg3Model[2] = new ModelRendererTurbo(this, 50, 112, textureX, textureY); // Tripod12
		tripodLeg3Model[3] = new ModelRendererTurbo(this, 60, 111, textureX, textureY); // Tripod13
		tripodLeg3Model[4] = new ModelRendererTurbo(this, 52, 114, textureX, textureY); // Tripod13
		tripodLeg3Model[5] = new ModelRendererTurbo(this, 56, 114, textureX, textureY); // Tripod13

		tripodLeg3Model[0].addShapeBox(0F, -0.8F, 0F, 1, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tripod06
		tripodLeg3Model[0].setRotationPoint(1F, -20F, 2F);
		tripodLeg3Model[0].rotateAngleX = 0.29670597F;
		tripodLeg3Model[0].rotateAngleY = -0.78539816F;

		tripodLeg3Model[1].addShapeBox(2F, -0.8F, 0F, 1, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tripod10
		tripodLeg3Model[1].setRotationPoint(1F, -20F, 2F);
		tripodLeg3Model[1].rotateAngleX = 0.29670597F;
		tripodLeg3Model[1].rotateAngleY = -0.78539816F;

		tripodLeg3Model[2].addShapeBox(-0.5F, 4F, 0.01F, 4, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tripod12
		tripodLeg3Model[2].setRotationPoint(1F, -20F, 2F);
		tripodLeg3Model[2].rotateAngleX = 0.29670597F;
		tripodLeg3Model[2].rotateAngleY = -0.78539816F;

		tripodLeg3Model[3].addShapeBox(1F, 7F, 0F, 1, 15, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tripod13
		tripodLeg3Model[3].setRotationPoint(1F, -20F, 2F);
		tripodLeg3Model[3].rotateAngleX = 0.29670597F;
		tripodLeg3Model[3].rotateAngleY = -0.78539816F;

		tripodLeg3Model[4].addShapeBox(0F, 5F, 0F, 1, 12, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tripod13
		tripodLeg3Model[4].setRotationPoint(1F, -20F, 2F);
		tripodLeg3Model[4].rotateAngleX = 0.29670597F;
		tripodLeg3Model[4].rotateAngleY = -0.78539816F;

		tripodLeg3Model[5].addShapeBox(2F, 5F, 0F, 1, 12, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tripod13
		tripodLeg3Model[5].setRotationPoint(1F, -20F, 2F);
		tripodLeg3Model[5].rotateAngleX = 0.29670597F;
		tripodLeg3Model[5].rotateAngleY = -0.78539816F;

		parts.put("tripodLeg3", tripodLeg3Model);

		flipAll();

		parts.remove("heavyBarrel");
		parts.remove("waterCooling");
		parts.remove("secondMagazine");
		parts.remove("secondMagazineMag");
		parts.remove("beltFedLoader");
		parts.remove("scope");
		parts.remove("infraredScope");
		parts.remove("hastyBipod");
		parts.remove("preciseBipod");
		parts.remove("shield");
		parts.remove("bauble");

		parts.remove("tripodBase");
		parts.remove("tripodLeg1");
		parts.remove("tripodLeg2");
		parts.remove("tripodLeg3");

		baseBox = new TmtNamedBoxGroup("base", baseModel, MachinegunRenderer.texture);
		barrelBox = new TmtNamedBoxGroup("barrel", barrelModel, MachinegunRenderer.texture);
		sightsBox = new TmtNamedBoxGroup("sights", sightsModel, MachinegunRenderer.texture);
		triggerBox = new TmtNamedBoxGroup("trigger", triggerModel, MachinegunRenderer.texture);
		ammoBox = new TmtNamedBoxGroup("ammo", ammoModel, MachinegunRenderer.texture);
		slideBox = new TmtNamedBoxGroup("slide", slideModel, MachinegunRenderer.texture);
		gripBox = new TmtNamedBoxGroup("grip", gripModel, MachinegunRenderer.texture);
		bipodBox = new TmtNamedBoxGroup("bipod", bipodModel, MachinegunRenderer.texture)
		{
			@Override
			public void render(float scale, float animation)
			{
				float legs = animation > 0.5f?(animation-0.5f)/0.5f: 0;
				GlStateManager.pushMatrix();
				GlStateManager.translate(8F*scale, 4.75F*scale, -26F*scale);
				GlStateManager.rotate(-90*animation, 1, 0, 0);
				GlStateManager.translate(0, scale*animation, 0);
				GlStateManager.rotate(-35f*(1f-legs), 0f, 0f, 1f);
				getModel()[0].render(scale);
				GlStateManager.rotate(2*35f*(1f-legs), 0f, 0f, 1f);
				getModel()[1].render(scale);
				GlStateManager.popMatrix();
			}
		};

		//The upgrades should use the same names as the parts they replace (if they replace parts)
		//I know the rendering is done in a weird way, but hey, it works.
		//If anyone would want to make custom upgrades for the mg, you would need to register your own model
		heavyBarrelBox = new TmtNamedBoxGroup("barrel", heavyBarrelModel, MachinegunRenderer.texture);
		waterCoolingBox = new TmtNamedBoxGroup("barrel", waterCoolingModel, MachinegunRenderer.texture);

		secondMagazineMainBox = new TmtNamedBoxGroup("second_magazine_main", secondMagazineMainModel, MachinegunRenderer.texture);
		secondMagazineMagBox = new TmtNamedBoxGroup("second_magazine_mag", secondMagazineMagModel, MachinegunRenderer.texture);

		beltFedLoaderBox = new TmtNamedBoxGroup("belt_fed_loader", beltFedLoaderModel, MachinegunRenderer.texture);

		scopeBox = new TmtNamedBoxGroup("scope", scopeModel, MachinegunRenderer.texture);
		infraredScopeBox = new TmtNamedBoxGroup("infrared_scope", infraredScopeModel, MachinegunRenderer.texture);

		hastyBipodBox = new TmtNamedBoxGroup("bipod", hastyBipodModel, MachinegunRenderer.texture)
		{
			@Override
			public void render(float scale, float animation)
			{
				float legs = animation > 0.5f?(animation-0.5f)/0.5f: 0;
				GlStateManager.pushMatrix();
				GlStateManager.translate(8F*scale, 4.75F*scale, -26F*scale);
				GlStateManager.rotate(-90*animation, 1, 0, 0);
				GlStateManager.translate(0, scale*animation, 0);
				GlStateManager.rotate(-35f*(1f-legs), 0f, 0f, 1f);
				getModel()[0].render(scale);
				GlStateManager.rotate(2*35f*(1f-legs), 0f, 0f, 1f);
				getModel()[1].render(scale);
				GlStateManager.popMatrix();
			}
		};
		preciseBipodBox = new TmtNamedBoxGroup("bipod", preciseBipodModel, MachinegunRenderer.texture)
		{
			@Override
			public void render(float scale, float animation)
			{
				float legs = animation > 0.5f?(animation-0.5f)/0.5f: 0;
				GlStateManager.pushMatrix();
				GlStateManager.translate(8F*scale, 4.75F*scale, -26F*scale);
				GlStateManager.rotate(-90*animation, 1, 0, 0);
				GlStateManager.translate(0, scale*animation, 0);
				GlStateManager.rotate(-35f*(1f-legs), 0f, 0f, 1f);
				getModel()[0].render(scale);
				GlStateManager.rotate(2*35f*(1f-legs), 0f, 0f, 1f);
				getModel()[1].render(scale);
				GlStateManager.popMatrix();
			}
		};
		tripodBox = new TmtNamedBoxGroup("bipod", null, MachinegunRenderer.texture)
		{
			final ModelRendererTurbo[] tripodBaseModel = ModelMachinegun.this.tripodBaseModel;
			final ModelRendererTurbo[] tripodLeg1Model = ModelMachinegun.this.tripodLeg1Model;
			final ModelRendererTurbo[] tripodLeg2Model = ModelMachinegun.this.tripodLeg2Model;
			final ModelRendererTurbo[] tripodLeg3Model = ModelMachinegun.this.tripodLeg3Model;

			@Override
			public void render(float scale, float animation)
			{
				GlStateManager.pushMatrix();

				if(animation < 1)
				{
					float legs = 1f-(animation > 0.5f?(animation-0.5f)/0.5f: 0);
					float tripodProgress = MathHelper.clamp(legs/0.75f, 0, 1);
					float tripodLegProgress = (MathHelper.clamp(legs/0.95f, 0, 1)*0.5934119f)-0.29670597F;

					GlStateManager.translate(8F*scale, 7.75F*scale, -26F*scale);


					//GlStateManager.color(1f, 1f, 1f, Math.min(tripodProgress, 1));
					GlStateManager.translate(0, (tripodProgress)*-24f*scale, 0);
					for(ModelRendererTurbo mod : tripodBaseModel)
						mod.render(scale);

					for(ModelRendererTurbo mod : tripodLeg1Model)
					{
						mod.rotateAngleX = tripodLegProgress;
						mod.render(scale);
					}
					for(ModelRendererTurbo mod : tripodLeg2Model)
					{
						mod.rotateAngleX = -tripodLegProgress;
						mod.render(scale);
					}
					for(ModelRendererTurbo mod : tripodLeg3Model)
					{
						mod.rotateAngleX = tripodLegProgress;
						mod.render(scale);
					}
				}
				else
				{

				}

				GlStateManager.popMatrix();
			}
		};
		shieldBox = new TmtNamedBoxGroup("shield", shieldModel, MachinegunRenderer.texture);

		//Custom skin elements can be added, but it requires mentioning in skin file too
		baubleBox = new TmtNamedBoxGroup("skin_bauble", baubleModel, MachinegunRenderer.texture);
	}
}
