package com.shortmeet.www.Base;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fenglingyue on 2017/7/30.
 * 对所有Activity进行的操作
 */

public class CtrollAllactivitys {
    private static List<BaseActivity> mBaseActivities=new ArrayList<>();

    public static void addActivity(BaseActivity baseActivity){
     mBaseActivities.add(baseActivity);
    }

    public static  void removeActivity(BaseActivity baseActivity){
        mBaseActivities.remove(baseActivity);
    }

    public static <T extends BaseActivity> T getActivity(Class activityClass){
         for(BaseActivity baseActivity:mBaseActivities){
         if(baseActivity.getClass()==activityClass){
                 return (T) baseActivity;
         }
         }
         throw  new RuntimeException("No Such ACTIVITY FOUND");
    }

    public static  void killActivity(Class activityclass){
       for(int i=mBaseActivities.size()-1;i>=0;i--){
          if(activityclass==mBaseActivities.get(i).getClass()){
             mBaseActivities.get(i).killSelf();
          }
       }
    }

   public  static  void KillAll(){
       for (int i=mBaseActivities.size()-1;i>=0;i--){
           mBaseActivities.get(i).killSelf();
       }
   }
}
