package com.pedidosplataforma.pedidosservice.config;

import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PedidoConfig {

    @Bean
    public Gson gson() {
        return new Gson();
    }
}
