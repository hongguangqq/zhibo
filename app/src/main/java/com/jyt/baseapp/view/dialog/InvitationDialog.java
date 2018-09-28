package com.jyt.baseapp.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.TextView;

import com.jyt.baseapp.R;

/**
 * @author LinWei
 *         2018/9/28
 * @updateDes ${TODO}
 */
public class InvitationDialog extends Dialog {
    private View mView;
    private TextView mTvCode;

    public InvitationDialog(@NonNull Context context) {
        super(context, R.style.IPhoneDialog);
        init(context);
    }

    public InvitationDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, R.style.IPhoneDialog);
        init(context);
    }

    protected InvitationDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mView);

    }

    private void init(Context context){
        mView = View.inflate(context, R.layout.dialog_invitation ,null);
        mTvCode = mView.findViewById(R.id.tv_code);
        mTvCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
