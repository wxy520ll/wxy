package cn.net.xinyi.xmjt.model;

/**
 * Created by mazhongwang on 15/5/22.
 */
public class IndexedContacts {
        public String name;
        public String mobileno;
        public char sortKey;
        public boolean isHead;
        @Override
        public String toString() {
                return "Contacts [name=" + name + ",sortkey=" + sortKey
                        + ",isHead=" + isHead + ", mobileno=" + mobileno + "]";
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getMobileno() {
                return mobileno;
        }

        public void setMobileno(String mobileno) {
                this.mobileno = mobileno;
        }

        public char getSortKey() {
                return sortKey;
        }

        public void setSortKey(char sortKey) {
                this.sortKey = sortKey;
        }

        public boolean isHead() {
                return isHead;
        }

        public void setHead(boolean isHead) {
                this.isHead = isHead;
        }
}