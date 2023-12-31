package com.svalero.ontimeapp.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Implementamos Serializable para poder pasar los objetos entre activities
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    private long id;
    private String username;
    private String pass;
    private String rol;
    private String photo;
    private String name;
    private String surname;
    private String address;
    private String mail;
    private String phone;
    private String department;

    /**
     * Constructor para registrar Usuarios
     */
    public User(String username, String pass, String rol, String photo, String name, String surname, String address, String mail, String phone, String department) {
        this.username = username;
        this.pass = pass;
        this.rol = rol;
        this.photo = photo;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.mail = mail;
        this.phone = phone;
        this.department = department;
    }
}
