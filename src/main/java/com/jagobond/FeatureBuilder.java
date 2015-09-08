package com.jagobond;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class FeatureBuilder {
    private static int threadNum = 1;

    public static void main(String[] args) throws IOException {

        // All the variables we'll need
        File oldFeatureDirectory = new File("src/test/java/com/jagobond/resources/tempFeatures");
        File oldRunnerDirectory = new File("src/test/java/com/jagobond/resources/tempRunners");

        ArrayList<String> featuresWithParsedScenarios = new ArrayList<>();
        ArrayList<String> finalFeatureList = new ArrayList<String>();
        ArrayList<String> tempTestRunners = new ArrayList<>();

        int featureNumber = 1;
        int runnerNumber = 1;


        // Get rid of any old embedded screenshots
        File targetDir = new File("./target");
        if(targetDir.exists()){
            for(File file : targetDir.listFiles()){
                if(file.getName().endsWith(".png")){
                    file.delete();
                }
            }
        }

        // Get rid of any old embedded screenshots
        File cucumberDir = new File("./target/cucumber");
        if(cucumberDir.exists()){
            deleteDir(cucumberDir);
        }

        File cucumberHtmlDir = new File("./target/cucumber-html-report");
        if (cucumberHtmlDir.exists()) {
            for (File file : cucumberHtmlDir.listFiles()) {
                if (file.getName().startsWith("embedded")) {
                    file.delete();
                }
            }
        }

        /*
        Making sure that there are no old features or test runners left
        over from a previous run of this program.
        */
        if(oldFeatureDirectory.exists()){
            if(deleteDir(oldFeatureDirectory))
                System.out.println("Cleared out an old Feature Directory");
            if(deleteDir(oldRunnerDirectory))
                System.out.println("Cleared out an old TestRunner Directory");
        }


        /*
        Grabbing the "cucumber.options" arguments, as they'll be used to clean up
        the feature files after parsing, dramatically reducing the time spent
        writing files to disk.
        */
        String argList = Arrays.toString(args);

        /*
        Split out each of the --tags parameters in our cucumber options
        */
        String[] tagBits = argList.split("--tags");
        ArrayList<String> tagBitsList = new ArrayList<>();
        for(String string : tagBits){
            if(string.contains("@"))
                tagBitsList.add(string);
        }

        /*
        A bit of timekeeping, just so we can tell just how much of our
        build's time this process is eating into
        */
        long time;
        System.out.println("Starting to split out the features");
        time = System.currentTimeMillis();


        /*
        The bulk of our feature processing is done here.
        */
        File directory = new File("src/test/java/com/jagobond/features");
        String[] extensions = {"feature"};
        Collection<File> initialFeatureFileList = FileUtils.listFiles(directory,extensions,true);

        for(File file : initialFeatureFileList){

            String featureString = null;
            String lineBuffer = "";
            String lineReader = "";
            String lineArchive = "";
            String scenarioString;

            ArrayList<String> examplesParsed = new ArrayList<>();

            Boolean foundFeature = false;
            Boolean startedBuffer = false;
            Boolean startedArchive = false;

            String gherkin = FileUtils.readFileToString(file);

            // Read through the feature file
            Scanner scanner = new Scanner(gherkin);
            while(scanner.hasNextLine()){

                if(startedArchive)  {
                    lineArchive = lineArchive + lineBuffer + "\n";
                }

                if(startedBuffer)  {
                    lineBuffer=lineReader;
                    startedArchive = true;
                }

                lineReader = scanner.nextLine();
                startedBuffer = true;

                // When you hit an actual scenario, take what you've already read and save
                // it as either the feature Header or a Scenario.
                if(lineReader.contains("Scenario:") || lineReader.contains("Scenario Outline:")){

                    if(!foundFeature){
                        featureString = lineArchive;
                        lineArchive = "";
                        foundFeature = true;
                    }else{
                        scenarioString = lineArchive;
                        lineArchive = "";

                        // If dealing with a Scenario Outline, it's going to need additional
                        // parsing
                        if(scenarioString.contains("Scenario Outline:")){
                            examplesParsed.addAll(parseExamples(scenarioString));
                        }else{
                            examplesParsed.add(scenarioString);
                        }
                    }
                }
            }

            // hasNext() is false on the last line, so we have to process the final
            // scenario separately.
            String theLastScenario = (lineArchive+lineBuffer+ "\n" + lineReader);
            if(theLastScenario.contains("Scenario Outline:")){
                examplesParsed.addAll(parseExamples(theLastScenario));
            }else{
                examplesParsed.add(theLastScenario);
            }

            // Take the results of each feature file and add it to a growing ArrayList
            for(String scenarioExample : examplesParsed){
                String result = featureString + scenarioExample;
                featuresWithParsedScenarios.add(result);
            }

        }

        //for each --tags string
        for(String string : tagBitsList){
            boolean additiveTag = true;
            ArrayList<String> tags = new ArrayList<>();

            // grab the tags out of it and find out if it's additive or subtractive
            for(int i = 0; i < string.length(); i++){
                if(Character.toString(string.charAt(i)).matches("@")){
                    if(Character.toString(string.charAt(i-1)).matches("~")) {
                        additiveTag = false;
                    }else {
                        additiveTag = true;
                    }
                    tags.add(grabTag(string,i));
                }
            }

            // if additive, remove features which DON'T contain our @tags
            // if subtractive, remove those which DO contain our ~@tags
            for(Iterator i = featuresWithParsedScenarios.iterator(); i.hasNext(); ){
                String feature = (String)i.next();
                boolean valid;
                if(additiveTag){
                    valid = false;
                    for(String tag : tags){
                        if(feature.contains(tag)){
                            valid = true;
                        }
                    }
                }else{
                    valid = true;
                    for(String tag : tags){
                        if(feature.contains(tag)){
                            valid = false;
                        }
                    }
                }
                if(!valid){
                    i.remove();
                }
            }

        }

        // Output for the Jenkins console
        for(String string : featuresWithParsedScenarios){
            System.out.println("-----------------------------------FEATURE---------------------------------------");
            System.out.println(string);
            System.out.println("-----------------------------------FEATURE---------------------------------------");
        }

        // More timekeeping
        System.out.println("Starting to create the Test Runners after " + ((System.currentTimeMillis() - time)/1000) + " seconds.");
        time = System.currentTimeMillis();

        // Tag each of the feature with a unique integer, so the framework can refer
        // to each specifically.
        for(String str: featuresWithParsedScenarios){
            finalFeatureList.add(str);

            // Every new feature file gets its own testRunner, too, which we build from scratch
            tempTestRunners.add("package com.jagobond.resources.tempRunners;\n" +
                    "\n" +
                    "import cucumber.api.junit.Cucumber;\n" +
                    "import org.junit.runner.RunWith;\n" +
                    "\n" +
                    "@RunWith(Cucumber.class)\n" +
                    "@Cucumber.Options(features = \"src/test/java/com/jagobond/resources/tempFeatures/" + threadNum + "\",\n" +
                    "                    format = {\"html:target/cucumber/" + threadNum + "\", \"json:target/cucumber/cucumber" + threadNum + ".json\"},\n" +
                    "                    tags   = " + "{}," + "\n" +
                    "                    glue = {\"com.jagobond.stepDefs\"}\n" +
                    "                 )\n" +
                    "\n" +
                    "public class DesktopTestRunner" + threadNum + " {\n" +
                    "}");

            threadNum++;
        }

        // More timekeeping
        System.out.println("Starting to write the files after an additional" + ((System.currentTimeMillis() - time)/1000) + " seconds.");
        time = System.currentTimeMillis();

        // Write each feature to disk.  This is generally going to cost more time than anything else.
        for(String str: finalFeatureList){
            try{
                FileUtils.writeStringToFile(new File("src/test/java/com/jagobond/resources/tempFeatures/" + featureNumber + "/browserTest.feature"), str);
            }catch(IOException e){
                e.printStackTrace();
            }
            featureNumber++;
        }

        // Write each testRunner to disk, also.
        for(String str: tempTestRunners){
            try{
                FileUtils.writeStringToFile(new File("src/test/java/com/jagobond/resources/tempRunners/DesktopTestRunner" + runnerNumber + ".java"), str);
            }catch (IOException e){
                e.printStackTrace();
            }
            runnerNumber++;
        }

        // How long did all of that take? (at time of writing, about 8 seconds if we select ALL our tests)
        System.out.println("Finished parameterizing features after an additional" + ((System.currentTimeMillis() - time)/1000) + " seconds.");

    }



    //~  Static Class methods ~//


    /*
    Takes a Scenario Outline file and splits it into individual Scanner
    Scenario files.
    */
    private static ArrayList<String> parseExamples(String scenarioString) {

        String lineBuffer = "";
        String lineReader = "";
        String lineArchive = "";
        String scenarioHeader = "";

        ArrayList<String> result = new ArrayList<>();
        ArrayList<String> exampleGroups = new ArrayList<>();
        ArrayList<String> exampleBeans = new ArrayList<>();

        Boolean foundScenario = false;
        Boolean startedBuffer = false;
        Boolean startedArchive = false;

        Scanner scanner = new Scanner(scenarioString);
        while(scanner.hasNextLine()){
            if(startedArchive)  {
                lineArchive = lineArchive + lineBuffer + "\n";
            }

            if(startedBuffer)  {
                lineBuffer=lineReader;
                startedArchive = true;
            }

            lineReader = scanner.nextLine();
            startedBuffer = true;

            if(lineReader.contains("Examples:")){
                if(!foundScenario){
                    scenarioHeader = lineArchive;
                    lineArchive = "";
                    foundScenario = true;
                }else{
                    exampleGroups.add(lineArchive);
                    lineArchive = "";

                }
            }
        }
        exampleGroups.add(lineArchive + lineBuffer + "\n" + lineReader);

        for(String example : exampleGroups){
            exampleBeans.addAll(parseExampleBeans(example));
        }

        for(String bean : exampleBeans){
            result.add(scenarioHeader+bean);
        }

        return result;
    }

    /*
    Takes the Examples parameters and splits them up into individual rows,
    returning the result
    */
    private static ArrayList<String> parseExampleBeans(String example) {

        String tags = "";
        String exampleHeading = "";
        String columnHeaders = "";
        String readLine;

        ArrayList<String> exampleRows = new ArrayList<>();
        ArrayList<String> result = new ArrayList<>();

        boolean foundColumnHeaders = false;

        Scanner scanner = new Scanner(example);

        while(scanner.hasNextLine()){
            readLine = scanner.nextLine();

            if(readLine.contains("@")){
                tags = readLine;
            }else if(readLine.contains("Examples:")){
                exampleHeading = readLine;
            }else if(readLine.contains("|")){
                if(!foundColumnHeaders){
                    columnHeaders = readLine;
                    foundColumnHeaders = true;
                }else{
                    exampleRows.add(readLine);
                }
            }else{
                continue;
            }
        }

        for(String bean : exampleRows){
            result.add(tags + "\n" + exampleHeading + "\n" + columnHeaders + "\n" + bean);
        }

        return result;
    }

    // Pulls a cucumberJVM tag out of a list
    private static String grabTag(String argList, int i) {
        int iter = i;
        String result = String.valueOf(argList.charAt(i));
        while(!String.valueOf(argList.charAt(++iter)).matches("[-. ~@\\],]")){
            result= result + String.valueOf(argList.charAt(iter));
        }
        System.out.println("Returning tag: " + result);
        return result;
    }

    // Deletes a directory, recursively
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
}
