using static System.Console;
using static System.Linq.Enumerable;

using static DuelThreads.Common.Utils.Auditor;

namespace DuelThreads.PureSelfManaged;

internal static class Program
{
    private const string Format = "{0} | {1}";
    private const string AuditStartMsg = "Starting {0}...";
    private const string AuditFinishMsg = "{0} completed in {1} seconds..";
    private const int Total = 10_000; // 10_000_000
    
    private static readonly Random Random = new(DateTime.Now.Millisecond);
    private static readonly SemaphoreSlim ParentSem = new(1, 1);
    private static readonly SemaphoreSlim ChildSem = new(0, int.MaxValue);
    
    public static void Main()
    {
        // var baseLine = RunBaseLine();
        RunSequentialJoin();
    }

    private static double RunBaseLine() => Audit(() =>
        GetRange()
            .ToList()
            .ForEach(Add));

    private static void RunSequentialJoin()
    {
        const string name = nameof(RunSequentialJoin);
        WriteLine(AuditStartMsg, name);
        var runtime = Audit(() =>
        {
            List<Thread> threads = new();
            foreach(var i in GetRange())
            {
                ParentSem.Wait();
                var thread = new Thread(() => Add(i));
                threads.Add(thread);
                thread.Start();
            }

            ParentSem.Wait();
            ChildSem.Release(threads.Count);
            // It's not necessary to join foreground threads...
            // the main thread will await them all.
            //threads.ForEach(thread => thread.Join());
        });
        WriteLine(AuditFinishMsg, name, runtime);
    } 

    private static IEnumerable<int> GetRange() => Range(0, Total);

    private static IEnumerable<Thread> GetThreads() => GetRange()
        .Select(i => new Thread(() => Add(i)));

    private static void Add(int thread)
    {
        WriteLine(Format, thread, Random.Next() * Random.Next());
        ParentSem.Release();
        ChildSem.Wait();
    }
}
