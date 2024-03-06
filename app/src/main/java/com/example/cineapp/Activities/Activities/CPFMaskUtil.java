package com.example.cineapp.Activities.Activities;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class CPFMaskUtil {

    public static String unmask(String cpf) {
        return cpf.replaceAll("[^0-9]", "");
    }

    public static String mask(String cpf) {
        String mask = "";
        int j = 0;
        for (int i = 0; i < cpf.length(); i++) {
            if (i == 3 || i == 6) {
                mask += ".";
            } else if (i == 9) {
                mask += "-";
            }
            mask += cpf.charAt(i);
        }
        return mask;
    }

    public static TextWatcher insert(final EditText editText) {
        return new TextWatcher() {
            boolean isUpdating;
            String old = "";

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = CPFMaskUtil.unmask(s.toString());
                String mask;
                mask = CPFMaskUtil.mask(str);
                editText.removeTextChangedListener(this);
                editText.setText(mask);
                editText.setSelection(mask.length());
                editText.addTextChangedListener(this);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        };
    }
}
