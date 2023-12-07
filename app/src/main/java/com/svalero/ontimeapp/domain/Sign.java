package com.svalero.ontimeapp.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Implementamos Serializable para poder pasar los objetos entre activities
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sign implements Serializable {

    long id;
    String modality;
    String day;
    String in_time;
    String out_time;
    String incidence_in;
    String incidence_out;
    User userInSign;

    /**
     * Para registrar un fichaje
     */
    public Sign(String modality, String day, String in_time, String out_time, String incidence_in, String incidence_out, User user) {
        this.modality = modality;
        this.day = day;
        this.in_time = in_time;
        this.out_time = out_time;
        this.incidence_in = incidence_in;
        this.incidence_out = incidence_out;
        this.userInSign = user;
    }

    public Sign(String modality, String day, String in_time, String incidence_in, User user) {
        this.modality = modality;
        this.day = day;
        this.in_time = in_time;
        this.incidence_in = incidence_in;
        this.userInSign = user;
    }
}
