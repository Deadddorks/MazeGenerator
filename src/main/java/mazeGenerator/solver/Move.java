package mazeGenerator.solver;
//--------------------------------------------------
//----- Imports ------------------------------------
//--------------------------------------------------
import mazeGenerator.maze.Direction;
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//~~~~~

/**
 * Used by <strong>Solver</strong> in order to communicate a solution.
 *
 * @author Deaddorks
 */
public class Move
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
	 * <strong>Direction</strong> of the <strong>Move</strong>.
	 */
	private Direction dir;
	/**
	 * Number of times to move in the specified <strong>Direction</strong>.
	 */
	private int moveCount;
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//~~~~~
	
	/**
	 * Creates a <strong>Move</strong>.
	 * @param dir <strong>Direction</strong> of the <strong>Move</strong>.
	 * @param moveCount Number of times to move in the specified <strong>Direction</strong>.
	 */
	public Move(final Direction dir, final int moveCount)
	{
		this.dir = dir;
		this.moveCount = moveCount;
	}
	
	/**
	 * Get the <strong>Direction</strong> of the <strong>Move</strong>.
	 * @return <strong>Direction</strong> of the <strong>Move</strong>.
	 */
	public Direction getDir()
	{
		return dir;
	}
	
	/**
	 * Gets the number of times to move in the specified <strong>Direction</strong>.
	 * @return Number of times to move in the specified <strong>Direction</strong>.
	 */
	public int getMoveCount()
	{
		return moveCount;
	}
	
	/**
	 * Get a <strong>String</strong> representation of the current <strong>Move</strong>.
	 * @return The <strong>String</strong> representation of the current <strong>Move</strong>.
	 */
	@Override
	public String toString()
	{
		return "{Move ["+dir+"] ["+moveCount+"] times}";
	}
	
}
