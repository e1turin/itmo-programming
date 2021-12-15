package exc;

import java.util.Objects;

public class IncorrectFileNameException extends Exception {
    public IncorrectFileNameException(String errorMessage) {
        super(errorMessage);
    }
    static boolean isCorrectFileName(String path) {
        return path.equals("C:/meme.txt");
    }
}