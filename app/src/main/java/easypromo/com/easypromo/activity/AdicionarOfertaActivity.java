package easypromo.com.easypromo.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import easypromo.com.easypromo.R;
import easypromo.com.easypromo.config.AcessoDatabase;
import easypromo.com.easypromo.helper.Base64Custom;
import easypromo.com.easypromo.model.Promocao;

public class AdicionarOfertaActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AdicionarOfertaActivity";

    private EditText etUrlOferta;
    private EditText etNomeProd;
    private EditText etPreco;
    private Spinner categoriasSpinner;
    private Button btImportar;
    private Button btCamera;
    private ImageView ivImagemSelecionada;
    private Button btEnviarOferta;
    private double mProgressoDeUp = 0;

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri imageUri;
    public Uri caminhoArquivo;
    private StorageTask mUploadTask;

    private ArrayAdapter<String> categoriasAdapter;
    private DatabaseReference dbReference;

    public String FIREBASE_IMAGE_STORAGE = "promocoes/usuario/";
    Promocao promocao;

    // VARIÁVEIS DA GALERIA
    private Intent dataGaleria;
    /**
     * RESULT_CAMERA
     */
    private static final int RESULT_CAMERA = 111;
    /**
     * RESULT_GALERIA
     */
    private static final int RESULT_GALERIA = 222;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_oferta);

        inicializarComponentes();
        preencherCategorias();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    private void inicializarComponentes() {
        etUrlOferta = findViewById(R.id.etUrlOferta);
        etNomeProd = findViewById(R.id.etNomeProd);
        etPreco = findViewById(R.id.etPreco);
        categoriasSpinner = findViewById(R.id.spCategorias);

        ivImagemSelecionada = findViewById(R.id.ivImagemProd);
        ivImagemSelecionada.setImageResource(R.drawable.banner);

        btImportar = findViewById(R.id.btImportar);
        btImportar.setOnClickListener(this);

        btCamera = findViewById(R.id.btCamera);
        btCamera.setOnClickListener(this);

        btEnviarOferta = findViewById(R.id.btEnviarOferta);
        btEnviarOferta.setOnClickListener(this);
    }

    private void preencherCategorias() {
        dbReference = AcessoDatabase.getReferencia().child("categorias");
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> categorias = new ArrayList<String>();
                categorias.add(getString(R.string.texto_spinner_categorias));


                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String nomeCategoria = areaSnapshot.child("nomeCategoria").getValue(String.class);
                    categorias.add(nomeCategoria);
                }

                categoriasAdapter = new ArrayAdapter<>(AdicionarOfertaActivity.this, android.R.layout.simple_spinner_item, categorias);
                categoriasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categoriasSpinner.setAdapter(categoriasAdapter);
                categoriasSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent;

        if (id == R.id.btCamera) {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, RESULT_CAMERA);

        } else if (id == R.id.btImportar) {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, RESULT_GALERIA);

        } else if (id == R.id.btEnviarOferta) {
            enviarOferta();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    importarGaleria();
                } else {
                    Toast.makeText(AdicionarOfertaActivity.this, "Não há permissão para a galeria", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        dataGaleria = data;
        if (requestCode == RESULT_CAMERA && resultCode == RESULT_OK) {

              Bitmap foto = (Bitmap) data.getExtras().get("data");
                ivImagemSelecionada.setImageBitmap(foto);

        } else if (requestCode == RESULT_GALERIA && resultCode == RESULT_OK) {

            final int MyVersion = Build.VERSION.SDK_INT;
            if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
                if (!verificarPermissao()) {
                    requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    importarGaleria();
                }
            }
        }
    }

    private void importarGaleria() {
        imageUri = dataGaleria.getData();
        String[] colunaArquivo = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(imageUri, colunaArquivo, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(colunaArquivo[0]);
        String picturePath = cursor.getString(columnIndex);

        Bitmap imagem = BitmapFactory.decodeFile(picturePath);
        if (imagem != null) {
            ivImagemSelecionada.setImageBitmap(imagem);
        }
    }


    private void uploadImage(){

        if (imageUri != null){

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading..");
            progressDialog.show();
            //CRIA O NÓ no STORAGE, as imagens upadas pelo usuario corrente.

            String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
            StorageReference ref = storageReference.child(FIREBASE_IMAGE_STORAGE +"/"+ user_id + "/promo" + UUID.randomUUID() + ".png");
            ref.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                          caminhoArquivo = taskSnapshot.getDownloadUrl();
                            Log.d(TAG, "onSuccess: quero ver o que ta no :" + caminhoArquivo);

                            cadastrar(caminhoArquivo.toString());
                            progressDialog.dismiss();
                            Log.d(TAG, "onSuccess: quero ver o que ta no ééé :" + caminhoArquivo);
                            Toast.makeText(AdicionarOfertaActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AdicionarOfertaActivity.this, PrincipalActivity.class);
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {

                @Override
                public void onFailure(Exception e) {

                    progressDialog.dismiss();
                    Toast.makeText(AdicionarOfertaActivity.this, "Failed "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                            .getTotalByteCount());

                    progressDialog.setMessage("Uploading "+(int)progress+"%");
                }
            });
        }
    }

    private boolean verificarPermissao() {
        int result = ContextCompat.checkSelfPermission(AdicionarOfertaActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void cadastrar(String url ) {
        promocao = new Promocao(
                Base64Custom.codificarBase64(etUrlOferta.getText().toString()),
                etUrlOferta.getText().toString(),
                etNomeProd.getText().toString(),
                Double.parseDouble(etPreco.getText().toString()),
                categoriasAdapter.getItem(categoriasSpinner.getSelectedItemPosition()),
                "", url);

        Log.d(TAG, "cadastrar:  aqui " + url);

        promocao.cadastrar();
    }

    private void enviarOferta() {
        if (!validarCampos()) return;

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdicionarOfertaActivity.this,
                R.style.AlertDialogTheme);

        alertDialog.setIcon(R.drawable.ic_action_add);
        alertDialog.setTitle("Enviar oferta");
        alertDialog.setMessage("Deseja enviar a oferta para aprovação?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                uploadImage();

                Toast.makeText(AdicionarOfertaActivity.this, "Oferta enviada", Toast.LENGTH_SHORT)
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

    private boolean validarCampos() {
        if (etUrlOferta.getText().length() <= 0 || etNomeProd.getText().length() <= 0 || etPreco.length() <= 0) {
            Toast.makeText(AdicionarOfertaActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }

        if (etPreco.getText().toString().replace(".", "").equals("")) {
            Toast.makeText(AdicionarOfertaActivity.this, "Informe um preço válido", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }

        if (Double.parseDouble(etPreco.getText().toString()) <= 0) {
            Toast.makeText(AdicionarOfertaActivity.this, "Informe um preço maior que zero", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }

        int positionCategoria = categoriasSpinner.getSelectedItemPosition();
        if (Objects.equals(categoriasAdapter.getItem(positionCategoria), getString(R.string.texto_spinner_categorias))) {
            Toast.makeText(AdicionarOfertaActivity.this, "Selecione uma categoria válida", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }

        return true;
    }
}