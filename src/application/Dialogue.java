package application;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class Dialogue 
{

	private String[][] strDialogue;
	private Rectangle rectDialogue; 
	private Label lblText; 
	private int xPos, yPos, lineCounter, currentTutorial; 
	
	
	
	final int TUTORIALPLAY = 1, TUTORIALEDIT = 2, TUTORIALNONE = 0;
	
	public Dialogue(Font f)
	{
		xPos = 625; 
		yPos = 75; 
		
		lineCounter = 0; 
		currentTutorial = TUTORIALPLAY;
		
		strDialogue = new String[1][8];
		strDialogue[0][0] = "Hello!  Welcome  to  QUATROBE4T!\nPress  SPACE  to  continue";
		strDialogue[0][1] = "The  game  is  simple";
		strDialogue[0][2] = "Just  press  the  right  key  when\nthe  circle  passes  through";
		strDialogue[0][3] = "Each  lane  is  assigned  to  a  specific key"; 
		strDialogue[0][4] = "From  left  to  right\nA  S  K  L"; 
		strDialogue[0][5] = "You  can  press  multiple  keys\nsimultaneously  if  required";
		strDialogue[0][6] = "Try  playing  this  song  on  your own";
		strDialogue[0][7] = "Good  luck!";

		lblText = new Label();
		lblText.setText(strDialogue[0][lineCounter]);
		lblText.setFont(f);
		lblText.setLayoutX(xPos);
		lblText.setLayoutY(yPos);
		lblText.setTextAlignment(TextAlignment.CENTER);
		lblText.setAlignment(Pos.CENTER);
		lblText.setPrefWidth(500);
		lblText.setPrefHeight(150);


		//x, y, width, height
		rectDialogue = new Rectangle(625, 75, 500, 150); 
		rectDialogue.setFill(Color.WHITE);
		rectDialogue.setArcHeight(10);
		rectDialogue.setArcWidth(10);
		rectDialogue.setStrokeWidth(10);
		rectDialogue.setStroke(Color.BLACK);

		


		
	}
	
	public void setLine()
	{
		if(currentTutorial == TUTORIALPLAY && lineCounter < 7)
		{
			lineCounter++;
			lblText.setText(strDialogue[0][lineCounter]);
		}
		else if (currentTutorial == TUTORIALPLAY)
		{
			
			currentTutorial = TUTORIALNONE; 
			
		}
		
	}
	public int getLine()
	{
		return lineCounter; 
	}
	
	public Label returnLine()
	{
		return lblText; 
	}
	
	
	
	public Rectangle getBox()
	{
		return rectDialogue; 
	}
	
	public int getStatus()
	{
		if(currentTutorial == TUTORIALNONE)
		{
			lblText.setText("");
		}
		return currentTutorial; 
	}
	
	
}
