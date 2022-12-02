#!/bin/bash

chmod +x given.sh
echo -n "" > out.txt
echo -n "" > err.txt

while true
do
    ./given.sh >> out.txt 2>> err.txt
    err_line=$(cat err.txt | wc --lines)
    # echo $err_line
    if (( ! err_line < 1 )); then
        cat out.txt | wc --lines
        break
    fi
done