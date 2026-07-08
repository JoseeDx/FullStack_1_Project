package com.tienda.ms_producto;

import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
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

    DataLoader(CategoriaRepository categoriaRepository, ProductoRepository productoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (categoriaRepository.count() > 0 || productoRepository.count() > 0) {
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
    }

    private Categoria nuevaCategoria(String nombre) {
        Categoria categoria = new Categoria();
        categoria.setNombre_categoria(nombre);
        categoria.setActivo(true);
        return categoria;
    }
}
