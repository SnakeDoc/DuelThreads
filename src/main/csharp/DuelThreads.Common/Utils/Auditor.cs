using System.Diagnostics;

namespace DuelThreads.Common.Utils;

public static class Auditor
{
    public static double Audit(Action action)
    {
        var watch = new Stopwatch();
        watch.Start();
        try
        {
            action();
        }
        catch (Exception e)
        {
            Console.WriteLine(e.Message);
        }
        watch.Stop();
        return watch.Elapsed.TotalSeconds;
    }
}