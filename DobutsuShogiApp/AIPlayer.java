import java.util.Stack;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Random;  

/** 
 * AIplayer, manages the AI
 */
public class AIPlayer {//add case for lion at end

   /** depth, the farthest depth we will search **/
   private int depth = 0;
   
   /** movesMap, the moves possible by the given piece**/
   private HashMap movesMap;
   
   /** the states after the AI's first move**/
   LinkedList<State> firstStates;
   
   /**
    * AIPlayer the constructor for setting up the AI player
    * @param depth, the depth that the AI will search to
    */
   public AIPlayer(int depth) {
      this.depth = depth;
     
      firstStates = new LinkedList<State>();
      movesMap = new HashMap<String,int[]>();
      int[] moves1 = {-3};
      movesMap.put("ChickUp",moves1);
      int[] moves2 = {3};
      movesMap.put("ChickDown",moves2);
      int[] moves3 = {-4,-2,2,4};
      movesMap.put("EleUp",moves3);
      movesMap.put("EleDown",moves3);
      int[] moves4 = {-3,-1,1,3};
      movesMap.put("GUp",moves4);
      movesMap.put("GDown",moves4);
      int[] moves5 = {-4,-3,-2,-1,1,2,3,4};
      movesMap.put("LionUp",moves5);
      movesMap.put("LionDown",moves5);
      int[] moves6 = {-3,-1,1,2,3,4};
      movesMap.put("HenDown",moves6);
      int[] moves7 = {-4,-3,-2,-1,1,3};
      movesMap.put("HenUp",moves7);
   }
   
   /** 
    * State these are used to represent the state of the game
    * after a given move
    */
   private static class State {
      /**The board of the game**/
      private String[] aIBoard;
      
      /** player 1's bench **/
      private String[] aIBench1;
      
      /** player 2's bench **/
      private String[] aIBench2;
      
      /** playerTurn, true if the state is in player 1's turn, false otherwise**/
      private boolean playerTurn = false;
      
      /** the points of the state, used for decision making **/
      int points = 0;
      
      /** height, the current height of the state in the DFS tree**/
      private int height = 0;
      
      /** location1 original location of the piece that moved**/  
      int location1 = -1;
      /** location2 the location that the piece was moved to**/
      int location2 = -1;
      
      /** whether or not we used the bench, true if we did, false otherwise*/
      boolean benchMove = false;
      
      /**
       * State the constructor for the state
       * Initializes the arrays
       */  
      public State() {
         this.aIBoard = new String[12];
         this.aIBench1 = new String[6];
         this.aIBench2 = new String[6];
      }
      
      /**
       * onBoardSwap, places pieces from location1 to location2
       * and location2 to the bench
       * @param location1 the location of the attacking piece
       * @param location2 the location of the defending piece
       */
      public void onBoardSwap(int location1, int location2) {
         int benchLocation = findEmpty();
         String[] bench = {""};
         
         if(playerTurn) {
            bench = aIBench1;
         }
         else {
            bench = aIBench2;
         }
         
         String benchedPiece = "";
         if(!aIBoard[location2].contains("Lion") && benchLocation > 0) {//if there is an empty bench spot
             benchedPiece = bench[benchLocation];  
             bench[benchLocation] = findName(aIBoard[location2]);
         }      
         
         if(aIBoard[location2].contains("Lion")) {//dont add lion to bench
             aIBoard[location2] = aIBoard[location1];
             aIBoard[location1] = "Empty";
         }
         else if(benchLocation == -1 ) {//deal with full bench
              String temp = aIBoard[location2];
              aIBoard[location2] = aIBoard[location1];
              aIBoard[location1] = temp;
         }
         else {
             aIBoard[location2] = aIBoard[location1];//move attacking piece
             aIBoard[location1] = benchedPiece;//empty piece goes here
         }
         
         //handle chick upgrades
         if(aIBoard[location2].equals("ChickUp") && location2 < 3) {
            aIBoard[location2] = "HenUp"; 
         }
         else if(aIBoard[location2].equals("ChickDown") && location2 > 8) {
            aIBoard[location2] = "HenDown"; 
         }
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
            if(name.contains("Chick")  || name.contains("Hen") ) {
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
            if(name.contains("Chick") || name.contains("Hen")) {
               return "ChickUp";
            }
            if(name.contains("G") ) {
               return "GUp";
            }
         }
         return name;      
      }
      
      /**
       * onBenchSwap when we swap from the bench and the board
       * i, the index of the piece on the bench
       * j, the index of the piece on the board
       */
      public void onBenchSwap(int i, int j) {
         String[] bench = {""};
         if(playerTurn) {
            bench = aIBench1;
         }
         else {
            bench = aIBench2;
         }
         String benched = bench[i];
         bench[i] = aIBoard[j];
         aIBoard[j] = benched;
      }
      
     /**
      * findEmpty find an empty spot in the given bench
      * @param bench, the bench to be searched
      */
      public int findEmpty() {
         String[] bench = {""};
         if(playerTurn) {
            bench = aIBench1;
         }
         else {
            bench = aIBench2;
         }
         for(int i = 0; i < bench.length; i++) {
            if(bench[i].contains("Empty") ) {
               return i;
            }
         }
         return -1;
      }
   }
   /**
    * copyState creates a copy of a given state and returns it
    * @param state, the state to copy
    * @return a copy of the given state
    */
   public State copyState(State state) {
      String[] aIBoard = new String[12];
      String[] aIBench1 = new String[6];
      String[] aIBench2 = new String[6];
        
      State state2 = new State();
      for(int i = 0; i < 12; i++) {
         state2.aIBoard[i] = state.aIBoard[i];
      }
      for(int i = 0; i < 6; i++) {
         state2.aIBench1[i] = state.aIBench1[i];
      }
      for(int i = 0; i < 6; i++) {
         state2.aIBench2[i] = state.aIBench2[i];
      }
   
      state2.playerTurn = state.playerTurn;
      state2.points = state.points;
        
      return state2;
   }
  
   /**
    * generateState, generate a state based on the current game state stored in the
    * GameState Object.
    * @param board the board of the game
    * @param bench1 player1's bench
    * @param bench2 player2's bench
    */  
   public State generateState(Piece[] board, Piece[] bench1, Piece[] bench2) {
      State state = new State();
      for(int i = 0; i < 12; i++) {
         state.aIBoard[i] = board[i].getName();
         if(board[i].getName().contains("Down") ) {
            if(!board[i].getName().contains("Lion") ) {
               state.points -= determinePoints(board[i].getName());
            }
         }
      }           
      for(int i = 0; i < 6; i++) {
         state.aIBench1[i] = bench1[i].getName();
      }
      for(int i = 0; i < 6; i++) {
         state.aIBench2[i] = bench2[i].getName();
         if(!board[i].getName().contains("Lion") ) {// dont start out with the negative points
            state.points -= determinePoints(bench2[i].getName());
         }
      }
      return state;
   }
   
   /**
    * makeMove, determine which two pieces to swap
    * @param board the board of the game
    * @param bench1 the bench of player 1
    * @param bench2 the bench of player 2
    * @return an array of two pieces to be swapped
    */ 
   public Piece[] makeMove(Piece[] board, Piece[] bench1, Piece[] bench2) {
      //System.out.println("--------------------------");
      findLegalMoves(generateState(board,bench1,bench2));
      int max = firstStates.peek().points;
      State bestState = firstStates.peek();
      Piece[] move = new Piece[2];
      //System.out.println("");
      State[] bestMoves = findBestMoves();
      Random rand = new Random();
      bestState = bestMoves[rand.nextInt(bestMoves.length)];
      
      
      if(!bestState.benchMove) {
         move[0] = board[bestState.location1];
         move[1] = board[bestState.location2];
      }
      else {
         move[1] = bench2[bestState.location1];
         move[0] = board[bestState.location2];
      }
      return move;
   }
   
   /**
    * findBestMoves, gathers the moves with the heighest value
    * and places them in an array.
    * @return bestMoves, the bestMoves that the AI could find
    */
   private State[] findBestMoves() {
       int max = firstStates.peek().points;
       //find max points
       for(int i = 0; i < firstStates.size(); i++) { 
           int points = firstStates.get(i).points;
           if(points > max) {
               max = points;
           } 
       }
       //find amount of best moves
       int count = 0;
       for(int i = 0; i < firstStates.size(); i++) { 
           int points = firstStates.get(i).points;
           if(points == max) {
               count++;
           } 
       }
       
       //create bestMoves array and empty linkedlist
       State[] bestMoves = new State[count];
       int index = 0;
       while(firstStates.peek() != null) {
           State state = firstStates.pop();
           if(state.points == max) {
               bestMoves[index] = state;
               index++;
           }
       } 
       return bestMoves;
   }
   
   /** 
    * createStateAfterMove, creates a state after a move
    * @param state the previous state
    * @param location1 the original location of the piece to be moved
    * @param location2 the new location of the moved piece
    * @return the state after a given move
    */ 
   private State createStateAfterMove(State state,int location1,int location2 ,boolean benchSwap) {
      State state2 = copyState(state);
      state2.points = state.points;
      if(!benchSwap) {
         state2.points += determinePoints(state2.aIBoard[location2]);
         state2.onBoardSwap(location1,location2);
         if(state2.aIBoard[location2] == "LionUp" && location2 < 3) {
             state2.points -= 99999;
         }
         else if(state2.aIBoard[location2] == "LionDown" && location2 > 8) {
             state2.points += 99999;
         }
         state2.benchMove = false;
      }
      else {
         state2.onBenchSwap(location1,location2);
         state2.benchMove = true;
      }
      state2.playerTurn = !state.playerTurn;
      state2.height = state.height + 1;
      state2.location1 = location1;
      state2.location2 = location2;
      
      return state2;
   }
   
   /**
    * findLegalMoves search for all possible moves from a given point
    * @param State the state to search from
    * @return the amount of points that the state had 
    */
   private int findLegalMoves(State state) {
      //System.out.println("Height: "+state.height +" Move: " + state.location1 + " swap with " + state.location2 + " points: " + state.points + " BenchSwap " + state.benchMove + " PlayerTurn " + state.playerTurn);
      int total = -5000;
      boolean totalNotGivenStartingValue = true;
      if(depth == state.height) {
         return state.points;
      }
      if(Math.abs(state.points) > 9000) {
          if(state.height == 1) {
              firstStates.add(state);
          }
          return state.points;
      } 
      for(int i = 0; i < 12; i++) {
         if(state.playerTurn && state.aIBoard[i].contains("Up")) {
            int moves[] =(int[]) movesMap.get(state.aIBoard[i]);
            for(int j = 0; j < moves.length; j++) {
               if(checkLegalMove(state.aIBoard, i, moves[j]) ) {
                  State state2 = createStateAfterMove(state,i,moves[j]+i,false);
                  int points = findLegalMoves(state2);
                  if(points < total || totalNotGivenStartingValue) {
                      totalNotGivenStartingValue = false;
                      total = points;
                  }
               }
            }
         } 
         else if (!state.playerTurn && state.aIBoard[i].contains("Down")) {
            int moves[] = (int[]) movesMap.get(state.aIBoard[i]);
            for(int j = 0; j < moves.length; j++) {
               if(checkLegalMove(state.aIBoard, i, moves[j]) ) {
                  State state2 = createStateAfterMove(state,i,moves[j]+i,false);
                  int points = findLegalMoves(state2);
                  if(points > total || totalNotGivenStartingValue) {
                      totalNotGivenStartingValue = false;
                      total = points;
                  }
               }
            }
         }  
      }
      String[] bench = {""};
      if(state.playerTurn) {
         bench = state.aIBench1;
      }
      else {
         bench = state.aIBench2;
      }
      for(int i = 0; i < 6; i++) {
         if(!bench[i].contains("Empty")) {
            for(int j = 0; j < 12; j++) {
            
               if(state.aIBoard[j].contains("Empty") ) {
                  State state2 = createStateAfterMove(state,i,j,true);
                
                  int points = state2.points;
                  points = findLegalMoves(state2);
                  if(state.playerTurn && points < total || totalNotGivenStartingValue) {
                      total = points;
                      totalNotGivenStartingValue = false;
                  }
                  else if(!state.playerTurn && points > total || totalNotGivenStartingValue) {
                      total = points;
                      totalNotGivenStartingValue = false;

                  }
               }
            }
         }
      }       
      if(state.height == 1) {
         firstStates.add(state);
      }
      state.points = total;
      //System.out.println("||End|| Height: "+state.height +" Move: " + state.location1 + " swap with " + state.location2 + " points: " + state.points + " BenchSwap " + state.benchMove + " PlayerTurn " + state.playerTurn);
      if(state.points > 9000) {
          //System.out.println("height: " + state.height);
      }
      
      return total;
   }
   
   /**
    * checkLegalMove determine if a move is legal
    * @param aIBoard, the board of the game
    * @param location, the index of the piece
    * @param move, the move that the piece will make
    */
   private boolean checkLegalMove(String[] aIBoard, int location, int move) {
      int newLocation = location + move;
   
      if(newLocation > -1 && newLocation < 12 &&
          ( (aIBoard[location].contains("Up") && !aIBoard[newLocation].contains("Up") ) || 
            (aIBoard[location].contains("Down") && !aIBoard[newLocation].contains("Down") ) ) ) {
         if(move == 3 || move == -3 ) {
            return true;
         }
         if( (move == -1 || move == 2 || move == -4) && location % 3 != 0) {
            return true;
         }
         if( (move == 1 || move == -2 || move == 4) && location % 3 != 2) {
            return true;
         }
      }
      return false;
   }
   
   /**
    * determinePoints, determine the amount of points of a taken piece,
    * based on it's name.
    * @param name the name of the piece
    */
   private int determinePoints(String name) {
      switch (name) {
         case "LionUp":  
            return 99999;
         case "LionDown":  
            return -99999;
         case "ChickUp":  
            return 1;
         case "ChickDown":  
            return -1;
         case "HenUp":  
            return 1;
         case "HenDown":  
            return -1;
         case "EleUp":  
            return 4;
         case "EleDown":  
            return -4;
         case "GUp":  
            return 4;
         case "GDown":  
            return -4; 
         default: 
            return 0;
      } 
   }
}
