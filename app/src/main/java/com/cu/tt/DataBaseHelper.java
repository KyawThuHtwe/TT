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
    public static final String COL_2="FROM_TIME";
    public static final String COL_3="TO_TIME";
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
        db.execSQL("CREATE TABLE "+TABLE_NAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT,FROM_TIME TEXT,TO_TIME TEXT,SUBJECT TEXT,TYPE TEXT,ROOM TEXT,TEACHER TEXT,CONTACT TEXT,NOTE TEXT,DAY TEXT)");
        //
        db.execSQL("CREATE TABLE ROLL_CALL (ID INTEGER PRIMARY KEY AUTOINCREMENT,DATE TEXT,SUBJECT TEXT,VOTE TEXT,DAY TEXT,S_SID TEXT)");
        //
        db.execSQL("CREATE TABLE NOTE (ID INTEGER PRIMARY KEY AUTOINCREMENT,TITLE TEXT,SUBJECT TEXT,TIME TEXT,DATE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        //
        db.execSQL("DROP TABLE IF EXISTS ROLL_CALL");
        //
        db.execSQL("DROP TABLE IF EXISTS NOTE");

    }
    public boolean insertNote(String title,String subject,String time,String date){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("TITLE",title);
        contentValues.put("SUBJECT",subject);
        contentValues.put("TIME",time);
        contentValues.put("DATE",date);
        long result=db.insert("NOTE",null,contentValues);
        db.close();

        if(result==-1){
            return false;
        }else {
            return true;
        }
    }
    public Cursor getNote(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("Select * from NOTE",null);
        return res;
    }
    public boolean updateNote(String id,String title,String subject,String time,String date){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("TITLE",title);
        contentValues.put("SUBJECT",subject);
        contentValues.put("TIME",time);
        contentValues.put("DATE",date);
        int result=db.update("NOTE",contentValues,"ID=?",new String[]{id});
        if(result>0){
            return true;
        }else {
            return false;
        }
    }

    public boolean insertRollCall(String date,String Subject,String vote,String day,String sid){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("DATE",date);
        contentValues.put("SUBJECT",Subject);
        contentValues.put("VOTE",vote);
        contentValues.put("DAY",day);
        contentValues.put("S_SID",sid);
        long result=db.insert("ROLL_CALL",null,contentValues);
        db.close();

        if(result==-1){
            return false;
        }else {
            return true;
        }
    }

    public Cursor getVote(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("Select * from ROLL_CALL",null);
        return res;
    }
    public boolean updateVote(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("VOTE","0");
        int result=db.update("ROLL_CALL",contentValues,"ID=?",new String[]{id});
        if(result>0){
            return true;
        }else {
            return false;
        }
    }
    public int deleteNote(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        int res=db.delete("NOTE","ID=?",new String[]{id});
        return res;
    }
    public int deleteVote(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        int res=db.delete("ROLL_CALL","ID=?",new String[]{id});
        return res;
    }
    public boolean deleteNoteTable(){
        SQLiteDatabase db=this.getReadableDatabase();
        int affectedRows=db.delete("NOTE",null,null);
        return affectedRows>0;
    }
    public boolean deleteRollCallTable(){
        SQLiteDatabase db=this.getReadableDatabase();
        int affectedRows=db.delete("ROLL_CALL",null,null);
        return affectedRows>0;
    }
    public boolean deleteTimeTable(){
        SQLiteDatabase db=this.getReadableDatabase();
        int affectedRows=db.delete("Time_table",null,null);
        return affectedRows>0;
    }

    public boolean insertData(String from_time,String to_time,String subject,String type,String room,String teacher,String contact,String note,String day){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_2,from_time);
        contentValues.put(COL_3,to_time);
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
    public boolean updateData(String id,String from_time,String to_time,String subject,String type,String room,String teacher,String contact,String note){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_2,from_time);
        contentValues.put(COL_3,to_time);
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
    public boolean updateS(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_4,"Subject");
        int result=db.update(TABLE_NAME,contentValues,"ID=?",new String[]{id});
        if(result>0){
            return true;
        }else {
            return false;
        }
    }
    public Cursor selectTotalCount(){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor count=sqLiteDatabase.rawQuery("Select SUBJECT,Count(*) count from "+TABLE_NAME+" Group By SUBJECT Having count>0",null);
        return count;
    }

}
