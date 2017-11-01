package com.asia.yongyou.yongyouagent.activity.open;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.asia.yongyou.yongyouagent.R;
import com.asia.yongyou.yongyouagent.activity.BaseActivity;
import com.asia.yongyou.yongyouagent.activity.PackageActivity;
import com.asia.yongyou.yongyouagent.activity.manager.LocalManager;
import com.asia.yongyou.yongyouagent.common.DividerItemDecoration;
import com.asia.yongyou.yongyouagent.constant.Constant;
import com.asia.yongyou.yongyouagent.entity.IsMatchVo;
import com.asia.yongyou.yongyouagent.entity.NumListVo;
import com.asia.yongyou.yongyouagent.entity.NumLvlVo;
import com.asia.yongyou.yongyouagent.entity.PackagesVo;
import com.asia.yongyou.yongyouagent.entity.ProductVo;
import com.asia.yongyou.yongyouagent.entity.SimCardNumVo;
import com.asia.yongyou.yongyouagent.utils.Java3DESUtil;
import com.asia.yongyou.yongyouagent.utils.ParseJsonUtils;
import com.asia.yongyou.yongyouagent.utils.ViewUtils;
import com.asia.yongyou.yongyouagent.widget.DisableButton;
import com.asia.yongyou.yongyouagent.ws.BaseResponseHandler;
import com.asia.yongyou.yongyouagent.ws.WSUser;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ChooseNumActivity extends BaseActivity implements View.OnClickListener {

	private LinearLayout searchNumLay,searchSimLay,selectedNumLay,selectedSimLay,
										searchNumAndLvlLay;
	private RelativeLayout chooseNumLevelLay;
	private RelativeLayout defaultLayout;
	private TextView selectedNum,selectedAmout,selectedMinRent;
	private TextView selectedSim;
	private TextView numLevel;
	private EditText numFuzzyInput,simFuzzyInput;
	private DisableButton searchNumBtn,searchSimBtn,choosePlanBtn;
	private Dialog bottomDialog;
	private View view;
	private TextView title;
	private ImageView headerback;
	private RecyclerView numLvlContainer,numContainer,simContainer;
	private CommonAdapter<String> mAdapter;
	private CommonAdapter<SimCardNumVo.DataBean> simAdapter;
	private ArrayList<String> mLvlDescDatas;
	private String currentLvl;
	private Map<String, String> levelData;
	private String currentTel,currentSim;
	private String telPhoneEnc;
	private PackagesVo packagesVo;
	private LoadMoreWrapper<NumListVo.DataBean>  mLoadMoreWrapper;
	private int currentPager;
	private List<NumListVo.DataBean> mNumDatas;
	private String simInput;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_num);

		initView();
		initData();
	}

	private void initData() {
		currentLvl="";
		mLvlDescDatas = new ArrayList<String>();
		currentPager=1;
		mNumDatas = new ArrayList<>();
	}

	private void initView() {
		choosePlanBtn = (DisableButton) this.findViewById(R.id.choose_plan_btn);
		choosePlanBtn.setOnClickListener(this);
		searchNumAndLvlLay = (LinearLayout) this.findViewById(R.id.search_num_level_lay);
		defaultLayout= (RelativeLayout) this.findViewById(R.id.default_layout);
		numContainer= (RecyclerView) this.findViewById(R.id.num_container);
		numContainer.setLayoutManager(new LinearLayoutManager(this));
		simContainer = (RecyclerView) this.findViewById(R.id.sim_container);
		simContainer.setLayoutManager(new LinearLayoutManager(this));
		title = (TextView) this.findViewById(R.id.header_text);
		title.setText("选择号码");
		headerback = (ImageView) this.findViewById(R.id.header_left_image);
		headerback.setVisibility(View.VISIBLE);
		headerback.setOnClickListener(this);
		chooseNumLevelLay = (RelativeLayout) this.findViewById(R.id.choose_level_lay);
		searchNumLay= (LinearLayout) this.findViewById(R.id.search_num_layout);
		searchSimLay= (LinearLayout) this.findViewById(R.id.search_sim_layout);
		selectedSim = (TextView) this.findViewById(R.id.sim_resources_selected);
		selectedNumLay = (LinearLayout) this.findViewById(R.id.layout_item_num_selected);
		selectedNum = (TextView) this.findViewById(R.id.num_resources_selected);
		selectedAmout = (TextView) this.findViewById(R.id.prestore_selected);
		selectedMinRent = (TextView) this.findViewById(R.id.min_consumption_selected);
		selectedSimLay = (LinearLayout) this.findViewById(R.id.layout_item_sim_selected);
		numLevel= (TextView) this.findViewById(R.id.num_level);
		numFuzzyInput = (EditText) this.findViewById(R.id.edit_short_num);
		numFuzzyInput.setOnClickListener(this);
		searchNumBtn = (DisableButton) this.findViewById(R.id.search_num_btn);
		simFuzzyInput = (EditText) this.findViewById(R.id.edit_short_sim);
		searchSimBtn = (DisableButton) this.findViewById(R.id.search_sim_btn);
		//sim卡的文本监听器
		simFuzzyInput.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				String shortSim = simFuzzyInput.getText().toString().trim();
				if (shortSim.length()!=5){
					searchSimBtn.setDisableButtonClickable(false);
				}else {
					searchSimBtn.setDisableButtonClickable(true);
				}
			}
		});
		chooseNumLevelLay.setOnClickListener(this);
		//手机号模糊查询的文本监听器
		numFuzzyInput.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				String shortNum = numFuzzyInput.getText().toString().trim();
				if (shortNum.length()<=6&&shortNum.length()>=3){
					searchNumBtn.setDisableButtonClickable(true);
				}else{
					searchNumBtn.setDisableButtonClickable(false);
				}

			}
		});
		searchNumBtn.setOnClickListener(this);
		searchSimBtn.setOnClickListener(this);
	}



	@Override
	public void onClick(View v) {

		switch (v.getId()){
			case R.id.header_left_image:
				finish();
			break;
			case R.id.choose_level_lay:
				//底部弹框,进行查找号码等级的网络请求
				WSUser.getNumLvl(ChooseNumActivity.this,null,new BaseResponseHandler(ChooseNumActivity.this,
						false,null){
					@Override
					public void onSuccess(int statusCode, JSONObject response) {
						super.onSuccess(statusCode, response);
						try {
							if (response.getString("status").equals(Constant.STATUS_OK)){
								NumLvlVo numLvlVo = (NumLvlVo) ParseJsonUtils.getInstance().getObject(response.toString(),NumLvlVo.class);
								levelData = numLvlVo.getLevelData();
								System.out.println("levelDatalevelDatalevelData:"+levelData);
								Iterator<String> iterator = levelData.keySet().iterator();
								while (iterator.hasNext()){
									mLvlDescDatas.add(iterator.next());
								}
								System.out.println("mLvlDescDatas:"+mLvlDescDatas);
								showBottomDialog(mLvlDescDatas);
							}else{
								ViewUtils.showToast(ChooseNumActivity.this,response.getString("msg"));
							}
						} catch (JSONException e) {
							Log.e("ChooseNumActivity",e.getMessage());
							ViewUtils.showToast(ChooseNumActivity.this,"获取号码等级失败！");
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						ViewUtils.showToast(ChooseNumActivity.this,"获取号码等级请求发送失败！");
					}
				});

			break;
			case R.id.layout_item_num_selected:
				//隐藏选中框，显示搜索框
				currentTel=null;
				selectedNumLay.setVisibility(View.GONE);
				searchNumLay.setVisibility(View.VISIBLE);
				chooseNumLevelLay.setVisibility(View.VISIBLE);
				numContainer.setVisibility(View.GONE);
			break;

			case R.id.layout_item_sim_selected:
				//隐藏sim卡框，显示搜索框
				currentSim=null;
				selectedSimLay.setVisibility(View.GONE);
				searchSimLay.setVisibility(View.VISIBLE);
				simContainer.setVisibility(View.GONE);
			break;

			case R.id.search_num_btn:
				currentPager=1;
				if ("".equals(currentLvl)||null==currentLvl){
					ViewUtils.showToast(ChooseNumActivity.this,"请选择号码等级！");
				}else{
					String numInput = numFuzzyInput.getText().toString().trim();
					// TODO: 2017/10/9 进行号码模糊查询的网络请求
					getFuzzyQueryNum(numInput,1);

				}

				break;
			case R.id.search_sim_btn:
				defaultLayout.setVisibility(View.VISIBLE);
				if ("".equals(currentLvl)||null==currentLvl){
					ViewUtils.showToast(ChooseNumActivity.this,"请选择号码等级！");
				}else{
					String simInputFive = simFuzzyInput.getText().toString().trim();
					simInput=simInputFive.substring(0,4);
					//TODO: 2017/10/16  进行 sim卡号模糊查询的请求
					getFuzzyQuerySim(simInput);
				}
				break;

			case R.id.choose_plan_btn:
				//开始选择套餐
				if ("".equals(currentTel)||null==currentTel){
					choosePlanBtn.setDisableButtonClickable(false);
				}else if ("".equals(currentSim)||null==currentSim){
					choosePlanBtn.setDisableButtonClickable(false);
				}else{
					choosePlanCheck();
				}

//				startActivity(new Intent(ChooseNumActivity.this,PackagesActivity.class));
			break;
			default:
				break;
		}

	}

	private void choosePlanCheck() {
		//首先校验手机号和sim卡号是否匹配
		WSUser.checkTelPhone(ChooseNumActivity.this,currentTel,currentSim,LocalManager.getInstance(this).getAgentNum(),
				new BaseResponseHandler(this,true,"正在校验..."){
					@Override
					public void onSuccess(int statusCode, JSONObject response) {
						super.onSuccess(statusCode, response);
						System.out.println("查询是否匹配isMatchResponse:"+response.toString());
						try {
							if (response.getString("status").equals("000000")){
								IsMatchVo isMatchVo= (IsMatchVo) ParseJsonUtils.getInstance().getObject(response.toString(),IsMatchVo.class);
								System.out.println("isMatch"+response.getString("isMatch"));
								//匹配 可以开始进行选择套餐请求.保存此号码
								if (isMatchVo.getIsMatch().equals("000000")){
									SharedPreferences sp=getSharedPreferences("openInfo", Context.MODE_PRIVATE);
									SharedPreferences.Editor editor = sp.edit();
									editor.putString("openTel",currentTel);
									editor.putString("openSim",currentSim);
									editor.commit();
									//查询套餐
									querySetMeal();
								}
								else{
									ViewUtils.showToast(ChooseNumActivity.this,response.getString("msg"));
								}

							}else{
								ViewUtils.showToast(ChooseNumActivity.this,response.getString("msg"));
							}
						} catch (JSONException e) {
							ViewUtils.showToast(ChooseNumActivity.this,"查询号码匹配异常！");
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						ViewUtils.showToast(ChooseNumActivity.this,"查询号码匹配请求失败！");
					}
				});
	}
	//匹配成功之后的选择套餐请求
	private void querySetMeal() {
		telPhoneEnc = Java3DESUtil.encryptThreeDESECB(currentTel);
		WSUser.choosePackages(this,telPhoneEnc,LocalManager.getInstance(this).getAgentNum(),new BaseResponseHandler(
				this,true,"正在查询套餐..."){
			@Override
			public void onSuccess(int statusCode, JSONObject response) {
				super.onSuccess(statusCode, response);
				System.out.println("查询套餐结果packagesresponse:"+response.toString());
				try {
					if (response.getString("status").equals(Constant.STATUS_OK)){
						//查询套餐成功
						packagesVo= (PackagesVo) ParseJsonUtils.getInstance().getObject(response.toString(),PackagesVo.class);
						System.out.println("查询的套餐："+JSON.toJSONString(packagesVo));
						//选择增值产品
						queryProduct();
					}else{
						ViewUtils.showToast(ChooseNumActivity.this,response.getString("msg"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
					ViewUtils.showToast(ChooseNumActivity.this,"查询套餐失败！");
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				ViewUtils.showToast(ChooseNumActivity.this,"查询套餐失败！");
			}
		});
	}

	private void queryProduct() {
		WSUser.chooseProduct(this,telPhoneEnc, LocalManager.getInstance(this).getAgentNum(),new BaseResponseHandler(
				ChooseNumActivity.this,false,null){
			@Override
			public void onSuccess(int statusCode, JSONObject response) {
				super.onSuccess(statusCode, response);
				try {
					if (response.getString("status").equals(Constant.STATUS_OK)){
						ProductVo productVo= (ProductVo) ParseJsonUtils.getInstance().getObject(response.toString(),ProductVo.class);
						System.out.println("productVo:"+JSON.toJSONString(productVo));
						Intent intent = new Intent();
						intent.setClass(ChooseNumActivity.this,PackageActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("packagesVo",packagesVo);
						bundle.putSerializable("productVo",productVo);
						intent.putExtras(bundle);
						startActivity(intent);
					}else {
						ViewUtils.showToast(ChooseNumActivity.this,response.getString("msg"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
					ViewUtils.showToast(ChooseNumActivity.this,"查询产品失败！");
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				ViewUtils.showToast(ChooseNumActivity.this,"查询产品请求发送失败！");
			}
		});
	}

	private void getFuzzyQuerySim(String simInput) {
		WSUser.simFuzzyQuery(this,simInput,LocalManager.getInstance(this).getAgentNum(),new BaseResponseHandler(
			this,false,null){
			@Override
			public void onSuccess(int statusCode, JSONObject response) {
				super.onSuccess(statusCode, response);
				try {
					if (response.getString("status").equals("000000")){
						defaultLayout.setVisibility(View.GONE);
						final SimCardNumVo simCardNumVo = (SimCardNumVo) ParseJsonUtils.getInstance().getObject(response.toString(),SimCardNumVo.class);
						if ("".equals(simCardNumVo.getData())||null==simCardNumVo.getData()){
							ViewUtils.showToast(ChooseNumActivity.this,"没有查询到匹配的sim卡号！请重新输入！");
						}else{
							simAdapter = new CommonAdapter<SimCardNumVo.DataBean>(ChooseNumActivity.this,R.layout.item_sim,simCardNumVo.getData()) {
								@Override
								protected void convert(final ViewHolder holder, SimCardNumVo.DataBean dataBean, final int position) {
									holder.setText(R.id.sim_resources,simCardNumVo.getData().get(position).getSim());
									//选中sim卡号
									holder.setOnClickListener(R.id.layout_item_sim, new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											selectedSim.setText(simCardNumVo.getData().get(position).getSim());
											currentSim=simCardNumVo.getData().get(position).getSim();
											if (currentTel!=null && currentSim!=null){
												choosePlanBtn.setDisableButtonClickable(true);
											}else{
												choosePlanBtn.setDisableButtonClickable(false);
											}
											//隐藏sim卡号搜索框，显示sim卡号选中框
											searchSimLay.setVisibility(View.GONE);
											simContainer.setVisibility(View.GONE);
											defaultLayout.setVisibility(View.GONE);
											selectedSimLay.setVisibility(View.VISIBLE);
										}
									});
								}
							};
							simContainer.setAdapter(simAdapter);
							simContainer.setVisibility(View.VISIBLE);
						}

					}else{
						ViewUtils.showToast(ChooseNumActivity.this,response.getString("msg"));
					}
				} catch (JSONException e) {
						ViewUtils.showToast(ChooseNumActivity.this,"查询SIM卡号失败！");
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
					ViewUtils.showToast(ChooseNumActivity.this,"查询SIM卡号请求发送失败！");
			}
		});
	}

	private void getFuzzyQueryNum(String shortNum, final int currentPager) {
		WSUser.numFuzzyQuery(this,currentLvl,shortNum, LocalManager.getInstance(ChooseNumActivity.this).getAgentNum(),
				currentPager,new BaseResponseHandler(ChooseNumActivity.this,false,null){
					@Override
					public void onSuccess(int statusCode, JSONObject response) {
						super.onSuccess(statusCode, response);
						try {
							if (response.getString("status").equals("000000")){
								System.out.println("请求号码：："+response);
								//搜索号码成功 sim卡搜索框隐藏 默认图片隐藏
								searchSimLay.setVisibility(View.GONE);
								defaultLayout.setVisibility(View.GONE);
								final NumListVo numVo = (NumListVo) ParseJsonUtils.getInstance().getObject(response.toString(),NumListVo.class);
								List<NumListVo.DataBean> datas = numVo.getData();
								loadSuccess(datas);

							}else{
								ViewUtils.showToast(ChooseNumActivity.this,response.getString("msg"));
							}
						} catch (JSONException e) {
							e.printStackTrace();
							ViewUtils.showToast(ChooseNumActivity.this,"查询号码失败！");
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						ViewUtils.showToast(ChooseNumActivity.this,"查询号码请求发送失败！");
					}

				});
	}

	private void loadSuccess(List<NumListVo.DataBean> datas) {
		if (currentPager==1){
			setRecyclerView();
		}
		currentPager++;
		this.mNumDatas.addAll(datas);
		mLoadMoreWrapper.notifyDataSetChanged();

	}

	private void showBottomDialog(final List<String>  numLvlLists) {
		bottomDialog = new Dialog(this, R.style.dialog);
		view = LayoutInflater.from(this).inflate(R.layout.activity_bottom_dialog, null);
		numLvlContainer = (RecyclerView) view.findViewById(R.id.recycle_num_lvl);
		numLvlContainer.setLayoutManager(new LinearLayoutManager(this));
		numLvlContainer.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL_LIST));
		mAdapter = new CommonAdapter<String>(view.getContext(),R.layout.item_num_leval,numLvlLists) {
			@Override
			protected void convert(ViewHolder holder, String s, final int position) {
				holder.setText(R.id.num_lvl_desc,s);
				holder.setOnClickListener(R.id.layout_item_num, new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						numLevel.setText(numLvlLists.get(position));
						currentLvl = levelData.get(numLvlLists.get(position));
						System.out.println("当前号码等级:"+currentLvl);
						bottomDialog.dismiss();
					}
				});
			}
		};
		numLvlContainer.setAdapter(mAdapter);
		bottomDialog.setContentView(view);
		Window dialogWindow = bottomDialog.getWindow();
		dialogWindow.setGravity(Gravity.BOTTOM);
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		dialogWindow.setAttributes(lp);
		bottomDialog.show();
	}

	private void setRecyclerView(){
		CommonAdapter<NumListVo.DataBean> numAdapter = new CommonAdapter<NumListVo.DataBean>(getApplicationContext(), R.layout.item_num, mNumDatas) {
			@Override
			protected void convert(ViewHolder holder, NumListVo.DataBean dataBean, final int position) {
				holder.setText(R.id.num_resources, mNumDatas.get(position).getTelPhone());
				holder.setText(R.id.prestore, "预存 " + Integer.parseInt(mNumDatas.get(position).getAmount())/100);
				holder.setText(R.id.min_consumption, "月低消 " + Integer.parseInt(mNumDatas.get(position).getMinRentFee())/100);

				holder.setOnClickListener(R.id.layout_item_num, new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						searchNumLay.setVisibility(View.GONE);
						searchSimLay.setVisibility(View.VISIBLE);
						selectedNum.setText(mNumDatas.get(position).getTelPhone());
						selectedAmout.setText("预存 " + Integer.parseInt(mNumDatas.get(position).getAmount())/100);
						selectedMinRent.setText("月低消 " + Integer.parseInt(mNumDatas.get(position).getMinRentFee())/100);
						currentTel = mNumDatas.get(position).getTelPhone();
						if (currentTel!=null && currentSim!=null){
							choosePlanBtn.setDisableButtonClickable(true);
						}else{
							choosePlanBtn.setDisableButtonClickable(false);
						}
						//选中后 搜索手机号码框隐藏  sim卡搜索框显示
						numContainer.setVisibility(View.GONE);
						searchNumLay.setVisibility(View.GONE);
						chooseNumLevelLay.setVisibility(View.GONE);
						selectedNumLay.setVisibility(View.VISIBLE);
						selectedNumLay.setClickable(true);
						searchSimLay.setVisibility(View.VISIBLE);
					}
				});
			}
		};

		mLoadMoreWrapper=new LoadMoreWrapper<NumListVo.DataBean>(numAdapter);
		mLoadMoreWrapper.setLoadMoreView(R.layout.load_more_layout);
		mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
			@Override
			public void onLoadMoreRequested() {
				loadMore();
			}
		});
		numContainer.setAdapter(mLoadMoreWrapper);
		numContainer.setVisibility(View.VISIBLE);

	}

	private void loadMore(){
			getFuzzyQueryNum(simInput,currentPager);
	}
}
