package com.jyt.baseapp.view.widget;

import android.content.Context;
import android.content.res.AssetManager;

import com.bigkoo.pickerview.OptionsPickerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * @author LinWei on 2018/5/3 16:51
 */
public class CityPickerView extends OptionsPickerView {

    private final Context mContext;

    // 省数据集合
    private ArrayList<String> mListProvince = new ArrayList<>();
    // 市数据集合
    private ArrayList<ArrayList<String>> mListCity = new ArrayList<>();
    // 区数据集合
    private ArrayList<ArrayList<ArrayList<String>>> mListArea = new ArrayList<>();
    private JSONObject mJsonObj;

    public CityPickerView(Context context,Builder builder) {
        super(builder);
        mContext = context;
        // 初始化Json对象
        initJsonData();
        // 初始化Json数据
        initJsonDatas();
        initCitySelect();
    }


    private void initCitySelect(){
        setPicker(mListProvince,mListCity);

    }

    private void initJsonData(){
        AssetManager assets = mContext.getAssets();
        try {
            InputStream is = assets.open("city.json");
            byte[] buf = new byte[is.available()];
            is.read(buf);
            String json = new String(buf, "UTF-8");
            mJsonObj = new JSONObject(json);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void initJsonDatas(){
        try {
            JSONArray jsprovinces = mJsonObj.getJSONArray("province");
            for (int i = 0; i < jsprovinces.length(); i++) {
                JSONObject jsonP = jsprovinces.getJSONObject(i);
                String province = jsonP.getString("name");//获取每个省的Json对象
                ArrayList<String> options2Items_01 = new ArrayList<>();
                JSONArray jsonCs = jsonP.getJSONArray("city");
                for (int j = 0; j < jsonCs.length(); j++) {
                    JSONObject jsonC = jsonCs.getJSONObject(j);// 获取每个市的Json对象
                    String city = jsonC.getString("name");
                    options2Items_01.add(city);// 添加市数据
                }
                mListProvince.add(province);// 添加省数据
                mListCity.add(options2Items_01);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mJsonObj = null;
    }

    public String getCity(int op1,int op2){
        return mListCity.get(op1).get(op2);
    }




}
