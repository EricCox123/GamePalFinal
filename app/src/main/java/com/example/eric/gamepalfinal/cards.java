package com.example.eric.gamepalfinal;

public class cards {

    public String usrID, name;
    public String imageUrl1, imageUrl2,imageUrl3,imageUrl4;



    public cards (String usrID, String name,String imageUrl1,String imageUrl2,String imageUrl3,String imageUrl4){
        this.usrID = usrID;
        this.name = name;
        this.imageUrl1 = imageUrl1;
        this.imageUrl2 = imageUrl2;
        this.imageUrl3 = imageUrl3;
        this.imageUrl4 = imageUrl4;

    }
    public String getUsrID(){
        return usrID;
    }
    public void setUsrID(String usrID){
        this.usrID = usrID;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getImageUrl1(){
        return imageUrl1;
    }
    public void setImageUrl1(String imageUrl1){
        this.imageUrl1 = imageUrl1;
    }

    public String getImageUrl2(){
        return imageUrl2;
    }
    public void setImageUrl2(String imageUrl2){
        this.imageUrl2 = imageUrl2;
    }

    public String getImageUrl3(){
        return imageUrl3;
    }
    public void setImageUrl3(String imageUrl13){
        this.imageUrl3 = imageUrl3;
    }

    public String getImageUrl4(){
        return imageUrl4;
    }
    public void setImageUrl4(String imageUrl4){
        this.imageUrl4 = imageUrl4;
    }

}
