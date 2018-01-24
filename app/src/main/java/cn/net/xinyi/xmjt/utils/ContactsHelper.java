package cn.net.xinyi.xmjt.utils;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.text.format.DateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cn.net.xinyi.xmjt.config.AppConfig;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.model.PoliceContacts;
import cn.net.xinyi.xmjt.utils.DB.DBOperation;

/**
 * Created by mazhongwang on 15/5/20.
 */
public class ContactsHelper {

    //更新本地通讯录数据库；
    public static  void updateLocalContacts(Context mContext, List<PoliceContacts> allContacts) {
        DBOperation dbo = new DBOperation(mContext);
        long result = dbo.insertContacts(allContacts);
        if (result == allContacts.size()) {
            String now = DateFormat.format("yyyyMMdd_hhmmss",
                    Calendar.getInstance(Locale.CHINA)).toString();
            AppContext.instance.setProperty(AppConfig.CONTACTS_UPDATE_TIME, now);
        }
    }

    public static List<PoliceContacts> getLocalContacts(Context mContext){
        DBOperation dbo = new DBOperation(mContext);
        return dbo.getContacts();
    }

    public static List<PoliceContacts> getLocalContactsByOrgans(Context mContext,ArrayList<String> organs){
        DBOperation dbo = new DBOperation(mContext);
        return dbo.getContactsByOrgans(organs);
    }

    public static List<PoliceContacts> getLocalContactsByNumber(Context mContext,String number){
        DBOperation dbo = new DBOperation(mContext);
        return dbo.getContactsByNumber(number);
    }

    //更新系统电话本
    public static int insertToSysContacts(Context mContext, List<PoliceContacts> mContacts) {

        if (mContacts == null || mContacts.size() == 0) {
//            DBOperation dbo = new DBOperation(mContext);
//            mContacts = dbo.getContacts();
            return 0;
        }

        int rawContactInsertIndex = 0;
        int count =0;
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        for (int i = 0; i < mContacts.size(); i++) {
            PoliceContacts mContact = mContacts.get(i);
            String name = mContact.getName();
            String mobileno = mContact.getMobileno();
            String email = "";
            String polno = "警号" + mContact.getPolno();
            String dept = mContact.getOrgan();
            String telno = mContact.getTelno();
            String title = mContact.getTitle();

            //判断联系人是否存在
            if (!findContacts(mContext, mContact)) {
                count ++;

                rawContactInsertIndex = ops.size();
                ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                        .withYieldAllowed(true)
                        .build());

                //姓名
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
                        .withYieldAllowed(true)
                        .build());

                //警号（用作昵称）
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Nickname.NAME, polno).withValue(ContactsContract.CommonDataKinds.Nickname.TYPE, ContactsContract.CommonDataKinds.Nickname.TYPE_CUSTOM)
                        .withYieldAllowed(true)
                        .build());

                //移动电话
                if (mobileno != null && !mobileno.isEmpty()) {
                    String[] mobilenos = mobileno.split("/");
                    for (String no : mobilenos) {
                        if (StringUtils.toLong(no) != 0) {//判断是否为电话号码
                            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, no)
                                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                                    .withValue(ContactsContract.CommonDataKinds.Phone.LABEL, "手机号")
                                    .withYieldAllowed(true)
                                    .build());
                        }
                    }
                }

                //固定电话
                if (telno != null && !telno.isEmpty()) {
                    String[] telnos = telno.split("/");
                    for (int j = 0; j < telnos.length; j++) {
                        if (StringUtils.toLong(telnos[j]) != 0) {//判断是否为电话号码
                            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, telnos[j])
                                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_TELEX)
                                    .withValue(ContactsContract.CommonDataKinds.Phone.LABEL, "座机号")
                                    .withYieldAllowed(true)
                                    .build());
                        }
                    }
                }

                //部门
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, dept)
                        .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                        .withYieldAllowed(true)
                        .build());

                //职位
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, title)
                        .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                        .withYieldAllowed(true)
                        .build());

                //email
//            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
//                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
//                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
//                    .withValue(ContactsContract.CommonDataKinds.Email.DATA, email)
//                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
//                    .withYieldAllowed(true)
//                    .build());

            }
        }

        if (ops != null) {
            ContentProviderResult[] results;
            try {
                results = mContext.getApplicationContext().getContentResolver()
                        .applyBatch(ContactsContract.AUTHORITY, ops);
            } catch (RemoteException e) {
                count = -1;
                e.printStackTrace();
            } catch (OperationApplicationException e) {
                count = -1;
                e.printStackTrace();
            }
        }
        return count;
    }

    public static boolean findContacts(Context mContext, PoliceContacts mContact){
        boolean flag = false;
        String name = mContact.getName();
        String mobileno = mContact.getMobileno();
        String email = "";
        String polno = "警号" + mContact.getPolno();
        String dept = mContact.getOrgan();
        String telno = mContact.getTelno();
        String title = mContact.getTitle();

        Uri uri = Uri.parse("content://com.android.contacts/data/");
        String where = "mimetype = 'vnd.android.cursor.item/nickname' and data1='"+polno+"'";
        ContentResolver resolver = mContext.getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{ContactsContract.Data.DISPLAY_NAME}, where, null, null);
        if (cursor.moveToFirst()) {
                flag = true;
        }
        cursor.close();
        return flag;
    }

    //根据电话号码获取电话本联系人姓名
    public static ArrayList<String> getSysContactsByNumber(Context mContext, String number){
        ArrayList<String> contacts = new ArrayList<String>();
        Uri uri = Uri.parse("content://com.android.contacts/data/phones/filter/"+number);
        ContentResolver resolver = mContext.getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{ContactsContract.Data.DISPLAY_NAME}, null, null, null); //从raw_contact表中返回display_name
        if(cursor.moveToFirst()){
            contacts.add(cursor.getString(0));
        }
        cursor.close();
        return contacts;
    }

    //根据电话号码获取民警通联系人姓名
    public static ArrayList<String> getPoliceContactsByNumber(Context mContext, String number){
        ArrayList<String> contacts = new ArrayList<String>();
        DBOperation dbo = new DBOperation(mContext);
        List<PoliceContacts> policeContactses = dbo.getContactsByNumber(number);
        if(policeContactses!=null && policeContactses.size()>0){
            for(PoliceContacts policeContact : policeContactses){
                contacts.add(policeContact.getName());
            }
        }
        return contacts;
    }
}
