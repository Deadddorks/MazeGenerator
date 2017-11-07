package mazeGenerator.maze;

// ~~~~~~~~~~ Imports ~~~~~~~~~~
import importable.util.Math;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public class GenerationList
{
	
	// ----- Label -----
	// ~~~~~~~~~~ Constants ~~~~~~~~~~
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// ~~~~~~~~~~ Variables ~~~~~~~~~~
	private Set<GenerationCell> allCells;
	private Set<GenerationCell> availableCells;
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public GenerationList(final GenerationCell cell)
	{
		allCells = new HashSet<>();
		availableCells = new HashSet<>();
		
		allCells.add(cell);
		availableCells.add(cell);
		
		cell.setGroup(this);
	}
	
	public void absorb(final GenerationList otherList)
	{
		allCells.addAll(otherList.allCells);
		availableCells.addAll(otherList.availableCells);
		
		otherList.setGroup(this);
		
		GenerationCell adjCell;
		for (GenerationCell cell : availableCells)
		{
			for (Direction dir : cell.notConnectedDirs())
			{
				adjCell = cell.getAdjCellInDir(dir);
				if (allCells.contains(adjCell))
				{
					cell.setLinkedInDir(dir);
					adjCell.setLinkedInDir(Direction.getDirectionByIndex(dir.getOppDirectionIndex()));
				}
			}
		}
		
		availableCells.removeIf(c -> !c.hasAnyAvailableConnections());
	}
	
	private void setGroup(final GenerationList list)
	{
		for (GenerationCell cell : allCells)
		{
			cell.setGroup(list);
		}
	}
	
	public GenerationCell getRandomCell()
	{
		GenerationCell[] temp = availableCells.toArray(new GenerationCell[0]);
		return temp[Math.randomInt(0, temp.length - 1)];
	}
	
}
