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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements OnClickListener {
    static final String TAG = "MainActivity";
    static final int MENUITEM_ID_DELETE = 1;
    static final int MENUITEM_ID_EDIT = 2;


    ListView itemListView;

    static DBAdapter dbAdapter;
    static CompanyListAdapter listAdapter;
    static List<Company> companyList = new ArrayList<Company>();

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        setListeners();
        dbAdapter = new DBAdapter(this);
        listAdapter = new CompanyListAdapter();
        itemListView.setAdapter(listAdapter);
        loadCompany();
    }

    protected void findViews(){
        itemListView = (ListView)findViewById(R.id.itemListView);
    }
    protected void loadCompany(){
        companyList.clear();
        // Read
        dbAdapter.open();
        Cursor c = dbAdapter.getAllCompanys();
        startManagingCursor(c);
        if(c.moveToFirst()){
            do {
                Company company = new Company(
                        c.getInt(c.getColumnIndex(DBAdapter.COL_ID)),
                        c.getString(c.getColumnIndex(DBAdapter.COL_COMPANY)),
                        c.getString(c.getColumnIndex(DBAdapter.COL_LASTUPDATE)),
                        c.getString(c.getColumnIndex(DBAdapter.COL_MEMO)),
                        c.getString(c.getColumnIndex(DBAdapter.COL_KANA)),
                        c.getString(c.getColumnIndex(DBAdapter.COL_SCHEDULETABLE))
                );
                companyList.add(company);
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
                        menu.add(0,MENUITEM_ID_EDIT,0,"Edit");
                        menu.add(0, MENUITEM_ID_DELETE, 0, "Delete");
                    }
                }
        );
    }

    @Override public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        Company company = companyList.get(menuInfo.position);
        final int companyId = company.getId();

        switch(item.getItemId()){
            case MENUITEM_ID_DELETE:
                new AlertDialog.Builder(this)
                        .setIcon(R.mipmap.icon)
                        .setTitle("Are you sure you want to delete this company?")
                        .setPositiveButton( "Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override public void onClick(DialogInterface dialog, int which) {
                                        dbAdapter.open();
                                        if(dbAdapter.deleteCompany(companyId)){
                                            Toast.makeText( getBaseContext(),
                                                    "The company was successfully deleted.",
                                                    Toast.LENGTH_SHORT
                                            ).show();
                                            loadCompany();
                                        }
                                        dbAdapter.close();
                                    }
                                })
                        .setNegativeButton( "Cancel", null)
                        .show();
                return true;

            case MENUITEM_ID_EDIT:
                editContent(company);

        }
        return super.onContextItemSelected(item);
    }

    @Override public void onClick(View v) {
        switch(v.getId()){
        }
    }

    private class CompanyListAdapter extends BaseAdapter {
        @Override public int getCount() {
            return companyList.size();
        }

        @Override public Object getItem(int position) {
            return companyList.get(position);
        }

        @Override public long getItemId(int position) {
            return position;
        }

        @Override public View getView(int position, View convertView, ViewGroup parent) {
            TextView companyTextView;
            TextView lastupdateTextView;
            View v = convertView;

            if(v==null){
                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.row, null);
            }

            Company company = (Company)getItem(position);
            if(company != null){
                companyTextView = (TextView)v.findViewById(R.id.companyTextView);
                lastupdateTextView = (TextView)v.findViewById( R.id.lastupdateTextView);
                companyTextView.setText(company.getCompany());
                lastupdateTextView.setText(company.getLastupdate());
            }

            return v;
        }
    }

    private void editContent(Company company)
    {
        Intent intent = new Intent(getApplication(), EditActivity.class);
        intent.putExtra("Company", company);
        int requestCode ;
        if(company.getId() == 0) requestCode = 1000;
        else requestCode = 2000;
        startActivityForResult( intent, requestCode );
    }
    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if(resultCode != RESULT_OK) return;

        dbAdapter.open();

        if(requestCode == 1000) dbAdapter.saveCompany((Company)intent.getSerializableExtra("RESULT"));
        if(requestCode == 2000) dbAdapter.update((Company)intent.getSerializableExtra("RESULT"));

        dbAdapter.close();
        loadCompany();
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
            editContent(new Company(0,"","","","",""));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}