package wb.com.yoyo.Manager;


/**
 * 用户管理类
 * Created by wb on 15/9/10.
 */
public class UserManager {
    public static UserManager manager;
    private UserManager(){

    }

    public static UserManager getInstance() {
        if(manager!=null)
            return manager;
        else
            return new UserManager();

    }
}
