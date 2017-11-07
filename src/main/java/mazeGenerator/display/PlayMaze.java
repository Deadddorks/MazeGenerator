package mazeGenerator.display;
//--------------------------------------------------
//----- Imports ------------------------------------
//--------------------------------------------------
import mazeGenerator.exceptions.InvalidMazeSizeException;
import importable.util.time.Timer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import mazeGenerator.maze.Cell;
import mazeGenerator.maze.Direction;
import mazeGenerator.maze.Maze;
import mazeGenerator.solver.Move;
import mazeGenerator.solver.NodeMapper;
import mazeGenerator.solver.Solver;

import java.util.ArrayList;
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//~~~~~

public class PlayMaze
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
	private GraphicsContext gC;

	// Canvas stats
	private int width;
	private int height;
	private int stroke;
	private int offX;
	private int offY;

	// Maze stats
	private int mazeWidth;
	private int mazeHeight;
	private int defaultMazeSize;

	// Used for player
	private int playerX;
	private int playerY;
	private boolean reachedEnd;
	private boolean drawNodes;

	// Wall colors
	private Color wallColor_playing = Color.BLACK;
	private Color wallColor_won = Color.GREEN;
	// Colors
	private Color emptyColor = Color.WHITE;
	private Color nodeColor = Color.LIGHTCYAN;
	private Color startColor = Color.YELLOW;
	private Color endColor = Color.RED;
	private Color playerColor = Color.BLUE;
	private Color pathColor = Color.BROWN;

	// Maze stuff
	private Maze maze;
	private NodeMapper nodeMapper;
	private Solver solver;
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//~~~~~
	
	public PlayMaze(final GraphicsContext gC, final int width, final int height, final int defaultMazeSize)
	{
		this.gC = gC;

		this.defaultMazeSize = defaultMazeSize;
		this.width = width;
		this.height = height;

		mazeWidth = defaultMazeSize;
		mazeHeight = defaultMazeSize;

		drawNodes = true;

		maze = null;
		nodeMapper= new NodeMapper();
		solver = new Solver();
	}
	public void setSize(final int width, final int height)
	{
		this.width = width;
		this.height = height;

		drawMaze();
	}

	public void setMazeSize(final int mazeWidth, final int mazeHeight)
	{
		this.mazeWidth = mazeWidth;
		this.mazeHeight = mazeHeight;
	}

	public void handleKeyPress(KeyEvent e)
	{
		// Actions that are allowed when game is at end
		switch (e.getCode())
		{
			case SPACE:
				newGame();
				break;
			case R:
				restart();
				break;
		}

		if (reachedEnd || maze == null)
		{
			return;
		}
		// Actions that aren't
		switch (e.getCode())
		{
			case UP:
				tryMove(Direction.UP);
				break;
			case DOWN:
				tryMove(Direction.DOWN);
				break;
			case RIGHT:
				tryMove(Direction.RIGHT);
				break;
			case LEFT:
				tryMove(Direction.LEFT);
				break;
			case M:
				displayMoves(solver.getSolution(nodeMapper.getMappedNodes(maze.getMazeWithDifferentStart(playerX, playerY))));
				break;
		}
	}

	public void printSolution()
	{
		ArrayList<Move> moves = solver.getSolution(nodeMapper.getMappedNodes(maze));
		System.out.println("Solution:");
		for (Move m : moves)
		{
			System.out.println(m);
		}
	}
	private void calcStrokeAndOffs()
	{
		stroke = Math.min(width / (2 * maze.getWidth() + 1), height / (2 * maze.getHeight() + 1));
		offX = (width - stroke * (mazeWidth * 2 + 1)) / 2;
		offY = (height - stroke * (mazeHeight * 2 + 1)) / 2;
	}

	public void restart()
	{
		playerX = 0;
		playerY = 0;
		reachedEnd = false;

		drawMaze();
		drawPlayer(playerX, playerY, playerColor);
	}

	public synchronized void newGame()
	{
		Timer timer = new Timer();

		System.out.println(" --- Maze Generation Started --- ");
		timer.start();

		Thread thread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					maze = new Maze(mazeWidth, mazeHeight);
					restart();

					System.out.println("Generation finished. Time elapsed: " + timer.msElapsed() + "ms");
				}
				catch (InvalidMazeSizeException e)
				{
					System.out.println("Could not generate maze. Invalid Size.");
				}
			}
		});

		thread.start();
	}

	public void tryMove(Direction dir)
	{
		if (maze == null)
		{
			return;
		}
		if (reachedEnd)
		{
			return;
		}

		Cell c = maze.getCellAtLoc(playerX, playerY);
		if (c.getOpenStatusInDir(dir))
		{
			drawPlayer(playerX, playerY, (c.getCellType() == Cell.CellType.START ? startColor : (drawNodes ? (c.isNode() ? nodeColor : emptyColor) : emptyColor)));

			playerX += dir.getXDelta();
			playerY += dir.getYDelta();

			drawPlayer(playerX, playerY, playerColor);

			if (playerX == maze.getEnd()[0] && playerY == maze.getEnd()[1])
			{
				reachedEnd = true;
				drawMaze();
				drawPlayer(playerX, playerY, playerColor);
			}
		}
	}
	
	public void displayMoves(ArrayList<Move> moves)
	{
		
		int x = playerX;
		int y = playerY;
		
		drawMaze();
		
		gC.setFill(pathColor);
		for (Move move : moves)
		{
			for (int moveNum = 0; moveNum < move.getMoveCount(); moveNum++)
			{
				x += move.getDir().getXDelta();
				y += move.getDir().getYDelta();
				
				gC.fillRect(offX + 2 * stroke * x + stroke, offY + 2 * stroke * y + stroke, stroke, stroke);
			}
		}
		
		gC.setFill(endColor);
		gC.fillRect(offX + 2 * stroke * maze.getEnd()[0] + stroke, offY + 2 * stroke * maze.getEnd()[1] + stroke, stroke, stroke);
		gC.setFill(playerColor);
		gC.fillRect(offX + 2 * stroke * playerX + stroke, offY + 2 * stroke * playerY + stroke, stroke, stroke);
	}

	public void drawPlayer(final int x, final int y, final Color c)
	{
		calcStrokeAndOffs();

		gC.setFill(c);
		gC.fillRect(offX + 2 * x * stroke + stroke, offY + 2 * y * stroke + stroke, stroke, stroke);
	}

	public void drawMaze()
	{

		gC.setFill(wallColor_playing);
		gC.fillRect(0, 0, width, height);

		if (maze == null)
		{
			return;
		}

		calcStrokeAndOffs();

		Cell cell;
		for (int w = 0; w < maze.getWidth(); w++)
		{
			for (int h = 0; h < maze.getHeight(); h++)
			{
				cell = maze.getCellAtLoc(w, h);
				// Top left corner
				gC.setFill(reachedEnd ? wallColor_won : wallColor_playing);
				gC.fillRect(offX + 2 * w * stroke, offY + 2 * h * stroke, stroke, stroke);
				// Top side
				gC.setFill(cell.getOpenStatusInDir(Direction.UP) ? emptyColor : (reachedEnd ? wallColor_won : wallColor_playing));
				gC.fillRect(offX + 2 * w * stroke + stroke, offY + 2 * h * stroke, stroke, stroke);
				// Left side
				gC.setFill(cell.getOpenStatusInDir(Direction.LEFT) ? emptyColor : (reachedEnd ? wallColor_won : wallColor_playing));
				gC.fillRect(offX + 2 * w * stroke, offY + 2 * h * stroke + stroke, stroke, stroke);
				// Center
				switch (cell.getCellType())
				{
					case START: gC.setFill(startColor); break;
					case END: gC.setFill(endColor); break;
					case NODE:
						gC.setFill(drawNodes ? nodeColor : emptyColor);
						break;
					case NORMAL: gC.setFill(emptyColor); break;
				}
				gC.fillRect(offX + 2 * w * stroke + stroke, offY + 2 * h * stroke + stroke, stroke, stroke);

				if (w == maze.getWidth() - 1)
				{
					// Top right corner
					gC.setFill(reachedEnd ? wallColor_won : wallColor_playing);
					gC.fillRect(offX + 2 * w * stroke + 2 * stroke, offY + 2 * h * stroke, stroke, stroke);
					// Right side
					gC.setFill(cell.getOpenStatusInDir(Direction.RIGHT) ? emptyColor : (reachedEnd ? wallColor_won : wallColor_playing));
					gC.fillRect(offX + 2 * w * stroke + 2 * stroke, offY + 2 * h * stroke + stroke, stroke, stroke);
				}
				if (h == maze.getHeight() - 1)
				{
					// Bottom left corner
					gC.setFill(reachedEnd ? wallColor_won : wallColor_playing);
					gC.fillRect(offX + 2 * w * stroke, offY + 2 * h * stroke + 2 * stroke, stroke, stroke);
					// Bottom side
					gC.setFill(cell.getOpenStatusInDir(Direction.DOWN) ? emptyColor : (reachedEnd ? wallColor_won : wallColor_playing));
					gC.fillRect(offX + 2 * w * stroke + stroke, offY + 2 * h * stroke + 2 * stroke, stroke, stroke);
				}
				if ((w == maze.getWidth() - 1) && (h == maze.getHeight() - 1))
				{
					gC.setFill(reachedEnd ? wallColor_won : wallColor_playing);
					gC.fillRect(offX + 2 * w * stroke + 2 * stroke, offY + 2 * h * stroke + 2 * stroke, stroke, stroke);
				}
			}
		}
	}
	
}
