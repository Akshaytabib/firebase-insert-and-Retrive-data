package com.example.insertandnotification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ImageView image, back;
    Button next;
    EditText editText;

    Uri imageUri;
    String myUrl = "";

    String id;
    String name;
    String images;

    String ID;

    DatabaseReference databaseReference;
    StorageTask uploadTask;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = findViewById(R.id.category_image);
        next = findViewById(R.id.save_btn);
        editText = findViewById(R.id.category_name);

        databaseReference = FirebaseDatabase.getInstance().getReference("Catogory");
        ID = databaseReference.push().getKey();
        storageReference = FirebaseStorage.getInstance().getReference("Catogory");

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().
                        setAspectRatio(1, 1)
                        .start(MainActivity.this);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uploadimg();
                startActivity(new Intent(MainActivity.this, MainActivity2.class));

            }
        });

    }

    private void Uploadimg() {
        if (imageUri != null) {
            final StorageReference reference = storageReference.child(ID + "." + getExtension(imageUri));
            uploadTask = reference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isComplete()) {
                        task.getException();
                    }
                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {

                    if (task.isSuccessful()) {
                        Uri downloaduri = (Uri) task.getResult();
                        myUrl = downloaduri.toString();
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Categories").child(ID);

                        String name = editText.getText().toString();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("categoryId", ID);
                        hashMap.put("categoryImage", myUrl);
                        hashMap.put("categoryName", name);

                        databaseReference.setValue(hashMap);

                        next.setVisibility(View.VISIBLE);

                    } else {
                        Toast.makeText(MainActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    next.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            next.setVisibility(View.VISIBLE);
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }

    private String getExtension(Uri uri) {
        ContentResolver resolver = getContentResolver();
        MimeTypeMap map = MimeTypeMap.getSingleton();
        return map.getExtensionFromMimeType(resolver.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            //CropImage.ActivityResult result = CropImage.getActivityResult(data);
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                image.setImageURI(imageUri);
            }
        } else {
            Toast.makeText(this, "An Error Ocurred!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
