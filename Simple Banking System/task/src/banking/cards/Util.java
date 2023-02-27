package banking.cards;

import java.util.Random;

public class Util {
    public static String generateFixedLengthNum(int length) {
        length = Math.abs(length);
        Random random = new Random();
        String randomValue = String.valueOf(random.nextDouble()).substring(2);
        String value = "";
        int maxLength = randomValue.length();
        int diff = length - maxLength;
        if (diff > 0) {
            length = maxLength;
            value += generateFixedLengthNum(diff);
        }
        value = randomValue.substring(0, length) + value;
        return value;
    }


    public static String generatePin(){
        return generateFixedLengthNum(4);
    }

    public static String generateAI(){
        return generateFixedLengthNum(9);
    }


    public static String generateChecksum(Long l) {
        String str = Long.toString(l);
        int[] ints = new int[str.length()];
        for(int i = 0;i< str.length(); i++){
            ints[i] = Integer.parseInt(str.substring(i, i+1));
        }
        for(int i = 0; i<ints.length; i++){
            if(i%2==0) {
                int j = ints[i];
                j = j*2;
                if(j>9){
                    j = j - 9;
                }
                ints[i]=j;
            }
        }
        int sum=0;
        for(int i = 0;i< ints.length; i++){
            sum+=ints[i];
        }
        if(sum%10==0){
            return "0";
        }else return Integer.toString(10-(sum%10));
    }

    public static boolean validatePin(String pin) throws IncorrectValueException {
        if (pin.length()!=4){
            throw new IncorrectValueException("length!=4");
        }
        try {
            Integer.parseInt(pin);
        } catch (Exception e){
            throw new IncorrectValueException("pint contains invalid symbols");
        }
        return true;
    }

    public static boolean validateCardNumber(String str) {
        int[] ints = new int[str.length()];
        for (int i = 0; i < str.length(); i++) {
            ints[i] = Integer.parseInt(str.substring(i, i + 1));
        }
        for (int i = ints.length - 2; i >= 0; i = i - 2) {
            int j = ints[i];
            j = j * 2;
            if (j > 9) {
                j = j - 9;
            }
            ints[i] = j;
        }
        int sum = 0;
        for (int i = 0; i < ints.length; i++) {
            sum += ints[i];
        }
        if (sum % 10 == 0) {
            return true;
        } else {
            return false;
        }
    }
}
