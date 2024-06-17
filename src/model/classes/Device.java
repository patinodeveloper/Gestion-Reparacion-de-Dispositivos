/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.classes;

/**
 *
 * @author Antonio
 */
public class Device {

    private int id;
    private int idClient;
    private int idTypeDevice;
    private String brand;
    private String model;
    private String serie_number;
    private String color;
    private String problem;

    public Device() {
    }

    public Device(int id, int idClient, int idTypeDevice, String brand, String model, String serie_number, String color, String problem) {
        this.id = id;
        this.idClient = idClient;
        this.idTypeDevice = idTypeDevice;
        this.brand = brand;
        this.model = model;
        this.serie_number = serie_number;
        this.color = color;
        this.problem = problem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public int getIdTypeDevice() {
        return idTypeDevice;
    }

    public void setIdTypeDevice(int idTypeDevice) {
        this.idTypeDevice = idTypeDevice;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerie_number() {
        return serie_number;
    }

    public void setSerie_number(String serie_number) {
        this.serie_number = serie_number;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    @Override
    public String toString() {
        return "Device{" + "id=" + id + ", idClient=" + idClient + ", idTypeDevice=" + idTypeDevice + ", brand=" + brand + ", model=" + model + ", serie_number=" + serie_number + ", color=" + color + ", problem=" + problem + '}';
    }

}
