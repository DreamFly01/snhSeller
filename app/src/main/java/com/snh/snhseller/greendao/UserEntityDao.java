package com.snh.snhseller.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.snh.snhseller.bean.beanDao.UserEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "USER_ENTITY".
*/
public class UserEntityDao extends AbstractDao<UserEntity, Void> {

    public static final String TABLENAME = "USER_ENTITY";

    /**
     * Properties of entity UserEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, int.class, "Id", false, "ID");
        public final static Property Username = new Property(1, String.class, "Username", false, "USERNAME");
        public final static Property ShopName = new Property(2, String.class, "ShopName", false, "SHOP_NAME");
        public final static Property BusinessActivities = new Property(3, String.class, "BusinessActivities", false, "BUSINESS_ACTIVITIES");
        public final static Property Logo = new Property(4, String.class, "Logo", false, "LOGO");
        public final static Property Introduction = new Property(5, String.class, "Introduction", false, "INTRODUCTION");
        public final static Property Contacts = new Property(6, String.class, "Contacts", false, "CONTACTS");
        public final static Property ContactsTel = new Property(7, String.class, "ContactsTel", false, "CONTACTS_TEL");
        public final static Property ShopTypeName = new Property(8, String.class, "shopTypeName", false, "SHOP_TYPE_NAME");
        public final static Property Token = new Property(9, String.class, "Token", false, "TOKEN");
        public final static Property Accid = new Property(10, String.class, "Accid", false, "ACCID");
        public final static Property SuppFxUrl = new Property(11, String.class, "suppFxUrl", false, "SUPP_FX_URL");
        public final static Property SuppType = new Property(12, String.class, "suppType", false, "SUPP_TYPE");
        public final static Property ContactsQQ = new Property(13, String.class, "ContactsQQ", false, "CONTACTS_QQ");
        public final static Property Address = new Property(14, String.class, "Address", false, "ADDRESS");
        public final static Property Province = new Property(15, String.class, "Province", false, "PROVINCE");
        public final static Property City = new Property(16, String.class, "City", false, "CITY");
        public final static Property Area = new Property(17, String.class, "Area", false, "AREA");
    }


    public UserEntityDao(DaoConfig config) {
        super(config);
    }
    
    public UserEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USER_ENTITY\" (" + //
                "\"ID\" INTEGER NOT NULL ," + // 0: Id
                "\"USERNAME\" TEXT," + // 1: Username
                "\"SHOP_NAME\" TEXT," + // 2: ShopName
                "\"BUSINESS_ACTIVITIES\" TEXT," + // 3: BusinessActivities
                "\"LOGO\" TEXT," + // 4: Logo
                "\"INTRODUCTION\" TEXT," + // 5: Introduction
                "\"CONTACTS\" TEXT," + // 6: Contacts
                "\"CONTACTS_TEL\" TEXT," + // 7: ContactsTel
                "\"SHOP_TYPE_NAME\" TEXT," + // 8: shopTypeName
                "\"TOKEN\" TEXT," + // 9: Token
                "\"ACCID\" TEXT," + // 10: Accid
                "\"SUPP_FX_URL\" TEXT," + // 11: suppFxUrl
                "\"SUPP_TYPE\" TEXT," + // 12: suppType
                "\"CONTACTS_QQ\" TEXT," + // 13: ContactsQQ
                "\"ADDRESS\" TEXT," + // 14: Address
                "\"PROVINCE\" TEXT," + // 15: Province
                "\"CITY\" TEXT," + // 16: City
                "\"AREA\" TEXT);"); // 17: Area
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USER_ENTITY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, UserEntity entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String Username = entity.getUsername();
        if (Username != null) {
            stmt.bindString(2, Username);
        }
 
        String ShopName = entity.getShopName();
        if (ShopName != null) {
            stmt.bindString(3, ShopName);
        }
 
        String BusinessActivities = entity.getBusinessActivities();
        if (BusinessActivities != null) {
            stmt.bindString(4, BusinessActivities);
        }
 
        String Logo = entity.getLogo();
        if (Logo != null) {
            stmt.bindString(5, Logo);
        }
 
        String Introduction = entity.getIntroduction();
        if (Introduction != null) {
            stmt.bindString(6, Introduction);
        }
 
        String Contacts = entity.getContacts();
        if (Contacts != null) {
            stmt.bindString(7, Contacts);
        }
 
        String ContactsTel = entity.getContactsTel();
        if (ContactsTel != null) {
            stmt.bindString(8, ContactsTel);
        }
 
        String shopTypeName = entity.getShopTypeName();
        if (shopTypeName != null) {
            stmt.bindString(9, shopTypeName);
        }
 
        String Token = entity.getToken();
        if (Token != null) {
            stmt.bindString(10, Token);
        }
 
        String Accid = entity.getAccid();
        if (Accid != null) {
            stmt.bindString(11, Accid);
        }
 
        String suppFxUrl = entity.getSuppFxUrl();
        if (suppFxUrl != null) {
            stmt.bindString(12, suppFxUrl);
        }
 
        String suppType = entity.getSuppType();
        if (suppType != null) {
            stmt.bindString(13, suppType);
        }
 
        String ContactsQQ = entity.getContactsQQ();
        if (ContactsQQ != null) {
            stmt.bindString(14, ContactsQQ);
        }
 
        String Address = entity.getAddress();
        if (Address != null) {
            stmt.bindString(15, Address);
        }
 
        String Province = entity.getProvince();
        if (Province != null) {
            stmt.bindString(16, Province);
        }
 
        String City = entity.getCity();
        if (City != null) {
            stmt.bindString(17, City);
        }
 
        String Area = entity.getArea();
        if (Area != null) {
            stmt.bindString(18, Area);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, UserEntity entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String Username = entity.getUsername();
        if (Username != null) {
            stmt.bindString(2, Username);
        }
 
        String ShopName = entity.getShopName();
        if (ShopName != null) {
            stmt.bindString(3, ShopName);
        }
 
        String BusinessActivities = entity.getBusinessActivities();
        if (BusinessActivities != null) {
            stmt.bindString(4, BusinessActivities);
        }
 
        String Logo = entity.getLogo();
        if (Logo != null) {
            stmt.bindString(5, Logo);
        }
 
        String Introduction = entity.getIntroduction();
        if (Introduction != null) {
            stmt.bindString(6, Introduction);
        }
 
        String Contacts = entity.getContacts();
        if (Contacts != null) {
            stmt.bindString(7, Contacts);
        }
 
        String ContactsTel = entity.getContactsTel();
        if (ContactsTel != null) {
            stmt.bindString(8, ContactsTel);
        }
 
        String shopTypeName = entity.getShopTypeName();
        if (shopTypeName != null) {
            stmt.bindString(9, shopTypeName);
        }
 
        String Token = entity.getToken();
        if (Token != null) {
            stmt.bindString(10, Token);
        }
 
        String Accid = entity.getAccid();
        if (Accid != null) {
            stmt.bindString(11, Accid);
        }
 
        String suppFxUrl = entity.getSuppFxUrl();
        if (suppFxUrl != null) {
            stmt.bindString(12, suppFxUrl);
        }
 
        String suppType = entity.getSuppType();
        if (suppType != null) {
            stmt.bindString(13, suppType);
        }
 
        String ContactsQQ = entity.getContactsQQ();
        if (ContactsQQ != null) {
            stmt.bindString(14, ContactsQQ);
        }
 
        String Address = entity.getAddress();
        if (Address != null) {
            stmt.bindString(15, Address);
        }
 
        String Province = entity.getProvince();
        if (Province != null) {
            stmt.bindString(16, Province);
        }
 
        String City = entity.getCity();
        if (City != null) {
            stmt.bindString(17, City);
        }
 
        String Area = entity.getArea();
        if (Area != null) {
            stmt.bindString(18, Area);
        }
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public UserEntity readEntity(Cursor cursor, int offset) {
        UserEntity entity = new UserEntity( //
            cursor.getInt(offset + 0), // Id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // Username
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // ShopName
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // BusinessActivities
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // Logo
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // Introduction
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // Contacts
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // ContactsTel
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // shopTypeName
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // Token
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // Accid
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // suppFxUrl
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // suppType
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // ContactsQQ
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // Address
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // Province
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // City
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17) // Area
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, UserEntity entity, int offset) {
        entity.setId(cursor.getInt(offset + 0));
        entity.setUsername(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setShopName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setBusinessActivities(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setLogo(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setIntroduction(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setContacts(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setContactsTel(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setShopTypeName(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setToken(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setAccid(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setSuppFxUrl(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setSuppType(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setContactsQQ(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setAddress(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setProvince(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setCity(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setArea(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(UserEntity entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(UserEntity entity) {
        return null;
    }

    @Override
    public boolean hasKey(UserEntity entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
