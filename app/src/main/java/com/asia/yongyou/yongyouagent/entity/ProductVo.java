package com.asia.yongyou.yongyouagent.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ichen on 2017/10/19.
 */
public class ProductVo implements Serializable {


	/**
	 * success : true
	 * msg : 操作成功
	 * status : 1
	 * addedProducts : [{"productTagName":"流量加油包","productType":"10002","products":[{"activeTimeString":"","commAdjustFee":0,"inactiveTimesString":"","isNotSubs":"","productClass":"2","productDesc":"套餐包含100M国内流量，资源永久不清零","productDetail":"","productId":1000200002,"productName":"流量加油包","productType":"10002","subsProductId":0}]},{"productTagName":"来电显示","productType":"20032","products":[{"activeTimeString":"","commAdjustFee":500,"inactiveTimesString":"","isNotSubs":"","productClass":"2","productDesc":"售价5元，包含1个月来电显示,次月自动续订","productDetail":"","productId":2003200002,"productName":"来显月包","productType":"20032","subsProductId":0},{"activeTimeString":"","commAdjustFee":0,"inactiveTimesString":"","isNotSubs":"","productClass":"2","productDesc":"售价54元，包含12个月来电显示","productDetail":"","productId":2003200001,"productName":"来显年包","productType":"20032","subsProductId":0}]}]
	 */

	private boolean success;
	private String msg;
	private String status;
	private List<AddedProductsBean> addedProducts;
	private int allSelectedCounts;

	public int getAllSelectedCounts() {
		return allSelectedCounts;
	}

	public void setAllSelectedCounts(int allSelectedCounts) {
		this.allSelectedCounts = allSelectedCounts;
	}

	public List<Integer> getSelectedProductList() {
		return selectedProductList;
	}

	public void setSelectedProductList(List<Integer> selectedProductList) {
		this.selectedProductList = selectedProductList;
	}

	private List<Integer> selectedProductList;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<AddedProductsBean> getAddedProducts() {
		return addedProducts;
	}

	public void setAddedProducts(List<AddedProductsBean> addedProducts) {
		this.addedProducts = addedProducts;
	}

	public static class AddedProductsBean implements Serializable {
		/**
		 * productTagName : 流量加油包
		 * productType : 10002
		 * products : [{"activeTimeString":"","commAdjustFee":0,"inactiveTimesString":"","isNotSubs":"","productClass":"2","productDesc":"套餐包含100M国内流量，资源永久不清零","productDetail":"","productId":1000200002,"productName":"流量加油包","productType":"10002","subsProductId":0}]
		 */

		private String productTagName;
		private String productType;
		private List<ProductsBean> products;
		private List<Integer> selectedCounts;

		public int getSelectAmout() {
			return selectAmout;
		}

		public void setSelectAmout(int selectAmout) {
			this.selectAmout = selectAmout;
		}

		private int selectAmout;

		public List<Integer> getSelectedCounts() {
			return selectedCounts;
		}

		public void setSelectedCounts(List<Integer> selectedCounts) {
			this.selectedCounts = selectedCounts;
		}

		public String getProductTagName() {
			return productTagName;
		}

		public void setProductTagName(String productTagName) {
			this.productTagName = productTagName;
		}

		public String getProductType() {
			return productType;
		}

		public void setProductType(String productType) {
			this.productType = productType;
		}

		public List<ProductsBean> getProducts() {
			return products;
		}

		public void setProducts(List<ProductsBean> products) {
			this.products = products;
		}

		public static class ProductsBean implements Serializable{
			/**
			 * activeTimeString :
			 * commAdjustFee : 0
			 * inactiveTimesString :
			 * isNotSubs :
			 * productClass : 2
			 * productDesc : 套餐包含100M国内流量，资源永久不清零
			 * productDetail :
			 * productId : 1000200002
			 * productName : 流量加油包
			 * productType : 10002
			 * subsProductId : 0
			 */

			private String activeTimeString;
			private int commAdjustFee;
			private String inactiveTimesString;
			private String isNotSubs;
			private String productClass;
			private String productDesc;
			private String productDetail;
			private int productId;
			private String productName;
			private String productType;
			private int subsProductId;
			private boolean isSelected;


			public boolean isSelected() {
				return isSelected;
			}

			public void setSelected(boolean selected) {
				isSelected = selected;
			}
			//			protected ProductsBean(Parcel in) {
//				activeTimeString = in.readString();
//				commAdjustFee = in.readInt();
//				inactiveTimesString = in.readString();
//				isNotSubs = in.readString();
//				productClass = in.readString();
//				productDesc = in.readString();
//				productDetail = in.readString();
//				productId = in.readInt();
//				productName = in.readString();
//				productType = in.readString();
//				subsProductId = in.readInt();
//			}

//			public static final Creator<ProductsBean> CREATOR = new Creator<ProductsBean>() {
//				@Override
//				public ProductsBean createFromParcel(Parcel in) {
//					return new ProductsBean(in);
//				}
//
//				@Override
//				public ProductsBean[] newArray(int size) {
//					return new ProductsBean[size];
//				}
//			};
//
//			public String getActiveTimeString() {
//				return activeTimeString;
//			}
//
//			public void setActiveTimeString(String activeTimeString) {
//				this.activeTimeString = activeTimeString;
//			}

			public int getCommAdjustFee() {
				return commAdjustFee;
			}

			public void setCommAdjustFee(int commAdjustFee) {
				this.commAdjustFee = commAdjustFee;
			}

			public String getInactiveTimesString() {
				return inactiveTimesString;
			}

			public void setInactiveTimesString(String inactiveTimesString) {
				this.inactiveTimesString = inactiveTimesString;
			}

			public String getIsNotSubs() {
				return isNotSubs;
			}

			public void setIsNotSubs(String isNotSubs) {
				this.isNotSubs = isNotSubs;
			}

			public String getProductClass() {
				return productClass;
			}

			public void setProductClass(String productClass) {
				this.productClass = productClass;
			}

			public String getProductDesc() {
				return productDesc;
			}

			public void setProductDesc(String productDesc) {
				this.productDesc = productDesc;
			}

			public String getProductDetail() {
				return productDetail;
			}

			public void setProductDetail(String productDetail) {
				this.productDetail = productDetail;
			}

			public int getProductId() {
				return productId;
			}

			public void setProductId(int productId) {
				this.productId = productId;
			}

			public String getProductName() {
				return productName;
			}

			public void setProductName(String productName) {
				this.productName = productName;
			}

			public String getProductType() {
				return productType;
			}

			public void setProductType(String productType) {
				this.productType = productType;
			}

			public int getSubsProductId() {
				return subsProductId;
			}

			public void setSubsProductId(int subsProductId) {
				this.subsProductId = subsProductId;
			}

//			@Override
//			public int describeContents() {
//				return 0;
//			}

//			@Override
//			public void writeToParcel(Parcel dest, int flags) {
//				dest.writeString(activeTimeString);
//				dest.writeInt(commAdjustFee);
//				dest.writeString(inactiveTimesString);
//				dest.writeString(isNotSubs);
//				dest.writeString(productClass);
//				dest.writeString(productDesc);
//				dest.writeString(productDetail);
//				dest.writeInt(productId);
//				dest.writeString(productName);
//				dest.writeString(productType);
//				dest.writeInt(subsProductId);
//			}
		}
	}
}
