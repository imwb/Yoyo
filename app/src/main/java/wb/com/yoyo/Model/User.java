package wb.com.yoyo.Model;


import android.os.Parcel;
import android.os.Parcelable;

import org.jivesoftware.smack.packet.RosterPacket;

/**
 * intent可以携带传递Parcel数据，需要实现三个方法 .
 * 1、describeContents()返回0就可以.
 * 2、将需要的数据写入Parcel中，框架调用这个方法传递数据.
 * 3、重写外部类反序列化该类时调用的方法.
 *
 * @author wb
 *
 */
public class User implements Parcelable {

    /**
     * 将user保存在intent中时的key
     */
    public static final String userKey = "lovesong_user";
    private String name;
    private String JID;
    private static RosterPacket.ItemType type;
    private String status;
    private String from;
    private String groupName;
    private boolean available;
    private String  avatar_path;
    private String  nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJID() {
        return JID;
    }

    public void setJID(String jID) {
        JID = jID;
    }

    public RosterPacket.ItemType getType() {
        return type;
    }

    @SuppressWarnings("static-access")
    public void setType(RosterPacket.ItemType type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getAvatar_path() {
        return avatar_path;
    }

    public void setAvatar_path(String avatar_path) {
        this.avatar_path = avatar_path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(JID);
        dest.writeString(name);
        dest.writeString(from);
        dest.writeString(status);
        dest.writeInt(available ? 1 : 0);
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {

        @Override
        public User createFromParcel(Parcel source) {
            User u = new User();
            u.JID = source.readString();
            u.name = source.readString();
            u.from = source.readString();
            u.status = source.readString();
            u.available = source.readInt() == 1 ? true : false;
            return u;
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }

    };

    public User clone() {
        User user = new User();
        user.setAvailable(User.this.available);
        user.setFrom(User.this.from);
        user.setJID(User.this.JID);
        user.setName(User.this.name);
        user.setStatus(User.this.status);
        return user;
    }

    @Override
    public String toString() {
        return "User [name=" + name + ", JID=" + JID + ", status=" + status
                + ", from=" + from + ", groupName=" + groupName + ", available=" + available + "]";
    }

}
