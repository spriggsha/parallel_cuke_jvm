## Introduction
A CucumberJVM-based framework which has been modifed to be multi-threaded.  It also includes features such as:

* runs against a Selenium Grid, using the `WebDriverWrapper.java` class to store the particular Grid's configuration
* makes use of [PageObjects](https://code.google.com/p/selenium/wiki/PageObjects)

The tests are controlled via Cucumber-JVM .feature files.

## How It Works
The particulars of how CucumberJVM works are not covered here.  See the project's [official documentation](https://github.com/cucumber/cucumber-jvm)
for that kind of information.
This framework adds multi-threading to the mix by adding:

1. __FeatureBuilder.java:__  Runs before the tests.  It
scours the feature files, finds the Scenarios and Scenario Outlines that have been chosen to run, and then 
parameterizes them into separate feature files, each with it's own JUnit-based test-runner file.  This 
allows the Maven Surefire Plugin to fork each test into its own thread.  Tying this into a Grid setup 
makes for a highly-scalable framework.

2. __Maven Surefire Plugin:__  Provided in the pom.xml.  Creates extra threads which pick up new features 
dynamically.  The number of threads is specified with the `-Dthreads=x` flag upon starting the program.

3. __ReportMerger.java__:  Credit to [Tristan McCarthy](https://github.com/tristanmccarthy/Cucumber-JVM-Parallel) 
for this one.  It takes all the individual cucumber reports and stitches them together into an index.html 
file in the /target/ folder.

### Setting up for your Grid
Multi-threading is great, but having a multi-noded Selenium Grid really makes this pay off.  In order to set up your Grid's particulars, go into the `WebDriverWrapper.java` file, and:
1.  Modify the `String hubLocation = "http://url:port/wd/hub";` line so that it points to your hub.
2.  Modify the various `case` statements in the constructor so that match up with the Capabilities of your Grid's nodes.
3.  Write your feature files, being sure to match up your `typeOfBrowser` variables with what's in your WebDriverWrapper.

## How to Run the Tests
The tests are run via a Maven command:

`mvn clean install -PparallelTests  "-Dcucumber.options= --tags {@runtag1,runtag2} --tags {~@dontruntag1,~dontruntag2}" -Dthreads={numThreads}`

+ __runtag__: the tags in the .feature files you want to run, ex. @chrome, @safari, @smokeTest
+ __dontruntag__: any tags that should not be run, ex. ~@wip, ~@skip
+ __threads__: the number of tests you want to run in parallel. If you don't want to run tests in parallel.  Defaults to 1 if you don't specifiy it.

Example:  `mvn clean install -PparallelTests "-Dcucumber.options= --tags @google --tags ~@skip" -Dthreads=12`.  This will run every feature tagged with `google`, skip every one tagged with `skip`, and run the whole thing using 12 separate threads.

When the tests have finished running, find your results in __{workspace}/target/index.html__
