package GroceryStoreSimulator;

import java.util.*;

class CompareCashier implements Comparator<Cashier>{
	// overide compare() method
 	public int compare(Cashier o1, Cashier o2) {
		return o1.getEndBusyTime() - o2.getEndBusyTime(); 
	}
}


class CheckoutArea {

  private PriorityQueue <Cashier> busyCashierQ;

  private Queue<Customer> customerQ;
  private Queue<Cashier> freeCashierQ;

  private int customerQLimit;

  public CheckoutArea() 
  {
    busyCashierQ = new PriorityQueue<>();
    customerQ = new ArrayDeque<>();
    freeCashierQ = new ArrayDeque<>();
    customerQLimit = 0;
  }

  public CheckoutArea(int numCashiers, int customerQlimit)
  {
        customerQ = new <Customer>ArrayDeque(customerQlimit);
        freeCashierQ = new <Cashier>ArrayDeque(numCashiers);
        
	busyCashierQ= new PriorityQueue<Cashier>( numCashiers, new CompareCashier());
						   

        this.customerQLimit=customerQlimit;
       
	for(int i=1;i<=numCashiers;i++)
        {
            
            Cashier newCashier=new Cashier(i);
            freeCashierQ.add(newCashier);
        }
  }


  // -------------------------------------------------
  // freeCashierQ methods: remove, insert, empty, size 
  // -------------------------------------------------
  public Cashier removeFreeCashierQ()
  {
	return freeCashierQ.remove();
  }

  public void insertFreeCashierQ(Cashier cashier)
  {
          freeCashierQ.add(cashier);
  }

  public boolean emptyFreeCashierQ()
  {
	return freeCashierQ.isEmpty();
  }

  public int sizeFreeCashierQ()
  {
	return freeCashierQ.size();
  }

  // -------------------------------------------------------
  // busyCashierQ methods: remove, insert, empty, size, peek 
  // -------------------------------------------------------

  public Cashier removeBusyCashierQ() 
  {
	return busyCashierQ.remove();
  }

  public void insertBusyCashierQ(Cashier cashier)
  {
	busyCashierQ.add(cashier);
  }

  public boolean emptyBusyCashierQ()
  {
	return busyCashierQ.isEmpty();
  }

  public int sizeBusyCashierQ()
  {
	return busyCashierQ.size();
  }

  public Cashier peekBusyCashierQ() 
  {
	return busyCashierQ.peek();
  }

  // -------------------------------------------------------
  // customerQ methods: remove, insert, empty, size 
  //                    and check isCustomerQTooLong()
  // -------------------------------------------------------

  public Customer removeCustomerQ()
  {
	return customerQ.remove();
  }

  public void insertCustomerQ(Customer customer)
  {
      if(!isCustomerQTooLong()){
	customerQ.add(customer);}
      
      else
      {System.out.println("line tooooo long" );}
  }

  public boolean emptyCustomerQ()
  {
	return customerQ.isEmpty();
  }

  public int sizeCustomerQ()
  {
	return customerQ.size();
  }

  public boolean isCustomerQTooLong()
  {
	boolean result;
        result = (customerQ.size() > customerQLimit);
	return result;
  }


  public void printStatistics()
  {
  	System.out.println("\t# waiting customers  : "+sizeCustomerQ());
  	System.out.println("\t# busy cashiers      : "+sizeBusyCashierQ());
  	System.out.println("\t# free cashiers      : "+sizeFreeCashierQ());
  }


  public static void main(String[] args) {

        // quick check

        // create a CheckoutArea and 4 customers
        CheckoutArea sc = new CheckoutArea(4, 5);
        Customer c1 = new Customer(1,18,10);
        Customer c2 = new Customer(2,33,11);
        Customer c3 = new Customer(3,21,12);
        Customer c4 = new Customer(4,37,13);

        // insert customers into customerQ
  	sc.insertCustomerQ(c1);
  	sc.insertCustomerQ(c2);
  	sc.insertCustomerQ(c3);
  	sc.insertCustomerQ(c4);
	System.out.println("customerQ:"+sc.customerQ);
	System.out.println("===============================================");
	System.out.println("Remove customer:"+sc.removeCustomerQ());
	System.out.println("Remove customer:"+sc.removeCustomerQ());
	System.out.println("Remove customer:"+sc.removeCustomerQ());
	System.out.println("Remove customer:"+sc.removeCustomerQ());
	System.out.println("===============================================");

        // remove cashiers from freeCashierQ
	System.out.println("freeCashierQ:"+sc.freeCashierQ);
	System.out.println("===============================================");
	Cashier p1=sc.removeFreeCashierQ();
	Cashier p2=sc.removeFreeCashierQ();
	Cashier p3=sc.removeFreeCashierQ();
	Cashier p4=sc.removeFreeCashierQ();
	System.out.println("Remove free cashier:"+p1);
	System.out.println("Remove free cashier:"+p2);
	System.out.println("Remove free cashier:"+p3);
	System.out.println("Remove free cashier:"+p4);
	System.out.println("===============================================");
	System.out.println("freeCashierQ:"+sc.freeCashierQ);
	System.out.println("===============================================");


        // insert customers to cashiers
        p1.startServeCustomer(c1, 13);
        p2.startServeCustomer(c2, 13);
        p3.startServeCustomer(c3, 13);
        p4.startServeCustomer(c4, 13);

        // insert cashiers to busyCashierQ
	System.out.println("busyCashierQ:"+sc.busyCashierQ);
	System.out.println("===============================================");
	sc.insertBusyCashierQ(p1);
	sc.insertBusyCashierQ(p4);
	sc.insertBusyCashierQ(p2);
	sc.insertBusyCashierQ(p3);
	System.out.println("busyCashierQ:"+sc.busyCashierQ);
	System.out.println("===============================================");

        // remove cashiers from busyCashierQ
	p1=sc.removeBusyCashierQ();
	p2=sc.removeBusyCashierQ();
	p3=sc.removeBusyCashierQ();
	p4=sc.removeBusyCashierQ();
        p1.endServeCustomer();
        p2.endServeCustomer();
        p3.endServeCustomer();
        p4.endServeCustomer();
	System.out.println("Remove busy cashier:"+p1);
	System.out.println("Remove busy cashier:"+p2);
	System.out.println("Remove busy cashier:"+p3);
	System.out.println("Remove busy cashier:"+p4);

   }
}


