package com.flickr4java.flickr.places;

public class ShapeData {

	private String created;
	
	private double alpha;
	
	private int countPoints;
	
	private int countEdges;
	
	private boolean hasDonuthole;
	
	private boolean isDonutHole;
	
	private String polyline;
	
	private String shapefile;

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public double getAlpha() {
		return alpha;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	public int getCountPoints() {
		return countPoints;
	}

	public void setCountPoints(int countPoints) {
		this.countPoints = countPoints;
	}

	public int getCountEdges() {
		return countEdges;
	}

	public void setCountEdges(int countEdges) {
		this.countEdges = countEdges;
	}

	public boolean isHasDonuthole() {
		return hasDonuthole;
	}

	public void setHasDonuthole(boolean hasDonuthole) {
		this.hasDonuthole = hasDonuthole;
	}

	public boolean isDonutHole() {
		return isDonutHole;
	}

	public void setIsDonutHole(boolean isDonutHole) {
		this.isDonutHole = isDonutHole;
	}

	public String getPolyline() {
		return polyline;
	}

	public void setPolyline(String polyline) {
		this.polyline = polyline;
	}

	public String getShapefile() {
		return shapefile;
	}

	public void setShapefile(String shapefile) {
		this.shapefile = shapefile;
	}
	
}
