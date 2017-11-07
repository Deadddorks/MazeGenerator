package mazeGenerator.maze;

// ~~~~~~~~~~ Imports ~~~~~~~~~~

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

import importable.util.Math;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Generator
{
	
	// ----- Label -----
	// ~~~~~~~~~~ Constants ~~~~~~~~~~
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// ~~~~~~~~~~ Variables ~~~~~~~~~~
	private Cell[][] cells;
	private GenerationCell[][] genCells;
	private List<GenerationList> lists;
	
	private int width;
	private int height;
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public Generator(final Cell[][] cells)
	{
		this.cells = cells;
		width = cells.length;
		height = cells[0].length;
		genCells = new GenerationCell[width][height];
		
		lists = new ArrayList<>();
		GenerationCell genCell;
		
		for (int i = 0; i < cells.length; i++)
		{
			for (int j = 0; j < cells[i].length; j++)
			{
				genCell = new GenerationCell(cells[i][j]);
				genCells[i][j] = genCell;
				lists.add(new GenerationList(genCell));
			}
		}
		
		GenerationCell[] adj;
		for (int i = 0; i < cells.length; i++)
		{
			for (int j = 0; j < cells[i].length; j++)
			{
				adj = new GenerationCell[Direction.values().length];
				for (Direction dir : Direction.values())
				{
					adj[dir.getDirectionIndex()] = getCellAtLoc(i + dir.getXDelta(), j + dir.getYDelta());
				}
				genCells[i][j].setAdjGenCells(adj);
			}
		}
		
	}
	
	public void generate()
	{
		Direction dir;
		Direction[] dirs;
		GenerationCell cell;
		GenerationCell adjCell;
		GenerationList otherList;
		while (lists.size() > 1)
		{
			cell = lists.get(Math.randomInt(0, lists.size() - 1)).getRandomCell();
			//System.out.println("<"+lists.size()+">" + cell.toString());
			dirs = cell.notConnectedDirs();
			dir = dirs[Math.randomInt(0, dirs.length - 1)];
			adjCell = cell.getAdjCellInDir(dir);
			cell.connect(dir);
			otherList = adjCell.getGroup();
			lists.remove(otherList);
			cell.getGroup().absorb(otherList);
		}
	}
	
	private GenerationCell getCellAtLoc(final int col, final int row)
	{
		return (inBounds(col, row) ? genCells[col][row] : null);
	}
	
	private boolean inBounds(final int col, final int row)
	{
		return (col >= 0 && col < width && row >= 0 && row < height);
	}
	
}
