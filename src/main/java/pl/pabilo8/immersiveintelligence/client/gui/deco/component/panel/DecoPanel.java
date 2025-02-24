package pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.GuiComponentDecoBase;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 10.02.2025
 **/
public class DecoPanel extends GuiComponentDecoBase<DecoPanel>
{
	private List<DecoLabel> labels = new ArrayList<>();
	private ResLoc background = IIReference.GUI_BG_STEEL;
	private ResLoc backgroundMask = IIReference.RES_TEXTURES_DECO_TEMPLATE_SQUARE;
	int vbo = -1;
	private int xPadding = 0;
	private int yPadding = 0;

	public DecoPanel(int x, int y)
	{
		super(x, y);
	}

	public void addComponent(GuiComponentDecoBase<?> component)
	{
		children.add(component);
	}

	public void addLabel(DecoLabel label)
	{
		labels.add(label);
	}

	public DecoPanel withBackground(ResLoc background)
	{
		this.background = background;
		return this;
	}

	public DecoPanel withBackgroundMask(ResLoc backgroundMask)
	{
		this.backgroundMask = backgroundMask;
		return this;
	}

	@Override
	protected boolean initialize()
	{
		if(background==null||backgroundMask==null)
			return true;
		TextureAtlasSprite maskSprite = ClientUtils.getSprite(backgroundMask);

		//Start
		vbo = GlStateManager.glGenLists(1);
		GlStateManager.glNewList(vbo, GL11.GL_COMPILE);
		GlStateManager.color(1f, 1f, 1f, 1f);
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);

		//Mask
		IIDrawUtils draw = IIDrawUtils.startTexturedColored();
		GL11.glEnable(GL11.GL_STENCIL_TEST);
		GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
		GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);
		GL11.glStencilFunc(GL11.GL_ALWAYS, 1, 0xFF);
		draw.drawRepeatedColorRect(x, y, width, height, IIColor.WHITE, 32, 32, 4, 4,
				maskSprite.getMinU(), maskSprite.getInterpolatedU(8), maskSprite.getMinV(), maskSprite.getInterpolatedV(8)
		);
		draw.finish();

		//Background
		draw = IIDrawUtils.startTexturedColored();
		GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
		GL11.glStencilFunc(GL11.GL_EQUAL, 1, 0xFF);
		draw.drawRepeatedColorRect(x, y, width, height, IIColor.WHITE, background, 32, 32, 0, 0);
		draw.finish();

		//Overlay
		draw = IIDrawUtils.startTexturedColored();
		GL11.glDisable(GL11.GL_STENCIL_TEST);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(SourceFactor.DST_COLOR, DestFactor.SRC_COLOR);
		draw.drawRepeatedColorRect(x, y, width, height, IIColor.WHITE, 32, 32, 4, 4,
				maskSprite.getMinU(), maskSprite.getInterpolatedU(8), maskSprite.getMinV(), maskSprite.getInterpolatedV(8)
		);
		draw.finish();

		//Finish
		GlStateManager.color(1f, 1f, 1f, 1f);
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableBlend();
		GlStateManager.glEndList();

		return vbo!=-1;
	}

	@Override
	protected void draw(int mouseX, int mouseY, float partialTicks)
	{
		GlStateManager.color(1, 1, 1, 1);
		bindAtlas();
		GlStateManager.callList(vbo);

		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		labels.forEach(label -> label.drawLabel(ClientUtils.mc(), mouseX, mouseY));
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}

	@Override
	public void cleanup()
	{
		children.clear();
		labels.clear();
		if(vbo!=-1)
		{
			GlStateManager.glDeleteLists(vbo, 1);
			vbo = -1;
		}
	}

	public DecoPanel withPadding(int xPadding, int yPadding)
	{
		this.xPadding = xPadding;
		this.yPadding = yPadding;
		return null;
	}
}
