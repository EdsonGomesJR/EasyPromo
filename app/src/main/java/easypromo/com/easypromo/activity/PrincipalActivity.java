package easypromo.com.easypromo.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
                abrirPesquisaPromocao();
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

    private void abrirPesquisaPromocao(){
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

                if (pesquisaPromocao.isEmpty()){
                    Toast.makeText(PrincipalActivity.this, "Pesquisa sem informações", Toast.LENGTH_LONG).show();
                }
                else{
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
}
