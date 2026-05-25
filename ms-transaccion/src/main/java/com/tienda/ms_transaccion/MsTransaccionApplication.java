package com.tienda.ms_transaccion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsTransaccionApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsTransaccionApplication.class, args);
	}

}
