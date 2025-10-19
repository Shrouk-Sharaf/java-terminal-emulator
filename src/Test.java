// public class Test {
//     public static void main(String[] args) {
//         Parser parser = new Parser();
        
//         String[] testInputs = {
//             "ls",
//             "cd Documents",
//             "cp file1.txt file2.txt", 
//             "mkdir folder1 folder2 folder3",
//             "   touch  myfile.txt   ",  // Multiple spaces
//             "",                         // Empty input
//             "   "                       // Only spaces
//         };
        
//         System.out.println("=== Parser Testing ===\n");
        
//         for (String input : testInputs) {
//             System.out.println("Testing: '" + input + "'");
            
//             boolean success = parser.parse(input);
            
//             if (success) {
//                 System.out.println("Command: " + parser.getCommandName());
//                 System.out.println("Args: " + java.util.Arrays.toString(parser.getArgs()));
//             }
//             else {
//                 System.out.println("Failed to parse");
//             }
//             System.out.println();
//         }
//     }
// }