package exc;

public class NogivenNameException extends ArrayIndexOutOfBoundsException {
    public NogivenNameException(String cause, String message, Throwable e){
        super();
        System.out.println(cause);
        System.out.println(message);
        e.printStackTrace();
    }
}
