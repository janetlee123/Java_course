import jdk.jfr.Description;
import numericalMethods.Function;
import numericalMethods.RK4;

public class PBR extends Reactor implements Function {
    private int numComponents;
    private Irreversible [] r; // array of reactions
    ReactionType Type;
    Heatcapacity[] cp;//add into constructor //Cp's for each i component

    private static final double particleDensity = 833*(1-0.45); //bulk density of catalyst

    //Constructors
    public PBR(double[] opConditions, Irreversible[] r,Heatcapacity []cp,ReactionType reactionType, int numComponents ){
        super(r,opConditions);
        //checks
        if (r == null) System.out.println("Array of rxns is null"); System.exit(0);
        if (opConditions==null) System.out.println("opConditions Null");System.exit(0);
        if(cp==null) System.out.println("Cp array is null");System.exit(0);
        if(numComponents <= 0 ) System.out.println("numComp < 0");System.exit(0);
        for (int i =0;i<r.length;i++){
            if (r[i]==null) System.out.println("r[i] == null ");System.exit(0);
        }
        for (int i =0;i <opConditions.length; i++){
            if ( opConditions[i] < 0) System.out.println("opCond[i] == null");System.exit(0);
        }
        for (int i = 0; i<cp.length;i++){
            if(cp[i] == null) System.out.println("cp[i] == null");System.exit(0);

        }

        //Setting variables
        this.r = new Irreversible[r.length];
        this.opConditions = new double [opConditions.length];
        this.cp = new Heatcapacity[cp.length];

        // filling instance variables
        for (int i =0;i<r.length;i++){
            this.r[i] = r[i];
        }
        for (int i =0;i <opConditions.length; i++){
            this.opConditions[i] = opConditions[i];
        }
        for (int i = 0; i<cp.length;i++){
            this.cp[i] = cp[i];
        }
        this.numComponents = numComponents;
        this.Type = reactionType;

    }//constructor
    public PBR (PBR source){
        super(source);
        //Checks
        if(source.cp==null) System.exit(0);
        if(source.numComponents <= 0 ) System.exit(0);
        for (int i = 0; i<source.cp.length;i++){
            if(source.cp[i] == null) System.exit(0);
        }

        //setting variables = source variables
        this.cp = new Heatcapacity[source.cp.length];
        for (int i = 0; i<source.cp.length;i++){
            this.cp[i] = source.cp[i];
        }
        this.numComponents = source.numComponents;

    }//copy constructor

    public PBR clone() {
        return new PBR(this);
    } // clone method
    @Override //FUNCTION METHOD
    public double[] calculateValue(double x, double[]y) {
        // returns the differential flowrates,pressure,and temperature depending on GAS, GAS_NONISOTHERMAL or LIQUID reaction
        // x = dW
        // [] y = [flowrates, pressure, temperature]
        
        double [] returnArray = new double[y.length]; // stores differential values of F,p and T
        int numComponents = opConditions.length-5; // initialflowrates.length -other parameters

        //setting local variables flowrates, pressure and temperature from []y
        double [] flowrates = new double[numComponents];
        double pressure = y[numComponents+1];
        double temperature = y[numComponents+2];
        for(int i =0; i<numComponents;i++) {
            flowrates[i] = y[i];
        }
        //getting the relativeRatei from Irreversible Class
        double [] relativeRatei = calculateRelativeRates(flowrates);
        // finding differential values
        //Flowrates
        for (int i =0; i < numComponents;i++){
            flowrates[i] = relativeRatei[i]*x;
        }
        pressure = calculateDifferentialPressure(flowrates,pressure,temperature)*x ; //dP = dP/dW*dW
        temperature = calculateDifferentialTemperature(flowrates,temperature)*x; //dT = dT/dW*dW

        switch (Type){
            case LIQUID:
                // returns dF
                for (int i = 0; i<numComponents; i++){
                    returnArray[i] = flowrates[i];
                }
                return returnArray;
            case GAS:
                // returns dF and dP
                for (int i = 0; i<numComponents; i++){
                    returnArray[i] = flowrates[i];
                }
                returnArray[numComponents+1] = pressure;
                return returnArray;
            case GAS_NONISOTHERMAL:
                //returns dF,dP and dT
                for (int i = 0; i<numComponents; i++){
                    returnArray[i] = flowrates[i];
                }
                returnArray[numComponents+1] = pressure;
                returnArray[numComponents+2]= temperature;
                return returnArray;
        }
        //else if there is no ReactionType Type then
        System.out.println("There is no ReactionType for this PBR");
        return new double[0];
    }


    //UNIQUE PBR METHODS
    public double[] calculateRelativeRates(double[] opFlowrates) {
        // returns the array of relative rates
        //opFlowrates are the concentrations
        //Updated so that it takes into consideration the p value (P/P0)
        int numReactions = this.r.length;
        double[][] arrayOfReactions = new double[numReactions][]; // [numReaction][numComponent]
        double[] relativeRates = new double[numComponents]; // relativeRates of each component

        double T0 = opConditions[numComponents+2];
        double v0 = opConditions[numComponents+3];

        //setting array of Reactions
        for (int i =0;i<r.length;i++){
            arrayOfReactions[i]=r[i].CatalyticRelativeRates(T0,v0,opFlowrates,this.particleDensity);
        }

        //summing the relative rates at each component
        for (int i =0; i < this.numComponents;i++){
            for ( int j=1; j<numReactions;j++ ) {
                relativeRates[i] = arrayOfReactions[j][i] + arrayOfReactions[j - 1][i];
            }
        }
        return relativeRates;
    } // calculates relative rate for complex reactions
    public double calculateDifferentialPressure(double[] flowrates, double p, double T) {
        // calculates dP/dW to be used in the numerical methods
        //dp/dW = -alpha/2*(Ft/Ft0)*(T/T0)*P/P0
        // [] flowrates = to find Ft
        // p = pressure P
        // T = temperature T
        // opConditions[] to get P0, T0 and alpha
        double Ft = 0; // total flowrates
        double Ft0 = 0; // total inital flowrates
        double T0 = opConditions[opConditions.length-4];
        double P0 = opConditions[opConditions.length-5];

        double [] opFlowrates = new double[flowrates.length];

        for (int i = 0; i< flowrates.length; i++){
            opFlowrates[i] = opConditions[i];
        }

        //Initializing alpha to test code
      double alpha = opConditions[opConditions.length];


        for (int i =0; i < flowrates.length; i++ ) {
            // for each component
            Ft +=flowrates[i];
            Ft0 +=opConditions[i];
        }

        return  -alpha/2*(Ft/Ft0)*(T/T0)*p/P0;

    }

    public double [] calculateEnthalpy(double T, double []deltaHrTr, int reaction) {
        // returns array of enthalpies of each reaction
        // deltaEnthalpy(T) = deltaEnthalpy(Tr) + deltaCp*(T-Tr)
        // used in the calculateTemperature method
        //cp[i].calculateCp(T) calculates deltaCp*(T-Tr)

        double deltaCp = 0;
        double [] deltaEnthalpyReaction = new double [r.length];

        for (int j = 0; j < this.numComponents; j++) {
            for (int i = 0; i < r.length; i++) {
                deltaCp += r[i].getStoicCoeff()[j] * cp[j].calculateCp(T);// deltaCp = stoichCoeff_i*Cpi (for each reaction)
                deltaEnthalpyReaction[i] = deltaHrTr[i] +deltaCp; //
            }
        }
        return deltaEnthalpyReaction; // *(T-298); ///deltaHrTr= deltaHR(Tr = 298)
    }

    public double calculateDifferentialTemperature(double [] flowrates,double T) {
        //calculatesDifferentialTemperature
        //dT/dW = sum(Rij*deltaHrij)-Ua/particledensity*(T-T0)/sum(Fj*Cpj)
        int numReactions = r.length;
        double v0 = opConditions[opConditions.length - 2];
        double T0 = opConditions[opConditions.length-3];
        double sum1 = 0; //sum of Rij*deltaHrij
        double sum2 =0; // sum of Fj*Cpj

        // Hardcoded for testing
        double Ua = 7.87;  //W/m2*K
        double [] Hr = new double[]{-235138,-371090};
        // ------------------------------------------------ Need to find where these are given to PBR

        for (int j = 0; j < this.numComponents; j++)
            for (int i = 0; i < numReactions; i++) {
                //calculating sum1
                double [] Rij =r[i].CatalyticRelativeRates(T,v0,flowrates,particleDensity);  // makes Rij = array of relativeRates of rxn i
                double []deltaHrij = calculateEnthalpy(T,Hr,i);
                sum1 += Rij[j]*deltaHrij[i];

                //calculating sum2
                // Cpj =  cp[j].calculateCp(T) = array of heat capacities for each component
                sum2+= flowrates[j]*cp[j].calculateCp(T);
            }

       return sum1*Ua/particleDensity*(T-T0)/sum2;

    }
    public double [] calculateOutletParameters(double CatalystWeight) {
        // Calls RK4 iterate method to find final flowrate of desired component i
        // Given the user inputted weight
        // RK4 returns array [] of final differential components
        double [] parameters;
        int numDifferentialComponents = opConditions.length-2;
        // numDifferentialComponents = Flowrates of i components, pressure and temperature
        // 2 - additional parameters in opConditions v0 and alpha
        double x_0 = 0;
        double x_f = CatalystWeight;
        double[] y_0 = new double[numDifferentialComponents];
        for (int i = 0; i < numDifferentialComponents; i++) {
            y_0[i] = opConditions[i];
            //System.out.println("y0"+i+": " + y_0[i]);
        }
        double delx = 0.5;
        int maxIt = 1000;

        parameters = RK4.integrate(x_0,x_f,y_0,delx,maxIt,this);

        return parameters;

    }
    public boolean equals(Object comparator)
    {
        if(!super.equals(comparator)) return false;
        if((this.opConditions==null && ((PBR)comparator).opConditions!=null)||(this.opConditions!=null &&
                ((PBR)comparator).opConditions==null)) return false;
        if((this.r==null && ((PBR)comparator).r!=null) ||
                (this.r!=null && ((PBR)comparator).r==null)) return false;
        if (this.opConditions.length!=((PBR)comparator).opConditions.length) return false;
        if (this.r.length!=((PBR)comparator).r.length) return false;
        for(int i=0; i<this.r.length;i++)
            if(this.r[i]!=((PBR)comparator).r[i]) return false;
        for(int i=0; i<this.opConditions.length;i++)
            if(this.opConditions[i]!=((PBR)comparator).opConditions[i]) return false;
        return true;
    }


}//end of class