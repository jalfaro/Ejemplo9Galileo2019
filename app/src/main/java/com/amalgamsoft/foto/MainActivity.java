package com.amalgamsoft.foto;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.DexterBuilder;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button btnFoto;
    private ImageView imagen;
    private String urlFile;
    private boolean isCameraAvailable;
    public static final String IMAGEN_PATH = Environment.getExternalStorageDirectory() +File.separator+ "camaraGalileo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isCameraAvailable = false;
        setContentView(R.layout.activity_main);
        StrictMode.VmPolicy.Builder newbuilder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(newbuilder.build());
        btnFoto = findViewById(R.id.btnFoto);
        imagen = findViewById(R.id.imagen);
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {

                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        //abrir la camara
                        isCameraAvailable = true;
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                }).check();
        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if (isCameraAvailable || android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File path = new File(IMAGEN_PATH);
                    path.mkdirs();
                    urlFile = IMAGEN_PATH + File.separator + "img_" + System.currentTimeMillis() + ".jpg";
                    File foto = new File(urlFile);
                    i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(foto));
                    startActivityForResult(i, 100);
                //}
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            imagen.setImageURI(Uri.parse(urlFile));
        }
    }

    private void handleSmallCameraPhoto(Intent intent) {
        Bundle extras = intent.getExtras();
        Bitmap imagen = (Bitmap) extras.get("data");

    }
}
