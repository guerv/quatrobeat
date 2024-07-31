package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Outcomes 
{
	private Image iPerf, iGood, iMiss; 
	private ImageView ivPerf, ivGood, ivMiss, iv; 
	
	private int xPos, yPos, seconds; 
	
	private Timeline outcomeTimer;
	
	private boolean isNull;
	
	final int PERFECT = 1, GOOD = 2, MISS = 3;

	
	public Outcomes(Rectangle lane, int lanePlaySelect)
	{
		iPerf = new Image("file:images/1.png");
		iGood = new Image("file:images/2.png");
		iMiss = new Image("file:images/3.png");
		
		ivPerf = new ImageView(iPerf);
		ivGood = new ImageView(iGood); 
		ivMiss = new ImageView(iMiss);
		
		ivPerf.setPreserveRatio(true);
		ivPerf.setFitWidth(100);
		ivGood.setPreserveRatio(true);
		ivGood.setFitWidth(100);
		ivMiss.setPreserveRatio(true);
		ivMiss.setFitWidth(100);
		
		iv = new ImageView(); 
		
		yPos = 650; 
		
		xPos = (int)lane.getX() + (100*(lanePlaySelect-1));
		//System.out.println(lane.getX() + " " + lanePlaySelect + " " + xPos);
		
		seconds = 0; 
		
		isNull = false;
		
	}
	
	
	public void setImage(int hitOutcome)
	{
		
		if(hitOutcome == PERFECT)
		{
			iv = ivPerf;
		}
		else if (hitOutcome == GOOD)
		{
			iv = ivGood; 
		}
		else 
		{
			iv = ivMiss; 
		}
		
		iv.setX(xPos);
		iv.setY(yPos);
	}
	
	public ImageView getImage()
	{
		
		return iv; 
		
	}
	
	public void startTimer()
	{
		KeyFrame kfShowOutcome = new KeyFrame(Duration.millis(100), new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent e)
			{
				if(seconds == 1)
				{
					isNull = true; 
					
					outcomeTimer.stop(); 
					
				}
				
				seconds++;

			}
		});
		outcomeTimer = new Timeline(kfShowOutcome);
		outcomeTimer.setCycleCount(2);
		outcomeTimer.play(); 
		
		
	}
	
	public boolean getNull()
	{
		return isNull; 
	}

}
