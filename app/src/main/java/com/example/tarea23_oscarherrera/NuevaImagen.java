package com.example.tarea23_oscarherrera;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.tarea23_oscarherrera.Configuracion.DBPICTURE;
import com.example.tarea23_oscarherrera.Model.Fotografia;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class NuevaImagen extends AppCompatActivity {

    private DBPICTURE oDBPICTURE;

    private TextInputEditText oTextInputEditTextDescripcion;
    private MaterialButton oMaterialButtonSalvar;
    private MaterialButton oMaterialButtonBack;

    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 101;

    private ImageView oSignatureView;
    private Bitmap oBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_signature);

        this.oTextInputEditTextDescripcion = this.findViewById(R.id.txtDescripcion);
        this.oMaterialButtonSalvar = this.findViewById(R.id.btn_salvar);
        this.oMaterialButtonBack = this.findViewById(R.id.btn_back);
        this.oSignatureView = this.findViewById(R.id.img);

        oDBPICTURE = new DBPICTURE(NuevaImagen.this);

        oMaterialButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Solicitar permisos de la cámara si no están otorgados
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso de la cámara otorgado, puedes abrir la cámara
                abrirCamera();
            } else {
                // Permiso de la cámara denegado, muestra un mensaje al usuario
                Toast.makeText(this, "Permiso de la cámara denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.oMaterialButtonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (oBitmap != null && !oTextInputEditTextDescripcion.getText().toString().isEmpty()) {
                    Fotografia oS = new Fotografia();
                    oS.setDescripcion(oTextInputEditTextDescripcion.getText().toString());
                    oS.setFoto(oBitmap);
                    if (oDBPICTURE.createSignature(oS)) {
                        Toast.makeText(NuevaImagen.this, "GUARDADO CON EXITO", Toast.LENGTH_SHORT).show();
                        oTextInputEditTextDescripcion.setText("");
                        oSignatureView.setImageDrawable(getResources().getDrawable(R.drawable.foto));
                    }
                } else {
                    Toast.makeText(NuevaImagen.this, "EXISTEN DATOS VACIOS", Toast.LENGTH_SHORT).show();
                }
            }
        });

        this.oSignatureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCamera();
            }
        });
    }

    public void abrirCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                oBitmap = (Bitmap) extras.get("data");
                oSignatureView.setImageBitmap(oBitmap);
            } else {
                Toast.makeText(this, "No se pudo capturar la imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
