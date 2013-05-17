package search;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

import search.UniformCostSearch.NodeCompare;
import search.UniformCostSearch.TraverseNode;
import state.SearchSpace;
import state.SearchSpaceLocation;

public class AStar {
	
	private SearchSpace grid;
	private TraverseNode[][] traverseGrid;
	private PriorityQueue<TraverseNode> queue;
	private HashSet<TraverseNode> visitedNodes;
	private TraverseNode goal;
	
	public class NodeCompare implements Comparator<TraverseNode>{

		@Override
		public int compare(TraverseNode node1, TraverseNode node2) {
			return node1.getPathLength() + distanceToGoal(node1)
					> node2.getPathLength() + distanceToGoal(node2) ? 1 : -1;
		}
	}
	
	public AStar(SearchSpace grid){
		this.grid = grid;
		buildTraverseGrid(grid);
		Comparator<TraverseNode> comparer = new NodeCompare();
		visitedNodes = new HashSet<TraverseNode>();
		queue = new PriorityQueue<TraverseNode>(100000,comparer);
	}
	
	public LinkedList<SearchSpaceLocation> getPath(int startX, int startY){
		LinkedList<SearchSpaceLocation> path = new LinkedList<SearchSpaceLocation>();
		TraverseNode goal = traverse(startX,startY);
		path.add(goal.getLocation());
		TraverseNode current = goal;
		System.out.println("L:" + goal.getPrev().getPathLength());
		while(current.prev != null){
			current = current.prev;
			System.out.println("PATH: X: " + current.getX() + " Y: " + current.getY());
			path.addFirst(current.getLocation());
		}
		return path;
	}
	
	public TraverseNode traverse(int startX, int startY){
		TraverseNode startNode = traverseGrid[startX][startY];
		startNode.setPathLength(0);
		queue.add(startNode);
		while(queue.peek() != null){
			TraverseNode current = queue.poll();
			if(current.isGoal()){
				return current;
			}
			visitedNodes.add(current);
			int nodeX = current.getX();
			int nodeY = current.getY();
			if(nodeY +1 < traverseGrid[nodeX].length)
				offer(traverseGrid[nodeX][nodeY+1],current);
			if(nodeX +1 < traverseGrid.length && nodeY +1 < traverseGrid.length)
				offer(traverseGrid[nodeX+1][nodeY+1],current);
			if(nodeX +1 < traverseGrid.length)
				offer(traverseGrid[nodeX+1][nodeY],current);
			if(nodeX +1 < traverseGrid.length && nodeY -1 >= 0)
				offer(traverseGrid[nodeX+1][nodeY-1],current);
			if(nodeY -1 >= 0)
				offer(traverseGrid[nodeX][nodeY-1],current);
			if(nodeX - 1 >= 0 && nodeY - 1 >= 0)
				offer(traverseGrid[nodeX-1][nodeY-1],current);
			if(nodeX - 1 >= 0)
				offer(traverseGrid[nodeX-1][nodeY],current);
			if(nodeX -1 >= 0  && nodeY +1 < traverseGrid.length)
				offer(traverseGrid[nodeX-1][nodeY+1],current);
		}
		return null; //Failed to find the goal
	}
	
	public void offer(TraverseNode node, TraverseNode fromNode){
		if(!queue.contains(node) && !visitedNodes.contains(node)
				&& node.getLocation().getOccValue() == 0){
			node.setPrev(fromNode);
			queue.offer(node);
		} else if(!queue.contains(node) && node.shouldUpdate(fromNode)
				&& node.getLocation().getOccValue() == 0){
			node.setPrev(fromNode);
		}
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
	
	
	public class TraverseNode{
		private SearchSpaceLocation location;
		private TraverseNode prev;
		private double pathLength;
		int x,y;
		
		public TraverseNode(int x, int y,SearchSpaceLocation node){
			this.location = node;
			this.pathLength = Double.POSITIVE_INFINITY;
			this.x = x;
			this.y = y;
		}
		
		public void setPathLength(double length) {
			pathLength = length;			
		}

		public boolean shouldUpdate(TraverseNode fromNode) {
			if(fromNode.pathLength + distanceToNodePenalty(fromNode) < pathLength)
				return true;
			return false;
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
			this.pathLength = node.pathLength;
			pathLength += distanceToNodePenalty(node);
			prev = node;
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
		public void setPrev(TraverseNode prev){
			this.prev = prev;
			this.update(prev);
		}
		public TraverseNode getPrev(){
			return prev;
		}
	}

	public double distanceToGoal(TraverseNode node){
		double d = node.distanceToNode(goal);
		return d;
	}

}
