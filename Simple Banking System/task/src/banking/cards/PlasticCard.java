package banking.cards;

import javax.sound.midi.Soundbank;

public abstract class PlasticCard {
    protected static String majorIndustryIdentifier;
    protected static String bankIdentificationNumber;
    protected String accountIdentifier;
    protected String cheksum;
    protected String pin;
    protected String cardNumber;
    protected Integer balance;

    PlasticCard(){
        accountIdentifier = Util.generateAI();
        pin = Util.generatePin();
        balance = 0;

    }
    public void updateBalance(Integer value){
        balance = balance+value;
    }

    public String getPin(){
        return this.pin;
    }
    public void setPin(String pin){
        try {
            Util.validatePin(pin);
        } catch (IncorrectValueException e){
            System.out.println(e);
        }
        this.pin = pin;
    }

    public String getCardNumber(){
        return this.cardNumber;
    }

    public Integer getBalance(){
        return this.balance;
    }

    public void printCard(){
        System.out.printf("""
                Your card number:
                %s
                Your card PIN:
                %s
                """, this.getCardNumber(), this.getPin());
    }
}
