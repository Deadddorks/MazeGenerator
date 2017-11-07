package mazeGenerator.solver;
//--------------------------------------------------
//----- Imports ------------------------------------
//--------------------------------------------------
import importable.util.time.Timer;

import java.util.ArrayList;
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//~~~~~

public class Solver
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
	ArrayList<Move> moveList;
	ArrayList<Node> nodeList;
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//~~~~~
	
	public Solver()
	{
		
	}

	public ArrayList<Move> getSolution(Node[][] nodes)
	{
		Timer timer = new Timer();
		System.out.println(" --- Searching for Solution --- ");
		timer.start();

		final int width = nodes.length;
		final int height = nodes[0].length;

		moveList = new ArrayList<>();
		nodeList = new ArrayList<>();

		Node initialNode = null;
		Node currentNode;

		boolean foundStart = false;
		boolean foundEnd = false;

		for (int w = 0; w < width; w++)
		{
			for (int h = 0; h < height; h++)
			{
				if (nodes[w][h] != null)
				{
					if (nodes[w][h].getIsStart())
					{
						System.out.println("Start: ("+w+", "+h+")");
						initialNode = nodes[w][h];
						foundStart = true;
						break;
					}
				}
			}
			if (foundStart)
			{
				break;
			}
		}
		if (!foundStart)
		{
			System.out.println("Start not found");
			//return null;
		}

		nodeList.add(initialNode);
		while (!foundTheEnd())
		{
			currentNode = getLastNode();
			if (!currentNode.hasMoves())
			{
				nodeList.remove(currentNode);
			}
			else
			{
				nodeList.add(currentNode.moveInDir(currentNode.getNextMove()));
			}
		}

		for (int moveNum = 0; moveNum < nodeList.size() - 1; moveNum++)
		{
			moveList.add(nodeList.get(moveNum).getMove(nodeList.get(moveNum + 1)));
		}

		System.out.println("Found solution in ["+timer.msElapsed()+"ms]");

		return moveList;
	}

	private boolean foundTheEnd()
	{
		//System.out.println(nodeList.size());
		return nodeList.get(nodeList.size() - 1).getIsEnd();
	}
	private Node getLastNode()
	{
		return nodeList.get(nodeList.size() - 1);
	}
	
}
