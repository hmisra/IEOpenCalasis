package org.legacyprojectx.datastructures;

public class DocumentClassifierTrainingFormat {
String [] positiveExamples;
String [] negativeExamples;
String positiveClass;
String negativeClass;
public String[] getPositiveExamples() {
	return positiveExamples;
}
public void setPositiveExamples(String[] positiveExamples) {
	this.positiveExamples = positiveExamples;
}
public String[] getNegativeExamples() {
	return negativeExamples;
}
public void setNegativeExamples(String[] negativeExamples) {
	this.negativeExamples = negativeExamples;
}
public String getPositiveClass() {
	return positiveClass;
}
public void setPositiveClass(String positiveClass) {
	this.positiveClass = positiveClass;
}
public String getNegativeClass() {
	return negativeClass;
}
public void setNegativeClass(String negativeClass) {
	this.negativeClass = negativeClass;
}

public DocumentClassifierTrainingFormat(String[] positiveExamples, String[] negativeExamples, String positiveClass, String negativeClass)
{
	this.positiveExamples=positiveExamples;
	this.negativeExamples=negativeExamples;
	this.positiveClass=positiveClass;
	this.negativeClass=negativeClass;
}
}
