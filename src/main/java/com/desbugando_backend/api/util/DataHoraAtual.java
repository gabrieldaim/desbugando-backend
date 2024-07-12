package com.desbugando_backend.api.util;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class DataHoraAtual {
    private Date dataCriacao;

    public DataHoraAtual(){
        // Obtém a data e hora atuais
        LocalDateTime now = LocalDateTime.now();
        // Converte LocalDateTime para Date
        this.dataCriacao = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
    }

    public Date getDataCriacao(){
        // Obtém a data e hora atuais
        LocalDateTime now = LocalDateTime.now();
        // Converte LocalDateTime para Date
        this.dataCriacao = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        return this.dataCriacao;
    }
}
