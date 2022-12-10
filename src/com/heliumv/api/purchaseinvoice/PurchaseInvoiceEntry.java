package com.heliumv.api.purchaseinvoice;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PurchaseInvoiceEntry {
	private Integer orderId;
	private BigDecimal price;
	private String currency;
	private PaymentEnum paymentType;
	private String description;
	private String image;
	private String imageType;
	private Long timestampMs;
	
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public PaymentEnum getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(PaymentEnum paymentType) {
		this.paymentType = paymentType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getImageType() {
		return imageType;
	}
	public void setImageType(String imageType) {
		this.imageType = imageType;
	}
	public Long getTimestampMs() {
		return timestampMs;
	}
	public void setTimestampMs(Long timestampMs) {
		this.timestampMs = timestampMs;
	}
}
