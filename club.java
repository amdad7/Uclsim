public class club {
    String name = "club";
    Integer level;
    char group;
    Integer points = 0;
    Integer goalsScoredGroup = 0;
    Integer goalsAgainstGroup = 0;
    String league;
    int pot;

    public club(String name, Integer level, String league, Integer pot) {
        this.level = level;
        this.name = name;
        this.league = league;
        this.pot = pot;
    }
}
