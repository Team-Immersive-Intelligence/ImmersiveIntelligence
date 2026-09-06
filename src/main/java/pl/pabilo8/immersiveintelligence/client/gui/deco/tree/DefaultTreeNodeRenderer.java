package pl.pabilo8.immersiveintelligence.client.gui.deco.tree;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import javax.annotation.Nonnull;

/**
 * Renders generic Deco tree nodes as compact labelled boxes.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 24.08.2026
 * @since 09.12.2025
 */
public class DefaultTreeNodeRenderer extends AbstractTreeNodeRenderer<Object>
{
	public static final int NODE_WIDTH = 20;
	public static final int NODE_HEIGHT = 20;

	private final IIColor activeColor = IIColor.fromHex("497d49");
	private final IIColor availableColor = IIColor.fromHex("888888");
	private final IIColor unavailableColor = IIColor.fromHex("3c3c3c");
	private final IIColor hoverColor = IIReference.COLOR_IMMERSIVE_ORANGE.withBrightness(0.75f);

	public DefaultTreeNodeRenderer()
	{
		super(IIReference.COLOR_IMMERSIVE_ORANGE.withBrightness(0.75f), IIColor.fromHex("497d49"));
	}

	@Override
	public void renderNode(@Nonnull IDecoTreeNode<Object> node, int x, int y, boolean isHovered, boolean isActive, boolean isAvailable)
	{
		IIColor color = isActive?activeColor: isHovered?hoverColor: isAvailable?availableColor: unavailableColor;

		ClientUtils.bindAtlas();
		IIDrawUtils.startTexturedColored()
				.drawConnectedTexColorRect(x, y, NODE_WIDTH, NODE_HEIGHT, color, DecoTextures.SLOT_IE, 32, 32, 4, 4)
				.finish();

		String text = getDisplayName(node);
		if(text.isEmpty())
			return;

		FontRenderer font = getFontRenderer();
		if(font.getStringWidth(text) > NODE_WIDTH-4)
			text = font.trimStringToWidth(text, NODE_WIDTH-6)+"...";

		GlStateManager.pushMatrix();
		GlStateManager.translate(0, 0, 1);
		font.drawString(text, x+(NODE_WIDTH-font.getStringWidth(text))/2,
				y+(NODE_HEIGHT-font.FONT_HEIGHT)/2, 0xFFFFFF);
		GlStateManager.popMatrix();
	}

	@Override
	public void renderRootNode(int x, int y)
	{
		IIDrawUtils.startColored()
				.drawColorRect(x-3, y-3, 6, 6, IIColor.fromHex("888888"))
				.finish();
	}
}
