package org.legacyprojectx.datastructures;

import java.util.ArrayList;

public class OutputClassifier {
	ArrayList<String> SVM_LABEL=new ArrayList<String>();
	ArrayList<String> SVM_PROB=new ArrayList<String>();
	ArrayList<String> GLMNET_LABEL=new ArrayList<String>();
	ArrayList<String> GLMNET_PROB=new ArrayList<String>();
	ArrayList<String> MAXENTROPY_LABEL=new ArrayList<String>();
	ArrayList<String> MAXENTROPY_PROB=new ArrayList<String>();
	ArrayList<String> SLDA_LABEL=new ArrayList<String>();
	ArrayList<String> SLDA_PROB=new ArrayList<String>();
	ArrayList<String> LOGITBOOST_LABEL=new ArrayList<String>();
	ArrayList<String> LOGITBOOST_PROB=new ArrayList<String>();
	ArrayList<String> BAGGING_LABEL=new ArrayList<String>();
	ArrayList<String> BAGGING_PROB=new ArrayList<String>();
	ArrayList<String> FORESTS_LABEL=new ArrayList<String>();
	ArrayList<String> FORESTS_PROB=new ArrayList<String>();
	ArrayList<String> NNETWORK_LABEL=new ArrayList<String>();
	ArrayList<String> NNETWORK_PROB=new ArrayList<String>();
	ArrayList<String> TREE_LABEL=new ArrayList<String>();
	ArrayList<String> TREE_PROB=new ArrayList<String>();
	ArrayList<String> sentence=new ArrayList<String>();
	
	public String getSVM_LABEL(int i) {
		return SVM_LABEL.get(i);
	}
	public void setSVM_LABEL(String sVM_LABEL) {
		SVM_LABEL.add( sVM_LABEL);
	}
	public String getSVM_PROB(int i) {
		return SVM_PROB.get(i);
	}
	public void setSVM_PROB(String sVM_PROB) {
		SVM_PROB.add(sVM_PROB);
	}
	public String getGLMNET_LABEL(int i) {
		return GLMNET_LABEL.get(i);
	}
	public void setGLMNET_LABEL(String gLMNET_LABEL) {
		GLMNET_LABEL.add(gLMNET_LABEL);
	}
	public String getGLMNET_PROB(int i) {
		return GLMNET_PROB.get(i);
	}
	public void setGLMNET_PROB(String gLMNET_PROB) {
		GLMNET_PROB.add(gLMNET_PROB);
	}
	public String getMAXENTROPY_LABEL(int i) {
		return MAXENTROPY_LABEL.get(i);
	}
	public void setMAXENTROPY_LABEL(String mAXENTROPY_LABEL) {
		MAXENTROPY_LABEL.add(mAXENTROPY_LABEL);
	}
	public String getMAXENTROPY_PROB(int i) {
		return MAXENTROPY_PROB.get(i);
	}
	public void setMAXENTROPY_PROB(String mAXENTROPY_PROB) {
		MAXENTROPY_PROB.add(mAXENTROPY_PROB);
	}
	public String getSLDA_LABEL(int i) {
		return SLDA_LABEL.get(i);
	}
	public void setSLDA_LABEL(String sLDA_LABEL) {
		SLDA_LABEL.add( sLDA_LABEL);
	}
	public String getSLDA_PROB(int i) {
		return SLDA_PROB.get(i);
	}
	public void setSLDA_PROB(String sLDA_PROB) {
		SLDA_PROB.add(sLDA_PROB);
	}
	public String getLOGITBOOST_LABEL(int i) {
		return LOGITBOOST_LABEL.get(i);
	}
	public void setLOGITBOOST_LABEL(String lOGITBOOST_LABEL) {
		LOGITBOOST_LABEL.add(lOGITBOOST_LABEL);
	}
	public String getLOGITBOOST_PROB(int i) {
		return LOGITBOOST_PROB.get(i);
	}
	public void setLOGITBOOST_PROB(String lOGITBOOST_PROB) {
		LOGITBOOST_PROB.add(lOGITBOOST_PROB);
	}
	public String getBAGGING_LABEL(int i) {
		return BAGGING_LABEL.get(i);
	}
	public void setBAGGING_LABEL(String bAGGING_LABEL) {
		BAGGING_LABEL.add(bAGGING_LABEL);
	}
	public String getBAGGING_PROB(int i) {
		return BAGGING_PROB.get(i);
	}
	public void setBAGGING_PROB(String bAGGING_PROB) {
		BAGGING_PROB.add(bAGGING_PROB);
	}
	public String getFORESTS_LABEL(int i) {
		return FORESTS_LABEL.get(i);
	}
	public void setFORESTS_LABEL(String fORESTS_LABEL) {
		FORESTS_LABEL.add(fORESTS_LABEL);
	}
	public String getFORESTS_PROB(int i) {
		return FORESTS_PROB.get(i);
	}
	public void setFORESTS_PROB(String fORESTS_PROB) {
		FORESTS_PROB.add(fORESTS_PROB);
	}
	public String getNNETWORK_LABEL(int i) {
		return NNETWORK_LABEL.get(i);
	}
	public void setNNETWORK_LABEL(String nNETWORK_LABEL) {
		NNETWORK_LABEL.add(nNETWORK_LABEL);
	}
	public String getNNETWORK_PROB(int i) {
		return NNETWORK_PROB.get(i);
	}
	public void setNNETWORK_PROB(String nNETWORK_PROB) {
		NNETWORK_PROB.add(nNETWORK_PROB);
	}
	public String getTREE_LABEL(int i) {
		return TREE_LABEL.get(i);
	}
	public void setTREE_LABEL(String tREE_LABEL) {
		TREE_LABEL.add(tREE_LABEL);
	}
	public String getTREE_PROB(int i) {
		return TREE_PROB.get(i);
	}
	public void setTREE_PROB(String tREE_PROB) {
		TREE_PROB.add( tREE_PROB);
	}
	public String getSentence(int i) {
		return sentence.get(i);
	}
	public void setSentence(String sentence) {
		this.sentence.add( sentence);
	}
	public ArrayList<String> getSentences()
	{
		return sentence;
	}
	
	public void getLabels()
	
	{
		
	}
	

}
