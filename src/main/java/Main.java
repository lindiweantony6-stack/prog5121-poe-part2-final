import java.util.Scanner;

/** Console driver: register, log in, then use QuickChat. */
public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.println("=== Registration ===");
        System.out.print("First name: ");
        String first = in.nextLine().trim();
        System.out.print("Last name: ");
        String last = in.nextLine().trim();
        Login login = new Login();

        boolean registered = false;
        while (!registered) {
            System.out.print("Username: ");
            String user = in.nextLine().trim();
            System.out.print("Password: ");
            String pass = in.nextLine().trim();
            System.out.print("Cell phone number (e.g. +27838968976): ");
            String cell = in.nextLine().trim();
            String result = login.registerUser(user, pass, cell, first, last);
            System.out.println(result);
            registered = login.isUserRegistered();
            if (registered) {
                System.out.println("User has been registered successfully.");
            }
        }

        System.out.println("\n=== Login ===");
        boolean loggedIn = false;
        while (!loggedIn) {
            System.out.print("Username: ");
            String user = in.nextLine().trim();
            System.out.print("Password: ");
            String pass = in.nextLine().trim();
            loggedIn = login.loginUser(user, pass);
            System.out.println(login.returnLoginStatus(loggedIn));
        }

        // Only reachable after a successful login
        System.out.println("\nWelcome to QuickChat.");

        // Ask until the user gives a valid number of messages (at least 1)
        int limit = 0;
        while (limit < 1) {
            limit = readInt(in, "How many messages do you wish to enter? ");
            if (limit < 1) {
                System.out.println("Please enter a whole number of 1 or more.");
            }
        }

        boolean messagesEntered = false;
        boolean running = true;
        while (running) {
            System.out.println("\n1) Send Messages\n2) Show recently sent messages\n3) Quit");
            int choice = readInt(in, "Choose an option: ");
            switch (choice) {
                case 1:
                    if (messagesEntered) {
                        System.out.println("You have already entered all " + limit + " messages.");
                    } else {
                        // for loop: the user enters exactly the number of messages they chose
                        for (int i = 0; i < limit; i++) {
                            System.out.println("\n--- Message " + (i + 1) + " of " + limit + " ---");
                            composeMessage(in, i);
                        }
                        messagesEntered = true;
                    }
                    break;
                case 2:
                    System.out.println("Coming Soon.");
                    break;
                case 3:
                    running = false;
                    break;
                default:
                    System.out.println("Please choose 1, 2 or 3.");
            }
        }
        System.out.println("Total messages sent: " + Message.returnTotalMessages());
    }

    /** Collects one message from the user; keeps asking until the input is valid. */
    private static void composeMessage(Scanner in, int number) {
        String recipient;
        while (true) {
            System.out.print("Recipient cell number: ");
            recipient = in.nextLine().trim();
            String cellResult = Message.checkRecipientCell(recipient);
            System.out.println(cellResult);
            if (cellResult.equals("Cell phone number successfully captured.")) {
                break;
            }
        }

        String text;
        while (true) {
            System.out.print("Message: ");
            text = in.nextLine();
            String lengthResult = Message.checkMessageLength(text);
            if (text.trim().isEmpty()) {
                System.out.println("Message cannot be empty.");
            } else if (lengthResult.equals("Message ready to send.")) {
                break;
            } else {
                System.out.println(lengthResult);
            }
        }

        Message msg = new Message(number, recipient, text);
        System.out.println(msg.getMessageIdMessage());
        System.out.println("1) Send Message  2) Disregard Message  3) Store Message to send later");

        int action;
        do {
            action = readInt(in, "Choose: ");
            if (action < Message.SEND || action > Message.STORE) {
                System.out.println("Please choose 1, 2 or 3.");
            }
        } while (action < Message.SEND || action > Message.STORE);

        System.out.println(msg.sentMessage(action));
        if (action == Message.SEND) {
            System.out.println(msg.details());
        } else if (action == Message.DISREGARD) {
            // The message is only deleted once the user presses 0
            while (!in.nextLine().trim().equals("0")) {
                System.out.println("Press 0 to delete the message.");
            }
            System.out.println("Message deleted.");
        }
    }

    /** Reads a whole number from the user; returns -1 if the input is not a number. */
    private static int readInt(Scanner in, String prompt) {
        System.out.print(prompt);
        String line = in.nextLine().trim();
        try {
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}