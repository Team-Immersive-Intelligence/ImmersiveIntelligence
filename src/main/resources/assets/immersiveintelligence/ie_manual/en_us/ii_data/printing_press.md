# meta
Printing Press
The Engineer's Book & Quill

# press0
@hammer;level_circuits;upgradable
|[multiblock]{mb:"II:PrintingPress"}|
The **Printing Press** is a machine which can print out text on demand, received in a data packet.
|[text]{mb:"II:PrintingPress"}|

# press1
|[machine_recipe]{machine:"metal_press", source:"paper_page"}|
The [Printing Paper] is a special thinned version of a standard one used in books. It is created by [pressing a piece of Paper in a Metal Press].
Printed pages should be removed as soon as possible from the printing press, as the press can only store [12 pages] in its output basket.

# press_usage
The printing press is controlled with the [Data System](data_main); required variables are described on the following page.
In addition to [Printing Paper], a printing press requires liquid ink to print text onto a page. The amount of ink used depends on the color of currently printed character.
Ink is created in a [mixer](mixer) by mixing water and the respective dye to create black, cyan, magenta, or yellow ink.
The printing press can also be [upgraded] to extend its functionality.

# data_inputs
**Data Inputs:**
|[data_variable]{type:"string", direction:"in", letter:"m", name:"Output mode", description:"The type of document that will be printed", values:[["text","A page containing text"],["code","A page containing code written in the POL programming language"],["blueprint","A page containing a construction blueprint to be used manually or by a Logistics Drone"]]}|
|[data_variable]{type:"integer", direction:"in", letter:"a", name:"Amount of copies", description:"How many pages will be printed"}|
|[data_variable]{type:"string", direction:"in", letter:"t", name:"Text to print", description:"Text content that will be printed", requirements:{m:"text/code"}}|
# punchtapes_upgrade
|[upgrade_display]{upgrade:"immersiveintelligence:press_punchtapes"}|
The [Punchtape Processor] upgrade allows printing [Punchtapes](punchtapes.md) with all the variables of the received packets, except "a" and "m".  
**Instead of empty page, supply an [Empty Punchtape].**
|[data_variable]{type:"string", direction:"in", letter:"m", name:"Output mode", description:"The type of document that will be printed", values:[["punchtape","A punchtape with variables of the received packet, except this one printed. Doesn't use any ink."]]}|
# batching_upgrade
|[upgrade_display]{upgrade:"immersiveintelligence:press_batching"}|
The [Batching Mechanism] upgrade allows printing documents consisting of multiple pages.
Within variable **t**, the escape code [/newpage] will signify to the printer to begin printing on the next page.
|[data_variable]{type:"string", direction:"in", letter:"m", name:"Output mode", description:"The type of document that will be printed", values:[["batched","Notifies the printing press that this is a multi-page data packet."]]}|
# envelopes_upgrade
|[upgrade_display]{upgrade:"immersiveintelligence:press_enveloper"}|
The [Enveloper] upgrade allows to print envelopes, which can then be used for mailing purposes.
|[data_variable]{type:"string", direction:"in", letter:"m", name:"Output mode", description:"The type of document that will be printed", values:[["envelope","Prints an envelope with the provided information in the below variables."]]}|
|[data_variable]{type:"string", direction:"in", letter:"r", name:"Recipient", description:"The identification of the letter recipient", values:[["Example: Hans No. 613","This variable is required."]]}||
|[data_variable]{type:"string", direction:"in", letter:"s", name:"Sender", description:"The identification of the letter sender", values:[["Example: Hans No. 125","This variable is optional."]]}|

# data_callback
|[text]{text:"Data Callback",bold:1b}|

|[data_callback]{type:"integer", name:"get_ink", label:"Ink Level", returns:"Black ink amount (mB)"}|
|[data_callback]{type:"integer", name:"get_ink_cyan", label:"Cyan Ink Level", returns:"Cyan ink amount (mB)"}|
|[data_callback]{type:"integer", name:"get_ink_yellow", label:"Yellow Ink Level", returns:"Yellow ink amount (mB)"}|
|[data_callback]{type:"integer", name:"get_ink_magenta", label:"Magenta Ink Level", returns:"Magenta ink amount (mB)"}|
|[data_callback]{type:"integer", name:"get_energy", label:"Energy Stored", returns:"Stored energy amount (IF)"}|
|[data_callback]{type:"integer", name:"get_paper", label:"Paper amount", returns:"Blank paper pages amount"}|
