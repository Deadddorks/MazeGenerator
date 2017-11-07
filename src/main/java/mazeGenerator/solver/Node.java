package mazeGenerator.solver;
//--------------------------------------------------
//----- Imports ------------------------------------
//--------------------------------------------------
import mazeGenerator.maze.Cell;
import mazeGenerator.maze.Direction;
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//~~~~~

/**
 * Very similar to a <strong>Cell</strong> but with the ability to map itself to other <strong>Node</strong>s.<br>
 * This is in turn used to generate a solution in <strong>Solver</strong>.
 *
 * @author Deaddorks
 */
public class Node extends Cell
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
	/**
	 * Array of the <strong>Node</strong>s adjacent to the current <strong>Node</strong>.
	 */
	private Node[] connectedNodes;
	/**
	 * Array that stores whether a certain <strong>Direction</strong> has already been traveled by the <strong>Solver</strong>.
	 */
	private boolean[] dirsTraveled;
	/**
	 * How far to the next <strong>Node</strong>.<br>
	 * 0 if no adjacent <strong>Node</strong>.
	 */
	private int[] distanceToNextNode;
	/**
	 * <strong>Direction</strong> of the <strong>Node</strong> previous to the current <strong>Node</strong>.
	 */
	private Direction dirFrom;
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//~~~~~
	
	/**
	 * Creates a <strong>Node</strong> from a <strong>Cell</strong>.
	 * @param c <strong>Cell</strong> to base the <strong>Node</strong> off of.
	 */
	public Node(Cell c)
	{
		this.isStart = c.getIsStart();
		this.isEnd = c.getIsEnd();
		this.isOpenInDir = c.getIsOpenInDirs();
		this.isNodeByDefault = c.getIsNodeByDefault();

		dirsTraveled = new boolean[4];
		for (int i = 0; i < dirsTraveled.length; i++)
		{
			dirsTraveled[i] = false;
		}

		connectedNodes = new Node[4];
		distanceToNextNode = new int[4];

		dirFrom = null;
	}
	
	/**
	 * Sets the <strong>Direction</strong> of the previous <strong>Node</strong>.
	 * @param dir
	 */
	public void setDirFrom(final int dir)
	{
		dirFrom = Direction.values()[dir];
		dirsTraveled[dir] = true;
	}
	
	/**
	 * Recursive method that sets up the <strong>dirFrom</strong> of all the <strong>Node</strong>s following the current <strong>Node</strong>.
	 */
	public void setNextNodesDirsFrom()
	{
		for (Direction dir : Direction.values())
		{
			if (!dirsTraveled[dir.getDirectionIndex()])
			{
				connectedNodes[dir.getDirectionIndex()].setDirFrom(dir.getOppDirectionIndex());
				connectedNodes[dir.getDirectionIndex()].setNextNodesDirsFrom();
			}
		}

	}
	
	/**
	 * Gets a <strong>Move</strong> in the <strong>Direction</strong> of <strong>nextNode</strong>.
	 * @param nextNode <strong>Node</strong> to <strong>Move</strong> to.
	 * @return Returns a <strong>Move</strong> in the <strong>Direction</strong> of <strong>nextNode</strong>.
	 */
	public Move getMove(final Node nextNode)
	{
		for (Direction dir : Direction.values())
		{
			if (nextNode == connectedNodes[dir.getDirectionIndex()])
			{
				return new Move(dir, distanceToNextNode[dir.getDirectionIndex()]);
			}
		}
		throw new RuntimeException("Node not found");
	}
	
	/**
	 * Adds a <strong>Node</strong> in the specified <strong>Direction</strong> with the specified <strong>distance</strong>.
	 * @param dir <strong>Direction</strong> of the <strong>Node</strong>.
	 * @param node <strong>Node</strong>.
	 * @param dist <strong>distance</strong> to the <strong>Node</strong>.
	 */
	public void addNode(final Direction dir, final Node node, final int dist)
	{
		connectedNodes[dir.getDirectionIndex()] = node;
		distanceToNextNode[dir.getDirectionIndex()] = dist;

		if (dist < 1)
		{
			dirsTraveled[dir.getDirectionIndex()] = true;
		}
	}
	
	/**
	 * Sets <strong>dirsTraveled</strong> to  <strong>true</strong> in the specified <strong>Direction</strong>
	 * and <strong>returns</strong> the <strong>Node</strong> in that <strong>Direction</strong>.
	 * @param dir <strong>Direction</strong> to travel.
	 * @return The <strong>Node</strong> in the specified <strong>Direction</strong>.
	 */
	public Node moveInDir(final Direction dir)
	{
		dirsTraveled[dir.getDirectionIndex()] = true;
		return connectedNodes[dir.getDirectionIndex()];
	}
	
	/**
	 * Get the first <strong>Direction</strong> that hasn't been traveled already.
	 * @return The first <strong>Direction</strong> that hasn't been traveled already.
	 */
	public Direction getNextMove()
	{
		return getDirsToMove()[0];
	}
	
	/**
	 * Gets the Array of <strong>Direction</strong>s that haven't already been traveled.
	 * @return Array of <strong>Direction</strong>s that haven't already been traveled.
	 */
	public Direction[] getDirsToMove()
	{
		Direction[] dirs = new Direction[numDirsToMove()];

		int count = 0;
		for (Direction dir : Direction.values())
		{
			if (!dirsTraveled[dir.getDirectionIndex()])
			{
				dirs[count] = dir;
				count++;
			}
		}

		return dirs;
	}
	
	/**
	 * Gets the number of possible <strong>Directions</strong> to travel in.
	 * @return The number of possible <strong>Directions</strong> to travel in.
	 */
	public int numDirsToMove()
	{
		int count = 0;
		for (boolean b : dirsTraveled)
		{
			count += !b ? 1 : 0;
		}
		return count;
	}
	
	/**
	 * Gets whether or not the current <strong>Node</strong> has any valid directions to travel.
	 * @return Whether or not the current <strong>Node</strong> has any valid directions to travel.
	 */
	public boolean hasMoves()
	{
		return numDirsToMove() > 0;
	}

	@Override
	public String toString()
	{
		String out = "{";

		out += "dirFrom: "+dirFrom;
		out += ", distanceToNextNode: [";
		for (Direction dir : Direction.values())
		{
			out += dir + ":" + distanceToNextNode[dir.getDirectionIndex()];
			if (dir != Direction.values()[Direction.values().length-1])
			{
				out += ", ";
			}
		}
		out += "]";

		out += "} {";

		out += "isStart: " + isStart;
		out += ", isEnd: " + isEnd;
		out += ", isNodeByDefault: " + isNodeByDefault;

		out += "}";
		return out;
	}
	
}
