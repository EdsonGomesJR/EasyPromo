package easypromo.com.easypromo.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import easypromo.com.easypromo.config.AcessoDatabase;
import easypromo.com.easypromo.helper.Utilidades;

public class Usuario {

    private String id;
    private String nome;
    private String email;
    private String senha;
    private String dataCadastro;
    private String perfilAdmin; // (1) Admin - (0) Padrão

    public Usuario(String nome, String email, String senha, String perfilAdmin) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataCadastro = Utilidades.getHoraAtual();
        this.perfilAdmin = (perfilAdmin.isEmpty()) ? "0" : "1";
    }

    public Usuario(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    public void cadastrar(String id){
        setId(id);
        DatabaseReference dbReferencia = AcessoDatabase.getReferencia();
        dbReferencia.child("usuarios").child(getId()).setValue(this);
    }

    @Exclude
    public String getId() {
        return id;
    }

    private void setId(String id) { this.id = id; }

    public String getNome() {
        return nome;
    }

    private void setNome(String nome) {
        this.nome = nome;
    }

    @Exclude
    public String getEmail() { return email; }

    private void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() { return senha; }

    private void setSenha(String senha) {
        this.senha = senha;
    }

    public String getDataCadastro() { return dataCadastro; }

    private void setDataCadastro() { this.dataCadastro = dataCadastro; }

    public String getTipoPerfil() { return perfilAdmin; }

    private void setTipoPerfil(String perfilAdmin) { this.perfilAdmin = perfilAdmin; }
}
