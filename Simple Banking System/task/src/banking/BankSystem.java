package banking;
import banking.cards.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class BankSystem {
    private Database db = Database.getInstance();
    private Scanner scanner = new Scanner(System.in);
    private static final CardFactory cardFactory = new CardFactory();
    private static final BankSystem instance = new BankSystem();
    private HashMap<String, PlasticCard> cardList;

    public static BankSystem getInstance() {return instance;}

    private BankSystem() {cardList  = new HashMap<>(); };

    public PlasticCard generateCard(){
        return cardFactory.getCard(CardTypes.CREDIT);
    }


    public void mainMenu(){
        System.out.print("""
                1. Create an account
                2. Log into account
                0. Exit
                """);
        String action = scanner.nextLine();

        switch (action) {
            case "1" -> {
                PlasticCard generated = generateCard();
                cardList.put(generated.getCardNumber(), generated);
                if (Util.validateCardNumber(generated.getCardNumber())){

                }
                db.insertCard(generated);
                generated.printCard();
                mainMenu();
            }
            case "2" -> {
                PlasticCard account = login();
                if (account != null) {
                    accountMenu(account);
                } else {
                    mainMenu();
                }
            }
            case "0" -> System.out.println("Bye!");
            default -> System.out.println("Invalid input!");
        }
    }


    public PlasticCard login(){
        String cardNumber, pin;
        System.out.println("Enter your card number:");
        cardNumber = scanner.nextLine();
        System.out.println("Enter your PIN:");
        pin = scanner.nextLine();
        CreditCard toReturn = null;

        HashMap<String,String> card = db.getCatd(cardNumber,pin);

        if (card == null){
            System.out.println("Wrong card number or PIN!");
            return null;
        }
        try {
            toReturn = new CreditCard(card.get("number"), Integer.parseInt(card.get("balance")));
        } catch (Exception e){
            System.out.println("Wrong card number or PIN!");
            return null;
        }

        System.out.println("You have successfully logged in!");
        return toReturn;
    }

    public void accountMenu(PlasticCard card){
        System.out.print("""
                1. Balance
                2. Add income
                3. Do transfer
                4. Close account
                5. Log out
                0. Exit
                """);

        String action = scanner.nextLine();

        switch (action) {
            case "1" -> {
                System.out.println("Balance: " + card.getBalance());
                accountMenu(card);
            }
            case "2" -> {
                addIncome(card);
            }
            case "3" -> {
                doTransfer(card);
            }
            case "4" ->{
                closeAccount(card);
            }
            case "5" -> {
                System.out.println("You have successfully logged out!");
                mainMenu();
            }
            case "0" -> {
                System.out.println("Bye!");
                System.exit(0);
            }
            default -> System.out.println("Invalid input!");
        }
    }

    private void addIncome(PlasticCard card){
        System.out.println("Enter income:");
        Integer income = Integer.parseInt(scanner.nextLine());

        if (db.updateBalance(card,income)){
            card.updateBalance(income);
            System.out.println("Income was added!");
            accountMenu(card);
        } else {
            System.out.println("Something wrong!");
            accountMenu(card);
        }
    }

    private void closeAccount(PlasticCard card){
        if (db.deleteCard(card)){
            System.out.println("Account closed!");
            mainMenu();
        } else {
            System.out.println("Something wrong!");
            accountMenu(card);
        }
    }

    private void doTransfer(PlasticCard card){
        System.out.println("Transfer");
        System.out.println("Enter card number:");
        String transferNumber = scanner.nextLine();
        if(transferNumber.equals(card.getCardNumber())){
            System.out.println("You can't transfer money to the same account!");
            accountMenu(card);
        }
        if (!Util.validateCardNumber(transferNumber)){
            System.out.println("Probably you made a mistake in the card number. ");
            System.out.println("Please try again!");
            accountMenu(card);
        }
        if (!db.exists(transferNumber)){
            System.out.println("Such a card does not exist.");
            accountMenu(card);
        }
        System.out.println("Enter how much money you want to transfer:");
        Integer toTransfer = Integer.parseInt(scanner.nextLine());
        if (toTransfer<1){
            System.out.println("Invalid ammount of money!");
            accountMenu(card);
        } else if (toTransfer>card.getBalance()){
            System.out.println("Not enough money!");
            accountMenu(card);
        }
        if (db.doTransfer(card.getCardNumber(), transferNumber, toTransfer)){
            card.updateBalance(-toTransfer);
            System.out.println("Success!");
        }

        accountMenu(card);
    }
}
