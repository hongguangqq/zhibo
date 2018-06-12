package com.jyt.baseapp.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jyt.baseapp.R;

/**
 * @author LinWei on 2018/6/6 19:13
 */
public class PriceDialog extends Dialog {
    private View mView;
    private TextView mTvMax;
    private TextView mTvPro;
    private TextView mTvSubmit;
    private TextView mTvCancel;
    private SeekBar mSeekBar;

    public PriceDialog(@NonNull Context context) {
        super(context, R.style.IPhoneDialog);
        init(context);
    }

    public PriceDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, R.style.IPhoneDialog);
        init(context);
    }

    protected PriceDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mView);

    }

    private void init(Context context){
        mView = View.inflate(context, R.layout.dialog_price ,null);
        mTvMax = (TextView) mView.findViewById(R.id.tv_price_max);
        mTvPro = (TextView) mView.findViewById(R.id.tv_price_now);
        mTvSubmit = (TextView) mView.findViewById(R.id.tv_submit);
        mTvCancel = (TextView) mView.findViewById(R.id.tv_cancel);
        mSeekBar = (SeekBar) mView.findViewById(R.id.sb_price);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTvPro.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mTvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.Submit(mSeekBar.getProgress());
                }
            }
        });

    }


    public float getCurrent(){
        return mSeekBar.getProgress();
    }

    public void setMax(int max){
        mTvMax.setText(max+"");
        mSeekBar.setMax(max);
    }

    public void setCurrent(int num){
        mTvPro.setText(num+"");
        mSeekBar.setProgress(num);
    }




    private OnPriceClickListener listener;
    public void setOnProgressListener(OnPriceClickListener listener){
        this.listener = listener;
    }
    public interface OnPriceClickListener {
        void Submit(float price);
    }

}
