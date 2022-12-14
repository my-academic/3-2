#!/bin/bash


current=$(pwd)
# mkdir output
cd input

# for i in *.txt_out; do
#     rm "$current/input/$i" 
# done

for i in *.txt; do
    cd ..
    cat "$current/input/$i" > input.txt
    echo $i
    java Main > "$current/my_output/out_$i"
    cd input
done