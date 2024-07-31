package application;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Robot 
{
	private int xPos, yPos, width, height;
	private Image imgStill, imgDance, imgMiss;
	private ImageView ivRobot; 
	
	final int DEFAULT = 0, PLAY = 1, MISS = 2; 
	
	public Robot(int x, int y)
	{
		xPos = x; 
		yPos = y; 
		
		imgStill = new Image("file:images/robot.png");
		imgDance = new Image("file:images/robotoDance.gif");
		imgMiss = new Image("file:images/robotoMiss.gif");
		

		width = (int)imgStill.getWidth();
		height = (int)imgStill.getHeight();
		
		ivRobot = new ImageView(imgStill); 
		
		ivRobot.setX(xPos);
		ivRobot.setY(yPos);

	}
	
	public Robot()
	{
		xPos = 0; 
		yPos = 0; 
		
		imgStill = new Image("file:images/robot.png");
		imgDance = new Image("file:images/robotoDance.gif");
		imgMiss = new Image("file:images/robotoMiss.gif");
		

		width = (int)imgStill.getWidth();
		height = (int)imgStill.getHeight();
		
		ivRobot = new ImageView(imgStill); 
		
		ivRobot.setX(xPos);
		ivRobot.setY(yPos);

	}
	
	public void setImage(int condition)
	{
		if (condition == DEFAULT)
		{
			ivRobot.setImage(imgStill);
		}
		else if(condition == PLAY)
		{
			ivRobot.setImage(imgDance);
		}
		else if(condition == MISS)
		{
			ivRobot.setImage(imgMiss);
		}
	}
	
	public ImageView getImage()
	{
		return ivRobot; 
	}
	
	public void setPos(int x, int y)
	{
		xPos = x;
		yPos = y; 
		
		ivRobot.setX(xPos);
		ivRobot.setY(yPos);
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height; 
	}
	
	
	
}
