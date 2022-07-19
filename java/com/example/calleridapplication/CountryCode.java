package com.example.calleridapplication;

public class CountryCode {
   private String Country;
   private String Code;

    @Override
    public String toString() {
        return "CountryCode{" +
                "Country='" + Country + '\'' +
                ", Code='" + Code + '\'' +
                '}';
    }

    public CountryCode(String country, String code) {
        Country = country;
        Code = code;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }








}
