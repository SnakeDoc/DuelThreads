using System.Diagnostics;

using static System.String;
using static System.Console;

namespace DuelThreads.Runner;

internal class Program
{
    // Dev path; will update when deployed
    private const string PathFormat = "../../../../DuelThreads.{0}/bin/Debug/net6.0/DuelThreads.{0}.exe";
    
    private enum Challenge { PureSelfManaged, PurePoolManaged, Async, Lightweight }

    private record Arguments(Challenge Challenge, int Threads)
    {
        internal static Arguments FromCliArguments(IReadOnlyList<string> args)
        {
            int.TryParse(args[0], out var challenge);
            int.TryParse(args[1], out var threads);
            return new Arguments((Challenge) challenge, threads);
        }
    }

    private static Dictionary<Challenge, string> PathMap { get; } = new()
    {
        {Challenge.PureSelfManaged, Format(PathFormat, nameof(Challenge.PureSelfManaged))},
        {Challenge.PurePoolManaged, Format(PathFormat, nameof(Challenge.PurePoolManaged))},
        {Challenge.Async, Format(PathFormat, nameof(Challenge.Async))},
        {Challenge.Lightweight, Format(PathFormat, nameof(Challenge.Lightweight))}
    };
    
    public static void Main(string[] args)
    {
        var arguments = Arguments.FromCliArguments(args);
        var exePath = PathMap[arguments.Challenge];
        WriteLine($"Runner running {arguments.Challenge} challenge using {arguments.Threads} threads.");
        var process = Process.Start(exePath, arguments.Threads.ToString());
        //process.StandardOutput.ReadToEnd();
        process.WaitForExit();
        WriteLine("Runner completed.");
    }
}

