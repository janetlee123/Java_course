
public class InvalidReactionParameterException extends Exception{
    private double[] stoicCoeff;
    //make rate coeff an instance variable
    private double[] RateCoeff;
    private double[] ReactantRateCoeff;
    private double[] ProductRateCoeff;
    private double k;
    private double A;
    private double E;
    private double Keq;
    private boolean isValid;
    //first constructor handles case where k invalid
    public InvalidReactionParameterException(double[] stoicCoeff, double[] RateCoeff, double[] ReactantRateCoeff, double[] ProductRateCoeff, double k, double A, double E, double Keq)
    {
       if(stoicCoeff==null) throw new IllegalArgumentException("stoicCoeff is null");
        this.stoicCoeff = new double[stoicCoeff.length];
        for (int i = 0; i < stoicCoeff.length; i++)
            this.stoicCoeff[i] = stoicCoeff[i];
        if(RateCoeff==null) throw new IllegalArgumentException("RateCoeff is null");
        this.RateCoeff = new double[RateCoeff.length];
        for (int i = 0; i < RateCoeff.length; i++)
            this.RateCoeff[i] = RateCoeff[i];
        if(ReactantRateCoeff==null) throw new IllegalArgumentException("ReactionRateCoeff is null");
        this.ReactantRateCoeff = new double[ReactantRateCoeff.length];
        for (int i = 0; i < ReactantRateCoeff.length; i++)
            this.ReactantRateCoeff[i] = ReactantRateCoeff[i];
        if(ProductRateCoeff==null) throw new IllegalArgumentException("ProductRateCoeff is null");
        this.ProductRateCoeff = new double[ProductRateCoeff.length];
        for (int i = 0; i < ProductRateCoeff.length; i++)
            this.ProductRateCoeff[i] = ProductRateCoeff[i];
        this.k=k;
        this.A=A;
        this.E=E;
        this.Keq=Keq;
        this.isValid=false;
    }
    public InvalidReactionParameterException(InvalidReactionParameterException source)
    {
        if(source==null) throw new IllegalArgumentException("source is null");
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
        this.Keq=source.Keq;
        this.isValid=source.isValid;

    }
    //third constructor handles other cases
    public InvalidReactionParameterException()
    {
        super("Attempt to copy construct a Reaction object with a null object.");
        this.isValid=false;
    }
    public InvalidReactionParameterException clone()
    {
        return new InvalidReactionParameterException(this);
    }
    public String getMessage()
    {
        if(this.isValid==false)//if parameters are invalid, isValid will be false
            return "Attempt to create a Reaction object with a negative parameter.";
        else
            return super.getMessage();//in this case, the default message has been set in the constructor above
    }
    public boolean equals(Object comparator)
    {
        if(comparator==null) return false;
        if(this.getClass()!=comparator.getClass()) return false;
        if(this.isValid!= ((InvalidReactionParameterException)comparator).isValid) return false;
        if(this.k!= ((InvalidReactionParameterException)comparator).k) return false;
        if(this.A!= ((InvalidReactionParameterException)comparator).A) return false;
        if(this.E!= ((InvalidReactionParameterException)comparator).E) return false;
        if(this.Keq!= ((InvalidReactionParameterException)comparator).Keq) return false;
        if(this.stoicCoeff.length!=((InvalidReactionParameterException)comparator).stoicCoeff.length) return false;
        for(int i = 0; i < this.stoicCoeff.length; i++)
            if(this.stoicCoeff[i]!=((InvalidReactionParameterException)comparator).stoicCoeff[i]) return false;
        if(this.RateCoeff.length!=((InvalidReactionParameterException)comparator).RateCoeff.length) return false;
        for(int i = 0; i < this.RateCoeff.length; i++)
            if(this.RateCoeff[i]!=((InvalidReactionParameterException)comparator).RateCoeff[i]) return false;
        if(this.ReactantRateCoeff.length!=((InvalidReactionParameterException)comparator).ReactantRateCoeff.length) return false;
        for(int i = 0; i < this.ReactantRateCoeff.length; i++)
            if(this.ReactantRateCoeff[i]!=((InvalidReactionParameterException)comparator).ReactantRateCoeff[i]) return false;
        if(this.ProductRateCoeff.length!=((InvalidReactionParameterException)comparator).ProductRateCoeff.length) return false;
        for(int i = 0; i < this.ProductRateCoeff.length; i++)
            if(this.ProductRateCoeff[i]!=((InvalidReactionParameterException)comparator).ProductRateCoeff[i]) return false;
        return true;

    }
}


