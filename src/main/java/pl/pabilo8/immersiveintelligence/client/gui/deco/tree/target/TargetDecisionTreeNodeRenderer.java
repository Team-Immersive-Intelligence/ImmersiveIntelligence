package pl.pabilo8.immersiveintelligence.client.gui.deco.tree.target;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import pl.pabilo8.immersiveintelligence.client.gui.deco.tree.AbstractTreeNodeRenderer;
import pl.pabilo8.immersiveintelligence.client.gui.deco.tree.IDecoTreeNode;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.TargetDecisionTreeNode;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.filter.*;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collection;

/**
 * Renders weighted target conditions as compact two-line editor nodes.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 30.08.2026
 * @since 24.08.2026
 */
public class TargetDecisionTreeNodeRenderer extends AbstractTreeNodeRenderer<TargetDecisionTreeNode>
{
	public static final int NODE_WIDTH = 86;
	public static final int NODE_HEIGHT = 24;

	private static final IIColor NORMAL = IIColor.fromHex("686868");
	private static final IIColor SELECTED = IIReference.COLOR_IMMERSIVE_ORANGE.withBrightness(0.72f);
	private static final IIColor HOVERED = IIReference.COLOR_IMMERSIVE_ORANGE.withBrightness(0.9f);

	@Override
	public int getNodeWidth()
	{
		return NODE_WIDTH;
	}

	@Override
	public int getNodeHeight()
	{
		return NODE_HEIGHT;
	}

	@Override
	public void renderNode(@Nonnull IDecoTreeNode<TargetDecisionTreeNode> node, int x, int y,
	                       boolean isHovered, boolean isActive, boolean isAvailable)
	{
		TargetDecisionTreeNode data = node.getUserData();
		IIColor color = isActive?SELECTED: isHovered?HOVERED: NORMAL;
		ClientUtils.bindAtlas();
		IIDrawUtils.startTexturedColored()
				.drawConnectedTexColorRect(x, y, NODE_WIDTH, NODE_HEIGHT, color, DecoTextures.SLOT_IE, 32, 32, 4, 4)
				.finish();

		FontRenderer font = getFontRenderer();
		String title = trim(font, getFilterName(data.getFilter()), NODE_WIDTH-6);
		String value = getFilterValue(data.getFilter())+"  "+(data.getWeight() >= 0?"+": "")+data.getWeight();
		value = trim(font, value, NODE_WIDTH-6);
		font.drawString(title, x+3, y+3, 0xFFFFFF);
		font.drawString(value, x+3, y+NODE_HEIGHT-font.FONT_HEIGHT-3, 0xD8D8D8);
	}

	private String trim(FontRenderer font, String text, int width)
	{
		return font.getStringWidth(text) <= width?text: font.trimStringToWidth(text, Math.max(1, width-font.getStringWidth("...")))+"...";
	}

	@Nonnull
	@Override
	public Collection<String> getTooltip(@Nonnull IDecoTreeNode<TargetDecisionTreeNode> node)
	{
		TargetDecisionTreeNode data = node.getUserData();
		return Arrays.asList(
				I18n.format("ii.gui.emplacement.target_tree.condition", getFilterName(data.getFilter()),
						getFilterValue(data.getFilter())),
				I18n.format("ii.gui.emplacement.target_tree.weight_value", data.getWeight()),
				I18n.format("ii.gui.emplacement.target_filter."+data.getFilter().getType().getId()+".tooltip"));
	}

	private String getFilterName(TargetFilter filter)
	{
		return I18n.format("ii.gui.emplacement.target_filter."+filter.getType().getId());
	}

	private String getFilterValue(TargetFilter filter)
	{
		if(filter instanceof EntityTypeTargetFilter)
			return I18n.format("ii.gui.emplacement.target_filter.entity_type."+
					((EntityTypeTargetFilter)filter).getTargetType().getName());
		if(filter instanceof BooleanTargetFilter)
			return I18n.format("ii.gui.emplacement.target_filter.boolean."+
					(((BooleanTargetFilter)filter).isExpected()?"true": "false"));
		if(filter instanceof FactionRelationshipTargetFilter)
			return I18n.format("ii.gui.emplacement.target_filter.relationship."+
					((FactionRelationshipTargetFilter)filter).getRelationship().getName());
		if(filter instanceof AnyTargetFilter)
			return I18n.format("ii.gui.emplacement.target_filter.any.value");
		String summary = filter.getSummary();
		return summary.isEmpty()?I18n.format("ii.gui.emplacement.target_filter.unset"): summary;
	}
}
