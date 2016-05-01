package com.k_nakamura.horiojapan.kousaku.saitama_u.jobhuntmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

public class EditActivity extends ActionBarActivity{
    static final String TAG = "EditActivity";
    static final int MENU_ID_SAVE = 1;

    EditText companyNameEditText;
    EditText memoEditText;

    Company company;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        findViews();

        Intent intent = getIntent();
        company = (Company)intent.getSerializableExtra("Company");
        loadCompany();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    private void loadCompany()
    {
        if(company.getId() == 0) return;

        companyNameEditText.setText(company.getCompany());
        memoEditText.setText(company.getMemo());
    }

    protected void findViews(){
        companyNameEditText = (EditText)findViewById(R.id.companyNameEditText);
        memoEditText = (EditText)findViewById(R.id.memoEditText);
    }


    protected void saveItem(){
        company.setCompany(companyNameEditText.getText().toString());
        company.setMemo(memoEditText.getText().toString());
        Date dateNow = new Date ();
        company.setLastupdate(dateNow.toLocaleString());
    }

    /*
     *  オプションメニュー作成
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //return super.onCreateOptionsMenu(menu);
        menu.add(0,MENU_ID_SAVE,0,"SAVE").setIcon(android.R.drawable.ic_menu_save).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == MENU_ID_SAVE) {
            saveItem();

            Intent intent = new Intent();
            intent.putExtra("RESULT", company);
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}