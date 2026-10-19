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
        int limit = readInt(in, "How many messages do you wish to enter? ", 1);
        int entered = 0;

        boolean running = true;
        while (running) {
            System.out.println("\n1) Send Messages\n2) Show recently sent messages\n3) Quit");
            int choice = readInt(in, "Choose an option: ", 1);
            switch (choice) {
                case 1:
                    if (entered >= limit) {
                        System.out.println("You have already entered all " + limit + " messages.");
                    } else if (composeMessage(in, entered)) {
                        entered++;
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

    /** Collects one message from the user. Returns true when it used up one of the allowed messages. */
    private static boolean composeMessage(Scanner in, int number) {
        System.out.print("Recipient cell number: ");
        String recipient = in.nextLine().trim();
        String cellResult = Message.checkRecipientCell(recipient);
        System.out.println(cellResult);
        if (!cellResult.equals("Cell phone number successfully captured.")) {
            return false;
        }

        System.out.print("Message: ");
        String text = in.nextLine();
        String lengthResult = Message.checkMessageLength(text);
        if (text.trim().isEmpty() || !lengthResult.equals("Message ready to send.")) {
            System.out.println(text.trim().isEmpty() ? "Message cannot be empty." : lengthResult);
            return false;
        }

        Message msg = new Message(number, recipient, text);
        System.out.println("Message ID generated: " + msg.getMessageId());
        System.out.println("1) Send Message  2) Disregard Message  3) Store Message to send later");
        int action = readInt(in, "Choose: ", 1);
        System.out.println(msg.sentMessage(action));
        if (action == Message.SEND) {
            System.out.println(msg.details());
        } else if (action == Message.DISREGARD) {
            in.nextLine(); // user presses 0 (or Enter) to delete the message
            System.out.println("Message deleted.");
        }
        return action >= Message.SEND && action <= Message.STORE;
    }

    private static int readInt(Scanner in, String prompt, int fallback) {
        System.out.print(prompt);
        String line = in.nextLine().trim();
        try {
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            return fallback == 1 ? -1 : fallback;
        }
    }
}
