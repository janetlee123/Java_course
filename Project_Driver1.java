import java.sql.SQLOutput;
import java.util.Random;

import static java.lang.Math.PI;
import static java.lang.Math.pow;

public class Project_Driver1 {
    private static final double gc = 32.174; // lbm*ft/s2*lbf
    private static final double voidFraction = 0.45;
    private static final double particleDensity = 833*(1-voidFraction); //bulk density of catalyst
    private static final double particleDiameter = 0.005; //m

    public double calculateAlpha(double gasViscosity, double gasDensity, double nominalPipeSize, double []opConditions){
        // called by driver to calculate the alpha value based on user inputs
        //returns the alpha value

        // Variables to calculate beta
        double area = PI*pow(getPipeDiameter(nominalPipeSize)/2,2); // Pi*r^2
        double volumetricFlowRate = opConditions[opConditions.length-2];
        double superficialVelocity = volumetricFlowRate/1000/area;//(m/s) divide by 1000 bc volumetricFlowRate given in L/min
        double G = superficialVelocity*gasDensity; // superficial mass velocity
        double beta;
        double alpha;
        double p0 = opConditions[opConditions.length - 4]*101.325; // converting atm to kPa

        // B = G(1-void)/rho0*gc*Dp*void^3
        beta = (G*(1-voidFraction))/(gasDensity*gc*particleDiameter*pow(voidFraction,3));

        //System.out.println("beta1: "+ beta);
        // B = B*[(150*(1-void)*viscosity)/Dp + 1.75*G]
        beta = beta*(((150*(1-voidFraction)*gasViscosity)/particleDiameter) + 1.75*G); //kPa/m

        //System.out.println("beta: "+beta);
        //alpha = 2*beta/(Ac*rhoc*(1-void)*P0)
        // rhoc*(1-void) = particleDensity
        alpha = (2*beta)/(area*particleDensity*p0);
        return alpha;

    }
    public double getPipeDiameter(double nominalPipeSize){
        //returns the pipe diameter in meters based on nominalPipeSize
        if(nominalPipeSize==0.5)
            return (21.34-1.57)/1000;
        else if(nominalPipeSize ==1) {
            return (33.4-1.52)/ 1000;
        }
        else if (nominalPipeSize == 2) {
            return (60.32-2.31) / 1000;
        }
        else if (nominalPipeSize ==3) {
            return (88.9-2.16)/1000;
        }
        else if (nominalPipeSize==4){
            return (114.30-3.51)/1000;
        }
        else if(nominalPipeSize ==5){
            return (141.30-2.21)/1000;
        }
        else
            System.out.println("PBR:getPipeDiameter: Nominal Pipe Size needs to range between 1/2 to 5.");
        return 0;
    }

    public static void main(String[] args) throws InvalidReactionParameterException {
        // component a = 0, b=1, c=2, d =3, e =4, f = 5, i = 6
        double R = 0.08206;
        double v0 = 20; //L/min
        double P0 = 12.5; //atm
        double density = 833;
        double[] opFlowrate = {10, 10, 10, 0, 0, 0}; // A, H , J, K , E , L
        double T0 = 300; // K


        //Setting Operating Conditions
        double[] opConditions = new double[opFlowrate.length + 5];
        for (int i = 0; i < opFlowrate.length; i++) {
            opConditions[i] = opFlowrate[i];
        }
        opConditions[opFlowrate.length + 1] = P0;
        opConditions[opFlowrate.length + 2] = T0;
        opConditions[opFlowrate.length + 3] = v0;
        opConditions[opFlowrate.length + 4] = 0.0034; //alpha value


        //Kinetic Parameters
        double AD1 = 9.7;
        double AA2 = 5.13;
        double AA3 = 3.037E-4;
        double AA4 = 1.085e-2;

        double E1 = 15000;
        double E2 = 17000;
        double E3 = 14700;
        double E4 = 34600;

        double kD1 = AD1 * Math.exp(-E1 / (Irreversible.R * T0)); // uses R=8.314 make sure all brackets are present for calc to work
        double kA2 = AA2 * Math.exp(-E2 / (Irreversible.R * T0));
        double kA3 = AA3 * Math.exp(-E3 / (Irreversible.R * T0));
        double kA4 = AA4 * Math.exp(-E4 / (Irreversible.R * T0));

        //Irreversible Reaction Instance Variables
        //Case 2:
        //RXN1 : A + H + J > K + E  deltaHrx(298) = -235138 J/mol
        //RXN2 : A + H > L          deltaHrx(298) = -371 090 J/mol
        double[] stoichR1 = {-1,-1,-1,0,0,0}; // A, H , J , K , E , L
        double[] stoichR2 = {-1,-1,0,0,0,1}; //A, H, J, K, E, L
        double[] rateCoeff1 = {2, 1, 1.44,0,0,0};
        double[] rateCoeff2 = {0.65, 0.57, 0, 0, 0};
        double [] reactantRateCoeff1 = {1,1,1,};
        double [] reactantRateCoeff2 = {1,1};
        double [] productRateCoeff1 = {1,1};
        double [] productRateCoeff2 = {1};

        Irreversible rxn1 = new Irreversible(stoichR1, rateCoeff1,reactantRateCoeff1,productRateCoeff1,kA3,AA2,E3);
        Irreversible rxn2 = new Irreversible(stoichR2,rateCoeff2, reactantRateCoeff2, productRateCoeff2, kA4,AA4,E4);
        Irreversible[] rxns = new Irreversible[]{rxn1, rxn2};

        //Setting heatCapacities
        Heatcapacity cpA = new Heatcapacity(new double[]{32.083, -1.4831, 24.774, -23.766, 68.274});
        Heatcapacity cpH = new Heatcapacity(new double[]{25.206,0.0804,-0.00006,0.00000002,-2E-12});
        Heatcapacity cpJ = new Heatcapacity(new double[]{15.04,0.1039,-0.00003,-0.000000007,3E-12});
        Heatcapacity cpK = new Heatcapacity(new double[]{1.0565,0.2585,-0.0002,0.00000005,-7E-12});
        Heatcapacity cpE = new Heatcapacity(new double[]{33.933,-0.008419,0.000029906,-1.7825E-08,3.6934E-12});
        Heatcapacity cpL = new Heatcapacity(new double[]{24.255,0.1376,0.0003,-0.0000005,2E-10});

        Heatcapacity []cp = new Heatcapacity[]{cpA,cpH,cpJ,cpK,cpE,cpL};

        int numComponents =opFlowrate.length;

        PBR pbr1 = new PBR(opConditions,rxns,cp,ReactionType.GAS_NONISOTHERMAL, numComponents);



    }


}
