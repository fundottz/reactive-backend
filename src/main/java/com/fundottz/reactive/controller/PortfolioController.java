package com.fundottz.reactive.controller;

import com.fundottz.reactive.model.domain.Portfolio;
import com.fundottz.reactive.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class PortfolioController {

    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final PortfolioService portfolioService;

    @GetMapping("/portfolio/{account}")
    public SseEmitter handle(@PathVariable String account) {


        SseEmitter emitter = new SseEmitter();
        this.emitters.add(emitter);

        emitter.onCompletion(() -> this.emitters.remove(emitter));
        emitter.onTimeout(() -> this.emitters.remove(emitter));

        portfolioService.getPortfolio(account).ifPresent(portfolio -> {
            try {
                emitter.send(portfolio);
            } catch (IOException e) {
                log.error("Could not send portfolio to client", e);
            }
        });

        return emitter;
    }

    @EventListener
    public void onPortfolioUpdated(Portfolio portfolio) {
        List<SseEmitter> deadEmitters = new ArrayList<>();

        this.emitters.forEach(emitter -> {
            try {
                emitter.send(portfolio);
            } catch (Exception e) {
                deadEmitters.add(emitter);
            }
        });

        this.emitters.removeAll(deadEmitters);
    }
}
