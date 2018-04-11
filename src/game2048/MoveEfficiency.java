package game2048;

public class MoveEfficiency implements Comparable<MoveEfficiency>{

    private int numberOfEmptyTiles;

    public Move getMove() {
        return move;
    }

    private Move move ;

    private int score;


  public   MoveEfficiency  (int numberOfEmptyTiles, int score, Move move){

      this.numberOfEmptyTiles=numberOfEmptyTiles;

      this.move=move;

      this.score=score;

  }



    public int compareTo( MoveEfficiency moveEfficiency) {





      int numberofEMP =  this.numberOfEmptyTiles-moveEfficiency.numberOfEmptyTiles;

          if(numberofEMP!=0) return numberofEMP;

        return  Integer.compare(score,moveEfficiency.score);

    }
}
