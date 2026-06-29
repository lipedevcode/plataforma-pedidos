package com.pedidosplataforma.pedidosservice.controller;

import com.google.gson.Gson;
import com.pedidosplataforma.pedidosservice.data.Pedido;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final RabbitTemplate rabbitTemplate;
    private final Gson gson;


    @PostMapping
    public ResponseEntity<String> criarPedido(@RequestBody Pedido pedido) {
        // Lógica para criar o pedido
        log.info("Enviando mensagem de pedido efetuado para os consumidores: {}", pedido);
        rabbitTemplate.convertAndSend("pedidos-exchange", "", gson.toJson(pedido));
        return ResponseEntity.ok("Pedido criado com sucesso!");
    }
}
