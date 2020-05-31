package udel.spec.specwebbackend.model;

public enum BenchmarkType {
    ACCEL("accel"),
    CPU2017("cpu2017"),
    CPU2006("cpu2006"),
    OMP2012("omp2012"),
    MPI2007("mpi2007");
    private String value;

    private BenchmarkType(String value){
        this.value=value;
    }

}
