public abstract class Reaction {

    public abstract double[] CatalyticRelativeRates(double T, double v, double[] RateDependentFlows, double density);

    public abstract double[] Non_CatalyticRelativeRates(double v, double[] ReactantFlows, double[] ProductFlows);

    public abstract Reaction clone();

    public boolean equals(Object comparator) {
        if (comparator == null) return false;
        else return (this.getClass() == comparator.getClass());
    }
}


