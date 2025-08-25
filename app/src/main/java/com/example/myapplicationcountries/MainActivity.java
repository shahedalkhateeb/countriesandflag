package com.example.myapplicationcountries;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CountryAdapter adapter;
    private CountryDatabase db;
    private FloatingActionButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerCountries);
        btnAdd = findViewById(R.id.btnAddCountry);

        db = CountryDatabase.getInstance(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CountryAdapter(this, db.countryDao().getAllCountries());
        recyclerView.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> showAddDialog());
    }

    // دالة بتجيب صورة العلم بناءً على اسم الدولة
    private int getFlagResId(String countryName) {
        if (countryName == null) return R.drawable.earthh;

        String name = countryName.trim().toLowerCase();

        switch (name) {
            case "france":
                return R.drawable.flag_france;
            case "usa":
                return R.drawable.flag_usa;
            case "germany":
                return R.drawable.flag_germany;
            case "italy":
                return R.drawable.flag_italy;
            case "spain":
                return R.drawable.flag_spain;
            case "jordan":
                return R.drawable.flag_jordan;
            case "egypt":
                return R.drawable.flag_egypt;
            case "palestine":
                return R.drawable.flag_palestine;
            default:
                return R.drawable.earthh;
        }
    }



    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Country");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit_country, null);
        EditText editName = view.findViewById(R.id.editCountryName);
        EditText editCapital = view.findViewById(R.id.editCapital);
        EditText editDescription = view.findViewById(R.id.editDescription);

        builder.setView(view);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String name = editName.getText().toString().trim();
            String capital = editCapital.getText().toString().trim();
            String description = editDescription.getText().toString().trim();

            if (!name.isEmpty() && !capital.isEmpty()) {
                // ✅ جلب صورة العلم الصحيحة
                int flagResId = getFlagResId(name);

                // ✅ إنشاء الكائن مع الصورة
                Country country = new Country(name, capital, description, flagResId);
                db.countryDao().insert(country);

                // ✅ تحديث القائمة
                List<Country> updatedList = db.countryDao().getAllCountries();
                adapter.setCountries(updatedList);

                Toast.makeText(this, "Country added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Name and Capital required", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.setCountries(db.countryDao().getAllCountries());
    }
}

