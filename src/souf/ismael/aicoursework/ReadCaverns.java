package souf.ismael.aicoursework;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.*;

public class ReadCaverns
{

    public static void main(String[] args)
    {
        List<String> file;
        Path p;
        String data;
        Cave[] caves;
        ArrayList<Cave> toNavigate = new ArrayList<>();
        ArrayList<Cave> Navigated = new ArrayList<>();
        String filename = args[0];

        p = FileSystems.getDefault().getPath("./", filename + ".cav");

        try
        {
            file = Files.readAllLines(p);
            data = file.get(0);
        } catch(IOException e)
        {
            System.err.println(".cav file not found");
            return;
        }
        if(args.length != 1)
        {
            System.err.println(".cav file not found");
            return;
        }

        //Cavern maps contains a series of integers separated by commas.
        String[] values = data.split(",");
        int numberOfCaves = Integer.parseInt(values[0]);

        caves = new Cave[numberOfCaves];

        //The first integer gives the number of caverns -N.
        int balance = 1;

        for(int n = 0; n < numberOfCaves; n++)
        {
            //The next N*2 integers give the coordinates of each of the caverns - each value is non-negative
            caves[n] = new Cave(Double.parseDouble(values[n*2+balance]), Double.parseDouble(values[n*2+balance+1]), ""+(n+1));
        }
        balance = 1+2*numberOfCaves;

        //Connectivity between caves using matrix
        for(int n = balance; n < values.length; n++)
        {
            int firstCave = (n - 1)%numberOfCaves;
            int lastCave = (n - balance) / numberOfCaves;

            if(values[n].equals("1"))
            {
                //Connectivity of caverns
                caves[firstCave].addEdges(caves[lastCave]);
            }
        }

        Cave firstCave = caves[0];
        Cave lastCave = caves[numberOfCaves - 1];

        toNavigate.add(firstCave);
        firstCave.setCostFromStart(0);
        Cave currentCave;

        do{
            currentCave = toNavigate.get(0);
            for(Cave path : currentCave.getPath())
            {
                if(!Navigated.contains(path) && path != currentCave)
                {
                    if(!toNavigate.contains(path))
                    {
                        toNavigate.add(path);
                    }

                    double finalCost = currentCave.getCostFromStart() +  currentCave.getEuclideanDistance(path);

                    if(finalCost < path.getCostFromStart())
                    {
                        path.setCostFromStart(finalCost);
                        path.setNeighbourCave(currentCave);
                    }
                }
            }
            toNavigate.remove(currentCave);
            Navigated.add(currentCave);

            //In order to find the best route sort the caves routes in ascending order using ASTAR
            sort(toNavigate, (cav1,cav2) -> (int) ((cav1.getCostFromStart() - cav2.getCostFromStart()) + (cav2.getEuclideanDistance(lastCave) - cav1.getEuclideanDistance(lastCave))));

        }while(currentCave != lastCave && toNavigate.size() > 0);

        String output = "";
        double eDistance = 0.0;

        if(Navigated.contains(lastCave))
        {
            do{
                output = currentCave.getPos() + " " + output;
                if(currentCave.getNeighbourCave() != null)
                {
                    eDistance += currentCave.getEuclideanDistance(currentCave.getNeighbourCave());
                }
            }while ((currentCave = currentCave.getNeighbourCave()) != null);
        }else
        {
            output = "0";
        }
        OutputFile(output.trim(), filename + ".csn");
    }

    private static void OutputFile(String file, String filename) {
        OutputStream outputFile = null;
        try
        {
            outputFile = new FileOutputStream(new File(filename));
            outputFile.write(file.getBytes(), 0, file.length());
        } catch (IOException e)
        {
            e.printStackTrace();
        }finally
        {
            try
            {
                outputFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
