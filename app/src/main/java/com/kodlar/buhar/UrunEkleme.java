package com.kodlar.buhar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageHelper;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class UrunEkleme extends AppCompatActivity {
    private EditText urunAdi;
    private EditText urunAgirlik;
    private EditText urunFiyati;
    private DatabaseReference UrunRef;
    private TextView Denemeyazisi;
    private ImageView imageView;
    private ProgressBar progressBar;
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    private Button uruninsert;
    private Uri imageUri;

    Spinner SSubeAdi,SUrunKategori,SUrunAltKategori;
    ArrayList<String> arrayList_SubeAdi;
    ArrayAdapter<String> arrayAdapter_SubeAdi;
    ArrayList<String> arrayList_Kampus;
    ArrayAdapter<String> arrayAdapter_Kampus;
    ArrayList<String> arrayList_icecekler;
    ArrayAdapter<String>  arrayAdapter_icecekler;
    ArrayList<String> arrayList_Atistirmalik;
    ArrayAdapter<String> arrayAdapter_UrunKategori;
    ArrayAdapter<String> arrayAdapter_UrunAltKategori;
    ArrayList<String>   arrayList_Merkez;
    ArrayAdapter<String>    arrayAdapter_Merkez;
    Adres adres = new Adres();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urun_ekleme);
        SSubeAdi=(Spinner) findViewById(R.id.SubeAdi);
        SUrunKategori=(Spinner) findViewById(R.id.UrunKategori);
       SUrunAltKategori=(Spinner) findViewById(R.id.UrunAltKategori);
        Denemeyazisi=(TextView)findViewById(R.id.denemetext);
        uruninsert=(Button)findViewById(R.id.uruninsert);
        urunAdi= (EditText)findViewById(R.id.ekleAd);
        urunAgirlik= (EditText)findViewById(R.id.ekleAgirlik);
        urunFiyati= (EditText)findViewById(R.id.ekleFiyat);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        imageView=(ImageView) findViewById(R.id.UrunFoto);

        progressBar.setVisibility(View.INVISIBLE);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent galleryIntent =new Intent();
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent,2);

                }
            });


        arrayList_SubeAdi=new ArrayList<>();
        arrayList_SubeAdi.add("Kampus");
        arrayList_SubeAdi.add("Merkez");
        arrayAdapter_SubeAdi=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item,arrayList_SubeAdi);
        SSubeAdi.setAdapter(arrayAdapter_SubeAdi);

        SSubeAdi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

               if(position==0){
                   arrayList_Kampus=new ArrayList<>();
                   arrayList_Kampus.add("icecekler");
                   arrayList_Kampus.add("Atistirmalik");
                   arrayList_Kampus.add("Manav");
                   arrayList_Kampus.add("Et");
                   arrayList_Kampus.add("Ekmek");
                   arrayAdapter_Kampus=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item,arrayList_Kampus);
                   SUrunKategori.setAdapter(arrayAdapter_Kampus);

                   SUrunKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                       @Override
                       public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                           if (position == 0) {
                               arrayList_icecekler = new ArrayList<>();
                               arrayList_icecekler.add("Su");
                               arrayList_icecekler.add("Gazliicecekler");
                               arrayList_icecekler.add("Gazsizicecekler");
                               arrayAdapter_UrunAltKategori = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList_icecekler);

                           }
                           if (position == 1) {
                               arrayList_Atistirmalik = new ArrayList<>();
                               arrayList_Atistirmalik.add("Cikolata");
                               arrayList_Atistirmalik.add("Cips");
                               arrayList_Atistirmalik.add("Kuruyemis");
                               arrayAdapter_UrunAltKategori = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList_Atistirmalik);


                           }
                           SUrunAltKategori.setAdapter(arrayAdapter_UrunAltKategori);
                            adres.setKategori(SUrunKategori.getSelectedItem().toString());
                       }
                       @Override
                       public void onNothingSelected(AdapterView<?> parent) {

                       }
                   });


                   SUrunAltKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                       @Override
                       public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                          adres.setAltkategori(SUrunAltKategori.getSelectedItem().toString());


                       }
                       @Override
                       public void onNothingSelected(AdapterView<?> parent) {

                       }
                   });
                   adres.setSubeadi(SSubeAdi.getSelectedItem().toString());

               }


               if(position==1){
                   arrayList_Merkez=new ArrayList<>();
                   arrayList_Merkez.add("icecekler");
                   arrayList_Merkez.add("Atistirmalik");
                   arrayList_Merkez.add("Manav");
                   arrayList_Merkez.add("Et");
                   arrayList_Merkez.add("Ekmek");
                   arrayAdapter_Merkez=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item,arrayList_Merkez);
                   SUrunKategori.setAdapter(arrayAdapter_Merkez);

                   SUrunKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                       @Override
                       public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                           if (position == 0) {
                               arrayList_icecekler = new ArrayList<>();
                               arrayList_icecekler.add("Su");
                               arrayList_icecekler.add("Gazliicecekler");
                               arrayList_icecekler.add("Gazsizicecekler");
                               arrayAdapter_UrunAltKategori = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList_icecekler);

                           }
                           if (position == 1) {
                               arrayList_Atistirmalik = new ArrayList<>();
                               arrayList_Atistirmalik.add("Cikolata");
                               arrayList_Atistirmalik.add("Cips");
                               arrayList_Atistirmalik.add("Kuruyemis");
                               arrayAdapter_UrunAltKategori = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList_Atistirmalik);


                           }
                           SUrunAltKategori.setAdapter(arrayAdapter_UrunAltKategori);
                           adres.setKategori(SUrunKategori.getSelectedItem().toString());
                       }
                       @Override
                       public void onNothingSelected(AdapterView<?> parent) {

                       }
                   });


                   SUrunAltKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                       @Override
                       public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                           adres.setAltkategori(SUrunAltKategori.getSelectedItem().toString());



                       }
                       @Override
                       public void onNothingSelected(AdapterView<?> parent) {

                       }
                   });

                   adres.setSubeadi(SSubeAdi.getSelectedItem().toString());

               }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        uruninsert.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
               insertUrun();

            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==2 && resultCode== RESULT_OK&& data!= null){

            imageUri=data.getData();
            imageView.setImageURI(imageUri);
        }

    }




    private void insertUrun(){
    UrunRef = FirebaseDatabase.getInstance().getReference().child(adres.getSubeadi()).child(adres.getKategori()).child(adres.getAltkategori());

        StorageReference fileRef =reference.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));
    String  urunadi= urunAdi.getText().toString();
    String  urunfiyati= urunFiyati.getText().toString();
    String  urunagirlik= urunAgirlik.getText().toString();



    fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {


                 String   image=uri.toString();
                 String urunid= UrunRef.push().getKey();

                 Urun urunler = new Urun(urunadi,urunfiyati,urunagirlik,image);

                    UrunRef.push().setValue(urunler);
                    progressBar.setVisibility(View.VISIBLE);


                    Toast.makeText(UrunEkleme.this,"Yükleme Başarılı!",Toast.LENGTH_SHORT).show();;
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }
    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(UrunEkleme.this,"Yükleme Hatası!",Toast.LENGTH_SHORT).show();;
        }
    });


    Toast.makeText(UrunEkleme.this,"Urun eklendi.",Toast.LENGTH_SHORT).show();
}

private String getFileExtension(Uri mUri){
    ContentResolver cr =getContentResolver();
    MimeTypeMap mime = MimeTypeMap.getSingleton();
    return mime.getExtensionFromMimeType(cr.getType(mUri));
}

}