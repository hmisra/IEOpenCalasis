package org.legacyprojectx.datastructures;

public class StringSimilarityHeapEntity  {
	String sentence;
	Double score;
	int sentIndex;
	
	
	public StringSimilarityHeapEntity(String sentence, Double score, int sentIndex)
	{
		this.sentence=sentence;
		this.score=score;
		this.sentIndex=sentIndex;
	}
	public String getSentence() {
		return sentence;
	}
	public void setSentence(String sentence) {
		this.sentence = sentence;
	}
	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}
	public int getSentIndex() {
		return sentIndex;
	}
	public void setSentIndex(int sentIndex) {
		this.sentIndex = sentIndex;
	}
	

	
}
