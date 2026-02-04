
/*
 * Brody Stewart, Software Development I 202620, February 4th 2026
 * Library Management System
 * The LMS (Library Management System) class is designed to store basic patron information of a local library.
 * It stores the patron's name, address and unpaid fines. These patrons are saved to an array as objects containing said information.
 * Each patron is also assigned an ID by the program, making it easier to find their specific information when searching for their ID.
 * Via a console dialogue menu, the user is able to do many things with this system
 * They can add a patron manually or by text file. They can remove a patron by their ID.
 * They can search for a patron's specific information by ID or name.
 * If they search by name, it will list the information of every patron with the same name.
 * Finally, the user can display all patron information in the system.
 *
 * Below is the text file and other formatting rules:
 * This text file can hold multiple patrons.
 * It must be formatted as "FirstName LastName-Address-Fines", separated by line for each patron.
 * Addresses must be between 5 and 100 characters and fines need to be between $0 and $250.
 */

import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFileChooser;
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.filechooser.FileNameExtensionFilter;

public class LMS {
    static ArrayList<Patron> patrons = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);
    static int highestPatronID = 0;
    static boolean running = false;
    /*
     * This is the main method. It calls the main menu on loop so that it will not allow the user to reach a dead-end.
     * When returning, it clears the console a little bit and waits for the user to press enter to display again.
     */
    public static void main(String[] args) {
        running = true;
        while (running){
            mainMenu();
            if (running){
                System.out.println("\n\nPress enter to return to the main menu.");
                String timeStopper = scanner.nextLine();
                System.out.println("\n".repeat(5));
            }
        }
        scanner.close();
    }

    /*
     * This is the main menu method. It's basic objective is to give the user options in how they interact with the program, displaying and handling them.
     * This method displays a main menu with numbered choices. When the user inputs one of these choices, they do a specific thing. These are described below.
     * After each task is completed, it returns to the main method and is called again.
     * If an incorrect input is put in, it alerts the user and re-calls mainMenu.
     * If the user types 'quit', it tells the user that the system is closing and it calls a System exit command. This ends the program.
     *
     * Dialogue choices:
     * For most prompts, the user is alerted that if they type 'exit' they will be brought back this main menu dialogue
     * 1: Adds a patron manually by prompting the user for the first name, last name, address and fine amount. It ensures these values are correctly formatted.
     * 2: Adds a patron via a text file. Brings up a file explorer GUI, only accepting 'txt' files.
     * 	  Scans the file for patron information, skipping lines that aren't correctly formatted as described at the top of this file. Does this by calling fileReader.
     * 3: Removes a patron with the matching ID given during a prompt by the user. If no matching ID, tells the user the patron does not exist.
     * 4: Allows the user to search for a specific patron and print its information. They are prompted if they'd like to search by ID or name.
     * 	  If by name, they are able to give a name and display all information of every patron of the same name.
     * 	  If by ID, they are able to see the specific patron's information with that ID.
     * 	  If either name or ID are incorrectly input, it alerts the user and asks them to try again.
     * 5: Goes through the entire patrons array, printing every patron's information. If no patrons in the array, alerts the user and returns to the main menu.
     */
    private static void mainMenu() {
        String choice;
        String input;

        System.out.println("\tThe Forest Library Management System");
        System.out.println("1. Add a patron");
        System.out.println("2. Add a patron by text file");
        System.out.println("3. Remove a patron");
        System.out.println("4. Find patron");
        System.out.println("5. Display all patrons");
        System.out.println("Enter 'quit' to leave the program.\n");
        System.out.print("Please enter the number of your choice: ");

        choice = scanner.nextLine();
        switch (choice) {
            case "1": // Add a patron manually
                System.out.println("Adding Patron. You may enter 'exit' at any prompt to return to the main menu.");
                String name = promptForName();
                if (name.equals("exit")) return;
                String address = promptForAddress();
                if(address.equals("exit"))  return;
                double fines = promptForFines();
                if(fines == -1)  return;
                addPatron(name, address, fines);
                break;
            case "2": // Add a Patron by File
                System.out.println("Adding Patron by file. Please ensure that the file is a text file and each new entry is on a separate line.");
                System.out.println("Format: Name-Address-Fines");
                fileReader();
                break;
            case "3": // Remove a patron
                int id;
                System.out.println("Removing Patron. You may enter 'exit' at any prompt to return to the main menu.");
                id = promptForID();
                if (id == -1) return; // Exit signal
                removePatron(id);
                break;
            case "4": // Display info using a given name or ID.
                boolean nameSearch = false;
                System.out.println("Type 1 if you'd like to search by Name. Otherwise, it will search by ID.");
                input = scanner.nextLine();
                if (input.equals("1")) {
                    nameSearch = true;
                }

                if (nameSearch) { // Search by Name
                    String searchName;
                    System.out.println("Enter a patron's name to find their information. You may enter 'exit' at any prompt to return to the main menu.");
                    searchName = promptForName();
                    if (searchName.equals("exit")) return;
                    displayInfo(searchName);
                }else { // If they search by ID instead
                    int searchId;
                    System.out.println("Enter a patron's ID to find their information. You may enter 'exit' at any prompt to return to the main menu.");
                    searchId = promptForID();
                    if (searchId == -1) return;
                    displayInfo(searchId);
                }
                break;
            case "5": // Display All Information
                System.out.println("Displaying all patron information.");
                displayAll();
                // displayAll();
                break;
            case "quit":
                System.out.println("System closing, have a good day.");
                running = false;
                break;
            default:
                System.out.println("\n".repeat(5));
                System.out.println("\nInvalid selection. Please try again.\n");
                mainMenu();
                break;
        }
    }

    /*  This is the promptForID method.
     *   It calls for the ID from the user, returning it as an integer.
     *   It first prompts for the ID, ensuring it's a number.
     *   It converts this to an integer, returning it.
     */
    private static int promptForID(){
        String input;
        int id;
        while (true) {
            System.out.print("Please enter the patron's ID number: ");
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) return -1;
            try {
                id = Integer.parseInt(input);
                return id;
            } catch (NumberFormatException e) {
                System.out.println("Error. Entry isn't a number. Please try again.");
            }
        }
    }

    /*  This is the promptForName method.
     *   It calls for the name from the user, returning it as a formatted string.
     *   It first prompts for the first name, ensuring its valid (no spaces and longer than 1 character).
     *   Then, it prompts using a similar system for the last name. It then combines the two into one string, split by a space.
     *   It returns this string.
     */
    private static String promptForName(){
        String input;
        String name;
        while (true) { // Get First Name
            System.out.print("Enter Patron's First Name: ");
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) return "exit";
            if (!input.contains(" ") && input.length() > 1) {
                name = input;
                break;
            }
            System.out.println("Invalid format. No spaces allowed. Please retry.");
        }
        while (true) { // Get Last Name
            System.out.print("Enter Patron's Last Name: ");
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) return "exit";
            if (!input.contains(" ") && input.length() > 2) {
                name = name + " " + input;
                return name;
            }
            System.out.println("Invalid format. No spaces allowed. Please retry.");
        }
    }

    /*  This is the promptForAddress method.
     *   It calls for the address from the user, returning it as a formatted string.
     *   It first prompts for the address, ensuring its valid (between 5 and 101 characters).
     *   If valid, it returns the string.
     */
    private static String promptForAddress(){
        String input;
        String address;
        while (true) { // Get Address
            System.out.println("Enter Address (5-100 characters): "); // 5-100 characters so even the shortest and longest addresses are covered.
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) return "exit";
            address = input;
            if (address.length() > 5 && address.length() <= 100)
                return address;
            System.out.println("Invalid format. Not a real address. Please retry.");
        }
    }

    /*  This is the promptForFines method.
     *   It calls for the unpaid fines from the user, returning it as a double.
     *   It first prompts for the fines, ensuring its valid (a number and $0-250).
     *   If valid, it returns it as a double.
     */
    private static double promptForFines(){
        String input;
        double fines;
        while (true) { // Get Fines
            System.out.print("Enter Unpaid Fines ($0-$250): ");
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) return -1;
            try {
                fines = Double.parseDouble(input);
                if (fines >= 0 && fines <= 250) return fines;
                System.out.println("Error. Fine must be $0-$250. Please try again.");
            } catch (NumberFormatException e) {
                System.out.println("Error. Entry isn't a number. Please try again.");
            }
        }
    }

    /* This is the addPatron method. It takes two strings and an integer.
     *  The first string is the name of the patron. The second is the patron's address. The double is their unpaid fines amount.
     *  Sets the id to the next highest patron id, based on the highestPatronID value.
     *  This program will not fill in missing IDs (1,2,3,5,8,9) to ensure there is no confusion with past users.
     *  Creates a new patron with this information and adds it to the patrons array.
     */
    private static void addPatron(String name, String address, double fine) {
        highestPatronID++;
        int id = highestPatronID;
        Patron patron = new Patron(id, name, address, fine);
        patrons.add(patron);
        System.out.println("Successfully added " + name + ".");
    }

    /* This is the file reader method. It reads a given text file.
     * This creates a simple file chooser GUI. It allows the user to select a specific text file from their computer.
     * It does this using JFileChooser and limits the file extension with FileNameExtensionFilter.
     *
     * Once a user has selected a file, it uses scanner to read through each line in the following steps:
     * 0. Skips empty lines.
     * 1. Read a line.
     * 2. Parse the line, splitting it where '-' is found. Store each broken string in the data array.
     * If the data array doesn't contain three strings (name, address, fine), it alerts the user and skips the line.
     * 3. Checks if the first string, name, is formatted correctly (is larger than 2 characters and contains a space).
     * If not, skips the line.
     * 4. Checks if the second string, address, is formatted correctly (is larger than 5 characters and less than 100).
     * If not, skips the line.
     * 5. Converts the third string, fine, into a double and checks if it's between 0-250 dollars.
     * If it isn't able to be converted into an integer, tells the user and skips the line.
     * If it isn't between 0 and 250 dollars, tells the user and skips the line.
     * 6. If all is correct, calls the addPatron method with the data and adds one to count.
     *
     * After everything is completed, it tells the user the total number of patrons added.
     * If it had an issue reading or accessing the file, it alerts the user and returns to main menu.
     */
    private static void fileReader() {
        javax.swing.JFrame tempFrame = new javax.swing.JFrame();
        tempFrame.setAlwaysOnTop(true);
        tempFrame.setVisible(false); // I know it said we don't have to use a GUI, but I think this is just a cleaner way to find the text file. Plus it's not that hard.

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Patron Text File");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files (*.txt)", "txt"); // Ensure the file is a text file by forcing them to choose one.
        fileChooser.setFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(false);
        int userChoice = fileChooser.showOpenDialog(tempFrame);
        // Dialogue prompt is set up, moving on to file handling.

        if (userChoice == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            System.out.println("Reading: " + file.getName());

            try (Scanner fileScanner = new Scanner(file)){
                int count = 0;
                while(fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    if (line.trim().isEmpty()) continue;

                    String[] data = line.split("-");
                    if(data.length == 3) {
                        double fine;
                        String name = data[0].trim();
                        if (name.length() < 2 || !name.contains(" ")) {
                            System.out.println("Error: Name is too short. Ensure it's formatted as Firstname Lastname on line '" +  line + "'. Skipping line.");
                            continue;
                        }

                        String address = data[1].trim();
                        if (address.length() < 5 || address.length() > 250) {
                            System.out.println("Error: Address is incorrect. Ensure it's 5-250 characters on line '" +  line + "'. Skipping line.");
                            continue;
                        }

                        try {
                            fine = Double.parseDouble(data[2].trim());
                            if(fine < 0 || fine > 250) {
                                throw new IllegalArgumentException("Fine out of range ($0-250)"); // Throws an error if it's not between 0-250.
                            }
                            addPatron(name, address, fine);
                            count++;
                        }catch (NumberFormatException e) {
                            System.out.println("Error: Fine is not a number in line '" +  line + "'. Skipping line.");
                        } catch (IllegalArgumentException e) {
                            System.out.println("Error: Fine is not between $0-250 in line '" +  line + "'. Skipping line.");
                        }
                    } else {
                        System.out.println("Error: Incorrect patron format in line '" +  line + "'. Skipping line.");
                    }
                }
                System.out.println("Successfully imported " + count + " patrons.");
            } catch (FileNotFoundException e) {
                System.out.println("Error: No file found.");
            }
        }else System.out.println("File reading cancelled. Returning.");
        tempFrame.dispose();
    }

    /* This is the removePatron method. It takes an integer parameter.
     * It takes a patron's Id as an integer and searches the entire patrons array for the patron with the matching ID.
     * When found, alerts the user this patron will be removed and removes them.
     * If none are found, alerts the user and returns to main menu.
     */
    private static void removePatron(int id) {
        for (int i = 0; i< patrons.size(); i++){
            if (patrons.get(i).getPatronId() == id) {
                System.out.println("Successfully removed patron " + id + ": " + patrons.get(i).getPatronName());
                patrons.remove(i);
                return;
            }
        }
        System.out.println("No Patron with the ID found. Exiting.");
    }

    /* This is one of the displayInfo methods. This takes an integer.
     * It takes a patron's Id as an integer and searches the entire patrons array for the patron with the matching ID.
     * When found, it prints the patron's information.
     * If none are found, alerts the user and returns to main menu.
     */
    private static void displayInfo(int id) {
        for (Patron p : patrons) { // Get the next highest patron id.
            int patronId = p.getPatronId();
            if (id == patronId) {
                p.print();
                return;
            }
        }
        System.out.println("No Patron with ID found. Exiting.");
    }

    /* This is one of the displayInfo methods. This takes a string.
     * It takes a patron's name as string and searches the entire patrons array for any patrons with the matching name.
     * When found, it prints the patron's information and keeps going until it reaches the end of the arraylist.
     * This means it shows all patrons with the matching name.
     * If none are found, alerts the user and returns to main menu.
     */
    private static void displayInfo(String name) {
        boolean found = false;
        for (Patron p : patrons) { // Get the next highest patron id.
            String patronName = p.getPatronName();
            if (name.equals(patronName)) {
                p.print();
                found = true;
            }
        }
        if (!found) System.out.println("No Patron with name found. Exiting.");
    }

    /* This is the displayAll method. It simply displays all patrons' information.
     * It runs a for each loop through the entire patrons array, printing out the information of every patron.
     * If the patrons array has nothing in it, alerts the user and returns to main menu.
     */
    private static void displayAll() {
        if(patrons.size() <= 0) {
            System.out.println("Error. No patrons in the system. Exiting.");
            return;
        }
        for (Patron p : patrons) {
            p.print();
        }
    }
}


