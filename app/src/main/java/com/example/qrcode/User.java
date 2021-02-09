package com.example.qrcode;

public class User {
    public String INVNUM, TABNUM, OSNAME, AMOUNT, SAMOUNT, POISK, DATE;

    public User(){

    }

    public User(String INVNUM, String TABNUM, String OSNAME, String AMOUNT, String SAMOUNT, String DATE, String POISK) {
        this.INVNUM = INVNUM;
        this.TABNUM = TABNUM;
        this.OSNAME = OSNAME;
        this.AMOUNT = AMOUNT;
        this.SAMOUNT = SAMOUNT;
        this.DATE = DATE;
        this.POISK = POISK;
    }
}
