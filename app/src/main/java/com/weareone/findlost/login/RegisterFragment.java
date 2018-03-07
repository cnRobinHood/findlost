package com.weareone.findlost.login;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.weareone.findlost.R;
import com.weareone.findlost.entities.User;
import com.weareone.findlost.entities.Userinfo;

/**
 * Created by cnrobin on 17-12-11.
 * Just Enjoy It!!!
 */

public class RegisterFragment extends DialogFragment {
    private EditText username;
    private EditText passwd;
    private EditText qqNum;
    private EditText realName;
    private EditText phoneNum;
    private Button registerButton;
    private Button resittingButton;
    private RadioGroup sexRadioGroup;
    private RadioButton maleButton;
    private RadioButton femaleButton;
    private User user;
    private Userinfo userinfo;

    public static RegisterFragment getInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        qqNum = view.findViewById(R.id.register_qq);
        phoneNum = view.findViewById(R.id.register_phoneNum);
        username = view.findViewById(R.id.register_uname);
        passwd = view.findViewById(R.id.register_passwd);
        sexRadioGroup = view.findViewById(R.id.rg_sex);
        registerButton = view.findViewById(R.id.register_button);
        resittingButton = view.findViewById(R.id.resitting_button);
        realName = view.findViewById(R.id.register_realname);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserAndUserinfo();
                ((LoginActivity) getActivity()).registerUser(user, userinfo);
            }
        });
    }

    public void getUserAndUserinfo() {
        user = new User();
        user.setUsername(username.getText().toString());
        user.setPassword(passwd.getText().toString());
        userinfo = new Userinfo();
        userinfo.setPhonenum(phoneNum.getText().toString());
        userinfo.setQq(qqNum.getText().toString());
        userinfo.setUserrealname(realName.getText().toString());
        userinfo.setUsername(username.getText().toString());
        if (sexRadioGroup.getCheckedRadioButtonId() == R.id.radio_male) {
            userinfo.setSex(0);
        } else {
            userinfo.setSex(1);
        }
    }
}
