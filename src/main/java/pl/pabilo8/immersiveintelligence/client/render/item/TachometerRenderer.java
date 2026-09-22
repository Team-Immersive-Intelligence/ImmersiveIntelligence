package pl.pabilo8.immersiveintelligence.client.render.item;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCompiledMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTHand;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIItemRendererAMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIItemRendererAMT.RegisteredItemRenderer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.item.tools.ItemIITachometer;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 22.12.2023
 * @since 13.10.2019
 */
@RegisteredItemRenderer(name = "items/tools/tachometer")
public class TachometerRenderer extends IIItemRendererAMT<ItemIITachometer>
{
	AMTModel model;
	IIAnimationCompiledMap gauge, hand;

	public TachometerRenderer()
	{
		super(IIContent.itemTachometer, ResLoc.of(IIReference.RES_ITEM_MODEL, "tools/tachometer").withExtension(ResLoc.EXT_OBJ));
	}

	@Override
	public void draw(ItemStack stack, TransformType transform, BufferBuilder buf, Tessellator tes, float partialTicks)
	{
		hand.apply(is1stPerson(transform)?1: 0);
		model.render(tes, buf);
	}

	@Override
	public void compileModels(OBJModel model, AMTModelHeader header)
	{
		this.model = new AMTModel(DefaultVertexFormats.ITEM, model, header, header1 -> new AMT[]{
				new AMTHand("hand", header, EnumHand.MAIN_HAND)
		});
		this.gauge = IIAnimationCompiledMap.create(this.model, ResLoc.of(IIReference.RES_II, "tools/tachometer/gauge"));
		this.hand = IIAnimationCompiledMap.create(this.model, ResLoc.of(IIReference.RES_II, "tools/hand"));
	}

	@Override
	protected void nullifyModels()
	{
		AMTUtils.disposeOf(this.model);
	}
}
