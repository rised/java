package Diplom;

public enum Priorities {
    ;
    private final int priority;
    public static int countOfPriorities = 3;

    Priorities(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
