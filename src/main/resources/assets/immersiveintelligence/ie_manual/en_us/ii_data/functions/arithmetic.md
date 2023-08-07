# meta
Arithmetic Functions
Doubleplusgood!

# add
|[text]{source:"%SECTION%",bold:1b}|
Performs addition of the parameters.
|[data_variable]{type:"integer", direction:"in", name:"Summand", description:"Part of the sum"}|
|[data_variable]{type:"integer", direction:"in", name:"Summand", description:"Part of the sum"}|
|[hr]|
|[data_variable]{type:"integer", direction:"out", name:"Sum", description:"Sum of all inputs. For example 2+2=4"}|

# subtract
|[text]{source:"%SECTION%",bold:1b}|
Performs subtraction of the second parameter from the first.
|[data_variable]{type:"integer", direction:"in", name:"Minuend", description:"Number being subtracted from"}|
|[data_variable]{type:"integer", direction:"in", name:"Subtrahend", description:"Number being subtracted"}|
|[hr]|
|[data_variable]{type:"integer", direction:"out", name:"Difference", description:"Sum of all inputs. For example: 9-7=2"}|

# multiply
|[text]{source:"%SECTION%",bold:1b}|
Multiplies first and second parameter.
|[data_variable]{type:"integer", direction:"in", name:"Factor", description:"Number multiplied"}|
|[data_variable]{type:"integer", direction:"in", name:"Factor", description:"Number multiplied by"}|
|[hr]|
|[data_variable]{type:"integer", direction:"out", name:"Product", description:"Sum of all inputs. For example: 3*4=12"}|

# divide
|[text]{source:"%SECTION%",bold:1b}|
Performs division of the first parameter by the second.
|[data_variable]{type:"integer", direction:"in", name:"Dividend", description:"The number being divided"}|
|[data_variable]{type:"integer", direction:"in", name:"Divisor", description:"The number dividing"}|
|[hr]|
|[data_variable]{type:"integer", direction:"out", name:"Fraction", description:"Sum of all inputs. For example: 20/4=5"}|

# modulo
|[text]{source:"%SECTION%",bold:1b}|
Performs addition of the parameters.
|[data_variable]{type:"integer", direction:"in", name:"Dividend", description:"The number being divided"}|
|[data_variable]{type:"integer", direction:"in", name:"Modulus", description:"The number dividing"}|
|[hr]|
|[data_variable]{type:"integer", direction:"out", name:"Remainder", description:"Whole remainder of a division of one number by another. For example: 8 mod 3 = 2"}|

# abs
|[text]{source:"%SECTION%",bold:1b}|
Gets the absolute value of the parameter
|[data_variable]{type:"integer", direction:"in", name:"Input"}|
|[hr]|
|[data_variable]{type:"integer", direction:"out", name:"Output mode", description:"Sum of all inputs. For example abs(5)=abs(-5)=5"}|