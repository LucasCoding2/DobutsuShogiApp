import javafx.scene.input.MouseEvent; 
import javafx.scene.layout.StackPane;
import javafx.scene.image.*;
import java.io.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.Text;

/**
 * GameState, manages the game state of the game
 */
public class Board {

    /** bench 1, the player's bench **/
    private Piece[] bench1;
    
    /** bench 2, the 2nd players bench or the AI's bench **/
    private Piece[] bench2;
    
    /** board, the main board of the game**/
    private Piece[] board;
    
    /** reticle, the reticle that tells us what piece was clicked last**/
    private ImageView reticleImageView;
    
    /** playerTurn true if player1, false if player2 or the AI's turn**/
    private boolean playerTurn;
    
    /** gameStarted, whether or not the game started, true if it has*/
    private boolean gameStarted;
    
    /** selectedPiece, the last selected piece**/
    private Piece selectedPiece;
    
    /** aIdepth the depth of the DFS tree **/
    private int aIDepth = 0;
    
    /** Images to be utilized **/
    private Image henDown, henUp, chickUp, chickDown, empty;
    
    /** aIPlayer, holds the object that manages the AI player **/
    private AIPlayer aIPlayer;
    
    /** Text to display who the winner is **/
    private Text winnerText;
    
    /**
     * GameState the constructor for setting up a game state
     * sets up variables and initializes the arrays
     */
    public Board() {
        bench1 = new Piece[6];
        bench2 = new Piece[6];
        board = new Piece[12];
        playerTurn = true;
        gameStarted = false;
        
        try { 
            henDown = new javafx.scene.image.Image(new FileInputStream("RoosterDown.png"));
            henUp = new javafx.scene.image.Image(new FileInputStream("RoosterUp.png"));
            chickUp = new javafx.scene.image.Image(new FileInputStream("ChickUp.png"));
            chickDown = new javafx.scene.image.Image(new FileInputStream("ChickDown.png"));
            empty = new javafx.scene.image.Image(new FileInputStream("Empty.png"));
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }
    /**
     * setGameStarted, set true, that the game has started.
     */
    public void setGameStarted() {
        gameStarted = true;
    }
    
    /**
     * gameStarted, return whether or not the game has started
     * @return gameStarted, true if the game has started, false otherwise
     */
    public boolean gameStarted() {
        return gameStarted;
    }
    
    /** Allow us to use text from a different class
     *  Sets the winnerText vairable
     *  @param text, the text to be set
     */
    public void setText(Text text) {
        winnerText = text;
    }
    
    /**
     * clearSelection set selectedPiece to null
     */
    public void clearSelection() {
        selectedPiece = null;
    }
    
    /**
     * setAIDepth - set the depth searched, that the AI will use 
     * depth, the depth that the AI will search
     */
    public void setAIDepth(int depth) {
        this.aIDepth = depth;
        this.aIPlayer = new AIPlayer(depth);
    }
    
    /**
     * setBench1Piece place a piece at a given index into bench1
     * piece, the piece to be stored
     * index, the index that the piece will be stored in
     */
    public void setBench1Piece(Piece piece) {
        bench1[piece.getLocation()] = piece;
        piece.setInBench(true);
    }
    
    /**
     * setBench2Piece place a piece at a given index into bench2
     * piece, the piece to be stored
     * index, the index that the piece will be stored in
     */
    public void setBench2Piece(Piece piece) {
        bench2[piece.getLocation()] = piece;
        piece.setInBench(true);
    }
    
    /**
     * setBoardPiece place a piece at the piece's location into the board
     * piece, the piece to be stored
     */
    public void setBoardPiece(Piece piece) {
        board[piece.getLocation()] = piece;
    }
    
    /**
     * setUpPieces, add the pieces to the canvas and add mouse events
     * r, the StackPane
     */
    public void setUpPieces(StackPane r) {
       for (Piece piece : board) { 
          piece.getIV().setOnMouseClicked(e ->  selectPiece(piece,e));
          r.getChildren().add(piece.getIV());
       }
       for (Piece piece : bench1) { 
          piece.getIV().setOnMouseClicked(e ->  selectPiece(piece,e));
          r.getChildren().add(piece.getIV());
       }
       for (Piece piece : bench2) { 
          piece.getIV().setOnMouseClicked(e ->  selectPiece(piece,e));
          r.getChildren().add(piece.getIV());
       }
       
    }
    
    /**
     * setReticle sets the reticle that is being used
     * @param iv, the Image View of the reticle
     */
    public void setReticle(ImageView iv) {
        this.reticleImageView = iv;
    }
    
    /**
     * selectPiece, determines what to do when a piece is clicked
     * @param piece the piece that was selected
     * @param e the mouse event
     */
    private void selectPiece(Piece piece, MouseEvent e) {
        if(!gameStarted) {// don't do anything if the game has not been started.
            return;
        }
        System.out.println(piece + "A");
        reticleImageView.setTranslateX(piece.getXPos());//move the reticle to the piece
        reticleImageView.setTranslateY(piece.getYPos());
        reticleImageView.setVisible(true);
        
        if(selectedPiece == null) {
            //determine if the piece can be selected as a piece to move
            if(playerTurn && piece.getName().contains("Up")) {
                selectedPiece = piece;
            }
            else if(!playerTurn && piece.getName().contains("Down")) {
                selectedPiece = piece;
            }
        }
        else if((piece.getName().contains("Up") && selectedPiece.getName().contains("Up")) ||
               (piece.getName().contains("Down") && selectedPiece.getName().contains("Down"))){
                selectedPiece = piece; //replace what we consider the last selected piece, do 
                                       //not swap pieces as the previously selected piece
                                       //and the current piece belong to the same player
        }
        else {// if selected PIece is not null, we determine what to do
            if(!selectedPiece.inBench() && selectedPiece.getName().contains("Up") && !piece.getName().contains("Up") &&
            checkLegal(selectedPiece,piece) ) {
                swapPieces(selectedPiece,piece,false);//if we can swap pieces we will
                playerTurn = !playerTurn; //shift the player turn   
                if(aIDepth > 0 && gameStarted) { // have the AI select a move, if player1 just moved
                    Piece[] move = aIPlayer.makeMove(board,bench1,bench2);
                    System.out.println(move[0] + "|" + move[1] + "| " + move[0].inBench());
                    swapPieces(move[0],move[1],move[0].inBench());
                    playerTurn = !playerTurn;//shift the player turn
                }               
                selectedPiece = null;
            }
            else if(!selectedPiece.inBench() && selectedPiece.getName().contains("Down") && !piece.getName().contains("Down") &&
            checkLegal(selectedPiece,piece)) {//if player2 and can move piece
                swapPieces(selectedPiece,piece,false);
                playerTurn = !playerTurn;//shift player turn
                selectedPiece = null;
            }     
            else if(selectedPiece.inBench() && checkLegal(selectedPiece,piece)) {
                swapPieces(selectedPiece,piece,true);
                playerTurn = !playerTurn;     
                if(aIDepth > 0) {
                    Piece[] move = aIPlayer.makeMove(board,bench1,bench2);
                    swapPieces(move[0],move[1],move[0].inBench());
                    System.out.println(move[0] + "|" + move[1] + "| " + move[0].inBench());

                    playerTurn = !playerTurn;
                }
                selectedPiece = null;
            }
        }
    }
    /**
     * checkLegal, check if it's legal to move piece1 into piece2's location
     * we have to make sure that the pieces are swapped based on what the
     * piece is able to do
     * @param piece1 the attacking piece
     * @param piece2 the defending piece
     * return true if the move is legal, false otherwise
     */
    private boolean checkLegal(Piece piece1, Piece piece2) {
        if(piece2.inBench()) {
            return false;
        }
        //get the locations of the pieces
        int location1 = piece1.getLocation();
        int location2 = piece2.getLocation();
        
        if(piece1.getName().contains("Ele") &&  
           ((location1 - 4 == location2 || location1 + 2 == location2 && location1 % 3 != 0) ||
           (location1 - 2 == location2 || location1 + 4 == location2 && location1 % 3 != 2) ) ) {
           return true;    
        }
        else if(piece1.getName().contains("G") && 
           (location1 - 3 == location2 || 
           (location1 - 1 == location2 && location1 % 3 != 0) ||
           (location1 + 1 == location2 && location1 % 3 != 2) ||
           location1 + 3 == location2) ) {
           return true;      
        }
        else if(piece1.getName().equals("ChickUp") && (location1 - 3 == location2) ){
           return true;     
        }
        else if(piece1.getName().equals("ChickDown") && (location1 + 3 == location2) ){
           return true;     
        }
        else if(piece1.getName().contains("Lion") && 
           (location1 - 3 == location2 || 
           location1 + 3 == location2 || 
           (location1 + 2 == location2 || location1 - 1 == location2 || location1 - 4 == location2 && location1 % 3 != 0) || 
           (location1 + 1 == location2 || location1 - 2 == location2 || location1 + 4 == location2 && location1 % 3 != 2) ) ) {
           return true;      
       }  
       else if(piece1.getName().equals("HenUp") && 
           (location1 - 3 == location2 || 
           location1 + 3 == location2 || 
           (location1 + 1 == location2 || location1 - 2 == location2 && location1 % 3 != 2) || 
           (location1 - 1 == location2 || location1 - 4 == location2 && location1 % 3 != 0) ) ) {
           return true;
       }
       else if(piece1.getName().equals("HenDown") && 
           (location1 - 3 == location2 ||
           location1 + 3 == location2 ||
           (location1 + 2 == location2 || location1 - 1 == location2 && location1 % 3 != 0) ||
           (location1 + 1 == location2 || location1 + 4 == location2 && location1 % 3 != 2) ) ) {
           return true;
       }
       else if(piece1.inBench() && piece2.getName().contains("Empty") && !piece2.inBench() ) {
          return true;
       }
       return false;
    }
    
    public void placePieceToBench(Piece piece, int location) {
        if(playerTurn) {
            bench1[location] = piece;
        }
        else {
            bench2[location] = piece;
        }
    }
    
    public static boolean henCheck(Piece piece) {
        if(piece.getName().equals("ChickUp") && piece.getLocation() < 3) {
            return true;                
        }
        if(piece.getName().equals("ChickDown") && piece.getLocation() > 8) {
           return true;
        }
        return false;
    }
    
    public boolean winCheck(Piece piece1, Piece piece2) {
        if(piece1.getName().equals("LionUp") && piece2.getLocation() < 3 ) {
            return true;
        }
        if(piece1.getName().equals("LionDown") && piece2.getLocation() > 8 ) {
            return true;
        }
        if(piece2.getName().contains("Lion")) {
            return true;
        }
        return false;
    }
    
    public void makeHen(Piece piece) {
        if(piece.rotated180() ) {
           piece.getIV().setRotate(0);
           piece.setRotated180(false);
        }
        if(piece.getName().equals("ChickUp") ) {
           piece.setName("HenUp");
           piece.getIV().setImage(henUp);
           return;
        }
        if(piece.getName().equals("ChickDown") ) {
            piece.setName("HenDown");
            piece.getIV().setImage(henDown);
            return;
        }
    }
    
    public void handleWin(Piece piece1, Piece piece2) {
        int piece1XPos,piece1YPos,piece1Location;
        //get the x and y positions, and the location/index of the attacking piece
        piece1XPos = piece1.getXPos();
        piece1YPos = piece1.getYPos();
        piece1Location = piece1.getLocation();
        piece1.setPosition(piece2.getXPos(),piece2.getYPos());
        piece1.setLocation(piece2.getLocation());
           
        piece2.getIV().setImage(empty);
            
        piece2.setPosition(piece1XPos,piece1YPos);
        piece2.setLocation(piece1Location);  
        
        if(piece1.getName().contains("Up") ) {
           winnerText.setText("Player 1 Wins");   
        }
        else {
           winnerText.setText("Player 2 Wins");
        } 
        winnerText.setVisible(true);
        gameStarted = false;
    }
    
    public void revertToChick(Piece piece) {
        if(piece.getName().equals("HenUp") ) {
            piece.setName("ChickDown");//we switch it to down below
            piece.getIV().setImage(chickDown);
        }
        else if(piece.getName().equals("HenDown") ) {
            piece.setName("ChickUp");
            piece.getIV().setImage(chickUp); 
        }
        piece.getIV().setRotate(0);
        piece.setRotated180(false);
    }
    
    /**
     * swapPieces swaps pieces
     * this method also handles what to do for pieces that can upgrade
     * this method also handles what to do when a player wins a game
     * @param piece1, the attacking or benched piece to be swapped
     * @param piece2, the defending on board piece to be swapped
     * @param benchSwap, true if piece1 is being placed from the bench
     * onto the board. false otherwise
     */
    private void swapPieces(Piece piece1, Piece piece2,boolean benchSwap) {
        int x,y,location;
        
        //get the x and y positions, and the location/index of the attacking piece
        x = piece1.getXPos();
        y = piece1.getYPos();
        location = piece1.getLocation();
        
        if(benchSwap) {
           piece1.setPosition(piece2.getXPos(),piece2.getYPos());
           piece1.setLocation(piece2.getLocation());
           piece2.setPosition(x,y);
           piece2.setLocation(location); 
           
           placePieceToBench(piece2,piece2.getLocation());
           board[piece1.getLocation()] = piece1;
           
           piece2.setInBench(true);
           piece1.setInBench(false);
        }
        else {
           if(winCheck(piece1, piece2)) {
               handleWin(piece1, piece2);
               return;
           }
           if(piece2.getName().contains("Hen")) {
               revertToChick(piece2);
           } 
           else {
               float angle = 180;
               boolean rotated = true;
               if(piece2.rotated180() ) {
                   angle = 0;
                   rotated = false;
               }
               piece2.getIV().setRotate(angle);
               piece2.setRotated180(rotated);
               piece2.setName(findName(piece2.getName()));
           }
           piece1.setPosition(piece2.getXPos(),piece2.getYPos());
           piece1.setLocation(piece2.getLocation());
           board[piece1.getLocation()] = piece1;
           
           if(henCheck(piece1)) {
               makeHen(piece1);
           }
           
           Piece empty;
           if(playerTurn) {   
                empty = findEmpty(bench1); 
           }
           else {
                empty = findEmpty(bench2);    
           }   
           piece2.setPosition(empty.getXPos(),empty.getYPos());
           piece2.setLocation(empty.getLocation());
           placePieceToBench(piece2,piece2.getLocation());
           
           empty.setPosition(x,y);
           empty.setLocation(location);
           board[empty.getLocation()] = empty;
            
           piece2.setInBench(true);        
           empty.setInBench(false);
        }
    }
    
    /**
     * findEmpty find an empty spot in the given bench
     * @param bench, the bench to be searched
     */
    private Piece findEmpty(Piece[] bench) {
        int i = 0;
        while(!bench[i].getName().contains("Empty") ) {
            i++;
        }
        return bench[i];
    }
    
    /**
     * findName for changing control of pieces we swap up with down or vice versa
     * @param name, the original name of the piece
     * @return the new name of the piece
     */
    private String findName(String name) {
        if(name.contains("Up")) {
            if(name.contains("Ele") ) {
                return "EleDown";
            }
            if(name.contains("Chick") ) {
                return "ChickDown";
            }
            if(name.contains("G") ) {
                return "GDown";
            }
        }
        else {
            if(name.contains("Ele") ) {
                return "EleUp";
            }
            if(name.contains("Chick") ) {
                return "ChickUp";
            }
            if(name.contains("G") ) {
                return "GUp";
            }
        }
        return name;      
    }
    
    public void clearBoard(StackPane r) {
       playerTurn = true;
       for (Piece piece : board) { 
          r.getChildren().remove(piece.getIV());
       }
       for (Piece piece : bench1) { 
          r.getChildren().remove(piece.getIV());
       }
       for (Piece piece : bench2) { 
          r.getChildren().remove(piece.getIV());
       }
       r.getChildren().remove(reticleImageView);
    }
}