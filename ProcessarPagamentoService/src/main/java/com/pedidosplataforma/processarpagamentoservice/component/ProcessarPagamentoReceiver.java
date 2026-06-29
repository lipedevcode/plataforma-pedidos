package com.pedidosplataforma.processarpagamentoservice.component;

import com.google.gson.Gson;
//import com.pedidosplataforma.processarpagamentoservice.Pedido;
import com.pedidosplataforma.processarpagamentoservice.Pedido;
import com.pedidosplataforma.processarpagamentoservice.service.ProcessarPagamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProcessarPagamentoReceiver {

    private final Gson gson;
    private final ProcessarPagamentoService processarPagamentoService;

    public void receiveMessage(String message) {
        Pedido pedido = gson.fromJson(message, Pedido.class);
        this.processarPagamentoService.processarPagamento(pedido);
    }
}
