package com.morello.monitoring_and_communication.controller;

import com.morello.monitoring_and_communication.entitites.EnergyCons;
import com.morello.monitoring_and_communication.events.MaxEnergyExceededEvent;
import com.morello.monitoring_and_communication.services.EnergyConsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@CrossOrigin()
@RestController
@RequestMapping("/api/energy_cons")
@RequiredArgsConstructor
public class EnergyConsController {
    private final SimpMessagingTemplate messagingTemplate;
    private final EnergyConsService energyConsService;

    @GetMapping("/stats")
    public List<EnergyCons> getEnergyConsDuring(@RequestParam Integer id, @RequestParam String startingDate) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date starting = dateFormat.parse(startingDate);

        return energyConsService.getEnergyConsDuring(id, starting);
    }

    @EventListener(MaxEnergyExceededEvent.class)
    @MessageMapping("/max-exceed")
    @SendTo("/topic/max_energy_exceeded")
    public String handleMaxEnergyExceededEvent(@Payload MaxEnergyExceededEvent event) {
        String message = event.getMessage();
        return message + ' ' + event.getDeviceId();
    }
}
