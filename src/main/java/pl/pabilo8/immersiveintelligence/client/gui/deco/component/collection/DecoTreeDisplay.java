package pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection;

import net.minecraft.client.renderer.GlStateManager;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoComponent;
import pl.pabilo8.immersiveintelligence.client.gui.deco.tree.*;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoSprite;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import javax.annotation.Nullable;
import java.util.*;

import static pl.pabilo8.immersiveintelligence.client.gui.deco.tree.TreeLayout.NodeLayoutInfo;
import static pl.pabilo8.immersiveintelligence.client.gui.deco.tree.TreeLayout.Orientation;

/**
 * Displays a pannable and zoomable Deco tree without storing layout state in the logical tree.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 24.08.2026
 * @ii-approved 0.3.1
 * @since 08.09.2025
 */
public class DecoTreeDisplay<T> extends DecoComponent<DecoTreeDisplay<T>>
{
	private static final int MARGIN = 20;
	private static final float MIN_ZOOM = 0.5f;
	private static final float MAX_ZOOM = 2f;
	private static final float ZOOM_FACTOR = 1.1f;

	@Nullable
	private IDecoTree<T> tree;
	private IDecoTreeNodeRenderer<T> nodeRenderer = defaultRenderer();
	@Nullable
	private TreeLayout<T> treeLayout;
	private Orientation layoutOrientation = Orientation.HORIZONTAL_LEFT_TO_RIGHT;
	protected DecoSprite background = DecoSprite.atlasSprite(DecoTextures.BG_DARK, 64, true);
	private boolean virtualRoot = true;

	@Nullable
	private IDecoTreeNode<T> hoveredNode;
	private float zoom = 1f;
	private int offsetX, offsetY;
	private boolean dragging;
	private int dragStartX, dragStartY, offsetXStart, offsetYStart;
	private int dragMinX, dragMaxX, dragMinY, dragMaxY;
	private boolean resetView = true;

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

	@SuppressWarnings("unchecked")
	private static <T> IDecoTreeNodeRenderer<T> defaultRenderer()
	{
		return (IDecoTreeNodeRenderer<T>)new DefaultTreeNodeRenderer();
	}

	/**
	 * Sets the tree and resets the view on the next layout.
	 */
	public DecoTreeDisplay<T> withTree(@Nullable IDecoTree<T> tree)
	{
		this.tree = tree;
		this.hoveredNode = null;
		invalidateLayout(true);
		return this;
	}

	/**
	 * Sets the renderer that also defines the layout node size.
	 */
	public DecoTreeDisplay<T> withNodeRenderer(IDecoTreeNodeRenderer<T> renderer)
	{
		this.nodeRenderer = Objects.requireNonNull(renderer, "renderer");
		invalidateLayout(true);
		return this;
	}

	/**
	 * Sets the tree orientation.
	 */
	public DecoTreeDisplay<T> withLayoutOrientation(Orientation orientation)
	{
		this.layoutOrientation = orientation==null?Orientation.HORIZONTAL_LEFT_TO_RIGHT: orientation;
		invalidateLayout(true);
		return this;
	}

	/**
	 * Sets whether the synthetic root connector is visible.
	 */
	public DecoTreeDisplay<T> withVirtualRoot(boolean virtualRoot)
	{
		this.virtualRoot = virtualRoot;
		if(initialized)
		{
			updateDragBounds();
			clampOffsets();
		}
		return this;
	}

	/**
	 * Sets the display background.
	 */
	public DecoTreeDisplay<T> withBackground(@Nullable DecoSprite background)
	{
		this.background = background;
		return this;
	}

	/**
	 * Recalculates positions after a structural tree edit and preserves pan and zoom.
	 */
	public DecoTreeDisplay<T> refreshLayout()
	{
		treeLayout = null;
		hoveredNode = null;
		if(tree!=null&&initialized)
		{
			layoutTree();
			updateDragBounds();
			clampOffsets();
		}
		else
			initialized = false;
		return this;
	}

	@Override
	public DecoTreeDisplay<T> withSize(int width, int height)
	{
		super.withSize(width, height);
		invalidateLayout(false);
		return this;
	}

	@Override
	public DecoTreeDisplay<T> withWidth(int width)
	{
		super.withWidth(width);
		invalidateLayout(false);
		return this;
	}

	@Override
	public DecoTreeDisplay<T> withHeight(int height)
	{
		super.withHeight(height);
		invalidateLayout(false);
		return this;
	}

	private void invalidateLayout(boolean resetView)
	{
		treeLayout = null;
		this.resetView |= resetView;
		initialized = false;
	}

	private void layoutTree()
	{
		if(tree==null)
			return;
		treeLayout = new TreeLayout<>(tree)
				.withOrientation(layoutOrientation)
				.withNodeSize(nodeRenderer.getNodeWidth(), nodeRenderer.getNodeHeight());
		treeLayout.calculateLayout(Math.max(1, width-MARGIN*2), Math.max(1, height-MARGIN*2));
	}

	private TreeLayout.LayoutBounds getDisplayedBounds()
	{
		if(treeLayout==null)
			return new TreeLayout.LayoutBounds(0, 0, 0, 0);
		if(virtualRoot)
			return treeLayout.getBounds();

		Collection<NodeLayoutInfo> infos = treeLayout.getAllNodeInfo();
		if(infos.isEmpty())
			return new TreeLayout.LayoutBounds(0, 0, 0, 0);
		int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
		for(NodeLayoutInfo info : infos)
		{
			minX = Math.min(minX, info.x);
			minY = Math.min(minY, info.y);
			maxX = Math.max(maxX, info.getRight());
			maxY = Math.max(maxY, info.getBottom());
		}
		return new TreeLayout.LayoutBounds(minX, minY, maxX, maxY);
	}

	private void resetView()
	{
		TreeLayout.LayoutBounds bounds = getDisplayedBounds();
		int viewW = Math.max(1, width-MARGIN*2);
		int viewH = Math.max(1, height-MARGIN*2);
		int contentW = Math.max(1, bounds.getWidth());
		int contentH = Math.max(1, bounds.getHeight());
		zoom = Math.max(MIN_ZOOM, Math.min(1f, Math.min(viewW/(float)contentW, viewH/(float)contentH)));
		offsetX = Math.round((viewW-(bounds.minX+bounds.maxX)*zoom)/2f);
		offsetY = Math.round((viewH-(bounds.minY+bounds.maxY)*zoom)/2f);
		updateDragBounds();
		clampOffsets();
	}

	private void updateDragBounds()
	{
		if(treeLayout==null)
			return;
		TreeLayout.LayoutBounds bounds = getDisplayedBounds();
		int viewW = Math.max(1, width-MARGIN*2);
		int viewH = Math.max(1, height-MARGIN*2);
		float scaledW = bounds.getWidth()*zoom;
		float scaledH = bounds.getHeight()*zoom;

		if(scaledW > viewW)
		{
			dragMinX = (int)Math.floor(viewW-bounds.maxX*zoom);
			dragMaxX = (int)Math.ceil(-bounds.minX*zoom);
		}
		else
			dragMinX = dragMaxX = Math.round((viewW-(bounds.minX+bounds.maxX)*zoom)/2f);

		if(scaledH > viewH)
		{
			dragMinY = (int)Math.floor(viewH-bounds.maxY*zoom);
			dragMaxY = (int)Math.ceil(-bounds.minY*zoom);
		}
		else
			dragMinY = dragMaxY = Math.round((viewH-(bounds.minY+bounds.maxY)*zoom)/2f);
	}

	private void clampOffsets()
	{
		offsetX = Math.min(dragMaxX, Math.max(dragMinX, offsetX));
		offsetY = Math.min(dragMaxY, Math.max(dragMinY, offsetY));
	}

	private boolean handleScrolling(DecoTreeDisplay<T> gui, int mouseScroll, int mouseX, int mouseY)
	{
		if(mouseScroll==0||treeLayout==null)
			return false;

		float oldZoom = zoom;
		float worldX = (mouseX-x-MARGIN-offsetX)/oldZoom;
		float worldY = (mouseY-y-MARGIN-offsetY)/oldZoom;
		zoom = Math.max(MIN_ZOOM, Math.min(MAX_ZOOM, mouseScroll > 0?zoom*ZOOM_FACTOR: zoom/ZOOM_FACTOR));
		if(zoom==oldZoom)
			return true;

		offsetX = Math.round(mouseX-x-MARGIN-worldX*zoom);
		offsetY = Math.round(mouseY-y-MARGIN-worldY*zoom);
		updateDragBounds();
		clampOffsets();
		return true;
	}

	private boolean handleMousePress(DecoTreeDisplay<T> gui, MouseButton button, int mouseX, int mouseY)
	{
		if(button!=MouseButton.LEFT)
			return false;
		updateHoveredNode(mouseX, mouseY);
		if(tree!=null&&hoveredNode!=null)
		{
			tree.onNodeClicked(hoveredNode);
			return true;
		}

		dragging = true;
		dragStartX = mouseX;
		dragStartY = mouseY;
		offsetXStart = offsetX;
		offsetYStart = offsetY;
		return true;
	}

	private boolean handleMouseDrag(DecoTreeDisplay<T> gui, MouseButton button, int mouseX, int mouseY)
	{
		if(!dragging||button!=MouseButton.LEFT)
			return false;
		offsetX = offsetXStart+mouseX-dragStartX;
		offsetY = offsetYStart+mouseY-dragStartY;
		clampOffsets();
		return true;
	}

	private boolean handleMouseRelease(DecoTreeDisplay<T> gui, MouseButton button, int mouseX, int mouseY)
	{
		if(button!=MouseButton.LEFT)
			return false;
		dragging = false;
		return true;
	}

	private void updateHoveredNode(int mouseX, int mouseY)
	{
		hoveredNode = null;
		if(tree==null||treeLayout==null||mouseX < x||mouseX > x+width||mouseY < y||mouseY > y+height)
		{
			if(tree!=null)
				tree.setHoveredNode(null);
			return;
		}

		float transformedX = (mouseX-x-MARGIN-offsetX)/zoom;
		float transformedY = (mouseY-y-MARGIN-offsetY)/zoom;
		for(IDecoTreeNode<T> node : tree.getAllNodes())
		{
			NodeLayoutInfo info = treeLayout.getNodeInfo(node);
			if(info!=null&&transformedX >= info.x&&transformedX <= info.getRight()
					&&transformedY >= info.y&&transformedY <= info.getBottom())
			{
				hoveredNode = node;
				break;
			}
		}
		tree.setHoveredNode(hoveredNode);
	}

	private Collection<String> getTreeTooltip(DecoTreeDisplay<T> gui)
	{
		return hoveredNode==null?Collections.emptyList(): nodeRenderer.getTooltip(hoveredNode);
	}

	@Override
	protected boolean initialize()
	{
		layoutTree();
		if(treeLayout!=null)
		{
			if(resetView)
				resetView();
			else
			{
				updateDragBounds();
				clampOffsets();
			}
		}
		resetView = false;
		return true;
	}

	@Override
	protected void draw(int mouseX, int mouseY, float partialTicks)
	{
		bindAtlas();
		if(background!=null)
			IIDrawUtils.startTexturedColored()
					.drawConnectedTexColorRect(x, y, width, height, IIColor.WHITE,
							background.getSizeX(), background.getSizeY(), background.getSizeX()/4, background.getSizeY()/4,
							background.getMapUV())
					.finish();

		if(tree==null||treeLayout==null)
			return;
		updateHoveredNode(mouseX, mouseY);

		List<IDecoTreeNode<T>> nodes = new ArrayList<>(tree.getAllNodes());
		Collection<IDecoTreeNode<T>> activeNodes = new HashSet<>(tree.getActiveNodes());
		NodeLayoutInfo rootInfo = treeLayout.getRootNodeInfo();

		if(parentGui!=null)
			parentGui.scissorStart(x, y, width, height);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x+MARGIN+offsetX, y+MARGIN+offsetY, 0);
		GlStateManager.scale(zoom, zoom, 1f);
		GlStateManager.disableTexture2D();

		if(virtualRoot)
			for(IDecoTreeNode<T> root : tree.getRootNodes())
			{
				NodeLayoutInfo info = treeLayout.getNodeInfo(root);
				if(info!=null)
					nodeRenderer.renderConnection(rootInfo, info, layoutOrientation, true);
			}

		for(IDecoTreeNode<T> node : nodes)
		{
			NodeLayoutInfo nodeInfo = treeLayout.getNodeInfo(node);
			if(nodeInfo==null)
				continue;
			for(IDecoTreeNode<T> dependency : node.getDependencies())
			{
				NodeLayoutInfo dependencyInfo = treeLayout.getNodeInfo(dependency);
				if(dependencyInfo!=null)
					nodeRenderer.renderConnection(dependencyInfo, nodeInfo, layoutOrientation,
							node.isActive()&&dependency.isActive());
			}
		}
		GlStateManager.enableTexture2D();

		for(IDecoTreeNode<T> node : nodes)
		{
			NodeLayoutInfo info = treeLayout.getNodeInfo(node);
			if(info!=null)
				nodeRenderer.renderNode(node, info.x, info.y, hoveredNode==node, node.isActive(), node.isAvailable(activeNodes));
		}
		if(virtualRoot)
			nodeRenderer.renderRootNode(rootInfo.x, rootInfo.y);

		GlStateManager.popMatrix();
		if(parentGui!=null)
			parentGui.scissorEnd();
	}

	@Override
	public void cleanup()
	{
		if(tree!=null)
			tree.setHoveredNode(null);
		tree = null;
		nodeRenderer = defaultRenderer();
		treeLayout = null;
		hoveredNode = null;
		dragging = false;
		zoom = 1f;
		offsetX = offsetY = 0;
		resetView = true;
		initialized = false;
	}
}
