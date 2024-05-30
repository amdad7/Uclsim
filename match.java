import java.io.*;
import java.util.*;

public class match {

    club a;
    club b;
    Integer aGoals = 0;
    Integer bGoals = 0;
    //int result  = draw;


    public match(club a, club b){
        this.a = a;this.b = b;
    }

    public Integer aPoints(){
        Integer points = 0;
        if (this.aGoals > this.bGoals){
            points = 3;
        } else if (this.aGoals.equals(this.bGoals)) {
            points = 1;
        }
        else {
            points = 0;
        }
        return points;
    }
    public Integer bPoints(){
        Integer points = 0;
        if (this.aGoals < this.bGoals){
            points = 3;
        } else if (this.aGoals.equals(this.bGoals)) {
            points = 1;
        }
        else {
            points = 0;
        }
        return points;
    }

    public void runMatch() {

        Random rand = new Random();
        ArrayList<ArrayList<Integer>> goalPossibility = new ArrayList<ArrayList<Integer>>();

        for (Integer i=0;i<6;i++){
            ArrayList<Integer> temp = new ArrayList<Integer>(7);
            for (Integer j=0;j<7;j++){
                temp.add(j,0);
            }
            goalPossibility.add(i,temp);
        }
        //System.out.println(goalPossibility.size());
        //System.out.println(goalPossibility.get(0).size());
        for (Integer i=1;i<6;i++){
            for (Integer j=6;j>=0;j--) {
                //goalPossibility.get(i).add(j,0);
                //System.out.println(String.format("%1d , %2d ",i,j));
                if (i == 1) {

                    goalPossibility.get(i).set(j,j);
                    continue;
                }
                Integer prevv = goalPossibility.get(i-1).get(j);
                if (j > (6-i)) {
                    Integer temp = prevv - 1;
                    goalPossibility.get(i).set(j,temp);
                }

                else if (j == (6-i)) {
                    if ((2*i)>6){
                        goalPossibility.get(i).set(j,prevv + (2*i - 6));
                    }
                    else{
                        goalPossibility.get(i).set(j,prevv + 1);
                    }
                }
                else if (j < 6-i) {
                    if (j > (6-(2*i))) {
                        goalPossibility.get(i).set(j,prevv + 1) ;
                    }
                    else {
                        goalPossibility.get(i).set(j,prevv);
                    }
                }
            }
           /*for (Integer k=1;k<6;k++){
                for (Integer l=0;l<=6;l++){
                    System.out.print(String.format("%d ",goalPossibility.get(k).get(l)));
                }
                System.out.println();
            }
            System.out.println("------------");*/
        }

        ArrayList<ArrayList<Integer>> goalSet = new ArrayList<ArrayList<Integer>>();
        for (Integer k=0;k<6;k++){
            ArrayList<Integer> temp = new ArrayList<Integer>();
            goalSet.add(temp);
            if (k==0){continue;}
            for (Integer l=0;l<=6;l++){
                for (Integer count = goalPossibility.get(k).get(l);count>0;count--){
                    goalSet.get(k).add(l);
                }
            }
            Collections.shuffle(goalSet.get(k));
        }
        /*for (Integer i=0;i<21;i++){
            System.out.print(String.format("%d ",goalSet.get(5).get(i)));
        }*/
        /*for (Integer i=1;i<6;i++) {
            System.out.print(String.format("%d ",goalSet.get(i).size()));
        }*/

        double aTotal,bTotal;
        aTotal = bTotal = 0;

        Integer optionsCountA = goalSet.get(this.a.level).size();
        Integer optionsCountB = goalSet.get(this.b.level).size();

        for (int i=0;i<2;i++){

            aTotal += goalSet.get(this.a.level).get(rand.nextInt(optionsCountA));
            bTotal += goalSet.get(this.a.level).get(rand.nextInt(optionsCountA));
            //System.out.println(aTotal);
            //System.out.println(bTotal);
        }
        double aAverage = aTotal/2 ;int tempa = (int)(aAverage );
        double bAverage = bTotal/2 ;int tempb = (int)(bAverage );
        //System.out.println(aAverage);
        //System.out.println(bAverage);
        this.aGoals = Integer.valueOf(tempa);
        this.bGoals = Integer.valueOf(tempb);
    }
}