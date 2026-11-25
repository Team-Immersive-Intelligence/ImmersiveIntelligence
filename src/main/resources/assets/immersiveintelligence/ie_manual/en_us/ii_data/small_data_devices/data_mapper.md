# meta
Data Mapper
Absolutely Legit Conversion Rates
# mapper
|[crafting]{source:"device"}|
The [Data Mapper] replaces selected variables using exact value mappings. It can be configured through its interface.
Each mapping entry shows its input type, input letter, output letter and output type. Use the edit button to configure it.
# rules
Choose the expected input type and letter, then the output type and letter. Add value pairs to the list. Each pair has an expected input value and its replacement output value.
Open the pair editor and select [Input value] or [Output value] to edit each side. Apply the pair, then apply the rule. Cancel leaves the previous configuration intact.
# example
To turn an integer [a] into a string [s], select Integer and A as input, and String and S as output.
Add pairs 1 -> one, 2 -> two and 5 -> five.
A matching packet loses A and receives S with the mapped word. Values without a matching pair pass through unchanged.
# ordering
Rules run in list order, but every rule reads the original packet. A replacement cannot become the input for another rule in the same device.
The first exact value pair in a rule wins. Later rules can replace earlier output values. Packet color, address and unrelated variables are preserved.
