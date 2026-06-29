package com.pedidosplataforma.processarpagamentoservice.service;

import com.pedidosplataforma.processarpagamentoservice.Pedido;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProcessarPagamentoService {

    public void processarPagamento(Pedido pedido) {
        log.info("Processando pagamento para o pedido: {}", pedido);
        // Lógica para processar o pagamento do pedido
    }
}
