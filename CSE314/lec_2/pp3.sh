#!/bin/bash

# arr=(
#     "abc.txt"
#     "myfile_1.txt"
#     "program_v1.01"
#     "myfile.txt"
# )

# for str in "${arr[@]}"
# do
#     touch $str
# done


for file in *; do
    echo "$file" | grep  -q '[[:digit:]]' && rm -v $file
done
# | grep -q "for" && rm -v $file
