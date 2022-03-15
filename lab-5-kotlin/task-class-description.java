public class MusicBand {
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private int numberOfParticipants; //Значение поля должно быть больше 0
    private long albumsCount; //Значение поля должно быть больше 0
    private java.util.Date establishmentDate; //Поле может быть null
    private MusicGenre genre; //Поле не может быть null
    private Label label; //Поле не может быть null
}
public class Coordinates {
    private Double x; //Поле не может быть null
    private Double y; //Поле не может быть null
}
public class Label {
    private long bands;
}
public enum MusicGenre {
    PROGRESSIVE_ROCK,
    RAP,
    BLUES,
    PUNK_ROCK;
}