import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

class CarRentalSystem {

    private final Map<String, String> rentedCars = new HashMap<>(100, 0.5f);
    private final Map<String, RentedCars> rentedCarsByOwner = new HashMap<>(100, 0.5f);

    private static String getPlateNo(Scanner sc) {
        System.out.println("Enter the registration number:");
        return sc.nextLine();
    }

    private static String getOwnerName(Scanner sc) {
        System.out.println("Enter the name of the owner:");
        return sc.nextLine();
    }

    private boolean isCarRent(String plateNo) {
        return rentedCars.containsKey(plateNo);
    }

    private String getCarRent(String plateNo) {
        return rentedCars.getOrDefault(plateNo, "Car " + plateNo + " has not been rented.");
    }

    private void rentCar(String plateNo, String ownerName) {
        if (isCarRent(plateNo)) {
            System.out.println("Car " + plateNo + " has already been rented by " + getCarRent(plateNo) + ".");
        } else {
            RentedCars rentedCarsCheck = rentedCarsByOwner.get(ownerName);
            if (rentedCarsCheck == null) {
                rentedCarsCheck = new RentedCars();
                rentedCarsByOwner.put(ownerName, rentedCarsCheck);
            }

            rentedCars.put(plateNo, ownerName);
            rentedCarsCheck.add(plateNo);
            System.out.println("Car " + plateNo + " has been rented successfully.");
        }
    }

    private void returnCar(String plateNo) {
        String ownerName = rentedCars.get(plateNo);
        if (ownerName != null) {
            rentedCars.remove(plateNo);
            RentedCars rentedCarsToRemoveFrom = rentedCarsByOwner.get(ownerName);
            rentedCarsToRemoveFrom.remove(plateNo);
            System.out.println("Car " + plateNo + " rented by " + ownerName + " has been returned.");
        } else {
            System.out.println("Car not found. No changes were made.");
        }
    }

    private int totalCarsRented() {
        return rentedCars.size();
    }

    private void getCarsNo(String ownerName) {
        RentedCars rentedCarsToGetSizeOf = rentedCarsByOwner.get(ownerName);
        if (rentedCarsToGetSizeOf != null) {
            System.out.println(rentedCarsToGetSizeOf.size());
        } else {
            System.out.println(ownerName + " has not rented any cars.");
        }
    }

    private void getCarsList(String ownerName) {
        RentedCars rentedCarsToShow = rentedCarsByOwner.get(ownerName);
        if (rentedCarsToShow != null) {
            System.out.println(rentedCarsToShow.showCars());
        } else {
            System.out.println(ownerName + " has not rented any cars.");
        }
    }

    private static void printCommandsList() {
        System.out.println("help         - Displays this list of commands");
        System.out.println("add          - Adds a new pair (car, driver)");
        System.out.println("check        - Checks if a car has already been rented");
        System.out.println("remove       - Removes an existing car from the hashtable");
        System.out.println("getOwner     - Displays the current owner of the car");
        System.out.println("totalRented  - Displays the number of cars rented");
        System.out.println("getCarsNo    - Displays the number of cars rented by a specific owner");
        System.out.println("getCarsList  - Displays the list of cars rented by a specific owner");
        System.out.println("quit         - Closes the application");
    }

    public void run(Scanner sc) {
        boolean quit = false;

        while (!quit) {
            String command = sc.nextLine();

            switch (command) {
                case "help":
                    printCommandsList();
                    break;
                case "add":
                    String plateNoAdd = getPlateNo(sc);
                    String ownerName = getOwnerName(sc);
                    rentCar(plateNoAdd, ownerName);
                    break;
                case "check":
                    String plateNoCheck = getPlateNo(sc);
                    String ownerCheck = getCarRent(plateNoCheck);
                    System.out.println(isCarRent(plateNoCheck)
                            ? "Car " + plateNoCheck + " has been rented by " + ownerCheck + "."
                            : "The searched car has not been rented.");
                    break;
                case "remove":
                    String plateNoRemove = getPlateNo(sc);
                    returnCar(plateNoRemove);
                    break;
                case "getOwner":
                    String plateNoGetOwner = getPlateNo(sc);
                    System.out.println(getCarRent(plateNoGetOwner));
                    break;
                case "totalRented":
                    System.out.println(totalCarsRented());
                    break;
                case "getCarsNo":
                    getCarsNo(getOwnerName(sc));
                    break;
                case "getCarsList":
                    getCarsList(getOwnerName(sc));
                    break;
                case "quit":
                    System.out.println("Closing the application...");
                    quit = true;
                    break;
                default:
                    System.out.println("Unknown command. Please try again.");
                    printCommandsList();
                    break;
            }
        }
    }
}


class RentedCars {
    private final List<String> rentedCarsByOwnerList;

    public RentedCars() {
        rentedCarsByOwnerList = new ArrayList<>();
    }

    public void add(String plateNo) {
        rentedCarsByOwnerList.add(plateNo);
    }

    public void remove(String plateNo) {
        rentedCarsByOwnerList.remove(plateNo);
    }

    public int size() {
        return rentedCarsByOwnerList.size();
    }

    public String showCars() {
        return rentedCarsByOwnerList.toString();
    }
}

public class Main {

    public static void main(String[] args) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("resources/input"));
        } catch (FileNotFoundException e) {
            System.out.println("Input file not found. Reading from console.");
            scanner = new Scanner(System.in);
        }
        CarRentalSystem carRentalSystem = new CarRentalSystem();
        carRentalSystem.run(scanner);
        scanner.close();
    }
}