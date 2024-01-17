package com.morello.monitoring_and_communication.services;

import com.morello.monitoring_and_communication.entitites.EnergyCons;
import com.morello.monitoring_and_communication.events.MaxEnergyExceededEvent;
import com.morello.monitoring_and_communication.repositories.EnergyConsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EnergyConsServiceImpl implements EnergyConsService{
    private final EnergyConsRepository energyConsRepository;
    private final RestTemplate restTemplate;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public List<EnergyCons> getEnergyConsByDeviceId(Integer id) {
        return energyConsRepository.findEnergyConsByDeviceId(id);
    }

    @Override
    public List<EnergyCons> getEnergyConsDuring(Integer deviceId, Date starting) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(starting);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date nextDay = calendar.getTime();
        return energyConsRepository.findEnergyConsByTimestampBetween(starting, nextDay).stream().filter(energyCons -> Objects.equals(energyCons.getDeviceId(), deviceId)).toList();
    }

    @Override
    public void addEnergyCons(EnergyCons energyCons) {
        EnergyCons newEnergyCons = energyConsRepository.save(energyCons);
        ResponseEntity<Integer> maxConsumptionResponse = restTemplate.getForEntity(
                "http://localhost:8081/api/devices/find/max_energy?id={id}",
                Integer.class,
                energyCons.getDeviceId()
        );

        if(maxConsumptionResponse.getStatusCode().is2xxSuccessful()) {
            Integer maxCons = maxConsumptionResponse.getBody();
            if(maxCons != null && energyCons.getMeasurement_value() > maxCons) {
                eventPublisher.publishEvent(new MaxEnergyExceededEvent(newEnergyCons, energyCons.getDeviceId(),"Max energy exceeded for device"));
            }
        }
    }

    @Override
    public void deleteDeviceCons(Integer id) {
        energyConsRepository.deleteAllByDeviceId(id);
    }
}
