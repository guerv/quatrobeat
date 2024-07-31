package application;

import java.io.File;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;

public class Song 
{
	private File fSong, fText;
	private Media media; 
	private int songLength, difficulty;
	private String songTitle, songArtist; 
	protected Image imgDifficultyNums[]; 
	private ImageView ivDifficulty; 

	public Song()
	{
		
		imgDifficultyNums = new Image[10];
		
		for(int i = 0; i < imgDifficultyNums.length; i++)
		{
			imgDifficultyNums[i] = new Image("file:images/numbers/" + (i+1) + ".png");
		}
		
		ivDifficulty = new ImageView(); 
		
		fSong = new File("audio/tutorialSong.wav");
		media = new Media(fSong.toURI().toString());
		songLength = 28; // in seconds

		difficulty = 1; 

		songTitle = "Tutorial";
		songArtist = ""; 

		fText = new File("levels/tutorial.txt");

	}

	public void setSong(int selectedSong)
	{


		if(selectedSong == 1)
		{
			fSong = new File("audio/20221002.wav");
			media = new Media(fSong.toURI().toString());
			songLength = 40; // in seconds

			difficulty = 4; 

			songTitle = "20221002";
			songArtist = "Otto Benson"; 
			
		}


		else if(selectedSong == 2)
		{
			fSong = new File("audio/piano un1 arpej.wav");
			media = new Media(fSong.toURI().toString());
			songLength = 30; // in seconds

			difficulty = 8; 

			songTitle = "piano un1 arpej";
			songArtist = "Aphex Twin"; 
		}


		else if(selectedSong == 3)
		{
			fSong = new File("audio/she.wav");
			media = new Media(fSong.toURI().toString());
			songLength = 32; // in seconds

			difficulty = 3; 

			songTitle = "She";
			songArtist = "Tyler, The Creator"; 
		}
		else if(selectedSong == 4)
		{
			fSong = new File("audio/siesta.wav");
			media = new Media(fSong.toURI().toString());
			songLength = 38; // in seconds

			difficulty = 9; 

			songTitle = "Siesta";
			songArtist = "Sadesper Record"; 
		}

		ivDifficulty = new ImageView(imgDifficultyNums[difficulty-1]);
		fText = new File("levels/song" + selectedSong +".txt");
	}

	public int getSongLength()
	{
		return songLength; 
	}

	public Media getMedia()
	{
		return media; 
	}

	public ImageView getDifficultyImg()
	{
		
		return ivDifficulty; 
	}

	public String getTitle()
	{
		return songTitle; 
	}

	public String songArtist()
	{
		return songArtist; 
	}

	public File getSaveFile()
	{
		return fText; 
	}
	
	public int getDifficulty()
	{
		return difficulty;
	}


}
