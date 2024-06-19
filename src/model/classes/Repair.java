/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.classes;

import java.util.Date;

/**
 *
 * @author Antonio
 */
public class Repair {

    private int id;
    private int idDevice;
    private String device;
    private String client;
    private String problem;
    private String service;
    private double price;
    private Date receivedDate;
    private Date deliveredDate;
    private Estado state;
    private EstadoPago paymentState;

    // Enumeraciones para el estado y el estado de pago
    public enum Estado {
        PENDIENTE("Pendiente"),
        COMPLETADO("Completado"),
        ENTREGADO("Entregado");

        private final String estado;

        Estado(String estado) {
            this.estado = estado;
        }

        @Override
        public String toString() {
            return this.estado;
        }

        public static Estado fromString(String text) {
            for (Estado e : Estado.values()) {
                if (e.estado.equalsIgnoreCase(text)) {
                    return e;
                }
            }
            throw new IllegalArgumentException("No enum constant for value: " + text);
        }
    }

    public enum EstadoPago {
        NO_PAGADO("No Pagado"),
        PAGO_PARCIAL("Pago Parcial"),
        PAGADO("Pagado");

        private final String estadoPago;

        EstadoPago(String estadoPago) {
            this.estadoPago = estadoPago;
        }

        @Override
        public String toString() {
            return this.estadoPago;
        }

        public static EstadoPago fromString(String text) {
            for (EstadoPago e : EstadoPago.values()) {
                if (e.estadoPago.equalsIgnoreCase(text)) {
                    return e;
                }
            }
            throw new IllegalArgumentException("No enum constant for value: " + text);
        }
    }

    public Repair() {
    }

    public Repair(int id, int idDevice, String device, String client, String problem, String service, double price, Date receivedDate, Date deliveredDate, Estado state, EstadoPago paymentState) {
        this.id = id;
        this.idDevice = idDevice;
        this.device = device;
        this.client = client;
        this.problem = problem;
        this.service = service;
        this.price = price;
        this.receivedDate = receivedDate;
        this.deliveredDate = deliveredDate;
        this.state = state;
        this.paymentState = paymentState;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdDevice() {
        return idDevice;
    }

    public void setIdDevice(int idDevice) {
        this.idDevice = idDevice;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public Date getDeliveredDate() {
        return deliveredDate;
    }

    public void setDeliveredDate(Date deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    public Estado getState() {
        return state;
    }

    public void setState(Estado state) {
        this.state = state;
    }

    public EstadoPago getPaymentState() {
        return paymentState;
    }

    public void setPaymentState(EstadoPago paymentState) {
        this.paymentState = paymentState;
    }

    @Override
    public String toString() {
        return "Repair{" + "id=" + id + ", idDevice=" + idDevice + ", device=" + device + ", client=" + client + ", problem=" + problem + ", service=" + service + ", price=" + price + ", receivedDate=" + receivedDate + ", deliveredDate=" + deliveredDate + ", state=" + state + ", paymentState=" + paymentState + '}';
    }

}
