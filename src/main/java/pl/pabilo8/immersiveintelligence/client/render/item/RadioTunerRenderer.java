package pl.pabilo8.immersiveintelligence.client.render.item;

import blusunrize.immersiveengineering.client.ImmersiveModelRegistry.ItemModelReplacement;
import blusunrize.immersiveengineering.client.ImmersiveModelRegistry.ItemModelReplacement_OBJ;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.*;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIItemRendererAMT.RegisteredItemRenderer;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.item.tools.ItemIIRadioTuner;
import pl.pabilo8.immersiveintelligence.common.item.tools.ItemIIRadioTuner.RadioTuners;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIModelHeader;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

/**
 * @author Pabilo8
 * @updated 22.12.2023
 * @since 13-10-2019
 */
@RegisteredItemRenderer(name = "items/tools/radio_tuner")
public class RadioTunerRenderer extends IIItemRendererAMT<ItemIIRadioTuner>
{
	private static final ResLoc RES_ADVANCED_TUNER = ResLoc.of(IIReference.RES_ITEM_MODEL, "tools/advanced_radio_tuner").withExtension(ResLoc.EXT_OBJ);

	AMT[] amtBasic, amtAdvanced;
	IIAnimationCompiledMap handBasic, handAdvanced, gaugeBasic1, gaugeBasic2, gaugeAdvanced1, gaugeAdvanced2, gaugeAdvanced3;

	public RadioTunerRenderer()
	{
		super(IIContent.itemRadioTuner, ResLoc.of(IIReference.RES_ITEM_MODEL, "tools/radio_tuner").withExtension(ResLoc.EXT_OBJ));
	}

	@Override
	protected ItemModelReplacement setTransforms(ItemModelReplacement_OBJ model)
	{
		Matrix4 tpp = new Matrix4()
				.scale(0.5, 0.5, 0.5)
				.rotate(Math.toRadians(-20.5f), 0, 1, 0)
				.translate(0, 0.125, 0.385f);

		Matrix4 fpp = new Matrix4()
				.scale(1, 1, 1)
				.rotate(Math.toRadians(-25), 0, 1, 0)
				.translate(0.25f, 0, 0.125)
				.translate(0, 0, -0.5f);

		return model
				.setTransformations(TransformType.GROUND, new Matrix4()
						.scale(0.45, 0.45, 0.45)
						.translate(0.5, 0, 0.5))
				.setTransformations(TransformType.THIRD_PERSON_RIGHT_HAND, tpp)
				.setTransformations(TransformType.FIRST_PERSON_RIGHT_HAND, fpp)
				.setTransformations(TransformType.GUI, new Matrix4()
						.translate(0.2, 0, 0)
						.scale(0.7, 0.7, 0.7)
						.rotate(Math.toRadians(-22.5), 0, 1, 0)
						.rotate(Math.toRadians(-7.5), 1, 0, 0)
				);
	}

	@Override
	public void draw(ItemStack stack, TransformType transform, BufferBuilder buf, Tessellator tes, float partialTicks)
	{
		EasyNBT nbt = EasyNBT.wrapNBT(stack);
		float prevFrequency = nbt.getInt(ItemIIRadioTuner.TAG_PREV_FREQUENCY);
		float frequency = nbt.getInt(ItemIIRadioTuner.TAG_FREQUENCY);
		float progress = prevFrequency+(frequency-prevFrequency)*partialTicks;

		switch(RadioTuners.values()[stack.getMetadata()])
		{
			case BASIC:
				handBasic.apply(is1stPerson(transform)?1: 0);
				renderBasic(progress, buf, tes);
				break;
			case ADVANCED:
				handAdvanced.apply(is1stPerson(transform)?1: 0);
				renderAdvanced(progress, buf, tes);
				break;
		}
	}

	private void renderBasic(float frequency, BufferBuilder buf, Tessellator tes)
	{
		gaugeBasic1.apply((frequency%6)/6f);
		gaugeBasic2.apply((float)Math.floor(frequency/((float)IIConfig.radioBasicMaxFrequency/6f))/6f);

		for(AMT amt : amtBasic)
			amt.render(tes, buf);
	}

	private void renderAdvanced(float frequency, BufferBuilder buf, Tessellator tes)
	{

		gaugeAdvanced3.apply((frequency%8)/8f);
		gaugeAdvanced2.apply((frequency%64)/64f);
		gaugeAdvanced1.apply((float)Math.floor(frequency/((float)IIConfig.radioAdvancedMaxFrequency/64f))/64f);

		for(AMT amt : amtAdvanced)
			amt.render(tes, buf);
	}

	@Override
	public void compileModels(OBJModel model, IIModelHeader header)
	{
		//Basic radio tuner
		this.amtBasic = IIAnimationUtils.getAMTItemModel(model, header, header1 -> new AMT[]{
				new AMTHand("hand", header, EnumHand.MAIN_HAND)
		});
		this.gaugeBasic1 = IIAnimationCompiledMap.create(this.amtBasic, ResLoc.of(IIReference.RES_II, "tools/basic_tuner/gauge1"));
		this.gaugeBasic2 = IIAnimationCompiledMap.create(this.amtBasic, ResLoc.of(IIReference.RES_II, "tools/basic_tuner/gauge2"));

		//Advanced radio tuner
		this.amtAdvanced = IIAnimationUtils.getAMTItemModel(IIAnimationUtils.modelFromRes(RES_ADVANCED_TUNER),
				IIAnimationLoader.loadHeader(RES_ADVANCED_TUNER.withExtension(ResLoc.EXT_OBJAMT)),
				header1 -> new AMT[]{
						new AMTHand("hand", header, EnumHand.MAIN_HAND)
				});
		this.gaugeAdvanced1 = IIAnimationCompiledMap.create(this.amtAdvanced, ResLoc.of(IIReference.RES_II, "tools/advanced_tuner/gauge1"));
		this.gaugeAdvanced2 = IIAnimationCompiledMap.create(this.amtAdvanced, ResLoc.of(IIReference.RES_II, "tools/advanced_tuner/gauge2"));
		this.gaugeAdvanced3 = IIAnimationCompiledMap.create(this.amtAdvanced, ResLoc.of(IIReference.RES_II, "tools/advanced_tuner/gauge3"));

		this.handBasic = IIAnimationCompiledMap.create(this.amtBasic, ResLoc.of(IIReference.RES_II, "tools/hand"));
		this.handAdvanced = IIAnimationCompiledMap.create(this.amtAdvanced, ResLoc.of(IIReference.RES_II, "tools/hand"));
	}

	@Override
	protected void nullifyModels()
	{
		IIAnimationUtils.disposeOf(this.amtBasic);
		IIAnimationUtils.disposeOf(this.amtAdvanced);
	}

	@Override
	public void registerSprites(TextureMap map)
	{
		super.registerSprites(map);
//		IIAnimationLoader.preloadTexturesFromMTL(headerRes.withExtension(ResLoc.EXT_MTL), map);
		IIAnimationLoader.preloadTexturesFromMTL(RES_ADVANCED_TUNER.withExtension(ResLoc.EXT_MTL), map);
	}
}
