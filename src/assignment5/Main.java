/* Critters GUI
 * EE422C Project 5 submission by
 * Arman Khondker
 * aak2464
 * 16345
 * Alex Kim
 * atk595
 * 16380
 * Slip days used: <1>
 * Git URL: https://github.com/EE422C-Fall-2018/project-5-critters-2-project-5-pair-18
 * Fall 2018
 */
/*
 * Other comments that will help the TA looking at your code.
 */
package assignment5;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.File;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
	private static String myPackage;
	public static GridPane world = new GridPane();
	private	static List<Class <?>> statList = new java.util.ArrayList<Class <?>>();
	private static List<XYChart.Series<Number, Number>> allStats = new java.util.ArrayList<XYChart.Series<Number, Number>>();
	final static NumberAxis xAxis = new NumberAxis();
	final static NumberAxis yAxis = new NumberAxis();
	final static LineChart<Number,Number> statGraph = new LineChart<Number,Number>(xAxis,yAxis);
	static double scale = 1.0;
	static int step = 0;
	private static Label statShow = new Label();
	private static Label seedShow = new Label();
	public static CheckBox gridShow = new CheckBox();
	private static AudioClip sound;
	private static int animationSpeed = 1;
	private static AnimationTimer timer = new AnimationTimer() {

		long prev = 0;

		@Override
		public void handle(long now) {
			// Update the world every 0.75s when animating
			if (System.currentTimeMillis() - prev >= 750) {
				for (int i = 0; i < animationSpeed; i++) {
					step++;
					Critter.worldTimeStep();
				}
				Critter.displayWorld();
				updateGraph();
				updateStats();
				prev = System.currentTimeMillis();
			}
		}

	};

	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}

	/**
	 * Main method
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Create the window
	 * @param primaryStage
	 * @throws Exception
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		// Starts music
		playMusic();
		
		// Set window
		primaryStage.setTitle("Critters");
		primaryStage.setWidth(1500);
		primaryStage.setHeight(800);

		// Create the panes to hold all items
		BorderPane root = new BorderPane();
		ScrollPane scrollWorld = new ScrollPane();

		// Create the world grid
		world.setHgap(0);
		world.setVgap(0);
		world.setStyle("-fx-background-color: #F0F8FF;");

		// Place the world grid on a scrollable interface
		gridShow.setSelected(true);
		scrollWorld.setContent(world);
		scrollWorld.setHbarPolicy(ScrollBarPolicy.ALWAYS);
		scrollWorld.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		root.setCenter(scrollWorld);

		// Display the world
		Critter.displayWorld();

		// Create the menu and stat bars
		menu(root);
		stats(root);

		// Add the panes to the scene
		Scene window = new Scene(root, 1500, 800);

		// Add the scene to the window and display it
		primaryStage.setScene(window);
		primaryStage.show();
	}

	/**
	 * Creates the action menu
	 * @param root
	 */
	private void menu(BorderPane root) {
		// Create HBox pane to hold the controller
		HBox menu = new HBox();
		menu.setPadding(new Insets(15, 12, 15, 12));
		menu.setSpacing(10);
		menu.setStyle("-fx-background-color: #336699;");

		// Initialize all menu items
		ComboBox<String> actions = new ComboBox<>();
		ComboBox<String> critterOptions = new ComboBox<>();
		ComboBox<String> animationOptions = new ComboBox<>();
		TextField inputVal = new TextField();
		Button showStats = new Button();
		Button hideStats = new Button();
		Button confirm = new Button();
		Button stopAnimation = new Button();

		// Create a drop-down with all options
		actions.setPromptText("Select an Action:");
		actions.setPrefSize(150, 20);
		actions.getItems().addAll(
				"Animate",
				"Step",
				"Seed",
				"Make",
				"Stats",
				"Quit");
		actions.managedProperty().bind(actions.visibleProperty());
		
		// Adjust available options based on action chosen
		actions.valueProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue ov, String t, String t1) {
				if (t1.equals("Animate")) {
					actions.setVisible(true);
					critterOptions.setVisible(false);
					animationOptions.setVisible(true);
					inputVal.setVisible(false);
					stopAnimation.setVisible(false);
					showStats.setVisible(false);
					hideStats.setVisible(false);

					if (animationOptions.getValue() != null)
						confirm.setVisible(true);
					else
						confirm.setVisible(false);
				}
				if (t1.equals("Step")) {
					actions.setVisible(true);
					critterOptions.setVisible(false);
					animationOptions.setVisible(false);
					inputVal.setVisible(true);
					inputVal.setPromptText("Number of Steps (default 1)");
					confirm.setVisible(true);
					stopAnimation.setVisible(false);
					showStats.setVisible(false);
					hideStats.setVisible(false);
				}
				if (t1.equals("Seed")) {
					actions.setVisible(true);
					critterOptions.setVisible(false);
					animationOptions.setVisible(false);
					inputVal.setVisible(true);
					inputVal.setPromptText("Seed Number");
					confirm.setVisible(false);
					stopAnimation.setVisible(false);
					showStats.setVisible(false);
					hideStats.setVisible(false);
				}
				if (t1.equals("Make")) {
					actions.setVisible(true);
					critterOptions.setVisible(true);
					animationOptions.setVisible(false);
					inputVal.setVisible(true);
					inputVal.setPromptText("Number of Critters (default 1)");
					showStats.setVisible(false);
					hideStats.setVisible(false);

					if (critterOptions.getValue() != null)
						confirm.setVisible(true);
					else
						confirm.setVisible(false);
				}
				if (t1.equals("Stats")) {
					actions.setVisible(true);
					critterOptions.setVisible(true);
					animationOptions.setVisible(false);
					inputVal.setVisible(false);
					stopAnimation.setVisible(false);
					confirm.setVisible(false);

					if (critterOptions.getValue() != null) {
						showStats.setVisible(true);
						hideStats.setVisible(true);
					}
					else {
						showStats.setVisible(false);
						hideStats.setVisible(false);
					}
				}
				if (t1.equals("Quit")) {
					actions.setVisible(true);
					critterOptions.setVisible(false);
					animationOptions.setVisible(false);
					inputVal.setVisible(false);
					confirm.setVisible(true);
					stopAnimation.setVisible(false);
					showStats.setVisible(false);
					hideStats.setVisible(false);
				}
			}    
		});

		// Create a drop-down with Critter options
		critterOptions.setPromptText("Select an Critter:");
		critterOptions.setPrefSize(150, 20);
		critterOptions.getItems().addAll(getCritters());
		critterOptions.managedProperty().bind(critterOptions.visibleProperty());
		critterOptions.valueProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue ov, String t, String t1) {
				if (actions.getValue() != "Stats")
					confirm.setVisible(true);
				else {
					showStats.setVisible(true);
					hideStats.setVisible(true);
				}
			}
		});

		// Create a drop-down with animation speed options
		animationOptions.setPromptText("Select Speed:");
		animationOptions.setPrefSize(150, 20);
		animationOptions.getItems().addAll(
				"1",
				"2",
				"5",
				"10");
		animationOptions.managedProperty().bind(animationOptions.visibleProperty());
		animationOptions.valueProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue ov, String t, String t1) {
				confirm.setVisible(true);
			}
		});

		// Create a text-field to get user input
		inputVal.setPrefSize(200, 20);
		inputVal.managedProperty().bind(inputVal.visibleProperty());
		inputVal.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		    	confirm.setVisible(true);
		    }
		});
		
		// Create a button lets the user stop the animation
		stopAnimation.setPrefSize(250, 20);
		stopAnimation.setText("Stop Animation");
		stopAnimation.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				actions.setVisible(true);
				animationOptions.setVisible(true);
				stopAnimation.setVisible(false);
				confirm.setVisible(true);
				timer.stop();
			}
		});
		
		// Create a button that lets the user see a new Critter's stats
		showStats.setPrefSize(100, 20);
		showStats.setText("Show");
		showStats.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String critterType;

				try {
					critterType = critterOptions.getValue();

					String className = myPackage+"."+critterType;

					try{
						if (!statList.contains(Class.forName(className))) {
							statList.add(Class.forName(className));
							updateStats();
						}
					} 
					catch(ClassNotFoundException e){
						throw new InvalidCritterException(critterType);
					}

				} catch (InvalidCritterException e1) {

				}
			}
		});
		showStats.managedProperty().bind(showStats.visibleProperty());
		
		// Create a button that lets the user hide a Critter's stats
		hideStats.setPrefSize(100, 20);
		hideStats.setText("Hide");
		hideStats.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String critterType;

				try {
					critterType = critterOptions.getValue();

					String className = myPackage+"."+critterType;

					try{
						if (statList.contains(Class.forName(className))) {
							statList.remove(Class.forName(className));
							updateStats();
						}
					} 
					catch(ClassNotFoundException e){
						throw new InvalidCritterException(critterType);
					}

				} catch (InvalidCritterException e1) {

				}
			}
		});
		hideStats.managedProperty().bind(hideStats.visibleProperty());

		// Create a button that lets the user confirm their selection
		confirm.setPrefSize(250, 20);
		confirm.setText("Confirm Selection");
		
		// Act upon the user's input
		confirm.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// Animate the steps automatically
				if (actions.getValue().equals("Animate")) {
					actions.setVisible(false);
					animationOptions.setVisible(false);
					critterOptions.setVisible(false);
					inputVal.setVisible(false);
					confirm.setVisible(false);
					stopAnimation.setVisible(true);

					animationSpeed = Integer.parseInt(animationOptions.getValue());
					timer.start();
				}

				// Perform world time step(s)
				if (actions.getValue().equals("Step")) {
					step++;
					if (!inputVal.getText().equals("")) {
						try {
							int steps = Integer.parseInt(inputVal.getText());
							for (int i = 0; i < steps; i++)
								Critter.worldTimeStep();
						} catch (NumberFormatException e) {

						}
					}
					else
						Critter.worldTimeStep();

					Critter.displayWorld();
					updateGraph();
					updateStats();
				}

				// Set a new seed
				if (actions.getValue().equals("Seed")) {
					try {
						long seed = Long.parseLong(inputVal.getText());
						Critter.setSeed(seed);
						seedShow.setText("Seed: " + inputVal.getText());
					} catch (NumberFormatException e1) {

					}
				}

				// Make new Critters
				if (actions.getValue().equals("Make")) {
					if (!inputVal.getText().equals("")) {
						try {
							int times = Integer.parseInt(inputVal.getText());
							for (int i = 0; i < times; i++)
								Critter.makeCritter(critterOptions.getValue());
						} catch (NumberFormatException e) {

						} catch (InvalidCritterException e) {
							e.printStackTrace();
						}
					}
					else {
						try {
							Critter.makeCritter(critterOptions.getValue());
						} catch (InvalidCritterException e) {
							e.printStackTrace();
						}
					}
					Critter.displayWorld();
					updateStats();
				}

				// Quit the program
				if (actions.getValue().equals("Quit")) {
					System.exit(0);
				}

				// Reset input
				inputVal.setText("");
			}
		});
		confirm.managedProperty().bind(confirm.visibleProperty());

		// Add the nodes to the HBox
		menu.getChildren().addAll(actions, critterOptions, animationOptions, inputVal, showStats, hideStats, confirm, stopAnimation);
		critterOptions.setVisible(false);
		inputVal.setVisible(false);
		showStats.setVisible(false);
		hideStats.setVisible(false);
		confirm.setVisible(false);
		stopAnimation.setVisible(false);
		animationOptions.setVisible(false);

		// Set the menu to the top of the screen
		root.setTop(menu);
	}

	private void stats(BorderPane root) {
		// Create the pane to hold all stats actions
		VBox stats = new VBox();

		// Creates the pane to hold the options
		HBox options = new HBox();

		// Create a button that allows the user to zoom into the world
		Button zoomIn = new Button();
		zoomIn.setPrefSize(25, 20);
		zoomIn.setText("+");
		zoomIn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				scale += 0.25;
				Critter.displayWorld();
			}
		});

		// Create a button that lets the user zoom out
		Button zoomOut = new Button();
		zoomOut.setPrefSize(25, 20);
		zoomOut.setText("-");
		zoomOut.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				scale -= 0.25;
				Critter.displayWorld();
			}
		});
		
		// White space between options
		Region space1 = new Region();
		space1.setPrefWidth(30);
		
		// Create a toggle that lets the user turn gridlines on or off
		gridShow.setText("Show Gridlines");
		gridShow.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Critter.displayWorld();
			}
		});
		
		// White space between options
		Region space2 = new Region();
		space2.setPrefWidth(30);
		
		// Create a label for the volume slider
		Label volumeL = new Label();
		volumeL.setText("Volume: ");

		// Create a volume adjustment slider
		Slider volumeControl = new Slider(0.0, 1.0, 0.5);
		sound.volumeProperty().bind(volumeControl.valueProperty());
		volumeControl.setPrefWidth(100);

		// Add all options features to the pane
		options.getChildren().addAll(zoomOut, zoomIn, space1, gridShow, space2, volumeL, volumeControl);

		// Create a Label to display all Critter stats requested
		stats.setPadding(new Insets(10));
		stats.setSpacing(8);
		stats.setStyle("-fx-background-color: #8FD4FF;");
		stats.setPrefWidth(400);

		// Create labels for each section
		Label title1 = new Label("World Options");
		title1.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		Label title2 = new Label("Stats");
		title2.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		statShow.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
		Label title3 = new Label("Population Graph");
		title3.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		Label title4 = new Label("World Parameters");
		title4.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		seedShow.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		
		// Create a scrollable interface for the population graph
		ScrollPane scrollGraph = new ScrollPane();
		xAxis.setLabel("Step Number");
		yAxis.setLabel("Number of Critters");
		statGraph.setPrefWidth(350);
		statGraph.setPrefHeight(350);
		scrollGraph.setContent(statGraph);

		// Run through each Critters' stats to populate the graph
		for (String c: getCritters()) {
			XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
			series.setName(c);
			series.getData().add(new XYChart.Data(0, 0));
			allStats.add(series);
			statGraph.getData().add(series);
		}
		
		// Create dimension options
		HBox dimensionOptions = new HBox();
		
		Label dimensions = new Label();
		dimensions.setText("Dimensions (WxH): ");
		dimensions.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		TextField worldWidth = new TextField();
		worldWidth.setText(Integer.toString(Params.world_width));
		worldWidth.setPrefWidth(70);
		TextField worldHeight = new TextField();
		worldHeight.setText(Integer.toString(Params.world_height));
		worldHeight.setPrefWidth(70);
		
		dimensionOptions.getChildren().addAll(dimensions, worldWidth, worldHeight);
		
		// Create energy cost options
		HBox energyCostOptions = new HBox();
		
		Label costs = new Label();
		costs.setText("Energy Costs: ");
		costs.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		Label walk = new Label();
		walk.setText("Walk: ");
		walk.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
		TextField walkCost = new TextField();
		walkCost.setText(Integer.toString(Params.walk_energy_cost));
		walkCost.setPrefWidth(40);
		Label run = new Label();
		run.setText("Run: ");
		run.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
		TextField runCost = new TextField();
		runCost.setText(Integer.toString(Params.run_energy_cost));
		runCost.setPrefWidth(40);
		Label rest = new Label();
		rest.setText("Rest: ");
		rest.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
		TextField restCost = new TextField();
		restCost.setText(Integer.toString(Params.rest_energy_cost));
		restCost.setPrefWidth(40);
		Label look = new Label();
		look.setText("Look: ");
		look.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
		TextField lookCost = new TextField();
		lookCost.setText(Integer.toString(Params.look_energy_cost));
		lookCost.setPrefWidth(40);
		
		energyCostOptions.getChildren().addAll(costs, walk, walkCost, run, runCost, rest, restCost, look, lookCost);

		// Create algae refresh options
		HBox algaeRefreshOptions = new HBox();
		
		Label refresh = new Label();
		refresh.setText("Algae Refresh: ");
		refresh.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		TextField refreshRate = new TextField();
		refreshRate.setText(Integer.toString(Params.refresh_algae_count));
		refreshRate.setPrefWidth(50);
		
		algaeRefreshOptions.getChildren().addAll(refresh, refreshRate);
		
		// Create parameter change confirmation button
		Button confirm = new Button();
		confirm.setPrefSize(200, 20);
		confirm.setText("Update Parameters");
		confirm.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Params.world_width = Integer.parseInt(worldWidth.getText());
				Params.world_height = Integer.parseInt(worldHeight.getText());
				Params.walk_energy_cost = Integer.parseInt(walkCost.getText());
				Params.run_energy_cost = Integer.parseInt(runCost.getText());
				Params.rest_energy_cost = Integer.parseInt(restCost.getText());
				Params.look_energy_cost = Integer.parseInt(lookCost.getText());
				Params.refresh_algae_count = Integer.parseInt(refreshRate.getText());
				Critter.displayWorld();
			}
		});
		

		// Add all elements to the right side of the window
		stats.getChildren().addAll(title1, options, title2, statShow, title3, scrollGraph, title4, dimensionOptions, energyCostOptions, algaeRefreshOptions, confirm, seedShow);
		root.setRight(stats);
	}
	
	/**
	 * Get all of the Critter names in the package
	 * @return List of Critter subclasses
	 */
	private static List<String> getCritters() {
		String path = myPackage.replaceAll("\\.", File.separator);
		List<Class<?>> classes = new ArrayList<>();
		List<String> critters = new ArrayList<>();
		String[] classPathEntries = System.getProperty("java.class.path").split(
				System.getProperty("path.separator")
				);

		// Store all classes in package in an ArrayList
		for (String classPathEntry: classPathEntries) {
			try {
				File rootFile = new File(classPathEntry + File.separatorChar + path);
				for (File curFile: rootFile.listFiles()) {
					if (curFile.getName().endsWith(".class")) {
						String className = curFile.getName().substring(0, curFile.getName().length() - 6);
						classes.add(Class.forName(myPackage + "." + className));
					}
				}
			} catch (Exception e) {

			}
		}

		// Pick out the Critter subclasses from the earlier list
		for (Class<?> c: classes) {
			if (!c.getSimpleName().equals("Critter") && Critter.class.isAssignableFrom(c)) {
				critters.add(c.getSimpleName());
			}
		}
		return critters;
	}
	
	/**
	 * Update each of the Critters' stats in preparation for displaying
	 */
	private static void updateStats() {
		String statsToShow = "";
		for (Class<?> c: statList) {
			String critterType = c.getName().substring(12);
			List<Critter> allInstances;
			try {
				allInstances = Critter.getInstances(critterType);
				Method stats = c.getMethod("runStats", List.class);
				String output = critterType + ": " + (String) (stats.invoke(null, allInstances));
				statsToShow += output + "\n";
			} catch (InvalidCritterException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
			}
		}
		statShow.setText(statsToShow + "\n");
	}
	
	/**
	 * Update the graph's data before displaying
	 */
	private static void updateGraph() {
		for (int i = 0; i < getCritters().size(); i++) {
			List<Critter> allInstances = new java.util.ArrayList<Critter>();
			try {
				allInstances = Critter.getInstances(getCritters().get(i));
				if (allInstances != null)
					allStats.get(i).getData().add(new XYChart.Data(step, allInstances.size()));
			} catch (InvalidCritterException e) {
			}
		}
	}
	
	/**
	 * Initialize the music player and start audio
	 */
	private void playMusic() {
		sound = new AudioClip(this.getClass().getResource("sicko_mode.mp3").toString());
		sound.setVolume(0.5);
		sound.play(); 
	}
}
