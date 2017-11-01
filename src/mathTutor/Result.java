package mathTutor;

import java.sql.Date;

// Result object to hold results data queried from database
public class Result {
      
      private Date date;                  // date of attempt
      private String type = "";           // practice or quiz
      private int numProblems = 0;        // number of problems
      private int numCorrect = 0;         // number correct
      private double time = 0.0;          // time elapsed (for quiz)
      private int attemptDetailsID = 0;   // primary key in sql database
	   
	   // constructor for practice mode result
	   Result(Date date, String type, int numProblems, int numCorrect, int attemptDetailsID){
         this.date = date;
         this.type = type;
         this.numProblems = numProblems;
         this.numCorrect = numCorrect;
         this.attemptDetailsID = attemptDetailsID;
	   }
      
      // constructor for quiz mode result
      Result(Date date, String type, int numProblems, int numCorrect, double time, int attemptDetailsID){
         this.date = date;
         this.type = type;
         this.numProblems = numProblems;
         this.numCorrect = numCorrect;
         this.time = time;
         this.attemptDetailsID = attemptDetailsID;
	   }
      
	   // get attempt_id
	   public int getAttemptDetailsID() {
         return attemptDetailsID;
	   }
      
      // get date of attempt
      public Date getDate() {
         return date;
      }
      
      // get mode (practice or quiz)
      public String getMode() {
         return type;
      }
      
      // get score (based on numCorrect and numProblems)
      public String getScore() {
         return Integer.toString(numCorrect) + " / " + Integer.toString(numProblems);
      }
	   
      // get time elapsed (for quiz mode)
      public double getTime() {
         return time;
      }
}
