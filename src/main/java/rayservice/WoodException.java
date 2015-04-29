package rayservice;

//import java.util.Iterator;

// implements Iterable<Throwable>
public class WoodException extends Exception {

    private static final long serialVersionUID = 1L;
    private final RayStatus rayStatus;
    private final String origin;

    public WoodException(RayStatus rayStatus, String origin) {
        this.rayStatus = rayStatus;
        this.origin = origin;
    }

    public RayStatus getContent() {
        return rayStatus;
    }

    public String getOrigin() {
        return origin;
    }

//    @Override
//    public Iterator<Throwable> iterator() {
//        return null;
//    }
}
