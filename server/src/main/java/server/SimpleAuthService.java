package server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleAuthService implements AuthService {
    private Connection connection;
    private PreparedStatement psAdd;
    private PreparedStatement psGetPass;
    private PreparedStatement psGetLogin;

    @Override
    public void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:BD.db");
        prepareAllStatements();
    }

    @Override
    public void disconnect() {
        try {
            psAdd.close();
            psGetPass.close();
            psGetLogin.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void prepareAllStatements() throws SQLException {
        psAdd = connection.prepareStatement("INSERT INTO users (login, password, nickname) VALUES (? , ? , ?)");
        psGetPass = connection.prepareStatement("SELECT password, nickname FROM users WHERE login = ?");
        psGetLogin = connection.prepareStatement("SELECT login FROM users WHERE login = ?");

    }

    // Задание №1. Добавить в сетевой чат авторизацию через базу данных SQLite.
    @Override
    public String getNicknameByLoginAndPassword(String login, String password) throws SQLException {
        ResultSet rs = null;
        psGetPass.setString(1, login);
        rs = psGetPass.executeQuery();
        if (rs.next() && rs.getString("password").equals(password)) {
            String nickname = rs.getString("nickname");
            rs.close();
            return nickname;
        }
        rs.close();
        return null;
    }

    @Override
    public boolean registration(String login, String password, String nickname) {
        return false;
    }
}

//public class SimpleAuthService implements AuthService {
//    private class UserData {
////        String login;
////        String password;
////        String nickname;
//
//        public UserData(String login, String password, String nickname) {
//            this.login = login;
//            this.password = password;
//            this.nickname = nickname;
//        }
//    }
//
//    private List<UserData> users;
//
//    public SimpleAuthService() {
//        users = new ArrayList<>();
//        users.add(new UserData("qwe", "qwe", "qwe"));
//        users.add(new UserData("asd", "asd", "asd"));
//        users.add(new UserData("zxc", "zxc", "zxc"));
//        for (int i = 1; i < 10; i++) {
//            users.add(new UserData("login" + i, "pass" + i, "nick" + i));
//        }
//    }
//
//    @Override
//    public String getNicknameByLoginAndPassword(String login, String password) {
//        for (UserData user : users) {
//            if(user.login.equals(login) && user.password.equals(password)){
//                return user.nickname;
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public boolean registration(String login, String password, String nickname) {
//        for (UserData user : users) {
//            if(user.login.equals(login) || user.nickname.equals(nickname)){
//                return false;
//            }
//        }
//        users.add(new UserData(login, password, nickname));
//        return true;
//    }
//}
