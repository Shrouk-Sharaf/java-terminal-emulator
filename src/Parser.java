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