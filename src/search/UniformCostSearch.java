package search;

import state.*;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;


public class UniformCostSearch {
	
	private SearchSpace grid;
	private TraverseNode[][] traverseGrid;
	private PriorityQueue<TraverseNode> queue;
	private HashSet<TraverseNode> visitedNodes;
	private TraverseNode goal;
	
	public UniformCostSearch(SearchSpace grid){
		this.grid = grid;
		buildTraverseGrid(grid);
	}
	
	private void buildTraverseGrid(SearchSpace searchSpace){
		traverseGrid = new TraverseNode[200][200];
		for(int x = 0; x < 200; x++){
			for(int y = 0; y < 200; y++){
				traverseGrid[x][y] = new TraverseNode(x,y,grid.getLocation(x, y));
				if(grid.isGoal(x, y)){
					System.out.println("GOAL:x:" + x + " Y: " + y);
					goal = traverseGrid[x][y];
				}
			}
		}
	}
	
	@SuppressWarnings("unused")
	public LinkedList<SearchSpaceLocation> expandNode(TraverseNode node){
		if(node == null)
			return null; // failure, queue is empty
		if(node.isGoal()){
			System.out.println("GPL: " + node.getPathLength());
			return node.getPath();
		}
		int nodeX = node.getX();
		int nodeY = node.getY();
		visitedNodes.add(node);
		if(nodeY +1 < traverseGrid[nodeX].length)
			offer(traverseGrid[nodeX][nodeY+1],node);
		if(nodeX +1 < traverseGrid.length && nodeY +1 < traverseGrid.length)
			offer(traverseGrid[nodeX+1][nodeY+1],node);
		if(nodeX +1 < traverseGrid.length)
			offer(traverseGrid[nodeX+1][nodeY],node);
		if(nodeX +1 < traverseGrid.length && nodeY -1 >= 0)
			offer(traverseGrid[nodeX+1][nodeY-1],node);
		if(nodeY -1 >= 0)
			offer(traverseGrid[nodeX][nodeY-1],node);
		if(nodeX - 1 >= 0 && nodeY - 1 >= 0)
			offer(traverseGrid[nodeX-1][nodeY-1],node);
		if(nodeX - 1 >= 0)
			offer(traverseGrid[nodeX-1][nodeY],node);
		if(nodeX -1 >= 0  && nodeY +1 < traverseGrid.length)
			offer(traverseGrid[nodeX-1][nodeY+1],node);
		return expandNode(queue.poll());
	}
	
	public void offer(TraverseNode node, TraverseNode fromNode){
		if(!queue.contains(node) && !visitedNodes.contains(node)
				&& node.getLocation().getOccValue() == 0){
			updateNode(node,fromNode);
			queue.offer(node);
		}
	}
	
	private void updateNode(TraverseNode node, TraverseNode fromNode){
		node.update(fromNode);
	}
	
	public class NodeCompare implements Comparator<TraverseNode>{

		@Override
		public int compare(TraverseNode node1, TraverseNode node2) {
			return node1.getPathLength() > node2.getPathLength() ? 1 : -1;
		}
	}
	
	public class TraverseNode{
		private SearchSpaceLocation location;
		private LinkedList<SearchSpaceLocation> path;
		private double pathLength;
		int x,y;
		
		public TraverseNode(int x, int y,SearchSpaceLocation node){
			this.location = node;
			this.path = new LinkedList<SearchSpaceLocation>();
			this.pathLength = 0;
			this.x = x;
			this.y = y;
		}
		
		public double getPathLength(){
			return pathLength;
		}
		public int getX(){
			return x;
		}
		public int getY(){
			return y;
		}
		public boolean isGoal(){
			return location.isGoal();
		}
		public void update(TraverseNode node){
			this.path = new LinkedList<SearchSpaceLocation>(node.getPath());
			this.pathLength = node.pathLength;
			path.add(location);
			pathLength += distanceToNodePenalty(node);
		}
		public LinkedList<SearchSpaceLocation> getPath(){
			return path;
		}
		public double distanceToNode(TraverseNode node){
			return Math.sqrt(Math.pow(x-node.x,2) + Math.pow(y-node.y, 2));
		}
		public double distanceToNodePenalty(TraverseNode node){
			double penalty = 1;
			if(location.hasPenalty() && node.getLocation().hasPenalty())
				penalty = 1.5;
			else if(location.hasPenalty())
				penalty = 1.1;
			else if (node.getLocation().hasPenalty())
				penalty = 1.3;
			return Math.sqrt(Math.pow(x-node.x,2) + Math.pow(y-node.y, 2)) * penalty;
		}
		public SearchSpaceLocation getLocation(){
			return location;
		}
	}

	public double distanceToGoal(TraverseNode node){
		double d = node.distanceToNode(goal);
		return d;
	}

	public LinkedList<SearchSpaceLocation> getPath(int startX,int startY) {
		Comparator<TraverseNode> comparer = new NodeCompare();
		queue = new PriorityQueue<TraverseNode>(100000,comparer);
		visitedNodes = new HashSet<TraverseNode>();
		visitedNodes.add(traverseGrid[startX][startY]);
		System.out.println("START");
		return expandNode(traverseGrid[startX][startY]);
	}
}
