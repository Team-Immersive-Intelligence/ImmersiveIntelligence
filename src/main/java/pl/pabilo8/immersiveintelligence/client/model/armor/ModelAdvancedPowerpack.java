package pl.pabilo8.immersiveintelligence.client.model.armor;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.TMTArmorModel;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

/**
 * @author Pabilo8
 * @since 15.04.2021
 */
public class ModelAdvancedPowerpack extends TMTArmorModel implements IReloadableModelContainer<ModelAdvancedPowerpack>
{
	static int textureX = 64;
	static int textureY = 32;
	private static final String texture = ImmersiveIntelligence.MODID+":textures/armor/advanced_powerpack.png";
	private static final String textureLayer = ImmersiveIntelligence.MODID+":textures/armor/advanced_powerpack_paint.png";
	private IIColor renderColor;

	private final ModelRendererTurbo[] bodyColoredModel;

	static
	{
		modelInstance = new ModelAdvancedPowerpack().subscribeToList("advanced_powerpack");
	}

	public ModelAdvancedPowerpack()
	{
		super(textureX, textureY, texture);

		headModel = new ModelRendererTurbo[0];
		leftArmModel = new ModelRendererTurbo[0];
		rightArmModel = new ModelRendererTurbo[0];
		leftLegModel = new ModelRendererTurbo[0];
		rightLegModel = new ModelRendererTurbo[0];
		leftFootModel = new ModelRendererTurbo[0];
		rightFootModel = new ModelRendererTurbo[0];

		bodyModel = new ModelRendererTurbo[47];
		bodyModel[0] = new ModelRendererTurbo(this, 16, 25, textureX, textureY); // Box 7
		bodyModel[1] = new ModelRendererTurbo(this, 42, 11, textureX, textureY); // Box 8
		bodyModel[2] = new ModelRendererTurbo(this, 56, 0, textureX, textureY); // Box 10
		bodyModel[3] = new ModelRendererTurbo(this, 0, 26, textureX, textureY); // Box 11
		bodyModel[4] = new ModelRendererTurbo(this, 0, 26, textureX, textureY); // Box 12
		bodyModel[5] = new ModelRendererTurbo(this, 0, 26, textureX, textureY); // Box 13
		bodyModel[6] = new ModelRendererTurbo(this, 4, 12, textureX, textureY); // Box 14
		bodyModel[7] = new ModelRendererTurbo(this, 28, 14, textureX, textureY); // Box 15
		bodyModel[8] = new ModelRendererTurbo(this, 31, 11, textureX, textureY); // Box 16
		bodyModel[9] = new ModelRendererTurbo(this, 44, 5, textureX, textureY); // Box 17
		bodyModel[10] = new ModelRendererTurbo(this, 30, 24, textureX, textureY); // Box 18
		bodyModel[11] = new ModelRendererTurbo(this, 56, 0, textureX, textureY); // Box 23
		bodyModel[12] = new ModelRendererTurbo(this, 56, 0, textureX, textureY); // Box 24
		bodyModel[13] = new ModelRendererTurbo(this, 52, 3, textureX, textureY); // Box 25
		bodyModel[14] = new ModelRendererTurbo(this, 52, 3, textureX, textureY); // Box 26
		bodyModel[15] = new ModelRendererTurbo(this, 34, 5, textureX, textureY); // Box 27
		bodyModel[16] = new ModelRendererTurbo(this, 4, 8, textureX, textureY); // Box 28
		bodyModel[17] = new ModelRendererTurbo(this, 21, 21, textureX, textureY); // Box 35
		bodyModel[18] = new ModelRendererTurbo(this, 21, 17, textureX, textureY); // Box 36
		bodyModel[19] = new ModelRendererTurbo(this, 44, 5, textureX, textureY); // Box 37
		bodyModel[20] = new ModelRendererTurbo(this, 21, 17, textureX, textureY); // Box 38
		bodyModel[21] = new ModelRendererTurbo(this, 21, 21, textureX, textureY); // Box 39
		bodyModel[22] = new ModelRendererTurbo(this, 4, 8, textureX, textureY); // Box 40
		bodyModel[23] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 18
		bodyModel[24] = new ModelRendererTurbo(this, 30, 18, textureX, textureY); // Box 19
		bodyModel[25] = new ModelRendererTurbo(this, 30, 18, textureX, textureY); // Box 19
		bodyModel[26] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 18
		bodyModel[27] = new ModelRendererTurbo(this, 25, 5, textureX, textureY); // Box 22
		bodyModel[28] = new ModelRendererTurbo(this, 4, 22, textureX, textureY); // Box 14
		bodyModel[29] = new ModelRendererTurbo(this, 2, 22, textureX, textureY); // Box 22
		bodyModel[30] = new ModelRendererTurbo(this, 22, 12, textureX, textureY); // Box 22
		bodyModel[31] = new ModelRendererTurbo(this, 16, 21, textureX, textureY); // Box 22
		bodyModel[32] = new ModelRendererTurbo(this, 16, 16, textureX, textureY); // Box 22
		bodyModel[33] = new ModelRendererTurbo(this, 44, 1, textureX, textureY); // Box 22
		bodyModel[34] = new ModelRendererTurbo(this, 36, 1, textureX, textureY); // Box 22
		bodyModel[35] = new ModelRendererTurbo(this, 1, 8, textureX, textureY); // Box 22
		bodyModel[36] = new ModelRendererTurbo(this, 2, 22, textureX, textureY); // Box 22
		bodyModel[37] = new ModelRendererTurbo(this, 25, 8, textureX, textureY); // Box 22
		bodyModel[38] = new ModelRendererTurbo(this, 12, 12, textureX, textureY); // Box 22
		bodyModel[39] = new ModelRendererTurbo(this, 44, 1, textureX, textureY); // Box 22
		bodyModel[40] = new ModelRendererTurbo(this, 36, 1, textureX, textureY); // Box 22
		bodyModel[41] = new ModelRendererTurbo(this, 1, 8, textureX, textureY); // Box 22
		bodyModel[42] = new ModelRendererTurbo(this, 0, 29, textureX, textureY); // Box 11
		bodyModel[43] = new ModelRendererTurbo(this, 0, 29, textureX, textureY); // Box 12
		bodyModel[44] = new ModelRendererTurbo(this, 0, 29, textureX, textureY); // Box 13
		bodyModel[45] = new ModelRendererTurbo(this, 13, 0, textureX, textureY); // Box 8
		bodyModel[46] = new ModelRendererTurbo(this, 13, 0, textureX, textureY); // Box 8

		bodyModel[0].addShapeBox(0F, 0F, 0F, 5, 5, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 7
		bodyModel[0].setRotationPoint(-2.5F, -10F, 5F);

		bodyModel[1].addShapeBox(0F, 0F, 0F, 8, 10, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 8
		bodyModel[1].setRotationPoint(-4F, -12F, 2F);

		bodyModel[2].addShapeBox(0F, 0F, 0F, 3, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 10
		bodyModel[2].setRotationPoint(-1.5F, -4F, 5F);

		bodyModel[3].addShapeBox(0F, 0F, 0F, 1, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 11
		bodyModel[3].setRotationPoint(-1.5F, -2.5F, 6.75F);
		bodyModel[3].rotateAngleX = -0.20943951F;

		bodyModel[4].addShapeBox(0F, 0F, 0F, 1, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 12
		bodyModel[4].setRotationPoint(-0.3F, -2.5F, 6.75F);
		bodyModel[4].rotateAngleX = -0.20943951F;

		bodyModel[5].addShapeBox(0F, 0F, 0F, 1, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 13
		bodyModel[5].setRotationPoint(0.9F, -2.5F, 6.75F);
		bodyModel[5].rotateAngleX = -0.20943951F;

		bodyModel[6].addShapeBox(0F, 0F, 0F, 2, 6, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 14
		bodyModel[6].setRotationPoint(-5.5F, -4F, 6.5F);
		bodyModel[6].rotateAngleY = -0.6981317F;

		bodyModel[7].addShapeBox(0F, 0F, 0F, 5, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 15
		bodyModel[7].setRotationPoint(-2.5F, -11F, 5F);

		bodyModel[8].addShapeBox(0F, 0F, 0F, 5, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F); // Box 16
		bodyModel[8].setRotationPoint(-2.5F, -5F, 5F);

		bodyModel[9].addShapeBox(0F, 0F, 0F, 2, 4, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 17
		bodyModel[9].setRotationPoint(-5F, -14F, 5.5F);

		bodyModel[10].addShapeBox(0F, 0F, 0F, 13, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 18
		bodyModel[10].setRotationPoint(-6.5F, -3F, 2.5F);

		bodyModel[11].addShapeBox(0F, 0F, 0F, 3, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 23
		bodyModel[11].setRotationPoint(-3.5F, -2F, 2F);

		bodyModel[12].addShapeBox(0F, 0F, 0F, 3, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 24
		bodyModel[12].setRotationPoint(0.5F, -2F, 2F);

		bodyModel[13].addShapeBox(0F, 0F, 0F, 3, 5, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 25
		bodyModel[13].setRotationPoint(-5.5F, -10F, 5F);

		bodyModel[14].addShapeBox(0F, 0F, 0F, 3, 5, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 26
		bodyModel[14].setRotationPoint(2.5F, -10F, 5F);

		bodyModel[15].addShapeBox(0F, 0F, 0F, 3, 4, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 27
		bodyModel[15].setRotationPoint(-1.5F, -13.5F, 4.5F);

		bodyModel[16].addShapeBox(0F, 0F, 0F, 3, 1, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 28
		bodyModel[16].setRotationPoint(-5.5F, -13.7F, 5F);

		bodyModel[17].addShapeBox(0F, 0F, 0F, 3, 1, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 35
		bodyModel[17].setRotationPoint(-5.5F, -12.5F, 5F);

		bodyModel[18].addShapeBox(0F, 0F, 0F, 3, 1, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 36
		bodyModel[18].setRotationPoint(-5.5F, -11.2F, 5F);

		bodyModel[19].addShapeBox(0F, 0F, 0F, 2, 4, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 37
		bodyModel[19].setRotationPoint(3F, -14F, 5.5F);

		bodyModel[20].addShapeBox(0F, 0F, 0F, 3, 1, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 38
		bodyModel[20].setRotationPoint(2.5F, -11.2F, 5F);

		bodyModel[21].addShapeBox(0F, 0F, 0F, 3, 1, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 39
		bodyModel[21].setRotationPoint(2.5F, -12.5F, 5F);

		bodyModel[22].addShapeBox(0F, 0F, 0F, 3, 1, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 40
		bodyModel[22].setRotationPoint(2.5F, -13.7F, 5F);

		bodyModel[23].addShapeBox(0F, 0F, 0F, 1, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 18
		bodyModel[23].setRotationPoint(-8.1F, -3F, 2.5F);

		bodyModel[24].addBox(0F, 0F, 0F, 3, 3, 3, 0F); // Box 19
		bodyModel[24].setRotationPoint(-8.9F, -2.5F, 3F);

		bodyModel[25].addBox(0F, 0F, 0F, 3, 3, 3, 0F); // Box 19
		bodyModel[25].setRotationPoint(5.9F, -2.5F, 3F);

		bodyModel[26].addShapeBox(0F, 0F, 0F, 1, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 18
		bodyModel[26].setRotationPoint(7.1F, -3F, 2.5F);

		bodyModel[27].addShapeBox(0F, 0F, 0F, 2, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 22
		bodyModel[27].setRotationPoint(-6F, -9F, 2.5F);

		bodyModel[28].addShapeBox(-2F, 0F, 0F, 2, 6, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 14
		bodyModel[28].setRotationPoint(5.5F, -4F, 6.5F);
		bodyModel[28].rotateAngleY = 0.6981317F;

		bodyModel[29].addShapeBox(0F, 0F, 0F, 1, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 22
		bodyModel[29].setRotationPoint(-6F, -8F, 5.5F);

		bodyModel[30].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 22
		bodyModel[30].setRotationPoint(4F, -11F, 2.5F);

		bodyModel[31].addShapeBox(0F, 0F, 0F, 2, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 22
		bodyModel[31].setRotationPoint(4F, -9F, 2.5F);

		bodyModel[32].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F); // Box 22
		bodyModel[32].setRotationPoint(4F, -8F, 2.5F);

		bodyModel[33].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 22
		bodyModel[33].setRotationPoint(6F, -8F, 2.5F);

		bodyModel[34].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F); // Box 22
		bodyModel[34].setRotationPoint(6F, -8F, 5.5F);

		bodyModel[35].addShapeBox(0F, 0F, 0F, 2, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 22
		bodyModel[35].setRotationPoint(6F, -8F, 4.5F);

		bodyModel[36].addShapeBox(0F, 0F, 0F, 1, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 22
		bodyModel[36].setRotationPoint(5F, -8F, 5.5F);

		bodyModel[37].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 22
		bodyModel[37].setRotationPoint(-4F, -11F, 4.5F);
		bodyModel[37].rotateAngleY = -3.14159265F;

		bodyModel[38].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F); // Box 22
		bodyModel[38].setRotationPoint(-4F, -8F, 4.5F);
		bodyModel[38].rotateAngleY = -3.14159265F;

		bodyModel[39].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 22
		bodyModel[39].setRotationPoint(-6F, -8F, 7.5F);
		bodyModel[39].rotateAngleY = -3.14159265F;

		bodyModel[40].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F); // Box 22
		bodyModel[40].setRotationPoint(-6F, -8F, 4.5F);
		bodyModel[40].rotateAngleY = -3.14159265F;

		bodyModel[41].addShapeBox(0F, 0F, 0F, 2, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 22
		bodyModel[41].setRotationPoint(-6F, -8F, 5.5F);
		bodyModel[41].rotateAngleY = -3.14159265F;

		bodyModel[42].addShapeBox(0F, 0F, 0F, 1, 2, 1, 0F, 0.125F, 0F, 0.125F, 0.125F, 0F, 0.125F, 0.125F, 0F, 0.125F, 0.125F, 0F, 0.125F, 0.125F, 0F, 0.125F, 0.125F, 0F, 0.125F, 0.125F, 0F, 0.125F, 0.125F, 0F, 0.125F); // Box 11
		bodyModel[42].setRotationPoint(-1.5F, -2.75F, 6.75F);
		bodyModel[42].rotateAngleX = -0.20943951F;

		bodyModel[43].addShapeBox(0F, 0F, 0F, 1, 2, 1, 0F, 0.125F, 0F, 0.125F, 0.125F, 0F, 0.125F, 0.125F, 0F, 0.125F, 0.125F, 0F, 0.125F, 0.125F, 0F, 0.125F, 0.125F, 0F, 0.125F, 0.125F, 0F, 0.125F, 0.125F, 0F, 0.125F); // Box 12
		bodyModel[43].setRotationPoint(-0.3F, -2.75F, 6.75F);
		bodyModel[43].rotateAngleX = -0.20943951F;

		bodyModel[44].addShapeBox(0F, 0F, 0F, 1, 2, 1, 0F, 0.125F, 0F, 0.125F, 0.125F, 0F, 0.125F, 0.125F, 0F, 0.125F, 0.125F, 0F, 0.125F, 0.125F, 0F, 0.125F, 0.125F, 0F, 0.125F, 0.125F, 0F, 0.125F, 0.125F, 0F, 0.125F); // Box 13
		bodyModel[44].setRotationPoint(0.9F, -2.75F, 6.75F);
		bodyModel[44].rotateAngleX = -0.20943951F;

		bodyModel[45].addShapeBox(0F, 0F, 0F, 4, 7, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 8
		bodyModel[45].setRotationPoint(-4F, -12F, 2F);
		bodyModel[45].rotateAngleY = -1.57079633F;

		bodyModel[46].addShapeBox(0F, 0F, 0F, 4, 7, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -4F, 0F, 0F, -4F); // Box 8
		bodyModel[46].setRotationPoint(4F, -12F, 2F);
		bodyModel[46].rotateAngleY = -1.57079633F;

		bodyColoredModel = new ModelRendererTurbo[5];
		bodyColoredModel[0] = new ModelRendererTurbo(this, 0, 26, textureX, textureY); // Box 11
		bodyColoredModel[1] = new ModelRendererTurbo(this, 0, 26, textureX, textureY); // Box 12
		bodyColoredModel[2] = new ModelRendererTurbo(this, 0, 26, textureX, textureY); // Box 13
		bodyColoredModel[3] = new ModelRendererTurbo(this, 4, 12, textureX, textureY); // Box 14
		bodyColoredModel[4] = new ModelRendererTurbo(this, 4, 22, textureX, textureY); // Box 14

		bodyColoredModel[0].addShapeBox(0F, 0F, 0F, 1, 2, 1, 0.001F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 11
		bodyColoredModel[0].setRotationPoint(-1.5F, -2.5F, 6.75F);
		bodyColoredModel[0].rotateAngleX = -0.20943951F;

		bodyColoredModel[1].addShapeBox(0F, 0F, 0F, 1, 2, 1, 0.001F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 12
		bodyColoredModel[1].setRotationPoint(-0.3F, -2.5F, 6.75F);
		bodyColoredModel[1].rotateAngleX = -0.20943951F;

		bodyColoredModel[2].addShapeBox(0F, 0F, 0F, 1, 2, 1, 0.001F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 13
		bodyColoredModel[2].setRotationPoint(0.9F, -2.5F, 6.75F);
		bodyColoredModel[2].rotateAngleX = -0.20943951F;

		bodyColoredModel[3].addShapeBox(0F, 0F, 0F, 2, 6, 4, 0.001F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 14
		bodyColoredModel[3].setRotationPoint(-5.5F, -4F, 6.5F);
		bodyColoredModel[3].rotateAngleY = -0.6981317F;

		bodyColoredModel[4].addShapeBox(-2F, 0F, 0F, 2, 6, 4, 0.001F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 14
		bodyColoredModel[4].setRotationPoint(5.5F, -4F, 6.5F);
		bodyColoredModel[4].rotateAngleY = 0.6981317F;

		parts.put("bodyColored", bodyColoredModel);

		for(ModelRendererTurbo mod : bodyModel)
			mod.rotationPointY += 11.9f;
		for(ModelRendererTurbo mod : bodyColoredModel)
			mod.rotationPointY += 11.9f;

		flipAll();
		init();
	}

	static ModelAdvancedPowerpack modelInstance;

	public static ModelAdvancedPowerpack getModel(EntityEquipmentSlot part, ItemStack stack)
	{
		return (ModelAdvancedPowerpack)modelInstance.prepareForRender(part, stack);
	}

	protected void actualRender(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
		GlStateManager.pushMatrix();

		if(entity.isSneaking())
		{
			GlStateManager.translate(0.0F, 0.2F, 0.0F);
		}
		if(renderSlot==EntityEquipmentSlot.CHEST)
		{
			if(!bipedBody.isHidden&&bipedBody.showModel)
			{
				GlStateManager.pushMatrix();
				GlStateManager.enableBlend();
				ClientUtils.bindTexture(texture);

				renderChild(bipedBody, bodyModel, scale, texture);
				renderColor.glColor();
				renderChild(bipedBody, bodyColoredModel, scale, textureLayer);
				GlStateManager.color(1f, 1f, 1f);

				//GlStateManager.disableBlend();
				GlStateManager.popMatrix();
			}
		}

		GlStateManager.popMatrix();
	}

	@Override
	protected TMTArmorModel prepareForRender(EntityEquipmentSlot part, ItemStack stack)
	{
		this.renderColor = IIColor.fromPackedRGB(IIContent.itemAdvancedPowerPack.getColor(stack));
		return super.prepareForRender(part, stack);
	}

	//yes, a bit weird mix of static and non-static methods here indeed
	//but it works:tm:
	@Override
	public void reloadModels()
	{
		unsubscribeToList();
		modelInstance = new ModelAdvancedPowerpack().subscribeToList("advanced_powerpack");
	}
}
