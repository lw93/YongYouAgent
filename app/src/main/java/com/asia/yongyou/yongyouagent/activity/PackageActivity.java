package com.asia.yongyou.yongyouagent.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.asia.yongyou.yongyouagent.R;
import com.asia.yongyou.yongyouagent.activity.open.OpenConnectBlueActivity;
import com.asia.yongyou.yongyouagent.activity.open.ValueAddedProdActivity;
import com.asia.yongyou.yongyouagent.common.PackgeFragmentAdapter;
import com.asia.yongyou.yongyouagent.entity.PackagesByPriceVo;
import com.asia.yongyou.yongyouagent.entity.PackagesVo;
import com.asia.yongyou.yongyouagent.entity.ProductVo;
import com.asia.yongyou.yongyouagent.fragment.PackgeOneFragment;
import com.asia.yongyou.yongyouagent.fragment.PackgeTwoFragment;
import com.asia.yongyou.yongyouagent.widget.DisableButton;

import java.util.ArrayList;
import java.util.List;

//import com.asia.yongyou.yongyouagent.activity.open.OpenConnectBlueActivity;
//import com.asia.yongyou.yongyouagent.activity.open.ValueAddedProdActivity;

/**
 * 选择套餐界面
 *
 * @author Created by liuwei
 * @time on 2017/10/9
 */
public class PackageActivity extends BaseActivity implements View.OnClickListener,
        PackgeOneFragment.OnFragmentOneInteractionListener, PackgeTwoFragment.OnFragmentTwoInteractionListener {

    private View headerView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView headerTitle;
    private ImageView headerBack;
    private List<String> titleList;
    private PackgeOneFragment packgeOneFragment;
    private PackgeTwoFragment packgeTwoFragment;
    private PackgeFragmentAdapter packgeAdapter;
    private List<Fragment> fragmentList;
    private TextView incrementCount;
    private ImageView arrowGo;
    private DisableButton button;
    private PackagesVo packagesVo;
    private ProductVo productVo;
    private RelativeLayout toChooseProd;
    private int allAmout;
    private PackagesVo.Products1Bean.ProductsBean productsBean;
    private PackagesVo.Products2Bean.ProductsBeanX productsBeanX;
    private PackagesByPriceVo.ProductsBean productsBeanByQury;
    private int oneCount = -1;
    private int twoCount = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
    }

    private void initData() {
        packagesVo = (PackagesVo) this.getIntent().getSerializableExtra("packagesVo");
        productVo = (ProductVo) this.getIntent().getSerializableExtra("productVo");
        System.out.println("接收到的packages：" + JSON.toJSONString(packagesVo));
    }

    private void initView() {
        setContentView(R.layout.activity_package);
        headerView = findViewById(R.id.packge_header);
        headerTitle = (TextView) headerView.findViewById(R.id.header_text);
        headerTitle.setText("选择套餐");
        headerBack = (ImageView) findViewById(R.id.header_left_image);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        incrementCount = (TextView) findViewById(R.id.increment_count);
        incrementCount.setText(allAmout + "项");
        arrowGo = (ImageView) findViewById(R.id.arrow_go);
        button = (DisableButton) findViewById(R.id.idconfirm_btn);
        enabledButton(false);
        toChooseProd = (RelativeLayout) this.findViewById(R.id.to_product);
        initTab(packagesVo);
    }

    private void initTab(PackagesVo packagesVo) {
        //TODO 请求并传入数据

        packgeOneFragment = PackgeOneFragment.newInstance(packagesVo);
        packgeTwoFragment = PackgeTwoFragment.newInstance(packagesVo);

        fragmentList = new ArrayList<Fragment>(2);
        fragmentList.add(packgeOneFragment);
        fragmentList.add(packgeTwoFragment);

        titleList = new ArrayList<String>(2);
        titleList.add(packagesVo.getProducts1().getProductTagName());
        titleList.add(packagesVo.getProducts2().getProductTagName());

        packgeAdapter = new PackgeFragmentAdapter(getSupportFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(packgeAdapter);
        viewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (headerBack != null) headerBack.setOnClickListener(this);
        if (toChooseProd != null) toChooseProd.setOnClickListener(this);
        if (button != null) button.setOnClickListener(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearAll();
    }

    private void clearAll() {
        if (packgeOneFragment != null) {
            packgeOneFragment = null;
        }
        if (packgeTwoFragment != null) {
            packgeTwoFragment = null;
        }
        if (productsBean != null) {
            productsBean = null;
        }
        if (productsBeanX != null) {
            productsBeanX = null;
        }
        if (productsBeanByQury != null) {
            productsBeanByQury = null;
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.header_left_image) {
            finish();
        } else if (id == R.id.to_product) {
            //TODO 跳转到增值服务
            chooseProd();
        } else if (id == R.id.idconfirm_btn) {
            StringBuilder builder = new StringBuilder();
            for (ProductVo.AddedProductsBean addedProductsBean : productVo.getAddedProducts()) {
                List<ProductVo.AddedProductsBean.ProductsBean> products = addedProductsBean.getProducts();
                for (ProductVo.AddedProductsBean.ProductsBean product : products) {
                    if (product.isSelected()) {
                        builder.append(product.getProductId());
                    }
                }
            }
            if (this.productsBean != null) {
                builder.append(productsBean.getProductId());
            } else if (this.productsBeanX != null) {
                builder.append(productsBeanX.getProductId());
            } else if (productsBeanByQury != null) {
                builder.append(productsBeanByQury.getProductId());
            }
            SharedPreferences sp = getSharedPreferences("prodIdList", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("prodIdList", builder.toString());
            editor.commit();
            startActivity(new Intent(PackageActivity.this, OpenConnectBlueActivity.class));
        }
    }

    //实现选择套餐1的监听,并设置PackgeTwoFragment不可用
    @Override
    public void onFragmentOneInteraction(int selected) {
        oneCount = selected;
        if (selected < 1 && twoCount < 1) enabledButton(false);
        packgeTwoFragment.setPackgeOneSelected(selected);
    }

    //套餐1选择的产品
    @Override
    public void onFragmentOneInteraction(PackagesVo.Products1Bean.ProductsBean productsBean) {
        if (productsBeanX != null) productsBeanX = null;
        this.productsBean = productsBean;
        if (this.productsBean == null) {
            enabledButton(false);
        } else {
            enabledButton(true);
        }
    }

    //搜索得到的套餐1
    @Override
    public void onFragmentOneInteraction(PackagesByPriceVo.ProductsBean productsBean) {
        if (this.productsBean != null) this.productsBean = null;
        if (productsBeanX != null) this.productsBeanX = null;
        this.productsBeanByQury = productsBean;
        if (this.productsBeanByQury == null) {
            enabledButton(false);
        } else {
            enabledButton(true);
        }
    }

    //实现选择套餐2的监听,并设置PackgeOneFragment不可用
    @Override
    public void onFragmentTwoInteraction(int selected) {
        twoCount = selected;
        if (selected < 1 && oneCount < 1) enabledButton(false);
        packgeOneFragment.setPackgeTwoSelected(selected);
    }

    //套餐2选择的产品
    @Override
    public void onFragmentTwoInteraction(PackagesVo.Products2Bean.ProductsBeanX productsBeanX) {
        if (productsBean != null) productsBean = null;
        this.productsBeanX = productsBeanX;
        if (this.productsBeanX == null) {
            enabledButton(false);
        } else {
            enabledButton(true);
        }
    }

    //搜索得到的套餐2
    @Override
    public void onFragmentTwoInteraction(PackagesByPriceVo.ProductsBean productsBean) {
        if (this.productsBean != null) this.productsBean = null;
        if (productsBeanX != null) this.productsBeanX = null;
        this.productsBeanByQury = productsBean;
        if (this.productsBeanByQury == null) {
            enabledButton(false);
        } else {
            enabledButton(true);
        }
    }

    private void enabledButton(boolean enable) {
        button.setDisableButtonClickable(enable);
        button.setEnabled(enable);
    }

    public void chooseProd() {
        Intent intent = new Intent(PackageActivity.this, ValueAddedProdActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("productVo", productVo);
        System.out.println("带走的productvo：" + JSON.toJSONString(productVo));
        intent.putExtras(bundle);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 2) {
            productVo = (ProductVo) data.getSerializableExtra("prodSelectedVo");
            System.out.println("接收到的productVo：" + JSON.toJSONString(productVo));
            allAmout = productVo.getAllSelectedCounts();
            incrementCount.setText(allAmout + "项");
        }
    }

}
