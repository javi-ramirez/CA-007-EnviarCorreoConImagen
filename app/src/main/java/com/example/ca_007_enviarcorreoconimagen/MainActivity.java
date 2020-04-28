package com.example.ca_007_enviarcorreoconimagen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;



import android.content.Intent;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.os.Environment;
import android.view.View.OnClickListener;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private Button boton;
    private ImageView imagen;
    private String nombre;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private Button enviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText1);
        imagen = (ImageView) findViewById(R.id.imageView1);
        boton = (Button) findViewById(R.id.button1);

        enviar = (Button) findViewById(R.id.button2);

        boton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //Obtenemos el nombre del archivo cómo se va a guardar
                nombre = editText.getText().toString();
                //Creamos un Intent que llame a la actividad que crea una imagen a partir de una fotografía
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                try
                {
                    File image = File.createTempFile(nombre, ".jpg",getExternalFilesDir("fotos"));

                    nombre=image.getAbsolutePath();
                    editText.setText(nombre);
                }
                catch (Exception e)
                {
                    Log.e("Camara",e.toString());
                }


                imagen.setImageDrawable(null);
                //Llamamos al recolector de basura, para liberar memoria y evitar saturarla
                System.gc();
                //Revisamos que la actividad configurada exista y la llamamos si existe
                if(intent.resolveActivity(getPackageManager())!=null)
                {
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        enviar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/jpg");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"javi.guti.rama@gmail.com"});
                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:/"+Environment.getExternalStorageDirectory().toString()+"/"+nombre+".jpg"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Foto");
                intent.putExtra(Intent.EXTRA_TEXT, "Esta es mi foto tomada");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            //imagen.setImageURI(Uri.parse("Ruta:/storage/emulated/0/CA006ObtenerFoto/Fotos/"+nombre+".jpg"));

            if (data !=null && data.getExtras() !=null)
            {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                imagen.setImageBitmap(imageBitmap);
            }
        }
    }
}
