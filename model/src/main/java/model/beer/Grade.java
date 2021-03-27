package model.beer;

public enum Grade {
    A(5),
    B(4),
    C(3),
    D(2),
    F(1);

    private final int gradePoint;

    Grade(int gradePoint) {
        this.gradePoint = gradePoint;
    }

    public static double gradePointAverage(Grade... grades) {
        int total = 0;
        for (Grade grade : grades) {
            total += grade.gradePoint();
        }
        return (total == 0) ? 0.0 : (total / grades.length);
    }

    public int gradePoint() {
        return gradePoint;
    }
}