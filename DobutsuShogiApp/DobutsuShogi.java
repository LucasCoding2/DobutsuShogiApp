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
	 
   /** Board, this vairable holds an object that manages the board**/ 
   static Board board = new Board();
      
   /** clickedPiece, if a piece was clicked**/
   boolean clickedPiece = false;
     
   /** the stack pane **/
   static StackPane stackPane;
 
   static Text text;
   
   /** The reticle to allow us to see which pieces we last clicked **/
   private static ImageView reticle;
   
   /** options to choose 1 v 1 play or against AI at a the specified depth **/
   static String[] options = { "1 v 1" ,"CPU 2","CPU 3", "CPU 4", "CPU 5", "CPU 6",
                     "CPU 7", "CPU 8", "CPU 9", "CPU 10", "CPU 11", "CPU 12"};
                     
   /** Button to start the game **/                
   static Button startButton; 
   
   /** Button to clear board **/
   static Button clearButton;
   
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
   	
      stackPane = new StackPane(canvas);
   
      Text text = new Text();
      text.setText("Player1 wins"); 
      text.setFont(new Font(40));
      //setting the position of the text 
      text.setX(50); 
      text.setY(50); 
      text.setVisible(false);
      board.setText(text);
      

      javafx.scene.image.Image i = new javafx.scene.image.Image(new FileInputStream("Background.png"));
      ImageView iv = new ImageView(i);
      iv.setOnMouseClicked(e ->  clearSelection());
        
      comboBox = new ComboBox();
      comboBox.setItems(FXCollections
                                 .observableArrayList(options) );
        
      startButton = new Button("Start Game");
                  
      startButton.setOnAction(
         new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               setUpBoard();
               stackPane.getChildren().add(text);
               String option = (String) comboBox.getValue();
               if(option == null) {
                  option = "1 v 1";
               }
               switch (option) {
                  case "1 v 1":  
                     board.setAIDepth(0);
                     break;
                  case "CPU 1":  
                     board.setAIDepth(1);
                     break;
                  case "CPU 2":  
                     board.setAIDepth(2);
                     break;
                  case "CPU 3":  
                     board.setAIDepth(3);
                     break;
                  case "CPU 4":  
                     board.setAIDepth(4);
                     break;
                  case "CPU 5":  
                     board.setAIDepth(5);
                     break;
                  case "CPU 6":  
                     board.setAIDepth(6);
                     break;
                  case "CPU 7":  
                     board.setAIDepth(7);
                     break;
                  case "CPU 8":  
                     board.setAIDepth(8);
                     break;
                  case "CPU 9":  
                     board.setAIDepth(9);
                     break;
                  case "CPU 10":  
                     board.setAIDepth(10);
                     break;
                  case "CPU 11":  
                     board.setAIDepth(11);
                     break;
                  case "CPU 12":  
                     board.setAIDepth(12);
                     break;
                  default: 
                     board.setAIDepth(0);
                     break;
               }
               board.setGameStarted();
               setUpReticle();
               stackPane.getChildren().add(reticle);
               reticle.setVisible(false);
               reticle.setDisable(true);
               startButton.setVisible(false);
               comboBox.setVisible(false);
               clearButton.setVisible(true);
            }
         });
        
      clearButton = new Button("Clear");
      clearButton.setVisible(false);
      clearButton.setOnAction(
         new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                board.clearBoard(stackPane);
                text.setVisible(false);
                startButton.setVisible(true);
                comboBox.setVisible(true);
                clearButton.setVisible(false);
                stackPane.getChildren().remove(text);
            }
         });

        
      stackPane.getChildren().add(iv);
      stackPane.getChildren().add(startButton);
      stackPane.getChildren().add(clearButton);
      stackPane.getChildren().add(comboBox);
      
      startButton.setTranslateX(-200);
      startButton.setTranslateY(300);
      
      clearButton.setTranslateX(200);
      clearButton.setTranslateY(300);
      
      comboBox.setTranslateX(-200);
      comboBox.setTranslateY(-100);
      iv.setOnMouseClicked(e ->  clicked(e));
        
      // create a scene  
      Scene scene = new Scene(stackPane, width, height);
      stage.setScene(scene);
      stage.show();
      tl.play();
   }
   /**
    * clicked what to do when the canvas is clicked
    * @param e - the Mouse Event
    * @return 
    */
   private boolean clicked(MouseEvent e) {
      if(board.gameStarted()) {
         reticle.setVisible(false); //clear selection
         board.clearSelection();
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
   
   public static void setUpReticle() {
       try {
         //Set up Reticle 
         String imageName = "Reticle.png";
         javafx.scene.image.Image reticleImage = new javafx.scene.image.Image(new FileInputStream(imageName));
         reticle = new ImageView(reticleImage);
         board.setReticle(reticle);
       }
       catch(Exception e) {
           System.out.println(e);
       }
   }
   
   
   /**
    * setUpBoard - set up the images of the pieces and their data
    */
   public static void setUpBoard() {
      try {
         //Set up Board Pieces  
         String pieceName = "EleUp";
         String imageName = "EleUp.png";
         int location = 9;
         javafx.scene.image.Image image = new javafx.scene.image.Image(new FileInputStream(imageName));
         ImageView ElephantUpImageView = new ImageView(image);
         int xPosition = -90;
         int yPosition = 115;
         Piece piece = new Piece(pieceName,location,ElephantUpImageView,xPosition,yPosition);     
         
         board.setBoardPiece(piece);
         
         pieceName = "LionUp";
         imageName = "LionUp.png";
         location = 10;
         image = new javafx.scene.image.Image(new FileInputStream(imageName));
         ImageView pieceImageView = new ImageView(image);
         xPosition = 5;
         yPosition = 115;
         piece = new Piece(pieceName,location,pieceImageView,xPosition,yPosition);
         board.setBoardPiece(piece);
         
         pieceName = "ChickUp";
         imageName = "ChickUp.png";
         location = 7; 
         image = new javafx.scene.image.Image(new FileInputStream(imageName));
         pieceImageView = new ImageView(image);
         xPosition = 5;
         yPosition = 25;
         piece = new Piece(pieceName,location,pieceImageView,xPosition,yPosition);
         
         board.setBoardPiece(piece);
         
         pieceName = "GUp";
         imageName = "GUp.png";
         location = 11; 
         image = new javafx.scene.image.Image(new FileInputStream(imageName));
         pieceImageView = new ImageView(image);
         xPosition = 100;
         yPosition = 115;
         piece = new Piece(pieceName,location,pieceImageView,xPosition,yPosition);
         
         board.setBoardPiece(piece);
         
         pieceName = "ChickDown";
         imageName = "ChickDown.png";
         location = 4; 
         image = new javafx.scene.image.Image(new FileInputStream(imageName));
         pieceImageView = new ImageView(image);
         xPosition = 5;
         yPosition = -70;
         piece = new Piece(pieceName,location,pieceImageView,xPosition,yPosition);
                 
         board.setBoardPiece(piece);
                 
         pieceName = "GDown";
         imageName = "GDown.png";
         location = 0; 
         image = new javafx.scene.image.Image(new FileInputStream(imageName));
         pieceImageView = new ImageView(image);
         xPosition = -90;
         yPosition = -160;
         piece = new Piece(pieceName,location,pieceImageView,xPosition,yPosition);
         
         board.setBoardPiece(piece);
       
         pieceName = "LionDown";
         imageName = "LionDown.png";
         location = 1; 
         image = new javafx.scene.image.Image(new FileInputStream(imageName));
         pieceImageView = new ImageView(image);
         xPosition = 5;
         yPosition = -160;
         piece = new Piece(pieceName,location,pieceImageView,xPosition,yPosition);
         
         board.setBoardPiece(piece);
       
         pieceName = "EleDown";
         imageName = "EleDown.png";
         location = 2; 
         image = new javafx.scene.image.Image(new FileInputStream(imageName));
         pieceImageView = new ImageView(image);
         xPosition = 100;
         yPosition = -160;
         piece = new Piece(pieceName,location,pieceImageView,xPosition,yPosition);
       
         board.setBoardPiece(piece);
 
         pieceName = "Empty8";
         imageName = "Empty.png";
         location = 8; 
         image = new javafx.scene.image.Image(new FileInputStream(imageName));
         pieceImageView = new ImageView(image);
         xPosition = 100;
         yPosition = 25;
         piece = new Piece(pieceName,location,pieceImageView,xPosition,yPosition);
             
         board.setBoardPiece(piece);
 
         pieceName = "Empty6";
         location = 6; 
         image = new javafx.scene.image.Image(new FileInputStream(imageName));
         pieceImageView = new ImageView(image);
         xPosition = -90;
         yPosition = 20;
         piece = new Piece(pieceName,location,pieceImageView,xPosition,yPosition);
             
         board.setBoardPiece(piece);

         pieceName = "Empty8";
         location = 8; 
         image = new javafx.scene.image.Image(new FileInputStream(imageName));
         pieceImageView = new ImageView(image);
         xPosition = 100;
         yPosition = 25;
         piece = new Piece(pieceName,location,pieceImageView,xPosition,yPosition);   
         
         board.setBoardPiece(piece);
       
         pieceName = "Empty3";
         location = 3; 
         image = new javafx.scene.image.Image(new FileInputStream(imageName));
         pieceImageView = new ImageView(image);
         xPosition = -90;
         yPosition = -70;
         piece = new Piece(pieceName,location,pieceImageView,xPosition,yPosition);
     
         board.setBoardPiece(piece);
        
         pieceName = "Empty5";
         location = 5; 
         image = new javafx.scene.image.Image(new FileInputStream(imageName));
         pieceImageView = new ImageView(image);
         xPosition = 100;
         yPosition = -70;
         piece = new Piece(pieceName,location,pieceImageView,xPosition,yPosition);
         
         board.setBoardPiece(piece);             
               
         //Set up Bench1 Pieces       
         pieceName = "EmptyB1_0";
         location = 0; 
         image = new javafx.scene.image.Image(new FileInputStream(imageName));
         pieceImageView = new ImageView(image);
         xPosition = -187;
         yPosition = 255;
         piece = new Piece(pieceName,location,pieceImageView,xPosition,yPosition);
     
         board.setBench1Piece(piece);
         
         pieceName = "EmptyB1_1";
         location = 1; 
         image = new javafx.scene.image.Image(new FileInputStream(imageName));
         pieceImageView = new ImageView(image);
         xPosition = -90;
         yPosition = 255;
         piece = new Piece(pieceName,location,pieceImageView,xPosition,yPosition);
     
         board.setBench1Piece(piece);
       
         pieceName = "EmptyB1_2";
         location = 2; 
         image = new javafx.scene.image.Image(new FileInputStream(imageName));
         pieceImageView = new ImageView(image);
         xPosition = 7;
         yPosition = 255;
         piece = new Piece(pieceName,location,pieceImageView,xPosition,yPosition);
     
         board.setBench1Piece(piece);
       
         pieceName = "EmptyB1_3";
         location = 3; 
         image = new javafx.scene.image.Image(new FileInputStream(imageName));
         pieceImageView = new ImageView(image);
         xPosition = 102;
         yPosition = 255;
         piece = new Piece(pieceName,location,pieceImageView,xPosition,yPosition);
     
         board.setBench1Piece(piece);
                
         pieceName = "EmptyB1_4";
         location = 4; 
         image = new javafx.scene.image.Image(new FileInputStream(imageName));
         pieceImageView = new ImageView(image);
         xPosition = 200;
         yPosition = 255;
         piece = new Piece(pieceName,location,pieceImageView,xPosition,yPosition);
     
         board.setBench1Piece(piece);
     
         pieceName = "EmptyB1_5";
         location = 5; 
         image = new javafx.scene.image.Image(new FileInputStream(imageName));
         pieceImageView = new ImageView(image);
         xPosition = 202;
         yPosition = 115;
         piece = new Piece(pieceName,location,pieceImageView,xPosition,yPosition);
                
         board.setBench1Piece(piece);
          
         //Set up Bench2 Pieces 
         pieceName = "EmptyB2_5";
         location = 5; 
         image = new javafx.scene.image.Image(new FileInputStream(imageName));
         pieceImageView = new ImageView(image);
         xPosition = -195;
         yPosition = -160;
         piece = new Piece(pieceName,location,pieceImageView,xPosition,yPosition);
                
         board.setBench2Piece(piece);
         
         pieceName = "EmptyB2_4";
         location = 4; 
         image = new javafx.scene.image.Image(new FileInputStream(imageName));
         pieceImageView = new ImageView(image);
         xPosition = 195;
         yPosition = -300;
         piece = new Piece(pieceName,location,pieceImageView,xPosition,yPosition);
                
         board.setBench2Piece(piece);
         
         pieceName = "EmptyB2_3";
         location = 3; 
         image = new javafx.scene.image.Image(new FileInputStream(imageName));
         pieceImageView = new ImageView(image);
         xPosition = 100;
         yPosition = -300;
         piece = new Piece(pieceName,location,pieceImageView,xPosition,yPosition);
                
         board.setBench2Piece(piece);
          
         pieceName = "EmptyB2_2";
         location = 2; 
         image = new javafx.scene.image.Image(new FileInputStream(imageName));
         pieceImageView = new ImageView(image);
         xPosition = 4;
         yPosition = -300;
         piece = new Piece(pieceName,location,pieceImageView,xPosition,yPosition);
                
         board.setBench2Piece(piece);
                        
         pieceName = "EmptyB2_1";
         location = 1; 
         image = new javafx.scene.image.Image(new FileInputStream(imageName));
         pieceImageView = new ImageView(image);
         xPosition = -95;
         yPosition = -300;
         piece = new Piece(pieceName,location,pieceImageView,xPosition,yPosition);
                
         board.setBench2Piece(piece);
               
         pieceName = "EmptyB2_0";
         location = 0; 
         image = new javafx.scene.image.Image(new FileInputStream(imageName));
         pieceImageView = new ImageView(image);
         xPosition = -190;
         yPosition = -300;
         piece = new Piece(pieceName,location,pieceImageView,xPosition,yPosition);
                
         board.setBench2Piece(piece);   
                  
         //Set up Mouse Events and children
         board.setUpPieces(stackPane);
      }
      catch(Exception e) {
         System.out.println(e);
      }
   }
}
