package pl.pabilo8.immersiveintelligence.client.util.amt.parts;

import blusunrize.immersiveengineering.client.ClientUtils;
import lombok.Getter;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * AMT type for drawing text with {@link FontRenderer}
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 26.07.2022
 */
public class AMTText extends AMT
{
	@Nullable
	@Getter
	private String text;
	@Getter
	private IIColor color = IIColor.WHITE;
	@Getter
	private float fontSize = 1;
	@Nonnull
	@Getter
	private FontRenderer fontRenderer;
	@Getter
	private boolean glowing = false;

	public AMTText(String name, Vec3d originPos)
	{
		super(name, originPos);
		this.fontRenderer = ClientUtils.mc().fontRenderer;
	}

	public AMTText(String name, AMTModelHeader header)
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

			if(glowing)
				GlStateManager.disableLighting();
			fontRenderer.drawString(text, 0, 0, color.getPackedRGB(), false);
			if(glowing)
				GlStateManager.enableLighting();

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

	@Override
	public void applyProperties(EasyNBT nbt)
	{
		super.applyProperties(nbt);
		nbt.checkSetString("text", this::setText);
		nbt.checkSetColor("color", this::setColor);
		nbt.checkSetFloat("fontSize", fontSize -> this.fontSize = fontSize/this.fontRenderer.FONT_HEIGHT/16f);
		nbt.checkSetBoolean("glowing", this::setGlowing);
	}

	@Override
	@Nonnull
	public AxisAlignedBB getBoundingBox()
	{
		if(this.text==null||this.text.isEmpty())
			return new AxisAlignedBB(originPos, originPos);

		return new AxisAlignedBB(originPos, originPos.addVector(
				this.fontRenderer.getStringWidth(this.text)*fontSize,
				this.fontRenderer.FONT_HEIGHT*fontSize,
				0)
		);
	}

	@Override
	protected AMT renamedCopy(String newName)
	{
		AMTText text = new AMTText(newName, originPos);
		text.setText(this.text);
		text.setColor(this.color);
		text.setFontSize(this.fontSize);
		text.setGlowing(this.glowing);
		return text;
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

	public AMTText setGlowing(boolean glowing)
	{
		this.glowing = glowing;
		return this;
	}
}
