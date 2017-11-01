package com.asia.yongyou.yongyouagent.activity.open;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.asia.yongyou.yongyouagent.R;
import com.asia.yongyou.yongyouagent.activity.BaseActivity;

import com.asia.yongyou.yongyouagent.entity.ProductVo;
import com.asia.yongyou.yongyouagent.widget.DisableButton;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ValueAddedProdDetailActivity extends BaseActivity implements View.OnClickListener {
	private RecyclerView prodDetailContainer;
	private CommonAdapter<ProductVo.AddedProductsBean.ProductsBean> mProdDetailAdapter;
	private Object []  produtcsBean;
	private List<ProductVo.AddedProductsBean.ProductsBean> mProductsBean;
	private CheckBox expendCheckBox;
	private TextView productDesc;
	private DisableButton sureBtn;
	private TextView prodDetailHeader;
	private ImageView backImg;
	private ArrayList<Integer> mSelectedCounts;
	private int position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_value_added_prod_detail);

		initData();
		initView();
	}

	private void initData() {
		mSelectedCounts=new ArrayList<Integer>();
		mProductsBean= new ArrayList<ProductVo.AddedProductsBean.ProductsBean>();
		position=this.getIntent().getIntExtra("position",0);
		System.out.println("position:::"+position);
		produtcsBean= ((Object[]) this.getIntent().getSerializableExtra("productsBean"));
		for (int i = 0; i < produtcsBean.length; i++) {
			mProductsBean.add((ProductVo.AddedProductsBean.ProductsBean) produtcsBean[i]);
			System.out.println("接收到的productsBean："+ ((ProductVo.AddedProductsBean.ProductsBean) produtcsBean[i]).getProductName() + "");
		}
	}

	private void initView() {
		prodDetailHeader= (TextView) this.findViewById(R.id.header_text);
		prodDetailHeader.setText("增值产品");
		backImg = (ImageView) this.findViewById(R.id.header_left_image);
		backImg.setOnClickListener(this);
		sureBtn= (DisableButton) this.findViewById(R.id.sure_btn);
		sureBtn.setDisableButtonClickable(true);
		sureBtn.setOnClickListener(this);
		prodDetailContainer = (RecyclerView) this.findViewById(R.id.prod_detail_container);
		prodDetailContainer.setLayoutManager(new LinearLayoutManager(this));
		mProdDetailAdapter= new CommonAdapter<ProductVo.AddedProductsBean.ProductsBean>(getApplicationContext(),R.layout.item_product_detail,mProductsBean) {

			@Override
			protected void convert(final ViewHolder holder, final ProductVo.AddedProductsBean.ProductsBean productsBean, final int position) {
				CheckBox selectedBox = holder.getView(R.id.selectView);
				selectedBox.setChecked(mProductsBean.get(position).isSelected());
//				selectedBox.setChecked(true);
				holder.setText(R.id.prod_detail_name,mProductsBean.get(position).getProductName());
				holder.setText(R.id.prod_detail_desc,mProductsBean.get(position).getProductDesc());
				holder.setChecked(R.id.expanded_btn,false);
				holder.setVisible(R.id.prod_detail_desc,false);
				final TextView productDesc=holder.getView(R.id.prod_detail_desc);
				selectedBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

							mProductsBean.get(position).setSelected(isChecked);
					}
				});
				CheckBox expandBtn = holder.getView(R.id.expanded_btn);
				expandBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if (isChecked){
							productDesc.setVisibility(View.VISIBLE);
						}else{
							productDesc.setVisibility(View.GONE);
						}
					}
				});

			}
		};
		prodDetailContainer.setAdapter(mProdDetailAdapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.header_left_image:
				finish();
			break;
			case R.id.sure_btn:
				// TODO: 2017/10/22  选中的条目数量携带回去
				for (ProductVo.AddedProductsBean.ProductsBean productsBean : mProductsBean) {
					if (productsBean.isSelected()){
						int productId = productsBean.getProductId();
						mSelectedCounts.add(productId);
					}
				}
				System.out.println("详情页选中的条目："+mSelectedCounts);
				Intent intent =new Intent(ValueAddedProdDetailActivity.this,ValueAddedProdActivity.class);
				intent.putExtra("mSelectedCounts", ((Serializable) mSelectedCounts));
				setResult(2,intent);
				finish();
//				startActivity(intent);

			break;
			default:
				break;
		}
	}
}
