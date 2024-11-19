package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.Autor;
import com.aluracursos.literalura.model.Idioma;
import com.aluracursos.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    @Query("SELECT a FROM Libro l JOIN l.autor a WHERE a.nombre LIKE %:nombre%")
    Optional<Autor> buscarAutorPorNombre(@Param("nombre") String nombre);

    @Query("SELECT l FROM Libro l JOIN l.autor a WHERE l.titulo LIKE %:nombre%")
    Optional<Libro> buscarLibroPorNombre(@Param("nombre") String nombre);

    @Query("SELECT a FROM Autor a WHERE a.fechaMuerte >= :fecha")
    List<Autor> buscarAutorPorAnio(@Param("fecha") Integer fecha);

    @Query("SELECT l FROM Libro l WHERE l.idioma= :idioma")
    List<Libro> buscarLibroPorIdioma(@Param("idioma") Idioma idioma);

    @Query("SELECT l FROM Autor a JOIN a.libro l")
    List<Libro> buscarTodosLosLibros();

    @Query("SELECT a FROM Libro l JOIN l.autor a")
    List<Autor> buscarTodosLosAutores();
}
