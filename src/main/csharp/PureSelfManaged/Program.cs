using System.Diagnostics;

using static System.Console;
using static System.Linq.Enumerable;

namespace PureSelfManaged;

internal static class Program
{
    private const string Format = "{0} | {1}";
    private const string AuditStartMsg = "Starting {0}...";
    private const string AuditFinishMsg = "{0} completed in {1} seconds..";
    private const int Total = 5_000; // 10_000_000
    
    private static readonly Random Random = new(DateTime.Now.Millisecond);
    private static readonly SemaphoreSlim ParentSem = new(1, 1);
    private static readonly SemaphoreSlim ChildSem = new(0, int.MaxValue);
    
    public static void Main()
    {
        // var baseLine = RunBaseLine();
        RunSequentialJoin();
    }

    private static double RunBaseLine() => Audit(() => GetRange()
        .ToList()
        .ForEach(_ => Add()));

    private static void RunSequentialJoin()
    {
        WriteLine(AuditStartMsg, nameof(RunSequentialJoin));
        var runtime = Audit(() =>
        {
            List<Thread> threads = new();
            for (var i = 0; i < Total; ++i)
            {
                ParentSem.Wait();
                var thread = new Thread(Add)
                {
                    Name = i.ToString()
                };
                threads.Add(thread);
                thread.Start();
            }

            ChildSem.Release(threads.Count);
            threads.ForEach(thread => thread.Join());
        });
        WriteLine(AuditFinishMsg, nameof(RunSequentialJoin), runtime);
    } 

    private static IEnumerable<int> GetRange() => Range(0, Total);

    private static IEnumerable<Thread> GetThreads() => GetRange().Select(_ => new Thread(Add));

    private static double Audit(Action action)
    {
        var watch = new Stopwatch();
        watch.Start();
        try
        {
            action();
        }
        catch (Exception e)
        {
            WriteLine(e.Message);
        }
        watch.Stop();
        return watch.Elapsed.TotalSeconds;
    }

    private static void Add()
    {
        WriteLine(Format, Thread.CurrentThread.Name, Random.Next() * Random.Next());
        ParentSem.Release();
        ChildSem.Wait();
    }
}
