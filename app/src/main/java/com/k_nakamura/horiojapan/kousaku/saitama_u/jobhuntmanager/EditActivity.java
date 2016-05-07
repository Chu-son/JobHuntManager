package com.k_nakamura.horiojapan.kousaku.saitama_u.jobhuntmanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class EditActivity extends ActionBarActivity implements TextWatcher,View.OnClickListener {
    static final String TAG = "EditActivity";
    static final int MENU_ID_SAVE = 1;

    EditText companyNameEditText;
    EditText kanaEditText;
    EditText memoEditText;
    LinearLayout scheduleLinearLayout;
    Button addScheduleButton;

    Company company;
    ScheduleDBAdapter scheduleDBAdapter;

    int count = 0;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        findViews();

        Intent intent = getIntent();
        company = (Company)intent.getSerializableExtra("Company");
        loadCompanyData();

        scheduleDBAdapter = new ScheduleDBAdapter(this);
        loadScheduleData();

        addScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleLinearLayout.addView(addSchedule("","",""));
                count++;
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    private void loadCompanyData()
    {
        if(company.getId() == 0) return;

        companyNameEditText.setText(company.getCompany());
        kanaEditText.setText(company.getKana());
        memoEditText.setText(company.getMemo());
    }

    private void loadScheduleData()
    {
        if(company.getScheduleDB().isEmpty()) return;
        scheduleDBAdapter.open(company.getScheduleDB());
        Cursor c = scheduleDBAdapter.getAllCompanys();
        startManagingCursor(c);
        if(c.moveToFirst()){
            do {
                scheduleLinearLayout.addView(addSchedule(
                        c.getString(c.getColumnIndex(scheduleDBAdapter.COL_SCHEDULENAME)),
                        c.getString(c.getColumnIndex(scheduleDBAdapter.COL_DATE)),
                        c.getString(c.getColumnIndex(scheduleDBAdapter.COL_PLACE))
                ));
            } while(c.moveToNext());
        }
        stopManagingCursor(c);
        scheduleDBAdapter.close();
    }
    private LinearLayout addSchedule(String name,String date,String place)
    {
        LinearLayout layout = new LinearLayout(this);
        LinearLayout.LayoutParams params;

        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        /*EditText editText = new EditText(this);
        editText.setText(name);
        editText.setHint("Contents");
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 1f;
        layout.addView(editText,params);

        editText = new EditText(this);
        editText.setText(date);
        editText.setHint("Date");
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 1.5f;
        layout.addView(editText,params);

        editText = new EditText(this);
        editText.setText(place);
        editText.setHint("Place");
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 1f;
        layout.addView(editText,params);*/

        TextView textView = new TextView(this);
        textView.setText(name);
        textView.setHint("Contents");
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 1f;
        layout.addView(textView,params);

        textView = new TextView(this);
        textView.setText(date);
        textView.setHint("Date");
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 1.5f;
        layout.addView(textView,params);

        textView = new TextView(this);
        textView.setText(place);
        textView.setHint("Place");
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 1f;
        layout.addView(textView,params);

        layout.setOnClickListener(this);

        return layout;
    }
    private void saveSchedules()
    {
        if(company.getScheduleDB().isEmpty()) return;
        scheduleDBAdapter.open(company.getScheduleDB());
        for(int i = scheduleLinearLayout.getChildCount() - count ; i < scheduleLinearLayout.getChildCount() ; i++)
        {
            LinearLayout ll = (LinearLayout) scheduleLinearLayout.getChildAt(i);
            scheduleDBAdapter.saveSchedule(((EditText)ll.getChildAt(0)).getText().toString(),((EditText)ll.getChildAt(1)).getText().toString(),((EditText)ll.getChildAt(2)).getText().toString());
        }
        scheduleDBAdapter.close();
    }

    private PopupWindow mPopupWindow;

    @Override
    public void onClick(View v) {

        mPopupWindow = new PopupWindow(this);

        // レイアウト設定
        View popupView = getLayoutInflater().inflate(R.layout.popup_layout, null);
        popupView.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
            }
        });
        mPopupWindow.setContentView(popupView);

        // 背景設定
        //mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popup_background));

        // タップ時に他のViewでキャッチされないための設定
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);

        // 表示サイズの設定 今回は幅300dp
        float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
        mPopupWindow.setWindowLayoutMode((int) width, WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setWidth((int) width);
        mPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        // 画面中央に表示
        mPopupWindow.showAtLocation(findViewById(R.id.scheduleLinearLayout), Gravity.CENTER, 0, 0);
    }

    @Override
    protected void onDestroy() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
        super.onDestroy();
    }

    protected void findViews(){
        companyNameEditText = (EditText)findViewById(R.id.companyNameEditText);
        kanaEditText = (EditText)findViewById(R.id.kanaEditText);
        kanaEditText.addTextChangedListener(this);
        memoEditText = (EditText)findViewById(R.id.memoEditText);
        scheduleLinearLayout = (LinearLayout)findViewById(R.id.scheduleLinearLayout);
        addScheduleButton = (Button)findViewById(R.id.addSchedule);
    }


    protected void saveItem(){
        company.setCompany(companyNameEditText.getText().toString());
        company.setMemo(memoEditText.getText().toString());
        Date dateNow = new Date ();
        company.setLastupdate(dateNow.toLocaleString());
        company.setKana(kanaEditText.getText().toString());
        if(company.getScheduleDB().isEmpty())company.setScheduleDB("table"+company.getKana());

        saveSchedules();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
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