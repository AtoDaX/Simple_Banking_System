package banking;
import banking.cards.*;
public class Main {
    public static void main(String[] args) {
        Database.setPath(args[1]);
        UserInterface UI = new UserInterface();
        UI.start();

    }
}