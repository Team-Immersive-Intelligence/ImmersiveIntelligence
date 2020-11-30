package pl.pabilo8.immersiveintelligence.client.model.armor;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.TMTArmorModel;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.tmt.Coord2D;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.tmt.Shape2D;

/**
 * @author Pabilo8
 * @since 13.09.2020
 */
@SideOnly(Side.CLIENT)
public class ModelLightEngineerArmor extends TMTArmorModel implements IReloadableModelContainer<ModelLightEngineerArmor>
{
	static int textureX = 64;
	static int textureY = 64;
	private static final String texture = ImmersiveIntelligence.MODID+":textures/armor/engineer_light.png";
	ModelRendererTurbo[] capeModel;

	static
	{
		modelInstance = new ModelLightEngineerArmor().subscribeToList("light_engineer_armor");
	}

	public ModelLightEngineerArmor()
	{
		super(textureX, textureY, texture);

		headModel = new ModelRendererTurbo[25];
		headModel[0] = new ModelRendererTurbo(this, 34, 40, textureX, textureY); // Box 0
		headModel[1] = new ModelRendererTurbo(this, 54, 23, textureX, textureY); // Box 0
		headModel[2] = new ModelRendererTurbo(this, 34, 40, textureX, textureY); // Box 0
		headModel[3] = new ModelRendererTurbo(this, 54, 23, textureX, textureY); // Box 0
		headModel[4] = new ModelRendererTurbo(this, 0, 38, textureX, textureY); // Box 7
		headModel[5] = new ModelRendererTurbo(this, 8, 0, textureX, textureY); // Box 7
		headModel[6] = new ModelRendererTurbo(this, 28, 45, textureX, textureY); // Box 7
		headModel[7] = new ModelRendererTurbo(this, 27, 40, textureX, textureY); // Shape 11
		headModel[8] = new ModelRendererTurbo(this, 27, 40, textureX, textureY); // Shape 11
		headModel[9] = new ModelRendererTurbo(this, 33, 33, textureX, textureY); // Box 7
		headModel[10] = new ModelRendererTurbo(this, 16, 55, textureX, textureY); // Box 15
		headModel[11] = new ModelRendererTurbo(this, 17, 25, textureX, textureY); // Box 15
		headModel[12] = new ModelRendererTurbo(this, 17, 25, textureX, textureY); // Box 15
		headModel[13] = new ModelRendererTurbo(this, 45, 33, textureX, textureY); // Box 7
		headModel[14] = new ModelRendererTurbo(this, 22, 36, textureX, textureY); // Box 7
		headModel[15] = new ModelRendererTurbo(this, 50, 23, textureX, textureY); // Box 7
		headModel[16] = new ModelRendererTurbo(this, 50, 23, textureX, textureY); // Box 7
		headModel[17] = new ModelRendererTurbo(this, 26, 10, textureX, textureY); // Box 25
		headModel[18] = new ModelRendererTurbo(this, 26, 9, textureX, textureY); // Box 25
		headModel[19] = new ModelRendererTurbo(this, 26, 10, textureX, textureY); // Box 25
		headModel[20] = new ModelRendererTurbo(this, 26, 9, textureX, textureY); // Box 25
		headModel[21] = new ModelRendererTurbo(this, 40, 52, textureX, textureY); // Box 7
		headModel[22] = new ModelRendererTurbo(this, 39, 52, textureX, textureY); // Box 7
		headModel[23] = new ModelRendererTurbo(this, 0, 20, textureX, textureY); // Box 7
		headModel[24] = new ModelRendererTurbo(this, 39, 52, textureX, textureY); // Box 7

		headModel[0].addBox(0F, 0F, 0F, 1, 4, 8, 0F); // Box 0
		headModel[0].setRotationPoint(4F, -8F, -4F);
		headModel[0].rotateAngleZ = 0.08726646F;

		headModel[1].addBox(0F, 4F, 5F, 1, 4, 3, 0F); // Box 0
		headModel[1].setRotationPoint(4F, -8F, -4F);
		headModel[1].rotateAngleZ = 0.08726646F;

		headModel[2].addBox(0F, 0F, 0F, 1, 4, 8, 0F); // Box 0
		headModel[2].setRotationPoint(-5F, -8F, -4F);
		headModel[2].rotateAngleZ = -0.08726646F;

		headModel[3].addBox(0F, 4F, 5F, 1, 4, 3, 0F); // Box 0
		headModel[3].setRotationPoint(-5F, -8F, -4F);
		headModel[3].rotateAngleZ = -0.08726646F;

		headModel[4].addBox(0F, 0F, 0F, 8, 8, 1, 0F); // Box 7
		headModel[4].setRotationPoint(-4F, -7.99F, 4F);
		headModel[4].rotateAngleX = 0.10471976F;

		headModel[5].addShapeBox(0F, 0F, 0F, 10, 1, 2, 0F, -1F, 0F, -1F, -1F, 0F, -1F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 7
		headModel[5].setRotationPoint(-5F, -9F, -5F);
		headModel[5].rotateAngleX = -0.06981317F;

		headModel[6].addBox(-1F, 0F, -1F, 5, 1, 2, 0F); // Box 7
		headModel[6].setRotationPoint(-4F, -6F, -5F);
		headModel[6].rotateAngleX = -0.06981317F;
		headModel[6].rotateAngleZ = 0.03490659F;

		headModel[7].addShape3D(-1F, -8F, -1F, new Shape2D(new Coord2D[]{new Coord2D(2, 0, 2, 0), new Coord2D(4, 0, 4, 0), new Coord2D(4, 4, 4, 4), new Coord2D(0, 4, 0, 4), new Coord2D(0, 2, 0, 2)}), 1, 4, 4, 15, 1, ModelRendererTurbo.MR_FRONT, new float[]{3, 2, 4, 4, 2}); // Shape 11
		headModel[7].setRotationPoint(-5F, -8F, -4F);
		headModel[7].rotateAngleX = -0.08726646F;
		headModel[7].rotateAngleY = -1.57079633F;

		headModel[8].addShape3D(-1F, -8F, -1F, new Shape2D(new Coord2D[]{new Coord2D(2, 0, 2, 0), new Coord2D(4, 0, 4, 0), new Coord2D(4, 4, 4, 4), new Coord2D(0, 4, 0, 4), new Coord2D(0, 2, 0, 2)}), 1, 4, 4, 15, 1, ModelRendererTurbo.MR_FRONT, new float[]{3, 2, 4, 4, 2}); // Shape 11
		headModel[8].setRotationPoint(4F, -8F, -4F);
		headModel[8].rotateAngleX = 0.08726646F;
		headModel[8].rotateAngleY = -1.57079633F;

		headModel[9].addBox(-4F, 0F, -1F, 5, 1, 2, 0F); // Box 7
		headModel[9].setRotationPoint(4F, -6F, -5F);
		headModel[9].rotateAngleX = -0.06981317F;
		headModel[9].rotateAngleZ = -0.03490659F;

		headModel[10].addBox(0F, 0F, 0F, 8, 1, 8, 0F); // Box 15
		headModel[10].setRotationPoint(-4F, -9F, -4F);

		headModel[11].addShapeBox(0F, -1F, 0F, 1, 1, 8, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 15
		headModel[11].setRotationPoint(4F, -8F, -4F);
		headModel[11].rotateAngleZ = 0.08726646F;

		headModel[12].addShapeBox(0F, -1F, 0F, 1, 1, 8, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 15
		headModel[12].setRotationPoint(-5F, -8F, -4F);
		headModel[12].rotateAngleZ = -0.08726646F;

		headModel[13].addShapeBox(0F, -1F, 0F, 8, 1, 1, 0F, 0F, 0F, -0.09F, 0F, 0F, -0.09F, 0F, 0F, -0.91F, 0F, 0F, -0.91F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 7
		headModel[13].setRotationPoint(-4F, -8F, 4F);
		headModel[13].rotateAngleX = 0.10471976F;

		headModel[14].addBox(0F, 1F, 0F, 10, 2, 2, 0F); // Box 7
		headModel[14].setRotationPoint(-5F, -9F, -5F);
		headModel[14].rotateAngleX = -0.06981317F;

		headModel[15].addBox(1F, 0F, 0.5F, 2, 2, 1, 0F); // Box 7
		headModel[15].setRotationPoint(-5F, -9F, -6F);
		headModel[15].rotateAngleX = -0.2443461F;

		headModel[16].addBox(1F, 0F, 0.5F, 2, 2, 1, 0F); // Box 7
		headModel[16].setRotationPoint(1F, -9F, -6F);
		headModel[16].rotateAngleX = -0.2443461F;

		headModel[17].addBox(-4F, 0F, 0F, 4, 0, 1, 0F); // Box 25
		headModel[17].setRotationPoint(0F, 0.01F, -3F);

		headModel[18].addBox(-4F, 0F, 0F, 4, 0, 1, 0F); // Box 25
		headModel[18].setRotationPoint(-4F, 0F, -3F);
		headModel[18].rotateAngleZ = -1.48352986F;

		headModel[19].addBox(0F, 0F, 0F, 4, 0, 1, 0F); // Box 25
		headModel[19].setRotationPoint(0F, 0.01F, -3F);

		headModel[20].addBox(0F, 0F, 0F, 4, 0, 1, 0F); // Box 25
		headModel[20].setRotationPoint(4F, 0F, -3F);
		headModel[20].rotateAngleZ = 1.48352986F;

		headModel[21].addShapeBox(-0.5F, 0F, -0.5F, 1, 8, 3, 0F, 0.2F, 0.1F, -1.2F, -0.5F, 0.1F, -0.5F, -0.39F, 0.1F, -1.1F, 0.2F, 0.1F, -1.8F, 0.4F, -0.1F, -0.9F, -0.75F, 0F, -0.05F, -0.5F, 0F, -0.25F, 0.25F, 0F, -1F); // Box 7
		headModel[21].setRotationPoint(5F, -8F, 4F);
		headModel[21].rotateAngleX = -0.01745329F;
		headModel[21].rotateAngleY = 0.85521133F;
		headModel[21].rotateAngleZ = 0.08726646F;

		headModel[22].addShapeBox(-1F, -1F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, -1F, 0F, 0F, -1F, 0F, -0.1F, 0F, 0F, -0.1F, 0F, -0.5F, -0.1F, -0.5F, 0F, -0.1F, 0F); // Box 7
		headModel[22].setRotationPoint(5F, -8F, 4F);
		headModel[22].rotateAngleX = -0.01745329F;
		headModel[22].rotateAngleZ = -0.01745329F;

		headModel[23].addShapeBox(-0.5F, 0F, -0.5F, 1, 8, 3, 0F, 0.2F, 0.1F, -1.2F, -0.5F, 0.1F, -0.5F, -0.39F, 0.1F, -1.1F, 0.2F, 0.1F, -1.8F, 0.4F, -0.1F, -0.9F, -0.75F, 0F, -0.05F, -0.5F, 0F, -0.25F, 0.25F, 0F, -1F); // Box 7
		headModel[23].setRotationPoint(-4F, -8F, 5F);
		headModel[23].rotateAngleX = -0.01745329F;
		headModel[23].rotateAngleY = 2.44346095F;
		headModel[23].rotateAngleZ = 0.08726646F;

		headModel[24].addShapeBox(-1F, -1F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, -1F, 0F, 0F, -1F, 0F, -0.1F, 0F, 0F, -0.1F, 0F, -0.5F, -0.1F, -0.5F, 0F, -0.1F, 0F); // Box 7
		headModel[24].setRotationPoint(-4F, -8F, 5F);
		headModel[24].rotateAngleX = -0.01745329F;
		headModel[24].rotateAngleY = 1.58824962F;
		headModel[24].rotateAngleZ = -0.01745329F;


		bodyModel = new ModelRendererTurbo[6];
		bodyModel[0] = new ModelRendererTurbo(this, 28, 19, textureX, textureY); // Box 29
		bodyModel[1] = new ModelRendererTurbo(this, 48, 15, textureX, textureY); // Box 29
		bodyModel[2] = new ModelRendererTurbo(this, 48, 19, textureX, textureY); // Box 29
		bodyModel[3] = new ModelRendererTurbo(this, 48, 15, textureX, textureY); // Box 29
		bodyModel[4] = new ModelRendererTurbo(this, 0, 47, textureX, textureY); // Box 35
		bodyModel[5] = new ModelRendererTurbo(this, 46, 35, textureX, textureY); // Box 35

		bodyModel[0].addBox(0F, 0F, 0F, 8, 4, 2, 0F); // Box 29
		bodyModel[0].setRotationPoint(-4F, 0.5F, -4F);

		bodyModel[1].addBox(0F, 0F, 0F, 6, 2, 2, 0F); // Box 29
		bodyModel[1].setRotationPoint(-3F, 4.5F, -3.5F);

		bodyModel[2].addBox(0F, 0F, 0F, 6, 2, 2, 0F); // Box 29
		bodyModel[2].setRotationPoint(-3F, 6.5F, -3.4F);

		bodyModel[3].addBox(0F, 0F, 0F, 6, 2, 2, 0F); // Box 29
		bodyModel[3].setRotationPoint(-3F, 8.5F, -3.5F);

		bodyModel[4].addShapeBox(0F, 0F, 0F, 8, 12, 4, 0F, 0.25F, 0.125F, 0.25F, 0.25F, 0.125F, 0.25F, 0.25F, 0.125F, 0.25F, 0.25F, 0.125F, 0.25F, 0.25F, 0.125F, 0.25F, 0.25F, 0.125F, 0.25F, 0.25F, 0.125F, 0.25F, 0.25F, 0.125F, 0.25F); // Box 35
		bodyModel[4].setRotationPoint(-4F, 0F, -2F);

		bodyModel[5].addBox(0F, 0F, 0F, 8, 12, 1, 0F); // Box 35
		bodyModel[5].setRotationPoint(-4F, 0F, 2F);


		leftArmModel = new ModelRendererTurbo[5];
		leftArmModel[0] = new ModelRendererTurbo(this, 44, 23, textureX, textureY); // Box 29
		leftArmModel[1] = new ModelRendererTurbo(this, 30, 9, textureX, textureY); // Box 29
		leftArmModel[2] = new ModelRendererTurbo(this, 40, 0, textureX, textureY); // Box 29
		leftArmModel[3] = new ModelRendererTurbo(this, 28, 0, textureX, textureY); // Box 29
		leftArmModel[4] = new ModelRendererTurbo(this, 8, 19, textureX, textureY); // Box 29

		leftArmModel[0].addBox(-5F, -3F, 0F, 1, 6, 4, 0F); // Box 29
		leftArmModel[0].setRotationPoint(8F, 0.5F, -2F);
		leftArmModel[0].rotateAngleZ = 0.03490659F;

		leftArmModel[1].addShapeBox(-5F, -3F, 0F, 4, 5, 5, 0F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F); // Box 29
		leftArmModel[1].setRotationPoint(4F, 0.5F, -2.5F);

		leftArmModel[2].addShapeBox(-5.5F, -3F, 0F, 2, 1, 2, 0F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F); // Box 29
		leftArmModel[2].setRotationPoint(7F, -1F, -1F);
		leftArmModel[2].rotateAngleZ = -0.26179939F;

		leftArmModel[3].addShapeBox(-5F, -3F, 0F, 4, 5, 4, 0F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F); // Box 29
		leftArmModel[3].setRotationPoint(4F, 5.5F, -2F);

		leftArmModel[4].addShapeBox(-5.5F, -3F, 0F, 6, 1, 4, 0F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F); // Box 29
		leftArmModel[4].setRotationPoint(4F, -0.5F, -2F);
		leftArmModel[4].rotateAngleZ = -0.13962634F;


		rightArmModel = new ModelRendererTurbo[5];
		rightArmModel[0] = new ModelRendererTurbo(this, 44, 23, textureX, textureY); // Box 29
		rightArmModel[1] = new ModelRendererTurbo(this, 40, 0, textureX, textureY); // Box 29
		rightArmModel[2] = new ModelRendererTurbo(this, 28, 0, textureX, textureY); // Box 29
		rightArmModel[3] = new ModelRendererTurbo(this, 30, 9, textureX, textureY); // Box 29
		rightArmModel[4] = new ModelRendererTurbo(this, 5, 28, textureX, textureY); // Box 29

		rightArmModel[0].addBox(5F, -3F, 0F, 1, 6, 4, 0F); // Box 29
		rightArmModel[0].setRotationPoint(-9F, 0.5F, -2F);
		rightArmModel[0].rotateAngleZ = -0.03490659F;

		rightArmModel[1].addShapeBox(4.5F, -3F, 0F, 2, 1, 2, 0F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F); // Box 29
		rightArmModel[1].setRotationPoint(-8F, -1F, -1F);
		rightArmModel[1].rotateAngleZ = 0.26179939F;

		rightArmModel[2].addShapeBox(5F, -3F, 0F, 4, 5, 4, 0F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F); // Box 29
		rightArmModel[2].setRotationPoint(-8F, 5.5F, -2F);

		rightArmModel[3].addShapeBox(5F, -3F, 0F, 4, 5, 5, 0F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F); // Box 29
		rightArmModel[3].setRotationPoint(-8F, 0.5F, -2.5F);

		rightArmModel[4].addShapeBox(4.5F, -2F, 0F, 6, 1, 4, 0F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F); // Box 29
		rightArmModel[4].setRotationPoint(-9F, -1F, -2F);
		rightArmModel[4].rotateAngleZ = 0.13962634F;

		leftLegModel = new ModelRendererTurbo[5];
		leftLegModel[0] = new ModelRendererTurbo(this, 48, 48, textureX, textureY); // Box 29
		leftLegModel[1] = new ModelRendererTurbo(this, 54, 10, textureX, textureY); // Box 29
		leftLegModel[2] = new ModelRendererTurbo(this, 7, 33, textureX, textureY); // Box 29
		leftLegModel[3] = new ModelRendererTurbo(this, 28, 25, textureX, textureY); // Box 29
		leftLegModel[4] = new ModelRendererTurbo(this, 43, 10, textureX, textureY); // Box 29

		leftLegModel[0].addShapeBox(-2F, -11.5F, 0F, 4, 12, 4, 0F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F); // Box 29
		leftLegModel[0].setRotationPoint(0F, 11.5F, -2F);

		leftLegModel[1].addShapeBox(-6F, -12F, 0F, 4, 4, 1, 0F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F); // Box 29
		leftLegModel[1].setRotationPoint(4F, 11.5F, -3F);
		leftLegModel[1].rotateAngleX = -0.03490659F;
		leftLegModel[1].rotateAngleY = 0.03490659F;

		leftLegModel[2].addShapeBox(-6F, -12.5F, 0F, 4, 4, 1, 0F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F); // Box 29
		leftLegModel[2].setRotationPoint(4F, 16.25F, -3F);
		leftLegModel[2].rotateAngleX = 0.05235988F;
		leftLegModel[2].rotateAngleY = 0.03490659F;

		leftLegModel[3].addShapeBox(-2F, -11.5F, 0F, 4, 4, 4, 0F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F); // Box 29
		leftLegModel[3].setRotationPoint(0F, 13.5F, -2F);

		leftLegModel[4].addShapeBox(-2F, -11.5F, 0F, 3, 3, 1, 0F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F); // Box 29
		leftLegModel[4].setRotationPoint(0.5F, 14F, 2F);


		rightLegModel = new ModelRendererTurbo[5];
		rightLegModel[0] = new ModelRendererTurbo(this, 48, 48, textureX, textureY); // Box 29
		rightLegModel[1] = new ModelRendererTurbo(this, 54, 10, textureX, textureY); // Box 29
		rightLegModel[2] = new ModelRendererTurbo(this, 7, 33, textureX, textureY); // Box 29
		rightLegModel[3] = new ModelRendererTurbo(this, 28, 25, textureX, textureY); // Box 29
		rightLegModel[4] = new ModelRendererTurbo(this, 43, 10, textureX, textureY); // Box 29

		rightLegModel[0].addShapeBox(2F, -11.5F, 0F, 4, 12, 4, 0F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F); // Box 29
		rightLegModel[0].setRotationPoint(-4F, 11.5F, -2F);

		rightLegModel[1].addShapeBox(2F, -12F, 0F, 4, 4, 1, 0F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F); // Box 29
		rightLegModel[1].setRotationPoint(-4F, 11.5F, -3F);
		rightLegModel[1].rotateAngleX = -0.03490659F;
		rightLegModel[1].rotateAngleY = -0.03490659F;

		rightLegModel[2].addShapeBox(2F, -12.5F, 0F, 4, 4, 1, 0F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F); // Box 29
		rightLegModel[2].setRotationPoint(-4F, 16.25F, -3F);
		rightLegModel[2].rotateAngleX = 0.05235988F;
		rightLegModel[2].rotateAngleY = -0.03490659F;

		rightLegModel[3].addShapeBox(2F, -11.5F, 0F, 4, 4, 4, 0F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F); // Box 29
		rightLegModel[3].setRotationPoint(-4F, 13.5F, -2F);

		rightLegModel[4].addShapeBox(2F, -11.5F, 0F, 3, 3, 1, 0F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F); // Box 29
		rightLegModel[4].setRotationPoint(-3.5F, 14F, 2F);

		rightFootModel = new ModelRendererTurbo[1];
		rightFootModel[0] = new ModelRendererTurbo(this, 44, 0, textureX, textureY); // Box 29

		rightFootModel[0].addShapeBox(0F, -11.5F, 0F, 4, 4, 6, 0F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F); // Box 29
		rightFootModel[0].setRotationPoint(-2F, 19.5F, -4F);


		leftFootModel = new ModelRendererTurbo[1];
		leftFootModel[0] = new ModelRendererTurbo(this, 44, 0, textureX, textureY); // Box 29

		leftFootModel[0].addShapeBox(0F, -11.5F, 0F, 4, 4, 6, 0F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F); // Box 29
		leftFootModel[0].setRotationPoint(-2F, 19.5F, -4F);

		capeModel = new ModelRendererTurbo[3];

		capeModel[0] = new ModelRendererTurbo(this, 1, -2, textureX, textureY); // Box 35
		capeModel[1] = new ModelRendererTurbo(this, 1, -2, textureX, textureY); // Box 35
		capeModel[2] = new ModelRendererTurbo(this, 11, 3, textureX, textureY); // Box 35

		capeModel[0].addBox(0F, 0F, 0F, 0, 16, 5, 0F); // Box 35
		capeModel[0].setRotationPoint(-4F, 0F, -2F);
		capeModel[0].rotateAngleX = 0.06108652F;
		capeModel[0].rotateAngleZ = -0.06108652F;

		capeModel[1].addBox(0F, 0F, 0F, 0, 16, 5, 0F); // Box 35
		capeModel[1].setRotationPoint(4F, 0F, -2F);
		capeModel[1].rotateAngleX = 0.06108652F;
		capeModel[1].rotateAngleZ = 0.06108652F;

		capeModel[2].addShapeBox(0F, 0F, 0F, 8, 16, 0, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 1F, 0F, 0F, 1F, 0F, 0F, 1F, 0F, 0F, 1F, 0F, 0F); // Box 35
		capeModel[2].setRotationPoint(-4F, 0F, 3F);
		capeModel[2].rotateAngleX = 0.06108652F;

		parts.put("cape", capeModel);

		flipAll();
		init();
	}

	static ModelLightEngineerArmor modelInstance;

	public static ModelLightEngineerArmor getModel(EntityEquipmentSlot part, ItemStack stack)
	{
		return (ModelLightEngineerArmor)modelInstance.prepareForRender(part, stack);
	}


	public void renderAddons(ItemStack renderStack, EntityEquipmentSlot renderSlot, float scale, float ageInTicks)
	{
		/*
		if(renderSlot==EntityEquipmentSlot.CHEST)
		{
			float pt = Math.abs((((ageInTicks+Minecraft.getMinecraft().getRenderPartialTicks())%160)/160f)-0.5f)/.5f;
			for(ModelRendererTurbo mod : capeModel)
				mod.rotateAngleX = 0.0625f+(pt*0.125f);
			renderChild(this.bipedBody, capeModel, scale);
		}
		 */
	}

	//yes, a bit weird mix of static and non-static methods here indeed
	//but it works:tm:
	@Override
	public void reloadModels()
	{
		unsubscribeToList();
		modelInstance = new ModelLightEngineerArmor().subscribeToList("light_engineer_armor");
	}
}
