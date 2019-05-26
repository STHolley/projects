package uk.ac.ncl.team12.northumbriaGo;

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: A class which will become the data set to go into the database for each user.
        Can also be used to extract data from the database

    ----------------------------------------------------------------------------------------------------*/


//DO NOT CHANGE. REQUIRED FOR THE DATABASE
public class Preference {

    public String uid;
    public int mode;
    public boolean darkTheme;
    public boolean firstUse;
    public String achievementIds;

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Various getters and setters for the above variables.

    ----------------------------------------------------------------------------------------------------*/

    void setUid(String uid){
        this.uid = uid;
    }

    String getUid(){
        return uid;
    }

    void setMode(int mode){
        this.mode = mode;
    }

    int getMode(){
        return mode;
    }

    void setDarkTheme(boolean darkTheme){
        this.darkTheme = darkTheme;
    }

    boolean getDarkTheme(){
        return darkTheme;
    }

    void setFirstUse(boolean firstUse){
        this.firstUse = firstUse;
    }

    boolean getFirstUse(){
        return firstUse;
    }

    void setAchievementIds(String achievementIds){
        this.achievementIds = achievementIds;
    }

    String getAchievementIds(){
        return achievementIds;
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: A constructor for the preference. This sets the requirements for the inputs and
        populates them accordingly.

    ----------------------------------------------------------------------------------------------------*/

    public Preference(String uid, int mode, boolean darkTheme, boolean firstUse , String achievementIds){
        this.uid = uid;
        this.mode = mode;
        this.darkTheme = darkTheme;
        this.firstUse = firstUse;
        this.achievementIds = achievementIds;
    }

    public Preference(){

    }


}
