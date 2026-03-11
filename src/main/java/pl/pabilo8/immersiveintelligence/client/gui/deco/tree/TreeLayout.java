package pl.pabilo8.immersiveintelligence.client.gui.deco.tree;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Layout engine for tree structures. Arranges nodes in hierarchical layouts.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 09.12.2025
 */
public class TreeLayout<T>
{
	public enum Orientation
	{
		HORIZONTAL_LEFT_TO_RIGHT,
		HORIZONTAL_RIGHT_TO_LEFT,
		VERTICAL_TOP_TO_BOTTOM,
		VERTICAL_BOTTOM_TO_TOP
	}

	private Orientation orientation = Orientation.HORIZONTAL_LEFT_TO_RIGHT;
	private int nodeWidth = 20;
	private int nodeHeight = 20;
	private int nodeLevelSpacing = 16;
	private int nodeSameLevelSpacing = 4;
	private boolean alignChildrenToParent = true;

	public final IDecoTree<T> tree;
	private final Map<IDecoTreeNode<T>, NodeLayoutInfo> nodeInfo = new HashMap<>();
	private NodeLayoutInfo rootInfo = null;

	private LayoutBounds bounds = new LayoutBounds(0, 0, 0, 0);

	public TreeLayout(@Nonnull IDecoTree<T> tree)
	{
		this.tree = tree;
	}

	/**
	 * Calculates the layout for all nodes in the tree.
	 */
	public void calculateLayout(int availableWidth, int availableHeight)
	{
		nodeInfo.clear();

		//Build parent-child relationships
		Map<IDecoTreeNode<T>, List<IDecoTreeNode<T>>> childrenMap = buildChildrenMap();
		//Group nodes by level
		Map<Integer, List<IDecoTreeNode<T>>> levels = calculateLevels();
		//Calculate positions for each level
		switch(orientation)
		{
			case HORIZONTAL_LEFT_TO_RIGHT:
				calculateHorizontalLayout(levels, availableWidth, availableHeight, false);
				break;
			case HORIZONTAL_RIGHT_TO_LEFT:
				calculateHorizontalLayout(levels, availableWidth, availableHeight, true);
				break;
			case VERTICAL_TOP_TO_BOTTOM:
				calculateVerticalLayout(levels, availableWidth, availableHeight, false);
				break;
			case VERTICAL_BOTTOM_TO_TOP:
				calculateVerticalLayout(levels, availableWidth, availableHeight, true);
				break;
		}

		//Adjust children to be closer to their parents
		if(alignChildrenToParent)
			adjustChildrenPositions(childrenMap);

		updateBounds();
	}

	/**
	 * Builds a map of parent -> children relationships.
	 */
	private Map<IDecoTreeNode<T>, List<IDecoTreeNode<T>>> buildChildrenMap()
	{
		Map<IDecoTreeNode<T>, List<IDecoTreeNode<T>>> childrenMap = new HashMap<>();

		for(IDecoTreeNode<T> node : tree.getAllNodes())
			for(IDecoTreeNode<T> dependency : node.getDependencies())
				childrenMap.computeIfAbsent(dependency, k -> new ArrayList<>()).add(node);

		return childrenMap;
	}

	/**
	 * Groups nodes by their level in the tree (distance from root).
	 */
	private Map<Integer, List<IDecoTreeNode<T>>> calculateLevels()
	{
		Map<IDecoTreeNode<T>, Integer> nodeLevels = new HashMap<>();
		Map<Integer, List<IDecoTreeNode<T>>> levels = new TreeMap<>();

		//Start with root nodes
		Queue<IDecoTreeNode<T>> queue = new LinkedList<>(tree.getRootNodes());
		for(IDecoTreeNode<T> root : tree.getRootNodes())
		{
			nodeLevels.put(root, 0);
			levels.computeIfAbsent(0, k -> new ArrayList<>()).add(root);
		}

		//BFS to assign levels
		while(!queue.isEmpty())
		{
			IDecoTreeNode<T> current = queue.poll();
			int currentLevel = nodeLevels.get(current);

			//Find children
			for(IDecoTreeNode<T> node : tree.getAllNodes())
				if(node.getDependencies().contains(current)&&!nodeLevels.containsKey(node))
				{
					int newLevel = currentLevel+1;
					nodeLevels.put(node, newLevel);
					levels.computeIfAbsent(newLevel, k -> new ArrayList<>()).add(node);
					queue.add(node);
				}
		}

		//Handle orphaned nodes
		for(IDecoTreeNode<T> node : tree.getAllNodes())
			if(!nodeLevels.containsKey(node))
			{
				int minLevel = 0;
				for(IDecoTreeNode<T> dep : node.getDependencies())
				{
					Integer depLevel = nodeLevels.get(dep);
					if(depLevel!=null&&depLevel >= minLevel)
						minLevel = depLevel+1;
				}
				nodeLevels.put(node, minLevel);
				levels.computeIfAbsent(minLevel, k -> new ArrayList<>()).add(node);
			}

		return levels;
	}

	/**
	 * Calculates positions for horizontal layout.
	 */
	private void calculateHorizontalLayout(Map<Integer, List<IDecoTreeNode<T>>> levels, int availableWidth, int availableHeight, boolean reverse)
	{
		int levelWidth = nodeWidth+nodeLevelSpacing;
		//Position virtual root
		rootInfo = new NodeLayoutInfo();
		rootInfo.x = reverse?availableWidth-nodeWidth-levelWidth: nodeWidth+levelWidth;
		rootInfo.y = availableHeight/2;
		rootInfo.width = rootInfo.height = 0;
		rootInfo.level = -1;

		levels.forEach((key, nodes) -> {
			int level = key+1;

			//Calculate X position for this level
			int levelX;
			if(reverse)
				levelX = rootInfo.x+availableWidth-(level*levelWidth);
			else
				levelX = rootInfo.x+(level*levelWidth);

			//Calculate total height needed for this level
			int totalNodeHeight = nodes.size()*nodeHeight+(nodes.size()-1)*nodeSameLevelSpacing;
			int startY = (availableHeight-totalNodeHeight)/2;

			//Sort nodes by number of children for better visual flow
			nodes.sort((a, b) -> {
				long childrenA = tree.getAllNodes().stream()
						.filter(n -> n.getDependencies().contains(a)).count();
				long childrenB = tree.getAllNodes().stream()
						.filter(n -> n.getDependencies().contains(b)).count();
				return Long.compare(childrenB, childrenA); //More children first
			});

			//Position each node
			for(int i = 0; i < nodes.size(); i++)
			{
				IDecoTreeNode<T> node = nodes.get(i);
				int nodeY = startY+i*(nodeHeight+nodeSameLevelSpacing);

				NodeLayoutInfo info = new NodeLayoutInfo();
				info.x = levelX;
				info.y = nodeY;
				info.width = nodeWidth;
				info.height = nodeHeight;
				info.level = level;
				info.indexInLevel = i;

				nodeInfo.put(node, info);
				node.setPosition(levelX, nodeY);
			}
		});
	}

	/**
	 * Calculates positions for vertical layout.
	 */
	private void calculateVerticalLayout(Map<Integer, List<IDecoTreeNode<T>>> levels, int availableWidth, int availableHeight, boolean reverse)
	{
		int levelHeight = nodeHeight+nodeLevelSpacing;
		//Position virtual root
		rootInfo.x = availableWidth/2;
		rootInfo.y = reverse?availableHeight-nodeHeight-levelHeight: nodeHeight+levelHeight;

		levels.forEach((key, nodes) -> {
			int level = key+1;

			//Calculate Y position for this level
			int levelY;
			if(reverse)
				levelY = rootInfo.y+availableHeight-(level*levelHeight);
			else
				levelY = rootInfo.y+(level*levelHeight);

			//Calculate total width needed for this level
			int totalNodeWidth = nodes.size()*nodeWidth+(nodes.size()-1)*nodeSameLevelSpacing;
			int startX = (availableWidth-totalNodeWidth)/2;

			//Sort nodes by number of children for better visual flow
			nodes.sort((a, b) -> {
				long childrenA = tree.getAllNodes().stream()
						.filter(n -> n.getDependencies().contains(a)).count();
				long childrenB = tree.getAllNodes().stream()
						.filter(n -> n.getDependencies().contains(b)).count();
				return Long.compare(childrenB, childrenA); //More children first
			});

			//Position each node
			for(int i = 0; i < nodes.size(); i++)
			{
				IDecoTreeNode<T> node = nodes.get(i);
				int nodeX = startX+i*(nodeWidth+nodeSameLevelSpacing);

				NodeLayoutInfo info = new NodeLayoutInfo();
				info.x = nodeX;
				info.y = levelY;
				info.width = nodeWidth;
				info.height = nodeHeight;
				info.level = level;
				info.indexInLevel = i;

				nodeInfo.put(node, info);
				node.setPosition(nodeX, levelY);
			}
		});
	}

	/**
	 * Adjusts children positions to be closer to their parents on the Y-axis.
	 */
	private void adjustChildrenPositions(Map<IDecoTreeNode<T>, List<IDecoTreeNode<T>>> childrenMap)
	{
		boolean yAxis = orientation==Orientation.HORIZONTAL_LEFT_TO_RIGHT||orientation==Orientation.HORIZONTAL_RIGHT_TO_LEFT;

		for(Map.Entry<IDecoTreeNode<T>, List<IDecoTreeNode<T>>> entry : childrenMap.entrySet())
		{
			IDecoTreeNode<T> parent = entry.getKey();
			List<IDecoTreeNode<T>> children = entry.getValue();
			int total = children.size()*(yAxis?nodeHeight: nodeWidth)+(children.size()-1)*nodeSameLevelSpacing;
			for(int i = 0; i < children.size(); i++)
			{
				NodeLayoutInfo nodeInfo = getNodeInfo(children.get(i));
				if(yAxis)
					nodeInfo.y = parent.getY()-total/2+i*(nodeHeight+nodeSameLevelSpacing)+nodeHeight/2;
				else
					nodeInfo.x = parent.getX()-total/2+i*(nodeWidth+nodeSameLevelSpacing)+nodeWidth/2;
			}
		}
	}

	/**
	 * Gets layout information for a specific node.
	 */
	public NodeLayoutInfo getNodeInfo(IDecoTreeNode<T> node)
	{
		return nodeInfo.get(node);
	}

	/**
	 * Gets all node layout information.
	 */
	public Collection<NodeLayoutInfo> getAllNodeInfo()
	{
		return nodeInfo.values();
	}

	public NodeLayoutInfo getRootNodeInfo()
	{
		return rootInfo;
	}

	private void updateBounds()
	{
		if(nodeInfo.isEmpty())
		{
			bounds = new LayoutBounds(0, 0, 0, 0);
			return;
		}

		int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;

		for(NodeLayoutInfo info : nodeInfo.values())
		{
			minX = Math.min(minX, info.x);
			minY = Math.min(minY, info.y);
			maxX = Math.max(maxX, info.getRight());
			maxY = Math.max(maxY, info.getBottom());
		}

		bounds = new LayoutBounds(minX, minY, maxX, maxY);
	}

	public LayoutBounds getBounds()
	{
		return bounds;
	}

	public int getContentWidth()
	{
		return bounds.getWidth();
	}

	public int getContentHeight()
	{
		return bounds.getHeight();
	}

	//--- Setters ---//

	public TreeLayout<T> withOrientation(Orientation orientation)
	{
		this.orientation = orientation;
		return this;
	}

	public TreeLayout<T> withNodeSize(int width, int height)
	{
		this.nodeWidth = width;
		this.nodeHeight = height;
		return this;
	}

	public TreeLayout<T> withSpacing(int levelSpacing, int sameLevelSpacing)
	{
		this.nodeLevelSpacing = levelSpacing;
		this.nodeSameLevelSpacing = sameLevelSpacing;
		return this;
	}

	public TreeLayout<T> withParentAlignment(boolean align)
	{
		this.alignChildrenToParent = align;
		return this;
	}

	/**
	 * Information about a node's position and size in the layout.
	 */
	public static class NodeLayoutInfo
	{
		public int x;
		public int y;
		public int width;
		public int height;
		public int level;
		public int indexInLevel;

		public int getCenterX()
		{
			return x+width/2;
		}

		public int getCenterY()
		{
			return y+height/2;
		}

		public int getRight()
		{
			return x+width;
		}

		public int getBottom()
		{
			return y+height;
		}
	}

	public static class LayoutBounds
	{
		public final int minX, minY, maxX, maxY;

		public LayoutBounds(int minX, int minY, int maxX, int maxY)
		{
			this.minX = minX;
			this.minY = minY;
			this.maxX = maxX;
			this.maxY = maxY;
		}

		public int getWidth()
		{
			return maxX-minX;
		}

		public int getHeight()
		{
			return maxY-minY;
		}
	}
}
