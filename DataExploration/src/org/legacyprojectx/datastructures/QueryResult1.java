package org.legacyprojectx.datastructures;
import java.util.*;
public class QueryResult1 {
String name;
String uri;
int instanceCount;
ArrayList<String> instances=new ArrayList<String>();
int age;
String gender;
String dateOfBirth;
String placeOfBirth;

String serviceInformation;
String datesOfServiceInformation="";
String timeOfServiceInformation="";
String locationOfServiceInformation="";

String educationInformation;
String militaryInformation;
String dobInformation;
String dateinDOB="";

String ageInformation;
String dateInAgeInformation="";


public String getDatesOfServiceInformation() {
	return datesOfServiceInformation;
}
public void setDatesOfServiceInformation(String datesOfServiceInformation) {
	this.datesOfServiceInformation = datesOfServiceInformation;
}
public String getTimeOfServiceInformation() {
	return timeOfServiceInformation;
}
public void setTimeOfServiceInformation(String timeOfServiceInformation) {
	this.timeOfServiceInformation = timeOfServiceInformation;
}
public String getLocationOfServiceInformation() {
	return locationOfServiceInformation;
}
public void setLocationOfServiceInformation(String locationOfServiceInformation) {
	this.locationOfServiceInformation = locationOfServiceInformation;
}
public String getDateinDOB() {
	return dateinDOB;
}
public void setDateinDOB(String dateinDOB) {
	this.dateinDOB = dateinDOB;
}
public String getDateInAgeInformation() {
	return dateInAgeInformation;
}
public void setDateInAgeInformation(String dateInAgeInformation) {
	this.dateInAgeInformation = dateInAgeInformation;
}
public boolean isDateOfService() {
	return dateOfService;
}
public void setDateOfService(boolean dateOfService) {
	this.dateOfService = dateOfService;
}
public boolean isLocationOfService() {
	return locationOfService;
}
public void setLocationOfService(boolean locationOfService) {
	this.locationOfService = locationOfService;
}
public boolean isTimeOfService() {
	return timeOfService;
}
public void setTimeOfService(boolean timeOfService) {
	this.timeOfService = timeOfService;
}
public boolean isDateOfDob() {
	return dateOfDob;
}
public void setDateOfDob(boolean dateOfDob) {
	this.dateOfDob = dateOfDob;
}
public boolean isDateInAge() {
	return dateInAge;
}
public void setDateInAge(boolean dateInAge) {
	this.dateInAge = dateInAge;
}
boolean dateOfService;
boolean locationOfService;
boolean timeOfService;
boolean dateOfDob;
boolean dateInAge;


boolean isName=false;
boolean isUri=false;
boolean isInstanceCount=false;
boolean isInstances=false;
boolean isAge=false;
boolean isServiceInformation=false;
boolean isEducationInformation=false;
boolean isMilitaryInformation=false;
boolean isAgeInformation=false;
boolean isDobInformation=false;

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
public String getDobInformation() {
	return dobInformation;
}
public void setDobInformation(String dobInformation) {
	this.dobInformation = dobInformation;
}
public String getAgeInformation() {
	return ageInformation;
}
public void setAgeInformation(String ageInformation) {
	this.ageInformation = ageInformation;
}
public boolean isName() {
	return isName;
}
public void setName(boolean isName) {
	this.isName = isName;
}
public boolean isUri() {
	return isUri;
}
public void setUri(boolean isUri) {
	this.isUri = isUri;
}
public boolean isInstanceCount() {
	return isInstanceCount;
}
public void setInstanceCount(boolean isInstanceCount) {
	this.isInstanceCount = isInstanceCount;
}
public boolean isInstances() {
	return isInstances;
}
public void setInstances(boolean isInstances) {
	this.isInstances = isInstances;
}
public boolean isAge() {
	return isAge;
}
public void setAge(boolean isAge) {
	this.isAge = isAge;
}
public boolean isServiceInformation() {
	return isServiceInformation;
}
public void setServiceInformation(boolean isServiceInformation) {
	this.isServiceInformation = isServiceInformation;
}
public boolean isEducationInformation() {
	return isEducationInformation;
}
public void setEducationInformation(boolean isEducationInformation) {
	this.isEducationInformation = isEducationInformation;
}
public boolean isMilitaryInformation() {
	return isMilitaryInformation;
}
public void setMilitaryInformation(boolean isMilitaryInformation) {
	this.isMilitaryInformation = isMilitaryInformation;
}
public boolean isAgeInformation() {
	return isAgeInformation;
}
public void setAgeInformation(boolean isAgeInformation) {
	this.isAgeInformation = isAgeInformation;
}
public boolean isDobInformation() {
	return isDobInformation;
}
public void setDobInformation(boolean isDobInformation) {
	this.isDobInformation = isDobInformation;
}
public int getAge() {
	return age;
}
public void setAge(int age) {
	this.age = age;
}
public String getGender() {
	return gender;
}
public void setGender(String gender) {
	this.gender = gender;
}
public String getDateOfBirth() {
	return dateOfBirth;
}
public void setDateOfBirth(String dateOfBirth) {
	this.dateOfBirth = dateOfBirth;
}
public String getPlaceOfBirth() {
	return placeOfBirth;
}
public void setPlaceOfBirth(String placeOfBirth) {
	this.placeOfBirth = placeOfBirth;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getUri() {
	return uri;
}
public void setUri(String uri) {
	this.uri = uri;
}
public int getInstanceCount() {
	return instanceCount;
}
public void setInstanceCount(int instanceCount) {
	this.instanceCount = instanceCount;
}
public ArrayList<String> getInstances() {
	return instances;
}
public void setInstances(ArrayList<String> instances) {
	this.instances = instances;
}
public void setInstances(String instance)
{
	this.instances.add(instance);
}



}
