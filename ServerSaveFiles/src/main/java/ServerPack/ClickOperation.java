package ServerPack;

import java.io.File;
import java.util.ArrayList;

public class ClickOperation {

    public ArrayList<String> getArrays(File file) {
        ArrayList<String> list = new ArrayList<>();
        File[] files = file.listFiles();
        for (File doc : files) {
            if (doc == null) {
                continue;
            }
            if (doc.isFile()) {
                list.add(doc.getName() + " -> " + "[FILE]" + " -> " + doc.length() + " байт");
            } else {
                list.add(doc.getName() + " -> " + "[DIR]" + " -> " + doc.length() + " байт");
            }
        }
        return list;
    }
}
