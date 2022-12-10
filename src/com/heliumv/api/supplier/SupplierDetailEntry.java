package com.heliumv.api.supplier;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;
import com.heliumv.api.customer.ContactEntryList;
import com.heliumv.api.customer.IPartnerEntry;

@XmlRootElement
public class SupplierDetailEntry extends BaseEntryId implements IPartnerEntry {
	private String name1 ;
	private String name2 ;
	private String name3 ;
	private String sign ;
	private String country ;
	private String zipcode ;
	private String phone ;
	private String city ;
	private String addressType ;
	private String countryName ;
	private String street ;
	private String titlePrefix ;
	private String titlePostfix ;
	private String uid ;
	private String eori ;
	private String remark ;
	private String email ;
	private String website ;
	private String fax ;
	private String formattedCity ;
	private String formattedSalutation ;
	private Integer partnerId;
	private ContactEntryList contactEntries;

	public SupplierDetailEntry() {
	}

	public String getName1() {
		return name1;
	}

	public void setName1(String name1) {
		this.name1 = name1;
	}

	public String getName2() {
		return name2;
	}

	public void setName2(String name2) {
		this.name2 = name2;
	}

	public String getName3() {
		return name3;
	}

	public void setName3(String name3) {
		this.name3 = name3;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getCountryCode() {
		return country;
	}

	public void setCountryCode(String country) {
		this.country = country;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getTitlePrefix() {
		return titlePrefix;
	}

	public void setTitlePrefix(String titlePrefix) {
		this.titlePrefix = titlePrefix;
	}

	public String getTitlePostfix() {
		return titlePostfix;
	}

	public void setTitlePostfix(String titlePostfix) {
		this.titlePostfix = titlePostfix;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getEori() {
		return eori;
	}

	public void setEori(String eori) {
		this.eori = eori;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getFormattedCity() {
		return formattedCity;
	}

	public void setFormattedCity(String formattedCity) {
		this.formattedCity = formattedCity;
	}

	public String getFormattedSalutation() {
		return formattedSalutation;
	}

	public void setFormattedSalutation(String formattedSalutation) {
		this.formattedSalutation = formattedSalutation;
	}

	public Integer getPartnerId() {
		return partnerId;
	}
	
	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}
	
	public ContactEntryList getContactEntries() {
		return contactEntries;
	}
	
	public void setContactEntries(ContactEntryList contactEntries) {
		this.contactEntries = contactEntries;
	}
}
