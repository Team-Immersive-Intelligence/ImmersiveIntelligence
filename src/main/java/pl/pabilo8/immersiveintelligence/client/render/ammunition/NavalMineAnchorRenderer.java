package pl.pabilo8.immersiveintelligence.client.render.ammunition;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.render.IIEntityRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimationLoader;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimationUtils;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.naval_mine.EntityNavalMine;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.naval_mine.EntityNavalMineAnchor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import java.util.List;

/**
 * @author Pabilo8
 * @since 21.01.2021
 */
public class NavalMineAnchorRenderer extends IIEntityRenderer<EntityNavalMineAnchor>
{
	private static final ResLoc CHAIN = ResLoc.of(IIReference.RES_II, "blocks/fortification/steel_chain_fence");
	private AMT[] models;

	public NavalMineAnchorRenderer(RenderManager render)
	{
		super(render, "naval_mine_anchor");
	}

	@Override
	public void draw(EntityNavalMineAnchor entity, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		for(AMT amt : models)
			amt.render(tes, buf);

		List<Entity> passengers = entity.getPassengers();
		if(!passengers.isEmpty())
		{
			GlStateManager.translate(0, 0.75, 0);
			EntityNavalMine mine = (EntityNavalMine)passengers.get(0);
			TextureAtlasSprite sprite = ClientUtils.getSprite(CHAIN);
			buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			float length = MathHelper.clamp((float)Math.sqrt(entity.getDistanceSq(mine)), 0, mine.maxLength);
			float startU = sprite.getInterpolatedU(8), endU = sprite.getInterpolatedU(11);

			for(float i = 0; i < length; i += 1)
			{
				float remainingLength = Math.min(1, length-i);
				IIClientUtils.drawFace(buf, -0.09375, i, 0, 0.09375, i+remainingLength, 0,
						startU, endU, sprite.getMinV(), sprite.getMaxV());
				IIClientUtils.drawFace(buf, 0, i, -0.09375, 0, i+remainingLength, 0.09375,
						startU, endU, sprite.getMinV(), sprite.getMaxV());
				IIClientUtils.drawFace(buf, 0.09375, i, 0, -0.09375, i+remainingLength, 0,
						startU, endU, sprite.getMinV(), sprite.getMaxV());
				IIClientUtils.drawFace(buf, 0, i, 0.09375, 0, i+remainingLength, -0.09375,
						startU, endU, sprite.getMinV(), sprite.getMaxV());
			}
			tes.draw();
		}
	}

	@Override
	public void compileModels()
	{
		models = IIAnimationUtils.getAMTFromRes(
				ResLoc.of(IIReference.RES_ITEM_MODEL, "ammo/naval_mine_anchor").withExtension(ResLoc.EXT_OBJ),
				ResLoc.of(IIReference.RES_ITEM_MODEL, "ammo/naval_mine_anchor").withExtension(ResLoc.EXT_OBJAMT)
		);
	}

	@Override
	public void registerSprites(TextureMap map)
	{
		IIAnimationLoader.preloadTexturesFromMTL(ResLoc.of(IIReference.RES_ITEM_MODEL, "ammo/naval_mine_anchor")
				.withExtension(ResLoc.EXT_MTL), map);
		ApiUtils.getRegisterSprite(map, CHAIN);
	}

	@Override
	protected void nullifyModels()
	{
		IIAnimationUtils.disposeOf(models);
	}
}
