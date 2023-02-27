package banking.cards;

public class CreditCard extends PlasticCard{
    private static final String majorIndustryIdentifier = "4";
    private static final String bankIdentificationNumber = "400000";


    CreditCard(){
        super();
        cheksum = Util.generateChecksum(Long.valueOf(bankIdentificationNumber+accountIdentifier));
        cardNumber = bankIdentificationNumber+accountIdentifier+cheksum;
    }
    public CreditCard(String number, Integer balance){
        this.cardNumber = number;
        this.balance = balance;
    }
}
