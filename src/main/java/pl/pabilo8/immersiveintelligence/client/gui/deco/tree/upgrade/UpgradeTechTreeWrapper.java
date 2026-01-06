package pl.pabilo8.immersiveintelligence.client.gui.deco.tree.upgrade;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;
import pl.pabilo8.immersiveintelligence.api.upgrade.IUpgradableDevice;
import pl.pabilo8.immersiveintelligence.api.upgrade.Upgrade;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.tree.DecoTreeNodeRenderer;
import pl.pabilo8.immersiveintelligence.client.gui.deco.tree.IDecoTree;
import pl.pabilo8.immersiveintelligence.client.gui.deco.tree.IDecoTreeNode;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBeginMachineUpgrade;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

import static pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree.UpgradeTreeNode;

/**
 * Wraps the server-side UpgradeTechTree for display in DecoTreeDisplay.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 09.12.2025
 */
public class UpgradeTechTreeWrapper implements IDecoTree
{
	private static final String KEY_TOOLTIP = IIReference.DESCRIPTION_KEY+"upgrade.";
	private final UpgradeTechTree techTree;
	private final IUpgradableDevice device;
	private final Map<UpgradeTreeNode, UpgradeTreeNodeWrapper> wrapperMap = new HashMap<>();
	@Nullable
	private UpgradeTreeNodeWrapper hoveredNode = null;

	public UpgradeTechTreeWrapper(@Nonnull UpgradeTechTree techTree, @Nonnull IUpgradableDevice device)
	{
		this.techTree = techTree;
		this.device = device;

		//Create wrapper for each node
		for(UpgradeTreeNode node : techTree.getAllUpgrades())
			wrapperMap.put(node, new UpgradeTreeNodeWrapper(node));

		//Setup dependencies and lockouts in wrappers
		for(Map.Entry<UpgradeTreeNode, UpgradeTreeNodeWrapper> entry : wrapperMap.entrySet())
		{
			UpgradeTreeNode original = entry.getKey();
			UpgradeTreeNodeWrapper wrapper = entry.getValue();
			//Convert dependencies
			for(UpgradeTreeNode dep : original.getDependencies())
				wrapper.dependencies.add(wrapperMap.get(dep));
			//Convert lockouts
			for(UpgradeTreeNode lock : original.getLocksOut())
				wrapper.lockOuts.add(wrapperMap.get(lock));
		}
	}

	@Nonnull
	@Override
	public Collection<IDecoTreeNode> getAllNodes()
	{
		return new ArrayList<>(wrapperMap.values());
	}

	@Nonnull
	@Override
	public Collection<IDecoTreeNode> getRootNodes()
	{
		return wrapperMap.entrySet().stream()
				.filter(entry -> entry.getKey().getDependencies().isEmpty())
				.map(Map.Entry::getValue).collect(Collectors.toList());
	}

	@Nonnull
	@Override
	public Collection<IDecoTreeNode> getActiveNodes()
	{
		List<Upgrade> installedUpgrades = device.getAllInstalledUpgrades();
		return wrapperMap.entrySet().stream()
				.filter(entry -> installedUpgrades.contains(entry.getKey().getUpgrade()))
				.map(Map.Entry::getValue)
				.collect(Collectors.toList());
	}

	@Nullable
	@Override
	public IDecoTreeNode getHoveredNode()
	{
		return hoveredNode;
	}

	@Override
	public void setHoveredNode(@Nullable IDecoTreeNode node)
	{
		this.hoveredNode = (UpgradeTreeNodeWrapper)node;
	}

	@Override
	public void onNodeClicked(@Nonnull IDecoTreeNode node)
	{
		UpgradeTreeNodeWrapper wrapper = (UpgradeTreeNodeWrapper)node;
		Upgrade upgrade = wrapper.original.getUpgrade();
		//Install if not installed, else uninstall
		IIPacketHandler.sendToServer(new MessageBeginMachineUpgrade((TileEntity)device, upgrade, ClientUtils.mc().player,
				!device.isUpgradeInstalled(upgrade)
		));
	}

	/**
	 * Wrapper class for UpgradeTechTree.UpgradeTreeNode
	 */
	private class UpgradeTreeNodeWrapper implements IDecoTreeNode
	{
		private final UpgradeTreeNode original;
		final Set<IDecoTreeNode> dependencies = new HashSet<>();
		final Set<IDecoTreeNode> lockOuts = new HashSet<>();
		private int x = 0;
		private int y = 0;

		public UpgradeTreeNodeWrapper(UpgradeTreeNode original)
		{
			this.original = original;
		}

		@Nonnull
		@Override
		public String getId()
		{
			return original.getUpgrade().getName();
		}

		@Nonnull
		@Override
		public String getDisplayName()
		{
			return original.getUpgrade().getLocalizedName();
		}

		@Nonnull
		@Override
		public Set<IDecoTreeNode> getDependencies()
		{
			return dependencies;
		}

		@Nonnull
		@Override
		public Set<IDecoTreeNode> getLockOuts()
		{
			return lockOuts;
		}

		@Nullable
		@Override
		public Collection<String> getTooltip()
		{
			Upgrade upgrade = original.getUpgrade();
			List<String> tooltip = new ArrayList<>();
			//Name
			tooltip.add(upgrade.getLocalizedName());

			//Description
			List<String> description = IIClientUtils.fontRegular.listFormattedStringToWidth(I18n.format(String.format("machineupgrade.%s.%s.desc",
					upgrade.getId().getResourceDomain(), upgrade.getId().getResourcePath().replace("/", "."))), 200);
			for(String line : description)
				tooltip.add(TextFormatting.GRAY+""+TextFormatting.ITALIC+line);

			//Parameters
			tooltip.add(IIReference.COLOR_IMMERSIVE_ORANGE.getHexCol(upgrade.getPurpose().getLocalizedName()));
			tooltip.add(IIReference.COLOR_IMMERSIVE_ORANGE.withBrightness(0.5f)
					.getHexCol(original.getTier().getLocalizedName()));
			tooltip.addAll(upgrade.getLocalizedBenefitNames());
			//Dependencies and lockouts
			if(!dependencies.isEmpty())
				if(ItemTooltipHandler.addExpandableTooltip(Keyboard.KEY_LSHIFT, KEY_TOOLTIP+"dependencies_hold", tooltip))
				{
					tooltip.add(I18n.format(KEY_TOOLTIP+"dependencies"));
					for(IDecoTreeNode dep : dependencies)
						tooltip.add("- "+dep.getDisplayName());
				}
			if(!lockOuts.isEmpty())
				if(ItemTooltipHandler.addExpandableTooltip(Keyboard.KEY_LCONTROL, KEY_TOOLTIP+"incompatible_hold", tooltip))
				{
					tooltip.add(I18n.format(KEY_TOOLTIP+"incompatible"));
					for(IDecoTreeNode lock : lockOuts)
						tooltip.add("- "+lock.getDisplayName());
				}

			return tooltip;
		}

		@Override
		public int getX()
		{
			return x;
		}

		@Override
		public int getY()
		{
			return y;
		}

		@Override
		public void setPosition(int x, int y)
		{
			this.x = x;
			this.y = y;
		}

		@Override
		public int getWidth()
		{
			return DecoTreeNodeRenderer.NODE_WIDTH;
		}

		@Override
		public int getHeight()
		{
			return DecoTreeNodeRenderer.NODE_HEIGHT;
		}

		@Override
		public boolean isActive()
		{
			return device.getAllInstalledUpgrades().contains(original.getUpgrade()); //Fixed method name
		}

		@Override
		public boolean isAvailable(@Nonnull Collection<IDecoTreeNode> activeNodes)
		{
			//Check if all dependencies are active
			for(IDecoTreeNode dep : dependencies)
				if(!activeNodes.contains(dep))
					return false;

			//Check if no lockouts are active
			for(IDecoTreeNode lock : lockOuts)
				if(activeNodes.contains(lock))
					return false;

			return true;
		}

		@Nullable
		@Override
		public Object getUserData()
		{
			return original.getUpgrade();
		}
	}
}
