package com.morello.monitoring_and_communication.repositories;

import com.morello.monitoring_and_communication.entitites.EnergyCons;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface EnergyConsRepository extends JpaRepository<EnergyCons, Integer> {
    List<EnergyCons> findEnergyConsByDeviceId(Integer id);
    List<EnergyCons> findEnergyConsByTimestampBetween(Date starting, Date nextDay);

    void deleteAllByDeviceId(Integer id);
}
