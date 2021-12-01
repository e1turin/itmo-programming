package types;

public enum Feeling {
    PAIN("Боль"), NOTHING("Ничего");
    String name;
    Feeling(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
