package com.example.ms_pedido;

import com.example.ms_pedido.model.Pedido;
import com.example.ms_pedido.repository.PedidoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Arrays;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner initData(PedidoRepository pedidoRepository) {
        return args -> {
            // Regla de Oro: Solo insertamos si la tabla está vacía para no duplicar datos
            if (pedidoRepository.count() == 0) {
                
                Pedido pedido1 = new Pedido();
                // El idPedido es autogenerado, por lo que solo asignamos la fecha
                pedido1.setFechaPedido(LocalDateTime.now());

                Pedido pedido2 = new Pedido();
                pedido2.setFechaPedido(LocalDateTime.now().minusDays(2));

                Pedido pedido3 = new Pedido();
                pedido3.setFechaPedido(LocalDateTime.now().minusHours(5));

                // Guardamos todos los registros en la base de datos
                pedidoRepository.saveAll(Arrays.asList(pedido1, pedido2, pedido3));
                
                System.out.println("✅ DataLoader: Datos de prueba insertados en ms-pedido exitosamente.");
            } else {
                System.out.println("⚡ DataLoader: La tabla de pedidos ya contiene datos. Omitiendo inserción.");
            }
        };
    }
}