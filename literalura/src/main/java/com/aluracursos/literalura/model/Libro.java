package com.aluracursos.literalura.model;

import jakarta.persistence.*;

import java.util.stream.Collectors;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    private Long id;

    private String titulo;

    @ManyToOne
    private Autor autor;

    @Enumerated(EnumType.STRING)
    private Idioma idioma;
    private Integer descargas;

    public Libro() {
    }

    public Libro(DatosLibro datosLibro) {
        this.id=datosLibro.id();
        this.titulo = datosLibro.titulo();
        this.idioma=Idioma.fromString(datosLibro.idioma().stream()
                .limit(1).collect(Collectors.joining()));
        this.descargas = datosLibro.numeroDescargas();
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }
    public void setAutor(Autor autor) {
       this.autor=autor;
    }

    public Idioma getIdioma() {
        return idioma;
    }
    public void setIdioma(Idioma idioma) {
        this.idioma = idioma;
    }

    public Integer getDescargas() {
        return descargas;
    }
    public void setDescargas(Integer descargas) {
        this.descargas = descargas;
    }



    @Override
    public String toString() {
        return "id=" + id +
                ", titulo=" + titulo +
                ", autor=" + autor +
                ", idioma=" + idioma +
                ", descargas=" + descargas;
    }
}
