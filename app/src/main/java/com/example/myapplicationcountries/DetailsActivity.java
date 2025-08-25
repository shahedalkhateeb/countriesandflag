package com.example.myapplicationcountries;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class DetailsActivity extends AppCompatActivity {

    private ImageView imgFlag;
    private TextView txtCountryName, txtCapital, txtDescription;
    private MaterialButton btnEdit, btnDelete;

    private Country currentCountry;
    private CountryDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        imgFlag = findViewById(R.id.imgFlagDetail);
        txtCountryName = findViewById(R.id.txtCountryNameDetail);
        txtCapital = findViewById(R.id.txtCapitalDetail);
        txtDescription = findViewById(R.id.txtDescriptionDetail);
        btnEdit = findViewById(R.id.btnEditCountry);
        btnDelete = findViewById(R.id.btnDeleteCountry);

        db = CountryDatabase.getInstance(this);

        currentCountry = (Country) getIntent().getSerializableExtra("country");

        if (currentCountry != null) {
            displayCountryData();
        } else {
            Toast.makeText(this, "No country data found", Toast.LENGTH_SHORT).show();
        }

        setupButtons();
    }

    private void displayCountryData() {
        txtCountryName.setText(currentCountry.getName());
        txtCapital.setText(currentCountry.getCapital());
        txtDescription.setText(currentCountry.getDescription());
        if (currentCountry.getFlagResId() != 0) {
            imgFlag.setImageResource(currentCountry.getFlagResId());
        }
    }

    private void setupButtons() {
        btnEdit.setOnClickListener(v -> showEditDialog());
        btnDelete.setOnClickListener(v -> showDeleteDialog());
    }

    private void showEditDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Country");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit_country, null);
        EditText editName = view.findViewById(R.id.editCountryName);
        EditText editCapital = view.findViewById(R.id.editCapital);
        EditText editDescription = view.findViewById(R.id.editDescription);

        editName.setText(currentCountry.getName());
        editCapital.setText(currentCountry.getCapital());
        editDescription.setText(currentCountry.getDescription());

        builder.setView(view);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newName = editName.getText().toString().trim();
            String newCapital = editCapital.getText().toString().trim();
            String newDesc = editDescription.getText().toString().trim();

            if (!newName.isEmpty() && !newCapital.isEmpty()) {
                currentCountry.setName(newName);
                currentCountry.setCapital(newCapital);
                currentCountry.setDescription(newDesc);

                db.countryDao().update(currentCountry);

                displayCountryData();
                Toast.makeText(this, "Country updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Name and Capital cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Country");
        builder.setMessage("Are you sure you want to delete this country?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            db.countryDao().delete(currentCountry);
            Toast.makeText(this, "Country deleted", Toast.LENGTH_SHORT).show();
            finish();
        });
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}

