package search;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

import state.SearchSpace;
import state.SearchSpaceLocation;

public class AStar {
	
	private SearchSpace grid;
	private TraverseNodeA[][] traverseGrid;
	private PriorityQueue<TraverseNodeA> queue;
	private HashSet<TraverseNodeA> visitedNodes;
	private TraverseNodeA goal;
	private double worldSize;
	PrintWriter writer;
	
	public class NodeCompare implements Comparator<TraverseNodeA>{

		@Override
		public int compare(TraverseNodeA node1, TraverseNodeA node2) {
			double path1 = node1.getPathLength() + node1.distanceToNode(goal);
			double path2 = node2.getPathLength() + node2.distanceToNode(goal);
			return path1 < path2 ? -1 : 1;
		}
	}
	
	public AStar(SearchSpace grid, double worldSize){
		try {
			writer = new PrintWriter("AStarPath.dat", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
		}
		this.worldSize = worldSize;
		this.grid = grid;
		buildTraverseGrid();
		Comparator<TraverseNodeA> comparer = new NodeCompare();
		visitedNodes = new HashSet<TraverseNodeA>();
		queue = new PriorityQueue<TraverseNodeA>(100000,comparer);
		

	}
	
	public LinkedList<SearchSpaceLocation> getPath(int startX, int startY){
		LinkedList<SearchSpaceLocation> path = new LinkedList<SearchSpaceLocation>();
		TraverseNodeA goal = traverse(startX,startY);
		path.add(goal.getLocation());
		TraverseNodeA current = goal;
		while(current.prev != null && current.prev.prev != null){
			current = current.prev;
			writer.println("set arrow from " + current.getX() + ", " + current.getY() + " to " + current.prev.getX() + ", " + current.prev.getY() + " nohead lt -1");
			writer.println("plot '-' with lines");
			writer.println("0 0 0 0\ne\n");
			path.addFirst(current.getLocation());
		}
		System.out.println("FP:" + goal.getPathLength());
		writer.close();
		return path;
	}
	
	public TraverseNodeA traverse(int startX, int startY){
		TraverseNodeA startNode = traverseGrid[startX][startY];
		startNode.setPathLength(0);
		queue.add(startNode);

		writer.println("set xrange [0:400]\nset yrange [0:400]");
		writer.println("");
		int i = 0;
		while(queue.peek() != null){
			TraverseNodeA current = queue.poll();
//			writer.println(current.getX() + " " + current.getY());
			if(current.prev != null && i++ % 50 == 0){
				writer.println("set arrow from " + current.getX() + ", " + current.getY() + " to " + current.prev.getX() + ", " + current.prev.getY() + " nohead lt 3");
				writer.println("plot '-' with lines");
				writer.println("0 0 0 0\ne\n");
			}
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
		System.out.println("fail");
		return null; //Failed to find the goal
	}
	
	public void offer(TraverseNodeA node, TraverseNodeA fromNode){
		if(node == null)
			return;

		if(!queue.contains(node) && !visitedNodes.contains(node)
				&& node.getLocation().getOccValue() == 0){
			node.setPrev(fromNode);
			queue.offer(node);
		} else if(!visitedNodes.contains(node) && node.shouldUpdate(fromNode)
				&& node.getLocation().getOccValue() == 0){
			node.setPrev(fromNode);
		}
	}
	
	private void buildTraverseGrid(){
		traverseGrid = new TraverseNodeA[(int)worldSize][(int)worldSize];
		for(int x = 0; x < (int)worldSize - 1; x++){
			for(int y = 0; y < (int)worldSize - 1; y++){
				traverseGrid[x][y] = new TraverseNodeA(x,y,grid.untransformedGetLocation(x,y));
				if(grid.untransformedGetLocationIsGoal(x, y)){
					goal = traverseGrid[x][y];
				}
			}
		}
	}
	
	
	public class TraverseNodeA{
		private SearchSpaceLocation location;
		private TraverseNodeA prev;
		private double pathLength;
		int x,y;
		
		public TraverseNodeA(int x, int y,SearchSpaceLocation node){
			this.location = node;
			this.pathLength =0;// Double.POSITIVE_INFINITY;
			this.x = x;
			this.y = y;
		}
		
		public void setPathLength(double length) {
			pathLength = length;			
		}

		public boolean shouldUpdate(TraverseNodeA fromNode) {
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
		public void update(TraverseNodeA node){
			this.pathLength = node.pathLength;
			pathLength += distanceToNodePenalty(node);
			prev = node;
		}
		public double distanceToNode(TraverseNodeA node){
			return Math.sqrt(Math.pow(x-node.x,2) + Math.pow(y-node.y, 2));
		}
		public double distanceToNodePenalty(TraverseNodeA node){
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
		public void setPrev(TraverseNodeA prev){
			this.prev = prev;
			this.update(prev);
		}
		public TraverseNodeA getPrev(){
			return prev;
		}
	}

	public double distanceToGoal(TraverseNodeA node){
		double d = node.distanceToNode(goal);
		return d;
	}

}
