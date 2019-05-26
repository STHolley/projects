package uk.ac.ncl.team12.northumbriaGo;

import java.util.Calendar;

    /*----------------------------------------------------------------------------------------------------
        Created By: Michael Putra

        Modified By: -----

        Description: A custom class for storing achievement items, along with data such as the path for
        the images (Both locked and unlocked variants), and things like text to accompany the images in
        both the list view and as the certificate popup.

    ----------------------------------------------------------------------------------------------------*/

public class AchievementItem {
    private int itemID;
    private String itemName;
    private String itemDescription;
    private int unlckItemLogo;
    private int lckItemLogo;
    private String itemCertificate;
    private boolean status = false;
    private Calendar dateUnlocked;

    /*----------------------------------------------------------------------------------------------------
        Created By: Michael Putra

        Modified By: -----

        Description: A simple constructor to populate the object's data fields

    ----------------------------------------------------------------------------------------------------*/

    public AchievementItem(int id, String name, String description, int unlogo, int lcklogo, String certificate) {
        this.itemID = id;
        this.itemName = name;
        this.itemDescription = description;
        this.unlckItemLogo = unlogo;
        this.lckItemLogo = lcklogo;
        this.itemCertificate = certificate;
        this.dateUnlocked = Calendar.getInstance();
    }

    /*----------------------------------------------------------------------------------------------------
        Created By: Michael Putra

        Modified By: -----

        Description: Various getters and setters

    ----------------------------------------------------------------------------------------------------*/
    public int getItemID() {
        return this.itemID;
    }

    public String getItemName() {
        return this.itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public int getUnlckItemLogo() {
        return unlckItemLogo;
    }

    public int getLckItemLogo() {
        return lckItemLogo;
    }

    public String getItemCertificate() {
        return itemCertificate;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Calendar getDateUnlocked() {
        return dateUnlocked;
    }

    public void setDateUnlocked(Calendar dateUnlocked) {
        this.dateUnlocked = dateUnlocked;
    }
}
