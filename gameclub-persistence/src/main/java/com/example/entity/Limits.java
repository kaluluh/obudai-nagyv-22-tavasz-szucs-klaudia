package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Table(name = "limits")
public class Limits {
    private Integer minimum;
    private Integer maximum;

    @Override
    public String toString() {
        return minimum + " - " + maximum;
    }
}
