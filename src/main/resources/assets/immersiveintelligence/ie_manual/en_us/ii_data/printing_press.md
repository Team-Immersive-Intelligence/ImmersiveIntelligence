# meta
Printing Press
The Engineer's Book & Quill

# 0
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

# data_output

#Printing Press output variables
ie.manual.entry.printing_press.vars_in.m.main=Output mode
ie.manual.entry.printing_press.vars_in.m.sub=Possible values: "text", "code", "blueprint"
ie.manual.entry.printing_press.vars_in.a.main=Amount of copies
ie.manual.entry.printing_press.vars_in.a.sub=
ie.manual.entry.printing_press.vars_in.t.main=Text to print
ie.manual.entry.printing_press.vars_in.t.sub=Available only when using the "text" mode

# data_callback
ie.manual.entry.printing_press.vars_callback.get_ink=Returns black ink amount
ie.manual.entry.printing_press.vars_callback.get_ink_cyan=Returns cyan ink amount
ie.manual.entry.printing_press.vars_callback.get_ink_yellow=Returns yellow ink amount
ie.manual.entry.printing_press.vars_callback.get_ink_magenta=Returns magenta ink amount
ie.manual.entry.printing_press.vars_callback.get_energy=Returns stored energy amount
ie.manual.entry.printing_press.vars_callback.get_paper=Returns blank paper pages amount
