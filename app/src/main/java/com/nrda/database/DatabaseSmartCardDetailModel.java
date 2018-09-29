package com.nrda.database;

public class DatabaseSmartCardDetailModel {

    private String tag_id;
    private String name;
    private String mobile;
    private String recharge_type;
    private String recharge_amount;
    private String card_type;
    private String color;
    private String smartcard_code;
    private String validity;

    public DatabaseSmartCardDetailModel() {
    }

    public DatabaseSmartCardDetailModel(String tag_id, String name, String mobile, String recharge_type, String recharge_amount, String card_type, String color, String smartcard_code, String validity) {
        this.tag_id = tag_id;
        this.name = name;
        this.mobile = mobile;
        this.recharge_type = recharge_type;
        this.recharge_amount = recharge_amount;
        this.card_type = card_type;
        this.color = color;
        this.smartcard_code = smartcard_code;
        this.validity = validity;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getTag_id() {
        return tag_id;
    }

    public void setTag_id(String tag_id) {
        this.tag_id = tag_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRecharge_type() {
        return recharge_type;
    }

    public void setRecharge_type(String recharge_type) {
        this.recharge_type = recharge_type;
    }

    public String getRecharge_amount() {
        return recharge_amount;
    }

    public void setRecharge_amount(String recharge_amount) {
        this.recharge_amount = recharge_amount;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSmartcard_code() {
        return smartcard_code;
    }

    public void setSmartcard_code(String smartcard_code) {
        this.smartcard_code = smartcard_code;
    }
}
