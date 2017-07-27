package seng302.team18.test_mock.model;

import seng302.team18.model.*;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Builds a preset course. Smaller than the course from CourseBuilder1
 * <p>
 * Concrete implementation of AbstractCourseBuilder.
 *
 * @see seng302.team18.test_mock.model.AbstractCourseBuilder
 */
public class CourseBuilder2 extends AbstractCourseBuilder {

    @Override
    protected List<CompoundMark> buildCompoundMarks() {
        List<CompoundMark> compoundMarks = new ArrayList<>();

        // Start & Finish Line
        Mark mark1 = new Mark(231, new Coordinate(32.30269, -64.85787));
        mark1.setHullNumber("LC21");
        mark1.setStoweName("PRO");
        mark1.setShortName("S1");
        mark1.setBoatName("StartLine1");

        Mark mark2 = new Mark(232, new Coordinate(32.30159, -64.85847));
        mark2.setHullNumber("LC22");
        mark2.setStoweName("PIN");
        mark2.setShortName("S2");
        mark2.setBoatName("StartLine2");

        CompoundMark compoundMark0 = new CompoundMark("Start/Finish Line", Arrays.asList(mark1, mark2), 11);
        compoundMarks.add(compoundMark0);

        Mark mark3 = new Mark(233, new Coordinate(32.30182, -64.85397));
        mark3.setHullNumber("LC21");
        mark3.setStoweName("PRO");
        mark3.setShortName("S1");
        mark3.setBoatName("First Mark");

        CompoundMark compoundMark1 = new CompoundMark("First Mark", Arrays.asList(mark3), 12);
        compoundMarks.add(compoundMark1);

        Mark mark4 = new Mark(234, new Coordinate(32.30492, -64.85354));
        mark1.setHullNumber("LC21");
        mark1.setStoweName("PRO");
        mark1.setShortName("S1");
        mark1.setBoatName("Upwind1");

        Mark mark5 = new Mark(235, new Coordinate(32.30463, -64.85245));
        mark2.setHullNumber("LC22");
        mark2.setStoweName("PIN");
        mark2.setShortName("S2");
        mark2.setBoatName("Upwind2");

        CompoundMark compoundMark2 = new CompoundMark("Upwind", Arrays.asList(mark4, mark5), 13);
        compoundMarks.add(compoundMark2);

        Mark mark6 = new Mark(236, new Coordinate(32.29966, -64.85654));
        mark1.setHullNumber("LC21");
        mark1.setStoweName("PRO");
        mark1.setShortName("S1");
        mark1.setBoatName("Downwind1");

        Mark mark7 = new Mark(237, new Coordinate(32.29935, -64.85564));
        mark2.setHullNumber("LC22");
        mark2.setStoweName("PIN");
        mark2.setShortName("S2");
        mark2.setBoatName("Downwind2");

        CompoundMark compoundMark3 = new CompoundMark("Downwind", Arrays.asList(mark6, mark7), 14);
        compoundMarks.add(compoundMark3);

        return compoundMarks;
    }


    @Override
    protected List<BoundaryMark> getBoundaryMarks() {
        return new ArrayList<>();
    }


    @Override
    protected double getWindDirection() {
        return 200 ;
    }


    @Override
    protected double getWindSpeed() {
        return 30;
    }


    @Override
    protected ZoneId getTimeZone() {
        return ZoneId.ofOffset("UTC", ZoneOffset.ofHours(+12));
    }


    @Override
    protected List<MarkRounding> getMarkRoundings() {
        List<MarkRounding> markRoundings = new ArrayList<>();

        markRoundings.add(new MarkRounding(1, getCompoundMarks().get(0), MarkRounding.Direction.SP, 3));
        markRoundings.add(new MarkRounding(2, getCompoundMarks().get(1), MarkRounding.Direction.PORT, 3));
        markRoundings.add(new MarkRounding(3, getCompoundMarks().get(2), MarkRounding.Direction.PS, 6));
        markRoundings.add(new MarkRounding(4, getCompoundMarks().get(3), MarkRounding.Direction.PS, 6));
        markRoundings.add(new MarkRounding(5, getCompoundMarks().get(0), MarkRounding.Direction.PS, 6));

        return markRoundings;
    }
}