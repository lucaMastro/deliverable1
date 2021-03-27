import java.io.*;

public class FileManager {
    private String pathFile;
    private File file;

    public FileManager(String path){
        this.pathFile = path;
        this.file = new File(path);
    }

    public void writeToFile(String s) throws IOException {
        FileWriter fw = null;
        try {
            fw = new FileWriter(this.file);
            fw.append(s);
            fw.flush();
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fw != null)
                fw.close();
        }
    }

    public String readIthLineFromFile(int lineNumber) throws IOException {
        FileReader fr = new FileReader(this.file);
        BufferedReader br = new BufferedReader(fr);
        String line = null;
        int i = 0;
        try{
            while ((line = br.readLine()) != null) {
                i++;
                if (i == lineNumber)
                    break;
            }
        } catch (IOException e){
            e.printStackTrace();
        }finally {
            br.close();
            fr.close();
        }
        if (i == lineNumber)
            return line;
        else
            return null;
    }
}

