package easypromo.com.easypromo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

import easypromo.com.easypromo.R;
import easypromo.com.easypromo.config.AcessoDatabase;

public class PrincipalActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.texto_toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_pesquisar:
                Toast.makeText(PrincipalActivity.this, "Pesquisar", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item_admin:
                abrirTelaAdmin();
                return true;
            case R.id.item_sair:
                deslogarUsuario();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deslogarUsuario(){
        autenticacao = AcessoDatabase.getAutenticacao();
        autenticacao.signOut();

        Intent intent = new Intent(PrincipalActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void abrirTelaAdmin(){
        Intent intent = new Intent(PrincipalActivity.this, AdminActivity.class);
        startActivity(intent);
    }
}
