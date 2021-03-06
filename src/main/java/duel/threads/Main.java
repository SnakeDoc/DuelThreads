package duel.threads;

import duel.threads.test.impl.ChallengeOneAsyncTest;
import duel.threads.test.impl.ChallengeOnePoolManagedTest;
import duel.threads.test.impl.ChallengeOneSelfManagedTest;
import duel.threads.test.impl.ChallengeThreeAsyncTest;

import java.util.concurrent.ThreadFactory;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        final Arguments arguments = new Arguments();
        try {
            arguments.parseArguments(args);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            help();
            return;
        }

        final ThreadFactory threadFactory = (arguments.getThreadType() == Arguments.ThreadType.VIRTUAL)
                ? Thread.ofVirtual().factory() : Thread.ofPlatform().factory();

        switch (arguments.getChallengeNumber()) {
            case ONE -> {
                switch (arguments.getManagementType()) {
                    case SELF -> {
                        new ChallengeOneSelfManagedTest(threadFactory, arguments.getNumberOfThreads()).run();
                    }
                    case POOL -> {
                        new ChallengeOnePoolManagedTest(threadFactory, arguments.getNumberOfThreads()).run();
                    }
                    case ASYNC -> {
                        new ChallengeOneAsyncTest(threadFactory, arguments.getNumberOfThreads()).run();
                    }
                };
            }
            case THREE -> {
                switch (arguments.getManagementType()) {
                    case ASYNC -> {
                        new ChallengeThreeAsyncTest(threadFactory, arguments.getNumberOfThreads()).run();
                    }
                }
            }
            case TWO, FOUR -> { throw new UnsupportedOperationException("Challenge Number not implemented!"); }
        };

    }

    public static void help() {
        System.out.print("""
                Required Parameters:
                    Challenge Number <1|2|3>
                    Thread Type <plat|virt>
                    Management Type <self|pool|async>
                    Number of Threads <integer>
                
                Example: 1 virt none 100000
                """);
    }

    private static class Arguments {

        private enum ThreadType { PLATFORM, VIRTUAL}
        private enum ChallengeNumber { ONE, TWO, THREE, FOUR }
        private enum ManagementType { SELF, POOL, ASYNC }

        private ChallengeNumber challengeNumber;
        private ThreadType threadType;
        private ManagementType managementType;
        private int numberOfThreads;

        public void parseArguments(String[] args) {

            if (args.length < 4) throw new IllegalArgumentException("Too few arguments!");
            if (args.length > 4) throw new IllegalArgumentException("Too many arguments!");

            try {
                challengeNumber = switch (Integer.parseInt(args[0])) {
                    case 1 -> ChallengeNumber.ONE;
                    case 3 -> ChallengeNumber.THREE;
                    case 2, 4 -> throw new UnsupportedOperationException("No Solution For Challenge Number '" + args[0] + "'");
                    default -> throw new IllegalArgumentException("No Challenge Number matched on '" + args[0] + "'");
                };
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Not a valid Challenge Number - '" + args[2] + "'", e);
            }

            threadType = switch (args[1].toLowerCase()) {
                case "plat" -> ThreadType.PLATFORM;
                case "virt" -> ThreadType.VIRTUAL;
                default -> throw new IllegalArgumentException("No Thread Type matched on '" + args[1] + "'");
            };

            managementType = switch (args[2].toLowerCase()) {
                case "self" -> ManagementType.SELF;
                case "pool" -> ManagementType.POOL;
                case "async" -> ManagementType.ASYNC;
                default -> throw new IllegalArgumentException("No Management Type matched on '" + args[2] + "'");
            };

            try {
                numberOfThreads = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Not a valid number of threads - '" + args[2] + "'", e);
            }

        }

        public ChallengeNumber getChallengeNumber() {
            return challengeNumber;
        }

        public ThreadType getThreadType() {
            return threadType;
        }

        public ManagementType getManagementType() {
            return managementType;
        }

        public int getNumberOfThreads() {
            return numberOfThreads;
        }

        @Override
        public String toString() {
            return "Challenge Number: " + getChallengeNumber() + "; Thread Type: " + getThreadType()
                    + "; Management Type: " + getManagementType() + "; Number of Threads: " + getNumberOfThreads();
        }

    }

}
