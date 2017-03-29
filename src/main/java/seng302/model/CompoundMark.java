package seng302.model;

import seng302.raceutil.GPSCalculations;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by csl62 on 15/03/17.
 */
public class CompoundMark {

    public static int GATE_SIZE = 2;
    public static int MARK_SIZE = 1;
    private String name;
    private List<Mark> marks;
    private List<Boat> passed;

    public CompoundMark(String name, List<Mark> marks) {
        if (0 < marks.size() && marks.size() <= 2) {
            this.name = name;
            this.marks = marks;
            passed = new ArrayList<>();
        } else {
            System.err.println("A compound mark cannot have more than 2 marks");
        }
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public List<Mark> getMarks() {
        return marks;
    }


    public Coordinate getMidCoordinate() {
        if (marks.size() == GATE_SIZE) {
            return GPSCalculations.GPSMidpoint(marks.get(0).getCoordinates(), marks.get(1).getCoordinates());
        } else if (marks.size() == MARK_SIZE) {
            return marks.get(0).getCoordinates();
        } else {
            return null;
        }
    }


    public List<Boat> getPassed() {
        return passed;
    }


    public void addPassed(Boat passed) {
        this.passed.add(passed);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompoundMark that = (CompoundMark) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (marks != null ? !marks.equals(that.marks) : that.marks != null) return false;
        return passed != null ? passed.equals(that.passed) : that.passed == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (marks != null ? marks.hashCode() : 0);
        result = 31 * result + (passed != null ? passed.hashCode() : 0);
        return result;
    }
}
