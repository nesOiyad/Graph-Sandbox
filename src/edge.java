public class edge {
    private node start;
    private node end;
    private int weight;

    public edge(node eStart, node eEnd, int eWeight) {
        start = eStart;
        end = eEnd;
        weight = eWeight;
    }

    public node getStart() {
        return start;
    }

    public node getEnd() {
        return end;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int num) {
        weight = num;
    }
}