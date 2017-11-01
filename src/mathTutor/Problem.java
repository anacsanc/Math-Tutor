package mathTutor;
import javafx.scene.control.TextField;

// Problem object with problem string, answer, useranswer, textfield
public class Problem {
      private String equation = "";          // equation as a string
	   private int answer = 0;				      // correct answer
	   private int userAns = 0;				   // answer from user
	   TextField ansText = new TextField();	// textfield for user input
	   
	   // constructor takes as arguments equation and answer
	   Problem(String equation, int answer){
         this.equation = equation;
         this.answer = answer;
	   }
      
      // constructor takes as arguments equation, answer, useranswer
	   Problem(String equation, int answer, int userAns){
         this.equation = equation;
         this.answer = answer;
         this.userAns = userAns;
	   }
      
	   // get equation 
	   public String getEquation() {
         return equation;
	   }
	   
      // get correct answer
	   public Integer getAnswer() {
		   return answer;
	   }
      
	   // get user's answer
	   public Integer getUserAnswer() {
		   return userAns;
	   }
	   
      // set user answer based on textfield entry
	   public void setUserAnswer() {
         try {
		      userAns = Integer.parseInt(ansText.getText());
         } catch(NumberFormatException e){
            userAns = 0;
         }
	   }
	   
      // check if user answer is correct
	   public Boolean isCorrect() {
		   return userAns == answer;
	   }
	   
      // text field for user input
	   public TextField createTextField() {
         ansText.setPrefColumnCount(6);
		   return ansText;
	   }
}
