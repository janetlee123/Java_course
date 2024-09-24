public class Heatcapacity {
    // takes the coefficients of each component and returns the components heat capacity
    private double[] coeff; //array of heat capacity coefficients
    public static final double Tr=298.15;

    public Heatcapacity(double[] coeff){//add exception handling
        if(coeff==null) System.exit(0);
        //check neg value
        if(coeff.length!=5) System.exit(0);//require a,b,c,d and e
        this.coeff=new double[coeff.length];
        for(int i=0; i<coeff.length;i++)
            this.coeff[i]=coeff[i];
    }
    public Heatcapacity(Heatcapacity source){
        if(source==null) System.exit(0);
        this.coeff=new double[source.coeff.length];
        for(int i=0; i<source.coeff.length;i++)
            this.coeff[i]=source.coeff[i];
    }

    public Heatcapacity clone(){return new Heatcapacity(this);}

    public boolean setCoeff(double[] coeff){
        if(coeff==null) return false;
        //check neg value
        if(coeff.length!=5) return false;//require a,b,c,d and e
        this.coeff=new double[coeff.length];
        for(int i=0; i<coeff.length;i++)
            this.coeff[i]=coeff[i];
        return true;
    }

    public double[] getCoeff(){
        double[] coeffCopy = new double[this.coeff.length];
        for(int i=0;i<this.coeff.length;i++)
            coeffCopy[i]=this.coeff[i];
        return coeffCopy;
    }
    public double calculateCp(double T) {
        //call this in the PBR for non-isothermal case, the input T should be the T value at each step in the integration
        // deltaCp = a + b*T + c*T^2 + dT^3 + e*T^4
        //double cp0 = this.coeff[0] + (this.coeff[1] * Tr) + (this.coeff[2] * Math.pow(Tr, 2)) + (this.coeff[3] * Math.pow(Tr, 3)) + (this.coeff[4] * Math.pow(Tr, 4));
        double cp =(this.coeff[0] + (this.coeff[1] * (T-Tr)) + (this.coeff[2] * Math.pow((T-Tr), 2)) + (this.coeff[3] * Math.pow((T-Tr), 3)) + (this.coeff[4] * Math.pow((T-Tr), 4)));
        return cp;
    }

    public boolean equals(Object comparator){
        if(comparator==null) return false;
        if(this.getClass()!=comparator.getClass());
        if(this.coeff.length!=((Heatcapacity)comparator).coeff.length) return false;
        for(int i=0;i<this.coeff.length;i++)
            if(this.coeff[i]!=((Heatcapacity)comparator).coeff[i]) return false;
        return true;
    }

}
