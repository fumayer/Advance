package com.example.testlibrary.flyweight;

import java.util.List;

/**
 * Created by sunjie on 2018/4/22.
 */

public interface Menu {


    //规定了实现类必须实现设置内外关系的方法

    public void setPersonMenu(String person, List list);

//规定了实现类必须实现查找外蕴状态对应的内蕴状态的方法

    public List findPersonMenu(String person, List list);


}
