# meta
Printing Press
The Engineer's Book & Quill

# 0 
@hammer;level_circuits;upgradable
|[multiblock]{mb:"II:PrintingPress"}|
The Printing Press is a machine which can print out text on demand when it receives a data packet. 
The Press requires ink and paper to operate. 
To form it, use a hammer on the light engineering block in front of the machine.

# 1
The printing paper is a special thinned version of a standard one used in books, it is created by pressing a paper in a metal press. 
Machine requires different ink colors, depending on the CMYK values of the currently printed color.

# 2
The amount of inks depends on the color of currently printed character.
Outputted printed pages should be removed immediately, as the press can only store 12 of them in its basket.

# data_inputs
|[text]{text:"%SECTION%",bold:1b}|
|[data_variable]{type:"string", direction:"in", letter:"m", name:"Output mode", description:"What type of page that will be printed", values:[["text","A page containing text"],["code","A page containing code written in the POL programming language"],["blueprint","A page containing a construction blueprint to be used manually or by a Logistics Drone"]]}|
|[data_variable]{type:"integer", direction:"in", letter:"a", name:"Amount of copies", description:"How many pages will be printed"}|
|[data_variable]{type:"string", direction:"in", letter:"t", name:"Text to print", description:"Text content that will be printed", requirements:{m:"text/code"} }|

# data_callback
|[text]{text:"%SECTION%",bold:1b}|

|[data_callback]{type:"integer", name:"get_ink", label:"Ink Level", returns:"Black ink amount (mB)"}|
|[data_callback]{type:"integer", name:"get_ink_cyan", label:"Cyan Ink Level", returns:"Cyan ink amount (mB)"}|
|[data_callback]{type:"integer", name:"get_ink_yellow", label:"Yellow Ink Level", returns:"Yellow ink amount (mB)"}|
|[data_callback]{type:"integer", name:"get_ink_magenta", label:"Magenta Ink Level", returns:"Magenta ink amount (mB)"}|
|[data_callback]{type:"integer", name:"get_energy", label:"Energy Stored", returns:"Stored energy amount (IF)"}|
|[data_callback]{type:"integer", name:"get_paper", label:"Paper amount", returns:"Blank paper pages amount"}|
