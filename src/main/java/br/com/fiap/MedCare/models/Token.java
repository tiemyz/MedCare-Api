package br.com.fiap.MedCare.models;

public record Token(
    String token,
    String type,
    String prefix
) {}