public class Solution {
    int total;
    int previousTotal;
    boolean fromFront;

    public Solution (int total, int previousTotal, boolean fromFront) {
        this.total = total;
        this.previousTotal = previousTotal;
        this.fromFront = fromFront;
    }

    @Override
    public String toString () {
        return String.format("%d,%s", total, (fromFront ? "F(↓)" : "B(←)"));
    }
}
