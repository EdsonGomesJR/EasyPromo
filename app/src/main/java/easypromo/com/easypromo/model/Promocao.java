package easypromo.com.easypromo.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.utils.L;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import easypromo.com.easypromo.R;
import easypromo.com.easypromo.adapter.PromocaoAdapter;
import easypromo.com.easypromo.config.AcessoDatabase;
import easypromo.com.easypromo.helper.Utilidades;


public class Promocao implements Comparable<Promocao>, Parcelable {

    // region Attributes
    private boolean mUsuarioAtualLike;
    private Context mContext;


    private String id;
    private String url;
    private String nome;
    private Double preco;
    private String categoria;
    private String status; // (1) Aprovada - (0) Pendente
    private String dataCadastro;
    private String imagem_path;
    private int qtdCurtidas;
    // endregion

    // region Constructors
    public Promocao() {
    }

    public Promocao(String id, int qtdCurtidas) {
        setId(id);
        setQtdCurtidas(qtdCurtidas);
    }

    public Promocao(String id, String url, String nome, Double preco, String categoria, String status, String imagem, int qtdCurtidas) {
        setId(id);
        setUrl(url);
        setNome(nome);
        setPreco(preco);
        setCategoria(categoria);
        setStatus(status);
        setDataCadastro(Utilidades.getHoraAtual());
        setImagem_path(imagem);
        setQtdCurtidas(qtdCurtidas);
    }
    // endregion

    protected Promocao(Parcel in) {
        id = in.readString();
        url = in.readString();
        nome = in.readString();
        if (in.readByte() == 0) {
            preco = null;
        } else {
            preco = in.readDouble();
        }
        categoria = in.readString();
        status = in.readString();
        dataCadastro = in.readString();
        imagem_path = in.readString();
        qtdCurtidas = in.readInt();
    }

    public static final Creator<Promocao> CREATOR = new Creator<Promocao>() {
        @Override
        public Promocao createFromParcel(Parcel in) {
            return new Promocao(in);
        }

        @Override
        public Promocao[] newArray(int size) {
            return new Promocao[size];
        }
    };

    // region Methods
    public void cadastrar() {
        DatabaseReference dbReferencia = AcessoDatabase.getReferencia();
        dbReferencia.child("promocoes").child(getId()).setValue(this);

    }

    public void aprovar() {
        DatabaseReference dbReferencia = AcessoDatabase.getReferencia();
        dbReferencia.child("promocoes").child(getId()).child("status").setValue("1");
    }

    public void recusar() {
        DatabaseReference dbReferencia = AcessoDatabase.getReferencia();
        dbReferencia.child("promocoes").child(getId()).child("status").setValue("2");
    }


    public void curtir() {
        DatabaseReference dbReferencia = AcessoDatabase.getReferencia();
        int curtidas = (getQtdCurtidas() + 1);
        dbReferencia.child("promocoes").child(getId()).child("qtdCurtidas").setValue(curtidas);

    }
    public void desCurtir() {
        DatabaseReference dbReferencia = AcessoDatabase.getReferencia();
        int curtidas = (getQtdCurtidas() - 1);
        dbReferencia.child("promocoes").child(getId()).child("qtdCurtidas").setValue(curtidas);

    }

    public void likes() {

        DatabaseReference dbReferencia = AcessoDatabase.getReferencia();

        String newLike = dbReferencia.push().getKey();
        Like like = new Like();
        like.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());

        dbReferencia.child("promocoes").child(getId()).child("likes").child(newLike).setValue(like);


    }

    public void verificarLikes() {

        final DatabaseReference dbReferencia = AcessoDatabase.getReferencia();

        Query query = dbReferencia.child("promocoes").child(getId()).child("likes");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    likes();
                } else {
                    String valor = "";
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        System.out.println("Veridicar Like !!!!!! " + ds.getValue(Like.class).getUser_id());
                        valor = ds.getValue(Like.class).getUser_id();


                    }
                    if (!valor.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                        likes();

                    } else {

                        removerLike();
                    }

                }
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void removerLike() {

        DatabaseReference dbReferencia = AcessoDatabase.getReferencia();
        Query query = dbReferencia.child("promocoes").child(getId()).child("likes");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DatabaseReference dbReferencia = AcessoDatabase.getReferencia();

                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

                    String keyId = singleSnapshot.getKey();
                    String keyValue = singleSnapshot.getValue(Like.class).getUser_id();
                    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    System.out.println("VALOR DO KEY VALUE  = " + keyValue + " VALOR DO KEYID " + keyId + " Valor do currentUser  " + currentUser);

                    if (keyValue.equals(currentUser)) {

                        dbReferencia.child("promocoes").child(getId()).child("likes").child(keyId).removeValue();

                    }

                    }

                }





            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


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

    @Override
    public int compareTo(Promocao outraPromocao) {
        Date atualDataCadastroFormatada = null;
        Date outraDataCadastroFormatada = null;

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            atualDataCadastroFormatada = formato.parse(this.dataCadastro);
            outraDataCadastroFormatada = formato.parse(outraPromocao.dataCadastro);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (atualDataCadastroFormatada.before(outraDataCadastroFormatada)) {
            return 1;
        } else if (atualDataCadastroFormatada.after(outraDataCadastroFormatada)) {
            return -1;
        }

        return 0;
    }
    // endregion

    // region Getters & Setters
    public String getId() {
        return id;
    }

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

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(String dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getImagem_path() {
        return imagem_path;
    }

    public void setImagem_path(String imagem_path) {
        this.imagem_path = imagem_path;
    }

    public int getQtdCurtidas() {
        return qtdCurtidas;
    }

    public void setQtdCurtidas(int qtdCurtidas) {
        this.qtdCurtidas = qtdCurtidas;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(url);
        dest.writeString(nome);
        if (preco == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(preco);
        }
        dest.writeString(categoria);
        dest.writeString(status);
        dest.writeString(dataCadastro);
        dest.writeString(imagem_path);
        dest.writeInt(qtdCurtidas);
    }
    // endregion
}