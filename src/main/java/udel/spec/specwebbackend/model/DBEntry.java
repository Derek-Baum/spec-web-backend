package udel.spec.specwebbackend.model;

import java.util.List;

//TODO: annotate this for eventual mongodb integration.
public class DBEntry {
    // "CPU(s) enabled","36 cores, 2 chips, 18 cores/chip"


    private String uniqueID; //this is just the filename.

    private String year;
    private String quarter;

    private String timeString;
    private String benchmarkType;
    private BenchmarkType benchmarkTypeEnum;

    private String hardwareModel;
    private String score;
    private String cpuName;
    private String cpuEnabled;//if type is mpi2007, add threads per core.
    private String accelModelName;//mostly N/A
    private String memory;
    private String operatingSystem;
    private String otherSoftware;
    private String compiler;


    //use the constructor to make a model, it must have a UID(filename), and a benchmark it is coming from.
    //set all defaults to N/A or corresponding nothings.
    public DBEntry(String uniqueID, String benchmarkType, BenchmarkType benchmarkTypeEnum){
        this.uniqueID=uniqueID;
        this.benchmarkTypeEnum = benchmarkTypeEnum;
        setTimeFromUID(uniqueID);

        this.benchmarkType=benchmarkType;

        this.hardwareModel="N/A";
        this.score="N/A";
        this.cpuName="N/A";
        this.cpuEnabled="N/A";
        this.accelModelName="N/A";
        this.memory="N/A";
        this.operatingSystem="N/A";
        this.otherSoftware="N/A";
        this.compiler="N/A";
    }

    //of the form: res2019q2/accel-20190521-00125
    private void setTimeFromUID(String uniqueID){
        this.year = uniqueID.substring(uniqueID.indexOf("res") + 3,uniqueID.indexOf("q"));
        this.quarter = uniqueID.substring(uniqueID.indexOf("q")+1,uniqueID.indexOf("/"));
        //YYYY-MM-DD
        String time = uniqueID.split("-")[1];
        this.timeString=time.substring(0,4) + "-" + time.substring(4,6) + "-" + time.substring(6);
    }

    /* accel keywords
      keywords = {"\"Hardware Model:\"",
	  "SPECaccel_acc_base",
	  "\"CPU Name\"",
	  "\"CPU(s) enabled\"",
	  "\"Accel Model Name\"",
	  "Memory",
	  "\"Operating System\"",
	  "\"Other Software\"",
	  "Compiler"};*/

    //mpi 2007
    /*  "Model",
        "SPECmpiL_base2007",
        "\"Chips enabled\"",
        "\"Cores enabled\"",
        "\"Cores per chip\"",
        "\"Threads per core\"",
        "Memory",
        "\"Operating System\"",
        "\"Other Software\"",*/

    /*
        Since the dbentry must always know a benchmarktype of itself, we can just have a setAllVals method in here, that accepts a list of strings given from the csv file.
        depending on the type, we can set different properties for each benchmark type.
     */
    public void addAttributes(List<String> values){

        if(this.benchmarkTypeEnum.equals(BenchmarkType.ACCEL)){
            addAccel(values);
        }else if(this.benchmarkTypeEnum.equals(BenchmarkType.CPU2006)){
            addCPU2006(values);
        }else if(this.benchmarkTypeEnum.equals(BenchmarkType.CPU2017)){
            addCPU2017(values);
        }else if(this.benchmarkTypeEnum.equals(BenchmarkType.MPI2007)){
            addMPI2007(values);
        }else if(this.benchmarkTypeEnum.equals(BenchmarkType.OMP2012)){
            addOMP2012(values);
        }

    }
    /*
    {"\"Hardware Model:\"",
	  "SPECaccel_acc_base",
	 "\"CPU Name\"",
	 "\"CPU(s) enabled\"",
	 "\"Accel Model Name\"",
		 "Memory",
	   "\"Operating System\"",
	"\"Other Software\"",
	   "Compiler"};
     */
    private void addAccel(List<String> values){

        this.hardwareModel=values.get(0);
        this.score=values.get(1);
        this.cpuName=values.get(2);
        this.cpuEnabled=values.get(3);
        this.accelModelName=values.get(4);
        this.memory=values.get(5);
        this.operatingSystem=values.get(6);
        this.otherSoftware=values.get(7);
        this.compiler=values.get(8);

    }


    /*
        {"\"Hardware Model:\"",
		 "SPECint_base2006",
		"\"CPU Name\"",
		 "\"CPU(s) enabled\"",
		"Memory",
		"\"Operating System\"",
		 "\"Other Software\"",
		"Compiler"};

     */
    private void addCPU2006(List<String> values){

        this.hardwareModel=values.get(0);
        this.score=values.get(1);
        this.cpuName=values.get(2);
        this.cpuEnabled=values.get(3);
        this.memory=values.get(4);
        this.operatingSystem=values.get(5);
        this.otherSoftware=values.get(6);
        this.compiler=values.get(7);

    }



    /*"\"Hardware Model:\"",
      "SPECrate2017_fp_base",
            "\"CPU Name\"",
            "Enabled",
            "Memory",
            "OS",
            "Compiler"};*/
    private void addCPU2017(List<String> values){
        this.hardwareModel=values.get(0);
        this.score=values.get(1);
        this.cpuName=values.get(2);
        this.cpuEnabled=values.get(3);
        this.memory=values.get(4);
        this.operatingSystem=values.get(5);
        this.compiler=values.get(6);
    }
    /*{"Model",
            "SPECmpiL_base2007",
            "\"Chips enabled\"",
            "\"Cores enabled\"",
            "\"Cores per chip\"",
            "\"Threads per core\"",
            "Memory",
            "\"Operating System\"",
            "\"Other Software\"",};*/
    //TODO: fix this CPU enabled. if I ever come back to this...
    private void addMPI2007(List<String> values){

        this.hardwareModel=values.get(0);
        this.score=values.get(1);

        this.cpuName=values.get(2);
        this.cpuEnabled=values.get(3);
        this.accelModelName=values.get(4);
        this.memory=values.get(5);
        this.operatingSystem=values.get(6);
        this.otherSoftware=values.get(7);
        this.compiler=values.get(8);

    }

    /*"\"Hardware Model:\"",
            "SPECompG_base2012",
            "\"CPU Name\"",
            "\"CPU(s) enabled\"",
            "Memory",
            "\"Operating System\"",
            "Compiler"};*/
    private void addOMP2012(List<String> values){

        this.hardwareModel=values.get(0);
        this.score=values.get(1);
        this.cpuName=values.get(2);
        this.cpuEnabled=values.get(3);
        this.memory=values.get(4);
        this.operatingSystem=values.get(5);
        this.compiler=values.get(6);

    }

    public String getUniqueID() {
        return uniqueID;
    }

    public String getTimeString() {
        return timeString;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public String getBenchmarkType() {
        return benchmarkType;
    }

    public void setBenchmarkType(String benchmarkType) {
        this.benchmarkType = benchmarkType;
    }

    public String getHardwareModel() {
        return hardwareModel;
    }

    public void setHardwareModel(String hardwareModel) {
        this.hardwareModel = hardwareModel;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getCpuName() {
        return cpuName;
    }

    public void setCpuName(String cpuName) {
        this.cpuName = cpuName;
    }

    public String getCpuEnabled() {
        return cpuEnabled;
    }

    public void setCpuEnabled(String cpuEnabled) {
        this.cpuEnabled = cpuEnabled;
    }

    public String getAccelModelName() {
        return accelModelName;
    }

    public void setAccelModelName(String accelModelName) {
        this.accelModelName = accelModelName;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public String getOtherSoftware() {
        return otherSoftware;
    }

    public void setOtherSoftware(String otherSoftware) {
        this.otherSoftware = otherSoftware;
    }

    public String getCompiler() {
        return compiler;
    }

    public void setCompiler(String compiler) {
        this.compiler = compiler;
    }
}
