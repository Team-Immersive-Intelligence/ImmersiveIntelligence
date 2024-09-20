package pl.pabilo8.immersiveintelligence.client.util.amt;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIModelHeader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * AMT type for drawing text with {@link FontRenderer}
 *
 * @author Pabilo8
 * @since 26.07.2022
 */
public class AMTText extends AMT
{
	@Nullable
	private String text;

	private IIColor color = IIColor.WHITE;
	private float fontSize = 1;
	@Nonnull
	private FontRenderer fontRenderer;

	public AMTText(String name, Vec3d originPos)
	{
		super(name, originPos);
		this.fontRenderer = ClientUtils.mc().fontRenderer;
	}

	public AMTText(String name, IIModelHeader header)
	{
		this(name, header.getOffset(name));
	}

	@Override
	protected void draw(Tessellator tes, BufferBuilder buf)
	{
		if(text!=null&&!text.isEmpty())
		{
			//GlStateManager.scale(0.0625,0.0625,0.0625);
			GlStateManager.pushMatrix();
			GlStateManager.disableCull();
			GlStateManager.translate(originPos.x, originPos.y+0.125, originPos.z);

			GlStateManager.scale(1, -1, -1);
			if(scale!=null)
				GlStateManager.scale(scale.x, scale.y, scale.z);

			GlStateManager.scale(fontSize, fontSize, fontSize);


			fontRenderer.drawString(text, 0, 0, color.getPackedRGB(), false);
			GlStateManager.popMatrix();
			GlStateManager.enableCull();

			GlStateManager.color(1f, 1f, 1f, 1f);
			ClientUtils.bindAtlas();
			GlStateManager.enableBlend();
			GlStateManager.enableAlpha();
		}
	}

	@Override
	public void disposeOf()
	{

	}

	public AMTText setText(@Nullable String text)
	{
		this.text = text;
		return this;
	}

	public AMTText setFontRenderer(@Nonnull FontRenderer fontRenderer)
	{
		this.fontRenderer = fontRenderer;
		return this;
	}

	public AMTText setColor(IIColor color)
	{
		this.color = color;
		return this;
	}

	public AMTText setFontSize(float fontSize)
	{
		this.fontSize = fontSize;
		return this;
	}
}
