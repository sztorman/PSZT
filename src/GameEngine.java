/**
 * Created by uziak on 19.01.14.
 */
public class GameEngine implements ValueFunction {

    //mamy kilka rodzajów sytuacji
    //1. jeśli wstawimy pionek, to uzyskamy linię z 4 kamieni - 5pkt.
    //2. Wstawienie pionka powoduje uzyskanie linii z więcej niż 4 kamieni - 0pkt.
    //3. Wstawienie pionka powoduje otrzymanie linii < 4 kamienie (odpowiednio 1, 2 pkt)
    //4. Wstawienie pionka powoduje zablokowanie przeciwinika 4 pknt
    int value = -1;
    private MinMax minimax;


    public int checkValueOfArea(int x, int y, Board board){
       Board localBoard = board;
       int black = 0;
       int white = 0;
        int y1;
       int valueTemp;
        //sprawdzanie tablicy po skosie
        y1 = y-3;
        for (int i = x-3;i<x+3;i++){

                if ((x>=0 && y1>=0) && (x<=10 && y1 <= 10)){
                    if (localBoard.getSquare(i,y1).equals(Square.State.BLACK)) black++;
                    else white++;
                }

            y1++;

            }

        getValueFunction(white, black);
        black = 0;
        white = 0;

        //sprawdzanie po drugim skosie
        y1 = y+3;
        for (int i = x-3;i<x+3;i++){

            if ((x>=0 && y1>=0) && (x<=10 && y1 <= 10)){
                if (localBoard.getSquare(i,y1).equals(Square.State.BLACK)) black++;
                else white++;
            }

            y1--;

        }

        getValueFunction(white, black);
        black = 0;
        white = 0;

        //sprawdzanie góra - dół o lewo - prawo

        for (int i = x-3;i<x+3;i++){

            if ((x>=0 && y1>=0) && (x<=10 && y1 <= 10)){
                if (localBoard.getSquare(i,y1).equals(Square.State.BLACK)) black++;
                else white++;
            }

        }

        getValueFunction(white, black);
        black = 0;
        white = 0;

        for (int i = y-3;i<y+3;i++){

            if ((x>=0 && y1>=0) && (x<=10 && y1 <= 10)){
                if (localBoard.getSquare(i,y1).equals(Square.State.BLACK)) black++;
                else white++;
            }

        }

        getValueFunction(white, black);
        black = 0;
        white = 0;

        return value;

    }


    private void getValueFunction(int white, int black){

        int valueTemp = 0;

        if (black ==3) valueTemp=5;
        else if ((0<black)&&(black<3)) valueTemp=black;
        else if (black<3) valueTemp = 0;
        else if (white == 3) valueTemp = 4;
        else valueTemp = 0;

        value = Math.max(value, valueTemp);

    }

    public int[] makeMove(GameManager gameManager){

        int value = 0;
        int depth = 5;
        int miniMaxResult;
        Board board = gameManager.getGame().getBoard();
        int lastX = gameManager.getGame().getLastPlacedSquare().getLocation().getKey();
        int lastY = gameManager.getGame().getLastPlacedSquare().getLocation().getValue();
        int[] result;

        result = new int[2];

        for (int i = lastX -1;i<lastX + 1;i++)
            for (int j = lastY -1; j<lastY +1;j++ ){

                if ((i<0)||(j<0) || (i>9) || (j>9) || (i==lastX) || (j==lastY) ){

                } else {
                    minimax = new MinMax();
                    miniMaxResult = minimax.MinaMaxAlgorithm(depth,i,j,board, true);
                    if (miniMaxResult>value){

                        value = miniMaxResult;
                        result[0] = i;
                        result[1] = j;
                    }
                }

            }
        return result;

    }



}
