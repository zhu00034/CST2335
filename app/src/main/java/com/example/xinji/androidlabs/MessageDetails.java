package com.example.xinji.androidlabs;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class MessageDetails extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        MessageFragment messageFragment = new MessageFragment();
        Bundle bundle = getIntent().getBundleExtra("bundle");
        messageFragment.setArguments(bundle);
        FragmentManager fragmentManager =getFragmentManager();

        //remove previous fragment
        if (fragmentManager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = fragmentManager.getBackStackEntryAt(0);
            fragmentManager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.addToBackStack(null).replace(R.id.phoneFrameLayout, messageFragment);
        fragmentTransaction.commit();
    }
}
