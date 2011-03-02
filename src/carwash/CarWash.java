// FILE: Carwash.java
// This program illustrates the use of the carWashSimulate method which uses
// a simple queue to simulate cars waiting at a car wash.

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import edu.colorado.simulations.BooleanSource;
import edu.colorado.simulations.Washer;
import edu.colorado.simulations.Averager;

/******************************************************************************
* The <CODE>CarWash</CODE> Java application illustrates the use of
* the <CODE>carWashSimulate</CODE> method.
* The illustration uses the following values:
*   <CODE>
*   <br>washTime = 240
*   <br>arrivalTime = 0.0025
*   <br>totalTime = 6000
*   </CODE>
*
* <p><dt><b>Java Source Code for this class:</b><dd>
*   <A HREF="../applications/CarWash.java">
*   http://www.cs.colorado.edu/~main/applications/CarWash.java
*   </A>
*
* @author Michael Main 
*   <A HREF="mailto:main@colorado.edu"> (main@colorado.edu) </A>
*
* @version
*   Jun 12, 1998
******************************************************************************/
public class CarWash
{
   /**
   * The main method activates <CODE>carWashSimulate</CODE> with the values:
   *   <CODE>
   *   <br>washTime = 240
   *   <br>arrivalTime = 0.0025
   *   <br>totalTime = 6000
   *   </CODE>
   * <BR>The <CODE>String</CODE> argument (<CODE>args</CODE>) is not used in
   * this implementation.
   **/
   public static void main(String[ ] args)
   {
      final int WASHTIME = 240;
      final double ARRIVALPROB = 0.0025;
      final int TOTALTIME = 6000;
      
      carWashSimulate(WASHTIME, ARRIVALPROB, TOTALTIME);
   }
    
   
   /**
   * Simulate the running of a car washer for a specified amount of time.
   * @param <CODE>washTime</CODE>
   *   the number of seconds required to wash one car
   * @param <CODE>arrivalProb</CODE>
   *   the probability of a customer arriving in any second, for example
   *   0.1 is 10%
   * @param <CODE>totalTime</CODE>
   *   the total number of seconds for the simulation
   * <dt><b>Precondition:</b><dd>
   *   <CODE>washTime</CODE> and <CODE>totalTime</CODE> are positive;
   *   <CODE>arrivalProb</CODE> lies in the range 0 to 1.
   * <dt><b>Postcondition:</b><dd>
   *   The method has simulated a car wash where <CODE>washTime</CODE> is the
   *   number of seconds needed to wash one car, <CODE>arrivalProb</CODE> is
   *   the probability of a customer arriving in any second, and
   *   <CODE>totalTime</CODE> is the total number of seconds for the
   *   simulation. Before the simulation, the method has written its three
   *   parameters to <CODE>System.out</CODE>. After the simulation, the method
   *   has written two pieces of information to <CODE>System.out</CODE>:
   *   (1) The number of cars washed, and (2) The average waiting time for
   *   customers that had their cars washed. (Customers that are still in the 
   *   queue are not included in this average).
   * @exception java.lang.IllegalArgumentException
   *   Indicates that one of the arguments violates the precondition.
   **/
   public static void carWashSimulate
   (int washTime, double arrivalProb, int totalTime)
   {
      Queue<Integer> arrivalTimes = new LinkedList<Integer>( );  
      int next;
      BooleanSource arrival = new BooleanSource(arrivalProb);
      Washer machine = new Washer(washTime);
      Averager waitTimes = new Averager( );
      int currentSecond;
  
      // Write the parameters to System.out.
      System.out.println("Seconds to wash one car: " + washTime);
      System.out.print("Probability of customer arrival during a second: ");
      System.out.println(arrivalProb);
      System.out.println("Total simulation seconds: " + totalTime); 
   
      // Check the precondition:
      if (washTime <= 0 || arrivalProb < 0 || arrivalProb > 1 || totalTime < 0)
         throw new IllegalArgumentException("Values out of range"); 

      for (currentSecond = 0; currentSecond < totalTime; currentSecond++)
      {  // Simulate the passage of one second of time.

         // Check whether a new customer has arrived.
         if (arrival.query( ))
            arrivalTimes.add(currentSecond);
  
         // Check whether we can start washing another car.
         if ((!machine.isBusy( ))  &&  (!arrivalTimes.isEmpty( )))
         {
            next = arrivalTimes.remove( );
            waitTimes.addNumber(currentSecond - next);
            machine.startWashing( );
         }

         // Subtract one second from the remaining time in the current wash cycle.
         machine.reduceRemainingTime( );
      }

      // Write the summary information about the simulation.
      System.out.println("Customers served: " + waitTimes.howManyNumbers( )); 
      if (waitTimes.howManyNumbers( ) > 0)
         System.out.println("Average wait: " + waitTimes.average( ) + " sec");
   }
    
}
