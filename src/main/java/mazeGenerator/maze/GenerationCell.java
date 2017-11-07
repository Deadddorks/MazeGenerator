package mazeGenerator.maze;

// ~~~~~~~~~~ Imports ~~~~~~~~~~

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public class GenerationCell
{
	
	// ----- Label -----
	// ~~~~~~~~~~ Constants ~~~~~~~~~~
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// ~~~~~~~~~~ Variables ~~~~~~~~~~
	private Cell cell;
	private GenerationCell[] adjGenCells;
	private boolean[] linkedInDir;
	private GenerationList group;
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public GenerationCell(final Cell cell)
	{
		this.cell = cell;
	}
	
	public void setAdjGenCells(final GenerationCell[] adjGenCells)
	{
		this.adjGenCells = adjGenCells;
		linkedInDir = new boolean[4];
		for (int i = 0 ; i < 4; i++)
		{
			linkedInDir[i] = (adjGenCells[i] == null);
		}
		
	}
	
	public GenerationCell getAdjCellInDir(final Direction dir)
	{
		return adjGenCells[dir.getDirectionIndex()];
	}
	
	public Direction[] notConnectedDirs()
	{
		int c = numAvailableConnections();
		Direction[] availableDirs = new Direction[c];
		c = 0;
		for (Direction dir : Direction.values())
		{
			if (!linkedInDir[dir.getDirectionIndex()])
			{
				availableDirs[c] = dir;
				c++;
			}
		}
		return availableDirs;
	}
	
	public void setLinkedInDir(final Direction dir)
	{
		linkedInDir[dir.getDirectionIndex()] = true;
	}
	
	public int numAvailableConnections()
	{
		int c = 0;
		for (boolean b : linkedInDir)
		{
			c += b ? 0 : 1;
		}
		return c;
	}
	public boolean hasAnyAvailableConnections()
	{
		return numAvailableConnections() > 0;
	}
	
	public void setGroup(final GenerationList group)
	{
		this.group = group;
	}
	public GenerationList getGroup()
	{
		return group;
	}
	
	public void connect(final Direction dir)
	{
		cell.setOpenStatusInDir(dir, true);
		
		GenerationCell adj = adjGenCells[dir.getDirectionIndex()];
		adj.cell.setOpenStatusInDir(Direction.getDirectionByIndex(dir.getOppDirectionIndex()), true);
		
		setLinkedInDir(dir);
		adj.setLinkedInDir(Direction.getDirectionByIndex(dir.getOppDirectionIndex()));
	}
	
	@Override
	public String toString()
	{
		return "{Generation Cell} " + numAvailableConnections() + "\n" + cell.toString();
	}
}
