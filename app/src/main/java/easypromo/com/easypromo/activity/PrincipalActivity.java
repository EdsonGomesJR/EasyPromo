package easypromo.com.easypromo.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import easypromo.com.easypromo.R;
import easypromo.com.easypromo.adapter.PromocaoAdapter;
import easypromo.com.easypromo.adapter.PromocaoAdapter;
import easypromo.com.easypromo.config.AcessoDatabase;
import easypromo.com.easypromo.fragment.MenuNavegacaoFragment;
import easypromo.com.easypromo.model.Promocao;
import easypromo.com.easypromo.model.Usuario;

public class PrincipalActivity extends AppCompatActivity {
    private FloatingActionButton btnFab;
    private MenuNavegacaoFragment mNavegacaoFragment;
    private android.support.v7.widget.Toolbar toolbar;

    private FirebaseAuth autenticacao;
    private DatabaseReference dbReference;

    private RecyclerView mRecyclerView;
    private  RecyclerView.Adapter mAdapter;

    private List<Promocao> mPromocaoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        recuperarUsuarioLogado();
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.texto_toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.getSupportActionBar().setHomeButtonEnabled(true);
        this.mNavegacaoFragment = (MenuNavegacaoFragment) this.getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavegacaoFragment.setUp(R.id.navigation_drawer, toolbar, (DrawerLayout) this.findViewById(R.id.drawer_layout));

        fabOpcoes();

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mPromocaoList = new ArrayList<>();

        dbReference = FirebaseDatabase.getInstance().getReference("promocoes");
        dbReference.keepSynced(true);

        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mPromocaoList.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){

                    Promocao promocao  = postSnapshot.getValue(Promocao.class);

                    promocao.getUrl();

                    if (promocao.getStatus().equals("1")){
                        mPromocaoList.add(promocao);

                    }
                }

                Collections.sort(mPromocaoList);
                mAdapter = new PromocaoAdapter(PrincipalActivity.this,mPromocaoList);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(PrincipalActivity.this,databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void recuperarUsuarioLogado() {
        autenticacao = AcessoDatabase.getAutenticacao();
        if (autenticacao.getCurrentUser() == null){
            Intent intent = new Intent(PrincipalActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_pesquisar:
                abrirPesquisaPromocao();
                return true;
            case R.id.item_sair:
                deslogarUsuario();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deslogarUsuario() {
        autenticacao = AcessoDatabase.getAutenticacao();
        autenticacao.signOut();
        Intent intent = new Intent(PrincipalActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void abrirPesquisaPromocao() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PrincipalActivity.this,
                R.style.AlertDialogTheme);

        alertDialog.setIcon(R.drawable.ic_search);
        alertDialog.setTitle("Pesquisar promoção");
        alertDialog.setMessage("Informe palavras-chave para pesquisar");
        alertDialog.setCancelable(false);

        final EditText etPesqPromo = new EditText(PrincipalActivity.this);
        etPesqPromo.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        alertDialog.setView(etPesqPromo);

        alertDialog.setPositiveButton("Pesquisar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String pesquisaPromocao = etPesqPromo.getText().toString();

                if (pesquisaPromocao.isEmpty()) {
                    Toast.makeText(PrincipalActivity.this, "Pesquisa sem informações", Toast.LENGTH_LONG).show();
                } else {
                    // FALTA IMPLEMENTAR
                }
            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alertDialog.create();
        alertDialog.show();
    }

    private void fabOpcoes() {

        btnFab = findViewById(R.id.fab_adicionar_oferta);
        btnFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrincipalActivity.this, AdicionarOfertaActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //testar o retorno dos dados

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            //recuperar local da imagem

            Uri localImagemSelecionada = data.getData();

            //recupera a imagem do local que foi selecionada
            try {
                Bitmap imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);

                //comprimir no formato PNG
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imagem.compress(Bitmap.CompressFormat.PNG, 75, stream);

                //criar um arquivo com formato parse

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

}


