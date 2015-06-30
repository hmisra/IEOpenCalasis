package org.legacyprojectx.tagger;

import java.util.ArrayList;

public class DataObject {
	String fname;
	String lname;
	String mname;
	String maidenName;
	String nickname;
	String spouseName;
	String gender;
	String salutation;
	String dod;
	String dob;
	String formerCity;
	String formerState;
	String city;
	String state;
	String spouseStatus;
	String age;
	String originalObit;
	String serviceInformation;
	String educationInformation;
	String militaryInformation;
	String isLifeCompanion;
	String donationDetails;
	ArrayList<String[]> sentencesInObits=new ArrayList<String[]>();
	ArrayList<String[]> tags=new ArrayList<String[]>();
	boolean tagged=false;
	boolean dataLoaded=false;
	public void setDataLoaded(boolean f)
	{
		dataLoaded=f;
	}
	public boolean getDataLoaded()
	{
		return dataLoaded;
	}
	public ArrayList<String[]> getSentences()
	{
		return this.sentencesInObits;
	}
	public ArrayList<String[]> gettags()
	{
		return this.tags;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public String getMname() {
		return mname;
	}
	public void setMname(String mname) {
		this.mname = mname;
	}
	public String getMaidenName() {
		return maidenName;
	}
	public void setMaidenName(String maidenName) {
		this.maidenName = maidenName;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getSpouseName() {
		return spouseName;
	}
	public void setSpouseName(String spouseName) {
		this.spouseName = spouseName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getSalutation() {
		return salutation;
	}
	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}
	public String getDod() {
		return dod;
	}
	public void setDod(String dod) {
		this.dod = dod;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getFormerCity() {
		return formerCity;
	}
	public void setFormerCity(String formerCity) {
		this.formerCity = formerCity;
	}
	public String getFormerState() {
		return formerState;
	}
	public void setFormerState(String formerState) {
		this.formerState = formerState;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getSpouseStatus() {
		return spouseStatus;
	}
	public void setSpouseStatus(String spouseStatus) {
		this.spouseStatus = spouseStatus;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getOriginalObit() {
		return originalObit;
	}
	public void setOriginalObit(String originalObit) {
		this.originalObit = originalObit;
	}
	public String getServiceInformation() {
		return serviceInformation;
	}
	public void setServiceInformation(String serviceInformation) {
		this.serviceInformation = serviceInformation;
	}
	public String getEducationInformation() {
		return educationInformation;
	}
	public void setEducationInformation(String educationInformation) {
		this.educationInformation = educationInformation;
	}
	public String getMilitaryInformation() {
		return militaryInformation;
	}
	public void setMilitaryInformation(String militaryInformation) {
		this.militaryInformation = militaryInformation;
	}
	public String getIsLifeCompanion() {
		return isLifeCompanion;
	}
	public void setIsLifeCompanion(String isLifeCompanion) {
		this.isLifeCompanion = isLifeCompanion;
	}
	public String getDonationDetails() {
		return donationDetails;
	}
	public void setDonationDetails(String donationDetails) {
		this.donationDetails = donationDetails;
	}
}
