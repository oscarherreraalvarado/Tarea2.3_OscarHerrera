package com.example.tarea23_oscarherrera;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.tarea23_oscarherrera.Adaptador.SignatureAdapter;
import com.example.tarea23_oscarherrera.Configuracion.DBPICTURE;
import com.example.tarea23_oscarherrera.Model.Fotografia;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private FloatingActionButton oFloatingActionButton;
    private RecyclerView oRecyclerView;

    private SignatureAdapter oAdapterSignature;

    private DBPICTURE oDBPICTURE;
    private ArrayList<Fotografia> oSignatureArrayList = new ArrayList<>();

    static final int REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.oFloatingActionButton = findViewById(R.id.floatingButtonSignature);
        this.oRecyclerView = findViewById(R.id.RecyclerViewSignature);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(MainActivity.this);
        mLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        oDBPICTURE = new DBPICTURE(MainActivity.this);

        oSignatureArrayList = oDBPICTURE.leerSignature();
        oAdapterSignature = new SignatureAdapter(oSignatureArrayList);

        this.oFloatingActionButton.setOnClickListener(this::onClick);
        oRecyclerView.setLayoutManager(mLinearLayoutManager);

        oRecyclerView.setAdapter(oAdapterSignature);
    }


    @Override
    protected void onPostResume() {
        updateSignature();
        super.onResume();
    }

    public void updateSignature()
    {
        oSignatureArrayList.clear();
        ArrayList<Fotografia> oA = oDBPICTURE.leerSignature();
        for (int i = 0;i<oA.size();i++)
        {
            Fotografia oS = new Fotografia();
            oS.setDescripcion(oA.get(i).getDescripcion());
            oS.setFoto(oA.get(i).getFoto());
            oSignatureArrayList.add(oS);
        }
        oAdapterSignature.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.floatingButtonSignature)
        {
            Intent oIntent = new Intent(MainActivity.this, NuevaImagen.class);
            startActivityForResult(oIntent,REQUEST_CODE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE)
        {
            updateSignature();
        }
    }
}