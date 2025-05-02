/*
 * PROGRAM DESCRIPTION:
 * - Player plays a rhythm game in which they press the right keys according to what note passes through a set of dull notes 
 * - player gets a better score depending on how well they do 
 * - player can also make own maps and preview them by importing songs to folder (userAudios) 
 * 
 */




package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Main extends Application {


	//FIELDS
	
	private Timeline movementTimer;
	private AnimationTimer outcomeAnimation;
	private Rectangle rectMAP, lane1, lane2, lane3, lane4; 
	private Rectangle rectSelect; 

	private ArrayList<Note> song0; 
	private ArrayList<Outcomes> hitOutcomes; 

	private Robot robotGame, robotMenu; 
	private Dialogue dialog; 

	private int scrollY, scrollDeltaY; 
	private int laneSelect, lanePlaySelect, noteIndex, outcomeIndex, lineCounter; 
	private int editSongLength;

	private boolean play, hitAKey, isTutorial, playFromStart; 
	private boolean edit, isMenu, isValid; 

	private int score, mapSpeed, mapDeltaY, songLength;

	private int currentSongTime, originalY, deltaTime, newTimeInt;
	private Duration newTimeDuration; 

	private int bufferAmount; 
	private boolean bufferStop; 


	private String currentLine, fullEditName; 

	private FileReader fr; 
	private BufferedReader br;

	private Button btnPlay, btnLoad, btnSave, btnSkipTutorial; 
	private Button btnDown, btnUp; 

	private File file, directoryUserAudio; 
	private File[] fileUserAudios; 

	private Font fDialogue, fHeadings, fText, fTitle, fHeadings2;


	private MediaPlayer mpGame, mpMenu; 



	private FileWriter fw;
	private BufferedWriter bw; 


	private Label lblScore, lblMenuHeader, lblMenuTitle;

	private Scene sceneGame, sceneEditSetup;
	private Pane rootGame, rootMenu; 
	private VBox vBoxSongs, vBoxEdits;
	private Button btnEditor, btnReturn;
	private Button[]btnSongs, btnEdits; 


	private Song currentSong; 
	private UserSong[] currentEdit; 
	private int currentSongInt, btnEditSelected;



	final int PERFECT = 1, GOOD = 2, MISS = 3;

	final int ROBOTDEFAULT = 0, ROBOTPLAY = 1, ROBOTMISS = 2; 
	final int TUTORIALPLAY = 1, TUTORIALEDIT = 2, TUTORIALNONE = 0;
	final boolean PLAY = true, EDIT = false; 



	public void start(Stage primaryStage) {
		try {

			
			//CONSTRUCTORS
			currentSong = new Song(); 
			currentEdit = new UserSong[4]; 

			for(int i = 0; i < currentEdit.length; i++)
			{
				currentEdit[i] = new UserSong(); 
			}

		
			File fMenu = new File("audio/menuSong.wav");

			Media mediaMenu = new Media(fMenu.toURI().toString());


			mpGame = new MediaPlayer(currentSong.getMedia()); 
			mpGame.setAutoPlay(true);
			mpGame.stop(); 

			mpMenu = new MediaPlayer(mediaMenu);

			mpMenu.setOnEndOfMedia(new Runnable() 
			{
				public void run()
				{
					mpMenu.seek(Duration.ZERO);
				}
			});
			mpMenu.setAutoPlay(true);
			mpMenu.stop(); 






			file = currentSong.getSaveFile(); 
			fw  = new FileWriter(file, true); 
			bw = new BufferedWriter(fw); 
			fr = new FileReader(file);
			br = new BufferedReader(fr); 
			currentLine = "";

			fDialogue = Font.loadFont(new FileInputStream(new File("ttf/ARCADECLASSIC.ttf")), 20);
			fHeadings = Font.loadFont(new FileInputStream(new File("ttf/ARCADECLASSIC.ttf")), 70);
			fTitle = Font.loadFont(new FileInputStream(new File("ttf/ARCADECLASSIC.ttf")), 120);

			fText = Font.loadFont(new FileInputStream(new File("ttf/Minecraft.ttf")), 20);
			fHeadings2 = Font.loadFont(new FileInputStream(new File("ttf/Minecraft.ttf")), 40);





			Pane startLayout = new Pane(); 

			rootGame = new Pane();
			rootMenu = new Pane(); 


			sceneGame = new Scene(rootGame,1200,700);
			Scene sceneMenu = new Scene(rootMenu, 1200,700); 

			Scene sceneStart = new Scene(startLayout); 




			isValid = false;

			directoryUserAudio = new File("userAudio");
			int fileCount = directoryUserAudio.list().length;

			fileUserAudios = directoryUserAudio.listFiles();

			Button btnSubmit = new Button("SUBMIT");
			btnSubmit.setPrefSize(125, 75);
			btnSubmit.setAlignment(Pos.CENTER);

			TextArea txtSongLength = new TextArea(); 
			txtSongLength.setPrefSize(200, 50); 
			txtSongLength.setWrapText(false);

			GridPane rootPickAudio = new GridPane();
			rootPickAudio.setPadding(new Insets(20,20,20,20));

			Label lblPickAudio = new Label("PICK AN IMPORTED SONG\nType length of song (in sec)");
			lblPickAudio.setFont(fText);

			ComboBox<String>cboAudio = new ComboBox<String>(); 

			for(int i = 1; i < fileCount; i++)
			{
				cboAudio.getItems().add((i) + " - " +fileUserAudios[i].getName());				
			}

			cboAudio.setPromptText("Pick Audio");
			cboAudio.setPrefSize(200, 50);

			rootPickAudio.add(lblPickAudio, 0, 0);
			GridPane.setHalignment(lblPickAudio, HPos.CENTER);

			rootPickAudio.add(txtSongLength, 0, 1);
			GridPane.setHalignment(txtSongLength, HPos.CENTER);

			rootPickAudio.add(cboAudio, 0, 2);
			GridPane.setHalignment(cboAudio, HPos.CENTER);

			rootPickAudio.add(btnSubmit, 0, 3);
			GridPane.setHalignment(btnSubmit, HPos.CENTER);


			sceneEditSetup = new Scene(rootPickAudio);


			





			Circle c1 = new Circle(100, 700-100, 40);
			c1.setFill(Color.TRANSPARENT);

			Circle c2 = new Circle(100*2, 700-100, 40);
			c2.setFill(Color.TRANSPARENT);

			Circle c3 = new Circle(100*3, 700-100, 40);
			c3.setFill(Color.TRANSPARENT);

			Circle c4 = new Circle(100*4, 700-100, 40);
			c4.setFill(Color.TRANSPARENT);



			Image[] imgC = new Image[4]; 



			imgC[0] = new Image("file:images/notes/rrnote.png");
			imgC[1] = new Image("file:images/notes/bbnote.png");
			imgC[2] = new Image("file:images/notes/ggnote.png");
			imgC[3]= new Image("file:images/notes/yynote.png");


			ImageView[] ivC = new ImageView[4]; 

			for(int i = 0; i < ivC.length; i++)
			{
				ivC[i] = new ImageView(imgC[i]); 

				ivC[i].setPreserveRatio(true); 
				ivC[i].setFitHeight(80); 

				ivC[i].setX(100*(i+1) - 40); 
				ivC[i].setY(600 - 40);

				ivC[i].setOpacity(0.5); 


			}





			Line boundaryBottom = new Line(50, 600, 500, 600);
			boundaryBottom.setStrokeWidth(5);
			Line boundaryTop = new Line(50, 100, 500, 100);
			boundaryTop.setStrokeWidth(5);



			btnPlay = new Button ("PLAY"); 
			btnPlay.setLayoutX(1200-200);
			btnPlay.setLayoutY(700-100);
			btnPlay.setPrefSize(200,100);
			btnPlay.setFocusTraversable(false); 

			btnSave = new Button ("SAVE");
			btnSave.setLayoutX(1200-400);
			btnSave.setLayoutY(700-100);
			btnSave.setPrefSize(200,100);
			btnSave.setFocusTraversable(false); 

			btnLoad = new Button("LOAD");
			btnLoad.setLayoutX(1200-600);
			btnLoad.setLayoutY(700-100);
			btnLoad.setPrefSize(200,100);
			btnLoad.setFocusTraversable(false); 


			btnSkipTutorial = new Button("SKIP"); 
			btnSkipTutorial.setLayoutX(1200-200);
			btnSkipTutorial.setLayoutY(700-100);
			btnSkipTutorial.setPrefSize(200,100);
			btnSkipTutorial.setFocusTraversable(false); 

			btnEditor = new Button("EDIT");
			btnEditor.setLayoutX(150);
			btnEditor.setLayoutY(700-150);
			btnEditor.setPrefSize(100,100);
			btnEditor.setFocusTraversable(false); 

			btnUp = new Button("ALL NOTES UP");
			btnUp.setPrefSize(200,50);
			btnUp.setLayoutX(1200-400);
			btnUp.setLayoutY(350);
			btnUp.setFocusTraversable(false); 

			btnDown = new Button("ALL NOTES DOWN");
			btnDown.setPrefSize(200,50);
			btnDown.setLayoutX(1200-400);
			btnDown.setLayoutY(400);
			btnDown.setFocusTraversable(false); 


			btnReturn = new Button("MENU");
			btnReturn.setFont(fText);
			btnReturn.setPrefSize(140, 75);
			btnReturn.setLayoutX(sceneMenu.getWidth() - 145);
			btnReturn.setLayoutY(80);
			btnReturn.setFocusTraversable(false); 


			lblScore = new Label("SCORE: " + score); 

			lblScore.setFont(fHeadings2);
			lblScore.setLayoutX(600);


			//start screen components
			Label lblTitle = new Label("TITLE SCHMITLE"); 
			lblTitle.setFont(fTitle);
			lblTitle.setPrefWidth(550);
			lblTitle.setPrefHeight(300);
			lblTitle.setAlignment(Pos.CENTER);

			Button btnStart = new Button("START"); 

			Font fBtns = Font.font("Verdana", 25);
			btnStart.setFont(fBtns);
			btnStart.setPrefHeight(70);
			btnStart.setLayoutX(300);
			btnStart.setLayoutY(500);

			btnStart.setOnAction(e -> 
			{
				primaryStage.setScene(sceneGame);	
				primaryStage.centerOnScreen();

				rootGame.getChildren().add(dialog.returnLine());

			});



			Image imgTitle = new Image("file:images/titleScreen.png");
			ImageView ivTitle = new ImageView(imgTitle);



			startLayout.getChildren().addAll(ivTitle, btnStart);



			currentSongInt = 0; 
			btnEditSelected = -1; 
			lineCounter = 0; 


			vBoxSongs = new VBox();
			vBoxEdits = new VBox(); 



			btnSongs = new Button[4];
			btnEdits = new Button[4];
			ImageView ivDifficulties[] = new ImageView [4];

			// all buttons in song vBox will display their respective title, artist, and difficulty
			for(int i = 0; i < btnSongs.length; i++)
			{

				currentSongInt++; 
				currentSong = new Song();
				currentSong.setSong(currentSongInt);

				btnSongs[i] = new Button();
				btnSongs[i].setPrefSize(500, 100);
				btnSongs[i].setFocusTraversable(false); 

				ivDifficulties[i] = currentSong.getDifficultyImg();

				
				//Align and set up details of specific button
				btnSongs[i].setGraphic(ivDifficulties[i]);
				btnSongs[i].setText(currentSong.getTitle() + " - " + currentSong.songArtist());

				btnSongs[i].setAlignment(Pos.CENTER_LEFT);

				btnSongs[i].setFont(fText);

				vBoxSongs.getChildren().add(btnSongs[i]);


				btnEdits[i] = new Button(); 
				btnEdits[i].setPrefSize(500,100); 
				btnEdits[i].setFocusTraversable(false); 
				btnEdits[i].setText("NEW SONG");
				btnEdits[i].setAlignment(Pos.CENTER_LEFT);
				btnEdits[i].setFont(fText);

				//add to vbox
				vBoxEdits.getChildren().add(btnEdits[i]);

			}

			//align vbox
			vBoxSongs.setPrefSize(sceneMenu.getWidth(), sceneMenu.getHeight());
			vBoxSongs.setAlignment(Pos.CENTER_RIGHT);
			vBoxEdits.setPrefSize(sceneMenu.getWidth(),sceneMenu.getHeight());
			vBoxEdits.setAlignment(Pos.CENTER_RIGHT);

			
			



			lblMenuHeader = new Label("DIFFICULTY\t\t\tSONG"); 
			lblMenuHeader.setFont(fText);
			lblMenuHeader.setLayoutX(700);
			lblMenuHeader.setLayoutY(125);

			lblMenuTitle = new Label("PLAY");
			lblMenuTitle.setFont(fTitle);
			lblMenuTitle.setPrefSize(sceneMenu.getWidth(), sceneMenu.getHeight());
			lblMenuTitle.setAlignment(Pos.TOP_LEFT);
			lblMenuTitle.setLayoutX(lblMenuTitle.getLayoutX() + 10);
			lblMenuTitle.setLayoutY(lblMenuTitle.getLayoutY() - 10);


			play = false; 
			hitAKey = false; 
			isTutorial = true; 
			playFromStart = false; 

			edit = false; 

			isMenu = PLAY; 



			bufferStop = false; 
			bufferAmount = 500; 

			score = 0; 

			scrollY = 0;
			scrollDeltaY = 0; 

			laneSelect = 1;
			lanePlaySelect = 0; 
			noteIndex = -1; 
			outcomeIndex = -1;

			fullEditName = "";

			rectMAP = new Rectangle(100-40, 100 - 2000, 300+80, 2000);
			rectMAP.setFill(Color.DIMGREY);

			lane1 = new Rectangle(rectMAP.getX(), rectMAP.getY(), rectMAP.getWidth()/4, rectMAP.getHeight());
			lane1.setFill(Color.GREY);
			lane2 = new Rectangle(rectMAP.getX() + 100, rectMAP.getY(), rectMAP.getWidth()/4, rectMAP.getHeight());
			lane2.setFill(Color.GREY);
			lane3 = new Rectangle(rectMAP.getX() + 200, rectMAP.getY(), rectMAP.getWidth()/4, rectMAP.getHeight());
			lane3.setFill(Color.GREY);
			lane4 = new Rectangle(rectMAP.getX() + 300, rectMAP.getY(), rectMAP.getWidth()/4, rectMAP.getHeight());
			lane4.setFill(Color.GREY);


			rectSelect = new Rectangle(rectMAP.getX() + 10, 600, lane1.getWidth() - 20, 10); 
			rectSelect.setFill(Color.WHITE);

			song0 = new ArrayList<Note>(); 
			hitOutcomes = new ArrayList<Outcomes>(); 

			robotGame = new Robot(745, 175); 

			robotMenu = new Robot(); 
			robotMenu.setPos(150, (int)sceneMenu.getHeight()/2 - robotMenu.getHeight()/2);
			robotMenu.setImage(ROBOTPLAY);

			Label lblSelectSong = new Label("SELECT  A   SONG");
			lblSelectSong.setFont(fHeadings);
			lblSelectSong.setPrefSize(sceneMenu.getWidth(), sceneMenu.getHeight());
			lblSelectSong.setAlignment(Pos.TOP_RIGHT);
			lblSelectSong.setLayoutX(lblSelectSong.getLayoutX() - 30);


			dialog = new Dialogue(fDialogue);


			// assign songtime once started as 0
			currentSongTime = 0;
			// once start game, change in song's time whilst editing is always 0 
			deltaTime = 0;
			// once start game, assume original yPos of the map
			originalY = (int)rectMAP.getY(); 
			// once start game, asume there is no new time to assign to a song
			newTimeInt = 0;
			newTimeDuration = new Duration(0);


			// map speed; will move map every 20 ms 
			mapSpeed = 20; 
			mapDeltaY = 10; //map will move 10 px every 20ms 
			songLength = currentSong.getSongLength();// value is in seconds 29s 

			//generate map based on song length(tutorial)
			generateSongMap(songLength);


			//add all components to menu
			rootMenu.getChildren().addAll(lblMenuTitle, robotMenu.getImage(), lblSelectSong, vBoxSongs, btnEditor,lblMenuHeader);

			//add all components to game
			rootGame.getChildren().addAll(rectMAP, lane1, lane2, lane3, lane4, c1, c2, c3, c4, boundaryBottom, boundaryTop, lblScore, robotGame.getImage(), dialog.getBox(), btnSkipTutorial); 

			//add icons around border to game - shows when to press
			for(int i = 0; i < ivC.length; i++)
			{
				rootGame.getChildren().add(ivC[i]); 
			}

			//assign titlepage
			primaryStage.setScene(sceneStart);
			primaryStage.show();


			// Initiate an Animation Timer to show/remove each Miss/Good/Perfect outcomes for their allotted time
			outcomeAnimation = new AnimationTimer()
			{
				public void handle(long val)
				{
					// only runs if there is an outcome to be shown (hitOutcomes.size!=0) and the first outcome is null (time to display is up)
					if(hitOutcomes.size() != 0 && hitOutcomes.get(0).getNull()) 
					{
						//we focus on index 0 in the hitOutcomes arraylist because it is the oldest outcome in the list at any given moment

						//remove the outcome from the scene
						rootGame.getChildren().remove(hitOutcomes.get(0).getImage());
						//remove the outcome from its ArrayList 
						hitOutcomes.remove(0);
						outcomeIndex--; //lower size

						// ensures that the robot will resume its dance after a miss etc.
						robotGame.setImage(ROBOTPLAY);

					}

				}
			};




			//MOVEMENT TIMER - animates notes, background etc. as it scrolls down the screen 
			KeyFrame kf = new KeyFrame(Duration.millis(mapSpeed), new EventHandler<ActionEvent>()
			{
				public void handle(ActionEvent e)
				{
					if(!isTutorial || (isTutorial && play)) // no longer in tutorial 
					{ 
						if(play && rectMAP.getY() <= 600) // runs if game is running and if the map's y pos is not beyond the bottom border 
						{
							// change the ypos of the lanes + map by specified amount downwards 
							rectMAP.setY(rectMAP.getY() + mapDeltaY);
							lane1.setY(lane1.getY() + mapDeltaY);
							lane2.setY(lane2.getY() + mapDeltaY);
							lane3.setY(lane3.getY() + mapDeltaY);
							lane4.setY(lane4.getY() + mapDeltaY);

							// change the ypos of all notes alloted to the song 
							for (int i =0; i < song0.size(); i++)
							{
								song0.get(i).setY(((int)song0.get(i).getY() + mapDeltaY));

								// If note passes playability - goes beyond the bottom border
								if (song0.get(i).getY() - 40 >= 600 && song0.get(i).getNullStatus() == false)
								{
									//sets note value to null - makes sure that the note wont be considered when the player is hitting a key
									song0.get(i).setNull(true);

									//									//System.out.println("MISS - didnt hit at all");

									// if note passes playability while playing, the note is missed 
									scoreSystemMiss(song0.get(i).getLane());


									//if(song0.get(i).ge
								}
							}

							// sets song to a new time depending on the map's current position 
							// considers the ypos of the map subtracted by the buffer amount, must be above the bottom border
							//the buffer amount is the time that the game gives the player before the song starts 
							//also only runs if the song is NOT playing - only time we need to seek a new position in the song
							if(rectMAP.getY() + rectMAP.getHeight() -  bufferAmount > 600 && 
									(mpGame.getStatus() != MediaPlayer.Status.PLAYING))
							{

								// Play the song in order to seek new position in song
								mpGame.play();

								if(!playFromStart) // no need to play from the start 
								{
									// seek the new time of the song - calculated from scroll
									mpGame.seek(newTimeDuration); 
								}
								else // play from the start
								{
									// if we wish to start again, start song from start
									mpGame.seek(Duration.ZERO); 
								}


								// after we're done seeking new time of the song, reset deltaTime and newTime variables 
								deltaTime = 0; // no current change in time
								newTimeInt = 0; // no new time requested at this moment

								// considering that this condition only runs if the buffer is past, we can say that the buffer is stopped when this condition runs
								bufferStop = true; 


							}

						}
						else if(play && rectMAP.getY() >= 600) // in the case if the map goes past the bottom border
						{
							//set robot's movement to still - no longer dancing
							robotGame.setImage(ROBOTDEFAULT);

							// no longer playing
							play = false; 

							// stop presenting outcomes
							outcomeAnimation.stop(); 

							// for every note, reset their status to not null
							for (int i = 0; i < song0.size(); i++)
							{
								song0.get(i).setNull(false); 
							}

							// need to remove all outcomes on screen once not playing
							for(int i = hitOutcomes.size() -1; i >= 0; i--)
							{
								rootGame.getChildren().remove(hitOutcomes.get(i).getImage()); //clear from scene
							}
							hitOutcomes.clear(); //clear from arraylist
							outcomeIndex = -1; //reset index


							// Convert to menu and exit the first tutorial 


							primaryStage.setScene(sceneMenu);	
							primaryStage.centerOnScreen();

							rootGame.getChildren().remove(btnSkipTutorial);


							mpMenu.play(); 
						}

						if(edit && play) //editing and playing
						{
							// close to the end but still editing, can't end song yet - will pause the song if close to the end
							// we do this because if, while still editing, the song reaches its end, we cannot play it again and use the seek method
							if((mpGame.getStatus() != MediaPlayer.Status.PAUSED) && mpGame.getCycleDuration().subtract(mpGame.getCurrentTime()).compareTo(new Duration(250)) < 0)
							{

								System.out.println("PAUSE DUE TO CLOSE");

								//pausing - robot is still 
								robotGame.setImage(ROBOTDEFAULT);

								// record the time the song is at when paused in milliseconds
								currentSongTime = (int)mpGame.getCurrentTime().toMillis();

								// record the current yPos the map is at when paused
								originalY = (int)rectMAP.getY(); 

								// pause - no longer playing 
								play = false; 
								outcomeAnimation.stop(); // clear outcomes
								mpGame.pause(); // pause song

								// reset status of all notes
								for (int i = 0; i < song0.size(); i++)
								{
									song0.get(i).setNull(false); 
								}
							}
						}
					}

				}
			});
			movementTimer = new Timeline(kf); // assign timeline to keyframe
			movementTimer.setCycleCount(Timeline.INDEFINITE); //runs indefinite amount
			movementTimer.play(); 



			// KEYBOARD EVENTS
			sceneGame.setOnKeyPressed(new EventHandler<KeyEvent>()
			{
				public void handle (KeyEvent e)
				{

					// Space key involves 3 actions - switch between playing/not playing or skipping through dialogue
					if(e.getCode() == KeyCode.SPACE) 
					{
						//if out of tutorial and we are editing
						if(!isTutorial && (edit == true)) // OUT OF TUTORIAL
						{
							if(!play)  // editing to playing
							{

								// as soon as playing, game will update bufferStop 
								if(!bufferStop && rectMAP.getY() + rectMAP.getHeight() - 500 < 600) //if map is before buffer area
								{
									bufferStop = true; //stop buffer if map is before buffer area
								}
								else if(bufferStop && rectMAP.getY() + rectMAP.getHeight() - 500 > 600)//if map is above buffer area
								{
									bufferStop = false; //do not stop buffer if map is after buffer area
								}

								// if map is inside buffer, start song from start
								if(bufferStop)
								{
									playFromStart = true;
								}
								// if map is out of buffer, do not set song from start
								else
								{
									playFromStart = false; 
								}

								// start playing
								play = true; 
								//edit = false;

								// show outcomes as warranted
								outcomeAnimation.start(); 

								// robot will dance as the song plays 
								robotGame.setImage(ROBOTPLAY);

								//convert the change in time to px and add the 
										//change to the current song time, this
										//will be the new time the song should 
										//look for when seeking
								newTimeInt = currentSongTime + (2*deltaTime); 
								//convert the integer value to duration for the seek method 
								newTimeDuration = new Duration(newTimeInt);



							}
							else // playing to editing 
							{

								// change robot dance to still 
								robotGame.setImage(ROBOTDEFAULT);

								// record the song's time at that moment in mx
								currentSongTime = (int)mpGame.getCurrentTime().toMillis();

								// record the map yPos at that moment
								originalY = (int)rectMAP.getY(); 

								// no longer playing
								play = false; 
								edit = true; 

								//stop showing outcomes
								outcomeAnimation.stop();
								//pause song
								mpGame.pause(); 

								//reset status of each note 
								for (int i = 0; i < song0.size(); i++)
								{
									song0.get(i).setNull(false); 
								}
							}
						}
						else if(isTutorial)//IN TUTORIAL - show dialogue
						{
							// move to next dialogue line
							dialog.setLine(); 


							// if at last dialogue - remove the dialogue box + text from scene
							if(dialog.getStatus() == TUTORIALNONE && !play)
							{

								// start playing conditions
								play = true;
								edit = false;

								robotGame.setImage(ROBOTPLAY);
								outcomeAnimation.start(); 


								removeTutorial(); 
								//load the tutorial song
								loadSong(edit); 

								score = 0; 


							}

						}

					}

					// if playing - meant to record keys the player hits 
					if(play) 
					{
						// only looks at key binds - ASKL 
						if(e.getCode() == KeyCode.A || e.getCode() == KeyCode.S ||e.getCode() == KeyCode.K ||e.getCode() == KeyCode.L)
						{
							// logs the lane selected according to the key pressed
							if(e.getCode() == KeyCode.A)
							{
								lanePlaySelect = 1; 
							}
							else if(e.getCode() == KeyCode.S)
							{
								lanePlaySelect = 2; 
							}
							else if(e.getCode()== KeyCode.K)
							{
								lanePlaySelect = 3;
							}
							else if (e.getCode() == KeyCode.L)
							{
								lanePlaySelect = 4;
							}


							// when pressed, checks how accurate the keypress was for that note 
							for(int i = 0; i < song0.size(); i++)
							{
								// if the lane that was selected lines up with the lane of the note
								if (song0.get(i).getLane() == lanePlaySelect)
								{
									// if the difference in yPos between the note and the bottom line is small -> very accurate
									if(Math.abs(song0.get(i).getY() - c1.getCenterY()) <= 10) 
									{

										// this hit is considered perfect 
										scoreSystemHit(PERFECT); 

										// this note is now null
										song0.get(i).setNull(true);

										// a note is hit 
										hitAKey = true; 


									}
									// if the difference in yPos between the note and the bottom line is medium -> somewhat accurate
									else if(Math.abs(song0.get(i).getY() - c1.getCenterY()) <= 40)
									{

										// this hit is considered good 
										scoreSystemHit(GOOD); 

										// this note is now null
										song0.get(i).setNull(true);

										// a note is hit 
										hitAKey = true;  

									}
								}
							}

							// if a note was not hit when key pressed, consider this a miss and deduct points
							if(!hitAKey)
							{
								//hit a key and nothing touched
								scoreSystemHit(MISS);
							}

							// once this code is done running, no longer consider that a key hits a note
							hitAKey = false;

						}
					}

					// EDITING A MAP - keyboard event
					if(!play)
					{
						// need to remove all outcomes on screen once not playing
						for(int i = hitOutcomes.size() -1; i >= 0; i--)
						{
							rootGame.getChildren().remove(hitOutcomes.get(i).getImage());
						}
						hitOutcomes.clear();
						outcomeIndex = -1; 

						// TO CONTROL KEY POINTER - selects what lane the user wishes to place the desired note
						if(e.getCode() == KeyCode.RIGHT && laneSelect != 4) //shift right
						{
							laneSelect++; //select a lane and move it
							rectSelect.setX(rectSelect.getX() + 100);
						}

						else if(e.getCode() == KeyCode.LEFT && laneSelect != 1) //shift left
						{
							laneSelect--; //select a lane and move it
							rectSelect.setX(rectSelect.getX() - 100);
						}


						//TO ADD/REMOVE A NOTE TO MAP
						if(e.getCode() == KeyCode.TAB && rectMAP.getY() + rectMAP.getHeight() - 20 >= 600) //add a note in specified bounds
						{
							// add new note 
							song0.add(new Note(laneSelect));
							noteIndex++; 
							song0.get(noteIndex).setY(600);
							rootGame.getChildren().addAll(song0.get(noteIndex).getNote(), song0.get(noteIndex).getIV());
						}
						if(e.getCode() == KeyCode.BACK_SPACE) //remove a note
						{

							//look for the selected note in ArrayList
							for (int i = 0; i < song0.size(); i++)
							{
								// if the selected lane to delete the note lines up with the current note
								if (laneSelect == song0.get(i).getLane())
								{
									// and if the pointer intersects the note 
									if (rectSelect.intersects(song0.get(i).getNote().getBoundsInParent()))
									{
										//remove the note
										rootGame.getChildren().removeAll(song0.get(i).getNote(), song0.get(i).getIV()); 
										song0.remove(i); 
										noteIndex--; 
									}
								}
							}
						}
					}
				}
			});


			//SCROLLING MAP - only occurs whilst editing the map 
			sceneGame.setOnScroll(new EventHandler<ScrollEvent>()
			{
				public void handle (ScrollEvent e)
				{

					if(!play && !isTutorial) // editing - the map is not running 
					{

						// runs if scrolling down (map will go down) and if the scroll difference + 
									//map yPos is less than 600
						// the 2nd condition is to ensure that user cannot scroll down 
									//past the bottom limit (600)
						// for instance, if the map yPos was close to the bottom limit and 
									//the scroll difference requested added TO that yPos 
									//yielded a value greater than the limit's yPos, 
									//do not move the map
						if(e.getDeltaY() > 0 && e.getDeltaY()+ rectMAP.getY() < 600)
						{
							// scrollY is new yPos we will assign to the map's components to move it down 
							scrollY = (int)rectMAP.getY() + (int)e.getDeltaY(); 
							//the yPos difference will be the ypos difference retrieved by ScrollEvent
							scrollDeltaY = (int)e.getDeltaY();
							//if viable, add scroll to the change in song's time
							deltaTime += e.getDeltaY(); 


						}
						// if user wants to scroll down but cannot because it will exceed bottom limit, set each component's yPos limited to the bottom limit
						else if(e.getDeltaY() > 0)
						{
							scrollY = 600;
							scrollDeltaY = 0; // no diff in ypos
						}

						// runs if scrolling up (map will move up) and if the scroll difference + map Ypos is greater than 100
						// the 2nd condition is to ensure that user cannot scroll up past the upper limit (100)
						// for instance, if the map yPos was close to the upper limit and the scroll difference requested added TO that yPos yielded a value less than the limit's yPos, do not move the map
						if(e.getDeltaY() < 0 && e.getDeltaY()  + (rectMAP.getY() + rectMAP.getHeight()) > 100)
						{
							// scrollY is new yPos we will assign to the map's components to move it up 
							scrollY = (int)rectMAP.getY()  + (int)e.getDeltaY(); 
							//the yPos difference will be the ypos difference retrieved by ScrollEvent
							scrollDeltaY = (int)e.getDeltaY();
							//if viable, add scroll to the change in song's time
							deltaTime += e.getDeltaY(); 

						}
						// if user wants to scroll up but cannot because it will exceed upper limit, set the bottom of each component ypositions limited to the upper limit
						else if (e.getDeltaY() < 0)
						{
							scrollY = 100 - (int)rectMAP.getHeight() ;
							scrollDeltaY = 0; // no diff in ypos
						}

						//if  no movement, no change in time
						if((int)rectMAP.getY() == originalY)
						{
							deltaTime = 0; 
						}


						//Assign new ypos to each static component on map
						rectMAP.setY(scrollY);
						lane1.setY(scrollY);
						lane2.setY(scrollY);
						lane3.setY(scrollY);
						lane4.setY(scrollY);

						//cycle through each note and set its new yPos 
						for(int i = 0; i < song0.size(); i++)
						{
							// add appropriate difference in scroll
							song0.get(i).setY(((int)song0.get(i).getY() + (int)scrollDeltaY));
						}

						// before buffer if map is before buffer
						if(!bufferStop && rectMAP.getY() + rectMAP.getHeight() - 500 < 600)
						{
							bufferStop = true; 
						}
						//after buffer is map is after buffer
						else if(bufferStop && rectMAP.getY() + rectMAP.getHeight() - 500 > 600)
						{
							bufferStop = false; 
						}
					}
				}
			});



			// when editing - user can press play 
			// starts the currently edited song from the start, allows user to preview their song 
			btnPlay.setOnAction(e ->
			{
				{

					// play song from start
					playFromStart = true; 
					// set robot to play
					robotGame.setImage(ROBOTPLAY);

					// changing to default position
					defaultPos(); 

					// show outcomes since playing
					outcomeAnimation.start(); 

					//set conditions 
					play = true; 
					edit = true; 
					
					// done to prevent disturbance with keyevent
					btnPlay.setFocusTraversable(false);
				}
			});

			//when editing - user can press save
			// saves what the user has on map to a text file - this file keeps track of what notes are on the map
			btnSave.setOnAction(e -> 
			{

				// start from beginning - always record notes at thois position
				playFromStart = true; 
				defaultPos(); 


				// done to prevent disturbance with keyevent
				btnSave.setFocusTraversable(false); 

				
				//create a text file based on what button is pressed
				currentEdit[btnEditSelected].createTextFile();


				//check if this is a new file
				if(currentEdit[btnEditSelected].isNewFile())
				{

					//if new file, ask for title, artist, and difficulty 
					 
					Note noteGraphic = new Note(1);  // graphic to textinputdialog
					boolean valid = false;

					// ask for name
					TextInputDialog dialog = new TextInputDialog(); 
					dialog.setTitle("NEW SONG DETAILS");
					dialog.setHeaderText(null);
					dialog.setContentText("Name of song:");
					dialog.setGraphic(noteGraphic.getIV());

					Optional<String> result1 = dialog.showAndWait();

					//set title according to this result

					if(result1.isPresent())
					{
						currentEdit[btnEditSelected].setTitle(result1.get());
					}
					else
					{
						currentEdit[btnEditSelected].setTitle("");
					}


					// ask for artist
					TextInputDialog dialog2 = new TextInputDialog(); 
					dialog2.setTitle("NEW SONG DETAILS");
					dialog2.setHeaderText(null);
					dialog2.setContentText("Name of artist:");
					dialog2.setGraphic(noteGraphic.getIV());

					Optional<String> result2 = dialog2.showAndWait();

					//set artist according to this result
					if(result2.isPresent())
					{
						
						currentEdit[btnEditSelected].setArtist(result2.get());
					}
					else
					{
						currentEdit[btnEditSelected].setArtist("");
					}


					//ask for difficulty
					TextInputDialog dialog3 = new TextInputDialog(); 
					dialog3.setTitle("NEW SONG DETAILS");
					dialog3.setHeaderText(null);
					dialog3.setContentText("Difficulty:\nDifficulty must be one digit (1-10)");
					dialog3.setGraphic(noteGraphic.getIV());


					Optional<String> result3;

					do
					{
						result3 = dialog3.showAndWait();

						// only valid if the difficulty is a single digit value
						if(!result3.isPresent() || result3.get().length() != 1 || !Character.isDigit(result3.get().charAt(0)))
						{
							valid = false; 


						}
						else
						{
							valid = true; 
						}

					}while(!valid);


					//set difficulty according to this result
					currentEdit[btnEditSelected].setDifficulty(Integer.parseInt(result3.get()));

				}


				//refer to file in the selected button
				file = currentEdit[btnEditSelected].getSaveFile(); 




				// set up filewriter and bufferedwriter
				try {
					fw  = new FileWriter(file, true);
				} 
				catch (IOException exception) {
					System.out.println("Problem writing to file.");
					System.err.println("IOException: " + exception.getMessage());
				} 
				bw = new BufferedWriter(fw); 

				//WRITE TO FILE

				try
				{
					//write title, artist, and difficulty according to button - will be refered to when player closes game
					bw.write(currentEdit[btnEditSelected].getTitle());
					bw.newLine();
					bw.write(currentEdit[btnEditSelected].songArtist());
					bw.newLine();
					bw.write(Integer.toString(currentEdit[btnEditSelected].getDifficulty()));
					bw.newLine(); 

					// write ypos of all notes
					for(int i = 0; i < song0.size(); i++)
					{
						bw.write(song0.get(i).getLane() + " " + song0.get(i).getY());
						bw.newLine();
					}

					// close buffered writer and reader
					bw.close();
					fw.close(); 
				}
				catch(IOException exception) //in case of an exception
				{
					System.out.println("Problem writing to file.");
					System.err.println("IOException: " + exception.getMessage());
				}


			});


			//LOAD SONG - while editing 
			//loads what is on file for specfied song 
			btnLoad.setOnAction(e -> 
			{

				// if textfile for this song exists
				if(currentEdit[btnEditSelected].checkTextFile(btnEditSelected))
				{
					// warns user that edits to map will be wiped when loading
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Loading File...");
					alert.setHeaderText(null);
					alert.setContentText("WARNING! Loading in this file will wipe what you have.\nContinue?");

					Optional<ButtonType> result = alert.showAndWait();

					if(result.get() == ButtonType.OK) //loads song if user confirms
					{
						loadSong(edit); 
					}

				}
				else //if not, tell user theres no file 
				{
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("ERROR");
					alert.setHeaderText(null);
					alert.setContentText("No file to load from.");
					alert.showAndWait();
				}

			});


			// to skip tutorial - move to menu
			btnSkipTutorial.setOnAction(e ->
			{

				// stop game song 
				mpGame.stop();

				
				removeTutorial(); 

				//set conditions
				play = false;
				edit = false;
				robotGame.setImage(ROBOTDEFAULT);
				outcomeAnimation.stop(); 

				// for every note, reset their status to not null
				for (int i = 0; i < song0.size(); i++)
				{
					song0.get(i).setNull(false); 

				}

				//remove all notes
				for(int i = song0.size() -1; i >= 0; i--)
				{
					rootGame.getChildren().removeAll(song0.get(i).getNote(), song0.get(i).getIV());
				}
				song0.clear();
				noteIndex = -1; 

				// need to remove all outcomes on screen once not playing
				for(int i = hitOutcomes.size() -1; i >= 0; i--)
				{
					rootGame.getChildren().remove(hitOutcomes.get(i).getImage()); //clear from scene
				}
				hitOutcomes.clear(); //clear from arraylist
				outcomeIndex = -1; //reset index


				// Convert to menu and exit the first tutorial 

				primaryStage.setScene(sceneMenu);	
				primaryStage.centerOnScreen();

				rootGame.getChildren().remove(btnSkipTutorial);


				mpMenu.play(); 


			});

			//move all notes down
			btnDown.setOnAction(e -> 
			{
				for(int i = 0; i < song0.size(); i ++)
				{
					song0.get(i).setY(song0.get(i).getY() +5);
				}
			});
			
			//move all notes up 
			btnUp.setOnAction(e -> 
			{
				for(int i = 0; i < song0.size(); i ++)
				{
					song0.get(i).setY(song0.get(i).getY() - 5);
				}
			});

			// play songs depending on what button picked
			btnSongs[0].setOnAction(e -> 
			{
				specifyGameSong(primaryStage, 1);

			});
			btnSongs[1].setOnAction(e -> 
			{

				specifyGameSong(primaryStage, 2);

			});
			btnSongs[2].setOnAction(e -> 
			{

				specifyGameSong(primaryStage, 3);

			});
			btnSongs[3].setOnAction(e -> 
			{
				specifyGameSong(primaryStage, 4);

			});



			// convert to editing  menu
			btnEditor.setOnAction(e -> 
			{
				// convert to editing menu
				if(isMenu == PLAY)
				{
					isMenu = EDIT;

					rootMenu.getChildren().remove(btnEditor);

					rootMenu.getChildren().remove(vBoxSongs);
					rootMenu.getChildren().add(vBoxEdits);

					rootMenu.getChildren().add(btnEditor);


					lblMenuTitle.setText("EDIT");

					edit = true; 
					play = false; 

				}
				//convert to playing menu
				else if (isMenu == EDIT)
				{
					isMenu = PLAY;

					rootMenu.getChildren().remove(btnEditor);


					rootMenu.getChildren().remove(vBoxEdits);
					rootMenu.getChildren().add(vBoxSongs);

					rootMenu.getChildren().add(btnEditor);


					lblMenuTitle.setText("PLAY");


					edit = false; 
					play = false; 
				}

				//show menu with editor songs
				checkOnEditMenu(edit); 


			});

			//show editable songs
			btnEdits[0].setOnAction(e -> 
			{
				
				// CLEAR WHAT IS ON GAME

				for(int i = song0.size() -1; i >= 0; i--)
				{
					rootGame.getChildren().removeAll(song0.get(i).getNote(), song0.get(i).getIV());
				}
				song0.clear();
				noteIndex = -1; 
				
				//specify what song chosen
				btnEditSelected = 0; 

				//specify file 
				currentEdit[btnEditSelected].setFileIndex(0);

				//check if file for this button already
				if(currentEdit[btnEditSelected].checkTextFile(btnEditSelected))
				{
					mpMenu.stop();

					primaryStage.setScene(sceneGame);
					primaryStage.centerOnScreen();
					rootGame.getChildren().addAll(btnLoad, btnSave, btnPlay, btnDown, btnUp, rectSelect, btnReturn);
					specifyEdit(primaryStage);
				}
				else
				{
					//if not, set up what song to use for this new file
					setupEdit(primaryStage);
				}


			});
			//continue for other buttons
			btnEdits[1].setOnAction(e -> 
			{
				
				// CLEAR WHAT IS ON GAME

				for(int i = song0.size() -1; i >= 0; i--)
				{
					rootGame.getChildren().removeAll(song0.get(i).getNote(), song0.get(i).getIV());
				}
				song0.clear();
				noteIndex = -1; 
				
				btnEditSelected = 1; 

				currentEdit[btnEditSelected].setFileIndex(1);

				if(currentEdit[btnEditSelected].checkTextFile(btnEditSelected))
				{
					mpMenu.stop();

					primaryStage.setScene(sceneGame);
					primaryStage.centerOnScreen();
					rootGame.getChildren().addAll(btnLoad, btnSave, btnPlay, btnDown, btnUp, rectSelect, btnReturn);
					specifyEdit(primaryStage);
				}
				else
				{
					setupEdit(primaryStage);

				}

			});
			btnEdits[2].setOnAction(e -> 
			{
				
				// CLEAR WHAT IS ON GAME

				for(int i = song0.size() -1; i >= 0; i--)
				{
					rootGame.getChildren().removeAll(song0.get(i).getNote(), song0.get(i).getIV());
				}
				song0.clear();
				noteIndex = -1; 
				
				btnEditSelected = 2; 

				currentEdit[btnEditSelected].setFileIndex(2);


				if(currentEdit[btnEditSelected].checkTextFile(btnEditSelected))
				{
					mpMenu.stop();

					primaryStage.setScene(sceneGame);
					primaryStage.centerOnScreen();
					rootGame.getChildren().addAll(btnLoad, btnSave, btnPlay, btnDown, btnUp, rectSelect, btnReturn);
					specifyEdit(primaryStage);
				}
				else
				{
					setupEdit(primaryStage);

				}
			});
			btnEdits[3].setOnAction(e -> 
			{

				// CLEAR WHAT IS ON GAME

				for(int i = song0.size() -1; i >= 0; i--)
				{
					rootGame.getChildren().removeAll(song0.get(i).getNote(), song0.get(i).getIV());
				}
				song0.clear();
				noteIndex = -1; 
				
				
				btnEditSelected = 3; 

				currentEdit[btnEditSelected].setFileIndex(3);

				if(currentEdit[btnEditSelected].checkTextFile(btnEditSelected))
				{
					mpMenu.stop();
					primaryStage.setScene(sceneGame);
					primaryStage.centerOnScreen();
					rootGame.getChildren().addAll(btnLoad, btnSave, btnPlay, btnDown, btnUp, rectSelect, btnReturn);
					specifyEdit(primaryStage);
				}
				else
				{
					setupEdit(primaryStage);

				}
			});


			// returns to menu from editing 
			btnReturn.setOnAction(e -> 
			{
				//alert user that returning to menu will remove unsaved changed
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Returning To Menu");
				alert.setHeaderText(null);
				alert.setContentText("WARNING! Returning to menu will remove unsaved changes.\nContinue?");

				Optional<ButtonType> result = alert.showAndWait(); 

				if(result.get() == ButtonType.OK) //confirm and return to menu
				{

					checkOnEditMenu(edit); 

					mpMenu.play(); 

					primaryStage.setScene(sceneMenu);	//how to recenter 
					primaryStage.centerOnScreen();

					rootGame.getChildren().removeAll(btnLoad, btnSave, btnPlay, btnDown, btnUp, rectSelect, btnReturn);				
				}


			});
			
			
			//CONFIRMS SET UP OF EDITS
			btnSubmit.setOnAction(e -> 
			{
				isValid = true; 

				// valid if duration of song is 2 digits - too long if not!
				if(txtSongLength.getText().length() < 3)
				{

					try
					{
						//valid if song's length can be transferred to integer
						editSongLength = Integer.parseInt(txtSongLength.getText());
						currentEdit[btnEditSelected].setLength(editSongLength); 
					}
					catch(Exception e1)
					{
						isValid = false; 
					}

				}
				else
				{
					isValid = false; 
				}

				if(cboAudio.getValue() != null) //must pick a value from combobox
				{
					currentEdit[btnEditSelected].setSong
					(fileUserAudios[Integer.parseInt
					        (cboAudio.getValue().substring(0, 1))].getName());
				}
				else
				{
					isValid = false; 
				}

				if(!isValid) // show error if no valid
				{
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("ERROR");
					alert.setHeaderText(null);
					alert.setContentText("Error submitting song details.\nSong's length must be less than 2 digits.\nSong's length must be an integer value.\nMust pick a song.");
					alert.showAndWait();
				}
				else // if valid, show edit for this song
				{
					//set stage to editing
					primaryStage.setScene(sceneGame);
					primaryStage.centerOnScreen();
					rootGame.getChildren().addAll(btnLoad, btnSave, btnPlay, btnDown, btnUp, rectSelect, btnReturn);
					specifyEdit(primaryStage);

				}

			});



		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}


	// update score depending on if hit a note
	public void scoreSystemHit(int hitOutcome)
	{


		outcomeIndex++; 
		hitOutcomes.add(new Outcomes(lane1, lanePlaySelect));
		hitOutcomes.get(outcomeIndex).setImage(hitOutcome);
		hitOutcomes.get(outcomeIndex).startTimer(); 

		rootGame.getChildren().add(hitOutcomes.get(outcomeIndex).getImage());


		if(hitOutcome == GOOD)
		{
			score += 75; 
		}
		else if(hitOutcome == PERFECT)
		{
			score += 100; 
		}
		else if(hitOutcome == MISS)
		{
			robotGame.setImage(ROBOTMISS);

			if(score - 50 > 0)
			{
				score -= 50; 
			} 
			else 
			{
				score = 0; 
			}
		}

		lblScore.setText("SCORE: " + score);


	}

	// update score depending on if a note was missed
	public void scoreSystemMiss(int missedLane)
	{


		robotGame.setImage(ROBOTMISS);

		outcomeIndex++; 
		hitOutcomes.add(new Outcomes(lane1, missedLane));
		hitOutcomes.get(outcomeIndex).setImage(MISS);
		hitOutcomes.get(outcomeIndex).startTimer(); 

		rootGame.getChildren().add(hitOutcomes.get(outcomeIndex).getImage());


		if(score -50 > 0)
		{
			score -= 50; 
		} 
		else
		{
			score =0; 
		}
		lblScore.setText("SCORE: " + score);


	}

	//position map to upper border
	public void defaultPos()
	{
		//position to top  boundary 

		for (int i =0; i < song0.size(); i++)
		{
			int diffY = (int)rectMAP.getY() + (int)rectMAP.getHeight() -(int)song0.get(i).getY();

			//System.out.println(diffY);

			song0.get(i).setY(100 - (diffY));


		}	


		rectMAP.setY( 100 - rectMAP.getHeight());
		lane1.setY(rectMAP.getY());
		lane2.setY(rectMAP.getY());
		lane3.setY(rectMAP.getY());
		lane4.setY(rectMAP.getY());
	}

	//generate map based on song length 
	public void generateSongMap(int songLength) 
	{

		rectMAP.setHeight(((songLength*1000)*mapDeltaY/mapSpeed) + 2500); //convert to ms, then to px
		lane1.setHeight(rectMAP.getHeight());
		lane2.setHeight(rectMAP.getHeight());
		lane3.setHeight(rectMAP.getHeight());
		lane4.setHeight(rectMAP.getHeight());

		rectMAP.setY(600 - rectMAP.getHeight());
		lane1.setY(rectMAP.getY());
		lane2.setY(rectMAP.getY());
		lane3.setY(rectMAP.getY());
		lane4.setY(rectMAP.getY());

		originalY = (int)rectMAP.getY(); 

	}

	//load song
	public void loadSong(boolean edit)
	{
		playFromStart = true; 

	//	file = currentEdit[btnEditSelected].getSaveFile(); 

		
		try {
			fr = new FileReader(file);
		} catch (FileNotFoundException e1) {
			System.out.println("Problem reading file.");
			System.err.println("IOException: " + e1.getMessage());
		}

		br = new BufferedReader(fr); 
		currentLine = "";



		// changing to default position
		defaultPos(); 

		// CLEAR WHAT IS ON GAME

		for(int i = song0.size() -1; i >= 0; i--)
		{
			rootGame.getChildren().removeAll(song0.get(i).getNote(), song0.get(i).getIV());
		}
		song0.clear();
		noteIndex = -1; 


		//LOAD TO GAME 

		try
		{
			if (edit)
			{
				while((currentLine = br.readLine()) != null)
				{	
					lineCounter++; 

					if(lineCounter <= 3)
					{
						if(lineCounter == 1)
						{
							currentEdit[btnEditSelected].setTitle(currentLine);
						}
						else if(lineCounter ==2)
						{
							currentEdit[btnEditSelected].setArtist(currentLine);
						}
						else if(lineCounter == 3)
						{
							currentEdit[btnEditSelected].setDifficulty(Integer.parseInt(currentLine));
						}
					}
					else
					{

						noteIndex++;
						song0.add(new Note(Integer.parseInt(currentLine.substring(0, 1)), Integer.parseInt(currentLine.substring(2))));

						rootGame.getChildren().addAll(song0.get(noteIndex).getNote(), song0.get(noteIndex).getIV());
					}
				}
			}
			else 
			{
				while((currentLine = br.readLine()) != null)
				{	


					noteIndex++;
					song0.add(new Note(Integer.parseInt(currentLine.substring(0, 1)), Integer.parseInt(currentLine.substring(2))));

					rootGame.getChildren().addAll(song0.get(noteIndex).getNote(), song0.get(noteIndex).getIV());

				}
			}

			br.close();
			fr.close();

			lineCounter = 0; 

		}
		catch(IOException exception)
		{
			System.out.println("Problem writing to file.");
			System.err.println("IOException: " + exception.getMessage());
		}

	}

	//specify what song is chosen based on button rpesed
	public void specifyGameSong(Stage primaryStage, int selectedSong)
	{
		score = 0; 
		lblScore.setText("SCORE: " + score);


		mpMenu.stop(); 

		edit = false; 
		play = true; 
		isTutorial = false; 

		currentSong.setSong(selectedSong); 

		generateSongMap(currentSong.getSongLength());

		mpGame = new MediaPlayer(currentSong.getMedia());
		mpGame.setAutoPlay(true);
		mpGame.stop();

		file = currentSong.getSaveFile(); 

		robotGame.setImage(ROBOTPLAY);
		outcomeAnimation.start(); 

		loadSong(edit); 

		//rootGame.getChildren().addAll(btnLoad, btnWrite, btnPlay, btnDown, btnUp, rectSelect);

		primaryStage.setScene(sceneGame);	
		primaryStage.centerOnScreen();
	}


	//remove components in tutorial
	public void removeTutorial()
	{
		rootGame.getChildren().removeAll(dialog.getBox(), dialog.returnLine());

	}

	//setup edit scene
	public void setupEdit(Stage primaryStage)
	{
		mpMenu.stop();
		primaryStage.setScene(sceneEditSetup);
		primaryStage.centerOnScreen();

	}

	//generate the editing song conditions
	public void specifyEdit(Stage primaryStage)
	{
		score = 0; 
		lblScore.setText("SCORE: " + score);



		edit = true; 
		play = false; 
		isTutorial = false; 


		generateSongMap(currentEdit[btnEditSelected].getSongLength());

		mpGame = new MediaPlayer(currentEdit[btnEditSelected].getMedia());
		System.out.println(currentEdit[btnEditSelected].getMedia().toString());
		mpGame.setAutoPlay(true);
		mpGame.stop();


		robotGame.setImage(ROBOTDEFAULT);

		primaryStage.setScene(sceneGame);	
		primaryStage.centerOnScreen();
	}

	// when returning to edit menu, check each file for artist, title difficulty, and displauy it
	public void checkOnEditMenu(boolean edit)
	{
		for(int i = 0; i < currentEdit.length; i++)
		{
			if(currentEdit[i].checkTextFile(i))
			{
				file = currentEdit[i].getSaveFile();

				try {
					fr  = new FileReader(file);
				} 
				catch (IOException exception) {
					System.out.println("Problem reading to file.");
					System.err.println("IOException: " + exception.getMessage());
				} 
				br = new BufferedReader(fr); 




				try
				{
					if(edit)
					{
						while((currentLine = br.readLine()) != null)
						{	
							lineCounter++; 

							if(lineCounter <= 3)
							{
								if(lineCounter == 1)
								{
									fullEditName += currentLine + " - ";
									currentEdit[i].setTitle(currentLine);

								}
								else if(lineCounter ==2)
								{
									fullEditName += currentLine; 
									currentEdit[i].setArtist(currentLine);

								}
								else if(lineCounter == 3)
								{
									currentEdit[i].setDifficulty(Integer.parseInt(currentLine));

									btnEdits[i].setGraphic(currentEdit[i].getDifficultyImg());

								}

							}

						}
					}


					br.close();
					fr.close();

					btnEdits[i].setText(fullEditName);
					fullEditName = ""; 

				}
				catch(IOException exception)
				{
					System.out.println("Problem writing to file.");
					System.err.println("IOException: " + exception.getMessage());
				}


				lineCounter = 0; 



			}
		}
	}


}
