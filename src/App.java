import java.util.Arrays;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

class Parser {
    String commandName;
    String[] args;  
    
    public boolean parse(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        
        String[] parts = input.trim().split("\\s+");
        commandName = parts[0];
        
        if (parts.length > 1) {
            args = Arrays.copyOfRange(parts, 1, parts.length);
        }
        else {
            args = new String[0];  
        }
        
        return true;
    }
    
    public String getCommandName() {
        return commandName;
    }
    
    public String[] getArgs() {
        return args;  
    }
}
class Terminal {
    public File myDirectory;

    public Terminal() 
    {
        myDirectory = new File(System.getProperty("user.dir"));
    }
    public void pwd() 
    {
        System.out.println(myDirectory.getAbsolutePath());
    }

    public void cd(String... args) {
        if (args.length == 0) 
        {
            myDirectory = new File(System.getProperty("user.home"));
            

        } 
        else 
        {
            String path = args[0];
            File newDir;
            if (path.equals("..")) 
            {
                newDir = myDirectory.getParentFile();
            } 
            else if (path.equals(".")) 
            {
                newDir = myDirectory;
            } 
            else 
            {
                newDir = new File(myDirectory,path);
            }
            if (newDir!=null&&newDir.exists()&&newDir.isDirectory()) 
            {
                myDirectory = newDir;
                System.out.println("Changed directory to: " + myDirectory.getAbsolutePath());

            } 
            else 
            {
                System.out.println("cd: no such directory: " + path);
            }
        }
    }
    public void ls() {
        File[] files = myDirectory.listFiles();
        if (files.length == 0) 
        {
            System.out.println("There are no files to list in this directory.");
        } 
        else 
        {
            for (File file : files) {
                System.out.println(file.getName());
            }
        }
    }

    public void mkdir(String...args) 
    {
        if (args.length == 0) 
        {
            System.out.println("mkdir: missing operand");
            return;
        }
        File newDir = new File(myDirectory,args[0]);
        if (newDir.exists()) 
        {
            System.out.println("mkdir: cannot create directory '" +args[0]+ "' since file already exists");
        } 
        else if (newDir.mkdirs()) 
        {
            System.out.println("Directory created: "+newDir.getAbsolutePath());
        } 
        else 
        {
            System.out.println("mkdir: failed to create directory");
        }
    }


    public  String resolvePath(String arg) {
    File file = new File(arg);
    if (file.isAbsolute()) {
        return file.getAbsolutePath(); 
    } else {
        File resolved = new File(myDirectory, arg);
        return resolved.getAbsolutePath();
    }
}
  public void touch(String arg){
      String path = resolvePath(arg);
       System.out.println("Creating file at: " + path);
      File newFile = new File(path);
      if(newFile.exists()){
         System.out.println("File already exist in  "+path);
      }
      else {
      try {
        boolean created = newFile.createNewFile();
        if (created) {
            System.out.println("File created successfully!");
        } else {
            System.out.println("File already exists!");
        }
    } catch (IOException e) {
        System.out.println("Error creating file: " + e.getMessage());
    }
}
  }
  public void rmdir(String arg){
    
    
    if(arg.equals("*")){
        File files[] = myDirectory.listFiles();
        if(files != null){
            for(File f : files){
                if(f.isDirectory() && f.listFiles().length == 0){
                    boolean deleted = f.delete();
                    if(deleted){
                        System.out.println("Deleted empty directory: " + f.getName());
                    }
                }
            }
        }
    } 
    else { 
        String path = resolvePath(arg);
        File dir = new File(path);
        if (dir.exists() && dir.isDirectory()) {
            File[] contents = dir.listFiles();
            if (contents != null && contents.length == 0) {
                boolean deleted = dir.delete();
                if (deleted)
                    System.out.println("Directory deleted: " + dir.getAbsolutePath());
                else
                    System.out.println("Could not delete directory.");
            } else {
                System.out.println("Directory is not empty.");
            }
        } else {
            System.out.println("Directory not found.");
        }
    }
}

  public void rm(String arg) {
    String path = resolvePath(arg);
    File file = new File(path);


    if (!file.exists()) {
        System.out.println("File not found: " + path);
        return;
    }


    if (file.isFile()) {
        boolean deleted = file.delete();
        if (deleted) {
            System.out.println("File deleted successfully: " + path);
        } else {
            System.out.println("Failed to delete file: " + path);
        }
    } else {
        System.out.println("Error: '" + path + "' is not a file.");
    }
}

  
    
    public void execute(Parser parser) 
    {
        String command = parser.getCommandName();
        String[] args = parser.getArgs();
        System.setProperty("user.dir", myDirectory.getAbsolutePath());


        switch (command) 
        {
            case "pwd":
                pwd();
                break;
            case "cd":
                cd(args);
                break;
            case "ls":
                ls();
                break;
            case "mkdir":
                mkdir(args);
                break;
            case "touch":
                touch(args[0]);
                break;
            case "rmdir":
                rmdir(args[0]);
                break;
            case "rm":
                 rm(args[0]);
                 break;
            default:
                System.out.println("Unknown command: " + command);
        }
    }

    
}
public class App{
    public static void main(String[] args)  {
        Parser parser =new Parser();
        Terminal terminal =new Terminal();
        Scanner input =new Scanner(System.in);

        while (true) {
            System.out.print(terminal.myDirectory.getAbsolutePath() + " $ ");
            String line = input.nextLine();

            if (line.equals("exit")) 
                break;

            if (parser.parse(line)) 
            {
                terminal.execute(parser);
            }
        }
        input.close();
    }
}    
