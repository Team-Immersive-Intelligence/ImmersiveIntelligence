package pl.pabilo8.immersiveintelligence.client.model.misc;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.Mines;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;

/**
 * @author Pabilo8
 * @since 29.01.2021
 */
public class ModelTripMine extends ModelIIBase implements IBulletModel
{
	int textureX = 32;
	int textureY = 32;
	public static final ResourceLocation[] TEXTURES = new ResourceLocation[]{
			new ResourceLocation(ImmersiveIntelligence.MODID+":textures/blocks/mine/tripmine.png"), //IE steel gray, default option
			new ResourceLocation(ImmersiveIntelligence.MODID+":textures/blocks/mine/tripmine_alt.png"), //green
			new ResourceLocation(ImmersiveIntelligence.MODID+":textures/blocks/mine/tripmine_alt2.png") //dull-yellow, S-Mine original color
	};

	public ModelRendererTurbo[] coreModel;

	public ModelTripMine() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[9];
		baseModel[0] = new ModelRendererTurbo(this, 8, 23, textureX, textureY); // Canister02
		baseModel[1] = new ModelRendererTurbo(this, 0, 16, textureX, textureY); // Canister03
		baseModel[2] = new ModelRendererTurbo(this, 12, 9, textureX, textureY); // Canister04
		baseModel[3] = new ModelRendererTurbo(this, 8, 9, textureX, textureY); // Canister05
		baseModel[4] = new ModelRendererTurbo(this, 4, 9, textureX, textureY); // Canister06
		baseModel[5] = new ModelRendererTurbo(this, 0, 9, textureX, textureY); // Canister07
		baseModel[6] = new ModelRendererTurbo(this, 0, 23, textureX, textureY); // Canister08
		baseModel[7] = new ModelRendererTurbo(this, 8, 16, textureX, textureY); // Canister09
		baseModel[8] = new ModelRendererTurbo(this, 4, 5, textureX, textureY); // Canister10

		baseModel[0].addShapeBox(0F, 0F, 0F, 1, 6, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Canister02
		baseModel[0].setRotationPoint(6F, 0F, 5.9F);

		baseModel[1].addShapeBox(0F, 0F, 0F, 3, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Canister03
		baseModel[1].setRotationPoint(7F, 0F, 8.9F);

		baseModel[2].addShapeBox(0F, 0F, 0F, 1, 6, 1, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Canister04
		baseModel[2].setRotationPoint(6F, 0F, 4.9F);

		baseModel[3].addShapeBox(0F, 0F, 0F, 1, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F); // Canister05
		baseModel[3].setRotationPoint(10F, 0F, 4.9F);

		baseModel[4].addShapeBox(0F, 0F, 0F, 1, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F); // Canister06
		baseModel[4].setRotationPoint(10F, 0F, 8.9F);

		baseModel[5].addShapeBox(0F, 0F, 0F, 1, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F); // Canister07
		baseModel[5].setRotationPoint(6F, 0F, 8.9F);

		baseModel[6].addShapeBox(0F, 0F, 0F, 1, 6, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Canister08
		baseModel[6].setRotationPoint(10F, 0F, 5.9F);

		baseModel[7].addShapeBox(0F, 0F, 0F, 3, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Canister09
		baseModel[7].setRotationPoint(7F, 0F, 4.9F);

		baseModel[8].addShapeBox(0F, 0F, 0F, 3, 1, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Canister10
		baseModel[8].setRotationPoint(7F, 5F, 5.9F);


		coreModel = new ModelRendererTurbo[11];
		coreModel[0] = new ModelRendererTurbo(this, 20, 23, textureX, textureY); // MineItself02
		coreModel[1] = new ModelRendererTurbo(this, 16, 19, textureX, textureY); // MineItself03
		coreModel[2] = new ModelRendererTurbo(this, 16, 27, textureX, textureY); // MineItself04
		coreModel[3] = new ModelRendererTurbo(this, 28, 21, textureX, textureY); // MineItself05
		coreModel[4] = new ModelRendererTurbo(this, 28, 21, textureX, textureY); // MineItself06
		coreModel[5] = new ModelRendererTurbo(this, 28, 21, textureX, textureY); // MineItself07
		coreModel[6] = new ModelRendererTurbo(this, 17, 24, textureX, textureY); // MineItself09
		coreModel[7] = new ModelRendererTurbo(this, 17, 24, textureX, textureY); // MineItself10
		coreModel[8] = new ModelRendererTurbo(this, 28, 21, textureX, textureY); // MineItself11
		coreModel[9] = new ModelRendererTurbo(this, 28, 21, textureX, textureY); // MineItself05
		coreModel[10] = new ModelRendererTurbo(this, 17, 24, textureX, textureY); // MineItself10

		coreModel[0].addShapeBox(0F, 0F, 0F, 3, 6, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MineItself02
		coreModel[0].setRotationPoint(7F, -0.01F, 6F);

		coreModel[1].addShapeBox(0F, 0F, 0F, 3, 1, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MineItself03
		coreModel[1].setRotationPoint(7F, -1F, 5.9F);

		coreModel[2].addShapeBox(0F, 0F, 0F, 1, 4, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MineItself04
		coreModel[2].setRotationPoint(8F, -4.2F, 6.9F);

		coreModel[3].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MineItself05
		coreModel[3].setRotationPoint(8F, -1.2F, 5.8F);

		coreModel[4].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MineItself06
		coreModel[4].setRotationPoint(9.1F, -1.2F, 6.9F);

		coreModel[5].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MineItself07
		coreModel[5].setRotationPoint(6.9F, -1.2F, 6.9F);

		coreModel[6].addShapeBox(0F, 0F, 0F, 2, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MineItself09
		coreModel[6].setRotationPoint(8F, -4.2F, 7.2F);
		coreModel[6].rotateAngleY = -0.78539816F;
		coreModel[6].rotateAngleZ = 0.78539816F;

		coreModel[7].addShapeBox(0F, 0F, 0F, 2, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MineItself10
		coreModel[7].setRotationPoint(8.4F, -4.2F, 7.9F);
		coreModel[7].rotateAngleY = 3.66519143F;
		coreModel[7].rotateAngleZ = 0.78539816F;

		coreModel[8].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MineItself11
		coreModel[8].setRotationPoint(8F, -5F, 6.9F);

		coreModel[9].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MineItself05
		coreModel[9].setRotationPoint(8F, -1.2F, 8F);

		coreModel[10].addShapeBox(0F, 0F, -0.5F, 2, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MineItself10
		coreModel[10].setRotationPoint(8.5F, -4.2F, 7.4F);
		coreModel[10].rotateAngleY = 1.53588974F;
		coreModel[10].rotateAngleZ = 0.78539816F;

		parts.put("base",baseModel);
		parts.put("core",coreModel);

		translateAll(-8, 1f, -8);

		flipAll();
	}

	@Override
	public void renderCasing(float gunpowderPercentage, int paintColour)
	{
		IIClientUtils.bindTexture(TEXTURES[Mines.tripmineColor]);
		for(ModelRendererTurbo mod : baseModel)
			mod.render();
	}

	@Override
	public void renderCore(int coreColour, EnumCoreTypes coreType)
	{
		IIClientUtils.bindTexture(TEXTURES[Mines.tripmineColor]);
		float[] c = IIUtils.rgbIntToRGB(coreColour);
		GlStateManager.color(c[0], c[1], c[2]);
		for(ModelRendererTurbo mod : coreModel)
			mod.render();
	}

	public void reloadModels()
	{
		ModelTripMine newModel = new ModelTripMine();
		this.baseModel = newModel.baseModel;
		this.coreModel = newModel.coreModel;
	}
}
