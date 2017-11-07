package mazeGenerator.maze;
//--------------------------------------------------
//----- Imports ------------------------------------
//--------------------------------------------------
import mazeGenerator.exceptions.CouldNotFindPointException;
import mazeGenerator.exceptions.InvalidMazeSizeException;
import mazeGenerator.exceptions.MazeCheckException;
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//~~~~~

/**
 * Used to generate a <strong>Maze</strong> for use in other Classes.
 *
 * @author Deaddorks
 */
public class Maze
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
	 * The Array of <strong>Cell</strong>s that contains the structure of the <strong>Maze</strong>.
	 */
	private Cell[][] cells;
	/**
	 * The <strong>width</strong> of the <strong>Maze</strong>.
	 */
	private final int width;
	/**
	 * The <strong>width</strong> of the <strong>Maze</strong>.
	 */
	private final int height;
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//~~~~~
	
	/**
	 * Creates a <strong>Maze</strong> of size <strong>(width, height)</strong>.
	 *
	 * @param width The <strong>width</strong> of the <strong>Maze</strong> to be generated.
	 * @param height The <strong>height</strong> of the <strong>Maze</strong> to be generated.
	 */
	public Maze(final int width, final int height)
	{
		
		if (width <= 1 && height <= 1)
		{
			throw new InvalidMazeSizeException(width, height);
		}
		
		this.width = width;
		this.height = height;
		
		// Create a bunch of blank cells
		cells = new Cell[width][height];
		for (int w = 0; w < width; w++)
		{
			for (int h = 0; h < height; h++)
			{
				cells[w][h] = new Cell();
			}
		}
		
		// Set start and end point
		cells[0][0].setIsStart(true);
		cells[width - 1][height - 1].setIsEnd(true);
		
		generateMethod();
	}
	/**
	 * Used to generate a maze from another maze.
	 *
	 * @param oldCells The Array of <strong>Cell</strong>s that stored the structure of the previous <strong>Maze</strong>.
	 * @param newStartCol The <strong>column index</strong> of the new <strong>Start Cell</strong>.
	 * @param newStartRow The <strong>row index</strong> of the new <strong>Start Cell</strong>.
	 */
	private Maze(final Cell[][] oldCells, final int newStartCol, final int newStartRow)
	{
		width = oldCells.length;
		height = oldCells[0].length;
		
		cells = new Cell[width][height];
		for (int w = 0; w < width; w++)
		{
			for (int h = 0; h < height; h++)
			{
				cells[w][h] = oldCells[w][h];
				cells[w][h].setIsStart(false);
			}
		}
		cells[newStartCol][newStartRow].setIsStart(true);
	}
	
	/**
	 * Returns the <strong>width</strong> of the <strong>Maze</strong>.
	 * @return The <strong>width</strong> of the <strong>Maze</strong>.
	 */
	public int getWidth()
	{
		return width;
	}
	/**
	 * Returns the <strong>height</strong> of the <strong>Maze</strong>.
	 * @return The <strong>height</strong> of the <strong>Maze</strong>.
	 */
	public int getHeight()
	{
		return height;
	}
	
	/**
	 * Used to safely get <strong>Cell</strong>s out of the Array without having to worry about <strong>ArrayIndexOutOfBoundsException</strong>s.
	 * @param col The <strong>column</strong> index of the <strong>Cell</strong> to be accessed.
	 * @param row The <strong>row</strong> index of the <strong>Cell</strong> to be accessed.
	 * @return Returns the <strong>Cell</strong> at the specified <strong>(column, row)</strong>.<br>Returns <strong>null</strong> if the location is out of bounds.
	 */
	public Cell getCellAtLoc(final int col, final int row)
	{
		inBounds(col, row);
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
	 * Creates a <strong>Maze</strong> with a different starting location.
	 * @param newStartCol The <strong>column index</strong> of the new <strong>Start Cell</strong>.
	 * @param newStartRow The <strong>row index</strong> of the new <strong>Start Cell</strong>.
	 * @return A <strong>Maze</strong> with the starting location at the specified location.
	 */
	public Maze getMazeWithDifferentStart(final int newStartCol, final int newStartRow)
	{
		return new Maze(cells, newStartCol, newStartRow);
	}
	
	/**
	 * Runs the algorithm to generate the <strong>Maze</strong>.
	 */
	private void generateMethod()
	{
		
		Generator generator = new Generator(cells);
		generator.generate();
		checkMaze();
	}
	
	/**
	 * Called by the <strong>generate()</strong> method in order to make sure that there are not more than 1 <strong>start</strong> or <strong>end</strong> locations.
	 */
	private void checkMaze()
	{
		int startCount = 0;
		int endCount = 0;
		
		for (int w = 0; w < width; w++)
		{
			for (int h = 0; h < height; h++)
			{
				startCount += cells[w][h].getCellType() == Cell.CellType.START ? 1 : 0;
				endCount += cells[w][h].getCellType() == Cell.CellType.END ? 1 : 0;
			}
		}
		
		if (startCount != 1)
		{
			throw new MazeCheckException("startCount != 1 -> startCount = [" + startCount + "]");
		}
		if (endCount != 1)
		{
			throw new MazeCheckException("endCount != 1 -> endCount = [" + endCount + "]");
		}
	}
	
	/**
	 * Used to get the <strong>column</strong> and <strong>row</strong> of the starting location.
	 * @return Returns an int[] {A, B};<br>
	 * A : The <strong>column</strong> of the starting location.<br>
	 * B : The <strong>row</strong> of the starting location.
	 */
	public int[] getStart()
	{
		for (int w = 0; w < width; w++)
		{
			for (int h = 0; h < height; h++)
			{
				if (cells[w][h].getCellType() == Cell.CellType.START)
				{
					return new int[]{w, h};
				}
			}
		}
		throw new CouldNotFindPointException("StartPos");
	}
	
	/**
	 * Used to get the <strong>column</strong> and <strong>row</strong> of the ending location.
	 * @return Returns an int[] {A, B};<br>
	 * A : The <strong>column</strong> of the ending location.<br>
	 * B : The <strong>row</strong> of the ending location.
	 */
	public int[] getEnd()
	{
		for (int w = 0; w < width; w++)
		{
			for (int h = 0; h < height; h++)
			{
				if (cells[w][h].getCellType() == Cell.CellType.END)
				{
					return new int[]{w, h};
				}
			}
		}
		throw new CouldNotFindPointException("EndPos");
	}
	
	/**
	 * Prints the <strong>Maze</strong> to the console.
	 * @param showNodes Whether or not to print which <strong>Cell</strong>s are <strong>Node</strong>s.
	 */
	public void print(final boolean showNodes)
	{
		String[] vals = new String[height * 2 + 1];
		for (int i = 0; i < vals.length; i++)
		{
			vals[i] = "";
		}
		String output = "";
		
		char corner = '+';
		char side = '|';
		char topBottom = '-';
		char blank = ' ';
		char node = 'O';
		
		Cell c;
		for (int h = 0; h < height; h++)
		{
			for (int w = 0; w < width; w++)
			{
				c = cells[w][h];
				
				vals[h * 2] += Character.toString(corner) + Character.toString(c.getOpenStatusInDir(Direction.UP) ? blank : topBottom) + Character.toString(c.getOpenStatusInDir(Direction.UP) ? blank : topBottom);
				vals[h * 2 + 1] += Character.toString(c.getOpenStatusInDir(Direction.LEFT) ? blank : side) + Character.toString(showNodes ? (c.isNode() ? node : blank) : blank) + Character.toString(showNodes ? (c.isNode() ? node : blank) : blank);
				
				if (w == width - 1)
				{
					vals[h * 2] += Character.toString(corner);
					vals[h * 2 + 1] += Character.toString(c.getOpenStatusInDir(Direction.RIGHT) ? blank : side);
				}
				if (h == height - 1)
				{
					vals[h * 2 + 2] += Character.toString(corner) + Character.toString(c.getOpenStatusInDir(Direction.DOWN) ? blank : topBottom) + Character.toString(c.getOpenStatusInDir(Direction.DOWN) ? blank : topBottom);
				}
				if ((w == width - 1) && (h == height - 1))
				{
					vals[h * 2 + 2] += Character.toString(corner);
				}
			}
		}
		
		for (String s : vals)
		{
			output += s + "\n";
		}
		System.out.print(output);
	}
	
}
