import java.util.Arrays;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.IOException;
import java.util.Scanner;

class Parser {
    String commandName;
    String[] args;
    String redirectOperator; 
    String redirectFile;     
    
    public boolean parse(String input) {
    commandName = null;
    args = new String[0];
    redirectOperator = null;
    redirectFile = null;
    if (input == null || input.trim().isEmpty()) {
        return false;
    }
    String[] parts = input.trim().split("\\s+");
    
    int redirectIndex = -1;
    for (int i = 0; i < parts.length; i++) {
        if (parts[i].equals(">") || parts[i].equals(">>")) {
            redirectOperator = parts[i];
            if (i + 1 < parts.length) {
                redirectFile = parts[i + 1];
                redirectIndex = i;
            }
            else {
                System.out.println("Error: Redirection operator '" + parts[i] + "' requires a filename");
                return false;
            }
            break;
        }
    }
    
    if (redirectIndex != -1) {
        commandName = parts[0];
        args = Arrays.copyOfRange(parts, 1, redirectIndex);
    } 
    else {
        commandName = parts[0];
        args = parts.length > 1 ? Arrays.copyOfRange(parts, 1, parts.length) : new String[0];
    }

    return commandName != null && !commandName.isEmpty();
}

    public String getRedirectOperator() {
        return redirectOperator;
    }
    
    public String getRedirectFile() {
        return redirectFile;
    }
    
    public boolean hasRedirection() {
        return redirectOperator != null;
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

    public Terminal() {
        myDirectory = new File(System.getProperty("user.dir"));
    }
    
    public void pwd() {
        System.out.println(myDirectory.getAbsolutePath());
    }

    public void cd(String... args) 
    {
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
                newDir = new File(path);
                if (!newDir.isAbsolute()) 
                {
                    newDir = new File(myDirectory, path);
                }
            }
            if (newDir!=null&&newDir.exists()&&newDir.isDirectory()) 
            {
                myDirectory = newDir;
            } 
            else 
            {
                System.out.println("cd: no such directory: " + path);
            }
        }
    }
    public void ls() 
    {
        File[] files = myDirectory.listFiles();
        if (files==null || files.length==0) 
        {
            System.out.println("There are no files to list in this directory.");
            return;
        } 
        Arrays.sort(files,(a, b)->a.getName().compareToIgnoreCase(b.getName()));
        for (File file : files) 
        {
            System.out.println(file.getName());
        }
        
    }

    public void mkdir(String...args) 
    {
        if (args.length ==0) 
        {
            System.out.println("mkdir: missing operand");
            return;
        }
        for (String dirName:args) {
            File newDir = new File(myDirectory, dirName);
            if (newDir.exists()) 
            {
                System.out.println("mkdir: cannot create directory '" + dirName + "' since it already exists");
            } 
            else if (newDir.mkdirs()) 
            {
                System.out.println("Directory created: " + newDir.getAbsolutePath());
            } 
            else 
            {
                System.out.println("mkdir: failed to create directory '" + dirName + "'");
            }
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

    public void cp(String arg1, String arg2) {
    try {
        String sourcePath = resolvePath(arg1);
        String destinationPath = resolvePath(arg2);
        
        File sourceFile = new File(sourcePath);
        
        if (!sourceFile.exists()) {
            System.out.println("Source file does not exist: " + sourcePath);
            return;
        }
        FileInputStream inputStream = new FileInputStream(sourcePath);
        FileOutputStream outputStream = new FileOutputStream(destinationPath);
        
      for (int x = inputStream.read(); x != -1; x = inputStream.read()) {
            outputStream.write(x);
        }
        inputStream.close();
        outputStream.close();
        System.out.println("File copied successfully!");
    } catch (IOException e) {
        System.out.println("Error copying file: " + e.getMessage());
    }
}

    public void cp_r(String arg1, String arg2) {
   try{
     String sourcePath = resolvePath(arg1);
     String destinationPath = resolvePath(arg2);
     if(new File(sourcePath).isFile()){
        cp(arg1,arg2);
    }
     else
     {
        File fileDest = new File(destinationPath);
        if (!fileDest.exists()) {
        fileDest.mkdirs();
        }
        if (new File(sourcePath).listFiles() != null) {
             for (File file : new File(sourcePath).listFiles()) {
            if (file.isDirectory()) {
                cp_r(file.getAbsolutePath(), new File(destinationPath, file.getName()).getAbsolutePath());
            } else {
                cp(file.getAbsolutePath(), new File(destinationPath, file.getName()).getAbsolutePath());
            }
        }
        }
        else{
            System.out.println("Directory is empty.");
        }
    }
   }
   catch(Exception e){
       System.out.println("Error copying file: " + e.getMessage());
   }

}

    public void cat(String... args) {
    for (String filename : args) {
        String path = resolvePath(filename);
        File file = new File(path);

        if (file.exists() && file.isFile()) 
        {
            try (Scanner scanner = new Scanner(file, "UTF-8")) 
            { 
                while (scanner.hasNextLine()) 
                {
                    System.out.println(scanner.nextLine());
                }
                System.out.flush(); 
            } 
            catch (IOException e)
             {
                System.out.println("Error reading file " + filename + ": " + e.getMessage());
            }
        } 
        else {
            System.out.println("File not found: " + path);
        }
    }
}

    private void executeWithRedirection(Runnable command, Parser parser) {
    if (parser.hasRedirection()) {
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        try (PrintStream collectingStream = new PrintStream(baos, true, "UTF-8")) {
            PrintStream originalOut = System.out;
            System.setOut(collectingStream);

            command.run();
            System.out.flush();
            System.setOut(originalOut);

            String output = baos.toString("UTF-8");
            
            System.out.print(output);

            boolean append = ">>".equals(parser.getRedirectOperator());
            try (FileWriter writer = new FileWriter(resolvePath(parser.getRedirectFile()), append)) {
                writer.write(output);
            }

        } catch (IOException e) {
            System.out.println("Error in redirection: " + e.getMessage());
        }
    } else {
        command.run();
    }
}

    public void execute(Parser parser) {
        String command = parser.getCommandName();
        String[] args = parser.getArgs();
        System.setProperty("user.dir", myDirectory.getAbsolutePath());

        switch (command) 
        {
            case "pwd":
                executeWithRedirection(() -> pwd(), parser);
                break;
            case "cd":
                cd(args);
                break;
            case "ls":
                executeWithRedirection(() -> ls(), parser);
                break;
            case "mkdir":
                mkdir(args);
                break;
            case "touch":
                if (args.length > 0) {
                    touch(args[0]);
                } else {
                    System.out.println("touch: missing file operand");
                }
                break;
            case "rmdir":
                if (args.length > 0) {
                    rmdir(args[0]);
                } else {
                    System.out.println("rmdir: missing operand");
                }
                break;
            case "rm":
                if (args.length > 0) {
                    rm(args[0]);
                } else {
                    System.out.println("rm: missing operand");
                }
                break;
            case "cp":
                if (args.length > 0 && args[0].equals("-r")) {
                    if (args.length >= 3) {
                        cp_r(args[1], args[2]);
                    } else {
                        System.out.println("cp -r: missing source or destination");
                    }
                } else {
                    if (args.length >= 2) {
                        cp(args[0], args[1]);
                    } else {
                        System.out.println("cp: missing source or destination");
                    }
                }
                break;
            case "cat":
                executeWithRedirection(() -> cat(args), parser);
                break;
            case "exit":
                System.out.println("Exiting...");
                System.exit(0);
                break;
            default:
                System.out.println("Unknown command: " + command);
        }
    }
}

public class App{
    public static void main(String[] args)  {
        Parser parser = new Parser();
        Terminal terminal = new Terminal();
        Scanner input = new Scanner(System.in);

        System.out.println("Welcome to our Terminal! Type 'exit' to quit.");

        while (true) {
            System.out.print(terminal.myDirectory.getAbsolutePath() + " $ ");
            String line = input.nextLine().trim();

            if (line.equals("exit")) {
                System.out.println("Exiting...");
                break;
            }

            if (!line.isEmpty() && parser.parse(line)) {
                terminal.execute(parser);
            }
        }
        input.close();
    }
}
