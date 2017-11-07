package mazeGenerator.maze;
//--------------------------------------------------
//----- Imports ------------------------------------
//--------------------------------------------------
import java.util.ArrayList;
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//~~~~~

public class GenerationCell
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
	 * <strong>Cell</strong> that actually represents this location.
	 */
	private Cell cell;
	
	/**
	 * <strong>X</strong> index of the <strong>Cell</strong> in the Array.
	 */
	private final int cellX;
	/**
	 * <strong>Y</strong> index of the <strong>Cell</strong> in the Array.
	 */
	private final int cellY;
	
	/**
	 * <strong>ArrayList</strong> that holds the group of <strong>Cell</strong>s that this <strong>Cell</strong> is contained within.
	 */
	private ArrayList<GenerationCell> listThatHoldsMe;
	/**
	 * Array of adjacent <strong>Cell</strong>s.
	 */
	private GenerationCell[] adjCells = null;
	/**
	 * <strong>Direction</strong>s that are not out of bounds and are not another <strong>Cell</strong> in the same Array
	 */
	private boolean[] isValidGenerationDirection;
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//~~~~~
	
	/**
	 * Creates a <strong>GenerationCell</strong> with the specified <strong>Cell</strong> at <strong>(cellX, cellY)</strong>.
	 * @param cellX <strong>X</strong> index of the <strong>Cell</strong>.
	 * @param cellY <strong>Y</strong> index of the <strong>Cell</strong>.
	 * @param cell
	 */
	public GenerationCell(final int cellX, final int cellY, final Cell cell)
	{
		this.cell = cell;
		this.cellX = cellX;
		this.cellY = cellY;

		isValidGenerationDirection = new boolean[4];
		for (Direction dir : Direction.values())
		{
			isValidGenerationDirection[dir.getDirectionIndex()] = !cell.getOpenStatusInDir(dir);
		}

		adjCells = new GenerationCell[4];
	}

	public void setListThatHoldsMe(final ArrayList<GenerationCell> list)
	{
		listThatHoldsMe = list;
	}
	public ArrayList<GenerationCell> getListThatHoldsMe()
	{
		return listThatHoldsMe;
	}

	public boolean isValidDir(final Direction dir)
	{
		return isValidGenerationDirection[dir.getDirectionIndex()];
	}

	public void setAdjCell(final Direction dir, final GenerationCell cell)
	{
		adjCells[dir.getDirectionIndex()] = cell;
	}
	public GenerationCell getAdjCell(final Direction dir)
	{
		return adjCells[dir.getDirectionIndex()];
	}

	public void calculateValidDirs()
	{
		for (Direction dir : Direction.values())
		{
			if (cell.getOpenStatusInDir(dir))
			{
				isValidGenerationDirection[dir.getDirectionIndex()] = false;
			}
			else
			{
				if (adjCells[dir.getDirectionIndex()] != null)
				{
					isValidGenerationDirection[dir.getDirectionIndex()] = (listThatHoldsMe != adjCells[dir.getDirectionIndex()].listThatHoldsMe);
				}
				else
				{
					isValidGenerationDirection[dir.getDirectionIndex()] = false;
				}
			}
		}
	}

	public Direction[] getValidDirs ()
	{
		Direction[] validDirs = new Direction[getNumValidMoves()];
		int counter = 0;

		for (Direction dir : Direction.values())
		{
			if (isValidGenerationDirection[dir.getDirectionIndex()])
			{
				validDirs[counter] = dir;
				counter++;
			}
		}

		return validDirs;
	}

	public int getNumValidMoves()
	{
		int counter = 0;
		for (Direction dir : Direction.values())
		{
			if (isValidGenerationDirection[dir.getDirectionIndex()])
			{
				counter++;
			}
		}
		return counter;
	}
	public boolean hasValidMoves()
	{
		return getNumValidMoves() > 0;
	}

	public int getX()
	{
		return cellX;
	}

	public int getY()
	{
		return cellY;
	}

	public Cell getCell()
	{
		return cell;
	}

	@Override
	public String toString()
	{
		String out = "{GenerationCell} : {isValidGenerationDirection: [";
		for (int index = 0; index < Direction.values().length; index++)
		{
			Direction dir = Direction.values()[index];
			out += dir + ": " + isValidGenerationDirection[dir.getDirectionIndex()];

			if (index + 1 < Direction.values().length)
			{
				out += ", ";
			}
		}
		out += "], adjCells(== null): [";
		for (int index = 0; index < Direction.values().length; index++)
		{
			Direction dir = Direction.values()[index];
			out += dir + ": " + (adjCells[index] == null ? "null" : "~");

			if (index + 1 < Direction.values().length)
			{
				out += ", ";
			}
		}
		out += "], ";
		out += "X: "+cellX + ", ";
		out += "Y: "+cellY + "} ";

		out += cell.toString();

		return out;
	}
	
}
