package com.cu.tt;

public class CalculateRollCall {
    private int total;
    private String subject;

    public CalculateRollCall(int total, String subject) {
        this.total = total;
        this.subject = subject;
    }

    public CalculateRollCall() {
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
