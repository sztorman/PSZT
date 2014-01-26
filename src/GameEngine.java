/**
 * Created by uziak on 19.01.14.
 */
public class GameEngine implements ValueFunction {

    //mamy kilka rodzajów sytuacji
    //1. jeśli wstawimy pionek, to uzyskamy linię z 4 kamieni - 5pkt.
    //2. Wstawienie pionka powoduje uzyskanie linii z więcej niż 4 kamieni - 0pkt.
    //3. Wstawienie pionka powoduje otrzymanie linii < 4 kamienie (odpowiednio 1, 2 pkt)
    //4. Wstawienie pionka powoduje zablokowanie przeciwinika 4 pknt
    int value = 0;
    private MinMax minimax;


    public int checkValueOfArea(int x, int y, Board board){
        int black = 0;
        int white = 0;
        int newValue = 0;
        int y1;
        int valueTemp;
        //sprawdzanie tablicy po skosie
        y1 = y-3;
        for (int i = x-3; i <= x+3; i++){
            if ((i>=0 && y1>=0) && (i< 10 && y1 < 10)){
                if (board.getSquare(i, y1).getState().equals(Square.State.BLACK)) black++;
                else if (board.getSquare(i, y1).getState().equals(Square.State.WHITE)) white++;
            }
            y1++;
        }

        getValueFunction(white, black);
        black = 0;
        white = 0;

        //sprawdzanie po drugim skosie
        y1 = y+3;
        for (int i = x-3; i <= x+3; i++){
            if ((i>=0) && (y1>=0) && (i<10) && (y1 < 10)){
                if (board.getSquare(i, y1).getState().equals(Square.State.BLACK)) black++;
                else if (board.getSquare(i, y1).getState().equals(Square.State.WHITE)) white++;
            }
            y1--;
        }

        getValueFunction(white, black);
        black = 0;
        white = 0;

        //sprawdzanie góra - dół o lewo - prawo
        for (int i = x-3; i <= x+3; i++){
            if ((i>=0 && y>=0) && (i<10 && y < 10)){
                if (board.getSquare(i, y).getState().equals(Square.State.BLACK)) black++;
                else if (board.getSquare(i, y).getState().equals(Square.State.WHITE)) white++;
            }
        }

        getValueFunction(white, black);
        black = 0;
        white = 0;

        for (int i = y-3; i <= y+3; i++){
            if ((x>=0 && i>=0) && (x<10 && i < 10)){
                if (board.getSquare(x, i).getState().equals(Square.State.BLACK)) black++;
                else if (board.getSquare(x, i).getState().equals(Square.State.WHITE)) white++;
            }
        }

        getValueFunction(white, black);
        black = 0;
        white = 0;

        return value;
    }


    private void getValueFunction(int white, int black){

        int valueTemp = 0;

        if (black>white){

            if (black ==3) valueTemp=10;
            else if (((0<black)&&(black<3))) valueTemp=black;

        } else if (white>=black){
            if (white == 3) valueTemp = -6;
            else if (white == 2) valueTemp = -2;
            else valueTemp = 0;
        }
       // else if (black<3) valueTemp = 0;

        value += valueTemp;
    }

    public int[] makeMove(Game game){

        int value = 0;
        int depth = 1;
        int miniMaxResult;
        Board board = game.getBoard();
        int lastX = game.getLastPlacedSquare().getLocation().getKey();
        int lastY = game.getLastPlacedSquare().getLocation().getValue();
        int[] result;

        result = new int[2];

        int empty = 0;
        int filled = 0;

        // Policz zajęte i wolne pola w okolicy ostatnio polozonego pionka
        for (int i = lastX-1; i<=lastX+1; i++)
            for (int j=lastY-1; j<=lastY+1; j++){
                if (i<0 || i>9 || j<0 || j>9 || board.getSquare(i,j).getState().equals(Square.State.BLACK) || board.getSquare(i,j).getState().equals(Square.State.WHITE) ){
                    filled++;
                } else if (board.getSquare(i,j).getState().equals(Square.State.EMPTY))
                    empty++;
            }

        // jezeli nie ma zadnego wolnego policz minmax dla wszystkich wolnych na planszy
        if (filled>0 && empty == 0) {
            for (int i = 0;i<10;i++)
                for (int j=0;j<10;j++){
                    if (board.getSquare(i,j).getState().equals(Square.State.EMPTY)){
                        minimax = new MinMax();
                        miniMaxResult = minimax.MinaMaxAlgorithm(depth,i,j, this.checkValueOfArea(i,j,game.getBoard()), board, true);
                        if (miniMaxResult>=value){
                            value = miniMaxResult;
                            result[0] = i;
                            result[1] = j;
                        }
                    }
                }
        }
        // w przeciwnym przypadku policz tylko dla wolnych w okolicy
        else {
            for (int i = lastX -1; i<=lastX + 1; i++)
                for (int j = lastY -1; j<=lastY +1; j++ ){
                    if (i>=0 && i<10 && j>=0 && j<10 && board.getSquare(i,j).getState().equals(Square.State.EMPTY)){
                        minimax = new MinMax();
                        miniMaxResult = minimax.MinaMaxAlgorithm(depth,i,j, this.checkValueOfArea(i,j,game.getBoard()), board, true);
                        if (miniMaxResult>=value){
                            value = miniMaxResult;
                            result[0] = i;
                            result[1] = j;
                        }
                    }
                }
        }
        return result;

    }


}
