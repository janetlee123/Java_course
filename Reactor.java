public abstract class Reactor {

    public Reaction [] r ;
    protected double [] opConditions; // array containing flowrates of all components, p0, T0

    public Reactor(Reaction[] r, double []opConditions)
    { //checks
        if (r == null) System.out.println("Array of rxns is null"); System.exit(0);
        if (opConditions==null) System.out.println("opConditions is null"); System.exit(0);

    }

    public Reactor(Reactor original)
    {
        if(original==null) System.exit(0);

    }

    public abstract Reactor clone();

    // private accessor and mutator methods so child class can access it
    // child calls super constructor to have access to reactions [], g_opcondiitons and enum

    // Accessor and Mutator Methods
    public double [] getopConditions() {
        return this.opConditions;
    }

    public Reaction[] getR() {
        return this.r;
    }

    public boolean setR(Reaction[] r) {
        if(r==null) return false;
        for (int i =0; i< r.length; i++)
            if(r[i] ==null) return false;
        this.r = r;
        return true;
    }

    public boolean setopConditions(double [] opConditions) {
        if(opConditions==null) return false;
        for (int i = 0; i<opConditions.length;i++){
            if(opConditions[i] < 0 ) System.exit(0);
        }
        for (int i = 0; i<opConditions.length;i++){
            if(opConditions[i] < 0 ) System.exit(0);
        }
        return true;
    }

    //Add  abstract calculateEnthalpy()

    public boolean equals(Object comparator)
    {
        if(this.getClass()!=comparator.getClass()) return false;
        if (!this.r.equals(((Reactor) comparator).r)) return false;
        else return true;
    }


}
