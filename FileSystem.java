import java.util.*;

public class FileSystem {

    class Directory {
        //directoryMap with key as name of directory and value as its sub directories
        Map<String, Directory> directoryMap = new HashMap<>();
    }

    Directory root;
    Directory tempPointer;
    List<String> dirInPath = new ArrayList<>();

    public FileSystem() {
        root = new Directory();
    }

    public String pwd(){
        String path = "/";
        if(dirInPath.isEmpty()){
            return path;
        }
        else{
            for(int i=0;i<dirInPath.size();i++) {
                path = path + dirInPath.get(i) + "/";
            }
            return path.substring(0, path.length() - 1);
        }
    }

    public List<String>ls(){
        String path = pwd();
        tempPointer = root;
        if (!path.equals("/")) {
            //splitting the directory path based on / and getting the individual levels of directory names
            String[] arr = path.split("/");
            for (int i = 1; i < arr.length; i++) {
                //updating directory pointer to point to the child(sub directory) as we go deeper
                tempPointer = tempPointer.directoryMap.get(arr[i]);
            }
        }
        //obtain child list from directory map
        return new ArrayList<>(tempPointer.directoryMap.keySet());
    }

    public void cd(String path){
            if(path.charAt(0)!='/'){
                path = pwd().equalsIgnoreCase("/")? pwd()+path : pwd()+"/"+path;
            }
            String[] arr = path.split("/");
            tempPointer = root;
            for (int i = 1; i < arr.length; i++) {
                tempPointer = tempPointer.directoryMap.get(arr[i]);
                if(tempPointer==null){
                    System.out.println("ERR: INVALID PATH");
                    for(int j=i;j>=1;j--){
                        dirInPath.remove(arr[j]);
                    }
                    return;
                }
                dirInPath.add(arr[i]);
            }
            System.out.println("SUCC: REACHED");
    }

    public void mkdir(String path) {
        if(ls().contains(path)){
            System.out.println("ERR: DIRECTORY ALREADY EXISTS");
        }
        else{
            if(path.charAt(0)!='/'){
                path = pwd().equalsIgnoreCase("/")? pwd()+path : pwd()+"/"+path;
            }
            String[] arr = path.split("/");
            //dirInPath.clear();
            tempPointer = root;
            //run loop until we reach last directory level
            for (int i = 1; i < arr.length; i++) {
                //if dir doesn't exist, create new entry in the latest directoryMap
                if(!tempPointer.directoryMap.containsKey(arr[i])){
                    tempPointer.directoryMap.put(arr[i], new Directory());
                }
                //update tempPointer to dir in path
                tempPointer = tempPointer.directoryMap.get(arr[i]);
                //dirInPath.add(arr[i]);
            }
            System.out.println("SUCC: CREATED");
        }
    }

    public void rm(String path) {
        if(path.charAt(0)!='/'){
            path = pwd().equalsIgnoreCase("/")? pwd()+path : pwd()+"/"+path;
        }
        tempPointer = root;
        String[] arr = path.split("/");
        for (int i = 1; i < arr.length - 1; i++) {
            //updating tempPointer to point to the child
            tempPointer = tempPointer.directoryMap.get(arr[i]);
        }
        //delete entry of last level of directory from hash map
        tempPointer.directoryMap.remove(arr[arr.length - 1]);
        dirInPath.remove(dirInPath.get(dirInPath.size()-1));
        System.out.println("SUCC: DELETED");
    }

    public void sessionClear(){
        tempPointer = root;
        dirInPath.clear();
        System.out.println("SUCC: CLEARED: RESET TO ROOT");
    }

    public static void main(String args[]){

        FileSystem fs = new FileSystem();

        /*switch(args[0]){
            case "cd":
                fs.cd(args[1]);
                break;
            case "ls":
                System.out.println("DIRS: "+fs.ls());
                break;
            case "mkdir":
                fs.mkdir(args[1]);
                break;
            case "rm":
                fs.rm(args[1]);
                System.out.println("SUCC: DELETED");
                break;
            case "pwd":
                System.out.println("PATH: "+fs.pwd());
                break;
            case "session clear":
                fs.sessionClear();
                System.out.println("SUCC: CLEARED: RESET TO ROOT");
                break;
            default:
                System.out.println("ERR: CANNOT RECOGNIZE INPUT.");
        }*/


        System.out.println("PATH: "+fs.pwd());
        fs.cd("/X/Y");
        System.out.println("PATH: "+fs.pwd());
        fs.mkdir("dir1");
        fs.mkdir("dir1");
        fs.mkdir("dir2");
        System.out.println("DIRS: "+fs.ls());
        fs.cd("dir1");
        System.out.println("PATH: "+fs.pwd());
        fs.cd("/");
        fs.rm("/dir1");
        fs.cd("/dir1");
        fs.mkdir("/dir3");
        fs.cd("/dir2");
        fs.cd("/X/Y");
        System.out.println("PATH: "+fs.pwd());
        System.out.println("PATH: "+fs.pwd());
        fs.sessionClear();
        System.out.println("PATH: "+fs.pwd());

    }
}