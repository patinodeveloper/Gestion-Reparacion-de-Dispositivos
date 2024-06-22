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
public class Settings {

    private int id;
    private String name;
    private String phone;
    private String address;
    private String message;

    public Settings() {
    }

    public Settings(int id, String name, String phone, String address, String message) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Settings{" + "id=" + id + ", name=" + name + ", phone=" + phone + ", address=" + address + ", message=" + message + '}';
    }

}
