package com.company;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.security.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Auth auth = new AuthImpl();

        System.out.println("Введи 0 если хочешь зарегистрироваться, 1 если войти.");
        int num = scanner.nextInt();

        System.out.println("Логин (email): ");
        String login = scanner.next();
        System.out.println("Пароль: ");
        String password = scanner.next();

        if (num == 0) {
            auth.register(login, password);
        }

        if (num == 1) {
            auth.login(login, password);
        }
    }
}

abstract class Server {
    abstract boolean register(String login, String password);

    abstract boolean login(String login, String password);

    abstract void dropDataBase();
}

abstract class Auth {
    abstract void register(String login, String password);

    abstract void login(String login, String password);
}

class AuthImpl extends Auth {

    @Override
    void register(String login, String password) {
        System.out.printf("Register %b: %s", ServerImpl.getInstance().register(login, password), login);
    }

    @Override
    void login(String login, String password) {
        System.out.printf("Login %b: %s", ServerImpl.getInstance().login(login, password), login);
    }
}

class ServerImpl extends Server {
    private static Server _instance = null;
    private final String DB_PATH = "./database.json";
    private HashMap<String, String> db = new HashMap<>();

    public static Server getInstance() {
        if (_instance == null)
            _instance = new ServerImpl();
        return _instance;
    }

    ServerImpl() {
        if (initDb())
            load_db_scheme();

        load_db_from_file();
    }

    private boolean initDb() {
        File file = new File(DB_PATH);

        if (file.exists()) return false;

        try { file.createNewFile(); }
        catch(IOException e) {}
        return true;
    }

    private void load_db_scheme() {
        try {
            FileWriter writer = new FileWriter(DB_PATH);
            writer.write("{}");
            writer.flush();
        }
        catch (IOException e) {}
    }

    private void load_db_from_file() {
        try {
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Path.of(DB_PATH));

            db = gson.fromJson(reader, db.getClass());

            reader.close();
        }
        catch (IOException e) {}
    }

    private void syncDb() {
        try (Writer writer = new FileWriter(DB_PATH)){
            Gson gson = new GsonBuilder().create();
            gson.toJson(db, writer);
        }
        catch(IOException e) {}
    }

    private boolean isEmailValid(String login) {
        return Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)
                .matcher(login)
                .matches();
    }

    @Override
    boolean register(String login, String password) {
         if (db.get(login) != null) return false;
         if (!isEmailValid(login)) return false;

         db.put(login, toMd5(password));
         syncDb();

         return true;
    }

    private String toMd5(String str) {
        String hashedString = "";

        try {
            byte[] bytesOfMessage = str.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] theMD5digest = md.digest(bytesOfMessage);
            hashedString = new String(theMD5digest, StandardCharsets.UTF_8);
        } catch (IOException | NoSuchAlgorithmException e) {}

        return hashedString;
    }

    @Override
    boolean login(String login, String password) {
        if (db.get(login) == null) return false;
        if (!db.get(login).equals(toMd5(password))) return false;

        return true;
    }

    @Override
    void dropDataBase() {
        try {
            Files.delete(Paths.get(DB_PATH));
        } catch (IOException e) {}
    }
}