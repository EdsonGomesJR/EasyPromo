package easypromo.com.easypromo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import easypromo.com.easypromo.R;
import easypromo.com.easypromo.adapter.PromocaoAdapter;
import easypromo.com.easypromo.model.Promocao;

public class IrLojaActivity extends AppCompatActivity {
    private WebView mWebView;
    private DatabaseReference dbReference;
    private Context mContext;
    private List<Promocao> promolist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ir_loja);

    setWebViewPost(getIntent().getStringExtra("ABRE"));

    }
    public  void  setWebViewPost (String webViewPost){
        mWebView =  findViewById(R.id.webView);
        mWebView.loadUrl(webViewPost);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);

    }
}

