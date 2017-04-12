package seng302.team18.test_mock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import seng302.team18.model.Coordinate;
import seng302.team18.util.GPSCalculations;

/**
 * Class to parse race data from a CSV.
 * Data is to be used to create Regatta.xml.
 * CSV should have lines in the format of:
 * Regatta ID, Regatta Name, Course Name, UTC, Magnetic Variation, Race ID, Race Type, Race Start Time, top left Lat, top left long, top right Lat, top right long, bottom right Lat, bottom right long, bottom left Lat, bottom left long
 */
public class LocationCSVParser {

    /**
     * Reads CSV file and uses information to create Reggata.xml
     * @param file CSV file
     */
    public static void ParserCSV(File file) {
        //get number of lines
        int numRaces = 0;
        try {
            numRaces = getNumRaces(file);
            System.out.println();
        }catch (Exception e){
        }

        //chose a random line
        Random rand = new Random();
        int  lineNum = rand.nextInt(numRaces);

        //get chosen line
        String location = "oh no";
        try {
            location = getLine(lineNum, file);
        }catch (Exception e){
        }

        //create Regatta.xml
        makeRegatta(location);
    }

    //gets the number of lines in a file
    private static int getNumRaces(File file) throws IOException{
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String input;
        int count = 0;
        while((input = bufferedReader.readLine()) != null)
        {
            count++;
        }
        return count;
    }

    //gets a specific line from a file
    private static String getLine(int lineNum, File file)throws IOException{
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line = bufferedReader.readLine();
        int count = 0;
        while(count < lineNum)
        {
            line = bufferedReader.readLine();
            count++;
        }
        return line;
    }

    //creates the regatta.xml file
    private static void makeRegatta(String location){
        //split the line into its items
        List<String> locationsInfoList = Arrays.asList(location.split(", "));
        String regattaID = locationsInfoList.get(0);
        String regattaName = locationsInfoList.get(1);
        String courseName = locationsInfoList.get(2);
        String UTC = locationsInfoList.get(3);
        String magneticVariation = locationsInfoList.get(4);
        String raceID = locationsInfoList.get(5);
        String raceType = locationsInfoList.get(6);
        String raceStartTime = locationsInfoList.get(7);
        double latTL = new Double(locationsInfoList.get(8));
        double longTL = new Double(locationsInfoList.get(9));
        double latTR = new Double(locationsInfoList.get(10));
        double longTR = new Double(locationsInfoList.get(11));
        double latBR = new Double(locationsInfoList.get(12));
        double longBR = new Double(locationsInfoList.get(13));
        double latBL = new Double(locationsInfoList.get(14));
        double longBL = new Double(locationsInfoList.get(15));

        //Create Co-ordinates
        Coordinate topleft = new Coordinate(latTL, longTL);
        Coordinate topRight = new Coordinate(latTR, longTR);
        Coordinate bottomRight = new Coordinate(latBR, longBR);
        Coordinate bottomLeft = new Coordinate(latBL, longBL);

        //Create Co-ordinates list
        List<Coordinate> locationsCoordinates = new ArrayList();
        locationsCoordinates.add(topleft);
        locationsCoordinates.add(topRight);
        locationsCoordinates.add(bottomRight);
        locationsCoordinates.add(bottomLeft);

        //get centre co-ordinate for XML
        Coordinate center = GPSCalculations.getCentralCoordinate(locationsCoordinates);

//        System.out.println(locationsCoordinates);
//        System.out.println(center.getLatitude() + "  " + center.getLongitude());

    }

}
