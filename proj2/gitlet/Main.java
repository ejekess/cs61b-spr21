package gitlet;

import java.util.Arrays;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
    if(args.length==0)
        {
            System.out.println("Please enter a command.");
        }
        String firstArg = args[0];
	//Repository.add("Hello.txt");
        /* * If a user inputs a command with the wrong number or format of operands,
         * print the message and exit.Incorrect operands.
         */

   switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                validateNumArgs("init",args,1);
                Repository.InitRepository();
                break;
            case "add":

                // TODO: handle the `add [filename]` command
                validateNumArgs("add",args,2);
                Repository.add(args[1]);
                break;

            case "commit":

                validateNumArgs("add",args,2);
                Repository.Commit(args[1]);
                break;

            case "log":
               validateNumArgs("log",args,1);
               Repository.Log();
               break;


            case "status":


                break;





            case "rm":
                Repository.remove(args[1]);
                break;


            default:
                System.out.println("No command with that name exists.");
        }
    }



    public static void validateNumArgs(String cmd, String[] args, int n) {

        if(cmd.equals("commit")&&args.length<2)
        {
           throw Utils.error("Please enter a commit message.",cmd);
        }
        if (args.length != n) {
            throw Utils.error("Incorrect operands.", cmd);
        }

    }
}
