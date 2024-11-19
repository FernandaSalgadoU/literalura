package com.aluracursos.literalura.model;

public enum Idioma {
    ES("es"),
    EN("en"),
    PT("pt"),
    FR("fr");

    private String idioma;

    Idioma(String idioma) {
        this.idioma = idioma;
    }

    public String getIdioma() {
        return idioma;
    }

    public static Idioma fromString(String texto) {
        for (Idioma idioma : Idioma.values()) {
            if (idioma.idioma.equalsIgnoreCase(texto)) {
                return idioma;
            }
        }
        throw new IllegalArgumentException("No fue encontrado el idioma " + texto);
    }
}
