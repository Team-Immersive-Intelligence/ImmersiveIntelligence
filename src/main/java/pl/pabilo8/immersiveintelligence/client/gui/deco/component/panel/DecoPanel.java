package pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoComponent;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoTitleLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoAlignment;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoFrame;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 10.02.2025
 **/
public class DecoPanel extends DecoComponent<DecoPanel>
{
	protected final List<DecoLabel> labels = new ArrayList<>();
	int vbo = -1;
	@Nullable
	private DecoFrame frame = null;
	private ResLoc background = DecoTextures.BG_STEEL;
	private ResLoc backgroundMask = DecoTextures.TEMPLATE_SQUARE;
	private int xPadding = 0;
	private int yPadding = 0;

	public DecoPanel(int x, int y)
	{
		super(x, y);
	}

	@Override
	public <T, C extends Container> void setParentGUI(DecoGui<T, C> parent)
	{
		super.setParentGUI(parent);
		for(DecoLabel label : labels)
		{
			label.x += parent.guiLeft;
			label.y += parent.guiTop;
		}
	}

	protected void removeComponent(@Nullable DecoComponent<?> component)
	{
		if(component==null)
			return;
		children.remove(component);
		component.cleanup();
	}

	protected void removeLabel(@Nullable DecoLabel label)
	{
		if(label!=null)
			labels.remove(label);
	}

	public <T extends DecoComponent<T>> T addComponent(T component)
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
		return component;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public void addComponents(DecoComponent... components)
	{
		for(DecoComponent component : components)
			addComponent(component);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public void addComponentsRow(int width, int gap, int height, DecoComponent... components)
	{
		if(components==null)
			return;
		int initialX = components[0].x;
		int singleWidth = (width-Math.max(0, gap*(components.length-1)))/components.length;
		int offset = singleWidth+gap;
		for(int i = 0; i < components.length; i++)
		{
			DecoComponent comp = components[i].withSize(singleWidth, height);
			comp.x = initialX+i*offset;
			addComponent(comp);
		}
	}

	public DecoLabel addLabel(String text, int x, int y)
	{
		return addLabel(new DecoLabel(IIClientUtils.fontRegular, x, y).withText(text));
	}

	public DecoLabel addLabel(DecoLabel label)
	{
		labels.add(label);
		label.x += x+xPadding;
		label.y += y+yPadding;
		return label;
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

	public DecoPanel withTitleLabel(String title, DecoAlignment alignment)
	{
		title = I18n.format(title);
		int stringWidth = Math.min(this.width, IIClientUtils.fontRegular.getStringWidth(title));
		int stringHeight = IIClientUtils.fontRegular.getWordWrappedHeight(title, stringWidth);

		int titleBarX = alignment.getAlignX(-2, stringWidth, this.width+4);
		int titleBarY = alignment.getAlignY(-2, stringHeight, this.height+4);

		addLabel(new DecoTitleLabel(IIClientUtils.fontRegular, titleBarX, titleBarY)
				.withBackgroundLocation(this.background.replace("background/", "label/label_"))
				.withAlign(alignment)
				.withSize(stringWidth, stringHeight)
				.withRawText(title)
		);
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
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
				GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.enableAlpha();

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
		initialized = false;
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

	@Override
	public List<String> getTooltip()
	{
		List<String> list = super.getTooltip();
		if(list.isEmpty()&&!labels.isEmpty())
			return labels.stream()
					.filter(DecoLabel::shouldDisplayTooltip)
					.map(DecoLabel::getTooltip)
					.flatMap(Collection::stream)
					.collect(Collectors.toList());
		return list;
	}

	@Nullable
	@Override
	public Object getProvidedIngredient()
	{
		for(DecoComponent<?> child : children)
			if(child.isMouseOver())
				return child.getProvidedIngredient();
		return null;
	}
}
