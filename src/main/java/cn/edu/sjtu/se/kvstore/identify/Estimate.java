package cn.edu.sjtu.se.kvstore.identify;

public class Estimate {

	private long preVisitTime;
	private double frequence;

	public long getPreVisitTime() {
		return preVisitTime;
	}

	public void setPreVisitTime(long preVisitTime) {
		this.preVisitTime = preVisitTime;
	}

	public double getFrequence() {
		return frequence;
	}

	public void setFrequence(double frequent) {
		this.frequence = frequent;
	}

	public Estimate(long preVisitTime, double frequence) {
		super();
		this.preVisitTime = preVisitTime;
		this.frequence = frequence;
	}

}
