package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Stage implements Dumpable{
    @Override
    public void dump() {

    }
    public static final File Stage_DIR=Utils.join(Repository.GITLET_DIR,"\\StageArea");

    private Map<String,String> blobsMap;

    public Map<String,String> getBlobsMap() {
        return blobsMap;
    }
    public Stage()
    {
      blobsMap=new HashMap<>();
    }
   public void add(String file,String blob)
   {
       blobsMap.put(file,blob);
   }



}
