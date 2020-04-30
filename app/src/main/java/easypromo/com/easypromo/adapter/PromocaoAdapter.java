package easypromo.com.easypromo.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import easypromo.com.easypromo.R;

import easypromo.com.easypromo.activity.IrLojaActivity;
import easypromo.com.easypromo.activity.PrincipalActivity;
import easypromo.com.easypromo.config.AcessoDatabase;
import easypromo.com.easypromo.helper.Utilidades;
import easypromo.com.easypromo.model.Like;
import easypromo.com.easypromo.model.Promocao;
import easypromo.com.easypromo.model.Usuario;

public class PromocaoAdapter extends RecyclerView.Adapter<PromocaoAdapter.PromocaoViewHolder> {

    private Context mContext;
    private List<Promocao> mPromocoes;
    private DatabaseReference dbReferencia;
    private String stringDeLikes = "";
    private StringBuilder mStringBuilder;

    public PromocaoAdapter(Context context, List<Promocao> promocoes) {

        mContext = context;
        mPromocoes = promocoes;
    }

    public static class PromocaoViewHolder extends RecyclerView.ViewHolder {

        public TextView mTitulo;
        public TextView mPreco;
        public TextView qtdCurtidas;
        public ImageView imgOferta;
        public TextView mUrlOfertaVindaLoja;
        public ImageView mAproveAction;
        public ImageView mReproveAction;

        public Button btnIrLoja;
        public ImageButton btLike;

        public PromocaoViewHolder(View itemView) {
            super(itemView);
            mUrlOfertaVindaLoja = itemView.findViewById(R.id.oferta_vinda_loja);
            mTitulo = itemView.findViewById(R.id.titulo_oferta);
            mPreco = itemView.findViewById(R.id.oferta_preco);
            btnIrLoja = itemView.findViewById(R.id.btn_card_ir_para_loja);
            qtdCurtidas = itemView.findViewById(R.id.like_count);
            imgOferta = itemView.findViewById(R.id.card_oferta_foto);
            mAproveAction = itemView.findViewById(R.id.btAprove);
            mReproveAction = itemView.findViewById(R.id.btReprove);
            btLike = itemView.findViewById(R.id.btLike);
        }
    }

    @Override
    public PromocaoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.view_card_oferta, parent, false);

        dbReferencia = AcessoDatabase.getReferencia();

        return new PromocaoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final PromocaoViewHolder holder, int position) {
        final Promocao promocaoCorrente = mPromocoes.get(position);
        dbReferencia.keepSynced(true);
        holder.mUrlOfertaVindaLoja.setText(Utilidades.enderecoLoja(promocaoCorrente.getUrl()));
        holder.mTitulo.setText(promocaoCorrente.getNome());
        holder.mPreco.setText("R$ " + Utilidades.precoFormatado(promocaoCorrente.getPreco()));
        // holder.qtdCurtidas.setText(String.valueOf(promocaoCorrente.getQtdCurtidas()));

        Picasso.with(mContext)
                .load(promocaoCorrente.getImagem_path())
                .placeholder(R.drawable.banner)
                .fit()
                .centerCrop()
                .into(holder.imgOferta);

        holder.btnIrLoja.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(mContext, IrLojaActivity.class);
                intent.putExtra("ABRE", promocaoCorrente.getUrl());
                mContext.startActivity(intent);
            }

        });

        Query query = dbReferencia.child("promocoes").child(promocaoCorrente.getId())
                .child("likes");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mStringBuilder = new StringBuilder();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    mStringBuilder.append(ds.getValue(Like.class).getUser_id());
                    mStringBuilder.append(",");


                    String[] split = mStringBuilder.toString().split(",");

                    int stringDeLikes = + split.length;
                    holder.qtdCurtidas.setText(String.valueOf(stringDeLikes));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        holder.btLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final DatabaseReference dbReferencia = AcessoDatabase.getReferencia();

                Query query = dbReferencia.child("promocoes").child(promocaoCorrente.getId()).child("likes");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (!dataSnapshot.exists()) {
                            promocaoCorrente.likes();


                        } else {
                            String valor = "";
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                valor = ds.getValue(Like.class).getUser_id();


                            }
                            if (!valor.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                                promocaoCorrente.likes();


                            } else {

                                promocaoCorrente.removerLike();

                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


//                String id = promocaoCorrente.getId();
//                int qtdCurtidas = promocaoCorrente.getQtdCurtidas();
//
//                Promocao promocao = new Promocao(id, qtdCurtidas);
                // promocao.curtir();
                // promocao.verificarLikes();
                mPromocoes.clear();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPromocoes.size();
    }
}

