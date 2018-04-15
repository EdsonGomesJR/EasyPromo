package easypromo.com.easypromo.model;

public class Promocao {

    // region Attributes
    private Integer id;
    private String produto;
    private String url;
    private String precoOriginal;
    private String precoPromocional;
    private String categoria;
    private String comentarios;
    // endregion

    // region Constructors
    public Promocao() {

    }
    // endregion

    // region Methods
    // endregion

    // region Getters & Setters
    private Integer getId() {
        return id;
    }

    private void setId(Integer id) {
        this.id = id;
    }

    private String getProduto() {
        return produto;
    }

    private void setProduto(String produto) {
        this.produto = produto;
    }

    private String getUrl() {
        return url;
    }

    private void setUrl(String url) {
        this.url = url;
    }

    private String getPrecoOriginal() {
        return precoOriginal;
    }

    private void setPrecoOriginal(String precoOriginal) {
        this.precoOriginal = precoOriginal;
    }

    private String getPrecoPromocional() {
        return precoPromocional;
    }

    private void setPrecoPromocional(String precoPromocional) {
        this.precoPromocional = precoPromocional;
    }

    private String getCategoria() {
        return categoria;
    }

    private void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    private String getComentarios() {
        return comentarios;
    }

    private void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }
    // endregion
}
