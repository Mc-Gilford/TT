package com.matias.domuapp.activities.profesionista;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;
import com.matias.domuapp.R;
import com.matias.domuapp.controller.AddressController;
import com.matias.domuapp.includes.MyToolbar;
import com.matias.domuapp.models.Direccion;
import com.matias.domuapp.models.Persona;
import com.matias.domuapp.models.Profesional;
import com.matias.domuapp.providers.AuthProvider;
import com.matias.domuapp.providers.ImagesProvider;
import com.matias.domuapp.providers.ProfesionistaProvider;
import com.matias.domuapp.utils.FileUtil;
import com.squareup.picasso.Picasso;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfileProfesionistActivity extends AppCompatActivity {

    private ImageView mImageViewProfile;
    private Button mButtonUpdate;
    private TextView mTextViewName;
    private TextView mTextViewBrandVehicle;
    private TextView mTextViewPlateVehicle;
    private CircleImageView mCircleImageBack;

    private ProfesionistaProvider mProfesionistProvider;
    private AuthProvider mAuthProvider;
    private ImagesProvider mImageProvider;

    private File mImageFile;
    private String mImage;

    private final int GALLERY_REQUEST = 1;
    private ProgressDialog mProgressDialog;
    private String mName;
    private String mVehicleBrand;
    private String mVehiclePlate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile_profesionist);
        //MyToolbar.show(this, "Actualizar perfil", true);

        mImageViewProfile = findViewById(R.id.imageViewProfile);
        mButtonUpdate = findViewById(R.id.btnUpdateProfile);
        mTextViewName = findViewById(R.id.textInputName);
        mTextViewBrandVehicle = findViewById(R.id.textInputVehicleBrand);
        mTextViewPlateVehicle = findViewById(R.id.textInputVehiclePlate);
        mCircleImageBack = findViewById(R.id.circleImageBack);

        mProfesionistProvider = new ProfesionistaProvider();
        mAuthProvider = new AuthProvider();
        mImageProvider = new ImagesProvider("profesionist_images");

        mProgressDialog = new ProgressDialog(this);

        getProfesionistInfo();

        mImageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        mButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });
        mCircleImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== GALLERY_REQUEST && resultCode == RESULT_OK) {
            try {
                mImageFile = FileUtil.from(this, data.getData());
                mImageViewProfile.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
            } catch(Exception e) {
                Log.d("ERROR", "Mensaje: " +e.getMessage());
            }
        }
    }

    private void getProfesionistInfo() {
        mProfesionistProvider.getProfesionist(mAuthProvider.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("person").child("name").getValue().toString();
                    String lastname = dataSnapshot.child("person").child("lastname").getValue().toString();
                    String secondname = dataSnapshot.child("person").child("secondname").getValue().toString();
                    String vehicleBrand = dataSnapshot.child("servicio").getValue().toString();
                    //String country = dataSnapshot.child("person").child("address").child("country").getValue().toString();
                    String state = dataSnapshot.child("person").child("address").child("state").getValue().toString();
                    String city = dataSnapshot.child("person").child("address").child("city").getValue().toString();
                    String colony = dataSnapshot.child("person").child("address").child("colony").getValue().toString();
                    String street = dataSnapshot.child("person").child("address").child("street").getValue().toString();
                    //String postalCode = dataSnapshot.child("person").child("address").child("postalCode").getValue().toString();
                    String number = dataSnapshot.child("person").child("address").child("number").getValue().toString();
                    String vehiclePlate=/*country+" "+*/state+","+city+","+colony+","+street+","+number;
                    String image = "";
                    if (dataSnapshot.hasChild("image")) {
                        image = dataSnapshot.child("image").getValue().toString();
                        Picasso.with(UpdateProfileProfesionistActivity.this).load(image).into(mImageViewProfile);
                    }
                    mTextViewName.setText(name+" "+lastname+" "+secondname);
                    mTextViewBrandVehicle.setText(vehicleBrand);
                    mTextViewPlateVehicle.setText(vehiclePlate);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateProfile() {
        mName = mTextViewName.getText().toString();
        mVehicleBrand = mTextViewBrandVehicle.getText().toString();
        mVehiclePlate = mTextViewPlateVehicle.getText().toString();
        if (!mName.equals("") && mImageFile != null) {
            mProgressDialog.setMessage("Espere un momento...");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();

            saveImage();
        }
        else {
            Toast.makeText(this, "Ingresa la imagen y el nombre", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImage() {
        mImageProvider.saveImage(UpdateProfileProfesionistActivity.this, mImageFile, mAuthProvider.getId()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String image = uri.toString();
                            Profesional profesionist = new Profesional();
                            Persona persona = new Persona();
                            profesionist.setImage(image);
                            String fullname[] = mName.split(" ");
                            persona.setName(fullname[0]);
                            persona.setLastname(fullname[1]);
                            persona.setSecondname(fullname[2]);
                            Direccion direccion;
                            AddressController addressController = new AddressController();
                            direccion=addressController.getDireccionString(mVehiclePlate);
                            persona.setAddress(direccion);
                            System.out.println("Vamos aquii"+persona.getAddress());
                            profesionist.setPerson(persona);
                            profesionist.setId(mAuthProvider.getId());
                            profesionist.setServicio(mVehicleBrand);
                            System.out.println(profesionist.getPerson().getAddress());
                            mProfesionistProvider.update(profesionist).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(UpdateProfileProfesionistActivity.this, "Su informacion se actualizo correctamente", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
                else {
                    Toast.makeText(UpdateProfileProfesionistActivity.this, "Hubo un error al subir la imagen", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
