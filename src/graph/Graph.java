package graph;


import java.util.List;

public class Graph {
	
	private List<Vertex> vertices;
	
	public Graph(List<Vertex> vertices) {
		super();
		this.vertices = vertices;
	}
	
	public List<Vertex> getVertices() {
		return vertices;
	}
	
	/**
	 * Edge between two vertices returns the mass difference between two peaks.
	 * @param v Vertex v (Peak 1)
	 * @param u Vertex u (Peak 2)
	 * @return double 
	 */
	public double getEdge(Vertex v, Vertex u){
		return Math.abs(v.getMass() - u.getMass());
	}
	
	public double getIntensity(Vertex v){
		return v.getIntensity();
	}
	
	/**
	 * Check whether two vertices have a connection.
	 * @param mass The given mass of an amino acid.
	 * @param v Vertex v (Peak 1)
	 * @param u Vertex u (Peak 2)
	 * @return
	 */
	public boolean hasConnection(double mass, Vertex v, Vertex u){
		if (Math.abs(getEdge(v, u) - mass ) <= 0.5){
			return true;
		} else {
			return false;
		}
	}

	public void addVertex(Vertex v){
		vertices.add(v);
	}	
	
}
