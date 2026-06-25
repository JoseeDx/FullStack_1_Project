package com.tienda.ms_facturacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsFacturacionApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsFacturacionApplication.class, args);
	}

}
