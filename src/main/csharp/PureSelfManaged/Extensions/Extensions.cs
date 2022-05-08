﻿namespace PureSelfManaged.Extensions;

public static class Extensions
{
    public static IEnumerable<T> Tap<T>(this IEnumerable<T> ts, Action action)
    {
        foreach (var t in ts)
        {
            action();
            yield return t;
        }
    }
    
    public static IEnumerable<T> Tap<T>(this IEnumerable<T> ts, Action<T> action)
    {
        foreach (var t in ts)
        {
            action(t);
            yield return t;
        }
    }
}