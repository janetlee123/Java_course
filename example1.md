The PBR reactor performs irreversible gas isothermal and non-isothermal
complex reactions. The catalyst weight is dependent on the flowrates of
each component, pressure, and temperature. These ordinary differential
equations are expressed in the following equations:

$$\frac{{dF}_{i}}{dW} = - {r'}_{i}$$

$$\frac{dp}{dW} = \frac{- \alpha}{2}\frac{Ft}{{Ft}_{0}}\frac{T}{T_{0}}\frac{P_{0}}{P}$$

$\frac{dT}{dW} = \frac{\sum_{i = 1}^{q}{{r^{'}}_{ij}{\mathrm{\Delta}H}_{Rij}(T) - \frac{U_{a}}{\rho_{b}}(T - T_{a})}}{\sum_{}^{}{F_{j}{Cp}_{j}}}$

For the PBR to perform these equations it must utilize methods and
variables found using methods in the RK4, HeatCapacity and Irreversible
classes. The instance variables for the PBR are listed below:

private Irreversible\[\] r;

ReactionType Type;

Heatcapacity\[\] cp;

private int numComponents;

First, an array of irreversible reactions, r, are taken as multiple
reactions can occur simultaneously as complex reactions within the PBR.
Next the reactionType Type is an enum that gives type of reaction the
PBR is to complete. If the Type was GAS, GAS_NONISOTHERMAL or LIQUID,
different sets of methods would be utilized. The array of Heatcapacity
was used to determine the heat capacity of each species within the
reaction. Lastly the numComponents was added to simplify how the PBR can
differentiate between the different operating conditions stored in the
double array OpConditions found in the super. The operatingConditions
array contains the operating flowrates of all the components, initial
pressure, initial temperature, volumetric flow rate, alpha, area of the
cross section of the pipe, and the overall heat transfer coefficient.
The operatingConditions are passed to the PBR as they contain a lot of
the components used in many different methods within the PBR class. This
array can contain more or less variables depending on which reactor it
is being fed to. However, the order in which the variables are entered
need to stay constant as the order of the variables are used to
differentiate the variables within PBR methods.

Following the golden rules, the constructor, copy constructor, clone
method, accessor, mutator and equals methods were completed using these
instance variables.

Next to implement the RK4 method, PBR would have to pass itself as the
function thereby it must contain the method public double\[\]
calculateValue(double x, double\[\] y). Depending on the Type of
ReactionType, different sets of differential methods were called at each
iteration of the RK4 method. For case LIQUID:the only differential
equation used is the differential equation for molar flowrates. For,
case GAS: the method would call for the differential flowrates and
differential pressure. Lastly for the case GAS_NONISOTHERMAL, the method
would call for all three differential equations. At each iteration of
the RK4 method, it would call for the calculateValue method which passes
through the differential values dF/dW, dP/Dw and dT/dW. Each
differential value had its own method. To differentiate between the
values passed into the method at each iteration and the values to be
returned, the values being passed to the methods are set to be double
\[\] flowrates, double pressure, double temperature. These values are
initialized and deep copied. The differential variables are stored in
the values: double\[\] Fi_1, double P_1, double T_1 . The array of all
the differential values to be returned to the RK4 method are stored in
the local variable double\[\] returnArray is populated based on each
case. For the flowrates, another local variable double \[\]
relativeRatei is used to store all the differential values of the
flowrates by calling the calculateRelativeRates method. The P_1 value is
calculated using the calculateDifferentialPressure method and the T_1
value is calculated using the calculateDifferentialTemperature method.
Each of the differential methods (calculateRelativeRates,
calculateDifferentialPressure,calculateDifferentialTemperature), use
flowrates, pressure and temperature.

The unique PBR methods are the methods that calculate for differential
values or outlet parameters.

First, the double\[\] calculateRelativeRates(double\[\] flowrates,
double pressure, double temperature) method takes in an array of
flowrates, pressure, and temperature at each iteration to determine the
relative rates of each component. If a single reaction is occurring, the
local variable \[\] relativeRates would be calculated using the
Irreversible reactions CatalyticRelativeRates(pressure, temperature,
flowrates) method and returned. As complex reactions can occur, the sum
of the relative rate for each component is computed in this method. To
complete this computation, each array of relative rates for each
reaction is calculated using the Irreversible reactions
CatalyticRelativeRates method, and stored in a two-dimensional array
double\[\]\[\] arrayOfReactions. The first dimension gives the reaction
number and the component. Next, to sum up the relative rates at each
reaction, the relative rate of a component was added to the relative
rate of the reaction number -1. These sums were stored in the array
double\[\] relativeRates, which were returned. The nested loop allowed
for the relative rates of each component at each reaction to be looped
through. As well, the number of reactions (numReactions) loop is the
nested loop as the number of components (numComponents) loop as there
are more components than reactions and errors occurred when completing
this computation, the reverse order. This should not have been an error
as nested loops are looped until completion first before exiting the
nested loop. Nevertheless, the computation had worked, and this nested
loop order was maintained. The relativeRates array was returned. The
methods for a single reaction and multiple reactions were separated as a
single reaction would have an error as the nested for loop began at 1.
The nested for loop began at 1 to accommodate for the calculation where
the arrayOfReactions\[j - 1\]\[i\]was summed with the
arrayOfReactions\[j\]\[i\] .

The double calculateDifferentialPressure(double\[\] flowrates, double p,
double T) method used to calculate the differential pressure depending
on the pressure, temperature, and flowrates. The alpha, initial
flowrates, initial pressure and initial temperature are obtained from
the super.getOperatingConditions method. To differentiate the different
variables, the integer numComponents is used to define the index of the
component. The sum of the operating flowrates and the flowrates that are
fed into the method are stored in the Ft0 and Ft variables. Then the
answer to the differential pressure calculation is returned.

The double calculateDifferentialTemperature(
flowrates,pressures,temperature) returns a double for the differential
temperature. First the equation is split up into qG, qR and sum local
variables. This is to ensure that the calculations are done correctly.
The qG calculates the sum of the product between a components relative
rate and rate of enthalpy of the reaction. To do this, the for loop is
used to loop through every reaction. At each iteration, the array of
stoichiometric coefficients is stored in the double array stoicCoeff,
the enthalpy of reaction is stored in Hr and the relative rates is
stored in a double array relativeRatesi. The enthalpy of reaction is
calculated using the following equation.

$$\mathrm{\Delta}H_{Rij} = \mathrm{\Delta}{H{^\circ}}_{Rij}\left( T_{R} \right) + \ {\Delta Cp}_{ij}(T - T_{R})$$

The heat capacity method calculateCp(temperature) calculates for
the$\Delta\ $Cp values for each ith reaction, and j component. The
nested loop allows for the Cp and the ΔHr to be calculated for each
component.The local variable deltaCp is the sum of all the
$\Delta\ $Cpij values. Next, the ΔHrij is calculated for each component.
The Tr is taken from the public static final variable Heatcapacity. By
making the Tr value a public static variable, it can be used by any
method and in this instance the calculateDifferentialTemperature method
and makes it readily accessible. As well it is a final value as the Tr
is a constant from the ΔH values for the given equations. The nested
loop is then closed and the qG is calculated by summing up all the ΔHrij
values multiplied by the relativeRate\[0\] by the deltaHrij. The
relativeRate\[0\] was chosen as the component with the 0 index is A and
component A is often assumed to be the limiting reagent in reactions
which the rate laws are calculated based on. The deltaCp and Hrij are
re-initiated to be 0 so that at the next reaction, these values would
not be the sum of both reactions.

Next the sum is the denominator in the differential temperature
equation. To calculate this, a for loop that loops through all the
components is made to sum up the product of the flowrates and the
respective Cp values.

Then qR is calculated based on the overall heat transfer coefficient U,
partical density, temperature, and Tr. The U and area values are stored
in the operatingConditions and set. Then the particleDensity is stored
in the Project_Driver1 file as a public static final value. This once
again makes the particleDensity readily available to the PBR code to be
used in this method.

Then the qG,qR and sum are used to return the differential temperature.

Next methods to be called by the Driver were created. The double
double\[\] calculateOutletParameters(double CatalystWeight)method calls
on the RK4 method to determine the molar flowrate, pressure and
temperature at the inputted catalyst weight. This method makes it easier
for the user to obtain all these variables without having to call the
individual differential equations method and calculateValue method.
First a local array variable \[\] parameters I initialized as this would
contain all the final parameter values. The number of differential
components is the number of values that will iterate throughout the RK4
method. By separating out only the differential components to be
iterating through the RK4 method, the volumetric flowrate, alpha,
overall heat transfer coefficient and cross-sectional area of the pipe
(non-iterating methods) will not have to be passed to the RK4 method.
The y_0 double would contain all the iterating values. The x is
initialized to 0 as the initial catalyst weight is 0 at the first
iteration. Next the x_f is the endpoint at which the RK4 iterates and is
set to be the catalyst weight. Lastly the step size delx and maximum
number of iterations maxIt are set to be 0.5 and 1000 to match the excel
preliminary validation values. As the RK4 method is used, the RK4
exception is used and a try-catch is used to ensure the RK4 method works
properly.

The next method is the double calculateSelectivity(double Fdesired,
double Fundesired) method which takes the molar flowrates of the desired
and undesired components. It checks to make sure that these molar
flowrates are positive and returns the ratio between the two components.

Thedouble calculateVolume(double weight) method, is called by the driver
file to determine the volume of the reactor in Litres based on the
weight of the catalyst. This calculation is done using the equation:

$$V = \frac{W}{\rho_{b}}*1000$$

It returns the result from this equation after checking to make sure
that the enter weight is positive.

The double calculateReactorLength(double volume)method is used to
determine the reactor length in meters based on the volume in litres.
Once again, it checks to make sure the volume entered is positive and
returns the value based on the equation below:

$$L = \frac{V}{1000*Ac}$$

Where the length is L, the V is the reactor volume in litres and the Ac
is the area of the cross section of the reactor.

The int numPBRReactors(double desiredFlowrate, int desiredComponentNum,
double catalystWeight) method is used to determine the number of PBR
reactors in series are required to provide a desired flowrate given the
catalyst weight. It throws an RK4Exception as it calls for the
calculateOutletParameters method that uses the RK4 method. This is
calculated by dividing the desired flowrate of the given component by
that component's flowrate in a single PBR at a given catalyst weight. To
obtain the flowrates of all the components, an array was used to store
the flowrates. Then the desiredComponentNum is the index of the desired
component. This is used so that the flowrate of the desired component is
used for the calculation. The result is rounded to the nearest integer
as you cannot have a partial PBR reactor.

The IO was attempted to be used in the PBR code, however, it could not
be completed or validated. This in turn added the readData (IO method),
calculateAlpha, calculatePipeDiameter and calculatePipeOuterDiameter
methods to the code.

The following methods were validated:

The double calculateAlpha(double gasViscosity, double gasDensity, double
nominalPipeSize, double\[\] opConditions) code is used to allow the user
to easily calculate the alpha given the gas density, gas viscosity,
nominal pipe size and the operating conditions. The alpha is calculated
based on the beta value which was subdivided into the equation within
the bracket and the equation before the bracket. As seen below:

$$\beta_{0} = \frac{G(1 - \phi)}{\rho_{0}g_{c}D_{p}\phi^{3}}\left\lbrack \frac{150(1 - \phi)\mu}{D_{p}} + 1.75G \right\rbrack$$

$$\alpha = \frac{2\beta_{0}}{A_{c}\rho_{c}(1 - \phi)P_{0}}$$

A local variable for is made for each variable used in the equations.
The corresponding equations were used to determine those values as seen
in Appendix.

The double getPipeDiameter(double nominalPipeSize)method is used in the
calculateAlpha method to calculate for the cross sectional area of the
pipe. If statements would return the inner diameter of the pipe given
the nominal pipe sizes.

The double getPipeOuterDiameter(double nominalPipeSize)method is used to
get the outer diameter of the pipe to be used for the cross sectional
area of the reactor for the calculateVolume method.

-things to improve:

\- make the qG calculation more robust = maybe add an instance variable
in the reactions class stating the location of the limiting reagent at
which the rate laws are calculated based on

\-
