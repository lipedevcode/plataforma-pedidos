package com.pedidosplataforma.notificacaoservice.service;

//import com.pedidosplataforma.pedidosservice.data.Pedido;
import com.pedidosplataforma.notificacaoservice.Pedido;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificacaoService {

    public void enviarNotificacao(Pedido pedido){
        log.info("Enviando notificação. {}", pedido);
        // Lógica para envio de notificação
    }
}
