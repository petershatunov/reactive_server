package com.example.reactive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import reactor.core.publisher.Flux;

@Configuration
class Config {

    @Autowired
    private ReservationRepository reservationRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void init(ApplicationReadyEvent event) {
        Flux<Reservation> reservationFlux = Flux
                .just("abc", "cde", "efg", "fgh")
                .map(name -> new Reservation(null, name))
                .flatMap(reservationRepository::save);

        reservationRepository.deleteAll()
                .thenMany(reservationFlux)
                .thenMany(reservationRepository.findAll())
                .subscribe(System.out::println);
    }
}
