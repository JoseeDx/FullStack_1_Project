package com.tienda.ms_producto;

import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.tienda.ms_producto.model.Categoria;
import com.tienda.ms_producto.model.Producto;
import com.tienda.ms_producto.repository.CategoriaRepository;
import com.tienda.ms_producto.repository.ProductoRepository;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;
    private final JdbcTemplate jdbcTemplate;

    DataLoader(CategoriaRepository categoriaRepository, ProductoRepository productoRepository, JdbcTemplate jdbcTemplate) {
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        // Marca de control propia, independiente de cuántas filas haya insertado Liquibase
        Boolean yaSembrado = jdbcTemplate.queryForObject(
                "SELECT datafaker_seeded FROM seed_control WHERE id = 1", Boolean.class);
        if (Boolean.TRUE.equals(yaSembrado)) {
            return;
        }

        Faker faker = new Faker();

        List<Categoria> categorias = List.of(
                nuevaCategoria("Teclados"),
                nuevaCategoria("Mouses"),
                nuevaCategoria("Auriculares"),
                nuevaCategoria("Monitores")
        );
        categoriaRepository.saveAll(categorias);

        for (int i = 1; i <= 10; i++) {
            Producto producto = new Producto();
            producto.setNombre_producto(faker.commerce().productName());
            producto.setDescripcion_producto(faker.lorem().sentence());
            producto.setPrecio_producto((double) faker.number().numberBetween(9990, 299990));
            producto.setCategoria(categorias.get(faker.number().numberBetween(0, categorias.size())));
            producto.setActivo(true);

            productoRepository.save(producto);
        }

        jdbcTemplate.update("UPDATE seed_control SET datafaker_seeded = TRUE WHERE id = 1");
    }

    private Categoria nuevaCategoria(String nombre) {
        Categoria categoria = new Categoria();
        categoria.setNombre_categoria(nombre);
        categoria.setActivo(true);
        return categoria;
    }
}
