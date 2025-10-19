import java.util.Arrays;

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

    public void execute(Parser parser) 
    {
        String command = parser.getCommandName();
        String[] args = parser.getArgs();

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
