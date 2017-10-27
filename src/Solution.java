public class Solution {
    int total;
    int previousTotal;
    boolean fromFirst;

    public Solution (int total, int previousTotal, boolean fromFirst) {
        this.total = total;
        this.previousTotal = previousTotal;
        this.fromFirst = fromFirst;
    }

    @Override
    public String toString () {
        return String.format("%d,%s", total, (fromFirst ? "F(↓)" : "L(←)"));
    }
}
