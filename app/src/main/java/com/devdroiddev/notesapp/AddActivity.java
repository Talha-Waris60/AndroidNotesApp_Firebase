package com.devdroiddev.notesapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.devdroiddev.notesapp.databinding.ActivityAddBinding;
import com.google.android.gms.common.PackageVerificationResult;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class AddActivity extends AppCompatActivity {
    ActivityAddBinding binding;
    private String title = "", description = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = binding.title.getText().toString();
                description = binding.description.getText().toString();
                
                //TODO: Save Note
                saveNote();
            }
        });
    }

    private void saveNote() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        // TODO: Create a Progress Dialogue
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Saving");
        dialog.setIcon(R.drawable.save);
        dialog.setMessage("saving your notes");
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        // TODO: Create the object of Model class here;
        String noteId = UUID.randomUUID().toString();
        NotesModel notesModel = new NotesModel(noteId,title,description,auth.getUid());
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Notes")
                .document(noteId)
                .set(notesModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(),"Note Saved", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });
    }
}
