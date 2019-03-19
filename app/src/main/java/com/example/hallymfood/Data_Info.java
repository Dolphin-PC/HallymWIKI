package com.example.hallymfood;

public class Data_Info {
    private String sub_Name;
    private String Name;
    private String Time;
    private String Time2;
    private String price;
    private String phone;

    public Data_Info(){}

    public Data_Info(String sub_Name,String Name,String Time,String Time2,String price,String phone){
        this.sub_Name=sub_Name;
        this.Name = Name;
        this.Time = Time;
        this.Time2= Time2;
        this.price = price;
        this.phone = phone;
    }
    public String getSub_Name() { return sub_Name; }
    public String getName() {
        return Name;
    }
    public String getTime() {
        return Time;
    }
    public String getTime2() {return Time2;}
    public String getPrice() {return price;}
    public String getPhone() { return phone; }

    public void setSub_Name(String sub_name) { this.sub_Name = sub_Name; }
    public void setName(String name) {
        this.Name = name;
    }
    public void setTime(String time) {
        Time = time;
    }
    public void setTime2(String time2) { Time2 = time2; }
    public void setPrice(String price) { this.price = price;}
    public void setPhone(String phone) { this.phone = phone; }
}
