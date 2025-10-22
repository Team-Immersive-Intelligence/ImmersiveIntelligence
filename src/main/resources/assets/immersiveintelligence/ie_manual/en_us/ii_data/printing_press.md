# meta
Printing Press
The Engineer's Book & Quill
# press0
@hammer;level_circuits;upgradable
|[multiblock]{mb:"II:PrintingPress"}|
The *
*Printing
Press
** is a machine which can print out text on demand, after it receives a data packet.
|[text]{mb:"II:PrintingPress"}|
# press1
|[machine_recipe]{machine:"metal_press", source:"paper_page"}|
The [Printing Paper] is a special thinned version of a standard one used in books, it is created by [pressing a piece of Paper in a Metal Press].
The amount of ink used depends on the color of currently printed character.  
Printed pages should be removed immediately, as the press can only store [12 of them] in its basket.

# data_inputs
*
*Data
Inputs:
**  
|[data_variable]{type:"string", direction:"in", letter:"m", name:"Output mode", description:"What type of page that will be printed", values:[["text","A page containing text"],["code","A page containing code written in the POL programming language"],["blueprint","A page containing a construction blueprint to be used manually or by a Logistics Drone"]]}|
|[data_variable]{type:"integer", direction:"in", letter:"a", name:"Amount of copies", description:"How many pages will be printed"}|
|[data_variable]{type:"string", direction:"in", letter:"t", name:"Text to print", description:"Text content that will be printed", requirements:{m:"text/code"} }|
# punchtapes_upgrade
|[upgrade_display]{upgrade:"immersiveintelligence:press_punchtapes"}|
The [Punchtape Processor] upgrade allows printing [Punchtapes](punchtapes.md) with all the variables of the received packets, but "a" and "m".  
*
*Instead
of
empty
page,
supply
an [Empty Punchtape].
**  
|[data_variable]{type:"string", direction:"in", letter:"m", name:"Output mode", description:"What type of page that will be printed", values:[["punchtape","A punchtape with variables of the received packet, except this one printed. Doesn't use any ink."]]}|
# batching_upgrade
|[upgrade_display]{upgrade:"immersiveintelligence:press_batching"}|
The [Batching Mechanism] upgrade allows printing documents consisting of multiple pages.  
|[data_variable]{type:"string", direction:"in", letter:"m", name:"Output mode", description:"What type of page that will be printed", values:[["punchtape","A punchtape with variables of the received packet, except this one printed. Doesn't use any ink."]]}|
# envelopes_upgrade
|[upgrade_display]{upgrade:"immersiveintelligence:press_enveloper"}|
The [Enveloper] upgrade allows to print envelopes, which can then be used for mailing purposes.  
|[data_variable]{type:"string", direction:"in", letter:"m", name:"Output mode", description:"What type of page that will be printed", values:[["punchtape","A punchtape with variables of the received packet, except this one printed. Doesn't use any ink."]]}|
# data_callback
|[text]{text:"Data Callback",bold:1b}|

|[data_callback]{type:"integer", name:"get_ink", label:"Ink Level", returns:"Black ink amount (mB)"}|
|[data_callback]{type:"integer", name:"get_ink_cyan", label:"Cyan Ink Level", returns:"Cyan ink amount (mB)"}|
|[data_callback]{type:"integer", name:"get_ink_yellow", label:"Yellow Ink Level", returns:"Yellow ink amount (mB)"}|
|[data_callback]{type:"integer", name:"get_ink_magenta", label:"Magenta Ink Level", returns:"Magenta ink amount (mB)"}|
|[data_callback]{type:"integer", name:"get_energy", label:"Energy Stored", returns:"Stored energy amount (IF)"}|
|[data_callback]{type:"integer", name:"get_paper", label:"Paper amount", returns:"Blank paper pages amount"}|
