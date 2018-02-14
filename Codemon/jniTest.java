import java.io.*;

public class jniTest
{

//get c lib
static { System.loadLibrary("codemon");}

//define c methods
native int compile(String file);
native int test(String file, int limit);
native int selfTest(String file1, String file2, int limit);
native int pvp(int players, String file);
native int getReport(String reportID);

//main
public static void main(String [] args)
{
    jniTest t = new jniTest();

    if(args.length == 2 && args[0].equals("-c"))
    {
        t.compile(args[1]);
    }
    else if(args.length == 3 && args[0].equals("-t"))
    {
        t.test(args[1], Integer.parseInt(args[2]));
    }
    else if(args.length == 4 && args[0].equals("-s"))
    {
        t.selfTest(args[1], args[2], Integer.parseInt(args[3]));
    }
    else if(args.length == 3 &&  args[0].equals("-p"))
    {
        t.pvp(Integer.parseInt(args[1]), args[2]);
    }
    else if(args.length == 2 && args[0].equals("-r"))
    {
        t.getReport(args[1]);
    }
    else
    {
        System.out.println("Error: Bad cmd line args");
    }

    //test.compile("kraken");
}


}
