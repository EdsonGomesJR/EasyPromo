package easypromo.com.easypromo.model;

import com.google.firebase.database.DatabaseReference;

import easypromo.com.easypromo.config.AcessoDatabase;
import easypromo.com.easypromo.helper.Utilidades;


    public class Promocao {

        // region Attributes
        private String id;
        private String url;
        private String nome;
        private Double preco;
        private String categoria;
        private String status; // (1) Aprovada - (0) Pendente
        private String dataCadastro;
        private String imagem_path;
        // endregion

        // region Constructors
        public Promocao(){}

        public Promocao(String id, String url, String nome, Double preco, String categoria, String status, String imagem) {
            setId(id);
            setUrl(url);
            setNome(nome);
            setPreco(preco);
            setCategoria(categoria);
            setStatus(status);
            setDataCadastro(Utilidades.getHoraAtual());
            setImagem_path(imagem);
        }
        // endregion

        // region Methods
        public void cadastrar(){
            DatabaseReference dbReferencia = AcessoDatabase.getReferencia();
            dbReferencia.child("promocoes").child(getId()).setValue(this);
        }

        public void aprovar(){
            DatabaseReference dbReferencia = AcessoDatabase.getReferencia();
            dbReferencia.child("promocoes").child(getId()).child("status").setValue("1");
        }

        public void recusar(){
            DatabaseReference dbReferencia = AcessoDatabase.getReferencia();
            dbReferencia.child("promocoes").child(getId()).child("status").setValue("2");
        }
        // endregion

        // region Getters & Setters
        public String getId() { return id; }

        public void setId(String id) {
            this.id = id;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {

            if (!url.startsWith("http://") && !url.startsWith("https://")){
                url = "http://" + url;
            }

            this.url = url;
        }

        public Double getPreco() {
            return preco;
        }

        public void setPreco(Double preco) {
            this.preco = preco;
        }

        public String getCategoria() {
            return categoria;
        }

        public void setCategoria(String categoria) {
            this.categoria = categoria;
        }

        public String getStatus() { return status; }

        public void setStatus(String status) { this.status = (status.isEmpty()) ? "0" : "1"; }

        public String getDataCadastro() { return dataCadastro; }

        public void setDataCadastro(String dataCadastro) { this.dataCadastro = dataCadastro; }
        // endregion

        @Override
        public String toString() {
            return "Promocao{" +
                    "id='" + id + '\'' +
                    ", url='" + url + '\'' +
                    ", nome='" + nome + '\'' +
                    ", preco=" + preco +
                    ", categoria='" + categoria + '\'' +
                    ", status='" + status + '\'' +
                    ", dataCadastro='" + dataCadastro + '\'' +
                    ", imagem_path='" + imagem_path + '\'' +
                    '}';
        }

        public String getImagem_path() {
            return imagem_path;
        }

        public void setImagem_path(String imagem_path) {
            this.imagem_path = imagem_path;
        }

        public String getLoja(){
            return "";
        }
    }