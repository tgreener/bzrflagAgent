package search;

import state.*;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;


public class UniformCostSearch {
	
	private SearchSpace grid;
	private PriorityQueue<SearchSpaceLocation> queue;
	private HashSet<SearchSpaceLocation> visitedNodes;
	private SearchSpaceLocation goal;
	
	public UniformCostSearch(SearchSpace grid, SearchSpaceLocation start, 
			SearchSpaceLocation goal){
		this.grid = grid;
		Comparator<SearchSpaceLocation> comparer = new NodeCompare();
		queue = new PriorityQueue<SearchSpaceLocation>(1000,comparer);
		visitedNodes = new HashSet<SearchSpaceLocation>();
		this.goal = goal;
		visitedNodes.add(start);
		expandNode(start);
		
	}
	public void expandNode(SearchSpaceLocation node){
		if(node.isGoal())
			return;
		if(node == null)
			return;
		int nodeX = node.getX();
		int nodeY = node.getY();
		visitedNodes.add(node);
		offer(grid.getLocation(nodeX, nodeY+1));
		offer(grid.getLocation(nodeX+1, nodeY+1));
		offer(grid.getLocation(nodeX+1, nodeY));
		offer(grid.getLocation(nodeX+1, nodeY-1));
		offer(grid.getLocation(nodeX, nodeY-1));
		offer(grid.getLocation(nodeX-1, nodeY-1));
		offer(grid.getLocation(nodeX-1, nodeY));
		offer(grid.getLocation(nodeX-1, nodeY+1));
		expandNode(queue.poll());
	}
	public void offer(SearchSpaceLocation node){
		if(!queue.contains(node) && !visitedNodes.contains(node)){
			queue.offer(node);
		}
	}
	
	public class NodeCompare implements Comparator<SearchSpaceLocation>{

		@Override
		public int compare(SearchSpaceLocation node1, SearchSpaceLocation node2) {
			return distanceToGoal(node1) > distanceToGoal(node2) ? 1 : -1;
		}
	}
	
	public class TraverseNode{
		private SearchSpaceLocation location;
		private LinkedList<SearchSpaceLocation> path;
		private double pathLength;
		
		public TraverseNode(SearchSpaceLocation node, LinkedList<SearchSpaceLocation> path,
				double pathLength){
			this.location = node;
			this.path = path;
			this.path.add(node);
			this.pathLength = pathLength;
			this.pathLength += distanceToGoal(node);
		}
		
	}

	public double distanceToGoal(SearchSpaceLocation node){
		double d = 0;
		return d;
	}
}
