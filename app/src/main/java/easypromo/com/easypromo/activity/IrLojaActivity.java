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



//        dbReference = FirebaseDatabase.getInstance().getReference("promocoes");
//        dbReference.keepSynced(true);
//
//        dbReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//
//                    Promocao promocao = postSnapshot.getValue(Promocao.class);
//
//
//
//                        System.out.println("Print das Urls " + promocao.getUrl());
//                        mWebView.setWebViewClient(new WebViewClient() {
//
//                            @Override
//                            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                                super.onPageStarted(view, url, favicon);
//
//
//                            }
//
//
//                            @Override
//                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                                view.loadUrl(url);
//                                return true;
//                            }
//
//                            @Override
//                            public void onPageFinished(WebView view, String url) {
//                            }
//                        });
//
//
//
//
//
//
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


        // Promocao promo = new Promocao();
//        Uri uri = Uri.parse("https://www.google.com.br");
//        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//        startActivity(intent);
//        return true;
//        startActivity(intent);




    }
    public  void  setWebViewPost (String webViewPost){
        mWebView =  findViewById(R.id.webView);
        mWebView.loadUrl(webViewPost);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);

    }
}

