package com.example.kcruz.aboutme;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.content.FileProvider;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ImageView imgShare;
    Context myContext;
    Uri imageUri;
    String shareProfiletxt ="Karla Cruz - Ingeniería Informáctica" +
            "\n Github: https://github.com/00010216/" +
            "\n Twitter: https://twitter.com/karlaCk_/" +
            "\n Email: 00010216@uca.edu.sv" +
            "\n Phone number: 77546027";
    TextView powered;
    String imgPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setLinks();

        myContext = getApplicationContext();
        imgShare = findViewById(R.id.img_share);
        imgShare.setOnClickListener(share);

        imgPath = createImage(R.drawable.cara);
    }

    View.OnClickListener share = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sharePicture();
        }
    };

    /*
     * Habilita los TextView para actuar como enlaces
     * */
    private void setLinks(){
        TextView txtTwitter = (TextView) findViewById(R.id.txt_twitter);
        TextView txtGit = (TextView) findViewById(R.id.txt_git);
        TextView txtMail = (TextView) findViewById(R.id.txt_email);
        TextView txtPhone = (TextView) findViewById(R.id.txt_phone);
        txtTwitter.setMovementMethod(LinkMovementMethod.getInstance());
        txtGit.setMovementMethod(LinkMovementMethod.getInstance());
        txtMail.setMovementMethod(LinkMovementMethod.getInstance());
        txtPhone.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /*
     * Crea una copia de la imagen de perfil en la memoria del teléfono
     * */
    private String createImage(int resource){
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),resource);
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/cara.JPG";
        OutputStream out = null;
        File file=new File(path);
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file.getPath();
    }

    /*
     * Intent para compartir la informacion
     * */
    public  void sharePicture(){
        //Se obtiene la URI de imagen con formato content:/, apto para compartir
        //con otras apps sin problemas de permisos
        imageUri = FileProvider.getUriForFile(myContext, "com.example.kcruz.fileprovider", new File(imgPath));

        Intent shareIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareProfiletxt);
        shareIntent.setType("image/*");

        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //permiso de lectura

        Intent chooser = Intent.createChooser(shareIntent, "Share");

        if (shareIntent.resolveActivity(getPackageManager()) != null){
            startActivity(chooser); //sendIntent
        }
    }


}
