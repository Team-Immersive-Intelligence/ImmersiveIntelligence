# meta
Chemical Painter
Worth a thousand brushes

# 0
|[multiblock]{mb:"II:ChemicalPainter"}|
The [Chemical Painter] is a machine used to paint items in a precisely set color using ink. 
To form it, use a hammer on the middle light engineering block.


# 1
Painting items manually is tedious and not very precise, a lot of dye can be wasted on just trying to get the wanted color. 
Chemical Painter eliminates this problem. 

Cyan, Magenta, Yellow and Black Ink have to be provided to four fluid input slots on the back of the device. 
Electricity has to be provided trough the top input port. Items can be inserted manually or trough the inward facing conveyor.

# 2
To set the color use the interface or data system. In the interface there are 3 buttons, which allow switching the color model: 
R- RGB, C -CMYK and H - HSV.

The chemical painter can be used to paint bullets to mark their purpose. 
Remember that most items can be cleaned using [Chemical Bath](link)

# data_output
ie.manual.entry.chemical_painter.vars_in.p.main=Output mode

# data_callback
#Chemical Painter callback variables
ie.manual.entry.chemical_painter.vars_callback.get_color=Returns currently used color as a RGB integer
ie.manual.entry.chemical_painter.vars_callback.get_color_hex=Returns currently used color as a hex string
ie.manual.entry.chemical_painter.vars_callback.get_ink=Returns black ink amount
ie.manual.entry.chemical_painter.vars_callback.get_ink_cyan=Returns cyan ink amount
ie.manual.entry.chemical_painter.vars_callback.get_ink_yellow=Returns yellow ink amount
ie.manual.entry.chemical_painter.vars_callback.get_ink_magenta=Returns magenta ink amount
ie.manual.entry.chemical_painter.vars_callback.get_energy=Returns stored energy amount
