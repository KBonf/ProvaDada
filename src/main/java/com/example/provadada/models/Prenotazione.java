package com.example.provadada.models;

public class Prenotazione {
    private int id;
    private String nome;
    private String email;
    private int numero_persone;
    private String data;
    private String orario;
    private String created_at;
    private String status;
    private String tavoli;

    // Getters e setters

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public int getNumero_persone() {
        return numero_persone;
    }
    public void setNumero_persone(int numero_persone) {
        this.numero_persone = numero_persone;
    }
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
    public String getOrario() {
        return orario;
    }
    public void setOrario(String orario) {
        this.orario = orario;
    }
    public String getCreated_at() {
        return created_at;
    }
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getTavoli() {
        return tavoli;
    }
    public void setTavoli(String tavoli) {
        this.tavoli = tavoli;
    }
}
