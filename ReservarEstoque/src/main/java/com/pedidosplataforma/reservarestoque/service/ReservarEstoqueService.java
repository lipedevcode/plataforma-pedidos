package com.pedidosplataforma.reservarestoque.service;

import com.pedidosplataforma.reservarestoque.Pedido;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReservarEstoqueService {

    public void reservarEstoque(Pedido pedido){
        log.info("Reservando estoque do pedido {}", pedido);
        // Lógica para reservar o estoque do pedido
    }
}
