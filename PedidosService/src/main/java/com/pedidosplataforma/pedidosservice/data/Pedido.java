package com.pedidosplataforma.pedidosservice.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;


@Getter
@Setter
@ToString
public class Pedido {
    private String nome;
    private Integer quantidade;
    private String tipo;
    private BigDecimal valorUnitario;
    private BigDecimal valorFrete;
    private String status;
    private Instant data;
}
