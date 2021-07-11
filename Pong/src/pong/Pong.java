package pong;

import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.DODGERBLUE;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Pong extends Application {
	
	//Encapsulation of various variables
	private static final int width = 800;
	private static final int height = 600;
	private static final int PLAYER_HEIGHT = 100;
	private static final int PLAYER_WIDTH = 15;
	private static final double BALL_R = 20;
	private int ballYSpeed = 1;
	private int ballXSpeed = 1;
	private double playerOneYPos = height / 2;
	private double playerTwoYPos = height / 2;
	private double ballXPos = width / 2;
	private double ballYPos = height / 2;
	private int scoreP1 = 0;
	private int scoreP2 = 0;
	private boolean gameStarted;
	private int playerOneXPos = 0;
	private double playerTwoXPos = width - PLAYER_WIDTH;
	
        //the Start function of the game
	public void start(Stage stage) throws Exception {
		stage.setTitle("PongFx by Davis");
		//creating a new background canvas
		Canvas canvas = new Canvas(width, height);
                //Abstract operation, creating a graphic object to draw the game
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		//Creating a JavaFX Timeline = free form animation defined by KeyFrames and their duration 
                //Duration of a keyframe defines the animation
		Timeline tl = new Timeline(new KeyFrame(Duration.millis(10), e -> run(gc)));
		//number of cycles in animation INDEFINITE = repeat indefinitely
		tl.setCycleCount(Timeline.INDEFINITE);
		
		//mouse control (move and click)
		canvas.setOnMouseMoved(e ->  playerOneYPos  = e.getY());
		canvas.setOnMouseClicked(e ->  gameStarted = true);
                stage.setScene(new Scene(new StackPane(canvas)));
		stage.show();
		tl.play();
	}
        //The run function of the game
	private void run(GraphicsContext gc) {
		//Intialization of the graphic objects using the actors, hence abstraction
		//polymorphism, gc.setFill method is used to fill bg as gradient color
		gc.setFill(new RadialGradient(0,0,0.5,0.8,1.0,true,CycleMethod.REFLECT,
                new Stop(0.0, DODGERBLUE),
                new Stop(2.0, BLACK)));
              
                //polymorphism, where gc.fillreact method is used to set the bg graphics
		gc.fillRect(0, 0, width, height);
		
		////polymorphism, gc.setFill method is used to fill text with color
		gc.setFill(Color.WHITE);
		gc.setFont(Font.font(50));
		
                //The game logic
		if(gameStarted) {
			//set ball movement
			ballXPos+=ballXSpeed;
			ballYPos+=ballYSpeed;
			
			//simple computer opponent who is following the ball
			if(ballXPos < width - width  / 4) {
				playerTwoYPos = ballYPos - PLAYER_HEIGHT/2;
			}  else {//Opponennt can only follow the ball
				playerTwoYPos =  ballYPos > playerTwoYPos + PLAYER_HEIGHT / 2 ?playerTwoYPos += 1: playerTwoYPos - 1;
			}
			//draw the ball
			gc.fillOval(ballXPos, ballYPos, BALL_R, BALL_R);
			
		} else {
			//set the start text
			gc.setStroke(Color.BLACK);
			gc.setTextAlign(TextAlignment.CENTER);
			gc.strokeText(" P L A Y \n", width / 2, height / 2);
                        gc.fillText(" P L A Y \n", width / 2, height / 2);
			//reset the ball start position 
			ballXPos = width / 2;
			ballYPos = height / 2;
			
			//reset the ball speed and the direction
			ballXSpeed = new Random().nextInt(2) == 0 ? 1: -1;
			ballYSpeed = new Random().nextInt(2) == 0 ? 1: -1;
		}
		
		//makes sure the ball stays in the canvas
		if(ballYPos > height || ballYPos < 0) ballYSpeed *=-1;
		
		//if you miss the ball, computer gets a point
		if(ballXPos < playerOneXPos - PLAYER_WIDTH) {
			scoreP2++;
			gameStarted = false;
		}
		
		//if the computer misses the ball, you get a point
		if(ballXPos > playerTwoXPos + PLAYER_WIDTH) {  
			scoreP1++;
			gameStarted = false;
		}
	
		//increase the speed after the ball hits the player
		if( ((ballXPos + BALL_R > playerTwoXPos) && ballYPos >= playerTwoYPos && ballYPos <= playerTwoYPos + PLAYER_HEIGHT) || 
			((ballXPos < playerOneXPos + PLAYER_WIDTH) && ballYPos >= playerOneYPos && ballYPos <= playerOneYPos + PLAYER_HEIGHT)) {
			ballYSpeed += 1 * Math.signum(ballYSpeed);
			ballXSpeed += 1 * Math.signum(ballXSpeed);
			ballXSpeed *= -1;
			ballYSpeed *= -1;
		}
		
		//draw score
		gc.fillText(scoreP1 + "\t\t\t\t\t" + scoreP2, width / 2, 100);
		//draw player 1 & 2
                //polymorphism, where gc.fillreact method is used to draw the player 2
		gc.fillRect(playerTwoXPos, playerTwoYPos, PLAYER_WIDTH, PLAYER_HEIGHT);
                //polymorphism, where gc.fillreact method is used to draw the player 1
		gc.fillRect(playerOneXPos, playerOneYPos, PLAYER_WIDTH, PLAYER_HEIGHT);
	}
        
		// start the application
		public static void main(String[] args) {
		launch(args);
		}
}