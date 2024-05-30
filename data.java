import java.util.*;
import java.io.*;

enum Rounds {
    GROUP,
    R16,
    R8,
    R4,
    FINAL
};

public class data {

    String[] Groups1 = "A,B,C,D,E,F,G,H".split(",");
    //list of all groups
    List<String> Groups = Arrays.asList(Groups1);
    //all the match results
    EnumMap<Rounds,HashMap<String,ArrayList<match>>> matchList = new EnumMap<>(Rounds.class);
    //all the clubs in the tournament
    List<club> clubs = new ArrayList<club> (32);
    //all the clubs devided in groups
    HashMap<String,ArrayList<club>> groupList = new HashMap<String,ArrayList<club>>();

    //fixture connected to the current ucl
    fixture myFixture;
    public data(fixture myFixture1){
        myFixture = myFixture1;
    }

    //function to integrate the match result into group table
    static void createGroupTables(EnumMap<Rounds,HashMap<String,ArrayList<match>>> matchList, String groupp){
        //System.out.println(groupp);
        for (match m: matchList.get(Rounds.GROUP).get(groupp)){

            Integer tempgoalsA = m.a.goalsScoredGroup; Integer tempgoalsB = m.a.goalsScoredGroup;
            Integer tempAgainstA = m.a.goalsAgainstGroup; Integer tempAgainstB = m.b.goalsAgainstGroup;
            Integer tempPointsA = m.a.points; Integer tempPointsB = m.b.points;

            m.a.goalsScoredGroup = tempgoalsA + m.aGoals;
            m.b.goalsScoredGroup = tempgoalsB + m.bGoals;
            m.a.goalsAgainstGroup = tempAgainstA + m.bGoals;
            m.b.goalsAgainstGroup = tempAgainstB + m.aGoals;
            m.a.points = tempPointsA + m.aPoints();
            m.b.points = tempPointsB + m.bPoints();

           /* if (groupp.equals("H")){
                System.out.println(String.format("%1s %2s",m.a.name,m.b.name));
                System.out.println(String.format("%1d %2d",m.a.points,m.b.points));
                System.out.println(String.format("%1d %2d",m.aPoints(),m.bPoints()));

            }*/


        }
    }

    //reading the club data from file
    public void createClubList(){
        //System.out.println(this.Groups[1]);
        try {
            File ff1 = new File("clubs.txt");
            Scanner reader = new Scanner(ff1);
            int i = 0;
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                //System.out.println(line);
                String[] arr1 = line.split(",");
                //System.out
                club temp = new club(arr1[0],Integer.parseInt(arr1[1]),arr1[2],Integer.parseInt(arr1[3]));
                this.clubs.add(i,temp);
            }
            reader.close();
            //System.out.println(this.clubs.get(31).name);
            //System.out.println(this.clubs.get(31).level);
            //System.out.println(this.clubs.get(31).league);
        }
        catch (FileNotFoundException e) {
            System.out.println("an error occured");
            e.printStackTrace();
        }

        try{
            this.groupList = this.myFixture.generateGroups(clubs,Groups);
            System.out.println("Generated all groups now simulating matches in group");
            Thread.sleep(3000);
        }
        catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
    }

    //running the group stage top level function
    public void runGroupStage(){
        HashMap<String,ArrayList<match>> groupResults = new HashMap<String,ArrayList<match>>();
        Integer groupCount = this.groupList.keySet().size();

        //simulating all the groups
        for (Integer i = 0; i < groupCount; i++){
            groupResults.put(Groups.get(i),this.myFixture.simulateGroup(this.groupList.get(this.Groups.get(i))));
        }
        this.matchList.put(Rounds.GROUP,groupResults);

        //creating the final group tables
        for (Integer i = 0; i < groupCount; i++) {
            createGroupTables(matchList,Groups.get(i));

            //priority order - points,goaldifference,goals scored, goals conceded
            this.groupList.get(Groups.get(i)).sort((o1,o2) -> {
                int cmp = Integer.compare(o1.points*-1,o2.points*-1);
                if (cmp == 0) {
                    cmp = Integer.compare(o2.goalsScoredGroup - o2.goalsAgainstGroup,o1.goalsScoredGroup - o1.goalsAgainstGroup);
                }
                if (cmp == 0) {
                    cmp = Integer.compare(o2.goalsScoredGroup,o1.goalsScoredGroup);
                }
                if (cmp == 0) {
                    cmp = Integer.compare(o2.goalsAgainstGroup,o1.goalsAgainstGroup);
                }
                return cmp;
            });
            ArrayList<club> tempclubs =  this.groupList.get(Groups.get(i));
            System.out.println(String.format("Group %1S",Groups.get(i)));
            System.out.println("NAME  GOALS-SCORED GOALS CONCEDED POINTS");
            for (Integer j = 0;j<4;j++){
                System.out.println(String.format("%1S %2d %3d %4d",tempclubs.get(j).name,tempclubs.get(j).goalsScoredGroup,
                        tempclubs.get(j).goalsAgainstGroup,tempclubs.get(j).points));
            }
            System.out.println("---------");

        }

    }


}
