package uk.ac.ncl.team12.northumbriaGo;

import com.google.android.gms.maps.model.LatLng;

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: An object for the attractions listed for the app. This allows for information about
        specific attractions to be loaded elsewhere as it is all stored in one object.

    ----------------------------------------------------------------------------------------------------*/

public class Attraction {

    private int id;
    private String name;
    private String[] tags;
    private String date;
    private String shortDesc;
    private String longDesc;
    private LatLng location;

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Various getters and setters for the variables above.

    ----------------------------------------------------------------------------------------------------*/

    public int getId(){
        return id;
    }

    private void setId(int id){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String[] getTags() {
        return tags;
    }

    private void setTags(String[] tags) {
        this.tags = tags;
    }

    public String getDate() {
        return date;
    }

    private void setDate(String date) {
        this.date = date;
    }

    String getShortDesc() {
        return shortDesc;
    }

    private void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getLongDesc() {
        return longDesc;
    }

    private void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }

    LatLng getLocation() {
        return location;
    }

    private void setLocation(LatLng location) {
        this.location = location;
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Samuel Holley

        Modified By: -----

        Description: Function runs each time the achievements page is opened. This allows us to refresh
        the list of achievements and unlock achievements which have been unlocked after the app was
        initially opened. On click listeners are added to each list item to give clickable functionality
        to allow the user to press them and load the information associated with them. This also comes
        with the ability to share your achievement using a certificate page inside the website for the
        app.

    ----------------------------------------------------------------------------------------------------*/

    Attraction(int id, String name, String[] tags, String date, String shortDesc, String longDesc, LatLng location){
        setId(id);
        setName(name);
        setTags(tags);
        setDate(date);
        setShortDesc(shortDesc);
        setLongDesc(longDesc);
        setLocation(location);
    }


}
