package com.example.contentprovider;


import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextName, editTextAuthor;
    private Button buttonSave, buttonLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = findViewById(R.id.editTextName);
        editTextAuthor = findViewById(R.id.editTextAuthor);
        buttonSave = findViewById(R.id.buttonSave);
        buttonLoad = findViewById(R.id.buttonLoad);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBook();
            }
        });

        buttonLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadBooks();
            }
        });
    }

    private void saveBook() {
        String name = editTextName.getText().toString();
        String author = editTextAuthor.getText().toString();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("author", author);

        Uri booksUri = Uri.parse("content://com.example.contentprovider.provider/books");
        getContentResolver().insert(booksUri, values);
        Toast.makeText(this, "Book saved", Toast.LENGTH_SHORT).show();
    }

    private void loadBooks() {
        Uri booksUri = Uri.parse("content://com.example.contentprovider.provider/books");
        Cursor cursor = getContentResolver().query(booksUri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            StringBuilder stringBuilder = new StringBuilder();
            while (!cursor.isAfterLast()) {
                stringBuilder.append("\n").append(cursor.getString(cursor.getColumnIndex("name")))
                        .append(" - ").append(cursor.getString(cursor.getColumnIndex("author")));
                cursor.moveToNext();
            }
            Toast.makeText(this, stringBuilder.toString(), Toast.LENGTH_LONG).show();
            cursor.close();
        }
    }
}
