package com.five_o_one.ap1d;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

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
    Path [][] adjmat = new Path[10][10];
    Map<Integer,String> locationNames = new HashMap<>();
    Map<Integer,String> transportNames = new HashMap<>();
    TextView estimate_print;
    EditText budgetfill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinenary);

        estimate_print = (TextView) findViewById(R.id.estimateprint);
        budgetfill = (EditText) findViewById(R.id.budget);

        Path infPath = new Path(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        locationNames.put(0,"Marina Bay Sands");
        adjmat[0][0] = infPath.adddirection(0,0);
        adjmat[0][1] = new Path(14,0,17,1.83,3,3.22,0,1);
        adjmat[0][2] = new Path(69,0,26,1.18,14,6.96,0,2);
        adjmat[0][3] = new Path(76,0,35,4.03,19,8.50,0,3);
        adjmat[0][4] = new Path(28,0,19,0.88,8,4.98,0,4);
        adjmat[0][5] = new Path(269,0,84,1.96,30,18.40,0,5);
        adjmat[0][6] = new Path(19,0,44,0.87,6,4.76,0,6);
        adjmat[0][7] = new Path(14,0,25,0.77,4,4.43,0,7);
        adjmat[0][8] = new Path(155,0,68,1.37,21,11.30,0,8);
        adjmat[0][9] = new Path(32,0,18,0.77,6,4.90,0,9);

        locationNames.put(1,"Singapore Flyer");
        adjmat[1][0] = new Path(14,0,17,0.83,6,4.32,1,0);
        adjmat[1][1] = infPath.adddirection(1,1);
        adjmat[1][2] = new Path(81,0,31,1.26,13,7.84,1,2);
        adjmat[1][3] = new Path(88,0,38,4.03,18,9.36,1,3);
        adjmat[1][4] = new Path(39,0,24,0.98,8,4.76,1,4);
        adjmat[1][5] = new Path(264,0,85,1.89,29,18.18,1,5);
        adjmat[1][6] = new Path(29,0,50,0.97,8,5.47,1,6);
        adjmat[1][7] = new Path(12,0,21,0.77,3,3.94,1,7);
        adjmat[1][8] = new Path(151,0,70,1.33,21,10.71,1,8);
        adjmat[1][9] = new Path(38,0,24,0.77,6,5.25,1,9);

        locationNames.put(2,"Vivo City");
        adjmat[2][0] = new Path(69,0,24,1.18,12,8.30,2,0);
        adjmat[2][1] = new Path(81,0,29,1.26,14,7.96,2,1);
        adjmat[2][2] = infPath.adddirection(2,2);
        adjmat[2][3] = new Path(12,0,10,2.00,9,4.54,2,3);
        adjmat[2][4] = new Path(47,0,18,0.98,11,6.42,2,4);
        adjmat[2][5] = new Path(270,0,85,1.99,31,22.58,2,5);
        adjmat[2][6] = new Path(91,0,55,1.16,13,8.19,2,6);
        adjmat[2][7] = new Path(68,0,31,1.07,13,9.58,2,7);
        adjmat[2][8] = new Path(176,0,72,1.49,26,15.73,2,8);
        adjmat[2][9] = new Path(48,0,17,0.77,11,8.87,2,9);

        locationNames.put(3,"Resort World Sentosa");
        adjmat[3][0] = new Path(76,0,33,1.18,13,8.74,3,0);
        adjmat[3][1] = new Path(88,0,38,1.26,14,8.40,3,1);
        adjmat[3][2] = new Path(12,0,10,0.00,4,3.22,3,2);
        adjmat[3][3] = infPath.adddirection(3,3);
        adjmat[3][4] = new Path(55,0,27,0.98,12,6.64,3,4);
        adjmat[3][5] = new Path(285,0,92,1.99,32,22.80,3,5);
        adjmat[3][6] = new Path(105,0,63,1.16,19,9.26,3,6);
        adjmat[3][7] = new Path(81,0,34,0.97,19,9.65,3,7);
        adjmat[3][8] = new Path(189,0,78,1.49,32,16.92,3,8);
        adjmat[3][9] = new Path(62,0,25,0.77,17,8.89,3,9);

        locationNames.put(4, "Buddha Tooth Relic Temple");
        adjmat[4][0] = new Path(28,0,18,0.88,7,5.32,4,0);
        adjmat[4][1] = new Path(39,0,23,0.98,8,4.76,4,1);
        adjmat[4][2] = new Path(47,0,19,0.98,9,4.98,4,2);
        adjmat[4][3] = new Path(55,0,28,3.98,14,6.52,4,3);
        adjmat[4][4] = infPath.adddirection(4,4);
        adjmat[4][5] = new Path(264,0,83,1.91,30,18.40,4,5);
        adjmat[4][6] = new Path(48,0,47,0.87,9,6.17,4,6);
        adjmat[4][7] = new Path(23,0,24,0.77,1,6.67,4,7);
        adjmat[4][8] = new Path(155,0,70,1.33,20,12.48,4,8);
        adjmat[4][9] = new Path(1,0,Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY,1,3.60,4,9);

        locationNames.put(5, "Zoo");
        adjmat[5][0] = new Path(269,0,86,1.88,32,22.48,5,0);
        adjmat[5][1] = new Path(264,0,87,1.96,29,19.40,5,1);
        adjmat[5][2] = new Path(270,0,86,2.11,32,21.48,5,2);
        adjmat[5][3] = new Path(285,0,96,4.99,36,23.68,5,3);
        adjmat[5][4] = new Path(264,0,84,1.91,30,21.60,5,4);
        adjmat[5][5] = infPath.adddirection(5,5);
        adjmat[5][6] = new Path(298,0,106,1.90,33,24.43,5,6);
        adjmat[5][7] = new Path(273,0,67,1.75,28,19.93,5,7);
        adjmat[5][8] = new Path(183,0,104,1.72,21,14.66,5,8);
        adjmat[5][9] = new Path(287,0,80,1.88,29,22.27,5,9);

        locationNames.put(6,"Marina Barrage");
        adjmat[6][0] = new Path(19,0,41,0.87,6,5.99,6,0);
        adjmat[6][1] = new Path(27,0,48,0.77,10,7.02,6,1);
        adjmat[6][2] = new Path(90,0,54,1.16,12,8.17,6,2);
        adjmat[6][3] = new Path(103,0,58,4.16,26,10.58,6,3);
        adjmat[6][4] = new Path(49,0,45,0.87,10,6.81,6,4);
        adjmat[6][5] = new Path(300,0,104,1.90,30,23.96,6,5);
        adjmat[6][6] = infPath.adddirection(6,6);
        adjmat[6][7] = new Path(28,0,48,0.87,10,7.30,6,7);
        adjmat[6][8] = new Path(169,0,99,1.49,25,16.11,6,8);
        adjmat[6][9] = new Path(49,0,45,0.77,9,6.80,6,9);

        locationNames.put(7,"Esplanade");
        adjmat[7][0] = new Path(14,0,25,0.77,5,4.56,7,0);
        adjmat[7][1] = new Path(12,0,21,0.77,4,4.61,7,1);
        adjmat[7][2] = new Path(67,0,32,1.07,10,6.62,7,2);
        adjmat[7][3] = new Path(80,0,33,3.97,21,8.34,7,3);
        adjmat[7][4] = new Path(23,0,24,0.77,4,4.57,7,4);
        adjmat[7][5] = new Path(275,0,76,1.85,27,18.75,7,5);
        adjmat[7][6] = new Path(28,0,49,0.87,8,5.46,7,6);
        adjmat[7][7] = infPath.adddirection(7,7);
        adjmat[7][8] = new Path(145,0,70,1.33,20,11.16,7,8);
        adjmat[7][9] = new Path(25,0,22,0.77,4,4.56,7,9);

        locationNames.put(8,"Treetop Walk at Macrithie Reservoir");
        adjmat[8][0] = new Path(153,0,64,1.37,23,12.74,8,0);
        adjmat[8][1] = new Path(148,0,66,1.29,21,11.54,8,1);
        adjmat[8][2] = new Path(172,0,71,1.45,27,18.58,8,2);
        adjmat[8][3] = new Path(186,0,78,4.53,38,20.49,8,3);
        adjmat[8][4] = new Path(151,0,68,1.33,23,16.29,8,4);
        adjmat[8][5] = new Path(182,0,100,1.69,18,11.64,8,5);
        adjmat[8][6] = new Path(166,0,98,1.49,28,14.43,8,6);
        adjmat[8][7] = new Path(141,0,69,1.37,20,11.64,8,7);
        adjmat[8][8] = infPath.adddirection(8,8);
        adjmat[8][9] = new Path(152,0,71,1.37,22,16.28,8,9);

        locationNames.put(9,"Maxwell Road Hawker Center");
        adjmat[9][0] = new Path(33,0,17,0.77,6,5.18,9,0);
        adjmat[9][1] = new Path(37,0,23,0.77,7,5.62,9,1);
        adjmat[9][2] = new Path(48,0,18,0.77,9,5.96,9,2);
        adjmat[9][3] = new Path(61,0,26,3.77,20,7.69,9,3);
        adjmat[9][4] = new Path(1,0,Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY,4,3.60,9,4);
        adjmat[9][5] = new Path(289,0,85,1.91,28,23.15,9,5);
        adjmat[9][6] = new Path(49,0,47,0.77,8,6.05,9,6);
        adjmat[9][7] = new Path(24,0,22,0.77,6,5.26,9,7);
        adjmat[9][8] = new Path(156,0,71,1.37,21,12.68,9,98);
        adjmat[9][9] = infPath.adddirection(9,9);


        Button bruteforcebutton = (Button) findViewById(R.id.bruteforce);
        Button fastestimatebutton = (Button) findViewById(R.id.fastestimate);

        transportNames.put(0,"Walk to:");
        transportNames.put(1,"Bus/Train to:");
        transportNames.put(2,"Taxi to:");

        bruteforcebutton.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                bruteforce();
            }
        });

        fastestimatebutton.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                kruska();
            }
        });
    }

    void bruteforce(){
        Selected.add(0);
        Map<Integer,Integer> convert = new HashMap<Integer, Integer>();
        Path[][] SelectedMatrix = new Path[Selected.size()][Selected.size()];
        int icount = 0;
        for (Integer i : Selected){
            int jcount = 0;
            convert.put(icount,i);
            for (Integer j : Selected){
                SelectedMatrix[icount][jcount] = adjmat[i][j];
                SelectedMatrix[icount][jcount].adddirection(i,j);
                jcount ++;
            }
            icount++;
        }
        BruteForce x = new BruteForce();
        x.CostSet = Double.parseDouble(budgetfill.getText().toString());
        int[] Pathcombination = new int[SelectedMatrix.length + 1];
        Pathcombination[0] = 0;
        Pathcombination[SelectedMatrix.length] = 0;

        x.recursivefor(SelectedMatrix.length,SelectedMatrix.length-1, Pathcombination, SelectedMatrix);
        String text = "Start from: \t\t\t";
        for (int i = 0; i < x.BestPath.length; i++){
            if (i > 0) {
                text = text + transportNames.get(x.TransportCombination[i - 1]) + "\t\t\t";
            }
            text = text + locationNames.get(convert.get(x.BestPath[i])) + "\n" ;
        }
        String totalcost = "$" + x.BestPathCost;
        String timetaken = x.BestPathTiming + "min";
        estimate_print.setText(text + "\nTotal Time: " + timetaken +"\nCost: " + totalcost);
    }

    void kruska(){
        Kruskal x = new Kruskal();
        Selected.add(0);
        Map<Integer,Integer> convert = new HashMap<Integer, Integer>();
        Path[][] SelectedMatrix = new Path[Selected.size()][Selected.size()];
        int icount = 0;
        for (Integer i : Selected){
            int jcount = 0;
            convert.put(icount,i);
            for (Integer j : Selected){
                SelectedMatrix[icount][jcount] = adjmat[i][j];
                SelectedMatrix[icount][jcount].adddirection(icount,jcount);
                jcount ++;
            }
            icount++;
        }
        for (Path[] i : SelectedMatrix) {
            for (Path j : i) {
                x.addEdge(j);
            }
        }
        List<Path> publictransporttime = x.kruskal(new TimeComparator());
        Collections.sort(publictransporttime,new Sort());
        double timing = 0;
        double cost = 0;
        for (Path i : publictransporttime){
            timing += i.timetaken[i.transportmethod];
            cost += i.costs[i.transportmethod];
        }

        double Budget = Double.parseDouble(budgetfill.getText().toString());
        if (Budget > cost) {
            double[] timereduction = new double[publictransporttime.size()];
            double[] increasecost = new double[publictransporttime.size()];
            for (int i = 0; i < publictransporttime.size(); i++) {
                timereduction[i] = publictransporttime.get(i).timetaken[publictransporttime.get(i).transportmethod] - publictransporttime.get(i).timetaken[2];
                increasecost[i] = publictransporttime.get(i).costs[2] - publictransporttime.get(i).costs[publictransporttime.get(i).transportmethod];
            }
            double  W = Budget - cost;
            int n = timereduction.length;
            double []  changedweights  = knapSack(W, increasecost, timereduction, n)[1];
            for (int i = 0; i < changedweights.length; i++ ){
                if (changedweights[i] != 0) {
                    publictransporttime.get(i).setTransportmethod(2);
                    timing = timing - timereduction[i];
                    cost = cost + increasecost[i];
                }
            }
        } else {
            double[] timeincrease = new double[publictransporttime.size()];
            double[] stackingcost = new double[publictransporttime.size()];
            for (int i = 0; i < publictransporttime.size(); i++) {
                timeincrease[i] = publictransporttime.get(i).timetaken[0] - publictransporttime.get(i).timetaken[publictransporttime.get(i).transportmethod];
                stackingcost[i] = publictransporttime.get(i).costs[publictransporttime.get(i).transportmethod] - publictransporttime.get(i).costs[0];
            }
            double  W = Budget;
            int n = timeincrease.length;
            double []  changedweights  = knapSack(W, stackingcost, timeincrease, n)[1];
            for (int i = 0; i < changedweights.length; i++ ){
                if (changedweights[i]==0) {
                    publictransporttime.get(i).setTransportmethod(0);
                    timing = timing + timeincrease[i];
                    cost = cost - stackingcost[i];
                }
            }
        }
        int curr = 0;
        String string = "Start from:\t\t\t" +locationNames.get(convert.get(publictransporttime.get(curr).from)) + "\n";
        for (int i = 0 ; i < publictransporttime.size(); i++){
            string = string + transportNames.get(publictransporttime.get(curr).transportmethod)+ "\t\t\t" + locationNames.get(convert.get(publictransporttime.get(curr).to)) + "\n";
            curr = publictransporttime.get(curr).to;
        }
        if (cost < 0){
            cost = 0;
        }
        DecimalFormat df = new DecimalFormat("#.##");
        string = string + "\n" + "Total Time: " + timing + " mins" + "\n" + "Cost: $" +df.format(cost);
        estimate_print.setText(string);
    }

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

