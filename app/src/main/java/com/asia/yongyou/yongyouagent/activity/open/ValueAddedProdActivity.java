package com.asia.yongyou.yongyouagent.activity.open;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.asia.yongyou.yongyouagent.R;
import com.asia.yongyou.yongyouagent.activity.BaseActivity;
import com.asia.yongyou.yongyouagent.activity.PackageActivity;
import com.asia.yongyou.yongyouagent.entity.ProductVo;
import com.asia.yongyou.yongyouagent.utils.ViewUtils;
import com.asia.yongyou.yongyouagent.widget.DisableButton;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;


public class ValueAddedProdActivity extends BaseActivity implements View.OnClickListener {

	private TextView title,productName,productAmout;
	private ImageView backTitle;
	private RecyclerView productContainer;
	private CommonAdapter<ProductVo.AddedProductsBean> mProductAdapter;
	private ProductVo productVo;
	private ArrayList<Integer> mSelectedDatas;
	private  int currentPosition;
	private DisableButton sureBtn;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_value_added_prod);
		initData();
		initView();

	}

	private void initData() {
//		mSelectedDatas=new ArrayList<Integer>();
		Intent intent = this.getIntent();
		productVo = (ProductVo) intent.getSerializableExtra("productVo");
		System.out.println("接收到的productVo："+ JSON.toJSONString(productVo));
	}

	private void initView() {
		title = (TextView) this.findViewById(R.id.header_text);
		title.setText("增值产品");
		backTitle = (ImageView) this.findViewById(R.id.header_left_image);
		backTitle.setVisibility(View.VISIBLE);
		backTitle.setOnClickListener(this);
		sureBtn= (DisableButton) this.findViewById(R.id.prod_sure_btn);
		sureBtn.setDisableButtonClickable(true);
		sureBtn.setOnClickListener(this);
		productContainer = (RecyclerView) this.findViewById(R.id.product_container);
		productContainer.setLayoutManager(new GridLayoutManager(this,2));
		mProductAdapter = new CommonAdapter<ProductVo.AddedProductsBean>(getApplicationContext(),R.layout.item_product,productVo.getAddedProducts()) {

			@Override
			protected void convert(ViewHolder holder, ProductVo.AddedProductsBean addedProductsBean, final int position) {
				holder.setText(R.id.product_name,productVo.getAddedProducts().get(position).getProductTagName());
				holder.setText(R.id.product_amout,productVo.getAddedProducts().get(position).getSelectAmout()+"项");
				holder.setOnClickListener(R.id.layout_item_product, new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						currentPosition=position;
						Intent intent = new Intent(ValueAddedProdActivity.this,ValueAddedProdDetailActivity.class);
						List<ProductVo.AddedProductsBean.ProductsBean> products =  productVo.getAddedProducts().get(position).getProducts();
						Bundle bundle = new Bundle();
						bundle.putSerializable("productsBean",products.toArray());
						bundle.putInt("position",position);
						intent.putExtras(bundle);
//						startActivity(intent);
						startActivityForResult(intent,position);

					}
				});
			}
		};
		productContainer.setAdapter(mProductAdapter);
	}

	@Override
	protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode==2){
				if(requestCode==currentPosition){
					mSelectedDatas = (ArrayList<Integer>) data.getSerializableExtra("mSelectedCounts");
					System.out.println("接收到的数据："+mSelectedDatas);
					if (null==mSelectedDatas||"".equals(mSelectedDatas)){
						productVo.getAddedProducts().get(requestCode).setSelectAmout(mSelectedDatas.size());
					}else {
						for (ProductVo.AddedProductsBean.ProductsBean productsBean : productVo.getAddedProducts().get(requestCode).getProducts()) {
							if (mSelectedDatas.contains(productsBean.getProductId())) {
								productsBean.setSelected(true);
							}else{
								productsBean.setSelected(false);
							}
						}
						productVo.getAddedProducts().get(requestCode).setSelectAmout(mSelectedDatas.size());
					}
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mProductAdapter.notifyItemChanged(requestCode);
						}
					});
				}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.header_left_image:
				//返回选择套餐页面
				finish();
			break;
			case R.id.prod_sure_btn:
				//点击确定按钮计算选中的所有值 并返回到选择套餐页面
				int allAmout=0;
				for (ProductVo.AddedProductsBean addedProductsBean : productVo.getAddedProducts()) {
					allAmout += addedProductsBean.getSelectAmout();
				}
				System.out.println("总共选中的项目数："+allAmout);
				productVo.setAllSelectedCounts(allAmout);
				Intent intent = new Intent(ValueAddedProdActivity.this,PackageActivity.class);
				intent.putExtra("allAmout",allAmout);
				intent.putExtra("prodSelectedVo", ((Serializable) productVo));
				setResult(2,intent);
				finish();
			break;

			default:
				break;
		}
	}


}
