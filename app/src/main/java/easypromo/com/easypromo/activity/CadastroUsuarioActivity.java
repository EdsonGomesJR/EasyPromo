package easypromo.com.easypromo.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import easypromo.com.easypromo.R;
import easypromo.com.easypromo.config.AcessoDatabase;
import easypromo.com.easypromo.model.Usuario;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText etNome;
    private EditText etEmail;
    private EditText etSenha;
    private EditText etConfirmaSenha;
    private Button btCadastrar;

    private Usuario usuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        etNome = findViewById(R.id.etNome);
        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        etConfirmaSenha = findViewById(R.id.etConfirmaSenha);

        btCadastrar = findViewById(R.id.btCadastrar);
        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validaSenha()) return;
                setAtributos();
                cadastrarUsuario();
            }
        });
    }

    private boolean validaSenha(){

        if (etSenha.getText().toString().equals(etConfirmaSenha.getText().toString())) return true;

        Toast.makeText(CadastroUsuarioActivity.this,
                "Senha de confirmação inválida", Toast.LENGTH_SHORT).show();
        return false;
    }

    private void setAtributos(){

        usuario = new Usuario();
        usuario.setNome(etNome.getText().toString());
        usuario.setEmail(etEmail.getText().toString());
        usuario.setSenha(etSenha.getText().toString());
    }

    private void cadastrarUsuario(){

        autenticacao = AcessoDatabase.getAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(CadastroUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(CadastroUsuarioActivity.this,
                            "Usuário cadastrado com sucesso", Toast.LENGTH_SHORT).show();

                    FirebaseUser usuarioFireBase = task.getResult().getUser();
                    usuario.setId(usuarioFireBase.getUid());
                    usuario.cadastrar();
                }
                else{
                    Toast.makeText(CadastroUsuarioActivity.this,
                            "Erro ao cadastrar usuário", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
