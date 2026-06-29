package com.pedidosplataforma.notificacaoservice.component;

import com.google.gson.Gson;
import com.pedidosplataforma.notificacaoservice.Pedido;
import com.pedidosplataforma.notificacaoservice.service.NotificacaoService;
//import com.pedidosplataforma.pedidosservice.data.Pedido;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificacaoReceiver {

    private final Gson gson;
    private final NotificacaoService notificacaoService;

    public void receiveMessage(String message){
        Pedido pedido = gson.fromJson(message, Pedido.class);
        this.notificacaoService.enviarNotificacao(pedido);
    }
}
