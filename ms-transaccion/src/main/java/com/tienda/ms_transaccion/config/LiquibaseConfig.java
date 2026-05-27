package com.tienda.ms_transaccion.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;

@Configuration //clase que contiene configuraciones
public class LiquibaseConfig {

    @Bean // crea un objeto que spring debe gestionar, data source es la conexion a la base de datos
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase(); // crea una instancia liquibase
        liquibase.setDataSource(dataSource); //le dice a liquibase que base de datos usar (la que esta en properties)
        liquibase.setChangeLog("classpath:db/changelog/db.changelog.sql");//le dice donde esta el archivo con los chagesets
        //con el classpath: le indica que busqque dentro de resources
        liquibase.setShouldRun(true); //le indica que si debe ejecutarse al arrancar el proyecto
        return liquibase; //retorna objeto configurado
    }

    // sin esto spring no sabria que debe usar liquibase ni donde usar los changesets,
    //esta clase conecta Spring con liquibase al arrancar la aplicacion
    //Al arrancar liquibase lee el archivo db.changelog.sql, revisa la tabla DATABASECHANGELOG para ver qué changesets ya ejecutó, y solo ejecuta los nuevos.
    //Por eso nunca duplica datos ni tablas 
}