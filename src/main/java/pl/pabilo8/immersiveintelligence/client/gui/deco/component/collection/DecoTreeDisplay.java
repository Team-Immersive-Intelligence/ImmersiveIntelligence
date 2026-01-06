package pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection;

import net.minecraft.client.renderer.GlStateManager;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoComponent;
import pl.pabilo8.immersiveintelligence.client.gui.deco.tree.*;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoSprite;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;

import static pl.pabilo8.immersiveintelligence.client.gui.deco.tree.TreeLayout.NodeLayoutInfo;
import static pl.pabilo8.immersiveintelligence.client.gui.deco.tree.TreeLayout.Orientation;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 08.09.2025
 */
public class DecoTreeDisplay extends DecoComponent<DecoTreeDisplay>
{
	@Nullable
	private IDecoTree tree;
	private IDecoTreeNodeRenderer nodeRenderer = new DecoTreeNodeRenderer();
	private TreeLayout treeLayout;
	private Orientation layoutOrientation = Orientation.HORIZONTAL_LEFT_TO_RIGHT;
	protected DecoSprite background = DecoSprite.atlasSprite(DecoTextures.GUI_BG_DARK, 64, true);

	private float zoom = 1.0f;
	private int offsetX = 0;
	private int offsetY = 0;
	private boolean isDragging = false;
	private int dragStartX, dragStartY;
	private int offsetXStart, offsetYStart;

	public DecoTreeDisplay(int x, int y)
	{
		super(x, y);
		withSize(128, 128);
		withOnTooltip(this::getTreeTooltip);
		withOnScroll(this::handleScrolling);
		withOnPressed(this::handleMousePress);
		withOnDragged(this::handleMouseDrag);
		withOnReleased(this::handleMouseRelease);
	}

	/**
	 * Sets the tree to be displayed.
	 *
	 * @param tree The tree structure
	 * @return this
	 */
	public DecoTreeDisplay withTree(@Nullable IDecoTree tree)
	{
		this.tree = tree;
		this.initialized = false;
		return this;
	}

	/**
	 * Sets the node renderer.
	 *
	 * @param renderer The node renderer
	 * @return this
	 */
	public DecoTreeDisplay withNodeRenderer(IDecoTreeNodeRenderer renderer)
	{
		this.nodeRenderer = renderer;
		this.initialized = false;
		return this;
	}

	/**
	 * Sets the layout orientation.
	 *
	 * @param orientation The layout orientation
	 * @return this
	 */
	public DecoTreeDisplay withLayoutOrientation(Orientation orientation)
	{
		this.layoutOrientation = orientation;
		this.initialized = false;
		return this;
	}

	/**
	 * Sets a background texture for the scenario display
	 */
	public DecoTreeDisplay withBackground(@Nullable DecoSprite background)
	{
		this.background = background;
		return this;
	}


	private void layoutTree()
	{
		if(tree==null)
			return;

		if(treeLayout==null||treeLayout.tree!=this.tree)
			this.treeLayout = new TreeLayout(tree)
					.withOrientation(layoutOrientation)
					.withNodeSize(DecoTreeNodeRenderer.NODE_WIDTH, DecoTreeNodeRenderer.NODE_HEIGHT);

		//Calculate layout within our component bounds (with margins)
		this.treeLayout.calculateLayout(this.width-8, this.height-8);
	}

	private boolean handleScrolling(DecoTreeDisplay gui, int mouseScroll, int mouseX, int mouseY)
	{
		float zoomFactor = 1.1f;
		if(mouseScroll > 0)
			zoom *= zoomFactor;
		else if(mouseScroll < 0)
			zoom /= zoomFactor;
		zoom = Math.max(0.5f, Math.min(2.0f, zoom));
		return true;
	}

	private boolean handleMousePress(DecoTreeDisplay gui, MouseButton mb, int mouseX, int mouseY)
	{
		if(mb==MouseButton.LEFT)
		{
			if(tree!=null)
			{
				//Handle node click
				IDecoTreeNode hovered = tree.getHoveredNode();
				if(hovered!=null)
				{
					tree.onNodeClicked(hovered);
					return true;
				}
			}
			isDragging = true;
			dragStartX = mouseX;
			dragStartY = mouseY;
			offsetXStart = offsetX;
			offsetYStart = offsetY;
			return true;
		}
		return false;
	}

	private boolean handleMouseDrag(DecoTreeDisplay gui, MouseButton mb, int mouseX, int mouseY)
	{
		if(isDragging&&mb==MouseButton.LEFT)
		{
			offsetX = offsetXStart+(mouseX-dragStartX);
			offsetY = offsetYStart+(mouseY-dragStartY);
			return true;
		}
		return false;
	}

	private boolean handleMouseRelease(DecoTreeDisplay gui, MouseButton mb, int mouseX, int mouseY)
	{
		if(mb==MouseButton.LEFT)
		{
			isDragging = false;
			return true;
		}
		return false;
	}

	private void updateHoveredNode(int mouseX, int mouseY)
	{
		if(tree==null||treeLayout==null) return;

		//Transform mouse coordinates to component-relative coordinates
		float transformedX = (mouseX-x-20-offsetX)/zoom;
		float transformedY = (mouseY-y-20-offsetY)/zoom;

		IDecoTreeNode hovered = null;
		for(IDecoTreeNode node : tree.getAllNodes())
		{
			NodeLayoutInfo info = treeLayout.getNodeInfo(node);
			if(info==null) continue;

			if(transformedX >= info.x&&transformedX <= info.x+info.width&&
					transformedY >= info.y&&transformedY <= info.y+info.height)
			{
				hovered = node;
				break;
			}
		}

		tree.setHoveredNode(hovered);
	}

	private Collection<String> getTreeTooltip(DecoTreeDisplay DecoTreeDisplay)
	{
		if(tree==null) return Collections.emptyList();

		IDecoTreeNode hovered = tree.getHoveredNode();
		if(hovered!=null)
		{
			Collection<String> tooltip = hovered.getTooltip();
			if(tooltip!=null)
				return tooltip;
		}
		return Collections.emptyList();
	}

	@Override
	protected boolean initialize()
	{
		layoutTree();
		return true;
	}

	@Override
	protected void draw(int mouseX, int mouseY, float partialTicks)
	{
		assert tree!=null;

		//Update hovered node
		updateHoveredNode(mouseX, mouseY);

		bindAtlas();
		if(background!=null)
		{
			IIDrawUtils draw = IIDrawUtils.startTexturedColored();
			draw.drawConnectedTexColorRect(x, y, width, height, IIColor.WHITE,
					background.getSizeX(), background.getSizeY(), background.getSizeX()/4, background.getSizeY()/4,
					background.getMapUV());
			draw.finish();
		}
		GlStateManager.disableTexture2D();

		if(parentGui!=null)
			parentGui.scissorStart(x, y, width, height);
		//Apply component-relative transformations
		GlStateManager.pushMatrix();
		GlStateManager.translate(x+20, y+20, 0); //Apply margin

		//Apply zoom and pan transformations
		GlStateManager.translate(offsetX, offsetY, 0);
		GlStateManager.scale(zoom, zoom, 1.0f);

		Collection<IDecoTreeNode> activeNodes = tree.getActiveNodes();
		DecoTreeNodeRenderer defaultRenderer = (nodeRenderer instanceof DecoTreeNodeRenderer)?
				(DecoTreeNodeRenderer)nodeRenderer: new DecoTreeNodeRenderer();

		//Get virtual root position
		NodeLayoutInfo rootInfo = treeLayout.getRootNodeInfo();

		//Draw connections from virtual root to root nodes
		for(IDecoTreeNode baseNode : tree.getRootNodes())
		{
			NodeLayoutInfo baseInfo = treeLayout.getNodeInfo(baseNode);
			if(baseInfo==null)
				continue;
			defaultRenderer.renderConnection(rootInfo, baseInfo, layoutOrientation, true);
		}

		//Draw connections between dependent nodes
		for(IDecoTreeNode node : tree.getAllNodes())
		{
			NodeLayoutInfo nodeInfo = treeLayout.getNodeInfo(node);
			if(nodeInfo==null)
				continue;

			for(IDecoTreeNode dependency : node.getDependencies())
			{
				NodeLayoutInfo depInfo = treeLayout.getNodeInfo(dependency);
				if(depInfo==null)
					continue;

				boolean connectionActive = node.isActive()||dependency.isActive();
				defaultRenderer.renderConnection(depInfo, nodeInfo, layoutOrientation, connectionActive);
			}
		}
		GlStateManager.enableTexture2D();

		//Draw nodes
		for(IDecoTreeNode node : tree.getAllNodes())
		{
			NodeLayoutInfo info = treeLayout.getNodeInfo(node);
			if(info==null) continue;

			boolean isHovered = tree.getHoveredNode()==node;
			boolean isActive = node.isActive();
			boolean isAvailable = node.isAvailable(activeNodes);

			nodeRenderer.render(node, info.x, info.y, isHovered, isActive, isAvailable);
		}

		//Draw virtual root
		nodeRenderer.renderRootNode(rootInfo.x, rootInfo.y);

		GlStateManager.popMatrix();
		if(parentGui!=null)
			parentGui.scissorEnd();
	}

	@Override
	public void cleanup()
	{
		tree = null;
		nodeRenderer = new DecoTreeNodeRenderer();
		treeLayout = null;
	}
}
