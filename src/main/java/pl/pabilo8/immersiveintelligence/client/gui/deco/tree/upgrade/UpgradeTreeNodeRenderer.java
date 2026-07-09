package pl.pabilo8.immersiveintelligence.client.gui.deco.tree.upgrade;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;
import pl.pabilo8.immersiveintelligence.api.upgrade.Upgrade;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.tree.AbstractTreeNodeRenderer;
import pl.pabilo8.immersiveintelligence.client.gui.deco.tree.IDecoTreeNode;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UpgradeTreeNodeRenderer extends AbstractTreeNodeRenderer<Upgrade>
{
	public static final int NODE_WIDTH = 20;
	public static final int NODE_HEIGHT = 20;

	private static final String KEY_TOOLTIP = IIReference.DESCRIPTION_KEY+"upgrade.";

	private final IIColor activeColor = IIColor.fromHex("497d49");
	private final IIColor availableColor = IIColor.fromHex("888888");
	private final IIColor unavailableColor = IIColor.fromHex("3c3c3c");
	private final IIColor hoverColor = IIReference.COLOR_IMMERSIVE_ORANGE.withBrightness(0.75f);

	@Override
	public void renderNode(@Nonnull IDecoTreeNode<Upgrade> node, int x, int y, boolean isHovered, boolean isActive, boolean isAvailable)
	{
		IIColor bg;
		if(isActive)
			bg = activeColor;
		else if(isHovered)
			bg = hoverColor;
		else if(isAvailable)
			bg = availableColor;
		else
			bg = unavailableColor;

		// background
		IIDrawUtils draw = IIDrawUtils.startColored();
		draw.drawColorRect(x, y, NODE_WIDTH, NODE_HEIGHT, bg);
		draw.finish();

		Upgrade upgrade = node.getUserData();
		ClientUtils.bindAtlas();
		IIDrawUtils.startTexturedColored()
				.drawConnectedTexColorRect(x, y, 20, 20, bg, DecoTextures.SLOT_IE, 32, 32, 8, 8)
				.drawTexColorRect(x+2, y+2, 16, 16, !isAvailable?IIColor.MC_DARK_GRAY: IIColor.WHITE, upgrade.getIcon())
				.finish();
	}

	@Override
	@Nonnull
	public String getDisplayName(@Nonnull IDecoTreeNode<Upgrade> node)
	{
		return node.getUserData().getLocalizedName();
	}

	@Override
	@Nonnull
	public Collection<String> getTooltip(@Nonnull IDecoTreeNode<Upgrade> node)
	{
		Upgrade upgrade = node.getUserData();

		List<String> tooltip = new ArrayList<>();
		tooltip.add(upgrade.getLocalizedName());

		if(upgrade.isWorkInProgress())
		{
			tooltip.add(IIColor.MC_YELLOW.getHexCol(I18n.format("ie.manual.entry.wip_warning0")));
			tooltip.add(IIColor.MC_YELLOW.getHexCol(I18n.format("ie.manual.entry.wip_warning.upgrade")));
		}

		List<String> description = IIClientUtils.fontRegular.listFormattedStringToWidth(
				I18n.format(String.format("machineupgrade.%s.%s.desc",
						upgrade.getId().getResourceDomain(),
						upgrade.getId().getResourcePath().replace("/", "."))), 200);
		for(String line : description)
			tooltip.add(TextFormatting.GRAY+""+TextFormatting.ITALIC+line);

		tooltip.add(IIReference.COLOR_IMMERSIVE_ORANGE.getHexCol(upgrade.getPurpose().getLocalizedName()));
		tooltip.addAll(upgrade.getLocalizedBenefitNames());

		if(!node.getDependencies().isEmpty())
			if(ItemTooltipHandler.addExpandableTooltip(Keyboard.KEY_LSHIFT, KEY_TOOLTIP+"dependencies_hold", tooltip))
			{
				tooltip.add(I18n.format(KEY_TOOLTIP+"dependencies"));
				for(IDecoTreeNode<Upgrade> dep : node.getDependencies())
					tooltip.add("- "+getDisplayName(dep));
			}

		if(!node.getLockOuts().isEmpty())
			if(ItemTooltipHandler.addExpandableTooltip(Keyboard.KEY_LCONTROL, KEY_TOOLTIP+"incompatible_hold", tooltip))
			{
				tooltip.add(I18n.format(KEY_TOOLTIP+"incompatible"));
				for(IDecoTreeNode<Upgrade> lock : node.getLockOuts())
					tooltip.add("- "+getDisplayName(lock));
			}

		return tooltip;
	}
}

