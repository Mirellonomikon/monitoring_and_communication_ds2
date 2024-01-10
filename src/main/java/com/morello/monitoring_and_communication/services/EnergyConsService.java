package com.morello.monitoring_and_communication.services;

import com.morello.monitoring_and_communication.entitites.EnergyCons;

import java.util.Date;
import java.util.List;

public interface EnergyConsService {
    List<EnergyCons> getEnergyConsByDeviceId(Integer id);
    List<EnergyCons> getEnergyConsDuring(Integer deviceId, Date starting);
    void addEnergyCons(EnergyCons energyCons);
    void deleteDeviceCons(Integer id);
}
