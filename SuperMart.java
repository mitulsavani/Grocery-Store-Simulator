package PJ3;

import java.util.*;
import java.io.*;
import PJ3.CheckoutArea;

// You may add new functions or data fields in this class 
// You may modify any functions or data members here
// You must use Customer, Cashier and CheckoutArea classes
// to implement SuperMart simulator

class SuperMart {

  // input parameters
  private int numCashiers, customerQLimit;
  private int chancesOfArrival, maxServiceTime;
  private int simulationTime;
  private int random;

  // statistical data
  private int numGoaway, numServed, totalWaitingTime;

  // internal data
  private int counter;	             // customer ID counter
  private CheckoutArea checkoutarea; // checkout area object
  private Scanner dataFile;	     // get customer data from file
  private Random dataRandom;	     // get customer data using random function
  
  
  // most recent customer arrival info, see getCustomerData()
  private boolean anyNewArrival;  
  private int serviceTime;

  Scanner input=new Scanner(System.in);
   
  
  // initialize data fields
  private SuperMart()
  {
      
	numCashiers=0;
        customerQLimit=0;
        chancesOfArrival=0;
        maxServiceTime=0;
        simulationTime=0;
        numGoaway=0;
        numServed=0;
        totalWaitingTime=0;
        counter=1;
        dataFile = new Scanner(System.in);
        anyNewArrival=false;
        serviceTime=0; 
    }

  private void setupParameters()
    {
	// read input parameters from users
        System.out.print("Enter simulation time(positive integer)       : ");
        simulationTime=input.nextInt();
        
        System.out.print("Enter the number of cahiers                   : ");
        numCashiers=input.nextInt();
        
        System.out.print("Enter chances (0% < & <= 100%) of new customer: ");
        chancesOfArrival=input.nextInt();
        
        System.out.print("Enter maximum service time of customer        : ");
        maxServiceTime=input.nextInt();
        
        System.out.print("Enter customer queue limit                    : ");
        customerQLimit=input.nextInt();
        
        System.out.print("Enter 0/1 to get data from random/file        : ");
        random=input.nextInt();
        
        System.out.println("Enter filename                                : DataFile");
        
        try
        {
            dataFile=new Scanner(new File("G:/SEMESTER 3/CSC 220/pj3-stud/pj3-stud/DataFile"));
        }
        catch(FileNotFoundException e)
        {
            System.out.println("File not recongnized");
        }
    }

  // Refer to step 1 in doSimulation()
  private void getCustomerData()
  {
	// get next customer data : from file or random number generator
	// set anyNewArrival and serviceTime
	// see Readme file for more info
	if(random==1)
        {
          anyNewArrival = (((dataFile.nextInt() % 100) + 1) <= chancesOfArrival);
          serviceTime = (dataFile.nextInt() % maxServiceTime) + 1;
        }
        
        
        
        else
        {
            anyNewArrival = ((dataRandom.nextInt(100)+1) <=chancesOfArrival);
            serviceTime= dataRandom.nextInt(maxServiceTime)+1;
        }
  }
  

  private void doSimulation()
    {
	System.out.println();
        System.out.println("******* START SIMULATION ********");
        System.out.println();
        System.out.println("Customer #1 to #"+counter+" is ready.....");
	// Initialize CheckoutArea
        checkoutarea=new CheckoutArea(numCashiers, customerQLimit);

	// Time driver simulation loop
  	for (int currentTime = 0; currentTime < simulationTime; currentTime++) {
            
            printCurrentTimeInterval(currentTime);

    		// Step 1: any new customer enters the checkout area?
    		getCustomerData();

    		if (anyNewArrival) 
                {

      		    // Step 1.1: setup customer data
                    Customer customer=new Customer(counter, serviceTime,currentTime);
                    
                    
      		    // Step 1.2: check customer waiting queue too long?
                        if(!checkoutarea.isCustomerQTooLong())
                        {
                            checkoutarea.insertCustomerQ(customer);
                            System.out.println("\tcustomer#"+counter+" arrives with checkout time "+serviceTime+ " units");
                            System.out.println("\tcustomer #"+counter+" wait in customer queue");
                        }
                        
                        else
                        {
                            System.out.println("customer Queue is full");
                        }
		    //           if customer queue is too long, update numGoaway
		    //           else goto customer queue
    		} else {
      		    System.out.println("\tNo new customer!");
    		}

    		// Step 2: free busy cashiers that are done at currenttime, add to free cashierQ
                freeBusyCashiers(checkoutarea, currentTime);
                
    		// Step 3: get free cashiers to serve waiting customers at currenttime
                if(!checkoutarea.emptyFreeCashierQ() && !checkoutarea.emptyCustomerQ())
                {
                    setFreeCashiersToBusy(checkoutarea, currentTime);
                    counter++;
                }
                
  	} // end simulation loop

  }

  private void printStatistics()
  {
      
	// add statements into this method!
	// print out simulation results
        System.out.println();
        System.out.println("*******************End of Simulation report***************** ");
        System.out.println();
        
        
	// see the given example in project statement
        // you need to display all free and busy gas pumps
        
        System.out.println("\t# of arrival customers    : " + (counter + numGoaway -1));
        System.out.println("\t# customer gone-away      : " + numGoaway);
        System.out.println("\t# customers served        : " + (counter - 1));

        System.out.println("\n\t*** Current Cashiers Info ***\n");
        System.out.println("\t# busy cashiers           : " + checkoutarea.sizeBusyCashierQ());
        System.out.println("\t# free cashiers           : " + checkoutarea.sizeFreeCashierQ());
        // need to free up all customers in queue to get extra waiting time.
        System.out.println("\n\tTotal waiting time       : "+totalWaitingTime);
        System.out.println("\tAverage waiting time      : "+totalWaitingTime/simulationTime);

        // need to free up all cashiers in queue to get extra free & busy time.
        System.out.println("\n\n\tBusy Cashiers Info:");
        
        while(!checkoutarea.emptyBusyCashierQ())
        {
          Cashier busyCashier = checkoutarea.removeBusyCashierQ();
          busyCashier.printStatistics();
        }

        System.out.println("\n\nFree Cashiers Info: ");
        
        while(!checkoutarea.emptyFreeCashierQ())
        {
        Cashier freeCashier = checkoutarea.removeFreeCashierQ();
        freeCashier.printStatistics();
        }
}

    
    private void freeBusyCashiers(CheckoutArea checkoutarea, int currentTime)
    {
        while(!checkoutarea.emptyBusyCashierQ())
        {
          //Peek at next cashier's customer to see if their service time is now done
          Cashier busyCashier = checkoutarea.peekBusyCashierQ();
          int busyCustomerEndServiceTime = getBusyCashiersCustomerEndServiceTime(busyCashier);

          if(busyCustomerEndServiceTime <= currentTime)
          {
            //If busy cashier is done serving customer, remove cashier from Queue
            busyCashier = checkoutarea.removeBusyCashierQ();
            System.out.println("\tCashier #" + busyCashier.getCashierID() + " is free");

            //Update Cashier's stats by changing availability as free
            Customer customer = busyCashier.endServeCustomer();
            System.out.println("\tCustomer #" + customer.getCustomerID() + " is done");

            //Place the now free cashier in the freeCashierQ
            checkoutarea.insertFreeCashierQ(busyCashier);
          }
          else
          {
            //If top cashier is not free (priority queue), end check
            break;
          }
        } 
    }
    
    private int getBusyCashiersCustomerEndServiceTime(Cashier cashier)
    {
        Customer busyCustomer=cashier.getCurrentCustomer();
        int arriveTime=busyCustomer.getArrivalTime();
        int serviceTime=busyCustomer.getServiceTime();
        return (arriveTime+serviceTime);
    }
    
    private void setFreeCashiersToBusy(CheckoutArea checkoutarea, int currentTime)
    {
        System.out.println("\tcustomer#"+counter+"  gets a cashier");
        
        Cashier cashier=checkoutarea.removeFreeCashierQ();
        
        Customer customer=checkoutarea.removeCustomerQ();
        
        cashier.startServeCustomer(customer, currentTime);
        
        cashier.setEndBusyTime(currentTime +serviceTime);
        
        checkoutarea.insertBusyCashierQ(cashier);
        
        System.out.println("\tCashier#"+cashier.getCashierID()+" start serving customer #"+counter +" for "+ serviceTime + " units");
    }
    
    private void printCurrentTimeInterval(int cTime)
    {
        System.out.println("-----------------------------------------------------------");
        System.out.println("Time :" + cTime);
    }
    
  // *** main method to run simulation ****

  public static void main(String[] args) {
   	SuperMart runSuperMart=new SuperMart();
   	runSuperMart.setupParameters();
   	runSuperMart.doSimulation();
   	runSuperMart.printStatistics();
  }

}
