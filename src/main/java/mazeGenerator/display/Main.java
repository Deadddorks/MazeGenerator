package mazeGenerator.display;

import importable.javafx.NodeGenerator;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * This <strong>Class</strong> handles the initialization of the <strong>window</strong> and the components within it.
 *
 * @author Deaddorks
 */
public class Main extends Application
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
	 * Width of the <strong>Canvas</strong>.
	 */
	private final int WIDTH = 800;
	/**
	 * Height of the <strong>Canvas</strong>.
	 */
	private final int HEIGHT = 800;
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//~~~~~
	//--------------------------------------------------
	//----- Variables ----------------------------------
	//--------------------------------------------------
	/**
	 * Handles all the player input and graphics of the <strong>Maze</strong>.
	 */
	private PlayMaze playMaze;
	/**
	 * Size of the initial <strong>Maze</strong> that is generated.<br>
	 * Also auto-populates the size into <strong>TextField</strong>s.
	 */
	private int defaultMazeSize = 7;
	
	/**
	 * <strong>Scene</strong> that holds all the contents of the window.
	 */
	private Scene mainScene;
	/**
	 * <strong>Canvas</strong> to draw the <strong>Maze</strong> on.
	 */
	private Canvas mazeCanvas;
	
	// JavaFX Containers
	/**
	 * <strong>VBox</strong> that holds everything in the window.
	 */
	private VBox mainVBox;
	/**
	 * <strong>HBox</strong> that goes across the bottom of the window.
	 */
	private HBox bottomPanel;
	/**
	 * Contains the <strong>Label</strong>s and <strong>TextField</strong>s for setting the <strong>Maze</strong> size.
	 */
	private VBox settings;
	/**
	 * Width <strong>Label</strong> and <strong>TextField</strong>.
	 */
	private HBox widthSettings;
	/**
	 * Height <strong>Label</strong> and <strong>TextField</strong>.
	 */
	private HBox heightSettings;
	// JavaFX Nodes
	/**
	 * <strong>Label</strong> for giving the user information about the <strong>TextField</strong> that follows it.
	 */
	private Label widthLabel;
	/**
	 * <strong>Label</strong> for giving the user information about the <strong>TextField</strong> that follows it.
	 */
	private Label heightLabel;
	/**
	 * <strong>TextField</strong> for user input into <strong>Maze</strong> size.
	 */
	private TextField widthField;
	/**
	 * <strong>TextField</strong> for user input into <strong>Maze</strong> size.
	 */
	private TextField heightField;
	/**
	 * <strong>Button</strong> that sends data to <strong>playMaze</strong>.
	 */
	private Button generateButton;
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//~~~~~
	
	/**
	 * Called by javafx. Brings up the window and initializes it.
	 *
	 * @param window Sent by javafx.
	 * @throws Exception Whatever you say javafx, you do your thing.
	 */
	@Override
	public void start(Stage window) throws Exception
	{

		mainVBox = new VBox();
		bottomPanel = new HBox();
		bottomPanel.setPadding(new Insets(10));
		settings = new VBox();
		settings.setPadding(new Insets(0, 5, 0, 0));
		settings.setFocusTraversable(true);
		widthSettings = new HBox();
		widthSettings.setAlignment(Pos.CENTER_RIGHT);
		heightSettings = new HBox();
		heightSettings.setAlignment(Pos.CENTER_RIGHT);

		// ----- Set up bottom panel here -----
		NodeGenerator nodeGenerator = new NodeGenerator(new Font("Times New Roman", 20), Color.BLACK);
		// Labels
		int labelRightPadding = 5;
		widthLabel = nodeGenerator.createLabel("Width:");
		heightLabel = nodeGenerator.createLabel("Height:");
		widthLabel.setPadding(new Insets(0, labelRightPadding, 0, 0));
		heightLabel.setPadding(new Insets(0, labelRightPadding, 0, 0));
		// Text fields
		int textFieldWidth = 100;
		widthField = nodeGenerator.createTextField("Width");
		heightField = nodeGenerator.createTextField("Height");
		widthField.setText(Integer.toString(defaultMazeSize));
		heightField.setText(Integer.toString(defaultMazeSize));
		widthField.setPrefWidth(textFieldWidth);
		heightField.setPrefWidth(textFieldWidth);
		// Buttons
		generateButton = nodeGenerator.createButton("Generate");
		generateButton.setFocusTraversable(false);
		generateButton.setOnMouseClicked(e -> {
			try
			{
				int width = Integer.parseInt(widthField.getText());
				int height = Integer.parseInt(heightField.getText());
				playMaze.setMazeSize(width, height);
				playMaze.newGame();
				settings.requestFocus();
			}
			catch (NumberFormatException parseException)
			{
				System.out.println("Invalid input: " + parseException.getMessage());
			}
		});
		// Setting Boxes
		widthSettings.getChildren().addAll(widthLabel, widthField);
		heightSettings.getChildren().addAll(heightLabel, heightField);
		settings.getChildren().addAll(widthSettings, heightSettings);
		bottomPanel.getChildren().add(settings);
		bottomPanel.getChildren().add(generateButton);
		// ----- ------------------------ -----

		mainScene = new Scene(mainVBox);
		mainScene.setOnKeyReleased(e -> {
			switch (e.getCode())
			{
				case S:
					widthField.requestFocus();
					break;
				default:
					if (!widthField.isFocused() && !heightField.isFocused())
					{
						playMaze.handleKeyPress(e);
					}
					break;
			}
		});

		mazeCanvas = new Canvas(WIDTH, HEIGHT);
		playMaze = new PlayMaze(mazeCanvas.getGraphicsContext2D(), WIDTH, HEIGHT, defaultMazeSize);
		mainVBox.getChildren().add(mazeCanvas);

		mainVBox.getChildren().add(bottomPanel);

		playMaze.drawMaze();
		playMaze.newGame();

		window.setTitle("Maze");
		window.setScene(mainScene);
		window.setResizable(false);
		window.sizeToScene();
		//window.setAlwaysOnTop(true);
		window.show();
	}
	
	/**
	 * Starts the program running
	 * @param args Used as Arguments when the program is run.
	 */
	public static void main(String[] args)
	{
		launch(args);
	}
}
