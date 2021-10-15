package romanow.abc.core.API;

import romanow.abc.core.DBRequest;
import romanow.abc.core.entity.Entity;
import romanow.abc.core.entity.baseentityes.JLong;
import romanow.abc.core.entity.users.Account;
import romanow.abc.core.entity.users.User;

public interface I_APIConnector {
    public void addEntity(String token, DBRequest body,int level, I_APICallBack<JLong> back);
    public void getEntity(String token, String className, long id, int level, I_APICallBack<Entity> back);
    public void ping(I_APICallBack back);
    public void login(Account account, I_APICallBack<User> back);
    }
