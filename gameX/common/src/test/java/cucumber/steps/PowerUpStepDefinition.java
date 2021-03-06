package cucumber.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import seng302.team18.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Step definitions for Power Up Features
 */
public class PowerUpStepDefinition {

    private Race race;
    private PickUp prototype;

    @Given("^a race has started$")
    public void a_race_has_started() throws Throwable {
        Coordinate boundary1 = new Coordinate(32.30502, -64.85857);
        Coordinate boundary2 = new Coordinate(32.30502, -64.85235);
        Coordinate boundary3 = new Coordinate(32.29925, -64.85235);
        Coordinate boundary4 = new Coordinate(32.29925, -64.85857);
        List<Coordinate> boundaries = new ArrayList<>();
        boundaries.add(boundary1);
        boundaries.add(boundary2);
        boundaries.add(boundary3);
        boundaries.add(boundary4);

        race = new Race();
        Course course = new Course(getCompoundMarks(), boundaries, getRoundings());
        race.setCourse(course);
    }

    @Given("^a power up is in the race$")
    public void a_power_up_is_in_the_race() throws Throwable {
        prototype = createPrototype();
        race.addPickUps(1, prototype, 10);
//        race.setUpdaters(Arrays.asList(new PowerUpUpdater(prototype, 1)));
        race.setStatus(RaceStatus.STARTED);

    }

    @When("^a power up expires$")
    public void a_power_up_expires() throws Throwable {
        Course course = race.getCourse();
        Assert.assertEquals(course.getPickUp(0).getId(), 0);
        Thread.sleep(11);
    }

    @Then("^the power up will disappear from the race\\.$")
    public void the_power_up_will_disappear_from_the_race() throws Throwable {
        Course course = race.getCourse();
        course.removeOldPickUps();
        Assert.assertNull(course.getPickUp(0));
    }

    @Then("^another power up will replace it\\.$")
    public void another_power_up_will_replace_it() throws Throwable {
        Course course = race.getCourse();
        race.removePickUps();
        race.addPickUps(1, prototype, 10);
        Assert.assertEquals(course.getPickUp(1).getId(), 1);
    }


    private List<CompoundMark> getCompoundMarks() {
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


    private List<MarkRounding> getRoundings() {
        List<MarkRounding> markRoundings = new ArrayList<>();

        markRoundings.add(new MarkRounding(1, getCompoundMarks().get(0), MarkRounding.Direction.SP, 3));
        markRoundings.add(new MarkRounding(2, getCompoundMarks().get(1), MarkRounding.Direction.PORT, 3));
        markRoundings.add(new MarkRounding(3, getCompoundMarks().get(2), MarkRounding.Direction.PS, 6));
        markRoundings.add(new MarkRounding(4, getCompoundMarks().get(3), MarkRounding.Direction.PS, 6));
        markRoundings.add(new MarkRounding(5, getCompoundMarks().get(0), MarkRounding.Direction.PS, 6));

        return markRoundings;
    }


    private PickUp createPrototype() {
        BodyMass mass = new BodyMass();
        mass.setWeight(0);
        mass.setRadius(12);

        PickUp pickUp = new PickUp(-1);
        pickUp.setBodyMass(mass);

        return pickUp;
    }
}
