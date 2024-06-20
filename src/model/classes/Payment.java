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
public class Payment {

    private int id;
    private int idRepair;
    private double amount;
    private Date paymentDate;
    private PaymentMethod paymentMethod;

    public enum PaymentMethod {
        EFECTIVO("Efectivo"),
        DEPOSITO("Depósito"),
        TRANSFERENCIA("Transferencia");

        private final String method;

        PaymentMethod(String method) {
            this.method = method;
        }

        @Override
        public String toString() {
            return this.method;
        }

        public static PaymentMethod fromString(String text) {
            for (PaymentMethod pm : PaymentMethod.values()) {
                if (pm.method.equalsIgnoreCase(text)) {
                    return pm;
                }
            }
            throw new IllegalArgumentException("No enum constant for value: " + text);
        }
    }

    public Payment() {
    }

    public Payment(int id, int idRepair, double amount, Date paymentDate, PaymentMethod paymentMethod) {
        this.id = id;
        this.idRepair = idRepair;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdRepair() {
        return idRepair;
    }

    public void setIdRepair(int idRepair) {
        this.idRepair = idRepair;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Override
    public String toString() {
        return "Payment{" + "id=" + id + ", idRepair=" + idRepair + ", amount=" + amount + ", paymentDate=" + paymentDate + ", paymentMethod=" + paymentMethod + '}';
    }

}
