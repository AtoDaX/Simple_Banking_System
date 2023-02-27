package banking.cards;

public class IncorrectValueException extends Exception{
    IncorrectValueException(String message){
        super(message);
    }
}
