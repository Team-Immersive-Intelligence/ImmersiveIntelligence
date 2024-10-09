# meta
Chemical Painter
Worth a thousand brushes

# intro
@hammer;level_advanced_industrial
|[multiblock]{mb:"II:ChemicalPainter"}|
The **Chemical Painter** is a machine used to paint items in a precisely set color using ink.
|[text]{mb:"II:ChemicalPainter"}|

# details
Painting items manually is tedious and not very precise, a lot of dye can be wasted on just trying to get the color of choice. 
[Chemical Painter] eliminates this problem. 

Cyan, Magenta, Yellow and Black Ink have to be provided to four fluid input slots on the back of the device. 
Electricity has to be provided to the top input port. Items can be inserted manually or through the inward facing conveyor.

# interface
To set the color use the interface or data system. In the interface there are 3 buttons, which allow switching the color model:  
[R] - [RGB]  
[C] - [CMYK]  
[H] - [HSV]  

The chemical painter can be used to paint markings on bullets to make them easier to identify visually. 
Most of these items can be later cleaned using the [Chemical Bath](chemical_bath) to regain their original color.

# data_output
Output Variables
<br>  
|[data_variable]{type:"integer", direction:"in", letter:"p", name:"Paint Color", description:"A number representing the RGB paint color. In its hexadecimal form its digits should be in RRGGBB form, where R is red, G is green and B is blue"}|
<br>  
|[data_variable]{type:"string", direction:"in", letter:"p", name:"Paint Color", description:"A string hex code for the color without any additional leading or trailing characters"}|

# data_callback
Callback Variables
|[data_callback]{type:"integer", name:"get_energy", label:"Energy Stored", returns:"Stored energy amount (IF)"}|
|[data_callback]{type:"integer", name:"get_progress", label:"Production Progress", returns:"Progress value 0:1"}|
|[data_callback]{type:"integer", name:"get_color", label:"Color", returns:"Currently used color as a RGB integer"}|
|[data_callback]{type:"integer", name:"get_ink", label:"Ink Level", returns:"Black ink amount (mB)"}|
|[data_callback]{type:"integer", name:"get_ink_cyan", label:"Cyan Ink Level", returns:"Cyan ink amount (mB)"}|
|[data_callback]{type:"integer", name:"get_ink_yellow", label:"Yellow Ink Level", returns:"Yellow ink amount (mB)"}|
|[data_callback]{type:"integer", name:"get_ink_magenta", label:"Magenta Ink Level", returns:"Magenta ink amount (mB)"}|
