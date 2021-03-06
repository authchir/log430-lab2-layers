package ca.etsmtl.log430.lab2;

import ca.etsmtl.log430.lab2.entities.Delivery;
import ca.etsmtl.log430.lab2.entities.Driver;
import ca.etsmtl.log430.lab2.management.ConflictingDeliveryException;
import ca.etsmtl.log430.lab2.management.DeliveriesManagement;
import ca.etsmtl.log430.lab2.management.DeliveryDoesNotExistException;
import ca.etsmtl.log430.lab2.management.DriverAlreadyAssignedException;
import ca.etsmtl.log430.lab2.management.DriverDoesNotExistException;
import ca.etsmtl.log430.lab2.management.DriverManagement;
import ca.etsmtl.log430.lab2.management.DriverScheduleFullException;

/**
 * Main class for assignment 2 for LOG430, Architecture logicielle.
 * 
 * <pre>
 * <b>Pseudo Code:</b>
 * 
 *   Instantiate lists
 *   do until done
 *     Present Menu
 *     if user choice = 1 then list drivers
 *     if user choice = 2 then list deliveries
 *     if user choice = 3 then
 *        list driver
 *        ask user to select a driver (by ID)
 *        list deliveries assigned to that driver today
 *     endif
 *     if user choice = 4 then
 *        list deliveries
 *        ask user to select a delivery (by ID)
 *        list driver assigned to that delivery today
 *     endif
 *     if user choice = 5 then
 *        list drivers
 *        ask user to select a driver(by ID)
 *        list deliveries
 *        ask user to select a delivery (by ID)
 *        assign delivery to driver (and vice versa)
 *     endif
 *     if user choice = x then you are done
 *   end do
 * </pre>
 * 
 * @author A.J. Lattanze, CMU
 * @version 1.4, 2012-May-31
 */

/*
 * Modification Log
 * **************************************************************************
 * v1.4, R. Champagne, 2012-May-31 - Various refactorings for new lab.
 * 
 * v1.3, R. Champagne, 2012-Feb-02 - Various refactorings for new lab.
 * 
 * v1.2, 2011-Feb-02, R. Champagne - Various refactorings, javadoc comments.
 * 
 * v1.1, 2002-May-21, R. Champagne - Adapted for use at ETS.
 * 
 * v1.0, 12/29/99, A.J. Lattanze - Original version.
 * **************************************************************************
 */

public class DriverAssignment {

	public static void main(String argv[]) {

		if (argv.length != 2) {
			System.out.println("\n\nIncorrect number of input parameters -" + " correct usage:");
			System.out.println("\njava DriverAssignment <delivery file name>" + " <driver file name>");
		} else {

			// Declarations:

			boolean done = false; // Loop invariant
			char userChoice; // User's menu choice
			Delivery delivery = null; // A delivery object
			Driver driver = null; // A driver object

			// Instantiates a menu object
			Menus menu = new Menus();

			// Instantiates a display object
			Displays display = new Displays();

			/*
			 * The following instantiations create a list of deliveries and
			 * drivers. The pathname for the file containing course information
			 * is passed to the main program on the command line as the first
			 * argument (argv[0]). The pathname for the file containing teacher
			 * information is passed to the main program on the command line as
			 * the second argument (argv[1]). An example driver file and course
			 * file is provided as drivers.txt and deliveries.txt
			 */

			/*
			DeliveryReader deliveriesList = new DeliveryReader(argv[0]);
			DriverReader driversList = new DriverReader(argv[1]);

			if ((deliveriesList.getListOfDeliveries() == null) || (driversList.getListOfDrivers() == null)) {
				System.out.println("\n\n *** The course list and/or the teacher" + " list was not initialized ***");
				done = true;
			} else {
				done = false;
			} // if
		 	*/
			
			try {
				DeliveriesManagement.loadData(argv[0]);
				DriverManagement.loadData(argv[1]);
			} catch (Exception e) {
				System.err.println(e.getMessage());
				System.exit(-1);
			}

			while (!done) {

				userChoice = menu.mainMenu();
				switch (userChoice) {

				case '1':
					display.displayDriverList(DriverManagement.getDriversList());
					break;

				case '2':
					display.displayDeliveryList(DeliveriesManagement.getListOfDeliveries());
					break;

				case '3':
					display.displayDriverList(DriverManagement.getDriversList());
					driver = DriverManagement.findDriverByID(menu.readDriverID());
					
					if (driver != null) {
						display.displayDeliveriesAssignedToDriver(driver);
					} // if
					break;

				case '4':
					display.displayDeliveryList(DeliveriesManagement.getListOfDeliveries());
					delivery = DeliveriesManagement.getDeliveryById(menu.readDeliveryID());

					if (delivery != null) {
						display.displayDriversAssignedToDelivery(delivery);
					} // if
					break;

				case '5':
					display.displayDriverList(DriverManagement.getDriversList());
					driver = DriverManagement.findDriverByID(menu.readDriverID());

					if (driver != null) {
						display.displayDeliveryList(DeliveriesManagement.getUnassignedDeliveriesList());
						delivery = DeliveriesManagement.getDeliveryById(menu.readDeliveryID());
						
						try {
							DeliveriesManagement.assignDriver(delivery, driver);
						} catch (DriverAlreadyAssignedException e) {
							display.displayError("This delivery has already been assigned. Cannot assign driver.");
						} catch (DeliveryDoesNotExistException e) {
							display.displayError("This delivery does not exist.");
						} catch (DriverDoesNotExistException e) {
							display.displayError("This driver does not exist.");
						} catch (ConflictingDeliveryException e) {
							display.displayError("A delivery already assigned to this driver conflicts with the selected delivery.");
						} catch (DriverScheduleFullException e) {
							display.displayError("The driver's schedule is already full, or this delivery will exceed the maximum amount of delivery time.");
						}
						
					} // if

					break;

				case '6':
					display.displayDriverList(DriverManagement.getDriversList());
					driver = DriverManagement.findDriverByID(menu.readDriverID());
					
					if (driver != null) {
						display.displayDeliveryList(driver.getDeliveriesMadeList());
					}
					
					break;

				case '7':
					display.displayDeliveryList(DeliveriesManagement.getUnassignedDeliveriesList());
					break;

				case 'X':
				case 'x':
					done = true;
				} // switch
			} // while
		} // if
	} // main
} // Class