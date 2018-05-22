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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import easypromo.com.easypromo.R;

import easypromo.com.easypromo.activity.IrLojaActivity;
import easypromo.com.easypromo.activity.PrincipalActivity;
import easypromo.com.easypromo.model.Promocao;
import easypromo.com.easypromo.model.Usuario;

public  class PromocaoAdapter extends RecyclerView.Adapter<PromocaoAdapter.PromocaoViewHolder> {

    private static final String TAG = "PromocaoAdapter";
    private Context mContext;
    private List<Promocao> mPromocoes;
IrLojaActivity irLojaActivity = new IrLojaActivity();


    public PromocaoAdapter(Context context, List<Promocao> promocoes){

        mContext = context;
        mPromocoes = promocoes;
    }

    public static class PromocaoViewHolder extends  RecyclerView.ViewHolder  {

        public TextView mTitulo;
        public TextView mPreco;
        public TextView numeroCurtidas;
        public ImageView imgOferta;
        public TextView mUrlOfertaVindaLoja;
        public ImageView mLikeAction;
        public ImageView mAproveAction;
        public ImageView mReproveAction;

        //public  CardView mCard;
        public  Button btnIrLoja;

        public PromocaoViewHolder(View itemView) {
            super(itemView);
            mUrlOfertaVindaLoja = itemView.findViewById(R.id.oferta_vinda_loja);
            mTitulo = itemView.findViewById(R.id.titulo_oferta);
            mPreco = itemView.findViewById(R.id.oferta_preco);
            btnIrLoja = itemView.findViewById(R.id.btn_card_ir_para_loja);
            numeroCurtidas = itemView.findViewById(R.id.like_count);
            mLikeAction = itemView.findViewById(R.id.card_like_icon);
            imgOferta = itemView.findViewById(R.id.card_oferta_foto);
            mAproveAction = itemView.findViewById(R.id.btAprove);
            mReproveAction = itemView.findViewById(R.id.btReprove);
        }
    }

    @Override
    public PromocaoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
     View v = LayoutInflater.from(mContext).inflate(R.layout.view_card_oferta, parent, false);

     return  new PromocaoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PromocaoViewHolder holder, int position) {
        final Promocao promocaoCorrente = mPromocoes.get(position);
        holder.mUrlOfertaVindaLoja.setText(promocaoCorrente.getUrl());
        holder.mTitulo.setText(promocaoCorrente.getNome());
        holder.mPreco.setText("R$ " + precoFormatado(promocaoCorrente.getPreco()));
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


    }


    @Override
    public int getItemCount() {
        return mPromocoes.size();
    }



    private String precoFormatado(Double valor){
        Locale locale = new Locale("pt", "BR");
        NumberFormat nf = NumberFormat.getInstance(locale);
        nf.setMaximumFractionDigits(2);
        return nf.format(valor);
    }





}

