package com.morello.monitoring_and_communication.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.morello.monitoring_and_communication.entitites.EnergyCons;
import com.morello.monitoring_and_communication.services.EnergyConsService;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Component
public class Consumer {
    private static final String queue_name = "energy_data_queue";

    @Autowired
    private EnergyConsService energyConsService;

    @Bean
    public void messageConsumer() throws URISyntaxException, NoSuchAlgorithmException, KeyManagementException, IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUri("amqps://egsbshkj:NjCKxQdp@goose.rmq2.cloudamqp.com/egsbj");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(queue_name, false, false, false, null);

        DeliverCallback retrieveCons = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            ObjectMapper objectMapper = new ObjectMapper();
            Map map = objectMapper.readValue(message, Map.class);
            EnergyCons energyCons = new EnergyCons();
            energyCons.setDeviceId((Integer) map.get("device_id"));
            Timestamp time = new Timestamp((Long) map.get("timestamp"));
            energyCons.setTimestamp(new Date(time.getTime()));
            energyCons.setMeasurement_value(Double.parseDouble(map.get("measurement_value").toString()));
            energyConsService.addEnergyCons(energyCons);
            System.out.println(" [x] Received '" + message + "'");
        };

        channel.basicConsume(queue_name, true, retrieveCons, consumerTag -> { });
    }
}
