package beans;

public class PojoV {
	double velocity;
	double timeDiff;
	
	public PojoV(double velocity, double timeDiff) {
		super();
		this.velocity = velocity;
		this.timeDiff = timeDiff;
	}

	public double getVelocity() {
		return velocity;
	}

	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

	public double getTimeDiff() {
		return timeDiff;
	}

	public void setTimeDiff(double timeDiff) {
		this.timeDiff = timeDiff;
	}

	
}
