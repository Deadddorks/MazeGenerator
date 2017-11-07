package mazeGenerator.solver;
//--------------------------------------------------
//----- Imports ------------------------------------
//--------------------------------------------------
import importable.util.Math;
import importable.util.time.Timer;
import mazeGenerator.maze.Direction;
import mazeGenerator.maze.Maze;
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//~~~~~

public class NodeMapper
{
	
	//--------------------------------------------------
	//----- _ ------------------------------------------
	//--------------------------------------------------
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//~~~~~
	
	//--------------------------------------------------
	//----- Constants ----------------------------------
	//--------------------------------------------------
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//~~~~~
	//--------------------------------------------------
	//----- Variables ----------------------------------
	//--------------------------------------------------
	private Node[][] nodes;
	private int width;
	private int height;

	private Timer timer;

	private boolean displayInfo;
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//~~~~~
	
	public NodeMapper()
	{
		timer = new Timer();
		displayInfo = true;
	}

	public void setDisplayInfo(final boolean value)
	{
		displayInfo = value;
	}

	public void mapMaze(Maze maze)
	{

		int timeToFindNodes;
		int timeToFindAdjacentNodes;
		int timeToFindNodeOrder;
		int totalTime;

		if (displayInfo)
		{
			System.out.println(" --- Starting Mapping --- ");
		}
		timer.start();

		width = maze.getWidth();
		height = maze.getHeight();

		int dist;
		Node node;

		nodes = new Node[width][height];
		for (int w = 0; w < width; w++)
		{
			for (int h = 0; h < height; h++)
			{
				if (maze.getCellAtLoc(w, h).isNode())
				{
					nodes[w][h] = new Node(maze.getCellAtLoc(w, h));
				}
			}
		}

		timeToFindNodes = timer.msElapsed();
		if (displayInfo)
		{
			System.out.println("Nodes Located. Time elapsed: "+timer.msElapsed() + "ms");
		}

		for (int h = 0; h < height; h++)
		{
			for (int w = 0; w < width; w++)
			{
				node = nodes[w][h];
				if (node != null)
				{
					for (Direction dir : Direction.values())
					{
						if (node.getOpenStatusInDir(dir))
						{
							dist = 1;
							while (getNode(w + dir.getXDelta() * dist, h + dir.getYDelta() * dist) == null)
							{
								dist++;
							}
							node.addNode(dir, nodes[w + dir.getXDelta() * dist][h + dir.getYDelta() * dist], dist);
						}
						else
						{
							node.addNode(dir, null, 0);
						}
					}
				}
			}
		}
		timeToFindAdjacentNodes = timer.msElapsed() - timeToFindNodes;
		if (displayInfo)
		{
			System.out.println("Adjacent nodes stored. Time elapsed: " + timer.msElapsed() + "ms");
		}

		nodes[maze.getStart()[0]][maze.getStart()[1]].setNextNodesDirsFrom();
		timeToFindNodeOrder = timer.msElapsed() - timeToFindAdjacentNodes - timeToFindNodes;
		if (displayInfo)
		{
			System.out.println("Node order detected. Time elapsed: " + timer.msElapsed() + "ms");
		}

		totalTime = timer.msElapsed();

		System.out.println("Maze Successfully Mapped!");
		printEfficiencyData(new int[] {timeToFindNodes, timeToFindAdjacentNodes, timeToFindNodeOrder, totalTime});
	}

	public Node[][] getMappedNodes(Maze maze)
	{
		mapMaze(maze);
		return nodes;
	}

	private boolean inBounds(final int col, final int row)
	{
		return (col >= 0 && col < width && row >= 0 && row < height);
	}
	private Node getNode(final int col, final int row)
	{
		if (!inBounds(col, row))
		{
			return null;
		}
		else
		{
			return nodes[col][row];
		}
	}

	public void printNodes()
	{
		System.out.println(" ---- Nodes ----- ");
		for (int h = 0; h < height; h++)
		{
			for (int w = 0; w < width; w++)
			{
				if (getNode(w, h) != null)
				{
					System.out.println("("+w+", "+h+") " + getNode(w, h).toString());
				}
			}
		}
	}

	public void printEfficiencyData(final int[] times)
	{
		int cellCount = width * height;
		int nodeCount = 0;
		int amountSaved;
		double percentSaved;

		for (int h = 0; h < height; h++)
		{
			for (int w = 0; w < width; w++)
			{
				if (nodes[w][h] != null)
				{
					nodeCount++;
				}
			}
		}
		amountSaved = cellCount - nodeCount;
		percentSaved = Math.round(100 - 100.0 * nodeCount / cellCount, 2);

		System.out.println("Efficiency Data: {Cells: ["+cellCount+"], Nodes: ["+nodeCount+"], Amount Saved: ["+amountSaved+"], Percent Saved: ["+percentSaved+"%]}");
		System.out.println("Time Data: {Nodes: ["+times[0]+"ms], Adjacent Nodes: ["+times[1]+"ms], Node Order: ["+times[2]+"ms], Total Time: ["+times[3]+"ms]}");
	}

	public void printMap()
	{
		System.out.println(" - Printing Node Map - ");
		for (int h = 0; h < height; h++)
		{
			for (int w = 0; w < width; w++)
			{
				System.out.print(nodes[w][h] == null ? "." : "X");
			}
			System.out.println();
		}
	}
	
}
