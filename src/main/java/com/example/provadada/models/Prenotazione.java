package com.example.provadada.models;

import com.google.gson.annotations.SerializedName;

public class Prenotazione {
    @SerializedName("id")
    private int id;

    @SerializedName("nome")
    private String nome;

    @SerializedName("email")
    private String email;

    @SerializedName("numero_persone")
    private int numeroPersone;

    @SerializedName("data")
    private String data;

    @SerializedName("orario")
    private String orario;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("status")
    private String status;

    @SerializedName("tavoli")
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
    public int getNumeroPersone() {
        return numeroPersone;
    }
    public void setNumeroPersone(int numeroPersone) {
        this.numeroPersone = numeroPersone;
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
    public String getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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

    @Override
    public String toString() {
        return "Prenotazione{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", numeroPersone=" + numeroPersone +
                ", data='" + data + '\'' +
                ", orario='" + orario + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", status='" + status + '\'' +
                ", tavoli='" + tavoli + '\'' +
                '}';
    }
}
