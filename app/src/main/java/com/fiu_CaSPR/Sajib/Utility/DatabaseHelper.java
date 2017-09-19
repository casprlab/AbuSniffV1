package com.fiu_CaSPR.Sajib.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by fhu004 on 7/31/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Safebuk.db";
    public static final String SQL_CREATE_MAIN_TABLE = "CREATE TABLE `Check_In` (\n" +
            "\t`User_ID`\tTEXT NOT NULL UNIQUE,\n" +
            "\t`Check_In`\tTEXT NOT NULL,\n" +
            "\tFOREIGN KEY(User_ID)\n" +
            "\t\tReferences Safebuk(User_ID)\n" +
            ");\n" +
            "\n" +
            "CREATE TABLE `Movies` (\n" +
            "\t`User_ID`\tTEXT NOT NULL UNIQUE,\n" +
            "\t`Movies`\tTEXT NOT NULL,\n" +
            "\tFOREIGN KEY(User_ID)\n" +
            "\t\tReferences Safebuk(User_ID)\n" +
            ");\n" +
            "\n" +
            "\n" +
            "CREATE TABLE `Likes` (\n" +
            "\t`User_ID`\tTEXT NOT NULL UNIQUE,\n" +
            "\t`Likes`\t\tTEXT NOT NULL,\n" +
            "\tFOREIGN KEY(User_ID)\n" +
            "\t\tReferences Safebuk(User_ID)\n" +
            ");\n" +
            "\n" +
            "CREATE TABLE `Life_Events` (\n" +
            "\t`User_ID`\tTEXT NOT NULL UNIQUE,\n" +
            "\t`Life_Events`\tTEXT NOT NULL,\n" +
            "\tFOREIGN KEY(User_ID)\n" +
            "\t\tReferences Safebuk(User_ID)\n" +
            ");\n" +
            "\n" +
            "CREATE TABLE `Music` (\n" +
            "\t`User_ID`\tTEXT NOT NULL UNIQUE,\n" +
            "\t`Music`\tTEXT NOT NULL,\n" +
            "\tFOREIGN KEY(User_ID)\n" +
            "\t\tReferences Safebuk(User_ID)\n" +
            ");\n" +
            "CREATE TABLE `Past_Studies` (\n" +
            "\t`User_ID`\tTEXT NOT NULL UNIQUE,\n" +
            "\t`Past_Studies`\tTEXT NOT NULL,\n" +
            "\tFOREIGN KEY(User_ID)\n" +
            "\t\tReferences Safebuk(User_ID)\n" +
            ");\n" +
            "\n" +
            "CREATE TABLE `Mutual_Post` (\n" +
            "\t`User_ID`\tTEXT NOT NULL UNIQUE,\n" +
            "\t`Mutual_Post`\tTEXT NOT NULL,\n" +
            "\t'Type'\t\tText ,\n" +
            "\tFOREIGN KEY(User_ID)\n" +
            "\t\tReferences Safebuk(User_ID)\n" +
            ");";

    public static final String SQL_CREATE_SIDE_TABLES="CREATE TABLE `Safebuk` (\n" +
            "\t`User_ID`\tTEXT NOT NULL PRIMARY KEY UNIQUE,\n" +
            "\t`Name`\tTEXT NOT NULL,\n" +
            "\t`Current_City`\tTEXT NOT NULL,\n" +
            "\t`Hometown`\tTEXT,\n" +
            "\t`High_School`\tTEXT,\n" +
            "\t`College`\tTEXT,\n" +
            "\t`Work_Place`\tTEXT,\n" +
            "\t`Address`\tTEXT,\n" +
            "\t`Phone_Number`\tTEXT,\n" +
            "\t`Email_Address`\tTEXT,\n" +
            "\t`About`\tTEXT,\n" +
            "\t`No_Of_Mutual_Friends`\tINTEGER,\n" +
            "\t`No_Of_Profile_Pictures`\tINTEGER,\n" +
            "\t`Birthday`\tTEXT,\n" +
            "\t`Relationship_Status`\tTEXT,\n" +
            "\t`Is_Family_Member`\tTEXT,\n" +
            "\t`Friend_Since`\tTEXT,\n" +
            "\t`HTML_Source`\tBLOB NOT NULL\n" +
            ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_MAIN_TABLE);
        db.execSQL(SQL_CREATE_SIDE_TABLES);
    }

    public void addItem(){
        ContentValues values = new ContentValues();
        values.put("User_ID", "123");
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(DATABASE_NAME, null, values);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        //TODO: CREATE SQL_DELETE ENTRIES
        //FIXME;
        //db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
