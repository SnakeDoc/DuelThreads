# Duel Threads

Concurrency techniques duel it out for the championship (and bragging rights)

--------------------------------------------------------------------------------------------

## Phases:

1) Argue over rules, challenges and the grand prize
2) Settle rules and challenges, decide beer is a great grand prize
3) Code solutions to agreed upon challenges
4) Crown Java as victorious, because we already know how this plays out
5) Profit?

## Rules:

1) Final submissions will be executed on cloud vm's hosted by Vultr
2) Linux or Windows may be used (extra brownie points awarded for portable, OS agnostic code)
3) Testing will be conducted on both the least and most spec'ed AMD EPYC High Performance Cloud Compute nodes
    1) 1 vCPU, 1GB RAM (laughs in Linux)
    2) 12 vCPU, 24GB RAM
4) The OS must not be "tuned" to enhance threading performance, ie. it must be "out of the box"
    1) Initial configuration scripts to setup the runtime environment are permitted once approved by all challengers
5) The language runtime must not be "tuned" to enhance threading performance, ie. it must be "out of the box"
    1) Preview/Experimental builds are permitted, but custom non-standard builds are forbidden.
        1) Example, "Joes increadible runtime built with a hot tub of secret sauce" is not permitted
    3) No user enhancements or configuration changes are permitted.
6) A profiling/diagnostic utility may be used during runtime to collect challenge statistics
    1) These tools often reduce performance - is there another way?
7) A startup script must be used to invoke program code for each challenge
    1) Any shell script type that is relevant may be used
    2) Startup scripts must not alter/boost program performance
    3) Startup scripts are allowed to start/stop/cleanup programsi
    4) Startup scripts are not part of the challenge themselves, but will aid in scripting a mostly automated challenge
8) A runtime "warmup" is permitted but must not be included in the challenge portion of your code
9) No external libraries are permitted, only language built-in features
10) X days/weeks will be permitted to complete the challenge?

## Challenges

Produce one solution for each of the following threading schemes:

1) Pure/bare self-managed language threads
2) Pure/bare pool-managed language threads
3) Async threads
4) Lightweight threads

    1) Using a shallow call stack, spawn and execute 10,000,000 threads
        1) Each thread must calculate the product of two random integer values
        2) Each thread must print to the console it's thread number and calculated value
        3) Each thread must execute in the order created, and held until program termination
        4) When a fatal error occurs, the last printed thread number is counted as the max concurrent thread count
        5) Highest concurrent thread count wins this challenge
    2) Using a deep(ish) call stack (see below), spawn and execute 10,000,000 threads
        1) Deep is defined as 100 recursive calls
        2) A base random integer value must be produced
        3) Each recursive call must multiply or divide (alternating) a random integer value and return this value
        4) The returned value is then multiplied or divided (alternating) at each level as the recursion unwraps
        5) Overflow is permitted - cast the final value as an integer at recursion return
        6) Each thread must print to the console it's thread number and calculated value
        7) Each thread must execute in the order created, and held until program termination
        8) When a fatal error occurs, the last printed thread number is counted as the max concurrent thread count
        9) Highest concurrent thread count wins this challenge
    3) Serve a provided html template to as many concurrent web socket requests as possible
        1) The html template will be provided in advance, but will remain simple
        2) The template will contain two basic variables which must be replaced by your program before serving every request
        3) The template variables will be the thread number and UNIX Epoch timestamp in milliseconds
        4) The socket and all handling code must be user written
        5) Only GET requests will be submitted and required to be handled
        6) Template, content and final html must be rendered entirely on the server
        7) No URL parameters, etc. will be expected
        8) Endpoint path must be: /async-socket-test
        9) Threads may be released at will
        10) Maximum concurrent successfully handled web requests will be counted
        11) An external webserver load stress tester will be used (TBD) to conduct this test

#### Ready for the duel?
