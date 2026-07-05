package pl.pabilo8.immersiveintelligence.client.render.item;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.ImmersiveModelRegistry.ItemModelReplacement;
import blusunrize.immersiveengineering.client.ImmersiveModelRegistry.ItemModelReplacement_OBJ;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCompiledMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTHand;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIItemRendererAMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIItemRendererAMT.RegisteredItemRenderer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.item.tools.ItemIIElectricHammer;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 22.12.2023
 * @since 13.10.2019
 */
@RegisteredItemRenderer(name = "items/tools/electric_hammer")
public class ElectricHammerRenderer extends IIItemRendererAMT<ItemIIElectricHammer> implements ISpecificHandRenderer
{
	private AMTModel model;
	private IIAnimationCompiledMap hammering, equip, hand;

	public ElectricHammerRenderer()
	{
		super(IIContent.itemHammer, ResLoc.of(IIReference.RES_ITEM_MODEL, "tools/electric_hammer")
				.withExtension(ResLoc.EXT_OBJ), true);
	}

	@Override
	protected ItemModelReplacement setTransforms(ItemModelReplacement_OBJ model)
	{
		return model
				.setTransformations(TransformType.GROUND, new Matrix4()
						.scale(0.625, 0.625, 0.625)
						.translate(0.5, 0, 0.5))
				.setTransformations(TransformType.THIRD_PERSON_RIGHT_HAND, new Matrix4()
						.scale(0.625, 0.625, 0.625)
						.translate(0.5, 0.25, 0.575))
				.setTransformations(TransformType.THIRD_PERSON_LEFT_HAND, new Matrix4()
						.scale(0.625, 0.625, 0.625)
						.translate(-0.5, 0.25, 0.575))
				.setTransformations(TransformType.FIRST_PERSON_RIGHT_HAND, new Matrix4()
						.scale(1.5, 1.5, 1.5)
						.translate(1f, 0, 0.125-0.25f-0.385f+0.125))
				.setTransformations(TransformType.FIRST_PERSON_LEFT_HAND, new Matrix4()
						.scale(1.5, 1.5, 1.5)
						.translate(0f, 0, 0.125-0.25f-0.385f+0.125));
	}

	@Override
	public void draw(ItemStack stack, TransformType transform, BufferBuilder buf, Tessellator tes, float partialTicks)
	{
		model.defaultize();
		if(is1stPerson(transform))
		{
			hand.apply(1);
			float itemEquipTime = getItemEquipTime(transform==TransformType.FIRST_PERSON_LEFT_HAND^
					ClientUtils.mc().player.getPrimaryHand()==EnumHandSide.RIGHT?EnumHand.MAIN_HAND: EnumHand.OFF_HAND, partialTicks);
			equip.apply(itemEquipTime);

			if(itemEquipTime==1&&item.hasEnoughEnergy(stack))
				hammering.apply(AMTUtils.getDebugProgress(24, partialTicks));
		}
		else
			hand.apply(0);

		model.render(tes, buf);
	}

	@Override
	public void compileModels(OBJModel model, AMTModelHeader header)
	{
		this.model = new AMTModel(DefaultVertexFormats.ITEM, model, header, header1 -> new AMT[]{
				new AMTHand("mainhand", header),
				new AMTHand("offhand", header)
		});
		this.hammering = IIAnimationCompiledMap.create(this.model, ResLoc.of(IIReference.RES_II, "tools/electric_hammer/hammer"));
		this.equip = IIAnimationCompiledMap.create(this.model, ResLoc.of(IIReference.RES_II, "tools/electric_hammer/equip"));
		this.hand = IIAnimationCompiledMap.create(this.model, ResLoc.of(IIReference.RES_II, "tools/electric_hammer/hand"));
	}

	@Override
	protected void nullifyModels()
	{
		AMTUtils.disposeOf(this.model);
	}

	@Override
	public boolean doHandRender(ItemStack stack, EnumHand hand, ItemStack otherHand, float swingProgress, float partialTicks)
	{
		return hand==EnumHand.OFF_HAND&&!otherHand.isEmpty()&&getItemEquipTime(EnumHand.MAIN_HAND, partialTicks)!=1;
	}
}
