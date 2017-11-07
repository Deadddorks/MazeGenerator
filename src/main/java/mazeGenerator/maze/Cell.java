package mazeGenerator.maze;
//--------------------------------------------------
//----- Imports ------------------------------------
//--------------------------------------------------

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//~~~~~

public class Cell
{

	//Test

	//--------------------------------------------------
	//----- _ ------------------------------------------
	//--------------------------------------------------
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//~~~~~
	
	//--------------------------------------------------
	//----- Constants ----------------------------------
	//--------------------------------------------------
	public enum CellType {START, END, NODE, NORMAL}
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//~~~~~
	//--------------------------------------------------
	//----- Variables ----------------------------------
	//--------------------------------------------------
	protected boolean[] isOpenInDir;
	protected boolean isNodeByDefault;
	protected boolean isStart;
	protected boolean isEnd;
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//~~~~~
	
	public Cell()
	{
		initCell();
	}

	private void initCell()
	{
		isNodeByDefault = false;
		isStart = false;
		isEnd = false;

		isOpenInDir = new boolean[4];
		for (int dir = 0; dir < 4; dir++)
		{
			isOpenInDir[dir] = false;
		}
	}

	public boolean getOpenStatusInDir(final Direction dir)
	{
		return isOpenInDir[dir.getDirectionIndex()];
	}

	public void setOpenStatusInDir(final Direction dir, final boolean status)
	{
		isOpenInDir[dir.getDirectionIndex()] = status;
	}

	public void setIsStart(final boolean value)
	{
		isStart = value;
		setNodeByDefault();
	}
	public void setIsEnd(final boolean value)
	{
		isEnd = value;
		setNodeByDefault();
	}
	private void setNodeByDefault()
	{
		isNodeByDefault = isStart || isEnd;
	}

	public boolean getIsStart()
	{
		return isStart;
	}
	public boolean getIsEnd()
	{
		return isEnd;
	}

	public boolean getIsNodeByDefault()
	{
		return isNodeByDefault;
	}
	public boolean[] getIsOpenInDirs()
	{
		return isOpenInDir;
	}

	public boolean isNode()
	{
		return isNodeByDefault || !((!isOpenInDir[0] && isOpenInDir[1] && !isOpenInDir[2] && isOpenInDir[3]) || (isOpenInDir[0] && !isOpenInDir[1] && isOpenInDir[2] && !isOpenInDir[3]));
	}

	public CellType getCellType()
	{
		if (isStart)
		{
			return CellType.START;
		}
		else if (isEnd)
		{
			return CellType.END;
		}
		else if (isNode())
		{
			return CellType.NODE;
		}
		else
		{
			return CellType.NORMAL;
		}
	}

	@Override
	public String toString()
	{
		String out = "{";

		out += "isOpenInDir: [";
		for (Direction dir : Direction.values())
		{
			out += dir + ":" + isOpenInDir[dir.getDirectionIndex()];
			if (dir != Direction.values()[Direction.values().length-1])
			{
				out += ", ";
			}
		}
		out += "]";

		out += ", isStart: " + isStart;
		out += ", isEnd: " + isEnd;
		out += ", isNodeByDefault: " + isNodeByDefault;

		out += "}";
		return out;
	}
	
}
