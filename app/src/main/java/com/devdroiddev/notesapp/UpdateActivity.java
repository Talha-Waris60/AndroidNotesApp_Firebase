package com.devdroiddev.notesapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.devdroiddev.notesapp.databinding.ActivityUpdateBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class UpdateActivity extends AppCompatActivity {
    ActivityUpdateBinding binding;
    private String id, title, description;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //TODO: Get the Intent here
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        title = intent.getStringExtra("title");
        description = intent.getStringExtra("description");

        binding.title.setText(title);
        binding.description.setText(description);

        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog dialog = new ProgressDialog(view.getContext());
                dialog.setTitle("Deleting");
                FirebaseFirestore.getInstance()
                        .collection("Notes")
                        .document(id)
                        .delete();
                finish();
            }
        });

        binding.saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title=binding.title.getText().toString();
                description=binding.description.getText().toString();
                updateNote();
            }
        });
    }
    private void updateNote() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        // TODO: Create a Progress Dialogue
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Updated");
        dialog.setIcon(R.drawable.update);
        dialog.setMessage("update your notes");
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        // TODO: Create the object of Model class here;
        NotesModel notesModel = new NotesModel(id,title,description,auth.getUid());
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Notes")
                .document(id)
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
