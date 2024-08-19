package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.IOException;
import java.util.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit  implements Dumpable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */

    public static final File COMMIT_DIR=Utils.join(Repository.GITLET_DIR,"\\Objects\\Commits");
    private String message;
    private String timestamp;//format:Thu Nov 9 17:01:33 2017 -0800



    String UID;

    public List<String> getBlobsUID() {
        return BlobsUID;
    }

    List<String> parentUID;//the most last commit,use the file name
     List<String> BlobsUID;

    /* TODO: fill in the rest of this class. */

    public Commit(String message, List<String> parentUID) {
        this.message = message;
        if(parentUID==null)
        {
            timestamp="Thu Jan 1 00:00:00 1970 -0800";
        }
        else
        {
            // 获取当前时间
            Date date = new Date();

            // 创建Formatter对象
            StringBuilder sb = new StringBuilder();
            Formatter formatter = new Formatter(sb, Locale.CHINA);

            // 格式化日期
            // 注意：%tB 输出完整月份名称，%tb 输出缩写月份名称
            // %tA 输出完整星期名称，%ta 输出缩写星期名称
            // %td 表示一个月中的某天（01-31），%tH（00-23），%tM（00-59），%tS（00-61），%tY（年份）
            // %tz 表示时区
            formatter.format("%ta %tb %td %tH:%tM:%tS %tY  %tz", date, date, date, date, date, date,date,date);

            timestamp=sb.toString();
        }
        this.parentUID=parentUID;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {

        return timestamp;

    }

    public List<String> getParentUID() {
        return parentUID;
    }

    public void setBlobsUID(Stage stage) {
        BlobsUID=new ArrayList<>();
        File file;
        for (Blob blob : stage.getBlobs()) {
            BlobsUID.add(blob.getBlobUID());

            file=Utils.join(Blob.BLOB_DIR,(blob.getBlobUID()+".txt"));
            try {
                file.createNewFile();
                Utils.writeObject(file,blob);
            } catch (IOException e) {
                throw Utils.error("Can't save Blobs","commit");
            }
        }
    }




    public String getUID() {
        return UID;
    }

    @Override
    public void dump() {
        return;
    }
}
