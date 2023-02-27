package banking;

import banking.cards.PlasticCard;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.HashMap;

public class Database {
    private static Database instance = null;
    private static String url = "jdbc:sqlite:/test.db";
    private SQLiteDataSource dataSource;
    private Connection connection;
    private Statement statement;
    private static String path = "test.db";

    public static void setPath(String path1){
        path = path1;
    }

    private Database(){
        url =  "jdbc:sqlite:./" + path;
        dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        this.connection = getConnection();
        if (this.connection==null){
            System.out.println("No connection!");
            return;
        }
        try (Statement statement = connection.createStatement()) {
            this.statement = statement;
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS card(" +
                    "id INTEGER PRIMARY KEY," +
                    "number TEXT," +
                    "pin TEXT," +
                    "balance INTEGER DEFAULT 0)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static Database getInstance() {
        if (instance==null){
            instance = new Database();
        }
        return instance;}



    private Connection getConnection(){
        try{
            Connection conn = dataSource.getConnection();
            if (conn.isValid(5)){
                return conn;
            }
            return null;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public void insertCard(PlasticCard card){
        String values = "(" + card.getCardNumber()+","+card.getPin()+","+card.getBalance() + ")";
        try {
            statement.executeUpdate("INSERT INTO card (number, pin, balance)" +
                    "VALUES " + values);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public HashMap<String ,String> getCatd(String number, String pin){
        HashMap<String,String > toReturn = new HashMap<>();
        try (ResultSet cardSet = statement.executeQuery("SELECT * FROM card where number=" + number + " AND pin="+pin)) {
            while (cardSet.next()) {
                Integer id = cardSet.getInt("id");
                String number1 = cardSet.getString("number");
                String pin1 = cardSet.getString("pin");
                Integer balance = cardSet.getInt("balance");

                toReturn.put("id", id.toString());
                toReturn.put("number", number1);
                toReturn.put("pin", pin1);
                toReturn.put("balance", balance.toString());
            }
            return toReturn;
        } catch (SQLException e) {
            return null;
        }

    }

    public boolean updateBalance(PlasticCard card, Integer income){
        String updateBalance =
                        "UPDATE card " +
                        "SET balance = balance + ? " +
                        "WHERE number = ?";
        try(PreparedStatement update = connection.prepareStatement(updateBalance)) {
            update.setInt(1,income);
            update.setString(2, card.getCardNumber());
            update.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean deleteCard(PlasticCard card){
        String deleteCard =
                        "DELETE FROM card " +
                        "WHERE number = ?";
        try(PreparedStatement delete = connection.prepareStatement(deleteCard)) {
            delete.setString(1, card.getCardNumber());
            delete.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean exists(String number){
        String existsStatement = "SELECT EXISTS(SELECT number FROM card WHERE number = ?)";

        try(PreparedStatement exists = connection.prepareStatement(existsStatement)) {
            exists.setString(1, number);
            ResultSet bool = exists.executeQuery();
            boolean toReturn = bool.getBoolean(1);
            return toReturn;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean doTransfer(String fromNumber, String toNumber, Integer ammount){
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String income = "UPDATE card " +
                "SET balance = balance + ? " +
                "WHERE number = ?";
        String from = "UPDATE card " +
                "SET balance = balance - ? " +
                "WHERE number = ?";

        try(PreparedStatement incomeSt = connection.prepareStatement(income);
            PreparedStatement fromSt = connection.prepareStatement(from)) {
            incomeSt.setInt(1, ammount);
            incomeSt.setString(2, toNumber);
            incomeSt.executeUpdate();

            fromSt.setInt(1, ammount);
            fromSt.setString(2,fromNumber);
            fromSt.executeUpdate();
            connection.commit();
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return true;

        } catch (SQLException e) {
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
            }
            return false;
        }



    }






}
