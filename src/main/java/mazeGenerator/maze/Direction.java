package mazeGenerator.maze;

/**
 * <strong>Enum</strong> used by many classes in order to depict a <strong>Direction</strong>.<br>
 * Has several methods that make using a <strong>Direction</strong> very simple.
 *
 * @author Deaddorks
 */
public enum Direction
{

	UP(0, 2, 0, -1),
	RIGHT(1, 3, 1, 0),
	DOWN(2, 0, 0, 1),
	LEFT(3, 1, -1, 0);
	
	/**
	 * Array index for the <strong>Direction</strong>.
	 */
	private int direction;
	/**
	 * Array index for the opposite <strong>Direction</strong>.
	 */
	private int oppDirection;
	/**
	 * Change in X on a 2D plane.
	 */
	private int xDelta;
	/**
	 * Change in Y on a 2D plane.
	 */
	private int yDelta;
	
	/**
	 * Gets the Array index of the specified <strong>Direction</strong>.
	 * @return Array index of the specified <strong>Direction</strong>.
	 */
	public int getDirectionIndex()
	{
		return direction;
	}
	/**
	 * Gets the Array index opposite of the specified <strong>Direction</strong>.
	 * @return Array index opposite of the specified <strong>Direction</strong>.
	 */
	public int getOppDirectionIndex()
	{
		return oppDirection;
	}
	
	/**
	 * Gets the change in the X Direction that correlates with the <strong>Direction</strong>.
	 * @return Delta X.
	 */
	public int getXDelta()
	{
		return xDelta;
	}
	/**
	 * Gets the change in the Y Direction that correlates with the <strong>Direction</strong>.
	 * @return Delta Y.
	 */
	public int getYDelta()
	{
		return yDelta;
	}
	
	/**
	 * Used to get a <strong>Direction</strong> from its Array index.
	 * @param index Array index of the <strong>Direction</strong>.
	 * @return The <strong>Direction</strong> that represents the specified Array index.
	 */
	public static Direction getDirectionByIndex(final int index)
	{
		return values()[index];
	}
	/**
	 * Used to get a <strong>Direction</strong> from the opposite Array index.
	 * @param index Array index of the opposite <strong>Direction</strong>.
	 * @return The <strong>Direction</strong> that represents the opposite Array index.
	 */
	public static Direction getOppositeDirectionByIndex(final int index)
	{
		return  values()[(index + 2) % values().length];
	}

	Direction(final int indexNum, final int oppIndexNum, final int xD, final int yD)
	{
		direction = indexNum;
		oppDirection = oppIndexNum;

		xDelta = xD;
		yDelta = yD;
	}
}
