package com.example.xinji.androidlabs;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.xinji.androidlabs.ChatDatabaseHelper.TABLE_NAME;

public class ChatWindow extends Activity {

    // Lab4 - step 4: create class variable
    private static final String ACTIVITY_NAME = "ChatWindow";
    ListView chatView;
    EditText chatEdit;
    Button sendButton;

    ArrayList<String> list = new ArrayList<String>();
    ChatDatabaseHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;

    ChatAdapter messageAdapter;

    Boolean isTablet;
    FrameLayout tabletFrameLayout;
    int requestCode=1;
    int messageIndex;

    // Lab4 - step 4
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        Log.i(ACTIVITY_NAME, "In onCreate()");

        chatView = (ListView) findViewById(R.id.chatview);
        chatEdit = (EditText) findViewById(R.id.chatEdit);
        sendButton = (Button) findViewById(R.id.sendButton);

        //Lab7 - step 3
        tabletFrameLayout = (FrameLayout) findViewById(R.id.tablet_FrameLayout);

        if(tabletFrameLayout == null){
            isTablet = false;
            Log.i(ACTIVITY_NAME, "Using phone layout");
        }
        else{
            isTablet = true;
            Log.i(ACTIVITY_NAME, "Using tablet layout");
        }



        //Lab5 - step 5
        //create a ChatDatabaseHelper object
        dbHelper = new ChatDatabaseHelper(this);
        //get a writeable database and stores that as an instance variable
        db = dbHelper.getWritableDatabase();

        // Lab 4 - step 11
        messageAdapter = new ChatAdapter(this);
        chatView.setAdapter(messageAdapter);

            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String chatContent = chatEdit.getText().toString();
                    list.add(chatContent);

                    // Lab 4 - step 12
                    //this restarts the process of getCount()& getView()
                    messageAdapter.notifyDataSetChanged();
                    //clear the textView and ready for a new message
                    chatEdit.setText("");

                    //Lab5 - step 7
                    //insert rows into the database
                    ContentValues cValues = new ContentValues();
                    cValues.put("MESSAGE", chatContent);

                    //Lab5 -step 7
                    //don't need to insert KEY_ID, because it is autoincrement
                    db.insert(TABLE_NAME, "", cValues);
                }
		    });


         //Lab 5 - step 5
        //execute a query for the existing chat messages
        //Cursor contains rows from the rawQuery, similar to an iterator
        cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + cursor.getString(
                    cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));

            list.add(cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            cursor.moveToNext();
        }

        // Lab 5 - step 6
        Log.i(ACTIVITY_NAME, "Cursor's column count=" + cursor.getColumnCount());
        //print out the name of columns
        for(int i=0; i<cursor.getColumnCount();i++){
            System.out.println(cursor.getColumnName(i));
        }

        final Intent intent = new Intent(this, MessageDetails.class);

        //when user click on the message, show message detatils in a fragment
        chatView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String message = messageAdapter.getItem(position);
                long messageId = messageAdapter.getItemId(position);

                Bundle bundle = new Bundle();
                bundle.putLong("id", messageId);
                bundle.putString("message", message);
                bundle.putBoolean("isTablet", isTablet);

                if(isTablet == true) {
                    MessageFragment messageFragment = new MessageFragment();
                    messageFragment.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();

                    if (fragmentManager.getBackStackEntryCount() > 0) {
                        FragmentManager.BackStackEntry first = fragmentManager.getBackStackEntryAt(0);
                        fragmentManager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }

                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.tablet_FrameLayout, messageFragment).addToBackStack(null);;
                    ft.commit();
                }
                else{
                    Intent intent = new Intent(ChatWindow.this, MessageDetails.class);
                    intent.putExtra("bundle", bundle);
                    startActivityForResult(intent, requestCode);
                }
            }
        });

    }

    // Lab 7 - step 8
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == this.requestCode){
            Long id = data.getLongExtra("id", -1);
            db.delete("CHAT", ChatDatabaseHelper.KEY_ID + "=" + id, null);
            list.clear();
            cursor = db.rawQuery("select * from " + TABLE_NAME, null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + cursor.getString(
                        cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
                list.add(cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
                cursor.moveToNext();
            }

            messageAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    //close super() and also close the database opened in onCreate()
    @Override
    protected void onDestroy(){
        super.onDestroy();

        //Lab5 - step 9
        db.close();
        cursor.close();
        Log.i(ACTIVITY_NAME, "In onDestroy()");


    }

    // Lab 4 - step 5
    //inner class of ChatWindow
    private class ChatAdapter extends ArrayAdapter<String> {
        //constructor
        public ChatAdapter(Context ctx){
            super(ctx, 0);
        }

        // Lab 4 - step 6a
        //return the number of rows in listView
        @Override
        public int getCount(){
            return list.size();
        }
        // Lab 4 - step 6b
        //returns the item to show in the list at the specified position
        @Override
        public String getItem(int position) {
            return list.get(position);
        }

        // Lab4 - step 6c
        //return the layout that will be positioned at the specified row in the list
        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            // Lab 4 - step 10
            //set the layout of a row, result is a container that has objects from the layout
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null;
            if(position%2 == 0){
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            }else{
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            }

            TextView message = (TextView)result.findViewById(R.id.message_Text);
            message.setText(getItem(position));//get the string at position
            return result;

        }

        // Lab 4 - step 6d
        //return the database id of the item at specified position
        public long getItemId(int position){

            // Lab 7 - step 6
            cursor.moveToPosition(position);
            return cursor.getLong(cursor.getColumnIndex(ChatDatabaseHelper.KEY_ID));
        }

    }
}

