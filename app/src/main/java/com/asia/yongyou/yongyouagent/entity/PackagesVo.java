package com.asia.yongyou.yongyouagent.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ichen on 2017/10/19.
 */
public class PackagesVo implements Serializable {


	/**
	 * success : true
	 * msg : 操作成功
	 * status : 1
	 * products1 : {"productTagName":"友享产品","productType":"","products":[{"activeTimeString":"","commAdjustFee":500,"inactiveTimesString":"","isNotSubs":"","productClass":"2","productDesc":"售价5元，包含1个月来电显示,次月自动续订","productDetail":"","productId":2003200002,"productName":"来显月包","productType":"20032","subsProductId":0}]}
	 * products2 : {"productTagName":"友聊产品","productType":"","products":[{"activeTimeString":"","commAdjustFee":0,"inactiveTimesString":"","isNotSubs":"","productClass":"2","productDesc":"售价54元，包含12个月来电显示","productDetail":"","productId":2003200001,"productName":"来显年包","productType":"20032","subsProductId":0}]}
	 */

	private boolean success;
	private String msg;
	private String status;
	private Products1Bean products1;
	private Products2Bean products2;

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

	public Products1Bean getProducts1() {
		return products1;
	}

	public void setProducts1(Products1Bean products1) {
		this.products1 = products1;
	}

	public Products2Bean getProducts2() {
		return products2;
	}

	public void setProducts2(Products2Bean products2) {
		this.products2 = products2;
	}

	public static class Products1Bean implements Serializable {
		/**
		 * productTagName : 友享产品
		 * productType :
		 * products : [{"activeTimeString":"","commAdjustFee":500,"inactiveTimesString":"","isNotSubs":"","productClass":"2","productDesc":"售价5元，包含1个月来电显示,次月自动续订","productDetail":"","productId":2003200002,"productName":"来显月包","productType":"20032","subsProductId":0}]
		 */

		private String productTagName;
		private String productType;
		private List<ProductsBean> products;

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

		public static class ProductsBean implements Serializable {
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
			 * productType : 20032
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

	public static class Products2Bean implements Serializable {
		/**
		 * productTagName : 友聊产品
		 * productType :
		 * products : [{"activeTimeString":"","commAdjustFee":0,"inactiveTimesString":"","isNotSubs":"","productClass":"2","productDesc":"售价54元，包含12个月来电显示","productDetail":"","productId":2003200001,"productName":"来显年包","productType":"20032","subsProductId":0}]
		 */

		private String productTagName;
		private String productType;
		private List<ProductsBeanX> products;

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

		public List<ProductsBeanX> getProducts() {
			return products;
		}

		public void setProducts(List<ProductsBeanX> products) {
			this.products = products;
		}

		public static class ProductsBeanX implements Serializable {
			/**
			 * activeTimeString :
			 * commAdjustFee : 0
			 * inactiveTimesString :
			 * isNotSubs :
			 * productClass : 2
			 * productDesc : 售价54元，包含12个月来电显示
			 * productDetail :
			 * productId : 2003200001
			 * productName : 来显年包
			 * productType : 20032
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
}
