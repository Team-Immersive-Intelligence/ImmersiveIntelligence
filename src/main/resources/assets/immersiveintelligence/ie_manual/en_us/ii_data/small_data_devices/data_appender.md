# meta
Data Appender
A Little More Data
# appender
|[crafting]{source:"device"}|
The [Data Appender] adds or removes variables from passing packets. Input is at the blue port; output is at the orange port.
The main list has edit, duplicate and remove buttons for each entry. Add an entry to open its editor.
# edit
Select the variable letter. Enable [Remove variable] to delete that variable from passing packets.
In addition mode, select a data type and edit its value. [Overwrite existing] allows replacement of a variable already present in the packet. With overwrite disabled, only missing variables are added.
Apply saves the entry; Cancel keeps the previous entry.
# ordering
Entries run from top to bottom. A removal can therefore be followed by an addition using the same letter.
Duplicate creates an independent copy of an entry. Packet color, address and unrelated variables are preserved. The incoming source packet is not modified.
