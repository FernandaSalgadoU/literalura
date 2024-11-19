package com.aluracursos.literalura.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    String nombre;
    Integer fechaMuerte;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libro;

    public Autor() {
    }

    public Autor(DatosAutor datosAutor) {
        this.nombre = datosAutor.nombre();
        this.fechaMuerte = datosAutor.fechaMuerte();
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getFechaMuerte() {
        return fechaMuerte;
    }
    public void setFechaMuerte(Integer fechaMuerte) {
        this.fechaMuerte = fechaMuerte;
    }

    public List<Libro> getLibro() {
        return libro;
    }

    //a cada elemento libro del listado, le otorgo a setAutor lo almacenado en esta instancia. Es decir, se considerará
    //id, nombre y fechaMuerte. Una vez hecho eso, el libro de esta instancia se iguala al libro modificado.

    public void setLibro(List<Libro> libro) {
        libro.forEach(l->l.setAutor(this));
        this.libro=libro;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", nombre=" + nombre +
                ", fechaMuerte=" + fechaMuerte;
    }
}
