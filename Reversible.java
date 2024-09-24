/*
public class Reversible extends Irreversible {

   private double Keq;

    public Reversible(double[] stoicCoeff, double[] RateCoeff, double[] ReactantRateCoeff, double[] ProductRateCoeff, double k, double A, double E, double Keq) throws InvalidReactionParameterException {
        super(stoicCoeff, RateCoeff, ReactantRateCoeff, ProductRateCoeff, k, A, E);//exception thrown in parent (Irreversible)
        if(Keq<0.0) throw new InvalidReactionParameterException();
        this.Keq=Keq;
    }

    public Reversible(Reversible source) throws InvalidReactionParameterException {
        super(source);//source does null check, no need to do it
        this.Keq=source.Keq;
    }
    public Reversible clone(){
        //must first create variable outside try-catch and assign to null,
        //otherwise compilation error generated stating variable may not have been initialized
        Reversible copy=null;
        try
        {
            copy = new Reversible(this);//object exists and so it is valid
        }
        catch(Exception e)
        {}//do nothing
        finally
        {
            return copy;
        }
    }
    public boolean setKeq(double Keq) {
        if (Keq < 0.0) return false;
        this.Keq = Keq;
        return true;
    }
    public double getKeq(){
        return this.Keq;
    }


    public double[] Non_CatalyticRelativeRates(double v, double[] ReactantFlows, double[] ProductFlows) {
        //k form parent constructor is the forward reaction
        //use both reactant and product flows in this method!!
        double kreverse=super.getK()/this.Keq;
        double Cforward = 1;
        for (int i = 0; i < ReactantFlows.length; i++) {
            double Ci = Math.pow((ReactantFlows[i]/v), super.getReactantRateCoeff()[i]);
            Cforward = Cforward * Ci;
        }//calculates forward rate concentrations, if not a rate dependent concentration the user will input 0 for the
        //rateCoeff resulting in C^0 so that concentration value will be 1 (i.e. not effecting rate)
        double Creverse = 1;
        for (int i = 0; i < ProductFlows.length; i++) {
            double Ci = Math.pow((ProductFlows[i]/v), super.getProductRateCoeff()[i]);
            Creverse = Creverse * Ci;
        }

        double[] relRates = new double[super.getStoicCoeff().length];
        for (int i = 0; i < super.getStoicCoeff().length; i++) {
            double netrates = getK()*Cforward-kreverse*Creverse;
            //using negative k for reactants because they are being consumed
            //using positve kreverse because products are being produced
            relRates[i] = netrates / super.getStoicCoeff()[i];//calculates relative net rates from stoichiometric coefficients
        }
        return relRates;
    }

    public boolean equals(Object comparator){
        if(!super.equals((Reversible)comparator)) return false;
        if(this.Keq!=((Reversible)comparator).Keq) return false;
        return true;
    }
}

 */
