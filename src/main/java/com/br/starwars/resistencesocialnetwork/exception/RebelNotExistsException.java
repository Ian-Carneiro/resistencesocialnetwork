package com.br.starwars.resistencesocialnetwork.exception;

public class RebelNotExistsException extends Exception {
    public RebelNotExistsException(){
        super("Rebel not found.");
    }
}
