import java.util.*;
import java.io.*;

public class fixture {

    //funtion to check eligibility of a team to fit in a group
    static Integer checkIfEligible(club team,ArrayList<club> teams) {
        Integer eligible = 1;
        for (club teamGR:teams){

            //System.out.print(team.league);
            //System.out.println(teamGR.league);
            if (teamGR.name.equals(team.name)){
                //System.out.println("save");
                continue;
            }
            if (teamGR.league.equals(team.league)){
                //System.out.print(team.name);
                //System.out.println(teamGR.name);
                eligible = 0;
                break;
            }
        }
        return eligible;
    }


    //function to find the right swaps when in conflict
    static Integer swap(Integer gp, Integer cp, HashMap<String,ArrayList<club>> groupList,club team,List<String> group){
        Integer swapped = 0;
        groupList.get(group.get(gp)).add(team);
        //System.out.println(String.format("started swapping for %s",team.name));

        for (Integer i =0;i<7;i++){

            club temp  = groupList.get(group.get(i)).get(cp-1);
            //System.out.println(String.format("swapping with %s",temp.name));
            groupList.get(group.get(i)).set(cp-1,team);
            groupList.get(group.get(gp)).set(cp-1,temp);
            if (((checkIfEligible(team,groupList.get(group.get(i))) == 1) && (checkIfEligible(temp,groupList.get(group.get(gp)))) == 1)){

                /*System.out.print("swapped  ");
                System.out.print(temp.name);
                System.out.print(i);
                System.out.println(String.format("team %s",team.name));*/

                swapped = 1;
                break;
            }
            else{
                groupList.get(group.get(i)).set(cp-1,temp);
                groupList.get(group.get(gp)).set(cp-1,team);
            }
        }
        return swapped;
    }

    //function to generate groups from pots
    public HashMap<String,ArrayList<club>> generateGroups(List<club> clubs,List<String> group) {
        HashMap<Integer,List<club>> pots = new HashMap<Integer,List<club>>();
        HashMap<club,Integer> isPicked = new HashMap<club,Integer>();
        HashMap<String,ArrayList<club>> groupList = new HashMap<String,ArrayList<club>>();

        //preparing group list
        for(String i:group) {
            ArrayList<club> temp = new ArrayList();
            groupList.put(i,temp);
        }

        //preparing pot
        HashMap<Integer,Integer> potPointer = new HashMap<Integer,Integer>();
        for(int i=1;i<=4;i++) {
            List<club> temp = new ArrayList(8);
            pots.put(i,temp);
            potPointer.put(i,0);
        }

        for (club team:clubs) {
            pots.get(team.pot).add(potPointer.get(team.pot),team);
            int temp  = potPointer.get(team.pot) + 1;
            potPointer.replace(team.pot,temp);
            isPicked.put(team,0);
        }

        //start of logic of spliting groups
        int cp,gp;
        cp=1;gp=0;

        //randomizing clubs pots order
        for (int i=1;i<4;i++) {
            Collections.shuffle(pots.get(i));
        }
        List<Integer> tp = new ArrayList<Integer>(5);
        List<Integer> pp = new ArrayList<Integer>(5);
        for (int j  = 0; j <5 ; j++){
            tp.add(j,0);
            pp.add(j,0);
        }

        //key parameters to track cycles
        Integer searchPot = 0;
        club searchStart = clubs.get(0);

        while (true) {

            //final break when all groups have more than 4members and all teams covered
            if (cp >= 4 && groupList.get(group.get(7)).size() >= 4){
                break;
            }
            //all pots cycling
            if (cp > 4){
                cp = 1;
            }
            //pots internal cycling
            if (tp.get(cp) >= pots.get(cp).size()){
                tp.set(cp,pp.get(cp));
            }
            //group cycling
            if (gp == groupList.keySet().size()){
                gp = 0;
            }
            //debuggeer for outputs per iteration
            /*
            System.out.print("tp: ");
            for (Integer i:tp){
                System.out.print(i);
                System.out.print(" ");
            }
            System.out.println("------///---");
            System.out.print("pp: ");
            for (Integer i:pp){

                System.out.print(i);
                System.out.print(" ");
            }
            System.out.println("------///---");

            System.out.print("cp  ");
            System.out.println(cp);
            System.out.print("tp  ");
            System.out.println(tp.get(cp));
            System.out.print("pp  ");
            System.out.println(pp.get(cp));*/


            Integer currentTaken = isPicked.get(pots.get(cp).get(tp.get(cp)));
            club team = pots.get(cp).get(tp.get(cp));

            //check if club is already alloted if coming in cycles
            if (currentTaken == 1){
                //tp.set(cp) = tp.get(cp) + 1;
                Integer temp = tp.get(cp);
                tp.set(cp,temp+1);
                //System.out.println("takenn");
                continue;

            }
            //checking if eligible to be in the current group
            Integer eligible = checkIfEligible(team,groupList.get(group.get(gp)));
            /*
            System.out.println(eligible);
            System.out.println("+++++++++++++++++++++++");
             */
            if (eligible == 1){

                searchPot = 0;
                /*
                System.out.print(team.name);
                System.out.print(" added to -> ");
                System.out.println(group.get(gp));
                */
                groupList.get(group.get(gp)).add(team);
                isPicked.put(pots.get(cp).get(tp.get(cp)),1);
                //if groups fill up
                if (groupList.get(group.get(gp)).size()==4){
                    /*
                    System.out.println(String.format("group %s",group.get(gp)));
                    for (club i:groupList.get(group.get(gp))){
                        System.out.println(i.name);
                    }
                    System.out.println("---");
                    */
                    gp+=1;

                }
                //re adjusting temperory and permenent pointers and moving to the next pot
                if (pp.get(cp) == tp.get(cp)){
                    Integer temp = pp.get(cp);
                    pp.set(cp,temp+1);
                    tp.set(cp,pp.get(cp));
                }
                cp+=1;
            }
            else {
                //detecting blockers and finding swaps for the same
                if (gp == 7 || pp.get(cp) == 7 || ((searchPot == 1) && (team.name.equals(searchStart.name)))){
                    Integer swapped = 0;
                    swapped = swap(gp,cp,groupList,team,group);
                    if (swapped == 1){
                        //System.out.println(String.format("success swap %1s from %2s",team.name,group.get(gp)));
                        isPicked.put(team,1);
                    }
                    else {
                        //System.out.println(String.format("failed swap %1s from %2s",team.name,group.get(gp)));
                        System.out.println("cant make groups with this set of teams");
                        break;
                    }
                    if (cp == 4) {
                        break;
                    }
                    cp+=1;
                    continue;

                }
                if (searchPot == 0){
                    searchPot = 1;
                    searchStart = team;
                }

                Integer temp = tp.get(cp);
                tp.set(cp,temp+1);
                /*if (cp >= 4 && tp.get(cp) >= pots.get(cp).size() && gp >= groupList.keySet().size()){
                    System.out.println("can't form groups with the given teams and rules");
                    break;
                }*/
            }


        }
        //outputting groups on console
        for (Integer gpp = 0; gpp < 8; gpp++){
            System.out.println(String.format("group %s",group.get(gpp)));
            for (club i:groupList.get(group.get(gpp))){
                System.out.println(i.name);
            }
            System.out.println("---");
        }
        return groupList;
    }

    //function to simulate the each group mathces in ucl every team plays each other twice
    public ArrayList<match> simulateGroup(ArrayList<club> clubs){

        ArrayList<match> matchList = new ArrayList<match>();
        Integer groupSize = clubs.size();
        for(Integer num = 0; num<2; num++){
            for (Integer i = 0; i < groupSize; i++) {
                for (Integer j = i + 1; j<groupSize; j++){
                    match m = new match(clubs.get(i),clubs.get(j));
                    m.runMatch();
                    System.out.println(String.format("%1s vs %2s",m.a.name,m.b.name));
                    System.out.println(String.format("%1o - %2o",m.aGoals,m.bGoals));
                    matchList.add(m);

                }
            }
        }
        return matchList;

    }
    // function to simulate round of 16
    public void simulateR16() {

    }
}