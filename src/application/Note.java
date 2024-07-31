package application;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Note 
{

	private int xPos, yPos, radius, lane; 
	private int song; 
	private Circle note; 
	private boolean isNull; 
	private Image imgNote, imgNull;
	private ImageView ivNote, ivNoteSIZE, ivNullSIZE; 

	public Note(int lane)
	{
		this.lane = lane; 

		radius = 40; 

		xPos = 100*lane;
		yPos = 0;

		song = 0;

		note = new Circle(xPos, yPos, radius);
		note.setFill(Color.TRANSPARENT);

		isNull = false; 
		
		imgNote = new Image("file:images/notes/oonote.png");
		imgNull = new Image("file:images/notes/oonoteN.png");
		
		ivNoteSIZE = new ImageView(imgNote); 
		ivNoteSIZE.setPreserveRatio(true); 
		ivNoteSIZE.setFitHeight(80); 
		
		ivNullSIZE = new ImageView(imgNull);
		ivNullSIZE.setPreserveRatio(true); 
		ivNullSIZE.setFitHeight(80); 
		
		ivNote = ivNoteSIZE; 
		ivNote.setX(xPos - 40);


	}

	public Note(int lane, int y)
	{
		this.lane = lane; 

		radius = 40; 

		xPos = 100*lane;
		yPos = y;

		song = 0;

		note = new Circle(xPos, yPos, radius);
		note.setFill(Color.TRANSPARENT);
		
		imgNote = new Image("file:images/notes/oonote.png");
		
		ivNoteSIZE = new ImageView(imgNote); 
		ivNoteSIZE.setPreserveRatio(true); 
		ivNoteSIZE.setFitHeight(80); 
		
		
		ivNote = ivNoteSIZE; 
		ivNote.setX(xPos - 40);

	}

	public void setLane(int lane)
	{		
		xPos = 100*lane; 
		note.setCenterX(xPos);
		
		ivNote.setX(xPos - 40);
	}

	public void setSong(int song)
	{
		this.song = song; 
	}

	public int getSong()
	{
		return song; 
	}

	public Circle getNote()
	{
		return note;
	}

	public ImageView getIV()
	{
		return ivNote; 
	}

	public int getY()
	{
		return yPos; 
	}

	public void setY(int y)
	{
		yPos = y; 
		note.setCenterY(yPos);
		
		ivNote.setY(yPos - (radius));
	}

	public int getLane()
	{
		return lane; 
	}

	public void setNull(boolean isNull)
	{
		this.isNull = isNull;

		if(this.isNull)
		{
			//note.setFill(Color.NAVY);
			ivNote.setOpacity(0.5);

		}
		else 
		{
			//note.setFill(Color.DARKGOLDENROD);
			ivNote.setOpacity(1.0);


		}
		
	}
	public boolean getNullStatus()
	{
		return isNull; 
	}
	
}
