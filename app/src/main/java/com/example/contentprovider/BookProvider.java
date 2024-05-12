package com.example.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class BookProvider extends ContentProvider {

    private static final String AUTHORITY = "com.example.contentprovider.provider";
    private static final String PATH_BOOKS = "books";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH_BOOKS);

    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        // Khởi tạo database, ví dụ SQLite
        SQLiteOpenHelper dbHelper = new SQLiteOpenHelper(getContext(), "BooksDB", null, 1) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL("CREATE TABLE Books (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, author TEXT)");
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                db.execSQL("DROP TABLE IF EXISTS Books");
                onCreate(db);
            }
        };
        db = dbHelper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return db.query("Books", projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = db.insert("Books", null, values);
        if (id != -1) {
            getContext().getContentResolver().notifyChange(uri, null);
            return Uri.withAppendedPath(CONTENT_URI, String.valueOf(id));
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return db.delete("Books", selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return db.update("Books", values, selection, selectionArgs);
    }

    @Override
    public String getType(Uri uri) {
        return "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + PATH_BOOKS;
    }
}
