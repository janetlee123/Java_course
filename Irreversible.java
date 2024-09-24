
public class Irreversible extends Reaction{

    private double[] stoicCoeff;
    private double[] RateCoeff;//for catalytic
    private double[] ReactantRateCoeff;//for non-catalytic
    private double[] ProductRateCoeff;//for non-catlytic reversible
    //tedious to have user enter Ratecoeff, ReactantRateCoeff and ProductRateCoeff but putting them in
    //the method parameter list was causing issue in the PBR class and for non-catalytic the forward
    //and reverse rates and flows needed to be differentiated to calculate reversible reactions
    private double k;
    private double A;
    private double E;
    public static final double R=8.314;


    public Irreversible(double[] stoicCoeff, double[] RateCoeff, double[] ReactantRateCoeff, double[] ProductRateCoeff, double k, double A, double E) throws InvalidReactionParameterException{
        //add negative value checks and exception handling
        if (stoicCoeff == null) throw new InvalidReactionParameterException();
        //don't check if negative because reactants should be negative and products should be positive
        this.stoicCoeff = new double[stoicCoeff.length];
        for (int i = 0; i < stoicCoeff.length; i++)
            this.stoicCoeff[i] = stoicCoeff[i];
        if(RateCoeff==null) throw new InvalidReactionParameterException();
        this.RateCoeff = new double[RateCoeff.length];
        for (int i = 0; i < RateCoeff.length; i++)
            this.RateCoeff[i] = RateCoeff[i];
        if(ReactantRateCoeff==null) throw new InvalidReactionParameterException();
        this.ReactantRateCoeff = new double[ReactantRateCoeff.length];
        for (int i = 0; i < ReactantRateCoeff.length; i++)
            this.ReactantRateCoeff[i] = ReactantRateCoeff[i];
        if(ProductRateCoeff==null) throw new InvalidReactionParameterException();
        this.ProductRateCoeff = new double[ProductRateCoeff.length];
        for (int i = 0; i < ProductRateCoeff.length; i++)
            this.ProductRateCoeff[i] = ProductRateCoeff[i];
        if(k<0.0) throw new InvalidReactionParameterException();
        if(A<0.0) throw new InvalidReactionParameterException();
        if(E<0.0) throw new InvalidReactionParameterException();
        this.k=k;
        this.A=A;
        this.E=E;
    }//end of constructor



    public Irreversible(Irreversible source) throws InvalidReactionParameterException{
        if(source==null) throw new InvalidReactionParameterException();
        this.stoicCoeff=new double[source.stoicCoeff.length];
        for(int i = 0; i < source.stoicCoeff.length; i++)
            this.stoicCoeff[i]=source.stoicCoeff[i];
        this.RateCoeff=new double[source.RateCoeff.length];
        for(int i = 0; i < source.RateCoeff.length; i++)
            this.RateCoeff[i]=source.RateCoeff[i];
        this.ReactantRateCoeff=new double[source.ReactantRateCoeff.length];
        for(int i = 0; i < source.ReactantRateCoeff.length; i++)
            this.ReactantRateCoeff[i]=source.ReactantRateCoeff[i];
        this.ProductRateCoeff=new double[source.ProductRateCoeff.length];
        for(int i = 0; i < source.ProductRateCoeff.length; i++)
            this.ProductRateCoeff[i]=source.ProductRateCoeff[i];
        this.k=source.k;
        this.A=source.A;
        this.E=source.E;

    }//end of copy constructor

    public Irreversible clone(){
        //must first create variable outside try-catch and assign to null,
        //otherwise compilation error generated stating variable may not have been initialized
        Irreversible copy=null;
        try
        {
            copy = new Irreversible(this);//object exists and so it is valid
        }
        catch(Exception e)
        {}//do nothing
        finally
        {
            return copy;
        }
    }


    //add negative value checks and exception handling to mutator
    public boolean setStoicCoeff(double[] stoicCoeff) {
        if (stoicCoeff == null) return false;
        for (int i = 0; i < stoicCoeff.length; i++)
            this.stoicCoeff[i] = stoicCoeff[i];
        return true;
    }//end of mutator
    public boolean setRates(double[] RateCoeff, double[] ReactantRateCoeff, double[] ProductRateCoeff){
        if(RateCoeff==null) return false;
        this.RateCoeff = new double[RateCoeff.length];
        for (int i = 0; i < RateCoeff.length; i++)
            this.RateCoeff[i] = RateCoeff[i];
        if(ReactantRateCoeff==null) return false;
        this.ReactantRateCoeff = new double[ReactantRateCoeff.length];
        for (int i = 0; i < ReactantRateCoeff.length; i++)
            this.ReactantRateCoeff[i] = ReactantRateCoeff[i];
        if(ProductRateCoeff==null) return false;
        this.ProductRateCoeff = new double[ProductRateCoeff.length];
        for (int i = 0; i < ProductRateCoeff.length; i++)
            this.ProductRateCoeff[i] = ProductRateCoeff[i];
        return true;
    }

    public boolean setK(double k) {
        if(k<0.0) return false;
        this.k=k;
        return true;
    }

    public boolean setParameters(double A, double E) {
        if(A<0.0) return false;
        if(E<0.0) return false;
        this.A=A;
        this.E=E;
        return true;
    }

    public double[] getStoicCoeff() {
        double[] stoicCoeffcopy = new double[this.stoicCoeff.length];
        for (int i = 0; i < this.stoicCoeff.length; i++)
            stoicCoeffcopy[i] = this.stoicCoeff[i];
        return stoicCoeffcopy;
    }//end of accessor
    public double[] getRateCoeff() {
        double[] RateCoeffcopy = new double[this.RateCoeff.length];
        for (int i = 0; i < this.RateCoeff.length; i++)
            RateCoeffcopy[i] = this.RateCoeff[i];
        return RateCoeffcopy;
    }
    public double[] getReactantRateCoeff() {
        double[] ReactantRateCoeffcopy = new double[this.ReactantRateCoeff.length];
        for (int i = 0; i < this.ReactantRateCoeff.length; i++)
            ReactantRateCoeffcopy[i] = this.ReactantRateCoeff[i];
        return ReactantRateCoeffcopy;
    }
    public double[] getProductRateCoeff() {
        double[] ProductRateCoeffcopy = new double[this.ProductRateCoeff.length];
        for (int i = 0; i < this.ProductRateCoeff.length; i++)
            ProductRateCoeffcopy[i] = this.ProductRateCoeff[i];
        return ProductRateCoeffcopy;
    }

    public double getK() {return this.k;}
    public double getA() {return this.A;}
    public double getE() {return this.E;}



    @Override
    public double[] CatalyticRelativeRates(double T, double v, double[] RateDependentFlows, double density) {
        //calculating k
        double k = this.A * Math.exp(-this.E / (this.R * T));//calculates k value from user inputs
        double Pdependent = 1;
        for (int i = 0; i < RateDependentFlows.length; i++) {
            double Pi = Math.pow(((RateDependentFlows[i] * this.R * T) / v), this.RateCoeff[i]);
            Pdependent = Pdependent * Pi;
        }//calculates rate dependent pressures, if not a rate dependent pressure the user will input 0 for the
        //rateCoeff resulting in P^0 so that pressure value will be 1 (i.e. not effecting rate)
        double[] relRates = new double[this.stoicCoeff.length];
        for (int i = 0; i < this.stoicCoeff.length; i++) {
            double r = k * Pdependent;
            //using negative k because reactants are being consumed
            relRates[i] = r / this.stoicCoeff[i];
        }//calculates relative rates from stoichiometric coefficients
        for(int i =0; i< relRates.length;i++){
            relRates[i]= relRates[i]/density;
        }//converts rates for a catalytic PBR: divide by density
        return relRates;

    }

    public double[] Non_CatalyticRelativeRates(double v, double[] ReactantFlows, double[] ProductFlows) {
        //irreversible class only uses reactant flows and rate coefficients!!
        double Cdependent = 1;
        for (int i = 0; i < ReactantFlows.length; i++) {
            double Ci = Math.pow((ReactantFlows[i]/v), this.ReactantRateCoeff[i]);
            Cdependent = Cdependent * Ci;
        }//calculates rate dependent concentrations, if not a rate dependent concentration the user will input 0 for the
        //rateCoeff resulting in C^0 so that concentration value will be 1 (i.e. not effecting rate)
        double[] relRates = new double[this.stoicCoeff.length];
        for (int i = 0; i < this.stoicCoeff.length; i++) {
            double r = this.k * Cdependent;//k given so no need to calculate
            //using negative k because reactants are being consumed
            relRates[i] = r / this.stoicCoeff[i];//calculates relative rates from stoichiometric coefficients
        }
        return relRates;
    }
    //RateCoeff, density, ProductFlows, ReactantFlows, ReactantRateCoeff and ProductRateCoeff
    //all entered as parameters because they are not used for every reaction
    //i.e. RateCoeff and density are used for catalytic reactions in PBR and
    //ProductFlows, ReactantFlows, ReactantRateCoeff and ProductRateCoeff are used for non-catalytic reactions in PFR
    public boolean equals(Object comparator){
        if(!super.equals(comparator)) return false;
        if(this.k!= ((Irreversible)comparator).k) return false;
        if(this.A!=((Irreversible)comparator).A) return false;
        if(this.E!=((Irreversible)comparator).E) return false;
        if(this.stoicCoeff.length!=((Irreversible)comparator).stoicCoeff.length) return false;
        for(int i = 0; i < this.stoicCoeff.length; i++)
            if(this.stoicCoeff[i]!=((Irreversible)comparator).stoicCoeff[i]) return false;
        if(this.RateCoeff.length!=((Irreversible)comparator).RateCoeff.length) return false;
        for(int i = 0; i < this.RateCoeff.length; i++)
            if(this.RateCoeff[i]!=((Irreversible)comparator).RateCoeff[i]) return false;
        if(this.ReactantRateCoeff.length!=((Irreversible)comparator).ReactantRateCoeff.length) return false;
        for(int i = 0; i < this.ReactantRateCoeff.length; i++)
            if(this.ReactantRateCoeff[i]!=((Irreversible)comparator).ReactantRateCoeff[i]) return false;
        if(this.ProductRateCoeff.length!=((Irreversible)comparator).ProductRateCoeff.length) return false;
        for(int i = 0; i < this.ProductRateCoeff.length; i++)
            if(this.ProductRateCoeff[i]!=((Irreversible)comparator).ProductRateCoeff[i]) return false;
        return true;
    }


}



