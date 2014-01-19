import java.util.ArrayList;

/**
 * Created by uziak on 18.01.14.
 */
public class MinMax {

    private int treeDepth;
    private Integer startWeight = 0;
    private GameEngine engine;
    private int bestMove = 0;

    private ArrayList<TreeNode> generateMove(int x, int y, Board board){

        int[][] values = new int[10][10];
        ArrayList<TreeNode> row = new ArrayList<TreeNode>();



        for (int i = 0;i<10;i++)
            for (int j=0;j<10;j++){
                if (board.getSquare(i,j).getState().equals(Square.State.EMPTY)){

                    if (bestMove == 0){
                        row.add(new TreeNode(i, j, engine.checkValueOfArea(i, j, board)));
                    } else {
                        row.add(new TreeNode(i, j, engine.checkValueOfArea(i, j, board)));
                    }

                } else values[i][j] = 0;
            }

        return row;
    }



    public int MinaMaxAlgorithm(int depth, int x, int y, Board newBoard, boolean maxPlayer){

        int bestValue = 0;
        int value = 0;
        Board newBoardB = newBoard;

        if (depth == 0) return 0;
        if (maxPlayer){
            bestValue= -1000;
            for (TreeNode node: generateMove(x, y, newBoardB)){
                value = MinaMaxAlgorithm(depth -1, node.getX(), node.getY(), newBoardB, maxPlayer = false);
                bestValue = Math.max(bestValue, value);
            }
        } else if (!maxPlayer){
            bestValue= 1000;
            for (TreeNode node: generateMove(x, y, newBoardB)){
                value = MinaMaxAlgorithm(depth -1, node.getX(), node.getY(), newBoardB, maxPlayer = true);
                bestValue = Math.min(bestValue, value);
            }
        }

        return value;
      }
}

