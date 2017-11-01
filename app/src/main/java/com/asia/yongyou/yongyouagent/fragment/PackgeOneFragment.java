package com.asia.yongyou.yongyouagent.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;

import com.asia.yongyou.yongyouagent.R;
import com.asia.yongyou.yongyouagent.activity.manager.LocalManager;
import com.asia.yongyou.yongyouagent.common.MyBaseAdapter;
import com.asia.yongyou.yongyouagent.common.PackgeOneListAdapter;
import com.asia.yongyou.yongyouagent.common.PackgeQuryListAdapter;
import com.asia.yongyou.yongyouagent.constant.Constant;
import com.asia.yongyou.yongyouagent.entity.PackagesByPriceVo;
import com.asia.yongyou.yongyouagent.entity.PackagesVo;
import com.asia.yongyou.yongyouagent.utils.Java3DESUtil;
import com.asia.yongyou.yongyouagent.utils.ParseJsonUtils;
import com.asia.yongyou.yongyouagent.utils.ViewUtils;
import com.asia.yongyou.yongyouagent.ws.BaseResponseHandler;
import com.asia.yongyou.yongyouagent.ws.WSUser;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 选择套餐1
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentOneInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PackgeOneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PackgeOneFragment extends Fragment implements View.OnClickListener, SearchView.OnQueryTextListener,
        MyBaseAdapter.onSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_PARAM1 = "param1";
    public static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private PackagesVo mPackagesVo;
    private OnFragmentOneInteractionListener mListener;
    private ListView listView;
    private View view;
    private Context context;
    private MyBaseAdapter listAdapter;
    private int packgeTwoSelected;
    private SearchView searchView;
    private String telPhone;
    private String agentNum;

    public PackgeOneFragment() {
        // Required empty public constructor
    }

    public int getPackgeTwoSelected() {
        return packgeTwoSelected;
    }

    public void setPackgeTwoSelected(int packgeTwoSelected) {
        this.packgeTwoSelected = packgeTwoSelected;
        if (packgeTwoSelected > 0) {
            HashMap<Integer, Boolean> map = listAdapter.getIsSelected();
            for (int i = 0; i < map.size(); i++) {
                map.put(i, false);
            }
            listAdapter.setIsSelected(map);
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param packagesVo Parameter 1.
     * @return A new instance of fragment PackgeOneFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PackgeOneFragment newInstance(PackagesVo packagesVo) {
        PackgeOneFragment fragment = new PackgeOneFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, packagesVo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context == null) this.context = getActivity();
        this.context = context;
        this.mListener = (OnFragmentOneInteractionListener) context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPackagesVo = (PackagesVo) getArguments().getSerializable(ARG_PARAM1);
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences("openInfo", Context.MODE_PRIVATE);
        telPhone = sharedPreferences.getString("openTel", null);
        agentNum = LocalManager.getInstance(context).getAgentNum();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_packge_item, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        searchView = (SearchView) view.findViewById(R.id.searchView);
        searchView.setFocusable(false);
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("请输入搜索内容");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listAdapter = new PackgeOneListAdapter(context, mPackagesVo.getProducts1().getProducts());
        listView.setAdapter(listAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        view.setOnClickListener(this);
        searchView.setOnQueryTextListener(this);
        setAdapterListener();
    }

    private void setAdapterListener() {
        listAdapter.setSelectedListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mListener != null) {
            mListener = null;
        }
        if (mPackagesVo != null) {
            mPackagesVo = null;
        }
        if (listAdapter != null) {
            listAdapter = null;
        }
        if (telPhone != null) {
            telPhone = null;
        }
        if (agentNum != null) {
            agentNum = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id != R.id.searchView) {
            HideSoftInput(view.getWindowToken());
        }
    }

    // 隐藏软键盘
    private void HideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (manager.isActive())
                manager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public boolean onQueryTextSubmit(final String query) {

        if (!TextUtils.isEmpty(query) && TextUtils.isDigitsOnly(query)) {
            int price = Integer.parseInt(query) * 100;
            WSUser.queryPackageByPrice(context, Java3DESUtil.encryptThreeDESECB(telPhone),
                    agentNum, "1", String.valueOf(price),
                    new BaseResponseHandler((Activity) context, true, "正在搜索,请稍等......") {
                        @Override
                        public void onStart() {
                            super.onStart();
                        }

                        @Override
                        public void onSuccess(int statusCode, JSONObject response) {
                            super.onSuccess(statusCode, response);
                            try {
                                if (response.getString("status").equals(Constant.STATUS_OK)) {
                                    PackagesByPriceVo packagesByPriceVo = (PackagesByPriceVo) ParseJsonUtils.getInstance().getObject(response.toString(), PackagesByPriceVo.class);
                                    if (packagesByPriceVo.getProducts().size() < 1) {
                                        ViewUtils.showToast((Activity) context, "未查到相关信息!");
                                        return;
                                    }
                                    listAdapter = new PackgeQuryListAdapter(context, packagesByPriceVo.getProducts());
                                    listView.setAdapter(listAdapter);
                                    setAdapterListener();
                                    onSelectedItemCount(0);
                                } else {
                                    String error = response.getString("msg");
                                    ViewUtils.showToast((Activity) context, error);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Throwable error, String content) {
                            super.onFailure(error, content);
                        }

                        @Override
                        public void onFinish() {
                            super.onFinish();
                        }
                    });
        } else if (!TextUtils.isEmpty(query)) {
            //TODO 搜索其他内容
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }

    //监听选择产品
    @Override
    public void onSelectedItem(int position, PackagesVo.Products1Bean.ProductsBean productsBean) {
        HashMap<Integer, Boolean> map = listAdapter.getIsSelected();
        for (int i = 0; i < map.size(); i++) {
            if (i == position) {
                map.put(i, true);
            } else {
                map.put(i, false);
            }
        }
        listAdapter.setIsSelected(map);

        if (mListener != null) {
            mListener.onFragmentOneInteraction(productsBean);
        }
    }

    @Override
    public void onSelectedItem(int position, PackagesVo.Products2Bean.ProductsBeanX productsBeanX) {

    }

    @Override
    public void onSelectedItem(int position, PackagesByPriceVo.ProductsBean productsBean) {
        HashMap<Integer, Boolean> map = listAdapter.getIsSelected();
        for (int i = 0; i < map.size(); i++) {
            if (i == position) {
                map.put(i, true);
            } else {
                map.put(i, false);
            }
        }
        listAdapter.setIsSelected(map);

        if (mListener != null) {
            mListener.onFragmentOneInteraction(productsBean);
        }
    }

    @Override
    public void onSelectedItemCount(int selected) {

        if (mListener != null) {
            mListener.onFragmentOneInteraction(selected);
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentOneInteractionListener {

        void onFragmentOneInteraction(int selected);

        void onFragmentOneInteraction(PackagesVo.Products1Bean.ProductsBean productsBean);

        void onFragmentOneInteraction(PackagesByPriceVo.ProductsBean productsBean);
    }
}
