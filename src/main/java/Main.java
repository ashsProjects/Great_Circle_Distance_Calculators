import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.Random;

public class Main {
    private static Places places = new Places();

    private static void checkCoords(Place place, int i) throws Exception {
        if (place.latRadians() < (-0.5 * Math.PI) || place.latRadians() > (0.5 * Math.PI))
            throw new Exception(String.valueOf(i));
        if (place.lonRadians() < (-1.0 * Math.PI) || place.lonRadians() > (Math.PI))
            throw new Exception(String.valueOf(i));
    }

    private static void calculateDistances(Scanner scan) {
        GreatCircleDistance distanceCalculator;
        double earthRadius = 3959.0; //miles
        Distances distances = new Distances();

        while (true) {
            try {
                System.out.print("Choose the formula to use\n"
                + "-Enter 1 or nothing for Vincenty\n"
                + "-Enter 2 for Haversine\n"
                + "-Enter 3 for Cosines\n"
                + ">>> ");

                String formula = scan.nextLine().trim();
                FormulaFactory factory = FormulaFactory.getInstance();
                distanceCalculator = factory.get(formula);

                System.out.print("Enter the radius to use (enter nothing for miles)\n>>> ");
                String input = scan.nextLine().trim();
                if (!input.equals("")) earthRadius = Double.parseDouble(input);
                break;
            } catch (NumberFormatException nfe) { 
                System.out.println("Invalid radius! Try again");
                continue;
            } catch (Exception e) {
                System.out.println("Invalid formula! Try again");
                continue;
            }
        }

        for (int i = 0; i < places.size(); i++) {
            Place from = places.get(i);
            Place to = places.get((i + 1) % places.size());
            double distance = distanceCalculator.between(from, to, earthRadius);

            StringBuilder sb = new StringBuilder();
            sb.append("Distance from (" + from.toString() + ") to (" + to.toString() + ")");

            distances.put(sb.toString(), distance);
        }

        try (FileWriter writer = new FileWriter(new File("output.txt"), false)) {
            writer.write(distances.toString());
            writer.write(String.format("\nParameters: (Num Places: %d, Formula: %s, Radius: %.2f)", places.size(), distanceCalculator.toString(), earthRadius));
            System.out.println("Successfully wote outputs to ./output.txt!");
            return;
        } catch (IOException ioe) {
            System.out.println("There was an error when writing the output! Exiting");
            System.exit(1);
        }
    }

    private static void runFromFile(Scanner scan, String fileName) {
        if (fileName == null) {
            System.out.print("Enter the name of the file to read from\n>>> ");
            fileName = scan.nextLine();
        }

        try (Scanner fileScan = new Scanner(new File(fileName))) {
            int i = 1;
            while (fileScan.hasNextLine()) {
                String[] data = fileScan.nextLine().split(",");

                Place place = null;
                if (data.length == 2) place = new Place(data[0].trim(), data[1].trim());
                else if (data.length == 3) place = new Place(data[0], data[1].trim(), data[2].trim());

                checkCoords(place, i);
                places.add(place);
                i++;
            }
            calculateDistances(scan);
        } catch (FileNotFoundException fe) {
            System.out.println("File not found. Exiting!");
            System.exit(1);
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid lat/lon found in file! Make sure the lat and lon are valid numbers");
            System.exit(1);
        } catch (ArrayIndexOutOfBoundsException ae) {
            System.out.println("Invalid input found in file! Make sure the entries are in the format 'lat, lon'");
            System.exit(1);
        } catch (Exception e) {
            System.out.println("Coordinates out of bounds on line " + e.getMessage());
        }
    }

    private static void runFromShell(Scanner scan) {
        System.out.print("How many places do you want to use?\n>>> ");
        int numPlaces = 0;

        while (true) {
            try {
                numPlaces = Integer.parseInt(scan.nextLine());
                break;
            } catch (NumberFormatException nfe) {
                System.out.println("Invalid input! Please enter an integer value");
                continue;
            }
        }

        int i = 1;
        while (i <= numPlaces) {
            System.out.print("Enter 'lat, lon' or 'name, lat, lon' for place " + i + "\n>>> ");
            try {
                String[] data = scan.nextLine().split(",");

                Place place = null;
                if (data.length == 2) place = new Place(data[0].trim(), data[1].trim());
                else if (data.length == 3) place = new Place(data[0], data[1].trim(), data[2].trim());

                checkCoords(place, i);
                places.add(place);
                i++;
            } catch (NumberFormatException nfe) {
                System.out.println("Invalid input! Please enter an valid lat and lon values, ex. (10, -89.2)");
                continue;
            } catch (ArrayIndexOutOfBoundsException ae) {
                System.out.println("Invalid input! Please enter two values for lat and lon, ex. (-8, 90)");
                continue;
            } catch (Exception e) {
                System.out.println("Invalid input! Longitude must be >= -180 and <= 180; Latitude must be >= -90 and <= 90");
                continue;
            }
        }

        calculateDistances(scan);
    }

    private static void generatePlaces(Scanner scan) {
        int numPlaces = 0;
        long randSeed = -1L;
        
        while (true) {
            try {
                System.out.print("Enter the number of places to generate\n>>> ");
                numPlaces = Integer.parseInt(scan.nextLine().trim());
                break;
            } catch (NumberFormatException nfe) {
                System.out.println("Invalid input! Please enter a valid number");
                continue;
            }
        }

        try {
            System.out.print("Enter the seed to use or press enter for none\n>>> ");
            randSeed = Long.parseLong(scan.nextLine().trim());
        } catch (NumberFormatException nfe) {
            System.out.println("Using no random seed");
        }

        Random rand = randSeed == -1 ? new Random(randSeed) : new Random();

        String s = "";
        try (FileWriter writer = new FileWriter(new File("places.txt"), false)) {
            for (int i = 1; i <= numPlaces; i++) {
                double lat = rand.nextInt(-90, 90) + rand.nextDouble();
                double lon = rand.nextInt(-180, 180) + rand.nextDouble();
                s += "place" + i + "," + String.format("%.3f", lat) + "," + String.format("%.3f", lon) + "\n";
            }
            writer.write(s);
        } catch (IOException ioe) {
            System.out.println("There was an error when creating the file! Exiting");
            System.exit(1);
        }

        System.out.println("Successfully created input file with " + numPlaces + " places! Continuining to distance calculation");
        runFromFile(scan, "places.txt");
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        while (true) {
            places.clear();
            System.out.print("\nChoose how you want to read in places: \n"
                + "-Enter 1 to read from command line\n"
                + "-Enter 2 to read from file\n"
                + "-Enter q to quit the program\n"
                + ">>> ");
            String readChoice = scan.nextLine();

            if (readChoice.equals("1")) {
                runFromShell(scan);
            } else if (readChoice.equals("2")) {
                System.out.print("Enter 1 to run from existing file\n"
                    + "Enter 2 to generate a new file then run\n"
                    + ">>> ");
                String generateChoice = scan.nextLine();

                if (generateChoice.equals("2")) generatePlaces(scan);
                else if (generateChoice.equals("1")) runFromFile(scan, null);
                else {
                    System.out.println("Invalid option!");
                    continue;
                }
            } else if (readChoice.equals("q")) {
                System.out.println("Exiting!");
                break;
            } else {
                System.out.println("Invalid option! Please enter 1, 2, or q");
                continue;
            }
        }

        scan.close();
    }
}