package com.jyt.baseapp.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.jyt.baseapp.R;
import com.jyt.baseapp.util.BaseUtil;

import butterknife.ButterKnife;

/**
 * @author LinWei on 2018/5/14 15:30
 */
public class PersonDialog extends AlertDialog {


    Button mBtnDperson0;
    Button mBtnDperson1;
    Button mBtnDperson2;
    Button mBtnDperson3;
    Button mBtnDperson4;
    private View mView;

    public PersonDialog(@NonNull Context context) {
        super(context);
        init(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mView);
        ButterKnife.bind(this);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.horizontalMargin = 0;//设置水平方向的Margin
        params.verticalMargin = 0;//设置竖直方向的Margin
        params.dimAmount = 0.7f;//背景黑暗度
        params.width = BaseUtil.getScannerWidth();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;//设置高度
        params.gravity = Gravity.BOTTOM;
        getWindow().setBackgroundDrawable(null);
        getWindow().setGravity(Gravity.BOTTOM);//对齐
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(params);//加入设置
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//背景黑暗
        getWindow().setWindowAnimations(R.style.MoveDialog);//加入动画style

    }

    private void init(Context context){
        mView = View.inflate(context,R.layout.dialog_person,null);
        mBtnDperson0 = (Button) mView.findViewById(R.id.btn_dperson0);
        mBtnDperson1 = (Button) mView.findViewById(R.id.btn_dperson1);
        mBtnDperson2 = (Button) mView.findViewById(R.id.btn_dperson2);
        mBtnDperson3 = (Button) mView.findViewById(R.id.btn_dperson3);
        mBtnDperson4 = (Button) mView.findViewById(R.id.btn_dperson4);
        mBtnDperson0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.ClickMenu0();
                }
            }
        });

        mBtnDperson1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.ClickMenu1();
                }
            }
        });

        mBtnDperson2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.ClickMenu2();
                }
            }
        });

        mBtnDperson3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.ClickMenu3();
                }
            }
        });

        mBtnDperson4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    public interface OnMenuClickListener {
        void ClickMenu0();

        void ClickMenu1();

        void ClickMenu2();

        void ClickMenu3();

    }

    public OnMenuClickListener listener;

    public void setOnMenuClickListener(OnMenuClickListener listener) {
        this.listener = listener;
    }

    public void setFollowTitle(boolean isFollow) {
        if (isFollow){
            mBtnDperson0.setText("取消关注");
        }else {
            mBtnDperson0.setText("关注");
        }
    }

}
