package com.example.xinji.androidlabs;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class MessageFragment extends Fragment {
    private View view;
    private TextView fragmentMessage;
    private TextView fragmentId;
    private Button deleteButton;
    Bundle bundle;
    private SQLiteDatabase writableDB;
    protected ChatWindow chatWindow;

    private boolean isTablet;

    public void onCreate(Bundle b) {
        super.onCreate(b);
        bundle = this.getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_message_fragment, container, false);
        ChatDatabaseHelper chatDatabaseHelper = new ChatDatabaseHelper(getActivity());
        writableDB = chatDatabaseHelper.getWritableDatabase();

        // Lab 7 -  step 5
        final long id = bundle.getLong("id");
        String message = bundle.getString("message");
        final boolean isTablet = bundle.getBoolean("isTablet");
        fragmentId = view.findViewById(R.id.fragmentId);
        fragmentId.setText("ID=" + id);
        fragmentMessage = view.findViewById(R.id.fragmentMessage);
        fragmentMessage.setText(message);

        deleteButton = view.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isTablet){
                    writableDB.delete(ChatDatabaseHelper.TABLE_NAME,
                            ChatDatabaseHelper.KEY_ID + "=" + id, null);
                    //FragmentTransaction ft = getFragmentManager().beginTransaction();
                    //ft.remove(MessageFragment.this);
                    // ft.commit();
                    getActivity().finish();
                    Intent intent = getActivity().getIntent();
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent();
                    intent.putExtra("id", id);
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
                }
            }
        });
        return view;
    }

    public void setIsTablet(boolean isTablet){
        this.isTablet = isTablet;
    }
}