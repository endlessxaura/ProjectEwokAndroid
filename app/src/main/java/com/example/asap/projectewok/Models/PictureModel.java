package com.example.asap.projectewok.Models;

/**
 * Created by asap on 8/19/16.
 */
public class PictureModel {
    //Properties
    int pictureID;
    String attachedType;
    int attachedID;
    String filePath;

    //Constructor
    public PictureModel(int pictureID, String attachedType, int attachedID, String filePath){
        this.pictureID = pictureID;
        this.attachedID = attachedID;
        this.attachedType = attachedType;
        this.filePath = filePath;
    }
}
