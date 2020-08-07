package com.cu.tt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
    static final String DATABASE_NAME="TimeDb.db";
    static final String TABLE_NAME="Time_table";
    public static final String COL_1="ID";
    public static final String COL_2="FROMTIME";
    public static final String COL_3="TOTIME";
    public static final String COL_4="SUBJECT";
    public static final String COL_5="TYPE";
    public static final String COL_6="ROOM";
    public static final String COL_7="TEACHER";
    public static final String COL_8="CONTACT";
    public static final String COL_9="NOTE";
    public static final String COL_10="DAY";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT,FROMTIME TEXT,TOTIME TEXT,SUBJECT TEXT,TYPE TEXT,ROOM TEXT,TEACHER TEXT,CONTACT TEXT,NOTE TEXT,DAY TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
    }
    public boolean insertData(String fromtime,String totime,String subject,String type,String room,String teacher,String contact,String note,String day){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_2,fromtime);
        contentValues.put(COL_3,totime);
        contentValues.put(COL_4,subject);
        contentValues.put(COL_5,type);
        contentValues.put(COL_6,room);
        contentValues.put(COL_7,teacher);
        contentValues.put(COL_8,contact);
        contentValues.put(COL_9,note);
        contentValues.put(COL_10,day);

        long result=db.insert(TABLE_NAME,null,contentValues);
        db.close();

        if(result==-1){
            return false;
        }else {
            return true;
        }

    }
    public Cursor getAllData(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("Select * from "+TABLE_NAME,null);
        return res;
    }
    public int deleteData(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        int res=db.delete(TABLE_NAME,"ID=?",new String[]{id});
        return res;
    }
    public boolean updateData(String id,String fromtime,String totime,String subject,String type,String room,String teacher,String contact,String note){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_2,fromtime);
        contentValues.put(COL_3,totime);
        contentValues.put(COL_4,subject);
        contentValues.put(COL_5,type);
        contentValues.put(COL_6,room);
        contentValues.put(COL_7,teacher);
        contentValues.put(COL_8,contact);
        contentValues.put(COL_9,note);
        int result=db.update(TABLE_NAME,contentValues,"ID=?",new String[]{id});
        if(result>0){
            return true;
        }else {
            return false;
        }
    }
}
