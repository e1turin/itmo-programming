package types;

public enum Color {
    RED("Красный"), WHITE("Белый"), UNDEFINED("Неопределеный");
    String name;

    Color(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
