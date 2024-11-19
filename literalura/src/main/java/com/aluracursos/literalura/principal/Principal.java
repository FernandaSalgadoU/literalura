package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.model.*;
import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI=new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner teclado = new Scanner(System.in);
    private AutorRepository repository;
    private List<Autor> lAutor;
    private List<Libro> lLibro;

    public Principal(AutorRepository repository) {
        this.repository = repository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar libro por titulo
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivosAnio();
                    break;
                case 5:
                    listaLibroPorIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }

    public void buscarLibroPorTitulo(){
        System.out.println("Ingrese nombre de libro que desea buscar");
        var tituloLibro=teclado.nextLine();

        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + tituloLibro.replace(" ","+"));
        var datos = conversor.obtenerDatos(json, Datos.class);

        Optional<DatosLibro> libroBuscado=datos.resultados().stream()
                .filter(t->t.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                .findFirst();

        if(libroBuscado.isPresent()){
            System.out.println("Libro encontrado: ");

            //comprobar si existe el libro y autor en la BD
            Optional<Libro> libroBD = repository.buscarLibroPorNombre(tituloLibro);

            Optional<Autor> autorBD = repository.buscarAutorPorNombre(
                    libroBuscado.get().autor().stream()
                            .map(a -> a.nombre())
                            .collect(Collectors.joining()));

            if(libroBD.isPresent()){
                System.out.println("El libro ya se encuentra almacenado en la BD.");
            }
            else{
                Autor autor;
                if(autorBD.isPresent()){
                    autor=autorBD.get();
                    System.out.println("Autor ya se encuentra almacenado en la BD.");
                }
                else{
                    Autor autorEncontrado = libroBuscado.stream()
                            .flatMap(dL -> dL.autor().stream()
                                    .map(dA-> new Autor(dA)))
                            .collect(Collectors.toList()).stream()
                            .findFirst().get();

                    autor=autorEncontrado;
                    repository.save(autorEncontrado);
                }

                List<Libro> libroEncontrado = libroBuscado.stream()
                        .map(dL->new Libro(dL))
                        .collect(Collectors.toList());

                autor.setLibro(libroEncontrado);
                repository.save(autor);
            }

        }else{
            System.out.println("Libro no encontrado.");
        }
    }
    public void listarLibrosRegistrados() {
        lLibro = repository.buscarTodosLosLibros();

        System.out.println("''''''LIBROS REGISTRADOS''''''");
        caracteristicasLibros(lLibro);
        System.out.println("\n'''''''''''''''''''''''''''''''");
    }
    public void listarAutoresRegistrados() {
        lAutor = repository.buscarTodosLosAutores();
        System.out.println("''''''AUTORES REGISTRADOS''''''");
        caracteristicasAutores(lAutor);
        System.out.println("\n'''''''''''''''''''''''''''''''");
    }
    public void listarAutoresVivosAnio() {
        System.out.println("Por favor, ingresa a partir de que año quieres identificar los autores vivos");
        var Anio=teclado.nextInt();
        teclado.nextLine();

        lAutor= repository.buscarAutorPorAnio(Anio);
        System.out.println("''''''AUTORES REGISTRADOS QUE SE ENCONTRABAN VIVOS EN EL AÑO " + Anio +"''''''");
        caracteristicasAutores(lAutor);
        System.out.println("\n'''''''''''''''''''''''''''''''");
    }
    public void listaLibroPorIdioma() {
        System.out.println("Escribe el idioma de los libros que deseas buscar");
        var textoIdioma =teclado.nextLine();
        Idioma idioma = Idioma.fromString(textoIdioma);

        lLibro = repository.buscarLibroPorIdioma(idioma);
        System.out.println("''''''LIBROS CON IDIOMA " + idioma + " REGISTRADOS''''''");
        caracteristicasLibros(lLibro);
        System.out.println("\n'''''''''''''''''''''''''''''''");

    }

    public void caracteristicasLibros(List<Libro> lLibro){
        lLibro.forEach(
                l -> System.out.println(
                        "\nTítulo: " + l.getTitulo() +
                                "\nAutor: " + l.getAutor().getNombre() +
                                "\nIdioma: " + l.getIdioma().getIdioma() +
                                "\nN° de descargas: " + l.getDescargas())
        );
    }

    public void caracteristicasAutores(List<Autor> lAutor){
        lAutor.forEach(
                a -> System.out.println(
                        "\nNombre: " + a.getNombre() +
                                "\nAño muerte: " + a.getFechaMuerte() +
                                "\nLibros: " + a.getLibro().stream().map(l->l.getTitulo())
                                .collect(Collectors.toList()))
        );
    }
}
