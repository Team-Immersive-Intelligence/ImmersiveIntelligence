#!/bin/sh

processAudio () {
    bitrate=$(./ffprobe.exe -v 0 -select_streams a:0 -show_entries stream=bit_rate -of compact=p=0:nk=1 "$1");
    if [ "$bitrate" -le "120000" ]
    then
        printf "$1 [\033[0;32mPASS\033[0m]\n";
        return;
    fi
    printf "$1 [\033[0;31mFAIL\033[0m]\n";
}

for f in $(find src/main/resources/assets/immersiveintelligence/sounds -name '*.ogg'); do processAudio $f; done