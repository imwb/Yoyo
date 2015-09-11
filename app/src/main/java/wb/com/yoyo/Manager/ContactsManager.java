package wb.com.yoyo.Manager;

import android.content.Context;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import wb.com.yoyo.Activity.ActivitySupport;
import wb.com.yoyo.DB.DBManager;
import wb.com.yoyo.Model.User;
import wb.com.yoyo.Utils.StringUtil;

/**
 * 联系人管理类
 * Created by wb on 15/9/11.
 */
public class ContactsManager {
    private XMPPConnection connection;
    private Context context;
    private DBManager dbManager;

    public ContactsManager(Context context) {
        SPManager spManager=new SPManager(context);
        this.context = context;
        dbManager=DBManager.getInstance(context,spManager.getLoginConfig().getUsername());
    }

    /**
     * 加载联系人
     * @return roster
     */
    public List<User> loadContacts(){
        ArrayList<User> users=new ArrayList<>();
        Collection<RosterEntry> entris=XmppManager.getConnection().getRoster().getEntries();
        Roster roster=XmppManager.getConnection().getRoster();

        for(RosterEntry entry:entris){
            User user = transEntryToUser(entry,roster);
            users.add(user);
        }
        return users;
    }

    /**
     * 保存到本地
     */
    public void savaContacts(List<User> users){
        dbManager.saveContacts(users);
    }

    /**
     * 从本地获取联系人
     * @return
     */
    public List<User> getContactsFromDB(){
        return dbManager.getAllContacts();
    }

    public void clearDB(){
        dbManager.deleteAllContacts();
    }
    public void addContact(User user){

    }

    public void updateContact(){

    }

    /**
     * 根据RosterEntry创建一个User
     *
     * @param entry
     * @return
     */
    public  User transEntryToUser(RosterEntry entry, Roster roster) {
        User user = new User();
        user.setJID(entry.getUser());
        Presence presence = roster.getPresence(entry.getUser());
        user.setFrom(presence.getFrom());
        user.setStatus(presence.getStatus());
        user.setAvailable(presence.isAvailable());
        user.setType(entry.getType());

        //user.setvCard(userManager.getUserVCard(entry.getUser()));
        if (entry.getName() == null) {
//			if(user.getvCard()==null)
//			user.setName(StringUtil.getUserNameByJid(entry.getUser()));
//			else {
//				user.setName(user.getvCard().getNickName());
//			}
            user.setName(StringUtil.getUserNameByJid(entry.getUser()));
        } else {
            user.setName(entry.getName());
        }
        String avatar=UserManager.saveAvatar(entry.getUser(),context);
        user.setAvatar_path(avatar);

        return user;
    }

}
