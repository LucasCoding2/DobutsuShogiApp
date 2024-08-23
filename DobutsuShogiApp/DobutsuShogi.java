import java.awt.*;    
import javax.swing.*;   
import java.io.*;
import javafx.collections.*;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.event.EventHandler; 
import javafx.scene.input.MouseEvent; 
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;

import java.util.HashMap;
import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;

/**
 * DobutsuShogi this class sets up the canvas and game board for play
 */
public class DobutsuShogi extends Application {
   private static final int width = 500;
   private static final int height = 700;
	 
   /** gameState, this vairable holds an object that manages the game state**/ 
   static GameState gameState = new GameState();
      
   /** clickedPiece, if a piece was clicked**/
   boolean clickedPiece = false;
     
   /** the stack pane **/
   static StackPane r;
 
   static Text text;
   
   /** The reticle to allow us to see which pieces we last clicked **/
   private static ImageView reticle;
   
   /** options to choose 1 v 1 play or against AI at a the specified depth **/
   static String[] options = { "1 v 1" ,"CPU 2","CPU 3", "CPU 4", "CPU 5", "CPU 6",
                     "CPU 7", "CPU 8"};
                     
   /** Button to start the game **/                
   static Button b; 
   
   /** drop down menu for the options **/
   static ComboBox comboBox;
   
   /** The main method **/
   public static void main(String args[]) {
      launch(args);
   }   
   
   /** start, sets up the canvas
    *  @param stage
    **/  
   public void start(Stage stage) throws Exception {
      stage.setTitle("Dobutsu Shogi");
   	
      Canvas canvas = new Canvas(width, height);
      GraphicsContext gc = canvas.getGraphicsContext2D();
   	
     
      canvas.setOnMouseClicked(e ->  clickedPiece  = clicked(e));
      Timeline tl = new Timeline(new KeyFrame(Duration.millis(10), e -> run(gc)));
      tl.setCycleCount(Timeline.INDEFINITE);
   	
      r = new StackPane(canvas);
   
      Text text = new Text();
      text.setText("Player1 wins"); 
      text.setFont(new Font(40));
      //setting the position of the text 
      text.setX(50); 
      text.setY(50); 
      text.setVisible(false);
      gameState.setText(text);
      
      javafx.scene.image.Image i = new javafx.scene.image.Image(new FileInputStream("Background.png"));
      ImageView iv = new ImageView(i);
      iv.setOnMouseClicked(e ->  clearSelection());
        
      comboBox = new ComboBox();
      comboBox.setItems(FXCollections
                                 .observableArrayList(options) );
        
      b = new Button("Start Game");
                  
      b.setOnAction(
         new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               setUpBoardState();
               r.getChildren().add(text);
               String option = (String) comboBox.getValue();
               if(option == null) {
                  option = "1 v 1";
               }
               switch (option) {
                  case "1 v 1":  
                     gameState.setAIDepth(0);
                     break;
                  case "CPU 1":  
                     gameState.setAIDepth(1);
                     break;
                  case "CPU 2":  
                     gameState.setAIDepth(2);
                     break;
                  case "CPU 3":  
                     gameState.setAIDepth(3);
                     break;
                  case "CPU 4":  
                     gameState.setAIDepth(4);
                     break;
                  case "CPU 5":  
                     gameState.setAIDepth(5);
                     break;
                  case "CPU 6":  
                     gameState.setAIDepth(6);
                     break;
                  case "CPU 7":  
                     gameState.setAIDepth(7);
                     break;
                  case "CPU 8":  
                     gameState.setAIDepth(8);
                     break;
                  default: 
                     gameState.setAIDepth(0);
                     break;
               }
               gameState.setGameStarted();
               r.getChildren().add(reticle);
               reticle.setVisible(false);
               reticle.setDisable(true);
               b.setVisible(false);
               comboBox.setVisible(false);
            }
         });
        
      r.getChildren().add(iv);
      r.getChildren().add(b);
      r.getChildren().add(comboBox);
      
      b.setTranslateX(-200);
      b.setTranslateY(300);
      comboBox.setTranslateX(-200);
      comboBox.setTranslateY(-100);
      iv.setOnMouseClicked(e ->  clicked(e));
        
      // create a scene  
      Scene sc = new Scene(r, width, height);
      stage.setScene(sc);
      stage.show();
      tl.play();
   }
   /**
    * clicked what to do when the canvas is clicked
    * @param e - the Mouse Event
    * @return 
    */
   private boolean clicked(MouseEvent e) {
      if(gameState.gameStarted()) {
         reticle.setVisible(false); //clear selection
         gameState.clearSelection();
      }       
      return true;
   }
   
   /**
    * run - set fill an rect
    * @param gc the Graphics Context
    */
   private void run(GraphicsContext gc) {
      gc.setFill(Color.BLACK);
      gc.fillRect(0, 0, width, height);
   }
   
   /**
    * clearSelection set the reticle to invisible
    */
   static void clearSelection() { 
      System.out.println("ClearSelection");   
      reticle.setVisible(false);
   }
   
   /**
    * setUpBoardState - set up the images of the pieces and their data
    */
   public static void setUpBoardState() {
      try {
         //Set up Board Pieces       
         javafx.scene.image.Image i = new javafx.scene.image.Image(new FileInputStream("EleUp.png" ));
         gameState.setBoardPiece(new Piece("EleUp",9,new ImageView(i),-90,115),9);
          
         i = new javafx.scene.image.Image(new FileInputStream("LionUp.png"));
         gameState.setBoardPiece(new Piece("LionUp",10,new ImageView(i),5,115),10);
       
         i = new javafx.scene.image.Image(new FileInputStream("Gup.png"));
         gameState.setBoardPiece(new Piece("GUp",11,new ImageView(i),100,115),11);
       
         i = new javafx.scene.image.Image(new FileInputStream("Empty.png"));
         gameState.setBoardPiece(new Piece("Empty6",6,new ImageView(i),-90,20),6);
       
         i = new javafx.scene.image.Image(new FileInputStream("ChickUp.png"));
         gameState.setBoardPiece(new Piece("ChickUp",7,new ImageView(i),5,25),7);
       
         i = new javafx.scene.image.Image(new FileInputStream("Empty.png"));
         gameState.setBoardPiece(new Piece("Empty8",8,new ImageView(i),100,25),8);
       
         i = new javafx.scene.image.Image(new FileInputStream("Empty.png"));
         gameState.setBoardPiece(new Piece("Empty3",3,new ImageView(i),-90,-70),3);
       
         i = new javafx.scene.image.Image(new FileInputStream("ChickDown.png"));
         gameState.setBoardPiece(new Piece("ChickDown",4,new ImageView(i),5,-70),4);
       
         i = new javafx.scene.image.Image(new FileInputStream("Empty.png"));
         gameState.setBoardPiece(new Piece("Empty5",5,new ImageView(i),100,-70),5);
       
         i = new javafx.scene.image.Image(new FileInputStream("GDown.png"));
         gameState.setBoardPiece(new Piece("GDown",0,new ImageView(i),-90,-160),0);
       
         i = new javafx.scene.image.Image(new FileInputStream("LionDown.png"));
         gameState.setBoardPiece(new Piece("LionDown",1, new ImageView(i),5,-160),1);
       
         i = new javafx.scene.image.Image(new FileInputStream("EleDown.png"));
         gameState.setBoardPiece(new Piece("EleDown",2,new ImageView(i),100,-160),2);
       
         //Set up Reticle 
         i = new javafx.scene.image.Image(new FileInputStream("Reticle.png"));
         reticle = new ImageView(i);
         gameState.setReticle(reticle);
       
         //Set up Bench1 Pieces       
         i = new javafx.scene.image.Image(new FileInputStream("Empty.png"));
         gameState.setBench1Piece(new Piece("EmptyB0",0,new ImageView(i),-187,255),0);
       
         i = new javafx.scene.image.Image(new FileInputStream("Empty.png"));
         gameState.setBench1Piece(new Piece("EmptyB1",1,new ImageView(i),-90,255),1);
       
         i = new javafx.scene.image.Image(new FileInputStream("Empty.png"));
         gameState.setBench1Piece(new Piece("EmptyB2",2,new ImageView(i),7,255),2);
       
         i = new javafx.scene.image.Image(new FileInputStream("Empty.png"));
         gameState.setBench1Piece(new Piece("EmptyB3",3,new ImageView(i),102,255),3);
       
         i = new javafx.scene.image.Image(new FileInputStream("Empty.png"));
         gameState.setBench1Piece(new Piece("EmptyB3",4,new ImageView(i),200,255),4);
       
         i = new javafx.scene.image.Image(new FileInputStream("Empty.png"));
         gameState.setBench1Piece(new Piece("EmptyB4",5,new ImageView(i),202,115),5);
       
         //Set up Bench2 Pieces 
         i = new javafx.scene.image.Image(new FileInputStream("Empty.png"));
         gameState.setBench2Piece(new Piece("EmptyB5",4,new ImageView(i),195,-300),4);
         
         i = new javafx.scene.image.Image(new FileInputStream("Empty.png"));
         gameState.setBench2Piece(new Piece("EmptyB6",3,new ImageView(i),100,-300),3);
      
         i = new javafx.scene.image.Image(new FileInputStream("Empty.png"));
         gameState.setBench2Piece(new Piece("EmptyB7",2,new ImageView(i),4,-300),2);
       
         i = new javafx.scene.image.Image(new FileInputStream("Empty.png"));
         gameState.setBench2Piece(new Piece("EmptyB8",1,new ImageView(i),-95,-300),1);
      
         i = new javafx.scene.image.Image(new FileInputStream("Empty.png"));
         gameState.setBench2Piece(new Piece("EmptyB9",0,new ImageView(i),-190,-300),0);     
       
         i = new javafx.scene.image.Image(new FileInputStream("Empty.png"));
         gameState.setBench2Piece(new Piece("EmptyB10",5,new ImageView(i),-195,-160),5);     
                   
         //Set up Mouse Events and children
         gameState.setUpPieces(r);
      }
      catch(Exception e) {
         System.out.println(e);
      }
   }
}
