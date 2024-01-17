package com.morello.monitoring_and_communication.entitites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "energy_consumption")
public class EnergyCons {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer deviceId;
    private Date timestamp;
        private double measurement_value;

    public EnergyCons(Integer deviceId, Date timestamp, double energyConsumption) {
        this.deviceId = deviceId;
        this.timestamp = timestamp;
        this.measurement_value = energyConsumption;
    }
}
