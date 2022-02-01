package com.example.pilon;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class editprofile extends AppCompatActivity {
    EditText ETnama, EThp,ETemail, ETnik;
    SharedPreferences shp1, shp;
    String id, nama, nik, no_hp, pasword, email, photo;
    ImageView profile, click;
    View view;
    ProgressDialog progressDialog;
    Button save;

    //
    String pilihan;
    private static final int PHOTO_REQUEST_GALLERY = 1;//gallery
    static final int REQUEST_TAKE_PHOTO = 1;
    final int CODE_GALLERY_REQUEST = 999;
    String rPilihan[]= {"Take a photo","From Album"};
    public final String APP_TAG = "MyApp";
    Bitmap bitMap = null;
    public String photoFileName = "photo.jpg";
    File photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_edit_profile);
        shp1 = getSharedPreferences ( "mydata", MODE_PRIVATE );
        shp = getSharedPreferences ( "mydata", MODE_PRIVATE );
        ETnama = findViewById ( R.id.idETunama );
        ETnik = findViewById ( R.id.idETunik );
        EThp = findViewById ( R.id.idEtuhp );
        ETemail = findViewById ( R.id.idETuemail );
        profile = findViewById ( R.id.profile_image );
        click = findViewById ( R.id.idclick );
        view = findViewById ( R.id.view );
        save = findViewById ( R.id.idsave );
        progressDialog = new ProgressDialog(this);

        id = shp1.getString ( "id", null );
        nama = shp1.getString ( "nama", null );
        nik = shp1.getString ( "nik", null );
        no_hp = shp1.getString ( "no_hp", null );
        email = shp1.getString ( "email", null );
        photo = shp1.getString ( "photo", null );

        ETnama.setText ( nama );
        ETnik.setText ( nik );
        EThp.setText ( no_hp );
        ETemail.setText ( email );
        if(photo.equals ( "" )){
            Picasso.get().load("http://tkjb2019.com/mobile/image/profile_default.png").into (profile);

        }
        else{
            Picasso.get().load("http://tkjb2019.com/mobile/image/"+photo).into ( profile);
        }
        click.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                if (bitMap != null) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(editprofile.this);
                    alertDialogBuilder.setMessage("Do yo want to take photo again?");
                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            //Toast.makeText(context,"You clicked yes button",Toast.LENGTH_LONG).show();
                            //call fuction of TakePhoto
                            TakePhoto();
                        }
                    });

                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();


                } else {
                    TakePhoto();
                }
            }
            }
            );
        save.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder ( editprofile.this )
                        .setMessage("Apakah anda ingin memperbarui  "+nama+" ?")
                        .setCancelable(false)
                        .setPositiveButton ( "Ya", new DialogInterface.OnClickListener ( ) {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog.setMessage("Mengupdate Data...");
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                                nik = ETnik.getText ().toString ();
                                nama = ETnama.getText ().toString ();
                                no_hp = EThp.getText ().toString ();
                                email = ETemail.getText ().toString ();




                                new Handler ().postDelayed( new Runnable() {
                                    @Override
                                    public void run() {
                                        validasiData();
                                    }
                                },1000);
                            }
                        })
                        .setNegativeButton ( "Tidak", new DialogInterface.OnClickListener ( ) {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        } )
                        .show ();

            }
        } );
    }

    public void cancel(View v){
        onBackPressed ();
    }
    public  void TakePhoto(){
        // Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        AlertDialog.Builder builder = new AlertDialog.Builder(editprofile.this);
        builder.setTitle("Select");
        builder.setItems(rPilihan, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                pilihan = String.valueOf(rPilihan[which]);

                if (pilihan.equals("Take a photo"))
                {
                    // create Intent to take a picture and return control to the calling application
                    Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE);
                    // Create a File reference to access to future access
                    photoFile = getPhotoFileUri(photoFileName);
                    // wrap File object into a content provider
                    // required for API >= 24
                    // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
                    String authorities = getPackageName() + ".fileprovider";
                    Uri fileProvider = FileProvider.getUriForFile(editprofile.this, authorities, photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

                    // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
                    // So as long as the result is not null, it's safe to use the intent.
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        // Start the image capture intent to take photo
                        startActivityForResult(intent, REQUEST_TAKE_PHOTO);

                    }
                }
                else
                {

                    ActivityCompat.requestPermissions(editprofile.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_GALLERY_REQUEST);

                }



            }
        });
        builder.show();


    }
    //permission
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == CODE_GALLERY_REQUEST){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Image"), CODE_GALLERY_REQUEST);
            }else{
                Toast.makeText(getApplicationContext(), "You don't have permission to access gallery!", Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        //set photo size
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == Activity.RESULT_OK) {
                // by this point we have the camera photo on disk
                //Bitmap takenImage = BitmapFactory.decodeFile(String.valueOf(photoFile));
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                bitMap = decodeSampledBitmapFromFile(String.valueOf(photoFile), 1000, 700);
                profile.setImageBitmap(bitMap);
            } else { // Result was a failure
                Toast.makeText(editprofile.this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (intent != null) {
                Uri photoUri = intent.getData();
                // Do something with the photo based on Uri
                bitMap = null;
                try {
                    //InputStream inputStream = getContentResolver().openInputStream(photoUri);
                    //bitMap = BitmapFactory.decodeStream(inputStream);

                    //Image.setVisibility(View.VISIBLE);
                    bitMap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Load the selected image into a preview
                profile.setImageBitmap(bitMap);
            }
        }
    }
    //get data photo
    public File getPhotoFileUri(String fileName)  {
        // Only continue if the SD Card is mounted
        if (isExternalStorageAvailable()) {
            // Get safe storage directory for photos
            // Use `getExternalFilesDir` on Context to access package-specific directories.
            // This way, we don't need to request external read/write runtime permissions.
            File mediaStorageDir = new File(getExternalFilesDir( Environment.DIRECTORY_PICTURES), APP_TAG);

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
                Log.d(APP_TAG, "failed to create directory");
            }
            File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

            return file;

        }
        return null;
    }

    //set photo
    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) { // BEST QUALITY MATCH

        //First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float) height / (float) reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float) width / (float) reqWidth);
        }

        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    // Returns ctrue if external storage for photos is available
    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }


    // get encode image to minimize image
    public String getStringImage(Bitmap bmp){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }



    void validasiData(){
        if(nik.equals("") || nama.equals("") || no_hp.equals("") || email.equals("")){
            progressDialog.dismiss();
            Toast.makeText(editprofile.this, "Periksa kembali data yang anda masukkan !", Toast.LENGTH_SHORT).show();
        }
        else {
                updateData();
        }
    }
    void updateData(){
        if(bitMap!=null){
            photo = getStringImage(bitMap);
        AndroidNetworking.post("http://tkjb2019.com/mobile/api_kelompok_5/updateUsers.php")
                .addBodyParameter("id",""+id)
                .addBodyParameter("nik",""+nik)
                .addBodyParameter("nama",""+nama)
                .addBodyParameter("no_hp",""+no_hp)
                .addBodyParameter("email",""+email)
                .addBodyParameter("photo",""+photo)
                .setTag("Update Data")
                .setPriority( Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener () {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.d("responEdit",""+response);
                        try{
                            Boolean status = response.getBoolean("status");
                            String pesan = response.getString("result");
                            Toast.makeText(editprofile.this, ""+pesan, Toast.LENGTH_SHORT).show();
                            if(status){
                                new AlertDialog.Builder(editprofile.this)
                                        .setMessage("Berhasil Mengupdate Data")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
//                                                Intent i = new Intent(editprofile.this,dashboard.class);
//                                                startActivity(i);
//                                                setResult(RESULT_OK,i);
//                                                editprofile.this.finish();
                                                startActivity ( new Intent ( editprofile.this, dashboard.class ) );

                                            }
                                        })
                                        .show();
                            }else{
                                new AlertDialog.Builder(editprofile.this)
                                        .setMessage("Gagal Mengupdate Data")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i = getIntent();
                                                setResult(RESULT_CANCELED,i);
                                                editprofile.this.finish();
                                            }
                                        })
                                        .show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
    else{
            AndroidNetworking.post("http://tkjb2019.com/mobile/api_kelompok_5/updateUsers.php")
                    .addBodyParameter("id",""+id)
                    .addBodyParameter("nik",""+nik)
                    .addBodyParameter("nama",""+nama)
                    .addBodyParameter("no_hp",""+no_hp)
                    .addBodyParameter("email",""+email)
                    .addBodyParameter("photo","")
                    .setTag("Update Data")
                    .setPriority( Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener () {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();
                            Log.d("responEdit",""+response);
                            try{
                                Boolean status = response.getBoolean("status");
                                String pesan = response.getString("result");
                                Toast.makeText(editprofile.this, ""+pesan, Toast.LENGTH_SHORT).show();
                                if(status){
                                    new AlertDialog.Builder(editprofile.this)
                                            .setMessage("Berhasil Mengupdate Data")
                                            .setCancelable(false)
                                            .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
//                                                Intent i = new Intent(editprofile.this,dashboard.class);
//                                                startActivity(i);
//                                                setResult(RESULT_OK,i);
//                                                editprofile.this.finish();
                                                    startActivity ( new Intent ( editprofile.this, dashboard.class ) );

                                                }
                                            })
                                            .show();
                                }else{
                                    new AlertDialog.Builder(editprofile.this)
                                            .setMessage("Gagal Mengupdate Data")
                                            .setCancelable(false)
                                            .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent i = getIntent();
                                                    setResult(RESULT_CANCELED,i);
                                                    editprofile.this.finish();
                                                }
                                            })
                                            .show();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });
        }
    }


}
