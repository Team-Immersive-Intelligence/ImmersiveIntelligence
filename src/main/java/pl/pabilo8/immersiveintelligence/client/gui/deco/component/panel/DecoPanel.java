package pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoComponent;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoFrame;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIBase;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 10.02.2025
 **/
public class DecoPanel extends DecoComponent<DecoPanel>
{
	private final List<DecoLabel> labels = new ArrayList<>();
	int vbo = -1;
	@Nullable
	private DecoFrame frame = null;
	private ResLoc background = DecoTextures.GUI_BG_STEEL;
	private ResLoc backgroundMask = DecoTextures.RES_TEXTURES_DECO_TEMPLATE_SQUARE;
	private int xPadding = 0;
	private int yPadding = 0;

	public DecoPanel(int x, int y)
	{
		super(x, y);
	}

	@Override
	public <T extends TileEntityIEBase & IIEInventory, C extends ContainerIIBase<T>> void setParentGUI(DecoGui<T, C> parent)
	{
		super.setParentGUI(parent);
		for(DecoLabel label : labels)
		{
			label.x += parent.guiLeft;
			label.y += parent.guiTop;
		}
	}

	public void addComponent(DecoComponent<?> component)
	{
		children.add(component);
		if(parentGui!=null)
		{
			component.setParentGUI(parentGui);
			component.x -= parentGui.guiLeft;
			component.y -= parentGui.guiTop;
		}
		component.x += x+xPadding;
		component.y += y+yPadding;
	}

	public void addComponents(DecoComponent<?>... components)
	{
		for(DecoComponent<?> component : components)
			addComponent(component);
	}

	public void addComponentsRow(int width, int gap, int height, DecoComponent<?>... components)
	{
		if(components==null)
			return;
		int initialX = components[0].x;
		int singleWidth = (width-Math.max(0, gap*(components.length-1)))/components.length;
		int offset = singleWidth+gap;
		for(int i = 0; i < components.length; i++)
		{
			DecoComponent<?> comp = components[i].withSize(singleWidth, height);
			comp.x = initialX+i*offset;
			addComponent(comp);
		}
	}

	public void addLabel(String text, int x, int y)
	{
		addLabel(new DecoLabel(IIClientUtils.fontRegular, x, y).withText(text));
	}

	public void addLabel(DecoLabel label)
	{
		labels.add(label);
		label.x += x+xPadding;
		label.y += y+yPadding;
	}

	public void addLabels(DecoLabel... labels)
	{
		for(DecoLabel label : labels)
			addLabel(label);
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

	public DecoPanel withFrame(@Nullable DecoFrame frame)
	{
		this.frame = frame;
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
		draw.drawConnectedTexColorRect(x, y, width, height, IIColor.WHITE, 32, 32, 4, 4,
				maskSprite.getMinU(), maskSprite.getInterpolatedU(8), maskSprite.getMinV(), maskSprite.getInterpolatedV(8)
		);
		draw.finish();

		//Background
		draw = IIDrawUtils.startTexturedColored();
		GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
		GL11.glStencilFunc(GL11.GL_EQUAL, 1, 0xFF);
		draw.drawConnectedTexColorRect(x, y, width, height, IIColor.WHITE, background, 32, 32, 0, 0);
		draw.finish();

		//Overlay
		draw = IIDrawUtils.startTexturedColored();
		GL11.glDisable(GL11.GL_STENCIL_TEST);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(SourceFactor.DST_COLOR, DestFactor.SRC_COLOR);
		draw.drawConnectedTexColorRect(x, y, width, height, IIColor.WHITE, 32, 32, 4, 4,
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
		children.forEach(DecoComponent::cleanup);
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
