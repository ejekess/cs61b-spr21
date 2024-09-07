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
            return;
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
                validateNumArgs(args[0],args,1);
                Repository.getStatus();

                break;





            case "rm":
                Repository.remove(args[1]);
                break;

            case  "reset":
                validateNumArgs(args[0],args,2);
                Repository.reset(args[1]);
                break;


            case "rm-branch":
                validateNumArgs(args[0],args,2);
                Repository.removeBranch(args[1]);
                break;

                case "checkout":

                    if(args.length==1)
                    {
                        throw Utils.error("Incorrect operands.","checkout");
                    }
                    if (args.length == 2) {
                        Repository.CheckOut3(args[1]);
                    }
                    else {
                        if (args[1].equals("--")) {
                            Repository.CheckOut1(args[2]);
                        } else if (args[2].equals("--")) {
                            Repository.CheckOut2(args[1], args[3]);
                        }
                        else
                        {
                            throw Utils.error("Incorrect operands.", "checkout");
                        }
                    }
                    break;


                    case  "branch":
                        validateNumArgs(args[0],args,2);
                     Repository.NewBranch(args[1]);
                    break;

                    case "merge":
                        validateNumArgs(args[0],args,2);
                        Repository.Merge(args[1]);
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
