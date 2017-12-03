package com.five_o_one.ap1d;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class ItinenaryActivity extends AppCompatActivity {
    Set<Integer> Selected  = new HashSet<>();
    //Path [][] adjmat = new Path[10][10];
    Map<Integer,String> locationNames = new HashMap<>();
    Map<Integer,String> transportNames = new HashMap<>();
    TextView estimate_print;
    EditText budgetfill;
    DatabaseHelper databaseHelper;
    List<LocationData> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinenary);

        estimate_print = (TextView) findViewById(R.id.estimateprint);
        budgetfill = (EditText) findViewById(R.id.budget);

        databaseHelper=DatabaseHelper.getInstance(this);
        List<LocationData> testList=databaseHelper.getDataList();

        data=databaseHelper.getSelectedDataList();
        Log.v("selected length: ",Integer.toString(data.size()));

        Button bruteforcebutton = (Button) findViewById(R.id.bruteforce);
        Button fastestimatebutton = (Button) findViewById(R.id.fastestimate);

        transportNames.put(0,"Walk to:");
        transportNames.put(1,"Bus/Train to:");
        transportNames.put(2,"Taxi to:");

        bruteforcebutton.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ProgressDialog pd;

                new AsyncTask<String, Integer, Exception>() {
                    @Override
                    protected void onPreExecute(){

                    }
                    @Override
                    protected Exception doInBackground(String... strings) {
                        try {
                            bruteforce();
                        } catch (Exception e) {
                            e.printStackTrace();
                            return e;
                        }
                        return null;
                    }
                    @Override
                    protected void onPostExecute(Exception e){
                        if(e==null) Toast.makeText(ItinenaryActivity.this,"Brute force sucess",Toast.LENGTH_SHORT);
                        else Toast.makeText(ItinenaryActivity.this,"Brute force failed",Toast.LENGTH_SHORT);
                    }
                }.execute();
            }
        });

//        fastestimatebutton.setOnClickListener( new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                kruska();
//            }
//        });
    }

    void bruteforce(){
//        Selected.add(0);
//        Map<Integer,Integer> convert = new HashMap<Integer, Integer>();
//        Path[][] SelectedMatrix = new Path[Selected.size()][Selected.size()];
//        int icount = 0;
//        for (Integer i : Selected){
//            int jcount = 0;
//            convert.put(icount,i);
//            for (Integer j : Selected){
//                SelectedMatrix[icount][jcount] = adjmat[i][j];
//                SelectedMatrix[icount][jcount].adddirection(i,j);
//                jcount ++;
//            }
//            icount++;
//        }

        Log.v(Integer.toString(data.size()),Integer.toString(data.get(0).getPaths().length));
        Path[][] selectedMatrix = new Path[data.size()][data.get(0).getPaths().length];

        for (int i=0; i<data.size();i++) {
            Path[] paths=data.get(i).getPaths();
            System.arraycopy(paths, 0, selectedMatrix[i], 0, paths.length);
        }

        BruteForce x = new BruteForce();
        x.CostSet = Double.parseDouble(budgetfill.getText().toString());
        int[] Pathcombination = new int[selectedMatrix.length + 1];
        Pathcombination[0] = 0;
        Pathcombination[selectedMatrix.length] = 0;

        x.recursivefor(selectedMatrix.length,selectedMatrix.length-1, Pathcombination, selectedMatrix);
        String text = "Start from: \t\t\t";
        for (int i = 0; i < x.BestPath.length; i++){
            if (i > 0) {
                text = text + transportNames.get(x.TransportCombination[i - 1]) + "\t\t\t";
            }
            text = text + data.get(0).getName() + "\n" ;
        }
        String totalcost = "$" + x.BestPathCost;
        String timetaken = x.BestPathTiming + "min";
        estimate_print.setText(text + "\nTotal Time: " + timetaken +"\nCost: " + totalcost);
    }

//    void kruska(){
//        Kruskal x = new Kruskal();
//        Selected.add(0);
//        Map<Integer,Integer> convert = new HashMap<Integer, Integer>();
//        Path[][] SelectedMatrix = new Path[Selected.size()][Selected.size()];
//        int icount = 0;
//        for (Integer i : Selected){
//            int jcount = 0;
//            convert.put(icount,i);
//            for (Integer j : Selected){
//                SelectedMatrix[icount][jcount] = adjmat[i][j];
//                SelectedMatrix[icount][jcount].adddirection(icount,jcount);
//                jcount ++;
//            }
//            icount++;
//        }
//        for (Path[] i : SelectedMatrix) {
//            for (Path j : i) {
//                x.addEdge(j);
//            }
//        }
//        List<Path> publictransporttime = x.kruskal(new TimeComparator());
//        Collections.sort(publictransporttime,new Sort());
//        double timing = 0;
//        double cost = 0;
//        for (Path i : publictransporttime){
//            timing += i.timetaken[i.transportmethod];
//            cost += i.costs[i.transportmethod];
//        }
//        double Budget = Double.parseDouble(budgetfill.getText().toString());
//        if (Budget > cost) {
//            double[] timereduction = new double[publictransporttime.size()];
//            double[] increasecost = new double[publictransporttime.size()];
//            for (int i = 0; i < publictransporttime.size(); i++) {
//                timereduction[i] = publictransporttime.get(i).timetaken[publictransporttime.get(i).transportmethod] - publictransporttime.get(i).timetaken[2];
//                increasecost[i] = publictransporttime.get(i).costs[2] - publictransporttime.get(i).costs[publictransporttime.get(i).transportmethod];
//            }
//            double  W = Budget - cost;
//            int n = timereduction.length;
//            double []  changedweights  = knapSack(W, increasecost, timereduction, n)[1];
//            for (int i = 0; i < changedweights.length; i++ ){
//                if (changedweights[i] != 0) {
//                    publictransporttime.get(i).setTransportmethod(2);
//                    timing = timing - timereduction[i];
//                    cost = cost + increasecost[i];
//                }
//            }
//        } else {
//            double[] timeincrease = new double[publictransporttime.size()];
//            double[] stackingcost = new double[publictransporttime.size()];
//            for (int i = 0; i < publictransporttime.size(); i++) {
//                timeincrease[i] = publictransporttime.get(i).timetaken[0] - publictransporttime.get(i).timetaken[publictransporttime.get(i).transportmethod];
//                stackingcost[i] = publictransporttime.get(i).costs[publictransporttime.get(i).transportmethod] - publictransporttime.get(i).costs[0];
//            }
//            double  W = Budget;
//            int n = timeincrease.length;
//            double []  changedweights  = knapSack(W, stackingcost, timeincrease, n)[1];
//            for (int i = 0; i < changedweights.length; i++ ){
//                if (changedweights[i]==0) {
//                    publictransporttime.get(i).setTransportmethod(0);
//                    timing = timing + timeincrease[i];
//                    cost = cost - stackingcost[i];
//                }
//            }
//        }
//        int curr = 0;
//        String string = "Start from:\t\t\t" +locationNames.get(convert.get(publictransporttime.get(curr).from)) + "\n";
//        for (int i = 0 ; i < publictransporttime.size(); i++){
//            string = string + transportNames.get(publictransporttime.get(curr).transportmethod)+ "\t\t\t" + locationNames.get(convert.get(publictransporttime.get(curr).to)) + "\n";
//            curr = publictransporttime.get(curr).to;
//        }
//        if (cost < 0){
//            cost = 0;
//        }
//        DecimalFormat df = new DecimalFormat("#.##");
//        string = string + "\n" + "Total Time: " + timing + " mins" + "\n" + "Cost: $" +df.format(cost);
//        estimate_print.setText(string);
//    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_SingaporeFlyer:
                if (checked) {
                    Selected.add(1);
                    break;
                } else {
                    Selected.remove(1);
                    break;
                }
            case R.id.checkbox_VivoCity:
                if (checked) {
                    Selected.add(2);
                    break;
                } else {
                    Selected.remove(2);
                    break;
                }
            case R.id.checkbox_RWS:
                if (checked) {
                    Selected.add(3);
                    break;
                } else {
                    Selected.remove(3);
                    break;
                }
            case R.id.checkbox_BuddhaToothRelic:
                if (checked) {
                    Selected.add(4);
                    break;
                } else {
                    Selected.remove(4);
                    break;
                }
            case R.id.checkbox_Zoo:
                if (checked) {
                    Selected.add(5);
                    break;
                } else {
                    Selected.remove(5);
                    break;
                }
            case R.id.checkbox_MarinaBarrage:
                if (checked) {
                    Selected.add(6);
                    break;
                } else {
                    Selected.remove(6);
                    break;
                }
            case R.id.checkbox_Esplanade:
                if (checked) {
                    Selected.add(7);
                    break;
                } else {
                    Selected.remove(7);
                    break;
                }
            case R.id.checkbox_TreeTopWalk:
                if (checked) {
                    Selected.add(8);
                    break;
                } else {
                    Selected.remove(8);
                    break;
                }
            case R.id.checkbox_MaxwellFoodCentre:
                if (checked) {
                    Selected.add(9);
                    break;
                } else {
                    Selected.remove(9);
                    break;
                }
        }
    }

    static double[][] knapSack(double W, double wt[], double val[], int n) {
        // Base Case
        if (n == 0 || W == 0) {
            double[] emptyempty = {0};
            double[][] empty = {emptyempty, new double[wt.length]};
            return empty;
        }
        if (wt[n - 1] > W) {
            return knapSack(W, wt, val, n - 1);
        } else {
            double[][] e = knapSack(W - wt[n - 1], wt, val, n - 1);
            double[][] f = knapSack(W, wt, val, n - 1);
            double[] a = {val[n - 1] + e[0][0]};
            double[] w = e[1];
            w[n - 1] = wt[n - 1];
            double[] b = f[0];
            double[] v = f[1];
            if (a[0] > b[0]) {
                double[][] c = {a, w};
                return c;
            } else {
                double[][] d = {b, v};
                return d;
            }
        }
    }
}


/*----------------------------------------------
* Class Definitions
* ---------------------------------------------*/

class BruteForce {
    int[] BestPath;
    int[] TransportCombination;
    double BestPathCost;
    double BestPathTiming = Double.POSITIVE_INFINITY;
    double CostSet;

    public void setBestPath(int[] bestPath) {
        BestPath = bestPath;
    }

    public void setTransportCombination(int[] transportCombination) {
        TransportCombination = transportCombination;
    }
    public void recursivecalc(int[] Pathcombination, Path[][] adjmat, int pos, int[] costcombination){
        if (pos == -1){
            double timing = 0;
            double cost = 0;

            for (int j = 0; j < Pathcombination.length-1; j++){
                Path curr = adjmat[Pathcombination[j]][Pathcombination[j+1]];
                timing += curr.timetaken[costcombination[j]];
                cost += curr.costs[costcombination[j]];
            }

            if (cost <= CostSet && timing <= BestPathTiming){
                BestPathTiming = timing;
                BestPathCost = cost;
                BestPath = Pathcombination.clone();
                TransportCombination = costcombination.clone();
            }
            return;
        }

        for (int i =0 ; i < 3; i++){
            costcombination[pos] = i;
            recursivecalc(Pathcombination, adjmat, pos-1, costcombination);
        }
        return;
    }


    public void recursivefor(int numberofnodes, int levels, int[] Pathcombination, Path[][] adjmat){
        if (levels == 0){
            Set<Integer> check = new HashSet<>();
            for (int i : Pathcombination){
                check.add(i);
            }
            if (check.size() == numberofnodes){
                recursivecalc(Pathcombination ,adjmat, Pathcombination.length - 2, new int[Pathcombination.length-1]);
            }
            return;
        }

        for (int i = 1; i < numberofnodes; i++){
            Pathcombination[levels] = i;
            recursivefor(numberofnodes,levels-1, Pathcombination, adjmat);
        }
        return;
    }

}

class Path{
    double[] timetaken;
    double[] costs;
    int to;
    int from;
    int transportmethod = 1;

    void setTransportmethod(int transportmethod){
        this.transportmethod = transportmethod;
    }
    Path(double walkingtime,double walkingcosts, double publictransporttime,double publictransportcosts, double taxitime, double taxicosts){
        double[] timings = {walkingtime,publictransporttime,taxitime};
        double[] costs = {walkingcosts,publictransportcosts,taxicosts};
        this.timetaken = timings;
        this.costs = costs;
        if (walkingtime < publictransporttime){
            transportmethod = 0;
        }
    }
    Path adddirection(int from, int to){
        this.to = to;
        this.from  =from;
        return this;
    }
    Path(double walkingtime,double walkingcosts, double publictransporttime,double publictransportcosts, double taxitime, double taxicosts, int from,int to){
        double[] timings = {walkingtime,publictransporttime,taxitime};
        double[] costs = {walkingcosts,publictransportcosts,taxicosts};
        this.timetaken = timings;
        this.costs = costs;
        this.to = to;
        this.from = from;
    }
}


class TimeComparator implements Comparator<Path> {
    public int compare(Path Path1, Path Path2) {
        if (Path1.timetaken[0] < Path2.timetaken[0]) {
            return -1;
        } else if (Path1.timetaken[0] == Path2.timetaken[0]) {

            if (Path1.timetaken[1] < Path2.timetaken[1]) {
                return -1;
            } else if (Path1.timetaken[1] == Path2.timetaken[1]) {
                if (Path1.timetaken[2] < Path2.timetaken[2]) {
                    return -1;
                } else if (Path1.timetaken[2] == Path2.timetaken[2]) {
                    return 0;
                } else {
                    return 1;
                }
            } else {
                return 1;
            }
        } else {
            return 1;
        }
    }
}

class Sort implements Comparator<Path> {
    public int compare(Path Path1, Path Path2) {
        if (Path1.from < Path2.from) {
            return -1;
        } else {
            return 1;
        }
    }
}


class DFSCycleDetection {
    private final ArrayList<int[]> graph;

    DFSCycleDetection(ArrayList<int[]> graph) {
        this.graph = graph;
    }

    boolean hasCycle() {
        List<Integer> visited = new ArrayList<>();
        for (int i = 0; i < graph.size(); ++i) {
            if (hasCycle(i, visited)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasCycle(int node, List<Integer> visited) {
        if (visited.contains(node)) {
            return true;
        }
        visited.add(node);
        // for (Integer nextNode : graph.get(node)) {{
        if (graph.get(node)[1] != -1) {
            if (hasCycle(graph.get(node)[1], visited)) {
                return true;
            }
        }
        // }
        visited.remove(visited.size() - 1);
        return false;
    }
}


class Kruskal {
    int maxNodes = 0;
    List<Path> EdgeList = new ArrayList<Path>();
    List<Path> FinalPaths = new ArrayList<>();

    void addEdge(Path x) {
        EdgeList.add(x);
        if (x.to + 1 > maxNodes) {
            maxNodes = x.to + 1;
        }
        if (x.from + 1 > maxNodes) {
            maxNodes = x.from + 1;
        }
    }

    List<Path> kruskal(Comparator comparator) {
        int[] Numnodesconnectedto = new int[maxNodes];
        ArrayList<int[]> adj = new ArrayList<int[]>();
        Collections.sort(EdgeList, comparator);

        for (int j = 0; j < maxNodes; j++) {
            int[] x = {-1, -1};
            adj.add(x);
        }

        int fillednodecounter = maxNodes - 1;
        for (int i = 0; i < EdgeList.size(); i++) {
            Path x = EdgeList.get(i);
            if (adj.get(x.from)[1] == -1 && adj.get(x.to)[0] == -1) {
                adj.get(x.from)[1] = x.to;
                adj.get(x.to)[0] = x.from;
                DFSCycleDetection cycleDetection = new DFSCycleDetection(adj);
               /* for (int j = 0; j < adj.size(); j++){
                    System.out.print(adj.get(j)[0] + "|");
                    System.out.println(adj.get(j)[1]);
                }
                System.out.println(cycleDetection.hasCycle());
                System.out.println("_________________");*/

                if ((!cycleDetection.hasCycle()) || (fillednodecounter == 0)) {
                    fillednodecounter = fillednodecounter - 1;
                    FinalPaths.add(x);
                } else {
                    adj.get(x.from)[1] = -1;
                    adj.get(x.to)[0] = -1;
                }
            }
        }
        return FinalPaths;
    }
}

