/**
 * Created bSquare mateusz on 19.01.2014.
 */
public class MyPair {
    private Integer first;
    private Square second;

    public MyPair(Integer first, Square second) {
        this.first = first;
        this.second = second;
    }

    public Integer getFirst() {
        return first;
    }

    public void setFirst(Integer first) {
        this.first = first;
    }

    public Square getSecond() {
        return second;
    }

    public void setSecond(Square second) {
        this.second = second;
    }

    public MyPair incrementFirst(){
        first = first + 1;
        return this;
    }
}
