package graph;

public class Vertex {
	
	private double mass;
	private double intensity;
	
	public Vertex(double mass, double intensity) {
		super();
		this.mass = mass;
		this.intensity = intensity;
	}
	
	public boolean equals(Vertex v) {
		if(this.mass == v.mass && this.intensity == v.intensity) return true;
		else return false;
	}
	
	public double getMass() {
		return mass;
	}
	
	public double getIntensity() {
		return intensity;
	}
	
	
	
	

}
