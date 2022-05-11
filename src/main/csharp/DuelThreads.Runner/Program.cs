using System.Diagnostics;

using static System.String;
using static System.Console;

namespace DuelThreads.Runner;

internal class Program
{
    // Dev path; will update when deployed
    private const string PathFormat = "../../../../DuelThreads.{0}/bin/Debug/net6.0/DuelThreads.{0}.exe";
    
    private enum Challenge { PureSelfManaged, PurePoolManaged, Async, Lightweight }

    private static Dictionary<Challenge, string> PathMap { get; } = new()
    {
        {Challenge.PureSelfManaged, Format(PathFormat, nameof(Challenge.PureSelfManaged))},
        {Challenge.PurePoolManaged, Format(PathFormat, nameof(Challenge.PurePoolManaged))},
        {Challenge.Async, Format(PathFormat, nameof(Challenge.Async))},
        {Challenge.Lightweight, Format(PathFormat, nameof(Challenge.Lightweight))}
    };
    
    public static void Main(string[] args)
    {
        var (challenge, threads, path) = ParseArgs(args);
        WriteLine($"Runner running {challenge} challenge using {threads} threads.");
        var process = Process.Start(path, threads.ToString());
        process.WaitForExit();
        WriteLine("Runner completed.");
    }
    
    private static (Challenge, int, string) ParseArgs(string[] args)
    {
        int.TryParse(args[0], out var which);
        int.TryParse(args[1], out var threads);
        var challenge = (Challenge) which;
        return (challenge, threads, PathMap[challenge]);
    }
}