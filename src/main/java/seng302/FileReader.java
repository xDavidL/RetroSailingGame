package seng302;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class for reading files
 */
class FileReader {


    /**
     * Method to return an ArrayList of boats to compete in a race
     * @param fileName The name of the file with a list of boats
     * @return an ArrayList of Boat objects.
     */
    ArrayList<Boat> readBoatListFile(String fileName) {
        ArrayList<Boat> boatList = new ArrayList<>();
        String line;
        String csvSplitBy = ",";

        try (BufferedReader b = new BufferedReader(new java.io.FileReader(fileName))) {
            while ((line = b.readLine()) != null) {
                String[] boatInfo = line.split(csvSplitBy);

                // Check next boat does not have the same name as any other in the file
                boolean duplicateName = false;

                for (Boat boat : boatList) {
                    if (boatInfo[0].equals(boat.getBoatName())) {
                        System.out.printf("There is a boat with the name '%s' in the list already.\n" +
                                        "The boat '%s %s' has not been added to the race\n",
                                boatInfo[0], boatInfo[0], boatInfo[1]);
                        duplicateName = true;
                    }
                }
                if (duplicateName == false) {
                    boatList.add(new Boat(boatInfo[0], boatInfo[1])); //files take form: boatName,teamName
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return boatList;
    }
}