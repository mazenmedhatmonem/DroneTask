package com.musalasoft.musalsoftDrone.seeder;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ModelSeeder implements Seeder{

    private final List<Seeder> seeders = new ArrayList<>();

    public ModelSeeder(
            DroneModelSeeder droneModelSeeder
    ) {
        seeders.add(droneModelSeeder);
    }

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seed();
    }

    @Override
    public void seed() {

        for (Seeder seeder : seeders) {
            seeder.seed();
        }
    }
}
