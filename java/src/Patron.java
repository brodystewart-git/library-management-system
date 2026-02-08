/* Brody Stewart, Software Development I 202620, February 4th 2026
 * Patron
 * The Patron class is designed as an object class that stores the information of a library patron. This should be used with the Library Management System class.
 * Patrons contain an (int) id, (string) name, (string) address and (double) unpaid fines.
 * The constructor allows for the creation of a patron with these as a parameter.
 * It features getters for information that another program may need and a print function, that prints out the information in the format: id-name-address-fines.
 */

public class Patron {
    private final int patron_id;
    private final String patron_name;
    private final String patron_address;
    private final double patron_fines;

    /* Patron constructor method
     * This takes the patron's ID as an integer, the patron's name and address as a string and their unpaid fines as a double.
     * These are then simply assigned to patron_id, patron_name, patron_address and patron_fines to be used by the object.
     */
    public Patron(int id, String name, String address, double fines) {
        patron_id = id;
        patron_name = name;
        patron_address = address;
        patron_fines = fines;
    }

    public int getPatronId() {
        return patron_id;
    }

    public String getPatronName() {
        return patron_name;
    }

    public void print() {
        System.out.println(patron_id + "-" + patron_name + "-" + patron_address + "-$" + patron_fines);
    }
    public String toString(){
        return (patron_id + "-" + patron_name + "-" + patron_address + "-" + patron_fines);
    }
}
