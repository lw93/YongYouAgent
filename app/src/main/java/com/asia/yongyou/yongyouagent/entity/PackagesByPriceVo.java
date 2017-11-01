package com.asia.yongyou.yongyouagent.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ichen on 2017/10/25.
 */
public class PackagesByPriceVo implements Serializable {


	/**
	 * success : true
	 * msg : 操作成功
	 * status : 1
	 * products : [{"activeTimeString":"","commAdjustFee":500,"inactiveTimesString":"","isNotSubs":"","productClass":"2","productDesc":"售价5元，包含1个月来电显示,次月自动续订","productDetail":"","productId":2003200002,"productName":"来显月包","productType":"10023","subsProductId":0}]
	 */

	private boolean success;
	private String msg;
	private String status;
	private List<ProductsBean> products;

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

	public List<ProductsBean> getProducts() {
		return products;
	}

	public void setProducts(List<ProductsBean> products) {
		this.products = products;
	}

	public static class ProductsBean {
		/**
		 * activeTimeString :
		 * commAdjustFee : 500
		 * inactiveTimesString :
		 * isNotSubs :
		 * productClass : 2
		 * productDesc : 售价5元，包含1个月来电显示,次月自动续订
		 * productDetail :
		 * productId : 2003200002
		 * productName : 来显月包
		 * productType : 10023
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

		public String getActiveTimeString() {
			return activeTimeString;
		}

		public void setActiveTimeString(String activeTimeString) {
			this.activeTimeString = activeTimeString;
		}

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
	}
}
