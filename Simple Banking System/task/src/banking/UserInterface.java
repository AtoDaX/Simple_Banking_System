package banking;

public class UserInterface {
    private BankSystem bankSystem = BankSystem.getInstance();

    public void start(){
        bankSystem.mainMenu();
    }
}
