package banking.cards;

public class CardFactory {
    public PlasticCard getCard(CardTypes type){
        PlasticCard toReturn = null;
        switch (type){
            case CREDIT:
                toReturn = new CreditCard();
                break;
            default:
                throw new IllegalArgumentException("Wrong card type:" + type);
        }
        return toReturn;
    }
}
