package com.k_nakamura.horiojapan.kousaku.saitama_u.jobhuntmanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

public class EditActivity extends ActionBarActivity implements View.OnClickListener {
    static final String TAG = "EditActivity";

    EditText noteEditText;
    Button saveButton;

    Note note;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        findViews();
        setListeners();

        Intent intent = getIntent();
        note = (Note)intent.getSerializableExtra("Note");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    protected void findViews(){
        noteEditText = (EditText)findViewById(R.id.memoEditText);
        saveButton = (Button)findViewById(R.id.saveButton);
    }
    protected void setListeners(){
        saveButton.setOnClickListener(this);
    }

    @Override public void onClick(View v) {
        switch(v.getId()){
            case R.id.saveButton:
                Toast.makeText(this, "追加", Toast.LENGTH_SHORT).show();
                saveItem();

                Intent intent = new Intent();
                intent.putExtra("RESULT", note);
                setResult(RESULT_OK, intent);
                finish();

                break;
        }
    }

    protected void saveItem(){
        note.setNote(noteEditText.getText().toString());
        Date dateNow = new Date ();
        note.setLastupdate(dateNow.toLocaleString());
    }
}