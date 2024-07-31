package application;

import java.io.File;
import java.io.IOException;

import javafx.scene.image.ImageView;
import javafx.scene.media.Media;

public class UserSong extends Song
{

	private File fSong, fText; 
	private Media media; 
	private int difficulty, songLength, btnNumber; 
	private boolean isNewFile; 
	private String songTitle, songArtist;
	private ImageView ivDifficulty; 

	public UserSong()
	{
		fSong = new File("userAudio/Fingerbib.wav");
		media = new Media(fSong.toURI().toString());
		difficulty = 0; 

		isNewFile  = true; 

		btnNumber = 0; 

		fText = new File("");

		songTitle = "";
		songArtist = "";
		
		ivDifficulty = new ImageView(); 
	}

	public void setSong(String selectedSong)
	{
		fSong = new File("userAudio/" + selectedSong);
		media = new Media(fSong.toURI().toString());
	}

	public void setLength (int songLength)
	{
		this.songLength = songLength; 
	}

	public void setFileIndex(int btnNumber)
	{
		this.btnNumber = btnNumber; 
	}

	public Media getMedia()
	{
		return media; 
	}

	public void createTextFile()
	{
		fText = new File("userLevels/" + btnNumber + ".txt");


		if(!fText.exists())
		{
			try {

				isNewFile = true;
				fText.createNewFile();


			} catch (IOException e) {
				System.err.println("IOException: " + e.getMessage());
			}

		}
		else
		{	
			isNewFile = false; 

			fText.delete(); 
			try {
				fText.createNewFile();

			} catch (IOException e) {
				System.err.println("IOException: " + e.getMessage());
			}

		}


	}
	
	public boolean checkTextFile(int fileNum)
	{
		
		boolean exists = false; 
		
		fText = new File("userLevels/" + fileNum + ".txt");

		if(fText.exists())
		{
			exists = true;
		}
		
		return exists; 
		
	}

	public File getSaveFile()
	{
		return fText;
	}

	public int getButtonNumber()
	{
		return btnNumber; 
	}

	public boolean isNewFile()
	{
		return isNewFile; 
	}

	public void setTitle(String title)
	{
		songTitle = title; 
	}

	public void setArtist(String artist)
	{
		songArtist = artist; 
	}

	public void setDifficulty (int diff)
	{
		difficulty = diff; 

		ivDifficulty = new ImageView(imgDifficultyNums[difficulty-1]);

	}
	
	public String getTitle()
	{
		return songTitle; 
	}

	public String songArtist()
	{
		return songArtist; 
	}
	
	public int getDifficulty()
	{
		return difficulty; 
	}
	
	public ImageView getDifficultyImg()
	{
		
		return ivDifficulty; 
	}

}
