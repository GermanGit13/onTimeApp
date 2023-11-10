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

}
