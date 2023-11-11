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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
