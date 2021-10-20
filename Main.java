package com.company;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        Auth auth = new Auth();
        auth.fillLogin();
    }
}

class Auth {
    private String login;

    public void fillLogin() {
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.print("Введите email или телефон: ");
            login = scanner.nextLine();
        } while(!isLoginValid());

        Coder backend = new Coder();
        backend.sendCode();

        System.out.print("Введите code: ");
        int code = scanner.nextInt();

        if (backend.checkCode(code)) {
            System.out.println("Удачно");
        } else {
            System.out.println("Неверный");
        }
    }

    private boolean isLoginValid() {
        return isEmailValid() || isPhoneValid();
    }

    private boolean isPhoneValid() {
        return Pattern.compile("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$")
                .matcher(login)
                .matches();
    }

    private boolean isEmailValid() {
        return Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)
                .matcher(login)
                .matches();
    }
}

class Coder {
    private int code;

    public void sendCode() {
        code = 111_111 + (int)(Math.random() * ((999_999 - 111_111) + 1));

        System.out.println("Отправлен код " + code);
    }

    public boolean checkCode(int userCode) {
        return code == userCode;
    }
}