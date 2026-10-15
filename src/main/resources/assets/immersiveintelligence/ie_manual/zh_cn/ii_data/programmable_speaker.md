# meta
可编程扬声器
工程师的快乐噪音
# intro
|[crafting]{source:"programmable_spkr"}|
可编程扬声器是一种在接收到红石信号时会发出声音的设备。红石线和数据线缆可以从任何一面连接。
数据信号可用于设置声音、音调、音量和重复模式。此外，红石信号也可用于设置音量。
# data
**数据输入：**<br>
|[data_variable]{type:"boolean", direction:"out", letter:"o", name:"播放一次", description:"若为真，无论红石信号如何，只播放一次声音"}|
|[data_variable]{type:"string", direction:"out", letter:"s", name:"声音 ID", description:"示例：immersiveintelligence:siren"}|
|[data_variable]{type:"integer", direction:"out", letter:"v", name:"音量", description:"值必须在 0 到 100 之间"}|
|[data_variable]{type:"integer", direction:"out", letter:"t", name:"音调/音高", description:"值必须在 0 到 200 之间。100 为正常音高。"}|