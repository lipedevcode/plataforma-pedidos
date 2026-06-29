package com.pedidosplataforma.reservarestoque.component;

import com.google.gson.Gson;
import com.pedidosplataforma.reservarestoque.Pedido;
import com.pedidosplataforma.reservarestoque.service.ReservarEstoqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservarEstoqueReceiver {

    private final Gson gson;
    private final ReservarEstoqueService reservarEstoqueService;

    public void receiveMessage(String message) {
        Pedido pedido = gson.fromJson(message, Pedido.class);
        this.reservarEstoqueService.reservarEstoque(pedido);
    }
}
