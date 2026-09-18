package com.mycompany.main;

//Imports Scanner so we can receive input
import java.util.Scanner;

//Creates the Main class
public class Main {

    //Creates Scanner to receive input from the user
    static Scanner input = new Scanner(System.in);

    //Stores the user's entered details
    static String username;
    static String password;
    static String cellphone;
    static String firstName;
    static String lastName;

    //Variables for the user's registration details
    //Stores the registered username
    static String registeredUsername;

    //Stores the registered password
    static String registeredPassword;
    
    static String registeredCellPhone;

    //Creates the username checking method
    public static boolean checkUserName(String username) {

        //The username must contain an underscore
        //The username must be no more than five characters long.
        if (username.length() <= 5 && username.contains("_")) {
            return true;
        } else {
            return false;
        }
    }

    //Creates the password checking method
    //The password must have at least eight characters
    //It must contain a capital letter, a number and a special character.    
    public static boolean checkPasswordComplexity(String password) {

        if (password.length() >= 8
                && password.matches(".*[A-Z].*")
                && password.matches(".*[0-9].*")
                && password.matches(".*[^a-zA-Z0-9].*")) {
            return true;
        } else {
            return false;
        }
    }

    //Creates the cellphone checking method
    //The number must start with +27
    //followed by exacly 9 digits
    public static boolean checkCellPhoneNumber(String cellphone) {

        if (cellphone.matches("^\\+27[0-9]{9}$")) {
            return true;
        } else {
            return false;
        }
    }

    //Creates the registration method
    //It collects the user's details and checks that they are valid.
    public static String registerUser() {
        
            //Ask for first name
            System.out.print("Enter your first name: ");
            firstName = input.nextLine();
            
             //Ask for last name
            System.out.print("Enter your last name: ");
            lastName = input.nextLine();
        
                while (true) {
            //Ask for username
            System.out.print("Enter your username: ");
            username = input.nextLine();
            // Check username
            boolean usernameCorrect = checkUserName(username);
            //Check if username is correct
            if (usernameCorrect) {
                break;
            } else {
                System.out.println("Username is not correctly formatted; please "
                        + "ensure that your username contains an underscore and "
                        + "is no more than five characters in length.");
            }
        }
        
        while (true) {
            //Ask for password
            System.out.print("Enter your password: ");
            password = input.nextLine();
            //Check password
            boolean passwordCorrect = checkPasswordComplexity(password);
            if (passwordCorrect) {
                break;
            } else {
                System.out.println("Password is not correctly formatted; please "
                        + " ensure that the password contains a capital letter, "
                        + " a number, and a special character.");
            }
        }
        
        while (true) {
            // Ask for cellphone number
            System.out.print("Enter your cellphone number (+27): ");
            cellphone = input.nextLine();
            //Check cellphone number
            boolean cellphoneCorrect = checkCellPhoneNumber(cellphone);
            if (cellphoneCorrect) {
                break;
            } else {
                System.out.println("Cell phone number incorrectly formatted or "
                    + "does not contain the international code.");
            }
        }
        
        //Store the user's details after successful registration
        registeredUsername = username;
        registeredPassword = password;
        registeredCellPhone = cellphone;
        
        //Return a successful registration message
        return "User registered successfully";
    }
    
    //Creates the login method
    //Checks whether the username and password -
    //entered during login match the registered details.
    public static boolean loginUser(String username, String password) {
        //Infinite loop for login
        while (true) {
         if (username.equals(registeredUsername)
                && password.equals(registeredPassword)) {
            return true;
        } else {
            //Login details are incorrect
            System.out.println("Username or password incorrect, please try again ");
            //Re-prompt the user for username
            System.out.print("Enter your username to login: ");
            username = input.nextLine();
            //Re-prompt the user for password
            System.out.print("Enter your password to login: ");
            password = input.nextLine();
        }   
      }
    }

    //Creates the login status method
    //It displays a welcome message when login is successful.
    //Otherwise, it displays an error message.
    public static String returnLoginStatus(boolean loginSuccessful) {

        if (loginSuccessful) {
            return "Welcome" + " " + firstName + " " + lastName + " " + 
                    "it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }

    //The main method runs the registration and login process
    public static void main(String[] args) {
        
        //Call the registration method
        String registrationMessage = registerUser();
        System.out.println(registrationMessage);
        
        //Continue to login if registration was successful
        if (registrationMessage.equals("User registered successfully")) {
        
        //Adds a blank line for neatness
        System.out.println("");
        
        //Call the login method
        System.out.print("Enter username to login: ");
        String loginUsername = input.nextLine();
        
        System.out.print("Enter password to login: ");
        String loginPassword = input.nextLine();
        
        //Checks whether login details are correct
        boolean loginSuccessful = loginUser(loginUsername, loginPassword);
        
        //Call the method that displays the login result
        System.out.println(returnLoginStatus(loginSuccessful));
    }
  }
}
