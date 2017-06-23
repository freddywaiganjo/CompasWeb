import java.util.Random;

import org.joda.time.LocalDate;
import org.joda.time.Period;

/**
 * 
 */

/**
 * @author ANITA
 *May 30, 2017
 */
public class GetMonths {

	public static void main(String args[]){
		 LocalDate date1 = new LocalDate("2016-12-12");
		 LocalDate date2 = new LocalDate("2017-11-11"); 
		 while(date1.isBefore(date2)){
		   // System.out.println(date1.toString("MMM/yyyy"));
		     LocalDate startOfMonth = date1.dayOfMonth().withMinimumValue();
		     LocalDate endOfMonth = date1.dayOfMonth().withMaximumValue();
		    // System.out.println(startOfMonth);
		     //System.out.println(endOfMonth);
		     date1 = date1.plus(Period.months(1));
		 }
		 
		 for (int i=0;i<4000;i++){
		 generateRandomString();
		 }
	}
	
	private static final String CHAR_LIST =
	        "123456789";
	    private static final int RANDOM_STRING_LENGTH = 4;
	
    
   /**
    * This method generates random string
    * @return
    */
   public static String generateRandomString(){
       StringBuffer randStr = new StringBuffer();
       for(int i=0; i<RANDOM_STRING_LENGTH; i++){
           int number = getRandomNumber();
           char ch = CHAR_LIST.charAt(number);
           randStr.append(ch);
       }
       System.out.println("String##"+randStr);
       return randStr.toString();
   }
   private static int getRandomNumber() {
       int randomInt = 0;
       Random randomGenerator = new Random();
       randomInt = randomGenerator.nextInt(CHAR_LIST.length());
       if (randomInt - 1 == -1) {
           return randomInt;
       } else {
           return randomInt - 1;
       }
   }

}
