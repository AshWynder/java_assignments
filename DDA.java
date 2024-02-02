
//imported scanner class for allowing input for program
import java.util.Scanner;
//imported the AtomicBoolean class for reading and updating values using threads
import java.util.concurrent.atomic.AtomicBoolean;

public class DDA {
    public static void main(String... args) {
    
    		//input class created for user input
        Scanner input = new Scanner(System.in);

        //variables that will store the username and password of the "user"
        String username = "username";
        String passcode = "usercode";

				//user prompted to enter username and password
        System.out.println("Enter your username:");
        String userName =  input.nextLine();
        System.out.println("Enter your password:");
        String password = PasswordField.readPassword("Enter password: ");
				
				//error variable for tracking input attempts
        String error = null;
        
        //for loop for ensuring 3 attempts
        for(int i = 0; i < 3; i++) {
        		//logic for checking correct username and password
            if (username.equals(userName) && password.equals(passcode)){
                System.out.println("Login success");
                error = null;
                break;
            }else {
            		//if either username or password is incorrect user can try again
                System.out.println("Username or password is incorrect try again");
                System.out.println("Enter your username:");
                userName =  input.nextLine();
                System.out.println("Enter your password:");
                password = PasswordField.readPassword("Enter password: ");
                error = "not null";
            }
        }
				//if the error variable is not null this means the user tried 3 times
        if(error != null )
            System.out.println("Three login attempts exhausted. Try again later");
   	     
    }
}

class PasswordField {
		//method with a string parameter and will return a string value which is the password
    public static String readPassword(String prompt) {
    		//later down the code will implement the `Runnable interface` that will aid in removing characters as they are typed
        EraserThread et = new EraserThread(prompt);
        Thread mask = new Thread(et);
        mask.start();

        try (Scanner scanner = new Scanner(System.in)) { //takes the input of the password which will be masked
            String password = scanner.nextLine();
            return password;
        } finally {
            et.stopMasking(); //if an error occurs masking is interrupted
        }
    }
}

class EraserThread implements Runnable {
    private final AtomicBoolean stop = new AtomicBoolean(false);//creates a boolean variable which will be used in masking

    public EraserThread(String prompt) {//prints `prompt to console`
        System.out.print(prompt);
    }

    @Override
    public void run() {
        try {
            while (!stop.get()) {
                System.out.print("\010*");//the unicode that represents * which will be shown on terminal
                Thread.sleep(1);
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
            Thread.currentThread().interrupt(); // Restore interrupted status
        }
    }

    public void stopMasking() {
        stop.set(true);//signals the thread to stop masking
    }
}

