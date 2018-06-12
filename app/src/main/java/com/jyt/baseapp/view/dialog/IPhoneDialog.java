package com.jyt.baseapp.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jyt.baseapp.R;

/**
 * @author LinWei on 2018/6/4 15:46
 */
public class IPhoneDialog extends Dialog {
    private View mView;
    private Context mContext;
    private TextView mTvTitle;
    private TextView mTvSubmit;
    private TextView mTvCancel;
    private EditText mEtInput;
    private boolean mIsShow;

    public IPhoneDialog(@NonNull Context context) {
        super(context, R.style.IPhoneDialog);
        init(context);
    }

    public IPhoneDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, R.style.IPhoneDialog);
        init(context);
    }

    protected IPhoneDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mView);
    }

    private void init(Context context){
        mContext = context;
        mView = View.inflate(context, R.layout.dialog_input ,null);
        mTvTitle = (TextView) mView.findViewById(R.id.tv_title);
        mTvSubmit = (TextView) mView.findViewById(R.id.tv_submit);
        mTvCancel = (TextView) mView.findViewById(R.id.tv_cancel);
        mEtInput = (EditText) mView.findViewById(R.id.et_input);

        mTvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.ClickSubmit(mIsShow , mEtInput.getText().toString());
                }
                if (isShowing()){
                    dismiss();
                }
            }
        });
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.ClickCancel();
                }
                if (isShowing()){
                    dismiss();
                }
            }
        });


    }


    public void setInputShow(boolean isShow){
        mIsShow = isShow;
        if (isShow){
            mEtInput.setVisibility(View.VISIBLE);
        }else {
            mEtInput.setVisibility(View.GONE);
        }
    }

    public void setTitle(String title){
        if (title!=null){
            mTvTitle.setText(title);
        }
    }

    public void setInputText(String text){
        if (text!=null){
            mEtInput.setText(text);
        }
    }

    public void setInputLength(int length){
        mEtInput.setMaxEms(length);
    }

    private OnIPhoneClickListener listener;
    public void setOnIPhoneClickListener(OnIPhoneClickListener listener){
        this.listener = listener;
    }
    public interface OnIPhoneClickListener{
        void ClickSubmit(boolean isShow , String input);
        void ClickCancel();
    }

    public void setInputLine(int line){
        mEtInput.setLines(line);
    }


}
