package cn.net.xinyi.xmjt.model;

public class FileInfoModle {

    public String fileName;

    public String filePath;

    public long fileSize;

    public boolean isDir;

    public int count;

    public long modifiedDate;

    public boolean selected;

    public boolean canRead;

    public boolean canWrite;

    public boolean isHidden;

    public long dbId; // id in the database, if is from database
}