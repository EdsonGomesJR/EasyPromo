package easypromo.com.easypromo.fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import easypromo.com.easypromo.R;
import easypromo.com.easypromo.activity.LoginActivity;
import easypromo.com.easypromo.activity.PrincipalActivity;
import easypromo.com.easypromo.config.AcessoDatabase;
import easypromo.com.easypromo.helper.Base64Custom;
import easypromo.com.easypromo.helper.Utilidades;
import easypromo.com.easypromo.model.Usuario;

public class UsuariosFragment extends Fragment {

	// region Variáveis
	private EditText emailUsuario;
	private Button btProcurarUsuario;
	private ScrollView svResultadoPesquisa;

	private TextView nome;
	private TextView email;
	private TextView dataCadastro;
	private RadioButton rbPerfilUsuario;
	private RadioButton rbPerfilAdmin;
	private Button btSalvar;
	private Button btAtivarInativar;

	private DatabaseReference dbReference;

	private String estadoInicialPerfil;
	private String estadoFinalPerfil;
	private String estadoInicialAtivo;
	private String estadoFinalAtivo;
	// endregion

	public UsuariosFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_usuarios, container, false);

		emailUsuario = view.findViewById(R.id.etEmailUsuario);
		svResultadoPesquisa = view.findViewById(R.id.svResultadoPesquisa);

		nome = view.findViewById(R.id.txtNome);
		email = view.findViewById(R.id.txtEmail);
		dataCadastro = view.findViewById(R.id.txtData);
		rbPerfilUsuario = view.findViewById(R.id.rbPerfilUsuario);
		rbPerfilAdmin = view.findViewById(R.id.rbPerfilAdmin);

		btProcurarUsuario = view.findViewById(R.id.btProcurarUsuario);
		btProcurarUsuario.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				Utilidades.hideKeyboard(getContext(), view);

				dbReference = AcessoDatabase.getReferencia()
						.child("usuarios")
						.child(Base64Custom.codificarBase64(emailUsuario.getText().toString()));

				dbReference.addValueEventListener(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot dataSnapshot) {

						if( dataSnapshot.getValue() != null ){
							emailUsuario.setText("");

							Usuario usuarioRecuperado = dataSnapshot.getValue(Usuario.class);

							svResultadoPesquisa.setVisibility(View.VISIBLE);

							nome.setText(usuarioRecuperado.getNome());
							email.setText(usuarioRecuperado.getEmail());
							dataCadastro.setText(usuarioRecuperado.getDataCadastro());

							estadoInicialAtivo = usuarioRecuperado.getAtivo();
							if (estadoInicialAtivo.equals("1")){
								btAtivarInativar.setText("Inativar");
							}
							else{
								btAtivarInativar.setText("Ativar");
							}

							estadoInicialPerfil = usuarioRecuperado.getTipoPerfil();
							verificaPerfilUsuario(estadoInicialPerfil);
						}
						else {
							Toast.makeText(getActivity(), "Usuário não cadastrado", Toast.LENGTH_LONG)
									.show();
						}
					}

					@Override
					public void onCancelled(DatabaseError databaseError) {

					}
				});
			}
		});

		btSalvar = view.findViewById(R.id.btSalvar);
		btSalvar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				estadoFinalPerfil = rbPerfilAdmin.isChecked() ? "1" : "0";
				if (estadoFinalPerfil.equals(estadoInicialPerfil)) return;

				atualizarUsuario();
			}
		});

		btAtivarInativar = view.findViewById(R.id.btAtivarInativar);
		btAtivarInativar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				boolean ativo;
				if (estadoInicialAtivo.equals("1")){
					ativo = true;
				}
				else{
					ativo = false;
				}

				inativarOuAtivarUsuario(ativo);
			}
		});

		return view;
	}

	private void verificaPerfilUsuario(String perfilUsuario){
		if (perfilUsuario.equals("1")){
			rbPerfilAdmin.setChecked(true);
		}
		else{
			rbPerfilUsuario.setChecked(true);
		}
	}

	private void atualizarUsuario(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext(),
				R.style.AlertDialogTheme);

		alertDialog.setIcon(R.drawable.ic_info_outline);
		alertDialog.setTitle("Atualizar usuário");
		alertDialog.setMessage("Deseja atualizar o usuário?");

		alertDialog.setPositiveButton("Atualizar", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {

				dbReference = AcessoDatabase.getReferencia();
				dbReference.child("usuarios").child(Base64Custom.codificarBase64(email.getText().toString())).child("tipoPerfil")
						.setValue(estadoFinalPerfil);

				Toast.makeText(getActivity(), "Usuário atualizado", Toast.LENGTH_SHORT)
						.show();
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

	private void inativarOuAtivarUsuario(boolean ativo){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext(),
				R.style.AlertDialogTheme);

		String textoStatusMaiuscula = ativo ? "Inativar" : "Ativar";
		String textoStatusMinuscula = ativo ? "inativar" : "ativar";

		estadoFinalAtivo = ativo ? "0" : "1";

		alertDialog.setIcon(R.drawable.ic_info_outline);
		alertDialog.setTitle(textoStatusMaiuscula + " usuário");
		alertDialog.setMessage("Deseja " + textoStatusMinuscula + " o usuário?");

		alertDialog.setPositiveButton(textoStatusMaiuscula, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {

				dbReference = AcessoDatabase.getReferencia();
				dbReference.child("usuarios").child(Base64Custom.codificarBase64(email.getText().toString())).child("ativo")
						.setValue(estadoFinalAtivo);
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