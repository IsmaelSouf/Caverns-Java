package souf.ismael.aicoursework;

import java.util.ArrayList;

class Cave implements Comparable<Cave>
{
    //Coordinates
    private double x;
    private double y;
    private ArrayList<Cave> path;
    private double CostFromStart;
    private Cave neighbourCave;
    private String pos;

    public Cave(double x, double y, String pos)
    {
        this.x = x;
        this.y = y;
        this.pos = pos;
        this.path = new ArrayList<>();
        CostFromStart = Double.MAX_VALUE;
        neighbourCave = null;
    }

    public String getPos()
    {
        return pos;
    }
    public double getCostFromStart()
    {
        return CostFromStart;
    }
    public void setCostFromStart(double costFromStart)
    {
        this.CostFromStart = costFromStart;
    }
    public Cave getNeighbourCave()
    {
        return neighbourCave;
    }
    public void setNeighbourCave(Cave neighbourCave)
    {
        this.neighbourCave = neighbourCave;
    }
    public ArrayList<Cave> getPath()
    {
        return path;
    }
    public void addEdges(Cave N)
    {
        this.path.add(N);
    }

    //Compare the two distance between the start and an alternative cave
    public int compareTo(Cave N)
    {
        return Double.compare(this.CostFromStart, N.CostFromStart);
    }
    //The distance between any two caverns is the Euclidean distance between the two coordinates:
    public double getEuclideanDistance(Cave N)
    {
        return Math.sqrt((Math.pow(N.x-x,2)) + Math.pow(N.y-y,2));
    }
}
