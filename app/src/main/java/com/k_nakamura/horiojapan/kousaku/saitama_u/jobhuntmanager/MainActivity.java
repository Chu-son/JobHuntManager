package com.k_nakamura.horiojapan.kousaku.saitama_u.jobhuntmanager;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements OnClickListener {
    static final String TAG = "MainActivity";
    static final int MENUITEM_ID_DELETE = 1;

    ListView itemListView;

    static DBAdapter dbAdapter;
    static NoteListAdapter listAdapter;
    static List<Note> noteList = new ArrayList<Note>();

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        setListeners();
        dbAdapter = new DBAdapter(this);
        listAdapter = new NoteListAdapter();
        itemListView.setAdapter(listAdapter);
        loadNote();
    }

    protected void findViews(){
        itemListView = (ListView)findViewById(R.id.itemListView);
    }
    protected void loadNote(){
        noteList.clear();
        // Read
        dbAdapter.open();
        Cursor c = dbAdapter.getAllNotes();
        startManagingCursor(c);
        if(c.moveToFirst()){
            do {
                Note note = new Note(
                        c.getInt(c.getColumnIndex(DBAdapter.COL_ID)),
                        c.getString(c.getColumnIndex(DBAdapter.COL_NOTE)),
                        c.getString(c.getColumnIndex(DBAdapter.COL_LASTUPDATE))
                );
                noteList.add(note);
            } while(c.moveToNext());
        }
        stopManagingCursor(c);
        dbAdapter.close();
        listAdapter.notifyDataSetChanged();
    }
    protected void setListeners(){
        itemListView.setOnCreateContextMenuListener(
                new OnCreateContextMenuListener(){
                    @Override public void onCreateContextMenu( ContextMenu menu, View v, ContextMenuInfo menuInfo) {
                        menu.add(0, MENUITEM_ID_DELETE, 0, "Delete");
                    }
                }
        );
    }

    @Override public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case MENUITEM_ID_DELETE:
                AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                Note note = noteList.get(menuInfo.position);
                final int noteId = note.getId();
                new AlertDialog.Builder(this)
                        .setIcon(R.mipmap.icon)
                        .setTitle("Are you sure you want to delete this note?")
                        .setPositiveButton( "Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override public void onClick(DialogInterface dialog, int which) {
                                        dbAdapter.open();
                                        if(dbAdapter.deleteNote(noteId)){
                                            Toast.makeText( getBaseContext(),
                                                    "The note was successfully deleted.",
                                                    Toast.LENGTH_SHORT
                                            ).show();
                                            loadNote();
                                        }
                                        dbAdapter.close();
                                    }
                                })
                        .setNegativeButton( "Cancel", null)
                        .show();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override public void onClick(View v) {
        switch(v.getId()){
        }
    }

    private class NoteListAdapter extends BaseAdapter {
        @Override public int getCount() {
            return noteList.size();
        }

        @Override public Object getItem(int position) {
            return noteList.get(position);
        }

        @Override public long getItemId(int position) {
            return position;
        }

        @Override public View getView(int position, View convertView, ViewGroup parent) {
            TextView noteTextView;
            TextView lastupdateTextView;
            View v = convertView;

            if(v==null){
                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.row, null);
            }

            Note note = (Note)getItem(position);
            if(note != null){
                noteTextView = (TextView)v.findViewById(R.id.noteTextView);
                lastupdateTextView = (TextView)v.findViewById( R.id.lastupdateTextView);
                noteTextView.setText(note.getNote());
                lastupdateTextView.setText(note.getLastupdate());
                //v.setTag(R.id.noteid, note);
            }

            return v;
        }
    }

    private void addContent()
    {
        Intent intent = new Intent(getApplication(), EditActivity.class);
        intent.putExtra("Note", new Note(0,"",""));
        int requestCode = 1000;
        startActivityForResult( intent, requestCode );
    }
    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if(resultCode != RESULT_OK) return;

        dbAdapter.open();
        dbAdapter.saveNote((Note)intent.getSerializableExtra("RESULT"));
        dbAdapter.close();
        loadNote();
    }

    /*
     *  オプションメニュー作成
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            addContent();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}