package com.yesyoudreamagain.cursordemo;

import android.database.Cursor;
import android.database.CursorJoiner;
import android.database.CursorWrapper;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import static android.database.Cursor.FIELD_TYPE_BLOB;
import static android.database.Cursor.FIELD_TYPE_FLOAT;
import static android.database.Cursor.FIELD_TYPE_INTEGER;
import static android.database.Cursor.FIELD_TYPE_STRING;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    String[] columns = { "_id", "name", "age", "location" };
    Object[] row1 = { 1, "Ayesha", 25, "Alwar" };
    Object[] row2 = { 2, "Vaibhav", 32, "Delhi" };
    Object[] row3 = { 3, "Chandan", 38, "Gurgaon" };
    Object[] row4 = { 4, "Ankur", 31, "Ghaziabad" };

    MatrixCursor cursor = new MatrixCursor(columns);
    cursor.addRow(row1);
    cursor.addRow(row2);
    cursor.addRow(row3);

    MatrixCursor cursor2 = new MatrixCursor(columns);
    cursor2.addRow(row1);
    cursor2.addRow(row3);
    cursor2.addRow(row4);

    // traversing the cursors
    //if (cursor.isBeforeFirst()) {
    //  traverseFromFirstToLast(cursor);
    //}
    //if (cursor.isAfterLast()){
    //  traverseFromLastToFirst(cursor);
    //}

    // CursorJoiner Demo
    //cursoeJoinerDemo(cursor, cursor2);

    // MergeCursor Demo
    //mergeCursorDemo(cursor,cursor2);

  }

  private void mergeCursorDemo(Cursor cursor, Cursor cursor2) {
    MergeCursor mergeCursor = new MergeCursor(new Cursor[] { cursor, cursor2 });
    traverseFromFirstToLast(mergeCursor);
  }

  private void cursoeJoinerDemo(MatrixCursor cursor, MatrixCursor cursor2) {
    cursor.moveToFirst();
    cursor2.moveToFirst();

    CursorJoiner cursorJoiner =
        new CursorJoiner(cursor, new String[] { "_id" }, cursor2, new String[] { "_id" });
    for (CursorJoiner.Result result : cursorJoiner) {
      switch (result) {
        case BOTH:   // things unchanged
          int bId = cursor2.getInt(cursor2.getColumnIndex("_id"));
          String bName = cursor2.getString(cursor2.getColumnIndex("name"));
          System.out.println("Things unchanged are - " + bId + "\t" + bName);
          break;
        case LEFT:  // things are deleted
          int lId = cursor.getInt(cursor.getColumnIndex("_id"));
          String lName = cursor.getString(cursor.getColumnIndex("name"));
          System.out.println("Things deleted are - " + lId + "\t" + lName);
          break;
        case RIGHT: // things are added
          int rId = cursor2.getInt(cursor2.getColumnIndex("_id"));
          String rName = cursor2.getString(cursor2.getColumnIndex("name"));
          System.out.println("Things added are - " + rId + "\t" + rName);
          break;
      }
    }
  }

  private void traverseFromLastToFirst(Cursor cursor) {
    if (cursor.moveToLast()) {
      do {
        // iterating with while loop and moving in up direction of table format of cursor.
        printEntriesOfEachRow(cursor);
      } while (cursor.moveToPrevious());
    }
  }

  private void traverseFromFirstToLast(Cursor cursor) {
    if (cursor.moveToFirst()) {
      do {
        // iterating with while loop and moving in down direction of table format of cursor.
        printEntriesOfEachRow(cursor);
      } while (cursor.moveToNext());
    }
  }

  private void printEntriesOfEachRow(Cursor cursor) {
    String[] columnNames = cursor.getColumnNames();
    for (String columnName : columnNames) {
      int cursorColumnIndex = cursor.getColumnIndex(columnName);
      int cursorColumnDataType = cursor.getType(cursorColumnIndex);
      switch (cursorColumnDataType) {
        default: //FIELD_TYPE_NULL:
          System.out.print("null");
          break;
        case FIELD_TYPE_INTEGER:
          int cellDataAsInt = cursor.getInt(cursorColumnIndex);
          System.out.print(cellDataAsInt);
          break;
        case FIELD_TYPE_FLOAT:
          float cellDataAsFloat = cursor.getFloat(cursorColumnIndex);
          System.out.print(cellDataAsFloat);
          break;
        case FIELD_TYPE_STRING:
          String cellDataAsString = cursor.getString(cursorColumnIndex);
          System.out.print(cellDataAsString);
          break;
        case FIELD_TYPE_BLOB:
          Object cellDataAsBlob = cursor.getBlob(cursorColumnIndex);
          System.out.print(cellDataAsBlob);
          break;
      }
      System.out.print("\t");
    }
    System.out.println();
  }

  class MyCursorWrapper extends CursorWrapper {

    private Cursor cursor;

    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public MyCursorWrapper(Cursor cursor) {
      super(cursor);
      this.cursor = cursor;
    }

    // not allowing user to get more than 1 count
    @Override public int getCount() {
      if (cursor.getCount() > 1) {
        return 1;
      } else {
        return 0;
      }
    }
  }
}
