package iitd.collegeconnect;


import android.app.Application;

public class Globals extends Application {
    private String username;
    private String other_username;
    private boolean is_other=false;

    public String getUsername(){
        return username;
    }
    public void setUsername(String uname){
        username = uname;
    }

    public String getother_username(){return other_username;}
    public void setother_username(String uname){
        other_username = uname;
    }

    public boolean getis_other(){return is_other;}
    public void setis_other(){is_other = !is_other;}
}
