# meta
Arithmetic-Logic Machine
Does Quick Maths

# intro
|[multiblock]{mb:"II:ArithmeticLogicMachine"}|
The **Arithmetic-Logic Machine** is an electronic device, which primary task is to process and modify data.  
|[text]{mb:"II:ArithmeticLogicMachine",unicode:true}|

# 1
When a data packet is received at one of the sides of the machine, it's processed by [functional circuits](functional_circuits) mounted on one of 6 slots in the rack.
The machine passes the packet through all the circuits, attempting to modify its variables' values in alphabetical order using [expressions](data_types#expression).
An [Expression] will **overwrite the variable value** with its result. 
If there is no expression for a variable, it will **pass without being changed**.

# 2
Each circuit provides its own set of expressions, some, like the [Advanced Arithmetic Circuit](functional_circuits#advanced_arithmetic), which has all the expressions of an [Arithmetic Circuit](functional_circuits#arithmetic) along with some new ones.   
The expression can also have a Conditional Variable, an [Accessor](data_types#accessor) of a [Boolean](data_types#boolean) variable, which will not execute the expression in case the value is [TRUE].

# 3
The machine also offers a non-standard datatype, the [Accessor](data_types#accessor), which doesn't hold any value of its own, but can get the 
value most recently assigned to the variable from the received packet. 

The variable in an expression can be toggled to an [Accessor] by clicking the '@' button.