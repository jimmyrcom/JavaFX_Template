package com.company.util;

/* BSD license
Copyright (c) 2015 Jimmy Ruska (jimmyruska@gmail.com)
All rights reserved.

Redistribution and use in source and binary forms are permitted
provided that the above copyright notice and this paragraph are
duplicated in all such forms and that any documentation,
advertising materials, and other materials related to such
distribution and use acknowledge that the software was developed
by the <organization>. The name of the
<organization> may not be used to endorse or promote products derived
from this software without specific prior written permission.
THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;


public class StateMachine <T> {
    T continuation;


    public enum Key {
        ENTRIES, STATE, CLEAN, LIFT, BIND, TRANSFORM, NO_TIME, LOGGER, NAME, NO_PRINT
    }

    public static class Entry <T> {
        String what;
        Function<T,T> fun;
        public Entry(String what, Function<T,T> fun){
            this.what = what;
            this.fun = fun;
        }
    }

    public static class StateMachineReturn <T> {
        T value;
        public StateMachineReturn(T obj){
            value = obj;
        }
    };

    public static class FunList<T> extends ArrayList<StateMachine.Entry<T>>{
        public FunList(){
            super();
        }
    }

    final Long startTime = System.currentTimeMillis();
    Long endTime = null;
    Long runTime = null;
    public String name = "";
    public Function<Entry<T>,Function<T,T>> transform = (x) -> x.fun;
    public ArrayList<Entry<T>> compose = new ArrayList<>();
    public Function<T,T> lift = (x) -> x;
    public Function<T,T> bind = (x) -> x;
    public Entry<T> clean = new Entry<T>("", x -> x);
    public Function<T,T> apply = this::apply;
    public boolean trace = true;
    public Consumer<Object> print = (x) -> { if (this.trace) System.out.println(x); };

    public static <T> T apply(HashMap<StateMachine.Key, Object> map){
        StateMachine s = new StateMachine<T>();
        if (map.containsKey(Key.NO_TIME)) s.transform = (Function<Entry<T>,Function<T,T>>)x -> x.fun;
        if (map.containsKey(Key.NO_PRINT)) s.print = x -> {};
        if (map.containsKey(Key.TRANSFORM)) s.transform= (Function<Entry<T>,Function<T,T>>)map.get(Key.TRANSFORM);
        if (map.containsKey(Key.BIND)) s.bind= (Function<T,T>)map.get(Key.BIND);
        if (map.containsKey(Key.ENTRIES)) s.compose= (ArrayList<Entry<T>>)map.get(Key.ENTRIES);
        if (map.containsKey(Key.CLEAN)) s.clean = (Entry<T>)map.get(Key.CLEAN);
        if (map.containsKey(Key.LIFT)) s.lift = (Function<T,T>)map.get(Key.LIFT);
        if (map.containsKey(Key.LOGGER)) s.print = (Consumer<Object>)map.get(Key.LOGGER);
        if (map.containsKey(Key.NAME)) s.name = (String)map.get(Key.NAME);
        return (T)s.run(map.get(Key.STATE));
    }

    public StateMachine(){
        transform = this::timing;
    }

    public T run(T input){
        return this.apply.apply(input);
    }

    public T apply(T input){
        print.accept("++ "+name+" StateMachine start");
        for (Entry<T> f : compose){
            Function<T,T> f2 = transform.apply(f);
            input = bind.apply(f2.apply(lift.apply(input)));
            if (input instanceof StateMachine.StateMachineReturn) return input;
            if (input instanceof StateMachine){
                StateMachine s = (StateMachine) input;
                s.apply(s.continuation);
                break;
            }
        }
        endTime = System.currentTimeMillis();
        runTime = endTime - startTime;
        input = printTiming(clean.what, clean.fun, input);
        print.accept("-- "+name+" StateMachine finished after "+ printTiming(runTime));
        return input;
    }

    public T printTiming(String begin, Function<T,T> f, T x){
        Long start = System.currentTimeMillis();
        print.accept("+++ " +begin);
        T out = f.apply(x);
        print.accept("--- Completed after " + printTiming(System.currentTimeMillis() - start) + "\n");
        return out;
    }

    public Function<T,T> timing(Entry<T> f){
        Function<T,T> fn = (x) -> {
            Long start = System.currentTimeMillis();
            try {
                return printTiming(f.what, f.fun, x);
            } catch (Exception e){
                x=printTiming(clean.what, clean.fun, x);
                e.printStackTrace();
                return x;
            }
        };
        return fn;
    }

    public static class TimeState{
        String acc = "";
        long millis = 0;
        public TimeState(long millis1, String acc1){
            acc = acc1;
            millis = millis1;
        }
    }
    public static String printTiming(long millis){
        TimeState x = new StateMachine.TimeState(millis,"");
        printTiming("day", 1000*60*60*24, x);
        printTiming("hour", 1000*60*60, x);
        printTiming("minute", 1000*60, x);
        printTiming("second", 1000, x);
        printTiming("millisecond", 1, x);
        if ("".equals(x.acc)) x.acc="0 seconds";
        return x.acc;
    }
    public static void printTiming(String what, long time, StateMachine.TimeState x){
        long interval = x.millis / (time);
        if (interval == 1) x.acc += interval + " "+what+" ";
        else if (interval > 0) x.acc += interval + " "+what+"s ";
        x.millis -= interval * (time);
    }

    public static <T> T MatcherOnce(List<Object> l, T target){
        if ((l.size() % 2) != 0) throw new Error("Must be even number of match -> run");
        for (int x =0 ; x < l.size(); x+=2){
            Predicate<T> test = (Predicate<T>)l.get(x);
            Function<T,T> f = (Function<T,T>)l.get(x);
            try {
                if (test.test(target)) return f.apply(target);
            } catch(Exception e){}
        }
        return target;
    }

    public static <T> T MatcherMany(List<Object> l, T target){
        if ((l.size() % 2) != 0) throw new Error("Must be even number of match -> run");
        for (int x =0 ; x < l.size(); x+=2){
            Predicate<T> test = (Predicate<T>)l.get(x);
            Function<T,T> f = (Function<T,T>)l.get(x);
            try {
                if (test.test(target)) target = f.apply(target);
                if (target instanceof StateMachineReturn) return (T)((StateMachineReturn)target).value;
            } catch(Exception e){}
        }
        return target;
    }
}