package com.company;

public class Main {
    public static void main(String[] args) {
        BabushkaPhone babushkaPhone = new BabushkaPhone(2342432, "Nokia", "300" ,"2003", 12, 5000);
        SmartPhone smartPhone = new SmartPhone(123561, "Apple", "300", "2240x1440", 4, true);

        babushkaPhone.receiveCall("Ivan");
        System.out.println(babushkaPhone.getPrice());
        babushkaPhone.getInfo();
        System.out.println(babushkaPhone.getNumber());

        smartPhone.sendMessage("hi");
        System.out.println(smartPhone.getScreenResolution());
        smartPhone.getInfo();
        System.out.println(smartPhone.getNumber());
        smartPhone.receiveCall("Plaza");
    }
}

class Phone {
    private int serialNumber;
    private String model;
    private String weight;

    Phone(int serialNumber, String model, String weight) {
        this.serialNumber = serialNumber;
        this.model = model;
        this.weight = weight;
    }

    Phone(int serialNumber, String model) {
        this.serialNumber = serialNumber;
        this.model = model;
    }

    public void getInfo() {
        System.out.println("serialNumber: " + serialNumber + " model: " + model + " weight: " + weight);
    }

    public int getNumber() {
        return this.serialNumber;
    }

    public void receiveCall(String name) {
        System.out.println("receiveCall: " + name);
    }
}

class BabushkaPhone extends Phone {
    private String productionYear;
    private int buttonCount;
    private int price;

    BabushkaPhone(int serialNumber, String model, String weight, String productionYear, int buttonCount, int price) {
        super(serialNumber, model, weight);
        this.productionYear =productionYear;
        this.buttonCount = buttonCount;
        this.price = price;
    }

    public void receiveCall(String name) {
        System.out.println("BabushkaPhone receiveCall: " + name);
    }

    public int getPrice() {
        return this.price;
    }
}

class SmartPhone extends Phone {
    private String screenResolution;
    private int camerasCount;
    private boolean touchId;

    public void sendMessage(String message) {
        System.out.println("sendMessage: " + message);
    }

    SmartPhone(int serialNumber, String model, String weight, String screenResolution, int camerasCount, boolean touchId) {
        super(serialNumber, model, weight);
        this.screenResolution = screenResolution;
        this.camerasCount = camerasCount;
        this.touchId = touchId;
    }

    public String getScreenResolution() {
        return this.screenResolution;
    }
}