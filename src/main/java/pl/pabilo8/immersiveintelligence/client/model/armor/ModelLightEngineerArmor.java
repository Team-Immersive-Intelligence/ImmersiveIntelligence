package pl.pabilo8.immersiveintelligence.client.model.armor;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.TMTArmorModel;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Coord2D;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Shape2D;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.IISkinHandler;
import pl.pabilo8.immersiveintelligence.common.util.IISkinHandler.IISpecialSkin;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIIUpgradeableArmor;

/**
 * @author Pabilo8
 * @since 13.09.2020
 */
@SideOnly(Side.CLIENT)
public class ModelLightEngineerArmor extends TMTArmorModel implements IReloadableModelContainer<ModelLightEngineerArmor>
{
	static int textureX = 64;
	static int textureY = 64;
	private static String TEXTURE = ImmersiveIntelligence.MODID+":textures/armor/engineer_light.png";
	private static String TEXTURE_GASMASK = ImmersiveIntelligence.MODID+":textures/armor/engineer_light_gasmask.png";
	private static String TEXTURE_GOGGLES = ImmersiveIntelligence.MODID+":textures/armor/engineer_light_goggles.png";
	private static String TEXTURE_PLATES = ImmersiveIntelligence.MODID+":textures/armor/engineer_light_plates.png";
	private static String TEXTURE_EXOSUIT = ImmersiveIntelligence.MODID+":textures/armor/engineer_light_exosuit.png";
	private static String TEXTURE_SCUBA = ImmersiveIntelligence.MODID+":textures/armor/engineer_light_scuba.png";

	ModelRendererTurbo[] capeModel, gasmaskModel, infiltratorGogglesModel, technicianGogglesModel, engineerGogglesModel;
	ModelRendererTurbo[] scubaTankModel, exoSuitRightLegModel, exoSuitLeftLegModel, racketsModel, flippersModel;
	ModelRendererTurbo[] platesHelmetModel, platesRightArmModel, platesLeftArmModel, platesChestModel, platesRightLegModel, platesLeftLegModel, platesSkirtRightModel, platesSkirtLeftModel;

	static
	{
		modelInstance = new ModelLightEngineerArmor().subscribeToList("light_engineer_armor");
	}


	public ModelLightEngineerArmor()
	{
		super(textureX, textureY, TEXTURE);

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

		leftArmModel[2].addShapeBox(-5.5F, -2.65F, 0F, 2, 1, 2, 0F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F); // Box 29
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

		rightArmModel[1].addShapeBox(4.5F, -2.65F, 0F, 2, 1, 2, 0F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F, 0.125F); // Box 29
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
		leftLegModel[2].setRotationPoint(4F, 16.25F, -2F);
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
		rightLegModel[2].setRotationPoint(-4F, 16.25F, -2F);
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

		gasmaskModel = new ModelRendererTurbo[11];
		gasmaskModel[0] = new ModelRendererTurbo(this, 12, 0, 32, 32); // Box 0
		gasmaskModel[1] = new ModelRendererTurbo(this, 28, 10, 32, 32); // Box 1
		gasmaskModel[2] = new ModelRendererTurbo(this, 2, 21, 32, 32); // Box 2
		gasmaskModel[3] = new ModelRendererTurbo(this, 2, 21, 32, 32); // Box 3
		gasmaskModel[4] = new ModelRendererTurbo(this, 8, 27, 32, 32); // Box 4
		gasmaskModel[5] = new ModelRendererTurbo(this, 17, 20, 32, 32); // Box 5
		gasmaskModel[6] = new ModelRendererTurbo(this, 17, 20, 32, 32); // Box 6
		gasmaskModel[7] = new ModelRendererTurbo(this, 12, 23, 32, 32); // Box 7
		gasmaskModel[8] = new ModelRendererTurbo(this, 22, 7, 32, 32); // Box 8
		gasmaskModel[9] = new ModelRendererTurbo(this, 22, 16, 32, 32); // Box 9
		gasmaskModel[10] = new ModelRendererTurbo(this, 20, 21, 32, 32); // Box 10

		gasmaskModel[0].addShapeBox(0F, 0F, 0F, 9, 6, 1, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		gasmaskModel[0].setRotationPoint(-4.5F, -18F, -5F);

		gasmaskModel[1].addShapeBox(0F, 0F, 0F, 1, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 1
		gasmaskModel[1].setRotationPoint(-0.5F, -18F, -5.5F);
		gasmaskModel[1].rotateAngleX = 0.08726646F;

		gasmaskModel[2].addShapeBox(0F, 0F, 0F, 1, 6, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		gasmaskModel[2].setRotationPoint(3.5F, -18F, -4F);

		gasmaskModel[3].addShapeBox(0F, 0F, 0F, 1, 6, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 3
		gasmaskModel[3].setRotationPoint(-4.5F, -18F, -4F);

		gasmaskModel[4].addShapeBox(0F, 0F, 0F, 8, 1, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 4
		gasmaskModel[4].setRotationPoint(-4F, -12.8F, -4F);

		gasmaskModel[5].addShapeBox(0F, 0F, 0F, 2, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 5
		gasmaskModel[5].setRotationPoint(1F, -16.5F, -5.5F);

		gasmaskModel[6].addShapeBox(0F, 0F, 0F, 2, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 6
		gasmaskModel[6].setRotationPoint(-3F, -16.5F, -5.5F);

		gasmaskModel[7].addBox(0F, 0F, 0F, 3, 3, 1, 0F); // Box 7
		gasmaskModel[7].setRotationPoint(-1.5F, -14F, -6F);

		gasmaskModel[8].addShapeBox(0F, 0F, 0F, 3, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F); // Box 8
		gasmaskModel[8].setRotationPoint(-1.5F, -12F, -5F);

		gasmaskModel[9].addShapeBox(0F, 0F, 0F, 2, 2, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 9
		gasmaskModel[9].setRotationPoint(-1F, -12.2F, -8F);
		gasmaskModel[9].rotateAngleX = 0.4712389F;

		gasmaskModel[10].addShapeBox(0F, 0F, -1F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 10
		gasmaskModel[10].setRotationPoint(-1.5F, -12F, -9F);
		gasmaskModel[10].rotateAngleX = 0.54105207F;

		infiltratorGogglesModel = new ModelRendererTurbo[6];
		infiltratorGogglesModel[0] = new ModelRendererTurbo(this, 22, 12, 32, 32); // Box 0
		infiltratorGogglesModel[1] = new ModelRendererTurbo(this, 22, 12, 32, 32); // Box 1
		infiltratorGogglesModel[2] = new ModelRendererTurbo(this, 14, 20, 32, 32); // Box 2
		infiltratorGogglesModel[3] = new ModelRendererTurbo(this, 14, 22, 32, 32); // Box 3
		infiltratorGogglesModel[4] = new ModelRendererTurbo(this, 0, 8, 32, 32); // Box 6
		infiltratorGogglesModel[5] = new ModelRendererTurbo(this, 0, 8, 32, 32); // Box 7

		infiltratorGogglesModel[0].addShapeBox(0F, 0F, 0F, 3, 3, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		infiltratorGogglesModel[0].setRotationPoint(0.5F, -17.5F, -6F);

		infiltratorGogglesModel[1].addShapeBox(0F, 0F, 0F, 3, 3, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 1
		infiltratorGogglesModel[1].setRotationPoint(-3.5F, -17.5F, -6F);

		infiltratorGogglesModel[2].addShapeBox(0F, 0F, 0F, 8, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		infiltratorGogglesModel[2].setRotationPoint(-4F, -16.5F, -5F);

		infiltratorGogglesModel[3].addShapeBox(0F, 0F, 0F, 8, 9, 1, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F); // Box 3
		infiltratorGogglesModel[3].setRotationPoint(-4F, -15.5F, -4.5F);
		infiltratorGogglesModel[3].rotateAngleX = 1.43116999F;

		infiltratorGogglesModel[4].addShapeBox(0F, 0F, 0F, 2, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 6
		infiltratorGogglesModel[4].setRotationPoint(1F, -17F, -6.5F);

		infiltratorGogglesModel[5].addShapeBox(0F, 0F, 0F, 2, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 7
		infiltratorGogglesModel[5].setRotationPoint(-3F, -17F, -6.5F);


		technicianGogglesModel = new ModelRendererTurbo[6];
		technicianGogglesModel[0] = new ModelRendererTurbo(this, 4, 27, 32, 32); // Box 0
		technicianGogglesModel[1] = new ModelRendererTurbo(this, 4, 27, 32, 32); // Box 1
		technicianGogglesModel[2] = new ModelRendererTurbo(this, 14, 20, 32, 32); // Box 2
		technicianGogglesModel[3] = new ModelRendererTurbo(this, 14, 2, 32, 32); // Box 3
		technicianGogglesModel[4] = new ModelRendererTurbo(this, 8, 5, 32, 32); // Box 6
		technicianGogglesModel[5] = new ModelRendererTurbo(this, 8, 5, 32, 32); // Box 7

		technicianGogglesModel[0].addShapeBox(0F, 0F, 0F, 3, 3, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		technicianGogglesModel[0].setRotationPoint(0.5F, -17.5F, -6F);

		technicianGogglesModel[1].addShapeBox(0F, 0F, 0F, 3, 3, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 1
		technicianGogglesModel[1].setRotationPoint(-3.5F, -17.5F, -6F);

		technicianGogglesModel[2].addShapeBox(0F, 0F, 0F, 8, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		technicianGogglesModel[2].setRotationPoint(-4F, -16.5F, -5F);

		technicianGogglesModel[3].addShapeBox(0F, 0F, 0F, 8, 9, 1, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F); // Box 3
		technicianGogglesModel[3].setRotationPoint(-4F, -15.5F, -4.5F);
		technicianGogglesModel[3].rotateAngleX = 1.43116999F;

		technicianGogglesModel[4].addShapeBox(0F, 0F, 0F, 2, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 6
		technicianGogglesModel[4].setRotationPoint(1F, -17F, -6.5F);

		technicianGogglesModel[5].addShapeBox(0F, 0F, 0F, 2, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 7
		technicianGogglesModel[5].setRotationPoint(-3F, -17F, -6.5F);


		engineerGogglesModel = new ModelRendererTurbo[16];
		engineerGogglesModel[0] = new ModelRendererTurbo(this, 6, 17, 32, 32); // Box 9
		engineerGogglesModel[1] = new ModelRendererTurbo(this, 6, 17, 32, 32); // Box 10
		engineerGogglesModel[2] = new ModelRendererTurbo(this, 2, 8, 32, 32); // Box 11
		engineerGogglesModel[3] = new ModelRendererTurbo(this, 14, 17, 32, 32); // Box 13
		engineerGogglesModel[4] = new ModelRendererTurbo(this, 0, 0, 32, 32); // Box 14
		engineerGogglesModel[5] = new ModelRendererTurbo(this, 6, 2, 32, 32); // Box 15
		engineerGogglesModel[6] = new ModelRendererTurbo(this, 0, 4, 32, 32); // Box 15
		engineerGogglesModel[7] = new ModelRendererTurbo(this, 6, 2, 32, 32); // Box 15
		engineerGogglesModel[8] = new ModelRendererTurbo(this, 2, 21, 32, 32); // Box 0
		engineerGogglesModel[9] = new ModelRendererTurbo(this, 2, 21, 32, 32); // Box 1
		engineerGogglesModel[10] = new ModelRendererTurbo(this, 0, 17, 32, 32); // Box 6
		engineerGogglesModel[11] = new ModelRendererTurbo(this, 0, 17, 32, 32); // Box 7
		engineerGogglesModel[12] = new ModelRendererTurbo(this, 14, 20, 32, 32); // Box 2
		engineerGogglesModel[13] = new ModelRendererTurbo(this, 14, 22, 32, 32); // Box 3
		engineerGogglesModel[14] = new ModelRendererTurbo(this, 14, 20, 32, 32); // Box 2
		engineerGogglesModel[15] = new ModelRendererTurbo(this, 14, 22, 32, 32); // Box 3

		engineerGogglesModel[0].addShapeBox(0F, 0F, 0F, 2, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 9
		engineerGogglesModel[0].setRotationPoint(1.5F, -22F, -5F);
		engineerGogglesModel[0].rotateAngleX = -0.54105207F;

		engineerGogglesModel[1].addShapeBox(0F, 0F, 0F, 2, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 10
		engineerGogglesModel[1].setRotationPoint(-3.5F, -22F, -5F);
		engineerGogglesModel[1].rotateAngleX = -0.54105207F;

		engineerGogglesModel[2].addShapeBox(0F, 0F, 0F, 4, 5, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 11
		engineerGogglesModel[2].setRotationPoint(-2F, -21F, -6F);
		engineerGogglesModel[2].rotateAngleX = 1.57079633F;

		engineerGogglesModel[3].addShapeBox(0F, 0F, 0F, 7, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 13
		engineerGogglesModel[3].setRotationPoint(-3.5F, -22.5F, -5F);

		engineerGogglesModel[4].addShapeBox(0F, 0F, 0F, 3, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 14
		engineerGogglesModel[4].setRotationPoint(-1.5F, -24.5F, -7F);

		engineerGogglesModel[5].addBox(0F, 0F, 0F, 2, 1, 2, 0F); // Box 15
		engineerGogglesModel[5].setRotationPoint(-1F, -22F, -1F);
		engineerGogglesModel[5].rotateAngleX = 1.57079633F;

		engineerGogglesModel[6].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F); // Box 15
		engineerGogglesModel[6].setRotationPoint(-1F, -22F, 0F);
		engineerGogglesModel[6].rotateAngleX = 1.57079633F;

		engineerGogglesModel[7].addBox(0F, 0F, 0F, 2, 1, 2, 0F); // Box 15
		engineerGogglesModel[7].setRotationPoint(-1F, -22F, 0F);

		engineerGogglesModel[8].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		engineerGogglesModel[8].setRotationPoint(0.5F, -17.5F, -7.5F);

		engineerGogglesModel[9].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 1
		engineerGogglesModel[9].setRotationPoint(-3.5F, -17.5F, -7.5F);

		engineerGogglesModel[10].addShapeBox(0F, 0F, 0F, 2, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 6
		engineerGogglesModel[10].setRotationPoint(1F, -17F, -8F);

		engineerGogglesModel[11].addShapeBox(0F, 0F, 0F, 2, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 7
		engineerGogglesModel[11].setRotationPoint(-3F, -17F, -8F);

		engineerGogglesModel[12].addShapeBox(0F, 0F, 0F, 8, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		engineerGogglesModel[12].setRotationPoint(-4F, -17.55F, -5F);

		engineerGogglesModel[13].addShapeBox(0F, 0F, 0F, 8, 9, 1, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F); // Box 3
		engineerGogglesModel[13].setRotationPoint(-4F, -16F, -4.5F);
		engineerGogglesModel[13].rotateAngleX = 1.43116999F;

		engineerGogglesModel[14].addShapeBox(0F, 0F, 0F, 8, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		engineerGogglesModel[14].setRotationPoint(-4F, -15.45F, -5F);

		engineerGogglesModel[15].addShapeBox(0F, 0F, -1F, 8, 9, 1, 0F, 0.05F, -9F, -1F, 0.05F, -9F, -1F, 0.05F, -9F, -1F, 0.05F, -9F, -1F, 0.05F, -9F, -1F, 0.05F, -9F, -1F, 0.05F, -9F, -1F, 0.05F, -9F, -1F); // Box 3
		engineerGogglesModel[15].setRotationPoint(-4F, -16F, -4.5F);
		engineerGogglesModel[15].rotateAngleX = 1.43116999F;

		platesHelmetModel = new ModelRendererTurbo[9];
		platesHelmetModel[0] = new ModelRendererTurbo(this, 16, 27, 32, 32); // Leather03
		platesHelmetModel[1] = new ModelRendererTurbo(this, 14, 0, 32, 32); // Metal03
		platesHelmetModel[2] = new ModelRendererTurbo(this, 14, 0, 32, 32); // Metal04
		platesHelmetModel[3] = new ModelRendererTurbo(this, 16, 27, 32, 32); // Leather03
		platesHelmetModel[4] = new ModelRendererTurbo(this, 14, 0, 32, 32); // Metal03
		platesHelmetModel[5] = new ModelRendererTurbo(this, 14, 0, 32, 32); // Metal04
		platesHelmetModel[6] = new ModelRendererTurbo(this, 16, 27, 32, 32); // Leather03
		platesHelmetModel[7] = new ModelRendererTurbo(this, 14, 0, 32, 32); // Metal03
		platesHelmetModel[8] = new ModelRendererTurbo(this, 14, 0, 32, 32); // Metal04

		platesHelmetModel[0].addShapeBox(0F, 0.5F, 0F, 6, 4, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Leather03
		platesHelmetModel[0].setRotationPoint(-3F, -20F, 4.3F);
		platesHelmetModel[0].rotateAngleX = 0.08726646F;

		platesHelmetModel[1].addShapeBox(4F, 0F, 0F, 3, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Metal03
		platesHelmetModel[1].setRotationPoint(-3.5F, -20F, 4.7F);
		platesHelmetModel[1].rotateAngleX = 0.08726646F;

		platesHelmetModel[2].addShapeBox(0F, 0F, 0F, 3, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Metal04
		platesHelmetModel[2].setRotationPoint(-3.5F, -20F, 4.7F);
		platesHelmetModel[2].rotateAngleX = 0.08726646F;

		platesHelmetModel[3].addShapeBox(0F, 0.5F, 0F, 6, 4, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Leather03
		platesHelmetModel[3].setRotationPoint(-5.5F, -20F, 3.1F);
		platesHelmetModel[3].rotateAngleX = -0.08726646F;
		platesHelmetModel[3].rotateAngleY = -1.57079633F;

		platesHelmetModel[4].addShapeBox(4F, 0F, 0F, 3, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Metal03
		platesHelmetModel[4].setRotationPoint(-6F, -20F, 3.5F);
		platesHelmetModel[4].rotateAngleX = -0.08726646F;
		platesHelmetModel[4].rotateAngleY = -1.57079633F;

		platesHelmetModel[5].addShapeBox(0F, 0F, 0F, 3, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Metal04
		platesHelmetModel[5].setRotationPoint(-6F, -20F, 3.5F);
		platesHelmetModel[5].rotateAngleX = -0.08726646F;
		platesHelmetModel[5].rotateAngleY = -1.57079633F;

		platesHelmetModel[6].addShapeBox(0F, 0.5F, 0F, 6, 4, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Leather03
		platesHelmetModel[6].setRotationPoint(4.5F, -20F, 3.1F);
		platesHelmetModel[6].rotateAngleX = 0.08726646F;
		platesHelmetModel[6].rotateAngleY = -1.57079633F;

		platesHelmetModel[7].addShapeBox(4F, 0F, 0F, 3, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Metal03
		platesHelmetModel[7].setRotationPoint(5F, -20F, 3.5F);
		platesHelmetModel[7].rotateAngleX = 0.08726646F;
		platesHelmetModel[7].rotateAngleY = -1.57079633F;

		platesHelmetModel[8].addShapeBox(0F, 0F, 0F, 3, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Metal04
		platesHelmetModel[8].setRotationPoint(5F, -20F, 3.5F);
		platesHelmetModel[8].rotateAngleX = 0.08726646F;
		platesHelmetModel[8].rotateAngleY = -1.57079633F;

		platesRightArmModel = new ModelRendererTurbo[5];
		platesRightArmModel[0] = new ModelRendererTurbo(this, 0, 24, 32, 32); // RightArm01
		platesRightArmModel[1] = new ModelRendererTurbo(this, 0, 12, 32, 32); // RightArm02
		platesRightArmModel[2] = new ModelRendererTurbo(this, 22, 0, 32, 32); // RightArm03
		platesRightArmModel[3] = new ModelRendererTurbo(this, 22, 0, 32, 32); // RightArm04
		platesRightArmModel[4] = new ModelRendererTurbo(this, 22, 0, 32, 32); // RightArm05

		platesRightArmModel[0].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, -4.2F, 0.2F, 0.2F, -4.2F, 0.2F, 0.2F, -4.2F, 0.2F, 0.2F, -4.2F, 0.2F, 0.2F, -4.2F, 0.2F, 0.2F, -4.2F, 0.2F, 0.2F, -4.2F, 0.2F, 0.2F, -4.2F, 0.2F, 0.2F); // RightArm01
		platesRightArmModel[0].setRotationPoint(-8F, -4F, -2F);

		platesRightArmModel[1].addShapeBox(0F, 0F, 0F, 5, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // RightArm02
		platesRightArmModel[1].setRotationPoint(-9.5F, -8F, 2.5F);
		platesRightArmModel[1].rotateAngleX = -0.06981317F;
		platesRightArmModel[1].rotateAngleY = -1.57079633F;

		platesRightArmModel[2].addShapeBox(0F, 0F, 0F, 4, 4, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // RightArm03
		platesRightArmModel[2].setRotationPoint(-8F, -7F, 2F);

		platesRightArmModel[3].addShapeBox(0F, 0F, 0F, 4, 4, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // RightArm04
		platesRightArmModel[3].setRotationPoint(-8F, -12F, 2F);
		platesRightArmModel[3].rotateAngleX = 0.08726646F;

		platesRightArmModel[4].addShapeBox(0F, 0F, 0F, 4, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // RightArm05
		platesRightArmModel[4].setRotationPoint(-8F, -12F, -3F);
		platesRightArmModel[4].rotateAngleX = -0.08726646F;


		platesLeftArmModel = new ModelRendererTurbo[5];
		platesLeftArmModel[0] = new ModelRendererTurbo(this, 0, 24, 32, 32); // LeftArm01
		platesLeftArmModel[1] = new ModelRendererTurbo(this, 0, 12, 32, 32); // LeftArm02
		platesLeftArmModel[2] = new ModelRendererTurbo(this, 22, 0, 32, 32); // LeftArm03
		platesLeftArmModel[3] = new ModelRendererTurbo(this, 22, 0, 32, 32); // LeftArm04
		platesLeftArmModel[4] = new ModelRendererTurbo(this, 22, 0, 32, 32); // LeftArm05

		platesLeftArmModel[0].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F); // LeftArm01
		platesLeftArmModel[0].setRotationPoint(4F, -4F, -2F);

		platesLeftArmModel[1].addShapeBox(0F, 0F, 0F, 5, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // LeftArm02
		platesLeftArmModel[1].setRotationPoint(8.5F, -8F, 2.5F);
		platesLeftArmModel[1].rotateAngleX = 0.06981317F;
		platesLeftArmModel[1].rotateAngleY = -1.57079633F;

		platesLeftArmModel[2].addShapeBox(0F, 0F, 0F, 4, 4, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // LeftArm03
		platesLeftArmModel[2].setRotationPoint(4F, -7F, 2F);

		platesLeftArmModel[3].addShapeBox(0F, 0F, 0F, 4, 4, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // LeftArm04
		platesLeftArmModel[3].setRotationPoint(4F, -12F, 2F);
		platesLeftArmModel[3].rotateAngleX = 0.08726646F;

		platesLeftArmModel[4].addShapeBox(0F, 0F, 0F, 4, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // LeftArm05
		platesLeftArmModel[4].setRotationPoint(4F, -12F, -3F);
		platesLeftArmModel[4].rotateAngleX = -0.08726646F;


		platesChestModel = new ModelRendererTurbo[13];
		platesChestModel[0] = new ModelRendererTurbo(this, 14, 0, 32, 32); // Chest01
		platesChestModel[1] = new ModelRendererTurbo(this, 14, 0, 32, 32); // Chest02
		platesChestModel[2] = new ModelRendererTurbo(this, 14, 0, 32, 32); // Chest03
		platesChestModel[3] = new ModelRendererTurbo(this, 0, 7, 32, 32); // Chest04
		platesChestModel[4] = new ModelRendererTurbo(this, 26, 11, 32, 32); // Chest06
		platesChestModel[5] = new ModelRendererTurbo(this, 12, 23, 32, 32); // Chest08
		platesChestModel[6] = new ModelRendererTurbo(this, 16, 7, 32, 32); // Chest09
		platesChestModel[7] = new ModelRendererTurbo(this, 16, 7, 32, 32); // Chest10
		platesChestModel[8] = new ModelRendererTurbo(this, 16, 11, 32, 32); // Chest11
		platesChestModel[9] = new ModelRendererTurbo(this, 16, 27, 32, 32); // Chest12
		platesChestModel[10] = new ModelRendererTurbo(this, 4, 21, 32, 32); // Chest13
		platesChestModel[11] = new ModelRendererTurbo(this, 26, 11, 32, 32); // Chest06
		platesChestModel[12] = new ModelRendererTurbo(this, 26, 11, 32, 32); // Chest06

		platesChestModel[0].addShapeBox(0F, 0F, 0F, 3, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Chest01
		platesChestModel[0].setRotationPoint(-1.5F, -6F, -4F);

		platesChestModel[1].addShapeBox(0F, 0F, 0F, 3, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Chest02
		platesChestModel[1].setRotationPoint(-4.5F, -6F, -3F);
		platesChestModel[1].rotateAngleX = -0.01745329F;
		platesChestModel[1].rotateAngleY = -0.45378561F;

		platesChestModel[2].addShapeBox(0F, 0F, 0F, 3, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Chest03
		platesChestModel[2].setRotationPoint(1.9F, -6F, -4.5F);
		platesChestModel[2].rotateAngleY = 0.45378561F;

		platesChestModel[3].addShapeBox(0F, 0F, 0F, 7, 4, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Chest04
		platesChestModel[3].setRotationPoint(-3.5F, -10.9F, -4.5F);

		platesChestModel[4].addShapeBox(0F, 0F, 0F, 2, 7, 1, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, 2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, 2F, 0F, 0F, 0F); // Chest06
		platesChestModel[4].setRotationPoint(-2.5F, -11F, -3.5F);
		platesChestModel[4].rotateAngleY = 1.57079633F;
		platesChestModel[4].rotateAngleZ = 1.57079633F;

		platesChestModel[5].addShapeBox(0F, 0F, 0F, 9, 3, 1, 0F, 1F, -1F, 0F, 1F, -1F, 0F, 1F, -1F, 0F, 1F, -1F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F); // Chest08
		platesChestModel[5].setRotationPoint(-4.5F, -14F, 3.5F);

		platesChestModel[6].addShapeBox(0F, 0F, 0F, 7, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Chest09
		platesChestModel[6].setRotationPoint(-3.5F, -11F, 3F);

		platesChestModel[7].addShapeBox(0F, 0F, 0F, 7, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Chest10
		platesChestModel[7].setRotationPoint(-3.5F, -7.5F, 3F);

		platesChestModel[8].addShapeBox(0F, 0F, 0F, 4, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Chest11
		platesChestModel[8].setRotationPoint(-2F, -4F, 3F);

		platesChestModel[9].addShapeBox(0F, 0F, 0F, 6, 4, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Chest12
		platesChestModel[9].setRotationPoint(-3F, -9F, 2.5F);

		platesChestModel[10].addShapeBox(0F, 0F, 0F, 3, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Chest13
		platesChestModel[10].setRotationPoint(-1.5F, -5F, 2.5F);

		platesChestModel[11].addShapeBox(0F, 0F, 0F, 2, 7, 1, 0F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 2F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 2F); // Chest06
		platesChestModel[11].setRotationPoint(5.5F, -11F, -3.5F);
		platesChestModel[11].rotateAngleY = 1.57079633F;
		platesChestModel[11].rotateAngleZ = 1.57079633F;

		platesChestModel[12].addShapeBox(0F, 0F, 0F, 2, 7, 1, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 1F, 0F, 0F, 0F); // Chest06
		platesChestModel[12].setRotationPoint(3.5F, -11F, -3.5F);
		platesChestModel[12].rotateAngleY = 3.14159265F;
		platesChestModel[12].rotateAngleZ = 1.57079633F;


		platesRightLegModel = new ModelRendererTurbo[7];
		platesRightLegModel[0] = new ModelRendererTurbo(this, 12, 21, 32, 32); // LegRight01
		platesRightLegModel[1] = new ModelRendererTurbo(this, 0, 19, 32, 32); // LegRight02
		platesRightLegModel[2] = new ModelRendererTurbo(this, 0, 19, 32, 32); // LegRight03
		platesRightLegModel[3] = new ModelRendererTurbo(this, 12, 21, 32, 32); // LegRight04
		platesRightLegModel[4] = new ModelRendererTurbo(this, 12, 15, 32, 32); // LegRight05
		platesRightLegModel[5] = new ModelRendererTurbo(this, 20, 19, 32, 32); // LegRight06
		platesRightLegModel[6] = new ModelRendererTurbo(this, 22, 0, 32, 32); // LegRight07

		platesRightLegModel[0].addShapeBox(0F, 0F, 0F, 3, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // LegRight01
		platesRightLegModel[0].setRotationPoint(-3.5F, 1F, -3.5F);

		platesRightLegModel[1].addShapeBox(0F, 0F, 0F, 3, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // LegRight02
		platesRightLegModel[1].setRotationPoint(-3.5F, 2.5F, -3.5F);

		platesRightLegModel[2].addShapeBox(0F, 0F, 0F, 3, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // LegRight03
		platesRightLegModel[2].setRotationPoint(-3.5F, 6.5F, -3.5F);

		platesRightLegModel[3].addShapeBox(0F, 0F, 0F, 3, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // LegRight04
		platesRightLegModel[3].setRotationPoint(-3.5F, 5F, -3.5F);

		platesRightLegModel[4].addShapeBox(0F, 0F, 0F, 3, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // LegRight05
		platesRightLegModel[4].setRotationPoint(-4.5F, 0F, 1.5F);
		platesRightLegModel[4].rotateAngleY = -1.57079633F;

		platesRightLegModel[5].addShapeBox(0F, 0F, 0F, 4, 2, 2, 0F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F); // LegRight06
		platesRightLegModel[5].setRotationPoint(-4F, 8F, -4F);

		platesRightLegModel[6].addShapeBox(0F, 0F, 0F, 4, 6, 1, 0F, 0.2F, 0F, 0.2F, 0.2F, 0F, 0.2F, 0.2F, 0F, 0.2F, 0.2F, 0F, 0.2F, 0.2F, 0F, 0.2F, 0.2F, 0F, 0.2F, 0.2F, 0F, 0.2F, 0.2F, 0F, 0.2F); // LegRight07
		platesRightLegModel[6].setRotationPoint(-4F, 7F, 2F);


		platesLeftLegModel = new ModelRendererTurbo[7];
		platesLeftLegModel[0] = new ModelRendererTurbo(this, 12, 21, 32, 32); // LegLeft01
		platesLeftLegModel[1] = new ModelRendererTurbo(this, 0, 19, 32, 32); // LegLeft02
		platesLeftLegModel[2] = new ModelRendererTurbo(this, 0, 19, 32, 32); // LegLeft03
		platesLeftLegModel[3] = new ModelRendererTurbo(this, 12, 21, 32, 32); // LegLeft04
		platesLeftLegModel[4] = new ModelRendererTurbo(this, 12, 15, 32, 32); // LegLeft05
		platesLeftLegModel[5] = new ModelRendererTurbo(this, 20, 19, 32, 32); // LegLeft06
		platesLeftLegModel[6] = new ModelRendererTurbo(this, 22, 0, 32, 32); // LegLeft07

		platesLeftLegModel[0].addShapeBox(0F, 0F, 0F, 3, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // LegLeft01
		platesLeftLegModel[0].setRotationPoint(0.5F, 1F, -3.5F);

		platesLeftLegModel[1].addShapeBox(0F, 0F, 0F, 3, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // LegLeft02
		platesLeftLegModel[1].setRotationPoint(0.5F, 2.5F, -3.5F);

		platesLeftLegModel[2].addShapeBox(0F, 0F, 0F, 3, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // LegLeft03
		platesLeftLegModel[2].setRotationPoint(0.5F, 6.5F, -3.5F);

		platesLeftLegModel[3].addShapeBox(0F, 0F, 0F, 3, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // LegLeft04
		platesLeftLegModel[3].setRotationPoint(0.5F, 5F, -3.5F);

		platesLeftLegModel[4].addShapeBox(0F, 0F, 0F, 3, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // LegLeft05
		platesLeftLegModel[4].setRotationPoint(3.5F, 0F, 1.5F);
		platesLeftLegModel[4].rotateAngleY = -1.57079633F;

		platesLeftLegModel[5].addShapeBox(0F, 0F, 0F, 4, 2, 2, 0F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F, 0.2F); // LegLeft06
		platesLeftLegModel[5].setRotationPoint(0F, 8F, -4F);

		platesLeftLegModel[6].addShapeBox(0F, 0F, 0F, 4, 6, 1, 0F, 0.2F, 0F, 0.2F, 0.2F, 0F, 0.2F, 0.2F, 0F, 0.2F, 0.2F, 0F, 0.2F, 0.2F, 0F, 0.2F, 0.2F, 0F, 0.2F, 0.2F, 0F, 0.2F, 0.2F, 0F, 0.2F); // LegLeft07
		platesLeftLegModel[6].setRotationPoint(0F, 7F, 2F);


		platesSkirtRightModel = new ModelRendererTurbo[5];
		platesSkirtRightModel[0] = new ModelRendererTurbo(this, 14, 0, 32, 32); // SkirtRight01
		platesSkirtRightModel[1] = new ModelRendererTurbo(this, 0, 21, 32, 32); // SkirtRight02
		platesSkirtRightModel[2] = new ModelRendererTurbo(this, 0, 0, 32, 32); // SkirtRight03
		platesSkirtRightModel[3] = new ModelRendererTurbo(this, 14, 0, 32, 32); // SkirtRight04
		platesSkirtRightModel[4] = new ModelRendererTurbo(this, 0, 21, 32, 32); // SkirtRight05

		platesSkirtRightModel[0].addShapeBox(0F, 0F, 0F, 3, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // SkirtRight01
		platesSkirtRightModel[0].setRotationPoint(-3F, -1F, -3.5F);
		platesSkirtRightModel[0].rotateAngleX = -0.12217305F;

		platesSkirtRightModel[1].addShapeBox(0F, 0F, 0F, 1, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F); // SkirtRight02
		platesSkirtRightModel[1].setRotationPoint(-4F, -1F, -3.5F);
		platesSkirtRightModel[1].rotateAngleX = -0.12217305F;

		platesSkirtRightModel[2].addShapeBox(0F, 0F, 0F, 6, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // SkirtRight03
		platesSkirtRightModel[2].setRotationPoint(-4.5F, -1F, 3F);
		platesSkirtRightModel[2].rotateAngleX = -0.19198622F;
		platesSkirtRightModel[2].rotateAngleY = -1.57079633F;

		platesSkirtRightModel[3].addShapeBox(0F, 0F, 0F, 3, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // SkirtRight04
		platesSkirtRightModel[3].setRotationPoint(-3F, -1F, 1.5F);
		platesSkirtRightModel[3].rotateAngleX = 0.27925268F;

		platesSkirtRightModel[4].addShapeBox(0F, 0F, 0F, 1, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F); // SkirtRight05
		platesSkirtRightModel[4].setRotationPoint(-4F, -1F, 1.5F);
		platesSkirtRightModel[4].rotateAngleX = 0.27925268F;


		platesSkirtLeftModel = new ModelRendererTurbo[5];
		platesSkirtLeftModel[0] = new ModelRendererTurbo(this, 14, 0, 32, 32); // SkirtLeft01
		platesSkirtLeftModel[1] = new ModelRendererTurbo(this, 0, 21, 32, 32); // SkirtLeft02
		platesSkirtLeftModel[2] = new ModelRendererTurbo(this, 0, 0, 32, 32); // SkirtLeft03
		platesSkirtLeftModel[3] = new ModelRendererTurbo(this, 14, 0, 32, 32); // SkirtLeft04
		platesSkirtLeftModel[4] = new ModelRendererTurbo(this, 0, 21, 32, 32); // SkirtLeft05

		platesSkirtLeftModel[0].addShapeBox(0F, 0F, 0F, 3, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // SkirtLeft01
		platesSkirtLeftModel[0].setRotationPoint(0F, -1F, -3.5F);
		platesSkirtLeftModel[0].rotateAngleX = -0.12217305F;

		platesSkirtLeftModel[1].addShapeBox(0F, 0F, 0F, 1, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F); // SkirtLeft02
		platesSkirtLeftModel[1].setRotationPoint(3F, -1F, -3.5F);
		platesSkirtLeftModel[1].rotateAngleX = -0.12217305F;

		platesSkirtLeftModel[2].addShapeBox(0F, 0F, 0F, 6, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // SkirtLeft03
		platesSkirtLeftModel[2].setRotationPoint(3.5F, -1F, 3F);
		platesSkirtLeftModel[2].rotateAngleX = 0.19198622F;
		platesSkirtLeftModel[2].rotateAngleY = -1.57079633F;

		platesSkirtLeftModel[3].addShapeBox(0F, 0F, 0F, 3, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // SkirtLeft04
		platesSkirtLeftModel[3].setRotationPoint(0F, -1F, 1.5F);
		platesSkirtLeftModel[3].rotateAngleX = 0.27925268F;

		platesSkirtLeftModel[4].addShapeBox(0F, 0F, 0F, 1, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F); // SkirtLeft05
		platesSkirtLeftModel[4].setRotationPoint(3F, -1F, 1.5F);
		platesSkirtLeftModel[4].rotateAngleX = 0.27925268F;

		exoSuitRightLegModel = new ModelRendererTurbo[21];
		exoSuitRightLegModel[0] = new ModelRendererTurbo(this, 0, 0, 16, 16); // FrameRight01
		exoSuitRightLegModel[1] = new ModelRendererTurbo(this, 0, 5, 16, 16); // FrameRight02
		exoSuitRightLegModel[2] = new ModelRendererTurbo(this, 0, 5, 16, 16); // FrameRight03
		exoSuitRightLegModel[3] = new ModelRendererTurbo(this, 0, 0, 16, 16); // FrameRight04
		exoSuitRightLegModel[4] = new ModelRendererTurbo(this, 8, 2, 16, 16); // FrameRight05
		exoSuitRightLegModel[5] = new ModelRendererTurbo(this, 3, -2, 16, 16); // FrameRight06
		exoSuitRightLegModel[6] = new ModelRendererTurbo(this, 3, -2, 16, 16); // FrameRight07
		exoSuitRightLegModel[7] = new ModelRendererTurbo(this, 8, 2, 16, 16); // FrameRight08
		exoSuitRightLegModel[8] = new ModelRendererTurbo(this, 6, 10, 16, 16); // FrameRight09
		exoSuitRightLegModel[9] = new ModelRendererTurbo(this, 6, 10, 16, 16); // FrameRight10
		exoSuitRightLegModel[10] = new ModelRendererTurbo(this, 4, 1, 16, 16); // FrameRight11
		exoSuitRightLegModel[11] = new ModelRendererTurbo(this, 4, 1, 16, 16); // FrameRight12
		exoSuitRightLegModel[12] = new ModelRendererTurbo(this, 0, 11, 16, 16); // WiresRight01
		exoSuitRightLegModel[13] = new ModelRendererTurbo(this, 0, 13, 16, 16); // WiresRight02
		exoSuitRightLegModel[14] = new ModelRendererTurbo(this, 4, 4, 16, 16); // WiresRight03
		exoSuitRightLegModel[15] = new ModelRendererTurbo(this, 4, 7, 16, 16); // WiresRight04
		exoSuitRightLegModel[16] = new ModelRendererTurbo(this, 12, 0, 16, 16); // WiresRight05
		exoSuitRightLegModel[17] = new ModelRendererTurbo(this, 4, 13, 16, 16); // FrameRight14
		exoSuitRightLegModel[18] = new ModelRendererTurbo(this, 8, 5, 16, 16); // FrameRight15
		exoSuitRightLegModel[19] = new ModelRendererTurbo(this, 4, 13, 16, 16); // Box 50
		exoSuitRightLegModel[20] = new ModelRendererTurbo(this, 4, 4, 16, 16); // WiresRight01

		exoSuitRightLegModel[0].addShapeBox(0F, 0F, 0F, 1, 4, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // FrameRight01
		exoSuitRightLegModel[0].setRotationPoint(-5F, 0F, -2F);

		exoSuitRightLegModel[1].addShapeBox(0F, 0F, 0F, 1, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // FrameRight02
		exoSuitRightLegModel[1].setRotationPoint(-5F, 4F, -2F);

		exoSuitRightLegModel[2].addShapeBox(0F, 0F, 0F, 1, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // FrameRight03
		exoSuitRightLegModel[2].setRotationPoint(-5F, 4F, 1F);

		exoSuitRightLegModel[3].addShapeBox(0F, 0F, 0F, 1, 4, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // FrameRight04
		exoSuitRightLegModel[3].setRotationPoint(-5F, 0F, 1F);

		exoSuitRightLegModel[4].addShapeBox(0F, 0F, 0F, 1, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // FrameRight05
		exoSuitRightLegModel[4].setRotationPoint(-5F, 8F, -1F);

		exoSuitRightLegModel[5].addShapeBox(0F, 0F, 0F, 0, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // FrameRight06
		exoSuitRightLegModel[5].setRotationPoint(-5F, 4F, -1F);

		exoSuitRightLegModel[6].addShapeBox(0F, 0F, 0F, 0, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // FrameRight07
		exoSuitRightLegModel[6].setRotationPoint(-5F, 2.5F, -1F);

		exoSuitRightLegModel[7].addShapeBox(0F, 0F, 0F, 1, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // FrameRight08
		exoSuitRightLegModel[7].setRotationPoint(-5F, 0F, -1F);

		exoSuitRightLegModel[8].addShapeBox(0F, 0F, 0F, 4, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // FrameRight09
		exoSuitRightLegModel[8].setRotationPoint(-4.5F, 6.5F, 1.5F);

		exoSuitRightLegModel[9].addShapeBox(0F, 0F, 0F, 4, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // FrameRight10
		exoSuitRightLegModel[9].setRotationPoint(-4.5F, 1F, 1.5F);

		exoSuitRightLegModel[10].addShapeBox(0F, 0F, 0F, 1, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // FrameRight11
		exoSuitRightLegModel[10].setRotationPoint(-4.5F, 0.5F, -2.5F);

		exoSuitRightLegModel[11].addShapeBox(0F, 0F, 0F, 1, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // FrameRight12
		exoSuitRightLegModel[11].setRotationPoint(-4.5F, 6.5F, -2.5F);

		exoSuitRightLegModel[12].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // WiresRight01
		exoSuitRightLegModel[12].setRotationPoint(-4.5F, 6F, -1F);

		exoSuitRightLegModel[13].addShapeBox(0F, 0F, 0F, 1, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // WiresRight02
		exoSuitRightLegModel[13].setRotationPoint(-4.5F, 3F, 0F);

		exoSuitRightLegModel[14].addShapeBox(0F, 0F, 0F, 1, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // WiresRight03
		exoSuitRightLegModel[14].setRotationPoint(-4.5F, 0F, -1F);

		exoSuitRightLegModel[15].addShapeBox(0F, 0F, 0F, 1, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F); // WiresRight04
		exoSuitRightLegModel[15].setRotationPoint(-4.5F, 5F, 0F);

		exoSuitRightLegModel[16].addShapeBox(0F, 0F, 0F, 1, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // WiresRight05
		exoSuitRightLegModel[16].setRotationPoint(-4.5F, 2F, -1F);

		exoSuitRightLegModel[17].addShapeBox(0F, 0F, 0F, 5, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // FrameRight14
		exoSuitRightLegModel[17].setRotationPoint(-5.05F, -1.5F, -2.5F);

		exoSuitRightLegModel[18].addShapeBox(0F, 0F, 0F, 1, 2, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // FrameRight15
		exoSuitRightLegModel[18].setRotationPoint(-4.5F, -1.5F, -1.5F);

		exoSuitRightLegModel[19].addShapeBox(0F, 0F, 0F, 5, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 50
		exoSuitRightLegModel[19].setRotationPoint(-5.05F, -1.5F, 1.5F);

		exoSuitRightLegModel[20].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // WiresRight01
		exoSuitRightLegModel[20].setRotationPoint(-4.5F, 7F, -1F);


		exoSuitLeftLegModel = new ModelRendererTurbo[21];
		exoSuitLeftLegModel[0] = new ModelRendererTurbo(this, 0, 0, 16, 16); // FrameLeft01
		exoSuitLeftLegModel[1] = new ModelRendererTurbo(this, 0, 5, 16, 16); // FrameLeft02
		exoSuitLeftLegModel[2] = new ModelRendererTurbo(this, 0, 5, 16, 16); // FrameLeft03
		exoSuitLeftLegModel[3] = new ModelRendererTurbo(this, 0, 0, 16, 16); // FrameLeft04
		exoSuitLeftLegModel[4] = new ModelRendererTurbo(this, 8, 2, 16, 16); // FrameLeft05
		exoSuitLeftLegModel[5] = new ModelRendererTurbo(this, 3, -2, 16, 16); // FrameLeft06
		exoSuitLeftLegModel[6] = new ModelRendererTurbo(this, 3, -2, 16, 16); // FrameLeft07
		exoSuitLeftLegModel[7] = new ModelRendererTurbo(this, 8, 2, 16, 16); // FrameLeft08
		exoSuitLeftLegModel[8] = new ModelRendererTurbo(this, 6, 10, 16, 16); // FrameLeft09
		exoSuitLeftLegModel[9] = new ModelRendererTurbo(this, 6, 10, 16, 16); // FrameLeft10
		exoSuitLeftLegModel[10] = new ModelRendererTurbo(this, 4, 1, 16, 16); // FrameLeft11
		exoSuitLeftLegModel[11] = new ModelRendererTurbo(this, 4, 1, 16, 16); // FrameLeft12
		exoSuitLeftLegModel[12] = new ModelRendererTurbo(this, 4, 13, 16, 16); // FrameLeft14
		exoSuitLeftLegModel[13] = new ModelRendererTurbo(this, 8, 5, 16, 16); // FrameLeft15
		exoSuitLeftLegModel[14] = new ModelRendererTurbo(this, 0, 11, 16, 16); // WiresLeft01
		exoSuitLeftLegModel[15] = new ModelRendererTurbo(this, 0, 13, 16, 16); // WiresLeft02
		exoSuitLeftLegModel[16] = new ModelRendererTurbo(this, 4, 4, 16, 16); // WiresLeft03
		exoSuitLeftLegModel[17] = new ModelRendererTurbo(this, 4, 7, 16, 16); // WiresLeft04
		exoSuitLeftLegModel[18] = new ModelRendererTurbo(this, 12, 0, 16, 16); // WiresLeft05
		exoSuitLeftLegModel[19] = new ModelRendererTurbo(this, 4, 13, 16, 16); // Box 49
		exoSuitLeftLegModel[20] = new ModelRendererTurbo(this, 4, 4, 16, 16); // WiresLeft01

		exoSuitLeftLegModel[0].addShapeBox(0F, 0F, 0F, 1, 4, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // FrameLeft01
		exoSuitLeftLegModel[0].setRotationPoint(4F, 0F, -2F);

		exoSuitLeftLegModel[1].addShapeBox(0F, 0F, 0F, 1, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // FrameLeft02
		exoSuitLeftLegModel[1].setRotationPoint(4F, 4F, -2F);

		exoSuitLeftLegModel[2].addShapeBox(0F, 0F, 0F, 1, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // FrameLeft03
		exoSuitLeftLegModel[2].setRotationPoint(4F, 4F, 1F);

		exoSuitLeftLegModel[3].addShapeBox(0F, 0F, 0F, 1, 4, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // FrameLeft04
		exoSuitLeftLegModel[3].setRotationPoint(4F, 0F, 1F);

		exoSuitLeftLegModel[4].addShapeBox(0F, 0F, 0F, 1, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // FrameLeft05
		exoSuitLeftLegModel[4].setRotationPoint(4F, 8F, -1F);

		exoSuitLeftLegModel[5].addShapeBox(0F, 0F, 0F, 0, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // FrameLeft06
		exoSuitLeftLegModel[5].setRotationPoint(5F, 4F, -1F);

		exoSuitLeftLegModel[6].addShapeBox(0F, 0F, 0F, 0, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // FrameLeft07
		exoSuitLeftLegModel[6].setRotationPoint(5F, 2.5F, -1F);

		exoSuitLeftLegModel[7].addShapeBox(0F, 0F, 0F, 1, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // FrameLeft08
		exoSuitLeftLegModel[7].setRotationPoint(4F, 0F, -1F);

		exoSuitLeftLegModel[8].addShapeBox(0F, 0F, 0F, 4, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // FrameLeft09
		exoSuitLeftLegModel[8].setRotationPoint(0.5F, 6.5F, 1.5F);

		exoSuitLeftLegModel[9].addShapeBox(0F, 0F, 0F, 4, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // FrameLeft10
		exoSuitLeftLegModel[9].setRotationPoint(0.5F, 1F, 1.5F);

		exoSuitLeftLegModel[10].addShapeBox(0F, 0F, 0F, 1, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // FrameLeft11
		exoSuitLeftLegModel[10].setRotationPoint(3.5F, 0.5F, -2.5F);

		exoSuitLeftLegModel[11].addShapeBox(0F, 0F, 0F, 1, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // FrameLeft12
		exoSuitLeftLegModel[11].setRotationPoint(3.5F, 6.5F, -2.5F);

		exoSuitLeftLegModel[12].addShapeBox(0F, 0F, 0F, 5, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // FrameLeft14
		exoSuitLeftLegModel[12].setRotationPoint(-0.05F, -1.5F, -2.5F);

		exoSuitLeftLegModel[13].addShapeBox(0F, 0F, 0F, 1, 2, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // FrameLeft15
		exoSuitLeftLegModel[13].setRotationPoint(3.5F, -1.5F, -1.5F);

		exoSuitLeftLegModel[14].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // WiresLeft01
		exoSuitLeftLegModel[14].setRotationPoint(3.5F, 6F, -1F);

		exoSuitLeftLegModel[15].addShapeBox(0F, 0F, 0F, 1, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // WiresLeft02
		exoSuitLeftLegModel[15].setRotationPoint(3.5F, 3F, 0F);

		exoSuitLeftLegModel[16].addShapeBox(0F, 0F, 0F, 1, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // WiresLeft03
		exoSuitLeftLegModel[16].setRotationPoint(3.5F, 0F, -1F);

		exoSuitLeftLegModel[17].addShapeBox(0F, 0F, 0F, 1, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F); // WiresLeft04
		exoSuitLeftLegModel[17].setRotationPoint(3.5F, 5F, 0F);

		exoSuitLeftLegModel[18].addShapeBox(0F, 0F, 0F, 1, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // WiresLeft05
		exoSuitLeftLegModel[18].setRotationPoint(3.5F, 2F, -1F);

		exoSuitLeftLegModel[19].addShapeBox(0F, 0F, 0F, 5, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 49
		exoSuitLeftLegModel[19].setRotationPoint(-0.05F, -1.5F, 1.5F);

		exoSuitLeftLegModel[20].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // WiresLeft01
		exoSuitLeftLegModel[20].setRotationPoint(3.5F, 7F, -1F);

		scubaTankModel = new ModelRendererTurbo[7];
		scubaTankModel[0] = new ModelRendererTurbo(this, 0, 10, 16, 16); // Box 4
		scubaTankModel[1] = new ModelRendererTurbo(this, 0, 0, 16, 16); // Box 7
		scubaTankModel[2] = new ModelRendererTurbo(this, 8, 0, 16, 16); // Box 8
		scubaTankModel[3] = new ModelRendererTurbo(this, 12, 0, 16, 16); // Box 9
		scubaTankModel[4] = new ModelRendererTurbo(this, 0, 10, 16, 16); // Box 4
		scubaTankModel[5] = new ModelRendererTurbo(this, 0, 5, 16, 16); // Box 8
		scubaTankModel[6] = new ModelRendererTurbo(this, 0, 0, 16, 16); // Box 7

		scubaTankModel[0].addShapeBox(0F, 0F, 0F, 4, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 4
		scubaTankModel[0].setRotationPoint(-1.5F, -3F, 4.5F);
		scubaTankModel[0].rotateAngleX = -0.34906585F;

		scubaTankModel[1].addShapeBox(0F, 0F, 0F, 3, 4, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 7
		scubaTankModel[1].setRotationPoint(1.5F, -4F, 3F);

		scubaTankModel[2].addShapeBox(0F, 0F, 0F, 1, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 8
		scubaTankModel[2].setRotationPoint(2.5F, -6.5F, 4F);

		scubaTankModel[3].addShapeBox(0F, 0F, 0F, 1, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 9
		scubaTankModel[3].setRotationPoint(2.5F, -11.5F, 3F);

		scubaTankModel[4].setFlipped(true);
		scubaTankModel[4].addShapeBox(0F, 0F, 0F, 4, 3, 3, 0F, -4F, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F); // Box 4
		scubaTankModel[4].setRotationPoint(-5.5F, -3F, 4.5F);
		scubaTankModel[4].rotateAngleX = -0.34906585F;

		scubaTankModel[5].addShapeBox(0F, 0F, 0F, 1, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 8
		scubaTankModel[5].setRotationPoint(2.5F, -1.5F, 4F);

		scubaTankModel[6].addShapeBox(0F, 0F, 0F, 3, 4, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 7
		scubaTankModel[6].setRotationPoint(-4.5F, -4F, 3F);

		for(ModelRendererTurbo mod : gasmaskModel)
			mod.rotationPointY += 12f;
		for(ModelRendererTurbo mod : infiltratorGogglesModel)
			mod.rotationPointY += 12f;
		for(ModelRendererTurbo mod : technicianGogglesModel)
			mod.rotationPointY += 12f;
		for(ModelRendererTurbo mod : engineerGogglesModel)
			mod.rotationPointY += 12f;
		for(ModelRendererTurbo mod : platesHelmetModel)
			mod.rotationPointY += 12f;

		//platesRightArmModel, platesLeftArmModel, platesChestModel, platesRightLegModel, platesLeftLegModel, platesSkirtRightModel, platesSkirtLeftModel
		for(ModelRendererTurbo mod : platesChestModel)
			mod.rotationPointY += 12f;

		for(ModelRendererTurbo mod : scubaTankModel)
		{
			mod.rotationPointY += 12f;
			mod.rotationPointX += 1f;
		}

		for(ModelRendererTurbo mod : platesLeftArmModel)
		{
			mod.rotationPointY += 9.5f;
			mod.rotationPointX -= 5f;
		}
		for(ModelRendererTurbo mod : platesRightArmModel)
		{
			mod.rotationPointY += 9.5f;
			mod.rotationPointX += 5f;
		}

		for(ModelRendererTurbo mod : platesSkirtLeftModel)
		{
			mod.rotationPointX -= 1.5f;
		}
		for(ModelRendererTurbo mod : platesSkirtRightModel)
		{
			mod.rotationPointX += 1.5f;
		}

		for(ModelRendererTurbo mod : exoSuitLeftLegModel)
		{
			mod.rotationPointX -= 1.5f;
		}
		for(ModelRendererTurbo mod : exoSuitRightLegModel)
		{
			mod.rotationPointX += 1.5f;
		}

		for(ModelRendererTurbo mod : platesLeftLegModel)
		{
			mod.rotationPointX -= 1.5f;
			mod.rotationPointY -= 1.5f;
		}
		for(ModelRendererTurbo mod : platesRightLegModel)
		{
			mod.rotationPointX += 1.5f;
			mod.rotationPointY -= 1.5f;
		}

		parts.put("gasmask", gasmaskModel);
		parts.put("goggles_infiltrator", infiltratorGogglesModel);
		parts.put("goggles_technician", technicianGogglesModel);
		parts.put("goggles_engineer", engineerGogglesModel);
		parts.put("plates_helmet_model", platesHelmetModel);

		parts.put("plates_right_arm", platesRightArmModel);
		parts.put("plates_left_arm", platesLeftArmModel);
		parts.put("plates_chest", platesChestModel);
		parts.put("plates_right_leg", platesRightLegModel);
		parts.put("plates_left_leg", platesLeftLegModel);
		parts.put("plates_skirt_right", platesSkirtRightModel);
		parts.put("plates_skirt_left", platesSkirtLeftModel);
		parts.put("exo_suit_right_leg", exoSuitRightLegModel);
		parts.put("exo_suit_left_leg", exoSuitLeftLegModel);

		parts.put("scuba_tank", scubaTankModel);

		flipAll();
		init();
	}

	static ModelLightEngineerArmor modelInstance;

	private void setSkin(String skin)
	{
		String baseName = skin.isEmpty()?ImmersiveIntelligence.MODID+":textures/armor/engineer_light": IIReference.SKIN_LOCATION+skin+"/engineer_light";

		TEXTURE = baseName+".png";
		setTexture(TEXTURE);
		TEXTURE_PLATES = baseName+"_plates.png";
		TEXTURE_SCUBA = baseName+"_scuba.png";
		TEXTURE_GOGGLES = baseName+"_goggles.png";
		TEXTURE_GASMASK = baseName+"_gasmask.png";
		TEXTURE_EXOSUIT = baseName+"_exosuit.png";
	}

	@Override
	protected TMTArmorModel prepareForRender(EntityEquipmentSlot part, ItemStack stack)
	{
		String s = IISkinHandler.getCurrentSkin(stack);
		if(IISkinHandler.isValidSkin(s))
		{
			IISpecialSkin skin = IISkinHandler.getSkin(s);
			if(skin.doesApply(IIContent.itemLightEngineerChestplate.getSkinnableName()))
				setSkin(s);
		}
		else
			setSkin("");
		return super.prepareForRender(part, stack);
	}

	public static ModelLightEngineerArmor getModel(EntityEquipmentSlot part, ItemStack stack)
	{
		return (ModelLightEngineerArmor)modelInstance.prepareForRender(part, stack);
	}


	public void renderAddons(ItemStack renderStack, EntityEquipmentSlot renderSlot, ModelRendererTurbo[] part, float ageInTicks, boolean entity, float scale)
	{
		NBTTagCompound upgrades = IIContent.itemLightEngineerHelmet.getUpgrades(renderStack);

		switch(renderSlot)
		{
			case HEAD:
			{
				if(upgrades.hasKey("gasmask"))
					renderWithEntity(entity, bipedHead, gasmaskModel, scale, TEXTURE_GASMASK);

				GlStateManager.pushMatrix();
				GlStateManager.disableCull();
				if(upgrades.hasKey("infiltrator_gear"))
					renderWithEntity(entity, bipedHead, infiltratorGogglesModel, scale, TEXTURE_GOGGLES);
				else if(upgrades.hasKey("technician_gear"))
					renderWithEntity(entity, bipedHead, technicianGogglesModel, scale, TEXTURE_GOGGLES);
				else if(upgrades.hasKey("engineer_gear"))
					renderWithEntity(entity, bipedHead, engineerGogglesModel, scale, TEXTURE_GOGGLES);
				GlStateManager.enableCull();
				GlStateManager.popMatrix();

				if(hasPlates(upgrades))
				{
					setColorForPlates(renderStack, upgrades);
					renderWithEntity(entity, bipedHead, platesHelmetModel, scale, TEXTURE_PLATES);
					GlStateManager.color(1f, 1f, 1f);
				}
			}
			break;
			case CHEST:
			{
				if(hasPlates(upgrades))
				{
					int armorIncrease = upgrades.getInteger("armor_increase");
					setColorForPlates(renderStack, upgrades);
					if(entity||part==bodyModel)
						renderWithEntity(entity, bipedBody, platesChestModel, scale, TEXTURE_PLATES);
					if(armorIncrease > 1&&(entity||part==rightArmModel))
						renderWithEntity(entity, bipedRightArm, platesRightArmModel, scale, TEXTURE_PLATES);
					if(armorIncrease > 2&&(entity||part==leftArmModel))
						renderWithEntity(entity, bipedLeftArm, platesLeftArmModel, scale, TEXTURE_PLATES);
					GlStateManager.color(1f, 1f, 1f);
				}
				if((entity||part==bodyModel)&&upgrades.hasKey("scuba"))
					renderWithEntity(entity, bipedBody, scubaTankModel, scale, TEXTURE_SCUBA);

			/*
			float pt = Math.abs((((ageInTicks+Minecraft.getMinecraft().getRenderPartialTicks())%160)/160f)-0.5f)/.5f;
			for(ModelRendererTurbo mod : capeModel)
				mod.rotateAngleX = 0.0625f+(pt*0.125f);
			renderWithEntity(entity,this.bipedBody, capeModel, scale,TEXTURE);
			 */
			}
			break;
			case LEGS:
			{
				if(hasPlates(upgrades))
				{
					setColorForPlates(renderStack, upgrades);
					if(entity||part==leftLegModel)
						renderWithEntity(entity, bipedLeftLeg, platesSkirtLeftModel, scale, TEXTURE_PLATES);
					if(entity||part==rightLegModel)
						renderWithEntity(entity, bipedRightLeg, platesSkirtRightModel, scale, TEXTURE_PLATES);
					GlStateManager.color(1f, 1f, 1f);
					//renderWithEntity(entity, bipedLeftLeg, platesLeftLegModel, scale, TEXTURE_PLATES);
					//renderWithEntity(entity, bipedRightLeg, platesRightLegModel, scale, TEXTURE_PLATES);
				}
				if(upgrades.hasKey("exoskeleton"))
				{
					if(entity||part==leftLegModel)
						renderWithEntity(entity, bipedLeftLeg, exoSuitLeftLegModel, scale, TEXTURE_EXOSUIT);
					if(entity||part==rightLegModel)
						renderWithEntity(entity, bipedRightLeg, exoSuitRightLegModel, scale, TEXTURE_EXOSUIT);
				}
			}
			break;
			case FEET:
			{
				if(upgrades.hasKey("reinforced"))
				{

				}
			}
			break;
		}

	}

	private boolean hasPlates(NBTTagCompound upgrades)
	{
		return upgrades.hasKey("steel_plates")||upgrades.hasKey("composite_plates");
	}

	private void setColorForPlates(ItemStack stack, NBTTagCompound upgrades)
	{
		if(ItemNBTHelper.hasKey(stack, ItemIIUpgradeableArmor.NBT_COLOR))
			IIColor.fromPackedRGB(ItemNBTHelper.getInt(stack, ItemIIUpgradeableArmor.NBT_COLOR)).glColor();
		else if(upgrades.hasKey("composite_plates"))
			GlStateManager.color(0.9f, 0.9f, 1f);
		else
			GlStateManager.color(1f, 1f, 1f);

	}

	public void renderWithEntity(boolean entity, ModelRenderer biped, ModelRendererTurbo[] model, float scale, String texture)
	{
		if(entity)
			renderChild(biped, model, scale, texture);
		else
		{
			ClientUtils.bindTexture(texture);
			for(ModelRendererTurbo mod : model)
				mod.render();
		}
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
