package pl.pabilo8.immersiveintelligence.client.gui.deco.tree;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Calculates stable hierarchical layouts for Deco tree and DAG structures.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 24.08.2026
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
	private NodeLayoutInfo rootInfo = new NodeLayoutInfo();
	private LayoutBounds bounds = new LayoutBounds(0, 0, 0, 0);

	public TreeLayout(@Nonnull IDecoTree<T> tree)
	{
		this.tree = tree;
	}

	/**
	 * Calculates positions for the current tree snapshot.
	 */
	public void calculateLayout(int availableWidth, int availableHeight)
	{
		nodeInfo.clear();
		List<IDecoTreeNode<T>> nodes = new ArrayList<>(tree.getAllNodes());
		if(nodes.isEmpty())
		{
			rootInfo = new NodeLayoutInfo(0, 0, 0, 0, -1, -1);
			bounds = new LayoutBounds(0, 0, 0, 0);
			return;
		}

		Set<IDecoTreeNode<T>> nodeSet = new HashSet<>(nodes);
		Map<IDecoTreeNode<T>, List<IDecoTreeNode<T>>> children = buildChildrenMap(nodes, nodeSet);
		Map<IDecoTreeNode<T>, Integer> indegree = buildIndegree(nodes, nodeSet);
		List<IDecoTreeNode<T>> roots = collectRoots(nodes, indegree);

		if(alignChildrenToParent&&isForest(nodes, children, indegree))
			calculateForestLayout(roots, children);
		else
			calculateLevelLayout(nodes, children, indegree);

		applyOrientation();
		for(Map.Entry<IDecoTreeNode<T>, NodeLayoutInfo> entry : nodeInfo.entrySet())
			entry.getKey().setPosition(entry.getValue().x, entry.getValue().y);
		updateBounds();
	}

	private Map<IDecoTreeNode<T>, List<IDecoTreeNode<T>>> buildChildrenMap(List<IDecoTreeNode<T>> nodes, Set<IDecoTreeNode<T>> nodeSet)
	{
		Map<IDecoTreeNode<T>, List<IDecoTreeNode<T>>> children = new LinkedHashMap<>();
		for(IDecoTreeNode<T> node : nodes)
			children.put(node, new ArrayList<>());
		for(IDecoTreeNode<T> node : nodes)
			for(IDecoTreeNode<T> dependency : node.getDependencies())
				if(nodeSet.contains(dependency))
					children.get(dependency).add(node);
		return children;
	}

	private Map<IDecoTreeNode<T>, Integer> buildIndegree(List<IDecoTreeNode<T>> nodes, Set<IDecoTreeNode<T>> nodeSet)
	{
		Map<IDecoTreeNode<T>, Integer> indegree = new LinkedHashMap<>();
		for(IDecoTreeNode<T> node : nodes)
		{
			int count = 0;
			for(IDecoTreeNode<T> dependency : node.getDependencies())
				if(nodeSet.contains(dependency))
					count++;
			indegree.put(node, count);
		}
		return indegree;
	}

	private List<IDecoTreeNode<T>> collectRoots(List<IDecoTreeNode<T>> nodes, Map<IDecoTreeNode<T>, Integer> indegree)
	{
		List<IDecoTreeNode<T>> roots = new ArrayList<>();
		for(IDecoTreeNode<T> node : nodes)
			if(indegree.get(node)==0)
				roots.add(node);
		return roots;
	}

	private boolean isForest(List<IDecoTreeNode<T>> nodes, Map<IDecoTreeNode<T>, List<IDecoTreeNode<T>>> children,
	                         Map<IDecoTreeNode<T>, Integer> indegree)
	{
		for(Integer value : indegree.values())
			if(value > 1)
				return false;

		Map<IDecoTreeNode<T>, Integer> remaining = new HashMap<>(indegree);
		ArrayDeque<IDecoTreeNode<T>> queue = new ArrayDeque<>();
		for(IDecoTreeNode<T> node : nodes)
			if(remaining.get(node)==0)
				queue.add(node);

		int visited = 0;
		while(!queue.isEmpty())
		{
			IDecoTreeNode<T> node = queue.removeFirst();
			visited++;
			for(IDecoTreeNode<T> child : children.get(node))
				if(remaining.compute(child, (n, value) -> value-1)==0)
					queue.addLast(child);
		}
		return visited==nodes.size();
	}

	private void calculateForestLayout(List<IDecoTreeNode<T>> roots,
	                                   Map<IDecoTreeNode<T>, List<IDecoTreeNode<T>>> children)
	{
		Map<IDecoTreeNode<T>, Integer> spans = new HashMap<>();
		int totalSpan = 0;
		for(IDecoTreeNode<T> root : roots)
		{
			int span = getSubtreeSpan(root, children, spans);
			totalSpan += span;
		}
		totalSpan += Math.max(0, roots.size()-1)*nodeSameLevelSpacing;

		int cursor = 0;
		for(IDecoTreeNode<T> root : roots)
		{
			int span = spans.get(root);
			placeSubtree(root, 0, cursor, span, children, spans);
			cursor += span+nodeSameLevelSpacing;
		}
		setVirtualRoot(totalSpan/2);
	}

	private int getSubtreeSpan(IDecoTreeNode<T> node, Map<IDecoTreeNode<T>, List<IDecoTreeNode<T>>> children,
	                           Map<IDecoTreeNode<T>, Integer> spans)
	{
		Integer cached = spans.get(node);
		if(cached!=null)
			return cached;

		List<IDecoTreeNode<T>> childList = children.get(node);
		int childrenSpan = 0;
		for(IDecoTreeNode<T> child : childList)
			childrenSpan += getSubtreeSpan(child, children, spans);
		childrenSpan += Math.max(0, childList.size()-1)*nodeSameLevelSpacing;

		int span = Math.max(getCrossNodeSize(), childrenSpan);
		spans.put(node, span);
		return span;
	}

	private void placeSubtree(IDecoTreeNode<T> node, int level, int crossStart, int span,
	                          Map<IDecoTreeNode<T>, List<IDecoTreeNode<T>>> children,
	                          Map<IDecoTreeNode<T>, Integer> spans)
	{
		int cross = crossStart+(span-getCrossNodeSize())/2;
		putNodeInfo(node, level, cross, 0);

		List<IDecoTreeNode<T>> childList = children.get(node);
		int childSpan = 0;
		for(IDecoTreeNode<T> child : childList)
			childSpan += spans.get(child);
		childSpan += Math.max(0, childList.size()-1)*nodeSameLevelSpacing;

		int cursor = crossStart+(span-childSpan)/2;
		for(int i = 0; i < childList.size(); i++)
		{
			IDecoTreeNode<T> child = childList.get(i);
			int currentSpan = spans.get(child);
			placeSubtree(child, level+1, cursor, currentSpan, children, spans);
			cursor += currentSpan+nodeSameLevelSpacing;
		}
	}

	private void calculateLevelLayout(List<IDecoTreeNode<T>> nodes,
	                                  Map<IDecoTreeNode<T>, List<IDecoTreeNode<T>>> children,
	                                  Map<IDecoTreeNode<T>, Integer> indegree)
	{
		Map<IDecoTreeNode<T>, Integer> remaining = new HashMap<>(indegree);
		Map<IDecoTreeNode<T>, Integer> nodeLevels = new HashMap<>();
		ArrayDeque<IDecoTreeNode<T>> queue = new ArrayDeque<>();
		for(IDecoTreeNode<T> node : nodes)
			if(remaining.get(node)==0)
			{
				nodeLevels.put(node, 0);
				queue.add(node);
			}

		while(!queue.isEmpty())
		{
			IDecoTreeNode<T> node = queue.removeFirst();
			int nextLevel = nodeLevels.get(node)+1;
			for(IDecoTreeNode<T> child : children.get(node))
			{
				nodeLevels.put(child, Math.max(nodeLevels.getOrDefault(child, 0), nextLevel));
				int left = remaining.compute(child, (n, value) -> value-1);
				if(left==0)
					queue.addLast(child);
			}
		}

		//Keep malformed cycles visible instead of recursing forever.
		for(IDecoTreeNode<T> node : nodes)
			nodeLevels.putIfAbsent(node, 0);

		Map<Integer, List<IDecoTreeNode<T>>> levels = new TreeMap<>();
		for(IDecoTreeNode<T> node : nodes)
			levels.computeIfAbsent(nodeLevels.get(node), level -> new ArrayList<>()).add(node);

		for(Map.Entry<Integer, List<IDecoTreeNode<T>>> entry : levels.entrySet())
		{
			int level = entry.getKey();
			List<IDecoTreeNode<T>> levelNodes = entry.getValue();
			if(level > 0)
				levelNodes.sort(Comparator.comparingDouble(this::getParentBarycentre));
			for(int i = 0; i < levelNodes.size(); i++)
				putNodeInfo(levelNodes.get(i), level, i*(getCrossNodeSize()+nodeSameLevelSpacing), i);
		}

		List<IDecoTreeNode<T>> roots = levels.getOrDefault(0, Collections.emptyList());
		int rootSpan = roots.isEmpty()?0:
				roots.size()*getCrossNodeSize()+Math.max(0, roots.size()-1)*nodeSameLevelSpacing;
		setVirtualRoot(rootSpan/2);
	}

	private double getParentBarycentre(IDecoTreeNode<T> node)
	{
		double total = 0;
		int count = 0;
		for(IDecoTreeNode<T> dependency : node.getDependencies())
		{
			NodeLayoutInfo info = nodeInfo.get(dependency);
			if(info!=null)
			{
				total += isHorizontal()?info.getCenterY(): info.getCenterX();
				count++;
			}
		}
		return count==0?Double.MAX_VALUE: total/count;
	}

	private void putNodeInfo(IDecoTreeNode<T> node, int level, int cross, int index)
	{
		int primary = level*(getPrimaryNodeSize()+nodeLevelSpacing);
		NodeLayoutInfo info = isHorizontal()?
				new NodeLayoutInfo(primary, cross, nodeWidth, nodeHeight, level, index):
				new NodeLayoutInfo(cross, primary, nodeWidth, nodeHeight, level, index);
		nodeInfo.put(node, info);
	}

	private void setVirtualRoot(int crossCenter)
	{
		int primary = -(getPrimaryNodeSize()+nodeLevelSpacing);
		rootInfo = isHorizontal()?
				new NodeLayoutInfo(primary, crossCenter, 0, 0, -1, -1):
				new NodeLayoutInfo(crossCenter, primary, 0, 0, -1, -1);
	}

	private void applyOrientation()
	{
		if(orientation==Orientation.HORIZONTAL_RIGHT_TO_LEFT)
		{
			for(NodeLayoutInfo info : nodeInfo.values())
				info.x = -info.x-info.width;
			rootInfo.x = -rootInfo.x;
		}
		else if(orientation==Orientation.VERTICAL_BOTTOM_TO_TOP)
		{
			for(NodeLayoutInfo info : nodeInfo.values())
				info.y = -info.y-info.height;
			rootInfo.y = -rootInfo.y;
		}
	}

	private boolean isHorizontal()
	{
		return orientation==Orientation.HORIZONTAL_LEFT_TO_RIGHT||orientation==Orientation.HORIZONTAL_RIGHT_TO_LEFT;
	}

	private int getPrimaryNodeSize()
	{
		return isHorizontal()?nodeWidth: nodeHeight;
	}

	private int getCrossNodeSize()
	{
		return isHorizontal()?nodeHeight: nodeWidth;
	}

	/**
	 * @return layout information for a node
	 */
	public NodeLayoutInfo getNodeInfo(IDecoTreeNode<T> node)
	{
		return nodeInfo.get(node);
	}

	public Collection<NodeLayoutInfo> getAllNodeInfo()
	{
		return Collections.unmodifiableCollection(nodeInfo.values());
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

		int minX = rootInfo.x;
		int minY = rootInfo.y;
		int maxX = rootInfo.getRight();
		int maxY = rootInfo.getBottom();
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

	public TreeLayout<T> withOrientation(Orientation orientation)
	{
		this.orientation = orientation==null?Orientation.HORIZONTAL_LEFT_TO_RIGHT: orientation;
		return this;
	}

	public TreeLayout<T> withNodeSize(int width, int height)
	{
		this.nodeWidth = Math.max(1, width);
		this.nodeHeight = Math.max(1, height);
		return this;
	}

	public TreeLayout<T> withSpacing(int levelSpacing, int sameLevelSpacing)
	{
		this.nodeLevelSpacing = Math.max(0, levelSpacing);
		this.nodeSameLevelSpacing = Math.max(0, sameLevelSpacing);
		return this;
	}

	public TreeLayout<T> withParentAlignment(boolean align)
	{
		this.alignChildrenToParent = align;
		return this;
	}

	/**
	 * Stores one calculated node rectangle.
	 */
	@NoArgsConstructor
	@AllArgsConstructor
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

	/**
	 * Stores the bounding rectangle of the calculated layout.
	 */
	@AllArgsConstructor
	public static class LayoutBounds
	{
		public final int minX, minY, maxX, maxY;

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
