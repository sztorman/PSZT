import java.util.ArrayList;

/**
 * Created by uziak on 18.01.14.
 */
public class MinMax {

    private int treeDepth;
    private Integer startWeight = 0;
    private GameEngine engine;
    private int bestMove = 0;

    private ArrayList<TreeNode> generateMove(int x, int y, Board board, int weight){

        ArrayList<TreeNode> row = new ArrayList<TreeNode>();
        engine = new GameEngine();
        int empty = 0;
        int filled = 0;

        for (int i = x-1;i<x+1;i++)
            for (int j=y-1;j<y+1;j++){
                if (i<0|| i>10 || j<0 || j>10 || board.getSquare(i,j).getState().equals(Square.State.BLACK) || board.getSquare(i,j).getState().equals(Square.State.WHITE) ){

                filled++;

                } else if (board.getSquare(i,j).getState().equals(Square.State.EMPTY)) empty++;
            }

        if (filled>0 && empty == 0) {

            for (int i = 0;i<10;i++)
                for (int j=0;j<10;j++){
                        if (board.getSquare(i,j).getState().equals(Square.State.EMPTY)){

                            row.add(new TreeNode(i, j, engine.checkValueOfArea(i, j, board) + weight));

                        }
                }

        } else {


        for (int i = x-1;i<x+1;i++)
            for (int j=y-1;j<y+1;j++){
                if (i>=0 && i<10 && j>=0 && j<10){
                if (board.getSquare(i,j).getState().equals(Square.State.EMPTY)){

                        row.add(new TreeNode(i, j, engine.checkValueOfArea(i, j, board) + weight));

                }
            }
            }
        }
        return row;
    }



    public int MinaMaxAlgorithm(int depth, int x, int y, int weight, Board newBoard, boolean maxPlayer){

        int bestValue = 0;
        int value = 0;
        Board newBoardB = newBoard;

        if (depth == 0) return engine.checkValueOfArea(x, y, newBoardB);
        if (maxPlayer){
            bestValue= -1000;
            for (TreeNode node: generateMove(x, y, newBoard, weight)){
                value = MinaMaxAlgorithm(depth -1, node.getX(), node.getY(), node.getWeight(), newBoardB, false);
                bestValue = Math.max(bestValue, value);
            }
        } else if (!maxPlayer){
            bestValue= 1000;
            for (TreeNode node: generateMove(x, y, newBoardB, weight)){
                value = MinaMaxAlgorithm(depth -1, node.getX(), node.getY(), node.getWeight(), newBoardB, true);
                bestValue = Math.min(bestValue, value);
            }
        }

        return value;
      }
}

