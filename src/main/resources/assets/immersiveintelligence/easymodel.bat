@echo off

set var=immersiveintelligence

mkdir models 2>NUL
mkdir blockstates 2>NUL
mkdir models\item 2>NUL
mkdir models\block 2>NUL

rem Materials

mkdir models\item\material 2>NUL

for /r %%i in (.\textures\items\material\*) do (
del models\item\material\%%~ni.json >NUL
echo { "parent": "item/generated", "textures": { "layer0": "%var%:items/material/%%~ni" }}> models\item\material\%%~ni.json
)

rem Others

for /r %%i in (.\textures\items\*) do (
del models\item\%%~ni.json >NUL
echo { "parent": "item/generated", "textures": { "layer0": "%var%:items/%%~ni" }}> models\item\%%~ni.json
)

rem Metal Blocks

del blockstates\ore.json
echo { "forge_marker": 1, "defaults": { "transform": "forge:default-block", "textures": { "all": "%var%:blocks/ore_platinum" }, "model": "cube_all" }, "variants": { "inventory,type=platinum": [{ "textures": { "all": "%var%:blocks/ore_platinum" } }], "type": { "platinum": { "textures": { "all": "%var%:blocks/ore_platinum" } } } } }> blockstates\ore.json

del blockstates\sheetmetal.json
echo { "forge_marker": 1, "defaults": { "transform": "forge:default-block", "textures": { "all": "%var%:blocks/sheetmetal_platinum" }, "model": "cube_all" }, "variants": { "inventory,type=platinum": [{ "textures": { "all": "%var%:blocks/sheetmetal_platinum" } }], "type": { "platinum": { "textures": { "all": "%var%:blocks/sheetmetal_platinum" } } } } }> blockstates\sheetmetal.json

del blockstates\storage.json
echo { "forge_marker": 1, "defaults": { "transform": "forge:default-block", "textures": { "all": "%var%:blocks/storage_platinum" }, "model": "cube_all" }, "variants": { "inventory,type=platinum": [{ "textures": { "all": "%var%:blocks/storage_platinum" } }], "type": { "platinum": { "textures": { "all": "%var%:blocks/storage_platinum" } } } } }> blockstates\storage.json

del blockstates\metal_decoration.json
echo { "forge_marker": 1, "defaults": { "transform": "forge:default-block", "model": "immersiveintelligence:ii_six_sides_overlay_all" }, "variants": { "inventory,type=coil_data": [{ "model": "immersiveengineering:ie_pillar", "textures": { "side": "%var%:blocks/metal_decoration/coil_data_side", "top": "%var%:blocks/metal_decoration/coil_data_top", "bottom": "%var%:blocks/metal_decoration/coil_data_top" } }], "inventory,type=electronic_engineering": [{ "textures": { "block_all": "%var%:blocks/metal_decoration/electronic_engineering", "overlay_all": "%var%:blocks/metal_decoration/steel_overlay" } }], "inventory,type=advanced_electronic_engineering": [{ "textures": { "block_all": "%var%:blocks/metal_decoration/advanced_electronic_engineering", "overlay_all": "%var%:blocks/metal_decoration/aluminium_overlay" } }], "type": { "coil_data": { "model": "%var%:ie_pillar", "textures": { "side": "%var%:blocks/metal_decoration/coil_data_side", "top": "%var%:blocks/metal_decoration/coil_data_top", "bottom": "%var%:blocks/metal_decoration/coil_data_top" } }, "electronic_engineering": { "textures": { "block_all": "%var%:blocks/metal_decoration/electronic_engineering", "overlay_all": "%var%:blocks/metal_decoration/steel_overlay" } }, "advanced_electronic_engineering": { "textures": { "block_all": "%var%:blocks/metal_decoration/advanced_electronic_engineering", "overlay_all": "%var%:blocks/metal_decoration/aluminium_overlay" } } } } } >blockstates\metal_decoration.json

pause