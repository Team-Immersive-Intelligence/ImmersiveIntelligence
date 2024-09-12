# meta
Programmable Speaker
Happy Engineer Noises

# intro
The Programmable Speaker is a device, which emits a sound when provided a redstone signal. Redstone and data wire can be attached from any side. Data can be used to set the sound and its pitch, while redstone sets the volume.

# data
|[data_variable]{type:"integer", direction:"out", letter:"o", name:"Play Once", description:"If true, sound is played once, regardless of redstone signal"}|
|[data_variable]{type:"integer", direction:"out", letter:"s", name:"Sound ID", description:"Example: immersiveintelligence:siren"}|
|[data_variable]{type:"integer", direction:"out", letter:"v", name:"Volume", description:"Value must be between 0 and 100"}|
|[data_variable]{type:"integer", direction:"out", letter:"t", name:"Tone/Pitch", description:"Value must be between -100 and 100"}|


