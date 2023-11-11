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
    LocalDate day;
    LocalTime in_time;
    LocalTime out_time;
    String incidence_in;
    String incidence_out;
    User user;

    /**
     * Para registrar un fichaje
     */
    public Sign(String modality, LocalDate day, LocalTime in_time, LocalTime out_time, String incidence_in, String incidence_out, User user) {
        this.modality = modality;
        this.day = day;
        this.in_time = in_time;
        this.out_time = out_time;
        this.incidence_in = incidence_in;
        this.incidence_out = incidence_out;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public LocalTime getIn_time() {
        return in_time;
    }

    public void setIn_time(LocalTime in_time) {
        this.in_time = in_time;
    }

    public LocalTime getOut_time() {
        return out_time;
    }

    public void setOut_time(LocalTime out_time) {
        this.out_time = out_time;
    }

    public String getIncidence_in() {
        return incidence_in;
    }

    public void setIncidence_in(String incidence_in) {
        this.incidence_in = incidence_in;
    }

    public String getIncidence_out() {
        return incidence_out;
    }

    public void setIncidence_out(String incidence_out) {
        this.incidence_out = incidence_out;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
