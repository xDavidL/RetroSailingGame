package seng302.team18.visualiser.display;

/**
 * Enumerated types for annotations displayed on the GUI
 */
public enum AnnotationType {
    NAME(1),
    SPEED(2),
    ESTIMATED_TIME_NEXT_MARK(3),
    TIME_SINCE_LAST_MARK(4);

    private int code;

    private AnnotationType(int code) {
        this.code = code;
    }

    public int getCode() {
        return  code;
    }
}