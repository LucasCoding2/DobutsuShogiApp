import javafx.scene.image.*;

public class Piece {
       /** the name of the piece **/
       private String name;
       /** the location of the piece on the board starting from 0**/
       private int location;
        
        /** The ImageView of the piece, it contains the image of the piece and allows use to reposition
        the image on the Canvas**/
       private ImageView iv;
       
       /** The x and y positions of the piece**/
       private int xPos = 0;
       private int yPos = 0;

       /** Whether or not the piece is in a bench**/
       private boolean inBench = false;

       /** Whether or not the piece was rotated 180 degrees **/
       private boolean rotated180 = false;
       
       /**
        * Piece the constructor for setting up a piece.
        * @param name the name of the piece, key for identificaiton
        * @param location the index the piece is stored in
        * @param ImageView the ImageView
        * @param xPos the x position of the ImageView on canvas
        * @param yPos the y position of the ImageView on canvas
        */
       public Piece(String name,int location, ImageView iv, int xPos, int yPos) {
               //set up the properties
               this.name = name;
               this.location = location;
               this.iv = iv;    
               this.xPos = xPos;
               this.yPos = yPos;
               
               //translate the image view
               this.iv.setTranslateX(xPos);
               this.iv.setTranslateY(yPos);
       }
       
       /**
        * String toString the toString method to return information about the object
        * @Return returns a String containing the Piece's name, position and whether it is benched
        */
       public String toString() {
           return  "Name: " + this.name + " Location: " + this.location + " Bench: " + inBench;
       }
       
       /**
        * ImageView getIV returns the ImageView associated with the piece
        * @Return iv - the imageView with the piece
        */
       public ImageView getIV() {
           return iv;
       }
       /**
        * getLocation, get the location the piece is stored in an array
        * @return location, the index the piece is stored in an array
        */
       public int getLocation() {
           return location;
       }
       
       /**
        * getXPos, get the x position of the ImageView
        * @return xPos, the xPos of the ImageView
        */
       public int getXPos() {
           return xPos;
       }
       
       /**
        * getYPos, get the y position of the ImageView
        * @return yPos, the yPos of the ImageView
        */
       public int getYPos() {
           return yPos;
       }
       
       /**
        * setXPos set the x position of the piece
        * @param xPos the new x position
        */
       public void setXPos(int xPos) {
           this.xPos = xPos;
       }
       
       /**
        * setYPos set the y position of the piece
        * @param yPos the new y position
        */
       public void setYPos(int yPos) {
            this.yPos = yPos;
       }
       
       /**
        * setLocation, set the new index that the piece is stored at
        * @param location, the location of the index that the piece is stored
        * at within an array.
        */
       public void setLocation(int location) {
           this.location = location;
       }
       
       /** 
        * setPosition, set the x and y positions of the piece
        * and translate the piece
        * @param xPos the new x position
        * @param yPos the new y position
        */
       public void setPosition(int xPos, int yPos) {
           this.xPos = xPos;
           this.yPos = yPos;
           this.iv.setTranslateX(xPos);
           this.iv.setTranslateY(yPos);
       }
       
       /**
        * setName, set the name of the piece, pieces swap owners, so 
        * they need to be renamed
        * @return name, the new name of the piece
        */
       public void setName(String name) {
           this.name = name;
       }
       
       /**
        * getName get the name of the piece
        * @return name the name of the piece
        */
       public String getName() {
           return name;
       }
       
       /**
        * setInBench, set whether or not the piece is in a bench
        * @param inBench true if the peice is in a bench
        * false otherwise
        */
       public void setInBench(boolean inBench) {
           this.inBench = inBench;
       }
       
       /**
        * inBench determine whether or not the piece is in a bench
        * @return inBench, true if the piece is in a bench
        * false otherwise
        */
       public boolean inBench() {
           return inBench;
       }
       
       /**
        * rotated180 determine if the piece was rotated or not
        * @return rotated180 true if the piece was rotated
        * false if the piece is not rotated
        */
       public boolean rotated180() {
           return rotated180;
       }
       
       /** 
        * setRotated180 set if the piece was rotated or not
        * @param rotated180 true if the piece was rotated180
        * false otherwise
        */
       public void setRotated180(boolean rotated180) {
           this.rotated180 = rotated180;
       }
    }

