package mazeGenerator.maze;
//--------------------------------------------------
//----- Imports ------------------------------------
//--------------------------------------------------
import importable.util.Math;

import java.util.ArrayList;
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//~~~~~

/**
 * Holds the <strong>ArrayList</strong>s and <strong>Array</strong> that are used to generate a <strong>Maze</strong>.
 *
 * @author Deaddorks
 */
public class GenerationList
{
	
	//--------------------------------------------------
	//----- _ ------------------------------------------
	//--------------------------------------------------
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//~~~~~
	
	//--------------------------------------------------
	//----- Constants ----------------------------------
	//--------------------------------------------------
	
	/**
	 * Helps decipher what sort of <strong>Cell</strong> is adjacent to the current <strong>Cell</strong>.
	 *
	 * @author Deaddorks
	 */
	public enum SearchResult
	{
		FOUND_NONE, FOUND_CELL_1, FOUND_CELL_2, FOUND_BOTH
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//~~~~~
	//--------------------------------------------------
	//----- Variables ----------------------------------
	//--------------------------------------------------
	/**
	 * <strong>ArrayList</strong> of <strong>ArrayList</strong> that stores which <strong>Cell</strong>s are already connected.<br>
	 * If 2 <strong>Cell</strong>s are in the same Nested <strong>ArrayList</strong>, then they are connected.
	 */
	private ArrayList<ArrayList<GenerationCell>> cellLists;
	/**
	 * The <strong>ArrayList</strong> of <strong>Cells</strong> that have possible connections to other <strong>Cell</strong>s.<br>
	 * Every time <strong>Cell</strong>s are connected, they are checked and removed if they no longer conform.
	 */
	private ArrayList<GenerationCell> availableCells;
	/**
	 *
	 */
	private GenerationCell[][] cells;
	
	/**
	 * Width of the <strong>Maze</strong> to be generated.
	 */
	private int width;
	/**
	 * Height of the <strong>Maze</strong> to be generated.
	 */
	private int height;
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//~~~~~
	
	/**
	 * Initializes all of the variables and Objects to set up the <strong>Maze</strong> generation.
	 *
	 * @param cellArray Blank Array of <strong>Cell</strong>s set up by <strong>Maze</strong>.
	 */
	public GenerationList(final Cell[][] cellArray)
	{
		width = cellArray.length;
		height = cellArray[0].length;

		this.cells = new GenerationCell[width][height];
		this.cellLists = new ArrayList<>();
		this.availableCells = new ArrayList<>();

		GenerationCell c;

		ArrayList<GenerationCell> temp;

		for (int w = 0; w < width; w++)
		{
			for (int h = 0; h < height; h++)
			{
				temp = new ArrayList<>();
				GenerationCell newCell = new GenerationCell(w, h, cellArray[w][h]);
				cells[w][h] = newCell;
				temp.add(newCell);
				newCell.setListThatHoldsMe(temp);

				cellLists.add(temp);
				availableCells.add(newCell);
			}
		}
		for (int w = 0; w < width; w++)
		{
			for (int h = 0; h < height; h++)
			{
				c = getCellAtLoc(w, h);
				for (Direction dir : Direction.values())
				{
					c.setAdjCell(dir, getCellAtLoc(w + dir.getXDelta(), h + dir.getYDelta()));
				}
			}
		}

		for (GenerationCell cell : availableCells)
		{
			cell.calculateValidDirs();
		}
	}
	
	/**
	 * Attempts to make a single merge of 2 <strong>Cell</strong>s, and then reconfigures the lists to reflect what are still valid options going forward.
	 */
	public void attemptMerge()
	{

		GenerationCell cell1 = availableCells.get(Math.randomInt(0, availableCells.size() - 1));
		GenerationCell cell2;
		ArrayList<GenerationCell> oldList;

		Direction[] validDirs = cell1.getValidDirs();

		Direction dir = validDirs[Math.randomInt(0, validDirs.length - 1)];
		cell2 = cell1.getAdjCell(dir);

		cell1.getCell().setOpenStatusInDir(dir, true);
		cell2.getCell().setOpenStatusInDir(Direction.getOppositeDirectionByIndex(dir.getDirectionIndex()), true);

		oldList = cell2.getListThatHoldsMe();
		for (GenerationCell c : oldList)
		{
			c.setListThatHoldsMe(cell1.getListThatHoldsMe());
		}
		cell1.getListThatHoldsMe().addAll(oldList);
		cellLists.remove(oldList);
		for (GenerationCell c : cell1.getListThatHoldsMe())
		{
			c.calculateValidDirs();
		}
		removeInvalidCells(cell1.getListThatHoldsMe());

	}
	
	/**
	 * Removes all the <strong>Cell</strong>s that are connected to other <strong>Cell</strong>s in the list.
	 * @param listToCheck Individual <strong>ArrayList</strong>, so that only the affected <strong>ArrayList</strong> is checked, instead of every <strong>Cell</strong>.
	 */
	public void removeInvalidCells(ArrayList<GenerationCell> listToCheck)
	{
		ArrayList<GenerationCell> tempList = new ArrayList<>();
		tempList.addAll(listToCheck);
		tempList.removeIf(c -> c.hasValidMoves());
		availableCells.removeAll(tempList);
	}
	
	/**
	 * Used to safely get <strong>GenerationCell</strong>s out of the Array without having to worry about <strong>ArrayIndexOutOfBoundsException</strong>s.
	 * @param col The <strong>column</strong> index of the <strong>Cell</strong> to be accessed.
	 * @param row The <strong>row</strong> index of the <strong>GenerationCell</strong> to be accessed.
	 * @return Returns the <strong>GenerationCell</strong> at the specified <strong>(column, row)</strong>.<br>Returns <strong>null</strong> if the location is out of bounds.
	 */
	public GenerationCell getCellAtLoc(final int col, final int row)
	{
		if (!inBounds(col, row))
		{
			return null;
		}
		else
		{
			return cells[col][row];
		}
	}
	
	/**
	 * Checks if a location is within the bounds of the Array.
	 * @param col The <strong>column</strong> index to be checked.
	 * @param row The <strong>row</strong> index to be checked.
	 * @return
	 * <strong>true</strong> - The location is within the bounds.<br>
	 * <strong>false</strong> - The location is not within the bounds.
	 */
	private boolean inBounds(final int col, final int row)
	{
		return (col >= 0 && col < width && row >= 0 && row < height);
	}
	
	/**
	 * Gets the number of separate regions of <strong>Cell</strong>s in the <strong>ArrayList</strong>.
	 * @return Size of the main <strong>ArrayList</strong>.<br>
	 * This denotes how many separated regions there are.
	 */
	public int size()
	{
		return cellLists.size();
	}
	
}
