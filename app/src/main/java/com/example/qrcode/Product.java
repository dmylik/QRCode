package com.example.qrcode;

public class Product {

    String name; //Наименование устройства
    String nomer; //Инвентарный номер и количество найденных
    int image; //Значение Картинки


    Product(String _describe, String _price, int _image) {
        name = _describe;
        nomer = _price;
        image = _image;
    }
}