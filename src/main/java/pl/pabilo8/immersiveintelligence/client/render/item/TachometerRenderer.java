package pl.pabilo8.immersiveintelligence.client.render.item;

import blusunrize.immersiveengineering.client.ImmersiveModelRegistry.ItemModelReplacement;
import blusunrize.immersiveengineering.client.ImmersiveModelRegistry.ItemModelReplacement_OBJ;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.*;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIItemRendererAMT.RegisteredItemRenderer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.item.tools.ItemIITachometer;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIModelHeader;

/**
 * @author Pabilo8
 * @updated 22.12.2023
 * @since 13-10-2019
 */
@RegisteredItemRenderer(name = "items/tools/tachometer")
public class TachometerRenderer extends IIItemRendererAMT<ItemIITachometer>
{
	AMT[] amt;
	IIAnimationCompiledMap gauge, hand;

	public TachometerRenderer()
	{
		super(IIContent.itemTachometer, ResLoc.of(IIReference.RES_ITEM_MODEL, "tools/tachometer").withExtension(ResLoc.EXT_OBJ));
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
		hand.apply(is1stPerson(transform)?1: 0);
		for(AMT mod : amt)
			mod.render(tes, buf);
	}

	@Override
	public void compileModels(OBJModel model, IIModelHeader header)
	{
		this.amt = IIAnimationUtils.getAMTItemModel(model, header, header1 -> new AMT[]{
				new AMTHand("hand", header, EnumHand.MAIN_HAND)
		});
		this.gauge = IIAnimationCompiledMap.create(this.amt, ResLoc.of(IIReference.RES_II, "tools/tachometer/gauge"));
		this.hand = IIAnimationCompiledMap.create(this.amt, ResLoc.of(IIReference.RES_II, "tools/hand"));
	}

	@Override
	protected void nullifyModels()
	{
		IIAnimationUtils.disposeOf(this.amt);
	}
}
